package com.vickllny.distributedcache.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
//@TableName("t_user")
public class User {

    @TableId("id")
    @NotNull
    protected String id;

    @TableField("user_name")
    @NotBlank
    protected String userName;

    @TableField("login_name")
    @NotBlank
    protected String loginName;
}
