package com.vickllny.distributedcache;

import com.vickllny.distributedcache.annotations.EnableASyncTask;
import com.vickllny.distributedcache.service.impl.TestService;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.vickllny.distributedcache.mapper")
@EnableASyncTask(basePackages = "com.vickllny")
public class DistributedCacheApplication implements CommandLineRunner {

	@Autowired
	private TestService testService;

	public static void main(String[] args) {
		SpringApplication.run(DistributedCacheApplication.class, args);
	}

	@Override
	public void run(final String... args) throws Exception {
		testService.test();
	}
}
