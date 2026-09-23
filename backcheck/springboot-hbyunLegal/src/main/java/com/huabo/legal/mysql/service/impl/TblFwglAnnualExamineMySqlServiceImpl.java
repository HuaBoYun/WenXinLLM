package com.huabo.legal.mysql.service.impl;

import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
import com.huabo.legal.constant.YesNo;
import com.huabo.legal.exception.ServiceException;
import com.huabo.legal.mysql.entity.TblFwglAnnualExamineMySql;
import com.huabo.legal.mysql.mapper.TblFwglAnnualExamineMySqlMapper;
import com.huabo.legal.mysql.service.TblFwglAnnualExamineMySqlService;
import com.huabo.legal.vo.param.TblFwglAnnualExamineQueryParam;
import com.vip.vjtools.vjkit.time.DateUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Date;

@Service
public class TblFwglAnnualExamineMySqlServiceImpl implements TblFwglAnnualExamineMySqlService {

	@Resource
	private TblFwglAnnualExamineMySqlMapper tblFwglAnnualExamineMySqlMapper;

	/**
	 * 年度考核列表 查询
	 * @param param
	 * @return
	 */
	@Override
	public PageInfo<TblFwglAnnualExamineMySql> getList(TblFwglAnnualExamineQueryParam param) {
		Example example = new Example(TblFwglAnnualExamineMySql.class);
		Example.Criteria criteria = example.createCriteria();
		if (StringUtils.isNotEmpty(param.getAnnualExamineName())) {
			criteria.andLike("annualExamineName","%" + param.getAnnualExamineName()+ "%");
		}
		if (StringUtils.isNotEmpty(param.getType())) {
			criteria.andEqualTo("type", param.getType());
		}
		if (param.getExamineBeginDate() != null) {
			criteria.andGreaterThanOrEqualTo("examineTime", param.getExamineBeginDate());
		}
		if (param.getExamineEndDate() != null) {
			criteria.andLessThan("examineTime", DateUtil.addDays(param.getExamineEndDate(), 1));
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
		example.setOrderByClause(" ANNUALEXAMINEID desc ");
		return PageMethod.startPage(param.getPageNumber(), param.getPageSize())
				.doSelectPageInfo(() -> tblFwglAnnualExamineMySqlMapper.selectByExample(example));
	}

	/**
	 * 年度考核 新增/更新
	 * @param param
	 * @return
	 */
	@Override
	public TblFwglAnnualExamineMySql saveOrUpdate(TblFwglAnnualExamineMySql param) {
		Date now = new Date();
		if (param.getAnnualExamineId() == null) {
			param.setState(YesNo.YES);
			param.setCreatedTime(now);
			param.setUpdatedTime(now);
			tblFwglAnnualExamineMySqlMapper.insertSelective(param);
		} else {
			if (idById(param.getAnnualExamineId())) {
				throw new ServiceException(400, 50001);
			}
			param.setCreatedTime(null);
			param.setUpdatedTime(now);
			tblFwglAnnualExamineMySqlMapper.updateByPrimaryKeySelective(param);
		}
		return findById(param.getAnnualExamineId());
	}

	/**
	 * 年度考核 删除
	 * @param id
	 */
	@Override
	public void delete(Integer id) {
		tblFwglAnnualExamineMySqlMapper.deleteByPrimaryKey(id);
	}

	/**
	 * 年度考核详情 查询
	 * @param id
	 * @return
	 */
	@Override
	public TblFwglAnnualExamineMySql findById(Integer id) {
		TblFwglAnnualExamineMySql annualExamine = tblFwglAnnualExamineMySqlMapper.selectByPrimaryKey(id);
		if (annualExamine == null) {
			throw new ServiceException(400, 50001);
		}
		return annualExamine;
	}

	/**
	 * 根据id查询 年度考核 是存在
	 * @param id
	 * @return
	 */
	private Boolean idById(Integer id) {
		int count = tblFwglAnnualExamineMySqlMapper.selectCount(TblFwglAnnualExamineMySql.ofId(id));
		if (count == 0) {
			return true;
		}
		return false;
	}
}
