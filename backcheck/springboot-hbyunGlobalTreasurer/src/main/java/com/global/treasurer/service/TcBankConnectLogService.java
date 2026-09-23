package com.global.treasurer.service;

import com.github.pagehelper.PageInfo;
import com.global.treasurer.entity.TcBankConnectLog;
import java.util.List;
import java.util.Map;

/**
 * 银企直连日志Service接口
 * @author AI Assistant
 * @date 2025-01-26
 */
public interface TcBankConnectLogService {

    /**
     * 分页查询日志列表
     */
    PageInfo<TcBankConnectLog> list(Integer pageNum, Integer pageSize, Map<String, Object> params);

    /**
     * 根据ID获取日志详情
     */
    TcBankConnectLog getById(String logId);

    /**
     * 根据请求ID获取日志
     */
    TcBankConnectLog getByRequestId(String requestId);

    /**
     * 保存日志
     */
    boolean save(TcBankConnectLog log);

    /**
     * 更新日志
     */
    boolean update(TcBankConnectLog log);

    /**
     * 删除日志
     */
    boolean delete(String logId);

    /**
     * 批量删除日志
     */
    boolean batchDelete(List<String> logIds);

    /**
     * 获取日志统计信息
     */
    Map<String, Object> getStatistics();

    /**
     * 重试请求
     */
    Map<String, Object> retryRequest(String logId);

    /**
     * 清理历史日志
     */
    int cleanHistoryLogs(Integer days);

    /**
     * 导出日志
     */
    List<TcBankConnectLog> exportLogs(Map<String, Object> params);

    /**
     * 获取失败日志列表
     */
    List<TcBankConnectLog> getFailedLogs(Integer limit);
}

