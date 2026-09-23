package com.huabo.legal.exam.modules.paper.job;

import com.huabo.legal.exam.modules.paper.service.PaperService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PaperQuJob {

	@Autowired
	private PaperService baseService;

	/**
	 * 进行考试生离场排查 满90自动交卷
	 */
//	@Scheduled(cron = "*/20 * * * * ?")
	public void examLeaveJob() {
		log.info("进行考试生离场排查 满90自动交卷 开始任务");
		baseService.updateExamLeaveJob();
		log.info("进行考试生离场排查 满90自动交卷 结束任务");
	}
}
