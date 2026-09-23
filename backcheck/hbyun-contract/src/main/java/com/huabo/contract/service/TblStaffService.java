package com.huabo.contract.service;

import java.math.BigDecimal;
import java.util.Map;

import com.huabo.contract.entity.TblOrganization;
import com.huabo.contract.entity.TblStaff;
import com.huabo.contract.vo.StaffResult;

public interface TblStaffService {

	Map<String, Object> findAllPageBeanPid(Integer pageNumber, Integer pageSize, BigDecimal orgid,
			TblOrganization organization) throws Exception;

	TblStaff findById(String staffid) throws Exception;

	void updateTs(TblStaff ts) throws Exception;

	StaffResult getUserInfoExam(Integer staffId, Integer orgid, String type) throws Exception;

	Map<String, Object> findAllPageBeanPid(String pid, Integer pageNumber, Integer pageSize, String token, String staffId);

	Map<String, Object> findHttpClient(BigDecimal formId, String jsonArry, String paramArry, String eleName, String valueId, Integer pageNumber, Integer pageSize, Integer queryType, BigDecimal oldFormId, String textname);

	/**
	 * 获取用户名称
	 * @param staffId
	 * @return
	 */
	String getStaffName(Long staffId);

	/**
	 * 获取用户名称
	 * @param staffIds
	 * @return
	 */
	String getStaffNames(String staffIds);
}
