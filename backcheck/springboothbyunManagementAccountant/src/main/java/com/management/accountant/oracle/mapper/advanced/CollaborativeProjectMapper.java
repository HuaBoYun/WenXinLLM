package com.management.accountant.oracle.mapper.advanced;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.management.accountant.oracle.entity.advanced.CollaborativeProject;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface CollaborativeProjectMapper extends BaseMapper<CollaborativeProject> {

    @Select("SELECT COLLABORATION_TYPE as type, COUNT(*) as projectCount, " +
            "NVL(SUM(PARTICIPANT_COUNT), 0) as participantCount " +
            "FROM TBL_COLLABORATIVE_PROJECT WHERE DEL_FLAG = 0 " +
            "GROUP BY COLLABORATION_TYPE ORDER BY projectCount DESC")
    List<Map<String, Object>> getTypeStats();

    @Select("SELECT NVL(SUM(PARTICIPANT_COUNT), 0) as totalParticipants, " +
            "COUNT(*) as totalProjects, " +
            "SUM(CASE WHEN PROJECT_STATUS = 'COMPLETED' THEN 1 ELSE 0 END) as completedProjects, " +
            "SUM(CASE WHEN PROJECT_STATUS = 'ACTIVE' THEN 1 ELSE 0 END) as activeProjects, " +
            "NVL(ROUND(SUM(NVL(PROGRESS, 0)) * 1.0 / NULLIF(COUNT(*), 0), 1), 0) as avgProgress " +
            "FROM TBL_COLLABORATIVE_PROJECT WHERE DEL_FLAG = 0")
    Map<String, Object> getFullStats();
}
