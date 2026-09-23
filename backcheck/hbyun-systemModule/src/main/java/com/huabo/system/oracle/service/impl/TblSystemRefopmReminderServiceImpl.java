package com.huabo.system.oracle.service.impl;

import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
import com.hbfk.util.redis.Random.RandomUtil;
import com.huabo.system.constant.YesNo;
import com.huabo.system.entity.TblSystemRefopmReminder;
import com.huabo.system.exception.ServiceException;
import com.huabo.system.mapper.TblSystemRefopmReminderMapper;
import com.huabo.system.oracle.service.TblSystemRefopmReminderService;
import com.huabo.system.vo.param.TblSystemRefopmReminderQueryParam;
import com.vip.vjtools.vjkit.collection.CollectionUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.weekend.Weekend;
import tk.mybatis.mapper.weekend.WeekendCriteria;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
public class TblSystemRefopmReminderServiceImpl implements TblSystemRefopmReminderService {

	@Resource
	private TblSystemRefopmReminderMapper tblSystemRefopmReminderMapper;


	@Override
	public PageInfo<TblSystemRefopmReminder> getList(TblSystemRefopmReminderQueryParam param) {
		Example example = new Example(TblSystemRefopmReminder.class);
		Example.Criteria criteria = example.createCriteria();
		if (Objects.nonNull(param.getCreator())) {
			criteria.andEqualTo("creator", param.getCreator());
		}
		if (StringUtils.isNotBlank(param.getModuleRoute())) {
			criteria.andEqualTo("moduleRoute", param.getModuleRoute());
		}
		if (Objects.nonNull(param.getReminderId())) {
			criteria.andEqualTo("reminderId", param.getReminderId());
		}
		if (Objects.nonNull(param.getState())) {
			criteria.andEqualTo("state", param.getState());
		}
		example.setOrderByClause(" ID desc ");
		return PageMethod.startPage(param.getPageNumber(), param.getPageSize())
				.doSelectPageInfo(() -> tblSystemRefopmReminderMapper.selectByExample(example));
	}

	@Override
	public TblSystemRefopmReminder saveOrUpdate(TblSystemRefopmReminder param) {
		Date now = new Date();
		if (param.getId() == null) {
			param.setId(RandomUtil.uuLongId());
			param.setIsStop(0);
			param.setState(YesNo.NO);
			param.setCreatedTime(now);
			tblSystemRefopmReminderMapper.insertSelective(param);
		} else {
			if (idById(param.getId())) {
				throw new ServiceException(400, 50001);
			}
			param.setCreatedTime(null);
			param.setCreator(null);
			param.setWorkUnit(null);
			param.setBelongGroup(null);
			tblSystemRefopmReminderMapper.updateByPrimaryKeySelective(param);
		}
		return findById(param.getId());
	}

	@Override
	public void delete(Long id) {
		tblSystemRefopmReminderMapper.deleteByPrimaryKey(id);
	}

	@Override
	public TblSystemRefopmReminder findById(Long id) {
		TblSystemRefopmReminder model = tblSystemRefopmReminderMapper.selectByPrimaryKey(id);
		if (model == null) {
			throw new ServiceException(400, 50001);
		}
		return model;
	}

	/**
	 * 自动催发列表
	 * @return
	 */
	@Override
	public List<TblSystemRefopmReminder> getAutoList() {
		Weekend<TblSystemRefopmReminder> weekend = Weekend.of(TblSystemRefopmReminder.class);
		WeekendCriteria<TblSystemRefopmReminder, Object> weekendCriteria = weekend.weekendCriteria();
		weekendCriteria.andEqualTo(TblSystemRefopmReminder::getReminderId, 1);
		weekendCriteria.andEqualTo(TblSystemRefopmReminder::getIsStop, 0);
		List<TblSystemRefopmReminder> list = tblSystemRefopmReminderMapper.selectByExample(weekend);
		if (CollectionUtil.isEmpty(list)) {
			return Collections.emptyList();
		}
		return list;
	}

	/**
	 * 根据整改ID停止自动催办
	 * @param refopmId
	 */
	@Override
	public void stopReminder(Long refopmId) {
		if (Objects.isNull(refopmId)) {
			throw new ServiceException(400, "停止自动催办-整改落实ID不能为空");
		}
		Weekend<TblSystemRefopmReminder> weekend = Weekend.of(TblSystemRefopmReminder.class);
		WeekendCriteria<TblSystemRefopmReminder, Object> weekendCriteria = weekend.weekendCriteria();
		weekendCriteria.andEqualTo(TblSystemRefopmReminder::getRefopmId, refopmId);
		TblSystemRefopmReminder update = new TblSystemRefopmReminder();
		update.setIsStop(1);
		tblSystemRefopmReminderMapper.updateByExampleSelective(update, weekend);
	}

	/**
	 * 停止催办
	 * @param id
	 */
	@Override
	public void isStopReminder(Long id) {
		tblSystemRefopmReminderMapper.updateByPrimaryKeySelective(TblSystemRefopmReminder.ofIsStop(id, 1));
	}

	/**
	 * 根据id查询 是存在
	 * @param id
	 * @return
	 */
	private Boolean idById(Long id) {
		int count = tblSystemRefopmReminderMapper.selectCount(TblSystemRefopmReminder.ofId(id));
		if (count == 0) {
			return true;
		}
		return false;
	}
}

