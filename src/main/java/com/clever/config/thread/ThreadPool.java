package com.clever.config.thread;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @Author xixi
 * @Date 2023-12-15 10:00
 **/
public class ThreadPool {
    /**
     * 核心池大小
     */
    private int corePoolSize = 30;

    /**
     * 最大线程数
     */
    private int maxPoolSize = 200;

    /**
     * 队列最大长度
     */
    private int queueCapacity = 1000;

    /**
     * 空闲时间
     */
    private int keepAliveSeconds = 300;

    /**
     * 线程前缀
     */
    private String prefix = "ISay";

    /**
     * 无线程可用处理策略
     */
    private RejectedExecutionHandler executionHandler = new ThreadPoolExecutor.CallerRunsPolicy();

    public int getCorePoolSize() {
        return corePoolSize;
    }

    public void setCorePoolSize(int corePoolSize) {
        this.corePoolSize = corePoolSize;
    }

    public int getMaxPoolSize() {
        return maxPoolSize;
    }

    public void setMaxPoolSize(int maxPoolSize) {
        this.maxPoolSize = maxPoolSize;
    }

    public int getQueueCapacity() {
        return queueCapacity;
    }

    public void setQueueCapacity(int queueCapacity) {
        this.queueCapacity = queueCapacity;
    }

    public int getKeepAliveSeconds() {
        return keepAliveSeconds;
    }

    public void setKeepAliveSeconds(int keepAliveSeconds) {
        this.keepAliveSeconds = keepAliveSeconds;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public RejectedExecutionHandler getExecutionHandler() {
        return executionHandler;
    }

    public void setExecutionHandler(RejectedExecutionHandler executionHandler) {
        this.executionHandler = executionHandler;
    }
}
