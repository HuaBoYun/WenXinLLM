package com.huabo.legal.mysql.service.impl;

import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
import com.huabo.legal.constant.YesNo;
import com.huabo.legal.exception.ServiceException;
import com.huabo.legal.mysql.entity.TblFwglAnnualPlanMySql;
import com.huabo.legal.mysql.mapper.TblFwglAnnualPlanMySqlMapper;
import com.huabo.legal.mysql.service.TblFwglAnnualPlanMySqlService;
import com.huabo.legal.vo.param.TblFwglAnnualPlanQueryParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Date;

@Service
public class TblFwglAnnualPlanMySqlServiceImpl implements TblFwglAnnualPlanMySqlService {

	@Resource
	private TblFwglAnnualPlanMySqlMapper tblFwglAnnualPlanMySqlMapper;

	/**
	 * 年度计划列表 查询
	 * @param param
	 * @return
	 */
	@Override
	public PageInfo<TblFwglAnnualPlanMySql> getList(TblFwglAnnualPlanQueryParam param) {
		Example example = new Example(TblFwglAnnualPlanMySql.class);
		Example.Criteria criteria = example.createCriteria();
		if (StringUtils.isNotEmpty(param.getAnnualPlanName())) {
			criteria.andLike("annualPlanName", "%" + param.getAnnualPlanName() + "%");
		}
		if (StringUtils.isNotBlank(param.getCreator())) {
			criteria.andEqualTo("creator", param.getCreator());
		}
		if (StringUtils.isNotBlank(param.getBelongGroup())) {
			criteria.andEqualTo("belongGroup", param.getBelongGroup());
		}
		if (StringUtils.isNotBlank(param.getWorkUnit())) {
			criteria.andEqualTo("workUnit", param.getWorkUnit());
		}
		example.setOrderByClause(" ANNUALPLANID desc ");
		return PageMethod.startPage(param.getPageNumber(), param.getPageSize())
				.doSelectPageInfo(() -> tblFwglAnnualPlanMySqlMapper.selectByExample(example));
	}

	/**
	 * 年度计划 新增/更新
	 * @param param
	 * @return
	 */
	@Override
	public TblFwglAnnualPlanMySql saveOrUpdate(TblFwglAnnualPlanMySql param) {
		Date now = new Date();
		if (param.getAnnualPlanId() == null) {
			param.setState(YesNo.YES);
			param.setCreatedTime(now);
			param.setUpdatedTime(now);
			tblFwglAnnualPlanMySqlMapper.insertSelective(param);
		} else {
			if (idById(param.getAnnualPlanId())) {
				throw new ServiceException(400, 50001);
			}
			param.setCreatedTime(null);
			param.setUpdatedTime(now);
			tblFwglAnnualPlanMySqlMapper.updateByPrimaryKeySelective(param);
		}
		return findById(param.getAnnualPlanId());
	}

	/**
	 * 年度计划 删除
	 * @param id
	 */
	@Override
	public void delete(Integer id) {
		tblFwglAnnualPlanMySqlMapper.deleteByPrimaryKey(id);
	}

	/**
	 * 年度计划详情 查询
	 * @param id
	 * @return
	 */
	@Override
	public TblFwglAnnualPlanMySql findById(Integer id) {
		TblFwglAnnualPlanMySql annualPlan = tblFwglAnnualPlanMySqlMapper.selectByPrimaryKey(id);
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
	private Boolean idById(Integer id) {
		int count = tblFwglAnnualPlanMySqlMapper.selectCount(TblFwglAnnualPlanMySql.ofId(id));
		if (count == 0) {
			return true;
		}
		return false;
	}
}
