package com.example.demo.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.demo.common.Result;
import com.example.demo.entity.Student;
import com.example.demo.mapper.StudentMapper;
import com.example.demo.utils.XmWrappers;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
public class StudentController {

    private final StudentMapper studentMapper;

    @PostMapping("/list")
    public Result<List<Student>> list(@RequestBody Student student) {
        final QueryWrapper<Student> search = XmWrappers.search(Student.class, student.getConditions(), student.getOrders());
        final List<Student> students = studentMapper.selectList(search);
        return new Result<List<Student>>().ok(students);
    }

}
