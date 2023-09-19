package com.example.demo.controller;

import com.example.demo.model.request.NewOrder;
import com.example.demo.model.result.Result;
import com.example.demo.model.result.ResultCode;
import com.example.demo.service.OrderService;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

@RestController
@RequestMapping("/order")
public class OrderController {
    @Resource
    OrderService orderService;

    @ApiOperation(value = "新建订单")
    @PostMapping("/new")
    public Result newOrder(@ModelAttribute NewOrder newOrder) {
        try {
            return Result.success(orderService.newOrder(newOrder));
        }catch (Exception e) {
            e.printStackTrace();
            return Result.error(ResultCode.PARAM_IS_INVALID);
        }
    }

    @GetMapping("/getOrder/{id}")
    public Result getOrder(@PathVariable("id") String orderId) {
        return orderService.getOrder(orderId);
    }
    @GetMapping("/getList/{id}")
    public Result getOrderList(@PathVariable("id") String userId) {
        return orderService.getOrderList(userId);
    }

    @PostMapping("/cancel")
    public Result cancelOrder(@RequestParam String orderId) {
        return orderService.cancelOrder(orderId);
    }

    /**
     * 用户点击确认上车调用
     */
    @PostMapping("/confirmUp")
    public Result confirmUp(@RequestParam String orderId) {
        return orderService.confirmUp(orderId);
    }

    /**
     * 用户点击确认下车调用
     */
    @PostMapping("/confirmDown")
    public Result confirmDown(@RequestParam String orderId) {
        return orderService.confirmDown(orderId);
    }

}
