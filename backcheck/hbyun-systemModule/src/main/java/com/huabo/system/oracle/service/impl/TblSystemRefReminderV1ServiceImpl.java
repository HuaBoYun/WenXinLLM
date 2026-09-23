package com.huabo.system.oracle.service.impl;

import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
import com.hbfk.util.redis.Random.RandomUtil;
import com.huabo.landray.LandrayClient;
import com.huabo.system.constant.YesNo;
import com.huabo.system.entity.TblStaff;
import com.huabo.system.entity.TblSystemRefReminderV;
import com.huabo.system.exception.ServiceException;
import com.huabo.system.mapper.TblStaffMapper;
import com.huabo.system.mapper.TblSystemRefReminderV1Mapper;
import com.huabo.system.oracle.service.TblSystemRefReminderV1Service;
import com.huabo.system.service.QyWeiXinService;
import com.huabo.system.vo.param.TblSystemRefReminderQueryParam;
import com.vip.vjtools.vjkit.collection.CollectionUtil;
import org.apache.commons.lang3.StringUtils;
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
public class TblSystemRefReminderV1ServiceImpl implements TblSystemRefReminderV1Service {

	@Resource
	private TblSystemRefReminderV1Mapper tblSystemRefReminderV1Mapper;

	@Resource
	private QyWeiXinService qyWeiXinService;

	@Resource
	private LandrayClient landrayClient;

	@Resource
	private TblStaffMapper tblStaffMapper;

	@Override
	public PageInfo<TblSystemRefReminderV> getList(TblSystemRefReminderQueryParam param) {
		return PageMethod.startPage(param.getPageNumber(), param.getPageSize()).doSelectPageInfo(() -> tblSystemRefReminderV1Mapper.getList(param));
	}

	@Override
	public TblSystemRefReminderV saveOrUpdate(TblSystemRefReminderV param, String token) {
		Date now = new Date();
		if (param.getId() == null) {
			param.setId(RandomUtil.uuLongId());
			param.setState(YesNo.NO);
			param.setCreatedTime(now);
			tblSystemRefReminderV1Mapper.insertSelective(param);
			System.out.println("1===========================add");
		} else {
			if (idById(param.getId())) {
				throw new ServiceException(400, 50001);
			}
			param.setCreatedTime(null);
			param.setCreator(null);
			param.setWorkUnit(null);
			param.setBelongGroup(null);
			tblSystemRefReminderV1Mapper.updateByPrimaryKeySelective(param);
			System.out.println("2===========================update");
		}
		System.out.println("3===========================" + param.getId());
		if (null != param.getIsRead() && param.getIsRead() == 1) {
			//已阅,转为已办
			if (com.hbfk.util.HttpClient.isSendQYWX) {
				//推送至企业微信
				//	       	 	this.qyWeiXinService.sendMessage(task);
			}
			if (com.hbfk.util.HttpClient.isSendOa) {
				TblStaff nextStaff = tblStaffMapper.getById(param.getReminderStaffId().toString());
				this.landrayClient.blProcess(param.getId().toString(), nextStaff.getUsername());
			}
		} else {
			//发起催办
			if (com.hbfk.util.HttpClient.isSendQYWX) {
				//推送至企业微信
				//	       	 	this.qyWeiXinService.sendMessage(task);
			}
			if (com.hbfk.util.HttpClient.isSendOa) {
				//推送至OA蓝凌
				this.landrayClient.sendMyTodoMsgReminderV1(token, findById(param.getId()), param.getReminderContent());
			}
		}

		return findById(param.getId());
	}

	@Override
	public void delete(Long id) {
		tblSystemRefReminderV1Mapper.deleteByPrimaryKey(id);
	}

	@Override
	public TblSystemRefReminderV findById(Long id) {
		TblSystemRefReminderV model = tblSystemRefReminderV1Mapper.selectByPrimaryKey(id);
		if (model == null) {
			throw new ServiceException(400, 50001);
		}
		return model;
	}

	/**
	 * 查询
	 * @param reminderBusinessTable
	 * @param id
	 * @return
	 */
	@Override
	public List<TblSystemRefReminderV> findList(String reminderBusinessTable, Long id) {
		if (StringUtils.isBlank(reminderBusinessTable)) {
			throw new ServiceException("业务表字段不能为空");
		}
		Weekend<TblSystemRefReminderV> weekend = Weekend.of(TblSystemRefReminderV.class);
		WeekendCriteria<TblSystemRefReminderV, Object> weekendCriteria = weekend.weekendCriteria();
		weekendCriteria.andEqualTo(TblSystemRefReminderV::getReminderBusinessTable, reminderBusinessTable);
		weekendCriteria.andEqualTo(TblSystemRefReminderV::getRefopmId, id);
		List<TblSystemRefReminderV> list = tblSystemRefReminderV1Mapper.selectByExample(weekend);
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
		int count = tblSystemRefReminderV1Mapper.selectCount(TblSystemRefReminderV.ofId(id));
		if (count == 0) {
			return true;
		}
		return false;
	}
}
