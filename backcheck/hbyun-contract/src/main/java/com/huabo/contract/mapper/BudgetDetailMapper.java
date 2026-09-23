package com.huabo.contract.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.huabo.contract.entity.BudgetDetail;
import com.huabo.contract.vo.BudgetDetailQueryParam;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

/**
 * 预算明细Mapper接口
 * 
 * @author 华博云开发团队
 * @since 2025-01-06
 */
@Mapper
public interface BudgetDetailMapper extends BaseMapper<BudgetDetail> {

    /**
     * 分页查询预算明细列表
     * 
     * @param page 分页参数
     * @param queryParam 查询参数
     * @return 分页结果
     */
    IPage<BudgetDetail> selectBudgetDetailPage(Page<BudgetDetail> page, @Param("param") BudgetDetailQueryParam queryParam);

    /**
     * 根据项目预算ID查询预算明细列表
     * 
     * @param budgetId 项目预算ID
     * @return 预算明细列表
     */
    List<BudgetDetail> selectByBudgetId(@Param("budgetId") Long budgetId);

    /**
     * 根据成本类别查询预算明细列表
     * 
     * @param costCategory 成本类别
     * @return 预算明细列表
     */
    List<BudgetDetail> selectByCostCategory(@Param("costCategory") Integer costCategory);

    /**
     * 根据明细状态查询预算明细列表
     * 
     * @param detailStatus 明细状态
     * @return 预算明细列表
     */
    List<BudgetDetail> selectByDetailStatus(@Param("detailStatus") Integer detailStatus);

    /**
     * 根据审批状态查询预算明细列表
     * 
     * @param approvalStatus 审批状态
     * @return 预算明细列表
     */
    List<BudgetDetail> selectByApprovalStatus(@Param("approvalStatus") Integer approvalStatus);

    /**
     * 根据风险等级查询预算明细列表
     * 
     * @param riskLevel 风险等级
     * @return 预算明细列表
     */
    List<BudgetDetail> selectByRiskLevel(@Param("riskLevel") Integer riskLevel);

    /**
     * 查询关键项预算明细列表
     * 
     * @return 关键项预算明细列表
     */
    List<BudgetDetail> selectCriticalItems();

    /**
     * 根据供应商ID查询预算明细列表
     * 
     * @param supplierId 供应商ID
     * @return 预算明细列表
     */
    List<BudgetDetail> selectBySupplierId(@Param("supplierId") Long supplierId);

    /**
     * 根据部门ID查询预算明细列表
     * 
     * @param departmentId 部门ID
     * @return 预算明细列表
     */
    List<BudgetDetail> selectByDepartmentId(@Param("departmentId") Long departmentId);

    /**
     * 查询负责人的预算明细列表
     * 
     * @param managerId 负责人ID
     * @return 预算明细列表
     */
    List<BudgetDetail> selectByManagerId(@Param("managerId") Long managerId);

    /**
     * 根据审批人ID查询预算明细列表
     * 
     * @param approverId 审批人ID
     * @return 预算明细列表
     */
    List<BudgetDetail> selectByApproverId(@Param("approverId") Long approverId);

    /**
     * 根据项目预算ID统计预算金额总额
     * 
     * @param budgetId 项目预算ID
     * @return 预算金额总额
     */
    BigDecimal sumBudgetedAmountByBudgetId(@Param("budgetId") Long budgetId);

    /**
     * 根据项目预算ID统计实际金额总额
     * 
     * @param budgetId 项目预算ID
     * @return 实际金额总额
     */
    BigDecimal sumActualAmountByBudgetId(@Param("budgetId") Long budgetId);

    /**
     * 根据成本类别统计预算金额
     * 
     * @param costCategory 成本类别
     * @return 预算金额
     */
    BigDecimal sumBudgetedAmountByCostCategory(@Param("costCategory") Integer costCategory);

    /**
     * 根据成本类别统计实际金额
     * 
     * @param costCategory 成本类别
     * @return 实际金额
     */
    BigDecimal sumActualAmountByCostCategory(@Param("costCategory") Integer costCategory);

    /**
     * 根据明细状态统计数量
     * 
     * @param detailStatus 明细状态
     * @return 数量
     */
    Integer countByDetailStatus(@Param("detailStatus") Integer detailStatus);

    /**
     * 根据审批状态统计数量
     * 
     * @param approvalStatus 审批状态
     * @return 数量
     */
    Integer countByApprovalStatus(@Param("approvalStatus") Integer approvalStatus);

    /**
     * 查询超预算的预算明细列表
     * 
     * @return 超预算的预算明细列表
     */
    List<BudgetDetail> selectOverBudget();

    /**
     * 查询高风险预算明细列表（风险等级 >= 3）
     * 
     * @return 高风险预算明细列表
     */
    List<BudgetDetail> selectHighRisk();

    /**
     * 查询待审批的预算明细列表
     * 
     * @return 待审批的预算明细列表
     */
    List<BudgetDetail> selectPendingApproval();

    /**
     * 批量更新明细状态
     * 
     * @param ids 明细ID列表
     * @param detailStatus 新状态
     * @param updateBy 更新人
     * @return 更新数量
     */
    Integer batchUpdateDetailStatus(@Param("ids") List<Long> ids, @Param("detailStatus") Integer detailStatus, @Param("updateBy") Long updateBy);

    /**
     * 批量更新审批状态
     * 
     * @param ids 明细ID列表
     * @param approvalStatus 新状态
     * @param approverId 审批人ID
     * @param approvalComments 审批意见
     * @return 更新数量
     */
    Integer batchUpdateApprovalStatus(@Param("ids") List<Long> ids, @Param("approvalStatus") Integer approvalStatus, 
                                     @Param("approverId") Long approverId, @Param("approvalComments") String approvalComments);

    /**
     * 查询预算明细统计信息
     * 
     * @return 统计信息
     */
    List<BudgetDetail> selectBudgetDetailStatistics();

    /**
     * 根据成本子类别查询预算明细列表
     * 
     * @param costSubcategory 成本子类别
     * @return 预算明细列表
     */
    List<BudgetDetail> selectByCostSubcategory(@Param("costSubcategory") String costSubcategory);
}
