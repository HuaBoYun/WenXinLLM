package com.huabo.system.job;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Slf4j
@Component
public class Job {

	@Resource
	private JobService jobService;

	/**
	 * 自动催办触发  每天凌晨1点
	 */
	@Scheduled(cron = "0 0 9 * * ?")
	public void refopmReminderAutoMonthJob() {
		log.info("自动催办触发-每天凌晨1点 开始任务");
		jobService.refopmReminderAutoMonth(null);
		log.info("自动催办触发-每天凌晨1点 结束任务");
	}

	/**
	 * 自动催办触发  每天凌晨1点
	 */
	@Scheduled(cron = "0 0 7 * * ?")
	public void refopmReminderAutoMonthJob1() {
		log.info("自动催办触发-每天凌晨1点 开始任务");
		jobService.refopmReminderAutoMonth1(null);
		log.info("自动催办触发-每天凌晨1点 结束任务");
	}
}
