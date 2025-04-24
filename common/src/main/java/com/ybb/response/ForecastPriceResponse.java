package com.ybb.response;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ForecastPriceResponse {
    private BigDecimal price;
}
