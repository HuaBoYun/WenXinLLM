package com.huabo.central.enterprises.audit.oracle.service;

import com.github.pagehelper.PageInfo;
import com.huabo.central.enterprises.audit.oracle.entity.TblCeaPeopleLeaveOracle;
import com.huabo.central.enterprises.audit.vo.param.TblCeaPeopleLeaveQueryParam;

public interface TblCeaPeopleLeaveOracleService {

	/**
	 * 人员请假单 列表查询
	 * @param param
	 * @return
	 */
	PageInfo<TblCeaPeopleLeaveOracle> getList(TblCeaPeopleLeaveQueryParam param);

	/**
	 * 人员请假单 新增/更新
	 * @param param
	 * @return
	 */
	TblCeaPeopleLeaveOracle saveOrUpdate(TblCeaPeopleLeaveOracle param);

	/**
	 * 人员请假单 删除
	 * @param id
	 */
	void delete(Long id);

	/**
	 * 人员请假单 详情查询
	 * @param id
	 * @return
	 */
	TblCeaPeopleLeaveOracle findById(Long id);

	/**
	 * 人员请假台账
	 * @param param
	 * @return
	 */
	PageInfo<TblCeaPeopleLeaveOracle> getTblCeaPeopleLeaveAllList(TblCeaPeopleLeaveQueryParam param);
}

