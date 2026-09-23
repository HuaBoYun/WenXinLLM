package com.hbfk.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Map;

/**
 * 模型组合执行结果数据访问层
 * 
 * @author AI Assistant
 * @date 2025-01-29
 */
@Mapper
public interface ModelCombinationExecutionMapper {

    /**
     * 统计指定组合的执行结果数量
     */
    @Select("SELECT COUNT(*) FROM TBL_INDICATOR_EXECUTION_RESULT r " +
            "INNER JOIN TBL_COMBINATION_INDICATOR i ON r.CONFIG_ID = i.CONFIG_ID " +
            "WHERE i.COMBINATION_ID = #{combinationId}")
    int countExecutionResultsByCombinationId(@Param("combinationId") String combinationId);

    /**
     * 删除指定组合的执行结果
     */
    @Delete("DELETE FROM TBL_INDICATOR_EXECUTION_RESULT " +
            "WHERE CONFIG_ID IN (" +
            "  SELECT CONFIG_ID FROM TBL_COMBINATION_INDICATOR " +
            "  WHERE COMBINATION_ID = #{combinationId}" +
            ")")
    int deleteExecutionResultsByCombinationId(@Param("combinationId") String combinationId);

    /**
     * 统计所有执行结果数量
     */
    @Select("SELECT COUNT(*) FROM TBL_INDICATOR_EXECUTION_RESULT")
    int countAllExecutionResults();

    /**
     * 删除所有执行结果（危险操作）
     */
    @Delete("DELETE FROM TBL_INDICATOR_EXECUTION_RESULT")
    int deleteAllExecutionResults();

    /**
     * 按组合统计执行结果数量
     */
    @Select("SELECT i.COMBINATION_ID, COUNT(*) as count " +
            "FROM TBL_INDICATOR_EXECUTION_RESULT r " +
            "INNER JOIN TBL_COMBINATION_INDICATOR i ON r.CONFIG_ID = i.CONFIG_ID " +
            "GROUP BY i.COMBINATION_ID")
    Map<String, Integer> countExecutionResultsGroupByCombination();

    /**
     * 检查指定配置是否有执行结果
     */
    @Select("SELECT COUNT(*) FROM TBL_INDICATOR_EXECUTION_RESULT WHERE CONFIG_ID = #{configId}")
    int countExecutionResultsByConfigId(@Param("configId") String configId);

    /**
     * 删除指定配置的执行结果
     */
    @Delete("DELETE FROM TBL_INDICATOR_EXECUTION_RESULT WHERE CONFIG_ID = #{configId}")
    int deleteExecutionResultsByConfigId(@Param("configId") String configId);

    /**
     * 检查执行记录是否存在
     */
    @Select("SELECT COUNT(*) FROM TBL_COMBINATION_EXECUTION WHERE EXECUTION_ID = #{executionId}")
    int countExecutionRecord(@Param("executionId") String executionId);

    /**
     * 删除执行记录的结果数据
     */
    @Delete("DELETE FROM TBL_INDICATOR_EXECUTION_RESULT WHERE EXECUTION_ID = #{executionId}")
    int deleteExecutionResultsByExecutionId(@Param("executionId") String executionId);

    /**
     * 删除执行记录
     */
    @Delete("DELETE FROM TBL_COMBINATION_EXECUTION WHERE EXECUTION_ID = #{executionId}")
    int deleteExecutionRecord(@Param("executionId") String executionId);
}
