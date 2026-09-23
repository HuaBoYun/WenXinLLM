package com.huabo.contract.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.huabo.contract.entity.BudgetDetail;
import com.huabo.contract.vo.BudgetDetailQueryParam;

import java.math.BigDecimal;
import java.util.List;

/**
 * 预算明细服务接口
 * 
 * @author 华博云开发团队
 * @since 2025-01-06
 */
public interface BudgetDetailService extends IService<BudgetDetail> {

    /**
     * 分页查询预算明细列表
     * 
     * @param queryParam 查询参数
     * @return 分页结果
     */
    IPage<BudgetDetail> getBudgetDetailPage(BudgetDetailQueryParam queryParam);

    /**
     * 根据项目预算ID查询预算明细列表
     * 
     * @param budgetId 项目预算ID
     * @return 预算明细列表
     */
    List<BudgetDetail> getByBudgetId(Long budgetId);

    /**
     * 根据成本类别查询预算明细列表
     * 
     * @param costCategory 成本类别
     * @return 预算明细列表
     */
    List<BudgetDetail> getByCostCategory(Integer costCategory);

    /**
     * 根据明细状态查询预算明细列表
     * 
     * @param detailStatus 明细状态
     * @return 预算明细列表
     */
    List<BudgetDetail> getByDetailStatus(Integer detailStatus);

    /**
     * 根据审批状态查询预算明细列表
     * 
     * @param approvalStatus 审批状态
     * @return 预算明细列表
     */
    List<BudgetDetail> getByApprovalStatus(Integer approvalStatus);

    /**
     * 根据风险等级查询预算明细列表
     * 
     * @param riskLevel 风险等级
     * @return 预算明细列表
     */
    List<BudgetDetail> getByRiskLevel(Integer riskLevel);

    /**
     * 查询关键项预算明细列表
     * 
     * @return 关键项预算明细列表
     */
    List<BudgetDetail> getCriticalItems();

    /**
     * 根据供应商ID查询预算明细列表
     * 
     * @param supplierId 供应商ID
     * @return 预算明细列表
     */
    List<BudgetDetail> getBySupplierId(Long supplierId);

    /**
     * 根据部门ID查询预算明细列表
     * 
     * @param departmentId 部门ID
     * @return 预算明细列表
     */
    List<BudgetDetail> getByDepartmentId(Long departmentId);

    /**
     * 查询负责人的预算明细列表
     * 
     * @param managerId 负责人ID
     * @return 预算明细列表
     */
    List<BudgetDetail> getByManagerId(Long managerId);

    /**
     * 根据审批人ID查询预算明细列表
     * 
     * @param approverId 审批人ID
     * @return 预算明细列表
     */
    List<BudgetDetail> getByApproverId(Long approverId);

    /**
     * 根据项目预算ID统计预算金额总额
     * 
     * @param budgetId 项目预算ID
     * @return 预算金额总额
     */
    BigDecimal sumBudgetedAmountByBudgetId(Long budgetId);

    /**
     * 根据项目预算ID统计实际金额总额
     * 
     * @param budgetId 项目预算ID
     * @return 实际金额总额
     */
    BigDecimal sumActualAmountByBudgetId(Long budgetId);

    /**
     * 根据成本类别统计预算金额
     * 
     * @param costCategory 成本类别
     * @return 预算金额
     */
    BigDecimal sumBudgetedAmountByCostCategory(Integer costCategory);

    /**
     * 根据成本类别统计实际金额
     * 
     * @param costCategory 成本类别
     * @return 实际金额
     */
    BigDecimal sumActualAmountByCostCategory(Integer costCategory);

    /**
     * 根据明细状态统计数量
     * 
     * @param detailStatus 明细状态
     * @return 数量
     */
    Integer countByDetailStatus(Integer detailStatus);

    /**
     * 根据审批状态统计数量
     * 
     * @param approvalStatus 审批状态
     * @return 数量
     */
    Integer countByApprovalStatus(Integer approvalStatus);

    /**
     * 查询超预算的预算明细列表
     * 
     * @return 超预算的预算明细列表
     */
    List<BudgetDetail> getOverBudget();

    /**
     * 查询高风险预算明细列表（风险等级 >= 3）
     * 
     * @return 高风险预算明细列表
     */
    List<BudgetDetail> getHighRisk();

    /**
     * 查询待审批的预算明细列表
     * 
     * @return 待审批的预算明细列表
     */
    List<BudgetDetail> getPendingApproval();

    /**
     * 批量更新明细状态
     * 
     * @param ids 明细ID列表
     * @param detailStatus 新状态
     * @param updateBy 更新人
     * @return 更新数量
     */
    Integer batchUpdateDetailStatus(List<Long> ids, Integer detailStatus, Long updateBy);

    /**
     * 批量更新审批状态
     * 
     * @param ids 明细ID列表
     * @param approvalStatus 新状态
     * @param approverId 审批人ID
     * @param approvalComments 审批意见
     * @return 更新数量
     */
    Integer batchUpdateApprovalStatus(List<Long> ids, Integer approvalStatus, Long approverId, String approvalComments);

    /**
     * 根据成本子类别查询预算明细列表
     * 
     * @param costSubcategory 成本子类别
     * @return 预算明细列表
     */
    List<BudgetDetail> getByCostSubcategory(String costSubcategory);

    /**
     * 预算明细审批
     * 
     * @param id 明细ID
     * @param approvalStatus 审批状态
     * @param approverId 审批人ID
     * @param approvalComments 审批意见
     * @return 是否成功
     */
    Boolean approveBudgetDetail(Long id, Integer approvalStatus, Long approverId, String approvalComments);

    /**
     * 预算明细调整
     * 
     * @param id 明细ID
     * @param newBudgetedAmount 新预算金额
     * @param adjustmentReason 调整原因
     * @param adjustmentDescription 调整说明
     * @param updateBy 更新人
     * @return 是否成功
     */
    Boolean adjustBudgetDetail(Long id, BigDecimal newBudgetedAmount, String adjustmentReason, String adjustmentDescription, Long updateBy);

    /**
     * 查询预算明细统计信息
     * 
     * @return 统计信息
     */
    List<BudgetDetail> getBudgetDetailStatistics();

    /**
     * 预算明细分析报告
     * 
     * @param budgetId 项目预算ID
     * @return 分析报告
     */
    List<String> getBudgetAnalysisReport(Long budgetId);

    /**
     * 验证预算明细信息
     * 
     * @param budgetDetail 预算明细信息
     * @return 验证结果
     */
    Boolean validateBudgetDetailInfo(BudgetDetail budgetDetail);
}
