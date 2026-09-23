package com.huabo.system.oracle.service;

import java.util.List;

import com.huabo.system.entity.TblSystemCustomizeTable;

public interface TblSystemCustomizeTableService {

	/**
	 * 自定义表映射列表 查询
	 * @return
	 */
	List<TblSystemCustomizeTable> getList();

	/**
	 * 自定义表映射 新增/更新
	 * @param param
	 * @return
	 */
	TblSystemCustomizeTable saveOrUpdate(TblSystemCustomizeTable param);

	/**
	 * 自定义表映射详情 查询
	 * @param id
	 * @return
	 */
	TblSystemCustomizeTable findById(Integer id);

	/**
	 * 自定义表映射 刪除
	 * @param id
	 */
	void delete(Integer id);
}
