package com.huabo.cybermonitor.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.huabo.cybermonitor.entity.TblMilitaryTask;
import com.huabo.cybermonitor.util.PageResult;
import com.huabo.cybermonitor.vo.TblMilitaryTaskQueryVO;

import java.util.Map;

public interface ITblMilitaryTaskService extends IService<TblMilitaryTask> {
    PageResult<TblMilitaryTask> selectByPage(TblMilitaryTaskQueryVO queryVO);
    boolean addRecord(TblMilitaryTask record);
    boolean updateRecord(TblMilitaryTask record);
    boolean deleteRecord(String id);
    Map<String, Object> getStatistics(String companyId);
}

