package com.example.demo.mapper;

import com.example.demo.model.entity.Order;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.Collection;
import java.util.List;

public interface OrderMapper {

    @Select("select count(*) from orders")
    Integer getNum();


    //插入订单
    //参数使用#{order.xxx}，会自动匹配Order类中的xxx属性
    @Insert("insert into orders(id, user_id, status, start_lon, start_lat, end_lon, end_lat,created_at) values(#{order.id}, #{order.userId}, #{order.status}, #{order.startLon}, #{order.startLat}, #{order.endLon}, #{order.endLat},#{order.createdAt})")
    void newOrder(@Param("order") Order order);

    @Select("select * from orders where id = #{id}")
    Order getOrderById(@Param("id") String id);

    //更新订单，如果对应参数不为空则更新
    @Update({
            "<script>",
            "update orders",
            "<set>",
            "<if test='record.userId != null'> user_id = #{record.userId},</if>",
            "<if test='record.carId != null'> car_id = #{record.carId},</if>",
            "<if test='record.status != null'> status = #{record.status},</if>",
            "<if test='record.startLon != null'> start_lon = #{record.startLon},</if>",
            "<if test='record.startLat != null'> start_lat = #{record.startLat},</if>",
            "<if test='record.endLon != null'> end_lon = #{record.endLon},</if>",
            "<if test='record.endLat != null'> end_lat = #{record.endLat},</if>",
            "<if test='record.createdAt != null'> created_at = #{record.createdAt},</if>",
            "</set>",
            "where id = #{record.id}",
            "</script>"
    })
    int updateOrder(@Param("record") Order order);

    @Select("select * from orders where user_id = #{userId}")
    List<Order> getOrderList(@Param("userId") String userId);

    @Select("select * from orders where status < 5 and car_id = #{carId}")
    Order getAssignOrder(@Param("carId") String carId);

    @Select("select * from orders where status = 0")
    List<Order> getWaitAssignOrderList();
}
