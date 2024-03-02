package com.atguigu.service;

import com.atguigu.pojo.User;
import com.atguigu.utils.Result;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author xwzy
* @description 针对表【news_user】的数据库操作Service
* @createDate 2024-02-27 22:46:49
*/
public interface UserService extends IService<User> {
    /**
     * 登录业务
     * @param user
     * @return
     */
    Result login(User user);

    Result getUserInfo(String token);

    Result checkUserName(String username);

    Result register(User user);
}
