package com.global.treasurer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.global.treasurer.entity.TblFundPlanDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 资金计划明细Mapper接口
 *
 * @author 华博云开发团队
 * @since 2025-01-19
 */
@Mapper
public interface FundPlanDetailMapper extends BaseMapper<TblFundPlanDetail> {

    /**
     * 分页查询资金计划明细列表
     *
     * @param params 查询参数
     * @return 资金计划明细列表
     */
    List<TblFundPlanDetail> selectDetailPage(Map<String, Object> params);

    /**
     * 统计资金计划明细数量
     *
     * @param params 查询参数
     * @return 数量
     */
    int countDetailList(Map<String, Object> params);

    /**
     * 根据计划ID查询明细列表
     *
     * @param planId 计划ID
     * @return 明细列表
     */
    List<TblFundPlanDetail> selectByPlanId(@Param("planId") Long planId);

    /**
     * 查询明细汇总信息
     *
     * @param params 查询参数
     * @return 汇总数据
     */
    Map<String, Object> selectDetailSummary(Map<String, Object> params);

    /**
     * 差异分析：查询已执行明细的计划vs实际对比
     *
     * @param params 查询参数(planId)
     * @return 差异分析明细列表
     */
    List<TblFundPlanDetail> selectVarianceAnalysis(Map<String, Object> params);

    /**
     * 执行分析：按执行状态统计
     *
     * @param params 查询参数(planId)
     * @return 各状态数量统计
     */
    List<Map<String, Object>> selectExecutionStatusStats(Map<String, Object> params);

    /**
     * 执行分析：按业务类型统计
     *
     * @param params 查询参数(planId)
     * @return 各业务类型统计
     */
    List<Map<String, Object>> selectTypeStats(Map<String, Object> params);

    /**
     * 更新执行数据（执行明细）
     *
     * @param params 包含 detailId, executionStatus, actualDate, actualAmount
     * @return 影响行数
     */
    int updateExecutionData(Map<String, Object> params);

    /**
     * 调整明细（更新计划金额和日期）
     *
     * @param params 包含 detailId, plannedAmount, plannedDate, description
     * @return 影响行数
     */
    int adjustDetail(Map<String, Object> params);

    /**
     * 逻辑删除明细
     *
     * @param detailId 明细ID
     * @return 影响行数
     */
    int logicDeleteById(@Param("detailId") Long detailId);

    /**
     * 批量删除明细
     *
     * @param detailIds 明细ID列表
     * @return 影响行数
     */
    int batchDeleteByIds(@Param("detailIds") List<Long> detailIds);
}
