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
        if(priceRules.size()>0){
            // 获取最大的版本号
            fareVersion = priceRules.get(0).getFareVersion();
        }

        priceRule.setFareVersion(++fareVersion);

        priceRuleMapper.insert(priceRule);

        return ResponseResult.success();
    }
}
