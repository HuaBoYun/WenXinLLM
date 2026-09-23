package com.huabo.system.oracle.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
import com.hbfk.util.redis.Random.RandomUtil;
import com.huabo.system.constant.YesNo;
import com.huabo.system.entity.TblSystemCustomizeScene;
import com.huabo.system.exception.ServiceException;
import com.huabo.system.mapper.TblSystemCustomizeSceneMapper;
import com.huabo.system.oracle.service.TblSystemCustomizeSceneService;
import com.huabo.system.vo.param.TblSystemCustomizeSceneQueryParam;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.math.BigDecimal;
import java.util.Date;

@Service
public class TblSystemCustomizeSceneServiceImpl implements TblSystemCustomizeSceneService {

	@Resource
	private TblSystemCustomizeSceneMapper tblSystemCustomizeSceneMapper;

	/**
	 * 自定义场景列表 查询
	 * @param param
	 * @return
	 */
	@Override
	public PageInfo<TblSystemCustomizeScene> getList(TblSystemCustomizeSceneQueryParam param) {
		
		IPage<TblSystemCustomizeScene> page = new Page<TblSystemCustomizeScene>(param.getPageNumber(),param.getPageSize());
		
		return PageMethod.startPage(param.getPageNumber(), param.getPageSize()).doSelectPageInfo(() -> tblSystemCustomizeSceneMapper.getList(page, param));
	}

	/**
	 * 自定义场景 新增/更新
	 * @param param
	 * @return
	 */
	@Override
	public TblSystemCustomizeScene saveOrUpdate(TblSystemCustomizeScene param) {
		Date now = new Date();
		if (param.getId() == null) {
			QueryWrapper<TblSystemCustomizeScene> wapper = new QueryWrapper<TblSystemCustomizeScene>();
			wapper.eq("PARENTCATALOGUEID", param.getParentCatalogueId());
			wapper.eq("CATALOGUEID", param.getCatalogueId());
			int count = tblSystemCustomizeSceneMapper.selectCount(wapper).intValue();
			if (count > 0) {
				throw new ServiceException(400, "菜单已存在");
			}
			wapper = new QueryWrapper<TblSystemCustomizeScene>();
			wapper.eq("SCENECODE", param.getSceneCode());
			int sceneCodeCount = tblSystemCustomizeSceneMapper.selectCount(wapper).intValue();
			if (sceneCodeCount > 0) {
				throw new ServiceException(400, "场景唯一编码 已存在");
			}
			param.setState(YesNo.NO);
			param.setCreatedTime(now);
			param.setUpdatedTime(now);
			param.setId(RandomUtil.uuBigDecimalId());
			tblSystemCustomizeSceneMapper.insert(param);
		} else {
			param.setUpdatedTime(now);
			tblSystemCustomizeSceneMapper.updateById(param);
		}
		return findById(param.getId());
	}

	/**
	 * 自定义场景详情 查询
	 * @param id
	 * @return
	 */
	@Override
	public TblSystemCustomizeScene findById(BigDecimal id) {
		TblSystemCustomizeScene systemCustomizeScene = tblSystemCustomizeSceneMapper.selectById(id);
		if (systemCustomizeScene == null) {
			throw new ServiceException(400, 50001);
		}
		return systemCustomizeScene;
	}

	/**
	 * 自定义场景 刪除
	 * @param id
	 */
	@Override
	public void delete(BigDecimal id) {
		tblSystemCustomizeSceneMapper.deleteById(id);
	}

}
