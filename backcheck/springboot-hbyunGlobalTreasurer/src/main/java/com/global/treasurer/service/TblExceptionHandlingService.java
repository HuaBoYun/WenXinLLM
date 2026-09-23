package com.global.treasurer.service;

import com.github.pagehelper.PageInfo;
import com.global.treasurer.entity.TblExceptionHandling;

import java.util.List;

/**
 * 异常处理Service接口
 * @author Claude
 * @date 2026-01-20
 */
public interface TblExceptionHandlingService {

    /**
     * 分页查询异常处理列表
     */
    PageInfo<TblExceptionHandling> getExceptionPage(Integer pageNum, Integer pageSize,
                                                    String exceptionNo, String exceptionType,
                                                    String exceptionLevel, String exceptionStatus,
                                                    String startDate, String endDate);

    /**
     * 根据ID查询异常处理
     */
    TblExceptionHandling getExceptionById(String exceptionId);

    /**
     * 保存异常记录
     */
    TblExceptionHandling saveException(TblExceptionHandling exception);

    /**
     * 更新异常记录
     */
    void updateException(TblExceptionHandling exception);

    /**
     * 处理异常
     */
    void handleException(String exceptionId, String handleMethod, String handleResult);

    /**
     * 重试异常
     */
    String retryException(String exceptionId);

    /**
     * 关闭异常
     */
    void closeException(String exceptionId, String closeRemark);

    /**
     * 删除异常记录（逻辑删除）
     */
    void deleteException(String exceptionId);

    /**
     * 获取待处理的异常列表
     */
    List<TblExceptionHandling> getPendingExceptionList();

    /**
     * 获取异常统计
     */
    java.util.Map<String, Object> getExceptionStatistics();

    /**
     * 测试方法：验证 exceptionLevel 参数编译问题
     */
    String testExceptionLevel(String exceptionLevel);
}

