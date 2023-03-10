package com.example.demo.utils;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.example.demo.common.base.Condition;
import com.example.demo.common.base.OrderBy;
import com.example.demo.enums.OperateEnum;
import com.example.demo.enums.OrderTypeEnum;

import java.util.ArrayList;
import java.util.List;

/**
 * Wrapper 条件构造
 *
 * @author Caratacus
 */
public final class XmWrappers {

    public static <T> QueryWrapper<T> search(Class<T> entityClass, List<Condition> conditions) {
        return search(entityClass, conditions, null);
    }

    public static <T> QueryWrapper<T> search(Class<T> entityClass, List<Condition> conditions, List<OrderBy> orders) {
        if (entityClass == null) {
            throw new IllegalArgumentException("entityClass 不能为空");
        }
        // 参数校验
        if (CollUtil.isEmpty(conditions) && CollUtil.isEmpty(orders)) {
            return null;
        }

        // 构造查询器
        QueryWrapper<T> queryWrapper = new QueryWrapper<>();
        final TableInfo tableInfo = TableInfoHelper.getTableInfo(entityClass);

        if (CollUtil.isNotEmpty(conditions)) {
            // 构造查询条件
            conditions.forEach(condition -> handleCondition(queryWrapper, tableInfo, condition));
        }
        if (CollUtil.isNotEmpty(orders)) {
            // 构造排序规则
            orders.forEach(order -> handleOrder(queryWrapper, tableInfo, order));
        }
        return queryWrapper;
    }

    private static <T> void handleCondition(QueryWrapper<T> queryWrapper, TableInfo tableInfo, Condition condition) {
        String field = condition.getField();
        OperateEnum operateEnum = condition.getOper();
        Object[] value = condition.getValue();

        if (operateEnum == null) {
            return;
        }

        // 参数校验
        if (!OperateEnum.SELECT.equals(operateEnum) && StrUtil.isBlankOrUndefined(field)) {
            return;
        }

        if (ArrayUtil.isEmpty(value)) {
            if (!OperateEnum.IS_NULL.equals(operateEnum) && !OperateEnum.IS_NOT_NULL.equals(operateEnum)) {
                return;
            }
        }

        String columnName = null;
        if (!OperateEnum.SELECT.equals(operateEnum)) {
            columnName = getColumn(tableInfo, field);
        }
        operateEnum.apply(queryWrapper, tableInfo, columnName, value);
    }


    private static <T> void handleOrder(QueryWrapper<T> queryWrapper, TableInfo tableInfo, OrderBy order) {
        String field = order.getField();
        final OrderTypeEnum orderTypeEnum = order.getOrderBy();

        if (StrUtil.isBlankOrUndefined(field)) {
            return;
        }
        // 抽取方法，减少代码重复
        if (orderTypeEnum == OrderTypeEnum.ASC) {
            queryWrapper.orderByAsc(getColumn(tableInfo, field));
        } else {
            queryWrapper.orderByDesc(getColumn(tableInfo, field));
        }

    }

    private static String getColumn(TableInfo tableInfo, String fieldName) {
        final String keyProperty = tableInfo.getKeyProperty();
        if (StrUtil.equalsIgnoreCase(keyProperty, fieldName)) {
            return tableInfo.getKeyColumn();
        } else {
            final List<TableFieldInfo> fieldList = tableInfo.getFieldList();
            TableFieldInfo fieldInfo = fieldList
                    .stream()
                    .filter(f -> f.getProperty().equals(fieldName))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("不能找到 property 对应的数据库字段: " + fieldName));
            return fieldInfo.getColumn();
        }
    }

    public static InnerCondition buildCondition() {
        return new InnerCondition();
    }

    public static InnerOrderBy buildOrderBy() {
        return new InnerOrderBy();
    }

    public static class InnerCondition {
        private final List<Condition> conditions = new ArrayList<>();

        public InnerCondition select(Object... values) {
            conditions.add(new Condition(OperateEnum.SELECT, values));
            return this;
        }

        public <T> InnerCondition field(String fieldName, String operateStr, Object... values) {
            final OperateEnum operateEnum = OperateEnum.of(operateStr);
            conditions.add(new Condition(fieldName, operateEnum, values));
            return this;
        }


        public <T> InnerCondition field(String fieldName, OperateEnum operateEnum, Object... values) {
            conditions.add(new Condition(fieldName, operateEnum, values));
            return this;
        }

        public <T> InnerCondition field(SFunction<T, ?> fn, OperateEnum operateEnum, Object... values) {
            conditions.add(new Condition(fn, operateEnum, values));
            return this;
        }

        public <T> InnerCondition eq(SFunction<T, ?> fn, Object values) {
            conditions.add(new Condition(fn, OperateEnum.EQUAL, values));
            return this;
        }

        public <T> InnerCondition eq(String fieldName, Object values) {
            conditions.add(new Condition(fieldName, OperateEnum.EQUAL, values));
            return this;
        }

        public <T> InnerCondition ne(SFunction<T, ?> fn, Object values) {
            conditions.add(new Condition(fn, OperateEnum.NOT_EQUAL, values));
            return this;
        }

        public <T> InnerCondition ne(String fieldName, Object values) {
            conditions.add(new Condition(fieldName, OperateEnum.NOT_EQUAL, values));
            return this;
        }

        public <T> InnerCondition gt(SFunction<T, ?> fn, Object values) {
            conditions.add(new Condition(fn, OperateEnum.GT, values));
            return this;
        }

        public <T> InnerCondition gt(String fieldName, Object values) {
            conditions.add(new Condition(fieldName, OperateEnum.GT, values));
            return this;
        }

        public <T> InnerCondition ge(SFunction<T, ?> fn, Object values) {
            conditions.add(new Condition(fn, OperateEnum.GE, values));
            return this;
        }

        public <T> InnerCondition ge(String fieldName, Object values) {
            conditions.add(new Condition(fieldName, OperateEnum.GE, values));
            return this;
        }

        public <T> InnerCondition lt(SFunction<T, ?> fn, Object values) {
            conditions.add(new Condition(fn, OperateEnum.LT, values));
            return this;
        }

        public <T> InnerCondition lt(String fieldName, Object values) {
            conditions.add(new Condition(fieldName, OperateEnum.LT, values));
            return this;
        }

        public <T> InnerCondition le(SFunction<T, ?> fn, Object values) {
            conditions.add(new Condition(fn, OperateEnum.LE, values));
            return this;
        }

        public <T> InnerCondition le(String fieldName, Object values) {
            conditions.add(new Condition(fieldName, OperateEnum.LE, values));
            return this;
        }

        public <T> InnerCondition in(SFunction<T, ?> fn, Object... values) {
            conditions.add(new Condition(fn, OperateEnum.IN, values));
            return this;
        }

        public <T> InnerCondition in(String fieldName, Object... values) {
            conditions.add(new Condition(fieldName, OperateEnum.IN, values));
            return this;
        }

        public <T> InnerCondition notIn(SFunction<T, ?> fn, Object... values) {
            conditions.add(new Condition(fn, OperateEnum.NOT_IN, values));
            return this;
        }

        public <T> InnerCondition notIn(String fieldName, Object... values) {
            conditions.add(new Condition(fieldName, OperateEnum.NOT_IN, values));
            return this;
        }

        public <T> InnerCondition isNull(SFunction<T, ?> fn) {
            conditions.add(new Condition(fn, OperateEnum.IS_NULL));
            return this;
        }

        public <T> InnerCondition isNull(String fieldName) {
            conditions.add(new Condition(fieldName, OperateEnum.IS_NULL));
            return this;
        }

        public <T> InnerCondition isNotNull(SFunction<T, ?> fn) {
            conditions.add(new Condition(fn, OperateEnum.IS_NOT_NULL));
            return this;
        }

        public <T> InnerCondition isNotNull(String fieldName) {
            conditions.add(new Condition(fieldName, OperateEnum.IS_NOT_NULL));
            return this;
        }

        public <T> InnerCondition between(SFunction<T, ?> fn, Object... values) {
            conditions.add(new Condition(fn, OperateEnum.BETWEEN, values));
            return this;
        }

        public <T> InnerCondition between(String fieldName, Object... values) {
            conditions.add(new Condition(fieldName, OperateEnum.BETWEEN, values));
            return this;
        }

        public <T> InnerCondition notBetween(SFunction<T, ?> fn, Object... values) {
            conditions.add(new Condition(fn, OperateEnum.NOT_BETWEEN, values));
            return this;
        }

        public <T> InnerCondition notBetween(String fieldName, Object... values) {
            conditions.add(new Condition(fieldName, OperateEnum.NOT_BETWEEN, values));
            return this;
        }

        public <T> InnerCondition like(SFunction<T, ?> fn, Object values) {
            conditions.add(new Condition(fn, OperateEnum.LIKE, values));
            return this;
        }

        public <T> InnerCondition like(String fieldName, Object values) {
            conditions.add(new Condition(fieldName, OperateEnum.LIKE, values));
            return this;
        }

        public <T> InnerCondition likeLeft(SFunction<T, ?> fn, Object values) {
            conditions.add(new Condition(fn, OperateEnum.LIKE_LEFT, values));
            return this;
        }

        public <T> InnerCondition likeLeft(String fieldName, Object values) {
            conditions.add(new Condition(fieldName, OperateEnum.LIKE_LEFT, values));
            return this;
        }

        public <T> InnerCondition likeRight(SFunction<T, ?> fn, Object values) {
            conditions.add(new Condition(fn, OperateEnum.LIKE_RIGHT, values));
            return this;
        }

        public <T> InnerCondition likeRight(String fieldName, Object values) {
            conditions.add(new Condition(fieldName, OperateEnum.LIKE_RIGHT, values));
            return this;
        }

        public <T> InnerCondition notLike(SFunction<T, ?> fn, Object values) {
            conditions.add(new Condition(fn, OperateEnum.NOT_LIKE, values));
            return this;
        }

        public <T> InnerCondition notLike(String fieldName, Object values) {
            conditions.add(new Condition(fieldName, OperateEnum.NOT_LIKE, values));
            return this;
        }

        public List<Condition> build() {
            return conditions;
        }
    }


    public static class InnerOrderBy {
        private final List<OrderBy> orderBys = new ArrayList<>();

        public <T> InnerOrderBy add(SFunction<T, ?> fn, OrderTypeEnum orderTypeEnum) {
            orderBys.add(new OrderBy(fn, orderTypeEnum));
            return this;
        }

        public <T> InnerOrderBy asc(SFunction<T, ?> fn) {
            orderBys.add(new OrderBy(fn, OrderTypeEnum.ASC));
            return this;
        }

        public <T> InnerOrderBy desc(SFunction<T, ?> fn) {
            orderBys.add(new OrderBy(fn, OrderTypeEnum.DESC));
            return this;
        }

        public <T> InnerOrderBy add(String fieldName, OrderTypeEnum orderTypeEnum) {
            orderBys.add(new OrderBy(fieldName, orderTypeEnum));
            return this;
        }

        public <T> InnerOrderBy add(String fieldName, String orderTypeStr) {
            OrderTypeEnum orderTypeEnum = OrderTypeEnum.ASC;
            if (StrUtil.equalsIgnoreCase(OrderTypeEnum.DESC.name(), orderTypeStr)) {
                orderTypeEnum =  OrderTypeEnum.DESC;
            }
            orderBys.add(new OrderBy(fieldName, orderTypeEnum));
            return this;
        }

        public <T> InnerOrderBy asc(String fieldName) {
            orderBys.add(new OrderBy(fieldName, OrderTypeEnum.ASC));
            return this;
        }

        public <T> InnerOrderBy desc(String fieldName) {
            orderBys.add(new OrderBy(fieldName, OrderTypeEnum.DESC));
            return this;
        }

        public List<OrderBy> build() {
            return orderBys;
        }
    }


}
