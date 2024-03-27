package com.clever.exception;

import com.clever.bean.model.Result;

/**
 * @Author xixi
 * @Date 2023-12-15 09:39
 **/
public class ConstantException {
    public final Integer code;
    public String msg;

    public ConstantException(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public ConstantException format(Object... params) {
        this.msg = String.format(this.msg, params);
        return this;
    }

    public ConstantException reset(String message) {
        this.msg = message;
        return this;
    }

    public Result<String> getResult() {
        return Result.ofFail(code, msg);
    }

    public Result<String> getResult(String... params) {
        String message = msg;
        for (String p : params) {
            message = message.replace("{}", p);
        }
        return Result.ofFail(code, message);
    }


    public static ConstantException USER_NO_ONLINE = new ConstantException(1001, "用户未登录");
    public static ConstantException USER_PASSWORD_ERROR_SO_MANY = new ConstantException(1002, "密码错误次数过多,30分钟后再试");
    public static ConstantException USER_ACCOUNT_NOT_FOUND = new ConstantException(1003, "账号不存在");
    public static ConstantException USER_LOGIN_PASSWORD_ERROR = new ConstantException(1004, "账号或密码不正确");
    public static ConstantException PARAMETER_VERIFICATION_FAIL = new ConstantException(1005, "参数校验失败");
    public static ConstantException PARAMETER_VERIFICATION_EMAIL_FAIL = new ConstantException(1006, "错误的邮箱");
    public static ConstantException ACCOUNT_IS_EXISTED = new ConstantException(1007, "账号已被注册");
    public static ConstantException ACTIVATION_LINKS_EXPIRED = new ConstantException(1008, "激活链接已失效");


    public static ConstantException EMAIL_SEND_OFTEN = new ConstantException(2001, "邮件已发送, 请查看您的邮箱");

    public static ConstantException DATA_IS_EXIST = new ConstantException(3001, "%s已存在");
    public static ConstantException DATA_NOT_EXIST = new ConstantException(3002, "%s不存在");
    public static ConstantException DATA_INVALID = new ConstantException(3002, "%s失效");

    public static ConstantException INSUFFICIENT_BALANCE = new ConstantException(3003, "余额不足");
    public static ConstantException RECHARGE_AMOUNT_CANNOT_BE_LESS_THAN_0 = new ConstantException(3003, "充值金额不能小于0");
    public static ConstantException INSUFFICIENT_INVENTORY_GOODS = new ConstantException(3004, "%s商品库存不足");
}
