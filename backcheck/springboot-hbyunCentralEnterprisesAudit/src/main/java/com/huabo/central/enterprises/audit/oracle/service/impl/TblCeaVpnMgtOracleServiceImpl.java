package com.huabo.central.enterprises.audit.oracle.service.impl;

import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
import com.hbfk.util.redis.Random.RandomUtil;
import com.huabo.central.enterprises.audit.constant.YesNo;
import com.huabo.central.enterprises.audit.exception.ServiceException;
import com.huabo.central.enterprises.audit.oracle.entity.TblCeaVpnMgtOracle;
import com.huabo.central.enterprises.audit.oracle.mapper.TblCeaVpnMgtOracleMapper;
import com.huabo.central.enterprises.audit.oracle.service.TblCeaVpnMgtOracleService;
import com.huabo.central.enterprises.audit.vo.param.TblCeaVpnMgtQueryParam;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

@Service
public class TblCeaVpnMgtOracleServiceImpl implements TblCeaVpnMgtOracleService {

	@Resource
	private TblCeaVpnMgtOracleMapper tblCeaVpnMgtOracleMapper;

	@Override
	public PageInfo<TblCeaVpnMgtOracle> getList(TblCeaVpnMgtQueryParam param) {
		return PageMethod.startPage(param.getPageNumber(), param.getPageSize()).doSelectPageInfo(() -> tblCeaVpnMgtOracleMapper.getList(param));
	}

	@Override
	public TblCeaVpnMgtOracle saveOrUpdate(TblCeaVpnMgtOracle param) {
		Date now = new Date();
		if (param.getId() == null) {
			param.setId(RandomUtil.uuLongId());
			param.setState(YesNo.NO);
			param.setCreatedTime(now);
			param.setUpdatedTime(now);
			tblCeaVpnMgtOracleMapper.insertSelective(param);
		} else {
			if (idById(param.getId())) {
				throw new ServiceException(400, 50001);
			}
			param.setCreator(null);
			param.setCreatedTime(null);
			param.setUpdatedTime(now);
			tblCeaVpnMgtOracleMapper.updateByPrimaryKeySelective(param);
		}
		return findById(param.getId());
	}

	@Override
	public void delete(Long id) {
		tblCeaVpnMgtOracleMapper.deleteByPrimaryKey(id);
	}

	@Override
	public TblCeaVpnMgtOracle findById(Long id) {
		TblCeaVpnMgtOracle model = tblCeaVpnMgtOracleMapper.selectByPrimaryKey(id);
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
		int count = tblCeaVpnMgtOracleMapper.selectCount(TblCeaVpnMgtOracle.ofId(id));
		if (count == 0) {
			return true;
		}
		return false;
	}
}
