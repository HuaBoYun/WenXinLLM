package com.huabo.system.service;

import com.github.pagehelper.PageInfo;
import com.huabo.system.entity.SystemAgentInfo;
import com.huabo.system.entity.SystemAgentIssued;
import com.huabo.system.vo.param.SystemAgentInfoQueryParam;
import com.huabo.system.vo.param.UpdateSystemAgentIssuedModuleParam;
import com.huabo.system.vo.param.UpdateSystemAgentIssuedPermissionParam;

public interface SystemAgentIssuedService {

	/**
	 * 智能体下发 列表
	 * @param param
	 * @return
	 */
	PageInfo<SystemAgentInfo> getList(SystemAgentInfoQueryParam param);

	/**
	 * 智能体下发 新增/更新
	 * @param param
	 * @return
	 */
	SystemAgentIssued saveOrUpdate(SystemAgentIssued param);

	/**
	 * 智能体下发 查询
	 * @param id
	 * @return
	 */
	SystemAgentIssued findById(Long id);

	/**
	 * 智能体下发-下发模块
	 * @param param
	 */
	void updateSystemAgentIssuedModule(UpdateSystemAgentIssuedModuleParam param);

	/**
	 * 智能体下发-下发权限(个人、角色、公司)
	 * @param param
	 */
	void updateSystemAgentIssuedPermission(UpdateSystemAgentIssuedPermissionParam param);
}
