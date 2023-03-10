package com.example.demo.enums;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfo;

import java.util.ArrayList;
import java.util.List;

public enum OperateEnum {

    SELECT("select") {
        @Override
        public <T> void apply(QueryWrapper<T> queryWrapper, TableInfo tableInfo, String column, Object value) {
            final Object[] objects = (Object[]) value;
            List<String> args = new ArrayList<>();
            for (Object o : objects) {
                final String str = Convert.toStr(o);
                if (StrUtil.isBlankOrUndefined(str)) {
                    continue;
                }
                args.add(OperateEnum.getColumn(tableInfo, str));
            }
            queryWrapper.select(args.toArray(new String[]{}));
        }
    },


    EQUAL("eq", "=") {
        @Override
        public <T> void apply(QueryWrapper<T> queryWrapper, TableInfo tableInfo, String column, Object value) {
            final Object[] objects = (Object[]) value;
            if (ArrayUtil.isEmpty(objects)) {
                return;
            }
            final Object object = objects[0];
            if (ObjectUtil.isNull(object)) {
                return;
            }
            queryWrapper.eq(column, object);
        }
    },

    NOT_EQUAL("ne", "!=", "<>") {
        @Override
        public <T> void apply(QueryWrapper<T> queryWrapper, TableInfo tableInfo, String column, Object value) {
            final Object[] objects = (Object[]) value;
            if (ArrayUtil.isEmpty(objects)) {
                return;
            }
            final Object object = objects[0];
            if (ObjectUtil.isNull(object)) {
                return;
            }
            queryWrapper.ne(column, object);
        }
    },

    GT("gt", ">") {
        @Override
        public <T> void apply(QueryWrapper<T> queryWrapper, TableInfo tableInfo, String column, Object value) {
            final Object[] objects = (Object[]) value;
            if (ArrayUtil.isEmpty(objects)) {
                return;
            }
            final Object object = objects[0];
            if (ObjectUtil.isNull(object)) {
                return;
            }
            queryWrapper.gt(column, object);
        }
    },

    GE("ge", ">=") {
        @Override
        public <T> void apply(QueryWrapper<T> queryWrapper, TableInfo tableInfo, String column, Object value) {
            final Object[] objects = (Object[]) value;
            if (ArrayUtil.isEmpty(objects)) {
                return;
            }
            final Object object = objects[0];
            if (ObjectUtil.isNull(object)) {
                return;
            }
            queryWrapper.ge(column, object);
        }
    },

    LT("lt", "<") {
        @Override
        public <T> void apply(QueryWrapper<T> queryWrapper, TableInfo tableInfo, String column, Object value) {
            final Object[] objects = (Object[]) value;
            if (ArrayUtil.isEmpty(objects)) {
                return;
            }
            final Object object = objects[0];
            if (ObjectUtil.isNull(object)) {
                return;
            }
            queryWrapper.lt(column, object);
        }
    },

    LE("le", "<=") {
        @Override
        public <T> void apply(QueryWrapper<T> queryWrapper, TableInfo tableInfo, String column, Object value) {
            final Object[] objects = (Object[]) value;
            if (ArrayUtil.isEmpty(objects)) {
                return;
            }
            final Object object = objects[0];
            if (ObjectUtil.isNull(object)) {
                return;
            }
            queryWrapper.le(column, object);
        }
    },

    IN("in") {
        @Override
        public <T> void apply(QueryWrapper<T> queryWrapper, TableInfo tableInfo, String column, Object value) {
            final Object[] objects = (Object[]) value;
            if (ArrayUtil.isEmpty(objects)) {
                return;
            }
            boolean isList = false;
            for (Object object : objects) {
                if (object instanceof List) {
                    isList = true;
                    final List<?> list = (List<?>) object;
                    if (CollUtil.isEmpty(list)) {
                        break;
                    }
                    queryWrapper.in(column, list);
                }
                break;
            }
            if (!isList) {
                queryWrapper.in(column, objects);
            }
        }
    },

    NOT_IN("not_in", "notin") {
        @Override
        public <T> void apply(QueryWrapper<T> queryWrapper, TableInfo tableInfo, String column, Object value) {
            final Object[] objects = (Object[]) value;
            if (ArrayUtil.isEmpty(objects)) {
                return;
            }
            boolean isList = false;
            for (Object object : objects) {
                if (object instanceof List) {
                    isList = true;
                    final List<?> list = (List<?>) object;
                    if (CollUtil.isEmpty(list)) {
                        break;
                    }
                    queryWrapper.in(column, list);
                }
                break;
            }
            if (!isList) {
                queryWrapper.in(column, objects);
            }
        }
    },

    IS_NULL("is_null", "isnull") {
        @Override
        public <T> void apply(QueryWrapper<T> queryWrapper, TableInfo tableInfo, String column, Object value) {
            final Object[] objects = (Object[]) value;
            if (ArrayUtil.isEmpty(objects)) {
                return;
            }
            queryWrapper.isNull(column);
        }
    },

    IS_NOT_NULL("is_not_null", "isnotnull") {
        @Override
        public <T> void apply(QueryWrapper<T> queryWrapper, TableInfo tableInfo, String column, Object value) {
            final Object[] objects = (Object[]) value;
            if (ArrayUtil.isEmpty(objects)) {
                return;
            }
            queryWrapper.isNotNull(column);
        }
    },

    BETWEEN("between") {
        @Override
        public <T> void apply(QueryWrapper<T> queryWrapper, TableInfo tableInfo, String column, Object value) {
            final Object[] objects = (Object[]) value;
            if (ArrayUtil.isEmpty(objects) && objects.length < 2) {
                return;
            }
            final Object object1 = objects[0];
            if (ObjectUtil.isNull(object1)) {
                return;
            }
            final Object object2 = objects[1];
            if (ObjectUtil.isNull(object2)) {
                return;
            }
            queryWrapper.between(column, object1, object2);
        }
    },

    NOT_BETWEEN("not_between", "between") {
        @Override
        public <T> void apply(QueryWrapper<T> queryWrapper, TableInfo tableInfo, String column, Object value) {
            final Object[] objects = (Object[]) value;
            if (ArrayUtil.isEmpty(objects) && objects.length < 2) {
                return;
            }
            final Object object1 = objects[0];
            if (ObjectUtil.isNull(object1)) {
                return;
            }
            final Object object2 = objects[1];
            if (ObjectUtil.isNull(object2)) {
                return;
            }
            queryWrapper.notBetween(column, object1, object2);
        }
    },

    LIKE("like") {
        @Override
        public <T> void apply(QueryWrapper<T> queryWrapper, TableInfo tableInfo, String column, Object value) {
            final Object[] objects = (Object[]) value;
            if (ArrayUtil.isEmpty(objects)) {
                return;
            }
            final String object = Convert.toStr(objects[0]);
            if (StrUtil.isBlankOrUndefined(object)) {
                return;
            }
            queryWrapper.like(column, object);
        }
    },

    LIKE_LEFT("like_left", "likeleft") {
        @Override
        public <T> void apply(QueryWrapper<T> queryWrapper, TableInfo tableInfo, String column, Object value) {
            final Object[] objects = (Object[]) value;
            if (ArrayUtil.isEmpty(objects)) {
                return;
            }
            final String object = Convert.toStr(objects[0]);
            if (StrUtil.isBlankOrUndefined(object)) {
                return;
            }
            queryWrapper.likeLeft(column, object);
        }
    },

    LIKE_RIGHT("like_right", "likeright") {
        @Override
        public <T> void apply(QueryWrapper<T> queryWrapper, TableInfo tableInfo, String column, Object value) {
            final Object[] objects = (Object[]) value;
            if (ArrayUtil.isEmpty(objects)) {
                return;
            }
            final String object = Convert.toStr(objects[0]);
            if (StrUtil.isBlankOrUndefined(object)) {
                return;
            }
            queryWrapper.likeRight(column, object);
        }
    },

    NOT_LIKE("not_like", "like") {
        @Override
        public <T> void apply(QueryWrapper<T> queryWrapper, TableInfo tableInfo, String column, Object value) {
            final Object[] objects = (Object[]) value;
            if (ArrayUtil.isEmpty(objects)) {
                return;
            }
            final String object = Convert.toStr(objects[0]);
            if (StrUtil.isBlankOrUndefined(object)) {
                return;
            }
            queryWrapper.notLike(column, object);
        }
    },

    ;

    private final String[] symbols;

    public abstract <T> void apply(QueryWrapper<T> queryWrapper, TableInfo tableInfo, String column, Object value);

    OperateEnum(String... symbols) {
        this.symbols = symbols;
    }

    public String[] getSymbols() {
        return symbols;
    }

    public static OperateEnum of(String symbol) {
        for (OperateEnum op : OperateEnum.values()) {
            for (String s : op.getSymbols()) {
                if (StrUtil.equalsIgnoreCase(symbol, s)) {
                    return op;
                }
            }
        }
        throw new IllegalArgumentException("Unsupported operator: " + symbol);
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
}
