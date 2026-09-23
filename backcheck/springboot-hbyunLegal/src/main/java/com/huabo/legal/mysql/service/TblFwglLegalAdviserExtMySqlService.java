package com.huabo.legal.mysql.service;

import com.huabo.legal.mysql.entity.TblFwglLegalAdviserExtMySql;

import java.util.List;

public interface TblFwglLegalAdviserExtMySqlService {

	/**
	 * 根据总法律顾问扩展IDS 总法律顾问-工作经历列表 查询
	 * @param adviserExtIds 总法律顾问扩展IDS
	 * @return
	 */
	List<TblFwglLegalAdviserExtMySql> getList(String adviserExtIds);

	/**
	 * 总法律顾问-工作经历 新增/更新
	 * @param param
	 * @return
	 */
	TblFwglLegalAdviserExtMySql saveOrUpdate(TblFwglLegalAdviserExtMySql param);

	/**
	 * 总法律顾问-工作经历详情 查询
	 * @param id
	 * @return
	 */
	TblFwglLegalAdviserExtMySql findById(Integer id);

	/**
	 * 总法律顾问-工作经历 删除
	 * @param id
	 */
	void delete(Integer id);
}
