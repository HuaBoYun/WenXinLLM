package com.huabo.central.enterprises.audit.oracle.service.impl;

import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
import com.hbfk.util.redis.Random.RandomUtil;
import com.huabo.central.enterprises.audit.constant.YesNo;
import com.huabo.central.enterprises.audit.exception.ServiceException;
import com.huabo.central.enterprises.audit.oracle.entity.TblCeaInsideReportedOracle;
import com.huabo.central.enterprises.audit.oracle.mapper.TblCeaInsideReportedOracleMapper;
import com.huabo.central.enterprises.audit.oracle.service.TblCeaInsideReportedOracleService;
import com.huabo.central.enterprises.audit.vo.param.TblCeaInsideReportedQueryParam;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Objects;

@Service
public class TblCeaInsideReportedOracleServiceImpl implements TblCeaInsideReportedOracleService {

	@Resource
	private TblCeaInsideReportedOracleMapper tblCeaInsideReportedOracleMapper;

	@Override
	public PageInfo<TblCeaInsideReportedOracle> getList(TblCeaInsideReportedQueryParam param) {
		Example example = new Example(TblCeaInsideReportedOracle.class);
		Example.Criteria criteria = example.createCriteria();
		if (StringUtils.isNotBlank(param.getSymbol())) {
			criteria.andLike("symbol", "%" + param.getSymbol() + "%");
		}
		if (StringUtils.isNotBlank(param.getInsideReportedName())) {
			criteria.andLike("insideReportedName", "%" + param.getInsideReportedName() + "%");
		}
		if (org.apache.commons.lang3.StringUtils.isNotBlank(param.getDeptIds())) {
			criteria.andCondition(
					" (creator = " + param.getCreator() + " or creator in (SELECT DISTINCT STAFFID FROM TBL_USER_ORGRELATION WHERE DEPTID IN (" + param
							.getDeptIds() + "))) ");
		} else {
			criteria.andEqualTo("creator", param.getCreator());
		}
		example.setOrderByClause(" ID desc ");
		return PageMethod.startPage(param.getPageNumber(), param.getPageSize())
				.doSelectPageInfo(() -> tblCeaInsideReportedOracleMapper.selectByExample(example));
	}

	@Override
	public TblCeaInsideReportedOracle saveOrUpdate(TblCeaInsideReportedOracle param) {
		Date now = new Date();
		if (param.getId() == null) {
			param.setId(RandomUtil.uuLongId());
			param.setState(YesNo.NO);
			param.setCreatedTime(now);
			param.setUpdatedTime(now);
			tblCeaInsideReportedOracleMapper.insertSelective(param);
		} else {
			if (idById(param.getId())) {
				throw new ServiceException(400, 50001);
			}
			param.setCreator(null);
			param.setCreatedTime(null);
			param.setUpdatedTime(now);
			tblCeaInsideReportedOracleMapper.updateByPrimaryKeySelective(param);
		}
		return findById(param.getId());
	}

	@Override
	public void delete(Long id) {
		tblCeaInsideReportedOracleMapper.deleteByPrimaryKey(id);
	}

	@Override
	public TblCeaInsideReportedOracle findById(Long id) {
		TblCeaInsideReportedOracle model = tblCeaInsideReportedOracleMapper.selectByPrimaryKey(id);
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
		int count = tblCeaInsideReportedOracleMapper.selectCount(TblCeaInsideReportedOracle.ofId(id));
		if (count == 0) {
			return true;
		}
		return false;
	}
}
