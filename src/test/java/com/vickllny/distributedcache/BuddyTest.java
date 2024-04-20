package com.vickllny.distributedcache;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatchers;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;

/**
 * @description:
 * @author: vickllny
 * @date 2024-04-20 09:40
 * @leetcode:
 */
public class BuddyTest {

    @Test
    public void test() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Class<?> dynamicType = new ByteBuddy()
                .subclass(Object.class)
                .name("com.vickllny.DynamicUser")
                .defineProperty("username", String.class)
                .defineProperty("password", String.class)
                .make()
                .load(getClass().getClassLoader(), ClassLoadingStrategy.Default.WRAPPER)
                .getLoaded();

        // 创建实例并设置字段值
        Object instance = dynamicType.getDeclaredConstructor().newInstance();
        dynamicType.getMethod("setUsername", String.class).invoke(instance, "username123");
        dynamicType.getMethod("setPassword", String.class).invoke(instance, "password123");

        // 调用getter方法获取字段值
        System.out.println(dynamicType.getMethod("getUsername").invoke(instance));
        System.out.println(dynamicType.getMethod("getPassword").invoke(instance));

        System.out.println(dynamicType);
    }
}
