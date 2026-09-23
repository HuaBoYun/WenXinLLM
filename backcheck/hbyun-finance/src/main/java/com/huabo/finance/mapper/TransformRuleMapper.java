package com.huabo.finance.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.finance.entity.TransformRule;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 转换规则Mapper接口
 *
 * @author 华博云开发团队
 * @since 2025-10-24
 */
@Mapper
public interface TransformRuleMapper extends BaseMapper<TransformRule> {

    /**
     * 根据采集任务ID查询转化规则列表(按执行顺序排序)
     */
    @Select("SELECT * FROM TBL_TRANSFORM_RULE WHERE COLLECTION_TASK_ID = #{collectionTaskId} " +
            "AND IS_ENABLED = '1' ORDER BY EXECUTE_ORDER ASC")
    List<TransformRule> selectByCollectionTaskId(@Param("collectionTaskId") String collectionTaskId);

    /**
     * 根据任务ID查询转化规则列表
     */
    @Select("SELECT * FROM TBL_TRANSFORM_RULE WHERE TASK_ID = #{taskId} AND IS_ENABLED = '1' ORDER BY EXECUTE_ORDER ASC")
    List<TransformRule> selectByTaskId(@Param("taskId") String taskId);

    /**
     * 查询需要自动执行的转化规则
     */
    @Select("SELECT * FROM TBL_TRANSFORM_RULE WHERE COLLECTION_TASK_ID = #{collectionTaskId} " +
            "AND AUTO_EXECUTE = 1 AND IS_ENABLED = '1' ORDER BY EXECUTE_ORDER ASC")
    List<TransformRule> selectAutoExecuteRules(@Param("collectionTaskId") String collectionTaskId);
}

