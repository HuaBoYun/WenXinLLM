package com.huabo.central.enterprises.audit.service;

import com.huabo.central.enterprises.audit.oracle.entity.TblCeaRepairExpensesOracle;
import com.huabo.central.enterprises.audit.util.MyJsonBean;
import com.huabo.central.enterprises.audit.vo.param.TblCeaRepairExpensesQueryParam;

public interface CeaRepairExpensesService {

	/**
	 * 修理费支出 列表查询
	 * @param param
	 * @return
	 */
	MyJsonBean<TblCeaRepairExpensesOracle> getTblCeaRepairExpensesList(TblCeaRepairExpensesQueryParam param);

	/**
	 * 修理费支出 新增/更新
	 * @param param
	 * @return
	 */
	MyJsonBean<TblCeaRepairExpensesOracle> saveOrUpdateTblCeaRepairExpenses(TblCeaRepairExpensesOracle param);

	/**
	 * 修理费支出 刪除
	 * @param id
	 * @return
	 */
	MyJsonBean<Void> deleteTblCeaRepairExpenses(Long id);

	/**
	 * 修理费支出 详情 查询
	 * @param id
	 * @return
	 */
	MyJsonBean<TblCeaRepairExpensesOracle> getTblCeaRepairExpenses(Long id);
}
