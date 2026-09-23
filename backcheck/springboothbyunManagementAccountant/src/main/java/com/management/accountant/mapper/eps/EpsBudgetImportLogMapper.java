package com.management.accountant.mapper.eps;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.management.accountant.entity.eps.EpsBudgetImportLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 预算导入日志数据访问接口
 * 
 * @author 华博云
 * @version 3.0.0
 */
@Mapper
public interface EpsBudgetImportLogMapper extends BaseMapper<EpsBudgetImportLog> {

    /**
     * 根据批次号查询导入日志
     * 
     * @param batchNumber 批次号
     * @return 导入日志
     */
    EpsBudgetImportLog selectByBatchNumber(@Param("batchNumber") String batchNumber);

    /**
     * 分页查询导入历史
     * 
     * @param systemId 预算体系ID
     * @param versionId 预算版本ID
     * @param importStatus 导入状态
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @param offset 偏移量
     * @param limit 限制数量
     * @return 导入历史列表
     */
    List<EpsBudgetImportLog> selectImportHistoryPage(@Param("systemId") Long systemId,
                                                   @Param("versionId") Long versionId,
                                                   @Param("importStatus") String importStatus,
                                                   @Param("startDate") String startDate,
                                                   @Param("endDate") String endDate,
                                                   @Param("offset") Long offset,
                                                   @Param("limit") Long limit);

    /**
     * 统计导入历史总数
     * 
     * @param systemId 预算体系ID
     * @param versionId 预算版本ID
     * @param importStatus 导入状态
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 总数
     */
    Long countImportHistory(@Param("systemId") Long systemId,
                           @Param("versionId") Long versionId,
                           @Param("importStatus") String importStatus,
                           @Param("startDate") String startDate,
                           @Param("endDate") String endDate);

    /**
     * 更新导入状态
     * 
     * @param batchNumber 批次号
     * @param status 状态
     * @param message 消息
     * @param updatedTime 更新时间
     * @return 更新行数
     */
    int updateImportStatus(@Param("batchNumber") String batchNumber,
                          @Param("status") String status,
                          @Param("message") String message,
                          @Param("updatedTime") LocalDateTime updatedTime);

    /**
     * 更新导入进度
     * 
     * @param batchNumber 批次号
     * @param successRecords 成功记录数
     * @param failedRecords 失败记录数
     * @param updatedTime 更新时间
     * @return 更新行数
     */
    int updateImportProgress(@Param("batchNumber") String batchNumber,
                           @Param("successRecords") Integer successRecords,
                           @Param("failedRecords") Integer failedRecords,
                           @Param("updatedTime") LocalDateTime updatedTime);

    /**
     * 根据状态查询导入日志
     * 
     * @param importStatus 导入状态
     * @return 导入日志列表
     */
    List<EpsBudgetImportLog> selectByImportStatus(@Param("importStatus") String importStatus);

    /**
     * 查询过期的导入记录
     * 
     * @param expiredDate 过期日期
     * @param statusList 状态列表
     * @return 过期记录列表
     */
    List<EpsBudgetImportLog> selectExpiredRecords(@Param("expiredDate") LocalDateTime expiredDate,
                                                @Param("statusList") List<String> statusList);

    /**
     * 删除过期的导入记录
     * 
     * @param expiredDate 过期日期
     * @param statusList 状态列表
     * @return 删除行数
     */
    int deleteExpiredRecords(@Param("expiredDate") LocalDateTime expiredDate,
                           @Param("statusList") List<String> statusList);

    /**
     * 根据预算体系ID查询导入日志
     * 
     * @param systemId 预算体系ID
     * @return 导入日志列表
     */
    List<EpsBudgetImportLog> selectBySystemId(@Param("systemId") Long systemId);

    /**
     * 根据预算版本ID查询导入日志
     * 
     * @param versionId 预算版本ID
     * @return 导入日志列表
     */
    List<EpsBudgetImportLog> selectByVersionId(@Param("versionId") Long versionId);

    /**
     * 根据模板ID查询导入日志
     * 
     * @param templateId 模板ID
     * @return 导入日志列表
     */
    List<EpsBudgetImportLog> selectByTemplateId(@Param("templateId") Long templateId);

    /**
     * 查询导入统计信息
     * 
     * @param systemId 预算体系ID
     * @param versionId 预算版本ID
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 统计信息
     */
    Map<String, Object> selectImportStatistics(@Param("systemId") Long systemId,
                                             @Param("versionId") Long versionId,
                                             @Param("startDate") String startDate,
                                             @Param("endDate") String endDate);

    /**
     * 查询最近的导入记录
     * 
     * @param systemId 预算体系ID
     * @param versionId 预算版本ID
     * @param limit 限制数量
     * @return 最近导入记录
     */
    List<EpsBudgetImportLog> selectRecentImports(@Param("systemId") Long systemId,
                                               @Param("versionId") Long versionId,
                                               @Param("limit") Integer limit);

    /**
     * 查询正在进行的导入任务
     * 
     * @return 正在进行的导入任务列表
     */
    List<EpsBudgetImportLog> selectRunningImports();

    /**
     * 查询失败的导入任务
     * 
     * @param retryCount 重试次数限制
     * @return 失败的导入任务列表
     */
    List<EpsBudgetImportLog> selectFailedImports(@Param("retryCount") Integer retryCount);

    /**
     * 更新重试次数
     * 
     * @param batchNumber 批次号
     * @param retryCount 重试次数
     * @return 更新行数
     */
    int updateRetryCount(@Param("batchNumber") String batchNumber,
                        @Param("retryCount") Integer retryCount);

    /**
     * 批量删除导入记录
     * 
     * @param batchNumbers 批次号列表
     * @return 删除行数
     */
    int batchDeleteByBatchNumbers(@Param("batchNumbers") List<String> batchNumbers);

    /**
     * 查询用户的导入记录
     * 
     * @param createdBy 创建人
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 导入记录列表
     */
    List<EpsBudgetImportLog> selectByCreatedBy(@Param("createdBy") Long createdBy,
                                             @Param("startDate") String startDate,
                                             @Param("endDate") String endDate);

    /**
     * 查询导入记录按状态分组统计
     * 
     * @param systemId 预算体系ID
     * @param versionId 预算版本ID
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 状态统计
     */
    List<Map<String, Object>> selectStatusStatistics(@Param("systemId") Long systemId,
                                                    @Param("versionId") Long versionId,
                                                    @Param("startDate") String startDate,
                                                    @Param("endDate") String endDate);

    /**
     * 查询导入记录按日期分组统计
     * 
     * @param systemId 预算体系ID
     * @param versionId 预算版本ID
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 日期统计
     */
    List<Map<String, Object>> selectDateStatistics(@Param("systemId") Long systemId,
                                                  @Param("versionId") Long versionId,
                                                  @Param("startDate") String startDate,
                                                  @Param("endDate") String endDate);

    /**
     * 查询大文件导入记录
     * 
     * @param fileSizeThreshold 文件大小阈值（字节）
     * @return 大文件导入记录
     */
    List<EpsBudgetImportLog> selectLargeFileImports(@Param("fileSizeThreshold") Long fileSizeThreshold);

    /**
     * 查询导入耗时统计
     * 
     * @param systemId 预算体系ID
     * @param versionId 预算版本ID
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 耗时统计
     */
    Map<String, Object> selectDurationStatistics(@Param("systemId") Long systemId,
                                                @Param("versionId") Long versionId,
                                                @Param("startDate") String startDate,
                                                @Param("endDate") String endDate);

    /**
     * 更新导入完成时间
     * 
     * @param batchNumber 批次号
     * @param completedTime 完成时间
     * @return 更新行数
     */
    int updateCompletedTime(@Param("batchNumber") String batchNumber,
                          @Param("completedTime") LocalDateTime completedTime);

    /**
     * 查询模板使用统计
     * 
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 模板使用统计
     */
    List<Map<String, Object>> selectTemplateUsageStatistics(@Param("startDate") String startDate,
                                                           @Param("endDate") String endDate);
}
