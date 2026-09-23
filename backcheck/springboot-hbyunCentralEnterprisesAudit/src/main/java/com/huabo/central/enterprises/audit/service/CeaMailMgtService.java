package com.huabo.central.enterprises.audit.service;

import com.huabo.central.enterprises.audit.oracle.entity.TblCeaMailMgtOracle;
import com.huabo.central.enterprises.audit.util.MyJsonBean;
import com.huabo.central.enterprises.audit.vo.param.TblCeaMailMgtQueryParam;

public interface CeaMailMgtService {

	/**
	 * 中石油邮箱管理 列表查询
	 * @param param
	 * @return
	 */
	MyJsonBean<TblCeaMailMgtOracle> getTblCeaMailMgtList(TblCeaMailMgtQueryParam param);

	/**
	 * 中石油邮箱管理 新增/更新
	 * @param param
	 * @return
	 */
	MyJsonBean<TblCeaMailMgtOracle> saveOrUpdateTblCeaMailMgt(TblCeaMailMgtOracle param);

	/**
	 * 中石油邮箱管理 刪除
	 * @param id
	 * @return
	 */
	MyJsonBean<Void> deleteTblCeaMailMgt(Long id);

	/**
	 * 中石油邮箱管理 详情 查询
	 * @param id
	 * @return
	 */
	MyJsonBean<TblCeaMailMgtOracle> getTblCeaMailMgt(Long id);
}
