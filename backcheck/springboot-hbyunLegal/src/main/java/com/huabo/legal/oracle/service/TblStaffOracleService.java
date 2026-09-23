package com.huabo.legal.oracle.service;

import com.huabo.legal.oracle.entity.TblStaffOracle;
import com.huabo.legal.vo.param.HeadquartersLegalQueryParam;
import com.huabo.legal.vo.result.StaffResult;
import com.huabo.legal.vo.result.UserInfo;

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
	 * 查询该用户是否是总部法务人员
	 * @param param
	 * @return
	 */
	Boolean isHeadquartersLegal(HeadquartersLegalQueryParam param);

	/**
	 * 根据集团id 批量查询名称Map
	 * @param orgid
	 * @return
	 */
	Map<String, String> getBelongGroupIdUserInfoMap(String orgid);

	/**
	 * 根据工作单位id 批量查询名称Map
	 * @param orgid
	 * @return
	 */
	Map<String, String> getWorkUnitIdUserInfoMap(String orgid);

	/**
	 * 根据创建人ID 批量查询名称Map
	 * @param staffId
	 * @return
	 */
	Map<String, String> getCreatorUserInfoMap(String staffId);


	/**
	 * 通过staffId获取用户信息
	 * @param staffId
	 * @return
	 */
	TblStaffOracle getUserInfoForId(Long staffId);

	/**
	 * 查询当前公司及子公司
	 * @param orgid
	 * @return
	 */
	@Deprecated
	List<Long> getTblOrganization(Long orgid);

	/**
	 * 查询当前公司及子公司 递归优化后
	 * @param orgid
	 * @return
	 */
	List<Long> getTblOrganizationAll(Long orgid);
}
