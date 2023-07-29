package com.example.demo.controller;

import com.example.demo.result.Result;
import com.example.demo.result.ResultCode;
import com.example.demo.service.OrderService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.sql.Date;
import java.util.Map;

@RestController
@RequestMapping("/order")
public class OrderController {
    @Resource
    OrderService orderService;

    @PostMapping("/new")
    public Result newOrder(@RequestBody Map<String,Object> params) {
//        String user_id;
//        String car_id;
        String user_id = (String) params.get("user_id");
        String car_id = (String) params.get("car_id");
        if (user_id == null || car_id == null) {
            return Result.error(ResultCode.PARAM_IS_BLANK);
        }
        return orderService.newOrder(user_id,car_id);
    }

    @PostMapping("/update")
    public Result updateOrder(@RequestBody Map<String,Object> params) {
        String id = (String) params.get("id");
        String status = (String) params.get("status");
        if (id == null || status == null) {
            return Result.error(ResultCode.PARAM_IS_BLANK);
        }
        return orderService.updateOrder(id,status);
    }

    @GetMapping("/getList")
    public Result getOrderList() {
        return orderService.getOrderList();
    }
}
