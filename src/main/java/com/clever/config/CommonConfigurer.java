package com.clever.config;

import com.clever.config.thread.ThreadPool;
import com.clever.config.thread.ThreadPoolConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.annotation.Resource;

/**
 * 常用类配置
 *
 * @Author xixi
 * @Date 2023-12-26 15:16
 **/
@Configuration
public class CommonConfigurer {
    @Resource
    private ThreadPoolConfig threadPoolConfig;

    @Bean(name = "threadPoolTaskExecutor")
    public ThreadPoolTaskExecutor threadPoolTaskExecutor() {
        ThreadPool threadPool = threadPoolConfig.getThreadPool();
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setMaxPoolSize(threadPool.getMaxPoolSize());
        executor.setCorePoolSize(threadPool.getCorePoolSize());
        executor.setQueueCapacity(threadPool.getQueueCapacity());
        executor.setKeepAliveSeconds(threadPool.getKeepAliveSeconds());
        executor.setThreadNamePrefix(threadPool.getPrefix());
        // 线程池对拒绝任务(无线程可用)的处理策略
        executor.setRejectedExecutionHandler(threadPool.getExecutionHandler());
        return executor;
    }
}
