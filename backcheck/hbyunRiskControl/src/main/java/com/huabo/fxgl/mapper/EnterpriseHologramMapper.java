package com.huabo.fxgl.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.fxgl.entity.*;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 企业全息画像Mapper接口
 * 
 * @author AI Agent
 * @since 2025-01-21
 */
@Mapper
@Repository
public interface EnterpriseHologramMapper extends BaseMapper<EnterpriseTagRel> {

    /**
     * 获取企业标签列表
     * 
     * @param enterpriseId 企业ID
     * @return 企业标签列表
     */
    @Select("SELECT r.REL_ID, r.ENTERPRISE_ID, r.TAG_ID, r.TAG_VALUE, r.EFFECTIVE_DATE, r.EXPIRE_DATE, r.IS_ACTIVE, " +
            "       d.TAG_CODE AS tagCode, d.TAG_NAME AS tagName, d.TAG_TYPE AS tagType, d.TAG_CATEGORY AS tagCategory, d.TAG_DESCRIPTION " +
            "FROM TBL_ENTERPRISE_TAG_REL r " +
            "LEFT JOIN TBL_ENTERPRISE_TAG_DEF d ON r.TAG_ID = d.TAG_ID " +
            "WHERE r.ENTERPRISE_ID = #{enterpriseId} " +
            "  AND r.IS_ACTIVE = 'Y' " +
            "  AND d.IS_ACTIVE = 'Y' " +
            "  AND (r.EXPIRE_DATE IS NULL OR r.EXPIRE_DATE >= CURRENT_DATE) " +
            "ORDER BY d.SORT_ORDER, d.TAG_NAME")
    List<EnterpriseTagRel> getEnterpriseTagsByEnterpriseId(@Param("enterpriseId") String enterpriseId);

    /**
     * 获取企业关键指标列表
     * 
     * @param enterpriseId 企业ID
     * @return 企业关键指标列表
     */
    @Select("SELECT METRIC_ID, ENTERPRISE_ID, METRIC_CODE, METRIC_NAME, METRIC_VALUE, " +
            "       METRIC_VALUE_TEXT, METRIC_UNIT, METRIC_TYPE, CALCULATION_DATE, DATA_SOURCE " +
            "FROM TBL_ENTERPRISE_KEY_METRICS " +
            "WHERE ENTERPRISE_ID = #{enterpriseId} " +
            "  AND IS_ACTIVE = 'Y' " +
            "  AND CALCULATION_DATE = (SELECT MAX(CALCULATION_DATE) FROM TBL_ENTERPRISE_KEY_METRICS WHERE ENTERPRISE_ID = #{enterpriseId}) " +
            "ORDER BY " +
            "  CASE METRIC_TYPE " +
            "    WHEN 'FINANCIAL' THEN 1 " +
            "    WHEN 'OPERATIONAL' THEN 2 " +
            "    WHEN 'HR' THEN 3 " +
            "    ELSE 4 " +
            "  END, " +
            "  METRIC_CODE")
    List<EnterpriseKeyMetrics> getEnterpriseKeyMetricsByEnterpriseId(@Param("enterpriseId") String enterpriseId);

    /**
     * 获取企业当前风险等级
     * 
     * @param enterpriseId 企业ID
     * @return 企业风险等级
     */
    @Select("SELECT RISK_LEVEL_ID, ENTERPRISE_ID, RISK_LEVEL, RISK_SCORE, EVALUATION_DATE, " +
            "       EVALUATION_PERIOD, RISK_FACTORS, EVALUATION_METHOD " +
            "FROM TBL_ENTERPRISE_RISK_LEVEL " +
            "WHERE ENTERPRISE_ID = #{enterpriseId} " +
            "  AND IS_CURRENT = 'Y' " +
            "ORDER BY EVALUATION_DATE DESC " +
            "LIMIT 1")
    EnterpriseRiskLevel getCurrentRiskLevelByEnterpriseId(@Param("enterpriseId") String enterpriseId);

    /**
     * 获取企业扩展信息
     * 
     * @param enterpriseId 企业ID
     * @return 企业扩展信息
     */
    @Select("SELECT EXTENDED_ID, ENTERPRISE_ID, REGISTERED_ADDRESS, OFFICE_ADDRESS, CONTACT_PHONE, " +
            "       CONTACT_FAX, EMAIL, WEBSITE, POSTAL_CODE, BUSINESS_LICENSE_NO, TAX_REGISTRATION_NO, " +
            "       ORGANIZATION_CODE, UNIFIED_SOCIAL_CREDIT_CODE, LISTING_STATUS, LISTING_EXCHANGE, STOCK_CODE " +
            "FROM TBL_ENTERPRISE_EXTENDED_INFO " +
            "WHERE ENTERPRISE_ID = #{enterpriseId}")
    EnterpriseExtendedInfo getEnterpriseExtendedInfoByEnterpriseId(@Param("enterpriseId") String enterpriseId);

    /**
     * 获取企业财务关键指标（用于企业信息卡片）
     * 
     * @param enterpriseId 企业ID
     * @return 财务关键指标
     */
    @Select("SELECT METRIC_CODE, METRIC_VALUE " +
            "FROM TBL_ENTERPRISE_KEY_METRICS " +
            "WHERE ENTERPRISE_ID = #{enterpriseId} " +
            "  AND IS_ACTIVE = 'Y' " +
            "  AND METRIC_TYPE = 'FINANCIAL' " +
            "  AND METRIC_CODE IN ('TOTAL_ASSETS', 'REVENUE') " +
            "  AND CALCULATION_DATE = (SELECT MAX(CALCULATION_DATE) FROM TBL_ENTERPRISE_KEY_METRICS WHERE ENTERPRISE_ID = #{enterpriseId})")
    @MapKey("metricCode")
    List<EnterpriseKeyMetrics> getFinancialKeyMetrics(@Param("enterpriseId") String enterpriseId);
}
