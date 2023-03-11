package com.example.demo.student;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.demo.common.base.Condition;
import com.example.demo.common.base.OrderBy;
import com.example.demo.entity.Student;
import com.example.demo.enums.OperateEnum;
import com.example.demo.enums.OrderTypeEnum;
import com.example.demo.mapper.StudentMapper;
import com.example.demo.utils.XmWrappers;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.io.IOException;
import java.util.List;

public class TestConditionOrderBy extends NoSpringMp {

    @Override
    public Resource[] getResources(ResourcePatternResolver resourceResolver) throws IOException {
//        return resourceResolver.getResources("classpath*:/mapper*/**/*.xml");
        return new Resource[]{
                resourceResolver.getResource("classpath:mapper/StudentMapper.xml"),
        };
    }

    @Test
    public void test1() throws Exception {
        final TestConditionOrderBy testUpdatePass = new TestConditionOrderBy();
        testUpdatePass.run(StudentMapper.class, studentMapper -> {
            {
                // 定义查询条件和排序规则
                final List<Condition> conditions = XmWrappers.buildCondition()
                        .field(Student::getName, OperateEnum.LIKE_LEFT, "张")
                        .field("age", "gt", 1)
                        .field("age", "lt", 100)
                        .field("age", "in", 10, 20)
//                        .field("age", OperateEnum.IS_NOT_NULL)
                        .build();

                final List<OrderBy> orders = XmWrappers.buildOrderBy()
                        .add(1, "age", "desc")
                        .add(Student::getName, "desc")
                        .add(1, Student::getId, OrderTypeEnum.ASC)
                        .build();

                final QueryWrapper<Student> search = XmWrappers.search(Student.class, conditions, orders);
                final List<Student> list = studentMapper.selectList(search);
            }
        });
    }

}
