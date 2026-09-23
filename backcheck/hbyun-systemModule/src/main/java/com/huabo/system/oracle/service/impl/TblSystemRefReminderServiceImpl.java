package com.huabo.system.oracle.service.impl;

import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
import com.hbfk.util.redis.Random.RandomUtil;
import com.huabo.landray.LandrayClient;
import com.huabo.system.constant.YesNo;
import com.huabo.system.exception.ServiceException;
import com.huabo.system.entity.TblStaff;
import com.huabo.system.entity.TblSystemRefReminder;
import com.huabo.system.mapper.TblStaffMapper;
import com.huabo.system.mapper.TblSystemRefReminderMapper;
import com.huabo.system.oracle.service.TblSystemRefReminderService;
import com.huabo.system.service.QyWeiXinService;
import com.huabo.system.vo.param.TblSystemRefReminderQueryParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Objects;

@Service
public class TblSystemRefReminderServiceImpl implements TblSystemRefReminderService {

	@Resource
	private TblSystemRefReminderMapper tblSystemRefReminderMapper;

	@Resource
	private QyWeiXinService qyWeiXinService;

	@Resource
	private LandrayClient landrayClient;

	@Resource
	private TblStaffMapper tblStaffMapper;

	@Override
	public PageInfo<TblSystemRefReminder> getList(TblSystemRefReminderQueryParam param) {
		Example example = new Example(TblSystemRefReminder.class);
		Example.Criteria criteria = example.createCriteria();
		if (Objects.nonNull(param.getCreator())) {
			criteria.andEqualTo("reminderStaffId", param.getCreator());
		}
		example.setOrderByClause(" ID desc ");
		return PageMethod.startPage(param.getPageNumber(), param.getPageSize())
				.doSelectPageInfo(() -> tblSystemRefReminderMapper.getList(param));
	}

	@Override
	public TblSystemRefReminder saveOrUpdate(TblSystemRefReminder param, String token) {
		Date now = new Date();
		if (param.getId() == null) {
			param.setId(RandomUtil.uuLongId());
			param.setState(YesNo.NO);
			param.setCreatedTime(now);
			tblSystemRefReminderMapper.insertSelective(param);
			System.out.println("1===========================add");
		} else {
			if (idById(param.getId())) {
				throw new ServiceException(400, 50001);
			}
			param.setCreatedTime(null);
			param.setCreator(null);
			param.setWorkUnit(null);
			param.setBelongGroup(null);
			tblSystemRefReminderMapper.updateByPrimaryKeySelective(param);
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
				this.landrayClient.sendMyTodoMsgReminder(token, findById(param.getId()), param.getReminderContent());
			}
		}

		return findById(param.getId());
	}

	@Override
	public void delete(Long id) {
		tblSystemRefReminderMapper.deleteByPrimaryKey(id);
	}

	@Override
	public TblSystemRefReminder findById(Long id) {
		TblSystemRefReminder model = tblSystemRefReminderMapper.selectByPrimaryKey(id);
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
		int count = tblSystemRefReminderMapper.selectCount(TblSystemRefReminder.ofId(id));
		if (count == 0) {
			return true;
		}
		return false;
	}
}
