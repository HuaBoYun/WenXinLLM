package com.huabo.legal.oracle.service;

import com.github.pagehelper.PageInfo;
import com.huabo.legal.oracle.entity.TblFwglFirmLawyerOracle;
import com.huabo.legal.vo.param.TblFwglFirmLawyerQueryParam;

public interface TblFwglFirmLawyerOracleService {

	/**
	 * 查询公司律师列表
	 * @param param
	 * @return
	 */
	PageInfo<TblFwglFirmLawyerOracle> getList(TblFwglFirmLawyerQueryParam param);

	/**
	 * 公司律师 新增/更新
	 * @param param
	 * @return
	 */
	TblFwglFirmLawyerOracle saveOrUpdate(TblFwglFirmLawyerOracle param);

	/**
	 * 公司律师 删除
	 * @param id
	 */
	void delete(Long id);

	/**
	 * 查询公司律师
	 * @param id
	 * @return
	 */
	TblFwglFirmLawyerOracle findById(Long id);
}
