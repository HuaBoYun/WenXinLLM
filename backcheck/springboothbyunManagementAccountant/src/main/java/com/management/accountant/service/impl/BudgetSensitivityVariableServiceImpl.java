package com.management.accountant.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.management.accountant.exception.ServiceException;
import com.management.accountant.oracle.entity.budget.BudgetSensitivityVariable;
import com.management.accountant.oracle.mapper.budget.BudgetSensitivityVariableMapper;
import com.management.accountant.service.BudgetSensitivityVariableService;
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
 * 预算敏感性变量 ServiceImpl
 */
@Service
@Slf4j
public class BudgetSensitivityVariableServiceImpl implements BudgetSensitivityVariableService {

    @Resource
    private BudgetSensitivityVariableMapper variableMapper;

    @Override
    public Map<String, Object> getPage(Map<String, Object> params) {
        int pageNum = params.get("pageNum") != null ? Integer.parseInt(params.get("pageNum").toString()) : 1;
        int pageSize = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 20;

        QueryWrapper<BudgetSensitivityVariable> wrapper = new QueryWrapper<>();
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
        Object enabledObj = params.get("enabled");
        if (enabledObj != null && !enabledObj.toString().isEmpty()) {
            wrapper.eq("ENABLED", Integer.parseInt(enabledObj.toString()));
        }
        wrapper.orderByDesc("PRIORITY").orderByDesc("CREATE_TIME");

        PageHelper.startPage(pageNum, pageSize);
        List<BudgetSensitivityVariable> list = variableMapper.selectList(wrapper);
        PageInfo<BudgetSensitivityVariable> pageInfo = new PageInfo<>(list);

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
        BudgetSensitivityVariable variable = new BudgetSensitivityVariable();
        variable.setId(UUID.randomUUID().toString().replace("-", ""));
        variable.setVariableCode("SVAR_" + System.currentTimeMillis());
        variable.setVariableName(variableName);
        variable.setVariableType((String) params.get("variableType"));
        variable.setDescription((String) params.get("description"));
        variable.setAnalysisStatus("ACTIVE");
        variable.setEnabled(params.get("enabled") != null ? Integer.parseInt(params.get("enabled").toString()) : 1);
        variable.setPriority(params.get("priority") != null ? Integer.parseInt(params.get("priority").toString()) : 1);
        if (params.get("baseValue") != null) variable.setBaseValue(new BigDecimal(params.get("baseValue").toString()));
        if (params.get("minValue") != null) variable.setMinValue(new BigDecimal(params.get("minValue").toString()));
        if (params.get("maxValue") != null) variable.setMaxValue(new BigDecimal(params.get("maxValue").toString()));
        if (params.get("stepSize") != null) variable.setStepSize(new BigDecimal(params.get("stepSize").toString()));
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
        if (!StringUtils.hasText(id)) throw new ServiceException("变量ID不能为空");
        BudgetSensitivityVariable variable = variableMapper.selectById(id);
        if (variable == null) throw new ServiceException("变量不存在");

        if (params.get("variableName") != null) variable.setVariableName((String) params.get("variableName"));
        if (params.get("variableType") != null) variable.setVariableType((String) params.get("variableType"));
        if (params.get("description") != null) variable.setDescription((String) params.get("description"));
        if (params.get("analysisStatus") != null) variable.setAnalysisStatus((String) params.get("analysisStatus"));
        if (params.get("enabled") != null) variable.setEnabled(Integer.parseInt(params.get("enabled").toString()));
        if (params.get("priority") != null) variable.setPriority(Integer.parseInt(params.get("priority").toString()));
        if (params.get("baseValue") != null) variable.setBaseValue(new BigDecimal(params.get("baseValue").toString()));
        if (params.get("minValue") != null) variable.setMinValue(new BigDecimal(params.get("minValue").toString()));
        if (params.get("maxValue") != null) variable.setMaxValue(new BigDecimal(params.get("maxValue").toString()));
        if (params.get("stepSize") != null) variable.setStepSize(new BigDecimal(params.get("stepSize").toString()));
        variable.setUpdateTime(new Date());
        variableMapper.updateById(variable);

        Map<String, Object> result = new HashMap<>();
        result.put("id", variable.getId());
        return result;
    }

    @Override
    public void deleteVariable(String id) {
        if (!StringUtils.hasText(id)) throw new ServiceException("变量ID不能为空");
        BudgetSensitivityVariable variable = variableMapper.selectById(id);
        if (variable == null) throw new ServiceException("变量不存在");
        variableMapper.deleteById(id);
    }

    @Override
    public Map<String, Object> updateEnabled(String id, Integer enabled) {
        if (!StringUtils.hasText(id)) throw new ServiceException("变量ID不能为空");
        BudgetSensitivityVariable variable = variableMapper.selectById(id);
        if (variable == null) throw new ServiceException("变量不存在");
        variable.setEnabled(enabled);
        variable.setUpdateTime(new Date());
        variableMapper.updateById(variable);
        Map<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("enabled", enabled);
        return result;
    }
}
