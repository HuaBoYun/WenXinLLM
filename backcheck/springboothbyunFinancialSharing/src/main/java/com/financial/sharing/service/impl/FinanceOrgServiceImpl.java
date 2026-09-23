package com.financial.sharing.service.impl;

import com.financial.sharing.service.FinanceOrgService;
import com.financial.sharing.oracle.mapper.FinanceOrgMapper;
import com.financial.sharing.util.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 财务组织服务实现类
 */
@Service
public class FinanceOrgServiceImpl implements FinanceOrgService {

    @Autowired
    private FinanceOrgMapper financeOrgMapper;

    @Override
    public PageResult<Map<String, Object>> getCwzzList(Map<String, Object> params) {
        List<Map<String, Object>> list = financeOrgMapper.selectCwzzList(params);
        int total = financeOrgMapper.countCwzzList(params);
        PageResult<Map<String, Object>> result = new PageResult<>();
        result.setTlist(list);
        result.setTotal(total);
        return result;
    }

    @Override
    public boolean relateCompany(Map<String, Object> data) {
        return financeOrgMapper.relateCompany(data) > 0;
    }

    @Override
    public PageResult<Map<String, Object>> getCwzzTreeList(Map<String, Object> params) {
        List<Map<String, Object>> list = financeOrgMapper.selectCwzzTreeList(params);
        int total = financeOrgMapper.countCwzzTreeList(params);
        PageResult<Map<String, Object>> result = new PageResult<>();
        result.setTlist(list);
        result.setTotal(total);
        return result;
    }
}