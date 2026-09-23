package com.huabo.system.oracle.service;

import com.github.pagehelper.PageInfo;
import com.huabo.system.entity.TblSystemRefReminderAccess;
import com.huabo.system.vo.param.TblSystemRefReminderAccessQueryParam;

public interface TblSystemRefReminderAccessService {

	/**
	 * 催办关系表 列表查询
	 * @param param
	 * @return
	 */
	PageInfo<TblSystemRefReminderAccess> getList(TblSystemRefReminderAccessQueryParam param);

	/**
	 * 催办关系表 新增/更新
	 * @param param
	 * @return
	 */
	TblSystemRefReminderAccess saveOrUpdate(TblSystemRefReminderAccess param);

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
	TblSystemRefReminderAccess findById(Long id);
}
