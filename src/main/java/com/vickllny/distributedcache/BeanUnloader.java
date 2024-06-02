package com.vickllny.distributedcache;

import org.springframework.beans.factory.support.AbstractBeanFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.DefaultSingletonBeanRegistry;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * @description:
 * @author: vickllny
 * @date 2024-06-02 09:16
 * @leetcode:
 */
public class BeanUnloader {

    public static void unloadBean(ClassLoader classLoader, ConfigurableApplicationContext context, String beanName) {
        DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory) context.getBeanFactory();

        // 强制解除对CustomClassLoader的引用
        if (beanFactory.containsSingleton(beanName)) {
            ((DefaultSingletonBeanRegistry) beanFactory).destroySingleton(beanName);
        }

        // 尝试清理强引用
        if (beanFactory instanceof AbstractBeanFactory) {
            ((AbstractBeanFactory) beanFactory).clearMetadataCache();
        }
    }

}
