package com.huabo.legal.oracle.service;

import com.huabo.legal.oracle.entity.TblFwglLawServiceEvaluateOracle;

import java.util.List;

public interface TblFwglLawServiceEvaluateOracleService {

	/**
	 * 常年法律服务-评价 列表 查询
	 * @param ids
	 * @return
	 */
	List<TblFwglLawServiceEvaluateOracle> getList(String ids);

	/**
	 * 常年法律服务-评价 新增/更新
	 * @param param
	 * @return
	 */
	TblFwglLawServiceEvaluateOracle saveOrUpdate(TblFwglLawServiceEvaluateOracle param);

	/**
	 * 常年法律服务-评价 删除
	 * @param id
	 */
	void delete(Long id);

	/**
	 * 常年法律服务-评价 查询
	 * @param id
	 * @return
	 */
	TblFwglLawServiceEvaluateOracle findById(Long id);
}
