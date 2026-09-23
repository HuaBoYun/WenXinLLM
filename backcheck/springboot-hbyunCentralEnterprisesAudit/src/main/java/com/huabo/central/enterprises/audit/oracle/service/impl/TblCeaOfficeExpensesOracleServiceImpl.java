package com.huabo.central.enterprises.audit.oracle.service.impl;

import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
import com.hbfk.util.redis.Random.RandomUtil;
import com.huabo.central.enterprises.audit.constant.YesNo;
import com.huabo.central.enterprises.audit.exception.ServiceException;
import com.huabo.central.enterprises.audit.oracle.entity.TblCeaOfficeExpensesOracle;
import com.huabo.central.enterprises.audit.oracle.mapper.TblCeaOfficeExpensesOracleMapper;
import com.huabo.central.enterprises.audit.oracle.service.TblCeaOfficeExpensesOracleService;
import com.huabo.central.enterprises.audit.vo.param.TblCeaOfficeExpensesQueryParam;
import com.vip.vjtools.vjkit.time.DateUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Objects;

@Service
public class TblCeaOfficeExpensesOracleServiceImpl implements TblCeaOfficeExpensesOracleService {

	@Resource
	private TblCeaOfficeExpensesOracleMapper tblCeaOfficeExpensesOracleMapper;

	@Override
	public PageInfo<TblCeaOfficeExpensesOracle> getList(TblCeaOfficeExpensesQueryParam param) {
		if (Objects.nonNull(param.getOfficeExpensesTimeEnd())) {
			param.setOfficeExpensesTimeEnd(DateUtil.addDays(param.getOfficeExpensesTimeEnd(), 1));
		}
		return PageMethod.startPage(param.getPageNumber(), param.getPageSize())
				.doSelectPageInfo(() -> tblCeaOfficeExpensesOracleMapper.getList(param));
	}

	@Override
	public TblCeaOfficeExpensesOracle saveOrUpdate(TblCeaOfficeExpensesOracle param) {
		Date now = new Date();
		if (param.getId() == null) {
			param.setId(RandomUtil.uuLongId());
			param.setState(YesNo.NO);
			param.setCreatedTime(now);
			param.setUpdatedTime(now);
			tblCeaOfficeExpensesOracleMapper.insertSelective(param);
		} else {
			if (idById(param.getId())) {
				throw new ServiceException(400, 50001);
			}
			param.setCreator(null);
			param.setCreatedTime(null);
			param.setUpdatedTime(now);
			tblCeaOfficeExpensesOracleMapper.updateByPrimaryKeySelective(param);
		}
		return findById(param.getId());
	}

	@Override
	public void delete(Long id) {
		tblCeaOfficeExpensesOracleMapper.deleteByPrimaryKey(id);
	}

	@Override
	public TblCeaOfficeExpensesOracle findById(Long id) {
		TblCeaOfficeExpensesOracle model = tblCeaOfficeExpensesOracleMapper.selectByPrimaryKey(id);
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
		int count = tblCeaOfficeExpensesOracleMapper.selectCount(TblCeaOfficeExpensesOracle.ofId(id));
		if (count == 0) {
			return true;
		}
		return false;
	}
}
