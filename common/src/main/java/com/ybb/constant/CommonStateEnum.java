package com.ybb.constant;

public enum CommonStateEnum {
    VERIFICATION_CODE_ERROR(2131,"验证码校验失败"),
    TOKEN_ERROR(1199, "token无效或已过期"),
    User_Not_exists(1200,"用户不存在"),
    SUCCESS(1,"succcess"),
    FAIL(0,"fail")

    ;

    private int code;
    private String message;
    CommonStateEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
