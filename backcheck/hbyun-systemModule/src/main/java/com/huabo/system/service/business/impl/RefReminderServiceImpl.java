package com.huabo.system.service.business.impl;

import com.github.pagehelper.PageInfo;
import com.hbfk.entity.TblStaffUtil;
import com.huabo.system.entity.TblSystemRefReminder;
import com.huabo.system.entity.TblSystemRefReminderAccess;
import com.huabo.system.exception.ServiceException;
import com.huabo.system.oracle.service.TblStaffOracleService;
import com.huabo.system.oracle.service.TblSystemRefReminderAccessService;
import com.huabo.system.oracle.service.TblSystemRefReminderService;
import com.huabo.system.service.business.RefReminderService;
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
public class RefReminderServiceImpl implements RefReminderService {

	@Resource
	private TblSystemRefReminderService tblSystemRefReminderService;
	@Resource
	private TblSystemRefReminderAccessService tblSystemRefReminderAccessService;
	@Resource
	private TblStaffOracleService tblStaffOracleService;

	/**
	 * 催办信息 列表查询
	 * @param param
	 * @return
	 */
	@Override
	public MyJsonBean<TblSystemRefReminder> getRefReminderList(TblSystemRefReminderQueryParam param) {
		PageInfo<TblSystemRefReminder> pageInfo = tblSystemRefReminderService.getList(param);
		if (CollectionUtil.isEmpty(pageInfo.getList())) {
			return MyResponseFormat.retParam(200, 200, MyPageResult.buildNoData());
		}
		List<Long> creatorIds = new ArrayList<>();
		List<Long> creators = pageInfo.getList().stream().filter(x -> Objects.nonNull(x.getCreator())).distinct()
				.map(TblSystemRefReminder::getCreator).distinct().collect(Collectors.toList());
		if (CollectionUtil.isNotEmpty(creators)) {
			creatorIds.addAll(creators);
		}
		List<Long> reminderStaffIds = pageInfo.getList().stream().filter(x -> Objects.nonNull(x.getReminderStaffId())).distinct()
				.map(TblSystemRefReminder::getReminderStaffId).distinct().collect(Collectors.toList());
		if (CollectionUtil.isNotEmpty(reminderStaffIds)) {
			creatorIds.addAll(reminderStaffIds);
		}
		Map<Long, String> creatorUserInfoMap = tblStaffOracleService.getCreatorUserInfoMap(StringUtils.join(creatorIds, ","));
		pageInfo.getList().forEach(item -> {
			item.setCreatorName(creatorUserInfoMap.getOrDefault(item.getCreator(), ""));
			item.setReminderStaffName(creatorUserInfoMap.getOrDefault(item.getReminderStaffId(), ""));
		});
		MyPageResult<TblSystemRefReminder> build = new MyPageResult<TblSystemRefReminder>().build(pageInfo);
		return MyResponseFormat.retParam(200, 200, build);
	}

	/**
	 * 催办访问记录 列表查询
	 * @param param
	 * @return
	 */
	@Override
	public MyJsonBean<TblSystemRefReminderAccess> getRefReminderAccessList(TblSystemRefReminderAccessQueryParam param) {
		if (StringUtils.isBlank(param.getModuleRoute())) {
			throw new ServiceException("模块编码不能为空");
		}
		PageInfo<TblSystemRefReminderAccess> pageInfo = tblSystemRefReminderAccessService.getList(param);
		if (CollectionUtil.isEmpty(pageInfo.getList())) {
			return MyResponseFormat.retParam(200, 200, MyPageResult.buildNoData());
		}
		List<Long> creatorIds = new ArrayList<>();
		List<Long> creators = pageInfo.getList().stream().filter(x -> Objects.nonNull(x.getCreator())).distinct()
				.map(TblSystemRefReminderAccess::getCreator).distinct().collect(Collectors.toList());
		if (CollectionUtil.isNotEmpty(creators)) {
			creatorIds.addAll(creators);
		}
		List<Long> reminderStaffIds = pageInfo.getList().stream().filter(x -> Objects.nonNull(x.getReminderStaffId())).distinct()
				.map(TblSystemRefReminderAccess::getReminderStaffId).distinct().collect(Collectors.toList());
		if (CollectionUtil.isNotEmpty(reminderStaffIds)) {
			creatorIds.addAll(reminderStaffIds);
		}
		Map<Long, String> creatorUserInfoMap = tblStaffOracleService.getCreatorUserInfoMap(StringUtils.join(creatorIds, ","));
		pageInfo.getList().forEach(item -> {
			item.setCreatorName(creatorUserInfoMap.getOrDefault(item.getCreator(), ""));
			item.setReminderStaffName(creatorUserInfoMap.getOrDefault(item.getReminderStaffId(), ""));
		});
		MyPageResult<TblSystemRefReminderAccess> build = new MyPageResult<TblSystemRefReminderAccess>().build(pageInfo);
		return MyResponseFormat.retParam(200, 200, build);
	}

	/**
	 * 催办信息 详情 查询
	 * @param id
	 * @return
	 */
	@Override
	public MyJsonBean<TblSystemRefReminder> getRefReminder(TblStaffUtil loginStaff, Long id, String token) {
		TblSystemRefReminder model = tblSystemRefReminderService.findById(id);
		if (Objects.nonNull(model.getCreator())) {
			model.setCreatorName(tblStaffOracleService.getCreatorUserInfo(model.getCreator()));
		}
		if (Objects.nonNull(model.getReminderStaffId())) {
			model.setReminderStaffName(tblStaffOracleService.getCreatorUserInfo(model.getReminderStaffId()));
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
		TblSystemRefReminder refReminder = tblSystemRefReminderService.findById(id);
		if (Objects.equals(refReminder.getIsRead(), 0)) {
			refReminder.setIsRead(1);
			tblSystemRefReminderService.saveOrUpdate(refReminder, token);
			//催办访问记录
			TblSystemRefReminderAccess save = new TblSystemRefReminderAccess();
			save.setCreator(loginStaff.getStaffid().longValue());
			save.setWorkUnit(loginStaff.getLinkDetp().getOrgid().longValue());
			save.setBelongGroup(loginStaff.getCurrentOrg().getOrgid().longValue());
			save.setReminderStaffId(refReminder.getReminderStaffId());
			save.setReminderId(refReminder.getId());
			tblSystemRefReminderAccessService.saveOrUpdate(save);
		}
		return MyResponseFormat.retParam(200, 200, null);
	}
}
