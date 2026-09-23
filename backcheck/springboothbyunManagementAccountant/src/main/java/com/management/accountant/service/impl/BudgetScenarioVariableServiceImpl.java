package com.management.accountant.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.management.accountant.exception.ServiceException;
import com.management.accountant.oracle.entity.budget.BudgetScenarioVariable;
import com.management.accountant.oracle.mapper.budget.BudgetScenarioVariableMapper;
import com.management.accountant.service.BudgetScenarioVariableService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 预算场景变量 ServiceImpl
 */
@Service
@Slf4j
public class BudgetScenarioVariableServiceImpl implements BudgetScenarioVariableService {

    @Resource
    private BudgetScenarioVariableMapper variableMapper;

    @Override
    public Map<String, Object> getPage(Map<String, Object> params) {
        int pageNum = params.get("pageNum") != null ? Integer.parseInt(params.get("pageNum").toString()) : 1;
        int pageSize = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 20;

        QueryWrapper<BudgetScenarioVariable> wrapper = new QueryWrapper<>();
        String scenarioId = (String) params.get("scenarioId");
        if (StringUtils.hasText(scenarioId)) {
            wrapper.eq("SCENARIO_ID", scenarioId);
        }
        String variableName = (String) params.get("variableName");
        if (StringUtils.hasText(variableName)) {
            wrapper.like("VARIABLE_NAME", variableName);
        }
        String variableType = (String) params.get("variableType");
        if (StringUtils.hasText(variableType)) {
            wrapper.eq("VARIABLE_TYPE", variableType);
        }
        String analysisStatus = (String) params.get("analysisStatus");
        if (StringUtils.hasText(analysisStatus)) {
            wrapper.eq("ANALYSIS_STATUS", analysisStatus);
        }
        wrapper.orderByDesc("CREATE_TIME");

        PageHelper.startPage(pageNum, pageSize);
        List<BudgetScenarioVariable> list = variableMapper.selectList(wrapper);
        PageInfo<BudgetScenarioVariable> pageInfo = new PageInfo<>(list);

        Map<String, Object> result = new HashMap<>();
        result.put("list", pageInfo.getList());
        result.put("tlist", pageInfo.getList());
        result.put("total", pageInfo.getTotal());
        result.put("totalRecord", pageInfo.getTotal());
        result.put("pageNum", pageInfo.getPageNum());
        result.put("pageNo", pageInfo.getPageNum());
        result.put("pageSize", pageInfo.getPageSize());
        result.put("totalPage", pageInfo.getPages());
        return result;
    }

    @Override
    public Map<String, Object> createVariable(Map<String, Object> params) {
        String variableName = (String) params.get("variableName");
        if (!StringUtils.hasText(variableName)) {
            throw new ServiceException("变量名称不能为空");
        }
        BudgetScenarioVariable variable = new BudgetScenarioVariable();
        variable.setId(UUID.randomUUID().toString().replace("-", ""));
        variable.setVariableCode("VAR_" + System.currentTimeMillis());
        variable.setVariableName(variableName);
        variable.setVariableType((String) params.get("variableType"));
        variable.setScenarioId((String) params.get("scenarioId"));
        variable.setDistribution((String) params.get("distribution"));
        variable.setDescription((String) params.get("description"));
        variable.setAnalysisStatus("ACTIVE");
        if (params.get("baseValue") != null) {
            variable.setBaseValue(new BigDecimal(params.get("baseValue").toString()));
        }
        if (params.get("optimisticValue") != null) {
            variable.setOptimisticValue(new BigDecimal(params.get("optimisticValue").toString()));
        }
        if (params.get("pessimisticValue") != null) {
            variable.setPessimisticValue(new BigDecimal(params.get("pessimisticValue").toString()));
        }
        if (params.get("correlation") != null) {
            variable.setCorrelation(new BigDecimal(params.get("correlation").toString()));
        }
        variable.setCreateTime(new Date());
        variable.setUpdateTime(new Date());
        variableMapper.insert(variable);

        Map<String, Object> result = new HashMap<>();
        result.put("id", variable.getId());
        result.put("variableName", variable.getVariableName());
        return result;
    }

    @Override
    public Map<String, Object> updateVariable(Map<String, Object> params) {
        String id = (String) params.get("id");
        if (!StringUtils.hasText(id)) {
            throw new ServiceException("变量ID不能为空");
        }
        BudgetScenarioVariable variable = variableMapper.selectById(id);
        if (variable == null) {
            throw new ServiceException("变量不存在");
        }
        if (params.get("variableName") != null) variable.setVariableName((String) params.get("variableName"));
        if (params.get("variableType") != null) variable.setVariableType((String) params.get("variableType"));
        if (params.get("distribution") != null) variable.setDistribution((String) params.get("distribution"));
        if (params.get("description") != null) variable.setDescription((String) params.get("description"));
        if (params.get("analysisStatus") != null) variable.setAnalysisStatus((String) params.get("analysisStatus"));
        if (params.get("baseValue") != null) variable.setBaseValue(new BigDecimal(params.get("baseValue").toString()));
        if (params.get("optimisticValue") != null) variable.setOptimisticValue(new BigDecimal(params.get("optimisticValue").toString()));
        if (params.get("pessimisticValue") != null) variable.setPessimisticValue(new BigDecimal(params.get("pessimisticValue").toString()));
        if (params.get("correlation") != null) variable.setCorrelation(new BigDecimal(params.get("correlation").toString()));
        variable.setUpdateTime(new Date());
        variableMapper.updateById(variable);

        Map<String, Object> result = new HashMap<>();
        result.put("id", variable.getId());
        return result;
    }

    @Override
    public void deleteVariable(String id) {
        if (!StringUtils.hasText(id)) {
            throw new ServiceException("变量ID不能为空");
        }
        BudgetScenarioVariable variable = variableMapper.selectById(id);
        if (variable == null) {
            throw new ServiceException("变量不存在");
        }
        variableMapper.deleteById(id);
    }
}
