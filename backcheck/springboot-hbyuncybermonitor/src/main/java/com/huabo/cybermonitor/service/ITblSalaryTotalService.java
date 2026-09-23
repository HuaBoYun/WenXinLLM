package com.huabo.cybermonitor.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.huabo.cybermonitor.entity.TblSalaryTotal;
import com.huabo.cybermonitor.util.PageResult;
import com.huabo.cybermonitor.vo.TblSalaryTotalQueryVO;

import java.util.Map;

public interface ITblSalaryTotalService extends IService<TblSalaryTotal> {
    PageResult<TblSalaryTotal> selectByPage(TblSalaryTotalQueryVO queryVO);
    boolean addRecord(TblSalaryTotal record);
    boolean updateRecord(TblSalaryTotal record);
    boolean deleteRecord(String id);
    Map<String, Object> getStatistics(String companyId);
}

