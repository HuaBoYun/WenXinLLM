package com.huabo.fxgl.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.fxgl.entity.FinancialIndicatorData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 财务指标数据Mapper接口
 * 
 * @author AI Agent
 * @since 2025-01-21
 */
@Mapper
@Repository
public interface FinancialIndicatorDataMapper extends BaseMapper<FinancialIndicatorData> {

    /**
     * 获取企业财务雷达图数据
     * 
     * @param enterpriseId 企业ID
     * @param periodDate 期间日期
     * @return 雷达图数据
     */
    @Select("SELECT " +
            "       def.INDICATOR_TYPE, " +
            "       AVG(fid.INDICATOR_VALUE) AS AVG_VALUE, " +
            "       COUNT(*) AS INDICATOR_COUNT " +
            "FROM TBL_FINANCIAL_INDICATOR_DATA fid " +
            "INNER JOIN TBL_FINANCIAL_INDICATOR_DEF def ON fid.INDICATOR_ID = def.INDICATOR_ID " +
            "WHERE fid.ENTERPRISE_ID = #{enterpriseId} " +
            "  AND fid.STATUS = 'ACTIVE' " +
            "  AND def.STATUS = 'ACTIVE' " +
            "  AND (#{periodDate} IS NULL OR fid.PERIOD_END_DATE <= #{periodDate}) " +
            "  AND fid.PERIOD_END_DATE = (SELECT MAX(PERIOD_END_DATE) FROM TBL_FINANCIAL_INDICATOR_DATA WHERE ENTERPRISE_ID = #{enterpriseId}) " +
            "GROUP BY def.INDICATOR_TYPE " +
            "ORDER BY def.INDICATOR_TYPE")
    List<Map<String, Object>> getFinancialRadarData(@Param("enterpriseId") String enterpriseId, 
                                                    @Param("periodDate") String periodDate);

    /**
     * 获取关键财务指标数据
     * 
     * @param enterpriseId 企业ID
     * @param periodDate 期间日期
     * @return 关键指标列表
     */
    @Select("SELECT " +
            "       fid.*, " +
            "       def.INDICATOR_CODE, " +
            "       def.INDICATOR_NAME, " +
            "       def.INDICATOR_TYPE, " +
            "       def.UNIT " +
            "FROM TBL_FINANCIAL_INDICATOR_DATA fid " +
            "INNER JOIN TBL_FINANCIAL_INDICATOR_DEF def ON fid.INDICATOR_ID = def.INDICATOR_ID " +
            "WHERE fid.ENTERPRISE_ID = #{enterpriseId} " +
            "  AND fid.STATUS = 'ACTIVE' " +
            "  AND def.STATUS = 'ACTIVE' " +
            "  AND (#{periodDate} IS NULL OR fid.PERIOD_END_DATE <= #{periodDate}) " +
            "  AND fid.PERIOD_END_DATE = (SELECT MAX(PERIOD_END_DATE) FROM TBL_FINANCIAL_INDICATOR_DATA WHERE ENTERPRISE_ID = #{enterpriseId}) " +
            "  AND def.INDICATOR_CODE IN (" +
            "    'ROE', 'ROA', 'SALES_PROFIT_RATE', 'ASSET_LIABILITY_RATIO', 'CURRENT_RATIO', " +
            "    'NET_ASSET_RETURN_RATE', 'TOTAL_ASSET_RETURN_RATE', 'SALES_PROFIT_MARGIN', " +
            "    'SURPLUS_CASH_GUARANTEE_RATIO', 'COST_EXPENSE_PROFIT_RATE', 'CAPITAL_RETURN_RATE', " +
            "    'OPERATING_CASH_RATIO', 'TOTAL_ASSET_TURNOVER', 'ACCOUNTS_RECEIVABLE_TURNOVER', " +
            "    'THREE_FUNDS_BALANCE', 'CURRENT_ASSET_TURNOVER', 'TOTAL_ASSET_CASH_RECOVERY_RATE', " +
            "    'DEBT_TO_ASSET_RATIO', 'INTEREST_COVERAGE_RATIO', 'QUICK_RATIO', " +
            "    'CASH_CURRENT_LIABILITY_RATIO', 'INTEREST_BEARING_DEBT_RATIO', 'SALES_GROWTH_RATE', " +
            "    'CAPITAL_PRESERVATION_APPRECIATION_RATE', 'SALES_PROFIT_GROWTH_RATE', 'TOTAL_ASSET_GROWTH_RATE', " +
            "    'INVENTORY_TURNOVER', 'TWO_FUNDS_TO_CURRENT_ASSETS_RATIO', 'COST_EXPENSE_TO_REVENUE_RATIO', " +
            "    'ECONOMIC_VALUE_ADDED_RATE', 'EBITDA_RATE', 'CAPITAL_ACCUMULATION_RATE'" +
            "  ) " +
            "ORDER BY def.SORT_ORDER")
    List<FinancialIndicatorData> getKeyFinancialIndicators(@Param("enterpriseId") String enterpriseId, 
                                                          @Param("periodDate") String periodDate);

    /**
     * 获取指标趋势数据
     * 
     * @param enterpriseId 企业ID
     * @param indicatorCode 指标编码
     * @param months 月份数
     * @return 趋势数据
     */
    @Select("SELECT " +
            "       fid.INDICATOR_VALUE, " +
            "       fid.PERIOD_END_DATE, " +
            "       def.INDICATOR_NAME, " +
            "       def.UNIT " +
            "FROM TBL_FINANCIAL_INDICATOR_DATA fid " +
            "INNER JOIN TBL_FINANCIAL_INDICATOR_DEF def ON fid.INDICATOR_ID = def.INDICATOR_ID " +
            "WHERE fid.ENTERPRISE_ID = #{enterpriseId} " +
            "  AND def.INDICATOR_CODE = #{indicatorCode} " +
            "  AND fid.STATUS = 'ACTIVE' " +
            "  AND fid.PERIOD_END_DATE >= ADD_MONTHS(SYSDATE, -#{months}) " +
            "ORDER BY fid.PERIOD_END_DATE")
    List<Map<String, Object>> getIndicatorTrendData(@Param("enterpriseId") String enterpriseId, 
                                                    @Param("indicatorCode") String indicatorCode, 
                                                    @Param("months") Integer months);

    /**
     * 计算指标同比增长率
     * 
     * @param enterpriseId 企业ID
     * @param indicatorCode 指标编码
     * @return 同比增长率
     */
    @Select("SELECT " +
            "       CASE WHEN prev.INDICATOR_VALUE = 0 THEN NULL " +
            "            ELSE (curr.INDICATOR_VALUE - prev.INDICATOR_VALUE) / prev.INDICATOR_VALUE * 100 " +
            "       END AS YEAR_OVER_YEAR_GROWTH " +
            "FROM (SELECT INDICATOR_VALUE FROM TBL_FINANCIAL_INDICATOR_DATA fid " +
            "      INNER JOIN TBL_FINANCIAL_INDICATOR_DEF def ON fid.INDICATOR_ID = def.INDICATOR_ID " +
            "      WHERE fid.ENTERPRISE_ID = #{enterpriseId} AND def.INDICATOR_CODE = #{indicatorCode} " +
            "        AND fid.STATUS = 'ACTIVE' " +
            "        AND fid.PERIOD_END_DATE = (SELECT MAX(PERIOD_END_DATE) FROM TBL_FINANCIAL_INDICATOR_DATA WHERE ENTERPRISE_ID = #{enterpriseId})) curr, " +
            "     (SELECT INDICATOR_VALUE FROM TBL_FINANCIAL_INDICATOR_DATA fid " +
            "      INNER JOIN TBL_FINANCIAL_INDICATOR_DEF def ON fid.INDICATOR_ID = def.INDICATOR_ID " +
            "      WHERE fid.ENTERPRISE_ID = #{enterpriseId} AND def.INDICATOR_CODE = #{indicatorCode} " +
            "        AND fid.STATUS = 'ACTIVE' " +
            "        AND fid.PERIOD_END_DATE = ADD_MONTHS((SELECT MAX(PERIOD_END_DATE) FROM TBL_FINANCIAL_INDICATOR_DATA WHERE ENTERPRISE_ID = #{enterpriseId}), -12)) prev")
    BigDecimal calculateYearOverYearGrowth(@Param("enterpriseId") String enterpriseId, 
                                          @Param("indicatorCode") String indicatorCode);

    /**
     * 获取企业最新财务数据期间
     *
     * @param enterpriseId 企业ID
     * @return 最新期间日期
     */
    @Select("SELECT MAX(PERIOD_END_DATE) FROM TBL_FINANCIAL_INDICATOR_DATA " +
            "WHERE ENTERPRISE_ID = #{enterpriseId} AND STATUS = 'ACTIVE'")
    String getLatestPeriodDate(@Param("enterpriseId") String enterpriseId);

    /**
     * 获取企业最新的所有财务指标数据
     *
     * @param enterpriseId 企业ID
     * @return 最新指标数据列表
     */
    @Select("SELECT " +
            "       fid.*, " +
            "       def.INDICATOR_CODE, " +
            "       def.INDICATOR_NAME, " +
            "       def.INDICATOR_TYPE, " +
            "       def.UNIT " +
            "FROM TBL_FINANCIAL_INDICATOR_DATA fid " +
            "INNER JOIN TBL_FINANCIAL_INDICATOR_DEF def ON fid.INDICATOR_ID = def.INDICATOR_ID " +
            "WHERE fid.ENTERPRISE_ID = #{enterpriseId} " +
            "  AND fid.STATUS = 'ACTIVE' " +
            "  AND def.STATUS = 'ACTIVE' " +
            "  AND fid.PERIOD_END_DATE = (SELECT MAX(PERIOD_END_DATE) FROM TBL_FINANCIAL_INDICATOR_DATA WHERE ENTERPRISE_ID = #{enterpriseId}) " +
            "ORDER BY def.SORT_ORDER")
    List<FinancialIndicatorData> getLatestIndicatorsByEnterprise(@Param("enterpriseId") String enterpriseId);
}
