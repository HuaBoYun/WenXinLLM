package com.huabo.system.oracle.service;

import com.github.pagehelper.PageInfo;
import com.huabo.system.entity.TblSystemRefReminder;
import com.huabo.system.vo.param.TblSystemRefReminderQueryParam;

public interface TblSystemRefReminderService {

	/**
	 * 催办关系表 列表查询
	 * @param param
	 * @return
	 */
	PageInfo<TblSystemRefReminder> getList(TblSystemRefReminderQueryParam param);

	/**
	 * 催办关系表 新增/更新
	 * @param param
	 * @return
	 */
	TblSystemRefReminder saveOrUpdate(TblSystemRefReminder param, String token);

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
	TblSystemRefReminder findById(Long id);

}
