package com.vickllny.distributedcache.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.IService;
import com.vickllny.distributedcache.cache.ICacheService;
import com.vickllny.distributedcache.domain.User;

import java.util.List;

public interface ICommonUserService<T extends User> extends IService<T> {

    default List<T> findByUserName(final String userName){
        final LambdaQueryWrapper<T> query = Wrappers.lambdaQuery(getEntityClass());
        query.eq(T::getUserName, userName);
        return list(query);
    }
}
