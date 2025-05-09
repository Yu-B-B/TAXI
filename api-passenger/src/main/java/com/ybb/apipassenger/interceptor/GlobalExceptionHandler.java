package com.ybb.apipassenger.interceptor;

import com.ybb.constant.CommonStateEnum;
import com.ybb.dto.ResponseResult;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

// 统一异常处理类
@RestControllerAdvice
@Order(99)
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public ResponseResult exception(Exception e) {
        e.printStackTrace();
        return ResponseResult.fail(CommonStateEnum.FAIL.getCode(),CommonStateEnum.FAIL.getMessage());
    }
}
