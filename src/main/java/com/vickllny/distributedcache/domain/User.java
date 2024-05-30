package com.vickllny.distributedcache.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

//@Data
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

    public String getId() {
        return id;
    }

    public void setId( String id) {
        this.id = id;
    }

    public @NotBlank String getUserName() {
        return userName;
    }

    public void setUserName(@NotBlank String userName) {
        this.userName = userName;
    }

    public @NotBlank String getLoginName() {
        return loginName;
    }

    public void setLoginName(@NotBlank String loginName) {
        this.loginName = loginName;
    }
}
