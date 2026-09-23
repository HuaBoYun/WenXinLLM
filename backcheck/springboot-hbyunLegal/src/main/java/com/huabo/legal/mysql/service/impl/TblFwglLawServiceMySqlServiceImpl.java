package com.huabo.legal.mysql.service.impl;

import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
import com.huabo.legal.constant.YesNo;
import com.huabo.legal.exception.ServiceException;
import com.huabo.legal.mysql.entity.TblFwglLawServiceMySql;
import com.huabo.legal.mysql.mapper.TblFwglLawServiceMySqlMapper;
import com.huabo.legal.mysql.service.TblFwglLawServiceMySqlService;
import com.huabo.legal.vo.param.TblFwglLawServiceQueryParam;
import com.vip.vjtools.vjkit.time.DateUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Date;

@Service
public class TblFwglLawServiceMySqlServiceImpl implements TblFwglLawServiceMySqlService {

	@Resource
	private TblFwglLawServiceMySqlMapper tblFwglLawServiceMySqlMapper;

	/**
	 * 查询常年法律服务/专项法律服务列表
	 * @param param
	 * @return
	 */
	@Override
	public PageInfo<TblFwglLawServiceMySql> getList(TblFwglLawServiceQueryParam param) {

		Example example = new Example(TblFwglLawServiceMySql.class);
		Example.Criteria criteria = example.createCriteria();
		if (param.getLawServiceType() != null) {
			criteria.andEqualTo("lawServiceType", param.getLawServiceType());
		}
		if (StringUtils.isNotBlank(param.getUnitName())) {
			criteria.andEqualTo("unitName", param.getUnitName());
		}
		if (param.getEmploymentTermType() != null) {
			criteria.andEqualTo("employmentTermType", param.getEmploymentTermType());
		}
		if (param.getState() != null) {
			criteria.andEqualTo("state", param.getState());
		}
		if (param.getFillInBeginDate() != null) {
			criteria.andGreaterThanOrEqualTo("fillInTime", param.getFillInBeginDate());
		}
		if (param.getFillInEndDate() != null) {
			criteria.andLessThan("fillInTime", DateUtil.addDays(param.getFillInEndDate(), 1));
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
		example.setOrderByClause(" LAWSERVICEID desc");
		return PageMethod.startPage(param.getPageNumber(), param.getPageSize())
				.doSelectPageInfo(() -> tblFwglLawServiceMySqlMapper.selectByExample(example));
	}

	/**
	 * 常年法律服务/专项法律服务 新增/更新
	 * @param param
	 * @return
	 */
	@Override
	public TblFwglLawServiceMySql saveOrUpdate(TblFwglLawServiceMySql param) {
		Date now = new Date();
		if (param.getLawServiceId() == null) {
			param.setState(YesNo.YES);
			param.setCreatedTime(now);
			param.setUpdatedTime(now);
			tblFwglLawServiceMySqlMapper.insertSelective(param);
		} else {
			if (idById(param.getLawServiceId())) {
				throw new ServiceException(400, 50001);
			}
			param.setCreatedTime(null);
			param.setUpdatedTime(now);
			tblFwglLawServiceMySqlMapper.updateByPrimaryKeySelective(param);
		}
		return findById(param.getLawServiceId());
	}

	/**
	 * 常年法律服务/专项法律服务 删除
	 * @param id
	 */
	@Override
	public void delete(Integer id) {
		tblFwglLawServiceMySqlMapper.deleteByPrimaryKey(id);
	}

	/**
	 * 常年法律服务/专项法律服务详情 查询
	 * @param id
	 * @return
	 */
	@Override
	public TblFwglLawServiceMySql findById(Integer id) {
		TblFwglLawServiceMySql lawService = tblFwglLawServiceMySqlMapper.selectByPrimaryKey(id);
		if (lawService == null) {
			throw new ServiceException(400, 50001);
		}
		return lawService;
	}

	/**
	 * 根据id查询 常年法律服务/专项法律服务 是存在
	 * @param id
	 * @return
	 */
	private Boolean idById(Integer id) {
		int count = tblFwglLawServiceMySqlMapper.selectCount(TblFwglLawServiceMySql.ofId(id));
		if (count == 0) {
			return true;
		}
		return false;
	}
}
