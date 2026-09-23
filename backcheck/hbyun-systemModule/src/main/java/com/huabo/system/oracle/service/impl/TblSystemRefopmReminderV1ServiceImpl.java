package com.huabo.system.oracle.service.impl;

import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
import com.hbfk.util.redis.Random.RandomUtil;
import com.huabo.system.constant.YesNo;
import com.huabo.system.entity.TblSystemRefopmReminderV;
import com.huabo.system.exception.ServiceException;
import com.huabo.system.mapper.TblSystemRefopmReminderV1Mapper;
import com.huabo.system.oracle.service.TblSystemRefopmReminderV1Service;
import com.huabo.system.vo.param.TblSystemRefopmReminderQueryParam;
import com.huabo.system.vo.result.RefReminderListResult;
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
public class TblSystemRefopmReminderV1ServiceImpl implements TblSystemRefopmReminderV1Service {

	@Resource
	private TblSystemRefopmReminderV1Mapper tblSystemRefopmReminderV1Mapper;


	@Override
	public PageInfo<TblSystemRefopmReminderV> getList(TblSystemRefopmReminderQueryParam param) {
		Example example = new Example(TblSystemRefopmReminderV.class);
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
				.doSelectPageInfo(() -> tblSystemRefopmReminderV1Mapper.selectByExample(example));
	}

	@Override
	public TblSystemRefopmReminderV saveOrUpdate(TblSystemRefopmReminderV param) {
		Date now = new Date();
		if (param.getId() == null) {
			param.setId(RandomUtil.uuLongId());
			param.setIsStop(0);
			param.setState(YesNo.NO);
			param.setCreatedTime(now);
			tblSystemRefopmReminderV1Mapper.insertSelective(param);
		} else {
			if (idById(param.getId())) {
				throw new ServiceException(400, 50001);
			}
			param.setCreatedTime(null);
			param.setCreator(null);
			param.setWorkUnit(null);
			param.setBelongGroup(null);
			tblSystemRefopmReminderV1Mapper.updateByPrimaryKeySelective(param);
		}
		return findById(param.getId());
	}

	@Override
	public void delete(Long id) {
		tblSystemRefopmReminderV1Mapper.deleteByPrimaryKey(id);
	}

	@Override
	public TblSystemRefopmReminderV findById(Long id) {
		TblSystemRefopmReminderV model = tblSystemRefopmReminderV1Mapper.selectByPrimaryKey(id);
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
	public List<TblSystemRefopmReminderV> getAutoList() {
		Weekend<TblSystemRefopmReminderV> weekend = Weekend.of(TblSystemRefopmReminderV.class);
		WeekendCriteria<TblSystemRefopmReminderV, Object> weekendCriteria = weekend.weekendCriteria();
		weekendCriteria.andEqualTo(TblSystemRefopmReminderV::getReminderId, 1);
		weekendCriteria.andEqualTo(TblSystemRefopmReminderV::getIsStop, 0);
		List<TblSystemRefopmReminderV> list = tblSystemRefopmReminderV1Mapper.selectByExample(weekend);
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
		Weekend<TblSystemRefopmReminderV> weekend = Weekend.of(TblSystemRefopmReminderV.class);
		WeekendCriteria<TblSystemRefopmReminderV, Object> weekendCriteria = weekend.weekendCriteria();
		weekendCriteria.andEqualTo(TblSystemRefopmReminderV::getRefopmId, refopmId);
		TblSystemRefopmReminderV update = new TblSystemRefopmReminderV();
		update.setIsStop(1);
		tblSystemRefopmReminderV1Mapper.updateByExampleSelective(update, weekend);
	}

	/**
	 * 停止催办
	 * @param id
	 */
	@Override
	public void isStopReminder(Long id) {
		tblSystemRefopmReminderV1Mapper.updateByPrimaryKeySelective(TblSystemRefopmReminderV.ofIsStop(id, 1));
	}

	/**
	 * 统计数量
	 * @param param
	 * @return
	 */
	@Override
	public Integer count(TblSystemRefopmReminderV param) {
		Weekend<TblSystemRefopmReminderV> weekend = Weekend.of(TblSystemRefopmReminderV.class);
		WeekendCriteria<TblSystemRefopmReminderV, Object> weekendCriteria = weekend.weekendCriteria();
		weekendCriteria.andEqualTo(TblSystemRefopmReminderV::getReminderBusinessTable,
				param.getReminderBusinessTable());
		if (Objects.nonNull(param.getId())) {
			weekendCriteria.andNotEqualTo(TblSystemRefopmReminderV::getId, param.getId());
		}
		return tblSystemRefopmReminderV1Mapper.selectCount(param);
	}

	/**
	 * 业务查询
	 * @param reminderBusinessTable
	 * @param reminderBusinessStaff
	 * @param reminderBusinessTime
	 * @param reminderBusinessContent
	 * @return
	 */
	@Override
	public List<RefReminderListResult> getBusinessList(String reminderBusinessTable, String reminderBusinessId,
			String reminderBusinessStaff, String reminderBusinessTime, String reminderBusinessContent) {
		return tblSystemRefopmReminderV1Mapper.findBusinessList(reminderBusinessTable, reminderBusinessId,
				reminderBusinessStaff, reminderBusinessTime, reminderBusinessContent);
	}

	/**
	 * 启动催办
	 * @param id
	 */
	@Override
	public void isStartReminder(Long id) {
		tblSystemRefopmReminderV1Mapper.updateByPrimaryKeySelective(TblSystemRefopmReminderV.ofIsStop(id, 0));
	}

	/**
	 * 根据id查询 是存在
	 * @param id
	 * @return
	 */
	private Boolean idById(Long id) {
		int count = tblSystemRefopmReminderV1Mapper.selectCount(TblSystemRefopmReminderV.ofId(id));
		if (count == 0) {
			return true;
		}
		return false;
	}
}

