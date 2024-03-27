package com.clever.util.generate;

import com.clever.util.generate.config.GenerateConfig;
import com.clever.util.generate.entity.ColumnMeta;
import com.clever.util.generate.entity.TableMeta;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author xixi
 * @Date 2023-12-18 11:36
 **/
public class BaseGenerator implements IGenerator {
    private static final Logger log = LoggerFactory.getLogger(BaseGenerator.class);
    protected GenerateConfig config;


    public BaseGenerator(GenerateConfig config) {
        this.config = config;
    }

    public void generate(String basePath) {
        List<TableMeta> tableMetaList = getTableMetaList("");
        this.handler(tableMetaList, basePath);
    }

    /**
     * 生成
     *
     * @param tableName 表名
     * @param basePath  基础路径
     */
    public void generate(String tableName, String basePath) {
        List<TableMeta> tableMetaList = new ArrayList<>();
        if (StringUtils.isNotBlank(tableName)) {
            for (String tblName : tableName.split(",")) {
                tableMetaList.addAll(getTableMetaList(tblName));
            }
        } else {
            tableMetaList = getTableMetaList(tableName);
        }
        this.handler(tableMetaList, basePath);
    }


    /**
     * 执行生成的操作
     *
     * @param tableMetaList 表列表
     * @param basePath      基础路径
     */
    protected void handler(List<TableMeta> tableMetaList, String basePath) {

    }

    /**
     * 获取表元数据列表
     *
     * @param tableName 表名
     * @return 表元数据列表
     */
    public List<TableMeta> getTableMetaList(String tableName) {
        List<TableMeta> tableMetaList = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(config.DB_URL, config.DB_USERNAME, config.DB_PASSWORD)) {
            PreparedStatement tablePreparedStatement = null;
            if (tableName != null && !tableName.isEmpty()) {
                // 查询指定表的元数据
                tablePreparedStatement = connection.prepareStatement("select TABLE_SCHEMA,TABLE_NAME,TABLE_COMMENT from information_schema.TABLES where TABLE_COMMENT != 'VIEW' and TABLE_SCHEMA = ? and TABLE_NAME = ? group by TABLE_SCHEMA,TABLE_NAME,TABLE_COMMENT");
                tablePreparedStatement.setString(1, config.DB_DATABASE);
                tablePreparedStatement.setString(2, tableName);
            } else {
                // 查询指定数据库的元数据
                tablePreparedStatement = connection.prepareStatement("select TABLE_SCHEMA,TABLE_NAME,TABLE_COMMENT from information_schema.TABLES where TABLE_COMMENT != 'VIEW' and TABLE_SCHEMA = ? group by TABLE_SCHEMA,TABLE_NAME,TABLE_COMMENT");
                tablePreparedStatement.setString(1, config.DB_DATABASE);
            }
            ResultSet resultSet = tablePreparedStatement.executeQuery();
            while (resultSet.next()) {
                TableMeta tableMeta = new TableMeta(resultSet.getString("TABLE_SCHEMA"), resultSet.getString("TABLE_NAME"), resultSet.getString("TABLE_COMMENT"));
                tableMeta.setUpperCamelCaseName(toDTCamelCase(tableMeta.getTableName()));
                tableMeta.setLowerCamelCaseName(toXTCamelCase(tableMeta.getTableName()));
                List<ColumnMeta> columnMetaList = new ArrayList<>();
                PreparedStatement preparedStatement = connection.prepareStatement("select TABLE_SCHEMA,TABLE_NAME,COLUMN_NAME,ORDINAL_POSITION,COLUMN_DEFAULT,IS_NULLABLE,DATA_TYPE,CHARACTER_MAXIMUM_LENGTH,COLUMN_KEY,COLUMN_COMMENT from information_schema.COLUMNS where TABLE_SCHEMA = ? and TABLE_NAME = ? order by ORDINAL_POSITION");
                // 设置参数值
                preparedStatement.setString(1, tableMeta.getTableSchema());
                preparedStatement.setString(2, tableMeta.getTableName());
                ResultSet columnResultSet = preparedStatement.executeQuery();
                while (columnResultSet.next()) {
                    ColumnMeta columnMeta = new ColumnMeta();
                    columnMeta.setTableSchema(columnResultSet.getString("TABLE_SCHEMA"));
                    columnMeta.setTableName(columnResultSet.getString("TABLE_NAME"));
                    columnMeta.setColumnName(columnResultSet.getString("COLUMN_NAME"));
                    columnMeta.setOrdinalPosition(columnResultSet.getInt("ORDINAL_POSITION"));
                    columnMeta.setColumnDefault(columnResultSet.getString("COLUMN_DEFAULT"));
                    columnMeta.setNullable(columnResultSet.getString("IS_NULLABLE").equals("YES"));
                    columnMeta.setDataType(columnResultSet.getString("DATA_TYPE"));
                    columnMeta.setColumnKey(columnResultSet.getString("COLUMN_KEY"));

                    String comment = columnResultSet.getString("COLUMN_COMMENT");
                    if (StringUtils.isNotBlank(comment)) {
                        comment = comment.replaceAll("\r|\n", ",").replaceAll(",,", ",").replaceAll("：,", "：").replaceAll(":,", ":").replaceAll("：", ":");
                        if (!StringUtils.contains(comment, ":") && StringUtils.contains(comment, ",")) {
                            comment = comment.replace(",", ":");
                        }
                    }
                    columnMeta.setColumnComment(comment);
                    columnMeta.setJavaType(mapColumnType(columnMeta.getDataType()));

                    columnMeta.setLowerCamelCaseName(toXTCamelCase(columnMeta.getColumnName()));
                    columnMeta.setUpperCamelCaseName(toDTCamelCase(columnMeta.getColumnName()));

                    columnMetaList.add(columnMeta);
                }
                tableMeta.setColumns(columnMetaList);
                tableMetaList.add(tableMeta);
            }
        } catch (SQLException e) {
            log.error("getTableMetaList method SQLException:", e);
        }
        return tableMetaList;
    }

    /**
     * 映射列类型
     *
     * @param columnType 列类型
     * @return Java类型
     */
    private static String mapColumnType(String columnType) {
        // Map database column types to Java types
        if (columnType.equalsIgnoreCase("INT") || columnType.equalsIgnoreCase("TINYINT") || columnType.equalsIgnoreCase("SMALLINT") || columnType.equalsIgnoreCase("MEDIUMINT") || columnType.equalsIgnoreCase("INTEGER")) {
            return "Integer";
        } else if (columnType.equalsIgnoreCase("BIGINT")) {
            return "Long";
        } else if (columnType.equalsIgnoreCase("VARCHAR") || columnType.equalsIgnoreCase("TEXT")) {
            return "String";
        } else if (columnType.equalsIgnoreCase("DATE") || columnType.equalsIgnoreCase("DATETIME")) {
            return "Date";
        } else if (columnType.equalsIgnoreCase("decimal")) {
            return "BigDecimal";
        } else if (columnType.equalsIgnoreCase("float")) {
            return "Float";
        } else if (columnType.equalsIgnoreCase("double")) {
            return "Double";
        } else {
            return "String";
        }
    }

    /**
     * 将表名转换为类名
     *
     * @param tableName 表名
     * @return 类名
     */
    protected String tableNameToClassName(String tableName) {
        if (tableName.isEmpty()) return tableName;
        String[] parts = tableName.split("_");
        StringBuilder sb = new StringBuilder();
        for (String part : parts) {
            sb.append(part.substring(0, 1).toUpperCase()).append(part.substring(1));
        }
        return sb.toString();
    }

    /**
     * 将列名转换为大驼峰命名法
     *
     * @param columnName 列名
     * @return 驼峰命名法的列名
     */
    protected String toDTCamelCase(String columnName) {
        // Convert column name to camel case (e.g., first_name -> firstName)
        String[] parts = columnName.split("_|-");
        StringBuilder sb = new StringBuilder();
        for (String part : parts) {
            sb.append(part.substring(0, 1).toUpperCase()).append(part.substring(1));
        }
        return sb.toString();
    }


    /**
     * 将列名转换为小驼峰命名法
     *
     * @param columnName 列名
     * @return 驼峰命名法的列名
     */
    protected String toXTCamelCase(String columnName) {
        // Convert column name to camel case (e.g., first_name -> firstName)
        String[] parts = columnName.split("_|-");
        StringBuilder sb = new StringBuilder();
        sb.append(parts[0]);
        for (int i = 1; i < parts.length; i++) {
            sb.append(parts[i].substring(0, 1).toUpperCase()).append(parts[i].substring(1));
        }
        return sb.toString();
    }

    protected String getBasePathOrCreate(String basePath) {
        // 获取项目根路径
        String projectRoot = System.getProperty("user.dir");
        String pathStr = "";
        try {
            // 获取文件路径
            Path path = Paths.get(projectRoot, basePath);
            // 如果路径不存在，则创建路径
            if (!Files.exists(path)) {

                Files.createDirectories(path);

            }
            pathStr = path.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return pathStr;
    }

    /**
     * 获取函数注释
     *
     * @param description 函数描述
     * @param params      参数列表
     * @param returnStr   返回值
     * @return 函数注释
     */
    protected String getFunctionComment(String description, LinkedHashMap<String, String> params, String returnStr) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("\t/**\n");
        stringBuilder.append(String.format("\t * %s\n", description));
        stringBuilder.append("\t *\n");
        for (Map.Entry<String, String> stringStringEntry : params.entrySet()) {
            stringBuilder.append(String.format("\t * @param %s %s\n", ((Map.Entry<?, ?>) stringStringEntry).getKey(), ((Map.Entry<?, ?>) stringStringEntry).getValue()));
        }
        if (!returnStr.equals("void")) {
            stringBuilder.append(String.format("\t * @return %s\n", returnStr));
        }
        stringBuilder.append("\t */\n");
        return stringBuilder.toString();
    }

    protected void render(Map<String, Object> variables, String templateName, String filePath) {
        try {
            String projectRoot = System.getProperty("user.dir");

            Configuration configuration = new Configuration(Configuration.VERSION_2_3_32);
            // 指定模板文件所在的路径
            configuration.setDirectoryForTemplateLoading(new File(Paths.get(projectRoot, "src/main/resources/templates").toString()));

            // 设置模板文件使用的字符集
            configuration.setDefaultEncoding("utf-8");

            // 创建模板对象，加载指定模板
            Template template = configuration.getTemplate(templateName);
            // 写入文件
            try (FileWriter writer = new FileWriter(filePath)) {
                template.process(variables, writer);
//                writer.write(ms);
                log.info("The table's {} generate complete. filePath = {}", templateName, filePath);
            } catch (IOException e) {
                System.out.println("An error occurred while writing to the file: " + e.getMessage());
            } catch (TemplateException e) {
                throw new RuntimeException(e);
            }


        } catch (IOException e) {
            log.error("render method IOException:", e);
        }


    }
}
