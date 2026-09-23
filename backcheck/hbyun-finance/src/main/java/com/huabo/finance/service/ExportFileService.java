package com.huabo.finance.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.huabo.finance.vo.ExportRequestVo;

public interface ExportFileService {

	/**
	 * 导出选中信息为Excel
	 * @param token
	 * @param exportRequestVo
	 * @param staff 
	 * @param response 
	 * @param request 
	 * @return
	 */
	JsonBean exportFileFunc(String token, ExportRequestVo exportRequestVo, TblStaffUtil staff, HttpServletResponse response, HttpServletRequest request) throws Exception;

}
