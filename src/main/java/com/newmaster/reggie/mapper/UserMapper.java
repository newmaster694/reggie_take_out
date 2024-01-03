package com.newmaster.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.newmaster.reggie.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
