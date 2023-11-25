package com.vickllny.distributedcache.service.impl;

import com.vickllny.distributedcache.cache.CacheConfig;
import com.vickllny.distributedcache.cache.impl.CacheServiceImpl;
import com.vickllny.distributedcache.domain.User;
import com.vickllny.distributedcache.mapper.UserMapper;
import com.vickllny.distributedcache.service.IUserService;
import com.vickllny.distributedcache.utils.ContextUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl extends CacheServiceImpl<UserMapper, User> implements IUserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    @CacheConfig
    @Transactional(rollbackFor = Exception.class)
    public Boolean saveUser(final User user) {
        //先删除
        final IUserService iUserService = ContextUtils.getBean(IUserService.class);
        final User user1 = iUserService.getById(user.getId());
        if(user1 != null){
            iUserService.removeById(user1.getId());
        }
        //新增
        return userMapper.insert(user) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean saveUser1(final User user) {
        return baseMapper.insert(user) > 0;
    }
}
