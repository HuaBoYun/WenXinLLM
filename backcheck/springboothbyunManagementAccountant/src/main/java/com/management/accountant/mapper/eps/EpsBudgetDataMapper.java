package com.management.accountant.mapper.eps;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.management.accountant.entity.eps.EpsBudgetData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 预算数据数据访问接口
 * 
 * @author 华博云
 * @version 3.0.0
 */
@Mapper
public interface EpsBudgetDataMapper extends BaseMapper<EpsBudgetData> {

    /**
     * 根据版本ID查询预算数据
     * 
     * @param versionId 版本ID
     * @return 预算数据列表
     */
    List<EpsBudgetData> selectByVersionId(@Param("versionId") Long versionId);

    /**
     * 根据科目ID查询预算数据
     * 
     * @param subjectId 科目ID
     * @param versionId 版本ID
     * @return 预算数据列表
     */
    List<EpsBudgetData> selectBySubjectId(@Param("subjectId") Long subjectId,
                                         @Param("versionId") Long versionId);

    /**
     * 根据组织ID查询预算数据
     * 
     * @param organizationId 组织ID
     * @param versionId 版本ID
     * @return 预算数据列表
     */
    List<EpsBudgetData> selectByOrganizationId(@Param("organizationId") Long organizationId,
                                              @Param("versionId") Long versionId);

    /**
     * 根据预算期间查询预算数据
     * 
     * @param budgetPeriod 预算期间
     * @param versionId 版本ID
     * @return 预算数据列表
     */
    List<EpsBudgetData> selectByBudgetPeriod(@Param("budgetPeriod") String budgetPeriod,
                                            @Param("versionId") Long versionId);

    /**
     * 检查数据是否存在
     * 
     * @param versionId 版本ID
     * @param subjectId 科目ID
     * @param organizationId 组织ID
     * @param budgetPeriod 预算期间
     * @return 存在数量
     */
    int checkDataExists(@Param("versionId") Long versionId,
                       @Param("subjectId") Long subjectId,
                       @Param("organizationId") Long organizationId,
                       @Param("budgetPeriod") String budgetPeriod);

    /**
     * 获取预算数据矩阵
     * 
     * @param versionId 版本ID
     * @param organizationId 组织ID
     * @param budgetPeriod 预算期间
     * @return 数据矩阵
     */
    List<Map<String, Object>> selectBudgetDataMatrix(@Param("versionId") Long versionId,
                                                    @Param("organizationId") Long organizationId,
                                                    @Param("budgetPeriod") String budgetPeriod);

    /**
     * 批量插入预算数据
     * 
     * @param budgetDataList 预算数据列表
     * @return 插入行数
     */
    int batchInsertBudgetData(@Param("budgetDataList") List<EpsBudgetData> budgetDataList);

    /**
     * 批量更新预算数据
     * 
     * @param budgetDataList 预算数据列表
     * @return 更新行数
     */
    int batchUpdateBudgetData(@Param("budgetDataList") List<EpsBudgetData> budgetDataList);

    /**
     * 批量删除预算数据
     * 
     * @param dataIds 数据ID列表
     * @return 删除行数
     */
    int batchDeleteBudgetData(@Param("dataIds") List<Long> dataIds);

    /**
     * 批量锁定预算数据
     * 
     * @param dataIds 数据ID列表
     * @param lockedBy 锁定人ID
     * @param lockedByName 锁定人姓名
     * @return 更新行数
     */
    int batchLockBudgetData(@Param("dataIds") List<Long> dataIds,
                           @Param("lockedBy") Long lockedBy,
                           @Param("lockedByName") String lockedByName);

    /**
     * 批量解锁预算数据
     * 
     * @param dataIds 数据ID列表
     * @return 更新行数
     */
    int batchUnlockBudgetData(@Param("dataIds") List<Long> dataIds);

    /**
     * 批量更新数据状态
     * 
     * @param dataIds 数据ID列表
     * @param dataStatus 数据状态
     * @param updatedBy 更新人ID
     * @return 更新行数
     */
    int batchUpdateDataStatus(@Param("dataIds") List<Long> dataIds,
                             @Param("dataStatus") String dataStatus,
                             @Param("updatedBy") Long updatedBy);

    /**
     * 计算预算数据汇总
     * 
     * @param versionId 版本ID
     * @param subjectIds 科目ID列表
     * @param organizationIds 组织ID列表
     * @param budgetPeriods 预算期间列表
     * @return 汇总结果
     */
    Map<String, Object> calculateBudgetDataSummary(@Param("versionId") Long versionId,
                                                  @Param("subjectIds") List<Long> subjectIds,
                                                  @Param("organizationIds") List<Long> organizationIds,
                                                  @Param("budgetPeriods") List<String> budgetPeriods);

    /**
     * 查询预算数据统计
     * 
     * @param versionId 版本ID
     * @param organizationId 组织ID
     * @param budgetPeriod 预算期间
     * @return 统计信息
     */
    Map<String, Object> selectBudgetDataStatistics(@Param("versionId") Long versionId,
                                                   @Param("organizationId") Long organizationId,
                                                   @Param("budgetPeriod") String budgetPeriod);

    /**
     * 查询预算数据按状态统计
     * 
     * @param versionId 版本ID
     * @return 状态统计
     */
    List<Map<String, Object>> selectBudgetDataStatusStatistics(@Param("versionId") Long versionId);

    /**
     * 查询预算数据按科目统计
     * 
     * @param versionId 版本ID
     * @return 科目统计
     */
    List<Map<String, Object>> selectBudgetDataSubjectStatistics(@Param("versionId") Long versionId);

    /**
     * 查询预算数据按组织统计
     * 
     * @param versionId 版本ID
     * @return 组织统计
     */
    List<Map<String, Object>> selectBudgetDataOrganizationStatistics(@Param("versionId") Long versionId);

    /**
     * 查询预算数据按期间统计
     * 
     * @param versionId 版本ID
     * @return 期间统计
     */
    List<Map<String, Object>> selectBudgetDataPeriodStatistics(@Param("versionId") Long versionId);

    /**
     * 查询锁定的预算数据
     * 
     * @param versionId 版本ID
     * @return 锁定数据列表
     */
    List<EpsBudgetData> selectLockedBudgetData(@Param("versionId") Long versionId);

    /**
     * 查询待审批的预算数据
     * 
     * @param versionId 版本ID
     * @return 待审批数据列表
     */
    List<EpsBudgetData> selectPendingApprovalBudgetData(@Param("versionId") Long versionId);

    /**
     * 查询已审批的预算数据
     * 
     * @param versionId 版本ID
     * @return 已审批数据列表
     */
    List<EpsBudgetData> selectApprovedBudgetData(@Param("versionId") Long versionId);

    /**
     * 查询草稿状态的预算数据
     * 
     * @param versionId 版本ID
     * @return 草稿数据列表
     */
    List<EpsBudgetData> selectDraftBudgetData(@Param("versionId") Long versionId);

    /**
     * 复制预算数据
     * 
     * @param sourceVersionId 源版本ID
     * @param targetVersionId 目标版本ID
     * @param copyParams 复制参数
     * @return 复制行数
     */
    int copyBudgetData(@Param("sourceVersionId") Long sourceVersionId,
                      @Param("targetVersionId") Long targetVersionId,
                      @Param("copyParams") Map<String, Object> copyParams);

    /**
     * 同步预算数据
     * 
     * @param sourceVersionId 源版本ID
     * @param targetVersionId 目标版本ID
     * @param syncParams 同步参数
     * @return 同步结果
     */
    Map<String, Object> syncBudgetData(@Param("sourceVersionId") Long sourceVersionId,
                                      @Param("targetVersionId") Long targetVersionId,
                                      @Param("syncParams") Map<String, Object> syncParams);

    /**
     * 查询预算数据差异
     * 
     * @param sourceVersionId 源版本ID
     * @param targetVersionId 目标版本ID
     * @return 差异信息
     */
    List<Map<String, Object>> selectBudgetDataDifferences(@Param("sourceVersionId") Long sourceVersionId,
                                                         @Param("targetVersionId") Long targetVersionId);

    /**
     * 查询预算数据变更历史
     * 
     * @param dataId 数据ID
     * @return 变更历史
     */
    List<Map<String, Object>> selectBudgetDataHistory(@Param("dataId") Long dataId);

    /**
     * 保存预算数据变更记录
     * 
     * @param historyRecord 变更记录
     * @return 插入行数
     */
    int insertBudgetDataHistory(@Param("historyRecord") Map<String, Object> historyRecord);

    /**
     * 查询预算数据配置
     * 
     * @param versionId 版本ID
     * @return 数据配置
     */
    Map<String, Object> selectBudgetDataConfiguration(@Param("versionId") Long versionId);

    /**
     * 保存预算数据配置
     * 
     * @param versionId 版本ID
     * @param configuration 配置数据
     * @return 插入或更新行数
     */
    int saveBudgetDataConfiguration(@Param("versionId") Long versionId,
                                   @Param("configuration") Map<String, Object> configuration);

    /**
     * 查询预算数据权限
     * 
     * @param dataId 数据ID
     * @param userId 用户ID
     * @return 权限信息
     */
    Map<String, Object> selectBudgetDataPermissions(@Param("dataId") Long dataId,
                                                   @Param("userId") Long userId);

    /**
     * 保存预算数据权限
     * 
     * @param permissionData 权限数据
     * @return 插入或更新行数
     */
    int saveBudgetDataPermissions(@Param("permissionData") Map<String, Object> permissionData);

    /**
     * 查询预算数据模板
     * 
     * @param versionId 版本ID
     * @return 数据模板
     */
    Map<String, Object> selectBudgetDataTemplate(@Param("versionId") Long versionId);

    /**
     * 应用预算数据模板
     * 
     * @param versionId 版本ID
     * @param templateData 模板数据
     * @return 应用结果
     */
    int applyBudgetDataTemplate(@Param("versionId") Long versionId,
                               @Param("templateData") Map<String, Object> templateData);

    /**
     * 查询预算数据趋势
     * 
     * @param versionId 版本ID
     * @param subjectId 科目ID
     * @param organizationId 组织ID
     * @return 趋势数据
     */
    List<Map<String, Object>> selectBudgetDataTrend(@Param("versionId") Long versionId,
                                                   @Param("subjectId") Long subjectId,
                                                   @Param("organizationId") Long organizationId);

    /**
     * 预测预算数据
     * 
     * @param versionId 版本ID
     * @param forecastParams 预测参数
     * @return 预测结果
     */
    List<Map<String, Object>> forecastBudgetData(@Param("versionId") Long versionId,
                                                @Param("forecastParams") Map<String, Object> forecastParams);

    /**
     * 查询预算数据异常
     * 
     * @param versionId 版本ID
     * @param anomalyParams 异常检测参数
     * @return 异常数据
     */
    List<Map<String, Object>> selectBudgetDataAnomalies(@Param("versionId") Long versionId,
                                                       @Param("anomalyParams") Map<String, Object> anomalyParams);

    /**
     * 修复预算数据
     * 
     * @param versionId 版本ID
     * @param repairParams 修复参数
     * @return 修复结果
     */
    Map<String, Object> repairBudgetData(@Param("versionId") Long versionId,
                                        @Param("repairParams") Map<String, Object> repairParams);

    /**
     * 查询预算数据性能统计
     * 
     * @param versionId 版本ID
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 性能统计
     */
    Map<String, Object> selectBudgetDataPerformanceStatistics(@Param("versionId") Long versionId,
                                                             @Param("startDate") String startDate,
                                                             @Param("endDate") String endDate);

    /**
     * 优化预算数据
     * 
     * @param versionId 版本ID
     * @param optimizeParams 优化参数
     * @return 优化结果
     */
    Map<String, Object> optimizeBudgetData(@Param("versionId") Long versionId,
                                          @Param("optimizeParams") Map<String, Object> optimizeParams);

    /**
     * 验证预算数据
     * 
     * @param versionId 版本ID
     * @param validationRules 验证规则
     * @return 验证结果
     */
    List<Map<String, Object>> validateBudgetData(@Param("versionId") Long versionId,
                                                @Param("validationRules") Map<String, Object> validationRules);

    /**
     * 汇总预算数据
     * 
     * @param versionId 版本ID
     * @param summarizeParams 汇总参数
     * @return 汇总结果
     */
    Map<String, Object> summarizeBudgetData(@Param("versionId") Long versionId,
                                           @Param("summarizeParams") Map<String, Object> summarizeParams);

    /**
     * 分解预算数据
     * 
     * @param versionId 版本ID
     * @param decomposeParams 分解参数
     * @return 分解结果
     */
    Map<String, Object> decomposeBudgetData(@Param("versionId") Long versionId,
                                           @Param("decomposeParams") Map<String, Object> decomposeParams);

    /**
     * 合并预算数据
     * 
     * @param sourceVersionIds 源版本ID列表
     * @param targetVersionId 目标版本ID
     * @param mergeParams 合并参数
     * @return 合并结果
     */
    Map<String, Object> mergeBudgetData(@Param("sourceVersionIds") List<Long> sourceVersionIds,
                                       @Param("targetVersionId") Long targetVersionId,
                                       @Param("mergeParams") Map<String, Object> mergeParams);

    /**
     * 拆分预算数据
     * 
     * @param sourceVersionId 源版本ID
     * @param splitParams 拆分参数
     * @return 拆分结果
     */
    Map<String, Object> splitBudgetData(@Param("sourceVersionId") Long sourceVersionId,
                                       @Param("splitParams") Map<String, Object> splitParams);

    /**
     * 更新预算数据使用次数
     * 
     * @param dataId 数据ID
     * @return 更新行数
     */
    int updateBudgetDataUsageCount(@Param("dataId") Long dataId);

    /**
     * 更新预算数据最后使用时间
     * 
     * @param dataId 数据ID
     * @return 更新行数
     */
    int updateBudgetDataLastUsedTime(@Param("dataId") Long dataId);

    /**
     * 查询热门预算数据
     * 
     * @param versionId 版本ID
     * @param limit 限制数量
     * @return 热门数据列表
     */
    List<EpsBudgetData> selectPopularBudgetData(@Param("versionId") Long versionId,
                                              @Param("limit") Integer limit);

    /**
     * 查询最近使用的预算数据
     * 
     * @param versionId 版本ID
     * @param userId 用户ID
     * @param limit 限制数量
     * @return 最近使用数据列表
     */
    List<EpsBudgetData> selectRecentlyUsedBudgetData(@Param("versionId") Long versionId,
                                                   @Param("userId") Long userId,
                                                   @Param("limit") Integer limit);

    /**
     * 清理无效数据
     * 
     * @param versionId 版本ID
     * @return 清理行数
     */
    int cleanupInvalidBudgetData(@Param("versionId") Long versionId);

    /**
     * 重建数据索引
     * 
     * @param versionId 版本ID
     * @return 重建结果
     */
    int rebuildBudgetDataIndex(@Param("versionId") Long versionId);

    /**
     * 查询数据完整性
     * 
     * @param versionId 版本ID
     * @return 完整性检查结果
     */
    Map<String, Object> checkBudgetDataIntegrity(@Param("versionId") Long versionId);
}
