package com.vickllny.distributedcache;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.ClassUtils;
import com.google.common.hash.Hashing;
import com.vickllny.distributedcache.annotations.EnableASyncTask;
import com.vickllny.distributedcache.config.SpringUtils;
import com.vickllny.distributedcache.domain.User;
import com.vickllny.distributedcache.service.impl.TestService;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.description.annotation.AnnotationDescription;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import net.bytebuddy.implementation.FieldAccessor;
import net.bytebuddy.matcher.ElementMatchers;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.mapper.MapperFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Modifier;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.UUID;

@SpringBootApplication
@MapperScan("com.vickllny.distributedcache.mapper")
@EnableASyncTask(basePackages = "com.vickllny")
public class DistributedCacheApplication implements CommandLineRunner {

	@Autowired
	private TestService testService;
	@Autowired
	private List<BaseMapper> baseMappers;
	@Autowired
	private SqlSessionFactory sqlSessionFactory;

	public static void main(String[] args) {
		SpringApplication.run(DistributedCacheApplication.class, args);
	}

	@Override
	public void run(final String... args) throws Exception {
		final String hash = hash(UUID.randomUUID().toString(), 6);

		//生成entityClass
		final DynamicType.Unloaded<User> dynamicType = new ByteBuddy()
				.subclass(User.class)
				.annotateType(AnnotationDescription.Builder.ofType(TableName.class)
						.define("value", "t_user_" + hash).build())
				.defineField("password", String.class)
				.annotateField(AnnotationDescription.Builder.ofType(TableField.class)
						.define("value", "password").build())
				.defineMethod("getPassword", String.class, Modifier.PUBLIC)
				.intercept(FieldAccessor.ofField("password"))
				.defineMethod("setPassword", void.class, Modifier.PUBLIC)
				.withParameters(String.class)
				.intercept(FieldAccessor.ofField("password"))
				.name("com.vickllny.distributedcache.domain.User" + StringUtils.capitalize(hash))
				.make();

		final Class<? extends User> entityClass = dynamicType.load(getClass().getClassLoader(), ClassLoadingStrategy.Default.WRAPPER).getLoaded();


		//生成mapper
		final DynamicType.Loaded<?> mapperLoad = new ByteBuddy()
				.makeInterface(TypeDescription.Generic.Builder.parameterizedType(BaseMapper.class, entityClass).build())
				.name(String.format("com.vickllny.distributedcache.mapper.%sMapper", entityClass.getSimpleName()))
				.make()
				.load(getClass().getClassLoader(), ClassLoadingStrategy.Default.INJECTION);
		final Class<?> mapperClass = mapperLoad.getLoaded();

		MapperFactoryBean<?> factoryBean = new MapperFactoryBean<>(mapperClass);
		factoryBean.setSqlSessionFactory(sqlSessionFactory);

		sqlSessionFactory.getConfiguration().addMapper(mapperClass);

		SpringUtils.registerBean(getBeanName(mapperClass.getSimpleName()), factoryBean.getObject());

		final User user = entityClass.getConstructor().newInstance();
		user.setId("12312312312");
		user.setUserName("aaaaaa");
		user.setLoginName("aaaaaa");
		ReflectionUtils.setField(entityClass.getField("password"), user, "1234556");

		//测试保存
		baseMappers.get(0).insert(user);
	}

	// 根据类名获取 bean name
	private static String getBeanName(String className) {

		int index = className.lastIndexOf(".");
		String simpleClassName = index != -1 ? className.substring(index + 1) : className;

		char firstChar = simpleClassName.charAt(0);
		if (firstChar >= 'A' && firstChar <= 'Z') {
			firstChar -= 'A' - 'a';
		}
		return firstChar + simpleClassName.substring(1);
	}

	String hash(String input, int length){
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			byte[] hashBytes = digest.digest(input.getBytes(StandardCharsets.UTF_8));

			// 将哈希字节数组转换为十六进制字符串
			StringBuilder hexString = new StringBuilder();
			for (byte hashByte : hashBytes) {
				String hex = Integer.toHexString(0xff & hashByte);
				if (hex.length() == 1) {
					hexString.append('0');
				}
				hexString.append(hex);
			}

			// 截取指定长度的哈希字符串
			return hexString.substring(0, length);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}
	}
}
