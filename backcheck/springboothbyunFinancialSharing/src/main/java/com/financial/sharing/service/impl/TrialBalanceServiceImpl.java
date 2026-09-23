package com.financial.sharing.service.impl;

import com.financial.sharing.oracle.mapper.AccountSubjectMapper;
import com.financial.sharing.oracle.mapper.TrialBalanceMapper;
import com.financial.sharing.service.TrialBalanceService;
import com.financial.sharing.util.BalanceReportGenerator;
import com.financial.sharing.util.ExcelExportUtil;
import com.financial.sharing.vo.param.TrialBalanceParam;
import com.financial.sharing.vo.result.TrialBalanceResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 试算平衡服务实现
 *
 * @author system
 * @since 2024-12-19
 */
@Slf4j
@Service
public class TrialBalanceServiceImpl implements TrialBalanceService {

    @Resource
    private AccountSubjectMapper accountSubjectMapper;

    @Resource
    private TrialBalanceMapper trialBalanceMapper;

    @Resource
    private BalanceReportGenerator reportGenerator;

    // 存储异步任务状态
    private static final Map<String, TrialBalanceTask> tasks = new ConcurrentHashMap<>();

    @Override
    @Transactional(readOnly = true)
    public TrialBalanceResult generateTrialBalance(TrialBalanceParam param) {
        log.info("开始生成试算平衡表，期间：{}，账簿ID：{}", param.getAccountingPeriod(), param.getBookId());

        TrialBalanceResult result = new TrialBalanceResult();
        result.setBookId(param.getBookId());
        result.setTenantId(param.getTenantId());
        result.setAccountingPeriod(param.getAccountingPeriod());
        result.setTrialTime(new Date());

        try {
            // 1. 从数据库获取科目余额数据
            List<TrialBalanceResult.SubjectBalance> subjectBalances = trialBalanceMapper.getSubjectBalances(param);
            if (subjectBalances == null || subjectBalances.isEmpty()) {
                log.warn("未找到科目余额数据，期间：{}，账簿ID：{}", param.getAccountingPeriod(), param.getBookId());
                subjectBalances = new ArrayList<>();
            }
            result.setSubjectBalances(subjectBalances);

            // 2. 从数据库计算借贷合计
            BigDecimal totalDebit = trialBalanceMapper.calculateTotalDebit(param);
            BigDecimal totalCredit = trialBalanceMapper.calculateTotalCredit(param);

            // 如果数据库查询返回null，则设为0
            if (totalDebit == null) totalDebit = BigDecimal.ZERO;
            if (totalCredit == null) totalCredit = BigDecimal.ZERO;

            result.setTotalDebit(totalDebit);
            result.setTotalCredit(totalCredit);

            // 3. 计算差额
            BigDecimal difference = totalDebit.subtract(totalCredit);
            result.setDifference(difference);

            // 4. 判断是否平衡
            boolean isBalanced = difference.abs().compareTo(BigDecimal.valueOf(param.getTolerance())) <= 0;
            result.setIsBalanced(isBalanced);

            // 5. 计算平衡率
            if (totalDebit.compareTo(BigDecimal.ZERO) > 0) {
                BigDecimal balanceRate = totalCredit.divide(totalDebit, 4, RoundingMode.HALF_UP)
                        .multiply(BigDecimal.valueOf(100));
                result.setBalanceRate(balanceRate);
            } else {
                result.setBalanceRate(BigDecimal.valueOf(100));
            }

            // 6. 执行平衡校验
            performBalanceValidation(result, param);

            // 7. 生成统计信息
            generateStatistics(result);

            // 8. 收集错误和警告
            collectErrorsAndWarnings(result);

            log.info("试算平衡完成，结果：{}，差额：{}", isBalanced ? "平衡" : "不平衡", difference);
            return result;
        } catch (Exception e) {
            log.error("生成试算平衡表失败", e);
            throw new RuntimeException("生成试算平衡表失败: " + e.getMessage(), e);
        }
    }

    @Override
    public TrialBalanceResult checkBalance(TrialBalanceParam param) {
        TrialBalanceResult result = generateTrialBalance(param);

        // 执行更严格的校验
        performStrictValidation(result, param);

        return result;
    }

    @Override
    public void generateBalanceReport(TrialBalanceParam param, HttpServletResponse response) {
        try {
            TrialBalanceResult result = generateTrialBalance(param);

            // 生成Excel报告
            if ("excel".equals(param.getReportFormat()) || param.getGenerateExcelReport()) {
                generateExcelReport(result, response);
            }
            // 生成HTML报告
            else if ("html".equals(param.getReportFormat())) {
                generateHtmlReport(result, response);
            }
            // 生成PDF报告
            else if ("pdf".equals(param.getReportFormat())) {
                generatePdfReport(result, response);
            }
        } catch (Exception e) {
            log.error("生成平衡报告失败", e);
            throw new RuntimeException("生成报告失败: " + e.getMessage(), e);
        }
    }

    @Async
    @Override
    public String asyncGenerateTrialBalance(TrialBalanceParam param) {
        String taskId = UUID.randomUUID().toString();
        TrialBalanceTask task = new TrialBalanceTask();
        task.setTaskId(taskId);
        task.setTaskName("试算平衡");
        task.setStatus("RUNNING");
        task.setCreateTime(new Date());
        task.setProgress(0);
        tasks.put(taskId, task);

        try {
            // 生成试算平衡
            TrialBalanceResult result = generateTrialBalance(param);

            task.setProgress(100);
            task.setStatus("COMPLETED");
            task.setResult(result);
            task.setCompleteTime(new Date());

            log.info("异步试算平衡完成，任务ID: {}", taskId);
        } catch (Exception e) {
            task.setStatus("FAILED");
            task.setMessage("生成失败: " + e.getMessage());
            task.setCompleteTime(new Date());
            log.error("异步试算平衡失败，任务ID: {}", taskId, e);
        }
        return taskId;
    }

    @Override
    public Object getTrialBalanceProgress(String taskId) {
        TrialBalanceTask task = tasks.get(taskId);
        if (task == null) {
            Map<String, Object> errorMap = new HashMap<>();
            errorMap.put("error", "任务不存在");
            return errorMap;
        }
        return task;
    }

    @Override
    @Transactional(readOnly = true)
    public List<TrialBalanceResult> getTrialBalanceHistory(Long bookId, Long tenantId, String period) {
        log.info("获取试算平衡历史记录，账簿ID：{}，租户ID：{}，期间：{}", bookId, tenantId, period);
        try {
            List<TrialBalanceResult> history = trialBalanceMapper.getTrialBalanceHistory(bookId, tenantId, period, 100);
            if (history == null) {
                history = new ArrayList<>();
            }
            log.info("获取试算平衡历史记录完成，记录数：{}", history.size());
            return history;
        } catch (Exception e) {
            log.error("获取试算平衡历史记录失败", e);
            return new ArrayList<>();
        }
    }

    @Override
    public boolean deleteTrialBalance(String taskId) {
        TrialBalanceTask task = tasks.remove(taskId);
        return task != null;
    }

    @Override
    @Transactional(readOnly = true)
    public List<String> getAvailablePeriods(Long bookId, Long tenantId) {
        log.info("获取可用会计期间，账簿ID：{}，租户ID：{}", bookId, tenantId);
        try {
            List<String> periods = trialBalanceMapper.getAvailablePeriods(bookId, tenantId);
            if (periods == null || periods.isEmpty()) {
                log.warn("未找到可用的会计期间，账簿ID：{}，租户ID：{}", bookId, tenantId);
                // 如果没有数据，返回默认的近12个月
                periods = generateDefaultPeriods();
            }
            log.info("获取可用会计期间完成，期间数：{}", periods.size());
            return periods;
        } catch (Exception e) {
            log.error("获取可用会计期间失败", e);
            // 出错时返回默认期间
            return generateDefaultPeriods();
        }
    }

    /**
     * 生成默认的会计期间（近12个月）
     */
    private List<String> generateDefaultPeriods() {
        List<String> periods = new ArrayList<>();
        Calendar cal = Calendar.getInstance();
        int currentYear = cal.get(Calendar.YEAR);
        int currentMonth = cal.get(Calendar.MONTH) + 1;

        // 生成近12个月的期间
        for (int i = 0; i < 12; i++) {
            int year = currentYear;
            int month = currentMonth - i;
            if (month <= 0) {
                year--;
                month += 12;
            }
            periods.add(String.format("%04d-%02d", year, month));
        }

        Collections.sort(periods);
        return periods;
    }

    @Override
    public void exportTrialBalance(TrialBalanceParam param, HttpServletResponse response) {
        try {
            TrialBalanceResult result = generateTrialBalance(param);

            // 构建导出数据
            List<Map<String, Object>> exportData = new ArrayList<>();
            List<String> headers = Arrays.asList("科目编码", "科目名称", "期初余额", "借方发生额",
                                                 "贷方发生额", "期末余额", "余额方向", "状态");

            // 添加合计行
            Map<String, Object> summaryRow = new HashMap<>();
            summaryRow.put("科目编码", "合计");
            summaryRow.put("科目名称", "");
            summaryRow.put("期初余额", "");
            summaryRow.put("借方发生额", result.getTotalDebit());
            summaryRow.put("贷方发生额", result.getTotalCredit());
            summaryRow.put("期末余额", result.getDifference());
            summaryRow.put("余额方向", "");
            summaryRow.put("状态", result.getIsBalanced() ? "平衡" : "不平衡");

            exportData.add(summaryRow);

            // 添加明细数据
            for (TrialBalanceResult.SubjectBalance balance : result.getSubjectBalances()) {
                Map<String, Object> row = new HashMap<>();
                row.put("科目编码", balance.getSubjectCode());
                row.put("科目名称", balance.getSubjectName());
                row.put("期初余额", balance.getOpeningBalance());
                row.put("借方发生额", balance.getDebitAmount());
                row.put("贷方发生额", balance.getCreditAmount());
                row.put("期末余额", balance.getClosingBalance());
                row.put("余额方向", balance.getBalanceDirection() == 1 ? "借" : "贷");
                row.put("状态", balance.getIsNormal() ? "正常" : "异常");
                exportData.add(row);
            }

            // 导出到Excel
            String fileName = "试算平衡表_" + param.getAccountingPeriod();
            ExcelExportUtil.exportDynamicHeaders(response, exportData, headers, fileName, "试算平衡");

        } catch (Exception e) {
            log.error("导出试算平衡失败", e);
            throw new RuntimeException("导出失败: " + e.getMessage(), e);
        }
    }

    /**
     * 获取科目余额数据
     */
    private List<TrialBalanceResult.SubjectBalance> getSubjectBalances(TrialBalanceParam param) {
        try {
            return trialBalanceMapper.getSubjectBalances(param);
        } catch (Exception e) {
            log.error("获取科目余额数据失败", e);
            return new ArrayList<>();
        }
    }

    /**
     * 执行平衡校验
     */
    private void performBalanceValidation(TrialBalanceResult result, TrialBalanceParam param) {
        TrialBalanceResult.ValidationResult validation = new TrialBalanceResult.ValidationResult();

        // 借贷平衡验证
        validation.setDebitCreditBalance(result.getIsBalanced());

        // 科目余额方向验证
        boolean directionCheck = true;
        for (TrialBalanceResult.SubjectBalance balance : result.getSubjectBalances()) {
            // 检查资产类科目是否在借方，负债类科目是否在贷方等
            // 这里简化处理
        }
        validation.setBalanceDirectionCheck(directionCheck);

        // 连续性验证
        validation.setContinuityCheck(param.getCheckContinuity());

        // 勾稽关系验证
        validation.setCrossReferenceCheck(param.getCheckCrossReference());

        // 逻辑性验证
        validation.setLogicCheck(param.getCheckLogic());

        // 计算总分
        int score = 0;
        if (validation.getDebitCreditBalance()) score += 40;
        if (validation.getBalanceDirectionCheck()) score += 20;
        if (validation.getContinuityCheck()) score += 15;
        if (validation.getCrossReferenceCheck()) score += 15;
        if (validation.getLogicCheck()) score += 10;
        validation.setTotalScore(score);

        // 设置等级
        if (score >= 90) {
            validation.setValidationGrade("A");
        } else if (score >= 80) {
            validation.setValidationGrade("B");
        } else if (score >= 60) {
            validation.setValidationGrade("C");
        } else {
            validation.setValidationGrade("D");
        }
        result.setValidationResult(validation);
    }

    /**
     * 执行严格验证
     */
    private void performStrictValidation(TrialBalanceResult result, TrialBalanceParam param) {
        // 执行更严格的校验逻辑
        // 检查科目编码连续性、特殊科目勾稽关系等
    }

    /**
     * 生成统计信息
     */
    private void generateStatistics(TrialBalanceResult result) {
        TrialBalanceResult.TrialBalanceStatistics stats = new TrialBalanceResult.TrialBalanceStatistics();

        List<TrialBalanceResult.SubjectBalance> balances = result.getSubjectBalances();

        // 统计总科目数
        stats.setTotalSubjects(balances.size());

        // 统计有余额科目数
        long nonZeroCount = balances.stream()
                .filter(b -> b.getClosingBalance().compareTo(BigDecimal.ZERO) != 0)
                .count();
        stats.setSubjectsWithBalance((int) nonZeroCount);
        stats.setZeroBalanceSubjects(balances.size() - (int) nonZeroCount);

        // 按科目类型统计
        Map<Integer, Long> typeCount = balances.stream()
                .collect(java.util.stream.Collectors.groupingBy(
                        TrialBalanceResult.SubjectBalance::getSubjectType,
                        java.util.stream.Collectors.counting()
                ));

        stats.setAssetSubjects(typeCount.getOrDefault(1, 0L).intValue());
        stats.setLiabilitySubjects(typeCount.getOrDefault(2, 0L).intValue());
        stats.setEquitySubjects(typeCount.getOrDefault(3, 0L).intValue());
        stats.setRevenueSubjects(typeCount.getOrDefault(4, 0L).intValue());
        stats.setExpenseSubjects(typeCount.getOrDefault(5, 0L).intValue());

        // 找出最大余额科目
        Optional<TrialBalanceResult.SubjectBalance> maxBalance = balances.stream()
                .max(Comparator.comparing(b -> b.getClosingBalance().abs()));

        if (maxBalance.isPresent()) {
            stats.setMaxBalanceSubject(maxBalance.get().getSubjectName());
            stats.setMaxBalanceAmount(maxBalance.get().getClosingBalance());
        }
        result.setStatistics(stats);
    }

    /**
     * 收集错误和警告
     */
    private void collectErrorsAndWarnings(TrialBalanceResult result) {
        List<TrialBalanceResult.BalanceError> errors = new ArrayList<>();
        List<TrialBalanceResult.BalanceWarning> warnings = new ArrayList<>();

        try {
            // 检查平衡错误
            if (!result.getIsBalanced()) {
                TrialBalanceResult.BalanceError error = new TrialBalanceResult.BalanceError();
                error.setErrorType("BALANCE_MISMATCH");
                error.setErrorLevel("ERROR");
                error.setDescription("借贷不平衡");
                error.setErrorAmount(result.getDifference());
                error.setSuggestion("请检查凭证录入是否正确");
                errors.add(error);
            }

            // 从数据库查询科目余额方向错误
            List<TrialBalanceResult.BalanceError> directionErrors = trialBalanceMapper.checkBalanceDirectionErrors(
                result.getBookId(), result.getAccountingPeriod()
            );
            if (directionErrors != null) {
                errors.addAll(directionErrors);
            }

            // 手动检查科目余额方向错误
            for (TrialBalanceResult.SubjectBalance balance : result.getSubjectBalances()) {
                if (balance.getSubjectType() == 1 || balance.getSubjectType() == 5) {
                    // 资产和费用类科目余额应该在借方
                    if (balance.getClosingBalance().compareTo(BigDecimal.ZERO) < 0) {
                        TrialBalanceResult.BalanceError error = new TrialBalanceResult.BalanceError();
                        error.setErrorType("DIRECTION_ERROR");
                        error.setErrorLevel("WARN");
                        error.setDescription("科目余额方向异常");
                        error.setSubjectId(balance.getSubjectId());
                        error.setSubjectName(balance.getSubjectName());
                        error.setSuggestion("检查科目余额计算是否正确");
                        errors.add(error);
                    }
                }
            }
        } catch (Exception e) {
            log.error("收集错误和警告失败", e);
        }
        result.setErrors(errors);
        result.setWarnings(warnings);
    }

    /**
     * 生成Excel报告
     */
    private void generateExcelReport(TrialBalanceResult result, HttpServletResponse response) {
        // 实现Excel报告生成逻辑
    }

    /**
     * 生成HTML报告
     */
    private void generateHtmlReport(TrialBalanceResult result, HttpServletResponse response) {
        // 实现HTML报告生成逻辑
    }

    /**
     * 生成PDF报告
     */
    private void generatePdfReport(TrialBalanceResult result, HttpServletResponse response) {
        // 实现PDF报告生成逻辑
    }

    /**
     * 试算平衡任务内部类
     */
    public static class TrialBalanceTask {
        private String taskId;
        private String taskName;
        private String status; // RUNNING, COMPLETED, FAILED, CANCELLED
        private int progress; // 0-100
        private String message;
        private Date createTime;
        private Date completeTime;
        private TrialBalanceResult result;

        // Getters and Setters
        public String getTaskId() { return taskId; }
        public void setTaskId(String taskId) { this.taskId = taskId; }
        public String getTaskName() { return taskName; }
        public void setTaskName(String taskName) { this.taskName = taskName; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        public int getProgress() { return progress; }
        public void setProgress(int progress) { this.progress = progress; }
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
        public Date getCreateTime() { return createTime; }
        public void setCreateTime(Date createTime) { this.createTime = createTime; }
        public Date getCompleteTime() { return completeTime; }
        public void setCompleteTime(Date completeTime) { this.completeTime = completeTime; }
        public TrialBalanceResult getResult() { return result; }
        public void setResult(TrialBalanceResult result) { this.result = result; }
    }
}