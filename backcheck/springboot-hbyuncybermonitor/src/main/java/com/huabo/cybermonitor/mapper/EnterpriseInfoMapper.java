package com.huabo.cybermonitor.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.huabo.cybermonitor.entity.EnterpriseInfo;
import com.huabo.cybermonitor.vo.EnterpriseInfoQueryVO;
import com.huabo.cybermonitor.vo.EnterpriseStatisticsVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * 企业基础信息表 Mapper 接口
 *
 * @author system
 * @since 2024-01-01
 */
@Mapper
public interface EnterpriseInfoMapper extends BaseMapper<EnterpriseInfo> {

    /**
     * 分页查询企业列表
     *
     * @param page 分页参数
     * @param queryVO 查询条件
     * @return 企业列表
     */
    IPage<EnterpriseInfo> selectEnterpriseList(Page<EnterpriseInfo> page, @Param("query") EnterpriseInfoQueryVO queryVO);

    /**
     * 根据企业ID获取企业详情（包含母公司和子公司信息）
     *
     * @param enterpriseId 企业ID
     * @return 企业详情
     */
    Map<String, Object> selectEnterpriseDetail(@Param("enterpriseId") String enterpriseId);

    /**
     * 获取母公司列表（用于下拉选择）
     *
     * @return 母公司列表
     */
    @Select("SELECT ENTERPRISE_ID as enterpriseId, ENTERPRISE_NAME as enterpriseName " +
            "FROM ENTERPRISE_INFO " +
            "WHERE ENTERPRISE_STATUS = 'NORMAL' " +
            "ORDER BY ENTERPRISE_NAME")
    List<Map<String, Object>> selectParentEnterpriseList();

    /**
     * 根据统一社会信用代码查询企业（用于验证重复）
     *
     * @param creditCode 统一社会信用代码
     * @param excludeId 排除的企业ID（编辑时使用）
     * @return 企业信息
     */
    @Select("<script>" +
            "SELECT * FROM ENTERPRISE_INFO " +
            "WHERE CREDIT_CODE = #{creditCode} " +
            "<if test='excludeId != null and excludeId != \"\"'>" +
            "AND ENTERPRISE_ID != #{excludeId} " +
            "</if>" +
            "</script>")
    EnterpriseInfo selectByCreditCode(@Param("creditCode") String creditCode, @Param("excludeId") String excludeId);

    /**
     * 根据企业名称查询企业（用于验证重复）
     *
     * @param enterpriseName 企业名称
     * @param excludeId 排除的企业ID（编辑时使用）
     * @return 企业信息
     */
    @Select("<script>" +
            "SELECT * FROM ENTERPRISE_INFO " +
            "WHERE ENTERPRISE_NAME = #{enterpriseName} " +
            "<if test='excludeId != null and excludeId != \"\"'>" +
            "AND ENTERPRISE_ID != #{excludeId} " +
            "</if>" +
            "</script>")
    EnterpriseInfo selectByEnterpriseName(@Param("enterpriseName") String enterpriseName, @Param("excludeId") String excludeId);

    /**
     * 获取企业统计数据
     *
     * @return 统计数据
     */
    @Select("SELECT " +
            "COUNT(*) as totalCount, " +
            "COUNT(CASE WHEN ENTERPRISE_TYPE = 'STATE_OWNED' THEN 1 END) as stateOwnedCount, " +
            "COUNT(CASE WHEN ENTERPRISE_TYPE = 'STATE_HOLDING' THEN 1 END) as stateHoldingCount, " +
            "COUNT(CASE WHEN ENTERPRISE_TYPE = 'STATE_PARTICIPATING' THEN 1 END) as stateParticipatingCount, " +
            "COUNT(CASE WHEN LISTING_STATUS = 'LISTED' THEN 1 END) as listedCount, " +
            "COUNT(CASE WHEN ENTERPRISE_STATUS = 'NORMAL' THEN 1 END) as normalCount " +
            "FROM ENTERPRISE_INFO")
    EnterpriseStatisticsVO selectEnterpriseStatistics();

    /**
     * 获取企业类型分布
     *
     * @return 类型分布
     */
    @Select("SELECT ENTERPRISE_TYPE as type, COUNT(*) as count " +
            "FROM ENTERPRISE_INFO " +
            "GROUP BY ENTERPRISE_TYPE")
    List<Map<String, Object>> selectEnterpriseTypeDistribution();

    /**
     * 获取企业地区分布
     *
     * @return 地区分布
     */
    @Select("SELECT REGION_CODE as region, COUNT(*) as count " +
            "FROM ENTERPRISE_INFO " +
            "WHERE REGION_CODE IS NOT NULL " +
            "GROUP BY REGION_CODE")
    List<Map<String, Object>> selectEnterpriseRegionDistribution();

    /**
     * 获取企业行业分布
     *
     * @return 行业分布
     */
    @Select("SELECT INDUSTRY_CODE as industry, COUNT(*) as count " +
            "FROM ENTERPRISE_INFO " +
            "WHERE INDUSTRY_CODE IS NOT NULL " +
            "GROUP BY INDUSTRY_CODE")
    List<Map<String, Object>> selectEnterpriseIndustryDistribution();

    /**
     * 获取子公司列表
     *
     * @param parentEnterpriseId 母公司ID
     * @return 子公司列表
     */
    @Select("SELECT * FROM ENTERPRISE_INFO " +
            "WHERE PARENT_ENTERPRISE_ID = #{parentEnterpriseId} " +
            "ORDER BY ENTERPRISE_NAME")
    List<EnterpriseInfo> selectChildEnterprises(@Param("parentEnterpriseId") String parentEnterpriseId);

    /**
     * 批量插入企业信息
     *
     * @param enterpriseList 企业列表
     * @return 插入数量
     */
    int batchInsert(@Param("list") List<EnterpriseInfo> enterpriseList);

    /**
     * 根据条件导出企业列表
     *
     * @param queryVO 查询条件
     * @return 企业列表
     */
    List<EnterpriseInfo> selectEnterpriseListForExport(@Param("query") EnterpriseInfoQueryVO queryVO);
}
