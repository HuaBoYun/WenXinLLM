package com.huabo.system.oracle.service.impl;

import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
import com.hbfk.util.redis.Random.RandomUtil;
import com.huabo.system.constant.YesNo;
import com.huabo.system.entity.TblSystemRefReminderAccess;
import com.huabo.system.exception.ServiceException;
import com.huabo.system.mapper.TblSystemRefReminderAccessMapper;
import com.huabo.system.oracle.service.TblSystemRefReminderAccessService;
import com.huabo.system.vo.param.TblSystemRefReminderAccessQueryParam;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

@Service
public class TblSystemRefReminderAccessServiceImpl implements TblSystemRefReminderAccessService {

	@Resource
	private TblSystemRefReminderAccessMapper tblSystemRefReminderAccessMapper;

	@Override
	public PageInfo<TblSystemRefReminderAccess> getList(TblSystemRefReminderAccessQueryParam param) {
		return PageMethod.startPage(param.getPageNumber(), param.getPageSize())
				.doSelectPageInfo(() -> tblSystemRefReminderAccessMapper.getList(param));
	}

	@Override
	public TblSystemRefReminderAccess saveOrUpdate(TblSystemRefReminderAccess param) {
		Date now = new Date();
		if (param.getId() == null) {
			param.setId(RandomUtil.uuLongId());
			param.setState(YesNo.NO);
			param.setCreatedTime(now);
			tblSystemRefReminderAccessMapper.insertSelective(param);
		} else {
			if (idById(param.getId())) {
				throw new ServiceException(400, 50001);
			}
			param.setCreatedTime(null);
			param.setCreator(null);
			param.setWorkUnit(null);
			param.setBelongGroup(null);
			tblSystemRefReminderAccessMapper.updateByPrimaryKeySelective(param);
		}
		return findById(param.getId());
	}

	@Override
	public void delete(Long id) {
		tblSystemRefReminderAccessMapper.deleteByPrimaryKey(id);
	}

	@Override
	public TblSystemRefReminderAccess findById(Long id) {
		TblSystemRefReminderAccess model = tblSystemRefReminderAccessMapper.selectByPrimaryKey(id);
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
		int count = tblSystemRefReminderAccessMapper.selectCount(TblSystemRefReminderAccess.ofId(id));
		if (count == 0) {
			return true;
		}
		return false;
	}
}
