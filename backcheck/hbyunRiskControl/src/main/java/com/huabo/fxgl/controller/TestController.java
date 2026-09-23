package com.huabo.fxgl.controller;


import javax.annotation.Resource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.huabo.fxgl.job.JobService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

/**
 * 测试控制器
 * <p>风险管控模块的测试接口，仅用于开发调试</p>
 *
 * @author hbyun
 */
@RequestMapping(value = "/test")
@RestController
@Tag(name="测试",description="测试")
@Slf4j
public class TestController {

	@Resource
	private JobService jobService;

	@GetMapping(value = "/test1")
	@Operation(summary = "测试111")
	public void test() {
		jobService.reminder();
	}

	@GetMapping(value = "/test2")
	@Operation(summary = "测试112")
	public void test2() {
		jobService.reminderMonthlyEvaluateContent();
	}
}
