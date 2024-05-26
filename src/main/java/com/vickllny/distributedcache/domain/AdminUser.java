package com.vickllny.distributedcache.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * @description:
 * @author: vickllny
 * @date 2024-05-26 16:21
 * @leetcode:
 */
@TableName("t_admin_user")
public class AdminUser extends User {

    @TableField(value = "admin_id")
    protected String adminId;

    public String getAdminId() {
        return adminId;
    }

    public void setAdminId(final String adminId) {
        this.adminId = adminId;
    }
}
