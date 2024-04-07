package com.vickllny.distributedcache.annotations;


import com.vickllny.distributedcache.annotations.impl.ASyncTaskScannerRegistrar;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import(ASyncTaskScannerRegistrar.class)
public @interface EnableASyncTask {

    String[] basePackages() default {};

}
