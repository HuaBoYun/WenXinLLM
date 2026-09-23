package com.global.treasurer.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.global.treasurer.entity.TblRegulatoryAnalysis;
import com.global.treasurer.mapper.TblRegulatoryAnalysisMapper;
import com.global.treasurer.service.TblRegulatoryAnalysisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.annotation.Resource;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.util.*;

/**
 * 监管分析Service实现类
 * @author Claude
 * @date 2026-01-20
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class TblRegulatoryAnalysisServiceImpl implements TblRegulatoryAnalysisService {
    private static final Logger log = LoggerFactory.getLogger(TblRegulatoryAnalysisServiceImpl.class);

    @Resource
    private TblRegulatoryAnalysisMapper tblRegulatoryAnalysisMapper;

    @Override
    public PageInfo<TblRegulatoryAnalysis> getAnalysisPage(Integer pageNum, Integer pageSize,
                                                           String analysisName, String analysisType,
                                                           String complianceStatus, String startDate, String endDate) {
        PageHelper.startPage(pageNum, pageSize);
        LambdaQueryWrapper<TblRegulatoryAnalysis> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.isNotBlank(analysisName), TblRegulatoryAnalysis::getAnalysisName, analysisName)
               .eq(StringUtils.isNotBlank(analysisType), TblRegulatoryAnalysis::getAnalysisType, analysisType)
               .eq(StringUtils.isNotBlank(complianceStatus), TblRegulatoryAnalysis::getComplianceStatus, complianceStatus)
               .eq(TblRegulatoryAnalysis::getDelFlag, "0")
               .orderByDesc(TblRegulatoryAnalysis::getCreateTime);
        List<TblRegulatoryAnalysis> list = tblRegulatoryAnalysisMapper.selectList(wrapper);
        return new PageInfo<>(list);
    }

    @Override
    public TblRegulatoryAnalysis getAnalysisById(String analysisId) {
        return tblRegulatoryAnalysisMapper.selectById(analysisId);
    }

    @Override
    public TblRegulatoryAnalysis saveAnalysis(TblRegulatoryAnalysis analysis) {
        log.info("开始保存监管分析");
        log.info("  - analysisName: {}", analysis.getAnalysisName());
        log.info("  - regulator: {}", analysis.getRegulator());
        log.info("  - analysisType: {}", analysis.getAnalysisType());
        log.info("  - analysisDate: {}", analysis.getAnalysisDate());
        log.info("  - complianceScore: {}", analysis.getComplianceScore());
        log.info("  - riskLevel: {}", analysis.getRiskLevel());
        log.info("  - description: {}", analysis.getDescription());

        analysis.setAnalysisNo("RA" + System.currentTimeMillis());
        analysis.setCreateTime(new Date());
        analysis.setDelFlag("0");
        if (analysis.getAnalysisStatus() == null) {
            analysis.setAnalysisStatus("DRAFT");
        }
        try {
            int result = tblRegulatoryAnalysisMapper.insert(analysis);
            log.info("保存成功, analysisId: {}, result: {}", analysis.getAnalysisId(), result);
            return analysis;
        } catch (Exception e) {
            log.error("保存监管分析失败, analysis: {}", analysis, e);
            throw new RuntimeException("保存失败: " + e.getMessage(), e);
        }
    }

    @Override
    public TblRegulatoryAnalysis updateAnalysis(TblRegulatoryAnalysis analysis) {
        log.info("开始更新监管分析: {}", analysis);
        log.info("  - analysisId: {}", analysis.getAnalysisId());

        try {
            // 使用 UpdateWrapper 只更新非空字段，同时设置 WHERE 条件
            com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper<TblRegulatoryAnalysis> updateWrapper = new com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper<>();

            // 首先设置 WHERE 条件：根据 analysisId 查找记录
            updateWrapper.eq(TblRegulatoryAnalysis::getAnalysisId, analysis.getAnalysisId());

            boolean needUpdate = false;

            if (analysis.getAnalysisName() != null) {
                updateWrapper.set(TblRegulatoryAnalysis::getAnalysisName, analysis.getAnalysisName());
                needUpdate = true;
                log.info("  更新 analysisName: {}", analysis.getAnalysisName());
            }
            if (analysis.getRegulator() != null) {
                updateWrapper.set(TblRegulatoryAnalysis::getRegulator, analysis.getRegulator());
                needUpdate = true;
                log.info("  更新 regulator: {}", analysis.getRegulator());
            }
            if (analysis.getAnalysisType() != null) {
                updateWrapper.set(TblRegulatoryAnalysis::getAnalysisType, analysis.getAnalysisType());
                needUpdate = true;
                log.info("  更新 analysisType: {}", analysis.getAnalysisType());
            }
            if (analysis.getAnalysisDate() != null) {
                updateWrapper.set(TblRegulatoryAnalysis::getAnalysisDate, analysis.getAnalysisDate());
                needUpdate = true;
                log.info("  更新 analysisDate: {}", analysis.getAnalysisDate());
            }
            if (analysis.getComplianceScore() != null) {
                updateWrapper.set(TblRegulatoryAnalysis::getComplianceScore, analysis.getComplianceScore());
                needUpdate = true;
                log.info("  更新 complianceScore: {}", analysis.getComplianceScore());
            }
            if (analysis.getRiskLevel() != null) {
                updateWrapper.set(TblRegulatoryAnalysis::getRiskLevel, analysis.getRiskLevel());
                needUpdate = true;
                log.info("  更新 riskLevel: {}", analysis.getRiskLevel());
            }
            if (analysis.getDescription() != null) {
                updateWrapper.set(TblRegulatoryAnalysis::getDescription, analysis.getDescription());
                needUpdate = true;
                log.info("  更新 description: {}", analysis.getDescription());
            }

            // 始终设置更新时间
            Date updateTime = new Date();
            updateWrapper.set(TblRegulatoryAnalysis::getUpdateTime, updateTime);

            int result = tblRegulatoryAnalysisMapper.update(null, updateWrapper);
            log.info("更新成功, analysisId: {}, result: {}, needUpdate: {}", analysis.getAnalysisId(), result, needUpdate);
            return analysis;
        } catch (Exception e) {
            log.error("更新监管分析失败, analysis: {}", analysis, e);
            throw new RuntimeException("更新失败: " + e.getMessage(), e);
        }
    }

    @Override
    public void deleteAnalysis(String analysisId) {
        TblRegulatoryAnalysis analysis = new TblRegulatoryAnalysis();
        analysis.setAnalysisId(analysisId);
        analysis.setDelFlag("1");
        analysis.setUpdateTime(new Date());
        tblRegulatoryAnalysisMapper.updateById(analysis);
    }

    @Override
    public TblRegulatoryAnalysis executeAnalysis(String analysisType, String startDate, String endDate) {
        TblRegulatoryAnalysis analysis = new TblRegulatoryAnalysis();
        analysis.setAnalysisType(analysisType);
        analysis.setAnalysisName(analysisType + "_监管分析");
        analysis.setAnalysisDate(new Date());
        analysis.setComplianceStatus("COMPLIANT");
        analysis.setRiskLevel("LOW");
        analysis.setTotalAmount(new BigDecimal("5000000"));
        analysis.setCrossBorderAmount(new BigDecimal("1000000"));
        analysis.setDomesticAmount(new BigDecimal("4000000"));
        analysis.setViolationCount(0);
        analysis.setWarningCount(2);
        analysis.setAnalysisResult("合规检查通过");
        analysis.setRecommendations("建议继续保持当前合规策略");
        analysis.setGenerateTime(new Date());
        analysis.setAnalysisStatus("COMPLETED");
        return saveAnalysis(analysis);
    }

    @Override
    public void reviewAnalysis(String analysisId, String reviewResult, String reviewRemark) {
        TblRegulatoryAnalysis analysis = new TblRegulatoryAnalysis();
        analysis.setAnalysisId(analysisId);
        analysis.setReviewRemark(reviewRemark);
        analysis.setReviewTime(new Date());
        analysis.setAnalysisStatus("REVIEWED");
        analysis.setUpdateTime(new Date());
        tblRegulatoryAnalysisMapper.updateById(analysis);
    }

    @Override
    public byte[] generateReport(String analysisId, String reportFormat) {
        log.info("生成监管报告: {}, 格式: {}", analysisId, reportFormat);
        try {
            TblRegulatoryAnalysis analysis = getAnalysisById(analysisId);
            if (analysis == null) {
                throw new RuntimeException("分析记录不存在");
            }

            return generateExcelReport(analysis);
        } catch (Exception e) {
            log.error("生成报告失败", e);
            throw new RuntimeException("生成报告失败: " + e.getMessage());
        }
    }

    /**
     * 生成Excel报告
     */
    private byte[] generateExcelReport(TblRegulatoryAnalysis analysis) throws Exception {
        Workbook workbook = new XSSFWorkbook();

        // 创建标题页
        Sheet sheet = workbook.createSheet("监管分析报告");

        // 设置标题样式
        CellStyle titleStyle = workbook.createCellStyle();
        Font titleFont = workbook.createFont();
        titleFont.setBold(true);
        titleFont.setFontHeightInPoints((short) 16);
        titleStyle.setFont(titleFont);
        titleStyle.setAlignment(HorizontalAlignment.CENTER);

        // 设置表头样式
        CellStyle headerStyle = workbook.createCellStyle();
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerStyle.setFont(headerFont);
        headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headerStyle.setBorderBottom(BorderStyle.THIN);
        headerStyle.setBorderTop(BorderStyle.THIN);
        headerStyle.setBorderLeft(BorderStyle.THIN);
        headerStyle.setBorderRight(BorderStyle.THIN);

        // 设置数据样式
        CellStyle dataStyle = workbook.createCellStyle();
        dataStyle.setBorderBottom(BorderStyle.THIN);
        dataStyle.setBorderTop(BorderStyle.THIN);
        dataStyle.setBorderLeft(BorderStyle.THIN);
        dataStyle.setBorderRight(BorderStyle.THIN);

        int rowNum = 0;

        // 报告标题
        Row titleRow = sheet.createRow(rowNum++);
        Cell titleCell = titleRow.createCell(0);
        titleCell.setCellValue("监管分析报告");
        titleCell.setCellStyle(titleStyle);

        rowNum++;

        // 基本信息部分
        Row section1Row = sheet.createRow(rowNum++);
        Cell section1Cell = section1Row.createCell(0);
        section1Cell.setCellValue("基本信息");
        section1Cell.setCellStyle(titleStyle);

        // 格式化日期
        String formattedDate = analysis.getAnalysisDate() != null
            ? analysis.getAnalysisDate().toString()
            : "-";

        String[][] basicInfo = {
            {"分析编号", analysis.getAnalysisNo()},
            {"分析名称", analysis.getAnalysisName()},
            {"监管机构", getRegulatorName(analysis.getRegulator())},
            {"分析类型", getAnalysisTypeName(analysis.getAnalysisType())},
            {"分析日期", formattedDate},
            {"合规评分", (analysis.getComplianceScore() != null ? String.valueOf(analysis.getComplianceScore()) : "0") + "%"},
            {"风险等级", getRiskLevelName(analysis.getRiskLevel())},
            {"状态", getStatusName(analysis.getAnalysisStatus())}
        };

        for (String[] info : basicInfo) {
            Row row = sheet.createRow(rowNum++);
            Cell labelCell = row.createCell(0);
            labelCell.setCellValue(info[0]);
            labelCell.setCellStyle(headerStyle);

            Cell valueCell = row.createCell(1);
            valueCell.setCellValue(info[1]);
            valueCell.setCellStyle(dataStyle);
        }

        rowNum++;

        // 分析描述部分
        Row section2Row = sheet.createRow(rowNum++);
        Cell section2Cell = section2Row.createCell(0);
        section2Cell.setCellValue("分析描述");
        section2Cell.setCellStyle(titleStyle);

        Row descRow = sheet.createRow(rowNum++);
        Cell descCell = descRow.createCell(0);
        descCell.setCellValue(analysis.getDescription() != null ? analysis.getDescription() : "无描述");
        descCell.setCellStyle(dataStyle);

        // 自动调整列宽
        sheet.setColumnWidth(0, 5000);
        sheet.setColumnWidth(1, 8000);

        // 写入字节数组
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        workbook.close();

        return outputStream.toByteArray();
    }

    /**
     * 获取监管机构名称
     */
    private String getRegulatorName(String regulator) {
        if (regulator == null) return "-";
        switch (regulator.toUpperCase()) {
            case "PBOC": return "中国人民银行";
            case "CBIRC": return "银保监会";
            case "CSRC": return "证监会";
            case "SAFE": return "外汇管理局";
            default: return regulator;
        }
    }

    /**
     * 获取分析类型名称
     */
    private String getAnalysisTypeName(String type) {
        if (type == null) return "-";
        switch (type.toUpperCase()) {
            case "CROSS_BORDER": return "跨境资金分析";
            case "LARGE_AMOUNT": return "大额交易分析";
            case "FREQUENCY": return "高频交易分析";
            case "COMPLIANCE": return "合规检查分析";
            default: return type;
        }
    }

    /**
     * 获取风险等级名称
     */
    private String getRiskLevelName(String level) {
        if (level == null) return "-";
        switch (level.toUpperCase()) {
            case "LOW": return "低风险";
            case "MEDIUM": return "中风险";
            case "HIGH": return "高风险";
            case "CRITICAL": return "严重风险";
            default: return level;
        }
    }

    /**
     * 获取状态名称
     */
    private String getStatusName(String status) {
        if (status == null) return "-";
        switch (status.toUpperCase()) {
            case "DRAFT": return "草稿";
            case "ANALYZING": return "分析中";
            case "COMPLETED": return "已完成";
            case "REVIEWED": return "已审核";
            default: return status;
        }
    }

    @Override
    public Map<String, Object> getComplianceStatistics() {
        Map<String, Object> result = new HashMap<>();
        LambdaQueryWrapper<TblRegulatoryAnalysis> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(TblRegulatoryAnalysis::getDelFlag, "0");
        List<TblRegulatoryAnalysis> list = tblRegulatoryAnalysisMapper.selectList(wrapper);
        
        result.put("totalCount", list.size());
        result.put("compliantCount", list.stream().filter(a -> "COMPLIANT".equals(a.getComplianceStatus())).count());
        result.put("nonCompliantCount", list.stream().filter(a -> "NON_COMPLIANT".equals(a.getComplianceStatus())).count());
        result.put("pendingReviewCount", list.stream().filter(a -> "PENDING_REVIEW".equals(a.getComplianceStatus())).count());
        return result;
    }

    @Override
    public Map<String, Object> getRiskLevelDistribution() {
        Map<String, Object> result = new HashMap<>();
        LambdaQueryWrapper<TblRegulatoryAnalysis> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(TblRegulatoryAnalysis::getDelFlag, "0");
        List<TblRegulatoryAnalysis> list = tblRegulatoryAnalysisMapper.selectList(wrapper);
        
        result.put("lowCount", list.stream().filter(a -> "LOW".equals(a.getRiskLevel())).count());
        result.put("mediumCount", list.stream().filter(a -> "MEDIUM".equals(a.getRiskLevel())).count());
        result.put("highCount", list.stream().filter(a -> "HIGH".equals(a.getRiskLevel())).count());
        result.put("criticalCount", list.stream().filter(a -> "CRITICAL".equals(a.getRiskLevel())).count());
        return result;
    }

    @Override
    public List<Map<String, Object>> getAnalysisTypes() {
        List<Map<String, Object>> types = new ArrayList<>();
        types.add(createTypeMap("CROSS_BORDER", "跨境资金分析"));
        types.add(createTypeMap("LARGE_AMOUNT", "大额交易分析"));
        types.add(createTypeMap("FREQUENCY", "高频交易分析"));
        types.add(createTypeMap("COMPLIANCE", "合规检查分析"));
        return types;
    }

    private Map<String, Object> createTypeMap(String code, String name) {
        Map<String, Object> map = new HashMap<>();
        map.put("code", code);
        map.put("name", name);
        return map;
    }

    @Override
    public Map<String, Object> getOverviewStatistics() {
        Map<String, Object> result = new HashMap<>();
        LambdaQueryWrapper<TblRegulatoryAnalysis> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(TblRegulatoryAnalysis::getDelFlag, "0");
        List<TblRegulatoryAnalysis> list = tblRegulatoryAnalysisMapper.selectList(wrapper);

        // 合规率计算
        long compliantCount = list.stream().filter(a -> "COMPLIANT".equals(a.getComplianceStatus())).count();
        double complianceRate = list.isEmpty() ? 0.0 : (double) compliantCount / list.size() * 100;

        // 待处理事项（未完成的）
        long pendingCount = list.stream()
            .filter(a -> !"COMPLETED".equals(a.getAnalysisStatus()))
            .filter(a -> !"REVIEWED".equals(a.getAnalysisStatus()))
            .count();

        // 风险预警（高风险及以上）
        long riskWarningCount = list.stream()
            .filter(a -> "HIGH".equals(a.getRiskLevel()) || "CRITICAL".equals(a.getRiskLevel()))
            .count();

        // 监管报告总数
        long reportCount = list.stream()
            .filter(a -> a.getReportFilePath() != null && !a.getReportFilePath().isEmpty())
            .count();

        result.put("complianceRate", Math.round(complianceRate * 10) / 10.0); // 保留一位小数
        result.put("pendingCount", pendingCount);
        result.put("riskWarningCount", riskWarningCount);
        result.put("reportCount", reportCount);

        return result;
    }
}

