package com.management.accountant.oracle.service.advanced;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.management.accountant.oracle.entity.advanced.CollaborativeProject;

import java.util.List;
import java.util.Map;

public interface CollaborativeProjectService {
    List<CollaborativeProject> selectList(Map<String, Object> params);
    Page<CollaborativeProject> selectPage(Map<String, Object> params, Integer pageNum, Integer pageSize);
    CollaborativeProject selectById(String projectId);
    boolean insert(CollaborativeProject project);
    boolean update(CollaborativeProject project);
    boolean deleteById(String projectId);
    boolean archiveProject(String projectId);
    List<Map<String, Object>> getParticipants(String projectId);
    List<Map<String, Object>> getActivities(String projectId);
    List<Map<String, Object>> getComments(String projectId);
    boolean addComment(String projectId, Map<String, Object> comment);
    Map<String, Object> getStats();
    List<Map<String, Object>> getTypeStats();
}
