package com.example.demo.model.entity;

import lombok.Data;

import java.sql.Date;
@Data
public class Order {

    String id;
    String userId;
    String carId;
    Integer status;
    Date createdAt;
    Double startLon;
    Double startLat;
    Double endLon;
    Double endLat;


}
