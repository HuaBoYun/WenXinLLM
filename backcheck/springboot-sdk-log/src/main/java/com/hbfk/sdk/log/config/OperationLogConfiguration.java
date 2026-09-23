package com.hbfk.sdk.log.config;

import com.hbfk.sdk.log.annotation.EnableOperationLog;
import com.hbfk.sdk.log.annotation.aspect.OperationOperationLogAspect;
import com.hbfk.sdk.log.support.OperationLogUtil;
import com.hbfk.sdk.log.support.listener.LogListener;
import com.hbfk.sdk.log.support.parse.LogFunctionParser;
import com.hbfk.sdk.log.support.parse.OperationLogValueParser;
import com.hbfk.sdk.log.support.service.FunctionService;
import com.hbfk.sdk.log.support.service.IParseFunction;
import com.hbfk.sdk.log.support.service.impl.ParseFunctionFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportAware;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.task.TaskExecutor;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.annotation.Resource;
import java.util.List;

/**
* @Description: OperationLog 配置
* @Author: 61
*/
@Slf4j
@EnableAsync
@EnableConfigurationProperties({LogThreadProperties.class})
@ConfigurationPropertiesScan("com.hbfk.sdk.log.config")
public class OperationLogConfiguration implements ImportAware {

    private AnnotationAttributes enableLogRecord;

    @Resource
    LogThreadProperties logThreadProperties;


    @Bean
    public OperationLogUtil operationLogUtil() {
        return new OperationLogUtil();
    }

    @Bean
    public FunctionService functionService(ParseFunctionFactory parseFunctionFactory) {
        return new FunctionService(parseFunctionFactory);
    }

    @Bean
    public ParseFunctionFactory parseFunctionFactory(@Autowired List<IParseFunction> parseFunctions) {
        return new ParseFunctionFactory(parseFunctions);
    }

    @Bean
    public OperationLogValueParser operationLogValueParser(LogFunctionParser logFunctionParser){
        OperationLogValueParser o = new OperationLogValueParser();
        o.setLogFunctionParser(logFunctionParser);
        return o;
    }

    @Bean
    public LogFunctionParser logFunctionParser(FunctionService functionService) {
        return new LogFunctionParser(functionService);
    }

    // 切面
    @Bean
    public OperationOperationLogAspect operationLogAspect(OperationLogValueParser operationLogValueParser,
                                                          OperationLogUtil operationLogUtil,
                                                          ApplicationContext applicationContext) {
        OperationOperationLogAspect o = new OperationOperationLogAspect();
            o.setModule(enableLogRecord.getString("module"));
            o.setOperationLogValueParser(operationLogValueParser);
            o.setOperationLogUtil(operationLogUtil);
            o.setApplicationContext(applicationContext);
        return o;
    }

    // 事件监听器
    @Bean
    public LogListener logListener(){
        return new LogListener();
    }

    @Bean
    @Primary
    public TaskExecutor taskExecutorLog() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
            executor.setCorePoolSize(logThreadProperties.getCorePoolSize());
            executor.setMaxPoolSize(logThreadProperties.getMaxPoolSize());
            executor.setQueueCapacity(logThreadProperties.getQueueCapacity());
            executor.setKeepAliveSeconds(logThreadProperties.getKeepAliveSeconds());
            executor.setThreadNamePrefix(logThreadProperties.getThreadNamePrefix());
        return executor;
    }


    @Override
    public void setImportMetadata(AnnotationMetadata importMetadata) {
        this.enableLogRecord = AnnotationAttributes.fromMap(
                importMetadata.getAnnotationAttributes(EnableOperationLog.class.getName(), false));
    }
}
