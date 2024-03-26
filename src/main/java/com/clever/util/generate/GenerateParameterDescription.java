package com.clever.util.generate;

import com.clever.util.generate.config.GenerateConfig;
import com.clever.util.generate.entity.ColumnMeta;
import com.clever.util.generate.entity.TableMeta;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

/**
 * @Author xixi
 * @Date 2023-12-21 08:52
 **/
public class GenerateParameterDescription extends BaseGenerator {
    private static final Logger log = LoggerFactory.getLogger(GenerateParameterDescription.class);

    public GenerateParameterDescription(GenerateConfig config) {
        super(config);
    }

    @Override
    protected void handler(List<TableMeta> tableMetaList, String basePath) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", "主键");
        jsonObject.put("creator", "创建人");
        jsonObject.put("modifier", "修改人");
        jsonObject.put("created_at", "创建时间");
        jsonObject.put("updated_at", "修改时间");
        for (TableMeta tableMeta : tableMetaList) {
            for (ColumnMeta columnMeta : tableMeta.getColumns()) {
                if (!jsonObject.containsKey(columnMeta.getColumnName()) && StringUtils.isNotBlank(columnMeta.getColumnComment())) {
                    jsonObject.put(columnMeta.getColumnName(), columnMeta.getColumnComment());
                }
            }
        }
        String filePath = Paths.get(getBasePathOrCreate(basePath), "parameter_description.json").toString();
        // 写入文件
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write(jsonObject.toString());
            log.info("The table's parameter description generate complete. filePath = {}", filePath);
        } catch (IOException e) {
            System.out.println("An error occurred while writing to the file: " + e.getMessage());
        }
    }
}
