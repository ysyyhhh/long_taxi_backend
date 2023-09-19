package com.example.demo;

import com.example.demo.mapper.CarMapper;
import com.example.demo.model.entity.Car;
import com.example.demo.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ServiceTest {

    @Resource
    UserService userService;

    @Resource
    CarMapper carMapper;
    @Test
    public void test() {
        userService.wxLogin("0a3xJh0w3U9ck139NM2w3jlEqH3xJh0E");

    }

    @Test
    public void carTest(){
        List<Car> carList = carMapper.getCarList();
        for (Car car : carList) {
            System.out.println(car);
        }
    }
}
