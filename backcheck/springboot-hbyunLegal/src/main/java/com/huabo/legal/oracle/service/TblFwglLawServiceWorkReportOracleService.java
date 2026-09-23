package com.huabo.legal.oracle.service;

import com.huabo.legal.oracle.entity.TblFwglLawServiceWorkReportOracle;

import java.util.List;

public interface TblFwglLawServiceWorkReportOracleService {

	/**
	 * 根据常年法律服务id/专项法律服务id 法律服务-工作报告表/服务登记列表 查询
	 * @param workReportId
	 * @return
	 */
	List<TblFwglLawServiceWorkReportOracle> getList(String workReportId);

	/**
	 * 法律服务-工作报告表/服务登记 新增/更新
	 * @param param
	 * @return
	 */
	TblFwglLawServiceWorkReportOracle saveOrUpdate(TblFwglLawServiceWorkReportOracle param);

	/**
	 * 法律服务-工作报告表/服务登记详情 查询
	 * @param id
	 * @return
	 */
	TblFwglLawServiceWorkReportOracle findById(Long id);

	/**
	 * 法律服务-工作报告表/服务登记 删除
	 * @param id
	 */
	void delete(Long id);
}
