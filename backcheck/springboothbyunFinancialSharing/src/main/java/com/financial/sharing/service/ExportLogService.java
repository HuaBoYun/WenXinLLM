package com.financial.sharing.service;

import com.financial.sharing.oracle.entity.ExportLogEntity;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 导出日志服务接口
 */
public interface ExportLogService {

    /**
     * 记录导出日志
     */
    void logExport(ExportLogEntity logEntity);

    /**
     * 批量记录导出日志
     */
    void batchLogExport(List<ExportLogEntity> logEntities);

    /**
     * 查询导出日志列表
     */
    Page<ExportLogEntity> getExportLogPage(Long userId, String exportType,
                                         LocalDateTime startTime, LocalDateTime endTime,
                                         int pageNo, int pageSize);

    /**
     * 获取导出统计信息
     */
    Map<String, Object> getExportStatistics(Long userId, LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 获取用户今日导出统计
     */
    Map<String, Object> getTodayExportStatistics(Long userId);

    /**
     * 获取热门导出类型统计
     */
    List<Map<String, Object>> getPopularExportTypes(LocalDateTime startTime, LocalDateTime endTime, int limit);

    /**
     * 清理过期日志
     */
    void cleanExpiredLogs(int daysToKeep);

    /**
     * 获取导出失败原因统计
     */
    List<Map<String, Object>> getExportFailureStatistics(LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 获取用户导出趋势数据
     */
    List<Map<String, Object>> getUserExportTrend(Long userId, int days);

    /**
     * 检查用户导出配额
     */
    boolean checkUserExportQuota(Long userId);

    /**
     * 更新导出状态
     */
    void updateExportStatus(Long logId, String status, String errorMessage);

    /**
     * 获取导出日志详情
     */
    ExportLogEntity getExportLogById(Long logId);
}