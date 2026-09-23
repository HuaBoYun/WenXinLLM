package com.huabo.central.enterprises.audit.oracle.service;

import com.github.pagehelper.PageInfo;
import com.huabo.central.enterprises.audit.oracle.entity.TblCeaOfficialReceptions;
import com.huabo.central.enterprises.audit.vo.param.TblCeaOfficialReceptionsQueryParam;

public interface TblCeaOfficialReceptionsService {

	/**
	 * 公务接待 列表查询
	 * @param param
	 * @return
	 */
	PageInfo<TblCeaOfficialReceptions> getList(TblCeaOfficialReceptionsQueryParam param);

	/**
	 * 公务接待 新增/更新
	 * @param param
	 * @return
	 */
	TblCeaOfficialReceptions saveOrUpdate(TblCeaOfficialReceptions param);

	/**
	 * 公务接待 删除
	 * @param id
	 */
	void delete(Long id);

	/**
	 * 公务接待 详情查询
	 * @param id
	 * @return
	 */
	TblCeaOfficialReceptions findById(Long id);
}
