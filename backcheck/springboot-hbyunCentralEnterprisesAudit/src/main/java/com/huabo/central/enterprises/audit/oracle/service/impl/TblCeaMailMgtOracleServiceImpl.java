package com.huabo.central.enterprises.audit.oracle.service.impl;

import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
import com.hbfk.util.redis.Random.RandomUtil;
import com.huabo.central.enterprises.audit.constant.YesNo;
import com.huabo.central.enterprises.audit.exception.ServiceException;
import com.huabo.central.enterprises.audit.oracle.entity.TblCeaMailMgtOracle;
import com.huabo.central.enterprises.audit.oracle.mapper.TblCeaMailMgtOracleMapper;
import com.huabo.central.enterprises.audit.oracle.service.TblCeaMailMgtOracleService;
import com.huabo.central.enterprises.audit.vo.param.TblCeaMailMgtQueryParam;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

@Service
public class TblCeaMailMgtOracleServiceImpl implements TblCeaMailMgtOracleService {

	@Resource
	private TblCeaMailMgtOracleMapper tblCeaMailMgtOracleMapper;

	@Override
	public PageInfo<TblCeaMailMgtOracle> getList(TblCeaMailMgtQueryParam param) {
		return PageMethod.startPage(param.getPageNumber(), param.getPageSize()).doSelectPageInfo(() -> tblCeaMailMgtOracleMapper.getList(param));
	}

	@Override
	public TblCeaMailMgtOracle saveOrUpdate(TblCeaMailMgtOracle param) {
		Date now = new Date();
		if (param.getId() == null) {
			param.setId(RandomUtil.uuLongId());
			param.setState(YesNo.NO);
			param.setCreatedTime(now);
			param.setUpdatedTime(now);
			tblCeaMailMgtOracleMapper.insertSelective(param);
		} else {
			if (idById(param.getId())) {
				throw new ServiceException(400, 50001);
			}
			param.setCreator(null);
			param.setCreatedTime(null);
			param.setUpdatedTime(now);
			tblCeaMailMgtOracleMapper.updateByPrimaryKeySelective(param);
		}
		return findById(param.getId());
	}

	@Override
	public void delete(Long id) {
		tblCeaMailMgtOracleMapper.deleteByPrimaryKey(id);
	}

	@Override
	public TblCeaMailMgtOracle findById(Long id) {
		TblCeaMailMgtOracle ipManage = tblCeaMailMgtOracleMapper.selectByPrimaryKey(id);
		if (ipManage == null) {
			throw new ServiceException(400, 50001);
		}
		return ipManage;
	}

	/**
	 * 根据id查询 是存在
	 * @param id
	 * @return
	 */
	private Boolean idById(Long id) {
		int count = tblCeaMailMgtOracleMapper.selectCount(TblCeaMailMgtOracle.ofId(id));
		if (count == 0) {
			return true;
		}
		return false;
	}
}
