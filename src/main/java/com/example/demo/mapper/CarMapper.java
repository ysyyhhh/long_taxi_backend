package com.example.demo.mapper;

import com.example.demo.entity.Car;
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
}
