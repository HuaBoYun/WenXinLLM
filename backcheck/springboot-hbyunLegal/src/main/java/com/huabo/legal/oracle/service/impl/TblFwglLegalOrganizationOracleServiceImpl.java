package com.huabo.legal.oracle.service.impl;

import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
import com.hbfk.util.redis.Random.RandomUtil;
import com.huabo.legal.constant.YesNo;
import com.huabo.legal.exception.ServiceException;
import com.huabo.legal.oracle.entity.TblFwglLegalOrganizationOracle;
import com.huabo.legal.oracle.mapper.TblFwglLegalOrganizationOracleMapper;
import com.huabo.legal.oracle.service.TblFwglLegalOrganizationOracleService;
import com.huabo.legal.oracle.service.TblStaffOracleService;
import com.huabo.legal.vo.param.TblFwglLegalOrganizationQueryParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
public class TblFwglLegalOrganizationOracleServiceImpl implements TblFwglLegalOrganizationOracleService {

	@Resource
	private TblFwglLegalOrganizationOracleMapper tblFwglLegalOrganizationOracleMapper;
	@Resource
	private TblStaffOracleService tblStaffOracleService;

	/**
	 * 法务机构及负责人列表 查询
	 * @param param
	 * @return
	 */
	@Override
	public PageInfo<TblFwglLegalOrganizationOracle> getList(TblFwglLegalOrganizationQueryParam param) {
		Example example = new Example(TblFwglLegalOrganizationOracle.class);
		Example.Criteria criteria = example.createCriteria();
		if (StringUtils.isNotBlank(param.getResponsiblePerson())) {
			criteria.andLike("responsiblePerson", "%" + param.getResponsiblePerson() + "%");
		}
		if (StringUtils.isNotBlank(param.getContact())) {
			criteria.andLike("contact", "%" + param.getContact() + "%");
		}
		if (StringUtils.isNotBlank(param.getOrganizationName())) {
			criteria.andLike("organizationName", "%" + param.getOrganizationName() + "%");
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
		example.setOrderByClause(" ORGANIZATIONID desc ");
		return PageMethod.startPage(param.getPageNumber(), param.getPageSize())
				.doSelectPageInfo(() -> tblFwglLegalOrganizationOracleMapper.selectByExample(example));
	}

	/**
	 * 法务机构及负责人 新增/更新
	 * @param param
	 * @return
	 */
	@Override
	public TblFwglLegalOrganizationOracle saveOrUpdate(TblFwglLegalOrganizationOracle param) {
		Date now = new Date();
		if (param.getOrganizationId() == null) {
			param.setOrganizationId(RandomUtil.uuLongId());
			param.setState(YesNo.NO);//新增初始状态为0
			param.setCreatedTime(now);
			param.setUpdatedTime(now);
			tblFwglLegalOrganizationOracleMapper.insertSelective(param);
		} else {
			if (idById(param.getOrganizationId())) {
				throw new ServiceException(400, 50001);
			}
			param.setCreatedTime(null);
			param.setUpdatedTime(now);
			tblFwglLegalOrganizationOracleMapper.updateByPrimaryKeySelective(param);
		}
		return findById(param.getOrganizationId());
	}

	/**
	 * 法务机构及负责人详情 查询
	 * @param id
	 * @return
	 */
	@Override
	public TblFwglLegalOrganizationOracle findById(Long id) {
		TblFwglLegalOrganizationOracle legalOrganization = tblFwglLegalOrganizationOracleMapper.selectByPrimaryKey(id);
		if (legalOrganization == null) {
			throw new ServiceException(400, 50001);
		}
		return legalOrganization;
	}

	/**
	 * 法务机构及负责人 刪除
	 * @param id
	 */
	@Override
	public void delete(Long id) {
		tblFwglLegalOrganizationOracleMapper.deleteByPrimaryKey(id);
	}

	/**
	 * 根据id查询 法务机构及负责人 是存在
	 * @param id
	 * @return
	 */
	private Boolean idById(Long id) {
		int count = tblFwglLegalOrganizationOracleMapper.selectCount(TblFwglLegalOrganizationOracle.ofId(id));
		if (count == 0) {
			return true;
		}
		return false;
	}
}
