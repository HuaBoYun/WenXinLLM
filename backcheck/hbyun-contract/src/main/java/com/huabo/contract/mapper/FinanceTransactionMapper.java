package com.huabo.contract.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.huabo.contract.entity.FinanceTransaction;
import com.huabo.contract.vo.FinanceTransactionQueryParam;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 收支管理Mapper接口
 * 
 * @author 华博云开发团队
 * @since 2025-01-06
 */
@Mapper
public interface FinanceTransactionMapper extends BaseMapper<FinanceTransaction> {

    /**
     * 分页查询收支记录
     */
    IPage<FinanceTransaction> selectFinanceTransactionPage(Page<FinanceTransaction> page, @Param("param") FinanceTransactionQueryParam param);

    /**
     * 根据项目ID查询收支记录
     */
    List<FinanceTransaction> selectByProjectId(@Param("projectId") Long projectId);

    /**
     * 根据交易类型查询收支记录
     */
    List<FinanceTransaction> selectByTransactionType(@Param("transactionType") Integer transactionType);

    /**
     * 根据交易分类查询收支记录
     */
    List<FinanceTransaction> selectByTransactionCategory(@Param("transactionCategory") Integer transactionCategory);

    /**
     * 根据审批状态查询收支记录
     */
    List<FinanceTransaction> selectByApprovalStatus(@Param("approvalStatus") Integer approvalStatus);

    /**
     * 根据财务确认状态查询收支记录
     */
    List<FinanceTransaction> selectByFinanceStatus(@Param("financeStatus") Integer financeStatus);

    /**
     * 根据审批人ID查询收支记录
     */
    List<FinanceTransaction> selectByApproverId(@Param("approverId") Long approverId);

    /**
     * 根据财务确认人ID查询收支记录
     */
    List<FinanceTransaction> selectByFinanceConfirmerId(@Param("financeConfirmerId") Long financeConfirmerId);

    /**
     * 根据合同ID查询收支记录
     */
    List<FinanceTransaction> selectByContractId(@Param("contractId") Long contractId);

    /**
     * 根据预算ID查询收支记录
     */
    List<FinanceTransaction> selectByBudgetId(@Param("budgetId") Long budgetId);

    /**
     * 根据成本中心查询收支记录
     */
    List<FinanceTransaction> selectByCostCenter(@Param("costCenter") String costCenter);

    /**
     * 根据会计科目查询收支记录
     */
    List<FinanceTransaction> selectByAccountingSubject(@Param("accountingSubject") String accountingSubject);

    /**
     * 根据创建人查询收支记录
     */
    List<FinanceTransaction> selectByCreateBy(@Param("createBy") Long createBy);

    /**
     * 根据项目ID统计收入总额
     */
    BigDecimal sumIncomeByProject(@Param("projectId") Long projectId);

    /**
     * 根据项目ID统计支出总额
     */
    BigDecimal sumExpenseByProject(@Param("projectId") Long projectId);

    /**
     * 根据交易类型统计金额
     */
    BigDecimal sumAmountByTransactionType(@Param("transactionType") Integer transactionType);

    /**
     * 根据交易分类统计金额
     */
    BigDecimal sumAmountByTransactionCategory(@Param("transactionCategory") Integer transactionCategory);

    /**
     * 根据审批状态统计数量
     */
    Integer countByApprovalStatus(@Param("approvalStatus") Integer approvalStatus);

    /**
     * 根据财务确认状态统计数量
     */
    Integer countByFinanceStatus(@Param("financeStatus") Integer financeStatus);

    /**
     * 查询待审批的收支记录
     */
    List<FinanceTransaction> selectPendingApproval();

    /**
     * 查询待财务确认的收支记录
     */
    List<FinanceTransaction> selectPendingFinanceConfirm();

    /**
     * 查询大额交易记录
     */
    List<FinanceTransaction> selectLargeAmountTransactions();

    /**
     * 查询逾期未审批的收支记录
     */
    List<FinanceTransaction> selectOverdueApproval(@Param("days") Integer days);

    /**
     * 批量更新审批状态
     */
    Integer batchUpdateApprovalStatus(@Param("ids") List<Long> ids, 
                                    @Param("approvalStatus") Integer approvalStatus, 
                                    @Param("approverId") Long approverId, 
                                    @Param("approvalComments") String approvalComments);

    /**
     * 批量更新财务确认状态
     */
    Integer batchUpdateFinanceStatus(@Param("ids") List<Long> ids, 
                                   @Param("financeStatus") Integer financeStatus, 
                                   @Param("financeConfirmerId") Long financeConfirmerId, 
                                   @Param("financeComments") String financeComments);

    /**
     * 根据日期范围查询收支记录
     */
    List<FinanceTransaction> selectByDateRange(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

    /**
     * 根据金额范围查询收支记录
     */
    List<FinanceTransaction> selectByAmountRange(@Param("minAmount") BigDecimal minAmount, @Param("maxAmount") BigDecimal maxAmount);

    /**
     * 查询收支统计信息
     */
    List<FinanceTransaction> selectFinanceStatistics();

    /**
     * 根据关键词搜索收支记录
     */
    List<FinanceTransaction> searchByKeyword(@Param("keyword") String keyword, @Param("limit") Integer limit);
}
