package com.huabo.system.oracle.service;

import java.math.BigDecimal;

import com.github.pagehelper.PageInfo;
import com.huabo.system.entity.TblSystemCustomizeScene;
import com.huabo.system.vo.param.TblSystemCustomizeSceneQueryParam;

public interface TblSystemCustomizeSceneService {

	/**
	 * 自定义场景列表 查询
	 * @param param
	 * @return
	 */
	PageInfo<TblSystemCustomizeScene> getList(TblSystemCustomizeSceneQueryParam param);

	/**
	 * 自定义场景 新增/更新
	 * @param param
	 * @return
	 */
	TblSystemCustomizeScene saveOrUpdate(TblSystemCustomizeScene param);

	/**
	 * 自定义场景详情 查询
	 * @param id
	 * @return
	 */
	TblSystemCustomizeScene findById(BigDecimal id);

	/**
	 * 自定义场景 刪除
	 * @param id
	 */
	void delete(BigDecimal id);
}
