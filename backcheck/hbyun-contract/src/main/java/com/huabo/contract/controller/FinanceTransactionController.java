package com.huabo.contract.controller;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.huabo.contract.entity.FinanceTransaction;
import com.huabo.contract.service.FinanceTransactionService;
import com.huabo.contract.vo.FinanceTransactionQueryParam;
import com.huabo.contract.vo.Result;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

/**
 * 收支管理控制器
 * 
 * @author 华博云开发团队
 * @since 2025-01-06
 */
@Tag(name="收支管理",description="收支管理")
@RestController
@RequestMapping("/finance/transaction")
@RequiredArgsConstructor
public class FinanceTransactionController {

    private final FinanceTransactionService financeTransactionService;

    @Operation(summary = "分页查询收支记录列表")
    @PostMapping("/page")
    public Result<IPage<FinanceTransaction>> getFinanceTransactionPage(@RequestBody FinanceTransactionQueryParam queryParam) {
        IPage<FinanceTransaction> page = financeTransactionService.getFinanceTransactionPage(queryParam);
        return Result.success(page);
    }

    @Operation(summary = "根据ID查询收支记录详情")
    @GetMapping("/{id}")
    public Result<FinanceTransaction> getFinanceTransactionById(@Parameter(description="收支记录ID") @PathVariable Long id) {
        FinanceTransaction financeTransaction = financeTransactionService.getById(id);
        return Result.success(financeTransaction);
    }

    @Operation(summary = "新增收支记录")
    @PostMapping("/create")
    public Result<Boolean> saveFinanceTransaction(@Valid @RequestBody Map<String, Object> requestData) {
        try {
            // 转换请求数据为实体对象
            FinanceTransaction financeTransaction = convertToFinanceTransaction(requestData);

            // 验证收支记录信息
            if (!financeTransactionService.validateTransactionInfo(financeTransaction)) {
                return Result.fail("收支记录信息验证失败");
            }

            // 生成交易编号
            if (financeTransaction.getTransactionNo() == null) {
                financeTransaction.setTransactionNo(financeTransactionService.generateTransactionNo());
            }

            boolean result = financeTransactionService.save(financeTransaction);
            return Result.success(result);
        } catch (Exception e) {
            return Result.fail("保存失败: " + e.getMessage());
        }
    }

    @Operation(summary = "修改收支记录")
    @PutMapping
    public Result<Boolean> updateFinanceTransaction(@Valid @RequestBody FinanceTransaction financeTransaction) {
        // 验证收支记录信息
        if (!financeTransactionService.validateTransactionInfo(financeTransaction)) {
            return Result.fail("收支记录信息验证失败");
        }
        
        boolean result = financeTransactionService.updateById(financeTransaction);
        return Result.success(result);
    }

    @Operation(summary = "删除收支记录")
    @DeleteMapping("/{id}")
    public Result<Boolean> deleteFinanceTransaction(@Parameter(description="收支记录ID") @PathVariable Long id) {
        boolean result = financeTransactionService.removeById(id);
        return Result.success(result);
    }

    @Operation(summary = "批量删除收支记录")
    @DeleteMapping("/batch")
    public Result<Boolean> batchDeleteFinanceTransaction(@RequestBody List<Long> ids) {
        boolean result = financeTransactionService.removeByIds(ids);
        return Result.success(result);
    }

    @Operation(summary = "根据项目ID查询收支记录列表")
    @GetMapping("/project/{projectId}")
    public Result<List<FinanceTransaction>> getByProjectId(@Parameter(description="项目ID") @PathVariable Long projectId) {
        List<FinanceTransaction> list = financeTransactionService.getByProjectId(projectId);
        return Result.success(list);
    }

    @Operation(summary = "根据交易类型查询收支记录列表")
    @GetMapping("/type/{transactionType}")
    public Result<List<FinanceTransaction>> getByTransactionType(@Parameter(description="交易类型") @PathVariable Integer transactionType) {
        List<FinanceTransaction> list = financeTransactionService.getByTransactionType(transactionType);
        return Result.success(list);
    }

    @Operation(summary = "根据交易分类查询收支记录列表")
    @GetMapping("/category/{transactionCategory}")
    public Result<List<FinanceTransaction>> getByTransactionCategory(@Parameter(description="交易分类") @PathVariable Integer transactionCategory) {
        List<FinanceTransaction> list = financeTransactionService.getByTransactionCategory(transactionCategory);
        return Result.success(list);
    }

    @Operation(summary = "根据审批状态查询收支记录列表")
    @GetMapping("/approval-status/{approvalStatus}")
    public Result<List<FinanceTransaction>> getByApprovalStatus(@Parameter(description="审批状态") @PathVariable Integer approvalStatus) {
        List<FinanceTransaction> list = financeTransactionService.getByApprovalStatus(approvalStatus);
        return Result.success(list);
    }

    @Operation(summary = "根据财务确认状态查询收支记录列表")
    @GetMapping("/finance-status/{financeStatus}")
    public Result<List<FinanceTransaction>> getByFinanceStatus(@Parameter(description="财务确认状态") @PathVariable Integer financeStatus) {
        List<FinanceTransaction> list = financeTransactionService.getByFinanceStatus(financeStatus);
        return Result.success(list);
    }

    @Operation(summary = "根据合同ID查询收支记录列表")
    @GetMapping("/contract/{contractId}")
    public Result<List<FinanceTransaction>> getByContractId(@Parameter(description="合同ID") @PathVariable Long contractId) {
        List<FinanceTransaction> list = financeTransactionService.getByContractId(contractId);
        return Result.success(list);
    }

    @Operation(summary = "根据预算ID查询收支记录列表")
    @GetMapping("/budget/{budgetId}")
    public Result<List<FinanceTransaction>> getByBudgetId(@Parameter(description="预算ID") @PathVariable Long budgetId) {
        List<FinanceTransaction> list = financeTransactionService.getByBudgetId(budgetId);
        return Result.success(list);
    }

    @Operation(summary = "根据项目ID统计收入总额")
    @GetMapping("/sum-income/project/{projectId}")
    public Result<BigDecimal> sumIncomeByProject(@Parameter(description="项目ID") @PathVariable Long projectId) {
        BigDecimal income = financeTransactionService.sumIncomeByProject(projectId);
        return Result.success(income);
    }

    @Operation(summary = "根据项目ID统计支出总额")
    @GetMapping("/sum-expense/project/{projectId}")
    public Result<BigDecimal> sumExpenseByProject(@Parameter(description="项目ID") @PathVariable Long projectId) {
        BigDecimal expense = financeTransactionService.sumExpenseByProject(projectId);
        return Result.success(expense);
    }

    @Operation(summary = "根据交易类型统计金额")
    @GetMapping("/sum-amount/type/{transactionType}")
    public Result<BigDecimal> sumAmountByTransactionType(@Parameter(description="交易类型") @PathVariable Integer transactionType) {
        BigDecimal amount = financeTransactionService.sumAmountByTransactionType(transactionType);
        return Result.success(amount);
    }

    @Operation(summary = "查询待审批的收支记录列表")
    @GetMapping("/pending-approval")
    public Result<List<FinanceTransaction>> getPendingApproval() {
        List<FinanceTransaction> list = financeTransactionService.getPendingApproval();
        return Result.success(list);
    }

    @Operation(summary = "查询待财务确认的收支记录列表")
    @GetMapping("/pending-finance-confirm")
    public Result<List<FinanceTransaction>> getPendingFinanceConfirm() {
        List<FinanceTransaction> list = financeTransactionService.getPendingFinanceConfirm();
        return Result.success(list);
    }

    @Operation(summary = "查询大额交易记录列表")
    @GetMapping("/large-amount")
    public Result<List<FinanceTransaction>> getLargeAmountTransactions() {
        List<FinanceTransaction> list = financeTransactionService.getLargeAmountTransactions();
        return Result.success(list);
    }

    @Operation(summary = "查询逾期未审批的收支记录列表")
    @GetMapping("/overdue-approval")
    public Result<List<FinanceTransaction>> getOverdueApproval(@Parameter(description="逾期天数") @RequestParam(defaultValue = "7") Integer days) {
        List<FinanceTransaction> list = financeTransactionService.getOverdueApproval(days);
        return Result.success(list);
    }

    @Operation(summary = "批量更新审批状态")
    @PutMapping("/batch-approval-status")
    public Result<Integer> batchUpdateApprovalStatus(@RequestBody List<Long> ids, 
                                                   @Parameter(description="新状态") @RequestParam Integer approvalStatus,
                                                   @Parameter(description="审批人ID") @RequestParam Long approverId,
                                                   @Parameter(description="审批意见") @RequestParam String approvalComments) {
        Integer count = financeTransactionService.batchUpdateApprovalStatus(ids, approvalStatus, approverId, approvalComments);
        return Result.success(count);
    }

    @Operation(summary = "批量更新财务确认状态")
    @PutMapping("/batch-finance-status")
    public Result<Integer> batchUpdateFinanceStatus(@RequestBody List<Long> ids, 
                                                  @Parameter(description="新状态") @RequestParam Integer financeStatus,
                                                  @Parameter(description="财务确认人ID") @RequestParam Long financeConfirmerId,
                                                  @Parameter(description="财务确认意见") @RequestParam String financeComments) {
        Integer count = financeTransactionService.batchUpdateFinanceStatus(ids, financeStatus, financeConfirmerId, financeComments);
        return Result.success(count);
    }

    @Operation(summary = "收支审批")
    @PostMapping("/approve/{id}")
    public Result<Boolean> approveTransaction(@Parameter(description="收支记录ID") @PathVariable Long id,
                                            @Parameter(description="审批状态") @RequestParam Integer approvalStatus,
                                            @Parameter(description="审批人ID") @RequestParam Long approverId,
                                            @Parameter(description="审批意见") @RequestParam String approvalComments) {
        Boolean result = financeTransactionService.approveTransaction(id, approvalStatus, approverId, approvalComments);
        return Result.success(result);
    }

    @Operation(summary = "财务确认")
    @PostMapping("/confirm-finance/{id}")
    public Result<Boolean> confirmFinance(@Parameter(description="收支记录ID") @PathVariable Long id,
                                        @Parameter(description="财务确认状态") @RequestParam Integer financeStatus,
                                        @Parameter(description="财务确认人ID") @RequestParam Long financeConfirmerId,
                                        @Parameter(description="财务确认意见") @RequestParam String financeComments) {
        Boolean result = financeTransactionService.confirmFinance(id, financeStatus, financeConfirmerId, financeComments);
        return Result.success(result);
    }

    @Operation(summary = "根据关键词搜索收支记录")
    @GetMapping("/search")
    public Result<List<FinanceTransaction>> searchByKeyword(@Parameter(description="关键词") @RequestParam String keyword,
                                                          @Parameter(description="限制数量") @RequestParam(defaultValue = "10") Integer limit) {
        List<FinanceTransaction> list = financeTransactionService.searchByKeyword(keyword, limit);
        return Result.success(list);
    }

    @Operation(summary = "查询收支统计信息")
    @GetMapping("/statistics")
    public Result<List<FinanceTransaction>> getFinanceStatistics() {
        List<FinanceTransaction> statistics = financeTransactionService.getFinanceStatistics();
        return Result.success(statistics);
    }

    @Operation(summary = "获取项目收支汇总")
    @GetMapping("/project-summary/{projectId}")
    public Result<List<String>> getProjectFinanceSummary(@Parameter(description="项目ID") @PathVariable Long projectId) {
        List<String> summary = financeTransactionService.getProjectFinanceSummary(projectId);
        return Result.success(summary);
    }

    @Operation(summary = "生成交易编号")
    @GetMapping("/generate-transaction-no")
    public Result<String> generateTransactionNo() {
        String transactionNo = financeTransactionService.generateTransactionNo();
        return Result.success(transactionNo);
    }

    /**
     * 将前端传来的Map数据转换为FinanceTransaction实体
     */
    private FinanceTransaction convertToFinanceTransaction(Map<String, Object> requestData) throws ParseException {
        System.out.println("开始转换请求数据：" + requestData);
        FinanceTransaction transaction = new FinanceTransaction();

        // 基本字段转换
        if (requestData.get("projectId") != null) {
            String projectId = requestData.get("projectId").toString();
            transaction.setProjectId(projectId);
            System.out.println("设置 projectId: " + projectId);
        }

        // 交易类型转换：支持数字和字符串两种格式
        if (requestData.get("transactionType") != null) {
            Object typeObj = requestData.get("transactionType");
            System.out.println("原始 transactionType: " + typeObj + ", 类型: " + typeObj.getClass().getName());
            if (typeObj instanceof Integer) {
                Integer transactionType = (Integer) typeObj;
                transaction.setTransactionType(transactionType);
                System.out.println("设置 transactionType (Integer): " + transactionType);
            } else {
                String typeStr = typeObj.toString();
                if ("收入".equals(typeStr) || "1".equals(typeStr)) {
                    transaction.setTransactionType(1);
                    System.out.println("设置 transactionType (String->Integer): 1");
                } else if ("支出".equals(typeStr) || "2".equals(typeStr)) {
                    transaction.setTransactionType(2);
                    System.out.println("设置 transactionType (String->Integer): 2");
                }
            }
        }

        // 交易分类转换：支持数字和字符串两种格式
        if (requestData.get("transactionCategory") != null) {
            Object categoryObj = requestData.get("transactionCategory");
            if (categoryObj instanceof Integer) {
                transaction.setTransactionCategory((Integer) categoryObj);
            } else {
                String categoryStr = categoryObj.toString();
                switch (categoryStr) {
                    case "项目收入":
                    case "合同收款":
                    case "1":
                        transaction.setTransactionCategory(1);
                        break;
                    case "材料采购":
                    case "2":
                        transaction.setTransactionCategory(2);
                        break;
                    case "人工费用":
                    case "3":
                        transaction.setTransactionCategory(3);
                        break;
                    case "设备租赁":
                    case "4":
                        transaction.setTransactionCategory(4);
                        break;
                    case "其他费用":
                    case "5":
                        transaction.setTransactionCategory(5);
                        break;
                }
            }
        }

        // 金额转换
        if (requestData.get("amount") != null) {
            Object amountObj = requestData.get("amount");
            System.out.println("原始 amount: " + amountObj + ", 类型: " + amountObj.getClass().getName());
            BigDecimal amount = new BigDecimal(amountObj.toString());
            transaction.setAmount(amount);
            System.out.println("设置 amount: " + amount);
        }

        // 日期转换
        if (requestData.get("transactionDate") != null) {
            String dateStr = requestData.get("transactionDate").toString();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            transaction.setTransactionDate(sdf.parse(dateStr));
        }

        // 其他字符串字段
        if (requestData.get("accountSubject") != null) {
            transaction.setAccountSubject(requestData.get("accountSubject").toString());
        }
        if (requestData.get("bankName") != null) {
            transaction.setBankName(requestData.get("bankName").toString());
        }
        if (requestData.get("description") != null) {
            transaction.setDescription(requestData.get("description").toString());
        }
        if (requestData.get("remarks") != null) {
            transaction.setRemarks(requestData.get("remarks").toString());
        }
        if (requestData.get("accountNumber") != null) {
            transaction.setAccountNumber(requestData.get("accountNumber").toString());
        }
        if (requestData.get("counterparty") != null) {
            transaction.setCounterparty(requestData.get("counterparty").toString());
        }
        if (requestData.get("contractId") != null) {
            transaction.setContractId(requestData.get("contractId").toString());
        }

        return transaction;
    }
}
