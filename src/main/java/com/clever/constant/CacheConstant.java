package com.clever.constant;

import com.clever.config.AppRunConfig;

import javax.annotation.Resource;

/**
 * Redis常量
 *
 * @Author xixi
 * @Date 2023-12-19 16:22
 **/
public class CacheConstant {

    public static String APP_NAME = "clever";
    /**
     * 统一格式化的缓存key<br/>
     * appName:key:smallKey
     */
    public static String CACHE_KEY_FORMAT = "%s:%s:%s";

    /**
     * 登录key
     */
    public static final String ONLINE_KEY_NAME = "online";

    /**
     * 格式化一个在线的缓存key
     *
     * @param token 登陆后生成的token
     * @return online key
     */
    public static String getOnlineKeyName(String token) {
        return String.format(CACHE_KEY_FORMAT, APP_NAME, ONLINE_KEY_NAME, token);
    }

    /**
     * 格式化一个缓存key
     *
     * @param key      缓存key
     * @param smallKey 缓存key
     * @return 缓存key
     */
    public static String formatKey(String key, String smallKey) {
        return String.format(CACHE_KEY_FORMAT, APP_NAME, key, smallKey);
    }
}
