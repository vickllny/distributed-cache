package com.vickllny.distributedcache.service;

import com.vickllny.distributedcache.cache.ICacheService;
import com.vickllny.distributedcache.domain.Role;
import com.vickllny.distributedcache.domain.User;

public interface IRoleService extends ICacheService<Role> {
    Boolean saveRole(Role role);
}
