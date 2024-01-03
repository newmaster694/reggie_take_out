package com.newmaster.reggie.dto;

import com.newmaster.reggie.entity.Setmeal;
import com.newmaster.reggie.entity.SetmealDish;
import lombok.Data;
import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
