package com.huabo.legal.oracle.service.impl;

import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
import com.hbfk.util.redis.Random.RandomUtil;
import com.huabo.legal.constant.YesNo;
import com.huabo.legal.exception.ServiceException;
import com.huabo.legal.oracle.entity.TblFwglXfExamOracle;
import com.huabo.legal.oracle.mapper.TblFwglXfExamOracleMapper;
import com.huabo.legal.oracle.service.TblFwglXfExamOracleService;
import com.huabo.legal.oracle.service.TblStaffOracleService;
import com.huabo.legal.vo.param.TblFwglXfExamQueryParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
public class TblFwglXfExamOracleServiceImpl implements TblFwglXfExamOracleService {

	@Resource
	private TblFwglXfExamOracleMapper tblFwglXfExamOracleMapper;
	@Resource
	private TblStaffOracleService tblStaffOracleService;

	/**
	 * 学法考试列表 查询
	 * @param param
	 * @return
	 */
	@Override
	public PageInfo<TblFwglXfExamOracle> getList(TblFwglXfExamQueryParam param) {
		Example example = new Example(TblFwglXfExamOracle.class);
		Example.Criteria criteria = example.createCriteria();
		if (StringUtils.isNotEmpty(param.getExamName())) {
			criteria.andLike("examName", "%" + param.getExamName() + "%");
		}
		if (StringUtils.isNotEmpty(param.getType())) {
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
		example.setOrderByClause(" EXAMID desc ");
		return PageMethod.startPage(param.getPageNumber(), param.getPageSize())
				.doSelectPageInfo(() -> tblFwglXfExamOracleMapper.selectByExample(example));
	}

	/**
	 * 学法考试 新增/更新
	 * @param param
	 * @return
	 */
	@Override
	public TblFwglXfExamOracle saveOrUpdate(TblFwglXfExamOracle param) {
		Date now = new Date();
		if (param.getExamId() == null) {
			param.setExamId(RandomUtil.uuLongId());
			param.setState(YesNo.YES);
			param.setCreatedTime(now);
			param.setUpdatedTime(now);
			tblFwglXfExamOracleMapper.insertSelective(param);
		} else {
			if (idById(param.getExamId())) {
				throw new ServiceException(400, 50001);
			}
			param.setCreatedTime(null);
			param.setUpdatedTime(now);
			tblFwglXfExamOracleMapper.updateByPrimaryKeySelective(param);
		}
		return findById(param.getExamId());
	}

	/**
	 * 学法考试详情 查询
	 * @param id
	 * @return
	 */
	@Override
	public TblFwglXfExamOracle findById(Long id) {
		TblFwglXfExamOracle xfExam = tblFwglXfExamOracleMapper.selectByPrimaryKey(id);
		if (xfExam == null) {
			throw new ServiceException(400, 50001);
		}
		return xfExam;
	}

	/**
	 * 学法考试 刪除
	 * @param id
	 */
	@Override
	public void delete(Long id) {
		tblFwglXfExamOracleMapper.deleteByPrimaryKey(id);
	}

	/**
	 * 根据id查询 学法考试 是存在
	 * @param id
	 * @return
	 */
	private Boolean idById(Long id) {
		int count = tblFwglXfExamOracleMapper.selectCount(TblFwglXfExamOracle.ofId(id));
		if (count == 0) {
			return true;
		}
		return false;
	}
}
