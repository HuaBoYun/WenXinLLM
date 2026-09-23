package com.huabo.central.enterprises.audit.service;

import com.huabo.central.enterprises.audit.oracle.entity.TblCeaInternalWebsiteApplyOracle;
import com.huabo.central.enterprises.audit.util.MyJsonBean;
import com.huabo.central.enterprises.audit.vo.param.TblCeaInternalWebsiteApplyQueryParam;

public interface CeaInternalWebsiteApplyService {

	/**
	 * 内部网站申信息发布 列表查询
	 * @param param
	 * @return
	 */
	MyJsonBean<TblCeaInternalWebsiteApplyOracle> getTblCeaInternalWebsiteApplyList(TblCeaInternalWebsiteApplyQueryParam param);

	/**
	 * 内部网站申信息发布 新增/更新
	 * @param param
	 * @return
	 */
	MyJsonBean<TblCeaInternalWebsiteApplyOracle> saveOrUpdateTblCeaInternalWebsiteApply(TblCeaInternalWebsiteApplyOracle param);

	/**
	 * 内部网站申信息发布 刪除
	 * @param id
	 * @return
	 */
	MyJsonBean<Void> deleteTblCeaInternalWebsiteApply(Long id);

	/**
	 * 内部网站申信息发布 详情 查询
	 * @param id
	 * @return
	 */
	MyJsonBean<TblCeaInternalWebsiteApplyOracle> getTblCeaInternalWebsiteApply(Long id);
}
