package com.huabo.central.enterprises.audit.service;

import com.huabo.central.enterprises.audit.oracle.entity.TblCeaInsideReportedOracle;
import com.huabo.central.enterprises.audit.util.MyJsonBean;
import com.huabo.central.enterprises.audit.vo.param.TblCeaInsideReportedQueryParam;
import com.huabo.central.enterprises.audit.vo.result.FileVo;

public interface CeaInsideReportedService {

	/**
	 * 内部文件呈报 列表查询
	 * @param param
	 * @return
	 */
	MyJsonBean<TblCeaInsideReportedOracle> getTblCeaInsideReportedList(TblCeaInsideReportedQueryParam param);

	/**
	 * 内部文件呈报 新增/更新
	 * @param param
	 * @return
	 */
	MyJsonBean<TblCeaInsideReportedOracle> saveOrUpdateTblCeaInsideReported(TblCeaInsideReportedOracle param);

	/**
	 * 内部文件呈报 刪除
	 * @param id
	 * @return
	 */
	MyJsonBean<Void> deleteTblCeaInsideReported(Long id);

	/**
	 * 内部文件呈报 详情 查询
	 * @param id
	 * @return
	 */
	MyJsonBean<FileVo<TblCeaInsideReportedOracle>> getTblCeaInsideReported(Long id);
}
