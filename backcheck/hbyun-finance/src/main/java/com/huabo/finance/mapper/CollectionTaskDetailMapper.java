package com.huabo.finance.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.finance.entity.CollectionTaskDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 采集任务详情Mapper接口
 *
 * @author 华博云开发团队
 * @since 2025-10-23
 */
@Mapper
public interface CollectionTaskDetailMapper extends BaseMapper<CollectionTaskDetail> {

    /**
     * 根据任务ID查询详情列表
     *
     * @param taskId 任务ID
     * @return 详情列表
     */
    @Select("SELECT * FROM TBL_COLLECTION_TASK_DETAIL WHERE TASK_ID = #{taskId} ORDER BY CREATE_TIME")
    List<CollectionTaskDetail> selectByTaskId(@Param("taskId") String taskId);

    /**
     * 根据任务ID查询总记录数(累加所有详情的记录数)
     *
     * @param taskId 任务ID
     * @return 总记录数
     */
    @Select("SELECT SUM(RECORD_COUNT) FROM TBL_COLLECTION_TASK_DETAIL WHERE TASK_ID = #{taskId}")
    Long selectTotalRecordCountByTaskId(@Param("taskId") String taskId);
}

