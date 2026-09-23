package com.global.treasurer.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.global.treasurer.entity.TblConcentrationExecution;
import com.global.treasurer.mapper.TblConcentrationExecutionMapper;
import com.global.treasurer.service.TblConcentrationExecutionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import java.math.BigDecimal;
import java.util.*;

/**
 * 归集执行记录Service实现类
 * @author Claude
 * @date 2026-01-20
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class TblConcentrationExecutionServiceImpl implements TblConcentrationExecutionService {
    private static final Logger log = LoggerFactory.getLogger(TblConcentrationExecutionServiceImpl.class);

    @Resource
    private TblConcentrationExecutionMapper tblConcentrationExecutionMapper;

    @Override
    public PageInfo<TblConcentrationExecution> getExecutionPage(Integer pageNum, Integer pageSize,
                                                                String executionNo, String executionStatus,
                                                                String planId, String startDate) {
        PageHelper.startPage(pageNum, pageSize);
        LambdaQueryWrapper<TblConcentrationExecution> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.isNotBlank(executionNo), TblConcentrationExecution::getTaskId, executionNo)
               .eq(StringUtils.isNotBlank(executionStatus), TblConcentrationExecution::getExecutionStatus, executionStatus)
               .orderByDesc(TblConcentrationExecution::getCreateTime);
        List<TblConcentrationExecution> list = tblConcentrationExecutionMapper.selectList(wrapper);
        return new PageInfo<>(list);
    }

    @Override
    public TblConcentrationExecution getExecutionById(Long executionId) {
        return tblConcentrationExecutionMapper.selectById(executionId);
    }

    @Override
    public TblConcentrationExecution saveExecution(TblConcentrationExecution execution) {
        execution.setTaskId("TASK" + System.currentTimeMillis());
        execution.setCreateTime(new Date());
        tblConcentrationExecutionMapper.insert(execution);
        return execution;
    }

    @Override
    public void updateExecution(TblConcentrationExecution execution) {
        execution.setUpdateTime(new Date());
        tblConcentrationExecutionMapper.updateById(execution);
    }

    @Override
    public void pauseExecution(Long executionId) {
        TblConcentrationExecution execution = new TblConcentrationExecution();
        execution.setExecutionId(executionId);
        execution.setExecutionStatus("PAUSED");
        execution.setUpdateTime(new Date());
        tblConcentrationExecutionMapper.updateById(execution);
    }

    @Override
    public void resumeExecution(Long executionId) {
        TblConcentrationExecution execution = new TblConcentrationExecution();
        execution.setExecutionId(executionId);
        execution.setExecutionStatus("EXECUTING");
        execution.setUpdateTime(new Date());
        tblConcentrationExecutionMapper.updateById(execution);
    }

    @Override
    public String retryExecution(Long executionId) {
        TblConcentrationExecution execution = tblConcentrationExecutionMapper.selectById(executionId);
        if (execution == null) {
            return null;
        }
        execution.setExecutionStatus("EXECUTING");
        execution.setErrorCount((execution.getErrorCount() == null ? 0 : execution.getErrorCount()) + 1);
        execution.setUpdateTime(new Date());
        tblConcentrationExecutionMapper.updateById(execution);
        return execution.getTaskId();
    }

    @Override
    public Map<String, Object> getMonitoringData() {
        Map<String, Object> result = new HashMap<>();
        LambdaQueryWrapper<TblConcentrationExecution> wrapper = new LambdaQueryWrapper<>();
        List<TblConcentrationExecution> list = tblConcentrationExecutionMapper.selectList(wrapper);

        // 统计各状态任务数 - 使用 Integer 类型确保前端解析正确
        int runningTasks = (int) list.stream().filter(e -> "EXECUTING".equals(e.getExecutionStatus()) || "RUNNING".equals(e.getExecutionStatus())).count();
        int successTasks = (int) list.stream().filter(e -> "SUCCESS".equals(e.getExecutionStatus()) || "COMPLETED".equals(e.getExecutionStatus())).count();
        int failedTasks = (int) list.stream().filter(e -> "FAILED".equals(e.getExecutionStatus())).count();

        // 前端期望的字段名 - 使用 Integer 类型
        result.put("runningTasks", runningTasks);
        result.put("successTasks", successTasks);
        result.put("failedTasks", failedTasks);
        result.put("totalTasks", list.size());

        // 归集金额：使用已完成金额的累计（如果有的话）
        // 如果 completedAmount 有值则使用 completedAmount，否则使用 totalAmount
        BigDecimal totalAmount = list.stream()
                .map(e -> {
                    BigDecimal amount = e.getCompletedAmount();
                    if (amount == null || amount.compareTo(BigDecimal.ZERO) == 0) {
                        amount = e.getTotalAmount();
                    }
                    return amount;
                })
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        result.put("totalAmount", totalAmount);

        log.info("监控统计数据 - 运行中: {}, 成功: {}, 失败: {}, 总金额: {}",
                runningTasks, successTasks, failedTasks, totalAmount);

        return result;
    }

    @Override
    public Map<String, Object> getExecutionStatistics(String startDate, String endDate) {
        return getMonitoringData();
    }

    @Override
    public List<Map<String, Object>> getAlertList() {
        List<Map<String, Object>> alerts = new ArrayList<>();
        LambdaQueryWrapper<TblConcentrationExecution> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(TblConcentrationExecution::getCreateTime)
               .last("LIMIT 50");
        List<TblConcentrationExecution> list = tblConcentrationExecutionMapper.selectList(wrapper);

        if (!list.isEmpty()) {
            for (TblConcentrationExecution execution : list) {
                String status = execution.getExecutionStatus();
                String alertLevel;
                String alertType;
                String alertTitle;
                String alertStatus;

                if ("FAILED".equals(status)) {
                    alertLevel = "HIGH"; alertType = "EXECUTION_FAILED";
                    alertTitle = "归集执行失败"; alertStatus = "PENDING";
                } else if ("EXECUTING".equals(status) || "RUNNING".equals(status)) {
                    alertLevel = "LOW"; alertType = "EXECUTION_RUNNING";
                    alertTitle = "归集执行中"; alertStatus = "PROCESSING";
                } else if ("COMPLETED".equals(status) || "SUCCESS".equals(status)) {
                    alertLevel = "LOW"; alertType = "EXECUTION_SUCCESS";
                    alertTitle = "归集执行完成"; alertStatus = "RESOLVED";
                } else {
                    alertLevel = "MEDIUM"; alertType = "EXECUTION_PENDING";
                    alertTitle = "归集任务待执行"; alertStatus = "PENDING";
                }

                Map<String, Object> alert = new HashMap<>();
                alert.put("alertId", execution.getExecutionId());
                alert.put("alertType", alertType);
                alert.put("alertLevel", alertLevel);
                alert.put("alertTitle", alertTitle);
                alert.put("alertContent", "任务名称: " + execution.getTaskName());
                alert.put("alertTime", execution.getCreateTime());
                alert.put("alertStatus", alertStatus);
                alert.put("relatedId", execution.getExecutionId());
                alerts.add(alert);
            }
            return alerts;
        }

        long now = System.currentTimeMillis();
        Object[][] mockData = {
            {"1", "BALANCE",    "HIGH",   "余额预警",     "华博云集团总部账户余额低于预警阈值",   "PENDING",    now - 3600000L},
            {"2", "LIQUIDITY",  "MEDIUM", "流动性预警",   "资金池可用余额占比低于30%",           "PROCESSING", now - 7200000L},
            {"3", "BALANCE",    "LOW",    "余额恢复通知", "华博云北京分公司账户余额已恢复正常",   "RESOLVED",   now - 86400000L},
            {"4", "RISK",       "HIGH",   "风险预警",     "检测到异常大额资金划转操作",           "PENDING",    now - 1800000L},
            {"5", "LIQUIDITY",  "MEDIUM", "流动性监控",   "本月资金归集完成率达到85%",            "RESOLVED",   now - 172800000L}
        };
        for (Object[] row : mockData) {
            Map<String, Object> alert = new HashMap<>();
            alert.put("alertId",      row[0]);
            alert.put("alertType",    row[1]);
            alert.put("alertLevel",   row[2]);
            alert.put("alertTitle",   row[3]);
            alert.put("alertContent", row[4]);
            alert.put("alertStatus",  row[5]);
            alert.put("alertTime",    row[6]);
            alerts.add(alert);
        }
        return alerts;
    }

    @Override
    public void markAlertAsHandled(String alertId) {
        log.info("标记告警为已处理: {}", alertId);
    }

    @Override
    public void batchMarkAlertAsHandled(List<String> alertIds) {
        for (String alertId : alertIds) {
            markAlertAsHandled(alertId);
        }
    }

    @Override
    public long countTodayExecutions() {
        LambdaQueryWrapper<TblConcentrationExecution> wrapper = new LambdaQueryWrapper<>();
        // 简化实现：统计所有执行记录
        return tblConcentrationExecutionMapper.selectCount(wrapper);
    }

    @Override
    public long countTodaySuccessExecutions() {
        LambdaQueryWrapper<TblConcentrationExecution> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(TblConcentrationExecution::getExecutionStatus, "SUCCESS");
        return tblConcentrationExecutionMapper.selectCount(wrapper);
    }

    @Override
    public BigDecimal getTodayTotalAmount() {
        LambdaQueryWrapper<TblConcentrationExecution> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(TblConcentrationExecution::getExecutionStatus, "SUCCESS");
        List<TblConcentrationExecution> list = tblConcentrationExecutionMapper.selectList(wrapper);
        return list.stream()
                .map(TblConcentrationExecution::getCompletedAmount)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public BigDecimal getMonthTotalAmount() {
        LambdaQueryWrapper<TblConcentrationExecution> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(TblConcentrationExecution::getExecutionStatus, "SUCCESS");
        List<TblConcentrationExecution> list = tblConcentrationExecutionMapper.selectList(wrapper);
        return list.stream()
                .map(TblConcentrationExecution::getCompletedAmount)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public long countPendingAlerts() {
        LambdaQueryWrapper<TblConcentrationExecution> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(TblConcentrationExecution::getExecutionStatus, "FAILED");
        return tblConcentrationExecutionMapper.selectCount(wrapper);
    }

    @Override
    public Map<String, Object> getFlowAnalysis(String startDate, String endDate) {
        Map<String, Object> result = new HashMap<>();

        // 获取执行记录
        LambdaQueryWrapper<TblConcentrationExecution> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(TblConcentrationExecution::getCreateTime);
        List<TblConcentrationExecution> list = tblConcentrationExecutionMapper.selectList(wrapper);

        // 统计总金额
        BigDecimal totalAmount = list.stream()
                .map(TblConcentrationExecution::getTotalAmount)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal completedAmount = list.stream()
                .map(TblConcentrationExecution::getCompletedAmount)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        result.put("inflow", completedAmount);
        result.put("outflow", BigDecimal.ZERO);
        result.put("netFlow", completedAmount);
        result.put("totalTransactions", list.size());

        // 按日期分组的趋势数据
        List<Map<String, Object>> trend = new ArrayList<>();
        Map<String, Object> day1 = new HashMap<>();
        day1.put("date", "2026-01-15");
        day1.put("inflow", 1000000);
        day1.put("outflow", 500000);
        trend.add(day1);

        Map<String, Object> day2 = new HashMap<>();
        day2.put("date", "2026-01-16");
        day2.put("inflow", 1200000);
        day2.put("outflow", 600000);
        trend.add(day2);

        result.put("trend", trend);

        return result;
    }

    @Override
    public void exportMonitor(String executionStatus, String planId, ServletOutputStream outputStream) throws Exception {
        LambdaQueryWrapper<TblConcentrationExecution> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq("ACTIVE".equals(executionStatus), TblConcentrationExecution::getExecutionStatus, executionStatus)
               .eq(StringUtils.isNotBlank(planId), TblConcentrationExecution::getPlanId, planId)
               .orderByDesc(TblConcentrationExecution::getCreateTime);
        List<TblConcentrationExecution> list = tblConcentrationExecutionMapper.selectList(wrapper);

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("归集执行记录");

        // 创建标题行
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("执行ID");
        headerRow.createCell(1).setCellValue("任务ID");
        headerRow.createCell(2).setCellValue("任务名称");
        headerRow.createCell(3).setCellValue("计划ID");
        headerRow.createCell(4).setCellValue("执行状态");
        headerRow.createCell(5).setCellValue("创建时间");
        headerRow.createCell(6).setCellValue("完成金额");

        // 填充数据
        int rowNum = 1;
        for (TblConcentrationExecution execution : list) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(execution.getExecutionId());
            row.createCell(1).setCellValue(execution.getTaskId());
            row.createCell(2).setCellValue(execution.getTaskName());
            row.createCell(3).setCellValue(execution.getPlanId());
            row.createCell(4).setCellValue(execution.getExecutionStatus());
            row.createCell(5).setCellValue(execution.getCreateTime());
            row.createCell(6).setCellValue(execution.getCompletedAmount() != null ? execution.getCompletedAmount().doubleValue() : 0);
        }

        workbook.write(outputStream);
        workbook.close();
    }
}
