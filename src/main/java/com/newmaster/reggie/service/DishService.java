package com.newmaster.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.newmaster.reggie.dto.DishDto;
import com.newmaster.reggie.pojo.Dish;

import java.util.List;

public interface DishService extends IService<Dish> {

    //新增数据,同时插入菜品对应的口味数据,需要操作两张表,dish,dish_flavor
    void saveWithFlavor(DishDto dishDto);

    //根据菜品id查询菜品信息以及其对应的口味信息
    DishDto getByIdWithFlavor(Long id);

    //更新菜品信息,同时更新口味信息
    void updateWithFlavor(DishDto dishDto);

    //删除菜品信息,连同口味表的信息一起删除
    void removeWithFlavor(List<Long> ids);
}
