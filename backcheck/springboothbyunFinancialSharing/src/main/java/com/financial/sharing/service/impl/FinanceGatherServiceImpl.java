package com.financial.sharing.service.impl;

import com.financial.sharing.service.FinanceGatherService;
import com.financial.sharing.oracle.mapper.FinanceGatherMapper;
import com.financial.sharing.util.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 财务数据收集服务实现类
 */
@Service
public class FinanceGatherServiceImpl implements FinanceGatherService {

    @Autowired
    private FinanceGatherMapper financeGatherMapper;

    @Override
    public List<Map<String, Object>> getGatherList(Map<String, Object> params) {
        return financeGatherMapper.selectGatherList(params);
    }

    @Override
    public Map<String, Object> getGatherDetail(String gatherId) {
        return financeGatherMapper.selectGatherById(gatherId);
    }

    @Override
    public int createGather(Map<String, Object> gather) {
        return financeGatherMapper.insertGather(gather);
    }

    @Override
    public int updateGather(Map<String, Object> gather) {
        return financeGatherMapper.updateGather(gather);
    }

    @Override
    public int deleteGather(String gatherId) {
        return financeGatherMapper.deleteGather(gatherId);
    }

    @Override
    public int submitGather(String gatherId) {
        Map<String, Object> params = new HashMap<>();
        params.put("gatherId", gatherId);
        params.put("status", "SUBMITTED");
        return financeGatherMapper.updateGatherStatus(params);
    }

    @Override
    public int approveGather(Map<String, Object> params) {
        return financeGatherMapper.updateGatherStatus(params);
    }

    @Override
    public Map<String, Object> getGatherStatistics(Map<String, Object> params) {
        return financeGatherMapper.selectGatherStatistics(params);
    }

    // ==================== Controller 需要的方法实现 ====================

    @Override
    public Map<String, Object> executeChouqu(Map<String, Object> params) {
        // 执行抽取任务
        financeGatherMapper.executeExtractTask(params);
        Map<String, Object> result = new HashMap<>();
        result.put("status", "executing");
        result.put("message", "抽取任务已启动");
        return result;
    }

    @Override
    public boolean stopChouqu(Map<String, Object> params) {
        return financeGatherMapper.stopExtractTask(params) > 0;
    }

    @Override
    public PageResult<Map<String, Object>> getFaTree(Map<String, Object> params) {
        List<Map<String, Object>> list = financeGatherMapper.selectPlanTreeList(params);
        int total = financeGatherMapper.countPlanTreeList(params);
        PageResult<Map<String, Object>> result = new PageResult<>();
        result.setTlist(list);
        result.setTotal(total);
        return result;
    }

    @Override
    public boolean stopFaGather(Map<String, Object> params) {
        return financeGatherMapper.stopPlanGather(params) > 0;
    }

    @Override
    public Map<String, Object> startFaGather(Map<String, Object> params) {
        financeGatherMapper.startPlanGather(params);
        Map<String, Object> result = new HashMap<>();
        result.put("status", "started");
        result.put("message", "方案采集已启动");
        return result;
    }

    @Override
    public Map<String, Object> getFaGatherStatus(Map<String, Object> params) {
        return financeGatherMapper.selectPlanGatherStatus(params);
    }

    @Override
    public boolean stopFaGatherSub(Map<String, Object> params) {
        return financeGatherMapper.stopSubPlanGather(params) > 0;
    }

    @Override
    public PageResult<Map<String, Object>> CaiJiLog(Map<String, Object> params) {
        List<Map<String, Object>> list = financeGatherMapper.selectGatherRecordList(params);
        int total = financeGatherMapper.countGatherRecordList(params);
        PageResult<Map<String, Object>> result = new PageResult<>();
        result.setTlist(list);
        result.setTotal(total);
        return result;
    }

    @Override
    public Map<String, Object> CaiJiLogResult(Map<String, Object> params) {
        return financeGatherMapper.selectGatherRecordResult(params);
    }
}