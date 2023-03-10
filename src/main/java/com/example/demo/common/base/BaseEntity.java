package com.example.demo.common.base;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class BaseEntity implements Serializable {
    private static final long serialVersionUID = -80173182440175475L;

    @TableField(exist = false)
    List<Condition> conditions;
    @TableField(exist = false)
    private List<OrderBy> orders;

}
