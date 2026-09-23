package com.huabo.legal.mysql.service;

import com.github.pagehelper.PageInfo;
import com.huabo.legal.mysql.entity.TblFwglLegalAdviserMySql;
import com.huabo.legal.vo.param.TblFwglLegalAdviserQueryParam;

public interface TblFwglLegalAdviserMySqlService {

	/**
	 * 总法律顾问列表 查询
	 * @param param
	 * @return
	 */
	PageInfo<TblFwglLegalAdviserMySql> getList(TblFwglLegalAdviserQueryParam param);

	/**
	 * 总法律顾问 新增/更新
	 * @param param
	 * @return
	 */
	TblFwglLegalAdviserMySql saveOrUpdate(TblFwglLegalAdviserMySql param);

	/**
	 * 总法律顾问详情 查询
	 * @param id
	 * @return
	 */
	TblFwglLegalAdviserMySql findById(Integer id);

	/**
	 * 总法律顾问 刪除
	 * @param id
	 */
	void delete(Integer id);

}
