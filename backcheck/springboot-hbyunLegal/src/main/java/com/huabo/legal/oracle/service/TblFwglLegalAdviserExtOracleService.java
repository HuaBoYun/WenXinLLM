package com.huabo.legal.oracle.service;

import com.huabo.legal.oracle.entity.TblFwglLegalAdviserExtOracle;

import java.util.List;

public interface TblFwglLegalAdviserExtOracleService {

	/**
	 * 根据总法律顾问扩展IDS 总法律顾问-工作经历列表 查询
	 * @param adviserExtIds 总法律顾问扩展IDS
	 * @return
	 */
	List<TblFwglLegalAdviserExtOracle> getList(String adviserExtIds);

	/**
	 * 总法律顾问-工作经历 新增/更新
	 * @param param
	 * @return
	 */
	TblFwglLegalAdviserExtOracle saveOrUpdate(TblFwglLegalAdviserExtOracle param);

	/**
	 * 总法律顾问-工作经历详情 查询
	 * @param id
	 * @return
	 */
	TblFwglLegalAdviserExtOracle findById(Long id);

	/**
	 * 总法律顾问-工作经历 删除
	 * @param id
	 */
	void delete(Long id);
}
