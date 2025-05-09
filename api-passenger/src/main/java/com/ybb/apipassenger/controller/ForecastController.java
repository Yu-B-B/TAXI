package com.ybb.apipassenger.controller;

import com.ybb.request.ForecastPriceDto;
import com.ybb.dto.ResponseResult;
import com.ybb.apipassenger.service.ForecastService;
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

    /**
     * 预估价格
     * @param request
     * @return
     */
    @PostMapping("/force-price")
    public ResponseResult getForeCasePrice(@RequestBody ForecastPriceDto request) {
        return forecastService.getForecastPrice(request);
    }
}
