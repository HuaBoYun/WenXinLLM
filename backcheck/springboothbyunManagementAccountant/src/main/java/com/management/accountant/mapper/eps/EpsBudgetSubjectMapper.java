package com.management.accountant.mapper.eps;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.management.accountant.entity.eps.EpsBudgetSubject;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 预算科目数据访问接口
 * 
 * @author 华博云
 * @version 3.0.0
 */
@Mapper
public interface EpsBudgetSubjectMapper extends BaseMapper<EpsBudgetSubject> {

    /**
     * 根据科目编码查询科目
     * 
     * @param subjectCode 科目编码
     * @return 预算科目
     */
    EpsBudgetSubject selectBySubjectCode(@Param("subjectCode") String subjectCode);

    /**
     * 根据预算体系ID查询科目列表
     * 
     * @param systemId 预算体系ID
     * @return 科目列表
     */
    List<EpsBudgetSubject> selectBySystemId(@Param("systemId") Long systemId);

    /**
     * 根据科目类型查询科目列表
     * 
     * @param subjectType 科目类型
     * @param systemId 预算体系ID
     * @return 科目列表
     */
    List<EpsBudgetSubject> selectBySubjectType(@Param("subjectType") String subjectType,
                                              @Param("systemId") Long systemId);

    /**
     * 根据父科目ID查询子科目列表
     * 
     * @param parentSubjectId 父科目ID
     * @return 子科目列表
     */
    List<EpsBudgetSubject> selectByParentSubjectId(@Param("parentSubjectId") Long parentSubjectId);

    /**
     * 查询科目树结构
     * 
     * @param systemId 预算体系ID
     * @param subjectType 科目类型
     * @param includeDisabled 是否包含禁用科目
     * @return 科目树
     */
    List<Map<String, Object>> selectSubjectTree(@Param("systemId") Long systemId,
                                               @Param("subjectType") String subjectType,
                                               @Param("includeDisabled") Boolean includeDisabled);

    /**
     * 查询科目路径
     * 
     * @param subjectId 科目ID
     * @return 科目路径
     */
    List<EpsBudgetSubject> selectSubjectPath(@Param("subjectId") Long subjectId);

    /**
     * 检查科目编码是否存在
     * 
     * @param subjectCode 科目编码
     * @param excludeId 排除的ID
     * @return 存在数量
     */
    int checkSubjectCodeExists(@Param("subjectCode") String subjectCode,
                              @Param("excludeId") Long excludeId);

    /**
     * 获取下一个排序号
     * 
     * @param parentSubjectId 父科目ID
     * @return 下一个排序号
     */
    Integer getNextSortOrder(@Param("parentSubjectId") Long parentSubjectId);

    /**
     * 批量更新状态
     * 
     * @param subjectIds 科目ID列表
     * @param isEnabled 是否启用
     * @param updatedBy 更新人ID
     * @return 更新行数
     */
    int batchUpdateStatus(@Param("subjectIds") List<Long> subjectIds,
                         @Param("isEnabled") Boolean isEnabled,
                         @Param("updatedBy") Long updatedBy);

    /**
     * 批量更新父科目
     * 
     * @param subjectIds 科目ID列表
     * @param parentSubjectId 父科目ID
     * @return 更新行数
     */
    int batchUpdateParent(@Param("subjectIds") List<Long> subjectIds,
                         @Param("parentSubjectId") Long parentSubjectId);

    /**
     * 更新科目层级信息
     * 
     * @param subjectId 科目ID
     * @param subjectLevel 科目层级
     * @param subjectPath 科目路径
     * @return 更新行数
     */
    int updateSubjectHierarchy(@Param("subjectId") Long subjectId,
                              @Param("subjectLevel") Integer subjectLevel,
                              @Param("subjectPath") String subjectPath);

    /**
     * 批量更新排序号
     * 
     * @param sortUpdates 排序更新列表
     * @return 更新行数
     */
    int batchUpdateSortOrder(@Param("sortUpdates") List<Map<String, Object>> sortUpdates);

    /**
     * 查询科目统计信息
     * 
     * @param systemId 预算体系ID
     * @param subjectType 科目类型
     * @return 统计信息
     */
    Map<String, Object> selectSubjectStatistics(@Param("systemId") Long systemId,
                                               @Param("subjectType") String subjectType);

    /**
     * 查询科目层级统计
     * 
     * @param systemId 预算体系ID
     * @return 层级统计
     */
    List<Map<String, Object>> selectSubjectLevelStatistics(@Param("systemId") Long systemId);

    /**
     * 查询科目类型统计
     * 
     * @param systemId 预算体系ID
     * @return 类型统计
     */
    List<Map<String, Object>> selectSubjectTypeStatistics(@Param("systemId") Long systemId);

    /**
     * 查询启用的科目列表
     * 
     * @param systemId 预算体系ID
     * @return 启用科目列表
     */
    List<EpsBudgetSubject> selectEnabledSubjects(@Param("systemId") Long systemId);

    /**
     * 查询禁用的科目列表
     * 
     * @param systemId 预算体系ID
     * @return 禁用科目列表
     */
    List<EpsBudgetSubject> selectDisabledSubjects(@Param("systemId") Long systemId);

    /**
     * 查询叶子科目列表
     * 
     * @param systemId 预算体系ID
     * @return 叶子科目列表
     */
    List<EpsBudgetSubject> selectLeafSubjects(@Param("systemId") Long systemId);

    /**
     * 查询非叶子科目列表
     * 
     * @param systemId 预算体系ID
     * @return 非叶子科目列表
     */
    List<EpsBudgetSubject> selectNonLeafSubjects(@Param("systemId") Long systemId);

    /**
     * 查询根科目列表
     * 
     * @param systemId 预算体系ID
     * @return 根科目列表
     */
    List<EpsBudgetSubject> selectRootSubjects(@Param("systemId") Long systemId);

    /**
     * 查询指定层级的科目列表
     * 
     * @param systemId 预算体系ID
     * @param subjectLevel 科目层级
     * @return 科目列表
     */
    List<EpsBudgetSubject> selectSubjectsByLevel(@Param("systemId") Long systemId,
                                                @Param("subjectLevel") Integer subjectLevel);

    /**
     * 查询科目使用情况
     * 
     * @param subjectId 科目ID
     * @return 使用情况
     */
    Map<String, Object> selectSubjectUsage(@Param("subjectId") Long subjectId);

    /**
     * 查询科目关联数据
     * 
     * @param subjectId 科目ID
     * @return 关联数据
     */
    Map<String, Object> selectSubjectRelatedData(@Param("subjectId") Long subjectId);

    /**
     * 查询科目配置
     * 
     * @param subjectId 科目ID
     * @return 科目配置
     */
    Map<String, Object> selectSubjectConfiguration(@Param("subjectId") Long subjectId);

    /**
     * 保存科目配置
     * 
     * @param subjectId 科目ID
     * @param configuration 配置数据
     * @return 插入或更新行数
     */
    int saveSubjectConfiguration(@Param("subjectId") Long subjectId,
                                @Param("configuration") Map<String, Object> configuration);

    /**
     * 查询科目权限
     * 
     * @param subjectId 科目ID
     * @param userId 用户ID
     * @return 权限信息
     */
    Map<String, Object> selectSubjectPermissions(@Param("subjectId") Long subjectId,
                                                @Param("userId") Long userId);

    /**
     * 保存科目权限
     * 
     * @param permissionData 权限数据
     * @return 插入或更新行数
     */
    int saveSubjectPermissions(@Param("permissionData") Map<String, Object> permissionData);

    /**
     * 查询科目变更历史
     * 
     * @param subjectId 科目ID
     * @return 变更历史
     */
    List<Map<String, Object>> selectSubjectChangeHistory(@Param("subjectId") Long subjectId);

    /**
     * 保存科目变更记录
     * 
     * @param changeRecord 变更记录
     * @return 插入行数
     */
    int insertSubjectChangeRecord(@Param("changeRecord") Map<String, Object> changeRecord);

    /**
     * 查询科目审计日志
     * 
     * @param subjectId 科目ID
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 审计日志
     */
    List<Map<String, Object>> selectSubjectAuditLogs(@Param("subjectId") Long subjectId,
                                                    @Param("startDate") String startDate,
                                                    @Param("endDate") String endDate);

    /**
     * 查询科目模板
     * 
     * @param templateType 模板类型
     * @return 科目模板
     */
    List<Map<String, Object>> selectSubjectTemplates(@Param("templateType") String templateType);

    /**
     * 批量插入科目
     * 
     * @param subjects 科目列表
     * @return 插入行数
     */
    int batchInsertSubjects(@Param("subjects") List<EpsBudgetSubject> subjects);

    /**
     * 批量更新科目
     * 
     * @param subjects 科目列表
     * @return 更新行数
     */
    int batchUpdateSubjects(@Param("subjects") List<EpsBudgetSubject> subjects);

    /**
     * 批量删除科目
     * 
     * @param subjectIds 科目ID列表
     * @return 删除行数
     */
    int batchDeleteSubjects(@Param("subjectIds") List<Long> subjectIds);

    /**
     * 恢复已删除的科目
     * 
     * @param subjectId 科目ID
     * @return 更新行数
     */
    int restoreSubject(@Param("subjectId") Long subjectId);

    /**
     * 查询科目依赖关系
     * 
     * @param subjectId 科目ID
     * @return 依赖关系
     */
    List<Map<String, Object>> selectSubjectDependencies(@Param("subjectId") Long subjectId);

    /**
     * 查询被依赖的科目
     * 
     * @param subjectId 科目ID
     * @return 被依赖的科目列表
     */
    List<EpsBudgetSubject> selectDependentSubjects(@Param("subjectId") Long subjectId);

    /**
     * 查询科目冲突
     * 
     * @param subjectId 科目ID
     * @return 冲突信息
     */
    List<Map<String, Object>> selectSubjectConflicts(@Param("subjectId") Long subjectId);

    /**
     * 查询科目差异
     * 
     * @param sourceSubjectId 源科目ID
     * @param targetSubjectId 目标科目ID
     * @return 差异信息
     */
    List<Map<String, Object>> selectSubjectDifferences(@Param("sourceSubjectId") Long sourceSubjectId,
                                                      @Param("targetSubjectId") Long targetSubjectId);

    /**
     * 同步科目数据
     * 
     * @param sourceSystemId 源体系ID
     * @param targetSystemId 目标体系ID
     * @param syncParams 同步参数
     * @return 同步结果
     */
    Map<String, Object> syncSubjectData(@Param("sourceSystemId") Long sourceSystemId,
                                       @Param("targetSystemId") Long targetSystemId,
                                       @Param("syncParams") Map<String, Object> syncParams);

    /**
     * 合并科目
     * 
     * @param sourceSubjectIds 源科目ID列表
     * @param targetSubjectId 目标科目ID
     * @param mergeParams 合并参数
     * @return 合并结果
     */
    Map<String, Object> mergeSubjects(@Param("sourceSubjectIds") List<Long> sourceSubjectIds,
                                     @Param("targetSubjectId") Long targetSubjectId,
                                     @Param("mergeParams") Map<String, Object> mergeParams);

    /**
     * 拆分科目
     * 
     * @param sourceSubjectId 源科目ID
     * @param splitParams 拆分参数
     * @return 拆分结果
     */
    Map<String, Object> splitSubject(@Param("sourceSubjectId") Long sourceSubjectId,
                                    @Param("splitParams") Map<String, Object> splitParams);

    /**
     * 验证科目数据完整性
     * 
     * @param systemId 预算体系ID
     * @return 验证结果
     */
    Map<String, Object> validateSubjectDataIntegrity(@Param("systemId") Long systemId);

    /**
     * 修复科目数据
     * 
     * @param systemId 预算体系ID
     * @param repairParams 修复参数
     * @return 修复结果
     */
    Map<String, Object> repairSubjectData(@Param("systemId") Long systemId,
                                         @Param("repairParams") Map<String, Object> repairParams);

    /**
     * 查询科目性能统计
     * 
     * @param systemId 预算体系ID
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 性能统计
     */
    Map<String, Object> selectSubjectPerformanceStatistics(@Param("systemId") Long systemId,
                                                          @Param("startDate") String startDate,
                                                          @Param("endDate") String endDate);

    /**
     * 查询热门科目
     * 
     * @param systemId 预算体系ID
     * @param limit 限制数量
     * @return 热门科目列表
     */
    List<EpsBudgetSubject> selectPopularSubjects(@Param("systemId") Long systemId,
                                               @Param("limit") Integer limit);

    /**
     * 查询最近使用的科目
     * 
     * @param systemId 预算体系ID
     * @param userId 用户ID
     * @param limit 限制数量
     * @return 最近使用科目列表
     */
    List<EpsBudgetSubject> selectRecentlyUsedSubjects(@Param("systemId") Long systemId,
                                                    @Param("userId") Long userId,
                                                    @Param("limit") Integer limit);

    /**
     * 更新科目使用次数
     * 
     * @param subjectId 科目ID
     * @return 更新行数
     */
    int updateSubjectUsageCount(@Param("subjectId") Long subjectId);

    /**
     * 更新科目最后使用时间
     * 
     * @param subjectId 科目ID
     * @return 更新行数
     */
    int updateSubjectLastUsedTime(@Param("subjectId") Long subjectId);

    /**
     * 查询科目层级结构
     * 
     * @param systemId 预算体系ID
     * @return 层级结构
     */
    Map<String, Object> selectSubjectHierarchy(@Param("systemId") Long systemId);

    /**
     * 重建科目层级
     * 
     * @param systemId 预算体系ID
     * @return 重建结果
     */
    int rebuildSubjectHierarchy(@Param("systemId") Long systemId);

    /**
     * 查询孤立科目
     * 
     * @param systemId 预算体系ID
     * @return 孤立科目列表
     */
    List<EpsBudgetSubject> selectOrphanSubjects(@Param("systemId") Long systemId);

    /**
     * 查询重复科目
     * 
     * @param systemId 预算体系ID
     * @return 重复科目列表
     */
    List<Map<String, Object>> selectDuplicateSubjects(@Param("systemId") Long systemId);

    /**
     * 查询无效科目
     * 
     * @param systemId 预算体系ID
     * @return 无效科目列表
     */
    List<EpsBudgetSubject> selectInvalidSubjects(@Param("systemId") Long systemId);

    /**
     * 清理无效数据
     * 
     * @param systemId 预算体系ID
     * @return 清理行数
     */
    int cleanupInvalidData(@Param("systemId") Long systemId);
}
