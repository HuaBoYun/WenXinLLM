package com.management.accountant.oracle.mapper.advanced;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.management.accountant.oracle.entity.advanced.CollabParticipant;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface CollabParticipantMapper extends BaseMapper<CollabParticipant> {

    @Select("SELECT PARTICIPANT_ID as participantId, PROJECT_ID as projectId, USER_NAME as userName, " +
            "DEPARTMENT as department, ROLE as role, CONTRIBUTION as contribution, " +
            "LAST_ACTIVE as lastActive, STATUS as status " +
            "FROM TBL_COLLAB_PARTICIPANT WHERE PROJECT_ID = #{projectId} AND DEL_FLAG = 0 " +
            "ORDER BY CREATE_TIME ASC")
    List<Map<String, Object>> findByProjectId(@Param("projectId") String projectId);
}
