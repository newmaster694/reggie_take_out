package com.newmaster.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newmaster.reggie.common.R;
import com.newmaster.reggie.entity.Category;
import com.newmaster.reggie.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
@Slf4j
public class CateGoryController {
    @Autowired
    private CategoryService categoryService;

    /**
     * 新增分类
     * @param category
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody Category category) {
        log.info("新增分类:{}", category);
        categoryService.save(category);
        return R.success("新增分类成功");
    }

    /**
     * 分页查询功能
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(Integer page, Integer pageSize) {
        // 分页构造器
        Page<Category> pageInfo = new Page<>(page, pageSize);

        // 条件构造器,根据sort字段条件排序
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();

        // 添加排序条件
        queryWrapper.orderByAsc(Category::getSort);

        categoryService.page(pageInfo, queryWrapper);

        return R.success(pageInfo);
    }

    /**
     * 根据id删除分类
     * @param id
     * @return
     */
    @DeleteMapping
    public R<String> delete(Long id) {
        log.info("删除分类,id为:{}", id);

        // categoryService.removeById(id);
        categoryService.remove(id);

        return R.success("分类信息删除成功");
    }

    /**
     * 根据ID修改分类信息
     * @param category
     * @return
     */
    @PutMapping
    public R<String> update(@RequestBody Category category) {
        log.info("修改分类信息:{}", category);
        categoryService.updateById(category);
        return R.success("修改成功");
    }

    /**
     * 根据条件来查询分类数据
     * @param category
     * @return
     */
    @GetMapping("/list")
    public R<List<Category>> list(Category category) {
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(category.getType() != null, Category::getType, category.getType());
        queryWrapper.orderByAsc(Category::getSort).orderByDesc(Category::getUpdateTime);
        List<Category> list = categoryService.list(queryWrapper);
        return R.success(list);
    }
}
