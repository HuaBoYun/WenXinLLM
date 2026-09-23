package com.management.accountant.oracle.mapper.budget;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.management.accountant.oracle.entity.budget.BudgetForecastModel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 预测模型Mapper接口
 *
 * @author AI Agent
 * @date 2025-12-31
 */
@Mapper
public interface BudgetForecastModelMapper extends BaseMapper<BudgetForecastModel> {

    /** 查询所有激活模型 */
    List<BudgetForecastModel> selectActiveModels();

    /** 按模型类型查询 */
    List<BudgetForecastModel> selectByModelType(@Param("modelType") String modelType);

    /** 查询模型汇总信息 */
    List<Map<String, Object>> selectModelSummary();
}
