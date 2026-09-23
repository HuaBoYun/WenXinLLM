package com.huabo.central.enterprises.audit.oracle.service.impl;

import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
import com.hbfk.util.redis.Random.RandomUtil;
import com.huabo.central.enterprises.audit.constant.YesNo;
import com.huabo.central.enterprises.audit.exception.ServiceException;
import com.huabo.central.enterprises.audit.oracle.entity.TblCeaProjectQuality;
import com.huabo.central.enterprises.audit.oracle.mapper.TblCeaProjectQualityMapper;
import com.huabo.central.enterprises.audit.oracle.service.TblCeaProjectQualityService;
import com.huabo.central.enterprises.audit.oracle.service.TblStaffOracleService;
import com.huabo.central.enterprises.audit.util.PageableParam;
import com.huabo.central.enterprises.audit.vo.param.TblCeaProjectQualityQueryParam;
import com.huabo.central.enterprises.audit.vo.param.TblCeaProjectQueryVoParam;
import com.huabo.central.enterprises.audit.vo.result.TblCeaProjectVoResult;
import com.huabo.central.enterprises.audit.vo.result.UserInfo;
import com.vip.vjtools.vjkit.collection.CollectionUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
public class TblCeaProjectQualityServiceImpl implements TblCeaProjectQualityService {

	@Resource
	private TblCeaProjectQualityMapper tblCeaProjectQualityMapper;
	@Resource
	private TblStaffOracleService tblStaffOracleService;

	@Override
	public PageInfo<TblCeaProjectQuality> getList(TblCeaProjectQualityQueryParam param) {
		return PageMethod.startPage(param.getPageNumber(), param.getPageSize()).doSelectPageInfo(() -> tblCeaProjectQualityMapper.getList(param));
	}

	@Override
	public TblCeaProjectQuality saveOrUpdate(TblCeaProjectQuality param) {
		Date now = new Date();
		if (param.getId() == null) {
			param.setId(RandomUtil.uuLongId());
			param.setState(YesNo.NO);
			param.setCreatedTime(now);
			param.setUpdatedTime(now);
			tblCeaProjectQualityMapper.insertSelective(param);
		} else {
			if (idById(param.getId())) {
				throw new ServiceException(400, 50001);
			}
			param.setCreator(null);
			param.setCreatedTime(null);
			param.setUpdatedTime(now);
			tblCeaProjectQualityMapper.updateByPrimaryKeySelective(param);
		}
		return findById(param.getId());
	}

	@Override
	public void delete(Long id) {
		tblCeaProjectQualityMapper.deleteByPrimaryKey(id);
	}

	@Override
	public TblCeaProjectQuality findById(Long id) {
		TblCeaProjectQuality model = tblCeaProjectQualityMapper.selectByPrimaryKey(id);
		if (model == null) {
			throw new ServiceException(400, 50001);
		}
		return model;
	}

	/**
	 * 项目管理-实施方案 列表查询
	 * @param param
	 * @return
	 */
	@Override
	public PageInfo<TblCeaProjectVoResult> getTblCeaProjectList(TblCeaProjectQueryVoParam param) {
		List<Long> ids = tblCeaProjectQualityMapper.getIds(param.getAssessId());
		if (CollectionUtil.isEmpty(ids)) {
			PageInfo<TblCeaProjectVoResult> pageInfo = new PageInfo<>(Collections.emptyList());
			pageInfo.setPageNum(param.getPageNumber());
			pageInfo.setPageSize(param.getPageSize());
			return pageInfo;
		}
		param.setIds(ids);
		PageInfo<TblCeaProjectVoResult> pageInfo = PageMethod.startPage(param.getPageNumber(), param.getPageSize())
				.doSelectPageInfo(() -> tblCeaProjectQualityMapper.getTblCeaProjectList(param));
		if (CollectionUtil.isEmpty(pageInfo.getList())) {
			return pageInfo;
		}
		pageInfo.getList().forEach(this::doTblCeaProjectTeamList);
		return pageInfo;
	}

	/**
	 * 项目管理-实施方案 列表查询
	 * @return
	 */
	@Override
	public PageInfo<TblCeaProjectVoResult> getTblCeaProjectVoList(TblCeaProjectQueryVoParam param) {
		PageInfo<TblCeaProjectVoResult> pageInfo = PageMethod.startPage(param.getPageNumber(), param.getPageSize())
				.doSelectPageInfo(() -> tblCeaProjectQualityMapper.getTblCeaProjectVoList(param));
		if (CollectionUtil.isEmpty(pageInfo.getList())) {
			return pageInfo;
		}
		pageInfo.getList().forEach(this::doTblCeaProjectTeamList);
		return pageInfo;
	}

	private void doTblCeaProjectTeamList(TblCeaProjectVoResult item) {
		List<TblCeaProjectVoResult> list = tblCeaProjectQualityMapper.getTblCeaProjectTeamList(item.getId());
		if (CollectionUtil.isEmpty(list)) {
			return;
		}
		TblCeaProjectVoResult tblCeaProjectVoResult = list.get(0);
		item.setFzzstaffid(tblCeaProjectVoResult.getFzzstaffid());
		item.setTeamLeaderId(tblCeaProjectVoResult.getTeamLeaderId());
		item.setTeamMembersIds(tblCeaProjectVoResult.getTeamMembersIds());
		if (Objects.nonNull(tblCeaProjectVoResult.getTeamLeaderId())) {
			item.setTeamLeaderName(tblStaffOracleService.getCreatorUserInfo(tblCeaProjectVoResult.getTeamLeaderId()));
		}
		if (Objects.nonNull(tblCeaProjectVoResult.getFzzstaffid())) {
			item.setFzzstaffName(tblStaffOracleService.getCreatorUserInfo(tblCeaProjectVoResult.getFzzstaffid()));
		}
		if (StringUtils.isNotBlank(tblCeaProjectVoResult.getTeamMembersIds())) {
			List<UserInfo> creatorUserInfos = tblStaffOracleService.getCreatorUserInfos(tblCeaProjectVoResult.getTeamMembersIds());
			if (CollectionUtil.isEmpty(creatorUserInfos)) {
				return;
			}
			item.setTeamMembersList(creatorUserInfos);
		}
	}

	/**
	 * 获取 项目管理-实施方案 名称
	 * @param projectId
	 * @return
	 */
	@Override
	public String getTblCeaProjectName(Long projectId) {
		return tblCeaProjectQualityMapper.findTblCeaProjectName(projectId);
	}

	/**
	 * 根据id查询 是存在
	 * @param id
	 * @return
	 */
	private Boolean idById(Long id) {
		int count = tblCeaProjectQualityMapper.selectCount(TblCeaProjectQuality.ofId(id));
		if (count == 0) {
			return true;
		}
		return false;
	}
}
