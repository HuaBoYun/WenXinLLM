package com.huabo.compliance.oracle.service.impl;

import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
import com.huabo.compliance.constant.YesNo;
import com.huabo.compliance.exception.ServiceException;
import com.huabo.compliance.oracle.entity.TblComplianceInspectPlanOracle;
import com.huabo.compliance.oracle.mapper.TblComplianceInspectPlanOracleMapper;
import com.huabo.compliance.oracle.service.TblComplianceInspectPlanOracleService;
import com.huabo.compliance.vo.param.TblComplianceInspectPlanQueryParam;
import com.vip.vjtools.vjkit.time.DateUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

@Service
public class TblComplianceInspectPlanOracleServiceImpl implements TblComplianceInspectPlanOracleService {

	@Resource
	private TblComplianceInspectPlanOracleMapper tblComplianceInspectPlanOracleMapper;

	@Override
	public PageInfo<TblComplianceInspectPlanOracle> getList(TblComplianceInspectPlanQueryParam param) {
		Example example = new Example(TblComplianceInspectPlanOracle.class);
		Example.Criteria criteria = example.createCriteria();
		if (StringUtils.isNotBlank(param.getPlanName())) {
			criteria.andLike("planName", "%" + param.getPlanName() + "%");
		}
		if (StringUtils.isNotBlank(param.getPlanCode())) {
			criteria.andEqualTo("planCode", param.getPlanCode());
		}
		if (null != param.getStatus()) {
			criteria.andEqualTo("state", param.getStatus());
		}
		if (param.getPlanTimeStartStart() != null) {
			criteria.andGreaterThanOrEqualTo("planTimeStart", param.getPlanTimeStartStart());
		}
		if (param.getPlanTimeStartEnd() != null) {
			criteria.andLessThanOrEqualTo("planTimeStart", DateUtil.addMonths(param.getPlanTimeStartEnd(), 12));
		}
		if (param.getPlanTimeEndStart() != null) {
			criteria.andGreaterThanOrEqualTo("planTimeEnd", param.getPlanTimeEndStart());
		}
		if (param.getPlanTimeEndEnd() != null) {
			criteria.andLessThanOrEqualTo("planTimeEnd", DateUtil.addMonths(param.getPlanTimeEndEnd(), 12));
		}
		if (param.getPlanYearStart() != null) {
			criteria.andGreaterThanOrEqualTo("planYear", param.getPlanYearStart());
		}
		if (param.getPlanYearEnd() != null) {
			criteria.andLessThanOrEqualTo("planYear", DateUtil.addMonths(param.getPlanYearEnd(), 12));
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
				.doSelectPageInfo(() -> tblComplianceInspectPlanOracleMapper.selectByExample(example));
	}

	@Override
	public TblComplianceInspectPlanOracle saveOrUpdate(TblComplianceInspectPlanOracle param) {
		Date now = new Date();
		if (param.getId() == null) {
			param.setState(YesNo.NO);
			param.setCreatedTime(now);
			param.setUpdatedTime(now);
			tblComplianceInspectPlanOracleMapper.insertSelective(param);
		} else {
			if (idById(param.getId())) {
				throw new ServiceException(400, 50001);
			}
			param.setCreatedTime(null);
			param.setUpdatedTime(now);
			tblComplianceInspectPlanOracleMapper.updateByPrimaryKeySelective(param);
		}
		return findById(param.getId());
	}

	@Override
	public void delete(Integer id) {
		tblComplianceInspectPlanOracleMapper.deleteByPrimaryKey(id);
	}

	@Override
	public TblComplianceInspectPlanOracle findById(Integer id) {
		TblComplianceInspectPlanOracle inspectPlan = tblComplianceInspectPlanOracleMapper.selectByPrimaryKey(id);
		if (inspectPlan == null) {
			throw new ServiceException(400, 50001);
		}
		return inspectPlan;
	}

	@Override
	public String getTblComplianceInspectPlanAutoNum(String year) {
		Date date = new Date();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy");
		String nowYear = simpleDateFormat.format(date);
		if (StringUtils.isBlank(year)) {
			year = nowYear;
		}
		String autoNum = "CC" + "-" + year + "-";
		String no = tblComplianceInspectPlanOracleMapper.findAutoNum(autoNum);
		if (StringUtils.isBlank(no)) {
			no = "0";
		}
		autoNum += (Integer.parseInt(no) + 1);
		return autoNum;
	}

	/**
	 * 根据id查询 是存在
	 * @param id
	 * @return
	 */
	private Boolean idById(Integer id) {
		int count = tblComplianceInspectPlanOracleMapper.selectCount(TblComplianceInspectPlanOracle.ofId(id));
		if (count == 0) {
			return true;
		}
		return false;
	}
}
