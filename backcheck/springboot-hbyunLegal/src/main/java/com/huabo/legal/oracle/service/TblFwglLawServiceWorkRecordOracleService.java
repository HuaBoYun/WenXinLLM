package com.huabo.legal.oracle.service;

import com.huabo.legal.oracle.entity.TblFwglLawServiceWorkRecordOracle;

import java.util.List;

public interface TblFwglLawServiceWorkRecordOracleService {

	/**
	 * 根据常年法律服务id/专项法律服务id 法律服务-工作记录列表 查询
	 * @param workRecordId
	 * @return
	 */
	List<TblFwglLawServiceWorkRecordOracle> getList(String workRecordId);

	/**
	 * 法律服务-工作记录 新增/更新
	 * @param param
	 * @return
	 */
	TblFwglLawServiceWorkRecordOracle saveOrUpdate(TblFwglLawServiceWorkRecordOracle param);

	/**
	 * 法律服务-工作记录详情 查询
	 * @param id
	 * @return
	 */
	TblFwglLawServiceWorkRecordOracle findById(Long id);

	/**
	 * 法律服务-工作记录 删除
	 * @param id
	 */
	void delete(Long id);
}

