package com.global.treasurer.service.impl;

import com.global.treasurer.entity.TblSettlementException;
import com.global.treasurer.mapper.TblSettlementExceptionMapper;
import com.global.treasurer.service.SettlementExceptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class SettlementExceptionServiceImpl implements SettlementExceptionService {
    @Autowired
    private TblSettlementExceptionMapper exceptionMapper;

    @Override
    public Map<String, Object> getExceptionPage(Map<String, Object> params) {
        Map<String, Object> result = new HashMap<>();

        // 兼容前端分页参数: pageNum 和 pageSize
        Integer page = params.get("pageNum") != null ? Integer.parseInt(params.get("pageNum").toString()) :
                       (params.get("current") != null ? Integer.parseInt(params.get("current").toString()) : 1);
        Integer size = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) :
                       (params.get("size") != null ? Integer.parseInt(params.get("size").toString()) : 10);

        params.put("offset", (page - 1) * size);
        params.put("limit", size);

        List<TblSettlementException> list = exceptionMapper.selectExceptionPage(params);
        int total = exceptionMapper.countExceptionList(params);

        // 兼容前端数据格式: rows 和 total
        result.put("rows", list);
        result.put("total", total);

        return result;
    }

    @Override
    public TblSettlementException getExceptionById(Long exceptionId) {
        return exceptionMapper.selectById(exceptionId);
    }

    @Override
    public int createException(TblSettlementException exception) {
        exception.setExceptionNo("EXCP-" + System.currentTimeMillis());
        exception.setExceptionStatus("PENDING");
        exception.setDeleteFlag(0);
        exception.setCreateTime(new Date());
        return exceptionMapper.insert(exception);
    }

    @Override
    public int updateException(TblSettlementException exception) {
        exception.setUpdateTime(new Date());
        return exceptionMapper.updateById(exception);
    }

    @Override
    public int deleteException(List<Long> exceptionIds) {
        if (exceptionIds == null || exceptionIds.isEmpty()) return 0;
        int count = 0;
        for (Long id : exceptionIds) {
            TblSettlementException e = getExceptionById(id);
            if (e != null && "RESOLVED".equals(e.getExceptionStatus())) {
                e.setDeleteFlag(1);
                e.setUpdateTime(new Date());
                count += exceptionMapper.updateById(e);
            }
        }
        return count;
    }

    @Override
    public int assignException(Long exceptionId, String assignee) {
        TblSettlementException exception = getExceptionById(exceptionId);
        if (exception == null) return 0;
        exception.setAssignee(assignee);
        exception.setUpdateTime(new Date());
        return exceptionMapper.updateById(exception);
    }

    @Override
    public int resolveException(Long exceptionId, String resolveNotes) {
        TblSettlementException exception = getExceptionById(exceptionId);
        if (exception == null) return 0;
        exception.setExceptionStatus("RESOLVED");
        exception.setResolveNotes(resolveNotes);
        exception.setResolveTime(new Date());
        exception.setUpdateTime(new Date());
        return exceptionMapper.updateById(exception);
    }

    @Override
    public int escalateException(Long exceptionId, String escalateTo) {
        TblSettlementException exception = getExceptionById(exceptionId);
        if (exception == null) return 0;
        exception.setExceptionLevel("CRITICAL");
        exception.setAssignee(escalateTo);
        exception.setUpdateTime(new Date());
        return exceptionMapper.updateById(exception);
    }

    @Override
    public List<TblSettlementException> getUnresolvedExceptions(Long orgId) {
        return exceptionMapper.selectUnresolvedExceptions(orgId);
    }

    @Override
    public List<TblSettlementException> getCriticalExceptions(Long orgId) {
        return exceptionMapper.selectCriticalExceptions(orgId);
    }

    @Override
    public Map<String, Object> getExceptionSummary(Map<String, Object> params) {
        return exceptionMapper.selectExceptionSummary(params);
    }

    @Override
    public List<Map<String, Object>> getExceptionTrend(Map<String, Object> params) {
        return exceptionMapper.selectExceptionTrend(params);
    }

    @Override
    public List<Map<String, Object>> getRecurringPatterns(Map<String, Object> params) {
        List<Map<String, Object>> patterns = new ArrayList<>();
        Map<String, Object> pattern1 = new HashMap<>();
        pattern1.put("pattern", "周末失败率高");
        pattern1.put("frequency", "85%");
        pattern1.put("suggestion", "建议周末增加监控");
        patterns.add(pattern1);
        return patterns;
    }
}
