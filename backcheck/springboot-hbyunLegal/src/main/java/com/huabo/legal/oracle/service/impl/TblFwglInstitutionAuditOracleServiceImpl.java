package com.huabo.legal.oracle.service.impl;

import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
import com.hbfk.util.redis.Random.RandomUtil;
import com.huabo.legal.constant.AuditType;
import com.huabo.legal.constant.YesNo;
import com.huabo.legal.exception.ServiceException;
import com.huabo.legal.oracle.entity.TblFwglInstitutionAuditOracle;
import com.huabo.legal.oracle.mapper.TblFwglInstitutionAuditOracleMapper;
import com.huabo.legal.oracle.service.TblFwglInstitutionAuditOracleService;
import com.huabo.legal.oracle.service.TblStaffOracleService;
import com.huabo.legal.vo.param.TblFwglInstitutionAuditQueryParam;
import com.huabo.legal.vo.param.TblFwglParam;
import com.huabo.legal.vo.result.LegalInstitutionResult;
import com.huabo.legal.vo.result.TblFwglComplianceResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
public class TblFwglInstitutionAuditOracleServiceImpl implements TblFwglInstitutionAuditOracleService {

	@Resource
	private TblFwglInstitutionAuditOracleMapper tblFwglInstitutionAuditOracleMapper;
	@Resource
	private TblStaffOracleService tblStaffOracleService;

	/**
	 * 制度审核/经营事项审核列表 查询
	 * @param param
	 * @return
	 */
	@Override
	public PageInfo<TblFwglInstitutionAuditOracle> getList(TblFwglInstitutionAuditQueryParam param) {
		Example example = new Example(TblFwglInstitutionAuditOracle.class);
		Example.Criteria criteria = example.createCriteria();
		//		if (param.getType() != null) {
		//			criteria.andEqualTo("type", param.getType());
		//		}
		if (StringUtils.isNotEmpty(param.getInstitutionName())) {
			criteria.andLike("auditName", "%" + param.getInstitutionName() + "%");
		}
		if (param.getAuditType() != null) {
			criteria.andEqualTo("auditType", param.getAuditType());
		}
		if (param.getState() != null) {
			criteria.andEqualTo("state", param.getState());
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
		example.setOrderByClause(" INSTITUTIONAUDITID desc ");
		return PageMethod.startPage(param.getPageNumber(), param.getPageSize())
				.doSelectPageInfo(() -> tblFwglInstitutionAuditOracleMapper.selectByExample(example));
	}

	/**
	 * 制度审核/经营事项审核 新增/更新
	 * @param param
	 * @return
	 */
	@Override
	public TblFwglInstitutionAuditOracle saveOrUpdate(TblFwglInstitutionAuditOracle param) {
		Date now = new Date();
		if (param.getInstitutionAuditId() == null) {
			param.setInstitutionAuditId(RandomUtil.uuLongId());
			param.setStatus(0);
			param.setState(YesNo.NO);//新增初始状态为0,审批状态
			param.setCreatedTime(now);
			param.setUpdatedTime(now);
			tblFwglInstitutionAuditOracleMapper.insertSelective(param);
		} else {
			if (idById(param.getInstitutionAuditId())) {
				throw new ServiceException(400, 50001);
			}
			param.setCreatedTime(null);
			param.setUpdatedTime(now);
			tblFwglInstitutionAuditOracleMapper.updateByPrimaryKeySelective(param);
		}
		return findById(param.getInstitutionAuditId());
	}

	/**
	 * 制度审核/经营事项审核 删除
	 * @param id
	 */
	@Override
	public void delete(Long id) {
		tblFwglInstitutionAuditOracleMapper.deleteByPrimaryKey(id);
	}

	/**
	 * 制度审核/经营事项审核详情 查询
	 * @param id
	 * @return
	 */
	@Override
	public TblFwglInstitutionAuditOracle findById(Long id) {
		TblFwglInstitutionAuditOracle institutionAudit = tblFwglInstitutionAuditOracleMapper.selectByPrimaryKey(id);
		if (institutionAudit == null) {
			throw new ServiceException(400, 50001);
		}
		return institutionAudit;
	}

	/**
	 * 本年度制度法律合规审查数量
	 * @param beginOfYear
	 * @param endYear
	 * @return
	 */
	@Override
	public List<TblFwglComplianceResult> getInstitutionAuditCompliance(TblFwglParam param, Date beginOfYear, Date endYear) {
		List<Long> tblOrganizationAll = tblStaffOracleService.getTblOrganizationAll(Long.valueOf(param.getBelongGroup()));
		return tblFwglInstitutionAuditOracleMapper
				.findInstitutionAuditCompliance(AuditType.INSTITUTION_TYPE, beginOfYear, endYear, param, tblOrganizationAll);
	}

	/**
	 * 本年度制度法律合规审查数量
	 * @param beginOfYear
	 * @param endYear
	 * @return
	 */
	@Override
	public List<TblFwglComplianceResult> getBusinessInstitutionAuditCompliance(TblFwglParam param, Date beginOfYear, Date endYear) {
		List<Long> tblOrganizationAll = tblStaffOracleService.getTblOrganizationAll(Long.valueOf(param.getBelongGroup()));
		return tblFwglInstitutionAuditOracleMapper
				.findInstitutionAuditCompliance(AuditType.BUSINESS_INSTITUTION_TYPE, beginOfYear, endYear, param, tblOrganizationAll);
	}

	/**
	 * 全体系经营事项及制度审核数量
	 * @param param
	 * @return
	 */
	@Override
	public List<LegalInstitutionResult> getLegalInstitution(TblFwglParam param) {
		List<Long> tblOrganizationAll = tblStaffOracleService.getTblOrganizationAll(Long.valueOf(param.getBelongGroup()));
		return tblFwglInstitutionAuditOracleMapper.getLegalInstitution(param, tblOrganizationAll);
	}

	/**
	 * 根据id查询 制度审核/经营事项审核 是存在
	 * @param id
	 * @return
	 */
	private Boolean idById(Long id) {
		int count = tblFwglInstitutionAuditOracleMapper.selectCount(TblFwglInstitutionAuditOracle.ofId(id));
		if (count == 0) {
			return true;
		}
		return false;
	}
}
