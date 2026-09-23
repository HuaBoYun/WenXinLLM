package com.huabo.central.enterprises.audit.oracle.service;

import com.github.pagehelper.PageInfo;
import com.huabo.central.enterprises.audit.oracle.entity.TblCeaProjectDeclareGroup;
import com.huabo.central.enterprises.audit.vo.param.TblCeaProjectDeclareGroupQueryParam;

public interface TblCeaProjectDeclareGroupService {

	/**
	 * 项目评优-申报-分组 列表查询
	 * @param param
	 * @return
	 */
	PageInfo<TblCeaProjectDeclareGroup> getList(TblCeaProjectDeclareGroupQueryParam param);

	/**
	 * 项目评优-申报-分组 新增/更新
	 * @param param
	 * @return
	 */
	TblCeaProjectDeclareGroup saveOrUpdate(TblCeaProjectDeclareGroup param);

	/**
	 * 项目评优-申报-分组 删除
	 * @param id
	 */
	void delete(Long id);

	/**
	 * 项目评优-申报-分组 详情查询
	 * @param id
	 * @return
	 */
	TblCeaProjectDeclareGroup findById(Long id);
}
