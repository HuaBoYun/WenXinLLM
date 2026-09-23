package com.huabo.cybermonitor.service;

import com.huabo.cybermonitor.util.SimpleXlsxWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.util.*;

/**
 * 企业驾驶舱报告导出服务
 *
 * @author huabo
 */
@Service
public class DashboardReportExportService {

    private static final Logger log = LoggerFactory.getLogger(DashboardReportExportService.class);

    @Autowired
    private IEnterpriseDashboardService enterpriseDashboardService;

    @Value("${report.export.path:/tmp/reports}")
    private String reportPath;

    /**
     * 导出驾驶舱报告
     *
     * @param enterpriseId 企业ID
     * @param reportType 报告类型 (pdf, excel)
     * @return 报告文件路径
     */
    public String exportDashboardReport(String enterpriseId, String reportType) {
        try {
            log.info("开始导出企业驾驶舱报告，企业ID：{}，报告类型：{}", enterpriseId, reportType);

            // 确保导出目录存在
            File dir = new File(reportPath);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            // 获取企业数据
            String enterpriseName = getEnterpriseName(enterpriseId);
            String fileName = "驾驶舱报告_" + enterpriseName + "_" + System.currentTimeMillis();

            String filePath;
            if ("pdf".equalsIgnoreCase(reportType)) {
                filePath = exportAsPDF(enterpriseId, enterpriseName, fileName);
            } else {
                filePath = exportAsExcel(enterpriseId, enterpriseName, fileName);
            }

            log.info("企业驾驶舱报告导出成功，文件路径：{}", filePath);
            return filePath;

        } catch (Exception e) {
            log.error("导出企业驾驶舱报告失败，企业ID：{}", enterpriseId, e);
            throw new RuntimeException("导出报告失败：" + e.getMessage());
        }
    }

    /**
     * 导出驾驶舱报告（返回字节数组用于直接下载）
     *
     * @param enterpriseId 企业ID
     * @param reportType 报告类型
     * @return 报告文件字节数组
     */
    public byte[] exportDashboardReportAsBytes(String enterpriseId, String reportType) {
        try {
            log.info("开始导出企业驾驶舱报告（字节数组），企业ID：{}，报告类型：{}", enterpriseId, reportType);

            // 获取企业数据
            String enterpriseName = getEnterpriseName(enterpriseId);

            byte[] bytes;
            if ("pdf".equalsIgnoreCase(reportType)) {
                bytes = exportAsPDFBytes(enterpriseId, enterpriseName);
            } else {
                bytes = exportAsExcelBytes(enterpriseId, enterpriseName);
            }

            log.info("企业驾驶舱报告导出成功，字节数组大小：{} bytes", bytes.length);
            return bytes;

        } catch (Exception e) {
            log.error("导出企业驾驶舱报告失败，企业ID：{}", enterpriseId, e);
            throw new RuntimeException("导出报告失败：" + e.getMessage());
        }
    }

    /**
     * 获取企业名称
     */
    private String getEnterpriseName(String enterpriseId) {
        try {
            Map<String, Object> overview = enterpriseDashboardService.getEnterpriseOverview(enterpriseId);
            String name = overview != null ? (String) overview.get("enterpriseName") : null;
            return name != null ? name : "未知企业";
        } catch (Exception e) {
            log.warn("获取企业名称失败，使用默认名称", e);
            return "企业";
        }
    }

    /**
     * 导出为 Excel 格式
     */
    private String exportAsExcel(String enterpriseId, String enterpriseName, String fileName) throws Exception {
        String filePath = reportPath + "/" + fileName + ".xlsx";

        // 准备数据
        List<List<Object>> dataList = buildReportData(enterpriseId);

        // 使用零依赖 OOXML 工具写入文件
        byte[] bytes = SimpleXlsxWriter.write("企业管理驾驶舱报告", dataList);
        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            fos.write(bytes);
        }

        return filePath;
    }

    /**
     * 导出为 Excel 格式（返回字节数组）
     */
    private byte[] exportAsExcelBytes(String enterpriseId, String enterpriseName) throws Exception {
        // 准备数据
        List<List<Object>> dataList = buildReportData(enterpriseId);
        // 使用零依赖 OOXML 工具生成字节数组
        return SimpleXlsxWriter.write("企业管理驾驶舱报告", dataList);
    }

    /**
     * 构建报告数据
     */
    private List<List<Object>> buildReportData(String enterpriseId) throws Exception {
        List<List<Object>> dataList = new ArrayList<>();

        // 获取企业数据
        String enterpriseName = getEnterpriseName(enterpriseId);
        Map<String, Object> overview = enterpriseDashboardService.getEnterpriseOverview(enterpriseId);
        Map<String, Object> indicators = enterpriseDashboardService.getKeyIndicators(enterpriseId);
        Map<String, Object> businessStatus = enterpriseDashboardService.getBusinessStatus(enterpriseId);
        Map<String, Object> riskMonitoring = enterpriseDashboardService.getRiskMonitoring(enterpriseId);
        Map<String, Object> submissionStatus = enterpriseDashboardService.getDataSubmissionStatus(enterpriseId);
        List<Map<String, Object>> planExecution = enterpriseDashboardService.getOperatingPlanExecution(enterpriseId);
        List<Map<String, Object>> budgetExecution = enterpriseDashboardService.getBudgetExecution(enterpriseId);

        // 标题
        dataList.add(Arrays.asList(enterpriseName + "管理驾驶舱报告"));
        dataList.add(Arrays.asList(""));
        dataList.add(Arrays.asList("报告生成时间：" + new Date()));
        dataList.add(Arrays.asList(""));

        // 一、企业概览
        dataList.add(Arrays.asList("一、企业概览"));
        dataList.add(Arrays.asList(""));
        dataList.add(Arrays.asList("企业名称", "", "行业分类", ""));
        dataList.add(Arrays.asList(overview.get("enterpriseName"), "", overview.get("industryClassification"), ""));
        dataList.add(Arrays.asList("企业规模", "", "上市状态", ""));
        dataList.add(Arrays.asList(overview.get("enterpriseScale"), "", overview.get("listingStatus"), ""));
        dataList.add(Arrays.asList(""));

        // 二、关键指标
        dataList.add(Arrays.asList("二、关键指标"));
        dataList.add(Arrays.asList(""));
        dataList.add(Arrays.asList("营业收入(万元)", "", "营业收入增长率(%)", ""));
        dataList.add(Arrays.asList(indicators.get("operatingRevenue"), "", indicators.get("revenueGrowthRate"), ""));
        dataList.add(Arrays.asList("净利润(万元)", "", "净利润增长率(%)", ""));
        dataList.add(Arrays.asList(indicators.get("netProfit"), "", indicators.get("profitGrowthRate"), ""));
        dataList.add(Arrays.asList("总资产(万元)", "", "资产负债率(%)", ""));
        dataList.add(Arrays.asList(indicators.get("totalAssets"), "", indicators.get("assetLiabilityRatio"), ""));
        dataList.add(Arrays.asList("净资产收益率(%)", "", "总资产收益率(%)", ""));
        dataList.add(Arrays.asList(indicators.get("roe"), "", indicators.get("roa"), ""));
        dataList.add(Arrays.asList(""));

        // 三、业务状态监控
        dataList.add(Arrays.asList("三、业务状态监控"));
        dataList.add(Arrays.asList(""));
        dataList.add(Arrays.asList("子公司数量", "", "业务板块数", ""));
        dataList.add(Arrays.asList(businessStatus.get("subsidiaryCount") + "家", "", businessStatus.get("businessSegmentCount") + "个", ""));
        dataList.add(Arrays.asList("员工总数", "", "发展趋势", ""));
        dataList.add(Arrays.asList(businessStatus.get("employeeCount") + "人", "", businessStatus.get("developmentTrend"), ""));
        dataList.add(Arrays.asList(""));

        // 四、风险监控
        dataList.add(Arrays.asList("四、风险监控"));
        dataList.add(Arrays.asList(""));
        dataList.add(Arrays.asList("风险等级", "", "风险评分", ""));
        dataList.add(Arrays.asList(riskMonitoring.get("riskLevel"), "", riskMonitoring.get("riskScore"), ""));
        dataList.add(Arrays.asList("预警数量", "", "合规评分", ""));
        dataList.add(Arrays.asList(riskMonitoring.get("warningCount") + "个", "", riskMonitoring.get("complianceScore") + "分", ""));
        dataList.add(Arrays.asList(""));

        // 五、数据报送状态
        dataList.add(Arrays.asList("五、数据报送状态"));
        dataList.add(Arrays.asList(""));
        dataList.add(Arrays.asList("报送完成率(%)", "", "数据质量评分", ""));
        dataList.add(Arrays.asList(submissionStatus.get("submissionCompletionRate"), "", submissionStatus.get("dataQualityScore") + "分", ""));
        dataList.add(Arrays.asList("最后报送时间", "", "", ""));
        dataList.add(Arrays.asList(submissionStatus.get("lastSubmissionTime"), "", "", ""));
        dataList.add(Arrays.asList(""));

        // 六、经营计划执行情况
        dataList.add(Arrays.asList("六、经营计划执行情况"));
        dataList.add(Arrays.asList(""));
        dataList.add(Arrays.asList("指标名称", "", "目标", "实际", "完成率(%)"));
        if (planExecution != null && !planExecution.isEmpty()) {
            for (Map<String, Object> item : planExecution) {
                dataList.add(Arrays.asList(
                    item.get("planName"), "",
                    item.get("targetValue"),
                    item.get("actualValue"),
                    item.get("executionRate")
                ));
            }
        } else {
            dataList.add(Arrays.asList("暂无数据", "", "", "", ""));
        }
        dataList.add(Arrays.asList(""));

        // 七、预算执行情况
        dataList.add(Arrays.asList("七、预算执行情况"));
        dataList.add(Arrays.asList(""));
        dataList.add(Arrays.asList("预算项目", "", "预算(万元)", "执行(万元)", "执行率(%)"));
        if (budgetExecution != null && !budgetExecution.isEmpty()) {
            for (Map<String, Object> item : budgetExecution) {
                dataList.add(Arrays.asList(
                    item.get("budgetName"), "",
                    item.get("budgetAmount"),
                    item.get("executedAmount"),
                    item.get("executionRate")
                ));
            }
        } else {
            dataList.add(Arrays.asList("暂无数据", "", "", "", ""));
        }

        return dataList;
    }

    /**
     * 导出为 PDF 格式
     */
    private String exportAsPDF(String enterpriseId, String enterpriseName, String fileName) throws Exception {
        // PDF 生成需要额外的依赖和实现
        // 暂时返回 Excel 格式，因为 PDF 生成比较复杂
        log.warn("PDF 导出暂未实现，使用 Excel 格式代替");
        return exportAsExcel(enterpriseId, enterpriseName, fileName);
    }

    /**
     * 导出为 PDF 格式（返回字节数组）
     */
    private byte[] exportAsPDFBytes(String enterpriseId, String enterpriseName) throws Exception {
        // PDF 生成需要额外的依赖和实现
        // 暂时返回 Excel 格式
        log.warn("PDF 导出暂未实现，使用 Excel 格式代替");
        return exportAsExcelBytes(enterpriseId, enterpriseName);
    }
}
