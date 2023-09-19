package com.example.demo.service;

import com.example.demo.model.entity.User;
import com.example.demo.mapper.UserMapper;
import com.example.demo.model.result.Result;
import com.example.demo.model.result.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

@Slf4j
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

    public static final String APPID = "微信小程序的APPID";
    public static final String SECRET = "微信小程序的SECRET";
    public Result wxLogin(String code) {
        //https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code
        RestTemplate restTemplate=new RestTemplate();
        String url="https://api.weixin.qq.com/sns/jscode2session?appid="+APPID+"&secret="+SECRET+"&js_code="+code+"&grant_type=authorization_code";
        String json =restTemplate.getForObject(url,String.class);

        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(json);
            if (jsonObject.has("errcode")) {
                return Result.error(ResultCode.USER_LOGIN_ERROR);
            }
            String openid = (String) jsonObject.get("openid");
            log.info("openid:{}",openid);

            User user = userMapper.findByLoginName(openid);
            if (user != null) {
                //如果存在则直接返回
                return Result.success(user);
            }

            //不存在则注册
            Integer num = userMapper.getNum();
            user = new User();

            // u001
            user.setId("u" + String.format("%03d", num + 1));
            user.setPhone(openid);
            user.setPassword(openid);
            user.setUsername("wx_" + user.getId());
            user.setType("wx_user");
            userMapper.insert(user);

            return Result.success(user);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Result.error(ResultCode.USER_LOGIN_ERROR);
    }
}
