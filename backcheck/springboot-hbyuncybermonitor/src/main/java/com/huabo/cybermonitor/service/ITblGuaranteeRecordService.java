package com.huabo.cybermonitor.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.huabo.cybermonitor.entity.TblGuaranteeRecord;
import com.huabo.cybermonitor.util.PageResult;
import com.huabo.cybermonitor.vo.TblGuaranteeRecordQueryVO;

public interface ITblGuaranteeRecordService extends IService<TblGuaranteeRecord> {
    PageResult<TblGuaranteeRecord> selectByPage(TblGuaranteeRecordQueryVO queryVO);
    boolean addRecord(TblGuaranteeRecord record);
    boolean updateRecord(TblGuaranteeRecord record);
    boolean deleteRecord(String id);
}

