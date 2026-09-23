package com.huabo.fxgl.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.huabo.fxgl.service.StatisticsService;
import com.huabo.fxgl.util.MyJsonBean;
import com.huabo.fxgl.vo.StatisticsReminderMonthlyPenetrateResult;
import com.huabo.fxgl.vo.StatisticsReminderParam;
import com.huabo.fxgl.vo.StatisticsReminderResult;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;


/**
 * 催办统计控制器
 * <p>提供风险管控模块催办信息的统计查询接口</p>
 *
 * @author hbyun
 */
@RestController
@Tag(name="催办统计",description="催办统计")
@Slf4j
public class StatisticsController {

	@Resource
	private StatisticsService statisticsService;

	@Operation(summary = "催办统计")
	@PostMapping("/statistics/reminder/getList")
	public MyJsonBean<StatisticsReminderResult> getStatisticsReminderList(@RequestBody StatisticsReminderParam param) {
		MyJsonBean<StatisticsReminderResult> myJsonBean = null;
		try {
			myJsonBean = statisticsService.getStatisticsReminderList(param);
		} catch (Exception e) {
			log.error("催办统计 ...接口 异常", e);
		}
		return myJsonBean;
	}

	@Operation(summary = "催办统计-穿透-月度评估")
	@GetMapping("/statistics/reminder/monthly-penetrate/getList")
	public MyJsonBean<List<StatisticsReminderMonthlyPenetrateResult>> getStatisticsReminderMonthlyPenetrateList(Integer type) {
		MyJsonBean<List<StatisticsReminderMonthlyPenetrateResult>> myJsonBean = null;
		try {
			//type 1-催办总数量 2-完成数量 3-未完成数量 4-超期数量
			myJsonBean = statisticsService.getStatisticsReminderMonthlyPenetrateList(type);
		} catch (Exception e) {
			log.error("催办统计-穿透-月度评估 异常", e);
		}
		return myJsonBean;
	}

	@Operation(summary = "催办统计-穿透-一体化管控措施")
	@GetMapping("/statistics/reminder/control-penetrate/getList")
	public MyJsonBean<List<StatisticsReminderMonthlyPenetrateResult>> getStatisticsReminderControlPenetrateList(Integer type) {
		MyJsonBean<List<StatisticsReminderMonthlyPenetrateResult>> myJsonBean = null;
		try {
			//type 1-催办总数量 2-完成数量 3-未完成数量 4-超期数量
			myJsonBean = statisticsService.getStatisticsReminderControlPenetrateList(type);
		} catch (Exception e) {
			log.error("催办统计-穿透-月度评估 异常", e);
		}
		return myJsonBean;
	}
}
