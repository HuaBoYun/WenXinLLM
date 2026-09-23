package com.huabo.legal.oracle.service;

import com.github.pagehelper.PageInfo;
import com.huabo.legal.oracle.entity.TblFwglLawServiceOracle;
import com.huabo.legal.vo.param.TblFwglLawServiceQueryParam;

public interface TblFwglLawServiceOracleService {

	/**
	 * 查询常年法律服务/专项法律服务列表
	 * @param param
	 * @return
	 */
	PageInfo<TblFwglLawServiceOracle> getList(TblFwglLawServiceQueryParam param);

	/**
	 * 常年法律服务/专项法律服务 新增/更新
	 * @param param
	 * @return
	 */
	TblFwglLawServiceOracle saveOrUpdate(TblFwglLawServiceOracle param);

	/**
	 * 常年法律服务/专项法律服务 删除
	 * @param id
	 */
	void delete(Long id);

	/**
	 * 常年法律服务/专项法律服务详情 查询
	 * @param id
	 * @return
	 */
	TblFwglLawServiceOracle findById(Long id);
}
