package com.clever.util.generate;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONUtil;
import cn.hutool.json.JSONConfig;

import com.clever.util.generate.config.GenerateConfig;
import com.clever.util.generate.entity.ColumnMeta;
import com.clever.util.generate.entity.FreeMaskerVariable;
import com.clever.util.generate.entity.TableMeta;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 这个类用于生成实体类
 *
 * @author: xixi
 * @create: 2023-12-14 08:40
 **/
public class GenerateEntity extends BaseGenerator {

    private static final Logger log = LoggerFactory.getLogger(GenerateEntity.class);


    public GenerateEntity(GenerateConfig config) {
        super(config);
    }

    /**
     * 执行生成实体类的操作
     *
     * @param tableMetaList 表列表
     * @param basePath      基础路径
     */
    @Override
    protected void handler(List<TableMeta> tableMetaList, String basePath) {
        if (tableMetaList.isEmpty()) return;
        // 遍历表元数据列表
        for (TableMeta tableMeta : tableMetaList) {
            FreeMaskerVariable freeMaskerVariable = new FreeMaskerVariable(config, tableMeta);
            render(freeMaskerVariable.getVariables(), "EntityTemplate.ftl", Paths.get(getBasePathOrCreate(basePath), toDTCamelCase(tableMeta.getTableName()) + ".java").toString());
        }
    }

    protected void handler1(List<TableMeta> tableMetaList, String packageName, String basePath) {
        config.setEntityPackageName(packageName);
        // 如果表元数据列表为空，则直接返回
        if (tableMetaList.isEmpty()) return;
        // 遍历表元数据列表
        for (TableMeta tableMeta : tableMetaList) {
            StringBuilder sb = new StringBuilder();
            // 添加包名
            sb.append("package ").append(packageName).append(";\n\n");
            // 添加Serializable接口
            sb.append("import java.io.Serializable;\n\n");

            // 如果表有主键列，则添加MyBatis Plus的注解
            if (tableMeta.getPrimaryKeyColumn() != null) {
                sb.append("import com.baomidou.mybatisplus.annotation.IdType;\n");
                sb.append("import com.baomidou.mybatisplus.annotation.TableId;\n");

            }
            // 如果有自动填充的列，则添加MyBatis Plus的注解
            if (tableMeta.isHasColumnInList(config.getAutoUpdateFillField()) || tableMeta.isHasColumnInList(config.getAutoInsertFillField())) {
                sb.append("import com.baomidou.mybatisplus.annotation.TableField;\n");
                sb.append("import com.baomidou.mybatisplus.annotation.FieldFill;\n");
            }
            // 是否存在需要判断不为空的字段
            if (tableMeta.isHasNeedNotBlankValidate()) {
                sb.append("import javax.validation.constraints.NotBlank;\n");
                sb.append("import javax.validation.constraints.NotNull;\n");
            }

            // 如果表有日期类型的列，则添加Date类
            if (tableMeta.isHasDateTypeColumn()) {
                sb.append("import java.util.Date;\n");
            }
            // 添加类注释
            sb.append("/**\n");
            if (!tableMeta.getTableComment().isEmpty()) {
                sb.append(" * ").append(tableMeta.getTableComment()).append("\n");
            }
            // 添加日期和作者
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            sb.append(" * ").append("@Author xixi").append("\n");
            sb.append(" * ").append("@Date ").append(dateFormat.format(new Date())).append("\n");
            sb.append(" */\n");
            // 添加类定义
            sb.append("public class ").append(tableNameToClassName(tableMeta.getTableName())).append(" implements Serializable {\n\n");
            // 遍历列元数据列表，为每个列生成字段
            for (ColumnMeta columnMeta : tableMeta.getColumns()) {
                // 如果列有注释，则添加字段注释
                if (!columnMeta.getColumnComment().isEmpty()) {
                    sb.append("\t/**\n");
                    sb.append("\t * ").append(columnMeta.getColumnComment()).append("\n");
                    sb.append("\t */\n");
                }
                // 如果列是主键，则添加MyBatis Plus的注解
                if ("PRI".equals(columnMeta.getColumnKey())) {
                    if ("String".equals(columnMeta.getJavaType())) {
                        sb.append("\t@TableId(type = IdType.ASSIGN_ID)\n");
                    } else {
                        sb.append("\t@TableId\n");
                    }
                }
                if (columnMeta.isHasNeedNotBlankValidate() && (!config.getAutoInsertFillField().contains(columnMeta.getColumnName()) && !config.getAutoUpdateFillField().contains(columnMeta.getColumnName()))) {
                    String name = columnMeta.getColumnComment();
                    if (StringUtils.isNotBlank(name)) {
                        name = name.replaceAll("：", ":");
                        String[] nameArr = name.split(":");
                        if (nameArr.length > 0) {
                            name = nameArr[0];
                        }
                    }
                    if ("String".equals(columnMeta.getJavaType())) {
                        sb.append("\t@NotBlank(message = \"").append(StringUtils.isNotBlank(name) ? name : columnMeta.getColumnName()).append("不能为空\")\n");
                    } else {
                        sb.append("\t@NotNull(message = \"").append(StringUtils.isNotBlank(name) ? name : columnMeta.getColumnName()).append("不能为空\")\n");
                    }
                }
                if (config.getAutoInsertFillField().contains(columnMeta.getColumnName())) {
                    sb.append("\t@TableField(value = \"").append(columnMeta.getColumnName()).append("\", fill = FieldFill.INSERT)\n");
                }
                if (config.getAutoUpdateFillField().contains(columnMeta.getColumnName())) {
                    sb.append("\t@TableField(value = \"").append(columnMeta.getColumnName()).append("\", fill = FieldFill.UPDATE)\n");
                }
                // 添加字段定义
                sb.append("\tprivate ").append(columnMeta.getJavaType()).append(" ").append(toXTCamelCase(columnMeta.getColumnName())).append(";\n");
            }
            sb.append("\n");
            // 为每个列生成getter和setter方法
            for (ColumnMeta columnMeta : tableMeta.getColumns()) {
                String columnName = columnMeta.getColumnName();
                String xtColumnName = toXTCamelCase(columnName);
                String dtColumnName = toDTCamelCase(columnName);
                // 如果列有注释，则添加方法注释
                if (!columnMeta.getColumnComment().isEmpty()) {
                    sb.append("\t/**\n");
                    sb.append("\t * ").append(columnMeta.getColumnComment()).append("\n");
                    sb.append("\t */\n");
                }
                // 添加getter方法
                sb.append("\tpublic ").append(columnMeta.getJavaType()).append(" get").append(dtColumnName).append("() {\n");
                sb.append("\t\treturn ").append(xtColumnName).append(";\n");
                sb.append("\t}\n\n");
                // 添加setter方法
                sb.append("\tpublic void set").append(dtColumnName).append("(").append(columnMeta.getJavaType()).append(" ").append(xtColumnName).append(") {\n");
                sb.append("\t\tthis.").append(xtColumnName).append(" = ").append(xtColumnName).append(";\n");
                sb.append("\t}\n\n");
            }
            // 结束类定义
            sb.append("}");
            // 获取文件全路径
            String filePath = Paths.get(getBasePathOrCreate(basePath), toDTCamelCase(tableMeta.getTableName()) + ".java").toString();
            // 写入文件
            try (FileWriter writer = new FileWriter(filePath)) {
                writer.write(sb.toString());
                log.info("The table's entity generate complete. table = '{}' filePath = {}", tableMeta.getTableName(), filePath);
            } catch (IOException e) {
                System.out.println("An error occurred while writing to the file: " + e.getMessage());
            }
        }
    }


}
