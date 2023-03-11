package com.example.demo.common.base;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class BaseEntity implements Serializable {
    private static final long serialVersionUID = -80173182440175475L;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @TableField(exist = false)
    List<Condition> conditions;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @TableField(exist = false)
    private List<OrderBy> orders;

}
