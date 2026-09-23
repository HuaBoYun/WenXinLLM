package com.management.accountant.service.eps;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.management.accountant.entity.eps.EpsBudgetVersion;

import java.util.List;
import java.util.Map;

/**
 * 预算版本服务接口
 * 
 * @author 华博云
 * @version 3.0.0
 */
public interface EpsBudgetVersionService {

    /**
     * 分页查询预算版本
     * 
     * @param current 当前页
     * @param size 每页大小
     * @param versionName 版本名称
     * @param versionStatus 版本状态
     * @param systemId 预算体系ID
     * @param budgetYear 预算年度
     * @return 分页结果
     */
    IPage<EpsBudgetVersion> queryBudgetVersionPage(Long current, Long size, String versionName, 
                                                  String versionStatus, Long systemId, Integer budgetYear);

    /**
     * 创建预算版本
     * 
     * @param budgetVersion 预算版本
     * @return 创建结果
     */
    boolean createBudgetVersion(EpsBudgetVersion budgetVersion);

    /**
     * 更新预算版本
     * 
     * @param budgetVersion 预算版本
     * @return 更新结果
     */
    boolean updateBudgetVersion(EpsBudgetVersion budgetVersion);

    /**
     * 删除预算版本
     * 
     * @param versionId 版本ID
     * @return 删除结果
     */
    boolean deleteBudgetVersion(Long versionId);

    /**
     * 批量删除预算版本
     * 
     * @param versionIds 版本ID列表
     * @return 删除结果
     */
    boolean batchDeleteBudgetVersions(List<Long> versionIds);

    /**
     * 根据ID查询预算版本
     * 
     * @param versionId 版本ID
     * @return 预算版本
     */
    EpsBudgetVersion getBudgetVersionById(Long versionId);

    /**
     * 根据编码查询预算版本
     * 
     * @param versionCode 版本编码
     * @return 预算版本
     */
    EpsBudgetVersion getBudgetVersionByCode(String versionCode);

    /**
     * 根据预算体系查询版本列表
     * 
     * @param systemId 预算体系ID
     * @return 版本列表
     */
    List<EpsBudgetVersion> getBudgetVersionsBySystemId(Long systemId);

    /**
     * 查询当前版本
     * 
     * @param systemId 预算体系ID
     * @return 当前版本
     */
    EpsBudgetVersion getCurrentBudgetVersion(Long systemId);

    /**
     * 设置当前版本
     * 
     * @param versionId 版本ID
     * @return 设置结果
     */
    boolean setCurrentBudgetVersion(Long versionId);

    /**
     * 发布预算版本
     * 
     * @param versionId 版本ID
     * @param publishInfo 发布信息
     * @return 发布结果
     */
    boolean publishBudgetVersion(Long versionId, Map<String, Object> publishInfo);

    /**
     * 撤销发布预算版本
     * 
     * @param versionId 版本ID
     * @param reason 撤销原因
     * @return 撤销结果
     */
    boolean unpublishBudgetVersion(Long versionId, String reason);

    /**
     * 锁定预算版本
     * 
     * @param versionId 版本ID
     * @param lockReason 锁定原因
     * @return 锁定结果
     */
    boolean lockBudgetVersion(Long versionId, String lockReason);

    /**
     * 解锁预算版本
     * 
     * @param versionId 版本ID
     * @return 解锁结果
     */
    boolean unlockBudgetVersion(Long versionId);

    /**
     * 复制预算版本
     * 
     * @param sourceVersionId 源版本ID
     * @param targetVersionCode 目标版本编码
     * @param targetVersionName 目标版本名称
     * @param copyData 是否复制数据
     * @return 复制结果
     */
    boolean copyBudgetVersion(Long sourceVersionId, String targetVersionCode, String targetVersionName, Boolean copyData);

    /**
     * 比较预算版本
     * 
     * @param sourceVersionId 源版本ID
     * @param targetVersionId 目标版本ID
     * @param compareType 比较类型
     * @return 比较结果
     */
    Map<String, Object> compareBudgetVersions(Long sourceVersionId, Long targetVersionId, String compareType);

    /**
     * 获取版本历史
     * 
     * @param versionId 版本ID
     * @return 版本历史
     */
    List<Map<String, Object>> getBudgetVersionHistory(Long versionId);

    /**
     * 获取版本统计
     * 
     * @param systemId 预算体系ID
     * @param budgetYear 预算年度
     * @return 版本统计
     */
    Map<String, Object> getBudgetVersionStatistics(Long systemId, Integer budgetYear);

    /**
     * 批量操作版本
     * 
     * @param batchData 批量操作数据
     * @return 操作结果
     */
    Map<String, Object> batchOperateVersions(Map<String, Object> batchData);

    /**
     * 检查版本编码是否存在
     * 
     * @param versionCode 版本编码
     * @param excludeId 排除的ID
     * @return 是否存在
     */
    boolean checkVersionCodeExists(String versionCode, Long excludeId);

    /**
     * 获取版本树结构
     * 
     * @param systemId 预算体系ID
     * @return 版本树
     */
    List<Map<String, Object>> getBudgetVersionTree(Long systemId);

    /**
     * 激活预算版本
     * 
     * @param versionId 版本ID
     * @return 激活结果
     */
    boolean activateBudgetVersion(Long versionId);

    /**
     * 停用预算版本
     * 
     * @param versionId 版本ID
     * @return 停用结果
     */
    boolean deactivateBudgetVersion(Long versionId);

    /**
     * 归档预算版本
     * 
     * @param versionId 版本ID
     * @param archiveReason 归档原因
     * @return 归档结果
     */
    boolean archiveBudgetVersion(Long versionId, String archiveReason);

    /**
     * 恢复预算版本
     * 
     * @param versionId 版本ID
     * @return 恢复结果
     */
    boolean restoreBudgetVersion(Long versionId);

    /**
     * 获取版本权限
     * 
     * @param versionId 版本ID
     * @param userId 用户ID
     * @return 权限信息
     */
    Map<String, Object> getVersionPermissions(Long versionId, Long userId);

    /**
     * 设置版本权限
     * 
     * @param versionId 版本ID
     * @param permissionData 权限数据
     * @return 设置结果
     */
    boolean setVersionPermissions(Long versionId, Map<String, Object> permissionData);

    /**
     * 获取版本配置
     * 
     * @param versionId 版本ID
     * @return 版本配置
     */
    Map<String, Object> getVersionConfiguration(Long versionId);

    /**
     * 保存版本配置
     * 
     * @param versionId 版本ID
     * @param configuration 配置数据
     * @return 保存结果
     */
    boolean saveVersionConfiguration(Long versionId, Map<String, Object> configuration);

    /**
     * 导出版本数据
     * 
     * @param versionId 版本ID
     * @param exportParams 导出参数
     * @return 导出结果
     */
    Map<String, Object> exportVersionData(Long versionId, Map<String, Object> exportParams);

    /**
     * 导入版本数据
     * 
     * @param versionId 版本ID
     * @param importData 导入数据
     * @return 导入结果
     */
    Map<String, Object> importVersionData(Long versionId, Map<String, Object> importData);

    /**
     * 计算版本数据
     * 
     * @param versionId 版本ID
     * @param calculationParams 计算参数
     * @return 计算结果
     */
    Map<String, Object> calculateVersionData(Long versionId, Map<String, Object> calculationParams);

    /**
     * 验证版本数据
     * 
     * @param versionId 版本ID
     * @param validationRules 验证规则
     * @return 验证结果
     */
    Map<String, Object> validateVersionData(Long versionId, Map<String, Object> validationRules);

    /**
     * 同步版本数据
     * 
     * @param sourceVersionId 源版本ID
     * @param targetVersionId 目标版本ID
     * @param syncParams 同步参数
     * @return 同步结果
     */
    Map<String, Object> syncVersionData(Long sourceVersionId, Long targetVersionId, Map<String, Object> syncParams);

    /**
     * 获取版本差异
     * 
     * @param sourceVersionId 源版本ID
     * @param targetVersionId 目标版本ID
     * @return 差异信息
     */
    Map<String, Object> getVersionDifferences(Long sourceVersionId, Long targetVersionId);

    /**
     * 合并版本
     * 
     * @param sourceVersionIds 源版本ID列表
     * @param targetVersionId 目标版本ID
     * @param mergeParams 合并参数
     * @return 合并结果
     */
    Map<String, Object> mergeVersions(List<Long> sourceVersionIds, Long targetVersionId, Map<String, Object> mergeParams);

    /**
     * 分支版本
     * 
     * @param sourceVersionId 源版本ID
     * @param branchVersionCode 分支版本编码
     * @param branchVersionName 分支版本名称
     * @return 分支结果
     */
    boolean branchVersion(Long sourceVersionId, String branchVersionCode, String branchVersionName);

    /**
     * 获取版本依赖
     * 
     * @param versionId 版本ID
     * @return 依赖信息
     */
    Map<String, Object> getVersionDependencies(Long versionId);

    /**
     * 检查版本冲突
     * 
     * @param versionId 版本ID
     * @return 冲突信息
     */
    Map<String, Object> checkVersionConflicts(Long versionId);

    /**
     * 解决版本冲突
     * 
     * @param versionId 版本ID
     * @param resolutionData 解决方案数据
     * @return 解决结果
     */
    boolean resolveVersionConflicts(Long versionId, Map<String, Object> resolutionData);

    /**
     * 获取版本标签
     * 
     * @param versionId 版本ID
     * @return 标签列表
     */
    List<String> getVersionTags(Long versionId);

    /**
     * 设置版本标签
     * 
     * @param versionId 版本ID
     * @param tags 标签列表
     * @return 设置结果
     */
    boolean setVersionTags(Long versionId, List<String> tags);

    /**
     * 获取版本评论
     * 
     * @param versionId 版本ID
     * @return 评论列表
     */
    List<Map<String, Object>> getVersionComments(Long versionId);

    /**
     * 添加版本评论
     * 
     * @param versionId 版本ID
     * @param commentData 评论数据
     * @return 添加结果
     */
    boolean addVersionComment(Long versionId, Map<String, Object> commentData);

    /**
     * 获取版本附件
     * 
     * @param versionId 版本ID
     * @return 附件列表
     */
    List<Map<String, Object>> getVersionAttachments(Long versionId);

    /**
     * 上传版本附件
     * 
     * @param versionId 版本ID
     * @param attachmentData 附件数据
     * @return 上传结果
     */
    boolean uploadVersionAttachment(Long versionId, Map<String, Object> attachmentData);

    /**
     * 删除版本附件
     * 
     * @param versionId 版本ID
     * @param attachmentId 附件ID
     * @return 删除结果
     */
    boolean deleteVersionAttachment(Long versionId, Long attachmentId);
}
