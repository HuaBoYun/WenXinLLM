package com.management.accountant.oracle.mapper.advanced;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.management.accountant.oracle.entity.advanced.CollabActivity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface CollabActivityMapper extends BaseMapper<CollabActivity> {

    @Select("SELECT ACTIVITY_ID as activityId, PROJECT_ID as projectId, " +
            "USER_NAME as userName, ACTIVITY_TYPE as activityType, " +
            "CONTENT as content, CREATE_TIME as createTime " +
            "FROM TBL_COLLAB_ACTIVITY WHERE PROJECT_ID = #{projectId} AND DEL_FLAG = 0 " +
            "ORDER BY CREATE_TIME DESC")
    List<Map<String, Object>> findByProjectId(@Param("projectId") String projectId);
}
