package com.huabo.system.aop;

import com.alibaba.fastjson.JSON;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.sdk.log.annotation.OperationLog;
import com.hbfk.util.redis.Random.RandomUtil;
import com.hbfk.util.user.UserProvider;
import com.huabo.system.entity.TblUserRequestLog;
import com.huabo.system.service.TblUserRequestLogService;
import com.huabo.system.util.IpUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Date;

/**
 * 用户请求日志 AOP 切面
 * 拦截所有带 @OperationLog 注解的方法，自动记录用户请求日志到 TBL_USER_REQUEST_LOG 表
 * 
 * @author Augment Agent
 * @date 2025-01-21
 */
@Slf4j
@Aspect
@Component
public class UserRequestLogAspect {

    @Autowired
    private TblUserRequestLogService tblUserRequestLogService;

    @Autowired
    private UserProvider userProvider;

    // 线程本地变量存储日志信息
    private static final ThreadLocal<TblUserRequestLog> LOG_THREAD_LOCAL = new ThreadLocal<>();
    private static final ThreadLocal<Long> START_TIME_THREAD_LOCAL = new ThreadLocal<>();

    /**
     * 前置通知：记录请求开始信息
     */
    @Before("@annotation(operationLog)")
    public void beforeMethod(JoinPoint joinPoint, OperationLog operationLog) {
        try {
            long startTime = System.currentTimeMillis();
            START_TIME_THREAD_LOCAL.set(startTime);

            // 获取 HTTP 请求对象
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            HttpServletRequest request = attributes != null ? attributes.getRequest() : null;

            // 创建日志对象
            TblUserRequestLog logRecord = new TblUserRequestLog();

            // 获取用户信息
            TblStaffUtil staff = userProvider.get();
            if (staff != null) {
                logRecord.setUserId(staff.getStaffid() != null ? staff.getStaffid().longValue() : null);
                logRecord.setUserName(staff.getRealname());
                logRecord.setUserAccount(staff.getUsername());
            }

            // 获取请求信息
            if (request != null) {
                logRecord.setIp(IpUtil.getClientIp(request));
                logRecord.setRequestClassMethod(joinPoint.getTarget().getClass().getName() + "#" + joinPoint.getSignature().getName());
            }

            // 获取操作信息
            logRecord.setActionDescription(operationLog.success());
            logRecord.setBusinessModule(operationLog.busType());
            logRecord.setSubBusinessModule(operationLog.subType());
            logRecord.setProjectModule("系统设置服务");

            // 记录请求参数
            Object[] args = joinPoint.getArgs();
            if (args != null && args.length > 0) {
                try {
                    logRecord.setRequestPayload(JSON.toJSONString(args));
                } catch (Exception e) {
                    logRecord.setRequestPayload("参数序列化失败");
                }
            }

            // 设置请求时间
            logRecord.setRequestTime(new Date());
            logRecord.setErrorFlag(0);

            // 存储到线程本地变量
            LOG_THREAD_LOCAL.set(logRecord);

            log.debug("用户请求日志 AOP 前置处理完成: method={}, user={}", 
                joinPoint.getSignature().getName(), logRecord.getUserName());

        } catch (Exception e) {
            log.error("用户请求日志 AOP 前置处理异常", e);
        }
    }

    /**
     * 后置返回通知：记录响应信息
     */
    @AfterReturning(pointcut = "@annotation(operationLog)", returning = "result")
    public void afterReturning(JoinPoint joinPoint, OperationLog operationLog, Object result) {
        try {
            TblUserRequestLog logRecord = LOG_THREAD_LOCAL.get();
            if (logRecord == null) {
                return;
            }

            // 记录响应数据
            if (result != null) {
                try {
                    logRecord.setResponsePayload(JSON.toJSONString(result));
                } catch (Exception e) {
                    logRecord.setResponsePayload("响应序列化失败");
                }
            }

            // 计算耗时
            Long startTime = START_TIME_THREAD_LOCAL.get();
            if (startTime != null) {
                long duration = System.currentTimeMillis() - startTime;
                logRecord.setRequestDurationMs(duration);
                logRecord.setEndTime(new Date());
            }

            // 保存到数据库
//            tblUserRequestLogService.saveRequestLog(logRecord);

            log.debug("用户请求日志 AOP 后置处理完成: method={}, duration={}ms",
                joinPoint.getSignature().getName(), logRecord.getRequestDurationMs());

        } catch (Exception e) {
            log.error("用户请求日志 AOP 后置处理异常", e);
        } finally {
            // 清理线程本地变量
            LOG_THREAD_LOCAL.remove();
            START_TIME_THREAD_LOCAL.remove();
        }
    }

    /**
     * 异常通知：记录异常信息
     */
    @AfterThrowing(pointcut = "@annotation(operationLog)", throwing = "ex")
    public void afterThrowing(JoinPoint joinPoint, OperationLog operationLog, Exception ex) {
        try {
            TblUserRequestLog logRecord = LOG_THREAD_LOCAL.get();
            if (logRecord == null) {
                return;
            }

            // 记录异常信息
            logRecord.setError(ex.getMessage());
            logRecord.setErrorFlag(1);
            logRecord.setResponsePayload("异常: " + ex.getClass().getName());

            // 计算耗时
            Long startTime = START_TIME_THREAD_LOCAL.get();
            if (startTime != null) {
                long duration = System.currentTimeMillis() - startTime;
                logRecord.setRequestDurationMs(duration);
                logRecord.setEndTime(new Date());
            }
            logRecord.setId(RandomUtil.uuLongId());
            // 保存到数据库
            tblUserRequestLogService.saveRequestLog(logRecord);

            log.error("用户请求日志 AOP 异常处理完成: method={}, error={}", 
                joinPoint.getSignature().getName(), ex.getMessage());

        } catch (Exception e) {
            log.error("用户请求日志 AOP 异常处理异常", e);
        } finally {
            // 清理线程本地变量
            LOG_THREAD_LOCAL.remove();
            START_TIME_THREAD_LOCAL.remove();
        }
    }

}

