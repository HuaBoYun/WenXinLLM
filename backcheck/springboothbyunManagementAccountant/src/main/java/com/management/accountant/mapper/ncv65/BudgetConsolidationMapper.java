package com.management.accountant.mapper.ncv65;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.management.accountant.entity.ncv65.BudgetConsolidation;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * NCV65全面预算系统 - 预算合并数据访问接口
 * 
 * @description 预算合并数据访问层，提供预算合并的数据库操作方法
 * @author AI Assistant
 * @date 2025-01-09
 * @version 1.0.0
 */
@Mapper
public interface BudgetConsolidationMapper extends BaseMapper<BudgetConsolidation> {

    // ==================== 基础查询方法 ====================

    /**
     * 根据合并编码查询
     */
    @Select("SELECT * FROM NCV65_BUDGET_CONSOLIDATION WHERE CONSOLIDATION_CODE = #{consolidationCode} AND IS_DELETED = 0")
    BudgetConsolidation selectByConsolidationCode(@Param("consolidationCode") String consolidationCode);

    /**
     * 根据预算年度查询合并列表
     */
    @Select("SELECT * FROM NCV65_BUDGET_CONSOLIDATION WHERE FISCAL_YEAR = #{fiscalYear} AND IS_DELETED = 0 ORDER BY CONSOLIDATION_CODE")
    List<BudgetConsolidation> selectByFiscalYear(@Param("fiscalYear") Integer fiscalYear);

    /**
     * 根据合并类型查询合并列表
     */
    @Select("SELECT * FROM NCV65_BUDGET_CONSOLIDATION WHERE CONSOLIDATION_TYPE = #{consolidationType} AND IS_DELETED = 0 ORDER BY CONSOLIDATION_CODE")
    List<BudgetConsolidation> selectByConsolidationType(@Param("consolidationType") String consolidationType);

    /**
     * 根据合并范围查询合并列表
     */
    @Select("SELECT * FROM NCV65_BUDGET_CONSOLIDATION WHERE CONSOLIDATION_SCOPE = #{consolidationScope} AND IS_DELETED = 0 ORDER BY CONSOLIDATION_CODE")
    List<BudgetConsolidation> selectByConsolidationScope(@Param("consolidationScope") String consolidationScope);

    /**
     * 根据合并方法查询合并列表
     */
    @Select("SELECT * FROM NCV65_BUDGET_CONSOLIDATION WHERE CONSOLIDATION_METHOD = #{consolidationMethod} AND IS_DELETED = 0 ORDER BY CONSOLIDATION_CODE")
    List<BudgetConsolidation> selectByConsolidationMethod(@Param("consolidationMethod") String consolidationMethod);

    /**
     * 根据合并状态查询合并列表
     */
    @Select("SELECT * FROM NCV65_BUDGET_CONSOLIDATION WHERE CONSOLIDATION_STATUS = #{consolidationStatus} AND IS_DELETED = 0 ORDER BY CONSOLIDATION_CODE")
    List<BudgetConsolidation> selectByConsolidationStatus(@Param("consolidationStatus") String consolidationStatus);

    /**
     * 根据父组织ID查询合并列表
     */
    @Select("SELECT * FROM NCV65_BUDGET_CONSOLIDATION WHERE PARENT_ORGANIZATION_ID = #{parentOrganizationId} AND IS_DELETED = 0 ORDER BY CONSOLIDATION_CODE")
    List<BudgetConsolidation> selectByParentOrganizationId(@Param("parentOrganizationId") String parentOrganizationId);

    /**
     * 根据合并层级查询合并列表
     */
    @Select("SELECT * FROM NCV65_BUDGET_CONSOLIDATION WHERE CONSOLIDATION_LEVEL = #{consolidationLevel} AND IS_DELETED = 0 ORDER BY CONSOLIDATION_CODE")
    List<BudgetConsolidation> selectByConsolidationLevel(@Param("consolidationLevel") Integer consolidationLevel);

    /**
     * 根据审批状态查询合并列表
     */
    @Select("SELECT * FROM NCV65_BUDGET_CONSOLIDATION WHERE APPROVAL_STATUS = #{approvalStatus} AND IS_DELETED = 0 ORDER BY CONSOLIDATION_CODE")
    List<BudgetConsolidation> selectByApprovalStatus(@Param("approvalStatus") String approvalStatus);

    /**
     * 根据发布状态查询合并列表
     */
    @Select("SELECT * FROM NCV65_BUDGET_CONSOLIDATION WHERE PUBLISH_STATUS = #{publishStatus} AND IS_DELETED = 0 ORDER BY CONSOLIDATION_CODE")
    List<BudgetConsolidation> selectByPublishStatus(@Param("publishStatus") String publishStatus);

    // ==================== 复杂查询方法 ====================

    /**
     * 分页查询预算合并
     */
    IPage<BudgetConsolidation> selectBudgetConsolidationPage(Page<BudgetConsolidation> page, @Param("params") Map<String, Object> params);

    /**
     * 查询待审批的合并列表
     */
    @Select("SELECT * FROM NCV65_BUDGET_CONSOLIDATION WHERE APPROVAL_STATUS = 'PENDING' AND IS_DELETED = 0 ORDER BY CREATE_TIME")
    List<BudgetConsolidation> selectPendingApprovals();

    /**
     * 查询待发布的合并列表
     */
    @Select("SELECT * FROM NCV65_BUDGET_CONSOLIDATION WHERE APPROVAL_STATUS = 'APPROVED' AND PUBLISH_STATUS = 'UNPUBLISHED' AND IS_DELETED = 0 ORDER BY CREATE_TIME")
    List<BudgetConsolidation> selectPendingPublish();

    /**
     * 查询已发布的合并列表
     */
    @Select("SELECT * FROM NCV65_BUDGET_CONSOLIDATION WHERE PUBLISH_STATUS = 'PUBLISHED' AND IS_DELETED = 0 ORDER BY PUBLISH_TIME DESC")
    List<BudgetConsolidation> selectPublishedConsolidations();

    /**
     * 查询自动合并的列表
     */
    @Select("SELECT * FROM NCV65_BUDGET_CONSOLIDATION WHERE IS_AUTO_CONSOLIDATION = 1 AND CONSOLIDATION_STATUS = 'ACTIVE' AND IS_DELETED = 0 ORDER BY CONSOLIDATION_CODE")
    List<BudgetConsolidation> selectAutoConsolidations();

    /**
     * 查询定期合并的列表
     */
    @Select("SELECT * FROM NCV65_BUDGET_CONSOLIDATION WHERE CONSOLIDATION_FREQUENCY != 'MANUAL' AND CONSOLIDATION_STATUS = 'ACTIVE' AND IS_DELETED = 0 ORDER BY NEXT_CONSOLIDATION_TIME")
    List<BudgetConsolidation> selectPeriodicConsolidations();

    /**
     * 查询需要执行的合并列表
     */
    List<BudgetConsolidation> selectConsolidationsToExecute();

    /**
     * 查询合并层次结构
     */
    List<BudgetConsolidation> selectConsolidationHierarchy(@Param("rootConsolidationId") String rootConsolidationId);

    /**
     * 查询合并及其所有子合并
     */
    List<BudgetConsolidation> selectConsolidationWithChildren(@Param("consolidationId") String consolidationId);

    /**
     * 查询合并及其所有父合并
     */
    List<BudgetConsolidation> selectConsolidationWithParents(@Param("consolidationId") String consolidationId);

    /**
     * 查询合并统计信息
     */
    Map<String, Object> selectConsolidationStatistics(@Param("fiscalYear") Integer fiscalYear);

    /**
     * 根据关键字搜索合并
     */
    List<BudgetConsolidation> searchConsolidations(@Param("keyword") String keyword);

    /**
     * 查询高优先级合并
     */
    @Select("SELECT * FROM NCV65_BUDGET_CONSOLIDATION WHERE CONSOLIDATION_PRIORITY = 'HIGH' AND CONSOLIDATION_STATUS = 'ACTIVE' AND IS_DELETED = 0 ORDER BY CONSOLIDATION_CODE")
    List<BudgetConsolidation> selectHighPriorityConsolidations();

    /**
     * 查询紧急合并
     */
    @Select("SELECT * FROM NCV65_BUDGET_CONSOLIDATION WHERE CONSOLIDATION_PRIORITY = 'URGENT' AND CONSOLIDATION_STATUS = 'ACTIVE' AND IS_DELETED = 0 ORDER BY CONSOLIDATION_CODE")
    List<BudgetConsolidation> selectUrgentConsolidations();

    // ==================== 业务操作方法 ====================

    /**
     * 启动合并
     */
    int startConsolidation(@Param("consolidationId") String consolidationId, @Param("updateBy") String updateBy);

    /**
     * 完成合并
     */
    int completeConsolidation(@Param("consolidationId") String consolidationId, @Param("updateBy") String updateBy);

    /**
     * 取消合并
     */
    int cancelConsolidation(@Param("consolidationId") String consolidationId, @Param("updateBy") String updateBy);

    /**
     * 重置合并
     */
    int resetConsolidation(@Param("consolidationId") String consolidationId, @Param("updateBy") String updateBy);

    /**
     * 审批通过
     */
    int approveConsolidation(@Param("consolidationId") String consolidationId, @Param("approvedBy") String approvedBy, @Param("approvalComments") String approvalComments);

    /**
     * 审批拒绝
     */
    int rejectConsolidation(@Param("consolidationId") String consolidationId, @Param("approvedBy") String approvedBy, @Param("approvalComments") String approvalComments);

    /**
     * 发布合并
     */
    int publishConsolidation(@Param("consolidationId") String consolidationId, @Param("publishedBy") String publishedBy);

    /**
     * 取消发布
     */
    int unpublishConsolidation(@Param("consolidationId") String consolidationId, @Param("updateBy") String updateBy);

    /**
     * 更新合并金额
     */
    int updateConsolidationAmounts(@Param("consolidationId") String consolidationId,
                                  @Param("beforeAmount") BigDecimal beforeAmount,
                                  @Param("afterAmount") BigDecimal afterAmount,
                                  @Param("adjustmentAmount") BigDecimal adjustmentAmount,
                                  @Param("eliminationAmount") BigDecimal eliminationAmount,
                                  @Param("updateBy") String updateBy);

    /**
     * 批量更新合并状态
     */
    int batchUpdateConsolidationStatus(@Param("consolidationIds") List<String> consolidationIds, @Param("status") String status, @Param("updateBy") String updateBy);

    /**
     * 批量审批合并
     */
    int batchApproveConsolidations(@Param("consolidationIds") List<String> consolidationIds, @Param("approvedBy") String approvedBy, @Param("approvalComments") String approvalComments);

    /**
     * 批量发布合并
     */
    int batchPublishConsolidations(@Param("consolidationIds") List<String> consolidationIds, @Param("publishedBy") String publishedBy);

    // ==================== 统计分析方法 ====================

    /**
     * 统计合并总数
     */
    @Select("SELECT COUNT(*) FROM NCV65_BUDGET_CONSOLIDATION WHERE IS_DELETED = 0")
    int countTotalConsolidations();

    /**
     * 按合并类型统计数量
     */
    List<Map<String, Object>> countConsolidationsByType();

    /**
     * 按合并范围统计数量
     */
    List<Map<String, Object>> countConsolidationsByScope();

    /**
     * 按合并方法统计数量
     */
    List<Map<String, Object>> countConsolidationsByMethod();

    /**
     * 按合并状态统计数量
     */
    List<Map<String, Object>> countConsolidationsByStatus();

    /**
     * 按审批状态统计数量
     */
    List<Map<String, Object>> countConsolidationsByApprovalStatus();

    /**
     * 按发布状态统计数量
     */
    List<Map<String, Object>> countConsolidationsByPublishStatus();

    /**
     * 按预算年度统计数量
     */
    List<Map<String, Object>> countConsolidationsByFiscalYear();

    /**
     * 统计合并金额汇总
     */
    Map<String, Object> sumConsolidationAmounts(@Param("fiscalYear") Integer fiscalYear);

    /**
     * 按组织统计合并金额
     */
    List<Map<String, Object>> sumConsolidationAmountsByOrganization(@Param("fiscalYear") Integer fiscalYear);

    /**
     * 统计自动合并数量
     */
    @Select("SELECT COUNT(*) FROM NCV65_BUDGET_CONSOLIDATION WHERE IS_AUTO_CONSOLIDATION = 1 AND IS_DELETED = 0")
    int countAutoConsolidations();

    /**
     * 统计定期合并数量
     */
    @Select("SELECT COUNT(*) FROM NCV65_BUDGET_CONSOLIDATION WHERE CONSOLIDATION_FREQUENCY != 'MANUAL' AND IS_DELETED = 0")
    int countPeriodicConsolidations();

    // ==================== 数据验证方法 ====================

    /**
     * 检查合并编码是否存在
     */
    @Select("SELECT COUNT(*) FROM NCV65_BUDGET_CONSOLIDATION WHERE CONSOLIDATION_CODE = #{consolidationCode} AND ID != #{excludeId} AND IS_DELETED = 0")
    int checkConsolidationCodeExists(@Param("consolidationCode") String consolidationCode, @Param("excludeId") String excludeId);

    /**
     * 检查合并名称是否存在
     */
    @Select("SELECT COUNT(*) FROM NCV65_BUDGET_CONSOLIDATION WHERE CONSOLIDATION_NAME = #{consolidationName} AND FISCAL_YEAR = #{fiscalYear} AND ID != #{excludeId} AND IS_DELETED = 0")
    int checkConsolidationNameExists(@Param("consolidationName") String consolidationName, @Param("fiscalYear") Integer fiscalYear, @Param("excludeId") String excludeId);

    /**
     * 检查合并是否可以删除
     */
    boolean checkConsolidationCanDelete(@Param("consolidationId") String consolidationId);

    /**
     * 检查合并是否可以修改
     */
    boolean checkConsolidationCanModify(@Param("consolidationId") String consolidationId);

    /**
     * 检查合并是否在使用中
     */
    boolean checkConsolidationInUse(@Param("consolidationId") String consolidationId);

    /**
     * 验证合并层次结构
     */
    boolean validateConsolidationHierarchy(@Param("consolidationId") String consolidationId, @Param("parentOrganizationId") String parentOrganizationId);

    /**
     * 检查合并冲突
     */
    boolean checkConsolidationConflict(@Param("consolidationId") String consolidationId, @Param("fiscalYear") Integer fiscalYear, @Param("organizationIds") List<String> organizationIds);

    /**
     * 验证合并规则
     */
    boolean validateConsolidationRules(@Param("consolidationRules") String consolidationRules);

    /**
     * 检查合并权限
     */
    boolean checkConsolidationPermission(@Param("consolidationId") String consolidationId, @Param("userId") String userId, @Param("operation") String operation);
}
