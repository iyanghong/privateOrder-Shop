package com.clever.annotation;

public interface AfterUpdateHandler {

    /**
     * 触发处理事件
     *
     * @param sql 执行的sql
     */
    void handle(String sql);
}
