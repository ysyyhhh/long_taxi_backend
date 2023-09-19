package com.example.demo.controller;

import com.example.demo.model.result.Result;
import com.example.demo.model.result.ResultCode;
import com.example.demo.service.CarService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

@RestController
@RequestMapping("/car")
public class CarController {

    @Resource
    CarService carService;

    @GetMapping("/getList")
    public Result getCarList() {
        return Result.success(carService.getCarList());
    }

    @PostMapping("/new")
    public Result newCar(@RequestBody Map<String,Object> params) {
        Boolean status = (Boolean) params.get("status");
        Double longitude = (Double) params.get("longitude");
        Double latitude = (Double) params.get("latitude");
        if (status == null || longitude == null || latitude == null) {
            return Result.error(ResultCode.PARAM_IS_BLANK);
        }
        return carService.newCar(status,longitude,latitude);
    }


    @PostMapping("/update")
    public Result updateCar(@RequestBody Map<String,Object> params) {
        String id = (String) params.get("id");
        Boolean status = (Boolean) params.get("status");
        Double longitude = (Double) params.get("longitude");
        Double latitude = (Double) params.get("latitude");
        if (id == null || status == null || longitude == null || latitude == null) {
            return Result.error(ResultCode.PARAM_IS_BLANK);
        }
        return carService.updateCar(id,status,longitude,latitude);
    }

    @GetMapping("/getCarByOrderId/{orderId}")
    public Result getCarByOrderId(@PathVariable("orderId") String orderId) {
        return Result.success(carService.getCarByOrderId(orderId));
    }

}
