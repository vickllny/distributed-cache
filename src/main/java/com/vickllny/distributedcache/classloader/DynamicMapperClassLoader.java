package com.vickllny.distributedcache.classloader;

import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.vickllny.distributedcache.BeanUnloader;
import com.vickllny.distributedcache.config.SpringUtils;
import com.vickllny.distributedcache.utils.ContextUtils;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ConfigurableApplicationContext;

public class DynamicMapperClassLoader extends ClassLoader {


    private Class<?> entityClazz;
    private Class<?> mapperClazz;
    private Class<?> serviceClazz;
    private String beanName;
    private String mapperBeanName;
    private String serviceBeanName;

    public DynamicMapperClassLoader(final String hash) {
        this.beanName = "DynamicMapperClassLoader" + hash;
    }

    public Class<?> getEntityClazz() {
        return entityClazz;
    }

    public void setEntityClazz(final Class<?> entityClazz) {
        this.entityClazz = entityClazz;
    }

    public Class<?> getMapperClazz() {
        return mapperClazz;
    }

    public void setMapperClazz(final Class<?> mapperClazz) {
        this.mapperClazz = mapperClazz;
    }

    public Class<?> getServiceClazz() {
        return serviceClazz;
    }

    public void setServiceClazz(final Class<?> serviceClazz) {
        this.serviceClazz = serviceClazz;
    }

    public String getBeanName() {
        return beanName;
    }

    public void setBeanName(final String beanName) {
        this.beanName = beanName;
    }

    public String getMapperBeanName() {
        return mapperBeanName;
    }

    public void setMapperBeanName(final String mapperBeanName) {
        this.mapperBeanName = mapperBeanName;
    }

    public String getServiceBeanName() {
        return serviceBeanName;
    }

    public void setServiceBeanName(final String serviceBeanName) {
        this.serviceBeanName = serviceBeanName;
    }

    public void uninstall(){
        //entity
        this.entityClazz = null;
        SpringUtils.removeBeanDefinition(this.beanName);
        //mapper
        final SqlSessionFactory sessionFactory = ContextUtils.getBean(SqlSessionFactory.class);
        ((MybatisConfiguration)sessionFactory.getConfiguration()).removeMapper(this.mapperClazz);
        final Object mapperBean = ContextUtils.getBean(mapperClazz);
        SpringUtils.destroyBean(mapperBean);
        this.mapperClazz = null;
        //service
        final Object serviceBean = ContextUtils.getBean(serviceClazz);
        Class<?> aClass = serviceBean.getClass();
        aClass = null;
        SpringUtils.destroyBean(serviceBean);

        BeanUnloader.unloadBean(this, SpringUtils.getContext(), this.beanName);
        BeanUnloader.unloadBean(this, SpringUtils.getContext(), this.mapperBeanName);
        BeanUnloader.unloadBean(this, SpringUtils.getContext(), this.serviceBeanName);
        this.beanName = null;
        this.mapperBeanName = null;
        this.serviceBeanName = null;
    }
}
