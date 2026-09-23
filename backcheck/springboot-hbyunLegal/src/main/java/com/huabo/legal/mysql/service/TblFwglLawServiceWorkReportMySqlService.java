package com.huabo.legal.mysql.service;

import com.huabo.legal.mysql.entity.TblFwglLawServiceWorkReportMySql;

import java.util.List;

public interface TblFwglLawServiceWorkReportMySqlService {

	/**
	 * 根据常年法律服务id/专项法律服务id 法律服务-工作报告表/服务登记列表 查询
	 * @param workReportId
	 * @return
	 */
	List<TblFwglLawServiceWorkReportMySql> getList(String workReportId);

	/**
	 * 法律服务-工作报告表/服务登记 新增/更新
	 * @param param
	 * @return
	 */
	TblFwglLawServiceWorkReportMySql saveOrUpdate(TblFwglLawServiceWorkReportMySql param);

	/**
	 * 法律服务-工作报告表/服务登记详情 查询
	 * @param id
	 * @return
	 */
	TblFwglLawServiceWorkReportMySql findById(Integer id);

	/**
	 * 法律服务-工作报告表/服务登记 删除
	 * @param id
	 */
	void delete(Integer id);
}
