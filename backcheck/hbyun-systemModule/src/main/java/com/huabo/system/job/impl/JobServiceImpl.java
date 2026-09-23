package com.huabo.system.job.impl;

import com.huabo.system.entity.TblSystemRefopmReminder;
import com.huabo.system.entity.TblSystemRefopmReminderV;
import com.huabo.system.job.JobService;
import com.huabo.system.oracle.service.TblSystemRefopmReminderService;
import com.huabo.system.oracle.service.TblSystemRefopmReminderV1Service;
import com.huabo.system.service.business.RefopmReminderService;
import com.huabo.system.service.business.RefopmReminderV1Service;
import com.huabo.system.vo.param.AutoRefopmReminderParam;
import com.vip.vjtools.vjkit.collection.CollectionUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class JobServiceImpl implements JobService {

	@Resource
	private TblSystemRefopmReminderService tblSystemRefopmReminderService;
	@Resource
	private RefopmReminderService refopmReminderService;
	@Resource
	private TblSystemRefopmReminderV1Service tblSystemRefopmReminderV1Service;
	@Resource
	private RefopmReminderV1Service refopmReminderV1Service;


	/**
	 * 定时器  自动催办月
	 * @param param
	 * @return
	 */
	@Override
	public void refopmReminderAutoMonth(AutoRefopmReminderParam param) {
		List<TblSystemRefopmReminder> list = tblSystemRefopmReminderService.getAutoList();
		if (CollectionUtil.isEmpty(list)) {
			return;
		}
		Calendar calendar = Calendar.getInstance();
		if (Objects.nonNull(param) && Objects.nonNull(param.getTime())) {
			calendar.setTime(param.getTime());
		}
		for (TblSystemRefopmReminder item : list) {
			if (Objects.equals(item.getReminderType(), 1)) { //月类型
				// 设置为当前日期
				int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
				Integer reminderMonth = item.getReminderMonth();
				if (dayOfMonth == reminderMonth) {
					refopmReminderService.reminder(null, item.getId(), null);
				} else {
					log.info("催办管理ID：{} ...月类型 ...未到时间催办", item.getId());
				}
			} else { //周类型
				int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK); // 获取星期几（1-7），其中1为星期天，2为星期一，依此类推
				Integer reminderWeek = item.getReminderWeek();
				//周一到周日分别是 2、3、4、5、6、7、1
				if (Objects.equals(reminderWeek, 1)) {
					reminderWeek = 2;
				} else if (Objects.equals(reminderWeek, 2)) {
					reminderWeek = 3;
				} else if (Objects.equals(reminderWeek, 3)) {
					reminderWeek = 4;
				} else if (Objects.equals(reminderWeek, 4)) {
					reminderWeek = 5;
				} else if (Objects.equals(reminderWeek, 5)) {
					reminderWeek = 6;
				} else if (Objects.equals(reminderWeek, 6)) {
					reminderWeek = 7;
				} else if (Objects.equals(reminderWeek, 7)) {
					reminderWeek = 1;
				} else {
					log.info("催办管理ID：{} ...周类型 ...类型值输入异常:{}", item.getId(), item.getReminderWeek());
					continue;
				}
				if (dayOfWeek == reminderWeek) {
					refopmReminderService.reminder(null, item.getId(), null);
				} else {
					log.info("催办管理ID：{} ...周类型 ...未到时间催办", item.getId());
				}
			}
		}
	}

	/**
	 * 定时器  自动催办月
	 * @param param
	 * @return
	 */
	@Override
	public void refopmReminderAutoMonth1(AutoRefopmReminderParam param) {
		List<TblSystemRefopmReminderV> list = tblSystemRefopmReminderV1Service.getAutoList();
		if (CollectionUtil.isEmpty(list)) {
			return;
		}
		Calendar calendar = Calendar.getInstance();
		if (Objects.nonNull(param) && Objects.nonNull(param.getTime())) {
			calendar.setTime(param.getTime());
		}
		for (TblSystemRefopmReminderV item : list) {
			if (Objects.equals(item.getReminderType(), 1)) { //月类型
				// 设置为当前日期
				int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
				Integer reminderMonth = item.getReminderMonth();
				if (dayOfMonth == reminderMonth) {
					refopmReminderService.reminder(null, item.getId(), null);
				} else {
					log.info("催办管理ID：{} ...月类型 ...未到时间催办", item.getId());
				}
			} else if (Objects.equals(item.getReminderType(), 2)) { //周类型
				int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK); // 获取星期几（1-7），其中1为星期天，2为星期一，依此类推
				Integer reminderWeek = item.getReminderWeek();
				//周一到周日分别是 2、3、4、5、6、7、1
				if (Objects.equals(reminderWeek, 1)) {
					reminderWeek = 2;
				} else if (Objects.equals(reminderWeek, 2)) {
					reminderWeek = 3;
				} else if (Objects.equals(reminderWeek, 3)) {
					reminderWeek = 4;
				} else if (Objects.equals(reminderWeek, 4)) {
					reminderWeek = 5;
				} else if (Objects.equals(reminderWeek, 5)) {
					reminderWeek = 6;
				} else if (Objects.equals(reminderWeek, 6)) {
					reminderWeek = 7;
				} else if (Objects.equals(reminderWeek, 7)) {
					reminderWeek = 1;
				} else {
					log.info("催办管理ID：{} ...周类型 ...类型值输入异常:{}", item.getId(), item.getReminderWeek());
					continue;
				}
				if (dayOfWeek == reminderWeek) {
					refopmReminderV1Service.reminder(null, item.getId(), null);
				} else {
					log.info("催办管理ID：{} ...周类型 ...未到时间催办", item.getId());
				}
			} else {
				refopmReminderV1Service.reminder(null, item.getId(), null);
			}
		}
	}
}
