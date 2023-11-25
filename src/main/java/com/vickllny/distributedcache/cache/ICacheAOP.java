package com.vickllny.distributedcache.cache;

import com.vickllny.distributedcache.tx.CacheTransactionManager;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;

public interface ICacheAOP {

    String FIND_PREFIX = "find";
    String LIST_PREFIX = "list";
    String COUNT_PREFIX = "count";
    String GET_PREFIX = "get";
    String SELECT_PREFIX = "select";
    String SAVE_PREFIX = "save";
    String UPDATE_PREFIX = "update";
    String DELETE_PREFIX = "delete";
    String REMOVE_PREFIX = "remove";
    String UNDER_LINE = "_";



    default Object doAround(ProceedingJoinPoint pjp) throws Throwable{
        final MethodSignature joinPointObject = (MethodSignature) pjp.getSignature();
        final Method method = joinPointObject.getMethod();

        final String methodName = method.getName();
        final CacheConfig cacheConfig = method.getAnnotation(CacheConfig.class);
        String namespace = cacheConfig.namespace();
        if(StringUtils.isBlank(namespace)){
            namespace = pjp.getTarget().getClass().getSimpleName();
        }

        Object result;
        if(methodName.startsWith(FIND_PREFIX) ||
                methodName.startsWith(GET_PREFIX) ||
                methodName.startsWith(LIST_PREFIX) ||
                methodName.startsWith(COUNT_PREFIX) ||
                methodName.startsWith(SELECT_PREFIX)){
            final String key = getKey(pjp, namespace, cacheConfig);
            result = getValue(key);
            if(result == null){
                result = pjp.proceed();
                setValue(key, result, cacheConfig);
            }
        }else if(methodName.startsWith(SAVE_PREFIX) ||
                methodName.startsWith(UPDATE_PREFIX) ||
                methodName.startsWith(DELETE_PREFIX) ||
                methodName.startsWith(REMOVE_PREFIX)){
            result = pjp.proceed();
            if(result != null && (boolean) result){
                updateCache(namespace);
            }
        }else {
            result = pjp.proceed();
        }
        return result;
    }

    default String getKey(ProceedingJoinPoint pjp, final String namespace, final CacheConfig cacheConfig){
        final StringBuilder sb = new StringBuilder();
        MethodSignature joinPointObject = (MethodSignature) pjp.getSignature();
        sb.append(UNDER_LINE).append(namespace).append(UNDER_LINE).append(joinPointObject.getMethod().getName()).append(UNDER_LINE);
        final Object[] args = pjp.getArgs();
        for (int i = 0; i < args.length; i++) {
            if(args[i]!= null){
                sb.append(args[i].getClass().getSimpleName()).append(UNDER_LINE).append(args[i].toString()).append(UNDER_LINE);
            }else{
                sb.append("null");
            }
        }
        return sb.toString();
    }

    Object getValue(final String key);

    void setValue(final String key, final Object value, final CacheConfig cacheConfig);

    void updateCache(final String namespace);

}
