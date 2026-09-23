package com.huabo.legal.oracle.service;

import com.github.pagehelper.PageInfo;
import com.huabo.legal.oracle.entity.TblFwglLegalAdviserOracle;
import com.huabo.legal.vo.param.TblFwglLegalAdviserQueryParam;

public interface TblFwglLegalAdviserOracleService {

	/**
	 * 总法律顾问列表 查询
	 * @param param
	 * @return
	 */
	PageInfo<TblFwglLegalAdviserOracle> getList(TblFwglLegalAdviserQueryParam param);

	/**
	 * 总法律顾问 新增/更新
	 * @param param
	 * @return
	 */
	TblFwglLegalAdviserOracle saveOrUpdate(TblFwglLegalAdviserOracle param);

	/**
	 * 总法律顾问详情 查询
	 * @param id
	 * @return
	 */
	TblFwglLegalAdviserOracle findById(Long id);

	/**
	 * 总法律顾问 刪除
	 * @param id
	 */
	void delete(Long id);

}
