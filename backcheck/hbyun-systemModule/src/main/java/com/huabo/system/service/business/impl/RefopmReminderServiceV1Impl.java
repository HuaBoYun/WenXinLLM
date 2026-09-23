package com.huabo.system.service.business.impl;

import com.github.pagehelper.PageInfo;
import com.hbfk.entity.TblStaffUtil;
import com.huabo.system.entity.TblSystemRefReminderV;
import com.huabo.system.entity.TblSystemRefopmReminderV;
import com.huabo.system.exception.ServiceException;
import com.huabo.system.oracle.service.TblStaffOracleService;
import com.huabo.system.oracle.service.TblSystemRefReminderV1Service;
import com.huabo.system.oracle.service.TblSystemRefopmReminderV1Service;
import com.huabo.system.service.business.RefopmReminderV1Service;
import com.huabo.system.utils.MyJsonBean;
import com.huabo.system.utils.MyPageResult;
import com.huabo.system.utils.MyResponseFormat;
import com.huabo.system.vo.param.TblSystemRefopmReminderQueryParam;
import com.huabo.system.vo.result.RefReminderListResult;
import com.vip.vjtools.vjkit.collection.CollectionUtil;
import com.vip.vjtools.vjkit.time.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class RefopmReminderServiceV1Impl implements RefopmReminderV1Service {

	@Resource
	private TblSystemRefopmReminderV1Service tblSystemRefopmReminderV1Service;
	@Resource
	private TblSystemRefReminderV1Service tblSystemRefReminderV1Service;
	@Resource
	private TblStaffOracleService tblStaffOracleService;

	/**
	 * 催办关系 列表查询
	 * @param param
	 * @return
	 */
	@Override
	public MyJsonBean<TblSystemRefopmReminderV> getRefopmReminderList(TblSystemRefopmReminderQueryParam param) {
		PageInfo<TblSystemRefopmReminderV> pageInfo = tblSystemRefopmReminderV1Service.getList(param);
		if (CollectionUtil.isEmpty(pageInfo.getList())) {
			return MyResponseFormat.retParam(200, 200, MyPageResult.buildNoData());
		}
		List<Long> creators = pageInfo.getList().stream().filter(x -> Objects.nonNull(x.getCreator())).distinct()
				.map(TblSystemRefopmReminderV::getCreator).distinct().collect(Collectors.toList());
		Map<Long, String> creatorUserInfoMap = tblStaffOracleService.getCreatorUserInfoMap(StringUtils.join(creators, ","));
		pageInfo.getList().forEach(item -> {
			item.setCreatorName(creatorUserInfoMap.getOrDefault(item.getCreator(), ""));
		});
		MyPageResult<TblSystemRefopmReminderV> build = new MyPageResult<TblSystemRefopmReminderV>().build(pageInfo);
		return MyResponseFormat.retParam(200, 200, build);
	}


	/**
	 * 催办关系 新增/更新
	 * @param param
	 * @return
	 */
	@Override
	public MyJsonBean<TblSystemRefopmReminderV> saveOrUpdateRefopmReminder(TblSystemRefopmReminderV param) {
		//定制
		if (Objects.equals(param.getReminderId(), 3)) {
			if (StringUtils.isBlank(param.getReminderBusinessTable())) {
				throw new ServiceException("催办类型为定制类型，催办业务表名不能为空");
			}
			if (StringUtils.isBlank(param.getReminderBusinessTime())) {
				throw new ServiceException("催办类型为定制类型，催办业务时间字段不能为空");
			}
			if (StringUtils.isBlank(param.getReminderBusinessStaff())) {
				throw new ServiceException("催办类型为定制类型，催办业务人员字段不能为空");
			}
			if (StringUtils.isBlank(param.getReminderBusinessContent()) && StringUtils.isBlank(param.getReminderContent())) {
				throw new ServiceException("催办内容与催办定制内容不能同时为空");
			}
			TblSystemRefopmReminderV reminderV = new TblSystemRefopmReminderV();
			reminderV.setReminderBusinessTable(param.getReminderBusinessTable());
			reminderV.setId(param.getId());
			Integer count = tblSystemRefopmReminderV1Service.count(reminderV);
			if (count > 0) {
				throw new ServiceException("定制类型，表业务也存在，无法重复执行");
			}
			param.setReminderType(3);
		}
		TblSystemRefopmReminderV tblSystemRefopmReminder = tblSystemRefopmReminderV1Service.saveOrUpdate(param);
		return MyResponseFormat.retParam(200, 200, tblSystemRefopmReminder);
	}

	/**
	 * 催办关系 刪除
	 * @param id
	 * @return
	 */
	@Override
	public MyJsonBean<Void> deleteRefopmReminder(Long id) {
		tblSystemRefopmReminderV1Service.delete(id);
		return MyResponseFormat.retParam(200, 200, null);
	}

	/**
	 * 催办关系 详情 查询
	 * @param id
	 * @return
	 */
	@Override
	public MyJsonBean<TblSystemRefopmReminderV> getRefopmReminder(Long id) {
		TblSystemRefopmReminderV model = tblSystemRefopmReminderV1Service.findById(id);
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
		TblSystemRefopmReminderV model = tblSystemRefopmReminderV1Service.findById(id);
		if (Objects.isNull(model)) {
			throw new ServiceException("催办信息不存在");
		}
		model.setState(1);
		tblSystemRefopmReminderV1Service.saveOrUpdate(model);
		//推送催办信息
		if (!Objects.equals(model.getReminderType(), 3) && StringUtils.isNotBlank(model.getReminderStaffIds())) {
			List<Long> staffIds = Arrays.stream(model.getReminderStaffIds().split(",")).map(Long::new).collect(Collectors.toList());
			if (CollectionUtil.isNotEmpty(staffIds)) {
				Date now = new Date();
				TblSystemRefopmReminderV newModel = tblSystemRefopmReminderV1Service.findById(id);
				staffIds.forEach(item -> {
					TblSystemRefReminderV save = new TblSystemRefReminderV();
					save.setIsRead(0);
					save.setRefopmId(newModel.getId());
					save.setReminderStaffId(item);
					save.setReminderContent(newModel.getReminderContent());
					save.setCreator(newModel.getCreator());
					save.setWorkUnit(newModel.getWorkUnit());
					save.setBelongGroup(newModel.getBelongGroup());
					save.setCreatedTime(now);
					tblSystemRefReminderV1Service.saveOrUpdate(save, token);
				});
			}
		} else if (Objects.equals(model.getReminderType(), 3)) { //定制类型
			if (StringUtils.isBlank(model.getReminderBusinessTable())) {
				return MyResponseFormat.retParam(200, 200, null);
			}
			//查询业务表
			List<RefReminderListResult> businessList = tblSystemRefopmReminderV1Service
					.getBusinessList(model.getReminderBusinessTable(), model.getReminderBusinessId(), model.getReminderBusinessStaff(),
							model.getReminderBusinessTime(), model.getReminderBusinessContent());
			if (CollectionUtil.isEmpty(businessList)) {
				return MyResponseFormat.retParam(200, 200, null);
			}
			//新增数据
			Date now = new Date();
			businessList.forEach(item -> {
				if (StringUtils.isBlank(item.getBusinessTime()) || Objects.isNull(item.getStaffId())) {
					return;
				}
				try {
					Date now1 = new Date();
					Date parse = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(item.getBusinessTime());
					if (Objects.nonNull(model.getDateTime())) {
						Date date = DateUtil.subDays(parse, model.getDateTime());
						if (date.before(now1) && now1.before(parse)) {
							doSaveRefReminder(model, item, loginStaff, now, token);
						}
					} else {
						if (now1.before(parse)) {
							doSaveRefReminder(model, item, loginStaff, now, token);
						}
					}
				} catch (Exception e) {
					log.error("时间转换异常");
				}
			});
		}
		return MyResponseFormat.retParam(200, 200, null);
	}

	private void doSaveRefReminder(TblSystemRefopmReminderV model, RefReminderListResult item, TblStaffUtil loginStaff, Date now, String token) {
		TblSystemRefReminderV save = new TblSystemRefReminderV();
		save.setIsRead(0);
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		try {
			save.setReminderTime(format.parse(item.getBusinessTime()));
		} catch (ParseException e) {
			throw new ServiceException("时间转换异常");
		}
		save.setRefopmId(model.getId());
		save.setReminderStaffId(item.getStaffId());
		save.setReminderContent(model.getReminderContent());
		save.setReminderBusinessTableId(item.getBusinessId());
		save.setReminderBusinessTable(model.getReminderBusinessTable());
		if (StringUtils.isNotBlank(item.getContent())) {
			save.setReminderContent(item.getContent());
		}
		save.setCreator(loginStaff.getStaffid().longValue());
		save.setWorkUnit(loginStaff.getLinkDetp().getOrgid().longValue());
		save.setBelongGroup(loginStaff.getCurrentOrg().getOrgid().longValue());
		save.setCreatedTime(now);
		tblSystemRefReminderV1Service.saveOrUpdate(save, token);
	}

	/**
	 * 催办管理 停止催办
	 * @param id
	 * @return
	 */
	@Override
	public MyJsonBean<Void> isStopReminder(Long id) {
		tblSystemRefopmReminderV1Service.isStopReminder(id);
		return MyResponseFormat.retParam(200, 200, null);
	}

	/**
	 * 催办管理 停止催办
	 * @param id
	 * @return
	 */
	@Override
	public MyJsonBean<Void> isStartReminder(Long id) {
		tblSystemRefopmReminderV1Service.isStartReminder(id);
		return MyResponseFormat.retParam(200, 200, null);
	}
}
