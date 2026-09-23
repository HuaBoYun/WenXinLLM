package com.huabo.system.service.business.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.huabo.system.constant.YesNo;
import com.huabo.system.entity.TblSystemCustomizeScene;
import com.huabo.system.entity.TblSystemCustomizeShow;
import com.huabo.system.entity.TblSystemCustomizeShowExt;
import com.huabo.system.entity.TblSystemRight;
import com.huabo.system.exception.ServiceException;
import com.huabo.system.mapper.TblSystemCustomizeSceneMapper;
import com.huabo.system.oracle.service.TblSystemCustomizeSceneService;
import com.huabo.system.oracle.service.TblSystemCustomizeShowExtService;
import com.huabo.system.oracle.service.TblSystemCustomizeShowService;
import com.huabo.system.service.TblSystemRightService;
import com.huabo.system.service.business.SystemCustomizeService;
import com.huabo.system.utils.PageResult;
import com.huabo.system.vo.param.*;
import com.huabo.system.vo.result.TblSystemCustomizeShowExtResult;
import com.vip.vjtools.vjkit.collection.CollectionUtil;
import com.vip.vjtools.vjkit.mapper.JsonMapper;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class SystemCustomizeServiceImpl implements SystemCustomizeService {

	@Resource
	private TblSystemCustomizeSceneService customizeSceneService;
	@Resource
	private TblSystemCustomizeShowService customizeShowService;
	@Resource
	private TblSystemCustomizeShowExtService customizeShowExtService;
	@Resource
	private TblSystemRightService tblSystemRightService;

	@Resource
	private TblSystemCustomizeSceneMapper tblSystemCustomizeSceneMapper;


	/**
	 * 获取自定义场景分页列表
	 * @param param
	 * @return
	 */
	@Override
	public JsonBean getCustomizeScenePage(TblSystemCustomizeSceneQueryParam param) {
		param.setBelongGroup(null);
		param.setCreator(null);
		param.setWorkUnit(null);

		Page<TblSystemCustomizeScene> page = new Page<TblSystemCustomizeScene>(param.getPageNumber(), param.getPageSize());
		page.setOptimizeCountSql(false); // 禁用自动优化
		IPage<TblSystemCustomizeScene> pageList = tblSystemCustomizeSceneMapper.getList(page, param);
		//PageInfo<TblSystemCustomizeScene> pageInfo = tblSystemCustomizeSceneMapper.getList(param);
		if (CollectionUtil.isNotEmpty(pageList.getRecords())) {
			List<BigDecimal> catalogueIds = pageList.getRecords().stream().map(TblSystemCustomizeScene::getCatalogueId).collect(Collectors.toList());
			Map<BigDecimal, TblSystemRight> rightNamesMap = tblSystemRightService.getRightNamesMap(catalogueIds);
			pageList.getRecords().forEach(x -> {
				TblSystemRight tblSystemRight = rightNamesMap.get(x.getCatalogueId());
				x.setCatalogueName(tblSystemRight.getName());
			});
			PageResult<TblSystemCustomizeScene> build = new PageResult<TblSystemCustomizeScene>().build(pageList);
			return ResponseFormat.retParam(200, 200, build);
		}
		return ResponseFormat.retParam(200, 200, PageResult.buildNoData());
	}

	/**
	 * 自定义场景-新增或修改
	 * @param param
	 * @return
	 */
	@Override
	public JsonBean saveOrUpdateCustomizeScene(TblSystemCustomizeScene param) {
		TblSystemCustomizeScene systemCustomizeScene = customizeSceneService.saveOrUpdate(param);
		return ResponseFormat.retParam(200, 200, systemCustomizeScene);
	}

	/**
	 * 自定义场景-信息删除
	 * @param id
	 */
	@Override
	public JsonBean deleteCustomizeScene(BigDecimal id) {
		customizeSceneService.delete(id);
		return ResponseFormat.retParam(200, 200, null);
	}

	/**
	 * 根据场景ID获取自定义展示列表
	 * @param param
	 * @return
	 */
	@Override
	public JsonBean getCustomizeShowList(TblSystemCustomizeShowQueryParam param) {
		param.setBelongGroup(null);
		param.setWorkUnit(null);
		param.setCreator(null);
		List<TblSystemCustomizeShow> list = customizeShowService.getList(param);
		if (CollectionUtil.isNotEmpty(list)) {
			return ResponseFormat.retParam(200, 200, list);
		}
		return ResponseFormat.retParam(200, 200, Collections.emptyList());
	}

	/**
	 * 自定义展示-新增或修改
	 * @param param
	 * @return
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public JsonBean saveOrUpdateCustomizeShow(TblSystemCustomizeShow param) {
		//新增的时候，双写2表
		checkParameter(param);
		//新增初始表
		BigDecimal showExtId = BigDecimal.valueOf(0);
		if (param.getId() == null) {
			//新增编辑表
			TblSystemCustomizeShowExt systemCustomizeShowExt = new TblSystemCustomizeShowExt();
			BeanUtils.copyProperties(param, systemCustomizeShowExt);
			customizeShowExtService.saveOrUpdate(systemCustomizeShowExt);
			showExtId = systemCustomizeShowExt.getId();
		} else { //需要同步扩展字段
			TblSystemCustomizeShowExt ext = customizeShowExtService.findInfo(param.getField(), param.getSceneCode());
			if (Objects.nonNull(ext)) {
				TblSystemCustomizeShowExt systemCustomizeShowExt = new TblSystemCustomizeShowExt();
				systemCustomizeShowExt.setId(ext.getId());
				systemCustomizeShowExt.setContentJson(param.getContentJson());
				customizeShowExtService.saveOrUpdate(systemCustomizeShowExt);
			}
		}
		if (showExtId != null) {
			param.setShowExtId(showExtId);
		}
		TblSystemCustomizeShow systemCustomizeShow = customizeShowService.saveOrUpdate(param);
		return ResponseFormat.retParam(200, 200, systemCustomizeShow);
	}

	/**
	 * 参数处理
	 * @param param
	 */
	private void checkParameter(TblSystemCustomizeShow param) {
		//系统必填 = 字段必填
		if (param.getIsSystemRequired() == YesNo.YES) {
			param.setIsRequired(YesNo.YES);
		}
		if (CollectionUtil.isNotEmpty(param.getPullDownExtList())) {
			String extJson = JsonMapper.INSTANCE.toJson(param.getPullDownExtList());
			param.setExtJson(extJson);
		}
	}

	/**
	 * 自定义展示-信息删除
	 * @param id
	 * @return
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public JsonBean deleteCustomizeShow(BigDecimal id) {
		TblSystemCustomizeShow customizeShow = customizeShowService.findById(id);
		//删除初始表
		customizeShowService.delete(id);
		//删除编辑表
		if (customizeShow.getShowExtId() != null) {
			customizeShowExtService.delete(customizeShow.getShowExtId());
		}
		return ResponseFormat.retParam(200, 200, null);
	}

	/**
	 * 自定义展示(编辑模块) 列表
	 * @param param
	 * @return
	 */
	@Override
	public JsonBean getCustomizeShowExtList(TblSystemCustomizeShowExtQueryParam param) {
		param.setWorkUnit(null);
		param.setCreator(null);
		List<TblSystemCustomizeShowExt> list = customizeShowExtService.getList(param);
		if (CollectionUtil.isNotEmpty(list)) {
			return ResponseFormat.retParam(200, 200, list);
		}
		return ResponseFormat.retParam(200, 200, Collections.emptyList());
	}

	/**
	 * 自定义展示(编辑模块)-新增或修改
	 * @param param
	 * @return
	 */
	@Override
	public JsonBean saveOrUpdateCustomizeShowExt(TblSystemCustomizeShowExt param) {
		TblSystemCustomizeShowExt systemCustomizeShowExt = customizeShowExtService.saveOrUpdate(param);
		return ResponseFormat.retParam(200, 200, systemCustomizeShowExt);
	}

	/**
	 * 自定义场景-信息
	 * @param id
	 * @return
	 */
	@Override
	public JsonBean getCustomizeScene(BigDecimal id) {
		TblSystemCustomizeScene systemCustomizeScene = customizeSceneService.findById(id);
		return ResponseFormat.retParam(200, 200, systemCustomizeScene);
	}

	/**
	 * 自定义展示-信息
	 * @param id
	 * @return
	 */
	@Override
	public JsonBean getCustomizeShow(BigDecimal id) {
		TblSystemCustomizeShow systemCustomizeShow = customizeShowService.findById(id);
		return ResponseFormat.retParam(200, 200, systemCustomizeShow);
	}

	/**
	 * 自定义展示(编辑模块)-信息
	 * @param id
	 * @return
	 */
	@Override
	public JsonBean getCustomizeShowExt(BigDecimal id) {
		TblSystemCustomizeShowExt systemCustomizeShowExt = customizeShowExtService.findById(id);
		return ResponseFormat.retParam(200, 200, systemCustomizeShowExt);
	}

	/**
	 * 自定义场景-状态变更
	 * @param param
	 * @return
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public JsonBean updateStateCustomizeScene(TblSystemCustomizeSceneParam param) {
		//场景-状态变更
		TblSystemCustomizeScene model = new TblSystemCustomizeScene();
		model.setId(param.getId());
		model.setState(param.getState());
		customizeSceneService.saveOrUpdate(model);

		//展示(编辑模块)-状态变更
		customizeShowExtService.updateStateSceneId(param.getId(), param.getState());
		return ResponseFormat.retParam(200, 200, null);
	}

	/**
	 * 自定义展示(编辑模块)-状态变更
	 * @param param
	 * @return
	 */
	@Override
	public JsonBean updateStateCustomizeSceneExt(TblSystemCustomizeSceneExtParam param) {
		customizeShowExtService.updateStateId(param.getId(), param.getState());
		return ResponseFormat.retParam(200, 200, null);
	}

	/**
	 * 预览展示-详情
	 * @param param
	 * @return
	 */
	@Override
	public JsonBean getCustomizeShowExtPreviewDetails(CustomizeShowExtPreviewDetailsQueryParam param) {
		List<TblSystemCustomizeShowExtResult> result = customizeShowExtService.getCustomizeShowExtPreviewDetails(param);
		return ResponseFormat.retParam(200, 200, result);
	}

	/**
	 * 预览展示-列表
	 * @param param
	 * @return
	 */
	@Override
	public JsonBean getCustomizeShowExtPreviewList(CustomizeShowExtPreviewDetailsQueryParam param) {
		List<TblSystemCustomizeShowExtResult> result = customizeShowExtService.getCustomizeShowExtPreviewList(param);
		return ResponseFormat.retParam(200, 200, result);
	}

	/**
	 * 自定义展示(编辑模块)-批量更新
	 * @param param
	 * @return
	 */
	@Override
	public JsonBean batchUpdateCustomizeShowExt(BatchUpdateCustomizeShowExtParam param) {
		customizeShowExtService.batchUpdateCustomizeShowExt(param);
		return ResponseFormat.retParam(200, 200, null);
	}

	/**
	 * 自定义展示-批量新增或修改
	 * @param param
	 * @return
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public JsonBean savesOrUpdatesCustomizeShow(List<TblSystemCustomizeShow> param) {
		if (CollectionUtil.isEmpty(param)) {
			throw new ServiceException("数组不能为空");
		}
		param.forEach(item -> {
			if (StringUtils.isBlank(item.getField())) {
				throw new ServiceException("field 字段 不能为空");
			}
			if (Objects.isNull(item.getSort())) {
				throw new ServiceException("sort 排序(详细) 不能为空");
			}
			if (Objects.isNull(item.getListSort())) {
				throw new ServiceException("listSort 排序(列表) 不能为空");
			}
			if (Objects.isNull(item.getIsSystemRequired())) {
				throw new ServiceException("isSystemRequired 是否系统必填 0-不必填 1-必填 不能为空");
			}
			if (Objects.isNull(item.getSceneId())) {
				throw new ServiceException("sceneId 关联ID-自定义场景关联ID 不能为空");
			}
			if (StringUtils.isBlank(item.getSceneCode())) {
				throw new ServiceException("sceneCode 场景唯一编码 不能为空");
			}
		});
		param.forEach(this::saveOrUpdateCustomizeShow);
		return ResponseFormat.retParam(200, 200, null);
	}

	/**
	 * 自定义展示(编辑模块)-批量新增或修改
	 * @param param
	 * @return
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public JsonBean savesOrUpdatesCustomizeShowExt(List<TblSystemCustomizeShowExt> param) {
		if (CollectionUtil.isEmpty(param)) {
			throw new ServiceException("数组不能为空");
		}
		param.forEach(this::saveOrUpdateCustomizeShowExt);
		return ResponseFormat.retParam(200, 200, null);
	}
}
