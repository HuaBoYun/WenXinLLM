package com.huabo.legal.oracle.service;

import com.github.pagehelper.PageInfo;
import com.huabo.legal.oracle.entity.TblFwglSpecialLawServiceExamineOracle;
import com.huabo.legal.vo.param.TblFwglSpecialLawServiceExamineQueryParam;

import java.util.List;

public interface TblFwglSpecialLawServiceExamineOracleService {

	/**
	 * 根据 专项法律服务-考核列表 查询
	 * @param examineId
	 * @return
	 */
	List<TblFwglSpecialLawServiceExamineOracle> getList(String examineId);

	/**
	 * 专项法律服务-考核 新增/更新
	 * @param param
	 * @return
	 */
	TblFwglSpecialLawServiceExamineOracle saveOrUpdate(TblFwglSpecialLawServiceExamineOracle param);

	/**
	 * 专项法律服务-考核详情 查询
	 * @param id
	 * @return
	 */
	TblFwglSpecialLawServiceExamineOracle findById(Long id);

	/**
	 * 专项法律服务-考核 删除
	 * @param id
	 */
	void delete(Long id);

	/**
	 * 考核台账列表
	 * @param examineId
	 * @return
	 */
	PageInfo<TblFwglSpecialLawServiceExamineOracle> getList(TblFwglSpecialLawServiceExamineQueryParam examineId);
}
