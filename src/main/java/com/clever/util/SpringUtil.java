package com.clever.util;

import com.clever.bean.model.OnlineUser;
import com.clever.exception.BaseException;
import com.clever.exception.ConstantException;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

/**
 * @Author xixi
 * @Date 2023-12-15 09:27
 **/
public class SpringUtil {

    private static ConfigurableApplicationContext applicationContext = null;

    public static ConfigurableApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public static void setApplicationContext(ConfigurableApplicationContext applicationContext) {
        if (SpringUtil.applicationContext == null) {
            SpringUtil.applicationContext = applicationContext;
        }
    }

    /**
     * 通过bean名获取对象
     *
     * @param name beanName
     * @return spring bean
     */
    public static Object getBean(String name) {
        return getApplicationContext().getBean(name);
    }

    /**
     * 根据class获取bean
     *
     * @param clazz class
     * @param <T>   T
     * @return spring bean
     */
    public static <T> T getBean(Class<T> clazz) {
        return getApplicationContext().getBean(clazz);
    }

    /**
     * 根据bean名+class获取对象
     *
     * @param name  beanName
     * @param clazz class
     * @param <T>   t
     * @return spring bean
     */
    public static <T> T getBean(String name, Class<T> clazz) {
        return getApplicationContext().getBean(name, clazz);
    }

    /**
     * 获取当前request
     *
     * @return request
     */
    public static HttpServletRequest getRequest() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (requestAttributes != null) {
            return requestAttributes.getRequest();
        }
        throw new RuntimeException("request对象获取失败");
    }

    /**
     * 获取当前在线用户
     *
     * @return 在线用户
     */
    public static OnlineUser getOnlineUser() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes != null) {
            OnlineUser onlineUser = (OnlineUser) requestAttributes.getAttribute("online", RequestAttributes.SCOPE_REQUEST);
            if (onlineUser != null) {
                return onlineUser;
            }
        }
        throw new BaseException(ConstantException.USER_NO_ONLINE);
    }

    /**
     * 获取当前在线用户,如果已登录的话
     *
     * @return 在线用户
     */
    public static OnlineUser getOnlineUserIfExist() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes != null) {
            OnlineUser onlineUser = (OnlineUser) requestAttributes.getAttribute("online", RequestAttributes.SCOPE_REQUEST);
            if (onlineUser != null) {
                return onlineUser;
            }
        }
        return null;
    }

    /**
     * 生成随机uuid字符串
     *
     * @return uuid字符串
     */
    public static String getUuid() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

}
