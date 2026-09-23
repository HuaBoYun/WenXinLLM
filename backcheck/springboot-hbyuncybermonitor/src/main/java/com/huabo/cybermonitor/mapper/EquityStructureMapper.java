package com.huabo.cybermonitor.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.cybermonitor.entity.EquityStructure;
import com.huabo.cybermonitor.vo.EquityStructureQueryVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 股权结构数据访问接口
 *
 * @author system
 * @since 2024-01-01
 */
@Mapper
public interface EquityStructureMapper extends BaseMapper<EquityStructure> {

    /**
     * 分页查询股权结构列表
     *
     * @param queryVO 查询参数
     * @return 股权结构列表
     */
    List<EquityStructure> selectEquityStructureList(@Param("queryVO") EquityStructureQueryVO queryVO);

    /**
     * 根据被投资企业ID查询股权结构
     *
     * @param investeeEnterpriseId 被投资企业ID
     * @return 股权结构列表
     */
    List<EquityStructure> selectByInvesteeEnterpriseId(@Param("investeeEnterpriseId") String investeeEnterpriseId);

    /**
     * 根据投资方企业ID查询股权结构
     *
     * @param investorEnterpriseId 投资方企业ID
     * @return 股权结构列表
     */
    List<EquityStructure> selectByInvestorEnterpriseId(@Param("investorEnterpriseId") String investorEnterpriseId);

    /**
     * 查询控股股权结构
     *
     * @param investeeEnterpriseId 被投资企业ID
     * @return 控股股权结构列表
     */
    List<EquityStructure> selectControllingEquity(@Param("investeeEnterpriseId") String investeeEnterpriseId);

    /**
     * 查询实际控制人股权结构
     *
     * @param investeeEnterpriseId 被投资企业ID
     * @return 实际控制人股权结构列表
     */
    List<EquityStructure> selectActualControllerEquity(@Param("investeeEnterpriseId") String investeeEnterpriseId);

    /**
     * 股权穿透查询（向上穿透）
     *
     * @param investeeEnterpriseId 被投资企业ID
     * @param maxLevel 最大穿透层级
     * @return 穿透股权结构列表
     */
    List<EquityStructure> selectEquityPenetrationUp(@Param("investeeEnterpriseId") String investeeEnterpriseId, 
                                                    @Param("maxLevel") Integer maxLevel);

    /**
     * 股权穿透查询（向下穿透）
     *
     * @param investorEnterpriseId 投资方企业ID
     * @param maxLevel 最大穿透层级
     * @return 穿透股权结构列表
     */
    List<EquityStructure> selectEquityPenetrationDown(@Param("investorEnterpriseId") String investorEnterpriseId, 
                                                      @Param("maxLevel") Integer maxLevel);

    /**
     * 查询股权路径
     *
     * @param startEnterpriseId 起始企业ID
     * @param endEnterpriseId 终止企业ID
     * @return 股权路径列表
     */
    List<EquityStructure> selectEquityPath(@Param("startEnterpriseId") String startEnterpriseId, 
                                          @Param("endEnterpriseId") String endEnterpriseId);

    /**
     * 计算间接持股比例
     *
     * @param investorEnterpriseId 投资方企业ID
     * @param investeeEnterpriseId 被投资企业ID
     * @return 间接持股比例
     */
    BigDecimal calculateIndirectShareholdingRatio(@Param("investorEnterpriseId") String investorEnterpriseId, 
                                                  @Param("investeeEnterpriseId") String investeeEnterpriseId);

    /**
     * 计算综合持股比例
     *
     * @param investorEnterpriseId 投资方企业ID
     * @param investeeEnterpriseId 被投资企业ID
     * @return 综合持股比例
     */
    BigDecimal calculateTotalShareholdingRatio(@Param("investorEnterpriseId") String investorEnterpriseId, 
                                               @Param("investeeEnterpriseId") String investeeEnterpriseId);

    /**
     * 查询质押股权
     *
     * @param investeeEnterpriseId 被投资企业ID
     * @return 质押股权列表
     */
    List<EquityStructure> selectPledgedEquity(@Param("investeeEnterpriseId") String investeeEnterpriseId);

    /**
     * 按投资方类型统计股权分布
     *
     * @param investeeEnterpriseId 被投资企业ID
     * @return 统计结果
     */
    List<Map<String, Object>> selectEquityDistributionByInvestorType(@Param("investeeEnterpriseId") String investeeEnterpriseId);

    /**
     * 按股权性质统计股权分布
     *
     * @param investeeEnterpriseId 被投资企业ID
     * @return 统计结果
     */
    List<Map<String, Object>> selectEquityDistributionByNature(@Param("investeeEnterpriseId") String investeeEnterpriseId);

    /**
     * 按投资层级统计股权分布
     *
     * @param investeeEnterpriseId 被投资企业ID
     * @return 统计结果
     */
    List<Map<String, Object>> selectEquityDistributionByLevel(@Param("investeeEnterpriseId") String investeeEnterpriseId);

    /**
     * 查询股权集中度
     *
     * @param investeeEnterpriseId 被投资企业ID
     * @return 股权集中度信息
     */
    Map<String, Object> selectEquityConcentration(@Param("investeeEnterpriseId") String investeeEnterpriseId);

    /**
     * 查询股权变动趋势
     *
     * @param investeeEnterpriseId 被投资企业ID
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 变动趋势数据
     */
    List<Map<String, Object>> selectEquityChangeTrend(@Param("investeeEnterpriseId") String investeeEnterpriseId,
                                                      @Param("startDate") String startDate,
                                                      @Param("endDate") String endDate);

    /**
     * 查询股权风险预警
     *
     * @param riskLevel 风险等级
     * @return 风险预警列表
     */
    List<EquityStructure> selectEquityRiskWarning(@Param("riskLevel") String riskLevel);

    /**
     * 批量更新股权状态
     *
     * @param equityIds 股权ID列表
     * @param equityStatus 股权状态
     * @return 更新数量
     */
    int batchUpdateEquityStatus(@Param("equityIds") List<String> equityIds, 
                               @Param("equityStatus") String equityStatus);

    /**
     * 批量更新间接持股比例
     *
     * @param equityStructures 股权结构列表
     * @return 更新数量
     */
    int batchUpdateIndirectShareholdingRatio(@Param("equityStructures") List<EquityStructure> equityStructures);

    /**
     * 删除过期股权记录
     *
     * @param days 过期天数
     * @return 删除数量
     */
    int deleteExpiredEquityRecords(@Param("days") Integer days);

    /**
     * 获取股权统计概览
     *
     * @return 统计概览
     */
    Map<String, Object> selectEquityStatisticsOverview();

    /**
     * 获取投资方类型分布
     *
     * @return 类型分布统计
     */
    List<Map<String, Object>> selectInvestorTypeDistribution();

    /**
     * 获取股权性质分布
     *
     * @return 性质分布统计
     */
    List<Map<String, Object>> selectEquityNatureDistribution();

    /**
     * 获取投资层级分布
     *
     * @return 层级分布统计
     */
    List<Map<String, Object>> selectInvestmentLevelDistribution();

    /**
     * 获取控股情况统计
     *
     * @return 控股情况统计
     */
    Map<String, Object> selectControllingStatistics();

    /**
     * 获取质押情况统计
     *
     * @return 质押情况统计
     */
    Map<String, Object> selectPledgeStatistics();

    /**
     * 导出股权结构列表
     *
     * @param queryVO 查询参数
     * @return 导出数据列表
     */
    List<Map<String, Object>> exportEquityStructureList(@Param("queryVO") EquityStructureQueryVO queryVO);
}
