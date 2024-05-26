package com.vickllny.distributedcache.service.impl;

import com.vickllny.distributedcache.cache.CacheConfig;
import com.vickllny.distributedcache.cache.impl.CacheServiceImpl;
import com.vickllny.distributedcache.domain.TenantUser;
import com.vickllny.distributedcache.domain.User;
import com.vickllny.distributedcache.mapper.TenantUserMapper;
import com.vickllny.distributedcache.mapper.UserMapper;
import com.vickllny.distributedcache.service.ITenantUserService;
import com.vickllny.distributedcache.service.IUserService;
import com.vickllny.distributedcache.utils.ContextUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TenantUserServiceImpl extends CacheServiceImpl<TenantUserMapper, TenantUser> implements ITenantUserService {

}
