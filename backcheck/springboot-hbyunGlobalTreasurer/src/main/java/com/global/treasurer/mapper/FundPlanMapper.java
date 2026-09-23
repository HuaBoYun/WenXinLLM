package com.global.treasurer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.global.treasurer.entity.TblFundPlan;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 资金计划Mapper接口
 *
 * @author 华博云开发团队
 * @since 2025-01-19
 */
@Mapper
public interface FundPlanMapper extends BaseMapper<TblFundPlan> {

    /**
     * 分页查询资金计划列表
     *
     * @param params 查询参数
     * @return 资金计划列表
     */
    List<TblFundPlan> selectPlanPage(Map<String, Object> params);

    /**
     * 统计资金计划数量
     *
     * @param params 查询参数
     * @return 数量
     */
    int countPlanList(Map<String, Object> params);

    /**
     * 根据ID查询资金计划详情
     *
     * @param planId 计划ID
     * @return 资金计划
     */
    TblFundPlan selectPlanById(@Param("planId") Long planId);

    /**
     * 根据计划编号查询
     *
     * @param planNo 计划编号
     * @return 资金计划
     */
    TblFundPlan selectByPlanNo(@Param("planNo") String planNo);

    /**
     * 更新计划状态
     *
     * @param planId 计划ID
     * @param status 状态
     * @return 影响行数
     */
    int updatePlanStatus(@Param("planId") Long planId, @Param("status") String status);

    /**
     * 批量删除资金计划（逻辑删除）
     *
     * @param planIds 计划ID列表
     * @return 影响行数
     */
    int batchDeleteByIds(@Param("planIds") List<Long> planIds);

    /**
     * 查询计划汇总信息
     *
     * @param params 查询参数
     * @return 汇总数据
     */
    Map<String, Object> selectPlanSummary(Map<String, Object> params);
}
