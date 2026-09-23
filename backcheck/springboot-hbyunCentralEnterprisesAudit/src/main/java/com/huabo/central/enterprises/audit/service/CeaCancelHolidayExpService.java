package com.huabo.central.enterprises.audit.service;

import com.huabo.central.enterprises.audit.oracle.entity.TblCeaCancelHolidayExp;
import com.huabo.central.enterprises.audit.util.MyJsonBean;
import com.huabo.central.enterprises.audit.vo.param.TblCeaCancelHolidayQueryParam;

public interface CeaCancelHolidayExpService {

	/**
	 * 销假单 列表查询
	 * @param param
	 * @return
	 */
	MyJsonBean<TblCeaCancelHolidayExp> getTblCeaCancelHolidayExpList(TblCeaCancelHolidayQueryParam param);

	/**
	 * 销假单 新增/更新
	 * @param param
	 * @return
	 */
	MyJsonBean<TblCeaCancelHolidayExp> saveOrUpdateTblCeaCancelHolidayExp(TblCeaCancelHolidayExp param);

	/**
	 * 销假单 刪除
	 * @param id
	 * @return
	 */
	MyJsonBean<Void> deleteTblCeaCancelHolidayExp(Long id);

	/**
	 * 销假单 详情 查询
	 * @param id
	 * @return
	 */
	MyJsonBean<TblCeaCancelHolidayExp> getTblCeaCancelHolidayExp(Long id);
}
