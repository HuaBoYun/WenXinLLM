package com.huabo.system.oracle.service;

import java.math.BigDecimal;
import java.util.List;

import com.huabo.system.entity.TblSystemCustomizeShow;
import com.huabo.system.vo.param.TblSystemCustomizeShowQueryParam;

public interface TblSystemCustomizeShowService {

	/**
	 * 自定义展示列表 查询
	 * @param param
	 * @return
	 */
	List<TblSystemCustomizeShow> getList(TblSystemCustomizeShowQueryParam param);

	/**
	 * 自定义展示 新增/更新
	 * @param param
	 * @return
	 */
	TblSystemCustomizeShow saveOrUpdate(TblSystemCustomizeShow param);

	/**
	 * 自定义展示详情 查询
	 * @param id
	 * @return
	 */
	TblSystemCustomizeShow findById(BigDecimal id);

	/**
	 * 自定义展示 刪除
	 * @param id
	 */
	void delete(BigDecimal id);

	/**
	 * 自定义展示-状态变更
	 * @param sceneId
	 * @param state
	 */
	void updateState(BigDecimal sceneId, Integer state);
}
