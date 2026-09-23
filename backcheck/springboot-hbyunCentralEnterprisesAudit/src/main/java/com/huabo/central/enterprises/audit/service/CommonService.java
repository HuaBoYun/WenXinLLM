package com.huabo.central.enterprises.audit.service;

import com.huabo.central.enterprises.audit.oracle.entity.TblStaffOracle;
import com.huabo.central.enterprises.audit.util.MyJsonBean;
import com.huabo.central.enterprises.audit.vo.param.UpdateUserOnDutyStatusParam;
import com.huabo.central.enterprises.audit.vo.param.UserAllQueryParam;
import com.huabo.central.enterprises.audit.vo.result.UserAllResult;

public interface CommonService {

	/**
	 * 用户信息
	 * @param id
	 * @return
	 */
	MyJsonBean<TblStaffOracle> getUserInfo(Long id);

	/**
	 * 更新用户在岗状态
	 * @param param
	 * @return
	 */
	MyJsonBean<Void> updateUserOnDutyStatus(UpdateUserOnDutyStatusParam param);

	/**
	 * 人员台账列表
	 * @param param
	 * @return
	 */
	MyJsonBean<UserAllResult> getUserAllList(UserAllQueryParam param);
}
