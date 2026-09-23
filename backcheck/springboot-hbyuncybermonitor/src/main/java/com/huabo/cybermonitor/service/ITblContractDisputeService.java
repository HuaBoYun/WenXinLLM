package com.huabo.cybermonitor.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.huabo.cybermonitor.entity.TblContractDispute;
import com.huabo.cybermonitor.util.PageResult;
import com.huabo.cybermonitor.vo.TblContractDisputeQueryVO;

public interface ITblContractDisputeService extends IService<TblContractDispute> {
    PageResult<TblContractDispute> selectByPage(TblContractDisputeQueryVO queryVO);
    boolean addRecord(TblContractDispute record);
    boolean updateRecord(TblContractDispute record);
    boolean deleteRecord(String id);
}

