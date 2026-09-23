package com.global.treasurer.service.impl;

import com.global.treasurer.entity.TblSettlementBatch;
import com.global.treasurer.mapper.TblSettlementBatchMapper;
import com.global.treasurer.service.SettlementBatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class SettlementBatchServiceImpl implements SettlementBatchService {
    @Autowired
    private TblSettlementBatchMapper batchMapper;

    @Override
    public Map<String, Object> getBatchPage(Map<String, Object> params) {
        Map<String, Object> result = new HashMap<>();

        // 兼容前端分页参数: pageNum 和 pageSize
        Integer page = params.get("pageNum") != null ? Integer.parseInt(params.get("pageNum").toString()) :
                       (params.get("current") != null ? Integer.parseInt(params.get("current").toString()) : 1);
        Integer size = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) :
                       (params.get("size") != null ? Integer.parseInt(params.get("size").toString()) : 10);

        params.put("offset", (page - 1) * size);
        params.put("limit", size);

        List<TblSettlementBatch> list = batchMapper.selectBatchPage(params);
        int total = batchMapper.countBatchList(params);

        // 兼容前端数据格式: rows 和 total
        result.put("rows", list);
        result.put("total", total);

        return result;
    }

    @Override
    public TblSettlementBatch getBatchById(Long batchId) {
        return batchMapper.selectById(batchId);
    }

    @Override
    public int createBatch(TblSettlementBatch batch) {
        batch.setBatchNo("BATCH-" + System.currentTimeMillis());
        batch.setBatchStatus("PENDING");
        batch.setDeleteFlag(0);
        batch.setCreatedTime(new Date());
        return batchMapper.insert(batch);
    }

    @Override
    public int updateBatch(TblSettlementBatch batch) {
        batch.setUpdatedTime(new Date());
        return batchMapper.updateById(batch);
    }

    @Override
    public int deleteBatch(List<Long> batchIds) {
        if (batchIds == null || batchIds.isEmpty()) return 0;
        int count = 0;
        for (Long id : batchIds) {
            TblSettlementBatch b = getBatchById(id);
            if (b != null && !"PROCESSING".equals(b.getBatchStatus())) {
                b.setDeleteFlag(1);
                b.setUpdatedTime(new Date());
                count += batchMapper.updateById(b);
            }
        }
        return count;
    }

    @Override
    public int startBatch(Long batchId) {
        TblSettlementBatch batch = getBatchById(batchId);
        if (batch == null) return 0;
        batch.setBatchStatus("PROCESSING");
        batch.setStartTime(new Date());
        return batchMapper.updateById(batch);
    }

    @Override
    public int stopBatch(Long batchId) {
        TblSettlementBatch batch = getBatchById(batchId);
        if (batch == null) return 0;
        batch.setBatchStatus("STOPPED");
        batch.setEndTime(new Date());
        return batchMapper.updateById(batch);
    }

    @Override
    public int retryBatch(Long batchId) {
        TblSettlementBatch batch = getBatchById(batchId);
        if (batch == null) return 0;
        batch.setBatchStatus("PENDING");
        return batchMapper.updateById(batch);
    }

    @Override
    public List<TblSettlementBatch> getExecutableBatches(Long orgId) {
        return batchMapper.selectExecutableBatches(orgId);
    }

    @Override
    public Map<String, Object> getBatchSummary(Map<String, Object> params) {
        return batchMapper.selectBatchSummary(params);
    }

    @Override
    public Map<String, Object> analyzeBatchPerformance(Map<String, Object> params) {
        return batchMapper.selectPerformanceAnalysis(params);
    }

    @Override
    public Map<String, Object> getBatchOptimizationSuggestions(Long orgId) {
        Map<String, Object> result = new HashMap<>();
        List<String> suggestions = Arrays.asList(
            "建议在非高峰期执行大批次",
            "建议增加批次大小以提高效率",
            "建议配置自动重试机制"
        );
        result.put("suggestions", suggestions);
        return result;
    }
}
