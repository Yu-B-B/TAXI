package com.ybb.constant;

public enum CommonStateEnum {
    VERIFICATION_CODE_ERROR(2131,"验证码校验失败"),
    TOKEN_ERROR(1199, "token无效或已过期"),


    CALL_USER_ADD_ERROR(1000,"调用新增用户异常"),

    CHECK_CODE_ERROR(1001,"手机号或手机验证码异常"),
    CAR_NOT_EXISTS(1507,"车辆不存在"),

    DRIVER_STATUS_UPDATE_ERROR(1508,"司机工作状态修改失败"),

    /**
     * 司机和车辆：1500-1599
     */
    DRIVER_CAR_BIND_NOT_EXISTS(1500,"司机和车辆绑定关系不存在"),

    DRIVER_NOT_EXITST(1501,"司机不存在"),

    DRIVER_CAR_BIND_EXISTS(1502,"司机和车辆绑定关系已存在，请勿重复绑定"),

    DRIVER_BIND_EXISTS(1503,"司机已经被绑定了，请勿重复绑定"),

    CAR_BIND_EXISTS(1504,"车辆已经被绑定了，请勿重复绑定"),

    CITY_DRIVER_EMPTY(1505,"当前城市没有可用的司机"),

    AVAILABLE_DRIVER_EMPTY(1506,"可用的司机为空"),


    /**
     * 订单：1600-1699
     */
    ORDER_GOING_ON(1600,"有正在进行的订单"),




    DEVICE_IS_BLACK(1601,"该设备超过下单次数"),

    CITY_SERVICE_NOT_SERVICE(1602,"当前城市不提供叫车服务"),

    ORDER_CANCEL_ERROR(1603, "订单取消失败"),

    ORDER_NOT_EXISTS(1604,"订单不存在"),

    ORDER_CAN_NOT_GRAB(1605 , "订单不能被抢"),

    ORDER_GRABING(1606,"订单正在被抢"),

    ORDER_UPDATE_ERROR(1607,"订单修改失败"),

    /**
    * 计价规则
    * */
    PRICE_RULE_EMPTY(1300,"计价规则不存在"),

    PRICE_RULE_EXISTS(1301,"计价规则已存在，不允许添加"),

    PRICE_RULE_NOT_EDIT(1302,"计价规则没有变化"),

    PRICE_RULE_CHANGED(1303,"计价规则有变化"),

    USER_NOT_EXISTS(1200,"用户不存在"),

    VALIDATION_EXCEPTION(1700,"统一验证框架的错误提示"),


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
