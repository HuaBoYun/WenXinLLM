package com.regulatory.penetration.service;

import com.regulatory.penetration.oracle.entity.TblCeaConferenceMgtOracle;
import com.regulatory.penetration.util.MyJsonBean;
import com.regulatory.penetration.vo.param.TblCeaConferenceMgtQueryParam;
import com.regulatory.penetration.vo.result.FileVo;

public interface CeaConferenceMgtService {

	/**
	 * 会议管理 列表查询
	 * @param param
	 * @return
	 */
	MyJsonBean<TblCeaConferenceMgtOracle> getTblCeaConferenceMgtList(TblCeaConferenceMgtQueryParam param);

	/**
	 * 会议管理 新增/更新
	 * @param param
	 * @return
	 */
	MyJsonBean<TblCeaConferenceMgtOracle> saveOrUpdateTblCeaConferenceMgt(TblCeaConferenceMgtOracle param);

	/**
	 * 会议管理 刪除
	 * @param id
	 * @return
	 */
	MyJsonBean<Void> deleteTblCeaConferenceMgt(Long id);

	/**
	 * 会议管理 详情 查询
	 * @param id
	 * @return
	 */
	MyJsonBean<FileVo<TblCeaConferenceMgtOracle>> getTblCeaConferenceMgt(Long id);
}
