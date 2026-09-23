package com.financial.sharing.oracle.mapper;

import com.financial.sharing.oracle.entity.ExportTaskEntity;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 导出任务Mapper接口
 *
 * @author 赵工
 * @since 2025-12-07
 */
public interface ExportTaskMapper {

    /**
     * 插入导出任务
     */
    int insertExportTask(ExportTaskEntity task);

    /**
     * 根据ID查询导出任务
     */
    ExportTaskEntity selectExportTaskById(@Param("taskId") Long taskId);

    /**
     * 更新导出任务
     */
    int updateExportTask(ExportTaskEntity task);

    /**
     * 根据用户ID查询导出任务列表
     */
    List<Map<String, Object>> selectExportTasksByUserId(@Param("param") Map<String, Object> params);

    /**
     * 根据用户ID统计导出任务数
     */
    Long countExportTasksByUserId(@Param("param") Map<String, Object> params);

    /**
     * 查询所有导出任务
     */
    List<Map<String, Object>> selectAllExportTasks(@Param("param") Map<String, Object> params);

    /**
     * 统计所有导出任务数
     */
    Long countAllExportTasks(@Param("param") Map<String, Object> params);

    /**
     * 删除过期任务
     */
    int deleteExpiredTasks(@Param("expireDate") Date expireDate);

    /**
     * 获取导出统计信息
     */
    Map<String, Object> selectExportStatistics(@Param("param") Map<String, Object> params);

    /**
     * 统计成本分摊数据量
     */
    Long countCostAllocation(@Param("param") Map<String, Object> params);

    /**
     * 分批查询成本分摊数据
     */
    List<Map<String, Object>> selectCostAllocationBatch(
        @Param("param") Map<String, Object> params,
        @Param("offset") Integer offset,
        @Param("pageSize") Integer pageSize);

    /**
     * 统计凭证分录数据量
     */
    Long countVoucherEntry(@Param("param") Map<String, Object> params);

    /**
     * 分批查询凭证分录数据
     */
    List<Map<String, Object>> selectVoucherEntryBatch(
        @Param("param") Map<String, Object> params,
        @Param("offset") Integer offset,
        @Param("pageSize") Integer pageSize);

    /**
     * 统计财务报表数据量
     */
    Long countFinancialReport(@Param("param") Map<String, Object> params);

    /**
     * 分批查询财务报表数据
     */
    List<Map<String, Object>> selectFinancialReportBatch(
        @Param("param") Map<String, Object> params,
        @Param("offset") Integer offset,
        @Param("pageSize") Integer pageSize);

    /**
     * 更新任务进度
     */
    int updateTaskProgress(@Param("taskId") Long taskId,
                          @Param("processedRecords") Long processedRecords,
                          @Param("progressPercent") java.math.BigDecimal progressPercent);

    /**
     * 批量更新任务状态
     */
    int batchUpdateTaskStatus(@Param("taskIds") List<Long> taskIds,
                             @Param("status") Integer status);

    /**
     * 查询正在运行的任务
     */
    List<ExportTaskEntity> selectRunningTasks();

    /**
     * 查询任务执行日志
     */
    List<Map<String, Object>> selectExportLogs(@Param("taskId") Long taskId);

    /**
     * 插入任务执行日志
     */
    int insertExportLog(@Param("param") Map<String, Object> log);

    /**
     * 获取文件存储路径配置
     */
    Map<String, Object> selectFileStorageConfig();
}