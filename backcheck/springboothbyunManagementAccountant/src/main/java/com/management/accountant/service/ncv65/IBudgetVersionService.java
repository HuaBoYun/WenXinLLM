package com.management.accountant.service.ncv65;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.management.accountant.entity.ncv65.BudgetVersion;

import java.util.List;
import java.util.Map;

/**
 * NCV65全面预算系统 - 预算版本服务接口
 * 
 * @description 预算版本业务逻辑接口，支持版本控制和历史追溯
 * @author AI Assistant
 * @date 2025-01-09
 * @version 1.0.0
 */
public interface IBudgetVersionService extends IService<BudgetVersion> {

    // ==================== 基础CRUD操作 ====================

    /**
     * 创建预算版本
     * @param version 版本信息
     * @return 是否创建成功
     */
    boolean createBudgetVersion(BudgetVersion version);

    /**
     * 更新预算版本
     * @param version 版本信息
     * @return 是否更新成功
     */
    boolean updateBudgetVersion(BudgetVersion version);

    /**
     * 删除预算版本
     * @param id 版本ID
     * @return 是否删除成功
     */
    boolean deleteBudgetVersion(String id);

    /**
     * 批量删除预算版本
     * @param ids 版本ID列表
     * @return 是否删除成功
     */
    boolean batchDeleteBudgetVersions(List<String> ids);

    /**
     * 根据ID查询预算版本
     * @param id 版本ID
     * @return 版本信息
     */
    BudgetVersion getBudgetVersionById(String id);

    /**
     * 根据编码查询预算版本
     * @param versionCode 版本编码
     * @return 版本信息
     */
    BudgetVersion getBudgetVersionByCode(String versionCode);

    // ==================== 查询操作 ====================

    /**
     * 分页查询预算版本
     * @param current 当前页
     * @param size 页大小
     * @param params 查询参数
     * @return 分页结果
     */
    IPage<BudgetVersion> getBudgetVersionPage(Integer current, Integer size, Map<String, Object> params);

    /**
     * 根据预算年度查询版本列表
     * @param fiscalYear 预算年度
     * @return 版本列表
     */
    List<BudgetVersion> getBudgetVersionsByFiscalYear(Integer fiscalYear);

    /**
     * 根据预算模型ID查询版本列表
     * @param modelId 预算模型ID
     * @return 版本列表
     */
    List<BudgetVersion> getBudgetVersionsByModelId(String modelId);

    /**
     * 根据版本类型查询版本列表
     * @param versionType 版本类型
     * @return 版本列表
     */
    List<BudgetVersion> getBudgetVersionsByType(String versionType);

    /**
     * 根据审批状态查询版本列表
     * @param approvalStatus 审批状态
     * @return 版本列表
     */
    List<BudgetVersion> getBudgetVersionsByApprovalStatus(String approvalStatus);

    /**
     * 查询当前版本
     * @param fiscalYear 预算年度
     * @param modelId 预算模型ID
     * @return 当前版本
     */
    BudgetVersion getCurrentVersion(Integer fiscalYear, String modelId);

    /**
     * 查询基准版本
     * @param fiscalYear 预算年度
     * @param modelId 预算模型ID
     * @return 基准版本
     */
    BudgetVersion getBaselineVersion(Integer fiscalYear, String modelId);

    /**
     * 查询最终版本
     * @param fiscalYear 预算年度
     * @param modelId 预算模型ID
     * @return 最终版本
     */
    BudgetVersion getFinalVersion(Integer fiscalYear, String modelId);

    /**
     * 查询已发布的版本列表
     * @param fiscalYear 预算年度
     * @return 已发布的版本列表
     */
    List<BudgetVersion> getPublishedVersions(Integer fiscalYear);

    /**
     * 查询锁定的版本列表
     * @param fiscalYear 预算年度
     * @return 锁定的版本列表
     */
    List<BudgetVersion> getLockedVersions(Integer fiscalYear);

    /**
     * 查询启用的版本列表
     * @param fiscalYear 预算年度
     * @return 启用的版本列表
     */
    List<BudgetVersion> getEnabledVersions(Integer fiscalYear);

    /**
     * 查询归档的版本列表
     * @param fiscalYear 预算年度
     * @return 归档的版本列表
     */
    List<BudgetVersion> getArchivedVersions(Integer fiscalYear);

    /**
     * 查询版本历史记录
     * @param versionCode 版本编码
     * @return 版本历史记录
     */
    List<BudgetVersion> getVersionHistory(String versionCode);

    /**
     * 查询最新版本
     * @param versionCode 版本编码
     * @return 最新版本
     */
    BudgetVersion getLatestVersion(String versionCode);

    /**
     * 查询我创建的版本列表
     * @param userId 用户ID
     * @return 我创建的版本列表
     */
    List<BudgetVersion> getMyVersions(String userId);

    /**
     * 查询待审批的版本列表
     * @param userId 用户ID
     * @return 待审批的版本列表
     */
    List<BudgetVersion> getPendingApprovalVersions(String userId);

    // ==================== 业务操作 ====================

    /**
     * 发布版本
     * @param versionId 版本ID
     * @return 是否发布成功
     */
    boolean publishVersion(String versionId);

    /**
     * 取消发布版本
     * @param versionId 版本ID
     * @return 是否取消发布成功
     */
    boolean unpublishVersion(String versionId);

    /**
     * 锁定版本
     * @param versionId 版本ID
     * @param lockReason 锁定原因
     * @return 是否锁定成功
     */
    boolean lockVersion(String versionId, String lockReason);

    /**
     * 解锁版本
     * @param versionId 版本ID
     * @return 是否解锁成功
     */
    boolean unlockVersion(String versionId);

    /**
     * 设置当前版本
     * @param versionId 版本ID
     * @param fiscalYear 预算年度
     * @param modelId 预算模型ID
     * @return 是否设置成功
     */
    boolean setCurrentVersion(String versionId, Integer fiscalYear, String modelId);

    /**
     * 设置基准版本
     * @param versionId 版本ID
     * @param fiscalYear 预算年度
     * @param modelId 预算模型ID
     * @return 是否设置成功
     */
    boolean setBaselineVersion(String versionId, Integer fiscalYear, String modelId);

    /**
     * 设置最终版本
     * @param versionId 版本ID
     * @param fiscalYear 预算年度
     * @param modelId 预算模型ID
     * @return 是否设置成功
     */
    boolean setFinalVersion(String versionId, Integer fiscalYear, String modelId);

    /**
     * 归档版本
     * @param versionId 版本ID
     * @return 是否归档成功
     */
    boolean archiveVersion(String versionId);

    /**
     * 复制版本
     * @param sourceVersionId 源版本ID
     * @param newVersionCode 新版本编码
     * @param newVersionName 新版本名称
     * @return 新版本ID
     */
    String copyVersion(String sourceVersionId, String newVersionCode, String newVersionName);

    /**
     * 创建版本快照
     * @param versionId 版本ID
     * @param snapshotName 快照名称
     * @return 快照版本ID
     */
    String createVersionSnapshot(String versionId, String snapshotName);

    /**
     * 恢复版本
     * @param versionId 版本ID
     * @param targetVersionId 目标版本ID
     * @return 是否恢复成功
     */
    boolean restoreVersion(String versionId, String targetVersionId);

    /**
     * 合并版本
     * @param sourceVersionId 源版本ID
     * @param targetVersionId 目标版本ID
     * @param mergeStrategy 合并策略
     * @return 合并后的版本ID
     */
    String mergeVersions(String sourceVersionId, String targetVersionId, String mergeStrategy);

    /**
     * 比较版本
     * @param version1Id 版本1ID
     * @param version2Id 版本2ID
     * @return 比较结果
     */
    Map<String, Object> compareVersions(String version1Id, String version2Id);

    /**
     * 批量发布版本
     * @param versionIds 版本ID列表
     * @return 发布成功的版本数量
     */
    int batchPublishVersions(List<String> versionIds);

    /**
     * 批量锁定版本
     * @param versionIds 版本ID列表
     * @param lockReason 锁定原因
     * @return 锁定成功的版本数量
     */
    int batchLockVersions(List<String> versionIds, String lockReason);

    /**
     * 批量归档版本
     * @param versionIds 版本ID列表
     * @return 归档成功的版本数量
     */
    int batchArchiveVersions(List<String> versionIds);

    // ==================== 版本管理 ====================

    /**
     * 生成下一个版本号
     * @param versionCode 版本编码
     * @param versionType 版本类型
     * @return 下一个版本号
     */
    String generateNextVersionNumber(String versionCode, String versionType);

    /**
     * 验证版本兼容性
     * @param versionId 版本ID
     * @return 兼容性检查结果
     */
    Map<String, Object> validateVersionCompatibility(String versionId);

    /**
     * 检查版本依赖
     * @param versionId 版本ID
     * @return 依赖检查结果
     */
    Map<String, Object> checkVersionDependencies(String versionId);

    /**
     * 更新版本统计信息
     * @param versionId 版本ID
     * @return 是否更新成功
     */
    boolean updateVersionStatistics(String versionId);

    /**
     * 执行版本数据完整性检查
     * @param versionId 版本ID
     * @return 检查结果
     */
    Map<String, Object> performDataIntegrityCheck(String versionId);

    // ==================== 统计分析 ====================

    /**
     * 获取版本统计信息
     * @return 统计信息
     */
    Map<String, Object> getBudgetVersionStatistics();

    /**
     * 按版本类型统计数量
     * @return 统计结果
     */
    List<Map<String, Object>> getBudgetVersionCountByType();

    /**
     * 按审批状态统计数量
     * @return 统计结果
     */
    List<Map<String, Object>> getBudgetVersionCountByApprovalStatus();

    /**
     * 按年度统计版本数量
     * @return 统计结果
     */
    List<Map<String, Object>> getBudgetVersionCountByYear();

    /**
     * 获取用户版本统计信息
     * @param userId 用户ID
     * @return 用户版本统计信息
     */
    Map<String, Object> getUserVersionStatistics(String userId);

    /**
     * 获取版本使用趋势
     * @param months 月份数
     * @return 使用趋势数据
     */
    List<Map<String, Object>> getVersionUsageTrend(Integer months);

    // ==================== 数据导入导出 ====================

    /**
     * 导出版本数据
     * @param versionIds 版本ID列表
     * @return 导出文件路径
     */
    String exportVersions(List<String> versionIds);

    /**
     * 导入版本数据
     * @param filePath 文件路径
     * @return 导入结果
     */
    Map<String, Object> importVersions(String filePath);

    /**
     * 导出版本对比报告
     * @param version1Id 版本1ID
     * @param version2Id 版本2ID
     * @return 导出文件路径
     */
    String exportVersionComparisonReport(String version1Id, String version2Id);

    // ==================== 数据清理 ====================

    /**
     * 清理过期的版本
     * @param days 过期天数
     * @return 清理数量
     */
    int cleanupExpiredVersions(Integer days);

    /**
     * 清理归档的版本
     * @param days 归档天数
     * @return 清理数量
     */
    int cleanupArchivedVersions(Integer days);

    // ==================== 验证方法 ====================

    /**
     * 检查版本编码是否存在
     * @param versionCode 版本编码
     * @param excludeId 排除的版本ID
     * @return 是否存在
     */
    boolean checkVersionCodeExists(String versionCode, String excludeId);

    /**
     * 检查版本名称是否存在
     * @param versionName 版本名称
     * @param excludeId 排除的版本ID
     * @return 是否存在
     */
    boolean checkVersionNameExists(String versionName, String excludeId);

    /**
     * 检查版本是否被使用
     * @param versionId 版本ID
     * @return 是否被使用
     */
    boolean checkVersionInUse(String versionId);

    /**
     * 检查用户是否有版本操作权限
     * @param versionId 版本ID
     * @param userId 用户ID
     * @param operation 操作类型
     * @return 是否有权限
     */
    boolean hasVersionPermission(String versionId, String userId, String operation);

    /**
     * 验证版本状态转换是否有效
     * @param fromStatus 原状态
     * @param toStatus 目标状态
     * @return 是否有效
     */
    boolean isValidStatusTransition(String fromStatus, String toStatus);
}
