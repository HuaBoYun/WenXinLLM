package com.huabo.compliance.oracle.service.impl;

import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
import com.huabo.compliance.constant.YesNo;
import com.huabo.compliance.exception.ServiceException;
import com.huabo.compliance.oracle.entity.TblComplianceRiskOracle;
import com.huabo.compliance.oracle.mapper.TblComplianceRiskOracleMapper;
import com.huabo.compliance.oracle.service.TblComplianceRiskOracleService;
import com.huabo.compliance.vo.param.TblComplianceRiskQueryParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Objects;

@Service
public class TblComplianceRiskOracleServiceImpl implements TblComplianceRiskOracleService {

	@Resource
	private TblComplianceRiskOracleMapper tblComplianceRiskOracleMapper;

	@Override
	public PageInfo<TblComplianceRiskOracle> getList(TblComplianceRiskQueryParam param) {
		Example example = new Example(TblComplianceRiskOracle.class);
		Example.Criteria criteria = example.createCriteria();
		if (StringUtils.isNotBlank(param.getRiskName())) {
			criteria.andLike("riskName", "%" + param.getRiskName() + "%");
		}
		if (StringUtils.isNotBlank(param.getRiskNumber())) {
			criteria.andLike("riskNumber", "%" + param.getRiskNumber() + "%");
		}
		if (param.getBelongGroup() != null) {
			criteria.andEqualTo("belongGroup", param.getBelongGroup());
		}
		if (Objects.equals(param.getAuthorityType(), 0)) {
			if (Objects.nonNull(param.getCreator())) {
				criteria.andEqualTo("creator", param.getCreator());
			}
		}
		example.setOrderByClause(" ID desc ");
		return PageMethod.startPage(param.getPageNumber(), param.getPageSize())
				.doSelectPageInfo(() -> tblComplianceRiskOracleMapper.selectByExample(example));
	}

	@Override
	public TblComplianceRiskOracle saveOrUpdate(TblComplianceRiskOracle param) {
		Date now = new Date();
		if (param.getId() == null) {
			param.setState(YesNo.NO);
			param.setCreatedTime(now);
			param.setUpdatedTime(now);
			tblComplianceRiskOracleMapper.insertSelective(param);
		} else {
			if (idById(param.getId())) {
				throw new ServiceException(400, 50001);
			}
			param.setCreatedTime(null);
			param.setUpdatedTime(now);
			tblComplianceRiskOracleMapper.updateByPrimaryKeySelective(param);
		}
		return findById(param.getId());
	}

	@Override
	public void delete(Integer id) {
		tblComplianceRiskOracleMapper.deleteByPrimaryKey(id);
	}

	@Override
	public TblComplianceRiskOracle findById(Integer id) {
		TblComplianceRiskOracle risk = tblComplianceRiskOracleMapper.selectByPrimaryKey(id);
		if (risk == null) {
			throw new ServiceException(400, 50001);
		}
		return risk;
	}

	private Boolean idById(Integer id) {
		int count = tblComplianceRiskOracleMapper.selectCount(TblComplianceRiskOracle.ofId(id));
		if (count == 0) {
			return true;
		}
		return false;
	}
}
