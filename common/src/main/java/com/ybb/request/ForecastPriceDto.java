package com.ybb.request;

import lombok.Data;

/**
 * 经纬度参数
 * 使用位置：客户端 --> 乘客端 --> 预估价格接口 --> 地图服务
 * */
@Data
public class ForecastPriceDto {
    // 出发地经度
    private String depLongitude;
    // 出发地纬度
    private String depLatitude;
    // 目的地经度
    private String destLongitude;
    // 目的地纬度
    private String destLatitude;
    // 城市编码
    private String cityCode;
    // 计价规则
    private String vehicleType;
}
