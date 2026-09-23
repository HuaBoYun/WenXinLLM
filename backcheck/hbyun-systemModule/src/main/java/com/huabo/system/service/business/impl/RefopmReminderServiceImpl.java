package com.huabo.system.service.business.impl;

import com.github.pagehelper.PageInfo;
import com.hbfk.entity.TblStaffUtil;
import com.huabo.system.exception.ServiceException;
import com.huabo.system.entity.TblSystemRefReminder;
import com.huabo.system.entity.TblSystemRefopmReminder;
import com.huabo.system.oracle.service.TblStaffOracleService;
import com.huabo.system.oracle.service.TblSystemRefReminderService;
import com.huabo.system.oracle.service.TblSystemRefopmReminderService;
import com.huabo.system.service.business.RefopmReminderService;
import com.huabo.system.utils.MyJsonBean;
import com.huabo.system.utils.MyPageResult;
import com.huabo.system.utils.MyResponseFormat;
import com.huabo.system.vo.param.TblSystemRefopmReminderQueryParam;
import com.vip.vjtools.vjkit.collection.CollectionUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class RefopmReminderServiceImpl implements RefopmReminderService {

	@Resource
	private TblSystemRefopmReminderService tblSystemRefopmReminderService;
	@Resource
	private TblSystemRefReminderService tblSystemRefReminderService;
	@Resource
	private TblStaffOracleService tblStaffOracleService;

	/**
	 * 催办关系 列表查询
	 * @param param
	 * @return
	 */
	@Override
	public MyJsonBean<TblSystemRefopmReminder> getRefopmReminderList(TblSystemRefopmReminderQueryParam param) {
		PageInfo<TblSystemRefopmReminder> pageInfo = tblSystemRefopmReminderService.getList(param);
		if (CollectionUtil.isEmpty(pageInfo.getList())) {
			return MyResponseFormat.retParam(200, 200, MyPageResult.buildNoData());
		}
		List<Long> creators = pageInfo.getList().stream().filter(x -> Objects.nonNull(x.getCreator())).distinct()
				.map(TblSystemRefopmReminder::getCreator).distinct().collect(Collectors.toList());
		Map<Long, String> creatorUserInfoMap = tblStaffOracleService.getCreatorUserInfoMap(StringUtils.join(creators, ","));
		pageInfo.getList().forEach(item -> {
			item.setCreatorName(creatorUserInfoMap.getOrDefault(item.getCreator(), ""));
		});
		MyPageResult<TblSystemRefopmReminder> build = new MyPageResult<TblSystemRefopmReminder>().build(pageInfo);
		return MyResponseFormat.retParam(200, 200, build);
	}


	/**
	 * 催办关系 新增/更新
	 * @param param
	 * @return
	 */
	@Override
	public MyJsonBean<TblSystemRefopmReminder> saveOrUpdateRefopmReminder(TblSystemRefopmReminder param) {
		TblSystemRefopmReminder tblSystemRefopmReminder = tblSystemRefopmReminderService.saveOrUpdate(param);
		return MyResponseFormat.retParam(200, 200, tblSystemRefopmReminder);
	}

	/**
	 * 催办关系 刪除
	 * @param id
	 * @return
	 */
	@Override
	public MyJsonBean<Void> deleteRefopmReminder(Long id) {
		tblSystemRefopmReminderService.delete(id);
		return MyResponseFormat.retParam(200, 200, null);
	}

	/**
	 * 催办关系 详情 查询
	 * @param id
	 * @return
	 */
	@Override
	public MyJsonBean<TblSystemRefopmReminder> getRefopmReminder(Long id) {
		TblSystemRefopmReminder model = tblSystemRefopmReminderService.findById(id);
		if (Objects.nonNull(model.getCreator())) {
			model.setCreatorName(tblStaffOracleService.getCreatorUserInfo(model.getCreator()));
		}
		if (StringUtils.isNotBlank(model.getReminderStaffIds())) {
			model.setReminderStaffIdsNames(tblStaffOracleService.getCreatorUserInfos(model.getReminderStaffIds()));
		}
		return MyResponseFormat.retParam(200, 200, model);
	}

	/**
	 * 催办管理 催办
	 * @param id
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public MyJsonBean<Void> reminder(TblStaffUtil loginStaff, Long id, String token) {
		TblSystemRefopmReminder model = tblSystemRefopmReminderService.findById(id);
		if (Objects.isNull(model)) {
			throw new ServiceException("催办信息不存在");
		}
		model.setState(1);
		tblSystemRefopmReminderService.saveOrUpdate(model);
		//推送催办信息
		if (StringUtils.isNotBlank(model.getReminderStaffIds())) {
			List<Long> staffIds = Arrays.stream(model.getReminderStaffIds().split(",")).map(Long::new).collect(Collectors.toList());
			if (CollectionUtil.isNotEmpty(staffIds)) {
				Date now = new Date();
				TblSystemRefopmReminder newModel = tblSystemRefopmReminderService.findById(id);
				staffIds.forEach(item -> {
					TblSystemRefReminder save = new TblSystemRefReminder();
					save.setIsRead(0);
					save.setRefopmId(newModel.getId());
					save.setReminderStaffId(item);
					save.setReminderContent(newModel.getReminderContent());
					save.setCreator(newModel.getCreator());
					save.setWorkUnit(newModel.getWorkUnit());
					save.setBelongGroup(newModel.getBelongGroup());
					save.setCreatedTime(now);
					tblSystemRefReminderService.saveOrUpdate(save, token);
				});
			}
		}
		return MyResponseFormat.retParam(200, 200, null);
	}

	/**
	 * 催办管理 停止催办
	 * @param id
	 * @return
	 */
	@Override
	public MyJsonBean<Void> isStopReminder(Long id) {
		tblSystemRefopmReminderService.isStopReminder(id);
		return MyResponseFormat.retParam(200, 200, null);
	}
}
