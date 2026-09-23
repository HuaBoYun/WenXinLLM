package com.regulatory.penetration.oracle.service;

import com.github.pagehelper.PageInfo;
import com.regulatory.penetration.oracle.entity.TblStaffOracle;
import com.regulatory.penetration.vo.param.UpdateUserOnDutyStatusParam;
import com.regulatory.penetration.vo.param.UserAllQueryParam;
import com.regulatory.penetration.vo.result.StaffResult;
import com.regulatory.penetration.vo.result.UserAllResult;
import com.regulatory.penetration.vo.result.UserInfo;

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
	 * 部门id或者单位id 查询名称
	 * @param orgid
	 * @return
	 */
	String getIdUserInfo(Long orgid);

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
	StaffResult getUserInfoExam(Integer staffId, Integer orgid, String type);

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
	 * 根据部门或者单位id 批量查询名称Map
	 * @param orgid
	 * @return
	 */
	Map<Long, String> getIdUserInfoMap(String orgid);


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

	/**
	 * 根据单位名称与部门ID 查询用户信息
	 * @param belongGroupName
	 * @return
	 */
	StaffResult getUserInfoForBelongGroupName(String belongGroupName, Long orgid);

	/**
	 * 查询当前公司及子公司 递归优化后
	 * @param orgid
	 * @return
	 */
	List<Long> getTblOrganizationAll(Long orgid);

	/**
	 * 更新用户在岗状态
	 * @param param
	 * @return
	 */
	void updateUserOnDutyStatus(UpdateUserOnDutyStatusParam param);

	/**
	 * 人员台账列表
	 * @param param
	 * @return
	 */
	PageInfo<UserAllResult> getUserAllList(UserAllQueryParam param);

	/**
	 * 根据用户ID、角色ID 查询该用户是否是这个角色
	 * @param staffId
	 * @return
	 */
	Boolean getUserRole(Long staffId, Long roleId);
}
