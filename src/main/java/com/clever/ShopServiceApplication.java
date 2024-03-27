package com.clever;

import com.clever.config.thread.ThreadPoolConfig;
import com.clever.util.SpringUtil;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * @Author xixi
 * @Date 2024-03-26 11:03
 **/
@EnableAsync
@SpringBootApplication
@MapperScan("com.clever.mapper")
@EnableConfigurationProperties({ThreadPoolConfig.class})
public class ShopServiceApplication {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(ShopServiceApplication.class, args);
        SpringUtil.setApplicationContext(context);
        System.out.println("\n" +
                ":'######::'##:::::::'########:'##::::'##:'########:'########::\n" +
                "'##... ##: ##::::::: ##.....:: ##:::: ##: ##.....:: ##.... ##:\n" +
                " ##:::..:: ##::::::: ##::::::: ##:::: ##: ##::::::: ##:::: ##:\n" +
                " ##::::::: ##::::::: ######::: ##:::: ##: ######::: ########::\n" +
                " ##::::::: ##::::::: ##...::::. ##:: ##:: ##...:::: ##.. ##:::\n" +
                " ##::: ##: ##::::::: ##::::::::. ## ##::: ##::::::: ##::. ##::\n" +
                ". ######:: ########: ########:::. ###:::: ########: ##:::. ##:\n" +
                ":......:::........::........:::::...:::::........::..:::::..::\n");
    }
}
