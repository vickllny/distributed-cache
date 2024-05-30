package com.vickllny.distributedcache;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.ClassUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.hash.Hashing;
import com.vickllny.distributedcache.annotations.EnableASyncTask;
import com.vickllny.distributedcache.classloader.DynamicMapperClassLoader;
import com.vickllny.distributedcache.config.SpringUtils;
import com.vickllny.distributedcache.domain.AdminUser;
import com.vickllny.distributedcache.domain.User;
import com.vickllny.distributedcache.service.IAdminUserService;
import com.vickllny.distributedcache.service.ICommonUserService;
import com.vickllny.distributedcache.service.impl.CommonUserServiceImpl;
import com.vickllny.distributedcache.service.impl.TestService;
import com.vickllny.distributedcache.utils.ContextUtils;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.description.annotation.AnnotationDescription;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import net.bytebuddy.implementation.FieldAccessor;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatchers;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.mapper.MapperFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
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
//		final String hash = hash(UUID.randomUUID().toString(), 6);
		final String hash = "test";

		DynamicMapperClassLoader loader = new DynamicMapperClassLoader();

//		Class<?> dynamicType1 = new ByteBuddy()
//				.subclass(Object.class)
//				.defineField("username", String.class, Modifier.PRIVATE)
//				.defineField("password", String.class)
//				.name("com.vickllny.distributedcache.DynamicUser")
//				.make()
//				.load(getClass().getClassLoader(), ClassLoadingStrategy.Default.WRAPPER)
//				.getLoaded();


		//生成entityClass
		final DynamicType.Unloaded<User> dynamicType = new ByteBuddy()
				.subclass(User.class)
				.annotateType(AnnotationDescription.Builder.ofType(TableName.class)
						.define("value", "t_user_" + hash).build())
				.defineProperty("password", String.class)
				.annotateField(AnnotationDescription.Builder.ofType(TableField.class)
						.define("value", "password").build())
				.defineProperty("lock", String.class)
				.annotateField(AnnotationDescription.Builder.ofType(TableField.class)
						.define("value", "_lock").build())
				.name("com.vickllny.distributedcache.domain.User" + StringUtils.capitalize(hash))
				.make();

		final Class<? extends User> entityClass = dynamicType.load(loader, ClassLoadingStrategy.Default.INJECTION).getLoaded();


		//生成mapper
		final DynamicType.Loaded<?> mapperLoad = new ByteBuddy()
				.makeInterface(TypeDescription.Generic.Builder.parameterizedType(BaseMapper.class, entityClass).build())
				.name(String.format("com.vickllny.distributedcache.mapper.%sMapper", entityClass.getSimpleName()))
				.make()
				.load(loader, ClassLoadingStrategy.Default.INJECTION);
		final Class<?> mapperClass = mapperLoad.getLoaded();
		MapperFactoryBean<?> factoryBean = new MapperFactoryBean<>(mapperClass);
		factoryBean.setSqlSessionFactory(sqlSessionFactory);
		sqlSessionFactory.getConfiguration().addMapper(mapperClass);
		Object mapperBeanObject = factoryBean.getObject();
		SpringUtils.registerBean(getBeanName(mapperClass.getSimpleName()), mapperBeanObject);
		final User user = entityClass.getDeclaredConstructor().newInstance();
		user.setId("12312312312");
		user.setUserName("aaaaaa");
		user.setLoginName("aaaaaa");
		ReflectionUtils.invokeMethod(entityClass.getDeclaredMethod("setPassword", String.class), user, "1234556");
		//测试保存
//		((BaseMapper)SpringUtils.getBean(mapperClass)).insert(user);

		final DynamicType.Loaded<?> serviceLoad = new ByteBuddy()
				.subclass(TypeDescription.Generic.Builder.parameterizedType(CommonUserServiceImpl.class, mapperClass, entityClass).build())
				.name(String.format("com.vickllny.distributedcache.service.impl.%sServiceImpl", entityClass.getSimpleName()))
				.make()
				.load(loader, ClassLoadingStrategy.Default.INJECTION);
		final Class<?> serviceClass = serviceLoad.getLoaded();

		Enhancer enhancer = new Enhancer();
		enhancer.setSuperclass(serviceClass);
		enhancer.setClassLoader(loader);
		enhancer.setCallback((MethodInterceptor) (obj, method, args1, proxy) -> {
            // 自定义方法拦截逻辑
            return proxy.invokeSuper(obj, args1);
        });
		CommonUserServiceImpl serviceBeanObject = (CommonUserServiceImpl)enhancer.create();
		serviceBeanObject.setBaseMapper((BaseMapper) mapperBeanObject);
		SpringUtils.registerBean(getBeanName(serviceClass.getSimpleName()), serviceBeanObject);
		List list = ((ServiceImpl) SpringUtils.getBean(serviceClass)).list();
		//
		final IAdminUserService iAdminUserService = ContextUtils.getBean(IAdminUserService.class);
		final List<AdminUser> userList = iAdminUserService.findByUserName("vickllny");
		System.out.println(userList);
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
