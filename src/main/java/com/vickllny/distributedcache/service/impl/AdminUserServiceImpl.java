package com.vickllny.distributedcache.service.impl;

import com.vickllny.distributedcache.cache.impl.CacheServiceImpl;
import com.vickllny.distributedcache.domain.AdminUser;
import com.vickllny.distributedcache.mapper.AdminUserMapper;
import com.vickllny.distributedcache.service.IAdminUserService;
import org.springframework.stereotype.Service;

@Service
public class AdminUserServiceImpl extends CacheServiceImpl<AdminUserMapper, AdminUser> implements IAdminUserService {
    
}
