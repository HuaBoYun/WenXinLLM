package com.huabo.cybermonitor.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.huabo.cybermonitor.entity.TblPropertyTransaction;
import com.huabo.cybermonitor.util.PageResult;
import com.huabo.cybermonitor.vo.TblPropertyTransactionQueryVO;

public interface ITblPropertyTransactionService extends IService<TblPropertyTransaction> {
    PageResult<TblPropertyTransaction> selectByPage(TblPropertyTransactionQueryVO queryVO);
    boolean addRecord(TblPropertyTransaction record);
    boolean updateRecord(TblPropertyTransaction record);
    boolean deleteRecord(String id);
}

