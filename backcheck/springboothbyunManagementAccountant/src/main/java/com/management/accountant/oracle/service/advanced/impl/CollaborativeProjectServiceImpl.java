package com.management.accountant.oracle.service.advanced.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.management.accountant.oracle.entity.advanced.CollabComment;
import com.management.accountant.oracle.entity.advanced.CollaborativeProject;
import com.management.accountant.oracle.mapper.advanced.CollabActivityMapper;
import com.management.accountant.oracle.mapper.advanced.CollabCommentMapper;
import com.management.accountant.oracle.mapper.advanced.CollabParticipantMapper;
import com.management.accountant.oracle.mapper.advanced.CollaborativeProjectMapper;
import com.management.accountant.oracle.service.advanced.CollaborativeProjectService;
import com.management.accountant.util.SnowflakeIdWorker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.*;

@Slf4j
@Service("collaborativeProjectServiceOracle")
public class CollaborativeProjectServiceImpl implements CollaborativeProjectService {

    @Resource
    private CollaborativeProjectMapper projectMapper;
    @Resource
    private CollabParticipantMapper participantMapper;
    @Resource
    private CollabActivityMapper activityMapper;
    @Resource
    private CollabCommentMapper commentMapper;

    private static final SnowflakeIdWorker idWorker = new SnowflakeIdWorker(1, 1);

    @Override
    public List<CollaborativeProject> selectList(Map<String, Object> params) {
        return projectMapper.selectList(buildWrapper(params));
    }

    @Override
    public Page<CollaborativeProject> selectPage(Map<String, Object> params, Integer pageNum, Integer pageSize) {
        return projectMapper.selectPage(new Page<>(pageNum, pageSize), buildWrapper(params));
    }

    @Override
    public CollaborativeProject selectById(String projectId) {
        return projectMapper.selectById(projectId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean insert(CollaborativeProject project) {
        project.setProjectId("CP" + idWorker.nextId());
        project.setCreateTime(new Date());
        project.setDelFlag(0);
        if (!StringUtils.hasText(project.getProjectStatus())) {
            project.setProjectStatus("ACTIVE");
        }
        if (project.getProgress() == null) {
            project.setProgress(0);
        }
        return projectMapper.insert(project) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean update(CollaborativeProject project) {
        project.setUpdateTime(new Date());
        return projectMapper.updateById(project) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteById(String id) {
        CollaborativeProject p = projectMapper.selectById(id);
        if (p != null) {
            p.setDelFlag(1);
            p.setUpdateTime(new Date());
            return projectMapper.updateById(p) > 0;
        }
        return false;
    }

    @Override
    public boolean archiveProject(String id) {
        CollaborativeProject p = projectMapper.selectById(id);
        if (p == null) return false;
        p.setProjectStatus("ARCHIVED");
        p.setUpdateTime(new Date());
        return projectMapper.updateById(p) > 0;
    }

    @Override
    public List<Map<String, Object>> getParticipants(String projectId) {
        return participantMapper.findByProjectId(projectId);
    }

    @Override
    public List<Map<String, Object>> getActivities(String projectId) {
        return activityMapper.findByProjectId(projectId);
    }

    @Override
    public List<Map<String, Object>> getComments(String projectId) {
        return commentMapper.findByProjectId(projectId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addComment(String projectId, Map<String, Object> comment) {
        CollabComment c = new CollabComment();
        c.setCommentId("CMT" + idWorker.nextId());
        c.setProjectId(projectId);
        c.setUserName(comment.get("userName") != null ? comment.get("userName").toString() : "当前用户");
        c.setContent(comment.get("content") != null ? comment.get("content").toString() : "");
        c.setCreateTime(new Date());
        c.setDelFlag(0);
        return commentMapper.insert(c) > 0;
    }

    @Override
    public Map<String, Object> getStats() {
        Map<String, Object> raw = projectMapper.getFullStats();
        Map<String, Object> stats = new HashMap<>();
        if (raw != null) {
            long total = raw.get("TOTALPROJECTS") != null ? ((Number) raw.get("TOTALPROJECTS")).longValue() : 0L;
            long completed = raw.get("COMPLETEDPROJECTS") != null ? ((Number) raw.get("COMPLETEDPROJECTS")).longValue() : 0L;
            long totalParticipants = raw.get("TOTALPARTICIPANTS") != null ? ((Number) raw.get("TOTALPARTICIPANTS")).longValue() : 0L;
            double avgProgress = raw.get("AVGPROGRESS") != null ? ((Number) raw.get("AVGPROGRESS")).doubleValue() : 0.0;
            stats.put("totalProjects", total);
            stats.put("totalParticipants", totalParticipants);
            stats.put("completionRate", total > 0 ? Math.round(completed * 100.0 / total) : 0);
            stats.put("efficiency", Math.round(avgProgress));
        } else {
            stats.put("totalProjects", 0);
            stats.put("totalParticipants", 0);
            stats.put("completionRate", 0);
            stats.put("efficiency", 0);
        }
        return stats;
    }

    @Override
    public List<Map<String, Object>> getTypeStats() {
        return projectMapper.getTypeStats();
    }

    private QueryWrapper<CollaborativeProject> buildWrapper(Map<String, Object> params) {
        QueryWrapper<CollaborativeProject> w = new QueryWrapper<>();
        w.eq("DEL_FLAG", 0);
        if (params != null) {
            String keyword = (String) params.get("keyword");
            if (StringUtils.hasText(keyword)) w.like("PROJECT_NAME", keyword);
            // 支持 collaborationType 或 type 两种参数名
            String type = params.get("collaborationType") != null
                    ? (String) params.get("collaborationType")
                    : (String) params.get("type");
            if (StringUtils.hasText(type)) w.eq("COLLABORATION_TYPE", type);
        }
        w.orderByDesc("CREATE_TIME");
        return w;
    }
}
