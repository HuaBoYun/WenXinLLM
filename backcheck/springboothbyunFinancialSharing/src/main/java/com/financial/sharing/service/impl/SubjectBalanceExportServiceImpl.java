package com.financial.sharing.service.impl;

import com.financial.sharing.oracle.mapper.AccountSubjectMapper;
import com.financial.sharing.service.SubjectBalanceExportService;
import com.financial.sharing.util.ExcelExportUtil;
import com.financial.sharing.vo.param.SubjectBalanceExportParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 科目余额导出服务实现
 *
 * @author system
 * @since 2024-12-19
 */
@Service
public class SubjectBalanceExportServiceImpl implements SubjectBalanceExportService {

    private static final Logger logger = LoggerFactory.getLogger(SubjectBalanceExportServiceImpl.class);

    @Autowired
    private AccountSubjectMapper accountSubjectMapper;

    // 存储异步导出任务状态
    private static final Map<String, ExportTask> exportTasks = new ConcurrentHashMap<>();

    @Override
    public void exportBalance(SubjectBalanceExportParam param, HttpServletResponse response) {
        try {
            // 查询数据
            List<Map<String, Object>> data = queryBalanceData(param);

            // 如果没有数据
            if (data == null || data.isEmpty()) {
                response.getWriter().write("没有找到符合条件的数据");
                return;
            }

            // 构建表头
            List<String> headers = buildHeaders(param);

            // 导出Excel
            ExcelExportUtil.exportDynamicHeaders(response, data, headers,
                    param.getExportFileName(), "科目余额表");

        } catch (Exception e) {
            logger.error("导出科目余额失败", e);
            throw new RuntimeException("导出失败: " + e.getMessage(), e);
        }
    }

    @Async
    @Override
    public String asyncExportBalance(SubjectBalanceExportParam param) {
        String taskId = UUID.randomUUID().toString();
        ExportTask task = new ExportTask();
        task.setTaskId(taskId);
        task.setTaskName("科目余额导出");
        task.setStatus("RUNNING");
        task.setCreateTime(new Date());
        task.setProgress(0);
        exportTasks.put(taskId, task);

        try {
            // 模拟异步处理
            task.setProgress(50);
            task.setMessage("正在处理数据...");

            // 实际导出逻辑（这里简化处理）
            List<Map<String, Object>> data = queryBalanceData(param);

            task.setProgress(100);
            task.setStatus("COMPLETED");
            task.setMessage("导出完成");
            task.setCompleteTime(new Date());

            logger.info("异步导出完成，任务ID: {}", taskId);

        } catch (Exception e) {
            task.setStatus("FAILED");
            task.setMessage("导出失败: " + e.getMessage());
            task.setCompleteTime(new Date());
            logger.error("异步导出失败，任务ID: {}", taskId, e);
        }
        return taskId;
    }

    @Override
    public Object getExportProgress(String taskId) {
        ExportTask task = exportTasks.get(taskId);
        if (task == null) {
            Map<String, Object> errorMap = new HashMap<>();
            errorMap.put("error", "任务不存在");
            return errorMap;
        }
        return task;
    }

    @Override
    public Object exportPreview(SubjectBalanceExportParam param) {
        try {
            // 限制预览数据量为前100条
            List<Map<String, Object>> data = queryBalanceData(param);

            if (data.size() > 100) {
                data = data.subList(0, 100);
            }

            // 构建表头
            List<String> headers = buildHeaders(param);

            Map<String, Object> result = new HashMap<>();
            result.put("headers", headers);
            result.put("data", data);
            result.put("total", data.size());
            return result;

        } catch (Exception e) {
            logger.error("导出预览失败", e);
            throw new RuntimeException("预览失败: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean cancelExport(String taskId) {
        ExportTask task = exportTasks.get(taskId);
        if (task != null && "RUNNING".equals(task.getStatus())) {
            task.setStatus("CANCELLED");
            task.setMessage("任务已取消");
            task.setCompleteTime(new Date());
            return true;
        }
        return false;
    }

    /**
     * 查询余额数据
     */
    private List<Map<String, Object>> queryBalanceData(SubjectBalanceExportParam param) {
        // 这里应该是实际的数据库查询逻辑
        // 由于没有具体的余额表，这里返回模拟数据
        List<Map<String, Object>> data = new ArrayList<>();

        // 模拟科目数据
        for (int i = 1; i <= 50; i++) {
            Map<String, Object> item = new HashMap<>();
            item.put("subjectCode", String.format("%04d", i));
            item.put("subjectName", "科目" + i);
            item.put("subjectLevel", 1);
            item.put("balanceDirection", i % 2 == 0 ? 2 : 1); // 1借方 2贷方
            item.put("openingBalance", new BigDecimal(Math.random() * 100000).setScale(2, BigDecimal.ROUND_HALF_UP));
            item.put("debitAmount", new BigDecimal(Math.random() * 50000).setScale(2, BigDecimal.ROUND_HALF_UP));
            item.put("creditAmount", new BigDecimal(Math.random() * 50000).setScale(2, BigDecimal.ROUND_HALF_UP));

            BigDecimal openingBalance = (BigDecimal) item.get("openingBalance");
            BigDecimal debitAmount = (BigDecimal) item.get("debitAmount");
            BigDecimal creditAmount = (BigDecimal) item.get("creditAmount");
            Integer direction = (Integer) item.get("balanceDirection");

            // 计算期末余额
            BigDecimal closingBalance;
            if (direction == 1) { // 借方
                closingBalance = openingBalance.add(debitAmount).subtract(creditAmount);
            } else { // 贷方
                closingBalance = openingBalance.add(creditAmount).subtract(debitAmount);
            }
            item.put("closingBalance", closingBalance.setScale(2, BigDecimal.ROUND_HALF_UP));

            data.add(item);
        }

        // 应用过滤条件
        return filterData(data, param);
    }

    /**
     * 过滤数据
     */
    private List<Map<String, Object>> filterData(List<Map<String, Object>> data, SubjectBalanceExportParam param) {
        return data.stream()
                .filter(item -> {
                    // 科目类型过滤
                    if (param.getSubjectType() != null) {
                        // 这里需要根据实际逻辑判断科目类型
                    }

                    // 余额方向过滤
                    if (param.getBalanceDirection() != null) {
                        if (!param.getBalanceDirection().equals(item.get("balanceDirection"))) {
                            return false;
                        }
                    }

                    // 零余额过滤
                    if (!param.getIncludeZeroBalance()) {
                        BigDecimal closingBalance = (BigDecimal) item.get("closingBalance");
                        if (closingBalance.compareTo(BigDecimal.ZERO) == 0) {
                            return false;
                        }
                    }
                    return true;
                })
                .sorted((a, b) -> {
                    // 排序
                    if ("subjectCode".equals(param.getSortBy())) {
                        int compare = ((String) a.get("subjectCode")).compareTo((String) b.get("subjectCode"));
                        return "desc".equals(param.getSortOrder()) ? -compare : compare;
                    }
                    return 0;
                })
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }

    /**
     * 构建表头
     */
    private List<String> buildHeaders(SubjectBalanceExportParam param) {
        List<String> headers = new ArrayList<>();

        if (param.getShowSubjectCode()) {
            headers.add("科目编码");
        }

        headers.add("科目名称");

        if (param.getShowSubjectFullPath()) {
            headers.add("科目全路径");
        }

        headers.add("余额方向");

        if (param.getIncludeOpeningBalance()) {
            headers.add("期初余额");
        }
        if (param.getIncludeTransactionAmount()) {
            headers.add("借方发生额");
            headers.add("贷方发生额");
        }
        if (param.getIncludeClosingBalance()) {
            headers.add("期末余额");
        }
        return headers;
    }

    /**
     * 导出任务内部类
     */
    public static class ExportTask {
        private String taskId;
        private String taskName;
        private String status; // RUNNING, COMPLETED, FAILED, CANCELLED
        private int progress; // 0-100
        private String message;
        private Date createTime;
        private Date completeTime;

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
    }
}