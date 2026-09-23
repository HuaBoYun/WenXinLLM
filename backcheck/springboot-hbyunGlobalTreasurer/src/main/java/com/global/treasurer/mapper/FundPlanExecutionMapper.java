package com.global.treasurer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.global.treasurer.entity.TblFundPlanExecution;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 资金计划执行Mapper接口
 *
 * @author 华博云开发团队
 * @since 2025-01-19
 */
@Mapper
public interface FundPlanExecutionMapper extends BaseMapper<TblFundPlanExecution> {

    /**
     * 分页查询资金计划执行列表
     *
     * @param params 查询参数
     * @return 资金计划执行列表
     */
    List<TblFundPlanExecution> selectExecutionPage(Map<String, Object> params);

    /**
     * 统计执行数量
     *
     * @param params 查询参数
     * @return 数量
     */
    int countExecutionList(Map<String, Object> params);

    /**
     * 查询执行汇总信息
     *
     * @param params 查询参数
     * @return 汇总数据
     */
    Map<String, Object> selectExecutionSummary(Map<String, Object> params);
}
