package com.financial.sharing.budgetPlanning.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.financial.sharing.budgetPlanning.entity.TblRollingForecastData;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 滚动预测数据Mapper接口
 * 
 * @author hbyun
 * @date 2026-02-02
 */
public interface RollingForecastDataMapper extends BaseMapper<TblRollingForecastData> {

    /**
     * 根据预测任务ID查询数据列表
     * 
     * @param forecastId 预测任务ID
     * @return 数据列表
     */
    List<TblRollingForecastData> selectByForecastId(@Param("forecastId") String forecastId);

    /**
     * 批量插入预测数据
     * 
     * @param dataList 数据列表
     * @return 插入数量
     */
    int batchInsert(@Param("dataList") List<TblRollingForecastData> dataList);

    /**
     * 根据预测任务ID删除数据
     * 
     * @param forecastId 预测任务ID
     * @return 删除数量
     */
    int deleteByForecastId(@Param("forecastId") String forecastId);
}

