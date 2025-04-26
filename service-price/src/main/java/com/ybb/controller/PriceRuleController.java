package com.ybb.controller;


import com.ybb.dto.PriceRule;
import com.ybb.dto.ResponseResult;
import com.ybb.service.PriceRuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/price-rule")
public class PriceRuleController {

    @Autowired
    PriceRuleService priceRuleService;

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
    public ResponseResult isNew(@RequestParam String fareType) {
        return priceRuleService.getNewFareVersion(fareType);
    }

    @GetMapping("/checkFareVersion")
    public ResponseResult checkFareVersion(@RequestParam String fareType,@RequestParam int fareVersion) {
        return priceRuleService.checkFareVersion(fareType,fareVersion);
    }
}