package com.management.accountant.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.management.accountant.exception.ServiceException;
import com.management.accountant.oracle.entity.budget.BudgetWorkflow;
import com.management.accountant.oracle.mapper.budget.BudgetWorkflowMapper;
import com.management.accountant.service.BudgetWorkflowService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.*;

@Service
@Slf4j
public class BudgetWorkflowServiceImpl implements BudgetWorkflowService {
    private final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(getClass());

    @Resource
    private BudgetWorkflowMapper workflowMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BudgetWorkflow create(BudgetWorkflow w) {
        if (w == null) throw new ServiceException("工作流信息不能为空");
        if (w.getDelFlag() == null) w.setDelFlag(0);
        if (w.getIsEnabled() == null) w.setIsEnabled(1);
        if (w.getVersion() == null) w.setVersion(1);
        if (w.getWorkflowStatus() == null) w.setWorkflowStatus("DRAFT");
        if (w.getAllowParallel() == null) w.setAllowParallel(0);
        if (w.getAllowSkip() == null) w.setAllowSkip(0);
        if (w.getAutoStart() == null) w.setAutoStart(0);
        if (w.getNotifyOnStart() == null) w.setNotifyOnStart(0);
        if (w.getNotifyOnComplete() == null) w.setNotifyOnComplete(1);
        if (w.getInstanceCount() == null) w.setInstanceCount(0);
        if (w.getAvgDuration() == null) w.setAvgDuration(0);
        w.setCreateTime(new Date()); w.setUpdateTime(new Date());
        workflowMapper.insert(w);
        return w;
    }

    @Override
    public BudgetWorkflow getById(String id) {
        QueryWrapper<BudgetWorkflow> qw = new QueryWrapper<>();
        qw.eq("WORKFLOW_ID", id).eq("DEL_FLAG", 0L);
        return workflowMapper.selectOne(qw);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(BudgetWorkflow w) {
        if (w == null || !StringUtils.hasText(w.getWorkflowId())) throw new ServiceException("工作流ID不能为空");
        w.setUpdateTime(new Date());
        workflowMapper.updateById(w);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(String id) {
        BudgetWorkflow u = new BudgetWorkflow();
        u.setWorkflowId(id); u.setDelFlag(1); u.setUpdateTime(new Date());
        workflowMapper.updateById(u);
    }

    @Override
    public Map<String, Object> getPage(Map<String, Object> params) {
        int pageNum = params.get("pageNum") != null ? Integer.parseInt(params.get("pageNum").toString()) : 1;
        int pageSize = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 20;
        QueryWrapper<BudgetWorkflow> qw = new QueryWrapper<>();
        qw.eq("DEL_FLAG", 0L);
        if (hasValue(params.get("workflowName"))) qw.like("WORKFLOW_NAME", params.get("workflowName"));
        if (hasValue(params.get("workflowType"))) qw.eq("WORKFLOW_TYPE", params.get("workflowType"));
        if (hasValue(params.get("workflowStatus"))) qw.eq("WORKFLOW_STATUS", params.get("workflowStatus"));
        // 分类树的 id 就是 WORKFLOW_TYPE，所以 categoryId 参数按 WORKFLOW_TYPE 筛选
        if (hasValue(params.get("categoryId"))) qw.eq("WORKFLOW_TYPE", params.get("categoryId"));
        if (hasValue(params.get("createBy"))) qw.like("CREATE_BY", params.get("createBy"));
        qw.orderByDesc("CREATE_TIME");
        IPage<BudgetWorkflow> pageResult = workflowMapper.selectPage(new Page<>(pageNum, pageSize), qw);
        Map<String, Object> result = new HashMap<>();
        result.put("records", pageResult.getRecords());
        result.put("total", pageResult.getTotal());
        result.put("pageNum", pageNum);
        result.put("pageSize", pageSize);
        return result;
    }

    @Override
    public List<Map<String, Object>> getCategoryTree() {
        // 按 WORKFLOW_TYPE 分组统计每个分类的数量
        QueryWrapper<BudgetWorkflow> qw = new QueryWrapper<>();
        qw.eq("DEL_FLAG", 0L)
          .select("WORKFLOW_TYPE, COUNT(*) AS INSTANCE_COUNT")
          .groupBy("WORKFLOW_TYPE");
        List<Map<String, Object>> rows = workflowMapper.selectMaps(qw);
        List<Map<String, Object>> tree = new ArrayList<>();
        for (Map<String, Object> row : rows) {
            Object typeVal = row.get("WORKFLOW_TYPE");
            if (typeVal == null) continue;
            String type = typeVal.toString();
            long count = 0;
            Object countVal = row.get("INSTANCE_COUNT");
            if (countVal != null) {
                count = Long.parseLong(countVal.toString());
            }
            Map<String, Object> node = new HashMap<>();
            node.put("id", type);
            node.put("name", type);
            node.put("workflowCount", count);
            node.put("children", new ArrayList<>());
            tree.add(node);
        }
        return tree;
    }

    @Override
    public List<Map<String, Object>> getCategories() { return getCategoryTree(); }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deploy(String id) {
        BudgetWorkflow u = new BudgetWorkflow();
        u.setWorkflowId(id);
        u.setIsEnabled(1);
        u.setWorkflowStatus("ACTIVE");
        u.setUpdateTime(new Date());
        workflowMapper.updateById(u);
        log.info("部署工作流: {}", id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchDeploy(List<String> ids) { for (String id : ids) deploy(id); }

    @Override
    public Map<String, Object> getStats() {
        QueryWrapper<BudgetWorkflow> total = new QueryWrapper<BudgetWorkflow>().eq("DEL_FLAG", 0L);
        QueryWrapper<BudgetWorkflow> active = new QueryWrapper<BudgetWorkflow>().eq("DEL_FLAG", 0L).eq("WORKFLOW_STATUS", "ACTIVE");
        QueryWrapper<BudgetWorkflow> draft = new QueryWrapper<BudgetWorkflow>().eq("DEL_FLAG", 0L).eq("WORKFLOW_STATUS", "DRAFT");
        long totalCount = workflowMapper.selectCount(total);
        long activeCount = workflowMapper.selectCount(active);
        long draftCount = workflowMapper.selectCount(draft);
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalWorkflows", totalCount);
        stats.put("activeWorkflows", activeCount);
        stats.put("draftWorkflows", draftCount);
        stats.put("runningInstances", 0);
        stats.put("activeRate", totalCount > 0 ? Math.round(activeCount * 100.0 / totalCount * 10) / 10.0 : 0);
        stats.put("draftRate", totalCount > 0 ? Math.round(draftCount * 100.0 / totalCount * 10) / 10.0 : 0);
        stats.put("instanceRate", 0);
        return stats;
    }

    private boolean hasValue(Object val) {
        if (val == null) return false;
        if (val instanceof String) return !((String) val).trim().isEmpty();
        if (val instanceof java.util.Collection) return !((java.util.Collection<?>) val).isEmpty();
        return true;
    }
}

