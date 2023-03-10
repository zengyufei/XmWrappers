package com.example.demo.student;

import cn.hutool.json.JSONUtil;
import com.example.demo.common.base.Condition;
import com.example.demo.common.base.OrderBy;
import com.example.demo.entity.Student;
import com.example.demo.enums.OperateEnum;
import com.example.demo.enums.OrderTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;


/*
*
日志
==>  Preparing: SELECT id,name,age FROM student WHERE (name LIKE ? AND age > ? AND age < ? AND age IN (?,?)) ORDER BY age DESC,name DESC,id ASC
==> Parameters: %张(String), 1(Integer), 100(Integer), 10(Integer), 20(Integer)
<==      Total: 0

MockHttpServletRequest:
      HTTP Method = POST
      Request URI = /list
       Parameters = {}
          Headers = [Content-Type:"application/json", Accept:"application/json;charset=UTF-8", Content-Length:"351"]
             Body = <no character encoding set>
    Session Attrs = {}

Handler:
             Type = com.example.demo.controller.StudentController
           Method = com.example.demo.controller.StudentController#list(Student)

Async:
    Async started = false
     Async result = null

Resolved Exception:
             Type = null

ModelAndView:
        View name = null
             View = null
            Model = null

FlashMap:
       Attributes = null

MockHttpServletResponse:
           Status = 200
    Error message = null
          Headers = [Content-Type:"application/json;charset=UTF-8"]
     Content type = application/json;charset=UTF-8
             Body = {"code":1000,"msg":"成功","message":"成功","data":[]}
    Forwarded URL = null
   Redirected URL = null
          Cookies = []
2023-03-10 15:56:37.625 [main] INFO  com.example.demo.student.TestStudentController:75 - 返回结果content={"code":1000,"msg":"成功","message":"成功","data":[]}
*
* */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class TestStudentController {

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mvc;
    private MockHttpSession session;


    @Before
    public void setUp() {
        mvc = MockMvcBuilders.webAppContextSetup(wac).build(); //初始化MockMvc对象
        session = new MockHttpSession();
    }


    @Test
    public void contextLoads() throws Exception {
        Student student = new Student();
        student.setConditions(new ArrayList<>());
        student.setOrders(new ArrayList<>());

        student.getConditions().add(new Condition(Student::getName, OperateEnum.LIKE_LEFT, "张"));
        student.getConditions().add(new Condition("age", "gt", 1));
        student.getConditions().add(new Condition("age", "lt", 100));
        student.getConditions().add(new Condition("age", "in", 10, 20));
        student.getConditions().add(new Condition("age", OperateEnum.IS_NOT_NULL));
        student.getOrders().add(new OrderBy("age", "desc"));
        student.getOrders().add(new OrderBy(Student::getName, "desc"));
        student.getOrders().add(new OrderBy(Student::getId, OrderTypeEnum.ASC));
        String strJson = JSONUtil.toJsonStr(student);
        /*
            ==>  Preparing: SELECT id,name,age FROM student WHERE (name LIKE ? AND age > ? AND age < ? AND age IN (?,?)) ORDER BY age DESC,name DESC,id ASC
            ==> Parameters: %张(String), 1(Integer), 100(Integer), 10(Integer), 20(Integer)
            <==      Total: 0
        */
        MvcResult mvcResult = mvc.perform(
                        MockMvcRequestBuilders.post("/list")
                                .accept(MediaType.parseMediaType("application/json;charset=UTF-8"))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(strJson)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
        int status = mvcResult.getResponse().getStatus();                 //得到返回代码
        String content = mvcResult.getResponse().getContentAsString();    //得到返回结果
        log.info("返回结果content={}", content);
        Assert.assertEquals(200, status);
    }
}
