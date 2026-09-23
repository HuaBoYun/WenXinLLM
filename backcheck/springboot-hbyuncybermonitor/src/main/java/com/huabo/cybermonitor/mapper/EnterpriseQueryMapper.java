package com.huabo.cybermonitor.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.huabo.cybermonitor.entity.EnterpriseInfo;
import com.huabo.cybermonitor.vo.EnterpriseQueryVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 企业查询 Mapper 接口
 *
 * @author system
 * @since 2024-01-01
 */
@Mapper
public interface EnterpriseQueryMapper {

    /**
     * 高级查询企业信息列表
     *
     * @param page    分页参数
     * @param queryVO 查询条件
     * @return 企业信息列表
     */
    IPage<EnterpriseInfo> selectEnterpriseAdvancedQuery(Page<EnterpriseInfo> page, @Param("queryVO") EnterpriseQueryVO queryVO);

    /**
     * 企业基础统计分析
     *
     * @return 统计信息
     */
    Map<String, Object> selectEnterpriseBasicStatistics();

    /**
     * 企业类型分布统计
     *
     * @return 类型分布统计
     */
    List<Map<String, Object>> selectEnterpriseTypeDistribution();

    /**
     * 企业地区分布统计
     *
     * @return 地区分布统计
     */
    List<Map<String, Object>> selectEnterpriseRegionDistribution();

    /**
     * 企业行业分布统计
     *
     * @return 行业分布统计
     */
    List<Map<String, Object>> selectEnterpriseIndustryDistribution();

    /**
     * 企业状态分布统计
     *
     * @return 状态分布统计
     */
    List<Map<String, Object>> selectEnterpriseStatusDistribution();

    /**
     * 企业监管层级分布统计
     *
     * @return 监管层级分布统计
     */
    List<Map<String, Object>> selectSupervisionLevelDistribution();

    /**
     * 企业注册资本分布统计
     *
     * @return 注册资本分布统计
     */
    List<Map<String, Object>> selectRegisteredCapitalDistribution();

    /**
     * 企业成立时间分布统计
     *
     * @return 成立时间分布统计
     */
    List<Map<String, Object>> selectEstablishDateDistribution();

    /**
     * 企业上市状态分布统计
     *
     * @return 上市状态分布统计
     */
    List<Map<String, Object>> selectListingStatusDistribution();

    /**
     * 企业风险等级分布统计
     *
     * @return 风险等级分布统计
     */
    List<Map<String, Object>> selectRiskLevelDistribution();

    /**
     * 国有控股企业统计
     *
     * @return 国有控股企业统计
     */
    Map<String, Object> selectStateControlledStatistics();

    /**
     * 上市公司统计
     *
     * @return 上市公司统计
     */
    Map<String, Object> selectListedCompanyStatistics();

    /**
     * 企业规模分析（按注册资本）
     *
     * @return 企业规模分析
     */
    List<Map<String, Object>> selectEnterpriseScaleAnalysis();

    /**
     * 企业发展趋势分析（按成立时间）
     *
     * @return 企业发展趋势分析
     */
    List<Map<String, Object>> selectEnterpriseDevelopmentTrend();

    /**
     * 重点监管企业列表
     *
     * @return 重点监管企业列表
     */
    List<EnterpriseInfo> selectKeySupervisionEnterprises();

    /**
     * 高风险企业列表
     *
     * @return 高风险企业列表
     */
    List<EnterpriseInfo> selectHighRiskEnterprises();

    /**
     * 新成立企业列表（最近一年）
     *
     * @return 新成立企业列表
     */
    List<EnterpriseInfo> selectNewlyEstablishedEnterprises();

    /**
     * 大型企业列表（按注册资本）
     *
     * @param threshold 注册资本阈值
     * @return 大型企业列表
     */
    List<EnterpriseInfo> selectLargeEnterprises(@Param("threshold") Double threshold);

    /**
     * 根据关键词搜索企业
     *
     * @param keyword 关键词
     * @return 企业信息列表
     */
    List<EnterpriseInfo> selectEnterprisesByKeyword(@Param("keyword") String keyword);

    /**
     * 企业综合查询（支持模糊搜索）
     *
     * @param page    分页参数
     * @param queryVO 查询条件
     * @return 企业信息列表
     */
    IPage<EnterpriseInfo> selectEnterpriseComprehensiveQuery(Page<EnterpriseInfo> page, @Param("queryVO") EnterpriseQueryVO queryVO);

    /**
     * 导出企业查询结果
     *
     * @param queryVO 查询条件
     * @return 企业信息列表
     */
    List<EnterpriseInfo> selectEnterpriseQueryForExport(@Param("queryVO") EnterpriseQueryVO queryVO);
}
