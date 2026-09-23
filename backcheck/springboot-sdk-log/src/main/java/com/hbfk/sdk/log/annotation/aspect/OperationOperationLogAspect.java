package com.hbfk.sdk.log.annotation.aspect;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.hbfk.sdk.log.beans.OperationLog;
import com.hbfk.sdk.log.beans.OperationLogOps;
import com.hbfk.sdk.log.context.OperationLogContext;
import com.hbfk.sdk.log.context.SqlLogHolder;
import com.hbfk.sdk.log.support.IpUtil;
import com.hbfk.sdk.log.support.OperationLogUtil;
import com.hbfk.sdk.log.support.listener.event.OperationLogEvent;
import com.hbfk.sdk.log.support.parse.OperationLogValueParser;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.ApplicationContext;
import org.springframework.util.CollectionUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description: 业务日志切面类
 * @Author: 61
 */
@Slf4j
@Aspect
@Setter
public class OperationOperationLogAspect {
    private static final String TRACE_ID_HEADER = "X-Trace-Id";

    private OperationLogUtil operationLogUtil;
    private String module;
    private OperationLogValueParser operationLogValueParser;
    private ApplicationContext applicationContext;


    @Around(
            "(@annotation(com.hbfk.sdk.log.annotation.OperationLog) || " +
                    "@annotation(com.hbfk.sdk.log.annotation.OperationLogs)) && " +
                    "(@within(org.springframework.web.bind.annotation.RestController) || " +
                    "@within(org.springframework.stereotype.Controller))"
    )
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        Collection<OperationLogOps> operations = new ArrayList<>();
        Method targetMethod = null;
        Class<? extends MethodSignature> targetClass = null;
        Object[] targetArgs = null;
        OperationLog operationLog = OperationLogContext.getOperationLog();
        log.info("OperationLog:前置处理开始");
        try {

            if (null == operationLog || StrUtil.isBlank(operationLog.getUserId())) {
                log.info("OperationLog:没有用户信息不记录该条信息");
                return joinPoint.proceed();
            }
            MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
            targetMethod = methodSignature.getMethod();
            targetClass = methodSignature.getClass();
            targetArgs = joinPoint.getArgs();
            // 获取 目标方法上的 operationLog 注解操作信息 支持一个方法多个注解
            operations = operationLogUtil.operationLogAnnotations(targetMethod);

        } catch (Exception e) {
            log.error("OperationLog:前置处理异常:{}", e.toString());
        }

        ExeRes m = new ExeRes();
        Object ret = null;
        try {
            log.info("OperationLog:目标方法处理开始");
            ret = joinPoint.proceed();
            m.setResult(ret);
            return ret;
        } catch (Throwable e) {
            // 获取完整的异常堆栈信息
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String fullStackTrace = sw.toString();
            // 截取前 500 个字符，避免超出数据库字段长度
            String truncatedError = fullStackTrace.length() > 500 ? fullStackTrace.substring(0, 500) : fullStackTrace;
            // 设置错误信息
            m.setErrorMsg(truncatedError);
            // 继续抛出异常
            throw e;
        } finally {

            log.info("OperationLog:后置处理开始,operations:[{}]", operations);
            try {
                if (!CollectionUtils.isEmpty(operations)) {
                    execute(ret, targetMethod, targetArgs, operations, targetClass,
                            m.isSuccess(), m.getErrorMsg(), operationLog, start);
                }
            } catch (Exception ex) {
                log.error("OperationLog:后置处理异常:{}", ex.toString());
            } finally {
                OperationLogContext.removeOperationLog();
            }
        }
    }

    private void execute(Object ret, Method m, Object[] args, Collection<OperationLogOps> operations,
                         Class<?> c, boolean success, String errorMsg, OperationLog operationLog, long start) {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = (HttpServletRequest) Objects.requireNonNull(requestAttributes).resolveReference(RequestAttributes.REFERENCE_REQUEST);
        assert request != null;
        for (OperationLogOps operation : operations) {
            String action = getAction(success, operation);
            if (StrUtil.isEmpty(action) && StrUtil.isEmpty(operation.getExtra())) {
                //没有日志内容则忽略
                continue;
            }
            // 获取需要表达式解析的字段值
            List<String> spEl = getSpElTemplates(operation, action);

            // 处理模版
            Map<String, String> values = operationLogValueParser.processTemplate(spEl, ret, c, m, args, errorMsg);
            long end = System.currentTimeMillis();
            // 获取当前线程的 SQL 日志
            List<String> sqlLogs = SqlLogHolder.getLogs();

            // 清理 ThreadLocal 避免内存泄漏
            SqlLogHolder.clear();
            operationLog.buildOperationLog(
                    request.getHeader(TRACE_ID_HEADER),
                    IpUtil.getIpAddr(request),
                    module,
                    JSONUtil.toJsonStr(filterArgs(args)),
                    JSONUtil.toJsonStr(ret),
                    getCodeInfo(m),
                    MapUtil.getStr(values, action, ""),
                    operation.getStartTime(),
                    (end - start),
                    JSONUtil.toJsonStr(sqlLogs),
                    operation.getBusType(),
                    operation.getSubType(),
                    errorMsg,
                    success ? 0 : 1
            );

            // 发布 OperationLogEvent 异步自定义订阅处理
            applicationContext.publishEvent(new OperationLogEvent(operationLog));
        }
    }

    /**
     * @Description: 获取代码类方法信息
     * @Param: [method]
     * @return: java.lang.String
     * @Author: 61
     */
    private String getCodeInfo(Method method) {
        return method.getDeclaringClass() + "#" + method.getName();
    }

    /**
     * @Description: 获取SpEL
     * @Param: [operation, action]
     * @return: java.util.List<java.lang.String>
     * @Author: 61
     */
    private List<String> getSpElTemplates(OperationLogOps operation, String action) {
        return CollectionUtil.newArrayList(operation.getBusNo(), action, operation.getExtra());
    }

    private String getAction(boolean success, OperationLogOps o) {
        return success ? o.getSuccessTemplate() : o.getFailTemplate();
    }

    private List<Object> filterArgs(Object[] objects) {
        return Arrays.stream(objects).filter(obj -> !(obj instanceof MultipartFile)
                && !(obj instanceof HttpServletResponse)
                && !(obj instanceof HttpServletRequest)).collect(Collectors.toList());
    }

    @Getter
    @NoArgsConstructor
    static class ExeRes {
        private boolean success;
        @Setter
        private Throwable throwable;
        private String errorMsg;
        private Object result;

        public void setResult(Object result) {
            this.success = true;
            this.result = result;
        }

        public void setErrorMsg(String errorMsg) {
            this.success = false;
            this.errorMsg = errorMsg;
        }
    }
}
