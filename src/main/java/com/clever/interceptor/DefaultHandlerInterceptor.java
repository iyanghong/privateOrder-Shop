package com.clever.interceptor;

import com.clever.config.AppRunConfig;
import com.clever.constant.CacheConstant;
import com.clever.service.RedisService;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author xixi
 * @Date 2023-12-19 16:13
 **/
public class DefaultHandlerInterceptor implements HandlerInterceptor {

    @Resource
    private RedisService redis;

    @Resource
    private AppRunConfig appRunConfig;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestMethod = "OPTIONS";
        if (requestMethod.equals(request.getMethod())) {
            return true;
        }
        String token = request.getHeader("Authorization");
        Object onlineUser = redis.getString(CacheConstant.getOnlineKeyName(token));
        if (onlineUser != null) {
            request.setAttribute("online", onlineUser);
        }
        /*else {
            if (StringUtils.isNotBlank(appRunConfig.getDefaultOnlineUserId())) {
                OnlineUser user = new OnlineUser();
                user.setId(appRunConfig.getDefaultOnlineUserId());
                request.setAttribute("online", user);
            }
        }*/
        return true;
    }
}
