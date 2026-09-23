package com.huabo.system.oracle.service.impl;

import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
import com.hbfk.util.redis.Random.RandomUtil;
import com.huabo.system.constant.YesNo;
import com.huabo.system.entity.TblSystemRefReminderAccessV;
import com.huabo.system.exception.ServiceException;
import com.huabo.system.mapper.TblSystemRefReminderAccessV1Mapper;
import com.huabo.system.oracle.service.TblSystemRefReminderAccessV1Service;
import com.huabo.system.vo.param.TblSystemRefReminderAccessQueryParam;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

@Service
public class TblSystemRefReminderAccessV1ServiceImpl implements TblSystemRefReminderAccessV1Service {

	@Resource
	private TblSystemRefReminderAccessV1Mapper tblSystemRefReminderAccessV1Mapper;

	@Override
	public PageInfo<TblSystemRefReminderAccessV> getList(TblSystemRefReminderAccessQueryParam param) {
		return PageMethod.startPage(param.getPageNumber(), param.getPageSize())
				.doSelectPageInfo(() -> tblSystemRefReminderAccessV1Mapper.getList(param));
	}

	@Override
	public TblSystemRefReminderAccessV saveOrUpdate(TblSystemRefReminderAccessV param) {
		Date now = new Date();
		if (param.getId() == null) {
			param.setId(RandomUtil.uuLongId());
			param.setState(YesNo.NO);
			param.setCreatedTime(now);
			tblSystemRefReminderAccessV1Mapper.insertSelective(param);
		} else {
			if (idById(param.getId())) {
				throw new ServiceException(400, 50001);
			}
			param.setCreatedTime(null);
			param.setCreator(null);
			param.setWorkUnit(null);
			param.setBelongGroup(null);
			tblSystemRefReminderAccessV1Mapper.updateByPrimaryKeySelective(param);
		}
		return findById(param.getId());
	}

	@Override
	public void delete(Long id) {
		tblSystemRefReminderAccessV1Mapper.deleteByPrimaryKey(id);
	}

	@Override
	public TblSystemRefReminderAccessV findById(Long id) {
		TblSystemRefReminderAccessV model = tblSystemRefReminderAccessV1Mapper.selectByPrimaryKey(id);
		if (model == null) {
			throw new ServiceException(400, 50001);
		}
		return model;
	}

	/**
	 * 根据id查询 是存在
	 * @param id
	 * @return
	 */
	private Boolean idById(Long id) {
		int count = tblSystemRefReminderAccessV1Mapper.selectCount(TblSystemRefReminderAccessV.ofId(id));
		if (count == 0) {
			return true;
		}
		return false;
	}
}
