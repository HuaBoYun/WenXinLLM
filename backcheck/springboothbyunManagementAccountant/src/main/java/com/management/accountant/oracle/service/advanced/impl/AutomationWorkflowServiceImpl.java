package com.management.accountant.oracle.service.advanced.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.management.accountant.oracle.entity.advanced.AutomationWorkflow;
import com.management.accountant.oracle.mapper.advanced.AutomationWorkflowMapper;
import com.management.accountant.oracle.service.advanced.AutomationWorkflowService;
import com.management.accountant.util.SnowflakeIdWorker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.*;

@Slf4j
@Service("automationWorkflowServiceOracle")
public class AutomationWorkflowServiceImpl implements AutomationWorkflowService {

    @Resource
    private AutomationWorkflowMapper workflowMapper;
    private static final SnowflakeIdWorker idWorker = new SnowflakeIdWorker(1, 1);

    @Override
    public List<AutomationWorkflow> selectList(Map<String, Object> params) {
        return workflowMapper.selectList(buildWrapper(params));
    }

    @Override
    public Page<AutomationWorkflow> selectPage(Map<String, Object> params, Integer pageNum, Integer pageSize) {
        return workflowMapper.selectPage(new Page<>(pageNum, pageSize), buildWrapper(params));
    }

    @Override
    public AutomationWorkflow selectById(String workflowId) {
        return workflowMapper.selectById(workflowId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean insert(AutomationWorkflow workflow) {
        workflow.setWorkflowId("WF" + idWorker.nextId());
        workflow.setCreateTime(new Date());
        workflow.setDelFlag(0);
        workflow.setExecutionCount(0);
        return workflowMapper.insert(workflow) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean update(AutomationWorkflow workflow) {
        workflow.setUpdateTime(new Date());
        return workflowMapper.updateById(workflow) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteById(String workflowId) {
        AutomationWorkflow wf = workflowMapper.selectById(workflowId);
        if (wf != null) { wf.setDelFlag(1); wf.setUpdateTime(new Date()); return workflowMapper.updateById(wf) > 0; }
        return false;
    }

    @Override
    public boolean runWorkflow(String workflowId) {
        AutomationWorkflow wf = workflowMapper.selectById(workflowId);
        if (wf == null) return false;
        wf.setStatus("RUNNING");
        wf.setLastExecution(new Date());
        wf.setExecutionCount(wf.getExecutionCount() != null ? wf.getExecutionCount() + 1 : 1);
        wf.setUpdateTime(new Date());
        return workflowMapper.updateById(wf) > 0;
    }

    @Override
    public boolean stopWorkflow(String workflowId) {
        AutomationWorkflow wf = workflowMapper.selectById(workflowId);
        if (wf == null) return false;
        wf.setStatus("STOPPED");
        wf.setUpdateTime(new Date());
        return workflowMapper.updateById(wf) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean copyWorkflow(String workflowId) {
        AutomationWorkflow src = workflowMapper.selectById(workflowId);
        if (src == null) return false;
        AutomationWorkflow copy = new AutomationWorkflow();
        copy.setWorkflowId("WF" + idWorker.nextId());
        copy.setWorkflowName(src.getWorkflowName() + " - 副本");
        copy.setWorkflowType(src.getWorkflowType());
        copy.setTriggerType(src.getTriggerType());
        copy.setStatus("INACTIVE");
        copy.setExecutionCount(0);
        copy.setDescription(src.getDescription());
        copy.setCreator(src.getCreator());
        copy.setCreateBy(src.getCreateBy());
        copy.setCreateTime(new Date());
        copy.setDelFlag(0);
        return workflowMapper.insert(copy) > 0;
    }

    @Override
    public Map<String, Object> getStats() {
        Map<String, Object> stats = new HashMap<>();
        // 总数
        QueryWrapper<AutomationWorkflow> w = new QueryWrapper<AutomationWorkflow>().eq("DEL_FLAG", 0);
        long total = workflowMapper.selectCount(w);
        stats.put("totalWorkflows", total);
        // 活跃数
        long active = workflowMapper.selectCount(new QueryWrapper<AutomationWorkflow>().eq("DEL_FLAG", 0).eq("STATUS", "ACTIVE"));
        stats.put("activeWorkflows", active);
        // 累计执行次数（所有记录的 EXECUTION_COUNT 之和）
        List<AutomationWorkflow> all = workflowMapper.selectList(new QueryWrapper<AutomationWorkflow>().eq("DEL_FLAG", 0).select("EXECUTION_COUNT", "STATUS"));
        long totalExecutions = 0;
        long successCount = 0;
        for (AutomationWorkflow wf : all) {
            int cnt = wf.getExecutionCount() != null ? wf.getExecutionCount() : 0;
            totalExecutions += cnt;
            if ("ACTIVE".equals(wf.getStatus()) || "STOPPED".equals(wf.getStatus())) {
                successCount += cnt;
            }
        }
        stats.put("totalExecutions", totalExecutions);
        // 成功率：有执行记录时计算，否则为0
        double successRate = totalExecutions > 0 ? Math.round(successCount * 100.0 / totalExecutions) : 0;
        stats.put("successRate", successRate);
        return stats;
    }

    @Override
    public Map<String, Long> countByType() {
        List<AutomationWorkflow> all = workflowMapper.selectList(
                new QueryWrapper<AutomationWorkflow>().eq("DEL_FLAG", 0).select("WORKFLOW_TYPE"));
        Map<String, Long> result = new HashMap<>();
        result.put("APPROVAL_WORKFLOW", 0L);
        result.put("DATA_SYNC_WORKFLOW", 0L);
        result.put("REPORT_WORKFLOW", 0L);
        result.put("NOTIFICATION_WORKFLOW", 0L);
        for (AutomationWorkflow wf : all) {
            String type = wf.getWorkflowType();
            if (type != null && result.containsKey(type)) {
                result.put(type, result.get(type) + 1);
            }
        }
        return result;
    }

    private QueryWrapper<AutomationWorkflow> buildWrapper(Map<String, Object> params) {
        QueryWrapper<AutomationWorkflow> w = new QueryWrapper<>();
        w.eq("DEL_FLAG", 0);
        if (params != null) {
            String keyword = (String) params.get("keyword");
            if (StringUtils.hasText(keyword)) w.like("WORKFLOW_NAME", keyword);
            // 兼容 type 和 workflowType 两种参数名
            String type = (String) params.get("type");
            if (!StringUtils.hasText(type)) type = (String) params.get("workflowType");
            if (StringUtils.hasText(type)) w.eq("WORKFLOW_TYPE", type);
            // 支持按状态筛选
            String status = (String) params.get("status");
            if (StringUtils.hasText(status)) w.eq("STATUS", status);
        }
        w.orderByDesc("CREATE_TIME");
        return w;
    }
}
