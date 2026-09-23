package com.huabo.central.enterprises.audit.oracle.service;

import com.github.pagehelper.PageInfo;
import com.huabo.central.enterprises.audit.oracle.entity.TblCeaRepairExpensesOracle;
import com.huabo.central.enterprises.audit.vo.param.TblCeaRepairExpensesQueryParam;

public interface TblCeaRepairExpensesOracleService {

	/**
	 * 修理费支出 列表查询
	 * @param param
	 * @return
	 */
	PageInfo<TblCeaRepairExpensesOracle> getList(TblCeaRepairExpensesQueryParam param);

	/**
	 * 修理费支出 新增/更新
	 * @param param
	 * @return
	 */
	TblCeaRepairExpensesOracle saveOrUpdate(TblCeaRepairExpensesOracle param);

	/**
	 * 修理费支出 删除
	 * @param id
	 */
	void delete(Long id);

	/**
	 * 修理费支出 详情查询
	 * @param id
	 * @return
	 */
	TblCeaRepairExpensesOracle findById(Long id);
}

