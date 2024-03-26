package com.clever.util.generate;

import com.clever.util.generate.config.GenerateConfig;
import com.clever.util.generate.entity.ColumnMeta;
import com.clever.util.generate.entity.FreeMaskerVariable;
import com.clever.util.generate.entity.TableMeta;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author xixi
 * @Date 2023-12-20 15:07
 **/
public class GenerateController extends BaseGenerator {
    private static final Logger log = LoggerFactory.getLogger(GenerateController.class);
    private final List<String> allowSearchColumnNames = Arrays.asList("_id", "name", "email", "phone", "status", "sex", "gender", "account", "type", "code", "host", "title");

    public GenerateController(GenerateConfig config) {
        super(config);
    }

    public GenerateController(GenerateConfig config, String entityPackageName, String servicePackageName) {
        super(config);
        config.setEntityPackageName(entityPackageName);
        config.setServicePackageName(servicePackageName);
    }

    /**
     * 生成控制器
     *
     * @param tableMetaList 表列表
     * @param basePath      基础路径
     */
    @Override
    protected void handler(List<TableMeta> tableMetaList, String basePath) {
        // 遍历表元数据列表
        for (TableMeta tableMeta : tableMetaList) {
            FreeMaskerVariable variables = new FreeMaskerVariable(config, tableMeta);
            List<ColumnMeta> getListByForeignKeyList = tableMeta.getColumns().stream().filter(it -> it.getColumnName().endsWith("_id") || it.getColumnName().equals(config.getCreatorFieldName())).collect(Collectors.toList());
            List<ColumnMeta> getByUniqueColumns = tableMeta.getColumns().stream().filter(it -> "UNI".equalsIgnoreCase(it.getColumnKey())).collect(Collectors.toList());
            List<ColumnMeta> allowSearchColumns = tableMeta.getColumns().stream().filter(it -> allowSearchColumnNames.stream().anyMatch(it.getColumnName()::endsWith)).collect(Collectors.toList());
            variables.setVariable("allowSearchColumns", variables.resolveColumnList(allowSearchColumns));
            variables.setVariable("getByUniqueColumns", variables.resolveColumnList(getByUniqueColumns));
            variables.setVariable("getListByForeignKeyList", variables.resolveColumnList(getListByForeignKeyList));
            // 获取文件路径
            String filePath = Paths.get(getBasePathOrCreate(basePath), tableMeta.getUpperCamelCaseName() + "Controller.java").toString();
            render(variables.getVariables(), "ControllerTemplate.ftl", filePath);
        }
    }

    /**
     * 生成service接口和实现类
     *
     * @param tableMetaList 表列表
     * @param packageName   包名
     * @param basePath      基础路径
     */
    protected void handler1(List<TableMeta> tableMetaList, String packageName, String basePath) {
        // 遍历表元数据列表
        for (TableMeta tableMeta : tableMetaList) {
            // 初始化字符串构建器
            StringBuilder stringBuilder = initBuilder(tableMeta, packageName);
            // 获取表的主键列
            ColumnMeta primaryKeyColumn = tableMeta.getPrimaryKeyColumn();

            // 构建分页查询函数
            buildSelectPage(stringBuilder, tableMeta);
            buildSelectById(stringBuilder, tableMeta);
            for (ColumnMeta columnMeta : tableMeta.getColumns()) {
                if (columnMeta.getColumnName().endsWith("_id")) {
                    buildSelectByForeignKey(stringBuilder, tableMeta, columnMeta);
                }
            }
            buildSave(stringBuilder, tableMeta);
            buildDelete(stringBuilder, tableMeta);

            stringBuilder.append("\n}\n");

            // 获取文件路径
            String filePath = Paths.get(getBasePathOrCreate(basePath), tableMeta.getUpperCamelCaseName() + "Controller.java").toString();
            // 写入文件
            try (FileWriter writer = new FileWriter(filePath)) {
                // 将构建器写入文件
                writer.write(stringBuilder.toString());
                // 打印文件生成完成信息
                log.info("The table's controller class generate complete. table = '{}' filePath = {}", tableMeta.getTableName(), filePath);
            } catch (IOException e) {
                System.out.println("An error occurred while writing to the file: " + e.getMessage());
            }

        }
    }

    private StringBuilder initBuilder(TableMeta tableMeta, String packageName) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("package ").append(packageName).append(";\n\n");
        stringBuilder.append("import com.baomidou.mybatisplus.extension.plugins.pagination.Page;\n");
        stringBuilder.append("import com.clever.util.SpringUtil;\n");
        stringBuilder.append("import com.clever.annotation.Auth;\n");
        stringBuilder.append("import com.clever.annotation.AuthGroup;\n");
        stringBuilder.append("import com.clever.bean.model.OnlineUser;\n");
        stringBuilder.append("import com.clever.bean.model.Result;\n");
        stringBuilder.append("import java.util.List;\n");
        stringBuilder.append(String.format("import %s.%s;\n", config.getEntityPackageName(), tableMeta.getUpperCamelCaseName()));
        stringBuilder.append(String.format("import %s.%sService;\n", config.getServicePackageName(), tableMeta.getUpperCamelCaseName()));
        stringBuilder.append("import org.springframework.validation.annotation.Validated;\n");
        stringBuilder.append("import org.springframework.web.bind.annotation.*;\n");
        stringBuilder.append("\nimport javax.annotation.Resource;\n");

        // 添加注释
        stringBuilder.append("\n/**\n");
        stringBuilder.append(String.format(" * %s接口\n", StringUtils.isNotBlank(tableMeta.getTableComment()) ? tableMeta.getTableComment() : tableMeta.getUpperCamelCaseName()));
        stringBuilder.append(" *\n");
        stringBuilder.append(" * @Author xixi\n");
        stringBuilder.append(String.format(" * @Date %s\n", new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date())));
        stringBuilder.append(" */\n");

        stringBuilder.append("@RestController\n");
        stringBuilder.append("@RequestMapping(\"").append("/").append(tableMeta.getLowerCamelCaseName()).append("\")\n");

        stringBuilder.append("@AuthGroup(name = \"").append(tableMeta.getCommentOrName()).append("模块\", description = \"").append(tableMeta.getCommentOrName()).append("模块权限组\")\n");
        stringBuilder.append(String.format("public class %sController {\n\n", tableMeta.getUpperCamelCaseName()));

        stringBuilder.append(String.format("    @Resource\n    private %sService %sService;\n\n", tableMeta.getUpperCamelCaseName(), tableMeta.getXtName()));
        return stringBuilder;
    }

    private void buildSelectPage(StringBuilder stringBuilder, TableMeta tableMeta) {
        // 创建一个TreeMap，用于存储方法注释参数
        LinkedHashMap<String, String> params = new LinkedHashMap<>();
        // 将页码和每页记录数放入注释参数中
        params.put("pageNumber", "页码");
        params.put("pageSize", "每页记录数");
        // 创建一个ArrayList对象，用于存储允许搜索的列
        List<ColumnMeta> allowSearchColumns = new ArrayList<>();
        // 遍历表中的每一列
        for (ColumnMeta columnMeta : tableMeta.getColumns()) {
            for (String allowSearchColumnName : allowSearchColumnNames) {
                // 如果允许搜索的列名列表中包含当前列名
                if (columnMeta.getColumnName().endsWith(allowSearchColumnName)) {
                    // 将当前列添加到允许搜索的列列表中
                    allowSearchColumns.add(columnMeta);
                    // 将当前列名和注释放入注释参数中
                    params.put(toXTCamelCase(columnMeta.getColumnName()), columnMeta.getColumnComment());
                }
            }
        }
        // 获取函数注释
        String functionComment = getFunctionComment(String.format("分页查询%s列表", tableMeta.getTableComment()), params, "当前页数据");
        stringBuilder.append("\n").append(functionComment);
        stringBuilder.append("\t@GetMapping(\"/page/{pageNumber}/{pageSize}\")\n");

        String name = StringUtils.isNotBlank(tableMeta.getTableComment()) ? tableMeta.getTableComment() : tableMeta.getTableName();
        stringBuilder.append(String.format("\t@Auth(value = \"%s.%s.page\", name = \"%s分页\", description = \"%s分页接口\")\n", config.getAppName(), tableMeta.getXtName(), name, name));
        stringBuilder.append(String.format("\tpublic Result<Page<%s>> selectPage(@PathVariable(\"pageNumber\") Integer pageNumber, @PathVariable(\"pageSize\") Integer pageSize", tableMeta.getUpperCamelCaseName()));
        // 遍历允许搜索的列
        for (ColumnMeta allowSearchColumn : allowSearchColumns) {
            stringBuilder.append(String.format(", %s %s", allowSearchColumn.getJavaType(), allowSearchColumn.getLowerCamelCaseName()));
        }
        stringBuilder.append(") {\n");
        stringBuilder.append(String.format("\t\treturn new Result<>(%sService.selectPage(pageNumber, pageSize", tableMeta.getXtName()));
        // 遍历允许搜索的列
        for (ColumnMeta allowSearchColumn : allowSearchColumns) {
            // 将列名和注释放入注释参数中
            stringBuilder.append(", ").append(allowSearchColumn.getLowerCamelCaseName());
        }
        stringBuilder.append("), \"分页数据查询成功\");\n");
        stringBuilder.append("\t}\n");
    }


    private void buildSelectById(StringBuilder stringBuilder, TableMeta tableMeta) {
        ColumnMeta primaryKeyColumn = tableMeta.getPrimaryKeyColumn();
        if (primaryKeyColumn == null) return;
        LinkedHashMap<String, String> columnMap = new LinkedHashMap<>();
        columnMap.put(primaryKeyColumn.getColumnName(), primaryKeyColumn.getColumnComment());
        String functionComment = getFunctionComment(String.format("根据%s获取%s信息", primaryKeyColumn.getCommentOrName(), tableMeta.getCommentOrName()), columnMap, String.format("%s信息", tableMeta.getCommentOrName()));
        stringBuilder.append("\n").append(functionComment);
        stringBuilder.append("\t@GetMapping(\"/get/{").append(primaryKeyColumn.getLowerCamelCaseName()).append("}\")\n");
        stringBuilder.append(String.format("\tpublic Result<%s> selectById(@PathVariable(\"%s\") %s %s) {\n", tableMeta.getUpperCamelCaseName(), primaryKeyColumn.getLowerCamelCaseName(), primaryKeyColumn.getJavaType(), primaryKeyColumn.getLowerCamelCaseName()));
        stringBuilder.append(String.format("\t\treturn new Result<>(%sService.selectById(%s), \"查询成功\");\n", tableMeta.getLowerCamelCaseName(), primaryKeyColumn.getLowerCamelCaseName()));
        stringBuilder.append("\t}\n");
    }

    private void buildSelectByForeignKey(StringBuilder stringBuilder, TableMeta tableMeta, ColumnMeta columnMeta) {
        // 创建一个LinkedHashMap，用于存储列名和列注释
        LinkedHashMap<String, String> columnMap = new LinkedHashMap<>();
        // 将列名和列注释添加到LinkedHashMap中
        columnMap.put(columnMeta.getLowerCamelCaseName(), columnMeta.getCommentOrName());
        // 获取函数注释
        String functionComment = getFunctionComment(String.format("根据%s获取%s列表", columnMeta.getCommentOrName(), tableMeta.getCommentOrName()), columnMap, String.format("%s列表", tableMeta.getCommentOrName()));
        stringBuilder.append("\n").append(functionComment);
        stringBuilder.append("\t@GetMapping(\"/getListBy").append(columnMeta.getUpperCamelCaseName()).append("/{").append(columnMeta.getLowerCamelCaseName()).append("}\")\n");
        stringBuilder.append("\t@Auth(value = \"").append(config.getAppName()).append(".").append(tableMeta.getXtName()).append(".getBy").append(columnMeta.getUpperCamelCaseName()).append("\", name = \"根据").append(columnMeta.getColumnName()).append("获取").append(tableMeta.getCommentOrName()).append("列表\", description = \"").append(tableMeta.getCommentOrName()).append("列表接口\")\n");
        stringBuilder.append("\tpublic Result<List<").append(tableMeta.getUpperCamelCaseName()).append(">> selectListBy").append(columnMeta.getUpperCamelCaseName()).append("(@PathVariable(\"").append(columnMeta.getLowerCamelCaseName()).append("\") ").append(columnMeta.getJavaType()).append(" ").append(columnMeta.getLowerCamelCaseName()).append(") {\n");
        stringBuilder.append(String.format("\t\treturn new Result<>(%sService.selectListBy%s(%s), \"查询成功\");\n", tableMeta.getLowerCamelCaseName(), columnMeta.getUpperCamelCaseName(), columnMeta.getLowerCamelCaseName()));
        stringBuilder.append("\t}\n");
    }

    private void buildSave(StringBuilder stringBuilder, TableMeta tableMeta) {
        if (tableMeta.getPrimaryKeyColumn() == null) return;
        LinkedHashMap<String, String> saveParamMap = new LinkedHashMap<>();
        saveParamMap.put(tableMeta.getLowerCamelCaseName(), String.format("%s实体信息", tableMeta.getCommentOrName()));
        String functionComment = getFunctionComment(String.format("保存%s信息", tableMeta.getCommentOrName()), saveParamMap, "void");
        stringBuilder.append("\n").append(functionComment);
        stringBuilder.append("\t@PostMapping(\"/save\")\n");
        stringBuilder.append("\t@Auth(value = \"").append(config.getAppName()).append(".").append(tableMeta.getXtName()).append(".save\", name = \"保存").append(tableMeta.getCommentOrName()).append("\", description = \"保存").append(tableMeta.getCommentOrName()).append("信息接口\")\n");
        stringBuilder.append(String.format("\tpublic Result<String> save(@Validated %s %s) {\n", tableMeta.getUpperCamelCaseName(), tableMeta.getLowerCamelCaseName()));
        stringBuilder.append("\t\tOnlineUser onlineUser = SpringUtil.getOnlineUser();\n");
        stringBuilder.append(String.format("\t\t%sService.save(%s, onlineUser);\n", tableMeta.getLowerCamelCaseName(), tableMeta.getLowerCamelCaseName()));
        stringBuilder.append("\t\treturn Result.ofSuccess(\"保存成功\");\n");
        stringBuilder.append("\t}\n");
    }

    private void buildDelete(StringBuilder stringBuilder, TableMeta tableMeta) {
        ColumnMeta primaryKeyColumn = tableMeta.getPrimaryKeyColumn();
        if (primaryKeyColumn == null) return;
        LinkedHashMap<String, String> deleteParamMap = new LinkedHashMap<>();
        deleteParamMap.put(primaryKeyColumn.getLowerCamelCaseName(), primaryKeyColumn.getCommentOrLowerCamelCaseName());
        String functionComment = getFunctionComment(String.format("根据%s删除%s信息", primaryKeyColumn.getCommentOrName(), tableMeta.getCommentOrName()), deleteParamMap, "void");
        stringBuilder.append("\n").append(functionComment);
        stringBuilder.append("\t@DeleteMapping(\"/delete/{").append(primaryKeyColumn.getLowerCamelCaseName()).append("}\")\n");
        stringBuilder.append("\t@Auth(value = \"").append(config.getAppName()).append(".").append(tableMeta.getXtName()).append(".delete\", name = \"删除").append(tableMeta.getCommentOrName()).append("\", description = \"删除").append(tableMeta.getCommentOrName()).append("信息接口\")\n");
        stringBuilder.append(String.format("\tpublic Result<String> delete(@PathVariable(\"%s\") %s %s) {\n", primaryKeyColumn.getLowerCamelCaseName(), primaryKeyColumn.getJavaType(), primaryKeyColumn.getLowerCamelCaseName()));
        stringBuilder.append("\t\tOnlineUser onlineUser = SpringUtil.getOnlineUser();\n");
        stringBuilder.append(String.format("\t\t%sService.delete(%s, onlineUser);\n", tableMeta.getLowerCamelCaseName(), primaryKeyColumn.getLowerCamelCaseName()));
        stringBuilder.append("\t\treturn Result.ofSuccess(\"删除成功\");\n");
        stringBuilder.append("\t}\n");
    }


}
