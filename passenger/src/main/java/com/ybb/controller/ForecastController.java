package com.ybb.controller;

import com.ybb.request.ForecastPriceDto;
import com.ybb.dto.ResponseResult;
import com.ybb.service.ForecastService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 预估价格接口
 */
@RestController
public class ForecastController {

    @Autowired
    private ForecastService forecastService;

    @PostMapping("/force-price")
    public ResponseResult getForeCasePrice(@RequestBody ForecastPriceDto request) {
        return forecastService.getForecastPrice(request.getDepLongitude(), request.getDepLatitude(), request.getDestLongitude(), request.getDestLatitude());
    }
}
