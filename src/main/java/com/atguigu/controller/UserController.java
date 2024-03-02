package com.atguigu.controller;

import com.atguigu.pojo.User;
import com.atguigu.service.UserService;
import com.atguigu.utils.JwtHelper;
import com.atguigu.utils.Result;
import com.atguigu.utils.ResultCodeEnum;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("user")
@CrossOrigin
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private JwtHelper jwtHelper;
    @PostMapping("login")
    public Result login(@RequestBody User user){
        Result result = new Result<>();
        result = userService.login(user);
        //System.out.println("result:"+ result);
        return result;
    }
    /**
     * 地址: user/getUserInfo
     * 方式: get
     * 请求头: token = token内容
     * 返回:
     *    {
     *     "code": 200,
     *     "message": "success",
     *     "data": {
     *         "loginUser": {
     *             "uid": 1,
     *             "username": "zhangsan",
     *             "userPwd": "",
     *             "nickName": "张三"
     *         }
     *      }
     *   }
     *
     * 大概流程:
     *    1.获取token,解析token对应的userId
     *    2.根据userId,查询用户数据
     *    3.将用户数据的密码置空,并且把用户数据封装到结果中key = loginUser
     *    4.失败返回504 (本次先写到当前业务,后期提取到拦截器和全局异常处理器)
     */
    @GetMapping("getUserInfo")
    public Result userInfo(@RequestHeader String token){
        Result result = userService.getUserInfo(token);
        return result;
    }
    @PostMapping("checkUserName")
    public Result checkUserName(@RequestParam String username){
        Result result = userService.checkUserName(username);
        return result;
    }
    @PostMapping("regist")
    public Result register(@RequestBody User user){
        Result result = userService.register(user);
        return result;
    }
    @GetMapping("checkLogin")
    public Result checkLogin(@RequestHeader String token){
        boolean expiration = jwtHelper.isExpiration(token);
        if(expiration){
            return Result.build(null, ResultCodeEnum.NOTLOGIN);
        }
        return Result.ok(null);
    }

}
