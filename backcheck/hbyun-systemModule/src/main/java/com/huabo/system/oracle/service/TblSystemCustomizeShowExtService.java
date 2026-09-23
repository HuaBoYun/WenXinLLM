package com.huabo.system.oracle.service;

import com.huabo.system.entity.TblSystemCustomizeShowExt;
import com.huabo.system.vo.param.BatchUpdateCustomizeShowExtParam;
import com.huabo.system.vo.param.CustomizeShowExtPreviewDetailsQueryParam;
import com.huabo.system.vo.param.TblSystemCustomizeShowExtQueryParam;
import com.huabo.system.vo.result.TblSystemCustomizeShowExtResult;

import java.math.BigDecimal;
import java.util.List;

public interface TblSystemCustomizeShowExtService {

	/**
	 * 自定义展示-编辑列表 查询
	 * @param param
	 * @return
	 */
	List<TblSystemCustomizeShowExt> getList(TblSystemCustomizeShowExtQueryParam param);

	/**
	 * 自定义展示-编辑 新增/更新
	 * @param param
	 * @return
	 */
	TblSystemCustomizeShowExt saveOrUpdate(TblSystemCustomizeShowExt param);

	/**
	 * 自定义展示-编辑详情 查询
	 * @param id
	 * @return
	 */
	TblSystemCustomizeShowExt findById(BigDecimal id);

	/**
	 * 自定义展示-编辑 刪除
	 * @param id
	 */
	void delete(BigDecimal id);

	/**
	 * 自定义展示(编辑模块)-状态变更
	 * @param sceneId
	 * @param state
	 */
	void updateStateSceneId(BigDecimal sceneId, Integer state);

	/**
	 * 自定义展示(编辑模块)-状态变更
	 * @param id
	 * @param state
	 */
	void updateStateId(BigDecimal id, Integer state);

	/**
	 * 预览展示-详情
	 * @param param
	 * @return
	 */
	List<TblSystemCustomizeShowExtResult> getCustomizeShowExtPreviewDetails(CustomizeShowExtPreviewDetailsQueryParam param);

	/**
	 * 预览展示-列表
	 * @param param
	 * @return
	 */
	List<TblSystemCustomizeShowExtResult> getCustomizeShowExtPreviewList(CustomizeShowExtPreviewDetailsQueryParam param);

	/**
	 * 自定义展示(编辑模块)-批量更新
	 * @param param
	 */
	void batchUpdateCustomizeShowExt(BatchUpdateCustomizeShowExtParam param);

	/**
	 * 查询扩展信息
	 * @param field
	 * @param sceneCode
	 * @return
	 */
	TblSystemCustomizeShowExt findInfo(String field, String sceneCode);
}
