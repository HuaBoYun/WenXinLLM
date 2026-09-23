package com.management.accountant.oracle.mapper.advanced;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.management.accountant.oracle.entity.advanced.FormulaTraceResult;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 公式追踪结果Mapper接口
 * 
 * @author AI Agent
 * @date 2026-02-06
 */
@Mapper
public interface FormulaTraceResultMapper extends BaseMapper<FormulaTraceResult> {

    /**
     * 根据任务ID查询结果列表
     * 
     * @param taskId 任务ID
     * @return 结果列表
     */
    List<FormulaTraceResult> selectByTaskId(@Param("taskId") String taskId);

    /**
     * 根据公式ID查询结果列表
     * 
     * @param formulaId 公式ID
     * @param companyId 公司ID
     * @return 结果列表
     */
    List<FormulaTraceResult> selectByFormulaId(@Param("formulaId") String formulaId, @Param("companyId") String companyId);

    /**
     * 批量插入结果
     * 
     * @param results 结果列表
     * @return 插入数量
     */
    int batchInsert(@Param("results") List<FormulaTraceResult> results);

    /**
     * 根据任务ID删除结果
     * 
     * @param taskId 任务ID
     * @return 删除数量
     */
    int deleteByTaskId(@Param("taskId") String taskId);

    /**
     * 批量删除结果
     * 
     * @param taskIds 任务ID列表
     * @return 删除数量
     */
    int batchDeleteByTaskIds(@Param("taskIds") List<String> taskIds);
}

