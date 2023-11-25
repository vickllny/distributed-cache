package com.vickllny.distributedcache.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("t_role")
public class Role {

    @TableId("id")
    private Integer id;
    @TableField("role_name")
    private String roleName;
    @TableField("role_code")
    private String roleCode;

}
