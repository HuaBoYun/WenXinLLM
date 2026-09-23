package com.huabo.system.oracle.service;


import com.huabo.system.entity.TblStaffOracle;
import com.huabo.system.vo.result.StaffResult;
import com.huabo.system.vo.result.UserInfo;

import java.util.List;
import java.util.Map;

public interface TblStaffOracleService {

	/**
	 * 根据集团id 查询名称
	 * @param orgid
	 * @return
	 */
	String getBelongGroupIdUserInfo(Long orgid);

	/**
	 * 根据工作单位id 查询名称
	 * @param orgid
	 * @return
	 */
	String getWorkUnitIdUserInfo(Long orgid);

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
	String getCreatorUserInfo(Long orgid);

	/**
	 * 根据用户id 查询信息
	 * @param staffId
	 * @return
	 */
	StaffResult getUserInfo(Long staffId);


	/**
	 * 根据集团id 批量查询名称
	 * @param orgid
	 * @return
	 */
	List<UserInfo> getBelongGroupIdUserInfos(String orgid);

	/**
	 * 根据集团id 批量查询名称 Orgmeno
	 * @param orgid
	 * @return
	 */
	List<UserInfo> getBelongGroupIdUserInfoOrgmeno(String orgid);

	/**
	 * 获取考试人员
	 * @param staffId
	 * @return
	 */
	StaffResult getUserInfoExam(Long staffId, Long orgid, String type);

	/**
	 * 根据集团id 批量查询名称Map
	 * @param orgid
	 * @return
	 */
	Map<Long, String> getBelongGroupIdUserInfoMap(String orgid);

	/**
	 * 根据工作单位id 批量查询名称Map
	 * @param orgid
	 * @return
	 */
	Map<Long, String> getWorkUnitIdUserInfoMap(String orgid);

	/**
	 * 根据创建人ID 批量查询名称Map
	 * @param staffId
	 * @return
	 */
	Map<Long, String> getCreatorUserInfoMap(String staffId);


	/**
	 * 通过staffId获取用户信息
	 * @param staffId
	 * @return
	 */
	TblStaffOracle getUserInfoForId(Long staffId);

	/**
	 * 通过staffId获取用户信息 Map
	 * @param staffIds
	 * @return
	 */
	Map<Long, TblStaffOracle> getUserInfoForIdMap(List<Long> staffIds);

	/**
	 * 根据用户名称与部门ID 查询用户信息
	 * @param staffName
	 * @return
	 */
	TblStaffOracle getUserInfoForName(String staffName, Long orgid);

	/**
	 * 根据部门名称与部门ID 查询用户信息
	 * @param workUnitName
	 * @return
	 */
	StaffResult getUserInfoForWorkUnitName(String workUnitName, Long orgid);

}
