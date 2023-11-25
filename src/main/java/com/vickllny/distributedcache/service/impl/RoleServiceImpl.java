package com.vickllny.distributedcache.service.impl;

import com.vickllny.distributedcache.cache.CacheConfig;
import com.vickllny.distributedcache.cache.impl.CacheServiceImpl;
import com.vickllny.distributedcache.domain.Role;
import com.vickllny.distributedcache.domain.User;
import com.vickllny.distributedcache.mapper.RoleMapper;
import com.vickllny.distributedcache.mapper.UserMapper;
import com.vickllny.distributedcache.service.IRoleService;
import com.vickllny.distributedcache.service.IUserService;
import com.vickllny.distributedcache.utils.ContextUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RoleServiceImpl extends CacheServiceImpl<RoleMapper, Role> implements IRoleService {

    @Autowired
    private RoleMapper roleMapper;

    @Override
    @CacheConfig
    @Transactional(rollbackFor = Exception.class)
    public Boolean saveRole(final Role role) {
        //先删除
        final IRoleService iRoleService = ContextUtils.getBean(IRoleService.class);
        final Role role1 = iRoleService.getById(role.getId());
        if(role1 != null){
            iRoleService.removeById(role1.getId());
        }
        //新增
        return roleMapper.insert(role) > 0;
    }

}
