package com.financial.sharing.oracle.service;

import com.financial.sharing.oracle.entity.TblCeaConferenceMgtOracle;
import com.financial.sharing.vo.param.TblCeaConferenceMgtQueryParam;
import com.github.pagehelper.PageInfo;

public interface TblCeaConferenceMgtOracleService {

	/**
	 * 会议管理 列表查询
	 * @param param
	 * @return
	 */
	PageInfo<TblCeaConferenceMgtOracle> getList(TblCeaConferenceMgtQueryParam param);

	/**
	 * 会议管理 新增/更新
	 * @param param
	 * @return
	 */
	TblCeaConferenceMgtOracle saveOrUpdate(TblCeaConferenceMgtOracle param);

	/**
	 * 会议管理 删除
	 * @param id
	 */
	void delete(Long id);

	/**
	 * 会议管理 详情查询
	 * @param id
	 * @return
	 */
	TblCeaConferenceMgtOracle findById(Long id);
}
