package com.newmaster.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newmaster.reggie.common.R;
import com.newmaster.reggie.dto.DishDto;
import com.newmaster.reggie.pojo.Category;
import com.newmaster.reggie.pojo.Dish;
import com.newmaster.reggie.pojo.DishFlavor;
import com.newmaster.reggie.service.CategoryService;
import com.newmaster.reggie.service.DishFlavorService;
import com.newmaster.reggie.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/dish")
@Slf4j
public class DishController {
    @Autowired
    private DishService dishService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private DishFlavorService dishFlavorService;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 菜品分页查询
     *
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(Integer page, Integer pageSize, String name) {
        log.info("page:{};pageSize:{};name:{}", page, pageSize, name);
        // 分页构造器
        Page<Dish> pageInfo = new Page<>(page, pageSize);
        Page<DishDto> dishDtoPage = new Page<>(page, pageSize);

        // 条件构造器
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();

        // 添加过滤条件
        queryWrapper.like(StringUtils.isNotEmpty(name), Dish::getName, name);

        // 添加排序条件
        queryWrapper.orderByAsc(Dish::getSort);

        // 执行查询
        dishService.page(pageInfo, queryWrapper);

        // 对象拷贝
        BeanUtils.copyProperties(pageInfo, dishDtoPage, "records");

        List<Dish> records = pageInfo.getRecords();

        List<DishDto> list = records.stream().map((item) -> {
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item, dishDto);
            Long categoryId = item.getCategoryId();// 分类id
            // 根据id查询分类对象
            Category category = categoryService.getById(categoryId);
            if (category != null) {
                String categoryName = category.getName();
                dishDto.setCategoryName(categoryName);
            }
            return dishDto;
        }).collect(Collectors.toList());

        dishDtoPage.setRecords(list);

        return R.success(dishDtoPage);
    }

    /**
     * 新增菜品
     *
     * @param dishDto
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody DishDto dishDto) {
        log.info(dishDto.toString());
        dishService.saveWithFlavor(dishDto);

        //清理所有的Redis缓存数据
        Set keys = redisTemplate.keys("dish_*");
        redisTemplate.delete(keys);

        return R.success("新增菜品成功");
    }

    /**
     * 根据id查询菜品信息
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<DishDto> get(@PathVariable Long id) {
        DishDto dishDto = dishService.getByIdWithFlavor(id);
        return R.success(dishDto);
    }

    /**
     * 修改菜品
     * @param dishDto
     * @return
     */
    @PutMapping
    public R<String> update(@RequestBody DishDto dishDto) {
        log.info(dishDto.toString());
        dishService.updateWithFlavor(dishDto);

        //清理所有的Redis缓存数据
        Set keys = redisTemplate.keys("dish_*");
        redisTemplate.delete(keys);

        return R.success("修改菜品成功");
    }

    // /**
    //  * 根据条件查询对应的菜品数据
    //  * @param dish
    //  * @return
    //  */
    // @GetMapping("/list")
    // public R<List<Dish>> list(Dish dish) {
    //     LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
    //     queryWrapper.eq(dish.getCategoryId() != null, Dish::getCategoryId, dish.getCategoryId());
    //     //查询状态为1(起售)的菜品
    //     queryWrapper.eq(Dish::getStatus, 1);
    //     queryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);
    //     List<Dish> list = dishService.list(queryWrapper);
    //     return R.success(list);
    // }

    /**
     * 根据条件查询对应的菜品数据
     * @param dish
     * @return
     */
    @GetMapping("/list")
    public R<List<DishDto>> list(Dish dish) {
        List<DishDto> dishDtoList = null;

        String key = "dish_" + dish.getCategoryId() + "_" + dish.getStatus();

        //先从Redis中获取缓存数据
        dishDtoList = (List<DishDto>) redisTemplate.opsForValue().get(key);

        //如果存在,直接返回,无需查询数据库
        if (dishDtoList != null) {
            return R.success(dishDtoList);
        }

        //如果不存在,需要查询数据库,将查询到的菜品缓存到Redis
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(dish.getCategoryId() != null, Dish::getCategoryId, dish.getCategoryId());
        //查询状态为1(起售)的菜品
        queryWrapper.eq(Dish::getStatus, 1);
        queryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);
        List<Dish> list = dishService.list(queryWrapper);

        dishDtoList = list.stream().map((item) -> {
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item, dishDto);
            Long categoryId = item.getCategoryId();// 分类id
            // 根据id查询分类对象
            Category category = categoryService.getById(categoryId);
            if (category != null) {
                String categoryName = category.getName();
                dishDto.setCategoryName(categoryName);
            }
            //当前菜品的id
            Long dishId = item.getId();
            LambdaQueryWrapper<DishFlavor> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(DishFlavor::getDishId, dishId);
            List<DishFlavor> dishFlavorList = dishFlavorService.list(lambdaQueryWrapper);
            dishDto.setFlavors(dishFlavorList);
            return dishDto;
        }).collect(Collectors.toList());

        redisTemplate.opsForValue().set(key, dishDtoList, 60, TimeUnit.MINUTES);

        return R.success(dishDtoList);
    }

    /**
     * 删除菜品
     * @param ids
     * @return
     */
    @DeleteMapping
    public R<String> delete(@RequestParam List<Long> ids) {
        log.info("ids:{}", ids);
        dishService.removeWithFlavor(ids);
        return R.success("删除菜品成功!");
    }

    /**
     * 停售菜品
     * @param ids
     * @return
     */
    @PostMapping("/status/0")
    public R<String> updateStop(@RequestParam List<Long> ids) {
        log.info("停售的菜品ids:{}", ids);
        LambdaUpdateWrapper<Dish> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.in(Dish::getId, ids).set(Dish::getStatus, 0);
        dishService.update(updateWrapper);
        return R.success("菜品修改成功");
    }

    /**
     * 起售菜品
     * @param ids
     * @return
     */
    @PostMapping("/status/1")
    public R<String> updateStart(@RequestParam List<Long> ids) {
        log.info("起售的菜品ids:{}", ids);
        LambdaUpdateWrapper<Dish> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.in(Dish::getId, ids).set(Dish::getStatus, 1);
        dishService.update(updateWrapper);
        return R.success("菜品修改成功");
    }
}
