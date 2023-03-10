package com.example.demo.enums;

import cn.hutool.core.util.StrUtil;

public enum OrderTypeEnum {
    ASC,
    DESC;

    public static OrderTypeEnum of(String symbol) {
        for (OrderTypeEnum op : OrderTypeEnum.values()) {
            if (StrUtil.equalsIgnoreCase(symbol, op.name())) {
                return op;
            }
        }
        throw new IllegalArgumentException("Unsupported operator: " + symbol);
    }

}
