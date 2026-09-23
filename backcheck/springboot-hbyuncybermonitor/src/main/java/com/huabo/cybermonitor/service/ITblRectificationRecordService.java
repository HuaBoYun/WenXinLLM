package com.huabo.cybermonitor.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.huabo.cybermonitor.entity.TblRectificationRecord;
import com.huabo.cybermonitor.util.PageResult;
import com.huabo.cybermonitor.vo.TblRectificationRecordQueryVO;

public interface ITblRectificationRecordService extends IService<TblRectificationRecord> {
    PageResult<TblRectificationRecord> selectByPage(TblRectificationRecordQueryVO queryVO);
    boolean addRecord(TblRectificationRecord record);
    boolean updateRecord(TblRectificationRecord record);
    boolean deleteRecord(String id);
}

