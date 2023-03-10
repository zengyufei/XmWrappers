package com.example.demo.common.base;

import com.baomidou.mybatisplus.core.toolkit.LambdaUtils;
import com.baomidou.mybatisplus.core.toolkit.support.LambdaMeta;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.example.demo.common.jackson.OperateEnumConverter;
import com.example.demo.enums.OperateEnum;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.apache.ibatis.reflection.property.PropertyNamer;

public class Condition {
    private String field;
    @JsonDeserialize(converter = OperateEnumConverter.class)
    private OperateEnum oper;
    private Object[] value;

    public Condition() {
    }

    public <T> Condition(OperateEnum oper, Object... value) {
        this.oper = oper;
        this.value = value;
    }

    public <T> Condition(SFunction<T, ?> fn, OperateEnum oper, Object... value) {
        LambdaMeta meta = LambdaUtils.extract(fn);
        this.field = PropertyNamer.methodToProperty(meta.getImplMethodName());
        this.oper = oper;
        this.value = value;
    }

    public <T> Condition(String field, OperateEnum oper, Object... value) {
        this.field = field;
        this.oper = oper;
        this.value = value;
    }

    public <T> Condition(String field, String operStr, Object... value) {
        this.field = field;
        this.oper = OperateEnum.of(operStr);
        this.value = value;
    }

    public Condition setField(String field) {
        this.field = field;
        return this;
    }

    public String getField() {
        return field;
    }

    public OperateEnum getOper() {
        return oper;
    }

    public Condition setOper(OperateEnum oper) {
        this.oper = oper;
        return this;
    }

    public Object[] getValue() {
        return value;
    }

    public Condition setValue(Object[] value) {
        this.value = value;
        return this;
    }
}
