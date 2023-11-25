package com.vickllny.distributedcache;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.vickllny.distributedcache.mapper")
public class DistributedCacheApplication {

	public static void main(String[] args) {
		SpringApplication.run(DistributedCacheApplication.class, args);
	}

}
