package com.newmaster.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.newmaster.reggie.mapper.DishFlavorMapper;
import com.newmaster.reggie.entity.DishFlavor;
import com.newmaster.reggie.service.DishFlavorService;
import org.springframework.stereotype.Service;

@Service
public class DishFlavorServiceImpl extends ServiceImpl<DishFlavorMapper, DishFlavor> implements DishFlavorService {
}
