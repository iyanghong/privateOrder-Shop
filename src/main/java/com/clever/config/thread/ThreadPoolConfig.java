package com.clever.config.thread;

import com.clever.config.thread.ThreadPool;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.stereotype.Component;

/**
 * 线程池配置
 *
 * @Author xixi
 * @Date 2023-12-15 10:01
 **/
@Component("threadPoolConfig")
@ConfigurationProperties(prefix = "clever")
public class ThreadPoolConfig {
    @NestedConfigurationProperty
    private ThreadPool threadPool = new ThreadPool();

    public ThreadPool getThreadPool() {
        return threadPool;
    }

    public void setThreadPool(ThreadPool threadPool) {
        this.threadPool = threadPool;
    }
}
