package com.newmaster.reggie.dto;

import com.newmaster.reggie.pojo.OrderDetail;
import com.newmaster.reggie.pojo.Orders;
import lombok.Data;

import java.util.List;

@Data
public class OrdersDto extends Orders {

    private String userName;

    private String phone;

    private String address;

    private String consignee;

    private List<OrderDetail> orderDetails;
	
}
