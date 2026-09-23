package com.huabo.central.enterprises.audit.oracle.service.impl;

import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
import com.hbfk.util.redis.Random.RandomUtil;
import com.huabo.central.enterprises.audit.constant.YesNo;
import com.huabo.central.enterprises.audit.exception.ServiceException;
import com.huabo.central.enterprises.audit.oracle.entity.TblCeaWeekly;
import com.huabo.central.enterprises.audit.oracle.mapper.TblCeaWeeklyMapper;
import com.huabo.central.enterprises.audit.oracle.service.TblCeaWeeklyService;
import com.huabo.central.enterprises.audit.vo.param.TblCeaWeeklyQueryParam;
import com.vip.vjtools.vjkit.collection.CollectionUtil;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.weekend.Weekend;
import tk.mybatis.mapper.weekend.WeekendCriteria;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Service
public class TblCeaWeeklyServiceImpl implements TblCeaWeeklyService {

	@Resource
	private TblCeaWeeklyMapper tblCeaWeeklyMapper;

	@Override
	public PageInfo<TblCeaWeekly> getList(TblCeaWeeklyQueryParam param) {
		return PageMethod.startPage(param.getPageNumber(), param.getPageSize()).doSelectPageInfo(() -> tblCeaWeeklyMapper.getList(param));
	}

	@Override
	public TblCeaWeekly saveOrUpdate(TblCeaWeekly param) {
		Date now = new Date();
		if (param.getId() == null) {
			param.setId(RandomUtil.uuLongId());
			param.setState(YesNo.NO);
			param.setCreatedTime(now);
			param.setUpdatedTime(now);
			tblCeaWeeklyMapper.insertSelective(param);
		} else {
			if (idById(param.getId())) {
				throw new ServiceException(400, 50001);
			}
			param.setCreator(null);
			param.setCreatedTime(null);
			param.setUpdatedTime(now);
			tblCeaWeeklyMapper.updateByPrimaryKeySelective(param);
		}
		return findById(param.getId());
	}

	@Override
	public void delete(Long id) {
		tblCeaWeeklyMapper.deleteByPrimaryKey(id);
	}

	@Override
	public TblCeaWeekly findById(Long id) {
		TblCeaWeekly model = tblCeaWeeklyMapper.selectByPrimaryKey(id);
		if (model == null) {
			throw new ServiceException(400, 50001);
		}
		return model;
	}

	/**
	 * 汇总周报列表
	 * @param ids
	 * @return
	 */
	@Override
	public List<TblCeaWeekly> getIdsList(List<Long> ids) {
		if (CollectionUtil.isEmpty(ids)) {
			return Collections.emptyList();
		}
		Weekend<TblCeaWeekly> weekend = Weekend.of(TblCeaWeekly.class);
		WeekendCriteria<TblCeaWeekly, Object> weekendCriteria = weekend.weekendCriteria();
		weekendCriteria.andIn(TblCeaWeekly::getId, ids);
		weekend.setOrderByClause(" id desc");
		List<TblCeaWeekly> list = tblCeaWeeklyMapper.selectByExample(weekend);
		if (CollectionUtil.isEmpty(list)){
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
		int count = tblCeaWeeklyMapper.selectCount(TblCeaWeekly.ofId(id));
		if (count == 0) {
			return true;
		}
		return false;
	}
}
