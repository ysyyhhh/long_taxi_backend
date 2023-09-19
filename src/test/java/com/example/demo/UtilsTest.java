package com.example.demo;

import com.example.demo.util.TencentMapUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@SpringBootTest
@RunWith(SpringRunner.class)
public class UtilsTest {

    @Resource
    TencentMapUtil tencentMapUtil;


    @Test
    public void test() {
        Double start_lon = 121.477439;
        Double start_lat = 31.232382;
        Double end_lon = 116.397499;
        Double end_lat = 39.948722;
        System.out.println( tencentMapUtil.getRoute(start_lon,start_lat,end_lon,end_lat) );
    }

}
