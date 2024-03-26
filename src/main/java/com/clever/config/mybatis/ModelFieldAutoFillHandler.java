package com.clever.config.mybatis;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.clever.bean.model.OnlineUser;
import com.clever.config.ModelConfig;
import com.clever.util.SnowflakeIdGenerator;
import com.clever.util.SpringUtil;
import org.apache.ibatis.reflection.MetaObject;

import javax.annotation.Resource;

/**
 * 字段插入更新的时候填充必要数据
 *
 * @Author xixi
 * @Date 2023-12-19 14:50
 **/
public class ModelFieldAutoFillHandler implements MetaObjectHandler {

    @Resource
    private ModelConfig modelConfig;

    @Override
    public void insertFill(MetaObject metaObject) {
        // 判断metaObject是否有setter方法，如果有，则获取id的值
        if (metaObject.hasSetter(modelConfig.getIdFieldName())) {
            Object value = metaObject.getValue(modelConfig.getIdFieldName());
            if (value == null) {
                // 如果没有id，则使用SnowflakeIdGenerator生成id
                this.strictInsertFill(metaObject, modelConfig.getIdFieldName(), String.class, SnowflakeIdGenerator.getSnowflakeNextId());
            }
        }
        if (metaObject.hasSetter(modelConfig.getCreatorFieldName())) {
            Object value = metaObject.getValue(modelConfig.getCreatorFieldName());
            OnlineUser onlineUser = SpringUtil.getOnlineUserIfExist();
            if (value == null && onlineUser != null) {
                // 如果没有id，则使用SnowflakeIdGenerator生成id
                this.strictInsertFill(metaObject, modelConfig.getCreatorFieldName(), String.class, onlineUser.getId());
            }
        }
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        if (metaObject.hasSetter(modelConfig.getModifierFieldName())) {
            Object value = metaObject.getValue(modelConfig.getModifierFieldName());
            OnlineUser onlineUser = SpringUtil.getOnlineUserIfExist();
            if (value == null && onlineUser != null) {
                // 如果没有id，则使用SnowflakeIdGenerator生成id
                this.strictInsertFill(metaObject, modelConfig.getModifierFieldName(), String.class, onlineUser.getId());
            }
        }
    }


}
