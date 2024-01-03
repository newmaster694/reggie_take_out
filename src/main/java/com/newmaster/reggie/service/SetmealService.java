package com.newmaster.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.newmaster.reggie.dto.SetmealDto;
import com.newmaster.reggie.entity.Setmeal;

import java.util.List;

public interface SetmealService extends IService<Setmeal> {
    /**
     * 新增套餐,同时保存套餐和菜品的关联关系
     * @param setmealDto
     */
    void saveWithDish(SetmealDto setmealDto);

    /**
     * 删除套餐,同时删除套餐与菜品的关联数据
     * @param ids
     */
    void removeWithDish(List<Long> ids);

    /**
     * 根据套餐id查询
     * @param id
     * @return
     */
    SetmealDto getByIdWithCatgory(Long id);

    /**
     * 修改套餐业务
     * @param setmealDto
     */
    void updateWithDish(SetmealDto setmealDto);
}
