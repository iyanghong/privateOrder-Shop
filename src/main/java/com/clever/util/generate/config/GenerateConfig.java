package com.clever.util.generate.config;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * GenerateConfig类用于配置数据库连接和包名。
 *
 * @Author xixi
 * @Date 2023-12-18 11:37
 **/
public class GenerateConfig {
    private String appName = "";
    // 数据库URL
    public final String DB_URL;
    // 数据库名称
    public String DB_DATABASE;
    // 数据库用户名
    public final String DB_USERNAME;
    // 数据库密码
    public final String DB_PASSWORD;

    // 实体类包名
    private String entityPackageName;

    // 映射类包名
    private String mapperPackageName;

    // 服务类包名
    private String servicePackageName;
    // 控制器包名
    private String controllerPackageName;

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
     * 构造函数，初始化数据库URL、用户名和密码。
     * 同时从数据库URL中解析出数据库名称。
     *
     * @param DB_URL      数据库URL
     * @param DB_USERNAME 数据库用户名
     * @param DB_PASSWORD 数据库密码
     */
    public GenerateConfig(String DB_URL, String DB_USERNAME, String DB_PASSWORD) {
        this.DB_URL = DB_URL;
        this.DB_USERNAME = DB_USERNAME;
        this.DB_PASSWORD = DB_PASSWORD;
        int lastSlashIndex = DB_URL.lastIndexOf('/');

        if (lastSlashIndex != -1 && lastSlashIndex < DB_URL.length() - 1) {
            this.DB_DATABASE = DB_URL.substring(lastSlashIndex + 1);
        }
    }

    public String getServicePackageName() {
        return servicePackageName;
    }

    public void setServicePackageName(String servicePackageName) {
        this.servicePackageName = servicePackageName;
    }

    /**
     * 获取实体类包名
     *
     * @return 实体类包名
     */
    public String getEntityPackageName() {
        return entityPackageName;
    }

    /**
     * 设置实体类包名
     *
     * @param entityPackageName 实体类包名
     */
    public void setEntityPackageName(String entityPackageName) {
        this.entityPackageName = entityPackageName;
    }

    /**
     * 获取映射类包名
     *
     * @return 映射类包名
     */
    public String getMapperPackageName() {
        return mapperPackageName;
    }

    /**
     * 设置映射类包名
     *
     * @param mapperPackageName 映射类包名
     */
    public void setMapperPackageName(String mapperPackageName) {
        this.mapperPackageName = mapperPackageName;
    }

    public String getIdFieldName() {
        return idFieldName;
    }

    public void setIdFieldName(String idFieldName) {
        this.idFieldName = idFieldName;
    }

    public String getCreatorFieldName() {
        return creatorFieldName;
    }

    public void setCreatorFieldName(String creatorFieldName) {
        this.creatorFieldName = creatorFieldName;
    }

    public String getModifierFieldName() {
        return modifierFieldName;
    }

    public void setModifierFieldName(String modifierFieldName) {
        this.modifierFieldName = modifierFieldName;
    }

    public String getCreateTimeFieldName() {
        return createTimeFieldName;
    }

    public void setCreateTimeFieldName(String createTimeFieldName) {
        this.createTimeFieldName = createTimeFieldName;
    }

    public String getModifyTimeFieldName() {
        return modifyTimeFieldName;
    }

    public void setModifyTimeFieldName(String modifyTimeFieldName) {
        this.modifyTimeFieldName = modifyTimeFieldName;
    }

    public String getDeleteFlagFieldName() {
        return deleteFlagFieldName;
    }

    public void setDeleteFlagFieldName(String deleteFlagFieldName) {
        this.deleteFlagFieldName = deleteFlagFieldName;
    }

    public List<String> getAutoInsertFillField() {
        return Collections.singletonList(this.creatorFieldName);
    }

    public List<String> getAutoUpdateFillField() {
        return Collections.singletonList(this.modifierFieldName);
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getControllerPackageName() {
        return controllerPackageName;
    }

    public void setControllerPackageName(String controllerPackageName) {
        this.controllerPackageName = controllerPackageName;
    }

    /**
     * 获取自动填充的字段
     *
     * @return 获取自动填充的字段
     */
    public List<String> getAutoFillField() {
        return Arrays.asList(this.creatorFieldName, this.modifierFieldName, this.createTimeFieldName, this.modifyTimeFieldName, this.deleteFlagFieldName);
    }
}