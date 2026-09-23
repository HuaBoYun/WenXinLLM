package com.huabo.cybermonitor.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.huabo.cybermonitor.entity.TblFinanceStatement;
import com.huabo.cybermonitor.util.PageResult;
import com.huabo.cybermonitor.vo.TblFinanceStatementQueryVO;

import java.util.Map;

public interface ITblFinanceStatementService extends IService<TblFinanceStatement> {
    PageResult<TblFinanceStatement> selectByPage(TblFinanceStatementQueryVO queryVO);
    boolean addRecord(TblFinanceStatement record);
    boolean updateRecord(TblFinanceStatement record);
    boolean deleteRecord(String id);
    Map<String, Object> getStatistics(String companyId);
}

