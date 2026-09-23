package com.huabo.compliance.oracle.service.impl;

import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
import com.huabo.compliance.constant.YesNo;
import com.huabo.compliance.exception.ServiceException;
import com.huabo.compliance.oracle.entity.TblComplianceReportOracle;
import com.huabo.compliance.oracle.mapper.TblComplianceReportOracleMapper;
import com.huabo.compliance.oracle.service.TblComplianceReportOracleService;
import com.huabo.compliance.vo.param.TblComplianceReportQueryParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Objects;

@Service
public class TblComplianceReportOracleServiceImpl implements TblComplianceReportOracleService {

	@Resource
	private TblComplianceReportOracleMapper tblComplianceReportOracleMapper;

	@Override
	public PageInfo<TblComplianceReportOracle> getList(TblComplianceReportQueryParam param) {
		Example example = new Example(TblComplianceReportOracle.class);
		Example.Criteria criteria = example.createCriteria();
		if (StringUtils.isNotBlank(param.getReportName())) {
			criteria.andLike("reportName", "%" + param.getReportName() + "%");
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
				.doSelectPageInfo(() -> tblComplianceReportOracleMapper.selectByExample(example));
	}

	@Override
	public TblComplianceReportOracle saveOrUpdate(TblComplianceReportOracle param) {
		Date now = new Date();
		if (param.getId() == null) {
			param.setState(YesNo.NO);
			param.setCreatedTime(now);
			param.setUpdatedTime(now);
			tblComplianceReportOracleMapper.insertSelective(param);
		} else {
			if (idById(param.getId())) {
				throw new ServiceException(400, 50001);
			}
			param.setCreatedTime(null);
			param.setUpdatedTime(now);
			tblComplianceReportOracleMapper.updateByPrimaryKeySelective(param);
		}
		return findById(param.getId());
	}

	@Override
	public void delete(Integer id) {
		tblComplianceReportOracleMapper.deleteByPrimaryKey(id);
	}

	@Override
	public TblComplianceReportOracle findById(Integer id) {
		TblComplianceReportOracle report = tblComplianceReportOracleMapper.selectByPrimaryKey(id);
		if (report == null) {
			throw new ServiceException(400, 50001);
		}
		return report;
	}

	/**
	 * 根据id查询 合规报告 是存在
	 * @param id
	 * @return
	 */
	private Boolean idById(Integer id) {
		int count = tblComplianceReportOracleMapper.selectCount(TblComplianceReportOracle.ofId(id));
		if (count == 0) {
			return true;
		}
		return false;
	}
}
