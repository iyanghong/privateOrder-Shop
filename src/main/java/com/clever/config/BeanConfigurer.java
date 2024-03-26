package com.clever.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.clever.config.mybatis.ModelFieldAutoFillHandler;
import org.hibernate.validator.HibernateValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

/**
 * @Author xixi
 * @Date 2023-12-19 16:07
 **/
@Configuration
public class BeanConfigurer {

    @Bean
    public ModelFieldAutoFillHandler metaObjectHandler() {
        return new ModelFieldAutoFillHandler();
    }


    /**
     * mp分页
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        return interceptor;
    }

    /**
     * 配置参数验证器为快速失败模式
     *
     * @return 参数验证器
     */
    @Bean
    public Validator getValidator() {
        ValidatorFactory factory = Validation.byProvider(HibernateValidator.class).configure().failFast(true).buildValidatorFactory();
        return factory.getValidator();
    }


}
