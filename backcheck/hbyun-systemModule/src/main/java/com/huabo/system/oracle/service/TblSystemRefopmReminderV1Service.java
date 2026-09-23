package com.huabo.system.oracle.service;

import com.github.pagehelper.PageInfo;
import com.huabo.system.entity.TblSystemRefopmReminderV;
import com.huabo.system.vo.param.TblSystemRefopmReminderQueryParam;
import com.huabo.system.vo.result.RefReminderListResult;

import java.util.List;

public interface TblSystemRefopmReminderV1Service {

	/**
	 * 催办关系表 列表查询
	 * @param param
	 * @return
	 */
	PageInfo<TblSystemRefopmReminderV> getList(TblSystemRefopmReminderQueryParam param);

	/**
	 * 催办关系表 新增/更新
	 * @param param
	 * @return
	 */
	TblSystemRefopmReminderV saveOrUpdate(TblSystemRefopmReminderV param);

	/**
	 * 催办关系表 删除
	 * @param id
	 */
	void delete(Long id);

	/**
	 * 催办关系表 详情查询
	 * @param id
	 * @return
	 */
	TblSystemRefopmReminderV findById(Long id);

	/**
	 * 自动催发列表
	 * @return
	 */
	List<TblSystemRefopmReminderV> getAutoList();

	/**
	 * 根据整改ID停止自动催办
	 * @param refopmId
	 */
	void stopReminder(Long refopmId);

	/**
	 * 停止催办
	 * @param id
	 */
	void isStopReminder(Long id);

	/**
	 * 统计数量
	 * @param param
	 * @return
	 */
	Integer count(TblSystemRefopmReminderV param);

	/**
	 * 业务查询
	 * @param reminderBusinessTable
	 * @param reminderBusinessStaff
	 * @param reminderBusinessTime
	 * @param reminderBusinessContent
	 * @return
	 */
	List<RefReminderListResult> getBusinessList(String reminderBusinessTable, String reminderBusinessId, String reminderBusinessStaff,
			String reminderBusinessTime, String reminderBusinessContent);

	/**
	 * 启动催办
	 * @param id
	 */
	void isStartReminder(Long id);
}

