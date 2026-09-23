package com.huabo.fxgl.job;


public interface JobService {

	/**
	 * 自动催办触发（重大风险-月度评估-一体化管控措施 提前3天内容 催办提醒）
	 * TBL_CONTROL_ENTRIES
	 * @return
	 */
	void reminder();

	/**
	 * 自动催办触发（重大风险-月度评估 催办提醒）
	 */
	void reminderMonthlyEvaluateContent();
}
