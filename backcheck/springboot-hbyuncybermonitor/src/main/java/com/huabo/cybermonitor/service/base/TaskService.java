package com.huabo.cybermonitor.service.base;


import com.hbfk.entity.TblStaffUtil;
import com.huabo.cybermonitor.entity.Indicator;
import com.huabo.cybermonitor.entity.MonitorModel;
import com.huabo.cybermonitor.entity.MonitorSolution;
import com.huabo.cybermonitor.entity.Staff;
import com.huabo.cybermonitor.task.*;
import com.huabo.cybermonitor.task.base.JobTaskService;
import com.huabo.cybermonitor.task.base.ScheduleJob;
import org.quartz.SchedulerException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;

@Service
public class TaskService{

	@Resource
	public ZNJKMXTask znjkmxTask;
	@Resource
	public ZNJKMXYJTask znjkmxyjTask;
	@Resource
	public JobTaskService jobTaskService;
	@Resource
	public ZNJKGZYJTask znjkgzyjTask;
	@Resource
	public ZNJKZBTask znjkzbTask;
	@Resource
	public ZNJKZBYJTask znjkzbyjTask;
	@Resource
	public ZNJKGZTask znjkgzTask;
	/**
	 * 规则预警   --执行
	 * @param solution
	 * @param user
	 * @throws SchedulerException 
	 */
	public ScheduleJob gzzjTaskExec(List<MonitorSolution> solution, Staff user, String label, Integer  source) throws SchedulerException{
//		for (MonitorSolution tblMonitorSolution : solution) {
//			Set<TblMonitorSolutionRule> tblMonitorSolutionRules = tblMonitorSolution.getTblMonitorSolutionRules();
//			for (TblMonitorSolutionRule tblMonitorSolutionRule : tblMonitorSolutionRules) {
//				TblMonitorRule tblMonitorRule = tblMonitorSolutionRule.getTblMonitorRule();
//				if(null!=tblMonitorRule){
//					System.out.println(tblMonitorRule.getConnectionstrings());
//				}
//			}
//		}
		ZNJKGZYJTask znjkgzyjTask = this.znjkgzyjTask;
		znjkgzyjTask.setMonitorSolutions(solution);
		znjkgzyjTask.setStaff(user);
		znjkgzyjTask.setLabel(label);
		znjkgzyjTask.setSource(source);
		//znjkgzyjTask.setSolutionRules(tblMonitorSolutionRules);
		ScheduleJob job = new ScheduleJob();
		job.setJobGroup(ScheduleJob.GROUP_ONLY);
		job.setJobName(user.getStaffid().toString());
		job.setCronExpression("0 0 0 ? * *");
		job.setJobStatus(ScheduleJob.STATUS_RUNNING);
		job.setIsConcurrent(ScheduleJob.CONCURRENT_IS);
		job.setTask(znjkgzyjTask);
	//	solution.setRunstatus(1);
		this.jobTaskService.addJob(job);
		this.jobTaskService.runAJobNow(job);
		return job;
	//	this.tblMonitorSolutionService.modify(solution);
	}
	/**
	 * 规则管理 --执行
	 * @param
	 * @param user
	 * @throws SchedulerException 
	 */
//	public ScheduleJob gzTaskExec(List<MonitorRule> rules,Staff user,String label) throws SchedulerException{
//		ZNJKGZTask task = znjkgzTask;
//		task.setRules(rules);
//		task.setStaff(user);
//		task.setLabel(label);
//		ScheduleJob job = new ScheduleJob();
//		job.setJobGroup(ScheduleJob.GROUP_ONLY);
//		job.setJobName(label+"_gz");
//		job.setCronExpression("0 0 0 ? * *");
//		job.setJobStatus(ScheduleJob.STATUS_RUNNING);
//		job.setIsConcurrent(ScheduleJob.CONCURRENT_IS);
//		job.setTask(task);
//		this.jobTaskService.addJob(job);
//		this.jobTaskService.runAJobNow(job);
//		return job;
//	}
	
	/**
	 * 指标管理  --执行
	 * @param
	 * @param user
	 * @throws SchedulerException 
	 */
	public ScheduleJob zbTaskExec(List<Indicator> indicators, Integer source, String executeId, Staff user) throws SchedulerException{
		for (Indicator tblIndicator : indicators) {
			//tblIndicator.getTblIndicatorthresholds().size();
		}
		ZNJKZBTask task = this.znjkzbTask;
		task.setIndicators(indicators);
		task.setTblStaff(user);
		task.setSource(source);
		task.setExecuteId(executeId);
		ScheduleJob job = new ScheduleJob();
		job.setJobGroup(ScheduleJob.GROUP_ONLY);
		job.setJobName(executeId+"_zb");
		job.setCronExpression("0 0 0 ? * *");
		job.setJobStatus(ScheduleJob.STATUS_RUNNING);
		job.setIsConcurrent(ScheduleJob.CONCURRENT_IS);
		job.setTask(task);
		this.jobTaskService.addJob(job);
		this.jobTaskService.runAJobNow(job);
		return job;
	}
	
	/**
	 * 指标预警  --执行
	 * @param
	 * @param user
	 * @throws SchedulerException 
	 */
	public ScheduleJob zbyjTaskExec(List<MonitorSolution> monitorSolutions,Staff user,String label,Integer source) throws SchedulerException{
		//monitorSolution.setRunstatus(1);
		for (MonitorSolution monitorSolution : monitorSolutions) {
//			Set<Indicator> indicators = monitorSolution.getTblMonitorSolutionIndicators();
//			indicators.size();
		}
		ZNJKZBYJTask task = this.znjkzbyjTask;
		task.setMonitorSolutions(monitorSolutions);
		//task.setIndicators( monitorSolution.getTblMonitorSolutionIndicators());
		task.setTblStaff(user);
		task.setLabel(label);
		task.setSource(source);
		ScheduleJob job = new ScheduleJob();
		job.setJobGroup(ScheduleJob.GROUP_ONLY);
		job.setJobName("zhibiaoyijianyujing");
		job.setCronExpression("0 0 0 ? * *");
		job.setJobStatus(ScheduleJob.STATUS_RUNNING);
		job.setIsConcurrent(ScheduleJob.CONCURRENT_IS);
		job.setTask(task);
		this.jobTaskService.addJob(job);
		this.jobTaskService.runAJobNow(job);
		return job;
	}
	
	/**
	 * 模型管理  --执行
	 * @param
	 * @param user
	 * @throws SchedulerException 
	 */
	public ScheduleJob mxTaskExec(List<MonitorModel> models, String executeId, TblStaffUtil user) throws SchedulerException{
		ScheduleJob job = new ScheduleJob();
		ZNJKMXTask task = this.znjkmxTask;
		task.setStaff(user);
		task.setExecuteId(executeId);
		task.setTblMonitorModels(models);
		job.setIsConcurrent(ScheduleJob.CONCURRENT_IS);
		job.setJobName(executeId+"_mx");
		job.setJobGroup(ScheduleJob.STATUS_NOT_RUNNING);
		job.setJobStatus(ScheduleJob.STATUS_RUNNING);
		job.setCronExpression("0 0 0 ? * *");
		job.setTask(task);
		this.jobTaskService.addJob(job);
		this.jobTaskService.runAJobNow(job);
		return job;
	}
	
	
	/**
	 * 模型管理  --执行
	 * @param
	 * @param user
	 * @throws SchedulerException 
	 */
	public ScheduleJob mxyjTaskExec(List<MonitorSolution> modelsolutions,Staff user,String type,Integer source) throws SchedulerException{
		for (MonitorSolution monitorSolution : modelsolutions) {
//			Set<MonitorModel> models = monitorSolution.getTblMonitorSolutionModels();
//					models.size();
		}
		ScheduleJob job = new ScheduleJob();
		ZNJKMXYJTask task = this.znjkmxyjTask;
		task.setTblStaff(user);
		task.setMonitorSolutions(modelsolutions);
		task.setLable(type);
		task.setSource(source);
		//task.setModels(modelsolution.getTblMonitorSolutionModels());
		job.setIsConcurrent(ScheduleJob.CONCURRENT_IS);
		job.setJobName("mxyjyj");
		job.setJobGroup(ScheduleJob.GROUP_ONLY);
		job.setJobStatus(ScheduleJob.STATUS_RUNNING);
		job.setCronExpression("0 0 0 ? * *");
		job.setTask(task);
		this.jobTaskService.addJob(job);
		this.jobTaskService.runAJobNow(job);
		return job;
	}
}
