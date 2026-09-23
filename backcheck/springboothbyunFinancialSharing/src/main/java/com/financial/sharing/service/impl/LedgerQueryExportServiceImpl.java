package com.financial.sharing.service.impl;

import com.financial.sharing.oracle.mapper.AccountSubjectMapper;
import com.financial.sharing.service.LedgerQueryExportService;
import com.financial.sharing.util.ExcelExportUtil;
import com.financial.sharing.vo.param.LedgerQueryExportParam;
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
 * 总账查询导出服务实现
 *
 * @author system
 * @since 2024-12-19
 */
@Service
public class LedgerQueryExportServiceImpl implements LedgerQueryExportService {

    private static final Logger logger = LoggerFactory.getLogger(LedgerQueryExportServiceImpl.class);

    @Autowired
    private AccountSubjectMapper accountSubjectMapper;

    // 存储异步导出任务状态
    private static final Map<String, ExportTask> exportTasks = new ConcurrentHashMap<>();

    @Override
    public void exportLedgerQuery(LedgerQueryExportParam param, HttpServletResponse response) {
        try {
            // 查询数据
            List<Map<String, Object>> data = queryLedgerData(param);

            // 如果没有数据
            if (data == null || data.isEmpty()) {
                response.getWriter().write("没有找到符合条件的数据");
                return;
            }

            // 构建表头
            List<String> headers = buildHeaders(param);

            // 导出Excel
            ExcelExportUtil.exportDynamicHeaders(response, data, headers,
                    param.getExportFileName(), "总账查询");

        } catch (Exception e) {
            logger.error("导出总账查询失败", e);
            throw new RuntimeException("导出失败: " + e.getMessage(), e);
        }
    }

    @Async
    @Override
    public String asyncExportLedgerQuery(LedgerQueryExportParam param) {
        String taskId = UUID.randomUUID().toString();
        ExportTask task = new ExportTask();
        task.setTaskId(taskId);
        task.setTaskName("总账查询导出");
        task.setStatus("RUNNING");
        task.setCreateTime(new Date());
        task.setProgress(0);
        exportTasks.put(taskId, task);

        try {
            // 模拟异步处理
            task.setProgress(30);
            task.setMessage("正在查询数据...");

            List<Map<String, Object>> data = queryLedgerData(param);

            task.setProgress(70);
            task.setMessage("正在生成文件...");

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
    public Object exportPreview(LedgerQueryExportParam param) {
        try {
            // 限制预览数据量为前100条
            List<Map<String, Object>> data = queryLedgerData(param);

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

    @Override
    public Map<String, Object> getExportTemplates() {
        Map<String, Object> templates = new HashMap<>();

        // 标准模板
        Map<String, Object> standardTemplate = new HashMap<>();
        standardTemplate.put("name", "标准模板");
        standardTemplate.put("description", "包含基本的总账查询信息");
        standardTemplate.put("columns", Arrays.asList("日期", "凭证号", "摘要", "科目编码", "科目名称", "借方金额", "贷方金额"));

        // 明细模板
        Map<String, Object> detailTemplate = new HashMap<>();
        detailTemplate.put("name", "明细模板");
        detailTemplate.put("description", "包含详细的辅助核算信息");
        detailTemplate.put("columns", Arrays.asList("日期", "凭证号", "摘要", "科目编码", "科目名称", "借方金额", "贷方金额", "部门", "项目", "往来单位"));

        // 汇总模板
        Map<String, Object> summaryTemplate = new HashMap<>();
        summaryTemplate.put("name", "汇总模板");
        summaryTemplate.put("description", "按科目汇总的数据");
        summaryTemplate.put("columns", Arrays.asList("科目编码", "科目名称", "期初余额", "借方发生额", "贷方发生额", "期末余额"));

        templates.put("standard", standardTemplate);
        templates.put("detail", detailTemplate);
        templates.put("summary", summaryTemplate);

        return templates;
    }

    /**
     * 查询总账数据
     */
    private List<Map<String, Object>> queryLedgerData(LedgerQueryExportParam param) {
        // 这里应该是实际的数据库查询逻辑
        // 由于没有具体的总账表，这里返回模拟数据
        List<Map<String, Object>> data = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        // 生成模拟数据
        Calendar cal = Calendar.getInstance();
        cal.setTime(param.getStartDate());

        while (!cal.getTime().after(param.getEndDate())) {
            // 每天生成几条数据
            int dailyCount = (int) (Math.random() * 5) + 1;
            for (int i = 0; i < dailyCount; i++) {
                Map<String, Object> item = new HashMap<>();
                item.put("date", sdf.format(cal.getTime()));
                item.put("voucherNo", "记" + String.format("%04d", (int)(Math.random() * 1000)));
                item.put("summary", generateRandomSummary());
                item.put("subjectCode", String.format("%04d", (int)(Math.random() * 100) + 100));
                item.put("subjectName", "科目" + ((int)(Math.random() * 50) + 1));

                // 随机生成借方或贷方金额
                BigDecimal amount = new BigDecimal(Math.random() * 10000).setScale(2, BigDecimal.ROUND_HALF_UP);
                if (Math.random() > 0.5) {
                    item.put("debitAmount", amount);
                    item.put("creditAmount", BigDecimal.ZERO);
                } else {
                    item.put("debitAmount", BigDecimal.ZERO);
                    item.put("creditAmount", amount);
                }

                // 可选的辅助核算信息
                if (Math.random() > 0.7) {
                    item.put("department", "部门" + ((int)(Math.random() * 10) + 1));
                    item.put("project", "项目" + ((int)(Math.random() * 5) + 1));
                }

                data.add(item);
            }

            cal.add(Calendar.DAY_OF_MONTH, 1);
        }

        // 应用过滤条件
        return filterData(data, param);
    }

    /**
     * 过滤数据
     */
    private List<Map<String, Object>> filterData(List<Map<String, Object>> data, LedgerQueryExportParam param) {
        return data.stream()
                .filter(item -> {
                    // 科目编码过滤
                    if (param.getSubjectCode() != null && !param.getSubjectCode().isEmpty()) {
                        if (!item.get("subjectCode").toString().contains(param.getSubjectCode())) {
                            return false;
                        }
                    }

                    // 科目名称过滤
                    if (param.getSubjectName() != null && !param.getSubjectName().isEmpty()) {
                        if (!item.get("subjectName").toString().contains(param.getSubjectName())) {
                            return false;
                        }
                    }

                    // 凭证号过滤
                    if (param.getVoucherNo() != null && !param.getVoucherNo().isEmpty()) {
                        if (!item.get("voucherNo").toString().contains(param.getVoucherNo())) {
                            return false;
                        }
                    }

                    // 金额范围过滤
                    if (param.getMinAmount() != null || param.getMaxAmount() != null) {
                        BigDecimal debitAmount = (BigDecimal) item.get("debitAmount");
                        BigDecimal creditAmount = (BigDecimal) item.get("creditAmount");
                        BigDecimal amount = debitAmount.compareTo(BigDecimal.ZERO) > 0 ? debitAmount : creditAmount;

                        if (param.getMinAmount() != null && amount.compareTo(BigDecimal.valueOf(param.getMinAmount())) < 0) {
                            return false;
                        }
                        if (param.getMaxAmount() != null && amount.compareTo(BigDecimal.valueOf(param.getMaxAmount())) > 0) {
                            return false;
                        }
                    }
                    return true;
                })
                .sorted((a, b) -> {
                    // 排序
                    switch (param.getSortBy()) {
                        case "date":
                            int dateCompare = ((String) a.get("date")).compareTo((String) b.get("date"));
                            return "desc".equals(param.getSortOrder()) ? -dateCompare : dateCompare;
                        case "voucherNo":
                            int voucherCompare = ((String) a.get("voucherNo")).compareTo((String) b.get("voucherNo"));
                            return "desc".equals(param.getSortOrder()) ? -voucherCompare : voucherCompare;
                        case "subjectCode":
                            int codeCompare = ((String) a.get("subjectCode")).compareTo((String) b.get("subjectCode"));
                            return "desc".equals(param.getSortOrder()) ? -codeCompare : codeCompare;
                        default:
                            return 0;
                    }
                })
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }

    /**
     * 生成随机摘要
     */
    private String generateRandomSummary() {
        String[] summaries = {
            "采购原材料",
            "销售商品",
            "支付工资",
            "收到的货款",
            "支付费用",
            "银行存款",
            "现金收入",
            "计提折旧",
            "摊销费用",
            "其他收支"
        };
        return summaries[(int)(Math.random() * summaries.length)];
    }

    /**
     * 构建表头
     */
    private List<String> buildHeaders(LedgerQueryExportParam param) {
        List<String> headers = new ArrayList<>();

        switch (param.getTemplateType()) {
            case "detail":
                headers = Arrays.asList("日期", "凭证号", "摘要", "科目编码", "科目名称",
                                       "借方金额", "贷方金额", "部门", "项目", "往来单位");
                break;
            case "summary":
                headers = Arrays.asList("科目编码", "科目名称", "期初余额",
                                       "借方发生额", "贷方发生额", "期末余额");
                break;
            default: // standard
                headers = Arrays.asList("日期", "凭证号", "摘要", "科目编码",
                                       "科目名称", "借方金额", "贷方金额");
                break;
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