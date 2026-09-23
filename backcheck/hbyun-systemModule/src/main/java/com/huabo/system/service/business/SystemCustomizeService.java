package com.huabo.system.service.business;

import java.math.BigDecimal;
import java.util.List;

import com.hbfk.util.JsonBean;
import com.huabo.system.entity.TblSystemCustomizeScene;
import com.huabo.system.entity.TblSystemCustomizeShow;
import com.huabo.system.entity.TblSystemCustomizeShowExt;
import com.huabo.system.vo.param.*;

public interface SystemCustomizeService {

	/**
	 * 获取自定义场景分页列表
	 * @param param
	 * @return
	 */
	JsonBean getCustomizeScenePage(TblSystemCustomizeSceneQueryParam param);

	/**
	 * 自定义场景-新增或修改
	 * @param param
	 * @return
	 */
	JsonBean saveOrUpdateCustomizeScene(TblSystemCustomizeScene param);

	/**
	 * 自定义场景-信息删除
	 * @param id
	 */
	JsonBean deleteCustomizeScene(BigDecimal id);

	/**
	 * 自定义展示列表
	 * @param param
	 * @return
	 */
	JsonBean getCustomizeShowList(TblSystemCustomizeShowQueryParam param);

	/**
	 * 自定义展示-新增或修改
	 * @param param
	 * @return
	 */
	JsonBean saveOrUpdateCustomizeShow(TblSystemCustomizeShow param);

	/**
	 * 自定义展示-信息删除
	 * @param id
	 * @return
	 */
	JsonBean deleteCustomizeShow(BigDecimal id);

	/**
	 * 自定义展示(编辑模块) 列表
	 * @param param
	 * @return
	 */
	JsonBean getCustomizeShowExtList(TblSystemCustomizeShowExtQueryParam param);

	/**
	 * 自定义展示(编辑模块)-新增或修改
	 * @param param
	 * @return
	 */
	JsonBean saveOrUpdateCustomizeShowExt(TblSystemCustomizeShowExt param);

	/**
	 * 自定义场景-信息
	 * @param id
	 * @return
	 */
	JsonBean getCustomizeScene(BigDecimal id);

	/**
	 * 自定义展示-信息
	 * @param id
	 * @return
	 */
	JsonBean getCustomizeShow(BigDecimal id);

	/**
	 * 自定义展示(编辑模块)-信息
	 * @param id
	 * @return
	 */
	JsonBean getCustomizeShowExt(BigDecimal id);

	/**
	 * 自定义场景-状态变更
	 * @param param
	 * @return
	 */
	JsonBean updateStateCustomizeScene(TblSystemCustomizeSceneParam param);

	/**
	 * 自定义展示(编辑模块)-状态变更
	 * @param param
	 * @return
	 */
	JsonBean updateStateCustomizeSceneExt(TblSystemCustomizeSceneExtParam param);

	/**
	 * 预览展示-详情
	 * @param param
	 * @return
	 */
    JsonBean getCustomizeShowExtPreviewDetails(CustomizeShowExtPreviewDetailsQueryParam param);

	/**
	 * 预览展示-列表
	 * @param param
	 * @return
	 */
	JsonBean getCustomizeShowExtPreviewList(CustomizeShowExtPreviewDetailsQueryParam param);

	/**
	 * 自定义展示(编辑模块)-批量更新
	 * @param param
	 * @return
	 */
	JsonBean batchUpdateCustomizeShowExt(BatchUpdateCustomizeShowExtParam param);

	/**
	 * 自定义展示-批量新增或修改
	 * @param param
	 * @return
	 */
	JsonBean savesOrUpdatesCustomizeShow(List<TblSystemCustomizeShow> param);

	/**
	 * 自定义展示(编辑模块)-批量新增或修改
	 * @param param
	 * @return
	 */
	JsonBean savesOrUpdatesCustomizeShowExt(List<TblSystemCustomizeShowExt> param);
}
