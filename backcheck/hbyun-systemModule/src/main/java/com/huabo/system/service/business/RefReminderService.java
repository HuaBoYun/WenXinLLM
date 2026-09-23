package com.huabo.system.service.business;

import com.hbfk.entity.TblStaffUtil;
import com.huabo.system.entity.TblSystemRefReminder;
import com.huabo.system.entity.TblSystemRefReminderAccess;
import com.huabo.system.utils.MyJsonBean;
import com.huabo.system.vo.param.TblSystemRefReminderAccessQueryParam;
import com.huabo.system.vo.param.TblSystemRefReminderQueryParam;

public interface RefReminderService {

	/**
	 * 催办信息 列表查询
	 * @param param
	 * @return
	 */
	MyJsonBean<TblSystemRefReminder> getRefReminderList(TblSystemRefReminderQueryParam param);

	/**
	 * 催办信息 浏览
	 * @param loginStaff
	 * @return
	 */
	MyJsonBean<Void> isRead(TblStaffUtil loginStaff, Long id, String token);

	/**
	 * 催办访问记录 列表查询
	 * @param param
	 * @return
	 */
	MyJsonBean<TblSystemRefReminderAccess> getRefReminderAccessList(TblSystemRefReminderAccessQueryParam param);

	/**
	 * 催办信息 详情 查询
	 * @param id
	 * @return
	 */
	MyJsonBean<TblSystemRefReminder> getRefReminder(TblStaffUtil loginStaff, Long id, String token);
}
