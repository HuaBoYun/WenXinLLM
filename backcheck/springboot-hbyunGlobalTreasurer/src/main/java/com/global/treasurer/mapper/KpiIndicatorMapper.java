package com.global.treasurer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.global.treasurer.entity.KpiIndicator;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * KPI指标Mapper接口
 *
 * @author 华博云开发团队
 * @since 2025-01-12
 */
@Mapper
public interface KpiIndicatorMapper extends BaseMapper<KpiIndicator> {

    /**
     * 根据条件查询KPI指标列表(支持动态条件)
     *
     * @param kpiCode KPI编码(模糊查询)
     * @param kpiName KPI名称(模糊查询)
     * @param kpiCategory KPI分类(精确查询)
     * @param kpiType KPI类型(精确查询)
     * @param kpiStatus KPI状态(精确查询)
     * @param orgId 组织ID(精确查询)
     * @return KPI指标列表
     */
    List<KpiIndicator> selectByCondition(@Param("kpiCode") String kpiCode,
                                         @Param("kpiName") String kpiName,
                                         @Param("kpiCategory") String kpiCategory,
                                         @Param("kpiType") String kpiType,
                                         @Param("kpiStatus") String kpiStatus,
                                         @Param("orgId") Long orgId);
}
