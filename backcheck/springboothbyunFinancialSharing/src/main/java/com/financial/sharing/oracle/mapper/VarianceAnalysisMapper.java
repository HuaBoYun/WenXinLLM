package com.financial.sharing.oracle.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.financial.sharing.oracle.entity.VarianceAnalysisEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 差异分析Mapper接口
 *
 * @author Financial Sharing System
 * @since 2024-12-29
 */
public interface VarianceAnalysisMapper extends BaseMapper<VarianceAnalysisEntity> {

    /**
     * 分页查询差异分析列表
     *
     * @param bookId 账套ID
     * @param tenantId 租户ID
     * @param analysisPeriod 分析期间
     * @param budgetId 预算ID
     * @param varianceType 差异类型
     * @param offset 偏移量
     * @param pageSize 每页数量
     * @return 分析列表
     */
    List<Map<String, Object>> selectAnalysisListWithPagination(
            @Param("bookId") String bookId,
            @Param("tenantId") String tenantId,
            @Param("analysisPeriod") String analysisPeriod,
            @Param("budgetId") String budgetId,
            @Param("varianceType") String varianceType,
            @Param("offset") Integer offset,
            @Param("pageSize") Integer pageSize
    );

    /**
     * 查询总记录数
     *
     * @param bookId 账套ID
     * @param tenantId 租户ID
     * @param analysisPeriod 分析期间
     * @param budgetId 预算ID
     * @param varianceType 差异类型
     * @return 总记录数
     */
    int countAnalysisList(
            @Param("bookId") String bookId,
            @Param("tenantId") String tenantId,
            @Param("analysisPeriod") String analysisPeriod,
            @Param("budgetId") String budgetId,
            @Param("varianceType") String varianceType
    );

    /**
     * 根据分析编号查询分析
     *
     * @param analysisNo 分析编号
     * @param bookId 账套ID
     * @param tenantId 租户ID
     * @return 分析信息
     */
    Map<String, Object> selectByAnalysisNo(
            @Param("analysisNo") String analysisNo,
            @Param("bookId") String bookId,
            @Param("tenantId") String tenantId
    );

    /**
     * 获取差异分析统计
     *
     * @param analysisPeriod 分析期间
     * @param bookId 账套ID
     * @param tenantId 租户ID
     * @return 统计数据
     */
    Map<String, Object> selectVarianceStatistics(
            @Param("analysisPeriod") String analysisPeriod,
            @Param("bookId") String bookId,
            @Param("tenantId") String tenantId
    );

    /**
     * 获取差异趋势数据
     *
     * @param startPeriod 开始期间
     * @param endPeriod 结束期间
     * @param bookId 账套ID
     * @param tenantId 租户ID
     * @return 趋势数据
     */
    List<Map<String, Object>> selectVarianceTrend(
            @Param("startPeriod") String startPeriod,
            @Param("endPeriod") String endPeriod,
            @Param("bookId") String bookId,
            @Param("tenantId") String tenantId
    );

    /**
     * 拉取产品成本明细，用于实时计算差异分析。
     * 数据源是 T_PRODUCT_COST（产品成本核算表），
     * 一条记录代表"某期间-某成本中心-某产品"的一笔成本核算。
     *
     * 入参为可选过滤项（null 表示不过滤）：
     * @param startPeriod 起始期间（含），格式 yyyy-MM
     * @param endPeriod 结束期间（含），格式 yyyy-MM
     * @param costCenterIds 成本中心 ID 列表（Long 字符串），空集表示全部
     * @param productTypes 产品类型枚举列表（RAW_MATERIAL/SEMI_PRODUCT/...），空集表示全部
     * @return 一行一个产品成本数据，含预算/实际拆分
     */
    List<Map<String, Object>> selectProductCostForVariance(
            @Param("startPeriod") String startPeriod,
            @Param("endPeriod") String endPeriod,
            @Param("costCenterIds") List<String> costCenterIds,
            @Param("productTypes") List<String> productTypes
    );
}

