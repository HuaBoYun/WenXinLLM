package com.huabo.system.oracle.service;

import com.github.pagehelper.PageInfo;
import com.huabo.system.entity.TblSystemRefopmReminder;
import com.huabo.system.vo.param.TblSystemRefopmReminderQueryParam;

import java.util.List;

public interface TblSystemRefopmReminderService {

	/**
	 * 催办关系表 列表查询
	 * @param param
	 * @return
	 */
	PageInfo<TblSystemRefopmReminder> getList(TblSystemRefopmReminderQueryParam param);

	/**
	 * 催办关系表 新增/更新
	 * @param param
	 * @return
	 */
	TblSystemRefopmReminder saveOrUpdate(TblSystemRefopmReminder param);

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
	TblSystemRefopmReminder findById(Long id);

	/**
	 * 自动催发列表
	 * @return
	 */
	List<TblSystemRefopmReminder> getAutoList();

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
}

