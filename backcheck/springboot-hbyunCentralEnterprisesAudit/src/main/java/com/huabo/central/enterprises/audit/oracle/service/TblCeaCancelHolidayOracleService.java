package com.huabo.central.enterprises.audit.oracle.service;

import com.github.pagehelper.PageInfo;
import com.huabo.central.enterprises.audit.oracle.entity.TblCeaCancelHolidayOracle;
import com.huabo.central.enterprises.audit.vo.param.TblCeaCancelHolidayQueryParam;

public interface TblCeaCancelHolidayOracleService {

	/**
	 * 销假单 列表查询
	 * @param param
	 * @return
	 */
	PageInfo<TblCeaCancelHolidayOracle> getList(TblCeaCancelHolidayQueryParam param);

	/**
	 * 销假单 新增/更新
	 * @param param
	 * @return
	 */
	TblCeaCancelHolidayOracle saveOrUpdate(TblCeaCancelHolidayOracle param);

	/**
	 * 销假单 删除
	 * @param id
	 */
	void delete(Long id);

	/**
	 * 销假单 详情查询
	 * @param id
	 * @return
	 */
	TblCeaCancelHolidayOracle findById(Long id);
}
