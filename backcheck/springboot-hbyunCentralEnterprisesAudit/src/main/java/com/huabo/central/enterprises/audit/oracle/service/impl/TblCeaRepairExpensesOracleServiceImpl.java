package com.huabo.central.enterprises.audit.oracle.service.impl;

import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
import com.hbfk.util.redis.Random.RandomUtil;
import com.huabo.central.enterprises.audit.constant.YesNo;
import com.huabo.central.enterprises.audit.exception.ServiceException;
import com.huabo.central.enterprises.audit.oracle.entity.TblCeaRepairExpensesOracle;
import com.huabo.central.enterprises.audit.oracle.mapper.TblCeaRepairExpensesOracleMapper;
import com.huabo.central.enterprises.audit.oracle.service.TblCeaRepairExpensesOracleService;
import com.huabo.central.enterprises.audit.vo.param.TblCeaRepairExpensesQueryParam;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

@Service
public class TblCeaRepairExpensesOracleServiceImpl implements TblCeaRepairExpensesOracleService {

	@Resource
	private TblCeaRepairExpensesOracleMapper tblCeaRepairExpensesOracleMapper;

	@Override
	public PageInfo<TblCeaRepairExpensesOracle> getList(TblCeaRepairExpensesQueryParam param) {
		return PageMethod.startPage(param.getPageNumber(), param.getPageSize())
				.doSelectPageInfo(() -> tblCeaRepairExpensesOracleMapper.getList(param));
	}

	@Override
	public TblCeaRepairExpensesOracle saveOrUpdate(TblCeaRepairExpensesOracle param) {
		Date now = new Date();
		if (param.getId() == null) {
			param.setId(RandomUtil.uuLongId());
			param.setState(YesNo.NO);
			param.setCreatedTime(now);
			param.setUpdatedTime(now);
			tblCeaRepairExpensesOracleMapper.insertSelective(param);
		} else {
			if (idById(param.getId())) {
				throw new ServiceException(400, 50001);
			}
			param.setCreator(null);
			param.setCreatedTime(null);
			param.setUpdatedTime(now);
			tblCeaRepairExpensesOracleMapper.updateByPrimaryKeySelective(param);
		}
		return findById(param.getId());
	}

	@Override
	public void delete(Long id) {
		tblCeaRepairExpensesOracleMapper.deleteByPrimaryKey(id);
	}

	@Override
	public TblCeaRepairExpensesOracle findById(Long id) {
		TblCeaRepairExpensesOracle model = tblCeaRepairExpensesOracleMapper.selectByPrimaryKey(id);
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
		int count = tblCeaRepairExpensesOracleMapper.selectCount(TblCeaRepairExpensesOracle.ofId(id));
		if (count == 0) {
			return true;
		}
		return false;
	}
}

