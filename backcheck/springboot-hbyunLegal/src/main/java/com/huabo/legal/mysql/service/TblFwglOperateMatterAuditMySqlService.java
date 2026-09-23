package com.huabo.legal.mysql.service;

import com.github.pagehelper.PageInfo;
import com.huabo.legal.mysql.entity.TblFwglOperateMatterAuditMySql;
import com.huabo.legal.vo.param.TblFwglOperateMatterAuditQueryParam;

public interface TblFwglOperateMatterAuditMySqlService {

	/**
	 * 经营事项审列表 查询
	 * @param param
	 * @return
	 */
	PageInfo<TblFwglOperateMatterAuditMySql> getList(TblFwglOperateMatterAuditQueryParam param);

	/**
	 * 经营事项审 新增/更新
	 * @param param
	 * @return
	 */
	TblFwglOperateMatterAuditMySql saveOrUpdate(TblFwglOperateMatterAuditMySql param);

	/**
	 * 经营事项审 删除
	 * @param id
	 */
	void delete(Integer id);

	/**
	 * 经营事项审详情 查询
	 * @param id
	 * @return
	 */
	TblFwglOperateMatterAuditMySql findById(Integer id);


}
