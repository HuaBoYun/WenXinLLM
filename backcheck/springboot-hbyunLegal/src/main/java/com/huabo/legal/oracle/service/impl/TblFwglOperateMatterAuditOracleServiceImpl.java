package com.huabo.legal.oracle.service.impl;

import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
import com.hbfk.util.redis.Random.RandomUtil;
import com.huabo.legal.constant.YesNo;
import com.huabo.legal.exception.ServiceException;
import com.huabo.legal.oracle.entity.TblFwglInstitutionAuditOracle;
import com.huabo.legal.oracle.entity.TblFwglOperateMatterAuditOracle;
import com.huabo.legal.oracle.mapper.TblFwglOperateMatterAuditOracleMapper;
import com.huabo.legal.oracle.service.TblFwglOperateMatterAuditOracleService;
import com.huabo.legal.oracle.service.TblStaffOracleService;
import com.huabo.legal.vo.param.TblFwglOperateMatterAuditQueryParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
public class TblFwglOperateMatterAuditOracleServiceImpl implements TblFwglOperateMatterAuditOracleService {

	@Resource
	private TblFwglOperateMatterAuditOracleMapper tblFwglOperateMatterAuditOracleMapper;
	@Resource
	private TblStaffOracleService tblStaffOracleService;

	/**
	 * 经营事项审列表 查询
	 * @param param
	 * @return
	 */
	@Override
	public PageInfo<TblFwglOperateMatterAuditOracle> getList(TblFwglOperateMatterAuditQueryParam param) {
		Example example = new Example(TblFwglInstitutionAuditOracle.class);
		Example.Criteria criteria = example.createCriteria();
		if (StringUtils.isNotEmpty(param.getOperateMatterName())) {
			criteria.andLike("operateMatterName", "%" + param.getOperateMatterName() + "%");
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
		example.setOrderByClause(" id desc ");
		return PageMethod.startPage(param.getPageNumber(), param.getPageSize())
				.doSelectPageInfo(() -> tblFwglOperateMatterAuditOracleMapper.selectByExample(example));
	}

	/**
	 * 经营事项审 新增/更新
	 * @param param
	 * @return
	 */
	@Override
	public TblFwglOperateMatterAuditOracle saveOrUpdate(TblFwglOperateMatterAuditOracle param) {
		Date now = new Date();
		if (param.getOperateMatterAuditId() == null) {
			param.setOperateMatterAuditId(RandomUtil.uuLongId());
			param.setState(YesNo.YES);
			param.setCreatedTime(now);
			param.setUpdatedTime(now);
			tblFwglOperateMatterAuditOracleMapper.insertSelective(param);
		} else {
			if (idById(param.getOperateMatterAuditId())) {
				throw new ServiceException(400, 50001);
			}
			param.setCreatedTime(null);
			param.setUpdatedTime(now);
			tblFwglOperateMatterAuditOracleMapper.updateByPrimaryKeySelective(param);
		}
		return findById(param.getOperateMatterAuditId());
	}

	/**
	 * 经营事项审 删除
	 * @param id
	 */
	@Override
	public void delete(Long id) {
		tblFwglOperateMatterAuditOracleMapper.deleteByPrimaryKey(id);
	}

	/**
	 * 经营事项审详情 查询
	 * @param id
	 * @return
	 */
	@Override
	public TblFwglOperateMatterAuditOracle findById(Long id) {
		TblFwglOperateMatterAuditOracle operateMatterAudit = tblFwglOperateMatterAuditOracleMapper.selectByPrimaryKey(id);
		if (operateMatterAudit == null) {
			throw new ServiceException(400, 50001);
		}
		return operateMatterAudit;
	}

	/**
	 * 根据id查询 经营事项审 是存在
	 * @param id
	 * @return
	 */
	private Boolean idById(Long id) {
		int count = tblFwglOperateMatterAuditOracleMapper.selectCount(TblFwglOperateMatterAuditOracle.ofId(id));
		if (count == 0) {
			return true;
		}
		return false;
	}
}
