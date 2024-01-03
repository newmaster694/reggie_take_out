package com.newmaster.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.newmaster.reggie.mapper.UserMapper;
import com.newmaster.reggie.entity.User;
import com.newmaster.reggie.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}
