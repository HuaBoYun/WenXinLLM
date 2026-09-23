package com.huabo.fxgl.util;

import com.huabo.fxgl.entity.TblRiskWarning;
import org.apache.poi.xwpf.usermodel.*;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.*;

import java.io.FileOutputStream;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Word 报告生成工具类
 * 使用 Apache POI XWPF 生成 .docx 格式的风险预警报告
 *
 * @author 华博云开发团队
 * @since 2025-11-15
 */
public class WordReportGenerator {

    /**
     * 生成 Word 报告主入口
     */
    public static void generate(String filePath, Map<String, Object> reportParams,
                                List<TblRiskWarning> warnings, List<String> includeContents) throws Exception {
        XWPFDocument document = new XWPFDocument();
        FileOutputStream out = null;
        try {
            writeTitle(document, reportParams);
            writeMetaInfo(document, reportParams, warnings.size());
            writeDescription(document, reportParams);

            if (includeContents == null || includeContents.contains("STATISTICS")) {
                writeStatistics(document, warnings);
            }
            if (includeContents == null || includeContents.contains("DETAILS")) {
                writeDetailTable(document, warnings);
            }
            if (includeContents == null || includeContents.contains("RECOMMENDATIONS")) {
                writeRecommendations(document, warnings);
            }

            out = new FileOutputStream(filePath);
            document.write(out);
        } finally {
            if (out != null) {
                try { out.close(); } catch (Exception ignored) {}
            }
            // POI 3.12 的 XWPFDocument.close() 是 protected，写入完成后无需手动关闭
        }
    }

    private static void writeTitle(XWPFDocument doc, Map<String, Object> params) {
        String title = (String) params.getOrDefault("reportTitle", "风险预警报告");
        XWPFParagraph titlePara = doc.createParagraph();
        titlePara.setAlignment(ParagraphAlignment.CENTER);
        XWPFRun run = titlePara.createRun();
        run.setText(title);
        run.setBold(true);
        run.setFontSize(22);
        run.setColor("409EFF");
        setFontFamily(run, "宋体");
        run.addBreak();
    }

    private static void writeMetaInfo(XWPFDocument doc, Map<String, Object> params, int total) {
        XWPFTable metaTable = doc.createTable(2, 4);

        String reportType = ReportDataHelper.getReportTypeText((String) params.get("reportType"));
        String timeRange = ReportDataHelper.getTimeRangeText((String) params.get("timeRange"));
        String generateTime = ReportDataHelper.formatDateTime(LocalDateTime.now());

        // 第一行：标签
        setCellText(metaTable.getRow(0).getCell(0), "报告类型", true, "F5F7FA");
        setCellText(metaTable.getRow(0).getCell(1), reportType, false, "FFFFFF");
        setCellText(metaTable.getRow(0).getCell(2), "时间范围", true, "F5F7FA");
        setCellText(metaTable.getRow(0).getCell(3), timeRange, false, "FFFFFF");
        // 第二行
        setCellText(metaTable.getRow(1).getCell(0), "生成时间", true, "F5F7FA");
        setCellText(metaTable.getRow(1).getCell(1), generateTime, false, "FFFFFF");
        setCellText(metaTable.getRow(1).getCell(2), "数据条数", true, "F5F7FA");
        setCellText(metaTable.getRow(1).getCell(3), total + " 条", false, "FFFFFF");
    }

    private static void writeDescription(XWPFDocument doc, Map<String, Object> params) {
        String description = (String) params.get("reportDescription");
        if (description == null || description.trim().isEmpty()) return;

        XWPFParagraph descPara = doc.createParagraph();
        XWPFRun run = descPara.createRun();
        run.setText(description);
        run.setFontSize(10);
        run.setColor("666666");
        setFontFamily(run, "宋体");
        run.addBreak();
    }

    private static void writeStatistics(XWPFDocument doc, List<TblRiskWarning> warnings) {
        XWPFParagraph title = doc.createParagraph();
        XWPFRun titleRun = title.createRun();
        titleRun.setText("统计概览");
        titleRun.setBold(true);
        titleRun.setFontSize(16);
        setFontFamily(titleRun, "宋体");
        titleRun.addBreak();

        Map<String, Integer> stats = ReportDataHelper.calculateStatistics(warnings);
        XWPFTable statTable = doc.createTable(2, 6);

        String[] labels = {"预警总数", "高风险", "中风险", "低风险", "待处理", "已处理"};
        String[] values = {
                String.valueOf(stats.get("total")), String.valueOf(stats.get("high")),
                String.valueOf(stats.get("medium")), String.valueOf(stats.get("low")),
                String.valueOf(stats.get("pending")), String.valueOf(stats.get("processed"))
        };
        String[] colors = {"67C23A", "F56C6C", "E6A23C", "67C23A", "409EFF", "909399"};

        for (int i = 0; i < 6; i++) {
            XWPFTableCell valueCell = statTable.getRow(0).getCell(i);
            setCellText(valueCell, values[i], true, colors[i], "FFFFFF", 18);
            XWPFTableCell labelCell = statTable.getRow(1).getCell(i);
            setCellText(labelCell, labels[i], false, colors[i], "FFFFFF", 9);
        }

        // 空行
        doc.createParagraph().createRun().addBreak();
    }

    private static void writeDetailTable(XWPFDocument doc, List<TblRiskWarning> warnings) {
        XWPFParagraph title = doc.createParagraph();
        XWPFRun titleRun = title.createRun();
        titleRun.setText("详细数据");
        titleRun.setBold(true);
        titleRun.setFontSize(16);
        setFontFamily(titleRun, "宋体");
        titleRun.addBreak();

        String[] headers = {"序号", "预警编码", "预警类型", "预警级别", "预警状态", "企业名称", "预警值", "预警时间"};
        XWPFTable table = doc.createTable(warnings.size() + 1, headers.length);

        // 表头
        for (int i = 0; i < headers.length; i++) {
            setCellText(table.getRow(0).getCell(i), headers[i], true, "409EFF", "FFFFFF", 10);
        }

        // 数据行
        for (int i = 0; i < warnings.size(); i++) {
            TblRiskWarning w = warnings.get(i);
            String bg = (i % 2 == 0) ? "FFFFFF" : "F9F9F9";
            XWPFTableRow row = table.getRow(i + 1);
            setCellText(row.getCell(0), String.valueOf(i + 1), false, bg);
            setCellText(row.getCell(1), safe(w.getWarningCode()), false, bg);
            setCellText(row.getCell(2), ReportDataHelper.getWarningTypeText(w.getWarningType()), false, bg);
            setCellText(row.getCell(3), ReportDataHelper.getWarningLevelText(w.getWarningLevel()), false, bg);
            setCellText(row.getCell(4), ReportDataHelper.getWarningStatusText(w.getWarningStatus()), false, bg);
            setCellText(row.getCell(5), safe(w.getCompanyName()), false, bg);
            setCellText(row.getCell(6), w.getWarningValue() != null ? w.getWarningValue().toString() : "-", false, bg);
            setCellText(row.getCell(7), w.getCreateTime() != null ? ReportDataHelper.formatDate(w.getCreateTime()) : "-", false, bg);
        }
    }

    private static void writeRecommendations(XWPFDocument doc, List<TblRiskWarning> warnings) {
        XWPFParagraph title = doc.createParagraph();
        XWPFRun titleRun = title.createRun();
        titleRun.setText("建议措施");
        titleRun.setBold(true);
        titleRun.setFontSize(16);
        setFontFamily(titleRun, "宋体");
        titleRun.addBreak();

        List<String> recommendations = ReportDataHelper.generateRecommendations(warnings);
        for (int i = 0; i < recommendations.size(); i++) {
            XWPFParagraph p = doc.createParagraph();
            XWPFRun run = p.createRun();
            run.setText((i + 1) + ". " + recommendations.get(i));
            run.setFontSize(11);
            setFontFamily(run, "宋体");
            run.addBreak();
        }
    }

    private static void setCellText(XWPFTableCell cell, String text, boolean bold, String bgColor) {
        setCellText(cell, text, bold, bgColor, "333333", 10);
    }

    private static void setCellText(XWPFTableCell cell, String text, boolean bold, String bgColor, String fontColor, int fontSize) {
        cell.setColor(bgColor);
        // 清空默认段落
        cell.removeParagraph(0);
        XWPFParagraph p = cell.addParagraph();
        p.setAlignment(ParagraphAlignment.CENTER);
        XWPFRun run = p.createRun();
        run.setText(text == null ? "" : text);
        run.setBold(bold);
        run.setFontSize(fontSize);
        run.setColor(fontColor);
        setFontFamily(run, "宋体");
    }

    /**
     * 设置字体（同时设置东亚字体和ASCII字体，确保中英文都显示正确）
     */
    private static void setFontFamily(XWPFRun run, String fontName) {
        try {
            CTRPr rpr = run.getCTR().isSetRPr() ? run.getCTR().getRPr() : run.getCTR().addNewRPr();
            CTFonts fonts = rpr.isSetRFonts() ? rpr.getRFonts() : rpr.addNewRFonts();
            fonts.setAscii(fontName);
            fonts.setEastAsia(fontName);
            fonts.setHAnsi(fontName);
            fonts.setCs(fontName);
        } catch (Exception e) {
            // 设置字体失败不影响主流程
        }
    }

    private static String safe(String v) {
        return v == null ? "" : v;
    }
}
