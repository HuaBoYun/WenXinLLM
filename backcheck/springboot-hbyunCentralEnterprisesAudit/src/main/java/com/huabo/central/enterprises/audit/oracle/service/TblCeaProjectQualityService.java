package com.huabo.central.enterprises.audit.oracle.service;

import com.github.pagehelper.PageInfo;
import com.huabo.central.enterprises.audit.oracle.entity.TblCeaProjectQuality;
import com.huabo.central.enterprises.audit.util.PageableParam;
import com.huabo.central.enterprises.audit.vo.param.TblCeaProjectQualityQueryParam;
import com.huabo.central.enterprises.audit.vo.param.TblCeaProjectQueryVoParam;
import com.huabo.central.enterprises.audit.vo.result.TblCeaProjectVoResult;

public interface TblCeaProjectQualityService {

	/**
	 * 项目评优-质量 列表查询
	 * @param param
	 * @return
	 */
	PageInfo<TblCeaProjectQuality> getList(TblCeaProjectQualityQueryParam param);

	/**
	 * 项目评优-质量 新增/更新
	 * @param param
	 * @return
	 */
	TblCeaProjectQuality saveOrUpdate(TblCeaProjectQuality param);

	/**
	 * 项目评优-质量 删除
	 * @param id
	 */
	void delete(Long id);

	/**
	 * 项目评优-质量 详情查询
	 * @param id
	 * @return
	 */
	TblCeaProjectQuality findById(Long id);

	/**
	 * 项目管理-实施方案 列表查询
	 * @param param
	 * @return
	 */
	PageInfo<TblCeaProjectVoResult> getTblCeaProjectList(TblCeaProjectQueryVoParam param);

	/**
	 * 项目管理-实施方案 列表查询
	 * @return
	 */
	PageInfo<TblCeaProjectVoResult> getTblCeaProjectVoList(TblCeaProjectQueryVoParam param);

	/**
	 * 获取 项目管理-实施方案 名称
	 * @param projectId
	 * @return
	 */
	String getTblCeaProjectName(Long projectId);
}
