package com.huabo.legal.mysql.service;

import com.huabo.legal.mysql.entity.TblFwglFirmLawyerExtMySql;

import java.util.List;

public interface TblFwglFirmLawyerExtMySqlService {

	/**
	 * 根据公司律师扩展ID 公司律师-简历列表 查询
	 * @param lawyerExtId 公司律师扩展ID
	 * @return
	 */
	List<TblFwglFirmLawyerExtMySql> getList(String lawyerExtId);

	/**
	 * 公司律师-简历 新增/更新
	 * @param param
	 * @return
	 */
	TblFwglFirmLawyerExtMySql saveOrUpdate(TblFwglFirmLawyerExtMySql param);

	/**
	 * 公司律师-简历详情 查询
	 * @param id
	 * @return
	 */
	TblFwglFirmLawyerExtMySql findById(Integer id);

	/**
	 * 公司律师-简历 删除
	 * @param id
	 */
	void delete(Integer id);
}
