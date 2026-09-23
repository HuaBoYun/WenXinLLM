# 日志管理sdk
### 第一步增加pom依赖
```xml
    <!-- 业务日志管理 -->
    <dependency>
        <groupId>com.huabo.sdk.log</groupId>
        <artifactId>springboot-sdk-log</artifactId>
        <version>0.0.1-SNAPSHOT</version>
    </dependency>
```
### 第二步在启动类上添加注解
* EnableFeignClients: feign调用日志
* EnableOperationLog: 日志sdk注解（module=项目模块code）
```java
@EnableOperationLog(module = "hbyunComplianceModule")
@EnableFeignClients(basePackages = "com.hbfk.sdk.log")
```
### 增加扩展的拦截器
* 存放用户信息的拦截器
```java
@Component
public class OperationLogInterceptor implements HandlerInterceptor {
    private static final String TOKEN = "token";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String token = request.getHeader(TOKEN);
        if (StringUtils.hasText(token)) {
            TblStaffUtil staffUtil = LegalDealUserToken.parseUserToken(token);
            if (staffUtil == null) {
                return true;
            }
            // 补充用户信息
            OperationLogContext.setOperationLog(OperationLog.builder()
                    .userId(staffUtil.getStaffid().toString())
                    .userName(staffUtil.getRealname())
                    .userAccount(staffUtil.getUsername())
                    .build());
        }
        return true;
    }
}
```
* 拦截器添加到项目中
```java
@Configuration
public class WebMVCConfig extends WebMvcConfigurerAdapter {
    @Resource
    private OperationLogInterceptor operationLogInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(operationLogInterceptor).addPathPatterns("/**");
    }
}
```
### 添加logSdk里的（SqlLoggingInterceptor）拦截器到mybatis中
* 例子如下：
```java
@Bean(name = "oracleSqlSessionFactory")
@Primary
public SqlSessionFactory oracleSqlSessionFactory(@Qualifier(value = "oracleDataSource") DataSource dataSource,
        MybatisProperties mybatisProperties) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        // 这部分代码
        bean.setPlugins(new Interceptor[] {new SqlLoggingInterceptor()});
        return bean.getObject();
        }
```
### 接口入口类方法增加请求信息注解
* 例子如下：详见 OperationLog 注解描述
```java
	@OperationLog(
			success = "计划【{{#param.planName}}】新增成功",
			busType = "合规管理",
			fail = "计划【{{#param.planName}}】新增失败",
			operationType = OperationType.UPDATE,
			subType = "计划管理"
	)
```
