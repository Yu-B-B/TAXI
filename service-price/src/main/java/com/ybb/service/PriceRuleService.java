package com.ybb.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ybb.constant.CommonStateEnum;
import com.ybb.dto.PriceRule;
import com.ybb.dto.ResponseResult;
import com.ybb.mapper.PriceRuleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PriceRuleService {

    @Autowired
    PriceRuleMapper priceRuleMapper;

    public ResponseResult add(PriceRule priceRule) {
        // 拼接fareType
        // 根据城市编码与车辆类型结合
        String cityCode = priceRule.getCityCode();
        String vehicleType = priceRule.getVehicleType();
        String fareType = cityCode + vehicleType;
        priceRule.setFareType(fareType);

        // 添加版本号，若该版本已经存在，当前则为二号版本
        QueryWrapper<PriceRule> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("city_code",cityCode);
        queryWrapper.eq("vehicle_type",vehicleType);
        queryWrapper.orderByDesc("fare_version");

        List<PriceRule> priceRules = priceRuleMapper.selectList(queryWrapper);
        Integer fareVersion = 0;
        // v2 - 大于0，说明当前城市已存在对应的计价规则，无需对计价规则新增
        if(priceRules.size()>0){
            // v1 - 获取最大的版本号
//            fareVersion = priceRules.get(0).getFareVersion();
            return ResponseResult.fail(CommonStateEnum.PRICE_RULE_EXISTS.getCode(),CommonStateEnum.PRICE_RULE_EXISTS.getMessage());

        }

        priceRule.setFareVersion(++fareVersion);

        priceRuleMapper.insert(priceRule);

        return ResponseResult.success();
    }

    public ResponseResult edit(PriceRule priceRule){
        // 拼接fareType
        String cityCode = priceRule.getCityCode();
        String vehicleType = priceRule.getVehicleType();
        String fareType = cityCode + "$" + vehicleType;
        priceRule.setFareType(fareType);

        // 添加版本号
        // 问题1：增加了版本号，前面的两个字段无法唯一确定一条记录，问题2：找最大的版本号，需要排序

        QueryWrapper<PriceRule> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("city_code",cityCode);
        queryWrapper.eq("vehicle_type",vehicleType);
        queryWrapper.orderByDesc("fare_version");

        List<PriceRule> priceRules = priceRuleMapper.selectList(queryWrapper);
        Integer fareVersion = 0;
        if (priceRules.size()>0){
            PriceRule lasterPriceRule = priceRules.get(0);
            Double unitPricePerMile = lasterPriceRule.getUnitPricePerMile();
            Double unitPricePerMinute = lasterPriceRule.getUnitPricePerMinute();
            Double startFare = lasterPriceRule.getStartFare();
            Integer startMile = lasterPriceRule.getStartMile();

            if (unitPricePerMile.doubleValue() == priceRule.getUnitPricePerMile().doubleValue()
                    && unitPricePerMinute.doubleValue() == priceRule.getUnitPricePerMinute().doubleValue()
                    && startFare.doubleValue() == priceRule.getStartFare().doubleValue()
                    && startMile.intValue() == priceRule.getStartMile().intValue()){
                return ResponseResult.fail(CommonStateEnum.PRICE_RULE_NOT_EDIT.getCode(),CommonStateEnum.PRICE_RULE_NOT_EDIT.getMessage());
            }


            fareVersion = lasterPriceRule.getFareVersion();
        }
        priceRule.setFareVersion(++fareVersion);


        priceRuleMapper.insert(priceRule);
        return ResponseResult.success();
    }
}
