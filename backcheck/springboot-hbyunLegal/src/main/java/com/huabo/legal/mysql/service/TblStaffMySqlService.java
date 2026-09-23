package com.huabo.legal.mysql.service;

import com.huabo.legal.vo.result.UserInfo;
import com.huabo.legal.vo.result.StaffResult;

import java.util.List;

public interface TblStaffMySqlService {

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
	String getCreatorUserInfo(Integer orgid);

	/**
	 * 根据用户id 查询信息
	 * @param staffId
	 * @return
	 */
	StaffResult getUserInfo(Integer staffId);

}
