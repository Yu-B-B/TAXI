package com.ybb.moke;

import com.ybb.dto.PriceRule;

public class PriceRuleMock {
    public PriceRule getPriceRuleMockData() {
        PriceRule rule = new PriceRule();
        rule.setCityCode("1212");
        rule.setFareType("10");
        rule.setVehicleType("2");
        rule.setStartFare(20.0);
        rule.setStartMile(3);
        rule.setUnitPricePerMile(1.8);
        rule.setUnitPricePerMinute(0.5);

        return rule;
    }
}
