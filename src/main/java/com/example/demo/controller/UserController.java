package com.example.demo.controller;

import com.example.demo.model.result.Result;
import com.example.demo.model.result.ResultCode;
import com.example.demo.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    UserService userService;

    @PostMapping("/login")
    public Result login(@RequestBody Map<String,Object> params) {
        String loginName = (String) params.get("loginName");
        String password = (String) params.get("password");
        if (loginName == null || password == null) {
            return Result.error(ResultCode.PARAM_IS_BLANK);
        }
        return userService.login(loginName,password);
    }

    @PostMapping("/signUp")
    public Result signUp(@RequestBody Map<String,String>params) {
        String phone = (String) params.get("phone");
        String password = (String) params.get("password");
        String username = (String) params.get("username");
        if (phone == null || password == null || username == null) {
            return Result.error(ResultCode.PARAM_IS_BLANK);
        }
        return userService.signUp(phone,password,username);
    }

    @PostMapping("/updatePassword")
    public Result updatePassword(@RequestBody Map<String,String>params) {
        String password = (String) params.get("password");
        String id = (String) params.get("id");
        if (password == null || id == null) {
            return Result.error(ResultCode.PARAM_IS_BLANK);
        }
        return userService.updatePassword(id,password);
    }

    @PostMapping("/update")
    public Result update(@RequestBody Map<String,String>params) {
        String password = (String) params.get("password");
        String type = (String) params.get("type");
        String id = (String) params.get("id");
        if (password == null || type == null || id == null) {
            return Result.error(ResultCode.PARAM_IS_BLANK);
        }
        return userService.update(id,password,type);
    }

    @PostMapping("/wxLogin")
    public Result wxLogin(@RequestBody Map<String,String>params) {
        String code = (String) params.get("code");
        if (code == null) {
            return Result.error(ResultCode.PARAM_IS_BLANK);
        }
        return userService.wxLogin(code);
    }

}
