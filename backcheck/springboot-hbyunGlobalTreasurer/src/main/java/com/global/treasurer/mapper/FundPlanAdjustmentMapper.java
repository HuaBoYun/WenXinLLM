package com.global.treasurer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.global.treasurer.entity.TblFundPlanAdjustment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 资金计划调整Mapper接口
 *
 * @author 华博云开发团队
 * @since 2025-01-19
 */
@Mapper
public interface FundPlanAdjustmentMapper extends BaseMapper<TblFundPlanAdjustment> {

    /**
     * 分页查询资金计划调整列表
     *
     * @param params 查询参数
     * @return 资金计划调整列表
     */
    List<TblFundPlanAdjustment> selectAdjustmentPage(Map<String, Object> params);

    /**
     * 统计调整数量
     *
     * @param params 查询参数
     * @return 数量
     */
    int countAdjustmentList(Map<String, Object> params);

    /**
     * 查询调整汇总信息
     *
     * @param params 查询参数
     * @return 汇总数据
     */
    Map<String, Object> selectAdjustmentSummary(Map<String, Object> params);
}
