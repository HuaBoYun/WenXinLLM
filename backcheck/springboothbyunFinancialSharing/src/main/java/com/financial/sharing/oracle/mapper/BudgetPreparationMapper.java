package com.financial.sharing.oracle.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.financial.sharing.oracle.entity.BudgetPreparationEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 预算编制Mapper接口
 *
 * @author Financial Sharing System
 * @since 2024-12-29
 */
public interface BudgetPreparationMapper extends BaseMapper<BudgetPreparationEntity> {

    /**
     * 分页查询预算编制列表
     *
     * @param bookId 账套ID
     * @param tenantId 租户ID
     * @param budgetYear 预算年度
     * @param budgetType 预算类型
     * @param costCenterId 成本中心ID
     * @param approvalStatus 审批状态
     * @param offset 偏移量
     * @param pageSize 每页数量
     * @return 预算列表
     */
    List<Map<String, Object>> selectBudgetListWithPagination(
            @Param("bookId") String bookId,
            @Param("tenantId") String tenantId,
            @Param("budgetYear") String budgetYear,
            @Param("budgetType") String budgetType,
            @Param("costCenterId") String costCenterId,
            @Param("approvalStatus") Integer approvalStatus,
            @Param("offset") Integer offset,
            @Param("pageSize") Integer pageSize
    );

    /**
     * 查询总记录数
     *
     * @param bookId 账套ID
     * @param tenantId 租户ID
     * @param budgetYear 预算年度
     * @param budgetType 预算类型
     * @param costCenterId 成本中心ID
     * @param approvalStatus 审批状态
     * @return 总记录数
     */
    int countBudgetList(
            @Param("bookId") String bookId,
            @Param("tenantId") String tenantId,
            @Param("budgetYear") String budgetYear,
            @Param("budgetType") String budgetType,
            @Param("costCenterId") String costCenterId,
            @Param("approvalStatus") Integer approvalStatus
    );

    /**
     * 根据预算编号查询预算
     *
     * @param budgetNo 预算编号
     * @param bookId 账套ID
     * @param tenantId 租户ID
     * @return 预算信息
     */
    Map<String, Object> selectByBudgetNo(
            @Param("budgetNo") String budgetNo,
            @Param("bookId") String bookId,
            @Param("tenantId") String tenantId
    );

    /**
     * 批量更新审批状态
     *
     * @param budgetIds 预算ID列表
     * @param approvalStatus 审批状态
     * @param approverId 审批人ID
     * @param approverName 审批人姓名
     * @param approvalComment 审批意见
     * @return 更新数量
     */
    int batchUpdateApprovalStatus(
            @Param("budgetIds") List<String> budgetIds,
            @Param("approvalStatus") Integer approvalStatus,
            @Param("approverId") String approverId,
            @Param("approverName") String approverName,
            @Param("approvalComment") String approvalComment
    );

    /**
     * 检查预算编号是否存在
     *
     * @param budgetNo 预算编号
     * @param budgetId 预算ID（更新时排除自己）
     * @param bookId 账套ID
     * @param tenantId 租户ID
     * @return 数量
     */
    int checkBudgetNoExists(
            @Param("budgetNo") String budgetNo,
            @Param("budgetId") String budgetId,
            @Param("bookId") String bookId,
            @Param("tenantId") String tenantId
    );

    /**
     * 获取预算统计数据
     *
     * @param budgetYear 预算年度
     * @param bookId 账套ID
     * @param tenantId 租户ID
     * @return 统计数据
     */
    Map<String, Object> selectBudgetStatistics(
            @Param("budgetYear") String budgetYear,
            @Param("bookId") String bookId,
            @Param("tenantId") String tenantId
    );

    /**
     * 获取成本估算首页统计概览
     * 返回字段：budgetProjects / totalBudget / actualCost / varianceRate
     * 数据源：TBL_BUDGET_PREPARATION + TBL_VARIANCE_ANALYSIS 跨表聚合
     */
    Map<String, Object> selectCostEstimateSummary(
            @Param("bookId") String bookId,
            @Param("tenantId") String tenantId
    );
}

