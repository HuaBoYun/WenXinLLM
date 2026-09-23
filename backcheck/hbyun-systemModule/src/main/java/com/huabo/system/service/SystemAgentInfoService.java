package com.huabo.system.service;

import com.github.pagehelper.PageInfo;
import com.huabo.system.entity.SystemAgentInfo;
import com.huabo.system.vo.param.SystemAgentInfoQueryParam;
import com.huabo.system.vo.param.SystemAgentModuleParam;

public interface SystemAgentInfoService {

	/**
	 * 智能体主表列表 查询
	 * @param param
	 * @return
	 */
	PageInfo<SystemAgentInfo> getList(SystemAgentInfoQueryParam param);

	/**
	 * 智能体主表 新增/更新
	 * @param param
	 * @return
	 */
	SystemAgentInfo saveOrUpdate(SystemAgentInfo param);

	/**
	 * 智能体主表详情 查询
	 * @param id
	 * @return
	 */
	SystemAgentInfo findById(Long id);

	/**
	 * 智能体主表 刪除
	 * @param id
	 */
	void delete(Long id);

	/**
	 * 不同模块下的智能体-列表
	 * @param param
	 * @return
	 */
	PageInfo<SystemAgentInfo> getSystemAgentModuleList(SystemAgentModuleParam param);
}
