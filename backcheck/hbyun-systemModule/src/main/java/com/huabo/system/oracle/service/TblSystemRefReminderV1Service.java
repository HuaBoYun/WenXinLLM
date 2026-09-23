package com.huabo.system.oracle.service;

import com.github.pagehelper.PageInfo;
import com.huabo.system.entity.TblSystemRefReminderV;
import com.huabo.system.vo.param.TblSystemRefReminderQueryParam;

import java.util.List;

public interface TblSystemRefReminderV1Service {

	/**
	 * 催办关系表 列表查询
	 * @param param
	 * @return
	 */
	PageInfo<TblSystemRefReminderV> getList(TblSystemRefReminderQueryParam param);

	/**
	 * 催办关系表 新增/更新
	 * @param param
	 * @return
	 */
	TblSystemRefReminderV saveOrUpdate(TblSystemRefReminderV param, String token);

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
	TblSystemRefReminderV findById(Long id);

	/**
	 * 查询
	 * @param reminderBusinessTable
	 * @param id
	 * @return
	 */
	List<TblSystemRefReminderV> findList(String reminderBusinessTable, Long id);
}
