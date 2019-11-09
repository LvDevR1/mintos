package com.rest.mintos.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class BigDecimalUtils {

    public static final int AMOUNT_SCALE = 2;

    public static BigDecimal amount(BigDecimal amount) {
        return amount.setScale(AMOUNT_SCALE, RoundingMode.HALF_UP);
    }

    public static BigDecimal amount(int value) {
        return amount(new BigDecimal(value));
    }

    public static BigDecimal amount(String value) {
        if (value == null) {
            return null;
        }
        return amount(new BigDecimal(value));
    }
}
