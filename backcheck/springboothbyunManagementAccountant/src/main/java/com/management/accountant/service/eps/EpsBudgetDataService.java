package com.management.accountant.service.eps;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.management.accountant.entity.eps.EpsBudgetData;

import java.util.List;
import java.util.Map;

/**
 * 预算数据服务接口
 * 
 * @author 华博云
 * @version 3.0.0
 */
public interface EpsBudgetDataService {

    /**
     * 分页查询预算数据
     * 
     * @param current 当前页
     * @param size 每页大小
     * @param versionId 预算版本ID
     * @param subjectId 预算科目ID
     * @param organizationId 组织ID
     * @param budgetPeriod 预算期间
     * @param dataStatus 数据状态
     * @return 分页结果
     */
    IPage<EpsBudgetData> queryBudgetDataPage(Long current, Long size, Long versionId, 
                                            Long subjectId, Long organizationId, 
                                            String budgetPeriod, String dataStatus);

    /**
     * 创建预算数据
     * 
     * @param budgetData 预算数据
     * @return 创建结果
     */
    boolean createBudgetData(EpsBudgetData budgetData);

    /**
     * 更新预算数据
     * 
     * @param budgetData 预算数据
     * @return 更新结果
     */
    boolean updateBudgetData(EpsBudgetData budgetData);

    /**
     * 删除预算数据
     * 
     * @param dataId 数据ID
     * @return 删除结果
     */
    boolean deleteBudgetData(Long dataId);

    /**
     * 批量删除预算数据
     * 
     * @param dataIds 数据ID列表
     * @return 删除结果
     */
    boolean batchDeleteBudgetData(List<Long> dataIds);

    /**
     * 根据ID查询预算数据
     * 
     * @param dataId 数据ID
     * @return 预算数据
     */
    EpsBudgetData getBudgetDataById(Long dataId);

    /**
     * 根据版本查询预算数据
     * 
     * @param versionId 版本ID
     * @return 预算数据列表
     */
    List<EpsBudgetData> getBudgetDataByVersionId(Long versionId);

    /**
     * 根据科目查询预算数据
     * 
     * @param subjectId 科目ID
     * @param versionId 版本ID
     * @return 预算数据列表
     */
    List<EpsBudgetData> getBudgetDataBySubjectId(Long subjectId, Long versionId);

    /**
     * 获取预算数据矩阵
     * 
     * @param versionId 版本ID
     * @param organizationId 组织ID
     * @param budgetPeriod 预算期间
     * @return 数据矩阵
     */
    Map<String, Object> getBudgetDataMatrix(Long versionId, Long organizationId, String budgetPeriod);

    /**
     * 批量保存预算数据
     * 
     * @param budgetDataList 预算数据列表
     * @return 保存结果
     */
    Map<String, Object> batchSaveBudgetData(List<EpsBudgetData> budgetDataList);

    /**
     * 导入预算数据
     * 
     * @param importData 导入数据
     * @return 导入结果
     */
    Map<String, Object> importBudgetData(Map<String, Object> importData);

    /**
     * 导出预算数据
     * 
     * @param exportParams 导出参数
     * @return 导出结果
     */
    Map<String, Object> exportBudgetData(Map<String, Object> exportParams);

    /**
     * 计算预算数据
     * 
     * @param calculateParams 计算参数
     * @return 计算结果
     */
    Map<String, Object> calculateBudgetData(Map<String, Object> calculateParams);

    /**
     * 验证预算数据
     * 
     * @param validateParams 验证参数
     * @return 验证结果
     */
    Map<String, Object> validateBudgetData(Map<String, Object> validateParams);

    /**
     * 汇总预算数据
     * 
     * @param summarizeParams 汇总参数
     * @return 汇总结果
     */
    Map<String, Object> summarizeBudgetData(Map<String, Object> summarizeParams);

    /**
     * 分解预算数据
     * 
     * @param decomposeParams 分解参数
     * @return 分解结果
     */
    Map<String, Object> decomposeBudgetData(Map<String, Object> decomposeParams);

    /**
     * 复制预算数据
     * 
     * @param copyParams 复制参数
     * @return 复制结果
     */
    Map<String, Object> copyBudgetData(Map<String, Object> copyParams);

    /**
     * 锁定预算数据
     * 
     * @param dataId 数据ID
     * @param lockReason 锁定原因
     * @return 锁定结果
     */
    boolean lockBudgetData(Long dataId, String lockReason);

    /**
     * 解锁预算数据
     * 
     * @param dataId 数据ID
     * @return 解锁结果
     */
    boolean unlockBudgetData(Long dataId);

    /**
     * 提交预算数据
     * 
     * @param submitParams 提交参数
     * @return 提交结果
     */
    Map<String, Object> submitBudgetData(Map<String, Object> submitParams);

    /**
     * 审批预算数据
     * 
     * @param approveParams 审批参数
     * @return 审批结果
     */
    Map<String, Object> approveBudgetData(Map<String, Object> approveParams);

    /**
     * 获取预算数据统计
     * 
     * @param versionId 版本ID
     * @param organizationId 组织ID
     * @param budgetPeriod 预算期间
     * @return 统计结果
     */
    Map<String, Object> getBudgetDataStatistics(Long versionId, Long organizationId, String budgetPeriod);

    /**
     * 获取预算数据变更历史
     * 
     * @param dataId 数据ID
     * @return 变更历史
     */
    List<Map<String, Object>> getBudgetDataHistory(Long dataId);

    /**
     * 批量操作预算数据
     * 
     * @param batchData 批量操作数据
     * @return 操作结果
     */
    Map<String, Object> batchOperateBudgetData(Map<String, Object> batchData);

    /**
     * 获取预算数据配置
     * 
     * @param versionId 版本ID
     * @return 数据配置
     */
    Map<String, Object> getBudgetDataConfiguration(Long versionId);

    /**
     * 保存预算数据配置
     * 
     * @param versionId 版本ID
     * @param configuration 配置数据
     * @return 保存结果
     */
    boolean saveBudgetDataConfiguration(Long versionId, Map<String, Object> configuration);

    /**
     * 获取预算数据权限
     * 
     * @param dataId 数据ID
     * @param userId 用户ID
     * @return 权限信息
     */
    Map<String, Object> getBudgetDataPermissions(Long dataId, Long userId);

    /**
     * 设置预算数据权限
     * 
     * @param dataId 数据ID
     * @param permissionData 权限数据
     * @return 设置结果
     */
    boolean setBudgetDataPermissions(Long dataId, Map<String, Object> permissionData);

    /**
     * 获取预算数据模板
     * 
     * @param versionId 版本ID
     * @return 数据模板
     */
    Map<String, Object> getBudgetDataTemplate(Long versionId);

    /**
     * 应用预算数据模板
     * 
     * @param versionId 版本ID
     * @param templateData 模板数据
     * @return 应用结果
     */
    boolean applyBudgetDataTemplate(Long versionId, Map<String, Object> templateData);

    /**
     * 获取预算数据差异
     * 
     * @param sourceVersionId 源版本ID
     * @param targetVersionId 目标版本ID
     * @return 差异信息
     */
    Map<String, Object> getBudgetDataDifferences(Long sourceVersionId, Long targetVersionId);

    /**
     * 同步预算数据
     * 
     * @param sourceVersionId 源版本ID
     * @param targetVersionId 目标版本ID
     * @param syncParams 同步参数
     * @return 同步结果
     */
    Map<String, Object> syncBudgetData(Long sourceVersionId, Long targetVersionId, Map<String, Object> syncParams);

    /**
     * 合并预算数据
     * 
     * @param sourceVersionIds 源版本ID列表
     * @param targetVersionId 目标版本ID
     * @param mergeParams 合并参数
     * @return 合并结果
     */
    Map<String, Object> mergeBudgetData(List<Long> sourceVersionIds, Long targetVersionId, Map<String, Object> mergeParams);

    /**
     * 拆分预算数据
     * 
     * @param sourceVersionId 源版本ID
     * @param splitParams 拆分参数
     * @return 拆分结果
     */
    Map<String, Object> splitBudgetData(Long sourceVersionId, Map<String, Object> splitParams);

    /**
     * 获取预算数据趋势
     * 
     * @param versionId 版本ID
     * @param subjectId 科目ID
     * @param organizationId 组织ID
     * @return 趋势数据
     */
    Map<String, Object> getBudgetDataTrend(Long versionId, Long subjectId, Long organizationId);

    /**
     * 获取预算数据预测
     * 
     * @param versionId 版本ID
     * @param forecastParams 预测参数
     * @return 预测结果
     */
    Map<String, Object> forecastBudgetData(Long versionId, Map<String, Object> forecastParams);

    /**
     * 获取预算数据异常
     * 
     * @param versionId 版本ID
     * @param anomalyParams 异常检测参数
     * @return 异常数据
     */
    Map<String, Object> getBudgetDataAnomalies(Long versionId, Map<String, Object> anomalyParams);

    /**
     * 修复预算数据
     * 
     * @param versionId 版本ID
     * @param repairParams 修复参数
     * @return 修复结果
     */
    Map<String, Object> repairBudgetData(Long versionId, Map<String, Object> repairParams);

    /**
     * 获取预算数据性能统计
     * 
     * @param versionId 版本ID
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 性能统计
     */
    Map<String, Object> getBudgetDataPerformanceStatistics(Long versionId, String startDate, String endDate);

    /**
     * 优化预算数据
     * 
     * @param versionId 版本ID
     * @param optimizeParams 优化参数
     * @return 优化结果
     */
    Map<String, Object> optimizeBudgetData(Long versionId, Map<String, Object> optimizeParams);
}
