package com.clever.util.generate;

/**
 * @Author xixi
 * @Date 2023-12-18 11:47
 **/
public interface IGenerator {
    void generate(String basePath);

    void generate(String tableName, String basePath);
}
