package com.clever.config.resolver;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.support.WebBindingInitializer;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.ServletModelAttributeMethodProcessor;

import javax.servlet.ServletRequest;

/**
 * @Author xixi
 * @Date 2023-12-19 16:45
 **/
public class EmptyStringToNullArgumentResolver extends ServletModelAttributeMethodProcessor implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    public EmptyStringToNullArgumentResolver(boolean annotationNotRequired) {
        super(annotationNotRequired);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    protected void bindRequestParameters(WebDataBinder binder, NativeWebRequest request) {
        EmptyStringToNullRequestDataBinder emptyStringToNullRequestDataBinder = new EmptyStringToNullRequestDataBinder(binder.getTarget(), binder.getObjectName());
        RequestMappingHandlerAdapter requestMappingHandlerAdapter = applicationContext.getBean(RequestMappingHandlerAdapter.class);
        WebBindingInitializer webBindingInitializer = requestMappingHandlerAdapter.getWebBindingInitializer();
        if (webBindingInitializer != null) {
            webBindingInitializer.initBinder(emptyStringToNullRequestDataBinder);
        }
        ServletRequest nativeRequest = request.getNativeRequest(ServletRequest.class);
        if (nativeRequest != null) {
            emptyStringToNullRequestDataBinder.bind(nativeRequest);
        }
    }
}

