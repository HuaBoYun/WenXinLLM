package com.huabo.system.service;

import com.github.pagehelper.PageInfo;
import com.huabo.system.entity.SystemAgentDialogue;
import com.huabo.system.vo.param.SystemAgentDialogueQueryParam;

public interface SystemAgentDialogueService {


	/**
	 * 最近对话列表 查询
	 * @param param
	 * @return
	 */
	PageInfo<SystemAgentDialogue> getList(SystemAgentDialogueQueryParam param);

	/**
	 * 最近对话 新增/更新
	 * @param param
	 * @return
	 */
	SystemAgentDialogue saveOrUpdate(SystemAgentDialogue param);

	/**
	 * 最近对话 查询
	 * @param id
	 * @return
	 */
	SystemAgentDialogue findById(Long id);

	/**
	 * 最近对话 刪除
	 * @param id
	 */
	void delete(Long id);
}
