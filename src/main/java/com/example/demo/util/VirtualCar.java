package com.example.demo.util;


import com.example.demo.model.entity.Car;
import com.example.demo.model.result.Result;
import com.example.demo.model.result.ResultCode;
import com.example.demo.service.CarService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class VirtualCar {
    @Resource
    CarService carService;

    Map<String,List> routeCache = new HashMap<>();

    List<TencentMapUtil.Point> getRouteCache(Car car){
        //当前有route，当前的位置在route上且不是最后一个点，就返回route, 否则返回null
        List<TencentMapUtil.Point> route = routeCache.get(car.getId());

        if(route == null){
            return null;
        }
        TencentMapUtil.Point lastPoint = route.get(route.size() - 1);
        if(lastPoint.getLon().equals(car.getLongitude()) && lastPoint.getLat().equals(car.getLatitude())) {
            return null;
        }

        //从下一个点
        return route;
    }

    TencentMapUtil.Point getNextPoint(Car car){
        List<TencentMapUtil.Point> route = getRouteCache(car);
        if(route == null){
            Result<List<TencentMapUtil.Point>> result = carService.getRoute(car.getId());
            if(result.getCode()!= ResultCode.SUCCESS.getCode()){
                return null;
            }
            route = result.getData();
        }
        if(route == null || route.isEmpty()){
            return null;
        }
        //当前位置的下一个点(从后往前找下一个点)
        TencentMapUtil.Point nextPoint = null;

        for(int i = route.size() - 1; i >= 0; i--){
            TencentMapUtil.Point point = route.get(i);
            if(point.equals(new TencentMapUtil.Point(car.getLongitude(),car.getLatitude()))){
                if(i + 1 < route.size()){
                    log.info("car {} move to next point {}, total is {}",car.getId(),i + 1,route.size());
                    nextPoint = route.get(i + 1);
                }
                break;
            }
        }
        if (nextPoint == null) {
            log.info("car {} move to first point",car.getId());
            nextPoint = route.get(0);
        }
        return nextPoint;
    }
    void next(Car car){
        //先获取指令，如果不能移动，就不移动
        Boolean start = carService.getStartOrStop(car.getId());
        if (start){
            //获取路径，如果缓存中没有，才去数据库中获取
            TencentMapUtil.Point nextPoint = getNextPoint(car);

            if (nextPoint != null) {
                car.setLatitude(nextPoint.getLat());
                car.setLongitude(nextPoint.getLon());
                log.info("car {} move to {},{}",car.getId(),car.getLongitude(),car.getLatitude());
            }

        }

        //更新状态
        carService.updateCar(car.getId(),true,car.getLongitude(),car.getLatitude());
    }
    void updateAllCar(){
        List<Car> carList = carService.getCarList();
        for (Car car : carList) {
            next(car);
        }
    }
    @PostConstruct
    public void init() {
        log.info("init virtual car");
        updateAllCar();
    }

    //每1秒更新一次车辆状态
    @Async
    @Scheduled(cron = "0/1 * * * * ?")
    public void updateCar() {
        log.info("update virtual car");
        updateAllCar();
    }

}
