package com.huabo.system.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.huabo.system.oracle.service.ScheduledTaskService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

/**
 * 定时任务Controller
 * @author 14218
 *
 */
@RestController
@Slf4j
@Tag(name = "定时任务Controller", description = "定时任务Controller")
@RequestMapping("/scheduled")
public class ScheduledTaskController {

	@Resource
	private ScheduledTaskService scheduledTaskService;

    @PostMapping(value = "/setCurrentDataRemind", produces = "application/json; charset=utf-8")
    @Operation(summary="设置当天需要提醒的数据并放到Redis中")
    public JsonBean setCurrentDataRemind(HttpServletRequest request) throws Exception {
    	try {
			log.info("设置当天需要提醒的数据");
			return this.scheduledTaskService.setCurrentContractDataRemind();
		} catch (Exception e) {
			log.error("获取用户信息失败："+e.getMessage());
			return ResponseFormat.retParam(0,60001,e.getMessage());
		}
    }
    
    
    @GetMapping(value = "/getRemindInfoList", produces = "application/json; charset=utf-8")
    @Operation(summary="获取当前需要办理的预警消息")
    public JsonBean getRemindInfoListAll(HttpServletRequest request,
    		@Parameter(name="token",description="登录用户token",required=true) @RequestHeader("token")String token) throws Exception {
    	try {
			log.info("获取当天需要提醒的数据");
			return this.scheduledTaskService.getRemindInfoListAll(token);
		} catch (Exception e) {
			log.error("获取用户信息失败："+e.getMessage());
			return ResponseFormat.retParam(0,60001,e.getMessage());
		}
    }
}
