package com.huabo.system.service.impl;

import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
import com.hbfk.util.redis.Random.RandomUtil;
import com.huabo.system.constant.YesNo;
import com.huabo.system.entity.SystemAgentDialogue;
import com.huabo.system.entity.SystemAgentInfo;
import com.huabo.system.exception.ServiceException;
import com.huabo.system.mapper.SystemAgentDialogueMapper;
import com.huabo.system.service.SystemAgentDialogueService;
import com.huabo.system.vo.param.SystemAgentDialogueQueryParam;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.weekend.Weekend;
import tk.mybatis.mapper.weekend.WeekendCriteria;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
public class SystemAgentDialogueServiceImpl implements SystemAgentDialogueService {

	@Resource
	private SystemAgentDialogueMapper systemAgentDialogueMapper;

	@Override
	public PageInfo<SystemAgentDialogue> getList(SystemAgentDialogueQueryParam param) {
		Weekend<SystemAgentDialogue> weekend = Weekend.of(SystemAgentDialogue.class);
		WeekendCriteria<SystemAgentDialogue, Object> weekendCriteria = weekend.weekendCriteria();
		if (Objects.nonNull(param.getId())) {
			weekendCriteria.andEqualTo(SystemAgentDialogue::getId, param.getId());
		}
		// 按创建人过滤（每个用户只能看到自己的历史记录）
		if (Objects.nonNull(param.getCreator())) {
			weekendCriteria.andEqualTo(SystemAgentDialogue::getCreator, param.getCreator());
		}
		// 按智能体类型过滤（contract/finance/risk/compliance/legal/audit）
		// 优先级：agentType 单值 > agentTypes 集合
		if (Objects.nonNull(param.getAgentType()) && !param.getAgentType().isEmpty()) {
			weekendCriteria.andEqualTo(SystemAgentDialogue::getAgentType, param.getAgentType());
		} else if (Objects.nonNull(param.getAgentTypes()) && !param.getAgentTypes().isEmpty()) {
			// 逗号分隔的多类型 IN 过滤：用于 AIMenu 默认状态下展示 6 大业务全集
			List<String> types = new ArrayList<>();
			for (String t : Arrays.asList(param.getAgentTypes().split(","))) {
				String trimmed = t == null ? "" : t.trim();
				if (!trimmed.isEmpty()) {
					types.add(trimmed);
				}
			}
			if (!types.isEmpty()) {
				weekendCriteria.andIn(SystemAgentDialogue::getAgentType, types);
			}
		}
		// 最近对话按时间倒序：
		//  1) UPDATEDTIME DESC：续写过的会话上浮（新数据有值）
		//  2) NULLS LAST：DDL 新增 UPDATEDTIME 列时老数据为 NULL，达梦默认 NULL 在前，需显式压到末尾
		//  3) CREATEDTIME DESC：UPDATEDTIME 为 NULL 时（老数据）退化为按创建时间倒序
		//  4) ID DESC：兜底保证顺序稳定（防止时间戳完全相同的并发场景）
		weekend.setOrderByClause("UPDATEDTIME DESC NULLS LAST, CREATEDTIME DESC, ID DESC");
		return PageMethod.startPage(param.getPageNumber(), param.getPageSize())
				.doSelectPageInfo(() -> systemAgentDialogueMapper.selectByExample(weekend));
	}

	@Override
	public SystemAgentDialogue saveOrUpdate(SystemAgentDialogue param) {
		Date now = new Date();
		if (param.getId() == null) {
			// 新增
			param.setId(RandomUtil.uuLongId());
			param.setCreatedTime(now);
			param.setUpdatedTime(now);
			systemAgentDialogueMapper.insertSelective(param);
		} else {
			// 更新：注意保留 creator/workUnit/belongGroup（不要置空，否则用户隔离失效）
			if (idById(param.getId())) {
				throw new ServiceException(400, 50001);
			}
			param.setCreatedTime(null);
			// 刷新更新时间，让最近对话上浮
			param.setUpdatedTime(now);
			systemAgentDialogueMapper.updateByPrimaryKeySelective(param);
		}
		return findById(param.getId());
	}

	@Override
	public SystemAgentDialogue findById(Long id) {
		SystemAgentDialogue model = systemAgentDialogueMapper.selectByPrimaryKey(id);
		if (model == null) {
			throw new ServiceException(400, 50001);
		}
		return model;
	}

	@Override
	public void delete(Long id) {
		systemAgentDialogueMapper.deleteByPrimaryKey(id);
	}

	/**
	 * 根据id查询 是存在
	 * @param id
	 * @return
	 */
	private Boolean idById(Long id) {
		int count = systemAgentDialogueMapper.selectCount(SystemAgentDialogue.ofId(id));
		if (count == 0) {
			return true;
		}
		return false;
	}
}
