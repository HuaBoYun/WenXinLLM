package com.huabo.cybermonitor.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.huabo.cybermonitor.entity.TblInvestigationTask;
import com.huabo.cybermonitor.util.PageResult;
import com.huabo.cybermonitor.vo.TblInvestigationTaskQueryVO;

import java.util.Map;

public interface ITblInvestigationTaskService extends IService<TblInvestigationTask> {
    PageResult<TblInvestigationTask> selectByPage(TblInvestigationTaskQueryVO queryVO);
    boolean addTask(TblInvestigationTask task);
    boolean updateTask(TblInvestigationTask task);
    boolean deleteTask(String id);
    Map<String, Object> getStatistics(String companyId);
}

