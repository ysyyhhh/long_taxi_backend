package com.example.demo.mapper;

import com.example.demo.entity.Order;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.sql.Date;
import java.util.List;

public interface OrderMapper {

    @Select("select count(*) from orders")
    Integer getNum();

    @Insert("insert into orders (id, user_id, car_id, status, created_at) values (#{record.id},#{record.user_id}, #{record.car_id}, #{record.status}, #{record.created_at})")
    int newOrder(@Param("record") Order order);

    @Select("select * from orders where id = #{id}")
    Order getOrderById(@Param("id") String id);

    @Update("update orders set status = #{record.status} where id = #{record.id}")
    int updateOrder(@Param("record") Order order);

    @Select("select * from orders")
    List<Order> getOrderList();
}
