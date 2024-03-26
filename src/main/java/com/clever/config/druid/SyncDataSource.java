package com.clever.config.druid;

import javax.sql.DataSource;
import java.util.List;

/**
 * @Author xixi
 * @Date 2023-12-15 10:13
 **/
public class SyncDataSource {
    /**
     * key
     */
    private String key;

    /**
     * url
     */
    private String url;

    /**
     * 用户名
     */
    private String username;

    /**
     * 连接密码
     */
    private String password;

    /**
     * 同步的数据库
     */
    private List<String> databases;

    /**
     * 连接池
     */
    private DataSource dataSource;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<String> getDatabases() {
        return databases;
    }

    public void setDatabases(List<String> databases) {
        this.databases = databases;
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }
}
