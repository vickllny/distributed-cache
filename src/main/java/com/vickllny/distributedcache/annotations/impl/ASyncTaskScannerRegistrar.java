package com.vickllny.distributedcache.annotations.impl;

import com.vickllny.distributedcache.annotations.ASyncTask;
import com.vickllny.distributedcache.annotations.EnableASyncTask;
import org.apache.commons.lang3.ClassUtils;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import java.util.Map;

public class ASyncTaskScannerRegistrar implements ImportBeanDefinitionRegistrar, ResourceLoaderAware {

    @Override
    public void setResourceLoader(final ResourceLoader resourceLoader) {

    }

    @Override
    public void registerBeanDefinitions(final AnnotationMetadata importingClassMetadata, final BeanDefinitionRegistry registry) {
        final Map<String, Object> attributes = importingClassMetadata.getAnnotationAttributes(EnableASyncTask.class.getName());
        if (attributes != null) {
            ClassPathBeanDefinitionScanner scanner = new ClassPathBeanDefinitionScanner(registry, false);
            //注册自定义扫描的注解
            scanner.addIncludeFilter(new AnnotationTypeFilter(ASyncTask.class));

            //获取包扫描的路径
            String[] basePackages = (String[]) attributes.get("basePackages");
            if(basePackages == null){
                basePackages = new String[]{ClassUtils.getPackageName(importingClassMetadata.getClassName())};
            }

            //默认取主配置类下的包路径，springboot使用的就是这种方式
            //AutoConfigurationPackages.get(this.beanFactory);
            //ClassUtils.getPackageName("mainClass");

//            scanner.setBeanNameGenerator(new ASyncTaskBeanNameGenerator());
            //扫描的包路径
            scanner.scan(basePackages);
        }
    }

    static class ASyncTaskBeanNameGenerator implements BeanNameGenerator {
        @Override
        public String generateBeanName(final BeanDefinition definition, final BeanDefinitionRegistry registry) {
            return null;
        }
    }
}
