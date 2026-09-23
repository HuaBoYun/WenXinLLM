package com.huabo.legal.oracle.service.impl;

import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
import com.hbfk.util.redis.Random.RandomUtil;
import com.huabo.legal.constant.YesNo;
import com.huabo.legal.exception.ServiceException;
import com.huabo.legal.oracle.entity.TblFwglAnnualPlanOracle;
import com.huabo.legal.oracle.mapper.TblFwglAnnualPlanOracleMapper;
import com.huabo.legal.oracle.service.TblFwglAnnualPlanOracleService;
import com.huabo.legal.oracle.service.TblStaffOracleService;
import com.huabo.legal.vo.param.TblFwglAnnualPlanQueryParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
public class TblFwglAnnualPlanOracleServiceImpl implements TblFwglAnnualPlanOracleService {

	@Resource
	private TblFwglAnnualPlanOracleMapper tblFwglAnnualPlanOracleMapper;
	@Resource
	private TblStaffOracleService tblStaffOracleService;

	/**
	 * 年度计划列表 查询
	 * @param param
	 * @return
	 */
	@Override
	public PageInfo<TblFwglAnnualPlanOracle> getList(TblFwglAnnualPlanQueryParam param) {
		Example example = new Example(TblFwglAnnualPlanOracle.class);
		Example.Criteria criteria = example.createCriteria();
		if (StringUtils.isNotEmpty(param.getAnnualPlanName())) {
			criteria.andLike("annualPlanName", "%" + param.getAnnualPlanName() + "%");
		}
		if (StringUtils.isNotBlank(param.getCreator())) {
			criteria.andEqualTo("creator", param.getCreator());
		}
		if (StringUtils.isNotBlank(param.getBelongGroup())) {
			List<Long> tblOrganizationAll = tblStaffOracleService.getTblOrganizationAll(Long.valueOf(param.getBelongGroup()));
			criteria.andIn("belongGroup", tblOrganizationAll);
			//			criteria.andCondition(" ( belongGroup in (select ORGID from TBL_ORGANIZATION where 1=1 start with ORGID= " + param.getBelongGroup()
			//					+ " and ORGTYPE!=0 AND " + "ORGTYPE<100  connect by prior orgid= FATHERORGID) or belongGroup = " + param.getBelongGroup() + ") ");
		}
		if (StringUtils.isNotBlank(param.getWorkUnit())) {
			criteria.andEqualTo("workUnit", param.getWorkUnit());
		}
		example.setOrderByClause(" ANNUALPLANID desc ");
		return PageMethod.startPage(param.getPageNumber(), param.getPageSize())
				.doSelectPageInfo(() -> tblFwglAnnualPlanOracleMapper.selectByExample(example));
	}

	/**
	 * 年度计划 新增/更新
	 * @param param
	 * @return
	 */
	@Override
	public TblFwglAnnualPlanOracle saveOrUpdate(TblFwglAnnualPlanOracle param) {
		Date now = new Date();
		if (param.getAnnualPlanId() == null) {
			param.setAnnualPlanId(RandomUtil.uuLongId());
			param.setState(YesNo.NO);
			param.setCreatedTime(now);
			param.setUpdatedTime(now);
			tblFwglAnnualPlanOracleMapper.insertSelective(param);
		} else {
			if (idById(param.getAnnualPlanId())) {
				throw new ServiceException(400, 50001);
			}
			param.setCreatedTime(null);
			param.setUpdatedTime(now);
			tblFwglAnnualPlanOracleMapper.updateByPrimaryKeySelective(param);
		}
		return findById(param.getAnnualPlanId());
	}

	/**
	 * 年度计划 删除
	 * @param id
	 */
	@Override
	public void delete(Long id) {
		tblFwglAnnualPlanOracleMapper.deleteByPrimaryKey(id);
	}

	/**
	 * 年度计划详情 查询
	 * @param id
	 * @return
	 */
	@Override
	public TblFwglAnnualPlanOracle findById(Long id) {
		TblFwglAnnualPlanOracle annualPlan = tblFwglAnnualPlanOracleMapper.selectByPrimaryKey(id);
		if (annualPlan == null) {
			throw new ServiceException(400, 50001);
		}
		return annualPlan;
	}

	/**
	 * 根据id查询 年度计划 是存在
	 * @param id
	 * @return
	 */
	private Boolean idById(Long id) {
		int count = tblFwglAnnualPlanOracleMapper.selectCount(TblFwglAnnualPlanOracle.ofId(id));
		if (count == 0) {
			return true;
		}
		return false;
	}
}
