package com.vickllny.distributedcache.service.impl;

import com.vickllny.distributedcache.annotations.ASyncTask;

@ASyncTask
public class TestService {

    public void test(){

        System.out.println(111);
    }
}
