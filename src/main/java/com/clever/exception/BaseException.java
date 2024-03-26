package com.clever.exception;

/**
 * @Author xixi
 * @Date 2023-12-15 09:39
 **/
public class BaseException extends RuntimeException {
    /**
     * 错误状态码
     */
    private final Integer code;

    public BaseException(Integer code, String message) {
        super(message);
        this.code = code;
    }

    public BaseException(ConstantException constantException) {
        super(constantException.msg);
        this.code = constantException.code;
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }

    public Integer getCode() {
        return code;
    }
}
