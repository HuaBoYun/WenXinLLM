package com.huabo.central.enterprises.audit.oracle.service;

import com.github.pagehelper.PageInfo;
import com.huabo.central.enterprises.audit.oracle.entity.TblCeaInternalWebsiteApplyOracle;
import com.huabo.central.enterprises.audit.vo.param.TblCeaInternalWebsiteApplyQueryParam;

public interface TblCeaInternalWebsiteApplyOracleService {

	/**
	 * 内部网站申信息发布 列表查询
	 * @param param
	 * @return
	 */
	PageInfo<TblCeaInternalWebsiteApplyOracle> getList(TblCeaInternalWebsiteApplyQueryParam param);

	/**
	 * 内部网站申信息发布 新增/更新
	 * @param param
	 * @return
	 */
	TblCeaInternalWebsiteApplyOracle saveOrUpdate(TblCeaInternalWebsiteApplyOracle param);

	/**
	 * 内部网站申信息发布 删除
	 * @param id
	 */
	void delete(Long id);

	/**
	 * 内部网站申信息发布 详情查询
	 * @param id
	 * @return
	 */
	TblCeaInternalWebsiteApplyOracle findById(Long id);
}
