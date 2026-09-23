package com.huabo.central.enterprises.audit.service;

import com.huabo.central.enterprises.audit.oracle.entity.TblCeaOfficialReceptions;
import com.huabo.central.enterprises.audit.util.MyJsonBean;
import com.huabo.central.enterprises.audit.vo.param.TblCeaOfficialReceptionsQueryParam;
import com.huabo.central.enterprises.audit.vo.result.FileVo;

public interface CeaOfficialReceptionsService {

	/**
	 * 公务接待 列表查询
	 * @param param
	 * @return
	 */
	MyJsonBean<TblCeaOfficialReceptions> getTblCeaOfficialReceptionsList(TblCeaOfficialReceptionsQueryParam param);

	/**
	 * 公务接待 新增/更新
	 * @param param
	 * @return
	 */
	MyJsonBean<TblCeaOfficialReceptions> saveOrUpdateTblCeaOfficialReceptions(TblCeaOfficialReceptions param);

	/**
	 * 公务接待 刪除
	 * @param id
	 * @return
	 */
	MyJsonBean<Void> deleteTblCeaOfficialReceptions(Long id);

	/**
	 * 公务接待 详情 查询
	 * @param id
	 * @return
	 */
	MyJsonBean<FileVo<TblCeaOfficialReceptions>> getTblCeaOfficialReceptions(Long id);
}
