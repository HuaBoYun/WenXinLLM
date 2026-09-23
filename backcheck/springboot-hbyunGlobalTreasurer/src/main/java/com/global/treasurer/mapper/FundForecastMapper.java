package com.global.treasurer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.global.treasurer.entity.TblFundForecast;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 资金预测Mapper接口
 *
 * @author 华博云开发团队
 * @since 2025-01-19
 */
@Mapper
public interface FundForecastMapper extends BaseMapper<TblFundForecast> {

    /**
     * 分页查询资金预测列表
     *
     * @param params 查询参数
     * @return 资金预测列表
     */
    List<TblFundForecast> selectForecastPage(Map<String, Object> params);

    /**
     * 统计预测数量
     *
     * @param params 查询参数
     * @return 数量
     */
    int countForecastList(Map<String, Object> params);

    /**
     * 查询预测汇总信息
     *
     * @param params 查询参数
     * @return 汇总数据
     */
    Map<String, Object> selectForecastSummary(Map<String, Object> params);

    /**
     * 查询各预测方法的准确率分析
     *
     * @param params 查询参数
     * @return 准确率分析列表
     */
    List<Map<String, Object>> selectAccuracyAnalysis(Map<String, Object> params);

    /**
     * 查询按月份的趋势分析
     *
     * @param params 查询参数
     * @return 趋势分析列表
     */
    List<Map<String, Object>> selectTrendAnalysis(Map<String, Object> params);
}
