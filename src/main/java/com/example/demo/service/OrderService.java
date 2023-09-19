package com.example.demo.service;

import com.example.demo.mapper.CarMapper;
import com.example.demo.model.code.OrderStatusCode;
import com.example.demo.model.entity.Car;
import com.example.demo.model.entity.Order;
import com.example.demo.mapper.OrderMapper;
import com.example.demo.model.request.NewOrder;
import com.example.demo.model.result.Result;
import com.example.demo.model.result.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.sql.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

@Slf4j
@Service
public class OrderService {


    static Queue<Order> waitAssignOrderQueue = new LinkedList<>();

    @Resource
    OrderMapper orderMapper;

    @Resource
    CarMapper carMapper;

    @Resource
    CarService carService;

    public Order newOrder(NewOrder newOrder) {
        log.info("newOrder");
        log.info(newOrder.toString());
        Order order = newOrder.toOrder();
        Integer num = orderMapper.getNum();
        String id = "o" + String.format("%03d", num + 1);
        order.setId(id);

        //订单状态为待分配
        order.setStatus(OrderStatusCode.WAIT_TO_ASSIGN.getStatus());

        order.setCreatedAt(new Date(System.currentTimeMillis()));

        orderMapper.newOrder(order);
        if(assignCar(order) == false) {
            //分配失败，加入队列
            waitAssignOrderQueue.add(order);
        }
        return order;
    }

    public Result getOrderList(String userId) {
        return Result.success(orderMapper.getOrderList(userId));
    }

    /**
     * 分配车辆
     */
    public boolean assignCar(Order order) {
        log.info("assignCar " + order.getId());
        if (order.getStatus() != OrderStatusCode.WAIT_TO_ASSIGN.getStatus()) {
            //订单状态不是待分配，直接返回true
            return true;
        }
        //获取空闲的车辆,如果没有则返回false
        Car car = carMapper.getFreeCar();
        if (car == null) {
            return false;
        }
        //订单状态变为待到达起点
        order.setStatus(OrderStatusCode.WAIT_TO_START.getStatus());
        order.setCarId(car.getId());
        orderMapper.updateOrder(order);

        return true;
    }

    @PostConstruct
    public void init() {
        log.info("init");
        //初始化时将所有待分配的订单加入队列
        List<Order> waitAssignOrderList = orderMapper.getWaitAssignOrderList();
        waitAssignOrderQueue.addAll(waitAssignOrderList);
        checkNewOrder();
    }
    /**
     * 每5s检测一次是否有新订单可以被分配
     */
    @Scheduled(cron = "0/5 * * * * ?")
    public void checkNewOrder() {
        log.info("checkNewOrder");
        while (waitAssignOrderQueue.size() > 0) {
            Order order = waitAssignOrderQueue.poll();
            if (assignCar(order) == false) {
                //分配失败，重新加入队列
                waitAssignOrderQueue.add(order);
                break;
            }
        }
    }

    /**
     * 乘客确认上车，订单状态变为待到达终点
     * @param id
     * @return
     */
    public Result confirmUp(String id) {
        Order order = orderMapper.getOrderById(id);
        if (order.getStatus() != OrderStatusCode.WAIT_TO_UP.getStatus()) {
            return Result.error(ResultCode.ORDER_STATUS_ERROR);
        }
        order.setStatus(OrderStatusCode.WAIT_TO_END.getStatus());
        orderMapper.updateOrder(order);
        carService.setStart(order.getCarId());
        return Result.success(order);
    }

    /**
     * 乘客确认到达终点，订单状态变为完成
     * @param id
     * @return
     */
    public Result confirmDown(String id) {
        Order order = orderMapper.getOrderById(id);
        if (order.getStatus() != OrderStatusCode.WAIT_TO_DOWN.getStatus()) {
            return Result.error(ResultCode.ORDER_STATUS_ERROR);
        }
        order.setStatus(OrderStatusCode.COMPLETED.getStatus());
        orderMapper.updateOrder(order);
        carService.setStart(order.getCarId());
        return Result.success(order);
    }


    public Result cancelOrder(String orderId) {
        Order order = orderMapper.getOrderById(orderId);
        //只有在订单状态为待分配和待到达起点时才可以取消订单
        if (order.getStatus() != OrderStatusCode.WAIT_TO_ASSIGN.getStatus() &&
                order.getStatus() != OrderStatusCode.WAIT_TO_START.getStatus()) {
            return Result.error(ResultCode.ORDER_STATUS_ERROR);
        }
        order.setStatus(OrderStatusCode.CANCEL.getStatus());
        orderMapper.updateOrder(order);
        return Result.success(order);
    }

    public Result getOrder(String orderId) {
        return Result.success(orderMapper.getOrderById(orderId));
    }
}
