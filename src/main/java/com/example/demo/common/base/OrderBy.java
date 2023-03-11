package com.example.demo.common.base;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.LambdaUtils;
import com.baomidou.mybatisplus.core.toolkit.support.LambdaMeta;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.example.demo.common.jackson.OrderTypeEnumConverter;
import com.example.demo.enums.OrderTypeEnum;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.ibatis.reflection.property.PropertyNamer;

@Data
@Accessors(chain = true)
public class OrderBy {

    private Integer index;
    private String field;
    @JsonDeserialize(converter = OrderTypeEnumConverter.class)
    private OrderTypeEnum orderBy;

    public OrderBy() {
    }

    public <T> OrderBy(SFunction<T, ?> fn, OrderTypeEnum orderBy) {
        LambdaMeta meta = LambdaUtils.extract(fn);
        this.field = PropertyNamer.methodToProperty(meta.getImplMethodName());
        this.orderBy = orderBy;
        if (this.orderBy == null) {
            this.orderBy = OrderTypeEnum.ASC;
        }
    }

    public <T> OrderBy(SFunction<T, ?> fn, String orderByStr) {
        LambdaMeta meta = LambdaUtils.extract(fn);
        this.field = PropertyNamer.methodToProperty(meta.getImplMethodName());
        if (StrUtil.isBlankOrUndefined(orderByStr)) {
            this.orderBy = OrderTypeEnum.ASC;
        } else {
            this.orderBy = OrderTypeEnum.of(orderByStr);
        }
    }

    public OrderBy(String field, OrderTypeEnum orderBy) {
        this.field = field;
        this.orderBy = orderBy;
        if (this.orderBy == null) {
            this.orderBy = OrderTypeEnum.ASC;
        }
    }

    public OrderBy(String field, String orderByStr) {
        this.field = field;
        if (StrUtil.isBlankOrUndefined(orderByStr)) {
            this.orderBy = OrderTypeEnum.ASC;
        } else {
            this.orderBy = OrderTypeEnum.of(orderByStr);
        }
    }

    public <T> OrderBy(SFunction<T, ?> fn) {
        LambdaMeta meta = LambdaUtils.extract(fn);
        this.field = PropertyNamer.methodToProperty(meta.getImplMethodName());
        this.orderBy = OrderTypeEnum.ASC;
    }

    public OrderBy(String field) {
        this.field = field;
        this.orderBy = OrderTypeEnum.ASC;
    }

    public OrderBy(Integer index, String field) {
        this.index = index;
        this.field = field;
    }

    public <T> OrderBy(Integer index, SFunction<T, ?> fn) {
        this.index = index;
        LambdaMeta meta = LambdaUtils.extract(fn);
        this.field = PropertyNamer.methodToProperty(meta.getImplMethodName());
    }

    public <T> OrderBy(Integer index, SFunction<T, ?> fn, OrderTypeEnum orderBy) {
        this.index = index;
        LambdaMeta meta = LambdaUtils.extract(fn);
        this.field = PropertyNamer.methodToProperty(meta.getImplMethodName());
        this.orderBy = orderBy;
        if (this.orderBy == null) {
            this.orderBy = OrderTypeEnum.ASC;
        }
    }

    public OrderBy(Integer index, String field, OrderTypeEnum orderBy) {
        this.index = index;
        this.field = field;
        this.orderBy = orderBy;
        if (this.orderBy == null) {
            this.orderBy = OrderTypeEnum.ASC;
        }
    }

    public <T> OrderBy(Integer index, SFunction<T, ?> fn, String orderByStr) {
        this.index = index;
        LambdaMeta meta = LambdaUtils.extract(fn);
        this.field = PropertyNamer.methodToProperty(meta.getImplMethodName());
        if (StrUtil.isBlankOrUndefined(orderByStr)) {
            this.orderBy = OrderTypeEnum.ASC;
        } else {
            this.orderBy = OrderTypeEnum.of(orderByStr);
        }
    }

    public OrderBy(Integer index, String field, String orderByStr) {
        this.index = index;
        this.field = field;
        if (StrUtil.isBlankOrUndefined(orderByStr)) {
            this.orderBy = OrderTypeEnum.ASC;
        } else {
            this.orderBy = OrderTypeEnum.of(orderByStr);
        }
    }

}
