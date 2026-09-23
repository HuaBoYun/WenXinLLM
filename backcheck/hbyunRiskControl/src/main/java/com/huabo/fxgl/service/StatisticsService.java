package com.huabo.fxgl.service;

import com.huabo.fxgl.util.MyJsonBean;
import com.huabo.fxgl.vo.StatisticsReminderMonthlyPenetrateResult;
import com.huabo.fxgl.vo.StatisticsReminderParam;
import com.huabo.fxgl.vo.StatisticsReminderResult;

import java.util.List;

public interface StatisticsService {

	/**
	 * 催办统计
	 * @param param
	 * @return
	 */
	MyJsonBean<StatisticsReminderResult> getStatisticsReminderList(StatisticsReminderParam param);

	/**
	 * 催办统计-穿透-月度评估
	 * @param type 1-催办总数量 2-完成数量 3-未完成数量 4-超期数量
	 * @return
	 */
	MyJsonBean<List<StatisticsReminderMonthlyPenetrateResult>> getStatisticsReminderMonthlyPenetrateList(Integer type);

	/**
	 * 催办统计-穿透-一体化管控措施
	 * @param type 1-催办总数量 2-完成数量 3-未完成数量 4-超期数量
	 * @return
	 */
	MyJsonBean<List<StatisticsReminderMonthlyPenetrateResult>> getStatisticsReminderControlPenetrateList(Integer type);
}
