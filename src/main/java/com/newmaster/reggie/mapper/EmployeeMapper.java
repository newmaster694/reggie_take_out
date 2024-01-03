package com.newmaster.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.newmaster.reggie.entity.Employee;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface EmployeeMapper extends BaseMapper<Employee>{
}
