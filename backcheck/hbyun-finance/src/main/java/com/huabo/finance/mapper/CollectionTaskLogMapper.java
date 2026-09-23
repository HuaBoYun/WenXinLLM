package com.huabo.finance.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.finance.entity.CollectionTaskLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 采集任务日志Mapper接口
 *
 * @author 华博云开发团队
 * @since 2025-10-23
 */
@Mapper
public interface CollectionTaskLogMapper extends BaseMapper<CollectionTaskLog> {

    /**
     * 根据任务ID查询日志列表
     *
     * @param taskId 任务ID
     * @param limit 限制条数
     * @return 日志列表
     */
    @Select("SELECT * FROM (SELECT * FROM TBL_COLLECTION_TASK_LOG WHERE TASK_ID = #{taskId} ORDER BY CREATE_TIME DESC) WHERE ROWNUM <= #{limit}")
    List<CollectionTaskLog> selectByTaskId(@Param("taskId") String taskId, @Param("limit") int limit);
}

