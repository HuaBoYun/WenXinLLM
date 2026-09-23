package com.huabo.system.oracle.service.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hbfk.util.redis.Random.RandomUtil;
import com.huabo.system.entity.TblSystemHomeAuthorizationOracle;
import com.huabo.system.exception.ServiceException;
import com.huabo.system.mapper.TblSystemHomeAuthorizationOracleMapper;
import com.huabo.system.oracle.service.TblSystemHomeAuthorizationOracleService;
import com.huabo.system.vo.param.TblSystemHomeAuthorizationQueryParam;

@Service
public class TblSystemHomeAuthorizationOracleServiceImpl implements TblSystemHomeAuthorizationOracleService {

	@Resource
	private TblSystemHomeAuthorizationOracleMapper tblSystemHomeAuthorizationOracleMapper;

	@Override
	public List<TblSystemHomeAuthorizationOracle> getList(TblSystemHomeAuthorizationQueryParam param) {
		QueryWrapper<TblSystemHomeAuthorizationOracle> wrapper = new QueryWrapper<TblSystemHomeAuthorizationOracle>();
		if (Objects.nonNull(param.getHomePageId())) {
			wrapper.eq("homePageId",  param.getHomePageId());
		}
		wrapper.orderByDesc("id");
		return tblSystemHomeAuthorizationOracleMapper.selectList(wrapper);
	}

	@Override
	public TblSystemHomeAuthorizationOracle saveOrUpdate(TblSystemHomeAuthorizationOracle param) {
		if (param.getId() == null) {
			param.setId(RandomUtil.uuBigDecimalId());
			tblSystemHomeAuthorizationOracleMapper.insert(param);
		} else {
			if (idById(param.getId())) {
				throw new ServiceException(400, 50001);
			}
			tblSystemHomeAuthorizationOracleMapper.updateById(param);
		}
		return findById(param.getId());
	}

	@Override
	public TblSystemHomeAuthorizationOracle findById(BigDecimal id) {
		TblSystemHomeAuthorizationOracle homeAuthorization = tblSystemHomeAuthorizationOracleMapper.selectById(id);
		if (homeAuthorization == null) {
			throw new ServiceException(400, 50001);
		}
		return homeAuthorization;
	}

	@Override
	public void delete(BigDecimal homePageId) {
		QueryWrapper<TblSystemHomeAuthorizationOracle> wrapper = new QueryWrapper<TblSystemHomeAuthorizationOracle>();
		wrapper.eq("HOMEPAGEID", homePageId);
		tblSystemHomeAuthorizationOracleMapper.delete(wrapper);
	}

	@Override
	public void deleteAuthCompany(BigDecimal belongGroup) {
		tblSystemHomeAuthorizationOracleMapper.deleteAuthCompany(belongGroup);
	}

	/**
	 * 根据id查询 系统首页配置 是存在
	 * @param id
	 * @return
	 */
	private Boolean idById(BigDecimal id) {
		QueryWrapper<TblSystemHomeAuthorizationOracle> wrapper = new QueryWrapper<TblSystemHomeAuthorizationOracle>();
		wrapper.eq("ID", id);
		int count = tblSystemHomeAuthorizationOracleMapper.selectCount(wrapper).intValue();
		if (count == 0) {
			return true;
		}
		return false;
	}
}
