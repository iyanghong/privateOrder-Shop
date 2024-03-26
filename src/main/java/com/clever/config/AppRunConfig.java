package com.clever.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @Author xixi
 * @Date 2023-12-19 16:15
 **/
@Configuration
public class AppRunConfig {

    @Value("${spring.application.name}")
    private String appName = "";
    /**
     * 机器id
     */
    @Value("${spring.application.workerId}")
    private String workerId = "";
    /**
     * 数据中心id
     */
    @Value("${spring.application.datacenterId}")
    private String datacenterId = "";
    /**
     * 默认在线用户id，用于开发调试
     */
    @Value("${spring.application.defaultOnlineUserId}")
    private String defaultOnlineUserId = "";

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getWorkerId() {
        return workerId;
    }

    public void setWorkerId(String workerId) {
        this.workerId = workerId;
    }

    public String getDatacenterId() {
        return datacenterId;
    }

    public void setDatacenterId(String datacenterId) {
        this.datacenterId = datacenterId;
    }

    public String getDefaultOnlineUserId() {
        return defaultOnlineUserId;
    }

    public void setDefaultOnlineUserId(String defaultOnlineUserId) {
        this.defaultOnlineUserId = defaultOnlineUserId;
    }
}
