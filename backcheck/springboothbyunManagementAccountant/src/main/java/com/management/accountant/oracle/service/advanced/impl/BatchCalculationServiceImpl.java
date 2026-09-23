package com.management.accountant.oracle.service.advanced.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.management.accountant.oracle.entity.advanced.BatchCalculation;
import com.management.accountant.oracle.mapper.advanced.BatchCalculationMapper;
import com.management.accountant.oracle.service.advanced.BatchCalculationService;
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
@Service("batchCalculationServiceOracle")
public class BatchCalculationServiceImpl implements BatchCalculationService {

    @Resource
    private BatchCalculationMapper calculationMapper;
    private static final SnowflakeIdWorker idWorker = new SnowflakeIdWorker(1, 1);

    @Override
    public List<BatchCalculation> selectList(Map<String, Object> params) {
        QueryWrapper<BatchCalculation> w = new QueryWrapper<>();
        w.eq("DEL_FLAG", 0);
        if (params != null) {
            String keyword = (String) params.get("keyword");
            if (StringUtils.hasText(keyword)) w.like("CALCULATION_NAME", keyword);
            String type = params.get("calculationType") != null ? params.get("calculationType").toString() : (String) params.get("type");
            if (StringUtils.hasText(type)) w.eq("CALCULATION_TYPE", type);
        }
        w.orderByDesc("CREATE_TIME");
        return calculationMapper.selectList(w);
    }

    @Override
    public Page<BatchCalculation> selectPage(Map<String, Object> params, Integer pageNum, Integer pageSize) {
        Page<BatchCalculation> page = new Page<>(pageNum, pageSize);
        QueryWrapper<BatchCalculation> w = new QueryWrapper<>();
        w.eq("DEL_FLAG", 0);
        if (params != null) {
            String keyword = (String) params.get("keyword");
            if (StringUtils.hasText(keyword)) w.like("CALCULATION_NAME", keyword);
            String type = params.get("calculationType") != null ? params.get("calculationType").toString() : (String) params.get("type");
            if (StringUtils.hasText(type)) w.eq("CALCULATION_TYPE", type);
        }
        w.orderByDesc("CREATE_TIME");
        return calculationMapper.selectPage(page, w);
    }

    @Override
    public BatchCalculation selectById(String calculationId) { return calculationMapper.selectById(calculationId); }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean insert(BatchCalculation calc) {
        calc.setCalculationId("BC" + idWorker.nextId());
        calc.setCreateTime(new Date());
        calc.setDelFlag(0);
        return calculationMapper.insert(calc) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean update(BatchCalculation calc) {
        calc.setUpdateTime(new Date());
        return calculationMapper.updateById(calc) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteById(String id) {
        BatchCalculation c = calculationMapper.selectById(id);
        if (c != null) { c.setDelFlag(1); c.setUpdateTime(new Date()); return calculationMapper.updateById(c) > 0; }
        return false;
    }

    @Override
    public boolean executeCalculation(String id) {
        BatchCalculation c = calculationMapper.selectById(id);
        if (c == null) return false;
        c.setCalculationStatus("RUNNING");
        c.setStartTime(new Date());
        c.setUpdateTime(new Date());
        return calculationMapper.updateById(c) > 0;
    }

    @Override
    public Map<String, Object> getCalculationResult(String id) {
        Map<String, Object> result = new HashMap<>();
        BatchCalculation c = calculationMapper.selectById(id);
        if (c != null) {
            result.put("calculationId", c.getCalculationId());
            result.put("status", c.getCalculationStatus());
            result.put("totalItems", c.getTotalItems());
            result.put("completedItems", c.getCompletedItems());
            result.put("failedItems", c.getFailedItems());
        }
        return result;
    }
}
