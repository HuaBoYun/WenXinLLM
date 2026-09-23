package com.huabo.central.enterprises.audit.oracle.service;

import com.github.pagehelper.PageInfo;
import com.huabo.central.enterprises.audit.oracle.entity.TblCeaLiQing;
import com.huabo.central.enterprises.audit.vo.param.TblCeaLiQingQueryParam;

public interface TblCeaLiQingService {

	/**
	 * 员工离庆 列表查询
	 * @param param
	 * @return
	 */
	PageInfo<TblCeaLiQing> getList(TblCeaLiQingQueryParam param);

	/**
	 * 员工离庆 新增/更新
	 * @param param
	 * @return
	 */
	TblCeaLiQing saveOrUpdate(TblCeaLiQing param);

	/**
	 * 员工离庆 删除
	 * @param id
	 */
	void delete(Long id);

	/**
	 * 员工离庆 详情查询
	 * @param id
	 * @return
	 */
	TblCeaLiQing findById(Long id);
}
