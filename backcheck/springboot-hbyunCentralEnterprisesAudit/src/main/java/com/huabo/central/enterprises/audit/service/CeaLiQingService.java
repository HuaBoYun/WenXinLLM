package com.huabo.central.enterprises.audit.service;

import com.huabo.central.enterprises.audit.oracle.entity.TblCeaLiQing;
import com.huabo.central.enterprises.audit.util.MyJsonBean;
import com.huabo.central.enterprises.audit.vo.param.TblCeaLiQingQueryParam;
import com.huabo.central.enterprises.audit.vo.result.FileVo;

public interface CeaLiQingService {

	/**
	 * 员工离庆 列表查询
	 * @param param
	 * @return
	 */
	MyJsonBean<TblCeaLiQing> getTblCeaLiQingList(TblCeaLiQingQueryParam param);

	/**
	 * 员工离庆 新增/更新
	 * @param param
	 * @return
	 */
	MyJsonBean<TblCeaLiQing> saveOrUpdateTblCeaLiQing(TblCeaLiQing param);

	/**
	 * 员工离庆 刪除
	 * @param id
	 * @return
	 */
	MyJsonBean<Void> deleteTblCeaLiQing(Long id);

	/**
	 * 员工离庆 详情 查询
	 * @param id
	 * @return
	 */
	MyJsonBean<FileVo<TblCeaLiQing>> getTblCeaLiQing(Long id);
}
