package com.huabo.system.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
import com.hbfk.util.redis.Random.RandomUtil;
import com.huabo.system.constant.YesNo;
import com.huabo.system.entity.SystemAgentInfo;
import com.huabo.system.entity.SystemAgentIssued;
import com.huabo.system.exception.ServiceException;
import com.huabo.system.mapper.SystemAgentInfoMapper;
import com.huabo.system.mapper.SystemAgentIssuedMapper;
import com.huabo.system.service.SystemAgentIssuedService;
import com.huabo.system.vo.param.SystemAgentInfoQueryParam;
import com.huabo.system.vo.param.UpdateSystemAgentIssuedModuleParam;
import com.huabo.system.vo.param.UpdateSystemAgentIssuedPermissionParam;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.weekend.Weekend;
import tk.mybatis.mapper.weekend.WeekendCriteria;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Objects;

@Service
public class SystemAgentIssuedServiceImpl implements SystemAgentIssuedService {

	@Resource
	private SystemAgentIssuedMapper systemAgentIssuedMapper;
	@Resource
	private SystemAgentInfoMapper systemAgentInfoMapper;

	/**
	 * 智能体下发 列表
	 * @param param
	 * @return
	 */
	@Override
	public PageInfo<SystemAgentInfo> getList(SystemAgentInfoQueryParam param) {
		Weekend<SystemAgentInfo> weekend = Weekend.of(SystemAgentInfo.class);
		WeekendCriteria<SystemAgentInfo, Object> weekendCriteria = weekend.weekendCriteria();
		weekendCriteria.andEqualTo(SystemAgentInfo::getFlagIssued, 1);
		if (StringUtils.isNotBlank(param.getAgentName())) {
			weekendCriteria.andLike(SystemAgentInfo::getAgentName, "%" + param.getAgentName() + "%");
		}
		if (StringUtils.isNotBlank(param.getAgentType())) {
			weekendCriteria.andLike(SystemAgentInfo::getAgentType, "%" + param.getAgentType() + "%");
		}
		return PageMethod.startPage(param.getPageNumber(), param.getPageSize())
				.doSelectPageInfo(() -> systemAgentInfoMapper.selectByExample(weekend));
	}

	/**
	 * 智能体下发 新增/更新
	 * @param param
	 * @return
	 */
	@Override
	public SystemAgentIssued saveOrUpdate(SystemAgentIssued param) {
		Date now = new Date();
		if (param.getId() == null) {
			param.setId(RandomUtil.uuLongId());
			param.setState(YesNo.NO);
			param.setCreatedTime(now);
			systemAgentIssuedMapper.insertSelective(param);
		} else {
			if (idById(param.getId())) {
				throw new ServiceException(400, 50001);
			}
			param.setCreatedTime(null);
			param.setCreator(null);
			param.setWorkUnit(null);
			param.setBelongGroup(null);
			systemAgentIssuedMapper.updateByPrimaryKeySelective(param);
		}
		return findById(param.getId());
	}

	@Override
	public SystemAgentIssued findById(Long id) {
		SystemAgentIssued model = systemAgentIssuedMapper.selectByPrimaryKey(id);
		if (model == null) {
			throw new ServiceException(400, 50001);
		}
		return model;
	}

	/**
	 * 智能体下发-下发模块
	 * @param param
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void updateSystemAgentIssuedModule(UpdateSystemAgentIssuedModuleParam param) {
		if (CollectionUtil.isEmpty(param.getIds())) {
			return;
		}
		if (Objects.equals(param.getFlagSend(), 1)) {
			Weekend<SystemAgentInfo> weekend = Weekend.of(SystemAgentInfo.class);
			WeekendCriteria<SystemAgentInfo, Object> weekendCriteria = weekend.weekendCriteria();
			weekendCriteria.andIn(SystemAgentInfo::getId, param.getIds());
			SystemAgentInfo update = new SystemAgentInfo();
			update.setModuleRoute(param.getModuleRoute());
			systemAgentInfoMapper.updateByExampleSelective(update, weekend);
		} else {
			systemAgentInfoMapper.updateNull(param.getIds());
		}
	}

	/**
	 * 智能体下发-下发权限(个人、角色、公司)
	 * @param param
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void updateSystemAgentIssuedPermission(UpdateSystemAgentIssuedPermissionParam param) {
		if (CollectionUtil.isEmpty(param.getIds())) {
			return;
		}
		Weekend<SystemAgentIssued> weekend = Weekend.of(SystemAgentIssued.class);
		WeekendCriteria<SystemAgentIssued, Object> weekendCriteria = weekend.weekendCriteria();
		weekendCriteria.andIn(SystemAgentIssued::getId, param.getIds());
		weekendCriteria.andEqualTo(SystemAgentIssued::getAuthorityType, param.getAuthorityType());
		systemAgentIssuedMapper.deleteByExample(weekend);
		if (Objects.equals(param.getFlagSend(),1)){
			param.getIds().forEach(item -> {
				param.getAuthorityIds().forEach(it -> {
					SystemAgentIssued save = new SystemAgentIssued();
					save.setId(RandomUtil.uuLongId());
					save.setAgentId(item);
					save.setAuthorityType(param.getAuthorityType());
					save.setAuthorityId(it);
					systemAgentIssuedMapper.insertSelective(save);
				});
			});
		}
	}

	/**
	 * 根据id查询 是存在
	 * @param id
	 * @return
	 */
	private Boolean idById(Long id) {
		int count = systemAgentIssuedMapper.selectCount(SystemAgentIssued.ofId(id));
		if (count == 0) {
			return true;
		}
		return false;
	}
}
