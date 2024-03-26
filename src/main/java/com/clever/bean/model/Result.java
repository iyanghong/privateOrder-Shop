package com.clever.bean.model;

import java.io.Serializable;

/**
 * @author: xixi
 * @create: 2023-12-13 16:34
 **/
public class Result<T> implements Serializable {
    public static final int SUCCESS_CODE = 0;
    /**
     * 状态码
     */
    private int code;
    /**
     * 响应消息
     */
    private String message;
    /**
     * 响应数据
     */
    private T data;

    public Result() {
        this.message = "操作成功";
    }

    public Result(int code) {
        this(SUCCESS_CODE, null, null);
    }

    public Result(T data) {
        this(SUCCESS_CODE, data, "操作成功");
    }

    public Result(T data, String message) {
        this(SUCCESS_CODE, data, message);
    }

    public Result(int code, String message) {
        this(code, null, message);
    }

    public static Result<String> ofMessage(String message) {
        return new Result<>(SUCCESS_CODE, null, message);
    }

    public static Result<String> ofFail(int code, String message) {
        return new Result<>(code, null, message);
    }

    public static Result<String> ofSuccess(String message) {
        return new Result<>(SUCCESS_CODE, null, message);
    }

    public static Result<String> ofSuccess() {
        return new Result<>(SUCCESS_CODE, null, "操作成功");
    }

    public static Result<Object> ofSuccess(Object data) {
        return new Result<>(SUCCESS_CODE, data, "操作成功");
    }

    public static Result<Object> ofSuccess(String message, Object data) {
        return new Result<>(SUCCESS_CODE, data, message);
    }

    public Result(int code, T data, String message) {
        this.code = code;
        this.data = data;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
