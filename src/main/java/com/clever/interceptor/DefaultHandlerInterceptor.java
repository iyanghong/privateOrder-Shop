package com.clever.interceptor;

import cn.hutool.json.JSONUtil;
import com.auth0.jwt.interfaces.Claim;
import com.clever.bean.model.OnlineUser;
import com.clever.bean.shopping.User;
import com.clever.config.AppRunConfig;
import com.clever.exception.BaseException;
import com.clever.exception.ConstantException;
import com.clever.service.UserService;
import com.clever.util.JwtUtil;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @Author xixi
 * @Date 2023-12-19 16:13
 **/
public class DefaultHandlerInterceptor implements HandlerInterceptor {


    @Resource
    private AppRunConfig appRunConfig;

    @Resource
    private UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestMethod = "OPTIONS";
        if (requestMethod.equals(request.getMethod())) {
            return true;
        }
        String token = request.getHeader("Authorization");
        Map<String, Claim> userData = JwtUtil.verifyToken(token);
        if (userData == null) {
            throw new BaseException(ConstantException.TOKEN_INVALID);
        }
        User user = JSONUtil.toBean(userData.get("data").asString(), User.class);
        OnlineUser onlineUser = new OnlineUser(user, token);
        request.setAttribute("online", onlineUser);

        return true;
    }
}
