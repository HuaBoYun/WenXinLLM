package com.huabo.system.job;


import com.huabo.system.vo.param.AutoRefopmReminderParam;

public interface JobService {

	/**
	 * 定时器  自动催办月
	 * @param param
	 * @return
	 */
	void refopmReminderAutoMonth(AutoRefopmReminderParam param);

	/**
	 * 定时器  自动催办月
	 * @param param
	 * @return
	 */
	void refopmReminderAutoMonth1(AutoRefopmReminderParam param);
}
