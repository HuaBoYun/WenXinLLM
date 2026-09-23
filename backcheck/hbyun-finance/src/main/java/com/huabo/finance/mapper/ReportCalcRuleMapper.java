package com.huabo.finance.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.finance.entity.ReportCalcRule;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 报表计算规则Mapper接口
 * 
 * @author 华博云开发团队
 * @since 2025-01-21
 */
public interface ReportCalcRuleMapper extends BaseMapper<ReportCalcRule> {

    /**
     * 根据报表类型查询计算规则列表(按排序序号排序)
     */
    @Select("SELECT * FROM TBL_REPORT_CALC_RULE WHERE REPORT_TYPE = #{reportType} " +
            "AND IS_ENABLED = 1 ORDER BY SORT_ORDER ASC")
    List<ReportCalcRule> selectByReportType(@Param("reportType") String reportType);

    /**
     * 根据报表类型和项目编码查询规则
     */
    @Select("SELECT * FROM TBL_REPORT_CALC_RULE WHERE REPORT_TYPE = #{reportType} " +
            "AND ITEM_CODE = #{itemCode} AND IS_ENABLED = 1")
    ReportCalcRule selectByTypeAndCode(@Param("reportType") String reportType, @Param("itemCode") String itemCode);

    /**
     * 查询子项目列表
     */
    @Select("SELECT * FROM TBL_REPORT_CALC_RULE WHERE PARENT_CODE = #{parentCode} " +
            "AND IS_ENABLED = 1 ORDER BY SORT_ORDER ASC")
    List<ReportCalcRule> selectChildrenByParentCode(@Param("parentCode") String parentCode);
}

