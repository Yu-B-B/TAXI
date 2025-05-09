package com.ybb.apipassenger.interceptor;

import com.ybb.constant.CommonStateEnum;
import com.ybb.dto.ResponseResult;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Set;

/*
* 校验异常处理类
* */
@RestControllerAdvice
@Order(1)
public class GlobalValidationExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseResult methodArgumentNotValidException(MethodArgumentNotValidException e) {
        e.printStackTrace();
        return ResponseResult.fail(CommonStateEnum.VALIDATION_EXCEPTION.getCode(),CommonStateEnum.VALIDATION_EXCEPTION.getMessage());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseResult ConstraintValidationExceptionHandler(ConstraintViolationException e){
        e.printStackTrace();
        Set<ConstraintViolation<?>> constraintViolations = e.getConstraintViolations();
        String message = "";
        for (ConstraintViolation c: constraintViolations ) {
            message = c.getMessage();
        }

        return ResponseResult.fail(CommonStateEnum.VALIDATION_EXCEPTION.getCode(),message);
    }
}
