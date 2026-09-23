package com.huabo.audit.oracle.service;

import com.huabo.audit.oracle.vo.StaffResult;
import com.huabo.audit.oracle.vo.UserInfo;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface TblStaffOracleService {

	/**
	 * 根据集团id 查询名称
	 * @param orgid
	 * @return
	 */
	String getBelongGroupIdUserInfo(Integer orgid);

	/**
	 * 根据工作单位id 查询名称
	 * @param orgid
	 * @return
	 */
	String getWorkUnitIdUserInfo(Integer orgid);

	/**
	 * 根据工作单位id 批量查询名称
	 * @param orgid
	 * @return
	 */
	List<UserInfo> getWorkUnitIdUserInfos(String orgid);


	/**
	 * 根据创建人ID 批量查询名称
	 * @param staffId
	 * @return
	 */
	List<UserInfo> getCreatorUserInfos(String staffId);

	/**
	 * 根据创建人ID 查询名称
	 * @param orgid
	 * @return
	 */
	String getCreatorUserInfo(BigDecimal orgid);

	/**
	 * 根据用户id 查询信息
	 * @param staffId
	 * @return
	 */
	StaffResult getUserInfo(Integer staffId);


	/**
	 * 根据集团id 批量查询名称
	 * @param orgid
	 * @return
	 */
	List<UserInfo> getBelongGroupIdUserInfos(String orgid);

	/**
	 * 获取考试人员
	 * @param staffId
	 * @return
	 */
	StaffResult getUserInfoExam(Integer staffId, Integer orgid, String type);

	/**
	 * 根据集团id 批量查询名称Map
	 * @param orgid
	 * @return
	 */
	Map<Integer, String> getBelongGroupIdUserInfoMap(String orgid);

	/**
	 * 根据工作单位id 批量查询名称Map
	 * @param orgid
	 * @return
	 */
	Map<Integer, String> getWorkUnitIdUserInfoMap(String orgid);

	/**
	 * 根据创建人ID 批量查询名称Map
	 * @param staffId
	 * @return
	 */
	Map<Integer, String> getCreatorUserInfoMap(String staffId);
}
