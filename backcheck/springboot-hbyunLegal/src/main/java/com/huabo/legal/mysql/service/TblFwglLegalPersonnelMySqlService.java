package com.huabo.legal.mysql.service;

import com.github.pagehelper.PageInfo;
import com.huabo.legal.mysql.entity.TblFwglLegalPersonnelMySql;
import com.huabo.legal.vo.param.TblFwglLegalPersonnelQueryParam;

public interface TblFwglLegalPersonnelMySqlService {

	/**
	 * 法务人员列表 查询
	 * @param param
	 * @return
	 */
	PageInfo<TblFwglLegalPersonnelMySql> getList(TblFwglLegalPersonnelQueryParam param);

	/**
	 * 法务人员 新增/更新
	 * @param param
	 * @return
	 */
	TblFwglLegalPersonnelMySql saveOrUpdate(TblFwglLegalPersonnelMySql param);

	/**
	 * 法务人员详情 查询
	 * @param id
	 * @return
	 */
	TblFwglLegalPersonnelMySql findById(Integer id);

	/**
	 * 法务人员 刪除
	 * @param id
	 */
	void delete(Integer id);
}
