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

    public ResponseResult getForecastPrice(String depLongitude, String depLatitude, String destLongitude, String destLatitude) {
        log.info("出发地经度 : {}", depLongitude);
        log.info("出发地纬度 : {}", depLatitude);
        log.info("目的地经度 : {}", destLongitude);
        log.info("目的地纬度 : {}", destLatitude);

        ForecastPriceDto priceDto = new ForecastPriceDto();
        priceDto.setDepLongitude(depLongitude);
        priceDto.setDepLatitude(depLatitude);
        priceDto.setDestLongitude(destLongitude);
        priceDto.setDestLatitude(destLatitude);
        // 调用计价服务计算价格
        ResponseResult<ForecastPriceResponse> responseResult = servicePriceClient.forecastPrice(priceDto);

        return ResponseResult.success(responseResult.getData().getPrice());
    }
}
