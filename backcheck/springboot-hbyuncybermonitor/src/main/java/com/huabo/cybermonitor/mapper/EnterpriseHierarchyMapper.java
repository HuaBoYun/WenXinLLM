package com.huabo.cybermonitor.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.cybermonitor.entity.EnterpriseHierarchy;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * 企业层级关系表 Mapper 接口
 *
 * @author system
 * @since 2024-01-01
 */
@Mapper
public interface EnterpriseHierarchyMapper extends BaseMapper<EnterpriseHierarchy> {

    /**
     * 获取企业层级关系列表（包含企业名称）
     *
     * @param enterpriseId 企业ID
     * @return 层级关系列表
     */
    @Select("SELECT h.*, " +
            "p.ENTERPRISE_NAME as parentEnterpriseName, " +
            "c.ENTERPRISE_NAME as childEnterpriseName " +
            "FROM ENTERPRISE_HIERARCHY h " +
            "LEFT JOIN ENTERPRISE_INFO p ON h.PARENT_ENTERPRISE_ID = p.ENTERPRISE_ID " +
            "LEFT JOIN ENTERPRISE_INFO c ON h.CHILD_ENTERPRISE_ID = c.ENTERPRISE_ID " +
            "WHERE h.PARENT_ENTERPRISE_ID = #{enterpriseId} " +
            "OR h.CHILD_ENTERPRISE_ID = #{enterpriseId} " +
            "ORDER BY h.HIERARCHY_LEVEL, h.CREATE_TIME")
    List<EnterpriseHierarchy> selectHierarchyByEnterpriseId(@Param("enterpriseId") String enterpriseId);

    /**
     * 获取企业的所有上级企业（向上穿透）
     *
     * @param enterpriseId 企业ID
     * @param maxLevel 最大层级（防止无限递归）
     * @return 上级企业列表
     */
    @Select("WITH RECURSIVE parent_hierarchy AS ( " +
            "  SELECT h.PARENT_ENTERPRISE_ID, h.CHILD_ENTERPRISE_ID, h.HIERARCHY_LEVEL, " +
            "         e.ENTERPRISE_NAME, 1 as level " +
            "  FROM ENTERPRISE_HIERARCHY h " +
            "  JOIN ENTERPRISE_INFO e ON h.PARENT_ENTERPRISE_ID = e.ENTERPRISE_ID " +
            "  WHERE h.CHILD_ENTERPRISE_ID = #{enterpriseId} " +
            "  AND (h.EXPIRY_DATE IS NULL OR h.EXPIRY_DATE > CURRENT_DATE) " +
            "  UNION ALL " +
            "  SELECT h.PARENT_ENTERPRISE_ID, h.CHILD_ENTERPRISE_ID, h.HIERARCHY_LEVEL, " +
            "         e.ENTERPRISE_NAME, ph.level + 1 " +
            "  FROM ENTERPRISE_HIERARCHY h " +
            "  JOIN ENTERPRISE_INFO e ON h.PARENT_ENTERPRISE_ID = e.ENTERPRISE_ID " +
            "  JOIN parent_hierarchy ph ON h.CHILD_ENTERPRISE_ID = ph.PARENT_ENTERPRISE_ID " +
            "  WHERE ph.level < #{maxLevel} " +
            "  AND (h.EXPIRY_DATE IS NULL OR h.EXPIRY_DATE > CURRENT_DATE) " +
            ") " +
            "SELECT * FROM parent_hierarchy ORDER BY level")
    List<Map<String, Object>> selectParentHierarchy(@Param("enterpriseId") String enterpriseId, @Param("maxLevel") Integer maxLevel);

    /**
     * 获取企业的所有下级企业（向下穿透）
     *
     * @param enterpriseId 企业ID
     * @param maxLevel 最大层级（防止无限递归）
     * @return 下级企业列表
     */
    @Select("WITH RECURSIVE child_hierarchy AS ( " +
            "  SELECT h.PARENT_ENTERPRISE_ID, h.CHILD_ENTERPRISE_ID, h.HIERARCHY_LEVEL, " +
            "         e.ENTERPRISE_NAME, 1 as level " +
            "  FROM ENTERPRISE_HIERARCHY h " +
            "  JOIN ENTERPRISE_INFO e ON h.CHILD_ENTERPRISE_ID = e.ENTERPRISE_ID " +
            "  WHERE h.PARENT_ENTERPRISE_ID = #{enterpriseId} " +
            "  AND (h.EXPIRY_DATE IS NULL OR h.EXPIRY_DATE > CURRENT_DATE) " +
            "  UNION ALL " +
            "  SELECT h.PARENT_ENTERPRISE_ID, h.CHILD_ENTERPRISE_ID, h.HIERARCHY_LEVEL, " +
            "         e.ENTERPRISE_NAME, ch.level + 1 " +
            "  FROM ENTERPRISE_HIERARCHY h " +
            "  JOIN ENTERPRISE_INFO e ON h.CHILD_ENTERPRISE_ID = e.ENTERPRISE_ID " +
            "  JOIN child_hierarchy ch ON h.PARENT_ENTERPRISE_ID = ch.CHILD_ENTERPRISE_ID " +
            "  WHERE ch.level < #{maxLevel} " +
            "  AND (h.EXPIRY_DATE IS NULL OR h.EXPIRY_DATE > CURRENT_DATE) " +
            ") " +
            "SELECT * FROM child_hierarchy ORDER BY level")
    List<Map<String, Object>> selectChildHierarchy(@Param("enterpriseId") String enterpriseId, @Param("maxLevel") Integer maxLevel);

    /**
     * 获取企业层级关系图数据
     *
     * @param enterpriseId 企业ID
     * @return 层级关系图数据
     */
    @Select("SELECT h.HIERARCHY_ID, h.PARENT_ENTERPRISE_ID, h.CHILD_ENTERPRISE_ID, " +
            "h.RELATIONSHIP_TYPE, h.HIERARCHY_LEVEL, " +
            "p.ENTERPRISE_NAME as parentEnterpriseName, " +
            "c.ENTERPRISE_NAME as childEnterpriseName " +
            "FROM ENTERPRISE_HIERARCHY h " +
            "LEFT JOIN ENTERPRISE_INFO p ON h.PARENT_ENTERPRISE_ID = p.ENTERPRISE_ID " +
            "LEFT JOIN ENTERPRISE_INFO c ON h.CHILD_ENTERPRISE_ID = c.ENTERPRISE_ID " +
            "WHERE (h.PARENT_ENTERPRISE_ID = #{enterpriseId} OR h.CHILD_ENTERPRISE_ID = #{enterpriseId}) " +
            "AND (h.EXPIRY_DATE IS NULL OR h.EXPIRY_DATE > CURRENT_DATE) " +
            "ORDER BY h.HIERARCHY_LEVEL")
    List<Map<String, Object>> selectHierarchyChartData(@Param("enterpriseId") String enterpriseId);

    /**
     * 检查是否存在循环引用
     *
     * @param parentEnterpriseId 母公司ID
     * @param childEnterpriseId 子公司ID
     * @return 是否存在循环引用
     */
    @Select("WITH RECURSIVE hierarchy_check AS ( " +
            "  SELECT PARENT_ENTERPRISE_ID, CHILD_ENTERPRISE_ID, 1 as level " +
            "  FROM ENTERPRISE_HIERARCHY " +
            "  WHERE PARENT_ENTERPRISE_ID = #{childEnterpriseId} " +
            "  AND (EXPIRY_DATE IS NULL OR EXPIRY_DATE > CURRENT_DATE) " +
            "  UNION ALL " +
            "  SELECT h.PARENT_ENTERPRISE_ID, h.CHILD_ENTERPRISE_ID, hc.level + 1 " +
            "  FROM ENTERPRISE_HIERARCHY h " +
            "  JOIN hierarchy_check hc ON h.PARENT_ENTERPRISE_ID = hc.CHILD_ENTERPRISE_ID " +
            "  WHERE hc.level < 10 " +
            "  AND (h.EXPIRY_DATE IS NULL OR h.EXPIRY_DATE > CURRENT_DATE) " +
            ") " +
            "SELECT COUNT(*) FROM hierarchy_check WHERE CHILD_ENTERPRISE_ID = #{parentEnterpriseId}")
    Integer checkCircularReference(@Param("parentEnterpriseId") String parentEnterpriseId, @Param("childEnterpriseId") String childEnterpriseId);

    /**
     * 获取企业的直接子公司数量
     *
     * @param enterpriseId 企业ID
     * @return 子公司数量
     */
    @Select("SELECT COUNT(*) FROM ENTERPRISE_HIERARCHY " +
            "WHERE PARENT_ENTERPRISE_ID = #{enterpriseId} " +
            "AND (EXPIRY_DATE IS NULL OR EXPIRY_DATE > CURRENT_DATE)")
    Integer countDirectChildren(@Param("enterpriseId") String enterpriseId);

    /**
     * 获取企业的所有子公司数量（包括间接子公司）
     *
     * @param enterpriseId 企业ID
     * @return 所有子公司数量
     */
    @Select("WITH RECURSIVE all_children AS ( " +
            "  SELECT CHILD_ENTERPRISE_ID, 1 as level " +
            "  FROM ENTERPRISE_HIERARCHY " +
            "  WHERE PARENT_ENTERPRISE_ID = #{enterpriseId} " +
            "  AND (EXPIRY_DATE IS NULL OR EXPIRY_DATE > CURRENT_DATE) " +
            "  UNION ALL " +
            "  SELECT h.CHILD_ENTERPRISE_ID, ac.level + 1 " +
            "  FROM ENTERPRISE_HIERARCHY h " +
            "  JOIN all_children ac ON h.PARENT_ENTERPRISE_ID = ac.CHILD_ENTERPRISE_ID " +
            "  WHERE ac.level < 10 " +
            "  AND (h.EXPIRY_DATE IS NULL OR h.EXPIRY_DATE > CURRENT_DATE) " +
            ") " +
            "SELECT COUNT(DISTINCT CHILD_ENTERPRISE_ID) FROM all_children")
    Integer countAllChildren(@Param("enterpriseId") String enterpriseId);

    /**
     * 批量插入层级关系
     *
     * @param hierarchyList 层级关系列表
     * @return 插入数量
     */
    int batchInsert(@Param("list") List<EnterpriseHierarchy> hierarchyList);
}
