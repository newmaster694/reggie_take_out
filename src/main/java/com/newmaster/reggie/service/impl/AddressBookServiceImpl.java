package com.newmaster.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.newmaster.reggie.mapper.AddressBookMapper;
import com.newmaster.reggie.pojo.AddressBook;
import com.newmaster.reggie.service.AddressBookService;
import org.springframework.stereotype.Service;

@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook> implements AddressBookService {
}
