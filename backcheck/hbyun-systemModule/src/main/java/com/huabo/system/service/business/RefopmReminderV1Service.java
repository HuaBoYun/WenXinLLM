package com.huabo.system.service.business;

import com.hbfk.entity.TblStaffUtil;
import com.huabo.system.entity.TblSystemRefopmReminderV;
import com.huabo.system.utils.MyJsonBean;
import com.huabo.system.vo.param.TblSystemRefopmReminderQueryParam;

public interface RefopmReminderV1Service {

	/**
	 * 催办关系 列表查询
	 * @param param
	 * @return
	 */
	MyJsonBean<TblSystemRefopmReminderV> getRefopmReminderList(TblSystemRefopmReminderQueryParam param);

	/**
	 * 催办关系 新增/更新
	 * @param param
	 * @return
	 */
	MyJsonBean<TblSystemRefopmReminderV> saveOrUpdateRefopmReminder(TblSystemRefopmReminderV param);

	/**
	 * 催办关系 刪除
	 * @param id
	 * @return
	 */
	MyJsonBean<Void> deleteRefopmReminder(Long id);

	/**
	 * 催办关系 详情 查询
	 * @param id
	 * @return
	 */
	MyJsonBean<TblSystemRefopmReminderV> getRefopmReminder(Long id);

	/**
	 * 催办管理 催办
	 * @param id
	 */
	MyJsonBean<Void> reminder(TblStaffUtil loginStaff, Long id, String token);

	/**
	 * 催办管理 停止催办
	 * @param id
	 * @return
	 */
	MyJsonBean<Void> isStopReminder(Long id);

	/**
	 * 催办管理 启动催办
	 * @param id
	 * @return
	 */
	MyJsonBean<Void> isStartReminder(Long id);
}
