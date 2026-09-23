package com.global.treasurer.service.impl;

import com.global.treasurer.entity.TblProcessingRecord;
import com.global.treasurer.mapper.TblProcessingRecordMapper;
import com.global.treasurer.service.ProcessingRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class ProcessingRecordServiceImpl implements ProcessingRecordService {
    @Autowired
    private TblProcessingRecordMapper processingRecordMapper;

    @Override
    public Map<String, Object> getProcessingRecordPage(Map<String, Object> params) {
        Map<String, Object> result = new HashMap<>();

        // 兼容前端分页参数: pageNum 和 pageSize
        Integer page = params.get("pageNum") != null ? Integer.parseInt(params.get("pageNum").toString()) :
                       (params.get("current") != null ? Integer.parseInt(params.get("current").toString()) : 1);
        Integer size = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) :
                       (params.get("size") != null ? Integer.parseInt(params.get("size").toString()) : 10);

        params.put("offset", (page - 1) * size);
        params.put("limit", size);

        List<TblProcessingRecord> list = processingRecordMapper.selectProcessingRecordPage(params);
        int total = processingRecordMapper.countProcessingRecordList(params);

        // 兼容前端数据格式: rows 和 total
        result.put("rows", list);
        result.put("total", total);

        return result;
    }

    @Override
    public TblProcessingRecord getProcessingRecordById(Long recordId) {
        return processingRecordMapper.selectById(recordId);
    }

    @Override
    public int createProcessingRecord(TblProcessingRecord record) {
        record.setDeleteFlag(0);
        record.setRetryCount(0);
        record.setCreatedTime(new Date());
        return processingRecordMapper.insert(record);
    }

    @Override
    public int updateProcessingRecord(TblProcessingRecord record) {
        record.setUpdatedTime(new Date());
        return processingRecordMapper.updateById(record);
    }

    @Override
    public int deleteProcessingRecord(List<Long> recordIds) {
        if (recordIds == null || recordIds.isEmpty()) return 0;
        int count = 0;
        for (Long id : recordIds) {
            TblProcessingRecord r = getProcessingRecordById(id);
            if (r != null) {
                r.setDeleteFlag(1);
                r.setUpdatedTime(new Date());
                count += processingRecordMapper.updateById(r);
            }
        }
        return count;
    }

    @Override
    public List<TblProcessingRecord> getRetryableRecords(Long orgId) {
        return processingRecordMapper.selectRetryableRecords(orgId);
    }

    @Override
    public List<TblProcessingRecord> getTimeoutRecords(Map<String, Object> params) {
        return processingRecordMapper.selectTimeoutRecords(params);
    }

    @Override
    public Map<String, Object> getProcessingSummary(Map<String, Object> params) {
        return processingRecordMapper.selectProcessingSummary(params);
    }

    @Override
    public int retryProcessing(Long recordId) {
        TblProcessingRecord record = getProcessingRecordById(recordId);
        if (record == null) return 0;
        record.setRetryCount(record.getRetryCount() + 1);
        record.setUpdatedTime(new Date());
        return processingRecordMapper.updateById(record);
    }
}
