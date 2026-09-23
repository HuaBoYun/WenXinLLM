package com.huabo.monitor.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * 大屏展示数据 Mapper
 */
@Mapper
public interface ScreenDisplayMapper {

    /**
     * 各部门评价项目数统计
     * @return 返回各部门的评价项目数量
     */
    @Select("SELECT COUNT(*) C,o.ORGNAME FROM TBL_ASSESS a left join TBL_ORGANIZATION o on a.LINKDEPTID = o.ORGID GROUP BY o.ORGNAME LIMIT 10")
    List<Map<String, Object>> getDepartmentProjectCount();

    /**
     * 控制有效性数据查询
     * @return 返回控制有效性数据列表
     */
    @Select("select e.ELEMENTCODE bm,o.ORGNAME dm,e.CONTROLTARGET mb,t.DESIGNPOINTVALIDITY sj,t.EXECUTEPOINTVALIDITY zx,t.TESTPOINTVALIDITY cs " +
            "from TBL_TESTTASK t INNER JOIN TBL_TESTELEMENT e on t.ELEMENTID = e.ELEMENTID INNER JOIN TBL_ORGANIZATION o on t.dutyorg = o.ORGID " +
            "where t.TESTPOINTVALIDITY IS NOT NULL AND T.DUTYORG IS NOT NULL and REGEXP_LIKE(t.dutyorg, '^[+-]?(\\d+(\\.\\d*)?|\\.\\d+)$')")
    List<Map<String, Object>> getControlEffectivenessData();

    /**
     * 各部门评价项目数占比统计
     * @return 返回各部门的评价项目数量
     */
    @Select("SELECT COUNT(*) C,o.ORGNAME FROM TBL_ASSESS a left join TBL_ORGANIZATION o on a.LINKDEPTID = o.ORGID GROUP BY o.ORGNAME")
    List<Map<String, Object>> getDepartmentProjectRatio();

    /**
     * 缺陷类型分布统计
     * @return 返回各缺陷类型的数量
     */
    @Select("SELECT t.BUGCRILEVEL l,count(1) c " +
            "FROM TBL_NBSJ_BUG b " +
            "LEFT JOIN TBL_NBSJ_BUG_CRITERION c on b.BUGID = c.BUGID " +
            "LEFT JOIN  TBL_NBSJ_BUGCRITERION t on c.BUGCRIID = t.BUGCRIID " +
            "group by t.BUGCRILEVEL")
    List<Map<String, Object>> getDefectTypeDistribution();

    /**
     * 各单位缺陷数量对比分析
     * @return 返回各单位的缺陷数量
     */
    @Select("SELECT o.ORGNAME n,count(1) c FROM TBL_NBSJ_BUG b LEFT JOIN TBL_ORGANIZATION o on b.BUGDEPARTMENT  = o.ORGID GROUP BY o.ORGNAME")
    List<Map<String, Object>> getDepartmentDefectComparison();

    /**
     * 本年缺陷项目趋势分析
     * @return 返回近12个月的缺陷数量趋势
     */
    @Select("WITH RECURSIVE months(month_date) AS ( " +
            "SELECT ADD_MONTHS(CURRENT_DATE, -11) AS month_date " +
            "UNION ALL " +
            "SELECT ADD_MONTHS(month_date, 1) FROM months WHERE ADD_MONTHS(month_date, 1) <= CURRENT_DATE " +
            ") " +
            "SELECT m.m, COALESCE(t.c, 0) AS c " +
            "FROM ( " +
            "SELECT CONCAT(YEAR(month_date), '-', LPAD(MONTH(month_date), 2, '0')) AS m, month_date " +
            "FROM months " +
            ") m " +
            "LEFT JOIN ( " +
            "SELECT CONCAT(YEAR(DISCOVERTIME), '-', LPAD(MONTH(DISCOVERTIME), 2, '0')) AS m, COUNT(1) AS c " +
            "FROM TBL_NBSJ_BUG " +
            "WHERE DISCOVERTIME >= ADD_MONTHS(CURRENT_DATE, -12) AND DISCOVERTIME <= CURRENT_DATE " +
            "GROUP BY YEAR(DISCOVERTIME), MONTH(DISCOVERTIME) " +
            ") t ON m.m = t.m " +
            "ORDER BY m.month_date")
    List<Map<String, Object>> getDefectTrendAnalysis();

    /**
     * 缺陷属性分布统计
     * @return 返回各缺陷属性的数量
     */
    @Select("SELECT count(1) AS c, B.BUGPROPERTY AS b " +
            "FROM TBL_NBSJ_BUG b " +
            "LEFT JOIN TBL_NBSJ_BUG_CRITERION c on b.BUGID = c.BUGID " +
            "WHERE c.BUGCRIID = 128183 " +
            "GROUP BY B.BUGPROPERTY")
    List<Map<String, Object>> getDefectPropertyDistribution();
}

