package com.example.demo.service;

import com.example.demo.mapper.OrderMapper;
import com.example.demo.model.code.OrderStatusCode;
import com.example.demo.model.entity.Car;
import com.example.demo.mapper.CarMapper;
import com.example.demo.model.entity.Order;
import com.example.demo.model.result.Result;
import com.example.demo.model.result.ResultCode;
import com.example.demo.util.TencentMapUtil;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CarService {

    @Resource
    CarMapper carMapper;

    @Resource
    OrderMapper orderMapper;

    @Resource
    TencentMapUtil tencentMapUtil;

    //车辆更新时间, 根据这个来判断是否需要更新车辆状态
    static Map<String,Long> carUpdateTime = new HashMap<>();

    //给车辆的指令，用于到达目的地后停止，下车or上车后启动
    static Map<String,Boolean> carCommand = new HashMap<>();

    //存储车辆到起点的路径
    public List<Car> getCarList() {
        List<Car> carList = carMapper.getCarList();
        return carList;
    }

    public Result newCar(Boolean status, Double longitude, Double latitude) {
        Integer num = carMapper.getNum();
        Car car = new Car();
        String id = "c" + String.format("%03d", num + 1);
        car.setId(id);
        car.setStatus(status);
        car.setLongitude(longitude);
        car.setLatitude(latitude);
        carMapper.newCar(car);

        carUpdateTime.put(id,System.currentTimeMillis());
        return Result.success(car);
    }

    /**
     * 更新车辆状态
     * @param id
     * @param status
     * @param longitude
     * @param latitude
     * @return
     */
    public Result updateCar(String id, Boolean status, Double longitude, Double latitude) {
        Car car = carMapper.getCarById(id);
        car.setStatus(status);
        car.setLongitude(longitude);
        car.setLatitude(latitude);
        carMapper.updateCar(car);
        carUpdateTime.put(id,System.currentTimeMillis());

        //检测是否到达目的地
        detectArrive(car);
        return Result.success(car);
    }

    private boolean arrive(Double carLon,Double carLat,Double lon,Double lat){
        return Math.abs(carLon - lon) < 0.003 && Math.abs(carLat - lat) < 0.003;
    }

    /**
     * 检测是否到达目的地
     * @param car
     */
    private void detectArrive(Car car) {
        Order order = orderMapper.getAssignOrder(car.getId());
        if (order == null ) return;
        boolean update = false;
        if (order.getStatus() == OrderStatusCode.WAIT_TO_START.getStatus()){
            //终点是起点
            if (arrive(car.getLongitude(),car.getLatitude(),order.getStartLon(),order.getStartLat())){
                order.setStatus(OrderStatusCode.WAIT_TO_UP.getStatus());
                update = true;
            }
        }else if (order.getStatus() == OrderStatusCode.WAIT_TO_END.getStatus()){
            //终点是终点
            if (arrive(car.getLongitude(),car.getLatitude(),order.getEndLon(),order.getEndLat())){
                order.setStatus(OrderStatusCode.WAIT_TO_DOWN.getStatus());
                update = true;
            }
        }
        if (update == true){
            orderMapper.updateOrder(order);

            //设置车辆指令为停止
            carCommand.put(car.getId(),false);
        }
    }

    /**
     * 每隔5秒更新一次车辆状态
     */
    @Scheduled(fixedRate = 5000)
    public void updateCarStatus() {
        List<Car> carList = carMapper.getActiveCarList();
        for (Car car : carList) {
            Long lastUpdateTime = carUpdateTime.get(car.getId());
            if (lastUpdateTime == null || System.currentTimeMillis() - lastUpdateTime > 5000) {
                //如果超过5秒没有更新，就认为车辆离线了
                car.setStatus(false);
                carMapper.updateCar(car);
            }
        }
    }

    public void setStart(String id){
        carCommand.put(id,true);
    }
    /**
     * 根据上下车来获取车辆命令
     * @param id
     * @return
     */
    public Boolean getStartOrStop(String id) {
        if (!carCommand.containsKey(id)){
            carCommand.put(id,true);
        }
        return carCommand.get(id);
    }

    //路径缓存，根据车辆id，订单id，订单状态来缓存路径
    static Map<String,List> routeCache = new HashMap<>();

    /**
     * 路径规划
     * @param id
     * @return
     */
    public Result getRoute(String id) {
        Car car = carMapper.getCarById(id);
        Order order = orderMapper.getAssignOrder(id);
        if (order == null) return Result.error(ResultCode.NO_ORDER);

        //如果缓存中有路径，就直接返回
        String key = id + order.getId() + order.getStatus();
        if (routeCache.containsKey(key)){
            List route = routeCache.get(key);
            return Result.success(route);
        }

        //根据订单状态获取起点和终点
        Double startLon,startLat,endLon,endLat;
        startLat = car.getLatitude();
        startLon = car.getLongitude();
        if (order.getStatus() == OrderStatusCode.WAIT_TO_START.getStatus()){
            endLon = order.getStartLon();
            endLat = order.getStartLat();
        }else if (order.getStatus() == OrderStatusCode.WAIT_TO_END.getStatus()){
            endLon = order.getEndLon();
            endLat = order.getEndLat();
        }else{
            return Result.error(ResultCode.NO_ORDER);
        }

        List route = tencentMapUtil.getRoute(startLon,startLat,endLon,endLat);
        routeCache.put(key,route);
        return Result.success(route);
    }

    public Car getCarByOrderId(String orderId) {
        return carMapper.getCarByOrderId(orderId);
    }

}
