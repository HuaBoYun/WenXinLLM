package com.huabo.system.service.business.impl;

import com.github.pagehelper.PageInfo;
import com.huabo.system.entity.SystemAgentDialogue;
import com.huabo.system.entity.SystemAgentInfo;
import com.huabo.system.exception.ServiceException;
import com.huabo.system.oracle.service.TblStaffOracleService;
import com.huabo.system.service.SystemAgentDialogueService;
import com.huabo.system.service.SystemAgentInfoService;
import com.huabo.system.service.SystemAgentIssuedService;
import com.huabo.system.service.business.SystemAgentService;
import com.huabo.system.utils.MyJsonBean;
import com.huabo.system.utils.MyPageResult;
import com.huabo.system.utils.MyResponseFormat;
import com.huabo.system.vo.param.*;
import com.vip.vjtools.vjkit.collection.CollectionUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class SystemAgentServiceImpl implements SystemAgentService {

	@Resource
	private SystemAgentInfoService systemAgentInfoService;
	@Resource
	private SystemAgentIssuedService systemAgentIssuedService;
	@Resource
	private TblStaffOracleService tblStaffOracleService;
	@Resource
	private SystemAgentDialogueService systemAgentDialogueService;

	/**
	 * 智能体信息-列表
	 * @param param
	 * @return
	 */
	@Override
	public MyJsonBean<SystemAgentInfo> getSystemAgentList(SystemAgentInfoQueryParam param) {
		PageInfo<SystemAgentInfo> pageInfo = systemAgentInfoService.getList(param);
		if (CollectionUtil.isEmpty(pageInfo.getList())) {
			return MyResponseFormat.retParam(200, 200, MyPageResult.buildNoData());
		}
		List<Long> creators = pageInfo.getList().stream().filter(x -> Objects.nonNull(x.getCreator())).distinct().map(SystemAgentInfo::getCreator)
				.distinct().collect(Collectors.toList());
		Map<Long, String> creatorUserInfoMap = tblStaffOracleService.getCreatorUserInfoMap(StringUtils.join(creators, ","));
		pageInfo.getList().forEach(item -> {
			item.setCreatorName(creatorUserInfoMap.getOrDefault(item.getCreator(), ""));
		});
		MyPageResult<SystemAgentInfo> build = new MyPageResult<SystemAgentInfo>().build(pageInfo);
		return MyResponseFormat.retParam(200, 200, build);
	}

	/**
	 * 智能体信息-新增或修改
	 * @param param
	 * @return
	 */
	@Override
	public MyJsonBean<SystemAgentInfo> saveOrUpdateSystemAgent(SystemAgentInfo param) {
		SystemAgentInfo model = systemAgentInfoService.saveOrUpdate(param);
		return MyResponseFormat.retParam(200, 200, model);
	}

	/**
	 * 智能体信息-信息 {id}为主键id
	 * @param id
	 * @return
	 */
	@Override
	public MyJsonBean<SystemAgentInfo> getSystemAgent(Long id) {
		SystemAgentInfo model = systemAgentInfoService.findById(id);
		if (Objects.nonNull(model.getCreator())) {
			model.setCreatorName(tblStaffOracleService.getCreatorUserInfo(model.getCreator()));
		}
		return MyResponseFormat.retParam(200, 200, model);
	}

	/**
	 * 智能体信息-信息删除
	 * @param id
	 * @return
	 */
	@Override
	public MyJsonBean<Void> deleteSystemAgent(Long id) {
		systemAgentInfoService.delete(id);
		return MyResponseFormat.retParam(200, 200, null);
	}

	/**
	 * 更新智能体HTTP
	 * @param param
	 * @return
	 */
	@Override
	public MyJsonBean<Void> updateSystemAgentHttp(UpdateSystemAgentHttpParam param) {
		SystemAgentInfo model = systemAgentInfoService.findById(param.getId());
		if (Objects.isNull(model)) {
			throw new ServiceException("智能体不存在");
		}
		SystemAgentInfo update = new SystemAgentInfo();
		update.setId(param.getId());
		update.setJumpAddress(param.getJumpAddress());
		systemAgentInfoService.saveOrUpdate(update);
		return MyResponseFormat.retParam(200, 200, null);
	}

	/**
	 * 智能体下发-列表
	 * @param param
	 * @return
	 */
	@Override
	public MyJsonBean<SystemAgentInfo> getSystemAgentIssuedList(SystemAgentInfoQueryParam param) {
		PageInfo<SystemAgentInfo> pageInfo = systemAgentIssuedService.getList(param);
		if (CollectionUtil.isEmpty(pageInfo.getList())) {
			return MyResponseFormat.retParam(200, 200, MyPageResult.buildNoData());
		}
		List<Long> creators = pageInfo.getList().stream().filter(x -> Objects.nonNull(x.getCreator())).distinct().map(SystemAgentInfo::getCreator)
				.distinct().collect(Collectors.toList());
		Map<Long, String> creatorUserInfoMap = tblStaffOracleService.getCreatorUserInfoMap(StringUtils.join(creators, ","));
		pageInfo.getList().forEach(item -> {
			item.setCreatorName(creatorUserInfoMap.getOrDefault(item.getCreator(), ""));
		});
		MyPageResult<SystemAgentInfo> build = new MyPageResult<SystemAgentInfo>().build(pageInfo);
		return MyResponseFormat.retParam(200, 200, build);
	}

	/**
	 * 智能体下发-下发模块
	 * @param param
	 * @return
	 */
	@Override
	public MyJsonBean<Void> updateSystemAgentIssuedModule(UpdateSystemAgentIssuedModuleParam param) {
		systemAgentIssuedService.updateSystemAgentIssuedModule(param);
		return MyResponseFormat.retParam(200, 200, null);
	}

	/**
	 * 智能体下发-下发权限(个人、角色、公司)
	 * @param param
	 * @return
	 */
	@Override
	public MyJsonBean<Void> updateSystemAgentIssuedPermission(UpdateSystemAgentIssuedPermissionParam param) {
		systemAgentIssuedService.updateSystemAgentIssuedPermission(param);
		return MyResponseFormat.retParam(200, 200, null);
	}

	/**
	 * 不同模块下的智能体-列表
	 * @param param
	 * @return
	 */
	@Override
	public MyJsonBean<SystemAgentInfo> getSystemAgentModuleList(SystemAgentModuleParam param) {
		PageInfo<SystemAgentInfo> pageInfo = systemAgentInfoService.getSystemAgentModuleList(param);
		if (CollectionUtil.isEmpty(pageInfo.getList())) {
			return MyResponseFormat.retParam(200, 200, MyPageResult.buildNoData());
		}
		List<Long> creators = pageInfo.getList().stream().filter(x -> Objects.nonNull(x.getCreator())).distinct().map(SystemAgentInfo::getCreator)
				.distinct().collect(Collectors.toList());
		Map<Long, String> creatorUserInfoMap = tblStaffOracleService.getCreatorUserInfoMap(StringUtils.join(creators, ","));
		pageInfo.getList().forEach(item -> {
			item.setCreatorName(creatorUserInfoMap.getOrDefault(item.getCreator(), ""));
		});
		MyPageResult<SystemAgentInfo> build = new MyPageResult<SystemAgentInfo>().build(pageInfo);
		return MyResponseFormat.retParam(200, 200, build);
	}

	/**
	 * 智能体-最近对话-列表
	 * @param param
	 * @return
	 */
	@Override
	public MyJsonBean<SystemAgentDialogue> getSystemAgentDialogueList(SystemAgentDialogueQueryParam param) {
		PageInfo<SystemAgentDialogue> pageInfo = systemAgentDialogueService.getList(param);
		if (CollectionUtil.isEmpty(pageInfo.getList())) {
			return MyResponseFormat.retParam(200, 200, MyPageResult.buildNoData());
		}
		List<Long> creators = pageInfo.getList().stream().filter(x -> Objects.nonNull(x.getCreator())).distinct().map(SystemAgentDialogue::getCreator)
				.distinct().collect(Collectors.toList());
		Map<Long, String> creatorUserInfoMap = tblStaffOracleService.getCreatorUserInfoMap(StringUtils.join(creators, ","));
		pageInfo.getList().forEach(item -> {
			item.setCreatorName(creatorUserInfoMap.getOrDefault(item.getCreator(), ""));
		});
		MyPageResult<SystemAgentDialogue> build = new MyPageResult<SystemAgentDialogue>().build(pageInfo);
		return MyResponseFormat.retParam(200, 200, build);
	}

	/**
	 * 智能体-最近对话-新增或修改
	 * @param param
	 * @return
	 */
	@Override
	public MyJsonBean<SystemAgentDialogue> saveOrUpdateSystemAgentDialogue(SystemAgentDialogue param) {
		SystemAgentDialogue model = systemAgentDialogueService.saveOrUpdate(param);
		return MyResponseFormat.retParam(200, 200, model);
	}

	/**
	 * 智能体-最近对话 {id}为主键id
	 * @param id
	 * @return
	 */
	@Override
	public MyJsonBean<SystemAgentDialogue> getSystemAgentDialogue(Long id) {
		SystemAgentDialogue model = systemAgentDialogueService.findById(id);
		if (Objects.nonNull(model.getCreator())) {
			model.setCreatorName(tblStaffOracleService.getCreatorUserInfo(model.getCreator()));
		}
		return MyResponseFormat.retParam(200, 200, model);
	}

	/**
	 * 智能体-最近对话 删除
	 * @param id
	 * @return
	 */
	@Override
	public MyJsonBean<Void> deleteSystemAgentDialogue(Long id) {
		systemAgentDialogueService.delete(id);
		return MyResponseFormat.retParam(200, 200, null);
	}
}
