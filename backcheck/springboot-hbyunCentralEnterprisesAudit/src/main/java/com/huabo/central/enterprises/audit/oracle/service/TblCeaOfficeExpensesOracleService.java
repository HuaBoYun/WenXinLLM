package com.huabo.central.enterprises.audit.oracle.service;

import com.github.pagehelper.PageInfo;
import com.huabo.central.enterprises.audit.oracle.entity.TblCeaOfficeExpensesOracle;
import com.huabo.central.enterprises.audit.vo.param.TblCeaOfficeExpensesQueryParam;

public interface TblCeaOfficeExpensesOracleService {

	/**
	 * 办公经费申请 列表查询
	 * @param param
	 * @return
	 */
	PageInfo<TblCeaOfficeExpensesOracle> getList(TblCeaOfficeExpensesQueryParam param);

	/**
	 * 办公经费申请 新增/更新
	 * @param param
	 * @return
	 */
	TblCeaOfficeExpensesOracle saveOrUpdate(TblCeaOfficeExpensesOracle param);

	/**
	 * 办公经费申请 删除
	 * @param id
	 */
	void delete(Long id);

	/**
	 * 办公经费申请 详情查询
	 * @param id
	 * @return
	 */
	TblCeaOfficeExpensesOracle findById(Long id);

}
