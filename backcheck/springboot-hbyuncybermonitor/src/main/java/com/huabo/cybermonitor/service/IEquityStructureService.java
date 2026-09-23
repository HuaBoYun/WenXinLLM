package com.huabo.cybermonitor.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.service.IService;
import com.huabo.cybermonitor.entity.EquityStructure;
import com.huabo.cybermonitor.util.PageResult;
import com.huabo.cybermonitor.vo.EquityStructureQueryVO;

/**
 * 股权结构服务接口
 *
 * @author system
 * @since 2024-01-01
 */
public interface IEquityStructureService extends IService<EquityStructure> {

    /**
     * 分页查询股权结构列表
     *
     * @param queryVO 查询参数
     * @return 分页结果
     */
    PageResult<EquityStructure> getEquityStructureList(EquityStructureQueryVO queryVO);

    /**
     * 根据ID获取股权结构详情
     *
     * @param equityId 股权结构ID
     * @return 股权结构详情
     */
    EquityStructure getEquityStructureById(String equityId);

    /**
     * 新增股权结构
     *
     * @param equityStructure 股权结构信息
     * @return 是否成功
     */
    boolean addEquityStructure(EquityStructure equityStructure);

    /**
     * 更新股权结构
     *
     * @param equityStructure 股权结构信息
     * @return 是否成功
     */
    boolean updateEquityStructure(EquityStructure equityStructure);

    /**
     * 删除股权结构
     *
     * @param equityId 股权结构ID
     * @return 是否成功
     */
    boolean deleteEquityStructure(String equityId);

    /**
     * 批量删除股权结构
     *
     * @param equityIds 股权结构ID列表
     * @return 是否成功
     */
    boolean batchDeleteEquityStructure(List<String> equityIds);

    /**
     * 根据被投资企业ID查询股权结构
     *
     * @param investeeEnterpriseId 被投资企业ID
     * @return 股权结构列表
     */
    List<EquityStructure> getEquityStructureByInvesteeEnterpriseId(String investeeEnterpriseId);

    /**
     * 根据投资方企业ID查询股权结构
     *
     * @param investorEnterpriseId 投资方企业ID
     * @return 股权结构列表
     */
    List<EquityStructure> getEquityStructureByInvestorEnterpriseId(String investorEnterpriseId);

    /**
     * 查询控股股权结构
     *
     * @param investeeEnterpriseId 被投资企业ID
     * @return 控股股权结构列表
     */
    List<EquityStructure> getControllingEquity(String investeeEnterpriseId);

    /**
     * 查询实际控制人股权结构
     *
     * @param investeeEnterpriseId 被投资企业ID
     * @return 实际控制人股权结构列表
     */
    List<EquityStructure> getActualControllerEquity(String investeeEnterpriseId);

    /**
     * 股权穿透查询（向上穿透）
     *
     * @param investeeEnterpriseId 被投资企业ID
     * @param maxLevel 最大穿透层级
     * @return 穿透股权结构列表
     */
    List<EquityStructure> getEquityPenetrationUp(String investeeEnterpriseId, Integer maxLevel);

    /**
     * 股权穿透查询（向下穿透）
     *
     * @param investorEnterpriseId 投资方企业ID
     * @param maxLevel 最大穿透层级
     * @return 穿透股权结构列表
     */
    List<EquityStructure> getEquityPenetrationDown(String investorEnterpriseId, Integer maxLevel);

    /**
     * 查询股权路径
     *
     * @param startEnterpriseId 起始企业ID
     * @param endEnterpriseId 终止企业ID
     * @return 股权路径列表
     */
    List<EquityStructure> getEquityPath(String startEnterpriseId, String endEnterpriseId);

    /**
     * 计算间接持股比例
     *
     * @param investorEnterpriseId 投资方企业ID
     * @param investeeEnterpriseId 被投资企业ID
     * @return 间接持股比例
     */
    BigDecimal calculateIndirectShareholdingRatio(String investorEnterpriseId, String investeeEnterpriseId);

    /**
     * 计算综合持股比例
     *
     * @param investorEnterpriseId 投资方企业ID
     * @param investeeEnterpriseId 被投资企业ID
     * @return 综合持股比例
     */
    BigDecimal calculateTotalShareholdingRatio(String investorEnterpriseId, String investeeEnterpriseId);

    /**
     * 查询质押股权
     *
     * @param investeeEnterpriseId 被投资企业ID
     * @return 质押股权列表
     */
    List<EquityStructure> getPledgedEquity(String investeeEnterpriseId);

    /**
     * 按投资方类型统计股权分布
     *
     * @param investeeEnterpriseId 被投资企业ID
     * @return 统计结果
     */
    List<Map<String, Object>> getEquityDistributionByInvestorType(String investeeEnterpriseId);

    /**
     * 按股权性质统计股权分布
     *
     * @param investeeEnterpriseId 被投资企业ID
     * @return 统计结果
     */
    List<Map<String, Object>> getEquityDistributionByNature(String investeeEnterpriseId);

    /**
     * 按投资层级统计股权分布
     *
     * @param investeeEnterpriseId 被投资企业ID
     * @return 统计结果
     */
    List<Map<String, Object>> getEquityDistributionByLevel(String investeeEnterpriseId);

    /**
     * 查询股权集中度
     *
     * @param investeeEnterpriseId 被投资企业ID
     * @return 股权集中度信息
     */
    Map<String, Object> getEquityConcentration(String investeeEnterpriseId);

    /**
     * 查询股权变动趋势
     *
     * @param investeeEnterpriseId 被投资企业ID
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 变动趋势数据
     */
    List<Map<String, Object>> getEquityChangeTrend(String investeeEnterpriseId, String startDate, String endDate);

    /**
     * 查询股权风险预警
     *
     * @param riskLevel 风险等级
     * @return 风险预警列表
     */
    List<EquityStructure> getEquityRiskWarning(String riskLevel);

    /**
     * 批量更新股权状态
     *
     * @param equityIds 股权ID列表
     * @param equityStatus 股权状态
     * @return 是否成功
     */
    boolean batchUpdateEquityStatus(List<String> equityIds, String equityStatus);

    /**
     * 批量更新间接持股比例
     *
     * @param equityStructures 股权结构列表
     * @return 是否成功
     */
    boolean batchUpdateIndirectShareholdingRatio(List<EquityStructure> equityStructures);

    /**
     * 删除过期股权记录
     *
     * @param days 过期天数
     * @return 删除数量
     */
    int deleteExpiredEquityRecords(Integer days);

    /**
     * 获取股权统计概览
     *
     * @return 统计概览
     */
    Map<String, Object> getEquityStatisticsOverview();

    /**
     * 获取投资方类型分布
     *
     * @return 类型分布统计
     */
    List<Map<String, Object>> getInvestorTypeDistribution();

    /**
     * 获取股权性质分布
     *
     * @return 性质分布统计
     */
    List<Map<String, Object>> getEquityNatureDistribution();

    /**
     * 获取投资层级分布
     *
     * @return 层级分布统计
     */
    List<Map<String, Object>> getInvestmentLevelDistribution();

    /**
     * 获取控股情况统计
     *
     * @return 控股情况统计
     */
    Map<String, Object> getControllingStatistics();

    /**
     * 获取质押情况统计
     *
     * @return 质押情况统计
     */
    Map<String, Object> getPledgeStatistics();

    /**
     * 导出股权结构列表
     *
     * @param queryVO 查询参数
     * @return 导出数据列表
     */
    List<Map<String, Object>> exportEquityStructureList(EquityStructureQueryVO queryVO);

    /**
     * 股权结构图谱数据
     *
     * @param enterpriseId 企业ID
     * @param direction 方向（up-向上，down-向下，both-双向）
     * @param maxLevel 最大层级
     * @return 图谱数据
     */
    Map<String, Object> getEquityStructureGraph(String enterpriseId, String direction, Integer maxLevel);

    /**
     * 股权穿透分析报告
     *
     * @param investeeEnterpriseId 被投资企业ID
     * @return 分析报告数据
     */
    Map<String, Object> getEquityPenetrationAnalysisReport(String investeeEnterpriseId);

    /**
     * 股权风险评估
     *
     * @param investeeEnterpriseId 被投资企业ID
     * @return 风险评估结果
     */
    Map<String, Object> getEquityRiskAssessment(String investeeEnterpriseId);

    /**
     * 获取投资方类型标签转换
     *
     * @param investorType 投资方类型
     * @return 类型标签
     */
    String getInvestorTypeLabel(String investorType);

    /**
     * 获取投资方式标签转换
     *
     * @param investmentMethod 投资方式
     * @return 方式标签
     */
    String getInvestmentMethodLabel(String investmentMethod);

    /**
     * 获取股权性质标签转换
     *
     * @param equityNature 股权性质
     * @return 性质标签
     */
    String getEquityNatureLabel(String equityNature);

    /**
     * 获取股权状态标签转换
     *
     * @param equityStatus 股权状态
     * @return 状态标签
     */
    String getEquityStatusLabel(String equityStatus);

    /**
     * 获取股权来源标签转换
     *
     * @param equitySource 股权来源
     * @return 来源标签
     */
    String getEquitySourceLabel(String equitySource);
}
