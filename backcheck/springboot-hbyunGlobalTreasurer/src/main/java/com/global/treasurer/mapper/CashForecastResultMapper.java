package com.global.treasurer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.global.treasurer.entity.CashForecastResult;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;

/**
 * 现金流预测结果Mapper
 *
 * @author AI Developer
 * @date 2025-01-15
 */
public interface CashForecastResultMapper extends BaseMapper<CashForecastResult> {

    /**
     * 分页查询列表
     *
     * @param param 查询参数
     * @return 列表
     */
    List<CashForecastResult> selectPage(@Param("param") Map<String, Object> param);

    /**
     * 批量删除（软删除）
     *
     * @param ids ID列表
     * @return 影响行数
     */
    int batchDelete(@Param("ids") List<Long> ids);

    /**
     * 根据配置ID删除预测结果
     *
     * @param configId 配置ID
     * @return 影响行数
     */
    int deleteByConfigId(@Param("configId") Long configId);

    /**
     * 根据配置ID查询预测结果
     *
     * @param configId 配置ID
     * @return 结果列表
     */
    List<CashForecastResult> selectByConfigId(@Param("configId") Long configId);
}
