package com.huabo.central.enterprises.audit.oracle.service;

import com.github.pagehelper.PageInfo;
import com.huabo.central.enterprises.audit.oracle.entity.TblCeaInsideReportedOracle;
import com.huabo.central.enterprises.audit.vo.param.TblCeaInsideReportedQueryParam;

public interface TblCeaInsideReportedOracleService {

	/**
	 * 内部文件呈报 列表查询
	 * @param param
	 * @return
	 */
	PageInfo<TblCeaInsideReportedOracle> getList(TblCeaInsideReportedQueryParam param);

	/**
	 * 内部文件呈报 新增/更新
	 * @param param
	 * @return
	 */
	TblCeaInsideReportedOracle saveOrUpdate(TblCeaInsideReportedOracle param);

	/**
	 * 内部文件呈报 删除
	 * @param id
	 */
	void delete(Long id);

	/**
	 * 内部文件呈报 详情查询
	 * @param id
	 * @return
	 */
	TblCeaInsideReportedOracle findById(Long id);
}
