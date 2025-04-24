package com.ybb.service;

import com.ybb.dto.PriceRule;
import com.ybb.dto.ResponseResult;
import com.ybb.feign.ServiceMapFeign;
import com.ybb.mapper.PriceRuleMapper;
import com.ybb.moke.PriceRuleMock;
import com.ybb.request.ForecastPriceDto;
import com.ybb.response.DirectionResponse;
import com.ybb.response.ForecastPriceResponse;
import com.ybb.util.BigDecimalUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class ForecastPriceService {

    @Autowired
    private ServiceMapFeign serviceMapFeign;

    @Autowired
    private PriceRuleMapper priceRuleMapper;

    public ResponseResult forecastPrice(ForecastPriceDto request) {
        // 调用地图服务获取距离
        ResponseResult<DirectionResponse> responseResult = serviceMapFeign.directionDrive(request);
        DirectionResponse data = responseResult.getData();

        // 根据计价规则计算价格
        log.info("调用计价规则");

//        Map<String,Object> map = new HashMap<>();
//        map.put("city_code","");
//        map.put("vehicle_code","");
//        priceRuleMapper.selectByMap(map);
        PriceRuleMock priceRuleMock = new PriceRuleMock();
        PriceRule mockData = priceRuleMock.getPriceRuleMockData();

        // 预估价格
        BigDecimal price = getForecastPrice(data.getDistance(), data.getDuration(), mockData);

        ForecastPriceResponse response = new ForecastPriceResponse();
        response.setPrice(price);
        return ResponseResult.success(response);
    }

    private BigDecimal getForecastPrice(Integer distance, Integer duration, PriceRule rule) {
        BigDecimal result = new BigDecimal(0);
        // ----------------------------------------------------------------------
        // 起步价
        Double fare = rule.getStartFare();
        BigDecimal starFarePrice = new BigDecimal(fare);
        result = result.add(starFarePrice);

        // v2
//        result = BigDecimalUtils.add(result,rule.getStartFare());

        // ----------------------------------------------------------------------
        //里程费
        // 总里程
        BigDecimal totalDistance = new BigDecimal(distance);
        BigDecimal distanceMil = totalDistance.divide(new BigDecimal(1000), 2, BigDecimal.ROUND_HALF_UP);// 将里程数（米）转为 千米 为单位
//        BigDecimal distanceMil = BigDecimalUtils.divide(distance,1000);
        // 起步里程
        BigDecimal startMil = new BigDecimal(rule.getStartMile());
        // 超过里程
        BigDecimal substractMil = distanceMil.subtract(startMil);
        // 判断里程数是否合规，负数置为0，正数取自己
        // 方法一：将BigDecimal转为double类型，使用三目运算判断

        // 方法二：使用BigDecimal中compare方法
        if (substractMil.compareTo(new BigDecimal(0)) <= 0) {
            substractMil = BigDecimal.ZERO;
        }
        // 单价
        BigDecimal unitPricePerMile = new BigDecimal(rule.getUnitPricePerMile());
        // 里程费用 = 单价 * 里程
        BigDecimal milePrice = unitPricePerMile.multiply(substractMil).setScale(2, BigDecimal.ROUND_HALF_UP);
//        BigDecimal milePrice = BigDecimalUtils.multiply(unitPricePerMile,substractMil);

        // 增加里程费
        result = result.add(milePrice);

        // ----------------------------------------------------------------------
        //时常费
        BigDecimal time = new BigDecimal(duration);
        // 时长 （s）转分钟
        BigDecimal timeMin = time.divide(new BigDecimal(60), 2, BigDecimal.ROUND_UP);
//        BigDecimal timeMin = BigDecimalUtils.divide(time,60);
        // 时长单价
        BigDecimal unitPriceMin = new BigDecimal(rule.getUnitPricePerMinute());
        // 时长总结
        BigDecimal totalPriceMin = unitPriceMin.multiply(timeMin).setScale(2, BigDecimal.ROUND_UP);
//        BigDecimal totalPriceMin = BigDecimalUtils.multiply(unitPriceMin,timeMin);

        result = result.add(totalPriceMin);

        return result;
    }
}
