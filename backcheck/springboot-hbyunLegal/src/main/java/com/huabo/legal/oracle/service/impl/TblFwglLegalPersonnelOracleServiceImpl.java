package com.huabo.legal.oracle.service.impl;

import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
import com.hbfk.util.redis.Random.RandomUtil;
import com.huabo.legal.constant.YesNo;
import com.huabo.legal.exception.ServiceException;
import com.huabo.legal.oracle.entity.TblFwglLegalPersonnelOracle;
import com.huabo.legal.oracle.mapper.TblFwglLegalPersonnelOracleMapper;
import com.huabo.legal.oracle.service.TblFwglLegalPersonnelOracleService;
import com.huabo.legal.oracle.service.TblStaffOracleService;
import com.huabo.legal.vo.param.TblFwglLegalPersonnelQueryParam;
import com.huabo.legal.vo.param.TblFwglParam;
import com.huabo.legal.vo.result.LegalFullimePercentageResult;
import com.huabo.legal.vo.result.LegalPersonnelCountResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
public class TblFwglLegalPersonnelOracleServiceImpl implements TblFwglLegalPersonnelOracleService {

	@Resource
	private TblFwglLegalPersonnelOracleMapper tblFwglLegalPersonnelOracleMapper;
	@Resource
	private TblStaffOracleService tblStaffOracleService;

	/**
	 * 法务人员列表 查询
	 * @param param
	 * @return
	 */
	@Override
	public PageInfo<TblFwglLegalPersonnelOracle> getList(TblFwglLegalPersonnelQueryParam param) {
		Example example = new Example(TblFwglLegalPersonnelOracle.class);
		Example.Criteria criteria = example.createCriteria();
		if (StringUtils.isNotEmpty(param.getPersonnelName())) {
			criteria.andLike("personnelName", "%" + param.getPersonnelName() + "%");
		}
		if (StringUtils.isNotEmpty(param.getPosition())) {
			criteria.andLike("position", "%" + param.getPosition() + "%");
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
		example.setOrderByClause("PERSONNELID desc ");
		return PageMethod.startPage(param.getPageNumber(), param.getPageSize())
				.doSelectPageInfo(() -> tblFwglLegalPersonnelOracleMapper.selectByExample(example));
	}

	/**
	 * 法务人员 新增/更新
	 * @param param
	 * @return
	 */
	@Override
	public TblFwglLegalPersonnelOracle saveOrUpdate(TblFwglLegalPersonnelOracle param) {
		Date now = new Date();
		if (param.getPersonnelId() == null) {
			param.setPersonnelId(RandomUtil.uuLongId());
			param.setState(YesNo.NO);//新增为0状态为未审批
			param.setCreatedTime(now);
			param.setUpdatedTime(now);
			tblFwglLegalPersonnelOracleMapper.insertSelective(param);
		} else {
			if (idById(param.getPersonnelId())) {
				throw new ServiceException(400, 50001);
			}
			param.setCreatedTime(null);
			param.setUpdatedTime(now);
			tblFwglLegalPersonnelOracleMapper.updateByPrimaryKeySelective(param);
		}
		return findById(param.getPersonnelId());
	}

	/**
	 * 法务人员详情 查询
	 * @param id
	 * @return
	 */
	@Override
	public TblFwglLegalPersonnelOracle findById(Long id) {
		TblFwglLegalPersonnelOracle legalPersonnel = tblFwglLegalPersonnelOracleMapper.selectByPrimaryKey(id);
		if (legalPersonnel == null) {
			throw new ServiceException(400, 50001);
		}
		return legalPersonnel;
	}

	/**
	 * 法务人员 刪除
	 * @param id
	 */
	@Override
	public void delete(Long id) {
		tblFwglLegalPersonnelOracleMapper.deleteByPrimaryKey(id);
	}

	/**
	 * 法务人员数量
	 * @param param
	 * @return
	 */
	@Override
	public List<LegalPersonnelCountResult> getLegalPersonnelCount(TblFwglParam param) {
		List<Long> tblOrganizationAll = tblStaffOracleService.getTblOrganizationAll(Long.valueOf(param.getBelongGroup()));
		return tblFwglLegalPersonnelOracleMapper.getLegalPersonnelCount(param, tblOrganizationAll);
	}

	/**
	 * 法务人数数量-专职法务人鱼/兼职法务人 占比
	 * @param param
	 * @return
	 */
	@Override
	public LegalFullimePercentageResult getLegalFullimePercentage(TblFwglParam param) {
		List<Long> tblOrganizationAll = tblStaffOracleService.getTblOrganizationAll(Long.valueOf(param.getBelongGroup()));
		return tblFwglLegalPersonnelOracleMapper.getLegalFullimePercentage(param, tblOrganizationAll);
	}

	/**
	 * 总人数
	 * @param param
	 * @return
	 */
	@Override
	public LegalFullimePercentageResult getCount(TblFwglParam param) {
		List<Long> tblOrganizationAll = tblStaffOracleService.getTblOrganizationAll(Long.valueOf(param.getBelongGroup()));
		return tblFwglLegalPersonnelOracleMapper.getCount(param, tblOrganizationAll);
	}

	/**
	 * 根据id查询 法务人员 是存在
	 * @param id
	 * @return
	 */
	private Boolean idById(Long id) {
		int count = tblFwglLegalPersonnelOracleMapper.selectCount(TblFwglLegalPersonnelOracle.ofId(id));
		if (count == 0) {
			return true;
		}
		return false;
	}
}
