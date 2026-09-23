package com.huabo.legal.oracle.service;

import com.github.pagehelper.PageInfo;
import com.huabo.legal.oracle.entity.TblFwglLeaderXfOracle;
import com.huabo.legal.vo.param.TblFwglLeaderXfQueryParam;

public interface TblFwglLeaderXfOracleService {

	/**
	 * 领导学法列表 查询
	 * @param param
	 * @return
	 */
	PageInfo<TblFwglLeaderXfOracle> getList(TblFwglLeaderXfQueryParam param);

	/**
	 * 领导学法 新增/更新
	 * @param param
	 * @return
	 */
	TblFwglLeaderXfOracle saveOrUpdate(TblFwglLeaderXfOracle param);

	/**
	 * 领导学法详情 查询
	 * @param id
	 * @return
	 */
	TblFwglLeaderXfOracle findById(Long id);

	/**
	 * 领导学法 刪除
	 * @param id
	 */
	void delete(Long id);
}
