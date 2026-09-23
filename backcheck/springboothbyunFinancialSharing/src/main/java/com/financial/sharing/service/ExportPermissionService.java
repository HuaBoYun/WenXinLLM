package com.financial.sharing.service;

import com.financial.sharing.oracle.entity.ExportLogEntity;

import java.util.List;

/**
 * 导出权限控制服务
 */
public interface ExportPermissionService {

    /**
     * 检查导出权限
     * @param userId 用户ID
     * @param exportType 导出类型
     * @return 是否有权限
     */
    boolean checkExportPermission(Long userId, String exportType);

    /**
     * 记录导出日志
     * @param userId 用户ID
     * @param exportType 导出类型
     * @param recordCount 记录数
     * @param fileName 文件名
     * @param fileSize 文件大小
     */
    void logExport(Long userId, String exportType, int recordCount, String fileName, Long fileSize);

    /**
     * 检查导出频率限制
     * @param userId 用户ID
     * @return 是否超过限制
     */
    boolean checkExportRateLimit(Long userId);

    /**
     * 获取用户导出历史
     * @param userId 用户ID
     * @param limit 限制数量
     * @return 导出历史列表
     */
    List<ExportLogEntity> getUserExportHistory(Long userId, int limit);

    /**
     * 获取今日导出次数
     * @param userId 用户ID
     * @return 今日导出次数
     */
    int getTodayExportCount(Long userId);

    /**
     * 检查导出文件大小限制
     * @param userId 用户ID
     * @param fileSize 文件大小（字节）
     * @return 是否超过限制
     */
    boolean checkFileSizeLimit(Long userId, Long fileSize);

    /**
     * 检查导出记录数限制
     * @param userId 用户ID
     * @param recordCount 记录数
     * @return 是否超过限制
     */
    boolean checkRecordCountLimit(Long userId, int recordCount);
}