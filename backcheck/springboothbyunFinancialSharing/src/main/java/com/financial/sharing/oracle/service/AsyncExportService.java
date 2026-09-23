package com.financial.sharing.oracle.service;

import com.financial.sharing.oracle.entity.ExportTaskEntity;
import com.financial.sharing.oracle.dto.ExportTaskDTO;
import com.hbfk.util.JsonBean;

import java.util.List;
import java.util.Map;

/**
 * 异步导出服务接口
 *
 * @author 赵工
 * @since 2025-12-07
 */
public interface AsyncExportService {

    /**
     * 创建导出任务
     *
     * @param exportTask 导出任务参数
     * @return 任务ID
     */
    JsonBean createExportTask(ExportTaskDTO exportTask);

    /**
     * 开始异步导出
     *
     * @param taskId 任务ID
     * @return 执行结果
     */
    JsonBean startExport(Long taskId);

    /**
     * 取消导出任务
     *
     * @param taskId 任务ID
     * @return 取消结果
     */
    JsonBean cancelExport(Long taskId);

    /**
     * 查询导出任务状态
     *
     * @param taskId 任务ID
     * @return 任务状态
     */
    JsonBean getExportStatus(Long taskId);

    /**
     * 获取导出进度
     *
     * @param taskId 任务ID
     * @return 进度信息
     */
    JsonBean getExportProgress(Long taskId);

    /**
     * 获取我的导出任务列表
     *
     * @param userId 用户ID
     * @param status 任务状态
     * @param pageNum 页码
     * @param pageSize 页大小
     * @return 任务列表
     */
    JsonBean getMyExportTasks(Long userId, Integer status, Integer pageNum, Integer pageSize);

    /**
     * 获取所有导出任务列表(管理员)
     *
     * @param status 任务状态
     * @param taskType 任务类型
     * @param pageNum 页码
     * @param pageSize 页大小
     * @return 任务列表
     */
    JsonBean getAllExportTasks(Integer status, String taskType, Integer pageNum, Integer pageSize);

    /**
     * 下载导出文件
     *
     * @param taskId 任务ID
     * @return 下载URL
     */
    JsonBean downloadExportFile(Long taskId);

    /**
     * 删除导出任务
     *
     * @param taskId 任务ID
     * @return 删除结果
     */
    JsonBean deleteExportTask(Long taskId);

    /**
     * 重新导出
     *
     * @param taskId 原任务ID
     * @return 新任务ID
     */
    JsonBean retryExport(Long taskId);

    /**
     * 批量删除过期任务
     *
     * @param days 保留天数
     * @return 删除数量
     */
    JsonBean cleanupExpiredTasks(Integer days);

    /**
     * 获取导出统计信息
     *
     * @param days 统计天数
     * @return 统计信息
     */
    JsonBean getExportStatistics(Integer days);

    /**
     * 预览导出数据
     *
     * @param exportTask 导出任务参数
     * @param previewRows 预览行数
     * @return 预览数据
     */
    JsonBean previewExportData(ExportTaskDTO exportTask, Integer previewRows);

    /**
     * 获取导出模板
     *
     * @param taskType 任务类型
     * @param exportFormat 导出格式
     * @return 模板信息
     */
    JsonBean getExportTemplate(String taskType, String exportFormat);

    /**
     * 导出任务进度回调
     *
     * @param taskId 任务ID
     * @param progress 进度信息
     * @return 回调结果
     */
    JsonBean updateExportProgress(Long taskId, Map<String, Object> progress);

    /**
     * 获取导出文件信息
     *
     * @param taskId 任务ID
     * @return 文件信息
     */
    JsonBean getExportFileInfo(Long taskId);

    /**
     * 设置导出配置
     *
     * @param taskId 任务ID
     * @param config 配置信息
     * @return 设置结果
     */
    JsonBean setExportConfig(Long taskId, Map<String, Object> config);

    /**
     * 获取导出日志
     *
     * @param taskId 任务ID
     * @return 日志信息
     */
    JsonBean getExportLogs(Long taskId);

    /**
     * 暂停导出任务
     *
     * @param taskId 任务ID
     * @return 暂停结果
     */
    JsonBean pauseExport(Long taskId);

    /**
     * 恢复导出任务
     *
     * @param taskId 任务ID
     * @return 恢复结果
     */
    JsonBean resumeExport(Long taskId);
}