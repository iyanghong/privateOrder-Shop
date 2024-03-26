package com.clever.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;

/**
 * @Author xixi
 * @Date 2023-12-19 15:09
 **/
@Configuration
@EnableCaching
public class ModelConfig {
    /**
     * id字段名
     */
    private String idFieldName = "id";
    /**
     * 创建者字段名
     */
    private String creatorFieldName = "creator";
    /**
     * 修改者字段名
     */
    private String modifierFieldName = "modifier";
    /**
     * 创建时间字段名
     */
    private String createTimeFieldName = "created_at";
    /**
     * 修改时间字段名
     */
    private String modifyTimeFieldName = "updated_at";
    /**
     * 删除标记字段名
     */
    private String deleteFlagFieldName = "deleted_at";


    /**
     * 获取id字段名
     *
     * @return id字段名
     */
    public String getIdFieldName() {
        return idFieldName;
    }

    /**
     * 设置id字段名
     *
     * @param idFieldName id字段名
     */
    public void setIdFieldName(String idFieldName) {
        this.idFieldName = idFieldName;
    }

    /**
     * 获取创建者字段名
     *
     * @return 创建者字段名
     */
    public String getCreatorFieldName() {
        return creatorFieldName;
    }

    /**
     * 设置创建者字段名
     *
     * @param creatorFieldName 创建者字段名
     */
    public void setCreatorFieldName(String creatorFieldName) {
        this.creatorFieldName = creatorFieldName;
    }

    /**
     * 获取修改者字段名
     *
     * @return 修改者字段名
     */
    public String getModifierFieldName() {
        return modifierFieldName;
    }

    /**
     * 设置修改者字段名
     *
     * @param modifierFieldName 修改者字段名
     */
    public void setModifierFieldName(String modifierFieldName) {
        this.modifierFieldName = modifierFieldName;
    }

    /**
     * 获取创建时间字段名
     *
     * @return 创建时间字段名
     */
    public String getCreateTimeFieldName() {
        return createTimeFieldName;
    }

    /**
     * 设置创建时间字段名
     *
     * @param createTimeFieldName 创建时间字段名
     */
    public void setCreateTimeFieldName(String createTimeFieldName) {
        this.createTimeFieldName = createTimeFieldName;
    }

    /**
     * 获取修改时间字段名
     *
     * @return 修改时间字段名
     */
    public String getModifyTimeFieldName() {
        return modifyTimeFieldName;
    }

    /**
     * 设置修改时间字段名
     *
     * @param modifyTimeFieldName 修改时间字段名
     */
    public void setModifyTimeFieldName(String modifyTimeFieldName) {
        this.modifyTimeFieldName = modifyTimeFieldName;
    }

    /**
     * 获取删除标记字段名
     *
     * @return 删除标记字段名
     */
    public String getDeleteFlagFieldName() {
        return deleteFlagFieldName;
    }

    /**
     * 设置删除标记字段名
     *
     * @param deleteFlagFieldName 删除标记字段名
     */
    public void setDeleteFlagFieldName(String deleteFlagFieldName) {
        this.deleteFlagFieldName = deleteFlagFieldName;
    }
}
