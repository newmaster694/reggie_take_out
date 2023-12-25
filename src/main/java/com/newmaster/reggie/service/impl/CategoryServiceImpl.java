package com.newmaster.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.newmaster.reggie.common.CustomException;
import com.newmaster.reggie.pojo.Category;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.newmaster.reggie.mapper.CategoryMapper;
import com.newmaster.reggie.pojo.Dish;
import com.newmaster.reggie.pojo.Setmeal;
import com.newmaster.reggie.service.CategoryService;
import com.newmaster.reggie.service.DishService;
import com.newmaster.reggie.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
    @Autowired
    private DishService dishService;

    @Autowired
    private SetmealService setmealService;

    /**
     * 根据id来删除分类,删除之前需进行判断
     *
     * @param id
     */
    @Override
    public void remove(Long id) {
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        // 添加分类条件,根据分类ID进行查询
        setmealLambdaQueryWrapper.eq(Setmeal::getCategoryId, id);
        dishLambdaQueryWrapper.eq(Dish::getCategoryId, id);
        long countDish = dishService.count(dishLambdaQueryWrapper);
        long countSetmeal = setmealService.count(setmealLambdaQueryWrapper);

        // 查询当前分类是否关联了菜品,如果已经关联,抛出一个业务异常
        if (countDish > 0) {
            // 已经关联菜品,抛出一个业务异常
            throw new CustomException("当前分类下关联了菜品,不能删除");
        }

        // 查询当前分类是否关联了套餐,如果已经关联,抛出一个业务异常
        if (countSetmeal > 0) {
            // 已经关联套餐,抛出一个业务异常
            throw new CustomException("当前分类下关联了套餐,不能删除");
        }

        // 正常删除分类
        super.removeById(id);
    }
}
