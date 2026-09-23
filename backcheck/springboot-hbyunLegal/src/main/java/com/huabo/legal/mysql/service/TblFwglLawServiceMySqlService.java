package com.huabo.legal.mysql.service;

import com.github.pagehelper.PageInfo;
import com.huabo.legal.mysql.entity.TblFwglLawServiceMySql;
import com.huabo.legal.vo.param.TblFwglLawServiceQueryParam;

public interface TblFwglLawServiceMySqlService {

	/**
	 * 查询常年法律服务/专项法律服务列表
	 * @param param
	 * @return
	 */
	PageInfo<TblFwglLawServiceMySql> getList(TblFwglLawServiceQueryParam param);

	/**
	 * 常年法律服务/专项法律服务 新增/更新
	 * @param param
	 * @return
	 */
	TblFwglLawServiceMySql saveOrUpdate(TblFwglLawServiceMySql param);

	/**
	 * 常年法律服务/专项法律服务 删除
	 * @param id
	 */
	void delete(Integer id);

	/**
	 * 常年法律服务/专项法律服务详情 查询
	 * @param id
	 * @return
	 */
	TblFwglLawServiceMySql findById(Integer id);
}
