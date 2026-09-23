package com.huabo.central.enterprises.audit.oracle.service;

import com.github.pagehelper.PageInfo;
import com.huabo.central.enterprises.audit.oracle.entity.TblCeaMailMgtOracle;
import com.huabo.central.enterprises.audit.vo.param.TblCeaMailMgtQueryParam;

public interface TblCeaMailMgtOracleService {

	/**
	 * 中石油邮箱管理 列表查询
	 * @param param
	 * @return
	 */
	PageInfo<TblCeaMailMgtOracle> getList(TblCeaMailMgtQueryParam param);

	/**
	 * 中石油邮箱管理 新增/更新
	 * @param param
	 * @return
	 */
	TblCeaMailMgtOracle saveOrUpdate(TblCeaMailMgtOracle param);

	/**
	 * 中石油邮箱管理 删除
	 * @param id
	 */
	void delete(Long id);

	/**
	 * 中石油邮箱管理 详情查询
	 * @param id
	 * @return
	 */
	TblCeaMailMgtOracle findById(Long id);
}
