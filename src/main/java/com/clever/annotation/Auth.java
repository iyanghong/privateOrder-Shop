package com.clever.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * 用于系统权限认证
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Auth {
    /**
     * 需要拥有的权限
     *
     * @return 权限
     */
    String value();

    /**
     * 权限名
     *
     * @return 权限名
     */
    String name();

    /**
     * 描述
     *
     * @return 权限描述
     */
    String description() default "";

    /**
     * 无权限被拦截后提示
     *
     * @return 拦截提示
     */
    String errorMessage() default "无权限，不能这样子哦";

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
