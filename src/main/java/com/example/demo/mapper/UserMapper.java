package com.example.demo.mapper;


import com.example.demo.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

public interface UserMapper {

    @Select("select * from users where username = #{loginName} or phone = #{loginName}")
    User findByLoginName(@Param("loginName") String loginName);


    @Insert("insert into users (id,username, password, phone) values (#{record.id},#{record.username}, #{record.password}, #{record.phone})")
    int insert(@Param("record") User user);

    @Select("select count(*) from users")
    int getNum();

    @Select("select * from users where id = #{id}")
    User findById(@Param("id") String id);

    @Update("update users set password = #{record.password}, type = #{record.type} where id = #{record.id}")
    int update(@Param("record") User user);

}
