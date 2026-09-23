package com.huabo.central.enterprises.audit.oracle.service.impl;

import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
import com.hbfk.util.redis.Random.RandomUtil;
import com.huabo.central.enterprises.audit.constant.YesNo;
import com.huabo.central.enterprises.audit.exception.ServiceException;
import com.huabo.central.enterprises.audit.oracle.entity.TblCeaInternalWebsiteApplyOracle;
import com.huabo.central.enterprises.audit.oracle.mapper.TblCeaInternalWebsiteApplyOracleMapper;
import com.huabo.central.enterprises.audit.oracle.service.TblCeaInternalWebsiteApplyOracleService;
import com.huabo.central.enterprises.audit.vo.param.TblCeaInternalWebsiteApplyQueryParam;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

@Service
public class TblCeaInternalWebsiteApplyOracleServiceImpl implements TblCeaInternalWebsiteApplyOracleService {

	@Resource
	private TblCeaInternalWebsiteApplyOracleMapper tblCeaInternalWebsiteApplyOracleMapper;

	@Override
	public PageInfo<TblCeaInternalWebsiteApplyOracle> getList(TblCeaInternalWebsiteApplyQueryParam param) {
		return PageMethod.startPage(param.getPageNumber(), param.getPageSize())
				.doSelectPageInfo(() -> tblCeaInternalWebsiteApplyOracleMapper.getList(param));
	}

	@Override
	public TblCeaInternalWebsiteApplyOracle saveOrUpdate(TblCeaInternalWebsiteApplyOracle param) {
		Date now = new Date();
		if (param.getId() == null) {
			param.setId(RandomUtil.uuLongId());
			param.setState(YesNo.NO);
			param.setCreatedTime(now);
			param.setUpdatedTime(now);
			tblCeaInternalWebsiteApplyOracleMapper.insertSelective(param);
		} else {
			if (idById(param.getId())) {
				throw new ServiceException(400, 50001);
			}
			param.setCreator(null);
			param.setCreatedTime(null);
			param.setUpdatedTime(now);
			tblCeaInternalWebsiteApplyOracleMapper.updateByPrimaryKeySelective(param);
		}
		return findById(param.getId());
	}

	@Override
	public void delete(Long id) {
		tblCeaInternalWebsiteApplyOracleMapper.deleteByPrimaryKey(id);
	}

	@Override
	public TblCeaInternalWebsiteApplyOracle findById(Long id) {
		TblCeaInternalWebsiteApplyOracle model = tblCeaInternalWebsiteApplyOracleMapper.selectByPrimaryKey(id);
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
		int count = tblCeaInternalWebsiteApplyOracleMapper.selectCount(TblCeaInternalWebsiteApplyOracle.ofId(id));
		if (count == 0) {
			return true;
		}
		return false;
	}
}
