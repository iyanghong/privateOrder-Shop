package com.clever;

import com.clever.util.DesUtil;
import com.clever.util.JavaToTsConverter;
import com.clever.util.StringEncryptUtil;
import com.clever.util.generate.*;
import com.clever.util.generate.config.GenerateConfig;

/**
 * @Author xixi
 * @Date 2023-12-22 16:27
 **/
public class Test {

    public static void main(String[] args) {
        generator();
//        convertEntityToTs();
    }

    public static void convertEntityToTs() {
        JavaToTsConverter.convert("/src/main/java/com/clever/bean", "/target/ts");
    }

    public static void generator() {
        // 创建一个GenerateConfig对象，用于配置数据库信息
//        GenerateConfig generateDatabaseConfig = new GenerateConfig("jdbc:mysql://localhost:3306", "root", "Ts962464");
        GenerateConfig generateDatabaseConfig = new GenerateConfig("jdbc:mysql://localhost:3306/shopping", "root", "Ts962464");
        // 设置应用名称
        generateDatabaseConfig.setAppName("clever-shopping");
        // 设置实体类包名
        generateDatabaseConfig.setEntityPackageName("com.clever.bean.shopping");
        // 设置mapper类包名
        generateDatabaseConfig.setMapperPackageName("com.clever.mapper");
        // 设置service类包名
        generateDatabaseConfig.setServicePackageName("com.clever.service");
        // 设置控制器类包名
        generateDatabaseConfig.setControllerPackageName("com.clever.controller");


        String tableName = "friend,friend_request";
        generateEntity(tableName, generateDatabaseConfig);
        generateMapperXML(tableName, generateDatabaseConfig);
        generateMapper(tableName, generateDatabaseConfig);
        generateService(tableName, generateDatabaseConfig);
        generateController(tableName, generateDatabaseConfig);
        generateApiPost(generateDatabaseConfig);
    }

    private static void generateEntity(String tableName, GenerateConfig generateDatabaseConfig) {
        // 创建一个GenerateEntity对象，用于生成实体类
        GenerateEntity handler = new GenerateEntity(generateDatabaseConfig);
        // 生成实体类
        handler.generate(tableName, "/src/main/java/com/clever/bean/shopping");

    }

    private static void generateMapperXML(String tableName, GenerateConfig generateDatabaseConfig) {
        // 创建一个GenerateMapperXML对象，用于生成mapper.xml文件
        GenerateMapperXML generateMapperXML = new GenerateMapperXML(generateDatabaseConfig);
        // 生成mapper.xml文件
        generateMapperXML.generate(tableName, "/src/main/resources/com/clever/mapper");

    }

    private static void generateMapper(String tableName, GenerateConfig generateDatabaseConfig) {
        // 创建一个GenerateMapper对象，用于生成mapper类
        GenerateMapper generateMapper = new GenerateMapper(generateDatabaseConfig, generateDatabaseConfig.getEntityPackageName());
        // 生成mapper类
        generateMapper.generate(tableName, "/src/main/java/com/clever/mapper");

    }

    private static void generateService(String tableName, GenerateConfig generateDatabaseConfig) {
        // 创建一个GenerateService对象，用于生成service类
        GenerateService generateService = new GenerateService(generateDatabaseConfig, generateDatabaseConfig.getEntityPackageName(), "com.clever.mapper");
        // 生成service类
        generateService.generate(tableName, "/src/main/java/com/clever/service");
    }

    private static void generateController(String tableName, GenerateConfig generateDatabaseConfig) {
        // 创建一个GenerateController对象，用于生成controller类
        GenerateController controller = new GenerateController(generateDatabaseConfig);
        // 生成controller类
        controller.generate(tableName, "/src/main/java/com/clever/controller");

    }

    private static void generateApiPost(GenerateConfig generateDatabaseConfig) {
        GenerateApiPost generateApiPost = new GenerateApiPost(generateDatabaseConfig);
        generateApiPost.generate("/src/main/resources");
    }
}
