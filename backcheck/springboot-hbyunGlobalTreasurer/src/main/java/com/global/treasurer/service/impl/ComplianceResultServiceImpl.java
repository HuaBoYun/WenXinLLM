package com.global.treasurer.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.global.treasurer.entity.TblComplianceResult;
import com.global.treasurer.mapper.ComplianceResultMapper;
import com.global.treasurer.service.ComplianceResultService;
import com.global.treasurer.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 合规检查结果服务实现类
 *
 * @author 华博云开发团队
 * @since 2026-01-22
 */
@Service
public class ComplianceResultServiceImpl implements ComplianceResultService {
    @Autowired
    private ComplianceResultMapper resultMapper;

    @Override
    public PageInfo<TblComplianceResult> getResultList(Map<String, Object> params) {
        int pageNum = params.get("pageNum") != null ? (Integer) params.get("pageNum") : 1;
        int pageSize = params.get("pageSize") != null ? (Integer) params.get("pageSize") : 10;
        PageHelper.startPage(pageNum, pageSize);
        List<TblComplianceResult> list = resultMapper.selectResultList(params);
        return new PageInfo<>(list);
    }

    @Override
    public TblComplianceResult getResultById(String resultId) {
        TblComplianceResult result = resultMapper.selectResultById(resultId);
        if (result == null) {
            throw new ServiceException(404, "合规检查结果不存在");
        }
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TblComplianceResult saveResult(TblComplianceResult result) {
        if (result.getResultId() == null || result.getResultId().isEmpty()) {
            result.setDeleteFlag(0);
            result.setCheckTime(new Date());
            result.setCreatedTime(new Date());
            resultMapper.insert(result);
        } else {
            result.setUpdatedTime(new Date());
            resultMapper.updateById(result);
        }
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteResult(String resultId) {
        TblComplianceResult result = getResultById(resultId);
        result.setDeleteFlag(1);
        result.setUpdatedTime(new Date());
        resultMapper.updateById(result);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchDeleteResults(List<String> resultIds) {
        resultMapper.batchDeleteByIds(resultIds);
    }

    @Override
    public List<TblComplianceResult> getResultsNeedingAttention() {
        return resultMapper.selectResultsNeedingAttention();
    }

    @Override
    public List<TblComplianceResult> getResultsNeedingImmediateAction() {
        return resultMapper.selectResultsNeedingImmediateAction();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TblComplianceResult processResult(String resultId, String actionTaken) {
        TblComplianceResult result = getResultById(resultId);
        result.setActionTaken(actionTaken);
        result.setCheckStatus("PROCESSED");
        result.setUpdatedTime(new Date());
        resultMapper.updateById(result);
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TblComplianceResult resolveResult(String resultId, String resolution) {
        TblComplianceResult result = getResultById(resultId);
        result.setCheckStatus("RESOLVED");
        result.setResolvedTime(new Date());
        result.setUpdatedTime(new Date());
        resultMapper.updateById(result);
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TblComplianceResult escalateResult(String resultId, String escalateReason) {
        TblComplianceResult result = getResultById(resultId);
        result.setIsEscalated(1);
        result.setCheckStatus("ESCALATED");
        result.setUpdatedTime(new Date());
        resultMapper.updateById(result);
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchProcessResults(List<String> resultIds) {
        for (String resultId : resultIds) {
            processResult(resultId, "批量处理");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchResolveResults(List<String> resultIds) {
        for (String resultId : resultIds) {
            resolveResult(resultId, "批量解决");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchEscalateResults(List<String> resultIds) {
        for (String resultId : resultIds) {
            escalateResult(resultId, "批量升级");
        }
    }

    @Override
    public Map<String, Object> getComplianceStatistics() {
        Map<String, Object> params = new HashMap<>();
        List<TblComplianceResult> allResults = resultMapper.selectResultList(params);
        Map<String, Object> statistics = new HashMap<>();
        int total = allResults.size();
        int passed = 0;
        int failed = 0;
        int pending = 0;
        int escalated = 0;
        for (TblComplianceResult r : allResults) {
            if (r.getIsPassed() != null && r.getIsPassed() == 1) {
                passed++;
            } else if (r.getIsPassed() != null && r.getIsPassed() == 0) {
                failed++;
            }
            if ("PENDING".equals(r.getCheckStatus())) {
                pending++;
            }
            if (r.getIsEscalated() != null && r.getIsEscalated() == 1) {
                escalated++;
            }
        }
        statistics.put("total", total);
        statistics.put("passed", passed);
        statistics.put("failed", failed);
        statistics.put("pending", pending);
        statistics.put("escalated", escalated);
        statistics.put("passRate", total > 0 ? Math.round(passed * 100.0 / total) : 0);
        return statistics;
    }

    @Override
    public List<TblComplianceResult> exportResults(Map<String, Object> params) {
        return resultMapper.selectResultList(params);
    }
}

