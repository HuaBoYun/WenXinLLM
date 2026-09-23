package com.huabo.central.enterprises.audit.oracle.service.impl;

import com.hbfk.util.redis.Random.RandomUtil;
import com.huabo.central.enterprises.audit.constant.YesNo;
import com.huabo.central.enterprises.audit.exception.ServiceException;
import com.huabo.central.enterprises.audit.oracle.entity.TblCeaWeeklyExt;
import com.huabo.central.enterprises.audit.oracle.mapper.TblCeaWeeklyExtMapper;
import com.huabo.central.enterprises.audit.oracle.service.TblCeaWeeklyExtService;
import com.vip.vjtools.vjkit.collection.CollectionUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Service
public class TblCeaWeeklyExtServiceImpl implements TblCeaWeeklyExtService {

	@Resource
	private TblCeaWeeklyExtMapper tblCeaWeeklyExtMapper;

	@Override
	public TblCeaWeeklyExt saveOrUpdate(TblCeaWeeklyExt param) {
		Date now = new Date();
		if (param.getId() == null) {
			param.setId(RandomUtil.uuLongId());
			param.setState(YesNo.NO);
			param.setCreatedTime(now);
			param.setUpdatedTime(now);
			tblCeaWeeklyExtMapper.insertSelective(param);
		} else {
			if (idById(param.getId())) {
				throw new ServiceException(400, 50001);
			}
			param.setCreator(null);
			param.setCreatedTime(null);
			param.setUpdatedTime(now);
			tblCeaWeeklyExtMapper.updateByPrimaryKeySelective(param);
		}
		return findById(param.getId());
	}

	@Override
	public void delete(Long id) {
		tblCeaWeeklyExtMapper.deleteByPrimaryKey(id);
	}

	@Override
	public TblCeaWeeklyExt findById(Long id) {
		TblCeaWeeklyExt model = tblCeaWeeklyExtMapper.selectByPrimaryKey(id);
		if (model == null) {
			throw new ServiceException(400, 50001);
		}
		return model;
	}


	/**
	 * 查询汇总数据
	 * @param id
	 * @return
	 */
	@Override
	public List<TblCeaWeeklyExt> getList(Long id) {
		TblCeaWeeklyExt tblCeaWeeklyExt = new TblCeaWeeklyExt();
		tblCeaWeeklyExt.setWeeklyId(id);
		List<TblCeaWeeklyExt> list = tblCeaWeeklyExtMapper.select(tblCeaWeeklyExt);
		if (CollectionUtil.isEmpty(list)) {
			return Collections.emptyList();
		}
		return list;
	}

	/**
	 * 根据id查询 是存在
	 * @param id
	 * @return
	 */
	private Boolean idById(Long id) {
		int count = tblCeaWeeklyExtMapper.selectCount(TblCeaWeeklyExt.ofId(id));
		if (count == 0) {
			return true;
		}
		return false;
	}
}
