package com.example.demo.service;

import com.example.demo.entity.Order;
import com.example.demo.mapper.OrderMapper;
import com.example.demo.result.Result;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.Date;

@Service
public class OrderService {

    @Resource
    OrderMapper orderMapper;

    public Result newOrder(String user_id, String car_id) {
        Order order = new Order();
        Integer num = orderMapper.getNum();
        String id = "o" + String.format("%03d", num + 1);
        order.setUser_id(user_id);
        order.setCar_id(car_id);
        order.setStatus("创建");
        order.setId(id);
        order.setCreated_at(new Date(System.currentTimeMillis()));
        orderMapper.newOrder(order);
        return Result.success(order);
    }

    public Result updateOrder(String id, String status) {
        Order order = orderMapper.getOrderById(id);
        order.setStatus(status);
        orderMapper.updateOrder(order);
        return Result.success(order);
    }

    public Result getOrderList() {
        return Result.success(orderMapper.getOrderList());
    }
}
