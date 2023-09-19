package com.example.demo.model.request;


import com.example.demo.model.code.OrderStatusCode;
import com.example.demo.model.entity.Order;
import lombok.Data;

@Data
public class NewOrder {
    String userId;
    Double startLon;
    Double startLat;
    Double endLon;
    Double endLat;

    public Order toOrder(){
        Order order = new Order();
        order.setUserId(userId);
        order.setStartLon(startLon);
        order.setStartLat(startLat);
        order.setEndLon(endLon);
        order.setEndLat(endLat);
        return order;
    }

}
