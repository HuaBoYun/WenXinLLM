package com.huabo.cybermonitor.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.huabo.cybermonitor.entity.TblContractRecord;
import com.huabo.cybermonitor.util.PageResult;
import com.huabo.cybermonitor.vo.TblContractRecordQueryVO;

import java.util.List;
import java.util.Map;

public interface ITblContractRecordService extends IService<TblContractRecord> {
    PageResult<TblContractRecord> selectByPage(TblContractRecordQueryVO queryVO);
    boolean addRecord(TblContractRecord record);
    boolean updateRecord(TblContractRecord record);
    boolean deleteRecord(String id);
    Map<String, Object> getStatisticsByOrgPattern(String orgPattern);
    Map<String, Object> getStatistics(String companyId);
}

