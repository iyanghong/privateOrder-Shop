package com.clever.util.generate.entity;

import com.clever.util.generate.config.GenerateConfig;
import org.apache.commons.lang.StringUtils;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Author xixi
 * @Date 2023-12-23 21:19
 **/
public class FreeMaskerVariable {
    private final Map<String, Object> variables = new HashMap<>();
    private final GenerateConfig config;

    public FreeMaskerVariable(GenerateConfig config) {
        this.config = config;
        resolveConfig(this.variables, config);
    }

    public FreeMaskerVariable(GenerateConfig config, TableMeta tableMeta) {
        this.config = config;
        resolveConfig(this.variables, config);
        resolveTable(this.variables, tableMeta);

    }

    public void resolveConfig(Map<String, Object> map, GenerateConfig config) {
        map.put("author", "xixi");
        map.put("date", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));

        map.put("appName", config.getAppName());
        map.put("DB_URL", config.DB_URL);
        map.put("DB_DATABASE", config.DB_DATABASE);
        map.put("DB_USERNAME", config.DB_USERNAME);
        map.put("entityPackageName", config.getEntityPackageName());
        map.put("mapperPackageName", config.getMapperPackageName());
        map.put("servicePackageName", config.getServicePackageName());
        map.put("controllerPackageName", config.getControllerPackageName());
        map.put("idFieldName", config.getIdFieldName());
        map.put("creatorFieldName", config.getCreatorFieldName());
        map.put("modifierFieldName", config.getModifierFieldName());
        map.put("createTimeFieldName", config.getCreateTimeFieldName());
        map.put("modifyTimeFieldName", config.getModifyTimeFieldName());
        map.put("deleteFlagFieldName", config.getDeleteFlagFieldName());
    }

    public void resolveTable(Map<String, Object> map, TableMeta tableMeta) {
        map.put("tableSchema", tableMeta.getTableSchema());
        map.put("tableName", tableMeta.getTableName());
        map.put("tableComment", tableMeta.getTableComment());
        map.put("lowerCamelCaseName", tableMeta.getLowerCamelCaseName());
        map.put("upperCamelCaseName", tableMeta.getUpperCamelCaseName());
        map.put("isHasDateTypeColumn", tableMeta.isHasDateTypeColumn());
        map.put("isHasNeedNotBlankValidate", tableMeta.isHasNeedNotBlankValidate());
        map.put("isHasNeedNotNullValidate", tableMeta.isHasNeedNotNullValidate());
        map.put("primaryKeyColumn", tableMeta.getPrimaryKeyColumn());
        map.put("commentOrName", tableMeta.getCommentOrName());
        map.put("commentOrUpperCamelCaseName", tableMeta.getCommentOrUpperCamelCaseName());
        map.put("commentOrLowerCamelCaseName", tableMeta.getCommentOrLowerCamelCaseName());
        boolean isHasAutoInsertColumn = false;
        boolean isHasAutoUpdateColumn = false;
        for (ColumnMeta columnMeta : tableMeta.getColumns()) {
            if (config.getAutoInsertFillField().contains(columnMeta.getColumnName())) {
                isHasAutoInsertColumn = true;
            }
            if (config.getAutoUpdateFillField().contains(columnMeta.getColumnName())) {
                isHasAutoUpdateColumn = true;
            }
        }
        map.put("isHasAutoInsertColumn", isHasAutoInsertColumn);
        map.put("isHasAutoUpdateColumn", isHasAutoUpdateColumn);
        List<Map<String, Object>> columns = resolveColumnList(tableMeta.getColumns());
        map.put("columns", columns);
    }

    public List<Map<String, Object>> resolveColumnList(List<ColumnMeta> columnMetaList) {
        List<Map<String, Object>> columns = new ArrayList<>();
        for (ColumnMeta columnMeta : columnMetaList) {
            columns.add(resolveColumn(columnMeta));
        }
        return columns;
    }

    public Map<String, Object> resolveColumn(ColumnMeta columnMeta) {
        Map<String, Object> column = new HashMap<>();
        column.put("tableSchema", columnMeta.getTableSchema());
        column.put("tableName", columnMeta.getTableName());
        column.put("columnName", columnMeta.getColumnName());
        column.put("ordinalPosition", columnMeta.getOrdinalPosition());
        column.put("columnDefault", columnMeta.getColumnDefault());
        column.put("nullable", columnMeta.isNullable());
        column.put("dataType", columnMeta.getDataType());
        column.put("characterMaximumLength", columnMeta.getCharacterMaximumLength());
        column.put("columnKey", columnMeta.getColumnKey());
        column.put("columnComment", columnMeta.getColumnComment());
        column.put("javaType", columnMeta.getJavaType());
        column.put("lowerCamelCaseName", columnMeta.getLowerCamelCaseName());
        column.put("upperCamelCaseName", columnMeta.getUpperCamelCaseName());
        column.put("isHasNeedNotBlankValidate", columnMeta.isHasNeedNotBlankValidate());
        column.put("commentOrName", columnMeta.getCommentOrName());
        column.put("commentOrUpperCamelCaseName", columnMeta.getCommentOrUpperCamelCaseName());
        column.put("commentOrLowerCamelCaseName", columnMeta.getCommentOrLowerCamelCaseName());
        column.put("isAutoInsertFill", config.getAutoInsertFillField().contains(columnMeta.getColumnName()));
        column.put("isAutoUpdateFill", config.getAutoUpdateFillField().contains(columnMeta.getColumnName()));
        String nameText = StringUtils.isNotBlank(columnMeta.getColumnComment()) ? columnMeta.getColumnComment() : "";
        if (StringUtils.isNotBlank(nameText)) {
            nameText = nameText.replaceAll("：", ":");
            String[] nameArr = nameText.split(":");
            if (nameArr.length > 0) {
                nameText = nameArr[0];
            }
        }
        column.put("nameText", StringUtils.isNotBlank(nameText) ? nameText : columnMeta.getCommentOrName());
        return column;
    }

    public Map<String, Object> getVariables() {
        return variables;
    }

    public void setVariable(String key, Object value) {
        this.variables.put(key, value);
    }
}
