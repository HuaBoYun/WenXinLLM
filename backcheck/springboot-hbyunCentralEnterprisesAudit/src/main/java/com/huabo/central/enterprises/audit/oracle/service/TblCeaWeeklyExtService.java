package com.huabo.central.enterprises.audit.oracle.service;

import com.huabo.central.enterprises.audit.oracle.entity.TblCeaWeeklyExt;

import java.util.List;

public interface TblCeaWeeklyExtService {

	/**
	 * 周报 新增/更新
	 * @param param
	 * @return
	 */
	TblCeaWeeklyExt saveOrUpdate(TblCeaWeeklyExt param);

	/**
	 * 周报 删除
	 * @param id
	 */
	void delete(Long id);

	/**
	 * 周报 详情查询
	 * @param id
	 * @return
	 */
	TblCeaWeeklyExt findById(Long id);

	/**
	 * 查询汇总数据
	 * @param id
	 * @return
	 */
	List<TblCeaWeeklyExt> getList(Long id);
}
