package com.management.accountant.oracle.service.advanced.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.management.accountant.oracle.entity.advanced.FormulaTrace;
import com.management.accountant.oracle.mapper.advanced.FormulaTraceMapper;
import com.management.accountant.oracle.service.advanced.FormulaTraceService;
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
@Service("formulaTraceServiceOracle")
public class FormulaTraceServiceImpl implements FormulaTraceService {

    @Resource
    private FormulaTraceMapper traceMapper;
    private static final SnowflakeIdWorker idWorker = new SnowflakeIdWorker(1, 1);

    @Override
    public List<FormulaTrace> selectList(Map<String, Object> params) {
        QueryWrapper<FormulaTrace> w = new QueryWrapper<>();
        w.eq("DEL_FLAG", 0);
        if (params != null) {
            String keyword = (String) params.get("keyword");
            if (StringUtils.hasText(keyword)) w.like("FORMULA_NAME", keyword);
        }
        w.orderByDesc("CREATE_TIME");
        return traceMapper.selectList(w);
    }

    @Override
    public Page<FormulaTrace> selectPage(Map<String, Object> params, Integer pageNum, Integer pageSize) {
        QueryWrapper<FormulaTrace> w = new QueryWrapper<>();
        w.eq("DEL_FLAG", 0);
        if (params != null) {
            String keyword = (String) params.get("keyword");
            if (StringUtils.hasText(keyword)) w.like("FORMULA_NAME", keyword);
        }
        w.orderByDesc("CREATE_TIME");
        return traceMapper.selectPage(new Page<>(pageNum, pageSize), w);
    }

    @Override
    public FormulaTrace selectById(String traceId) { return traceMapper.selectById(traceId); }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean insert(FormulaTrace trace) {
        trace.setTraceId("FT" + idWorker.nextId());
        trace.setCreateTime(new Date());
        trace.setDelFlag(0);
        return traceMapper.insert(trace) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean update(FormulaTrace trace) {
        trace.setUpdateTime(new Date());
        return traceMapper.updateById(trace) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteById(String id) {
        FormulaTrace t = traceMapper.selectById(id);
        if (t != null) { t.setDelFlag(1); t.setUpdateTime(new Date()); return traceMapper.updateById(t) > 0; }
        return false;
    }

    @Override
    public boolean createTraceTask(String formulaId, String traceType, Integer traceDepth) {
        FormulaTrace trace = new FormulaTrace();
        trace.setTraceId("FT" + idWorker.nextId());
        trace.setFormulaId(formulaId);
        trace.setTraceType(traceType);
        trace.setTraceDepth(traceDepth);
        trace.setTraceStatus("PENDING");
        trace.setCreateTime(new Date());
        trace.setDelFlag(0);
        return traceMapper.insert(trace) > 0;
    }

    @Override
    public Map<String, Object> getFormulaDependencies(String formulaId) {
        Map<String, Object> result = new HashMap<>();
        result.put("formulaId", formulaId);
        result.put("dependencies", new ArrayList<>());
        return result;
    }

    @Override
    public Map<String, Object> getFormulaImpactAnalysis(String formulaId) {
        Map<String, Object> result = new HashMap<>();
        result.put("formulaId", formulaId);
        result.put("impacts", new ArrayList<>());
        return result;
    }
}
