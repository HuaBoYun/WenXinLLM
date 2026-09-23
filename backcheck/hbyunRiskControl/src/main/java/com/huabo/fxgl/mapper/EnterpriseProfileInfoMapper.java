package com.huabo.fxgl.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.fxgl.entity.EnterpriseProfileInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 企业基本信息Mapper接口
 * 
 * @author AI Agent
 * @since 2025-01-21
 */
@Mapper
@Repository
public interface EnterpriseProfileInfoMapper extends BaseMapper<EnterpriseProfileInfo> {

    /**
     * 获取企业基本信息及扩展信息
     * 
     * @param enterpriseId 企业ID
     * @return 企业信息
     */
    @Select("SELECT e.*, " +
            "       (SELECT COUNT(*) FROM TBL_PERSONNEL_INFO p WHERE p.ENTERPRISE_ID = e.ENTERPRISE_ID AND p.STATUS = 'ACTIVE') AS EMPLOYEE_COUNT, " +
            "       (SELECT MAX(ORG_LEVEL) FROM TBL_ORGANIZATION_STRUCTURE o WHERE o.ENTERPRISE_ID = e.ENTERPRISE_ID) AS ORGANIZATION_LEVELS " +
            "FROM TBL_ENTERPRISE_INFO e " +
            "WHERE e.ENTERPRISE_ID = #{enterpriseId} AND e.STATUS = 'ACTIVE'")
    EnterpriseProfileInfo getEnterpriseInfoWithExtended(@Param("enterpriseId") String enterpriseId);

    /**
     * 获取企业关键财务指标
     * 
     * @param enterpriseId 企业ID
     * @return 关键指标Map
     */
    @Select("SELECT " +
            "       COALESCE(SUM(CASE WHEN def.INDICATOR_CODE = 'TOTAL_ASSETS' THEN fid.INDICATOR_VALUE END), 0) AS TOTAL_ASSETS, " +
            "       COALESCE(SUM(CASE WHEN def.INDICATOR_CODE = 'ANNUAL_REVENUE' THEN fid.INDICATOR_VALUE END), 0) AS ANNUAL_REVENUE, " +
            "       COALESCE(SUM(CASE WHEN def.INDICATOR_CODE = 'NET_PROFIT' THEN fid.INDICATOR_VALUE END), 0) AS NET_PROFIT " +
            "FROM TBL_FINANCIAL_INDICATOR_DATA fid " +
            "INNER JOIN TBL_FINANCIAL_INDICATOR_DEF def ON fid.INDICATOR_ID = def.INDICATOR_ID " +
            "WHERE fid.ENTERPRISE_ID = #{enterpriseId} " +
            "  AND fid.STATUS = 'ACTIVE' " +
            "  AND def.INDICATOR_CODE IN ('TOTAL_ASSETS', 'ANNUAL_REVENUE', 'NET_PROFIT') " +
            "  AND fid.PERIOD_END_DATE = (SELECT MAX(PERIOD_END_DATE) FROM TBL_FINANCIAL_INDICATOR_DATA WHERE ENTERPRISE_ID = #{enterpriseId})")
    Map<String, BigDecimal> getEnterpriseKeyMetrics(@Param("enterpriseId") String enterpriseId);

    /**
     * 获取活跃企业列表
     * 
     * @return 企业列表
     */
    @Select("SELECT ENTERPRISE_ID, ENTERPRISE_NAME, ENTERPRISE_CODE, INDUSTRY_TYPE, ENTERPRISE_SCALE " +
            "FROM TBL_ENTERPRISE_INFO " +
            "WHERE STATUS = 'ACTIVE' " +
            "ORDER BY ENTERPRISE_NAME")
    List<EnterpriseProfileInfo> getActiveEnterpriseList();

    /**
     * 根据企业编码查询企业信息
     * 
     * @param enterpriseCode 企业编码
     * @return 企业信息
     */
    @Select("SELECT * FROM TBL_ENTERPRISE_INFO " +
            "WHERE ENTERPRISE_CODE = #{enterpriseCode} AND STATUS = 'ACTIVE'")
    EnterpriseProfileInfo getByEnterpriseCode(@Param("enterpriseCode") String enterpriseCode);

    /**
     * 检查企业是否存在
     *
     * @param enterpriseId 企业ID
     * @return 存在返回1，不存在返回0
     */
    @Select("SELECT COUNT(1) FROM TBL_ENTERPRISE_INFO " +
            "WHERE ENTERPRISE_ID = #{enterpriseId} AND STATUS = 'ACTIVE'")
    Integer checkEnterpriseExists(@Param("enterpriseId") String enterpriseId);

    /**
     * 获取子公司数量
     *
     * @param enterpriseId 企业ID
     * @return 子公司数量
     */
    @Select("SELECT COUNT(1) FROM TBL_ORGANIZATION_STRUCTURE " +
            "WHERE PARENT_ORG_ID = #{enterpriseId} AND STATUS = 'ACTIVE'")
    Integer getSubsidiaryCount(@Param("enterpriseId") String enterpriseId);

    /**
     * 按企业名称检查企业是否存在
     *
     * @param enterpriseName 企业名称
     * @return 存在返回1，不存在返回0
     */
    @Select("SELECT COUNT(1) FROM TBL_ENTERPRISE_INFO " +
            "WHERE ENTERPRISE_NAME = #{enterpriseName} AND STATUS = 'ACTIVE'")
    Integer checkEnterpriseExistsByName(@Param("enterpriseName") String enterpriseName);

    /**
     * 按企业名称获取企业ID
     *
     * @param enterpriseName 企业名称
     * @return 企业ID
     */
    @Select("SELECT ENTERPRISE_ID FROM TBL_ENTERPRISE_INFO " +
            "WHERE ENTERPRISE_NAME = #{enterpriseName} AND STATUS = 'ACTIVE'")
    String getEnterpriseIdByName(@Param("enterpriseName") String enterpriseName);

    /**
     * 按企业编码获取企业ID
     *
     * @param enterpriseCode 企业编码
     * @return 企业ID
     */
    @Select("SELECT ENTERPRISE_ID FROM TBL_ENTERPRISE_INFO " +
            "WHERE ENTERPRISE_CODE = #{enterpriseCode} AND STATUS = 'ACTIVE'")
    String getEnterpriseIdByCode(@Param("enterpriseCode") String enterpriseCode);
}
