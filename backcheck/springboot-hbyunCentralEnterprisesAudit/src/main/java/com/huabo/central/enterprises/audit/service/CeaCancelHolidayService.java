package com.huabo.central.enterprises.audit.service;

import com.huabo.central.enterprises.audit.oracle.entity.TblCeaCancelHolidayOracle;
import com.huabo.central.enterprises.audit.util.MyJsonBean;
import com.huabo.central.enterprises.audit.vo.param.TblCeaCancelHolidayQueryParam;

public interface CeaCancelHolidayService {

	/**
	 * 销假单 列表查询
	 * @param param
	 * @return
	 */
	MyJsonBean<TblCeaCancelHolidayOracle> getTblCeaCancelHolidayList(TblCeaCancelHolidayQueryParam param);

	/**
	 * 销假单 新增/更新
	 * @param param
	 * @return
	 */
	MyJsonBean<TblCeaCancelHolidayOracle> saveOrUpdateTblCeaCancelHoliday(TblCeaCancelHolidayOracle param);

	/**
	 * 销假单 刪除
	 * @param id
	 * @return
	 */
	MyJsonBean<Void> deleteTblCeaCancelHoliday(Long id);

	/**
	 * 销假单 详情 查询
	 * @param id
	 * @return
	 */
	MyJsonBean<TblCeaCancelHolidayOracle> getTblCeaCancelHoliday(Long id);
}
