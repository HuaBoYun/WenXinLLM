package com.management.accountant.oracle.service.advanced.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.management.accountant.oracle.entity.advanced.RollingBudgetPlan;
import com.management.accountant.oracle.mapper.advanced.RollingBudgetPlanMapper;
import com.management.accountant.oracle.service.advanced.RollingBudgetPlanService;
import com.management.accountant.util.SnowflakeIdWorker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.*;

@Slf4j
@Primary
@Service("rollingBudgetPlanServiceOracle")
public class RollingBudgetPlanServiceImpl implements RollingBudgetPlanService {

    @Resource
    private RollingBudgetPlanMapper planMapper;
    private static final SnowflakeIdWorker idWorker = new SnowflakeIdWorker(1, 1);

    @Override
    public List<RollingBudgetPlan> selectList(Map<String, Object> params) {
        QueryWrapper<RollingBudgetPlan> w = new QueryWrapper<>();
        w.eq("DEL_FLAG", 0);
        if (params != null) {
            String keyword = (String) params.get("keyword");
            if (StringUtils.hasText(keyword)) w.like("PLAN_NAME", keyword);
        }
        w.orderByDesc("CREATE_TIME");
        return planMapper.selectList(w);
    }

    @Override
    public Page<RollingBudgetPlan> selectPage(Map<String, Object> params, Integer pageNum, Integer pageSize) {
        QueryWrapper<RollingBudgetPlan> w = new QueryWrapper<>();
        w.eq("DEL_FLAG", 0);
        if (params != null) {
            String keyword = (String) params.get("keyword");
            if (StringUtils.hasText(keyword)) w.like("PLAN_NAME", keyword);
        }
        w.orderByDesc("CREATE_TIME");
        return planMapper.selectPage(new Page<>(pageNum, pageSize), w);
    }

    @Override
    public RollingBudgetPlan selectById(String planId) { return planMapper.selectById(planId); }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean insert(RollingBudgetPlan plan) {
        plan.setPlanId("RP" + idWorker.nextId());
        plan.setCreateTime(new Date());
        plan.setDelFlag(0);
        return planMapper.insert(plan) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean update(RollingBudgetPlan plan) {
        plan.setUpdateTime(new Date());
        return planMapper.updateById(plan) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteById(String id) {
        RollingBudgetPlan p = planMapper.selectById(id);
        if (p != null) { p.setDelFlag(1); p.setUpdateTime(new Date()); return planMapper.updateById(p) > 0; }
        return false;
    }

    @Override
    public boolean executePlan(String id) {
        RollingBudgetPlan p = planMapper.selectById(id);
        if (p == null) return false;
        p.setPlanStatus("RUNNING");
        p.setLastExecutionTime(new Date());
        p.setUpdateTime(new Date());
        return planMapper.updateById(p) > 0;
    }

    @Override
    public boolean activatePlan(String id) {
        RollingBudgetPlan p = planMapper.selectById(id);
        if (p == null) return false;
        p.setPlanStatus("ACTIVE");
        p.setUpdateTime(new Date());
        return planMapper.updateById(p) > 0;
    }

    @Override
    public boolean pausePlan(String id) {
        RollingBudgetPlan p = planMapper.selectById(id);
        if (p == null) return false;
        p.setPlanStatus("PAUSED");
        p.setUpdateTime(new Date());
        return planMapper.updateById(p) > 0;
    }

    @Override
    public List<RollingBudgetPlan> selectByRollingType(String rollingType) {
        QueryWrapper<RollingBudgetPlan> w = new QueryWrapper<>();
        w.eq("DEL_FLAG", 0).eq("ROLLING_TYPE", rollingType);
        return planMapper.selectList(w);
    }
}
