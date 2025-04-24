package com.ybb.controller;

import com.ybb.dto.ResponseResult;
import com.ybb.request.ForecastPriceDto;
import com.ybb.service.ForecastPriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ForecastPriceController {
    @Autowired
    private ForecastPriceService forecastPriceService;

    @PostMapping("/forecast-price")
    public ResponseResult forecastPrice(@RequestBody ForecastPriceDto request) {
        return forecastPriceService.forecastPrice(request);
    }
}
