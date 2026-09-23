package com.huabo.legal.oracle.service;

import com.huabo.legal.oracle.entity.TblFwglLawServiceLawyerOracle;

import java.util.List;

public interface TblFwglLawServiceLawyerOracleService {

	/**
	 * 根据常年法律服务id/专项法律服务id 法律服务-律师信息列表 查询
	 * @param lawyerId 律师id
	 * @return
	 */
	List<TblFwglLawServiceLawyerOracle> getList(String lawyerId);

	/**
	 * 法律服务-律师信息 新增/更新
	 * @param param
	 * @return
	 */
	TblFwglLawServiceLawyerOracle saveOrUpdate(TblFwglLawServiceLawyerOracle param);

	/**
	 * 法律服务-律师信息详情 查询
	 * @param id
	 * @return
	 */
	TblFwglLawServiceLawyerOracle findById(Long id);

	/**
	 * 法律服务-律师信息 删除
	 * @param id
	 */
	void delete(Long id);
}
