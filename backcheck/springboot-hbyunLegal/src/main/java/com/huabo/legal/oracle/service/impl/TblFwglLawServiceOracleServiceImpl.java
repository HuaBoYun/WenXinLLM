package com.huabo.legal.oracle.service.impl;

import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
import com.hbfk.util.redis.Random.RandomUtil;
import com.huabo.legal.constant.YesNo;
import com.huabo.legal.exception.ServiceException;
import com.huabo.legal.oracle.entity.TblFwglLawServiceOracle;
import com.huabo.legal.oracle.mapper.TblFwglLawServiceOracleMapper;
import com.huabo.legal.oracle.service.TblFwglLawServiceOracleService;
import com.huabo.legal.oracle.service.TblStaffOracleService;
import com.huabo.legal.vo.param.TblFwglLawServiceQueryParam;
import com.vip.vjtools.vjkit.time.DateUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
public class TblFwglLawServiceOracleServiceImpl implements TblFwglLawServiceOracleService {

	@Resource
	private TblFwglLawServiceOracleMapper tblFwglLawServiceOracleMapper;
	@Resource
	private TblStaffOracleService tblStaffOracleService;

	/**
	 * 查询常年法律服务/专项法律服务列表
	 * @param param
	 * @return
	 */
	@Override
	public PageInfo<TblFwglLawServiceOracle> getList(TblFwglLawServiceQueryParam param) {

		Example example = new Example(TblFwglLawServiceOracle.class);
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
			List<Long> tblOrganizationAll = tblStaffOracleService.getTblOrganizationAll(Long.valueOf(param.getBelongGroup()));
			criteria.andIn("belongGroup", tblOrganizationAll);
			//			criteria.andCondition(" ( belongGroup in (select ORGID from TBL_ORGANIZATION where 1=1 start with ORGID= " + param.getBelongGroup()
			//					+ " and ORGTYPE!=0 AND " + "ORGTYPE<100  connect by prior orgid= FATHERORGID) or belongGroup = " + param.getBelongGroup()+") ");
		}
		if (StringUtils.isNotBlank(param.getWorkUnit())) {
			criteria.andEqualTo("workUnit", param.getWorkUnit());
		}
		if (StringUtils.isNotBlank(param.getWorkUnitId())) {
			criteria.andEqualTo("workUnitId", param.getWorkUnitId());
		}
		example.setOrderByClause(" LAWSERVICEID desc");
		return PageMethod.startPage(param.getPageNumber(), param.getPageSize())
				.doSelectPageInfo(() -> tblFwglLawServiceOracleMapper.selectByExample(example));
	}

	/**
	 * 常年法律服务/专项法律服务 新增/更新
	 * @param param
	 * @return
	 */
	@Override
	public TblFwglLawServiceOracle saveOrUpdate(TblFwglLawServiceOracle param) {
		Date now = new Date();
		if (param.getLawServiceId() == null) {
			param.setLawServiceId(RandomUtil.uuLongId());
			param.setState(YesNo.YES);
			param.setCreatedTime(now);
			param.setUpdatedTime(now);
			tblFwglLawServiceOracleMapper.insertSelective(param);
		} else {
			if (idById(param.getLawServiceId())) {
				throw new ServiceException(400, 50001);
			}
			param.setCreatedTime(null);
			param.setUpdatedTime(now);
			tblFwglLawServiceOracleMapper.updateByPrimaryKeySelective(param);
		}
		return findById(param.getLawServiceId());
	}

	/**
	 * 常年法律服务/专项法律服务 删除
	 * @param id
	 */
	@Override
	public void delete(Long id) {
		tblFwglLawServiceOracleMapper.deleteByPrimaryKey(id);
	}

	/**
	 * 常年法律服务/专项法律服务详情 查询
	 * @param id
	 * @return
	 */
	@Override
	public TblFwglLawServiceOracle findById(Long id) {
		TblFwglLawServiceOracle lawService = tblFwglLawServiceOracleMapper.selectByPrimaryKey(id);
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
	private Boolean idById(Long id) {
		int count = tblFwglLawServiceOracleMapper.selectCount(TblFwglLawServiceOracle.ofId(id));
		if (count == 0) {
			return true;
		}
		return false;
	}
}
