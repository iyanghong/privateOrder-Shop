package com.clever.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface AfterUpdate {

    /**
     * 实现AfterUpdateHandler的spring bean名
     *
     * @see com.clever.annotation.AfterUpdateHandler
     */
    Class<? extends AfterUpdateHandler> value();
}
