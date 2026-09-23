package com.huabo.compliance.oracle.service.impl;

import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
import com.huabo.compliance.constant.YesNo;
import com.huabo.compliance.exception.ServiceException;
import com.huabo.compliance.oracle.entity.TblCompliancePlanMgtOracle;
import com.huabo.compliance.oracle.mapper.TblCompliancePlanMgtOracleMapper;
import com.huabo.compliance.oracle.service.TblCompliancePlanMgtOracleService;
import com.huabo.compliance.vo.param.TblCompliancePlanMgtQueryParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Objects;

@Service
public class TblCompliancePlanMgtOracleServiceImpl implements TblCompliancePlanMgtOracleService {

	@Resource
	private TblCompliancePlanMgtOracleMapper tblCompliancePlanMgtOracleMapper;

	@Override
	public PageInfo<TblCompliancePlanMgtOracle> getList(TblCompliancePlanMgtQueryParam param) {
		Example example = new Example(TblCompliancePlanMgtOracle.class);
		Example.Criteria criteria = example.createCriteria();
		if (StringUtils.isNotBlank(param.getPlanName())) {
			criteria.andLike("planName", "%" + param.getPlanName() + "%");
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
				.doSelectPageInfo(() -> tblCompliancePlanMgtOracleMapper.selectByExample(example));
	}

	@Override
	public TblCompliancePlanMgtOracle saveOrUpdate(TblCompliancePlanMgtOracle param) {
		Date now = new Date();
		if (param.getId() == null) {
			param.setState(YesNo.NO);
			param.setCreatedTime(now);
			param.setUpdatedTime(now);
			tblCompliancePlanMgtOracleMapper.insertSelective(param);
		} else {
			if (idById(param.getId())) {
				throw new ServiceException(400, 50001);
			}
			param.setCreatedTime(null);
			param.setUpdatedTime(now);
			tblCompliancePlanMgtOracleMapper.updateByPrimaryKeySelective(param);
		}
		return findById(param.getId());
	}

	@Override
	public void delete(Integer id) {
		tblCompliancePlanMgtOracleMapper.deleteByPrimaryKey(id);
	}

	@Override
	public TblCompliancePlanMgtOracle findById(Integer id) {
		TblCompliancePlanMgtOracle planMgt = tblCompliancePlanMgtOracleMapper.selectByPrimaryKey(id);
		if (planMgt == null) {
			throw new ServiceException(400, 50001);
		}
		return planMgt;
	}

	/**
	 * 根据id查询 计划管理 是存在
	 * @param id
	 * @return
	 */
	private Boolean idById(Integer id) {
		int count = tblCompliancePlanMgtOracleMapper.selectCount(TblCompliancePlanMgtOracle.ofId(id));
		if (count == 0) {
			return true;
		}
		return false;
	}
}
