package com.huabo.cybermonitor.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.huabo.cybermonitor.entity.EnterpriseInfo;
import com.huabo.cybermonitor.vo.EnterpriseQueryVO;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 企业查询服务接口
 *
 * @author system
 * @since 2024-01-01
 */
public interface IEnterpriseQueryService {

    /**
     * 高级查询企业信息列表
     *
     * @param queryVO 查询条件
     * @return 企业信息列表
     */
    IPage<EnterpriseInfo> getEnterpriseAdvancedQuery(EnterpriseQueryVO queryVO);

    /**
     * 企业综合查询（支持模糊搜索）
     *
     * @param queryVO 查询条件
     * @return 企业信息列表
     */
    IPage<EnterpriseInfo> getEnterpriseComprehensiveQuery(EnterpriseQueryVO queryVO);

    /**
     * 根据关键词搜索企业
     *
     * @param keyword 关键词
     * @return 企业信息列表
     */
    List<EnterpriseInfo> searchEnterprisesByKeyword(String keyword);

    /**
     * 企业基础统计分析
     *
     * @return 统计信息
     */
    Map<String, Object> getEnterpriseBasicStatistics();

    /**
     * 企业类型分布统计
     *
     * @return 类型分布统计
     */
    List<Map<String, Object>> getEnterpriseTypeDistribution();

    /**
     * 企业地区分布统计
     *
     * @return 地区分布统计
     */
    List<Map<String, Object>> getEnterpriseRegionDistribution();

    /**
     * 企业行业分布统计
     *
     * @return 行业分布统计
     */
    List<Map<String, Object>> getEnterpriseIndustryDistribution();

    /**
     * 企业状态分布统计
     *
     * @return 状态分布统计
     */
    List<Map<String, Object>> getEnterpriseStatusDistribution();

    /**
     * 企业监管层级分布统计
     *
     * @return 监管层级分布统计
     */
    List<Map<String, Object>> getSupervisionLevelDistribution();

    /**
     * 企业注册资本分布统计
     *
     * @return 注册资本分布统计
     */
    List<Map<String, Object>> getRegisteredCapitalDistribution();

    /**
     * 企业成立时间分布统计
     *
     * @return 成立时间分布统计
     */
    List<Map<String, Object>> getEstablishDateDistribution();

    /**
     * 企业上市状态分布统计
     *
     * @return 上市状态分布统计
     */
    List<Map<String, Object>> getListingStatusDistribution();

    /**
     * 企业风险等级分布统计
     *
     * @return 风险等级分布统计
     */
    List<Map<String, Object>> getRiskLevelDistribution();

    /**
     * 国有控股企业统计
     *
     * @return 国有控股企业统计
     */
    Map<String, Object> getStateControlledStatistics();

    /**
     * 上市公司统计
     *
     * @return 上市公司统计
     */
    Map<String, Object> getListedCompanyStatistics();

    /**
     * 企业规模分析（按注册资本）
     *
     * @return 企业规模分析
     */
    List<Map<String, Object>> getEnterpriseScaleAnalysis();

    /**
     * 企业发展趋势分析（按成立时间）
     *
     * @return 企业发展趋势分析
     */
    List<Map<String, Object>> getEnterpriseDevelopmentTrend();

    /**
     * 重点监管企业列表
     *
     * @return 重点监管企业列表
     */
    List<EnterpriseInfo> getKeySupervisionEnterprises();

    /**
     * 高风险企业列表
     *
     * @return 高风险企业列表
     */
    List<EnterpriseInfo> getHighRiskEnterprises();

    /**
     * 新成立企业列表（最近一年）
     *
     * @return 新成立企业列表
     */
    List<EnterpriseInfo> getNewlyEstablishedEnterprises();

    /**
     * 大型企业列表（按注册资本）
     *
     * @param threshold 注册资本阈值
     * @return 大型企业列表
     */
    List<EnterpriseInfo> getLargeEnterprises(Double threshold);

    /**
     * 企业综合分析报告
     *
     * @return 综合分析报告
     */
    Map<String, Object> getEnterpriseComprehensiveAnalysis();

    /**
     * 企业监管概览
     *
     * @return 监管概览
     */
    Map<String, Object> getEnterpriseSupervisionOverview();

    /**
     * 企业风险分析
     *
     * @return 风险分析
     */
    Map<String, Object> getEnterpriseRiskAnalysis();

    /**
     * 导出企业查询结果
     *
     * @param queryVO  查询条件
     * @param response HTTP响应
     */
    void exportEnterpriseQuery(EnterpriseQueryVO queryVO, HttpServletResponse response);

    /**
     * 导出企业统计报表
     *
     * @param reportType 报表类型
     * @param response   HTTP响应
     */
    void exportEnterpriseStatistics(String reportType, HttpServletResponse response);

    /**
     * 生成企业分析报告
     *
     * @param reportType 报告类型
     * @param format     导出格式
     * @param response   HTTP响应
     */
    void generateEnterpriseAnalysisReport(String reportType, String format, HttpServletResponse response);
}
