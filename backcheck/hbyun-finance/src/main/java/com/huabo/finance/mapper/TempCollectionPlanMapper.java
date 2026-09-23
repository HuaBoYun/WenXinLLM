package com.huabo.finance.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.finance.entity.TempCollectionPlan;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 临时采集方案Mapper接口
 * 
 * @author 华博云开发团队
 * @since 2025-01-21
 */
public interface TempCollectionPlanMapper extends BaseMapper<TempCollectionPlan> {

    /**
     * 根据任务ID查询临时方案
     */
    @Select("SELECT * FROM TBL_TEMP_COLLECTION_PLAN WHERE TASK_ID = #{taskId}")
    TempCollectionPlan selectByTaskId(@Param("taskId") String taskId);

    /**
     * 根据任务ID删除临时方案
     */
    @Select("DELETE FROM TBL_TEMP_COLLECTION_PLAN WHERE TASK_ID = #{taskId}")
    int deleteByTaskId(@Param("taskId") String taskId);

    /**
     * 查询需要保留的临时方案列表
     */
    @Select("SELECT * FROM TBL_TEMP_COLLECTION_PLAN WHERE IS_KEEP = 1 ORDER BY CREATE_TIME DESC")
    List<TempCollectionPlan> selectKeepList();
}

