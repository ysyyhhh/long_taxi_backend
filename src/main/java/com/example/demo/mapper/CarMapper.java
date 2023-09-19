package com.example.demo.mapper;

import com.example.demo.model.entity.Car;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface CarMapper {

    @Select("select * from cars")
    List<Car> getCarList();

    @Select("select count(*) from cars")
    Integer getNum();

    @Insert("insert into cars (id, status, longitude, latitude) values (#{record.id},#{record.status}, #{record.longitude}, #{record.latitude})")
    Integer newCar(@Param("record") Car car);


    @Select("select * from cars where id = #{id}")
    Car getCarById(@Param("id") String id);

    @Update("update cars set status = #{record.status}, longitude = #{record.longitude}, latitude = #{record.latitude} where id = #{record.id}")
    Integer updateCar(@Param("record")Car car);

    //FreeCar定义： 在orders中没有status 小于 5 且 car_id 等于自己的订单
    @Select("select * from cars where id not in (select car_id from orders where status < 5 and car_id is not null) limit 1")
    Car getFreeCar();

    @Select("select * from cars where status = 1")
    List<Car> getActiveCarList();

    @Select("select * from cars where id in (select car_id from orders where status < 5 and car_id is not null and id = #{orderId})")
    Car getCarByOrderId(@Param("orderId") String orderId);
}
