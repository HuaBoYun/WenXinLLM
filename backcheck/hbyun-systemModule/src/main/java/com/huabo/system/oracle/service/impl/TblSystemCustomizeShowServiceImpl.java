package com.huabo.system.oracle.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hbfk.util.redis.Random.RandomUtil;
import com.huabo.system.constant.YesNo;
import com.huabo.system.entity.TblSystemCustomizeShow;
import com.huabo.system.exception.ServiceException;
import com.huabo.system.mapper.TblSystemCustomizeShowMapper;
import com.huabo.system.oracle.service.TblSystemCustomizeShowService;
import com.huabo.system.vo.param.TblSystemCustomizeShowQueryParam;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
public class TblSystemCustomizeShowServiceImpl implements TblSystemCustomizeShowService {

	@Resource
	private TblSystemCustomizeShowMapper tblSystemCustomizeShowMapper;

	/**
	 * 自定义展示列表 查询
	 * @param param
	 * @return
	 */
	@Override
	public List<TblSystemCustomizeShow> getList(TblSystemCustomizeShowQueryParam param) {
		QueryWrapper<TblSystemCustomizeShow> wapper = new QueryWrapper<TblSystemCustomizeShow>();
		if (param.getSceneId() != null) {
			wapper.eq("sceneId", param.getSceneId());
		}
		if (StringUtils.isNotBlank(param.getSceneCode())) {
			wapper.eq("sceneCode", param.getSceneCode());
		}
		if (param.getIsList() != null) {
			wapper.eq("isList", param.getIsList());
		}
		if (param.getIsDetails() != null) {
			wapper.eq("isDetails", param.getIsDetails());
		}
		wapper.orderByAsc("sort");
		return tblSystemCustomizeShowMapper.selectList(wapper);
	}

	/**
	 * 自定义展示 新增/更新
	 * @param param
	 * @return
	 */
	@Override
	public TblSystemCustomizeShow saveOrUpdate(TblSystemCustomizeShow param) {
		Date now = new Date();
		if (param.getId() == null) {
			QueryWrapper<TblSystemCustomizeShow> wapper = new QueryWrapper<TblSystemCustomizeShow>();
			wapper.eq("FIELD", param.getField());
			wapper.eq("SCENEID", param.getSceneId());
			int count = tblSystemCustomizeShowMapper.selectCount(wapper).intValue();
			if (count > 0) {
				throw new ServiceException(400, "字段名已存在");
			}
			param.setState(YesNo.NO);
			param.setCreatedTime(now);
			param.setUpdatedTime(now);
			param.setId(RandomUtil.uuBigDecimalId());
			tblSystemCustomizeShowMapper.insert(param);
		} else {
			param.setUpdatedTime(now);
			tblSystemCustomizeShowMapper.updateById(param);
		}
		return findById(param.getId());
	}

	/**
	 * 自定义展示详情 查询
	 * @param id
	 * @return
	 */
	@Override
	public TblSystemCustomizeShow findById(BigDecimal id) {
		TblSystemCustomizeShow systemCustomizeShow = tblSystemCustomizeShowMapper.selectById(id);
		if (systemCustomizeShow == null) {
			throw new ServiceException(400, 50001);
		}
		return systemCustomizeShow;
	}

	/**
	 * 自定义展示 刪除
	 * @param id
	 */
	@Override
	public void delete(BigDecimal id) {
		tblSystemCustomizeShowMapper.deleteById(id);
	}

	/**
	 * 自定义展示-状态变更
	 * @param sceneId
	 * @param state
	 */
	@Override
	public void updateState(BigDecimal sceneId, Integer state) {
		TblSystemCustomizeShow update = new TblSystemCustomizeShow();
		update.setState(state);
		QueryWrapper<TblSystemCustomizeShow> wapper = new QueryWrapper<TblSystemCustomizeShow>();
		wapper.eq("SCENEID", sceneId);
		tblSystemCustomizeShowMapper.update(update, wapper);
	}

	/**
	 * 根据id查询 自定义展示 是存在
	 * @param id
	 * @return
	 */
	private Boolean idById(BigDecimal id) {
		QueryWrapper<TblSystemCustomizeShow> wapper = new QueryWrapper<TblSystemCustomizeShow>();
		wapper.eq("ID", id);
		int count = tblSystemCustomizeShowMapper.selectCount(wapper).intValue();
		if (count == 0) {
			return true;
		}
		return false;
	}
}
