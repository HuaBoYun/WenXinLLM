package com.huabo.legal.service.impl;

import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.huabo.legal.config.DateBaseConfig;
import com.huabo.legal.oracle.service.TblStaffOracleService;
import com.huabo.legal.service.CommonService;
import com.huabo.legal.vo.param.HeadquartersLegalQueryParam;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class CommonServiceImpl implements CommonService {

	@Resource
	private TblStaffOracleService tblStaffOracleService;

	/**
	 * 查询该用户是否是总部法务人员
	 * @param param
	 * @return
	 */
	@Override
	public JsonBean isHeadquartersLegal(HeadquartersLegalQueryParam param) {

		if (DateBaseConfig.DATABASETYPE.equals("Oracle")) {
			return ResponseFormat.retParam(200, 200, tblStaffOracleService.isHeadquartersLegal(param));
		} else {

		}
		return ResponseFormat.retParam(200, 200, null);
	}
}
