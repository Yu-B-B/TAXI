package com.ybb.apipassenger.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ybb.constant.CommonStateEnum;
import com.ybb.dto.PriceRule;
import com.ybb.dto.ResponseResult;
import com.ybb.mapper.PriceRuleMapper;
import com.ybb.request.PriceRuleIsNewRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
        queryWrapper.eq("city_code", cityCode);
        queryWrapper.eq("vehicle_type", vehicleType);
        queryWrapper.orderByDesc("fare_version");

        List<PriceRule> priceRules = priceRuleMapper.selectList(queryWrapper);
        Integer fareVersion = 0;
        // v2 - 大于0，说明当前城市已存在对应的计价规则，无需对计价规则新增
        if (priceRules.size() > 0) {
            // v1 - 获取最大的版本号
//            fareVersion = priceRules.get(0).getFareVersion();
            return ResponseResult.fail(CommonStateEnum.PRICE_RULE_EXISTS.getCode(), CommonStateEnum.PRICE_RULE_EXISTS.getMessage());

        }

        priceRule.setFareVersion(++fareVersion);

        priceRuleMapper.insert(priceRule);

        return ResponseResult.success();
    }

    public ResponseResult edit(PriceRule priceRule) {
        // 拼接fareType
        String cityCode = priceRule.getCityCode();
        String vehicleType = priceRule.getVehicleType();
        String fareType = cityCode + "$" + vehicleType;
        priceRule.setFareType(fareType);

        // 添加版本号
        // 问题1：增加了版本号，前面的两个字段无法唯一确定一条记录，问题2：找最大的版本号，需要排序

        QueryWrapper<PriceRule> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("city_code", cityCode);
        queryWrapper.eq("vehicle_type", vehicleType);
        queryWrapper.orderByDesc("fare_version");

        List<PriceRule> priceRules = priceRuleMapper.selectList(queryWrapper);
        Integer fareVersion = 0;
        if (priceRules.size() > 0) {
            PriceRule lasterPriceRule = priceRules.get(0);
            Double unitPricePerMile = lasterPriceRule.getUnitPricePerMile();
            Double unitPricePerMinute = lasterPriceRule.getUnitPricePerMinute();
            Double startFare = lasterPriceRule.getStartFare();
            Integer startMile = lasterPriceRule.getStartMile();

            if (unitPricePerMile.doubleValue() == priceRule.getUnitPricePerMile().doubleValue()
                    && unitPricePerMinute.doubleValue() == priceRule.getUnitPricePerMinute().doubleValue()
                    && startFare.doubleValue() == priceRule.getStartFare().doubleValue()
                    && startMile.intValue() == priceRule.getStartMile().intValue()) {
                return ResponseResult.fail(CommonStateEnum.PRICE_RULE_NOT_EDIT.getCode(), CommonStateEnum.PRICE_RULE_NOT_EDIT.getMessage());
            }


            fareVersion = lasterPriceRule.getFareVersion();
        }
        priceRule.setFareVersion(++fareVersion);


        priceRuleMapper.insert(priceRule);
        return ResponseResult.success();
    }

    /**
     * 获取最新的计价规则
     *
     * @param fareType
     * @return
     */
    public ResponseResult<PriceRule> getNewFareVersion(String fareType) {
        QueryWrapper<PriceRule> wrapper = new QueryWrapper<PriceRule>();
        wrapper.eq("fare_type", fareType);
        wrapper.orderByDesc("fare_version");
        List<PriceRule> list = priceRuleMapper.selectList(wrapper);
        if (list.size() > 0) {
            return ResponseResult.success(list.get(0));
        }
        // 计价规则不存在
        return ResponseResult.fail(CommonStateEnum.PRICE_RULE_EMPTY.getCode(), CommonStateEnum.PRICE_RULE_EMPTY.getMessage());
    }

    /**
     * 判断计价规则是否为最新
     * @return
     */
    public ResponseResult<Boolean> checkFareVersion(PriceRuleIsNewRequest request) {
        ResponseResult<PriceRule> version = getNewFareVersion(request.getFareType());
        if(CommonStateEnum.PRICE_RULE_EMPTY.getCode() == version.getCode()) {
//            return ResponseResult.fail(CommonStateEnum.PRICE_RULE_EMPTY.getCode(), CommonStateEnum.PRICE_RULE_EMPTY.getMessage());
            return ResponseResult.fail(false);
        }
        PriceRule data = version.getData();
        if(request.getFareVersion() < data.getFareVersion()) {
            return ResponseResult.fail(false);
        }else{
            return ResponseResult.success(true);
        }
    }

    /**
     * 判断当前城市是否有计价规则
     * @param priceRule
     * @return
     */
    public ResponseResult<Boolean> checkFareRule(PriceRule priceRule) {
        String cityCode = priceRule.getCityCode();
        String vehicleType = priceRule.getVehicleType();

        LambdaQueryWrapper<PriceRule> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PriceRule::getCityCode,cityCode);
        queryWrapper.eq(PriceRule::getVehicleType,vehicleType);
        queryWrapper.orderByDesc(PriceRule::getFareVersion);
        List<PriceRule> priceRules = priceRuleMapper.selectList(queryWrapper);
        if(priceRules.size() > 0) {
            return ResponseResult.success(true);
        }else {
            return ResponseResult.fail(false);
        }
    }
}
