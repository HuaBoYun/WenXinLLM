package com.financial.sharing.service.impl;

import com.financial.sharing.service.FinanceDataService;
import com.financial.sharing.oracle.mapper.FinanceDataMapper;
import com.financial.sharing.util.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 财务数据服务实现类
 */
@Service
public class FinanceDataServiceImpl implements FinanceDataService {

    @Autowired
    private FinanceDataMapper financeDataMapper;

    // ==================== 会计科目表 ====================

    @Override
    public PageResult<Map<String, Object>> getKmList(Map<String, Object> params) {
        List<Map<String, Object>> list = financeDataMapper.selectKmList(params);
        int total = financeDataMapper.countKmList(params);
        PageResult<Map<String, Object>> result = new PageResult<>();
        result.setTlist(list);
        result.setTotalRecord(total);
        // 设置分页信息
        result.setCurrentPage(params.get("pageNo") != null ? (Integer) params.get("pageNo") : 1);
        result.setPageSize(params.get("pageSize") != null ? (Integer) params.get("pageSize") : 20);
        return result;
    }

    @Override
    public PageResult<Map<String, Object>> getPzkList(Map<String, Object> params) {
        List<Map<String, Object>> list = financeDataMapper.selectPzkList(params);
        int total = financeDataMapper.countPzkList(params);
        PageResult<Map<String, Object>> result = new PageResult<>();
        result.setTlist(list);
        result.setTotal(total);
        return result;
    }

    // ==================== 辅助账管理 ====================

    @Override
    public PageResult<Map<String, Object>> getAuxiliaryInfoTree(Map<String, Object> params) {
        List<Map<String, Object>> list = financeDataMapper.selectAuxiliaryInfoTree(params);
        int total = financeDataMapper.countAuxiliaryInfoTree(params);
        PageResult<Map<String, Object>> result = new PageResult<>();
        result.setTlist(list);
        result.setTotal(total);
        return result;
    }

    @Override
    public PageResult<Map<String, Object>> getAuxiliaryBalanceTable(Map<String, Object> params) {
        List<Map<String, Object>> list = financeDataMapper.selectAuxiliaryBalanceTable(params);
        int total = financeDataMapper.countAuxiliaryBalanceTable(params);
        PageResult<Map<String, Object>> result = new PageResult<>();
        result.setTlist(list);
        result.setTotal(total);
        return result;
    }

    @Override
    public PageResult<Map<String, Object>> getAuxiliaryInfoTable(Map<String, Object> params) {
        List<Map<String, Object>> list = financeDataMapper.selectAuxiliaryInfoTable(params);
        int total = financeDataMapper.countAuxiliaryInfoTable(params);
        PageResult<Map<String, Object>> result = new PageResult<>();
        result.setTlist(list);
        result.setTotal(total);
        return result;
    }

    @Override
    public PageResult<Map<String, Object>> getAuxiliaryTotalTable(Map<String, Object> params) {
        List<Map<String, Object>> list = financeDataMapper.selectAuxiliaryTotalTable(params);
        int total = financeDataMapper.countAuxiliaryTotalTable(params);
        PageResult<Map<String, Object>> result = new PageResult<>();
        result.setTlist(list);
        result.setTotal(total);
        return result;
    }

    // ==================== 账簿管理 ====================

    @Override
    public PageResult<Map<String, Object>> getAuxiliaryDetailTable(Map<String, Object> params) {
        List<Map<String, Object>> list = financeDataMapper.selectAuxiliaryDetailTable(params);
        int total = financeDataMapper.countAuxiliaryDetailTable(params);
        PageResult<Map<String, Object>> result = new PageResult<>();
        result.setTlist(list);
        result.setTotal(total);
        return result;
    }

    @Override
    public PageResult<Map<String, Object>> getDiaryBookList(Map<String, Object> params) {
        List<Map<String, Object>> list = financeDataMapper.selectDiaryBookList(params);
        int total = financeDataMapper.countDiaryBookList(params);
        PageResult<Map<String, Object>> result = new PageResult<>();
        result.setTlist(list);
        result.setTotal(total);
        return result;
    }

    @Override
    public PageResult<Map<String, Object>> getTotalAccountList(Map<String, Object> params) {
        List<Map<String, Object>> list = financeDataMapper.selectTotalAccountList(params);
        int total = financeDataMapper.countTotalAccountList(params);
        PageResult<Map<String, Object>> result = new PageResult<>();
        result.setTlist(list);
        result.setTotal(total);
        return result;
    }

    @Override
    public PageResult<Map<String, Object>> getYebList(Map<String, Object> params) {
        List<Map<String, Object>> list = financeDataMapper.selectYebList(params);
        int total = financeDataMapper.countYebList(params);
        PageResult<Map<String, Object>> result = new PageResult<>();
        result.setTlist(list);
        result.setTotal(total);
        return result;
    }

    @Override
    public PageResult<Map<String, Object>> getFinanceDataList(Map<String, Object> params) {
        List<Map<String, Object>> list = financeDataMapper.selectFinanceDataList(params);
        int total = financeDataMapper.countFinanceDataList(params);
        PageResult<Map<String, Object>> result = new PageResult<>();
        result.setTlist(list);
        result.setTotal(total);
        return result;
    }
}