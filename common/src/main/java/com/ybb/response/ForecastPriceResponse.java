package com.ybb.response;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ForecastPriceResponse {
    private BigDecimal price;

    private String cityCode;

    private String vehicleType;

    private String fareType;

    private Integer fareVersion; // 运价版本
}
