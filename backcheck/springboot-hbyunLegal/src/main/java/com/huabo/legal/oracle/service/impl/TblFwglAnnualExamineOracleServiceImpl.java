package com.huabo.legal.oracle.service.impl;

import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
import com.hbfk.util.redis.Random.RandomUtil;
import com.huabo.legal.constant.YesNo;
import com.huabo.legal.exception.ServiceException;
import com.huabo.legal.oracle.entity.TblFwglAnnualExamineOracle;
import com.huabo.legal.oracle.mapper.TblFwglAnnualExamineOracleMapper;
import com.huabo.legal.oracle.service.TblFwglAnnualExamineOracleService;
import com.huabo.legal.oracle.service.TblStaffOracleService;
import com.huabo.legal.vo.param.TblFwglAnnualExamineQueryParam;
import com.vip.vjtools.vjkit.time.DateUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
public class TblFwglAnnualExamineOracleServiceImpl implements TblFwglAnnualExamineOracleService {

	@Resource
	private TblFwglAnnualExamineOracleMapper tblFwglAnnualExamineOracleMapper;
	@Resource
	private TblStaffOracleService tblStaffOracleService;

	/**
	 * 年度考核列表 查询
	 * @param param
	 * @return
	 */
	@Override
	public PageInfo<TblFwglAnnualExamineOracle> getList(TblFwglAnnualExamineQueryParam param) {
		Example example = new Example(TblFwglAnnualExamineOracle.class);
		Example.Criteria criteria = example.createCriteria();
		if (StringUtils.isNotEmpty(param.getAnnualExamineName())) {
			criteria.andLike("annualExamineName", "%" + param.getAnnualExamineName() + "%");
		}
		if (StringUtils.isNotEmpty(param.getType())) {
			criteria.andEqualTo("type", param.getType());
		}
		if (param.getExamineBeginDate() != null) {
			criteria.andGreaterThanOrEqualTo("examineTime", param.getExamineBeginDate());
		}
		if (param.getExamineEndDate() != null) {
			criteria.andLessThan("examineTime", DateUtil.addDays(param.getExamineEndDate(), 1));
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
		example.setOrderByClause(" ANNUALEXAMINEID desc ");
		return PageMethod.startPage(param.getPageNumber(), param.getPageSize())
				.doSelectPageInfo(() -> tblFwglAnnualExamineOracleMapper.selectByExample(example));
	}

	/**
	 * 年度考核 新增/更新
	 * @param param
	 * @return
	 */
	@Override
	public TblFwglAnnualExamineOracle saveOrUpdate(TblFwglAnnualExamineOracle param) {
		Date now = new Date();
		if (param.getAnnualExamineId() == null) {
			param.setAnnualExamineId(RandomUtil.uuLongId());
			param.setState(YesNo.NO);//新增为0状态为未审批
			param.setCreatedTime(now);
			param.setUpdatedTime(now);
			tblFwglAnnualExamineOracleMapper.insertSelective(param);
		} else {
			if (idById(param.getAnnualExamineId())) {
				throw new ServiceException(400, 50001);
			}
			param.setCreatedTime(null);
			param.setUpdatedTime(now);
			tblFwglAnnualExamineOracleMapper.updateByPrimaryKeySelective(param);
		}
		return findById(param.getAnnualExamineId());
	}

	/**
	 * 年度考核 删除
	 * @param id
	 */
	@Override
	public void delete(Long id) {
		tblFwglAnnualExamineOracleMapper.deleteByPrimaryKey(id);
	}

	/**
	 * 年度考核详情 查询
	 * @param id
	 * @return
	 */
	@Override
	public TblFwglAnnualExamineOracle findById(Long id) {
		TblFwglAnnualExamineOracle annualExamine = tblFwglAnnualExamineOracleMapper.selectByPrimaryKey(id);
		if (annualExamine == null) {
			throw new ServiceException(400, 50001);
		}
		return annualExamine;
	}

	/**
	 * 判断题目事务id是否存在
	 * @param topicTransactionId
	 * @return
	 */
	@Override
	public Boolean isTopicTransactionId(String topicTransactionId) {
		int count = tblFwglAnnualExamineOracleMapper.selectCount(TblFwglAnnualExamineOracle.ofTopicTransactionId(topicTransactionId));
		if (count > 0) {
			return true;
		}
		return false;
	}

	/**
	 * 根据id查询 年度考核 是存在
	 * @param id
	 * @return
	 */
	private Boolean idById(Long id) {
		int count = tblFwglAnnualExamineOracleMapper.selectCount(TblFwglAnnualExamineOracle.ofId(id));
		if (count == 0) {
			return true;
		}
		return false;
	}
}
