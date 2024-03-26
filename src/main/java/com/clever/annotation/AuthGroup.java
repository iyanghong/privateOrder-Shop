package com.clever.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 权限组
 *
 * @Author xixi
 * @Date 2023-12-20 10:58
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface AuthGroup {

    String value();
    /**
     * 权限组名
     *
     * @return 名
     */
    String name();

    /**
     * 权限组描述
     *
     * @return 描述
     */
    String description() default "";
    /**
     * 是否启用
     *
     * @return 是否启用
     */
    boolean enable() default true;

    /**
     * 是否只需要登录
     *
     * @return 是否只需要登录
     */
    boolean isOnlyLogin() default false;
}