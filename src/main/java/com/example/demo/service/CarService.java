package com.example.demo.service;

import com.example.demo.entity.Car;
import com.example.demo.mapper.CarMapper;
import com.example.demo.result.Result;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class CarService {
    @Resource
    CarMapper carMapper;
    public Result getCarList() {
        List<Car> carList = carMapper.getCarList();
        return Result.success(carList);
    }

    public Result newCar(String status, Double longitude, Double latitude) {
        Integer num = carMapper.getNum();
        Car car = new Car();
        String id = "c" + String.format("%03d", num + 1);
        car.setId(id);
        car.setStatus(status);
        car.setLongitude(longitude);
        car.setLatitude(latitude);
        carMapper.newCar(car);
        return Result.success(car);
    }

    public Result updateCar(String id, String status, Double longitude, Double latitude) {
        Car car = carMapper.getCarById(id);
        car.setStatus(status);
        car.setLongitude(longitude);
        car.setLatitude(latitude);
        carMapper.updateCar(car);
        return Result.success(car);
    }
}
