package com.clever.config.handler;

import com.clever.bean.model.Result;
import com.clever.exception.BaseException;
import com.clever.exception.ConstantException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.ValidationException;
import java.util.Objects;

/**
 * 全局异常处理器
 *
 * @Author xixi
 * @Date 2023-12-20 08:53
 **/
@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 应用到所有@RequestMapping注解方法，在其执行之前初始化数据绑定器
     *
     * @param binder binder
     */
    @InitBinder
    public void initBinder(WebDataBinder binder) {
    }

    /**
     * 拦截未知的运行时异常
     *
     * @param e RuntimeException
     * @return 运行时未知错误响应
     */
    @ResponseBody
    @ExceptionHandler(RuntimeException.class)
    public Result<String> notFountRuntimeException(RuntimeException e) {
        log.error("555, 系统未知错误", e);
        return Result.ofFail(555, e.getMessage());
    }

    /**
     * 拦截主动抛出的运行时异常
     *
     * @param e BaseException
     * @return 已知错误响应
     */
    @ResponseBody
    @ExceptionHandler(BaseException.class)
    public Result<?> systemBaseException(BaseException e) {
        return Result.ofFail(e.getCode(), e.getMessage());
    }

    /**
     * 抛出参数错误异常
     *
     * @param e ValidationException
     * @return 已知错误响应
     */
    @ResponseBody
    @ExceptionHandler(ValidationException.class)
    public Result<String> paramValidationException(ValidationException e) {
        String message = e.getMessage();
        log.error(String.valueOf(e));
        int i = message.indexOf(":");
        i = i < 0 ? 0 : i + 2;
        return Result.ofFail(ConstantException.PARAMETER_VERIFICATION_FAIL.code, message.substring(i));
    }

    @ResponseBody
    @ExceptionHandler(BindException.class)
    public Result<String> objectParamBindException(BindException e) {
        BindingResult bindingResult = e.getBindingResult();
        FieldError fieldError = e.getFieldError();
        String message;
        if (fieldError != null) {
            message = fieldError.getDefaultMessage();
        } else {
            message = e.getMessage();
        }
        return Result.ofFail(ConstantException.PARAMETER_VERIFICATION_FAIL.code, message);
    }

    /**
     * 抛出接口对象参数错误异常
     *
     * @param e MethodArgumentNotValidException
     * @return 已知错误响应
     */
    @ResponseBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<String> paramMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        String message = Objects.requireNonNull(e.getBindingResult().getFieldError()).getDefaultMessage();
        log.error(String.valueOf(e));
        return Result.ofFail(ConstantException.PARAMETER_VERIFICATION_FAIL.code, message);
    }
}
