package com.clever.util.generate;

import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONObject;
import com.clever.util.generate.config.GenerateConfig;
import com.clever.util.generate.entity.ColumnMeta;
import com.clever.util.generate.entity.TableMeta;
import com.clever.util.generate.enums.RequestMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author xixi
 * @Date 2023-12-25 15:21
 **/
public class GenerateApiPost extends BaseGenerator {
    private static final Logger log = LoggerFactory.getLogger(GenerateApiPost.class);
    private final List<String> allowSearchColumnNames = Arrays.asList("_id", "name", "email", "phone", "status", "sex", "gender", "account", "type", "code", "host", "title");

    public GenerateApiPost(GenerateConfig config) {
        super(config);
    }

    private Integer count = 0;

    /**
     * 生成ApiPost文档
     *
     * @param tableMetaList 表列表
     * @param basePath      基础路径
     */
    @Override
    protected void handler(List<TableMeta> tableMetaList, String basePath) {
        count = 0;
        LinkedHashMap<String, Object> folder = initFolder(config.getAppName());
        HashMap<String,String> variable = new HashMap<>();
        List<LinkedHashMap<String, Object>> modules = new ArrayList<>();
        for (TableMeta tableMeta : tableMetaList) {
//            if (tableMeta.com)
            LinkedHashMap<String, Object> item = initFolder(tableMeta.getCommentOrName());
            List<ColumnMeta> getListByForeignKeyList = tableMeta.getColumns().stream().filter(it -> it.getColumnName().endsWith("_id") || it.getColumnName().equals(config.getCreatorFieldName())).collect(Collectors.toList());
            List<ColumnMeta> getByUniqueColumns = tableMeta.getColumns().stream().filter(it -> "UNI".equalsIgnoreCase(it.getColumnKey())).collect(Collectors.toList());


            List<LinkedHashMap<String, Object>> children = new ArrayList<>();

            children.add(buildCreate(tableMeta, null));
            if (tableMeta.getPrimaryKeyColumn() != null) {
                children.add(buildUpdate(tableMeta, tableMeta.getPrimaryKeyColumn()));
                children.add(buildSave(tableMeta, tableMeta.getPrimaryKeyColumn()));
                children.add(buildGet(tableMeta, tableMeta.getPrimaryKeyColumn()));
            }

            children.add(buildPage(tableMeta, null));
            for (ColumnMeta columnMeta : getListByForeignKeyList) {
                children.add(buildGetList(tableMeta, columnMeta));
            }
            for (ColumnMeta columnMeta : getByUniqueColumns) {
                children.add(buildGetByForeignKey(tableMeta, columnMeta));
            }

            if (tableMeta.getPrimaryKeyColumn() != null) {
                children.add(buildDelete(tableMeta, tableMeta.getPrimaryKeyColumn()));
            }

            item.put("children", children);
            modules.add(item);

            tableMeta.getColumns().forEach(columnMeta -> {
                variable.put(columnMeta.getLowerCamelCaseName(),columnMeta.getColumnComment());
            });
        }
        folder.put("children", modules);
        String filePath = Paths.get(getBasePathOrCreate(basePath), toDTCamelCase(config.getAppName()) + "ApiPost.json").toString();
        // 写入文件
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write(JSONObject.toJSONString(folder));
            log.info("The apipost generate complete. count = '{}' filePath = {}", count, filePath);
        } catch (IOException e) {
            System.out.println("An error occurred while writing to the file: " + e.getMessage());
        }

        String variableFilePath = Paths.get(getBasePathOrCreate(basePath), toDTCamelCase(config.getAppName()) + "ApiPostVariable.json").toString();
        // 写入文件
        try (FileWriter writer = new FileWriter(variableFilePath)) {
            writer.write(JSONObject.toJSONString(variable));
            log.info("The apipost generate complete. count = '{}' filePath = {}", count, filePath);
        } catch (IOException e) {
            System.out.println("An error occurred while writing to the file: " + e.getMessage());
        }
    }

    public LinkedHashMap<String, Object> initFolder(String name) {
        LinkedHashMap<String, Object> folder = new LinkedHashMap<>();
        folder.put("target_type", "folder");
        folder.put("name", name);
        folder.put("mark", "developing");
        folder.put("sort", 1);
        folder.put("tags", new ArrayList<>());
        folder.put("created_uuid", "9H4DC0CC7D61");
        LinkedHashMap<String, Object> request = new LinkedHashMap<>();
        request.put("description", "");
        folder.put("request", request);
        return folder;
    }

    public LinkedHashMap<String, Object> buildCreate(TableMeta tableMeta, ColumnMeta primaryColumn) {
        String name = "新增" + tableMeta.getCommentOrName();
        String url = "/" + tableMeta.getLowerCamelCaseName();
        LinkedHashMap<String, Object> body = new LinkedHashMap<>();
        body.put("mode", "form-data");
        List<LinkedHashMap<String, Object>> bodyParameter = new ArrayList<>();
        for (ColumnMeta column : tableMeta.getColumns()) {
            if (column.getColumnKey().equals("PRI")) {
                continue;
            }
            if (config.getAutoFillField().contains(column.getColumnName())) {
                continue;
            }
            bodyParameter.add(initParameter(column.getLowerCamelCaseName(), column.getColumnComment(), column.getJavaType(), "", column.isHasNeedNotBlankValidate()));
        }
        body.put("parameter", bodyParameter);
        return initApi(RequestMethod.POST, name, url, null, body, null);
    }

    public LinkedHashMap<String, Object> buildUpdate(TableMeta tableMeta, ColumnMeta primaryColumn) {
        LinkedHashMap<String, Object> map = new LinkedHashMap<>();
        String name = "修改" + tableMeta.getCommentOrName();
        String url = "/" + tableMeta.getLowerCamelCaseName() + "/{id}";
        LinkedHashMap<String, Object> body = new LinkedHashMap<>();
        body.put("mode", "form-data");
        List<LinkedHashMap<String, Object>> bodyParameter = new ArrayList<>();
        for (ColumnMeta column : tableMeta.getColumns()) {
            if (config.getAutoFillField().contains(column.getColumnName())) {
                continue;
            }
            bodyParameter.add(initParameter(column.getLowerCamelCaseName(), column.getColumnComment(), column.getJavaType(), "", column.isHasNeedNotBlankValidate()));
        }
        body.put("parameter", bodyParameter);
        return initApi(RequestMethod.PATCH, name, url, null, body, null);
    }

    public LinkedHashMap<String, Object> buildSave(TableMeta tableMeta, ColumnMeta primaryColumn) {
        String name = "保存" + tableMeta.getCommentOrName();
        String url = "/" + tableMeta.getLowerCamelCaseName() + "/save";
        LinkedHashMap<String, Object> body = new LinkedHashMap<>();
        body.put("mode", "form-data");
        List<LinkedHashMap<String, Object>> bodyParameter = new ArrayList<>();
        for (ColumnMeta column : tableMeta.getColumns()) {
            if (config.getAutoFillField().contains(column.getColumnName())) {
                continue;
            }
            bodyParameter.add(initParameter(column.getLowerCamelCaseName(), column.getColumnComment(), column.getJavaType(), "", column.isHasNeedNotBlankValidate()));
        }
        body.put("parameter", bodyParameter);

        return initApi(RequestMethod.POST, name, url, null, body, null);
    }

    public LinkedHashMap<String, Object> buildPage(TableMeta tableMeta, ColumnMeta columnMeta) {
        String name = "分页查询" + tableMeta.getCommentOrName();
        String url = "/" + tableMeta.getLowerCamelCaseName() + "/page/{pageNumber}/{pageSize}";
        LinkedHashMap<String, Object> query = new LinkedHashMap<>();
        List<LinkedHashMap<String, Object>> queryParameter = new ArrayList<>();
        List<ColumnMeta> allowSearchColumns = tableMeta.getColumns().stream().filter(it -> allowSearchColumnNames.stream().anyMatch(it.getColumnName()::endsWith)).collect(Collectors.toList());
        for (ColumnMeta column : allowSearchColumns) {
            queryParameter.add(initParameter(column.getLowerCamelCaseName(), column.getColumnComment(), column.getJavaType(), "", false));
        }
        query.put("parameter", queryParameter);

        LinkedHashMap<String, Object> resful = new LinkedHashMap<>();
        List<LinkedHashMap<String, Object>> resfulParameter = new ArrayList<>();
        resfulParameter.add(initParameter("pageNumber", "页码", "Integer", "1", true));
        resfulParameter.add(initParameter("pageSize", "每页条数", "Integer", "10", true));
        resful.put("parameter", resfulParameter);

        return initApi(RequestMethod.GET, name, url, query, null, resful);
    }

    public LinkedHashMap<String, Object> buildGet(TableMeta tableMeta, ColumnMeta columnMeta) {
        String name = "查询" + tableMeta.getCommentOrName();
        String url = "/" + tableMeta.getLowerCamelCaseName() + "/{id}";
        LinkedHashMap<String, Object> resful = new LinkedHashMap<>();
        List<LinkedHashMap<String, Object>> resfulParameter = new ArrayList<>();
        resfulParameter.add(initParameter("id", "主键", "Integer", "1", true));
        resful.put("parameter", resfulParameter);
        return initApi(RequestMethod.GET, name, url, null, null, resful);
    }

    public LinkedHashMap<String, Object> buildGetByForeignKey(TableMeta tableMeta, ColumnMeta columnMeta) {
        String name = "根据" + columnMeta.getCommentOrName() + "查询" + tableMeta.getCommentOrName();
        String url = String.format("/%s/%s/{%s}", tableMeta.getLowerCamelCaseName(), columnMeta.getLowerCamelCaseName(), columnMeta.getLowerCamelCaseName());
        LinkedHashMap<String, Object> resful = new LinkedHashMap<>();
        List<LinkedHashMap<String, Object>> resfulParameter = new ArrayList<>();
        resfulParameter.add(initParameter(columnMeta.getLowerCamelCaseName(), columnMeta.getCommentOrLowerCamelCaseName(), columnMeta.getJavaType(), "1", true));
        resful.put("parameter", resfulParameter);
        return initApi(RequestMethod.GET, name, url, null, null, resful);
    }

    public LinkedHashMap<String, Object> buildGetList(TableMeta tableMeta, ColumnMeta columnMeta) {
        String name = "根据" + columnMeta.getCommentOrName() + "查询" + tableMeta.getCommentOrName() + "列表";
        String url = String.format("/%s/listBy%s/{%s}", tableMeta.getLowerCamelCaseName(), columnMeta.getUpperCamelCaseName(), columnMeta.getLowerCamelCaseName());
        LinkedHashMap<String, Object> resful = new LinkedHashMap<>();
        List<LinkedHashMap<String, Object>> resfulParameter = new ArrayList<>();
        resfulParameter.add(initParameter(columnMeta.getLowerCamelCaseName(), columnMeta.getCommentOrLowerCamelCaseName(), columnMeta.getJavaType(), "1", true));
        resful.put("parameter", resfulParameter);
        return initApi(RequestMethod.GET, name, url, null, null, resful);
    }

    public LinkedHashMap<String, Object> buildDelete(TableMeta tableMeta, ColumnMeta columnMeta) {
        String name = "删除" + tableMeta.getCommentOrName();
        String url = "/" + tableMeta.getLowerCamelCaseName() + "/{id}";
        LinkedHashMap<String, Object> resful = new LinkedHashMap<>();
        List<LinkedHashMap<String, Object>> resfulParameter = new ArrayList<>();
        resfulParameter.add(initParameter("id", "主键", "Integer", "1", true));
        resful.put("parameter", resfulParameter);
        return initApi(RequestMethod.DELETE, name, url, null, null, resful);
    }


    public LinkedHashMap<String, Object> initApi(RequestMethod method, String name, String url, LinkedHashMap<String, Object> query, LinkedHashMap<String, Object> body, LinkedHashMap<String, Object> resful) {
        LinkedHashMap<String, Object> map = new LinkedHashMap<>();
        map.put("target_type", "api");
        map.put("name", name);
        map.put("mark", "complated");
        map.put("sort", 1);
        map.put("tags", new ArrayList<>());
        map.put("created_uuid", "9H4DC0CC7D61");
        map.put("method", method.name());
        map.put("mock", "{}");
        map.put("mock_url", url);
        LinkedHashMap<String, Object> request = new LinkedHashMap<>();
        request.put("url", url);
        request.put("description", name + "接口");

        LinkedHashMap<String, Object> event = new LinkedHashMap<>();
        event.put("pre_script", "");
        event.put("test", "");
        map.put("event", event);

        request.put("pre_tasks", new ArrayList<>());
        request.put("post_tasks", new ArrayList<>());

        if (query != null) {
            request.put("query", query);
        }
        if (body != null) {
            request.put("body", body);
        }
        if (resful != null) {
            request.put("resful", resful);
        }
        map.put("request", request);
        return map;
    }

    /**
     * 初始化参数
     *
     * @param key      键
     * @param name     名称
     * @param type     类型
     * @param example  示例
     * @param required 是否必填
     * @return 参数
     */
    public LinkedHashMap<String, Object> initParameter(String key, String name, String type, String example, boolean required) {
        LinkedHashMap<String, Object> map = new LinkedHashMap<>();
        map.put("key", key);
        map.put("description", name);
        map.put("is_checked", 1);
        map.put("type", "TEXT");
        map.put("not_null", required);
        map.put("field_type", type);
        map.put("value", example);
        return map;
    }
}
