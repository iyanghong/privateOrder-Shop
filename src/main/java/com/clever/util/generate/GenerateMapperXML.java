package com.clever.util.generate;

import com.clever.util.generate.config.GenerateConfig;
import com.clever.util.generate.entity.FreeMaskerVariable;
import com.clever.util.generate.entity.TableMeta;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

/**
 * @Author xixi
 * @Date 2023-12-18 11:33
 **/
public class GenerateMapperXML extends BaseGenerator {
    private static final Logger log = LoggerFactory.getLogger(GenerateMapperXML.class);

    public GenerateMapperXML(GenerateConfig config) {
        super(config);
    }

    @Override
    protected void handler(List<TableMeta> tableMetaList, String basePath) {
        if (tableMetaList.isEmpty()) return;
        // 遍历表元数据列表
        for (TableMeta tableMeta : tableMetaList) {
            FreeMaskerVariable freeMaskerVariable = new FreeMaskerVariable(config, tableMeta);
            String filePath = Paths.get(getBasePathOrCreate(basePath), toDTCamelCase(tableMeta.getTableName()) + "Mapper.xml").toString();
            render(freeMaskerVariable.getVariables(), "MapperXMLTemplate.ftl", filePath);
            log.info("The table's mapper xml generate complete. table = '{}' filePath = {}", tableMeta.getTableName(), filePath);
        }
    }
}
