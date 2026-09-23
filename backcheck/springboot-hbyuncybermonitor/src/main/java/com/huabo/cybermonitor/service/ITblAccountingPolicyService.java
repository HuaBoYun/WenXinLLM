package com.huabo.cybermonitor.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.huabo.cybermonitor.entity.TblAccountingPolicy;
import com.huabo.cybermonitor.util.PageResult;
import com.huabo.cybermonitor.vo.TblAccountingPolicyQueryVO;

public interface ITblAccountingPolicyService extends IService<TblAccountingPolicy> {
    PageResult<TblAccountingPolicy> selectByPage(TblAccountingPolicyQueryVO queryVO);
    boolean addRecord(TblAccountingPolicy record);
    boolean updateRecord(TblAccountingPolicy record);
    boolean deleteRecord(String id);
}

