package com.ybb.dto;

import com.ybb.constant.CommonStateEnum;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true) // 可将对象进行链式调用设置值，如.setCode(11).setMessage();
public class ResponseResult<T> {
    private int code;
    private String message;
    private T data;

    public static <T> ResponseResult<T> success() {
        return new ResponseResult().setCode(CommonStateEnum.SUCCESS.getCode()).setMessage(CommonStateEnum.SUCCESS.getMessage());
    }
    public static <T> ResponseResult<T> success(T data) {
        ResponseResult<T> result = new ResponseResult<>();
        result.setCode(CommonStateEnum.SUCCESS.getCode())
                .setMessage(CommonStateEnum.SUCCESS.getMessage())
                .setData(data);
        return result;
    }

    public static <T> ResponseResult<T> fail(int code, String message) {
        return new ResponseResult().setCode(code)
                .setMessage(message);
    }

    public static <T> ResponseResult<T> fail(int code, String message, T data) {
        return new ResponseResult().setCode(code)
                .setMessage(message)
                .setData(data);
    }

    public static <T> ResponseResult<T> fail(T data) {
        return new ResponseResult().setData(data);
    }
}
