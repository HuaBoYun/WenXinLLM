package com.huabo.legal.oracle.service;

import com.huabo.legal.oracle.entity.TblFwglLegalPersonnelExtOracle;

import java.util.List;

public interface TblFwglLegalPersonnelExtOracleService {

	/**
	 * 法务人员-工作经历 新增/更新
	 * @param param
	 * @return
	 */
	TblFwglLegalPersonnelExtOracle saveOrUpdate(TblFwglLegalPersonnelExtOracle param);

	/**
	 * 法务人员-工作经历详情 查询
	 * @param id
	 * @return
	 */
	TblFwglLegalPersonnelExtOracle findById(Long id);

	/**
	 * 法务人员-工作经历 删除
	 * @param id
	 */
	void delete(Long id);

	/**
	 * 根据法务人员扩展ID 法务人员-工作经历列表 查询
	 * @param personnelExtId 法务人员扩展ID
	 */
	List<TblFwglLegalPersonnelExtOracle> getList(String personnelExtId);

}
