package com.newmaster.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.newmaster.reggie.mapper.EmployeeMapper;
import com.newmaster.reggie.entity.Employee;
import com.newmaster.reggie.service.EmployeeService;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {
}
