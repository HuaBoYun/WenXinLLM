package com.financial.sharing.oracle.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.financial.sharing.oracle.entity.ProductCostEntity;
import com.financial.sharing.vo.param.ProductCostQueryParam;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 产品成本Mapper接口
 * 
 * @author Financial Sharing System
 * @since 2024-12-19
 */
public interface ProductCostMapper extends BaseMapper<ProductCostEntity> {

    /**
     * 分页查询产品成本列表
     */
    IPage<Map<String, Object>> selectProductCostPage(Page<?> page, @Param("param") ProductCostQueryParam param);

    /**
     * 查询成本统计概览
     */
    Map<String, Object> selectCostStatisticsOverview(@Param("param") ProductCostQueryParam param);

    /**
     * 查询成本分布统计
     */
    List<Map<String, Object>> selectCostDistributionStatistics(@Param("param") ProductCostQueryParam param);

    /**
     * 查询成本变化趋势
     */
    List<Map<String, Object>> selectCostChangeTrend(@Param("param") ProductCostQueryParam param);

    /**
     * 查询产品成本详情
     */
    Map<String, Object> selectProductCostDetail(@Param("costingId") Long costingId);

    /**
     * 检查产品成本是否存在
     */
    int checkProductCostExists(@Param("productId") Long productId, @Param("costingPeriod") String costingPeriod,
                               @Param("costingId") Long costingId, @Param("bookId") Long bookId, @Param("tenantId") Long tenantId);

    /**
     * 分页查询产品信息列表（基于 T_PRODUCT_COST 去重派生）
     */
    IPage<Map<String, Object>> selectProductInfoPage(Page<?> page, @Param("param") ProductCostQueryParam param);

    /**
     * 成本构成分析（按产品聚合：直接材料/直接人工/制造费用 + 占比）
     */
    List<Map<String, Object>> selectCostCompositionAnalysis(@Param("param") ProductCostQueryParam param);

    /**
     * 成本预警列表（总成本超过整体均值 1.2 倍的产品视为高成本预警）
     */
    List<Map<String, Object>> selectCostAlerts(@Param("param") ProductCostQueryParam param);

    /**
     * 成本控制效果（汇总指标：控制率、节约金额等）
     */
    Map<String, Object> selectCostControlEffectiveness(@Param("param") ProductCostQueryParam param);

    /**
     * 预算执行情况（按产品/期间汇总实际成本与预算）
     */
    List<Map<String, Object>> selectBudgetExecution(@Param("param") ProductCostQueryParam param);

    /**
     * 成本报告分页列表（基于独立 T_COST_REPORT 表）
     */
    IPage<Map<String, Object>> selectCostReportPage(Page<?> page, @Param("param") ProductCostQueryParam param);

    /**
     * 查询全部启用的成本预警规则（来自 T_COST_ALERT_RULE）
     */
    List<Map<String, Object>> selectAlertRules(@Param("param") ProductCostQueryParam param);

    /**
     * 查询成本控制措施列表（来自 T_COST_CONTROL_MEASURE）
     */
    List<Map<String, Object>> selectControlMeasures(@Param("param") ProductCostQueryParam param);

    /**
     * 查询成本异常列表（来自 T_COST_EXCEPTION）
     */
    List<Map<String, Object>> selectCostExceptions(@Param("param") ProductCostQueryParam param);

    /**
     * 查询成本控制效果汇总（基于 T_BUDGET_EXECUTION 聚合）
     */
    Map<String, Object> selectControlEffectivenessSummary(@Param("param") ProductCostQueryParam param);
}

