package com.ybb.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ybb.constant.CommonStateEnum;
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
import java.util.List;
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

        // TODO:读取数据库中计价规则
        /*QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("city_code",request.getCityCode());
        queryWrapper.eq("vehicle_type",request.getVehicleType());
        queryWrapper.orderByDesc("fare_version");

        List<PriceRule> priceRules = priceRuleMapper.selectList(queryWrapper);
        // 获取最新的计价规则，最新计价规则中数据都是最新的
        if (priceRules.size() == 0){
            return ResponseResult.fail(CommonStateEnum.PRICE_RULE_EMPTY.getCode(),CommonStateEnum.VERIFICATION_CODE_ERROR.getValue());
        }

        PriceRule priceRule = priceRules.get(0);


         */
        PriceRuleMock priceRuleMock = new PriceRuleMock();
        PriceRule priceRule = priceRuleMock.getPriceRuleMockData();

        // 预估价格
        BigDecimal price = getForecastPrice(data.getDistance(), data.getDuration(), priceRule);

        ForecastPriceResponse response = new ForecastPriceResponse();
        response.setPrice(price);
        response.setCityCode(request.getCityCode());
        response.setVehicleType(request.getVehicleType());
        // v1 - 不携带版本号，防止出现早预估后下单场景
        // v2 - 携带版本号返回，后面的请求再来时重新查询
        response.setFareVersion(priceRule.getFareVersion());
        response.setFareType(priceRule.getFareType());
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

    public ResponseResult<BigDecimal> calculatePrice( Integer distance ,  Integer duration, String cityCode, String vehicleType){
        // 查询计价规则
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("city_code",cityCode);
        queryWrapper.eq("vehicle_type",vehicleType);
        queryWrapper.orderByDesc("fare_version");

        List<PriceRule> priceRules = priceRuleMapper.selectList(queryWrapper);
        if (priceRules.size() == 0){
            return ResponseResult.fail(CommonStateEnum.PRICE_RULE_EMPTY.getCode(),CommonStateEnum.VERIFICATION_CODE_ERROR.getMessage());
        }

        PriceRule priceRule = priceRules.get(0);

        log.info("根据距离、时长和计价规则，计算价格");

        BigDecimal price = getForecastPrice(distance, duration, priceRule);
        return ResponseResult.success(price);
    }


}
