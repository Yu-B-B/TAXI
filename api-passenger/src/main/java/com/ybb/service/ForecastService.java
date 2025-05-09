package com.ybb.service;

import com.ybb.dto.ResponseResult;
import com.ybb.feign.ServicePriceClient;
import com.ybb.request.ForecastPriceDto;
import com.ybb.response.ForecastPriceResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ForecastService {
    @Autowired
    private ServicePriceClient servicePriceClient;

    public ResponseResult getForecastPrice(ForecastPriceDto request) {

        // 调用计价服务计算价格
        ResponseResult<ForecastPriceResponse> responseResult = servicePriceClient.forecastPrice(request);

        ForecastPriceResponse response = new ForecastPriceResponse();

        response.setVehicleType(request.getVehicleType());
        response.setCityCode(request.getCityCode());


        return ResponseResult.success(response);
    }
}
