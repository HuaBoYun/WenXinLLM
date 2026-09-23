package com.huabo.legal.oracle.service.impl;

import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
import com.hbfk.util.redis.Random.RandomUtil;
import com.huabo.legal.constant.YesNo;
import com.huabo.legal.exception.ServiceException;
import com.huabo.legal.oracle.entity.TblFwglPracticeApplyOracle;
import com.huabo.legal.oracle.mapper.TblFwglPracticeApplyOracleMapper;
import com.huabo.legal.oracle.service.TblFwglPracticeApplyOracleService;
import com.huabo.legal.oracle.service.TblStaffOracleService;
import com.huabo.legal.vo.param.TblFwglParam;
import com.huabo.legal.vo.param.TblFwglPracticeApplyQueryParam;
import com.huabo.legal.vo.result.LegalCompanyLawyerResult;
import com.huabo.legal.vo.result.LegalPersonnelCardEmploymentRateResult;
import com.vip.vjtools.vjkit.time.DateUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
public class TblFwglPracticeApplyOracleServiceImpl implements TblFwglPracticeApplyOracleService {

	@Resource
	private TblFwglPracticeApplyOracleMapper tblFwglPracticeApplyOracleMapper;
	@Resource
	private TblStaffOracleService tblStaffOracleService;

	/**
	 * 执业申请列表 查询
	 * @param param
	 * @return
	 */
	@Override
	public PageInfo<TblFwglPracticeApplyOracle> getList(TblFwglPracticeApplyQueryParam param) {
		Example example = new Example(TblFwglPracticeApplyOracle.class);
		Example.Criteria criteria = example.createCriteria();

		if (null != param.getIszysq() && "0".equals(param.getIszysq())) {
			//人员台账
			criteria.andCondition("(certificatenumber is not null or certificatenumber != '')");
			criteria.andEqualTo("state", "6");
		} else {
			//执业申请
			//criteria.andCondition("(certificatenumber is null or certificatenumber = '')");
			criteria.andCondition(
					" ( (certificatenumber is null or certificatenumber = '')  or (certificatenumber is not null AND STATE!=6 or certificatenumber != '' AND STATE!=6) ) ");
		}
		if (param.getIsCertificationNumber() != null) {
			if (param.getIsCertificationNumber() == 1) {
				criteria.andCondition("certificationNumber is not null or certificationNumber != ''");
			}
			if (param.getIsCertificationNumber() == 0) {
				criteria.andCondition("(certificationNumber is null or certificationNumber = '')");
			}
		}
		if (param.getIsCertificateNumber() != null) {
			if (param.getIsCertificateNumber() == 1) {
				criteria.andCondition("certificateNumber is not null or certificateNumber != ''");
			}
			if (param.getIsCertificateNumber() == 0) {
				criteria.andCondition("certificateNumber is null or certificateNumber != ''");
			}
		}
		if (StringUtils.isNotEmpty(param.getPracticeApplyName())) {
			criteria.andLike("practiceApplyName", "%" + param.getPracticeApplyName() + "%");
		}
		if (param.getStartTime() != null) {
			criteria.andGreaterThanOrEqualTo("createdTime", param.getStartTime());
		}
		if (param.getEndTime() != null) {
			criteria.andLessThan("createdTime", DateUtil.addMonths(param.getEndTime(), 12));
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
		if (StringUtils.isNotBlank(param.getStaffId())) {
			criteria.andEqualTo("staffId", param.getStaffId());
		}
		example.setOrderByClause(" PRACTICEAPPLYID desc ");
		return PageMethod.startPage(param.getPageNumber(), param.getPageSize())
				.doSelectPageInfo(() -> tblFwglPracticeApplyOracleMapper.selectByExample(example));
	}

	/**
	 * 执业申请 新增/更新
	 * @param param
	 * @return
	 */
	@Override
	public TblFwglPracticeApplyOracle saveOrUpdate(TblFwglPracticeApplyOracle param) {
		Date now = new Date();
		if (param.getPracticeApplyId() == null) {
			param.setPracticeApplyId(RandomUtil.uuLongId());
			param.setState(YesNo.NO);//新增为0状态为未审批
			param.setCreatedTime(now);
			param.setUpdatedTime(now);
			tblFwglPracticeApplyOracleMapper.insertSelective(param);
		} else {
			if (idById(param.getPracticeApplyId())) {
				throw new ServiceException(400, 50001);
			}
			param.setCreatedTime(null);
			param.setUpdatedTime(now);
			tblFwglPracticeApplyOracleMapper.updateByPrimaryKeySelective(param);
		}
		return findById(param.getPracticeApplyId());
	}

	/**
	 * 执业申请 删除
	 * @param id
	 */
	@Override
	public void delete(Long id) {
		tblFwglPracticeApplyOracleMapper.deleteByPrimaryKey(id);
	}

	/**
	 * 执业申请详情 查询
	 * @param id
	 * @return
	 */
	@Override
	public TblFwglPracticeApplyOracle findById(Long id) {
		TblFwglPracticeApplyOracle practiceApply = tblFwglPracticeApplyOracleMapper.selectByPrimaryKey(id);
		if (practiceApply == null) {
			throw new ServiceException(400, 50001);
		}
		return practiceApply;
	}

	/**
	 * 法务人员持证上岗率
	 * @param param
	 */
	@Override
	public LegalPersonnelCardEmploymentRateResult getLegalPersonnelCardEmploymentRate(TblFwglParam param) {

		TblFwglPracticeApplyOracle practiceApplyOracle = new TblFwglPracticeApplyOracle();
		practiceApplyOracle.setBelongGroup(param.getBelongGroup());
		//法务人员持证上岗率 该集团 人数总数
		int total = tblFwglPracticeApplyOracleMapper.selectCount(practiceApplyOracle);
		//法务人员持证上岗率 该集团 持证人数
		List<Long> tblOrganizationAll = tblStaffOracleService.getTblOrganizationAll(Long.valueOf(param.getBelongGroup()));
		Integer count = tblFwglPracticeApplyOracleMapper.findLegalPersonnelCardEmploymentRate(param, tblOrganizationAll);
		return new LegalPersonnelCardEmploymentRateResult(count, total - count);
	}

	/**
	 * 公司律师人数占比
	 * @param param
	 * @return
	 */
	@Override
	public LegalPersonnelCardEmploymentRateResult getFirmLegalProportion(TblFwglParam param) {
		TblFwglPracticeApplyOracle practiceApplyOracle = new TblFwglPracticeApplyOracle();
		practiceApplyOracle.setBelongGroup(param.getBelongGroup());
		//公司律师人数占比 该集团 人数总数
		int total = tblFwglPracticeApplyOracleMapper.selectCount(practiceApplyOracle);
		//公司律师人数占比 该集团 公司律师
		List<Long> tblOrganizationAll = tblStaffOracleService.getTblOrganizationAll(Long.valueOf(param.getBelongGroup()));
		Integer count = tblFwglPracticeApplyOracleMapper.findFirmLegalProportion(param, tblOrganizationAll);
		return new LegalPersonnelCardEmploymentRateResult(count, total - count);
	}

	/**
	 * 公司律师人数
	 * @param param
	 * @return
	 */
	@Override
	public List<LegalCompanyLawyerResult> getLegalCompanyLawyer(TblFwglParam param) {
		List<Long> tblOrganizationAll = tblStaffOracleService.getTblOrganizationAll(Long.valueOf(param.getBelongGroup()));
		return tblFwglPracticeApplyOracleMapper.getLegalCompanyLawyer(param, tblOrganizationAll);
	}

	/**
	 * 根据id查询 执业申请 是存在
	 * @param id
	 * @return
	 */
	private Boolean idById(Long id) {
		int count = tblFwglPracticeApplyOracleMapper.selectCount(TblFwglPracticeApplyOracle.ofId(id));
		if (count == 0) {
			return true;
		}
		return false;
	}
}
