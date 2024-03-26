package com.clever;

import com.clever.util.generate.*;
import com.clever.util.generate.config.GenerateConfig;

public class Main {
    public static void main(String[] args) {
        // 创建一个GenerateConfig对象，用于配置数据库信息
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


        // 创建一个GenerateEntity对象，用于生成实体类
        GenerateEntity handler = new GenerateEntity(generateDatabaseConfig);
        // 生成实体类
        handler.generate("", "/src/main/java/com/clever/bean/shopping");
        // 创建一个GenerateMapperXML对象，用于生成mapper.xml文件
        GenerateMapperXML generateMapperXML = new GenerateMapperXML(generateDatabaseConfig);
        // 生成mapper.xml文件
        generateMapperXML.generate("", "/src/main/resources/com/clever/mapper");

        // 创建一个GenerateMapper对象，用于生成mapper类
        GenerateMapper generateMapper = new GenerateMapper(generateDatabaseConfig, "com.clever.bean.shopping");
        // 生成mapper类
        generateMapper.generate("", "/src/main/java/com/clever/mapper");

        // 创建一个GenerateService对象，用于生成service类
        GenerateService generateService = new GenerateService(generateDatabaseConfig, "com.clever.bean.shopping", "com.clever.mapper");
        // 生成service类
        generateService.generate("", "/src/main/java/com/clever/service");


        // 创建一个GenerateController对象，用于生成controller类
        GenerateController controller = new GenerateController(generateDatabaseConfig);
        // 生成controller类
        controller.generate("", "/src/main/java/com/clever/controller");


        GenerateApiPost generateApiPost = new GenerateApiPost(generateDatabaseConfig);
        generateApiPost.generate("/src/main/resources");
    }
}