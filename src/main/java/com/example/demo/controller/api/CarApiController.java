/**
 * 车端调用的接口
 */
package com.example.demo.controller.api;

import com.example.demo.model.request.CarData;
import com.example.demo.model.result.Result;
import com.example.demo.model.result.ResultCode;
import com.example.demo.service.CarService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

@RestController
@RequestMapping("/api/car")
public class CarApiController {
    @Resource
    CarService carService;

    @PostMapping("/update")
    public Result updateCar(@ModelAttribute CarData carData) {
        return carService.updateCar(carData.getId(),true,carData.getLongitude(),carData.getLatitude());
    }

    @GetMapping("/getStartOrStop/{id}")
    public Result getStartOrStop(@PathVariable String id) {
        return Result.success(carService.getStartOrStop(id));
    }


    @GetMapping("/getRoute/{id}")
    public Result getRoute(@PathVariable String id) {
        return carService.getRoute(id);
    }

}
