package com.vickllny.distributedcache.config;

import com.vickllny.distributedcache.tx.CacheTransactionManager;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {

//    @Bean
    public PlatformTransactionManager platformTransactionManager(DataSource dataSource){
        return new DataSourceTransactionManager(dataSource);
    }


    @Bean
    @ConditionalOnClass(PlatformTransactionManager.class)
    public BeanPostProcessor cacheTransactionManagerPostProcessor(){
        return new BeanPostProcessor() {
            @Override
            public Object postProcessAfterInitialization(final Object bean, final String beanName) throws BeansException {
                if(bean instanceof PlatformTransactionManager){
//                    return new CacheTransactionManager((PlatformTransactionManager) bean);
                }
                return bean;
            }
        };
    }
}
