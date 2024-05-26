package com.vickllny.distributedcache.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * @description:
 * @author: vickllny
 * @date 2024-05-26 16:23
 * @leetcode:
 */
@TableName(value = "t_tenant_user")
public class TenantUser extends User {

    @TableField(value = "tenant_id")
    protected String tenantId;

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(final String tenantId) {
        this.tenantId = tenantId;
    }
}
