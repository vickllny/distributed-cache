package com.vickllny.distributedcache.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class ContextUtils implements ApplicationContextAware {


    private static final Logger LOGGER = LoggerFactory.getLogger(ContextUtils.class);

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(final ApplicationContext applicationContext) throws BeansException {
        ContextUtils.applicationContext = applicationContext;
    }

    public static <T> T getBean(Class<T> clazz){
        try {
            return applicationContext.getBean(clazz);
        }catch (NoSuchBeanDefinitionException e){
            LOGGER.error(e.getMessage());
        }
        return null;
    }
}
