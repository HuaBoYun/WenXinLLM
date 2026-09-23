package com.huabo.legal.oracle.service.impl;

import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
import com.hbfk.util.redis.Random.RandomUtil;
import com.huabo.legal.constant.YesNo;
import com.huabo.legal.exception.ServiceException;
import com.huabo.legal.oracle.entity.TblFwglPracticeExamineOracle;
import com.huabo.legal.oracle.mapper.TblFwglPracticeExamineOracleMapper;
import com.huabo.legal.oracle.service.TblFwglPracticeExamineOracleService;
import com.huabo.legal.oracle.service.TblStaffOracleService;
import com.huabo.legal.vo.param.TblFwglPracticeExamineQueryParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
public class TblFwglPracticeExamineOracleServiceImpl implements TblFwglPracticeExamineOracleService {

	@Resource
	private TblFwglPracticeExamineOracleMapper tblFwglPracticeExamineOracleMapper;
	@Resource
	private TblStaffOracleService tblStaffOracleService;

	/**
	 * 执业考核列表 查询
	 * @param param
	 * @return
	 */
	@Override
	public PageInfo<TblFwglPracticeExamineOracle> getList(TblFwglPracticeExamineQueryParam param) {
		Example example = new Example(TblFwglPracticeExamineOracle.class);
		Example.Criteria criteria = example.createCriteria();
		if (StringUtils.isNotEmpty(param.getPracticeExamineName())) {
			criteria.andLike("practiceExamineName", "%" + param.getPracticeExamineName() + "%");
		}
		if (StringUtils.isNotEmpty(param.getPracticeExamineBelongGroup())) {
			criteria.andEqualTo("practiceExamineBelongGroup", param.getPracticeExamineBelongGroup());
		}
		if (param.getExamineGrade() != null) {
			criteria.andEqualTo("examineGrade", param.getExamineGrade());
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
		example.setOrderByClause(" PRACTICEEXAMINEID desc ");
		return PageMethod.startPage(param.getPageNumber(), param.getPageSize())
				.doSelectPageInfo(() -> tblFwglPracticeExamineOracleMapper.selectByExample(example));
	}

	/**
	 * 执业考核 新增/更新
	 * @param param
	 * @return
	 */
	@Override
	public TblFwglPracticeExamineOracle saveOrUpdate(TblFwglPracticeExamineOracle param) {
		Date now = new Date();
		if (param.getPracticeExamineId() == null) {
			param.setPracticeExamineId(RandomUtil.uuLongId());
			param.setState(YesNo.NO);
			param.setCreatedTime(now);
			param.setUpdatedTime(now);
			tblFwglPracticeExamineOracleMapper.insertSelective(param);
		} else {
			if (idById(param.getPracticeExamineId())) {
				throw new ServiceException(400, 50001);
			}
			param.setCreatedTime(null);
			param.setUpdatedTime(now);
			tblFwglPracticeExamineOracleMapper.updateByPrimaryKeySelective(param);
		}
		return findById(param.getPracticeExamineId());
	}

	/**
	 * 执业考核 删除
	 * @param id
	 */
	@Override
	public void delete(Long id) {
		tblFwglPracticeExamineOracleMapper.deleteByPrimaryKey(id);
	}

	/**
	 * 执业考核详情 查询
	 * @param id
	 * @return
	 */
	@Override
	public TblFwglPracticeExamineOracle findById(Long id) {
		TblFwglPracticeExamineOracle practiceExamine = tblFwglPracticeExamineOracleMapper.selectByPrimaryKey(id);
		if (practiceExamine == null) {
			throw new ServiceException(400, 50001);
		}
		return practiceExamine;
	}

	/**
	 * 根据id查询 执业考核 是存在
	 * @param id
	 * @return
	 */
	private Boolean idById(Long id) {
		int count = tblFwglPracticeExamineOracleMapper.selectCount(TblFwglPracticeExamineOracle.ofId(id));
		if (count == 0) {
			return true;
		}
		return false;
	}
}
