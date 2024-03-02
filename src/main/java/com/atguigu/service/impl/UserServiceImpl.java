package com.atguigu.service.impl;

import com.atguigu.utils.JwtHelper;
import com.atguigu.utils.MD5Util;
import com.atguigu.utils.Result;
import com.atguigu.utils.ResultCodeEnum;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atguigu.pojo.User;
import com.atguigu.service.UserService;
import com.atguigu.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
* @author xwzy
* @description 针对表【news_user】的数据库操作Service实现
* @createDate 2024-02-27 22:46:49
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private JwtHelper jwtHelper;
    @Override
    public Result login(User user) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername,user.getUsername());
        User loginUser = userMapper.selectOne(wrapper);
        //如果账号不存在
        if(loginUser == null){
            return Result.build(null,ResultCodeEnum.USERNAME_ERROR);
        }
        //如果账号存在
        String loginPassword = loginUser.getUserPwd();
        //  检查密码是否正确
        if(loginUser.getUserPwd().equals(MD5Util.encrypt(user.getUserPwd()))){
            //根据userid获得token(这里并不是通过username生成）
            String token = jwtHelper.createToken(Long.valueOf(loginUser.getUid()));
            Map data = new HashMap<>();
            data.put("token",token);
            return Result.ok(data);
        }
        return Result.build(null,ResultCodeEnum.PASSWORD_ERROR);
    }

    /**
     * 查询用户数据
     * @param token
     * @return result封装
     */
    @Override
    public Result getUserInfo(String token) {

        //1.判定是否有效期
        if (jwtHelper.isExpiration(token)) {
            //true过期,直接返回未登录
            return Result.build(null,ResultCodeEnum.NOTLOGIN);
        }

        //2.获取token对应的用户
        int userId = jwtHelper.getUserId(token).intValue();

        //3.查询数据
        User user = userMapper.selectById(userId);

        if (user != null) {
            user.setUserPwd(null);
            Map data = new HashMap();
            data.put("loginUser",user);
            return Result.ok(data);
        }

        return Result.build(null,ResultCodeEnum.NOTLOGIN);
    }

    @Override
    public Result checkUserName(String username) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername,username);
        //selectCount返回查询条数
        Long count = userMapper.selectCount(wrapper);
        if(count == 0){
            return Result.ok(null);
        }
        //用户名已占用
        return Result.build(null,ResultCodeEnum.USERNAME_USED);
    }

    @Override
    public Result register(User user) {
        Result result = checkUserName(user.getUsername());
        //判断用户是否被占用
        //被占用
        if(result.getMessage().equals("userNameUsed")){
            return result;
        }
        //未被占用，将密码加密后存入数据库
        String password = MD5Util.encrypt(user.getUserPwd());
        user.setUserPwd(password);
        userMapper.insert(user);
        return Result.ok(null);
    }
}




