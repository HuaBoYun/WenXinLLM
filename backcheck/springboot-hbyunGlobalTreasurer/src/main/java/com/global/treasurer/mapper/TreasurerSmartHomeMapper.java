package com.global.treasurer.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 全球司库首页Mapper
 * 提供首页数据统计相关SQL查询
 *
 * @author 华博云开发团队
 * @since 2025-07-01
 */
@Mapper
public interface TreasurerSmartHomeMapper {

    // ========== KPI统计 ==========

    /** 获取资金归集率 */
    Double getFundConcentrationRate(@Param("orgid") String orgid, @Param("year") Integer year);

    /** 获取融资余额（亿） */
    Double getFinancingBalance(@Param("orgid") String orgid, @Param("year") Integer year);

    /** 获取投资收益率 */
    Double getInvestReturnRate(@Param("orgid") String orgid, @Param("year") Integer year);

    /** 获取票据到期额（亿） */
    Double getBillMaturityAmount(@Param("orgid") String orgid, @Param("year") Integer year);

    /** 获取风险评分 */
    Double getRiskScore(@Param("orgid") String orgid, @Param("year") Integer year);

    /** 获取结算完成率 */
    Double getSettlementRate(@Param("orgid") String orgid, @Param("year") Integer year);

    // ========== 欢迎区统计 ==========

    /** 获取资金归集总额（亿） */
    Double getTotalFundConcentration(@Param("orgid") String orgid, @Param("year") Integer year);

    /** 获取投资规模（亿） */
    Double getTotalInvestScale(@Param("orgid") String orgid, @Param("year") Integer year);

    /** 获取票据规模（亿） */
    Double getTotalBillScale(@Param("orgid") String orgid, @Param("year") Integer year);

    // ========== 现金流统计 ==========

    /** 获取现金流入（亿） */
    Double getCashInflow(@Param("orgid") String orgid, @Param("year") Integer year, @Param("month") Integer month);

    /** 获取现金流出（亿） */
    Double getCashOutflow(@Param("orgid") String orgid, @Param("year") Integer year, @Param("month") Integer month);

    // ========== 融资进度 ==========

    /** 获取融资计划完成率 */
    Double getFinancingPlanRate(@Param("orgid") String orgid, @Param("year") Integer year);

    /** 获取还款执行率 */
    Double getFinancingRepayRate(@Param("orgid") String orgid, @Param("year") Integer year);

    /** 获取融资成本控制率 */
    Double getFinancingCostRate(@Param("orgid") String orgid, @Param("year") Integer year);
}
