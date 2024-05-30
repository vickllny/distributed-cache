package com.vickllny.distributedcache.service.impl;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.vickllny.distributedcache.domain.User;
import com.vickllny.distributedcache.service.ICommonUserService;

public class CommonUserServiceImpl<M extends BaseMapper<T>, T extends User> extends ServiceImpl<M, T> implements ICommonUserService<T> {

    public void setBaseMapper(M baseMapper){
        super.baseMapper = baseMapper;
    }

}
