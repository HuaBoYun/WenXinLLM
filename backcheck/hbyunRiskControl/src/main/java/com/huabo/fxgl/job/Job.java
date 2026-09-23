package com.huabo.fxgl.job;

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
	 * 自动催办触发（重大风险-月度评估-一体化管控措施 提前3天内容 催办提醒）  每天凌晨7点
	 */
	@Scheduled(cron = "0 0 7 * * ?")
	public void reminderJob() {
		log.info("自动催办触发（重大风险-月度评估-一体化管控措施 催办提醒）-每天凌晨7点 开始任务");
		jobService.reminder();
		log.info("自动催办触发（重大风险-月度评估-一体化管控措施 提前3天内容 催办提醒）-每天凌晨7点 结束任务");
	}

	/**
	 * 自动催办触发（重大风险-月度评估 催办提醒）  每天凌晨7点
	 */
	@Scheduled(cron = "0 0 6 * * ?")
	public void reminderJob1() {
		log.info("自动催办触发（重大风险-月度评估 催办提醒）-每天凌晨7点 开始任务");
		jobService.reminderMonthlyEvaluateContent();
		log.info("自动催办触发（重大风险-月度评估 催办提醒）-每天凌晨7点 结束任务");
	}
}
