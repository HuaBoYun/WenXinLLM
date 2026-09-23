package com.management.accountant.oracle.service.advanced.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.management.accountant.oracle.entity.advanced.BudgetOptimization;
import com.management.accountant.oracle.mapper.advanced.BudgetOptimizationMapper;
import com.management.accountant.oracle.service.advanced.BudgetOptimizationService;
import com.management.accountant.util.SnowflakeIdWorker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.*;

@Slf4j
@Service("budgetOptimizationServiceOracle")
public class BudgetOptimizationServiceImpl implements BudgetOptimizationService {

    @Resource
    private BudgetOptimizationMapper optimizationMapper;
    private static final SnowflakeIdWorker idWorker = new SnowflakeIdWorker(1, 1);

    @Override
    public List<BudgetOptimization> selectList(Map<String, Object> params) {
        return optimizationMapper.selectList(buildWrapper(params));
    }

    @Override
    public Page<BudgetOptimization> selectPage(Map<String, Object> params, Integer pageNum, Integer pageSize) {
        return optimizationMapper.selectPage(new Page<>(pageNum, pageSize), buildWrapper(params));
    }

    @Override
    public BudgetOptimization selectById(String optimizationId) { return optimizationMapper.selectById(optimizationId); }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean insert(BudgetOptimization opt) {
        opt.setOptimizationId("OPT" + idWorker.nextId());
        opt.setCreateTime(new Date());
        opt.setDelFlag(0);
        return optimizationMapper.insert(opt) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean update(BudgetOptimization opt) {
        opt.setUpdateTime(new Date());
        return optimizationMapper.updateById(opt) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteById(String id) {
        BudgetOptimization o = optimizationMapper.selectById(id);
        if (o != null) { o.setDelFlag(1); o.setUpdateTime(new Date()); return optimizationMapper.updateById(o) > 0; }
        return false;
    }

    @Override
    public boolean runOptimization(String id) {
        BudgetOptimization o = optimizationMapper.selectById(id);
        if (o == null) return false;
        o.setOptimizationStatus("RUNNING");
        o.setUpdateTime(new Date());
        return optimizationMapper.updateById(o) > 0;
    }

    @Override
    public boolean stopOptimization(String id) {
        BudgetOptimization o = optimizationMapper.selectById(id);
        if (o == null) return false;
        o.setOptimizationStatus("STOPPED");
        o.setUpdateTime(new Date());
        return optimizationMapper.updateById(o) > 0;
    }

    @Override
    public boolean applyOptimization(String id) {
        BudgetOptimization o = optimizationMapper.selectById(id);
        if (o == null) return false;
        o.setOptimizationStatus("APPLIED");
        o.setUpdateTime(new Date());
        return optimizationMapper.updateById(o) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean copyOptimization(String id) {
        BudgetOptimization src = optimizationMapper.selectById(id);
        if (src == null) return false;
        BudgetOptimization copy = new BudgetOptimization();
        copy.setOptimizationId("OPT" + idWorker.nextId());
        copy.setTaskName(src.getTaskName() + " - 副本");
        copy.setOptimizationType(src.getOptimizationType());
        copy.setOptimizationStatus("PENDING");
        copy.setTargetBudget(src.getTargetBudget());
        copy.setDescription(src.getDescription());
        copy.setCreateBy(src.getCreateBy());
        copy.setCreateTime(new Date());
        copy.setDelFlag(0);
        return optimizationMapper.insert(copy) > 0;
    }

    @Override
    public Map<String, Object> getStats() {
        Map<String, Object> stats = new HashMap<>();
        QueryWrapper<BudgetOptimization> w = new QueryWrapper<>();
        w.eq("DEL_FLAG", 0);
        long total = optimizationMapper.selectCount(w);
        stats.put("totalOptimizations", total);

        QueryWrapper<BudgetOptimization> rw = new QueryWrapper<>();
        rw.eq("DEL_FLAG", 0).eq("OPTIMIZATION_STATUS", "RUNNING");
        stats.put("runningOptimizations", optimizationMapper.selectCount(rw));

        // 各优化类型任务数
        String[] types = {"RESOURCE_ALLOCATION", "COST_OPTIMIZATION", "EFFICIENCY_OPTIMIZATION", "PORTFOLIO_OPTIMIZATION"};
        Map<String, Long> typeCountMap = new HashMap<>();
        for (String t : types) {
            QueryWrapper<BudgetOptimization> tw = new QueryWrapper<>();
            tw.eq("DEL_FLAG", 0).eq("OPTIMIZATION_TYPE", t);
            typeCountMap.put(t, optimizationMapper.selectCount(tw));
        }
        stats.put("typeCountMap", typeCountMap);

        // 已完成任务数，用于计算平均改善和效率（简单统计）
        QueryWrapper<BudgetOptimization> cw = new QueryWrapper<>();
        cw.eq("DEL_FLAG", 0).eq("OPTIMIZATION_STATUS", "COMPLETED");
        long completed = optimizationMapper.selectCount(cw);
        stats.put("completedOptimizations", completed);
        // 效率 = 完成数/总数 百分比
        stats.put("efficiency", total > 0 ? Math.round((double) completed / total * 100) : 0);
        // avgImprovement 暂用固定估算（无专用字段），后续可扩展
        stats.put("avgImprovement", completed > 0 ? 15 : 0);
        // totalSavings 暂用完成任务数 * 估算节约额
        stats.put("totalSavings", completed > 0 ? completed * 100000 + "元" : "0元");
        return stats;
    }

    private QueryWrapper<BudgetOptimization> buildWrapper(Map<String, Object> params) {
        QueryWrapper<BudgetOptimization> w = new QueryWrapper<>();
        w.eq("DEL_FLAG", 0);
        if (params != null) {
            String keyword = (String) params.get("keyword");
            if (StringUtils.hasText(keyword)) w.like("TASK_NAME", keyword);
            // 前端传 optimizationType 字段
            String type = (String) params.get("optimizationType");
            if (StringUtils.hasText(type)) w.eq("OPTIMIZATION_TYPE", type);
        }
        w.orderByDesc("CREATE_TIME");
        return w;
    }
}
