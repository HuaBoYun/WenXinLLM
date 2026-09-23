package com.huabo.contract.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huabo.contract.entity.FinanceTransaction;
import com.huabo.contract.mapper.FinanceTransactionMapper;
import com.huabo.contract.service.FinanceTransactionService;
import com.huabo.contract.vo.FinanceTransactionQueryParam;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 收支管理服务实现类
 * 
 * @author 华博云开发团队
 * @since 2025-01-06
 */
@Service
public class FinanceTransactionServiceImpl extends ServiceImpl<FinanceTransactionMapper, FinanceTransaction> implements FinanceTransactionService {

    @Override
    public IPage<FinanceTransaction> getFinanceTransactionPage(FinanceTransactionQueryParam queryParam) {
        Page<FinanceTransaction> page = new Page<>(queryParam.getCurrent(), queryParam.getSize());
        return baseMapper.selectFinanceTransactionPage(page, queryParam);
    }

    @Override
    public List<FinanceTransaction> getByProjectId(Long projectId) {
        return baseMapper.selectByProjectId(projectId);
    }

    @Override
    public List<FinanceTransaction> getByTransactionType(Integer transactionType) {
        return baseMapper.selectByTransactionType(transactionType);
    }

    @Override
    public List<FinanceTransaction> getByTransactionCategory(Integer transactionCategory) {
        return baseMapper.selectByTransactionCategory(transactionCategory);
    }

    @Override
    public List<FinanceTransaction> getByApprovalStatus(Integer approvalStatus) {
        return baseMapper.selectByApprovalStatus(approvalStatus);
    }

    @Override
    public List<FinanceTransaction> getByFinanceStatus(Integer financeStatus) {
        return baseMapper.selectByFinanceStatus(financeStatus);
    }

    @Override
    public List<FinanceTransaction> getByApproverId(Long approverId) {
        return baseMapper.selectByApproverId(approverId);
    }

    @Override
    public List<FinanceTransaction> getByFinanceConfirmerId(Long financeConfirmerId) {
        return baseMapper.selectByFinanceConfirmerId(financeConfirmerId);
    }

    @Override
    public List<FinanceTransaction> getByContractId(Long contractId) {
        return baseMapper.selectByContractId(contractId);
    }

    @Override
    public List<FinanceTransaction> getByBudgetId(Long budgetId) {
        return baseMapper.selectByBudgetId(budgetId);
    }

    @Override
    public List<FinanceTransaction> getByCostCenter(String costCenter) {
        return baseMapper.selectByCostCenter(costCenter);
    }

    @Override
    public List<FinanceTransaction> getByAccountingSubject(String accountingSubject) {
        return baseMapper.selectByAccountingSubject(accountingSubject);
    }

    @Override
    public List<FinanceTransaction> getByCreateBy(Long createBy) {
        return baseMapper.selectByCreateBy(createBy);
    }

    @Override
    public BigDecimal sumIncomeByProject(Long projectId) {
        return baseMapper.sumIncomeByProject(projectId);
    }

    @Override
    public BigDecimal sumExpenseByProject(Long projectId) {
        return baseMapper.sumExpenseByProject(projectId);
    }

    @Override
    public BigDecimal sumAmountByTransactionType(Integer transactionType) {
        return baseMapper.sumAmountByTransactionType(transactionType);
    }

    @Override
    public BigDecimal sumAmountByTransactionCategory(Integer transactionCategory) {
        return baseMapper.sumAmountByTransactionCategory(transactionCategory);
    }

    @Override
    public Integer countByApprovalStatus(Integer approvalStatus) {
        return baseMapper.countByApprovalStatus(approvalStatus);
    }

    @Override
    public Integer countByFinanceStatus(Integer financeStatus) {
        return baseMapper.countByFinanceStatus(financeStatus);
    }

    @Override
    public List<FinanceTransaction> getPendingApproval() {
        return baseMapper.selectPendingApproval();
    }

    @Override
    public List<FinanceTransaction> getPendingFinanceConfirm() {
        return baseMapper.selectPendingFinanceConfirm();
    }

    @Override
    public List<FinanceTransaction> getLargeAmountTransactions() {
        return baseMapper.selectLargeAmountTransactions();
    }

    @Override
    public List<FinanceTransaction> getOverdueApproval(Integer days) {
        return baseMapper.selectOverdueApproval(days);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer batchUpdateApprovalStatus(List<Long> ids, Integer approvalStatus, Long approverId, String approvalComments) {
        return baseMapper.batchUpdateApprovalStatus(ids, approvalStatus, approverId, approvalComments);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer batchUpdateFinanceStatus(List<Long> ids, Integer financeStatus, Long financeConfirmerId, String financeComments) {
        return baseMapper.batchUpdateFinanceStatus(ids, financeStatus, financeConfirmerId, financeComments);
    }

    @Override
    public List<FinanceTransaction> getByDateRange(Date startDate, Date endDate) {
        return baseMapper.selectByDateRange(startDate, endDate);
    }

    @Override
    public List<FinanceTransaction> getByAmountRange(BigDecimal minAmount, BigDecimal maxAmount) {
        return baseMapper.selectByAmountRange(minAmount, maxAmount);
    }

    @Override
    public List<FinanceTransaction> getFinanceStatistics() {
        return baseMapper.selectFinanceStatistics();
    }

    @Override
    public List<FinanceTransaction> searchByKeyword(String keyword, Integer limit) {
        return baseMapper.searchByKeyword(keyword, limit);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean approveTransaction(Long id, Integer approvalStatus, Long approverId, String approvalComments) {
        FinanceTransaction transaction = getById(id);
        if (transaction == null) {
            return false;
        }
        
        transaction.setApprovalStatus(approvalStatus);
        transaction.setApproverId(approverId != null ? approverId.toString() : null);
        transaction.setApprovalTime(new Date());
        transaction.setApprovalComments(approvalComments);
        transaction.setUpdateTime(new Date());
        
        return updateById(transaction);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean confirmFinance(Long id, Integer financeStatus, Long financeConfirmerId, String financeComments) {
        FinanceTransaction transaction = getById(id);
        if (transaction == null) {
            return false;
        }
        
        transaction.setFinanceStatus(financeStatus);
        transaction.setFinanceConfirmerId(financeConfirmerId != null ? financeConfirmerId.toString() : null);
        transaction.setFinanceConfirmTime(new Date());
        transaction.setFinanceComments(financeComments);
        transaction.setUpdateTime(new Date());
        
        return updateById(transaction);
    }

    @Override
    public String generateTransactionNo() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String dateStr = sdf.format(new Date());
        long timestamp = System.currentTimeMillis() % 10000;
        return "FT" + dateStr + String.format("%04d", timestamp);
    }

    @Override
    public Boolean validateTransactionInfo(FinanceTransaction financeTransaction) {
        if (financeTransaction == null) {
            System.out.println("验证失败：financeTransaction 为 null");
            return false;
        }

        // 验证必填字段
        if (financeTransaction.getProjectId() == null) {
            System.out.println("验证失败：projectId 为 null");
            return false;
        }
        if (financeTransaction.getTransactionType() == null) {
            System.out.println("验证失败：transactionType 为 null");
            return false;
        }
        if (financeTransaction.getAmount() == null) {
            System.out.println("验证失败：amount 为 null");
            return false;
        }

        // 验证交易金额
        BigDecimal amount = financeTransaction.getAmount();
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            System.out.println("验证失败：金额无效，amount = " + amount);
            return false;
        }

        // 验证交易类型
        if (financeTransaction.getTransactionType() == null ||
            (!Integer.valueOf(1).equals(financeTransaction.getTransactionType()) &&
             !Integer.valueOf(2).equals(financeTransaction.getTransactionType()))) {
            System.out.println("验证失败：交易类型无效，transactionType = " + financeTransaction.getTransactionType());
            return false;
        }

        // 验证发票金额
        if (financeTransaction.getInvoiceAmount() != null &&
            financeTransaction.getInvoiceAmount().compareTo(BigDecimal.ZERO) < 0) {
            System.out.println("验证失败：发票金额无效，invoiceAmount = " + financeTransaction.getInvoiceAmount());
            return false;
        }

        System.out.println("验证成功：所有字段都通过验证");
        return true;
    }

    @Override
    public List<String> getProjectFinanceSummary(Long projectId) {
        List<String> summary = new ArrayList<>();
        
        // 查询项目收支记录
        List<FinanceTransaction> transactions = getByProjectId(projectId);
        
        if (transactions.isEmpty()) {
            summary.add("该项目暂无收支记录");
            return summary;
        }
        
        // 统计收支情况
        BigDecimal totalIncome = BigDecimal.ZERO;
        BigDecimal totalExpense = BigDecimal.ZERO;
        int incomeCount = 0;
        int expenseCount = 0;
        int pendingApprovalCount = 0;
        int largeAmountCount = 0;
        
        for (FinanceTransaction transaction : transactions) {
            if (transaction.isIncome()) {
                totalIncome = totalIncome.add(transaction.getAmount());
                incomeCount++;
            } else if (transaction.isExpense()) {
                totalExpense = totalExpense.add(transaction.getAmount());
                expenseCount++;
            }
            
            if (transaction.needsApproval()) {
                pendingApprovalCount++;
            }
            
            if (transaction.isLargeAmount()) {
                largeAmountCount++;
            }
        }
        
        // 生成汇总报告
        summary.add("=== 项目收支汇总报告 ===");
        summary.add("收支记录总数：" + transactions.size() + " 条");
        summary.add("收入记录：" + incomeCount + " 条，总金额：" + totalIncome + " 元");
        summary.add("支出记录：" + expenseCount + " 条，总金额：" + totalExpense + " 元");
        
        BigDecimal netIncome = totalIncome.subtract(totalExpense);
        summary.add("净收入：" + netIncome + " 元");
        
        if (netIncome.compareTo(BigDecimal.ZERO) > 0) {
            summary.add("项目盈利状况：盈利");
        } else if (netIncome.compareTo(BigDecimal.ZERO) < 0) {
            summary.add("项目盈利状况：亏损");
        } else {
            summary.add("项目盈利状况：收支平衡");
        }
        
        summary.add("待审批记录：" + pendingApprovalCount + " 条");
        summary.add("大额交易记录：" + largeAmountCount + " 条");
        
        return summary;
    }
}
