package com.global.treasurer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.global.treasurer.entity.TblFinancingPlan;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 融资计划Mapper接口
 *
 * @author 华博云开发团队
 * @since 2025-12-30
 */
@Mapper
public interface FinancingPlanMapper extends BaseMapper<TblFinancingPlan> {

    /**
     * 分页查询融资计划列表
     *
     * @param params 查询参数
     * @return 融资计划列表
     */
    List<TblFinancingPlan> selectPlanList(Map<String, Object> params);

    /**
     * 根据ID查询融资计划详情
     *
     * @param planId 计划ID
     * @return 融资计划
     */
    TblFinancingPlan selectPlanById(@Param("planId") Long planId);

    /**
     * 根据计划编号查询
     *
     * @param planNo 计划编号
     * @return 融资计划
     */
    TblFinancingPlan selectByPlanNo(@Param("planNo") String planNo);

    /**
     * 统计融资计划数量
     *
     * @param params 查询参数
     * @return 数量
     */
    int countPlanList(Map<String, Object> params);

    /**
     * 更新计划状态
     *
     * @param planId 计划ID
     * @param status 状态
     * @return 影响行数
     */
    int updatePlanStatus(@Param("planId") Long planId, @Param("status") String status);

    /**
     * 批量删除融资计划（逻辑删除）
     *
     * @param planIds 计划ID列表
     * @return 影响行数
     */
    int batchDeleteByIds(@Param("planIds") List<Long> planIds);

    /**
     * 查询年度融资计划汇总
     *
     * @param planYear 计划年度
     * @param companyId 公司ID
     * @return 汇总数据
     */
    Map<String, Object> selectYearSummary(@Param("planYear") Integer planYear, @Param("companyId") Long companyId);

    /**
     * 按状态统计融资计划数量
     *
     * @param status 计划状态
     * @param planYear 计划年度
     * @param companyId 公司ID
     * @return 数量
     */
    int countByStatus(@Param("status") String status, @Param("planYear") Integer planYear, @Param("companyId") Long companyId);

    /**
     * 查询月度融资计划趋势
     *
     * @param planYear 计划年度
     * @return 月度趋势数据
     */
    List<Map<String, Object>> selectMonthlyTrend(@Param("planYear") Integer planYear, @Param("months") Integer months);
}

