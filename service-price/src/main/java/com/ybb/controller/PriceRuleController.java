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
     * 根据【计价类型】（城市编码 + 车辆类型）判断是否为最新的【计价版本】
     * @param fareType
     * @param fareVersion
     * @return
     */
    @GetMapping("/isNew")
    public ResponseResult isNew(@RequestParam String fareType,@RequestParam String fareVersion) {
        return priceRuleService.isNew(fareType,fareVersion);
    }
}