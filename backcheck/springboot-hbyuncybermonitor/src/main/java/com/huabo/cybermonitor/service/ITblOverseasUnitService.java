package com.huabo.cybermonitor.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.huabo.cybermonitor.entity.TblOverseasUnit;
import com.huabo.cybermonitor.util.PageResult;
import com.huabo.cybermonitor.vo.TblOverseasUnitQueryVO;

import java.util.Map;

public interface ITblOverseasUnitService extends IService<TblOverseasUnit> {
    PageResult<TblOverseasUnit> selectByPage(TblOverseasUnitQueryVO queryVO);
    boolean addRecord(TblOverseasUnit record);
    boolean updateRecord(TblOverseasUnit record);
    boolean deleteRecord(String id);
    Map<String, Object> getStatistics(String companyId);
}

