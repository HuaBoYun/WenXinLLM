package com.global.treasurer.service;

import com.global.treasurer.entity.TblProcessingRecord;
import java.util.List;
import java.util.Map;

public interface ProcessingRecordService {
    Map<String, Object> getProcessingRecordPage(Map<String, Object> params);
    TblProcessingRecord getProcessingRecordById(Long recordId);
    int createProcessingRecord(TblProcessingRecord record);
    int updateProcessingRecord(TblProcessingRecord record);
    int deleteProcessingRecord(List<Long> recordIds);
    List<TblProcessingRecord> getRetryableRecords(Long orgId);
    List<TblProcessingRecord> getTimeoutRecords(Map<String, Object> params);
    Map<String, Object> getProcessingSummary(Map<String, Object> params);
    int retryProcessing(Long recordId);
}
