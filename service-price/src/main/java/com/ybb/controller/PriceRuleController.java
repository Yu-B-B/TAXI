package com.ybb.controller;


import com.ybb.dto.PriceRule;
import com.ybb.dto.ResponseResult;
import com.ybb.request.PriceRuleIsNewRequest;
import com.ybb.service.PriceRuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/price-rule")
public class PriceRuleController {

    @Autowired
    PriceRuleService priceRuleService;

    /**
     * 添加计价规则
     * @param priceRule
     * @return
     */
    @PostMapping("/add")
    public ResponseResult add(@RequestBody PriceRule priceRule) {

        return priceRuleService.add(priceRule);
    }

    @PostMapping("/edit")
    public ResponseResult edit(@RequestBody PriceRule priceRule) {

        return priceRuleService.edit(priceRule);
    }

    /**
     * 根据【计价类型】（城市编码 + 车辆类型）获取最新的【计价版本】
     * @param fareType
     * @return
     */
    @GetMapping("/getNewVersion")
    public ResponseResult getNewFareVersion(@RequestParam String fareType) {
        return priceRuleService.getNewFareVersion(fareType);
    }

    /**
     * 判断【计价版本】是否为最新
     * 使用GetMapping会出现特殊符号转义情况
     *
     * @return
     */
    @PostMapping("/checkFareVersion")
    public ResponseResult checkFareVersion(@RequestBody PriceRuleIsNewRequest ruleIsNewRequest) {
        return priceRuleService.checkFareVersion(ruleIsNewRequest);
    }

    /**
     * 判断当前城市是否存在计价规则
     *
     * @return
     */
    @PostMapping("/checkFareRule")
    public ResponseResult<Boolean> checkFareRule(@RequestParam PriceRule priceRule) {
        return priceRuleService.checkFareRule(priceRule);
    }

}