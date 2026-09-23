package com.huabo.cybermonitor.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.huabo.cybermonitor.entity.TblRelatedTransaction;
import com.huabo.cybermonitor.util.PageResult;
import com.huabo.cybermonitor.vo.TblRelatedTransactionQueryVO;

public interface ITblRelatedTransactionService extends IService<TblRelatedTransaction> {
    PageResult<TblRelatedTransaction> selectByPage(TblRelatedTransactionQueryVO queryVO);
    boolean addRecord(TblRelatedTransaction record);
    boolean updateRecord(TblRelatedTransaction record);
    boolean deleteRecord(String id);
}

