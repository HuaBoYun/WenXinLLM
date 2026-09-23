package com.huabo.central.enterprises.audit.oracle.service;

import com.github.pagehelper.PageInfo;
import com.huabo.central.enterprises.audit.oracle.entity.TblCeaSupervisionNoticeOracle;
import com.huabo.central.enterprises.audit.vo.param.TblCeaSupervisionNoticeQueryParam;

public interface TblCeaSupervisionNoticeOracleService {

	/**
	 * 督办通知单 列表查询
	 * @param param
	 * @return
	 */
	PageInfo<TblCeaSupervisionNoticeOracle> getList(TblCeaSupervisionNoticeQueryParam param);

	/**
	 * 督办通知单 新增/更新
	 * @param param
	 * @return
	 */
	TblCeaSupervisionNoticeOracle saveOrUpdate(TblCeaSupervisionNoticeOracle param);

	/**
	 * 督办通知单 删除
	 * @param id
	 */
	void delete(Long id);

	/**
	 * 督办通知单 详情查询
	 * @param id
	 * @return
	 */
	TblCeaSupervisionNoticeOracle findById(Long id);
}
