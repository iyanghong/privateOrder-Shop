package com.clever.config.resolver;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.PropertyValue;
import org.springframework.web.servlet.mvc.method.annotation.ExtendedServletRequestDataBinder;

import javax.servlet.ServletRequest;

/**
 * @Author xixi
 * @Date 2023-12-19 16:45
 **/
public class EmptyStringToNullRequestDataBinder extends ExtendedServletRequestDataBinder {

    public EmptyStringToNullRequestDataBinder(Object target, String objectName) {
        super(target, objectName);
    }

    protected void addBindValues(MutablePropertyValues mutablePropertyValues, ServletRequest request) {
        super.addBindValues(mutablePropertyValues, request);
        //把已绑定的空字符串转为null
        for (PropertyValue propertyValue : mutablePropertyValues.getPropertyValueList()) {
            if (propertyValue.getValue() == null || StringUtils.isBlank(propertyValue.getValue().toString())) {
                propertyValue.setConvertedValue(null);
            }
        }
    }
}
