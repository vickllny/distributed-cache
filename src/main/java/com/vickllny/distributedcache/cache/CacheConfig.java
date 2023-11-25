package com.vickllny.distributedcache.cache;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface CacheConfig {
    String namespace() default "";
    String key() default "";

    boolean deleteNamespace() default false;

    long expired() default 1800L;

    TimeUnit timeUnit() default TimeUnit.SECONDS;
}
