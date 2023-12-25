package com.newmaster.reggie.dto;

import com.newmaster.reggie.pojo.Setmeal;
import com.newmaster.reggie.pojo.SetmealDish;
import lombok.Data;
import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
