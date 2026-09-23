package com.financial.sharing.service.impl;

import com.financial.sharing.service.FinanceBookService;
import com.financial.sharing.oracle.mapper.FinanceBookMapper;
import com.financial.sharing.util.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 财务账簿服务实现类
 */
@Service
public class FinanceBookServiceImpl implements FinanceBookService {

    @Autowired
    private FinanceBookMapper financeBookMapper;

    // ==================== 账簿管理 ====================

    @Override
    public PageResult<Map<String, Object>> getZbglList(Map<String, Object> params) {
        List<Map<String, Object>> list = financeBookMapper.selectZbglList(params);
        int total = financeBookMapper.countZbglList(params);
        PageResult<Map<String, Object>> result = new PageResult<>();
        result.setTlist(list);
        result.setTotal(total);
        return result;
    }

    @Override
    public Map<String, Object> getZbglDetail(Map<String, Object> params) {
        return financeBookMapper.selectZbglDetail(params);
    }

    @Override
    public Map<String, Object> saveZbgl(Map<String, Object> data) {
        financeBookMapper.insertZbgl(data);
        return data;
    }

    @Override
    public boolean deleteZbgl(Map<String, Object> data) {
        return financeBookMapper.deleteZbgl(data) > 0;
    }

    // ==================== 账簿类型管理 ====================

    @Override
    public PageResult<Map<String, Object>> getZblxList(Map<String, Object> params) {
        List<Map<String, Object>> list = financeBookMapper.selectZblxList(params);
        int total = financeBookMapper.countZblxList(params);
        PageResult<Map<String, Object>> result = new PageResult<>();
        result.setTlist(list);
        result.setTotal(total);
        return result;
    }

    @Override
    public Map<String, Object> getZblxDetail(Map<String, Object> params) {
        return financeBookMapper.selectZblxDetail(params);
    }

    @Override
    public Map<String, Object> saveZblx(Map<String, Object> data) {
        financeBookMapper.insertZblx(data);
        return data;
    }

    @Override
    public boolean deleteZblx(Map<String, Object> data) {
        return financeBookMapper.deleteZblx(data) > 0;
    }

    // ==================== 账簿授权管理 ====================

    @Override
    public boolean zbglAuthRole(Map<String, Object> data) {
        return financeBookMapper.insertZbglAuthRole(data) > 0;
    }

    @Override
    public PageResult<Map<String, Object>> zbglAuthRoleList(Map<String, Object> params) {
        List<Map<String, Object>> list = financeBookMapper.selectZbglAuthRoleList(params);
        int total = financeBookMapper.countZbglAuthRoleList(params);
        PageResult<Map<String, Object>> result = new PageResult<>();
        result.setTlist(list);
        result.setTotal(total);
        return result;
    }

    @Override
    public boolean zbglCancelAuthRole(Map<String, Object> data) {
        return financeBookMapper.deleteZbglAuthRole(data) > 0;
    }

    @Override
    public PageResult<Map<String, Object>> getGsZbglAuthRoleList(Map<String, Object> params) {
        List<Map<String, Object>> list = financeBookMapper.selectGsZbglAuthRoleList(params);
        int total = financeBookMapper.countGsZbglAuthRoleList(params);
        PageResult<Map<String, Object>> result = new PageResult<>();
        result.setTlist(list);
        result.setTotal(total);
        return result;
    }

    @Override
    public boolean sureGsZb(Map<String, Object> data) {
        return financeBookMapper.updateGsZbSelection(data) > 0;
    }
}