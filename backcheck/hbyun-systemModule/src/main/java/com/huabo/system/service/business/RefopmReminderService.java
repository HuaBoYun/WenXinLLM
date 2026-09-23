package com.huabo.system.service.business;

import com.hbfk.entity.TblStaffUtil;
import com.huabo.system.entity.TblSystemRefopmReminder;
import com.huabo.system.utils.MyJsonBean;
import com.huabo.system.vo.param.TblSystemRefopmReminderQueryParam;

public interface RefopmReminderService {

	/**
	 * 催办关系 列表查询
	 * @param param
	 * @return
	 */
	MyJsonBean<TblSystemRefopmReminder> getRefopmReminderList(TblSystemRefopmReminderQueryParam param);

	/**
	 * 催办关系 新增/更新
	 * @param param
	 * @return
	 */
	MyJsonBean<TblSystemRefopmReminder> saveOrUpdateRefopmReminder(TblSystemRefopmReminder param);

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
	MyJsonBean<TblSystemRefopmReminder> getRefopmReminder(Long id);

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
}
