package com.huabo.central.enterprises.audit.service;

import com.huabo.central.enterprises.audit.oracle.entity.TblCeaOfficeExpensesOracle;
import com.huabo.central.enterprises.audit.util.MyJsonBean;
import com.huabo.central.enterprises.audit.vo.param.TblCeaOfficeExpensesQueryParam;

public interface CeaOfficeExpensesService {

	/**
	 * 办公经费申请 列表查询
	 * @param param
	 * @return
	 */
	MyJsonBean<TblCeaOfficeExpensesOracle> getTblCeaOfficeExpensesList(TblCeaOfficeExpensesQueryParam param);

	/**
	 * 办公经费申请 新增/更新
	 * @param param
	 * @return
	 */
	MyJsonBean<TblCeaOfficeExpensesOracle> saveOrUpdateTblCeaOfficeExpenses(TblCeaOfficeExpensesOracle param);

	/**
	 * 办公经费申请 刪除
	 * @param id
	 * @return
	 */
	MyJsonBean<Void> deleteTblCeaOfficeExpenses(Long id);

	/**
	 * 办公经费申请 详情 查询
	 * @param id
	 * @return
	 */
	MyJsonBean<TblCeaOfficeExpensesOracle> getTblCeaOfficeExpenses(Long id);
}
