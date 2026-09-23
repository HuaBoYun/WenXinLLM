package com.management.accountant.service.eps;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.management.accountant.entity.eps.EpsBudgetSubject;

import java.util.List;
import java.util.Map;

/**
 * 预算科目服务接口
 * 
 * @author 华博云
 * @version 3.0.0
 */
public interface EpsBudgetSubjectService {

    /**
     * 分页查询预算科目
     * 
     * @param current 当前页
     * @param size 每页大小
     * @param subjectName 科目名称
     * @param subjectCode 科目编码
     * @param subjectType 科目类型
     * @param systemId 预算体系ID
     * @param parentSubjectId 父科目ID
     * @return 分页结果
     */
    IPage<EpsBudgetSubject> queryBudgetSubjectPage(Long current, Long size, String subjectName, 
                                                  String subjectCode, String subjectType, 
                                                  Long systemId, Long parentSubjectId);

    /**
     * 创建预算科目
     * 
     * @param budgetSubject 预算科目
     * @return 创建结果
     */
    boolean createBudgetSubject(EpsBudgetSubject budgetSubject);

    /**
     * 更新预算科目
     * 
     * @param budgetSubject 预算科目
     * @return 更新结果
     */
    boolean updateBudgetSubject(EpsBudgetSubject budgetSubject);

    /**
     * 删除预算科目
     * 
     * @param subjectId 科目ID
     * @return 删除结果
     */
    boolean deleteBudgetSubject(Long subjectId);

    /**
     * 批量删除预算科目
     * 
     * @param subjectIds 科目ID列表
     * @return 删除结果
     */
    boolean batchDeleteBudgetSubjects(List<Long> subjectIds);

    /**
     * 根据ID查询预算科目
     * 
     * @param subjectId 科目ID
     * @return 预算科目
     */
    EpsBudgetSubject getBudgetSubjectById(Long subjectId);

    /**
     * 根据编码查询预算科目
     * 
     * @param subjectCode 科目编码
     * @return 预算科目
     */
    EpsBudgetSubject getBudgetSubjectByCode(String subjectCode);

    /**
     * 获取预算科目树
     * 
     * @param systemId 预算体系ID
     * @param subjectType 科目类型
     * @param includeDisabled 是否包含禁用科目
     * @return 科目树
     */
    List<Map<String, Object>> getBudgetSubjectTree(Long systemId, String subjectType, Boolean includeDisabled);

    /**
     * 根据预算体系查询科目列表
     * 
     * @param systemId 预算体系ID
     * @return 科目列表
     */
    List<EpsBudgetSubject> getBudgetSubjectsBySystemId(Long systemId);

    /**
     * 根据科目类型查询科目列表
     * 
     * @param subjectType 科目类型
     * @param systemId 预算体系ID
     * @return 科目列表
     */
    List<EpsBudgetSubject> getBudgetSubjectsByType(String subjectType, Long systemId);

    /**
     * 获取子科目列表
     * 
     * @param parentSubjectId 父科目ID
     * @return 子科目列表
     */
    List<EpsBudgetSubject> getChildBudgetSubjects(Long parentSubjectId);

    /**
     * 移动预算科目
     * 
     * @param subjectId 科目ID
     * @param targetParentId 目标父科目ID
     * @param targetPosition 目标位置
     * @return 移动结果
     */
    boolean moveBudgetSubject(Long subjectId, Long targetParentId, Integer targetPosition);

    /**
     * 复制预算科目
     * 
     * @param sourceSubjectId 源科目ID
     * @param targetSubjectCode 目标科目编码
     * @param targetSubjectName 目标科目名称
     * @param targetParentId 目标父科目ID
     * @param copyChildren 是否复制子科目
     * @return 复制结果
     */
    boolean copyBudgetSubject(Long sourceSubjectId, String targetSubjectCode, String targetSubjectName, 
                             Long targetParentId, Boolean copyChildren);

    /**
     * 启用预算科目
     * 
     * @param subjectId 科目ID
     * @return 启用结果
     */
    boolean enableBudgetSubject(Long subjectId);

    /**
     * 禁用预算科目
     * 
     * @param subjectId 科目ID
     * @return 禁用结果
     */
    boolean disableBudgetSubject(Long subjectId);

    /**
     * 获取科目路径
     * 
     * @param subjectId 科目ID
     * @return 科目路径
     */
    List<EpsBudgetSubject> getBudgetSubjectPath(Long subjectId);

    /**
     * 检查科目编码是否存在
     * 
     * @param subjectCode 科目编码
     * @param excludeId 排除的ID
     * @return 是否存在
     */
    boolean checkSubjectCodeExists(String subjectCode, Long excludeId);

    /**
     * 获取科目统计
     * 
     * @param systemId 预算体系ID
     * @param subjectType 科目类型
     * @return 科目统计
     */
    Map<String, Object> getBudgetSubjectStatistics(Long systemId, String subjectType);

    /**
     * 导入预算科目
     * 
     * @param importData 导入数据
     * @return 导入结果
     */
    Map<String, Object> importBudgetSubjects(Map<String, Object> importData);

    /**
     * 导出预算科目
     * 
     * @param exportParams 导出参数
     * @return 导出结果
     */
    Map<String, Object> exportBudgetSubjects(Map<String, Object> exportParams);

    /**
     * 批量操作科目
     * 
     * @param batchData 批量操作数据
     * @return 操作结果
     */
    Map<String, Object> batchOperateSubjects(Map<String, Object> batchData);

    /**
     * 获取科目层级结构
     * 
     * @param systemId 预算体系ID
     * @return 层级结构
     */
    Map<String, Object> getBudgetSubjectHierarchy(Long systemId);

    /**
     * 验证科目层级
     * 
     * @param subjectId 科目ID
     * @param parentSubjectId 父科目ID
     * @return 验证结果
     */
    boolean validateSubjectHierarchy(Long subjectId, Long parentSubjectId);

    /**
     * 获取科目配置
     * 
     * @param subjectId 科目ID
     * @return 科目配置
     */
    Map<String, Object> getSubjectConfiguration(Long subjectId);

    /**
     * 保存科目配置
     * 
     * @param subjectId 科目ID
     * @param configuration 配置数据
     * @return 保存结果
     */
    boolean saveSubjectConfiguration(Long subjectId, Map<String, Object> configuration);

    /**
     * 获取科目权限
     * 
     * @param subjectId 科目ID
     * @param userId 用户ID
     * @return 权限信息
     */
    Map<String, Object> getSubjectPermissions(Long subjectId, Long userId);

    /**
     * 设置科目权限
     * 
     * @param subjectId 科目ID
     * @param permissionData 权限数据
     * @return 设置结果
     */
    boolean setSubjectPermissions(Long subjectId, Map<String, Object> permissionData);

    /**
     * 获取科目使用情况
     * 
     * @param subjectId 科目ID
     * @return 使用情况
     */
    Map<String, Object> getSubjectUsage(Long subjectId);

    /**
     * 获取科目关联数据
     * 
     * @param subjectId 科目ID
     * @return 关联数据
     */
    Map<String, Object> getSubjectRelatedData(Long subjectId);

    /**
     * 同步科目数据
     * 
     * @param sourceSystemId 源体系ID
     * @param targetSystemId 目标体系ID
     * @param syncParams 同步参数
     * @return 同步结果
     */
    Map<String, Object> syncSubjectData(Long sourceSystemId, Long targetSystemId, Map<String, Object> syncParams);

    /**
     * 合并科目
     * 
     * @param sourceSubjectIds 源科目ID列表
     * @param targetSubjectId 目标科目ID
     * @param mergeParams 合并参数
     * @return 合并结果
     */
    Map<String, Object> mergeSubjects(List<Long> sourceSubjectIds, Long targetSubjectId, Map<String, Object> mergeParams);

    /**
     * 拆分科目
     * 
     * @param sourceSubjectId 源科目ID
     * @param splitParams 拆分参数
     * @return 拆分结果
     */
    Map<String, Object> splitSubject(Long sourceSubjectId, Map<String, Object> splitParams);

    /**
     * 获取科目模板
     * 
     * @param templateType 模板类型
     * @return 科目模板
     */
    List<Map<String, Object>> getSubjectTemplates(String templateType);

    /**
     * 应用科目模板
     * 
     * @param systemId 预算体系ID
     * @param templateId 模板ID
     * @param applyParams 应用参数
     * @return 应用结果
     */
    boolean applySubjectTemplate(Long systemId, Long templateId, Map<String, Object> applyParams);

    /**
     * 获取科目变更历史
     * 
     * @param subjectId 科目ID
     * @return 变更历史
     */
    List<Map<String, Object>> getSubjectChangeHistory(Long subjectId);

    /**
     * 保存科目变更记录
     * 
     * @param changeRecord 变更记录
     * @return 保存结果
     */
    boolean saveSubjectChangeRecord(Map<String, Object> changeRecord);

    /**
     * 获取科目审计日志
     * 
     * @param subjectId 科目ID
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 审计日志
     */
    List<Map<String, Object>> getSubjectAuditLogs(Long subjectId, String startDate, String endDate);

    /**
     * 验证科目数据完整性
     * 
     * @param systemId 预算体系ID
     * @return 验证结果
     */
    Map<String, Object> validateSubjectDataIntegrity(Long systemId);

    /**
     * 修复科目数据
     * 
     * @param systemId 预算体系ID
     * @param repairParams 修复参数
     * @return 修复结果
     */
    Map<String, Object> repairSubjectData(Long systemId, Map<String, Object> repairParams);

    /**
     * 获取科目性能统计
     * 
     * @param systemId 预算体系ID
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 性能统计
     */
    Map<String, Object> getSubjectPerformanceStatistics(Long systemId, String startDate, String endDate);
}
