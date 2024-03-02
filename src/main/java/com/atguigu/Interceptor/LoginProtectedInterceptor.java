package com.atguigu.Interceptor;

import com.atguigu.utils.JwtHelper;
import com.atguigu.utils.Result;
import com.atguigu.utils.ResultCodeEnum;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.awt.geom.RectangularShape;

@Component//加上component，在webmvcconfig添加拦截器时可以注入
public class LoginProtectedInterceptor implements HandlerInterceptor {
    @Autowired
    private JwtHelper jwtHelper;
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("token");
        boolean expiration = jwtHelper.isExpiration(token);
        if(!expiration){
            return true;//token验证通过，放行
        }
        //token已过期，给前端响应错误信息,此处需要json格式
        String json = objectMapper.writeValueAsString(Result.build(null, ResultCodeEnum.NOTLOGIN));
        response.getWriter().print(json);
        return false;//拦截
    }
}
