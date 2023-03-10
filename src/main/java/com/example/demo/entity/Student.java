package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.example.demo.common.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
@TableName(value = "student")
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Student extends BaseEntity implements Serializable {

    /**
     * 学生id
     */
    @TableId
    @TableField(value = "id")
    private String id;

    /**
     * 学生名称
     */
    @TableField(value = "name")
    private String name;

    /**
     * 学生年纪
     */
    @TableField(value = "age")
    private Integer age;

}
