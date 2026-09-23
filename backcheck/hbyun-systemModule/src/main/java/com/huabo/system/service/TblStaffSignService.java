package com.huabo.system.service;

import com.hbfk.util.JsonBean;
import com.huabo.system.entity.TblStaffSign;

public interface TblStaffSignService {

	JsonBean saveSignInfo(TblStaffSign sign, String token) throws Exception;

	JsonBean saveDistribution(TblStaffSign sign, String token) throws Exception;

	JsonBean removeSignInfo(String signid, String token) throws Exception;

	JsonBean getSignInfo(String signid, String token) throws Exception;

	JsonBean getSignList(TblStaffSign sign, Integer pageNumber, Integer pageSize, String token) throws Exception;

	JsonBean getSignNatureList(String token) throws Exception;
	
}
