package com.huabo.cybermonitor.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.huabo.cybermonitor.entity.TblFinancingRecord;
import com.huabo.cybermonitor.util.PageResult;
import com.huabo.cybermonitor.vo.TblFinancingRecordQueryVO;

import java.util.Map;

public interface ITblFinancingRecordService extends IService<TblFinancingRecord> {
    PageResult<TblFinancingRecord> selectByPage(TblFinancingRecordQueryVO queryVO);
    boolean addRecord(TblFinancingRecord record);
    boolean updateRecord(TblFinancingRecord record);
    boolean deleteRecord(String id);
    Map<String, Object> getStatistics(String companyId);
}

