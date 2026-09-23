package com.huabo.legal.oracle.service;

import com.huabo.legal.oracle.entity.TblFwglFirmLawyerExtOracle;

import java.util.List;

public interface TblFwglFirmLawyerExtOracleService {

	/**
	 * 根据公司律师扩展ID 公司律师-简历列表 查询
	 * @param lawyerExtId 公司律师扩展ID
	 * @return
	 */
	List<TblFwglFirmLawyerExtOracle> getList(String lawyerExtId);

	/**
	 * 公司律师-简历 新增/更新
	 * @param param
	 * @return
	 */
	TblFwglFirmLawyerExtOracle saveOrUpdate(TblFwglFirmLawyerExtOracle param);

	/**
	 * 公司律师-简历详情 查询
	 * @param id
	 * @return
	 */
	TblFwglFirmLawyerExtOracle findById(Long id);

	/**
	 * 公司律师-简历 删除
	 * @param id
	 */
	void delete(Long id);
}
