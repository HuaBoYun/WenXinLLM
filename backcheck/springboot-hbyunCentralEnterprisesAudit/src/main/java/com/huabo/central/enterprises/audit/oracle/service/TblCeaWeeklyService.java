package com.huabo.central.enterprises.audit.oracle.service;

import com.github.pagehelper.PageInfo;
import com.huabo.central.enterprises.audit.oracle.entity.TblCeaWeekly;
import com.huabo.central.enterprises.audit.vo.param.TblCeaWeeklyQueryParam;

import java.util.List;

public interface TblCeaWeeklyService {

	/**
	 * 周报 列表查询
	 * @param param
	 * @return
	 */
	PageInfo<TblCeaWeekly> getList(TblCeaWeeklyQueryParam param);

	/**
	 * 周报 新增/更新
	 * @param param
	 * @return
	 */
	TblCeaWeekly saveOrUpdate(TblCeaWeekly param);

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
	TblCeaWeekly findById(Long id);

	/**
	 * 汇总周报列表
	 * @param ids
	 * @return
	 */
	List<TblCeaWeekly> getIdsList(List<Long> ids);
}
