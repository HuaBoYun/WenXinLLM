package com.management.accountant.mapper.ncv65;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.management.accountant.entity.ncv65.BudgetZeroBased;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * NCV65全面预算系统 - 零基预算数据访问接口
 * 
 * @description 零基预算数据访问层，提供零基预算的数据库操作方法
 * @author AI Assistant
 * @date 2025-01-09
 * @version 1.0.0
 */
@Mapper
public interface BudgetZeroBasedMapper extends BaseMapper<BudgetZeroBased> {

    // ==================== 基础查询方法 ====================

    /**
     * 根据零基预算编码查询
     */
    @Select("SELECT * FROM NCV65_BUDGET_ZERO_BASED WHERE ZBB_CODE = #{zbbCode} AND IS_DELETED = 0")
    BudgetZeroBased selectByZbbCode(@Param("zbbCode") String zbbCode);

    /**
     * 根据决策单元类型查询零基预算列表
     */
    @Select("SELECT * FROM NCV65_BUDGET_ZERO_BASED WHERE DECISION_UNIT_TYPE = #{decisionUnitType} AND IS_DELETED = 0 ORDER BY PRIORITY_SCORE DESC")
    List<BudgetZeroBased> selectByDecisionUnitType(@Param("decisionUnitType") String decisionUnitType);

    /**
     * 根据决策包类型查询零基预算列表
     */
    @Select("SELECT * FROM NCV65_BUDGET_ZERO_BASED WHERE DECISION_PACKAGE_TYPE = #{decisionPackageType} AND IS_DELETED = 0 ORDER BY PRIORITY_SCORE DESC")
    List<BudgetZeroBased> selectByDecisionPackageType(@Param("decisionPackageType") String decisionPackageType);

    /**
     * 根据优先级查询零基预算列表
     */
    @Select("SELECT * FROM NCV65_BUDGET_ZERO_BASED WHERE PRIORITY = #{priority} AND IS_DELETED = 0 ORDER BY PRIORITY_SCORE DESC")
    List<BudgetZeroBased> selectByPriority(@Param("priority") String priority);

    /**
     * 根据预算状态查询零基预算列表
     */
    @Select("SELECT * FROM NCV65_BUDGET_ZERO_BASED WHERE BUDGET_STATUS = #{budgetStatus} AND IS_DELETED = 0 ORDER BY PRIORITY_SCORE DESC")
    List<BudgetZeroBased> selectByBudgetStatus(@Param("budgetStatus") String budgetStatus);

    /**
     * 根据组织ID查询零基预算列表
     */
    @Select("SELECT * FROM NCV65_BUDGET_ZERO_BASED WHERE ORGANIZATION_ID = #{organizationId} AND IS_DELETED = 0 ORDER BY PRIORITY_SCORE DESC")
    List<BudgetZeroBased> selectByOrganizationId(@Param("organizationId") String organizationId);

    // ==================== 复杂查询方法 ====================

    /**
     * 分页查询零基预算
     */
    IPage<BudgetZeroBased> selectBudgetZeroBasedPage(Page<BudgetZeroBased> page, @Param("params") Map<String, Object> params);

    /**
     * 查询已批准的零基预算列表
     */
    @Select("SELECT * FROM NCV65_BUDGET_ZERO_BASED WHERE BUDGET_STATUS = 'APPROVED' AND IS_DELETED = 0 ORDER BY PRIORITY_SCORE DESC")
    List<BudgetZeroBased> selectApprovedBudgets();

    /**
     * 查询待审批的零基预算列表
     */
    @Select("SELECT * FROM NCV65_BUDGET_ZERO_BASED WHERE APPROVAL_STATUS = 'PENDING' AND IS_DELETED = 0 ORDER BY PRIORITY_SCORE DESC")
    List<BudgetZeroBased> selectPendingApprovals();

    /**
     * 查询已获得资金的零基预算列表
     */
    @Select("SELECT * FROM NCV65_BUDGET_ZERO_BASED WHERE FUNDING_STATUS IN ('PARTIALLY_FUNDED', 'FULLY_FUNDED') AND IS_DELETED = 0 ORDER BY PRIORITY_SCORE DESC")
    List<BudgetZeroBased> selectFundedBudgets();

    /**
     * 按优先级排序查询零基预算列表
     */
    @Select("SELECT * FROM NCV65_BUDGET_ZERO_BASED WHERE FISCAL_YEAR = #{fiscalYear} AND IS_DELETED = 0 ORDER BY PRIORITY_SCORE DESC, RANKING")
    List<BudgetZeroBased> selectByPriorityRanking(@Param("fiscalYear") Integer fiscalYear);

    // ==================== 业务操作方法 ====================

    /**
     * 提交预算
     */
    int submitBudget(@Param("zbbId") String zbbId, @Param("updateBy") String updateBy);

    /**
     * 审批通过
     */
    int approveBudget(@Param("zbbId") String zbbId, @Param("approvedBy") String approvedBy, @Param("approvalComments") String approvalComments);

    /**
     * 审批拒绝
     */
    int rejectBudget(@Param("zbbId") String zbbId, @Param("approvedBy") String approvedBy, @Param("approvalComments") String approvalComments);

    /**
     * 分配资金
     */
    int allocateFunding(@Param("zbbId") String zbbId, @Param("fundedAmount") java.math.BigDecimal fundedAmount, @Param("updateBy") String updateBy);

    /**
     * 更新排名
     */
    int updateRanking(@Param("zbbId") String zbbId, @Param("ranking") Integer ranking, @Param("updateBy") String updateBy);

    // ==================== 统计分析方法 ====================

    /**
     * 统计零基预算总数
     */
    @Select("SELECT COUNT(*) FROM NCV65_BUDGET_ZERO_BASED WHERE IS_DELETED = 0")
    int countTotalZeroBased();

    /**
     * 按决策单元类型统计数量
     */
    List<Map<String, Object>> countByDecisionUnitType();

    /**
     * 按决策包类型统计数量
     */
    List<Map<String, Object>> countByDecisionPackageType();

    /**
     * 按优先级统计数量
     */
    List<Map<String, Object>> countByPriority();

    /**
     * 统计预算金额汇总
     */
    Map<String, Object> sumBudgetAmounts(@Param("fiscalYear") Integer fiscalYear);

    // ==================== 数据验证方法 ====================

    /**
     * 检查零基预算编码是否存在
     */
    @Select("SELECT COUNT(*) FROM NCV65_BUDGET_ZERO_BASED WHERE ZBB_CODE = #{zbbCode} AND ID != #{excludeId} AND IS_DELETED = 0")
    int checkZbbCodeExists(@Param("zbbCode") String zbbCode, @Param("excludeId") String excludeId);

    /**
     * 检查零基预算是否可以删除
     */
    boolean checkZbbCanDelete(@Param("zbbId") String zbbId);

    /**
     * 检查零基预算是否可以修改
     */
    boolean checkZbbCanModify(@Param("zbbId") String zbbId);
}
