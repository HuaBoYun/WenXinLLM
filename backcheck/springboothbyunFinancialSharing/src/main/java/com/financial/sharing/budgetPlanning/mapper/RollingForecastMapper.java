package com.financial.sharing.budgetPlanning.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.financial.sharing.budgetPlanning.dto.RollingForecastQueryParam;
import com.financial.sharing.budgetPlanning.entity.TblRollingForecast;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 滚动预测Mapper接口
 * 
 * @author hbyun
 * @date 2026-02-02
 */
public interface RollingForecastMapper extends BaseMapper<TblRollingForecast> {

    /**
     * 查询预测任务列表
     * 
     * @param param 查询参数
     * @return 预测任务列表
     */
    List<TblRollingForecast> selectForecastList(RollingForecastQueryParam param);

    /**
     * 查询预测任务（包含数据明细）
     * 
     * @param forecastId 预测任务ID
     * @return 预测任务
     */
    TblRollingForecast selectForecastWithData(@Param("forecastId") String forecastId);

    /**
     * 查询预测任务统计信息
     * 
     * @param orgId 组织ID
     * @return 统计信息
     */
    Map<String, Object> selectForecastStatistics(@Param("orgId") String orgId);

    /**
     * 生成预测任务编号
     * 
     * @return 预测任务编号
     */
    String generateForecastNo();
}

