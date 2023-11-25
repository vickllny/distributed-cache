package com.vickllny.distributedcache.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.vickllny.distributedcache.cache.ICacheService;
import com.vickllny.distributedcache.domain.User;

public interface IUserService extends ICacheService<User> {
    Boolean saveUser(User user);
    Boolean saveUser1(User user);
}
