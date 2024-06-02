package com.vickllny.distributedcache.config;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.boot.web.servlet.context.AnnotationConfigServletWebServerApplicationContext;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class SpringUtils implements ApplicationContextAware {
    private static ApplicationContext context;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }

    public static void set(ApplicationContext applicationContext) {
        context = applicationContext;
    }

    /**
     * 通过字节码获取
     * @param beanClass
     * @param <T>
     * @return
     */
    public static <T> T getBean(Class<T> beanClass) {
        return context.getBean(beanClass);
    }

    /**
     * 通过BeanName获取
     * @param beanName
     * @param <T>
     * @return
     */
    public static <T> T getBean(String beanName) {
        return (T) context.getBean(beanName);
    }

    /**
     * 通过beanName和字节码获取
     * @param name
     * @param beanClass
     * @param <T>
     * @return
     */
    public static <T> T getBean(String name, Class<T> beanClass) {
        return context.getBean(name, beanClass);
    }


    public static <T> void removeBeanDefinition(final String beanName){
        ((AnnotationConfigServletWebServerApplicationContext) context).removeBeanDefinition(beanName);
    }
    public static <T> void registerBeanDefinition(final String beanName, final Class<T> clazz, T bean){
        final BeanDefinitionBuilder definition = BeanDefinitionBuilder.rootBeanDefinition(clazz, () -> bean);
        ((AnnotationConfigServletWebServerApplicationContext) context).registerBeanDefinition(beanName, definition.getBeanDefinition());
    }
    public static void registerBean(String beanName, Object bean){
        final ConfigurableListableBeanFactory beanFactory = ((AnnotationConfigServletWebServerApplicationContext) context).getBeanFactory();
        beanFactory.registerSingleton(beanName, bean);

    }

    public static void destroyBean(final Object bean){
        final ConfigurableListableBeanFactory beanFactory = ((AnnotationConfigServletWebServerApplicationContext) context).getBeanFactory();
        beanFactory.destroyBean(bean);
    }
}
