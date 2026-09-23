package com.huabo.contract.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.huabo.contract.entity.FinanceTransaction;
import com.huabo.contract.vo.FinanceTransactionQueryParam;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 收支管理服务接口
 * 
 * @author 华博云开发团队
 * @since 2025-01-06
 */
public interface FinanceTransactionService extends IService<FinanceTransaction> {

    /**
     * 分页查询收支记录
     */
    IPage<FinanceTransaction> getFinanceTransactionPage(FinanceTransactionQueryParam queryParam);

    /**
     * 根据项目ID查询收支记录
     */
    List<FinanceTransaction> getByProjectId(Long projectId);

    /**
     * 根据交易类型查询收支记录
     */
    List<FinanceTransaction> getByTransactionType(Integer transactionType);

    /**
     * 根据交易分类查询收支记录
     */
    List<FinanceTransaction> getByTransactionCategory(Integer transactionCategory);

    /**
     * 根据审批状态查询收支记录
     */
    List<FinanceTransaction> getByApprovalStatus(Integer approvalStatus);

    /**
     * 根据财务确认状态查询收支记录
     */
    List<FinanceTransaction> getByFinanceStatus(Integer financeStatus);

    /**
     * 根据审批人ID查询收支记录
     */
    List<FinanceTransaction> getByApproverId(Long approverId);

    /**
     * 根据财务确认人ID查询收支记录
     */
    List<FinanceTransaction> getByFinanceConfirmerId(Long financeConfirmerId);

    /**
     * 根据合同ID查询收支记录
     */
    List<FinanceTransaction> getByContractId(Long contractId);

    /**
     * 根据预算ID查询收支记录
     */
    List<FinanceTransaction> getByBudgetId(Long budgetId);

    /**
     * 根据成本中心查询收支记录
     */
    List<FinanceTransaction> getByCostCenter(String costCenter);

    /**
     * 根据会计科目查询收支记录
     */
    List<FinanceTransaction> getByAccountingSubject(String accountingSubject);

    /**
     * 根据创建人查询收支记录
     */
    List<FinanceTransaction> getByCreateBy(Long createBy);

    /**
     * 根据项目ID统计收入总额
     */
    BigDecimal sumIncomeByProject(Long projectId);

    /**
     * 根据项目ID统计支出总额
     */
    BigDecimal sumExpenseByProject(Long projectId);

    /**
     * 根据交易类型统计金额
     */
    BigDecimal sumAmountByTransactionType(Integer transactionType);

    /**
     * 根据交易分类统计金额
     */
    BigDecimal sumAmountByTransactionCategory(Integer transactionCategory);

    /**
     * 根据审批状态统计数量
     */
    Integer countByApprovalStatus(Integer approvalStatus);

    /**
     * 根据财务确认状态统计数量
     */
    Integer countByFinanceStatus(Integer financeStatus);

    /**
     * 查询待审批的收支记录
     */
    List<FinanceTransaction> getPendingApproval();

    /**
     * 查询待财务确认的收支记录
     */
    List<FinanceTransaction> getPendingFinanceConfirm();

    /**
     * 查询大额交易记录
     */
    List<FinanceTransaction> getLargeAmountTransactions();

    /**
     * 查询逾期未审批的收支记录
     */
    List<FinanceTransaction> getOverdueApproval(Integer days);

    /**
     * 批量更新审批状态
     */
    Integer batchUpdateApprovalStatus(List<Long> ids, Integer approvalStatus, Long approverId, String approvalComments);

    /**
     * 批量更新财务确认状态
     */
    Integer batchUpdateFinanceStatus(List<Long> ids, Integer financeStatus, Long financeConfirmerId, String financeComments);

    /**
     * 根据日期范围查询收支记录
     */
    List<FinanceTransaction> getByDateRange(Date startDate, Date endDate);

    /**
     * 根据金额范围查询收支记录
     */
    List<FinanceTransaction> getByAmountRange(BigDecimal minAmount, BigDecimal maxAmount);

    /**
     * 查询收支统计信息
     */
    List<FinanceTransaction> getFinanceStatistics();

    /**
     * 根据关键词搜索收支记录
     */
    List<FinanceTransaction> searchByKeyword(String keyword, Integer limit);

    /**
     * 审批收支记录
     */
    Boolean approveTransaction(Long id, Integer approvalStatus, Long approverId, String approvalComments);

    /**
     * 财务确认收支记录
     */
    Boolean confirmFinance(Long id, Integer financeStatus, Long financeConfirmerId, String financeComments);

    /**
     * 生成交易编号
     */
    String generateTransactionNo();

    /**
     * 验证收支记录信息
     */
    Boolean validateTransactionInfo(FinanceTransaction financeTransaction);

    /**
     * 获取项目收支汇总
     */
    List<String> getProjectFinanceSummary(Long projectId);
}
