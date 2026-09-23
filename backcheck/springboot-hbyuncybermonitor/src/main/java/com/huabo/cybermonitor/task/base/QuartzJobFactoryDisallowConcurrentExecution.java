package com.huabo.cybermonitor.task.base;

import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@DisallowConcurrentExecution
public class QuartzJobFactoryDisallowConcurrentExecution implements Job {

	private static final Logger logger = LoggerFactory.getLogger(JobTaskService.class);

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		ScheduleJob scheduleJob = (ScheduleJob) context.getMergedJobDataMap().get("scheduleJob");
		Scheduler  s = context.getScheduler();
		scheduleJob.getTask().run();
		JobKey jobKey = JobKey.jobKey(scheduleJob.getJobName(), scheduleJob.getJobGroup());
		try {
			if(ScheduleJob.GROUP_ONLY.equals(scheduleJob.getJobGroup())){
				s.deleteJob(jobKey);
			}
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
	}
}