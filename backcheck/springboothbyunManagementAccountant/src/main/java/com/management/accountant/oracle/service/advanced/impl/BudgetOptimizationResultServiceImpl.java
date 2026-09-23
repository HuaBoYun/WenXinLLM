package com.management.accountant.oracle.service.advanced.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.management.accountant.oracle.entity.advanced.BudgetOptimizationResult;
import com.management.accountant.oracle.mapper.advanced.BudgetOptimizationResultMapper;
import com.management.accountant.oracle.service.advanced.BudgetOptimizationResultService;
import com.management.accountant.util.SnowflakeIdWorker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class BudgetOptimizationResultServiceImpl implements BudgetOptimizationResultService {

    @Resource
    private BudgetOptimizationResultMapper resultMapper;

    private static final SnowflakeIdWorker idWorker = new SnowflakeIdWorker(2, 1);

    @Override
    public List<BudgetOptimizationResult> listByOptimizationId(String optimizationId) {
        QueryWrapper<BudgetOptimizationResult> w = new QueryWrapper<>();
        w.eq("OPTIMIZATION_ID", optimizationId).eq("DEL_FLAG", 0).orderByAsc("CREATE_TIME");
        return resultMapper.selectList(w);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveBatch(List<BudgetOptimizationResult> results) {
        for (BudgetOptimizationResult r : results) {
            r.setResultId("RES" + idWorker.nextId());
            r.setCreateTime(new Date());
            r.setDelFlag(0);
            resultMapper.insert(r);
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteByOptimizationId(String optimizationId) {
        QueryWrapper<BudgetOptimizationResult> w = new QueryWrapper<>();
        w.eq("OPTIMIZATION_ID", optimizationId);
        BudgetOptimizationResult update = new BudgetOptimizationResult();
        update.setDelFlag(1);
        return resultMapper.update(update, w) >= 0;
    }
}
