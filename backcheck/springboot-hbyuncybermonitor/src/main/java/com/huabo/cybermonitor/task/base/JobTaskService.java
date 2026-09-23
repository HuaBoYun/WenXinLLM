package com.huabo.cybermonitor.task.base;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.lang.StringUtils;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

/**
 * 定时任务
 * @author SongXiangYing
 *
 */
@Service
public class JobTaskService {

	private static final Logger logger = LoggerFactory.getLogger(JobTaskService.class);

	@Autowired
	private SchedulerFactoryBean schedulerFactoryBean;
//
//	@Autowired
//	private YwsjTime ywsjTime;
//
//	@Autowired
//	private YwsjTable ywsjTable;
//
//	@Autowired
//	private FinanceTask financeTask;
//
//	@Autowired
//	private LuceneTask luceneTask;
//
//	@Autowired
//	private ReckonFinance reckonFinance;
//	@Autowired
//	private ReformSoutionTask reformSoutionTask;
//
//
//	@Autowired
//	private RedisRoleOrgTask redisRoleOrgTask;
//
//	@Autowired
//	private LawRegulationTaskRtask lawRegulationTaskRtask;

	/*@Autowired
	private FtpUploadFileTask ftpUploadFileTask;*/
	/**
	 * 更改任务状态
	 *
	 * @throws SchedulerException
	 */
	public void changeStatus(ScheduleJob job, String cmd) throws SchedulerException {
		if (job == null) {
			return;
		}
		if ("stop".equals(cmd)) {
			deleteJob(job);
			job.setJobStatus(ScheduleJob.STATUS_NOT_RUNNING);
		} else if ("start".equals(cmd)) {
			job.setJobStatus(ScheduleJob.STATUS_RUNNING);
			addJob(job);
		}
	}

	/**
	 * 更改任务 cron表达式
	 *
	 * @throws SchedulerException
	 */
//	public void updateCron(Long jobId, String cron) throws SchedulerException {
//		ScheduleJob job = getTaskById(jobId);
//		if (job == null) {
//			return;
//		}
//		job.setCronExpression(cron);
//		if (ScheduleJob.STATUS_RUNNING.equals(job.getJobStatus())) {
//			updateJobCron(job);
//		}
//		scheduleJobMapper.updateByPrimaryKeySelective(job);
//
//	}

	/**
	 * 添加任务
	 *
	 * @param
	 * @throws SchedulerException
	 */
	public void addJob(ScheduleJob job) throws SchedulerException {
		if (job == null || !ScheduleJob.STATUS_RUNNING.equals(job.getJobStatus())) {
			return;
		}

		Scheduler scheduler = schedulerFactoryBean.getScheduler();
		logger.info(scheduler + ".......................................................................................add");
		TriggerKey triggerKey = TriggerKey.triggerKey(job.getJobName(), job.getJobGroup());

		CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);

		// 不存在，创建一个
		if (null == trigger) {
			Class clazz = ScheduleJob.CONCURRENT_IS.equals(job.getIsConcurrent()) ? QuartzJobFactory.class : QuartzJobFactoryDisallowConcurrentExecution.class;

			JobDetail jobDetail = JobBuilder.newJob(clazz).withIdentity(job.getJobName(), job.getJobGroup()).build();

			jobDetail.getJobDataMap().put("scheduleJob", job);

			CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(job.getCronExpression());

			trigger = TriggerBuilder.newTrigger().withIdentity(job.getJobName(), job.getJobGroup()).withSchedule(scheduleBuilder).build();

			scheduler.scheduleJob(jobDetail, trigger);
		} else {
			// Trigger已存在，那么更新相应的定时设置
			CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(job.getCronExpression());

			// 按新的cronExpression表达式重新构建trigger
			trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();

			// 按新的trigger重新设置job执行
			scheduler.rescheduleJob(triggerKey, trigger);
		}
	}

	@PostConstruct
	public void init() throws Exception {
//
		//Scheduler scheduler = schedulerFactoryBean.getScheduler();
		//业务数据表添加和删除
		ScheduleJob table = new  ScheduleJob();
		table.setCronExpression("0 0 23 * * ?");
		table.setJobGroup(ScheduleJob.GROUP_TYPE);
		table.setJobName("ywsjTable");
		table.setJobStatus(ScheduleJob.STATUS_RUNNING);
		table.setIsConcurrent(ScheduleJob.CONCURRENT_NOT);
		//table.setTask(ywsjTable);
		//业务数据导入
		ScheduleJob sdj = new  ScheduleJob();
		sdj.setCronExpression("0 0 1 * * ?");
		sdj.setJobGroup(ScheduleJob.GROUP_TYPE);
		sdj.setJobName("YWSJIMPORTNAME");
		sdj.setJobStatus(ScheduleJob.STATUS_RUNNING);
		sdj.setIsConcurrent(ScheduleJob.CONCURRENT_NOT);
		//sdj.setTask(ywsjTime);
		//万能搜索创建索引
		ScheduleJob lucene = new  ScheduleJob();
		lucene.setCronExpression("0 0 2 * * ?");
		lucene.setJobGroup(ScheduleJob.GROUP_TYPE);
		lucene.setJobName("lucene");
		lucene.setJobStatus(ScheduleJob.STATUS_RUNNING);
		lucene.setIsConcurrent(ScheduleJob.CONCURRENT_NOT);
		///lucene.setTask(luceneTask);
		//财务数据导入
		ScheduleJob finance = new  ScheduleJob();
		finance.setCronExpression("0 0 3 * * ?");
		finance.setJobGroup(ScheduleJob.GROUP_TYPE);
		finance.setJobName("financeTask");
		finance.setJobStatus(ScheduleJob.STATUS_RUNNING);
		finance.setIsConcurrent(ScheduleJob.CONCURRENT_NOT);
		//finance.setTask(financeTask);

		ScheduleJob reckon = new  ScheduleJob();
		reckon.setCronExpression("0 0 0 1 * ?");
		reckon.setJobGroup(ScheduleJob.GROUP_TYPE);
		reckon.setJobName("reckonFinance");
		reckon.setJobStatus(ScheduleJob.STATUS_RUNNING);
		reckon.setIsConcurrent(ScheduleJob.CONCURRENT_NOT);
		//reckon.setTask(reckonFinance);


		ScheduleJob ftpfile = new  ScheduleJob();
		ftpfile.setCronExpression("0 30 2 * * ?");
		ftpfile.setJobGroup(ScheduleJob.GROUP_TYPE);
		ftpfile.setJobName("ftpUploadFileTask");
		ftpfile.setJobStatus(ScheduleJob.STATUS_RUNNING);
		ftpfile.setIsConcurrent(ScheduleJob.CONCURRENT_NOT);
		//ftpfile.setTask(ftpUploadFileTask);

		/**
		 * 定时任务执行缓存， 每周六23点30分
		 */
		ScheduleJob redisTask = new  ScheduleJob();
		redisTask.setCronExpression("0 0 0 * * ? *");
		redisTask.setJobGroup(ScheduleJob.GROUP_TYPE);
		redisTask.setJobName("redisRoleOrgTask");
		redisTask.setJobStatus(ScheduleJob.STATUS_RUNNING);
		redisTask.setIsConcurrent(ScheduleJob.CONCURRENT_NOT);
		//redisTask.setTask(redisRoleOrgTask);


		ScheduleJob soultion = new  ScheduleJob();
		soultion.setCronExpression("0 0 9 0 0 1 *");
		soultion.setJobGroup(ScheduleJob.GROUP_TYPE);
		soultion.setJobName("reformSoutionTask");
		soultion.setJobStatus(ScheduleJob.STATUS_RUNNING);
		soultion.setIsConcurrent(ScheduleJob.CONCURRENT_NOT);
		//soultion.setTask(reformSoutionTask);


		ScheduleJob lawRegulation = new  ScheduleJob();
		soultion.setCronExpression("* * * LW 1-2 ? *");
		soultion.setJobGroup(ScheduleJob.GROUP_TYPE);
		soultion.setJobName("lawRegulationTaskRtask");
		soultion.setJobStatus(ScheduleJob.STATUS_RUNNING);
		soultion.setIsConcurrent(ScheduleJob.CONCURRENT_NOT);
		//soultion.setTask(lawRegulationTaskRtask);

		//addJob(redisTask);
		//addJob(lawRegulation);

		//addJob(finance);
		//addJob(sdj);
		//addJob(table);
		//addJob(ftpfile);
		//addJob(lucene);
		//addJob(reckon);
//
//		// 这里获取任务信息数据
//		List<ScheduleJob> jobList = scheduleJobMapper.getAll();
//
//		for (ScheduleJob job : jobList) {
//			addJob(job);
//		}
	}

	/**
	 * 获取所有计划中的任务列表
	 *
	 * @return
	 * @throws SchedulerException
	 */
	public List<ScheduleJob> getAllJob() throws SchedulerException {
		//Scheduler scheduler = schedulerFactoryBean.getScheduler();
		GroupMatcher<JobKey> matcher = GroupMatcher.anyJobGroup();
		//Set<JobKey> jobKeys = scheduler.getJobKeys(matcher);
		List<ScheduleJob> jobList = new ArrayList<ScheduleJob>();
//		for (JobKey jobKey : jobKeys) {
//			List<? extends Trigger> triggers = scheduler.getTriggersOfJob(jobKey);
//			for (Trigger trigger : triggers) {
//				ScheduleJob job = new ScheduleJob();
//				job.setJobName(jobKey.getName());
//				job.setJobGroup(jobKey.getGroup());
//				job.setDescription("触发器:" + trigger.getKey());
//				Trigger.TriggerState triggerState = scheduler.getTriggerState(trigger.getKey());
//				job.setJobStatus(triggerState.name());
//				if (trigger instanceof CronTrigger) {
//					CronTrigger cronTrigger = (CronTrigger) trigger;
//					String cronExpression = cronTrigger.getCronExpression();
//					job.setCronExpression(cronExpression);
//				}
//				jobList.add(job);
//			}
//		}
		return jobList;
	}

	/**
	 * 所有正在运行的job
	 *
	 * @return
	 * @throws SchedulerException
	 */
	public List<ScheduleJob> getRunningJob() throws SchedulerException {
		//Scheduler scheduler = schedulerFactoryBean.getScheduler();
		//List<JobExecutionContext> executingJobs = scheduler.getCurrentlyExecutingJobs();
		//List<ScheduleJob> jobList = new ArrayList<ScheduleJob>(executingJobs.size());
		List<ScheduleJob> jobList = new ArrayList<ScheduleJob>();
//		for (JobExecutionContext executingJob : executingJobs) {
//			ScheduleJob job = new ScheduleJob();
//			JobDetail jobDetail = executingJob.getJobDetail();
//			JobKey jobKey = jobDetail.getKey();
//			Trigger trigger = executingJob.getTrigger();
//			job.setJobName(jobKey.getName());
//			job.setJobGroup(jobKey.getGroup());
//			job.setDescription("触发器:" + trigger.getKey());
//			Trigger.TriggerState triggerState = scheduler.getTriggerState(trigger.getKey());
//			job.setJobStatus(triggerState.name());
//			if (trigger instanceof CronTrigger) {
//				CronTrigger cronTrigger = (CronTrigger) trigger;
//				String cronExpression = cronTrigger.getCronExpression();
//				job.setCronExpression(cronExpression);
//			}
//			jobList.add(job);
//		}
		return jobList;
	}

	/**
	 * 暂停一个job
	 *
	 * @param scheduleJob
	 * @throws SchedulerException
	 */
	public void pauseJob(ScheduleJob scheduleJob) throws SchedulerException {
//		Scheduler scheduler = schedulerFactoryBean.getScheduler();
//		JobKey jobKey = JobKey.jobKey(scheduleJob.getJobName(), scheduleJob.getJobGroup());
//		scheduler.pauseJob(jobKey);
	}

	/**
	 * 恢复一个job
	 *
	 * @param scheduleJob
	 * @throws SchedulerException
	 */
	public void resumeJob(ScheduleJob scheduleJob) throws SchedulerException {
//		Scheduler scheduler = schedulerFactoryBean.getScheduler();
//		JobKey jobKey = JobKey.jobKey(scheduleJob.getJobName(), scheduleJob.getJobGroup());
//		scheduler.resumeJob(jobKey);
	}

	/**
	 * 删除一个job
	 *
	 * @param scheduleJob
	 * @throws SchedulerException
	 */
	public void deleteJob(ScheduleJob scheduleJob) throws SchedulerException {
//		Scheduler scheduler = schedulerFactoryBean.getScheduler();
//		JobKey jobKey = JobKey.jobKey(scheduleJob.getJobName(), scheduleJob.getJobGroup());
//		scheduler.deleteJob(jobKey);

	}

	/**
	 * 立即执行job
	 *
	 * @param scheduleJob
	 * @throws SchedulerException
	 */
	public void runAJobNow(ScheduleJob scheduleJob) throws SchedulerException {
//		Scheduler scheduler = schedulerFactoryBean.getScheduler();
//		JobKey jobKey = JobKey.jobKey(scheduleJob.getJobName(), scheduleJob.getJobGroup());
//		scheduler.triggerJob(jobKey);
	}

	/**
	 * 更新job时间表达式
	 *
	 * @param scheduleJob
	 * @throws SchedulerException
	 */
	public void updateJobCron(ScheduleJob scheduleJob) throws SchedulerException {
		//Scheduler scheduler = schedulerFactoryBean.getScheduler();

		TriggerKey triggerKey = TriggerKey.triggerKey(scheduleJob.getJobName(), scheduleJob.getJobGroup());

		//CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);

		CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(scheduleJob.getCronExpression());

		//trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();

		//scheduler.rescheduleJob(triggerKey, trigger);
	}

	private String getCronExession(String exec) throws ConfigurationException{
//		if(exec.equals("日")){
//			return ConfigXmlUtils.FlagConfig("znyj", "day");
//		}else if(exec.equals("周")){
//			return ConfigXmlUtils.FlagConfig("znyj", "season");
//		}else if(exec.equals("月")){
//			return ConfigXmlUtils.FlagConfig("znyj", "month");
//		}else if(exec.equals("季度")){
//			return ConfigXmlUtils.FlagConfig("znyj", "quarter");
//		}else if(exec.equals("年")){
//			return ConfigXmlUtils.FlagConfig("znyj", "year");
//		}
		return null;
	}

	public void updateJob(ScheduleJob scheduleJob,String exe) throws SchedulerException, ConfigurationException{
		if(StringUtils.isNotBlank(exe)){
			scheduleJob.setCronExpression(getCronExession(exe));
			addJob(scheduleJob);
			//TODO  测试结果   添加任务直接执行
			runAJobNow(scheduleJob);
			//log.info("----添加任务----");
		}else{
			deleteJob(scheduleJob);
			//log.info("----删除任务----");
		}
	}
	public static void main(String[] args) {
		CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule("xxxxx");
	}
}
