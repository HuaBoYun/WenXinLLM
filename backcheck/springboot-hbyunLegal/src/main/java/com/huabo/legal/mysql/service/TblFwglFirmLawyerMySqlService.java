package com.huabo.legal.mysql.service;

import com.github.pagehelper.PageInfo;
import com.huabo.legal.mysql.entity.TblFwglFirmLawyerMySql;
import com.huabo.legal.vo.param.TblFwglFirmLawyerQueryParam;

public interface TblFwglFirmLawyerMySqlService {

	/**
	 * 查询公司律师列表
	 * @param param
	 * @return
	 */
	PageInfo<TblFwglFirmLawyerMySql> getList(TblFwglFirmLawyerQueryParam param);

	/**
	 * 公司律师 新增/更新
	 * @param param
	 * @return
	 */
	TblFwglFirmLawyerMySql saveOrUpdate(TblFwglFirmLawyerMySql param);

	/**
	 * 公司律师 删除
	 * @param id
	 */
	void delete(Integer id);

	/**
	 * 查询公司律师
	 * @param id
	 * @return
	 */
	TblFwglFirmLawyerMySql findById(Integer id);
}
