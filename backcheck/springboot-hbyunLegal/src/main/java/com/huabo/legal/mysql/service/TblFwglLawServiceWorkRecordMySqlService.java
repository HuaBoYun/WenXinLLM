package com.huabo.legal.mysql.service;

import com.huabo.legal.mysql.entity.TblFwglLawServiceWorkRecordMySql;

import java.util.List;

public interface TblFwglLawServiceWorkRecordMySqlService {

	/**
	 * 根据常年法律服务id/专项法律服务id 法律服务-工作记录列表 查询
	 * @param workRecordId
	 * @return
	 */
	List<TblFwglLawServiceWorkRecordMySql> getList(String workRecordId);

	/**
	 * 法律服务-工作记录 新增/更新
	 * @param param
	 * @return
	 */
	TblFwglLawServiceWorkRecordMySql saveOrUpdate(TblFwglLawServiceWorkRecordMySql param);

	/**
	 * 法律服务-工作记录详情 查询
	 * @param id
	 * @return
	 */
	TblFwglLawServiceWorkRecordMySql findById(Integer id);

	/**
	 * 法律服务-工作记录 删除
	 * @param id
	 */
	void delete(Integer id);
}

