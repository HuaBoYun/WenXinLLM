package com.management.accountant.oracle.service.advanced.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.management.accountant.oracle.entity.advanced.BudgetSimulation;
import com.management.accountant.oracle.mapper.advanced.BudgetSimulationMapper;
import com.management.accountant.oracle.service.advanced.BudgetSimulationService;
import com.management.accountant.util.SnowflakeIdWorker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.*;

@Slf4j
@Service("budgetSimulationServiceOracle")
public class BudgetSimulationServiceImpl implements BudgetSimulationService {

    @Resource
    private BudgetSimulationMapper simulationMapper;
    private static final SnowflakeIdWorker idWorker = new SnowflakeIdWorker(1, 1);

    @Override
    public List<BudgetSimulation> selectList(Map<String, Object> params) {
        QueryWrapper<BudgetSimulation> w = new QueryWrapper<>();
        w.eq("DEL_FLAG", 0);
        if (params != null) {
            String keyword = (String) params.get("keyword");
            if (StringUtils.hasText(keyword)) w.like("SIMULATION_NAME", keyword);
            String type = (String) params.get("simulationType");
            if (StringUtils.hasText(type)) w.eq("SIMULATION_TYPE", type);
        }
        w.orderByDesc("CREATE_TIME");
        return simulationMapper.selectList(w);
    }

    @Override
    public Page<BudgetSimulation> selectPage(Map<String, Object> params, Integer pageNum, Integer pageSize) {
        Page<BudgetSimulation> page = new Page<>(pageNum, pageSize);
        QueryWrapper<BudgetSimulation> w = new QueryWrapper<>();
        w.eq("DEL_FLAG", 0);
        if (params != null) {
            String keyword = (String) params.get("keyword");
            if (StringUtils.hasText(keyword)) w.like("SIMULATION_NAME", keyword);
            String type = (String) params.get("simulationType");
            if (StringUtils.hasText(type)) w.eq("SIMULATION_TYPE", type);
        }
        w.orderByDesc("CREATE_TIME");
        return simulationMapper.selectPage(page, w);
    }

    @Override
    public BudgetSimulation selectById(String simulationId) { return simulationMapper.selectById(simulationId); }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean insert(BudgetSimulation sim) {
        sim.setSimulationId("SIM" + idWorker.nextId());
        sim.setCreateTime(new Date());
        sim.setDelFlag(0);
        return simulationMapper.insert(sim) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean update(BudgetSimulation sim) {
        sim.setUpdateTime(new Date());
        return simulationMapper.updateById(sim) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteById(String id) {
        BudgetSimulation s = simulationMapper.selectById(id);
        if (s != null) { s.setDelFlag(1); s.setUpdateTime(new Date()); return simulationMapper.updateById(s) > 0; }
        return false;
    }

    @Override
    public boolean executeSimulation(String id) {
        BudgetSimulation s = simulationMapper.selectById(id);
        if (s == null) return false;
        s.setSimulationStatus("IN_PROGRESS");
        s.setUpdateTime(new Date());
        return simulationMapper.updateById(s) > 0;
    }

    @Override
    public Map<String, Object> getSimulationResult(String id) {
        Map<String, Object> result = new HashMap<>();
        BudgetSimulation s = simulationMapper.selectById(id);
        if (s != null) {
            result.put("simulationId", s.getSimulationId());
            result.put("status", s.getSimulationStatus());
            result.put("result", s.getSimulationResult());
        }
        return result;
    }
}
