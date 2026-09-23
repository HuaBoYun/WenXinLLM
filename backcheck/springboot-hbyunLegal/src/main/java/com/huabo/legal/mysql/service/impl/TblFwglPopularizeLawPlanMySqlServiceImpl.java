package com.huabo.legal.mysql.service.impl;

import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
import com.huabo.legal.constant.YesNo;
import com.huabo.legal.exception.ServiceException;
import com.huabo.legal.mysql.entity.TblFwglPopularizeLawPlanMySql;
import com.huabo.legal.mysql.mapper.TblFwglPopularizeLawPlanMySqlMapper;
import com.huabo.legal.mysql.service.TblFwglPopularizeLawPlanMySqlService;
import com.huabo.legal.vo.param.TblFwglPopularizeLawPlanQueryParam;
import com.vip.vjtools.vjkit.time.DateUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Date;

@Service
public class TblFwglPopularizeLawPlanMySqlServiceImpl implements TblFwglPopularizeLawPlanMySqlService {

	@Resource
	private TblFwglPopularizeLawPlanMySqlMapper tblFwglPopularizeLawPlanMySqlMapper;

	/**
	 * 普法培训列表 查询
	 * @param param
	 * @return
	 */
	@Override
	public PageInfo<TblFwglPopularizeLawPlanMySql> getList(TblFwglPopularizeLawPlanQueryParam param) {
		Example example = new Example(TblFwglPopularizeLawPlanMySql.class);
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
			criteria.andEqualTo("belongGroup", param.getBelongGroup());
		}
		if (StringUtils.isNotBlank(param.getWorkUnit())) {
			criteria.andEqualTo("workUnit", param.getWorkUnit());
		}
		example.setOrderByClause(" POPULARIZELAWPLANID desc ");
		return PageMethod.startPage(param.getPageNumber(), param.getPageSize())
				.doSelectPageInfo(() -> tblFwglPopularizeLawPlanMySqlMapper.selectByExample(example));
	}

	/**
	 * 普法培训 新增/更新
	 * @param param
	 * @return
	 */
	@Override
	public TblFwglPopularizeLawPlanMySql saveOrUpdate(TblFwglPopularizeLawPlanMySql param) {
		Date now = new Date();
		if (param.getPopularizeLawPlanId() == null) {
			param.setState(YesNo.YES);
			param.setCreatedTime(now);
			param.setUpdatedTime(now);
			tblFwglPopularizeLawPlanMySqlMapper.insertSelective(param);
		} else {
			if (idById(param.getPopularizeLawPlanId())) {
				throw new ServiceException(400, 50001);
			}
			param.setCreatedTime(null);
			param.setUpdatedTime(now);
			tblFwglPopularizeLawPlanMySqlMapper.updateByPrimaryKeySelective(param);
		}
		return findById(param.getPopularizeLawPlanId());
	}

	/**
	 * 普法培训 删除
	 * @param id
	 */
	@Override
	public void delete(Integer id) {
		tblFwglPopularizeLawPlanMySqlMapper.deleteByPrimaryKey(id);
	}

	/**
	 * 普法培训详情 查询
	 * @param id
	 * @return
	 */
	@Override
	public TblFwglPopularizeLawPlanMySql findById(Integer id) {
		TblFwglPopularizeLawPlanMySql popularizeLawPlan = tblFwglPopularizeLawPlanMySqlMapper.selectByPrimaryKey(id);
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
	private Boolean idById(Integer id) {
		int count = tblFwglPopularizeLawPlanMySqlMapper.selectCount(TblFwglPopularizeLawPlanMySql.ofId(id));
		if (count == 0) {
			return true;
		}
		return false;
	}
}
