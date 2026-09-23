package com.huabo.legal.oracle.service.impl;

import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
import com.hbfk.util.redis.Random.RandomUtil;
import com.huabo.legal.constant.YesNo;
import com.huabo.legal.exception.ServiceException;
import com.huabo.legal.oracle.entity.TblFwglSubjectManagementOracle;
import com.huabo.legal.oracle.mapper.TblFwglSubjectManagementOracleMapper;
import com.huabo.legal.oracle.service.TblFwglSubjectManagementOracleService;
import com.huabo.legal.oracle.service.TblStaffOracleService;
import com.huabo.legal.vo.param.TblFwglSubjectManagementQueryParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
public class TblFwglSubjectManagementOracleServiceImpl implements TblFwglSubjectManagementOracleService {

	@Resource
	private TblFwglSubjectManagementOracleMapper tblFwglSubjectManagementOracleMapper;
	@Resource
	private TblStaffOracleService tblStaffOracleService;

	/**
	 * 课题管理列表 查询
	 * @param param
	 * @return
	 */
	@Override
	public PageInfo<TblFwglSubjectManagementOracle> getList(TblFwglSubjectManagementQueryParam param) {
		Example example = new Example(TblFwglSubjectManagementOracle.class);
		Example.Criteria criteria = example.createCriteria();
		if (StringUtils.isNotEmpty(param.getSubjectManagementName())) {
			criteria.andLike("subjectManagementName", "%" + param.getSubjectManagementName() + "%");
		}
		if (param.getType() != null) {
			criteria.andEqualTo("type", param.getType());
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
		example.setOrderByClause(" SUBJECTMANAGEMENTID desc ");
		return PageMethod.startPage(param.getPageNumber(), param.getPageSize())
				.doSelectPageInfo(() -> tblFwglSubjectManagementOracleMapper.selectByExample(example));
	}

	/**
	 * 课题管理 新增/更新
	 * @param param
	 * @return
	 */
	@Override
	public TblFwglSubjectManagementOracle saveOrUpdate(TblFwglSubjectManagementOracle param) {
		Date now = new Date();
		if (param.getSubjectManagementId() == null) {
			param.setSubjectManagementId(RandomUtil.uuLongId());
			param.setState(YesNo.YES);
			param.setCreatedTime(now);
			param.setUpdatedTime(now);
			tblFwglSubjectManagementOracleMapper.insertSelective(param);
		} else {
			if (idById(param.getSubjectManagementId())) {
				throw new ServiceException(400, 50001);
			}
			param.setCreatedTime(null);
			param.setUpdatedTime(now);
			tblFwglSubjectManagementOracleMapper.updateByPrimaryKeySelective(param);
		}
		return findById(param.getSubjectManagementId());
	}

	/**
	 * 课题管理详情 查询
	 * @param id
	 * @return
	 */
	@Override
	public TblFwglSubjectManagementOracle findById(Long id) {
		TblFwglSubjectManagementOracle subjectManagement = tblFwglSubjectManagementOracleMapper.selectByPrimaryKey(id);
		if (subjectManagement == null) {
			throw new ServiceException(400, 50001);
		}
		return subjectManagement;
	}

	/**
	 * 课题管理 刪除
	 * @param id
	 */
	@Override
	public void delete(Long id) {
		tblFwglSubjectManagementOracleMapper.deleteByPrimaryKey(id);
	}

	/**
	 * 根据id查询 课题管理 是存在
	 * @param id
	 * @return
	 */
	private Boolean idById(Long id) {
		int count = tblFwglSubjectManagementOracleMapper.selectCount(TblFwglSubjectManagementOracle.ofId(id));
		if (count == 0) {
			return true;
		}
		return false;
	}
}
