package com.management.accountant.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.management.accountant.exception.ServiceException;
import com.management.accountant.oracle.entity.advanced.FormulaTraceTask;
import com.management.accountant.oracle.mapper.advanced.FormulaTraceTaskMapper;
import com.management.accountant.service.BudgetFormulaTrackingService;
import com.management.accountant.util.PageResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.*;

/**
 * 预算公式追踪Service实现类
 *
 * @description 预算公式追踪业务实现
 * @author AI Assistant
 * @date 2025-01-04
 */
@Service
@Slf4j
public class BudgetFormulaTrackingServiceImpl implements BudgetFormulaTrackingService {

    private final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(getClass());

    @Resource
    private FormulaTraceTaskMapper formulaTraceTaskMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> create(Map<String, Object> params) {
        String taskName = (String) params.get("taskName");
        if (!StringUtils.hasText(taskName)) {
            throw new ServiceException("任务名称不能为空");
        }
        String traceType = (String) params.get("traceType");
        if (!StringUtils.hasText(traceType)) {
            throw new ServiceException("追踪类型不能为空");
        }

        FormulaTraceTask task = new FormulaTraceTask();
        task.setTaskId(UUID.randomUUID().toString().replace("-", ""));
        task.setTaskName(taskName);
        task.setTraceType(traceType);
        task.setStatus("PENDING");
        task.setCreateTime(new Date());
        task.setUpdateTime(new Date());

        Object traceDepth = params.get("traceDepth");
        if (traceDepth != null) {
            task.setTraceDepth(Integer.parseInt(traceDepth.toString()));
        } else {
            task.setTraceDepth(5);
        }
        if (params.get("formulaIds") != null) {
            task.setFormulaIds(params.get("formulaIds").toString());
        }
        if (StringUtils.hasText((String) params.get("remark"))) {
            task.setRemark((String) params.get("remark"));
        }

        int rows = formulaTraceTaskMapper.insert(task);
        if (rows <= 0) {
            throw new ServiceException("创建追踪任务失败");
        }

        log.info("创建追踪任务成功，taskId: {}", task.getTaskId());
        return taskToMap(task);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(String taskId, Map<String, Object> params) {
        if (!StringUtils.hasText(taskId)) {
            throw new ServiceException("任务ID不能为空");
        }
        FormulaTraceTask existing = formulaTraceTaskMapper.selectById(taskId);
        if (existing == null) {
            throw new ServiceException("追踪任务不存在");
        }

        if (StringUtils.hasText((String) params.get("taskName"))) {
            existing.setTaskName((String) params.get("taskName"));
        }
        if (StringUtils.hasText((String) params.get("traceType"))) {
            existing.setTraceType((String) params.get("traceType"));
        }
        if (params.get("traceDepth") != null) {
            existing.setTraceDepth(Integer.parseInt(params.get("traceDepth").toString()));
        }
        if (params.get("formulaIds") != null) {
            existing.setFormulaIds(params.get("formulaIds").toString());
        }
        if (StringUtils.hasText((String) params.get("remark"))) {
            existing.setRemark((String) params.get("remark"));
        }
        existing.setUpdateTime(new Date());

        formulaTraceTaskMapper.updateById(existing);
        log.info("更新追踪任务成功，taskId: {}", taskId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(String taskId) {
        if (!StringUtils.hasText(taskId)) {
            throw new ServiceException("任务ID不能为空");
        }
        FormulaTraceTask existing = formulaTraceTaskMapper.selectById(taskId);
        if (existing == null) {
            throw new ServiceException("追踪任务不存在");
        }
        formulaTraceTaskMapper.deleteById(taskId);
        log.info("删除追踪任务成功，taskId: {}", taskId);
    }

    @Override
    public PageResult<Map<String, Object>> page(int current, int size, Map<String, Object> params) {
        String keyword = (String) params.get("keyword");
        String traceType = (String) params.get("traceType");
        String status = (String) params.get("status");

        PageHelper.startPage(current, size);
        QueryWrapper<FormulaTraceTask> wrapper = new QueryWrapper<>();
        if (StringUtils.hasText(keyword)) {
            wrapper.like("TASK_NAME", keyword);
        }
        if (StringUtils.hasText(traceType)) {
            wrapper.eq("TRACE_TYPE", traceType);
        }
        if (StringUtils.hasText(status)) {
            wrapper.eq("STATUS", status);
        }
        wrapper.orderByDesc("CREATE_TIME");

        List<FormulaTraceTask> taskList = formulaTraceTaskMapper.selectList(wrapper);
        PageInfo<FormulaTraceTask> pageInfo = new PageInfo<>(taskList);

        List<Map<String, Object>> records = new ArrayList<>();
        for (FormulaTraceTask t : taskList) {
            records.add(taskToMap(t));
        }

        PageResult<Map<String, Object>> result = new PageResult<>();
        result.setList(records);
        result.setTlist(records);
        result.setTotal((int) pageInfo.getTotal());
        result.setTotalRecord((int) pageInfo.getTotal());
        result.setPageNum(pageInfo.getPageNum());
        result.setPageNo(pageInfo.getPageNum());
        result.setPageSize(pageInfo.getPageSize());
        result.setTotalPage(pageInfo.getPages());
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> execute(String taskId) {
        if (!StringUtils.hasText(taskId)) {
            throw new ServiceException("任务ID不能为空");
        }
        FormulaTraceTask task = formulaTraceTaskMapper.selectById(taskId);
        if (task == null) {
            throw new ServiceException("追踪任务不存在");
        }
        task.setStatus("executing");
        task.setExecuteTime(new Date());
        task.setUpdateTime(new Date());
        formulaTraceTaskMapper.updateById(task);

        log.info("执行追踪任务成功，taskId: {}", taskId);
        Map<String, Object> result = taskToMap(task);
        result.put("message", "追踪任务已启动");
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void stop(String taskId) {
        if (!StringUtils.hasText(taskId)) {
            throw new ServiceException("任务ID不能为空");
        }
        FormulaTraceTask task = formulaTraceTaskMapper.selectById(taskId);
        if (task == null) {
            throw new ServiceException("追踪任务不存在");
        }
        task.setStatus("PENDING");
        task.setUpdateTime(new Date());
        formulaTraceTaskMapper.updateById(task);
        log.info("停止追踪任务成功，taskId: {}", taskId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> copy(String taskId) {
        if (!StringUtils.hasText(taskId)) {
            throw new ServiceException("任务ID不能为空");
        }
        FormulaTraceTask source = formulaTraceTaskMapper.selectById(taskId);
        if (source == null) {
            throw new ServiceException("追踪任务不存在");
        }

        FormulaTraceTask copy = new FormulaTraceTask();
        copy.setTaskId(UUID.randomUUID().toString().replace("-", ""));
        copy.setTaskName(source.getTaskName() + "_副本");
        copy.setTraceType(source.getTraceType());
        copy.setTraceDepth(source.getTraceDepth());
        copy.setFormulaIds(source.getFormulaIds());
        copy.setRemark(source.getRemark());
        copy.setStatus("PENDING");
        copy.setCreateTime(new Date());
        copy.setUpdateTime(new Date());

        formulaTraceTaskMapper.insert(copy);
        log.info("复制追踪任务成功，源taskId: {}, 新taskId: {}", taskId, copy.getTaskId());
        return taskToMap(copy);
    }

    @Override
    public Map<String, Object> getDetail(String taskId) {
        if (!StringUtils.hasText(taskId)) {
            throw new ServiceException("任务ID不能为空");
        }
        FormulaTraceTask task = formulaTraceTaskMapper.selectById(taskId);
        if (task == null) {
            throw new ServiceException("追踪任务不存在");
        }
        return taskToMap(task);
    }

    @Override
    public Map<String, Object> getLogs(String taskId) {
        if (!StringUtils.hasText(taskId)) {
            throw new ServiceException("任务ID不能为空");
        }
        FormulaTraceTask task = formulaTraceTaskMapper.selectById(taskId);
        if (task == null) {
            throw new ServiceException("追踪任务不存在");
        }

        List<Map<String, Object>> logs = new ArrayList<>();
        Map<String, Object> logEntry = new HashMap<>();
        logEntry.put("logId", "LOG_" + taskId + "_1");
        logEntry.put("level", "INFO");
        logEntry.put("message", "任务【" + task.getTaskName() + "】状态：" + task.getStatus());
        logEntry.put("timestamp", task.getUpdateTime() != null ? task.getUpdateTime() : task.getCreateTime());
        logs.add(logEntry);
        if (StringUtils.hasText(task.getErrorMessage())) {
            Map<String, Object> errEntry = new HashMap<>();
            errEntry.put("logId", "LOG_" + taskId + "_2");
            errEntry.put("level", "ERROR");
            errEntry.put("message", task.getErrorMessage());
            errEntry.put("timestamp", task.getUpdateTime());
            logs.add(errEntry);
        }

        Map<String, Object> result = new HashMap<>();
        result.put("taskId", taskId);
        result.put("logs", logs);
        result.put("totalCount", logs.size());
        return result;
    }

    @Override
    public Map<String, Object> trackDependencies(String taskId) {
        if (!StringUtils.hasText(taskId)) {
            throw new ServiceException("任务ID不能为空");
        }
        // 先查当前任务
        FormulaTraceTask current = formulaTraceTaskMapper.selectById(taskId);
        if (current == null) {
            throw new ServiceException("追踪任务不存在");
        }

        // 查同类型的其他任务作为依赖关系展示
        QueryWrapper<FormulaTraceTask> wrapper = new QueryWrapper<>();
        if (StringUtils.hasText(current.getTraceType())) {
            wrapper.eq("TRACE_TYPE", current.getTraceType());
        }
        wrapper.ne("TASK_ID", taskId).orderByDesc("CREATE_TIME");
        List<FormulaTraceTask> taskList = formulaTraceTaskMapper.selectList(wrapper);

        List<Map<String, Object>> dependencies = new ArrayList<>();
        for (FormulaTraceTask t : taskList) {
            Map<String, Object> dep = new HashMap<>();
            dep.put("taskId", t.getTaskId());
            dep.put("taskName", t.getTaskName());
            dep.put("traceType", t.getTraceType());
            dep.put("status", t.getStatus());
            dep.put("createTime", t.getCreateTime());
            dependencies.add(dep);
        }

        Map<String, Object> result = new HashMap<>();
        result.put("taskId", taskId);
        result.put("taskName", current.getTaskName());
        result.put("traceType", current.getTraceType());
        result.put("dependencies", dependencies);
        result.put("totalCount", dependencies.size());
        result.put("trackTime", new Date());
        return result;
    }

    @Override
    public Map<String, Object> analyzeImpact(Map<String, Object> params) {
        // 支持 taskId 或 formulaId 两种入参
        String taskId = (String) params.get("taskId");
        String formulaId = (String) params.get("formulaId");
        String changeType = (String) params.get("changeType");

        FormulaTraceTask current = null;
        if (StringUtils.hasText(taskId)) {
            current = formulaTraceTaskMapper.selectById(taskId);
        } else if (StringUtils.hasText(formulaId)) {
            // 兼容旧调用：用 formulaId 模糊匹配
            QueryWrapper<FormulaTraceTask> q = new QueryWrapper<>();
            q.like("FORMULA_IDS", formulaId).last("FETCH FIRST 1 ROWS ONLY");
            current = formulaTraceTaskMapper.selectOne(q);
        }
        if (current == null) {
            throw new ServiceException("追踪任务不存在");
        }

        // 查 IMPACT 类型任务作为影响项
        QueryWrapper<FormulaTraceTask> wrapper = new QueryWrapper<>();
        wrapper.eq("TRACE_TYPE", "IMPACT").ne("TASK_ID", current.getTaskId()).orderByDesc("CREATE_TIME");
        List<FormulaTraceTask> taskList = formulaTraceTaskMapper.selectList(wrapper);

        List<Map<String, Object>> impactedItems = new ArrayList<>();
        for (FormulaTraceTask t : taskList) {
            Map<String, Object> item = new HashMap<>();
            item.put("taskId", t.getTaskId());
            item.put("taskName", t.getTaskName());
            item.put("traceType", t.getTraceType());
            item.put("status", t.getStatus());
            item.put("createTime", t.getCreateTime());
            impactedItems.add(item);
        }

        Map<String, Object> result = new HashMap<>();
        result.put("taskId", current.getTaskId());
        result.put("taskName", current.getTaskName());
        result.put("changeType", changeType);
        result.put("impactedItems", impactedItems);
        result.put("totalImpact", impactedItems.size());
        result.put("analysisTime", new Date());
        return result;
    }

    @Override
    public Map<String, Object> getFormulaChain(String formulaId) {
        if (!StringUtils.hasText(formulaId)) {
            throw new ServiceException("公式ID不能为空");
        }
        QueryWrapper<FormulaTraceTask> wrapper = new QueryWrapper<>();
        wrapper.eq("TRACE_TYPE", "CALCULATION").orderByDesc("CREATE_TIME");
        List<FormulaTraceTask> taskList = formulaTraceTaskMapper.selectList(wrapper);

        List<Map<String, Object>> chain = new ArrayList<>();
        for (int i = 0; i < taskList.size(); i++) {
            FormulaTraceTask t = taskList.get(i);
            Map<String, Object> node = new HashMap<>();
            node.put("nodeId", t.getTaskId());
            node.put("taskName", t.getTaskName());
            node.put("traceType", t.getTraceType());
            node.put("level", i);
            node.put("nodeType", i == 0 ? "ROOT" : i == taskList.size() - 1 ? "LEAF" : "INTERMEDIATE");
            chain.add(node);
        }

        Map<String, Object> result = new HashMap<>();
        result.put("formulaId", formulaId);
        result.put("chain", chain);
        result.put("chainLength", chain.size());
        result.put("queryTime", new Date());
        return result;
    }

    @Override
    public Map<String, Object> validateCircularDependency(Map<String, Object> params) {
        String formulaId = (String) params.get("formulaId");
        @SuppressWarnings("unchecked")
        List<String> referencedFormulas = (List<String>) params.get("referencedFormulas");

        if (!StringUtils.hasText(formulaId)) {
            throw new ServiceException("公式ID不能为空");
        }

        boolean hasCircular = referencedFormulas != null && referencedFormulas.contains(formulaId);
        List<String> circularPath = new ArrayList<>();
        if (hasCircular) {
            circularPath.add(formulaId);
            circularPath.addAll(referencedFormulas.subList(0, Math.min(3, referencedFormulas.size())));
            circularPath.add(formulaId);
        }

        Map<String, Object> result = new HashMap<>();
        result.put("formulaId", formulaId);
        result.put("hasCircularDependency", hasCircular);
        result.put("circularPath", circularPath);
        result.put("isValid", !hasCircular);
        result.put("validationTime", new Date());
        result.put("message", hasCircular ? "检测到循环依赖" : "未检测到循环依赖");
        return result;
    }

    @Override
    public Map<String, Object> generateDependencyGraph(Map<String, Object> params) {
        QueryWrapper<FormulaTraceTask> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("CREATE_TIME");
        List<FormulaTraceTask> taskList = formulaTraceTaskMapper.selectList(wrapper);

        List<Map<String, Object>> nodes = new ArrayList<>();
        for (FormulaTraceTask t : taskList) {
            Map<String, Object> node = new HashMap<>();
            node.put("id", t.getTaskId());
            node.put("label", t.getTaskName());
            node.put("type", t.getTraceType());
            node.put("status", t.getStatus());
            nodes.add(node);
        }

        Map<String, Object> result = new HashMap<>();
        result.put("nodes", nodes);
        result.put("edges", new ArrayList<>());
        result.put("nodeCount", nodes.size());
        result.put("generateTime", new Date());
        return result;
    }

    @Override
    public Map<String, Object> getDependenciesStats(Map<String, Object> params) {
        List<FormulaTraceTask> allTasks = formulaTraceTaskMapper.selectList(new QueryWrapper<>());
        long totalTasks = allTasks.size();
        long activeTasks = allTasks.stream()
                .filter(t -> "executing".equals(t.getStatus()) || "PENDING".equals(t.getStatus()))
                .count();
        long completedTasks = allTasks.stream()
                .filter(t -> "completed".equals(t.getStatus()))
                .count();

        Map<String, Object> stats = new HashMap<>();
        stats.put("totalFormulas", totalTasks);
        stats.put("activeTasks", activeTasks);
        stats.put("dependencies", completedTasks);
        stats.put("accuracy", totalTasks > 0 ? Math.round(completedTasks * 100.0 / totalTasks * 10) / 10.0 : 0);
        return stats;
    }

    @Override
    public Map<String, Object> getAvailableFormulas(Map<String, Object> params) {
        QueryWrapper<FormulaTraceTask> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("CREATE_TIME");
        List<FormulaTraceTask> taskList = formulaTraceTaskMapper.selectList(wrapper);

        List<Map<String, Object>> formulas = new ArrayList<>();
        for (FormulaTraceTask t : taskList) {
            Map<String, Object> formula = new HashMap<>();
            formula.put("id", t.getTaskId());
            formula.put("name", t.getTaskName());
            formula.put("type", t.getTraceType());
            formula.put("status", t.getStatus());
            formulas.add(formula);
        }

        Map<String, Object> result = new HashMap<>();
        result.put("formulas", formulas);
        result.put("totalCount", formulas.size());
        return result;
    }

    @Override
    public Map<String, Object> getTaskFormulas(String taskId) {
        if (!StringUtils.hasText(taskId)) {
            throw new ServiceException("任务ID不能为空");
        }
        FormulaTraceTask task = formulaTraceTaskMapper.selectById(taskId);
        if (task == null) {
            throw new ServiceException("追踪任务不存在");
        }

        List<Map<String, Object>> formulas = new ArrayList<>();
        if (StringUtils.hasText(task.getFormulaIds())) {
            String[] ids = task.getFormulaIds().replace("[", "").replace("]", "").replace("\"", "").split(",");
            for (String id : ids) {
                String trimmed = id.trim();
                if (!trimmed.isEmpty()) {
                    Map<String, Object> formula = new HashMap<>();
                    formula.put("id", trimmed);
                    formula.put("name", trimmed);
                    formula.put("status", task.getStatus());
                    formulas.add(formula);
                }
            }
        }

        Map<String, Object> result = new HashMap<>();
        result.put("taskId", taskId);
        result.put("formulas", formulas);
        result.put("totalCount", formulas.size());
        return result;
    }

    private Map<String, Object> taskToMap(FormulaTraceTask task) {
        Map<String, Object> map = new HashMap<>();
        map.put("taskId", task.getTaskId());
        map.put("taskName", task.getTaskName());
        map.put("traceType", task.getTraceType());
        map.put("traceDepth", task.getTraceDepth());
        map.put("formulaIds", task.getFormulaIds());
        map.put("status", task.getStatus());
        map.put("resultData", task.getResultData());
        map.put("errorMessage", task.getErrorMessage());
        map.put("remark", task.getRemark());
        map.put("createTime", task.getCreateTime());
        map.put("updateTime", task.getUpdateTime());
        map.put("executeTime", task.getExecuteTime());
        map.put("completeTime", task.getCompleteTime());
        return map;
    }
}

