package com.huabo.cybermonitor.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.huabo.cybermonitor.entity.TblProcurementProject;
import com.huabo.cybermonitor.util.PageResult;
import com.huabo.cybermonitor.vo.TblProcurementProjectQueryVO;

import java.util.Map;

public interface ITblProcurementProjectService extends IService<TblProcurementProject> {
    PageResult<TblProcurementProject> selectByPage(TblProcurementProjectQueryVO queryVO);
    boolean addRecord(TblProcurementProject record);
    boolean updateRecord(TblProcurementProject record);
    boolean deleteRecord(String id);
    Map<String, Object> getStatistics(String companyId);
}

