package com.huabo.system.service;

import java.math.BigDecimal;
import java.util.List;

import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.huabo.system.entity.TblStaff;
import com.huabo.system.entity.TblUserOrgRelation;
import com.huabo.system.entity.TblVirtualOrgInfo;

public interface TblVirtualOrgInfoService {

	JsonBean adds(List<TblVirtualOrgInfo> voiList) throws Exception;

	JsonBean modify(TblVirtualOrgInfo voi) throws Exception;

	JsonBean remove(String fid) throws Exception;

	JsonBean list(BigDecimal orgid, String virtualname, Integer pageNumber, Integer pageSize) throws Exception;


}
