package com.huabo.legal.oracle.service.impl;

import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
import com.hbfk.util.redis.Random.RandomUtil;
import com.huabo.legal.constant.YesNo;
import com.huabo.legal.exception.ServiceException;
import com.huabo.legal.oracle.entity.TblFwglPopularizeLawPlanOracle;
import com.huabo.legal.oracle.mapper.TblFwglPopularizeLawPlanOracleMapper;
import com.huabo.legal.oracle.service.TblFwglPopularizeLawPlanOracleService;
import com.huabo.legal.oracle.service.TblStaffOracleService;
import com.huabo.legal.vo.param.TblFwglPopularizeLawPlanQueryParam;
import com.vip.vjtools.vjkit.time.DateUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
public class TblFwglPopularizeLawPlanOracleServiceImpl implements TblFwglPopularizeLawPlanOracleService {

	@Resource
	private TblFwglPopularizeLawPlanOracleMapper tblFwglPopularizeLawPlanOracleMapper;
	@Resource
	private TblStaffOracleService tblStaffOracleService;

	/**
	 * 普法培训列表 查询
	 * @param param
	 * @return
	 */
	@Override
	public PageInfo<TblFwglPopularizeLawPlanOracle> getList(TblFwglPopularizeLawPlanQueryParam param) {
		Example example = new Example(TblFwglPopularizeLawPlanOracle.class);
		Example.Criteria criteria = example.createCriteria();
		if (StringUtils.isNotEmpty(param.getPopularizeLawPlanName())) {
			criteria.andLike("popularizeLawPlanName", "%" + param.getPopularizeLawPlanName() + "%");
		}
		if (param.getPlanYearBeginDate() != null) {
			criteria.andGreaterThanOrEqualTo("planYear", param.getPlanYearBeginDate());
		}
		if (param.getPlanYearEndDate() != null) {
			criteria.andLessThan("planYear", DateUtil.addMonths(param.getPlanYearEndDate(), 12));
		}
		if (StringUtils.isNotBlank(param.getCreator())) {
			criteria.andEqualTo("creator", param.getCreator());
		}
		if (StringUtils.isNotBlank(param.getBelongGroup())) {
			List<Long> tblOrganizationAll = tblStaffOracleService.getTblOrganizationAll(Long.valueOf(param.getBelongGroup()));
			criteria.andIn("belongGroup", tblOrganizationAll);
			//			criteria.andCondition(" ( belongGroup in (select ORGID from TBL_ORGANIZATION where 1=1 start with ORGID= " + param.getBelongGroup()
			//					+ " and ORGTYPE!=0 AND " + "ORGTYPE<100  connect by prior orgid= FATHERORGID) or belongGroup = " + param.getBelongGroup()+") ");
		}
		if (StringUtils.isNotBlank(param.getWorkUnit())) {
			criteria.andEqualTo("workUnit", param.getWorkUnit());
		}
		example.setOrderByClause(" POPULARIZELAWPLANID desc ");
		return PageMethod.startPage(param.getPageNumber(), param.getPageSize())
				.doSelectPageInfo(() -> tblFwglPopularizeLawPlanOracleMapper.selectByExample(example));
	}

	/**
	 * 普法培训 新增/更新
	 * @param param
	 * @return
	 */
	@Override
	public TblFwglPopularizeLawPlanOracle saveOrUpdate(TblFwglPopularizeLawPlanOracle param) {
		Date now = new Date();
		if (param.getPopularizeLawPlanId() == null) {
			param.setPopularizeLawPlanId(RandomUtil.uuLongId());
			param.setState(YesNo.NO);
			param.setCreatedTime(now);
			param.setUpdatedTime(now);
			tblFwglPopularizeLawPlanOracleMapper.insertSelective(param);
		} else {
			if (idById(param.getPopularizeLawPlanId())) {
				throw new ServiceException(400, 50001);
			}
			param.setCreatedTime(null);
			param.setUpdatedTime(now);
			tblFwglPopularizeLawPlanOracleMapper.updateByPrimaryKeySelective(param);
		}
		return findById(param.getPopularizeLawPlanId());
	}

	/**
	 * 普法培训 删除
	 * @param id
	 */
	@Override
	public void delete(Long id) {
		tblFwglPopularizeLawPlanOracleMapper.deleteByPrimaryKey(id);
	}

	/**
	 * 普法培训详情 查询
	 * @param id
	 * @return
	 */
	@Override
	public TblFwglPopularizeLawPlanOracle findById(Long id) {
		TblFwglPopularizeLawPlanOracle popularizeLawPlan = tblFwglPopularizeLawPlanOracleMapper.selectByPrimaryKey(id);
		if (popularizeLawPlan == null) {
			throw new ServiceException(400, 50001);
		}
		return popularizeLawPlan;
	}

	/**
	 * 根据id查询 普法培训 是存在
	 * @param id
	 * @return
	 */
	private Boolean idById(Long id) {
		int count = tblFwglPopularizeLawPlanOracleMapper.selectCount(TblFwglPopularizeLawPlanOracle.ofId(id));
		if (count == 0) {
			return true;
		}
		return false;
	}
}
