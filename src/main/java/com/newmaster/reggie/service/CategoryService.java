package com.newmaster.reggie.service;

import com.newmaster.reggie.entity.Category;
import com.baomidou.mybatisplus.extension.service.IService;

public interface CategoryService extends IService<Category> {
    void remove(Long id);
}
