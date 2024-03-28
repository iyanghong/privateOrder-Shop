package com.clever.config;

import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import com.clever.config.mybatis.CustomIdGenerator;
import com.clever.config.resolver.ApiMethodUserArgumentResolver;
import com.clever.config.resolver.EmptyStringToNullArgumentResolver;
import com.clever.interceptor.DefaultHandlerInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author xixi
 * @Date 2023-12-19 16:41
 **/
@Configuration
@Order(10)
public class WebConfigurer implements WebMvcConfigurer {

    private static final Logger log = LoggerFactory.getLogger(WebConfigurer.class);

    @Resource
    private AppRunConfig appRunConfig;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        log.info("加载跨域拦截");
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowCredentials(true)
                .allowedMethods("GET", "POST", "DELETE", "PUT", "OPTIONS","PATCH")
                .allowedHeaders("*")
                .exposedHeaders("*");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/file/resource/**").addResourceLocations(appRunConfig.getUploadFileFolder());
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(currentUserMethodArgumentResolver());
    }

    /**
     * 空字符串转null处理器
     *
     * @return 用户注入器
     */
    @Bean
    public EmptyStringToNullArgumentResolver emptyStringToNullArgumentResolver() {
        log.info("加载空字符串解析器");
        return new EmptyStringToNullArgumentResolver(true);
    }

    /**
     * 接口参数用户注入器
     *
     * @return 用户注入器
     */
    @Bean
    public ApiMethodUserArgumentResolver currentUserMethodArgumentResolver() {
        log.info("加载用户注入器");
        return new ApiMethodUserArgumentResolver();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        log.info("加载接口拦截器");
        registry.addInterceptor(new DefaultHandlerInterceptor()).addPathPatterns("/**").excludePathPatterns(
                "/user/login",
                "/user/register",
                "/file/resource/**/**"
        );
    }


    /**
     * 自定义id生成器
     *
     * @return id生成器
     */
    @Bean
    public IdentifierGenerator identifierGenerator() {
        return new CustomIdGenerator();
    }
}
