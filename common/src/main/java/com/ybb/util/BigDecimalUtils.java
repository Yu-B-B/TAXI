package com.ybb.util;

import java.math.BigDecimal;

public class BigDecimalUtils {
    public static BigDecimal add(BigDecimal a, Object b) {
        return a.add(new BigDecimal(b.toString()));
    }

    public static BigDecimal divide(Object a, Object b) {
        return new BigDecimal(String.valueOf(a)).divide(new BigDecimal(String.valueOf(b)), 2, BigDecimal.ROUND_HALF_UP);
    }

    public static BigDecimal multiply(Object a, Object b) {
        return new BigDecimal(String.valueOf(a)).multiply(new BigDecimal(String.valueOf(b))).setScale(2, BigDecimal.ROUND_HALF_UP);
    }
}
