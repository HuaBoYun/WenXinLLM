package com.management.accountant.oracle.service.advanced.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.management.accountant.exception.ServiceException;
import com.management.accountant.oracle.entity.advanced.FormulaTraceResult;
import com.management.accountant.oracle.entity.advanced.FormulaTraceTask;
import com.management.accountant.oracle.entity.budget.BudgetFormula;
import com.management.accountant.oracle.mapper.advanced.FormulaTraceResultMapper;
import com.management.accountant.oracle.mapper.advanced.FormulaTraceTaskMapper;
import com.management.accountant.oracle.mapper.budget.BudgetFormulaMapper;
import com.management.accountant.oracle.service.advanced.FormulaTraceTaskService;
import com.management.accountant.util.PageResult;
import com.management.accountant.util.SnowflakeIdWorker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.*;

/**
 * 公式追踪任务Service实现类
 * 
 * @author AI Agent
 * @date 2026-02-06
 */
@Slf4j
@Service("formulaTraceTaskServiceOracle")
public class FormulaTraceTaskServiceImpl implements FormulaTraceTaskService {

    @Resource
    private FormulaTraceTaskMapper formulaTraceTaskMapper;

    @Resource
    private FormulaTraceResultMapper formulaTraceResultMapper;

    @Resource
    private BudgetFormulaMapper budgetFormulaMapper;

    private final SnowflakeIdWorker idWorker = new SnowflakeIdWorker(1, 1);

    @Override
    public Map<String, Object> getStats(String companyId) {
        try {
            log.info("获取公式追踪统计数据, companyId: {}", companyId);
            
            Map<String, Object> stats = new HashMap<>();
            
            // 查询各状态的任务数量
            QueryWrapper<FormulaTraceTask> wrapper = new QueryWrapper<>();
            wrapper.eq("COMPANY_ID", companyId);
            
            List<FormulaTraceTask> allTasks = formulaTraceTaskMapper.selectList(wrapper);
            
            int totalTasks = allTasks.size();
            long pendingTasks = allTasks.stream().filter(t -> "pending".equals(t.getStatus())).count();
            long executingTasks = allTasks.stream().filter(t -> "executing".equals(t.getStatus())).count();
            long completedTasks = allTasks.stream().filter(t -> "completed".equals(t.getStatus())).count();
            long failedTasks = allTasks.stream().filter(t -> "failed".equals(t.getStatus())).count();
            
            stats.put("totalTasks", totalTasks);
            stats.put("pendingTasks", pendingTasks);
            stats.put("executingTasks", executingTasks);
            stats.put("completedTasks", completedTasks);
            stats.put("failedTasks", failedTasks);
            
            // 计算成功率
            if (totalTasks > 0) {
                double successRate = (double) completedTasks / totalTasks * 100;
                stats.put("successRate", String.format("%.2f", successRate));
            } else {
                stats.put("successRate", "0.00");
            }
            
            return stats;
        } catch (Exception e) {
            log.error("获取公式追踪统计数据失败", e);
            throw new ServiceException("获取统计数据失败: " + e.getMessage());
        }
    }

    @Override
    public PageResult<FormulaTraceTask> getTaskList(String keyword, String status, String companyId, Integer pageNo, Integer pageSize) {
        try {
            log.info("获取公式追踪任务列表, keyword: {}, status: {}, companyId: {}, pageNo: {}, pageSize: {}", 
                    keyword, status, companyId, pageNo, pageSize);
            
            // 计算偏移量
            int offset = (pageNo - 1) * pageSize;
            
            // 查询列表
            List<FormulaTraceTask> list = formulaTraceTaskMapper.selectPageList(keyword, status, companyId, offset, pageSize);
            
            // 查询总数
            int total = formulaTraceTaskMapper.selectCount(keyword, status, companyId);
            
            // 构建分页结果
            PageResult<FormulaTraceTask> pageResult = new PageResult<>();
            pageResult.setTlist(list);
            pageResult.setTotalRecord(total);
            pageResult.setPageNo(pageNo);
            pageResult.setPageSize(pageSize);
            pageResult.setTotalPage((total + pageSize - 1) / pageSize);
            
            return pageResult;
        } catch (Exception e) {
            log.error("获取公式追踪任务列表失败", e);
            throw new ServiceException("获取任务列表失败: " + e.getMessage());
        }
    }

    @Override
    public List<Map<String, Object>> getAvailableFormulas(String companyId) {
        try {
            log.info("获取可用公式列表, companyId: {}", companyId);
            
            // 查询所有启用的公式
            List<BudgetFormula> formulas = budgetFormulaMapper.selectEnabledFormulas();
            
            // 转换为Map列表
            List<Map<String, Object>> result = new ArrayList<>();
            for (BudgetFormula formula : formulas) {
                Map<String, Object> map = new HashMap<>();
                map.put("formulaId", formula.getFormulaId());
                map.put("formulaCode", formula.getFormulaCode());
                map.put("formulaName", formula.getFormulaName());
                map.put("formulaType", formula.getFormulaType());
                map.put("formulaExpression", formula.getFormulaExpression());
                result.add(map);
            }
            
            return result;
        } catch (Exception e) {
            log.error("获取可用公式列表失败", e);
            throw new ServiceException("获取公式列表失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String createTask(FormulaTraceTask task, String companyId, String userId, String userName) {
        try {
            log.info("创建公式追踪任务, taskName: {}, companyId: {}, userId: {}", task.getTaskName(), companyId, userId);
            
            // 生成任务ID
            String taskId = "TASK_" + idWorker.nextId();
            task.setTaskId(taskId);
            
            // 设置默认值
            if (task.getTraceDepth() == null) {
                task.setTraceDepth(5);
            }
            if (!StringUtils.hasText(task.getTraceType())) {
                task.setTraceType("forward");
            }
            task.setStatus("pending");
            
            // 设置公司和用户信息
            task.setCompanyId(companyId);
            task.setCreatedBy(userId);
            task.setCreatorName(userName);
            task.setCreateTime(new Date());
            task.setUpdateTime(new Date());
            
            // 插入数据库
            int rows = formulaTraceTaskMapper.insert(task);
            if (rows <= 0) {
                throw new ServiceException("创建任务失败");
            }
            
            log.info("创建公式追踪任务成功, taskId: {}", taskId);
            return taskId;
        } catch (Exception e) {
            log.error("创建公式追踪任务失败", e);
            throw new ServiceException("创建任务失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateTask(String taskId, FormulaTraceTask task, String userId, String userName) {
        try {
            log.info("更新公式追踪任务, taskId: {}, userId: {}", taskId, userId);
            
            // 查询原任务
            FormulaTraceTask existingTask = formulaTraceTaskMapper.selectById(taskId);
            if (existingTask == null) {
                throw new ServiceException("任务不存在");
            }
            
            // 更新字段
            if (StringUtils.hasText(task.getTaskName())) {
                existingTask.setTaskName(task.getTaskName());
            }
            if (StringUtils.hasText(task.getFormulaIds())) {
                existingTask.setFormulaIds(task.getFormulaIds());
            }
            if (StringUtils.hasText(task.getTraceType())) {
                existingTask.setTraceType(task.getTraceType());
            }
            if (task.getTraceDepth() != null) {
                existingTask.setTraceDepth(task.getTraceDepth());
            }
            if (StringUtils.hasText(task.getRemark())) {
                existingTask.setRemark(task.getRemark());
            }
            
            // 设置更新信息
            existingTask.setUpdatedBy(userId);
            existingTask.setUpdaterName(userName);
            existingTask.setUpdateTime(new Date());
            
            // 更新数据库
            int rows = formulaTraceTaskMapper.updateById(existingTask);
            
            log.info("更新公式追踪任务成功, taskId: {}", taskId);
            return rows > 0;
        } catch (Exception e) {
            log.error("更新公式追踪任务失败", e);
            throw new ServiceException("更新任务失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteTask(String taskId) {
        try {
            log.info("删除公式追踪任务, taskId: {}", taskId);
            
            // 删除任务相关的结果
            formulaTraceResultMapper.deleteByTaskId(taskId);
            
            // 删除任务
            int rows = formulaTraceTaskMapper.deleteById(taskId);
            
            log.info("删除公式追踪任务成功, taskId: {}", taskId);
            return rows > 0;
        } catch (Exception e) {
            log.error("删除公式追踪任务失败", e);
            throw new ServiceException("删除任务失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchDeleteTasks(List<String> taskIds) {
        try {
            log.info("批量删除公式追踪任务, taskIds: {}", taskIds);
            
            if (taskIds == null || taskIds.isEmpty()) {
                return false;
            }
            
            // 批量删除结果
            formulaTraceResultMapper.batchDeleteByTaskIds(taskIds);
            
            // 批量删除任务
            int rows = formulaTraceTaskMapper.batchDeleteByIds(taskIds);
            
            log.info("批量删除公式追踪任务成功, 删除数量: {}", rows);
            return rows > 0;
        } catch (Exception e) {
            log.error("批量删除公式追踪任务失败", e);
            throw new ServiceException("批量删除任务失败: " + e.getMessage());
        }
    }

    @Override
    public FormulaTraceTask getTaskById(String taskId) {
        try {
            log.info("根据ID查询任务, taskId: {}", taskId);
            return formulaTraceTaskMapper.selectById(taskId);
        } catch (Exception e) {
            log.error("根据ID查询任务失败", e);
            throw new ServiceException("查询任务失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> executeTask(String taskId, String companyId) {
        try {
            log.info("执行公式追踪任务, taskId: {}, companyId: {}", taskId, companyId);

            // 查询任务
            FormulaTraceTask task = formulaTraceTaskMapper.selectById(taskId);
            if (task == null) {
                throw new ServiceException("任务不存在");
            }

            // 更新任务状态为执行中
            task.setStatus("executing");
            task.setExecuteTime(new Date());
            task.setUpdateTime(new Date());
            formulaTraceTaskMapper.updateById(task);

            try {
                // 解析公式ID列表
                String[] formulaIds = task.getFormulaIds().split(",");

                // 删除旧的追踪结果
                formulaTraceResultMapper.deleteByTaskId(taskId);

                // 执行追踪
                List<FormulaTraceResult> results = new ArrayList<>();
                for (String formulaId : formulaIds) {
                    formulaId = formulaId.trim();
                    if (StringUtils.hasText(formulaId)) {
                        // 追踪单个公式
                        List<FormulaTraceResult> formulaResults = traceFormula(
                                taskId, formulaId, task.getTraceType(), task.getTraceDepth(), companyId, 0, new HashSet<>());
                        results.addAll(formulaResults);
                    }
                }

                // 批量插入结果
                if (!results.isEmpty()) {
                    for (FormulaTraceResult result : results) {
                        formulaTraceResultMapper.insert(result);
                    }
                }

                // 更新任务状态为完成
                task.setStatus("completed");
                task.setCompleteTime(new Date());
                task.setUpdateTime(new Date());
                task.setResultData(String.valueOf(results.size()));
                task.setErrorMessage(null);
                formulaTraceTaskMapper.updateById(task);

                // 返回结果
                Map<String, Object> result = new HashMap<>();
                result.put("success", true);
                result.put("taskId", taskId);
                result.put("resultCount", results.size());
                result.put("message", "追踪任务执行成功");

                log.info("执行公式追踪任务成功, taskId: {}, resultCount: {}", taskId, results.size());
                return result;

            } catch (Exception e) {
                // 更新任务状态为失败
                task.setStatus("failed");
                task.setCompleteTime(new Date());
                task.setUpdateTime(new Date());
                task.setErrorMessage(e.getMessage());
                formulaTraceTaskMapper.updateById(task);

                throw e;
            }

        } catch (Exception e) {
            log.error("执行公式追踪任务失败", e);
            throw new ServiceException("执行任务失败: " + e.getMessage());
        }
    }

    /**
     * 追踪单个公式
     */
    private List<FormulaTraceResult> traceFormula(String taskId, String formulaId, String traceType,
                                                   Integer maxDepth, String companyId, int currentDepth, Set<String> visited) {
        List<FormulaTraceResult> results = new ArrayList<>();

        // 防止循环依赖
        if (visited.contains(formulaId)) {
            return results;
        }
        visited.add(formulaId);

        // 检查深度
        if (currentDepth >= maxDepth) {
            return results;
        }

        // 查询公式信息
        BudgetFormula formula = budgetFormulaMapper.selectById(formulaId);
        if (formula == null) {
            return results;
        }

        // 创建追踪结果
        FormulaTraceResult result = new FormulaTraceResult();
        result.setResultId("RESULT_" + idWorker.nextId());
        result.setTaskId(taskId);
        result.setFormulaId(formulaId);
        result.setFormulaName(formula.getFormulaName());
        result.setFormulaExpression(formula.getFormulaExpression());
        result.setTraceDepth(currentDepth);
        result.setCompanyId(companyId);
        result.setCreateTime(new Date());

        // 解析公式表达式，查找依赖的公式
        List<String> dependencyIds = parseDependencyFormulas(formula.getFormulaExpression());
        if (!dependencyIds.isEmpty()) {
            result.setDependencyFormulaIds(String.join(",", dependencyIds));

            // 查询依赖公式的名称
            List<String> dependencyNames = new ArrayList<>();
            for (String depId : dependencyIds) {
                BudgetFormula depFormula = budgetFormulaMapper.selectById(depId);
                if (depFormula != null) {
                    dependencyNames.add(depFormula.getFormulaName());
                }
            }
            result.setDependencyFormulaNames(String.join(",", dependencyNames));
        }

        // 构建追踪路径
        result.setTracePath(buildTracePath(formulaId, currentDepth));

        // 构建依赖树（简化版）
        result.setDependencyTree(buildDependencyTree(formula, dependencyIds, currentDepth));

        // 影响分析
        result.setImpactAnalysis(buildImpactAnalysis(formula, dependencyIds));

        results.add(result);

        // 递归追踪依赖的公式
        if ("forward".equals(traceType) && !dependencyIds.isEmpty()) {
            for (String depId : dependencyIds) {
                List<FormulaTraceResult> depResults = traceFormula(
                        taskId, depId, traceType, maxDepth, companyId, currentDepth + 1, visited);
                results.addAll(depResults);
            }
        }

        return results;
    }

    /**
     * 解析公式表达式中的依赖公式
     */
    private List<String> parseDependencyFormulas(String expression) {
        List<String> dependencies = new ArrayList<>();
        if (!StringUtils.hasText(expression)) {
            return dependencies;
        }

        // 简化的解析逻辑：查找形如 ${FORMULA_xxx} 的引用
        String pattern = "\\$\\{FORMULA_([^}]+)\\}";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(pattern);
        java.util.regex.Matcher m = p.matcher(expression);
        while (m.find()) {
            dependencies.add(m.group(1));
        }

        return dependencies;
    }

    /**
     * 构建追踪路径
     */
    private String buildTracePath(String formulaId, int depth) {
        StringBuilder path = new StringBuilder();
        for (int i = 0; i < depth; i++) {
            path.append("  ");
        }
        path.append("└─ ").append(formulaId);
        return path.toString();
    }

    /**
     * 构建依赖树
     */
    private String buildDependencyTree(BudgetFormula formula, List<String> dependencyIds, int depth) {
        StringBuilder tree = new StringBuilder();
        tree.append("{\"id\":\"").append(formula.getFormulaId()).append("\",");
        tree.append("\"name\":\"").append(formula.getFormulaName()).append("\",");
        tree.append("\"depth\":").append(depth).append(",");
        tree.append("\"children\":[");

        if (!dependencyIds.isEmpty()) {
            for (int i = 0; i < dependencyIds.size(); i++) {
                if (i > 0) tree.append(",");
                tree.append("{\"id\":\"").append(dependencyIds.get(i)).append("\"}");
            }
        }

        tree.append("]}");
        return tree.toString();
    }

    /**
     * 构建影响分析
     */
    private String buildImpactAnalysis(BudgetFormula formula, List<String> dependencyIds) {
        StringBuilder analysis = new StringBuilder();
        analysis.append("公式 ").append(formula.getFormulaName());

        if (dependencyIds.isEmpty()) {
            analysis.append(" 不依赖其他公式");
        } else {
            analysis.append(" 依赖 ").append(dependencyIds.size()).append(" 个公式");
        }

        return analysis.toString();
    }
}

