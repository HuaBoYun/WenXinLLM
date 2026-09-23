package com.huabo.system.service.business;

import com.huabo.system.entity.SystemAgentDialogue;
import com.huabo.system.entity.SystemAgentInfo;
import com.huabo.system.utils.MyJsonBean;
import com.huabo.system.vo.param.*;

public interface SystemAgentService {

	/**
	 * 智能体信息-列表
	 * @param param
	 * @return
	 */
	MyJsonBean<SystemAgentInfo> getSystemAgentList(SystemAgentInfoQueryParam param);

	/**
	 * 智能体信息-新增或修改
	 * @param param
	 * @return
	 */
	MyJsonBean<SystemAgentInfo> saveOrUpdateSystemAgent(SystemAgentInfo param);

	/**
	 * 智能体信息-信息 {id}为主键id
	 * @param id
	 * @return
	 */
	MyJsonBean<SystemAgentInfo> getSystemAgent(Long id);

	/**
	 * 智能体信息-信息删除
	 * @param id
	 * @return
	 */
	MyJsonBean<Void> deleteSystemAgent(Long id);

	/**
	 * 更新智能体HTTP
	 * @param param
	 * @return
	 */
	MyJsonBean<Void> updateSystemAgentHttp(UpdateSystemAgentHttpParam param);

	/**
	 * 智能体下发-列表
	 * @param param
	 * @return
	 */
	MyJsonBean<SystemAgentInfo> getSystemAgentIssuedList(SystemAgentInfoQueryParam param);

	/**
	 * 智能体下发-下发模块
	 * @param param
	 * @return
	 */
	MyJsonBean<Void> updateSystemAgentIssuedModule(UpdateSystemAgentIssuedModuleParam param);

	/**
	 * 智能体下发-下发权限(个人、角色、公司)
	 * @param param
	 * @return
	 */
	MyJsonBean<Void> updateSystemAgentIssuedPermission(UpdateSystemAgentIssuedPermissionParam param);

	/**
	 * 不同模块下的智能体-列表
	 * @param param
	 * @return
	 */
	MyJsonBean<SystemAgentInfo> getSystemAgentModuleList(SystemAgentModuleParam param);

	/**
	 * 智能体-最近对话-列表
	 * @param param
	 * @return
	 */
	MyJsonBean<SystemAgentDialogue> getSystemAgentDialogueList(SystemAgentDialogueQueryParam param);

	/**
	 * 智能体-最近对话-新增或修改
	 * @param param
	 * @return
	 */
	MyJsonBean<SystemAgentDialogue> saveOrUpdateSystemAgentDialogue(SystemAgentDialogue param);

	/**
	 * 智能体-最近对话 {id}为主键id
	 * @param id
	 * @return
	 */
	MyJsonBean<SystemAgentDialogue> getSystemAgentDialogue(Long id);

	/**
	 * 智能体-最近对话 删除
	 * @param id
	 * @return
	 */
	MyJsonBean<Void> deleteSystemAgentDialogue(Long id);
}
