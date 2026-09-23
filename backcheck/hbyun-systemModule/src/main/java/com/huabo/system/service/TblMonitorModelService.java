package com.huabo.system.service;

import com.hbfk.util.PageInfo;
import com.huabo.system.entity.TblMonitorModel;

import java.util.Map;

public interface TblMonitorModelService {
    Map<String, Object> findByModelJKZX(String solutionid, PageInfo<TblMonitorModel> pageInfo);
}
