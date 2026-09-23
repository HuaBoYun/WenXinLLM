package com.huabo.finance.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hbfk.util.JsonBean;
import com.huabo.finance.entity.TblSysScheduledTask;
import com.huabo.finance.service.ScheduledTaskService;
import com.huabo.finance.vo.TblSysScheduledTaskVo;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping(value = "/scheduled")
@Tag(name="财务微服务",description="财务微服务")
public class ScheduledTaskController {

	@Resource
	private ScheduledTaskService scheduledTaskService;
	
	@PostMapping(value = "/task/add",produces = "application/json; charset=utf-8")
	@Operation(summary = "定时任务-新增")
	public JsonBean task_add(HttpServletRequest request, HttpServletResponse response,TblSysScheduledTask tst) throws Exception {
		return this.scheduledTaskService.add(tst);
	}
	
	@PostMapping(value = "/task/modify",produces = "application/json; charset=utf-8")
	@Operation(summary = "定时任务-修改")
	public JsonBean task_modify(HttpServletRequest request, HttpServletResponse response,TblSysScheduledTask tst) throws Exception {
		return this.scheduledTaskService.modify(tst);
	}
	
	@GetMapping(value = "/task/remove",produces = "application/json; charset=utf-8")
	@Operation(summary = "定时任务-删除")
	public JsonBean task_remove(HttpServletRequest request, HttpServletResponse response,
			@Parameter(name="taskId",description="定时任务主键",required=true)@RequestParam(value = "taskId",required = true)String taskId) throws Exception {
		return this.scheduledTaskService.delete(taskId);
	}
	
	@GetMapping(value = "/task/detail",produces = "application/json; charset=utf-8")
	@Operation(summary = "定时任务-获取详情")
	public JsonBean task_detail(HttpServletRequest request, HttpServletResponse response,
			@Parameter(name="taskId",description="定时任务主键",required=true)@RequestParam(value = "taskId",required = true)String taskId) throws Exception {
		return this.scheduledTaskService.getOne(taskId);
	}
	
	@GetMapping(value = "/task/list",produces = "application/json; charset=utf-8")
	@Operation(summary = "定时任务-分页列表")
	public JsonBean task_list(HttpServletRequest request, HttpServletResponse response,TblSysScheduledTaskVo vo
			) throws Exception {
		return this.scheduledTaskService.getlist(vo);
	}
	
	@PostMapping(value = "/task/status",produces = "application/json; charset=utf-8")
	@Operation(summary = "定时任务-启用弃用")
	public JsonBean task_status(HttpServletRequest request, HttpServletResponse response,
			@Parameter(name="taskId",description="定时任务主键",required=true)@RequestParam(value = "taskId",required = true)String taskId,
			@Parameter(name="status",description="状态: 1启用, 0停用",required=true)@RequestParam(value = "status",required = true)Integer status
			) throws Exception {
		return this.scheduledTaskService.task_status(taskId,status);
	}
	
	
	
	
}
