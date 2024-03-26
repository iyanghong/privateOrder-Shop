package com.clever.util.generate;

import com.clever.util.generate.config.GenerateConfig;
import com.clever.util.generate.entity.FreeMaskerVariable;
import com.clever.util.generate.entity.TableMeta;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @Author xixi
 * @Date 2023-12-18 11:34
 **/
public class GenerateMapper extends BaseGenerator {
    private static final Logger log = LoggerFactory.getLogger(GenerateMapper.class);

    public GenerateMapper(GenerateConfig config) {
        super(config);
    }

    public GenerateMapper(GenerateConfig config, String entityPageName) {
        super(config);
        config.setEntityPackageName(entityPageName);
    }

    @Override
    protected void handler(List<TableMeta> tableMetaList, String basePath) {
        if (tableMetaList.isEmpty()) return;
        // 遍历表元数据列表
        for (TableMeta tableMeta : tableMetaList) {
            FreeMaskerVariable freeMaskerVariable = new FreeMaskerVariable(config, tableMeta);
            String filePath = Paths.get(getBasePathOrCreate(basePath), tableMeta.getUpperCamelCaseName() + "Mapper.java").toString();
            render(freeMaskerVariable.getVariables(), "MapperTemplate.ftl", filePath);
            log.info("The table's mapper class generate complete. table = '{}' filePath = {}", tableMeta.getTableName(), filePath);
        }
    }
}