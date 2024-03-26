package com.clever.config.resolver;

import com.clever.bean.model.OnlineUser;
import com.clever.exception.BaseException;
import com.clever.exception.ConstantException;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * 接口方法注入登录用户的参数解析器
 *
 * @Author xixi
 * @Date 2023-12-19 16:42
 **/
public class ApiMethodUserArgumentResolver implements HandlerMethodArgumentResolver {
    /**
     * 注入当前user
     *
     * @param methodParameter 方法参数
     * @return 是否有此类型
     */
    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return methodParameter.getParameterType().isAssignableFrom(OnlineUser.class);
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer,
                                  NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) {
        //在request作用域里面取user
        Object online = nativeWebRequest.getAttribute("online", RequestAttributes.SCOPE_REQUEST);
        if (online != null) {
            return online;
        } else {
            throw new BaseException(ConstantException.USER_NO_ONLINE);
        }
    }
}
