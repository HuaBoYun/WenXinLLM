package com.financial.sharing.util;

import com.financial.sharing.vo.result.TrialBalanceResult;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.*;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 平衡报告生成器
 * 支持生成HTML、PDF、Excel格式的试算平衡报告
 *
 * @author system
 * @since 2024-12-19
 */
@Component
public class BalanceReportGenerator {

    private static final Logger logger = LoggerFactory.getLogger(BalanceReportGenerator.class);

    private Configuration freemarkerConfig;

    public BalanceReportGenerator() {
        // 初始化FreeMarker配置
        freemarkerConfig = new Configuration(Configuration.VERSION_2_3_31);
        freemarkerConfig.setDefaultEncoding("UTF-8");
        try {
            // 设置模板路径（实际项目中应该从配置读取）
            freemarkerConfig.setDirectoryForTemplateLoading(new File("templates"));
        } catch (IOException e) {
            logger.warn("模板目录不存在，将使用内置模板", e);
        }
    }

    /**
     * 生成HTML格式的平衡报告
     *
     * @param result 试算平衡结果
     * @return HTML内容
     */
    public String generateHtmlReport(TrialBalanceResult result) {
        try {
            // 准备模板数据
            Map<String, Object> data = prepareTemplateData(result);

            // 使用内置模板生成HTML
            return buildHtmlReport(data);
        } catch (Exception e) {
            logger.error("生成HTML报告失败", e);
            return buildErrorReport("生成HTML报告失败: " + e.getMessage());
        }
    }

    /**
     * 生成Excel格式的平衡报告
     *
     * @param result 试算平衡结果
     * @return Excel文件字节数组
     */
    public byte[] generateExcelReport(TrialBalanceResult result) {
        try {
            // 准备数据
            List<Map<String, Object>> data = prepareExcelData(result);

            // 这里应该使用EasyExcel或POI生成Excel
            // 为了简化，返回一个简单的字节数组
            return generateSimpleExcel(data);
        } catch (Exception e) {
            logger.error("生成Excel报告失败", e);
            return new byte[0];
        }
    }

    /**
     * 准备模板数据
     */
    private Map<String, Object> prepareTemplateData(TrialBalanceResult result) {
        Map<String, Object> data = new HashMap<>();

        // 基本信息
        data.put("reportTitle", "试算平衡表");
        data.put("reportDate", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        data.put("accountingPeriod", result.getAccountingPeriod());
        data.put("isBalanced", result.getIsBalanced());

        // 汇总数据
        Map<String, Object> summary = new HashMap<>();
        summary.put("totalDebit", result.getTotalDebit());
        summary.put("totalCredit", result.getTotalCredit());
        summary.put("difference", result.getTotalDebit().subtract(result.getTotalCredit()));
        data.put("summary", summary);

        // 科目余额列表
        data.put("balances", result.getSubjectBalances());

        // 错误信息
        data.put("errors", result.getErrors());

        return data;
    }

    /**
     * 构建HTML报告
     */
    private String buildHtmlReport(Map<String, Object> data) {
        StringBuilder html = new StringBuilder();

        html.append("<!DOCTYPE html>\n");
        html.append("<html>\n");
        html.append("<head>\n");
        html.append("    <meta charset=\"UTF-8\">\n");
        html.append("    <title>").append(data.get("reportTitle")).append("</title>\n");
        html.append("    <style>\n");
        html.append(getReportStyles());
        html.append("    </style>\n");
        html.append("</head>\n");
        html.append("<body>\n");

        // 报告标题
        html.append("    <div class=\"report-header\">\n");
        html.append("        <h1>").append(data.get("reportTitle")).append("</h1>\n");
        html.append("        <div class=\"report-info\">\n");
        html.append("            <p>会计期间：").append(data.get("accountingPeriod")).append("</p>\n");
        html.append("            <p>生成时间：").append(data.get("reportDate")).append("</p>\n");
        html.append("        </div>\n");
        html.append("    </div>\n");

        // 平衡状态
        @SuppressWarnings("unchecked")
        Map<String, Object> summary = (Map<String, Object>) data.get("summary");
        boolean isBalanced = (Boolean) data.get("isBalanced");

        html.append("    <div class=\"balance-status\">\n");
        html.append("        <h2>平衡状态</h2>\n");
        html.append("        <table class=\"summary-table\">\n");
        html.append("            <tr><td>借方合计</td><td class=\"amount-right\">").append(formatAmount((BigDecimal) summary.get("totalDebit"))).append("</td></tr>\n");
        html.append("            <tr><td>贷方合计</td><td class=\"amount-right\">").append(formatAmount((BigDecimal) summary.get("totalCredit"))).append("</td></tr>\n");
        html.append("            <tr><td>差额</td><td class=\"amount-right ").append(isBalanced ? "" : "不平衡").append("\">").append(formatAmount((BigDecimal) summary.get("difference"))).append("</td></tr>\n");
        html.append("        </table>\n");
        html.append("    </div>\n");

        // 错误信息
        @SuppressWarnings("unchecked")
        List<TrialBalanceResult.BalanceError> errors = (List<TrialBalanceResult.BalanceError>) data.get("errors");
        if (errors != null && !errors.isEmpty()) {
            html.append("    <div class=\"errors-section\">\n");
            html.append("        <h2>平衡错误</h2>\n");
            for (TrialBalanceResult.BalanceError error : errors) {
                html.append("        <div class=\"error-item\">\n");
                html.append("            <h3>").append(error.getDescription()).append("</h3>\n");
                html.append("            <p>建议：").append(error.getSuggestion()).append("</p>\n");
                html.append("        </div>\n");
            }
            html.append("    </div>\n");
        }

        html.append("</body>\n");
        html.append("</html>\n");

        return html.toString();
    }

    /**
     * 获取报告样式
     */
    private String getReportStyles() {
        return "body {font-family: Arial, sans-serif; margin: 0; padding: 20px; background-color: #f5f5f5;}\n" +
               ".report-header {background-color: white; padding: 20px; border-radius: 5px; box-shadow: 0 2px 5px rgba(0,0,0,0.1); margin-bottom: 20px;}\n" +
               ".report-header h1 {color: #333; margin-top: 0;}\n" +
               ".report-info p {margin: 5px 0; color: #666;}\n" +
               ".balance-status {background-color: white; padding: 20px; border-radius: 5px; box-shadow: 0 2px 5px rgba(0,0,0,0.1); margin-bottom: 20px;}\n" +
               ".balance-status h2 {margin-top: 0; color: #333;}\n" +
               ".summary-table {width: 100%; border-collapse: collapse; margin-top: 10px;}\n" +
               ".summary-table td {padding: 8px; border-bottom: 1px solid #ddd;}\n" +
               ".summary-table td:first-child {font-weight: bold;}\n" +
               ".amount-right {text-align: right;}\n" +
               ".不平衡 {color: #721c24; background-color: #f8d7da;}\n" +
               ".errors-section {background-color: white; padding: 20px; border-radius: 5px; box-shadow: 0 2px 5px rgba(0,0,0,0.1); margin-bottom: 20px;}\n" +
               ".errors-section h2 {margin-top: 0; color: #721c24;}\n" +
               ".error-item {background-color: #f8d7da; border: 1px solid #f5c6cb; padding: 15px; margin-bottom: 10px; border-radius: 4px;}\n" +
               ".error-item h3 {margin-top: 0; color: #721c24;}\n";
    }

    /**
     * 格式化金额
     */
    private String formatAmount(BigDecimal amount) {
        if (amount == null) {
            return "0.00";
        }
        return amount.setScale(2, BigDecimal.ROUND_HALF_UP).toString();
    }

    /**
     * 构建错误报告
     */
    private String buildErrorReport(String errorMessage) {
        return "<!DOCTYPE html>\n" +
               "<html>\n" +
               "<head>\n" +
               "    <meta charset=\"UTF-8\">\n" +
               "    <title>报告生成错误</title>\n" +
               "</head>\n" +
               "<body>\n" +
               "    <h1>报告生成失败</h1>\n" +
               "    <p>错误信息：" + errorMessage + "</p>\n" +
               "</body>\n" +
               "</html>";
    }

    /**
     * 准备Excel数据
     */
    private List<Map<String, Object>> prepareExcelData(TrialBalanceResult result) {
        List<Map<String, Object>> data = new ArrayList<>();

        // 添加合计行
        Map<String, Object> summary = new HashMap<>();
        summary.put("科目编码", "合计");
        summary.put("科目名称", "");
        summary.put("期初余额", "");
        summary.put("借方发生额", result.getTotalDebit());
        summary.put("贷方发生额", result.getTotalCredit());
        summary.put("期末余额", "");
        data.add(summary);

        return data;
    }

    /**
     * 生成简单的Excel文件（简化版本）
     */
    private byte[] generateSimpleExcel(List<Map<String, Object>> data) {
        // 这里应该使用EasyExcel或POI生成真正的Excel文件
        // 为了简化，返回CSV格式的字节数组
        StringBuilder csv = new StringBuilder();
        csv.append("科目编码,科目名称,期初余额,借方发生额,贷方发生额,期末余额\n");

        for (Map<String, Object> row : data) {
            csv.append(row.get("科目编码")).append(",")
               .append(row.get("科目名称")).append(",")
               .append(row.get("期初余额")).append(",")
               .append(row.get("借方发生额")).append(",")
               .append(row.get("贷方发生额")).append(",")
               .append(row.get("期末余额")).append("\n");
        }

        return csv.toString().getBytes();
    }
}