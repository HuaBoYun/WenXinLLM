package com.huabo.legal.oracle.service;

import com.github.pagehelper.PageInfo;
import com.huabo.legal.mysql.entity.TblFwglOperateMatterAuditMySql;
import com.huabo.legal.oracle.entity.TblFwglOperateMatterAuditOracle;
import com.huabo.legal.vo.param.TblFwglOperateMatterAuditQueryParam;

public interface TblFwglOperateMatterAuditOracleService {

	/**
	 * 经营事项审列表 查询
	 * @param param
	 * @return
	 */
	PageInfo<TblFwglOperateMatterAuditOracle> getList(TblFwglOperateMatterAuditQueryParam param);

	/**
	 * 经营事项审 新增/更新
	 * @param param
	 * @return
	 */
	TblFwglOperateMatterAuditOracle saveOrUpdate(TblFwglOperateMatterAuditOracle param);

	/**
	 * 经营事项审 删除
	 * @param id
	 */
	void delete(Long id);

	/**
	 * 经营事项审详情 查询
	 * @param id
	 * @return
	 */
	TblFwglOperateMatterAuditOracle findById(Long id);


}
