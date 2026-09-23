package com.huabo.central.enterprises.audit.oracle.service.impl;

import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
import com.hbfk.util.redis.Random.RandomUtil;
import com.huabo.central.enterprises.audit.constant.YesNo;
import com.huabo.central.enterprises.audit.exception.ServiceException;
import com.huabo.central.enterprises.audit.oracle.entity.TblCeaLiQing;
import com.huabo.central.enterprises.audit.oracle.mapper.TblCeaLiQingMapper;
import com.huabo.central.enterprises.audit.oracle.service.TblCeaLiQingService;
import com.huabo.central.enterprises.audit.vo.param.TblCeaLiQingQueryParam;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

@Service
public class TblCeaLiQingServiceImpl implements TblCeaLiQingService {

	@Resource
	private TblCeaLiQingMapper tblCeaLiQingMapper;

	@Override
	public PageInfo<TblCeaLiQing> getList(TblCeaLiQingQueryParam param) {
		return PageMethod.startPage(param.getPageNumber(), param.getPageSize()).doSelectPageInfo(() -> tblCeaLiQingMapper.getList(param));
	}

	@Override
	public TblCeaLiQing saveOrUpdate(TblCeaLiQing param) {
		Date now = new Date();
		if (param.getId() == null) {
			param.setId(RandomUtil.uuLongId());
			param.setState(YesNo.NO);
			param.setCreatedTime(now);
			param.setUpdatedTime(now);
			tblCeaLiQingMapper.insertSelective(param);
		} else {
			if (idById(param.getId())) {
				throw new ServiceException(400, 50001);
			}
			param.setCreator(null);
			param.setCreatedTime(null);
			param.setUpdatedTime(now);
			tblCeaLiQingMapper.updateByPrimaryKeySelective(param);
		}
		return findById(param.getId());
	}

	@Override
	public void delete(Long id) {
		tblCeaLiQingMapper.deleteByPrimaryKey(id);
	}

	@Override
	public TblCeaLiQing findById(Long id) {
		TblCeaLiQing model = tblCeaLiQingMapper.selectByPrimaryKey(id);
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
		int count = tblCeaLiQingMapper.selectCount(TblCeaLiQing.ofId(id));
		if (count == 0) {
			return true;
		}
		return false;
	}
}
