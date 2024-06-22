package com.vickllny.distributedcache;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
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
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.JdbcType;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.mapper.MapperFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ReflectionUtils;

import javax.sql.DataSource;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.Statement;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@EnableScheduling
@SpringBootApplication
@MapperScan("com.vickllny.distributedcache.mapper")
@EnableASyncTask(basePackages = "com.vickllny")
public class DistributedCacheApplication implements CommandLineRunner {

	@Autowired
	private TestService testService;
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	@Autowired
	private List<BaseMapper> baseMappers;
	@Autowired
	private SqlSessionFactory sqlSessionFactory;

	@Autowired
	private ConfigurableApplicationContext context;

	public static void main(String[] args) {
		SpringApplication.run(DistributedCacheApplication.class, args);
	}

	@Override
	public void run(final String... args) throws Exception {
		final String insertSql = "insert into t_user_test(id,user_name,login_name,password,_lock) values(:id,:userName,:loginName,:password,:lock)";
		final MapSqlParameterSource parameterSource = new MapSqlParameterSource();
		namedParameterJdbcTemplate.update(insertSql, new SqlParameterSource() {
			@Override
			public boolean hasValue(final String paramName) {
				return false;
			}

			@Override
			public Object getValue(final String paramName) throws IllegalArgumentException {
				return null;
			}
		})


		final String hash = hash(UUID.randomUUID().toString(), 6);
//		final String hash = "test";

//
//		Class<? extends DynamicMapperClassLoader> classLoaderType = new ByteBuddy()
//				.subclass(DynamicMapperClassLoader.class)
//				.name(DynamicMapperClassLoader.class.getPackage().getName() + ".DynamicMapperClassLoader" + hash)
//				.make()
//				.load(getClass().getClassLoader(), ClassLoadingStrategy.Default.WRAPPER)
//				.getLoaded();
//
//		final DynamicMapperClassLoader loader = classLoaderType.getDeclaredConstructor(String.class).newInstance(hash);
//
//
//		//生成entityClass
//		String tableName = "t_user_" + hash;
//		final DynamicType.Unloaded<User> dynamicType = new ByteBuddy()
//				.subclass(User.class)
//				.annotateType(AnnotationDescription.Builder.ofType(TableName.class)
//						.define("value", tableName).build())
//				.defineProperty("password", String.class)
//				.annotateField(AnnotationDescription.Builder.ofType(TableField.class)
//						.define("value", "password").build())
//				.defineProperty("lock", String.class)
//				.annotateField(AnnotationDescription.Builder.ofType(TableField.class)
//						.define("value", "_lock").build())
//				.name("com.vickllny.distributedcache.domain.User" + StringUtils.capitalize(hash))
//				.make();
//
//		final Class<? extends User> entityClass = dynamicType.load(loader, ClassLoadingStrategy.Default.INJECTION).getLoaded();
//
//		createUserMySQLTable(tableName, Stream.of("password", "_lock").collect(Collectors.toList()));
//
//		//生成mapper
//		final DynamicType.Loaded<?> mapperLoad = new ByteBuddy()
//				.makeInterface(TypeDescription.Generic.Builder.parameterizedType(BaseMapper.class, entityClass).build())
//				.name(String.format("com.vickllny.distributedcache.mapper.%sMapper", entityClass.getSimpleName()))
//				.make()
//				.load(loader, ClassLoadingStrategy.Default.INJECTION);
//		final Class<?> mapperClass = mapperLoad.getLoaded();
//		MapperFactoryBean<?> factoryBean = new MapperFactoryBean<>(mapperClass);
//		factoryBean.setSqlSessionFactory(sqlSessionFactory);
//		sqlSessionFactory.getConfiguration().addMapper(mapperClass);
//		Object mapperBeanObject = factoryBean.getObject();
//		SpringUtils.registerBean(getBeanName(mapperClass.getSimpleName()), mapperBeanObject);
//		loader.setMapperBeanName(getBeanName(mapperClass.getSimpleName()));
//		final User user = entityClass.getDeclaredConstructor().newInstance();
//		user.setId("12312312312");
//		user.setUserName("aaaaaa");
//		user.setLoginName("aaaaaa");
//		ReflectionUtils.invokeMethod(entityClass.getDeclaredMethod("setPassword", String.class), user, "1234556");
//		ReflectionUtils.invokeMethod(entityClass.getDeclaredMethod("setLock", String.class), user, "false");
//		//测试保存
//		((BaseMapper)SpringUtils.getBean(mapperClass)).insert(user);
//
//		final DynamicType.Loaded<?> serviceLoad = new ByteBuddy()
//				.subclass(TypeDescription.Generic.Builder.parameterizedType(CommonUserServiceImpl.class, mapperClass, entityClass).build())
//				.name(String.format("com.vickllny.distributedcache.service.impl.%sServiceImpl", entityClass.getSimpleName()))
//				.make()
//				.load(loader, ClassLoadingStrategy.Default.INJECTION);
//		final Class<?> serviceClass = serviceLoad.getLoaded();
//
//		Enhancer enhancer = new Enhancer();
//		enhancer.setSuperclass(serviceClass);
//		enhancer.setClassLoader(loader);
//		enhancer.setCallback((MethodInterceptor) (obj, method, args1, proxy) -> {
//            // 自定义方法拦截逻辑
//            return proxy.invokeSuper(obj, args1);
//        });
//		CommonUserServiceImpl serviceBeanObject = (CommonUserServiceImpl)enhancer.create();
//
//		enhancer = null;
//
//		serviceBeanObject.setBaseMapper((BaseMapper) mapperBeanObject);
//		SpringUtils.registerBean(getBeanName(serviceClass.getSimpleName()), serviceBeanObject);
//		loader.setServiceBeanName(getBeanName(serviceClass.getSimpleName()));
//		List list = ((ServiceImpl) SpringUtils.getBean(serviceClass)).list();
//		System.out.println(list);
//
//		loader.setEntityClazz(entityClass);
//		loader.setMapperClazz(mapperClass);
//		loader.setServiceClazz(serviceClass);
//
//		SpringUtils.registerBeanDefinition("DynamicMapperClassLoader" + hash, (Class<? super DynamicMapperClassLoader>) classLoaderType, loader);
//		classLoaderType = null;
	}

	@Scheduled(fixedDelay = 10000)
	public void destroyBean(){
		final List<DynamicMapperClassLoader> list = ContextUtils.getBeans(DynamicMapperClassLoader.class);
		if(CollectionUtils.isEmpty(list)){
			return;
		}
		for (DynamicMapperClassLoader loader : list) {
			loader.uninstall();
			loader = null;
			System.gc();
		}
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

	void createUserMySQLTable(final String tableName, final List<String> columnList){
		StringBuffer sql = new StringBuffer("CREATE TABLE `").append(tableName).append("` (\n");
		sql.append("  `id` varchar(100) DEFAULT NULL,\n");
		sql.append("  `user_name` varchar(100) DEFAULT NULL,\n");
		sql.append("  `login_name` varchar(100) DEFAULT NULL,\n");
		for (int i = 0; i < columnList.size(); i++) {
			String column = columnList.get(i);
			sql.append("`").append(column).append("` varchar(100) DEFAULT NULL");
			if(i != columnList.size() - 1){
				sql.append(",");
			}
		}
		sql.append(" ) ENGINE=InnoDB DEFAULT CHARSET=utf8;");
		System.out.println(sql);
		executeDDL(sql.toString());
	}

	void executeDDL(final String ddl){
		final DataSource dataSource = ContextUtils.getBean(DataSource.class);
		try (final Connection connection = dataSource.getConnection();
			 final Statement statement = connection.createStatement()){
			statement.execute(ddl);
		}catch (Exception e){
			e.printStackTrace();
		}
	}
}
