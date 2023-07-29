package com.example.demo.service;

import com.example.demo.entity.User;
import com.example.demo.mapper.UserMapper;
import com.example.demo.result.Result;
import com.example.demo.result.ResultCode;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

@Service
public class UserService {

    @Resource
    UserMapper userMapper;

    public Result login(String loginName, String password) {
        User user = userMapper.findByLoginName(loginName);
        if (user == null) {
            return Result.error(ResultCode.USER_LOGIN_ERROR);
        }
        if (!user.getPassword().equals(password)) {
            return Result.error(ResultCode.USER_LOGIN_ERROR);
        }
        return Result.success(user);
    }

    public Result signUp(String phone, String password, String username) {
        User user = userMapper.findByLoginName(phone);
        if (user != null) {
            return Result.error(ResultCode.USER_HAS_EXISTED);
        }
        Integer num = userMapper.getNum();

        user = new User();
        // u001
        user.setId("u" + String.format("%03d", num + 1));
        user.setPhone(phone);
        user.setPassword(password);
        user.setUsername(username);
        user.setType("user");
        userMapper.insert(user);

        return Result.success(user);
    }

    public Result updatePassword(String id,String password) {
        User user = userMapper.findById(id);
        if (user == null) {
            return Result.error(ResultCode.USER_NOT_EXIST);
        }
        user.setPassword(password);
        userMapper.update(user);
        return Result.success(user);
    }

    public Result update(String id, String password, String type) {
        User user = userMapper.findById(id);
        if (user == null) {
            return Result.error(ResultCode.USER_NOT_EXIST);
        }
        user.setPassword(password);
        user.setType(type);
        userMapper.update(user);
        return Result.success(user);
    }
}
