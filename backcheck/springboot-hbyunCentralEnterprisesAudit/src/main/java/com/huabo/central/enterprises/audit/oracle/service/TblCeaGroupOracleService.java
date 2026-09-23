package com.huabo.central.enterprises.audit.oracle.service;

import com.github.pagehelper.PageInfo;
import com.huabo.central.enterprises.audit.oracle.entity.TblCeaGroupOracle;
import com.huabo.central.enterprises.audit.vo.param.TblCeaGroupQueryParam;

public interface TblCeaGroupOracleService {

	/**
	 * 群组 列表查询
	 * @param param
	 * @return
	 */
	PageInfo<TblCeaGroupOracle> getList(TblCeaGroupQueryParam param);

	/**
	 * 群组 新增/更新
	 * @param param
	 * @return
	 */
	TblCeaGroupOracle saveOrUpdate(TblCeaGroupOracle param);

	/**
	 * 群组 删除
	 * @param id
	 */
	void delete(Long id);

	/**
	 * 群组 详情查询
	 * @param id
	 * @return
	 */
	TblCeaGroupOracle findById(Long id);
}
