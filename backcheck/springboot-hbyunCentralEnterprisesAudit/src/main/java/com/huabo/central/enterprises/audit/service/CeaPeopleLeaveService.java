package com.huabo.central.enterprises.audit.service;

import com.huabo.central.enterprises.audit.oracle.entity.TblCeaPeopleLeaveOracle;
import com.huabo.central.enterprises.audit.util.MyJsonBean;
import com.huabo.central.enterprises.audit.vo.param.TblCeaPeopleLeaveQueryParam;

public interface CeaPeopleLeaveService {

	/**
	 * 人员请假单 列表查询
	 * @param param
	 * @return
	 */
	MyJsonBean<TblCeaPeopleLeaveOracle> getTblCeaPeopleLeaveList(TblCeaPeopleLeaveQueryParam param);

	/**
	 * 人员请假单 新增/更新
	 * @param param
	 * @return
	 */
	MyJsonBean<TblCeaPeopleLeaveOracle> saveOrUpdateTblCeaPeopleLeave(TblCeaPeopleLeaveOracle param);

	/**
	 * 人员请假单 刪除
	 * @param id
	 * @return
	 */
	MyJsonBean<Void> deleteTblCeaPeopleLeave(Long id);

	/**
	 * 人员请假单 详情 查询
	 * @param id
	 * @return
	 */
	MyJsonBean<TblCeaPeopleLeaveOracle> getTblCeaPeopleLeave(Long id);

	/**
	 * 人员请假台账
	 * @param param
	 * @return
	 */
	MyJsonBean<TblCeaPeopleLeaveOracle> getTblCeaPeopleLeaveAllList(TblCeaPeopleLeaveQueryParam param);
}
