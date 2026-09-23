package com.huabo.system.oracle.service.impl;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hbfk.util.redis.Random.RandomUtil;
import com.huabo.system.constant.YesNo;
import com.huabo.system.entity.TblSystemCustomizeShowExt;
import com.huabo.system.exception.ServiceException;
import com.huabo.system.mapper.TblSystemCustomizeShowExtMapper;
import com.huabo.system.oracle.service.TblSystemCustomizeShowExtService;
import com.huabo.system.vo.param.BatchUpdateCustomizeShowExtParam;
import com.huabo.system.vo.param.CustomizeShowExtPreviewDetailsQueryParam;
import com.huabo.system.vo.param.TblSystemCustomizeShowExtQueryParam;
import com.huabo.system.vo.result.TblSystemCustomizeShowExtResult;
import com.vip.vjtools.vjkit.collection.CollectionUtil;

import tk.mybatis.mapper.entity.Example;

@Service
public class TblSystemCustomizeShowExtServiceImpl implements TblSystemCustomizeShowExtService {

	@Resource
	private TblSystemCustomizeShowExtMapper tblSystemCustomizeShowExtMapper;

	/**
	 * 自定义展示-编辑列表 查询
	 * @param param
	 * @return
	 */
	@Override
	public List<TblSystemCustomizeShowExt> getList(TblSystemCustomizeShowExtQueryParam param) {
		QueryWrapper<TblSystemCustomizeShowExt> wapper = new QueryWrapper<TblSystemCustomizeShowExt>();
		if (param.getSceneId() != null) {
			wapper.eq("SCENEID", param.getSceneId());
		}
		if (StringUtils.isNotBlank(param.getSceneCode())) {
			wapper.eq("SCENECODE", param.getSceneCode());
		}
		if (param.getBelongGroup() != null) {
			wapper.eq("BELONGGROUP", param.getBelongGroup());
		}
		wapper.orderByAsc("SORT");
		wapper.orderByDesc("ID");
		List<TblSystemCustomizeShowExt> customizeShowExtList = tblSystemCustomizeShowExtMapper.selectList(wapper);
		//pullDownExtList 置空 前端自行extJson进行渲染
		customizeShowExtList.forEach(x -> x.setPullDownExtList(null));
		return customizeShowExtList;
	}

	/**
	 * 自定义展示-编辑 新增/更新
	 * @param param
	 * @return
	 */
	@Override
	public TblSystemCustomizeShowExt saveOrUpdate(TblSystemCustomizeShowExt param) {
		Date now = new Date();
		if (param.getId() == null) {
			param.setState(YesNo.NO);
			param.setCreatedTime(now);
			param.setUpdatedTime(now);
			param.setId(RandomUtil.uuBigDecimalId());
			tblSystemCustomizeShowExtMapper.insert(param);
		} else {
			param.setUpdatedTime(now);
			tblSystemCustomizeShowExtMapper.updateById(param);
		}
		return findById(param.getId());
	}

	/**
	 * 自定义展示-编辑详情 查询
	 * @param id
	 * @return
	 */
	@Override
	public TblSystemCustomizeShowExt findById(BigDecimal id) {
		TblSystemCustomizeShowExt systemCustomizeSceneExt = tblSystemCustomizeShowExtMapper.selectById(id);
		if (systemCustomizeSceneExt == null) {
			throw new ServiceException(400, 50001);
		}
		return systemCustomizeSceneExt;
	}

	/**
	 * 自定义展示-编辑 刪除
	 * @param id
	 */
	@Override
	public void delete(BigDecimal id) {
		tblSystemCustomizeShowExtMapper.deleteById(id);
	}

	/**
	 * 自定义展示(编辑模块)-状态变更
	 * @param sceneId
	 * @param state
	 */
	@Override
	public void updateStateSceneId(BigDecimal sceneId, Integer state) {
		TblSystemCustomizeShowExt update = new TblSystemCustomizeShowExt();
		update.setState(state);
		tblSystemCustomizeShowExtMapper.updateById(update);
	}

	/**
	 * 自定义展示(编辑模块)-状态变更
	 * @param id
	 * @param state
	 */
	@Override
	public void updateStateId(BigDecimal id, Integer state) {
		tblSystemCustomizeShowExtMapper.updateById(TblSystemCustomizeShowExt.ofState(id, state));
	}

	/**
	 * 预览展示-详情
	 * @param param
	 * @return
	 */
	@Override
	public List<TblSystemCustomizeShowExtResult> getCustomizeShowExtPreviewDetails(CustomizeShowExtPreviewDetailsQueryParam param) {
		List<TblSystemCustomizeShowExtResult> customizeShowExtPreview = tblSystemCustomizeShowExtMapper.getCustomizeShowExtPreview(param);
		if (CollectionUtil.isEmpty(customizeShowExtPreview)) {
			return Collections.emptyList();
		}
		return customizeShowExtPreview.stream().filter(x -> x.getIsDetails() == 1).collect(Collectors.toList());
	}

	/**
	 * 预览展示-列表
	 * @param param
	 * @return
	 */
	@Override
	public List<TblSystemCustomizeShowExtResult> getCustomizeShowExtPreviewList(CustomizeShowExtPreviewDetailsQueryParam param) {
		List<TblSystemCustomizeShowExtResult> customizeShowExtPreview = tblSystemCustomizeShowExtMapper.getCustomizeShowExtPreview(param);
		if (CollectionUtil.isEmpty(customizeShowExtPreview)) {
			return Collections.emptyList();
		}
		return customizeShowExtPreview.stream().filter(x -> x.getIsList() == 1).collect(Collectors.toList());
	}

	/**
	 * 自定义展示(编辑模块)-批量更新
	 * @param param
	 */
	@Override
	public void batchUpdateCustomizeShowExt(BatchUpdateCustomizeShowExtParam param) {
		param.getIds().forEach(x -> updateStateId(x, param.getStatus()));
	}

	/**
	 * 查询扩展信息
	 * @param field
	 * @param sceneCode
	 * @return
	 */
	@Override
	public TblSystemCustomizeShowExt findInfo(String field, String sceneCode) {
		QueryWrapper<TblSystemCustomizeShowExt> wapper = new QueryWrapper<TblSystemCustomizeShowExt>();
		wapper.eq("SCENECODE", sceneCode);
		wapper.eq("FIELD", field);
		return tblSystemCustomizeShowExtMapper.selectOne(wapper);
	}

	/**
	 * 根据id查询 自定义场景 是存在
	 * @param id
	 * @return
	 */
	private Boolean idById(BigDecimal id) {
		QueryWrapper<TblSystemCustomizeShowExt> wrapper = new QueryWrapper<TblSystemCustomizeShowExt>();
		wrapper.eq("ID", id);
		int count = tblSystemCustomizeShowExtMapper.selectCount(wrapper).intValue();
		if (count == 0) {
			return true;
		}
		return false;
	}
}
