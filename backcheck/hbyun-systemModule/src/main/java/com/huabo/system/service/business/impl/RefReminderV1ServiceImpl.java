package com.huabo.system.service.business.impl;

import com.github.pagehelper.PageInfo;
import com.hbfk.entity.TblStaffUtil;
import com.huabo.system.entity.TblSystemRefReminderAccess;
import com.huabo.system.entity.TblSystemRefReminderAccessV;
import com.huabo.system.entity.TblSystemRefReminderV;
import com.huabo.system.entity.TblSystemRefopmReminderV;
import com.huabo.system.exception.ServiceException;
import com.huabo.system.mapper.TblSystemRefopmReminderV1Mapper;
import com.huabo.system.oracle.service.TblStaffOracleService;
import com.huabo.system.oracle.service.TblSystemRefReminderAccessV1Service;
import com.huabo.system.oracle.service.TblSystemRefReminderV1Service;
import com.huabo.system.oracle.service.TblSystemRefopmReminderV1Service;
import com.huabo.system.service.business.RefReminderV1Service;
import com.huabo.system.utils.MyJsonBean;
import com.huabo.system.utils.MyPageResult;
import com.huabo.system.utils.MyResponseFormat;
import com.huabo.system.vo.param.TblSystemRefReminderAccessQueryParam;
import com.huabo.system.vo.param.TblSystemRefReminderQueryParam;
import com.vip.vjtools.vjkit.collection.CollectionUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class RefReminderV1ServiceImpl implements RefReminderV1Service {

	@Resource
	private TblSystemRefReminderV1Service tblSystemRefReminderV1Service;
	@Resource
	private TblSystemRefReminderAccessV1Service tblSystemRefReminderAccessV1Service;
	@Resource
	private TblSystemRefopmReminderV1Mapper tblSystemRefopmReminderV1Mapper;
	@Resource
	private TblStaffOracleService tblStaffOracleService;

	/**
	 * 催办信息 列表查询
	 * @param param
	 * @return
	 */
	@Override
	public MyJsonBean<TblSystemRefReminderV> getRefReminderList(TblSystemRefReminderQueryParam param) {
		PageInfo<TblSystemRefReminderV> pageInfo = tblSystemRefReminderV1Service.getList(param);
		if (CollectionUtil.isEmpty(pageInfo.getList())) {
			return MyResponseFormat.retParam(200, 200, MyPageResult.buildNoData());
		}
		List<Long> creatorIds = new ArrayList<>();
		List<Long> creators = pageInfo.getList().stream().filter(x -> Objects.nonNull(x.getCreator())).distinct()
				.map(TblSystemRefReminderV::getCreator).distinct().collect(Collectors.toList());
		if (CollectionUtil.isNotEmpty(creators)) {
			creatorIds.addAll(creators);
		}
		List<Long> reminderStaffIds = pageInfo.getList().stream().filter(x -> Objects.nonNull(x.getReminderStaffId())).distinct()
				.map(TblSystemRefReminderV::getReminderStaffId).distinct().collect(Collectors.toList());
		if (CollectionUtil.isNotEmpty(reminderStaffIds)) {
			creatorIds.addAll(reminderStaffIds);
		}
		Map<Long, String> creatorUserInfoMap = tblStaffOracleService.getCreatorUserInfoMap(StringUtils.join(creatorIds, ","));
		pageInfo.getList().forEach(item -> {
			item.setCreatorName(creatorUserInfoMap.getOrDefault(item.getCreator(), "自动催办"));
			item.setReminderStaffName(creatorUserInfoMap.getOrDefault(item.getReminderStaffId(), "自动催办"));
			if (StringUtils.isBlank(item.getTypeName())) {
				item.setTypeName("月度评估-一体化管控措施");
			}
		});
		MyPageResult<TblSystemRefReminderV> build = new MyPageResult<TblSystemRefReminderV>().build(pageInfo);
		return MyResponseFormat.retParam(200, 200, build);
	}

	/**
	 * 催办访问记录 列表查询
	 * @param param
	 * @return
	 */
	@Override
	public MyJsonBean<TblSystemRefReminderAccessV> getRefReminderAccessList(TblSystemRefReminderAccessQueryParam param) {
		PageInfo<TblSystemRefReminderAccessV> pageInfo = tblSystemRefReminderAccessV1Service.getList(param);
		if (CollectionUtil.isEmpty(pageInfo.getList())) {
			return MyResponseFormat.retParam(200, 200, MyPageResult.buildNoData());
		}
		List<Long> creatorIds = new ArrayList<>();
		List<Long> creators = pageInfo.getList().stream().filter(x -> Objects.nonNull(x.getCreator())).distinct()
				.map(TblSystemRefReminderAccessV::getCreator).distinct().collect(Collectors.toList());
		if (CollectionUtil.isNotEmpty(creators)) {
			creatorIds.addAll(creators);
		}
		List<Long> reminderStaffIds = pageInfo.getList().stream().filter(x -> Objects.nonNull(x.getReminderStaffId())).distinct()
				.map(TblSystemRefReminderAccessV::getReminderStaffId).distinct().collect(Collectors.toList());
		if (CollectionUtil.isNotEmpty(reminderStaffIds)) {
			creatorIds.addAll(reminderStaffIds);
		}
		Map<Long, String> creatorUserInfoMap = tblStaffOracleService.getCreatorUserInfoMap(StringUtils.join(creatorIds, ","));
		pageInfo.getList().forEach(item -> {
			item.setCreatorName(creatorUserInfoMap.getOrDefault(item.getCreator(), ""));
			item.setReminderStaffName(creatorUserInfoMap.getOrDefault(item.getReminderStaffId(), ""));
		});
		MyPageResult<TblSystemRefReminderAccessV> build = new MyPageResult<TblSystemRefReminderAccessV>().build(pageInfo);
		return MyResponseFormat.retParam(200, 200, build);
	}

	/**
	 * 催办信息 详情 查询
	 * @param id
	 * @return
	 */
	@Override
	public MyJsonBean<TblSystemRefReminderV> getRefReminder(TblStaffUtil loginStaff, Long id, String token) {
		TblSystemRefReminderV model = tblSystemRefReminderV1Service.findById(id);
		if (Objects.nonNull(model.getCreator())) {
			String creatorUserInfo = tblStaffOracleService.getCreatorUserInfo(model.getCreator());
			if (StringUtils.isNotBlank(creatorUserInfo)) {
				model.setCreatorName(creatorUserInfo);
			} else {
				model.setCreatorName("自动催办");
			}
		}
		if (Objects.nonNull(model.getReminderStaffId())) {
			String creatorUserInfo = tblStaffOracleService.getCreatorUserInfo(model.getReminderStaffId());
			if (StringUtils.isNotBlank(creatorUserInfo)) {
				model.setReminderStaffName(creatorUserInfo);
			} else {
				model.setReminderStaffName("自动催办");
			}
		}
		TblSystemRefopmReminderV vo = tblSystemRefopmReminderV1Mapper.selectByPrimaryKey(model.getRefopmId());
		if (Objects.nonNull(vo)) {
			if (StringUtils.isNotBlank(vo.getTypeName())) {
				model.setReminderStaffName(vo.getTypeName());
			} else {
				model.setReminderStaffName("月度评估-一体化管控措施");
			}
		}
		//催办信息 浏览
		isRead(loginStaff, id, token);
		return MyResponseFormat.retParam(200, 200, model);
	}

	/**
	 * 催办信息 浏览
	 * @param loginStaff
	 * @return
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public MyJsonBean<Void> isRead(TblStaffUtil loginStaff, Long id, String token) {
		//催办人-已阅读
		TblSystemRefReminderV refReminder = tblSystemRefReminderV1Service.findById(id);
		if (Objects.equals(refReminder.getIsRead(), 0)) {
			refReminder.setIsRead(1);
			tblSystemRefReminderV1Service.saveOrUpdate(refReminder, token);
			//催办访问记录
			TblSystemRefReminderAccessV save = new TblSystemRefReminderAccessV();
			save.setCreator(loginStaff.getStaffid().longValue());
			save.setWorkUnit(loginStaff.getLinkDetp().getOrgid().longValue());
			save.setBelongGroup(loginStaff.getCurrentOrg().getOrgid().longValue());
			save.setReminderStaffId(refReminder.getReminderStaffId());
			save.setReminderId(refReminder.getId());
			tblSystemRefReminderAccessV1Service.saveOrUpdate(save);
		}
		return MyResponseFormat.retParam(200, 200, null);
	}
}
