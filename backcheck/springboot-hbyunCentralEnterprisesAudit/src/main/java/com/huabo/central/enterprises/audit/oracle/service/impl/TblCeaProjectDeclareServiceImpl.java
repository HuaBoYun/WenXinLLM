package com.huabo.central.enterprises.audit.oracle.service.impl;

import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
import com.hbfk.util.redis.Random.RandomUtil;
import com.huabo.central.enterprises.audit.constant.YesNo;
import com.huabo.central.enterprises.audit.exception.ServiceException;
import com.huabo.central.enterprises.audit.oracle.entity.ImplementPlanTeam;
import com.huabo.central.enterprises.audit.oracle.entity.TblCeaProjectDeclare;
import com.huabo.central.enterprises.audit.oracle.mapper.ImplementPlanTeamMapper;
import com.huabo.central.enterprises.audit.oracle.mapper.TblCeaProjectDeclareMapper;
import com.huabo.central.enterprises.audit.oracle.service.TblCeaProjectAppraisingService;
import com.huabo.central.enterprises.audit.oracle.service.TblCeaProjectDeclareService;
import com.huabo.central.enterprises.audit.oracle.service.TblStaffOracleService;
import com.huabo.central.enterprises.audit.vo.param.TblCeaProjectDeclareQueryParam;
import com.huabo.central.enterprises.audit.vo.param.TblCeaProjectDeclareSortQueryParam;
import com.huabo.central.enterprises.audit.vo.result.UserInfo;
import com.vip.vjtools.vjkit.collection.CollectionUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.weekend.Weekend;
import tk.mybatis.mapper.weekend.WeekendCriteria;

import javax.annotation.Resource;
import java.util.*;

@Service
public class TblCeaProjectDeclareServiceImpl implements TblCeaProjectDeclareService {

	@Resource
	private TblCeaProjectDeclareMapper tblCeaProjectDeclareMapper;
	@Resource
	private ImplementPlanTeamMapper implementPlanTeamMapper;
	@Resource
	private TblCeaProjectAppraisingService tblCeaProjectAppraisingService;
	@Resource
	private TblStaffOracleService tblStaffOracleService;

	@Override
	public PageInfo<TblCeaProjectDeclare> getList(TblCeaProjectDeclareQueryParam param) {
		if (Objects.equals(param.getNoAppraising(), 0)) {
			//已被选择了的申报IDS
			List<Long> ids = tblCeaProjectAppraisingService.getNoQuality();
			param.setNoIds(ids);
		}
		PageInfo<TblCeaProjectDeclare> pageInfo = PageMethod.startPage(param.getPageNumber(), param.getPageSize())
				.doSelectPageInfo(() -> tblCeaProjectDeclareMapper.getList(param));
		if (CollectionUtil.isEmpty(pageInfo.getList())) {
			return pageInfo;
		}
		pageInfo.getList().forEach(item -> {
			if (Objects.nonNull(item.getImplementationProjectOrderId())) {
				item.setImplementationProjectOrderName(tblStaffOracleService.getCreatorUserInfo(item.getImplementationProjectOrderId()));
			}
			Weekend<ImplementPlanTeam> weekend = Weekend.of(ImplementPlanTeam.class);
			WeekendCriteria<ImplementPlanTeam, Object> weekendCriteria = weekend.weekendCriteria();
			weekendCriteria.andEqualTo(ImplementPlanTeam::getImPlanId, item.getImplementationPlanId());
			List<ImplementPlanTeam> implementPlanTeams = implementPlanTeamMapper.selectByExample(weekend);
			if (CollectionUtil.isEmpty(implementPlanTeams)) {
				return;
			}
			Optional<ImplementPlanTeam> optional = implementPlanTeams.stream().min(Comparator.comparing(ImplementPlanTeam::getId));
			if (optional.isPresent()) {
				ImplementPlanTeam implementPlanTeam = optional.get();
				if (Objects.nonNull(implementPlanTeam.getTeamLeaderId())) {
					item.setImplementationPlanTeamLeader(tblStaffOracleService.getCreatorUserInfo(implementPlanTeam.getTeamLeaderId()));
				}
				if (Objects.nonNull(implementPlanTeam.getFzzstaffid())) {
					item.setImplementationPlanTeamfzzname(tblStaffOracleService.getCreatorUserInfo(implementPlanTeam.getFzzstaffid()));
				}
				if (StringUtils.isNotBlank(implementPlanTeam.getTeamMembersIds())) {
					List<UserInfo> creatorUserInfos = tblStaffOracleService.getCreatorUserInfos(implementPlanTeam.getTeamMembersIds());
					item.setImplementationPlanReviewers(creatorUserInfos);
				}
			}
		});
		return pageInfo;
	}

	@Override
	public TblCeaProjectDeclare saveOrUpdate(TblCeaProjectDeclare param) {
		Date now = new Date();
		if (param.getId() == null) {
			param.setId(RandomUtil.uuLongId());
			param.setState(YesNo.NO);
			param.setCreatedTime(now);
			param.setUpdatedTime(now);
			tblCeaProjectDeclareMapper.insertSelective(param);
		} else {
			if (idById(param.getId())) {
				throw new ServiceException(400, 50001);
			}
			param.setCreator(null);
			param.setCreatedTime(null);
			param.setUpdatedTime(now);
			tblCeaProjectDeclareMapper.updateByPrimaryKeySelective(param);
		}
		return findById(param.getId());
	}

	@Override
	public void delete(Long id) {
		tblCeaProjectDeclareMapper.deleteByPrimaryKey(id);
	}

	@Override
	public TblCeaProjectDeclare findById(Long id) {
		TblCeaProjectDeclare model = tblCeaProjectDeclareMapper.findById(id);
		if (model == null) {
			throw new ServiceException(400, 50001);
		}
		Weekend<ImplementPlanTeam> weekend = Weekend.of(ImplementPlanTeam.class);
		WeekendCriteria<ImplementPlanTeam, Object> weekendCriteria = weekend.weekendCriteria();
		weekendCriteria.andEqualTo(ImplementPlanTeam::getImPlanId, model.getImplementationPlanId());
		List<ImplementPlanTeam> implementPlanTeams = implementPlanTeamMapper.selectByExample(weekend);
		if (CollectionUtil.isEmpty(implementPlanTeams)) {
			return model;
		}
		Optional<ImplementPlanTeam> optional = implementPlanTeams.stream().min(Comparator.comparing(ImplementPlanTeam::getId));
		if (optional.isPresent()) {
			ImplementPlanTeam implementPlanTeam = optional.get();
			if (Objects.nonNull(implementPlanTeam.getTeamLeaderId())) {
				model.setImplementationPlanTeamLeader(tblStaffOracleService.getCreatorUserInfo(implementPlanTeam.getTeamLeaderId()));
			}
			if (Objects.nonNull(implementPlanTeam.getFzzstaffid())) {
				model.setImplementationPlanTeamfzzname(tblStaffOracleService.getCreatorUserInfo(implementPlanTeam.getFzzstaffid()));
			}
			if (StringUtils.isNotBlank(implementPlanTeam.getTeamMembersIds())) {
				List<UserInfo> creatorUserInfos = tblStaffOracleService.getCreatorUserInfos(implementPlanTeam.getTeamMembersIds());
				model.setImplementationPlanReviewers(creatorUserInfos);
			}
			if (StringUtils.isNotBlank(implementPlanTeam.getTeamMembersIds())) {
				List<UserInfo> creatorUserInfos = tblStaffOracleService.getCreatorUserInfos(implementPlanTeam.getTeamMembersIds());
				model.setImplementationPlanReviewers(creatorUserInfos);
			}
		}
		return model;
	}

	/**
	 * 项目评优排序 列表查询
	 * @param param
	 * @return
	 */
	@Override
	public PageInfo<TblCeaProjectDeclare> getTblCeaProjectDeclareSortList(TblCeaProjectDeclareSortQueryParam param) {
		return PageMethod.startPage(param.getPageNumber(), param.getPageSize())
				.doSelectPageInfo(() -> tblCeaProjectDeclareMapper.findTblCeaProjectDeclareSortList(param));
	}

	/**
	 * 更新分组为空
	 * @param groupId
	 */
	@Override
	public void updateNullGroupList(Long groupId) {
		tblCeaProjectDeclareMapper.updateNullGroupList(groupId);
	}

	/**
	 * 根据分组ID查询数据
	 * @param groupId
	 * @return
	 */
	@Override
	public List<TblCeaProjectDeclare> getGroupId(Long groupId) {
		List<TblCeaProjectDeclare> select = tblCeaProjectDeclareMapper.select(TblCeaProjectDeclare.ofGroupId(groupId));
		if (CollectionUtil.isEmpty(select)) {
			return Collections.emptyList();
		}
		return select;
	}

	/**
	 * 分组置空
	 * @param id
	 */
	@Override
	public void updateNull(Long id) {
		tblCeaProjectDeclareMapper.updateNull(id);
	}

	/**
	 * 根据id查询 是存在
	 * @param id
	 * @return
	 */
	private Boolean idById(Long id) {
		int count = tblCeaProjectDeclareMapper.selectCount(TblCeaProjectDeclare.ofId(id));
		if (count == 0) {
			return true;
		}
		return false;
	}
}
