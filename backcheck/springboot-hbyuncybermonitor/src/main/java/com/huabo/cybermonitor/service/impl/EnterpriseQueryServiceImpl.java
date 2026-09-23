package com.huabo.cybermonitor.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.huabo.cybermonitor.entity.EnterpriseInfo;
import com.huabo.cybermonitor.mapper.EnterpriseQueryMapper;
import com.huabo.cybermonitor.service.IEnterpriseQueryService;
import com.huabo.cybermonitor.util.ExcelUtil;
import com.huabo.cybermonitor.vo.EnterpriseQueryVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 企业查询服务实现类
 *
 * @author system
 * @since 2024-01-01
 */
@Slf4j
@Service
public class EnterpriseQueryServiceImpl implements IEnterpriseQueryService {

    @Autowired
    private EnterpriseQueryMapper enterpriseQueryMapper;

    @Override
    public IPage<EnterpriseInfo> getEnterpriseAdvancedQuery(EnterpriseQueryVO queryVO) {
        Page<EnterpriseInfo> page = new Page<>(queryVO.getPageNumber(), queryVO.getPageSize());
        return enterpriseQueryMapper.selectEnterpriseAdvancedQuery(page, queryVO);
    }

    @Override
    public IPage<EnterpriseInfo> getEnterpriseComprehensiveQuery(EnterpriseQueryVO queryVO) {
        Page<EnterpriseInfo> page = new Page<>(queryVO.getPageNumber(), queryVO.getPageSize());
        return enterpriseQueryMapper.selectEnterpriseComprehensiveQuery(page, queryVO);
    }

    @Override
    public List<EnterpriseInfo> searchEnterprisesByKeyword(String keyword) {
        return enterpriseQueryMapper.selectEnterprisesByKeyword(keyword);
    }

    @Override
    public Map<String, Object> getEnterpriseBasicStatistics() {
        return enterpriseQueryMapper.selectEnterpriseBasicStatistics();
    }

    @Override
    public List<Map<String, Object>> getEnterpriseTypeDistribution() {
        return enterpriseQueryMapper.selectEnterpriseTypeDistribution();
    }

    @Override
    public List<Map<String, Object>> getEnterpriseRegionDistribution() {
        return enterpriseQueryMapper.selectEnterpriseRegionDistribution();
    }

    @Override
    public List<Map<String, Object>> getEnterpriseIndustryDistribution() {
        return enterpriseQueryMapper.selectEnterpriseIndustryDistribution();
    }

    @Override
    public List<Map<String, Object>> getEnterpriseStatusDistribution() {
        return enterpriseQueryMapper.selectEnterpriseStatusDistribution();
    }

    @Override
    public List<Map<String, Object>> getSupervisionLevelDistribution() {
        return enterpriseQueryMapper.selectSupervisionLevelDistribution();
    }

    @Override
    public List<Map<String, Object>> getRegisteredCapitalDistribution() {
        return enterpriseQueryMapper.selectRegisteredCapitalDistribution();
    }

    @Override
    public List<Map<String, Object>> getEstablishDateDistribution() {
        return enterpriseQueryMapper.selectEstablishDateDistribution();
    }

    @Override
    public List<Map<String, Object>> getListingStatusDistribution() {
        return enterpriseQueryMapper.selectListingStatusDistribution();
    }

    @Override
    public List<Map<String, Object>> getRiskLevelDistribution() {
        return enterpriseQueryMapper.selectRiskLevelDistribution();
    }

    @Override
    public Map<String, Object> getStateControlledStatistics() {
        return enterpriseQueryMapper.selectStateControlledStatistics();
    }

    @Override
    public Map<String, Object> getListedCompanyStatistics() {
        return enterpriseQueryMapper.selectListedCompanyStatistics();
    }

    @Override
    public List<Map<String, Object>> getEnterpriseScaleAnalysis() {
        return enterpriseQueryMapper.selectEnterpriseScaleAnalysis();
    }

    @Override
    public List<Map<String, Object>> getEnterpriseDevelopmentTrend() {
        return enterpriseQueryMapper.selectEnterpriseDevelopmentTrend();
    }

    @Override
    public List<EnterpriseInfo> getKeySupervisionEnterprises() {
        return enterpriseQueryMapper.selectKeySupervisionEnterprises();
    }

    @Override
    public List<EnterpriseInfo> getHighRiskEnterprises() {
        return enterpriseQueryMapper.selectHighRiskEnterprises();
    }

    @Override
    public List<EnterpriseInfo> getNewlyEstablishedEnterprises() {
        return enterpriseQueryMapper.selectNewlyEstablishedEnterprises();
    }

    @Override
    public List<EnterpriseInfo> getLargeEnterprises(Double threshold) {
        return enterpriseQueryMapper.selectLargeEnterprises(threshold);
    }

    @Override
    public Map<String, Object> getEnterpriseComprehensiveAnalysis() {
        Map<String, Object> analysis = new HashMap<>();
        try {
            // 基础统计
            Map<String, Object> basicStats = getEnterpriseBasicStatistics();
            analysis.put("basicStatistics", basicStats);

            // 分布统计
            analysis.put("typeDistribution", getEnterpriseTypeDistribution());
            analysis.put("regionDistribution", getEnterpriseRegionDistribution());
            analysis.put("industryDistribution", getEnterpriseIndustryDistribution());
            analysis.put("statusDistribution", getEnterpriseStatusDistribution());

            // 特殊统计
            analysis.put("stateControlledStats", getStateControlledStatistics());
            analysis.put("listedCompanyStats", getListedCompanyStatistics());

            // 趋势分析
            analysis.put("developmentTrend", getEnterpriseDevelopmentTrend());
            analysis.put("scaleAnalysis", getEnterpriseScaleAnalysis());

        } catch (Exception e) {
            log.error("获取企业综合分析失败", e);
            analysis.put("error", "获取企业综合分析失败：" + e.getMessage());
        }
        return analysis;
    }

    @Override
    public Map<String, Object> getEnterpriseSupervisionOverview() {
        Map<String, Object> overview = new HashMap<>();
        try {
            // 监管层级分布
            overview.put("supervisionLevelDistribution", getSupervisionLevelDistribution());

            // 风险等级分布
            overview.put("riskLevelDistribution", getRiskLevelDistribution());

            // 重点监管企业
            List<EnterpriseInfo> keyEnterprises = getKeySupervisionEnterprises();
            overview.put("keySupervisionEnterprises", keyEnterprises);
            overview.put("keySupervisionCount", keyEnterprises.size());

            // 高风险企业
            List<EnterpriseInfo> highRiskEnterprises = getHighRiskEnterprises();
            overview.put("highRiskEnterprises", highRiskEnterprises);
            overview.put("highRiskCount", highRiskEnterprises.size());

            // 新成立企业
            List<EnterpriseInfo> newEnterprises = getNewlyEstablishedEnterprises();
            overview.put("newlyEstablishedEnterprises", newEnterprises);
            overview.put("newlyEstablishedCount", newEnterprises.size());

        } catch (Exception e) {
            log.error("获取企业监管概览失败", e);
            overview.put("error", "获取企业监管概览失败：" + e.getMessage());
        }
        return overview;
    }

    @Override
    public Map<String, Object> getEnterpriseRiskAnalysis() {
        Map<String, Object> riskAnalysis = new HashMap<>();
        try {
            // 风险等级分布
            riskAnalysis.put("riskLevelDistribution", getRiskLevelDistribution());

            // 高风险企业列表
            List<EnterpriseInfo> highRiskEnterprises = getHighRiskEnterprises();
            riskAnalysis.put("highRiskEnterprises", highRiskEnterprises);
            riskAnalysis.put("highRiskCount", highRiskEnterprises.size());

            // 风险企业占比
            Map<String, Object> basicStats = getEnterpriseBasicStatistics();
            Integer totalCount = (Integer) basicStats.get("totalEnterprises");
            if (totalCount != null && totalCount > 0) {
                double riskRatio = (double) highRiskEnterprises.size() / totalCount * 100;
                riskAnalysis.put("highRiskRatio", Math.round(riskRatio * 100.0) / 100.0);
            }

            // 行业风险分布
            riskAnalysis.put("industryDistribution", getEnterpriseIndustryDistribution());

            // 地区风险分布
            riskAnalysis.put("regionDistribution", getEnterpriseRegionDistribution());

        } catch (Exception e) {
            log.error("获取企业风险分析失败", e);
            riskAnalysis.put("error", "获取企业风险分析失败：" + e.getMessage());
        }
        return riskAnalysis;
    }

    @Override
    public void exportEnterpriseQuery(EnterpriseQueryVO queryVO, HttpServletResponse response) {
        try {
            List<EnterpriseInfo> enterpriseList = enterpriseQueryMapper.selectEnterpriseQueryForExport(queryVO);

            // 设置导出的列标题
            String[] headers = {
                "企业名称", "统一社会信用代码", "企业类型", "监管层级", "所属地区", "所属行业",
                "企业状态", "注册资本", "成立日期", "法定代表人", "联系电话", "企业邮箱"
            };

            ExcelUtil excelUtil = new ExcelUtil("企业查询结果", headers);

            // 添加数据行
            for (int i = 0; i < enterpriseList.size(); i++) {
                EnterpriseInfo enterprise = enterpriseList.get(i);
                Object[] row = {
                    enterprise.getEnterpriseName(),
                    enterprise.getCreditCode(),
                    enterprise.getEnterpriseType(),
                    enterprise.getSupervisionLevel(),
                    enterprise.getEnterpriseStatus(),
                    enterprise.getRegisteredCapital(),
                    enterprise.getEstablishDate(),
                    enterprise.getLegalRepresentative(),
                    enterprise.getContactPhone(),
                };
                excelUtil.addRow(i + 1, row);
            }

            excelUtil.exportExcel(response, "企业查询结果.xls");
        } catch (Exception e) {
            log.error("导出企业查询结果失败", e);
            throw new RuntimeException("导出企业查询结果失败：" + e.getMessage());
        }
    }

    @Override
    public void exportEnterpriseStatistics(String reportType, HttpServletResponse response) {
        try {
            String fileName = "企业统计报表.xls";
            String[] headers = {"统计项", "数量", "占比"};

            ExcelUtil excelUtil = new ExcelUtil("企业统计报表", headers);

            List<Map<String, Object>> data = null;
            switch (reportType) {
                case "type":
                    data = getEnterpriseTypeDistribution();
                    fileName = "企业类型分布统计.xls";
                    break;
                case "region":
                    data = getEnterpriseRegionDistribution();
                    fileName = "企业地区分布统计.xls";
                    break;
                case "industry":
                    data = getEnterpriseIndustryDistribution();
                    fileName = "企业行业分布统计.xls";
                    break;
                case "status":
                    data = getEnterpriseStatusDistribution();
                    fileName = "企业状态分布统计.xls";
                    break;
                default:
                    data = getEnterpriseTypeDistribution();
                    break;
            }

            if (data != null) {
                for (int i = 0; i < data.size(); i++) {
                    Map<String, Object> item = data.get(i);
                    Object[] row = {
                        item.get("name"),
                        item.get("count"),
                        item.get("percentage") + "%"
                    };
                    excelUtil.addRow(i + 1, row);
                }
            }

            excelUtil.exportExcel(response, fileName);
        } catch (Exception e) {
            log.error("导出企业统计报表失败", e);
            throw new RuntimeException("导出企业统计报表失败：" + e.getMessage());
        }
    }

    @Override
    public void generateEnterpriseAnalysisReport(String reportType, String format, HttpServletResponse response) {
        try {
            String fileName = "企业分析报告." + format.toLowerCase();

            if ("excel".equalsIgnoreCase(format)) {
                String[] headers = {"分析项目", "分析结果", "说明"};
                ExcelUtil excelUtil = new ExcelUtil("企业分析报告", headers);

                Map<String, Object> analysis = null;
                switch (reportType) {
                    case "comprehensive":
                        analysis = getEnterpriseComprehensiveAnalysis();
                        fileName = "企业综合分析报告.xls";
                        break;
                    case "supervision":
                        analysis = getEnterpriseSupervisionOverview();
                        fileName = "企业监管概览报告.xls";
                        break;
                    case "risk":
                        analysis = getEnterpriseRiskAnalysis();
                        fileName = "企业风险分析报告.xls";
                        break;
                    default:
                        analysis = getEnterpriseComprehensiveAnalysis();
                        break;
                }

                // 简化实现，添加基本信息
                Object[] row1 = {"报告类型", reportType, "企业分析报告"};
                Object[] row2 = {"生成时间", new java.util.Date(), "报告生成时间"};
                Object[] row3 = {"数据状态", "正常", "数据获取状态"};

                excelUtil.addRow(1, row1);
                excelUtil.addRow(2, row2);
                excelUtil.addRow(3, row3);

                excelUtil.exportExcel(response, fileName);
            } else {
                // PDF格式暂不实现，返回提示
                throw new RuntimeException("PDF格式报告功能开发中");
            }
        } catch (Exception e) {
            log.error("生成企业分析报告失败", e);
            throw new RuntimeException("生成企业分析报告失败：" + e.getMessage());
        }
    }
}
