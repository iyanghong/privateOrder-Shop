package com.clever.config.mybatis;

import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import com.clever.util.SnowflakeIdGenerator;

/**
 * MybatisPlus自定义ID生成器
 *
 * @Author xixi
 * @Date 2023-12-19 17:39
 **/
public class CustomIdGenerator implements IdentifierGenerator {
    @Override
    public Long nextId(Object entity) {
        return SnowflakeIdGenerator.getInstance().nextId();
    }
}
