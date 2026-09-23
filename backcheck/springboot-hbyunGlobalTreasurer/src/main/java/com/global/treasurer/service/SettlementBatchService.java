package com.global.treasurer.service;

import com.global.treasurer.entity.TblSettlementBatch;
import java.util.List;
import java.util.Map;

public interface SettlementBatchService {
    Map<String, Object> getBatchPage(Map<String, Object> params);
    TblSettlementBatch getBatchById(Long batchId);
    int createBatch(TblSettlementBatch batch);
    int updateBatch(TblSettlementBatch batch);
    int deleteBatch(List<Long> batchIds);
    int startBatch(Long batchId);
    int stopBatch(Long batchId);
    int retryBatch(Long batchId);
    List<TblSettlementBatch> getExecutableBatches(Long orgId);
    Map<String, Object> getBatchSummary(Map<String, Object> params);
    Map<String, Object> analyzeBatchPerformance(Map<String, Object> params);
    Map<String, Object> getBatchOptimizationSuggestions(Long orgId);
}
