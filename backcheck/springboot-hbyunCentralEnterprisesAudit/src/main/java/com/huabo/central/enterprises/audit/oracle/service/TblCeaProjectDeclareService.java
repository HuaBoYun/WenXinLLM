package com.huabo.central.enterprises.audit.oracle.service;

import com.github.pagehelper.PageInfo;
import com.huabo.central.enterprises.audit.oracle.entity.TblCeaProjectDeclare;
import com.huabo.central.enterprises.audit.vo.param.TblCeaProjectDeclareQueryParam;
import com.huabo.central.enterprises.audit.vo.param.TblCeaProjectDeclareSortQueryParam;

import java.util.List;

public interface TblCeaProjectDeclareService {

	/**
	 * 项目评优-申报 列表查询
	 * @param param
	 * @return
	 */
	PageInfo<TblCeaProjectDeclare> getList(TblCeaProjectDeclareQueryParam param);

	/**
	 * 项目评优-申报 新增/更新
	 * @param param
	 * @return
	 */
	TblCeaProjectDeclare saveOrUpdate(TblCeaProjectDeclare param);

	/**
	 * 项目评优-申报 删除
	 * @param id
	 */
	void delete(Long id);

	/**
	 * 项目评优-申报 详情查询
	 * @param id
	 * @return
	 */
	TblCeaProjectDeclare findById(Long id);

	/**
	 * 项目评优排序 列表查询
	 * @param param
	 * @return
	 */
	PageInfo<TblCeaProjectDeclare> getTblCeaProjectDeclareSortList(TblCeaProjectDeclareSortQueryParam param);

	/**
	 * 更新分组为空
	 * @param groupId
	 */
	void updateNullGroupList(Long groupId);

	/**
	 * 根据分组ID查询数据
	 * @param groupId
	 * @return
	 */
	List<TblCeaProjectDeclare> getGroupId(Long groupId);

	/**
	 * 分组置空
	 * @param id
	 */
	void updateNull(Long id);
}
