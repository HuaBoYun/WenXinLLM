package com.huabo.legal.mysql.service;

import com.huabo.legal.mysql.entity.TblFwglLegalPersonnelExtMySql;

import java.util.List;

public interface TblFwglLegalPersonnelExtMySqlService {

	/**
	 * 法务人员-工作经历 新增/更新
	 * @param param
	 * @return
	 */
	TblFwglLegalPersonnelExtMySql saveOrUpdate(TblFwglLegalPersonnelExtMySql param);

	/**
	 * 法务人员-工作经历详情 查询
	 * @param id
	 * @return
	 */
	TblFwglLegalPersonnelExtMySql findById(Integer id);

	/**
	 * 法务人员-工作经历 删除
	 * @param id
	 */
	void delete(Integer id);

	/**
	 * 根据法务人员扩展ID 法务人员-工作经历列表 查询
	 * @param personnelExtId 法务人员扩展ID
	 */
	List<TblFwglLegalPersonnelExtMySql> getList(String personnelExtId);

}
