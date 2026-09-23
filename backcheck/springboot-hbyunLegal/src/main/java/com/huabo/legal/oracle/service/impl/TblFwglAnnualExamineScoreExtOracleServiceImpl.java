package com.huabo.legal.oracle.service.impl;

import com.hbfk.util.redis.Random.RandomUtil;
import com.huabo.legal.constant.YesNo;
import com.huabo.legal.exception.ServiceException;
import com.huabo.legal.oracle.entity.TblFwglAnnualExamineScoreExtOracle;
import com.huabo.legal.oracle.mapper.TblFwglAnnualExamineScoreExtOracleMapper;
import com.huabo.legal.oracle.service.TblFwglAnnualExamineScoreExtOracleService;
import com.huabo.legal.oracle.service.TblStaffOracleService;
import com.huabo.legal.util.SnowflakeIdWorker;
import com.huabo.legal.vo.param.TblFwglAnnualExamineScoreExtFileParam;
import com.huabo.legal.vo.param.TblFwglAnnualExamineScoreExtQueryParam;
import com.vip.vjtools.vjkit.collection.CollectionUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
public class TblFwglAnnualExamineScoreExtOracleServiceImpl implements TblFwglAnnualExamineScoreExtOracleService {

	private final SnowflakeIdWorker snowflakeIdWorker = new SnowflakeIdWorker(5, 5);

	@Resource
	private TblFwglAnnualExamineScoreExtOracleMapper tblFwglAnnualExamineScoreExtOracleMapper;
	@Resource
	private TblStaffOracleService tblStaffOracleService;

	/**
	 * 年度考核-考核评分列表 查询
	 * @param param
	 * @return
	 */
	@Override
	public List<TblFwglAnnualExamineScoreExtOracle> getList(TblFwglAnnualExamineScoreExtQueryParam param) {
		Example example = new Example(TblFwglAnnualExamineScoreExtOracle.class);
		Example.Criteria criteria = example.createCriteria();
		if (StringUtils.isNotBlank(param.getScoreTransaction())) {
			criteria.andEqualTo("scoreTransaction", param.getScoreTransaction());
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
		example.setOrderByClause(" ANNUALEXAMINESCOREEXTID desc ");
		return tblFwglAnnualExamineScoreExtOracleMapper.selectByExample(example);
	}

	/**
	 * 年度考核-考核评分 批量新增/更新
	 * @param param
	 * @return
	 */
	@Override
	@Transactional
	public String saveOrUpdate(List<TblFwglAnnualExamineScoreExtOracle> param) {
		long transaction = snowflakeIdWorker.nextId(); //雪花算法
		String transactionId = String.valueOf(transaction);
		//校验判断
		verify(param);
		//循环操作
		param.forEach(x -> {
			Date now = new Date();
			if (x.getAnnualExamineScoreExtId() == null) {
				x.setAnnualExamineScoreExtId(RandomUtil.uuLongId());
				x.setScoreTransaction(transactionId);
				x.setState(YesNo.YES);
				x.setCreatedTime(now);
				x.setUpdatedTime(now);
				tblFwglAnnualExamineScoreExtOracleMapper.insertSelective(x);
			} else {
				if (idById(x.getAnnualExamineScoreExtId())) {
					throw new ServiceException(400, 50001);
				}
				x.setScoreTransaction(transactionId);
				x.setCreatedTime(null);
				x.setUpdatedTime(now);
				tblFwglAnnualExamineScoreExtOracleMapper.updateByPrimaryKeySelective(x);
			}
		});
		return transactionId;
	}

	/**
	 * 年度考核-考核评分-附件-确定按钮
	 * @param param
	 * @return
	 */
	@Override
	public TblFwglAnnualExamineScoreExtOracle saveOrUpdate(TblFwglAnnualExamineScoreExtFileParam param) {

		TblFwglAnnualExamineScoreExtOracle annualExamineScoreExt = new TblFwglAnnualExamineScoreExtOracle();
		BeanUtils.copyProperties(param, annualExamineScoreExt);
		Date now = new Date();
		if (annualExamineScoreExt.getAnnualExamineScoreExtId() == null) {
			annualExamineScoreExt.setAnnualExamineScoreExtId(RandomUtil.uuLongId());
			annualExamineScoreExt.setState(YesNo.YES);
			annualExamineScoreExt.setCreatedTime(now);
			annualExamineScoreExt.setUpdatedTime(now);
			tblFwglAnnualExamineScoreExtOracleMapper.insertSelective(annualExamineScoreExt);
		} else {
			if (idById(annualExamineScoreExt.getAnnualExamineScoreExtId())) {
				throw new ServiceException(400, 50001);
			}
			annualExamineScoreExt.setCreatedTime(null);
			annualExamineScoreExt.setUpdatedTime(now);
			tblFwglAnnualExamineScoreExtOracleMapper.updateByPrimaryKeySelective(annualExamineScoreExt);
		}
		return findById(annualExamineScoreExt.getAnnualExamineScoreExtId());
	}

	/**
	 * 校验判断
	 * @param param
	 */
	private void verify(List<TblFwglAnnualExamineScoreExtOracle> param) {
		if (CollectionUtil.isEmpty(param)) {
			throw new ServiceException(400, "年度考核-考核评分 批量新增/更新 提交数据为空");
		}
		param.forEach(x -> {
			if (x.getAnnualExamineTopicExtId() == null) {
				throw new ServiceException(400, "annualExamineTopicExtId关联ID-年度考核题目ID,不能为空");
			}
			if (x.getExamineType() == null) {
				throw new ServiceException(400, "examineType考核类型 1-外部监管考核 2-子单位考核,不能为空");
			}
		});
	}

	/**
	 * 年度考核-考核评分 删除
	 * @param id
	 */
	@Override
	public void delete(Long id) {
		tblFwglAnnualExamineScoreExtOracleMapper.deleteByPrimaryKey(id);
	}

	/**
	 * 年度考核-考核评分 删除
	 * @param annualExamineId 年度考核ID
	 */
	@Override
	public void deleteTblFwglAnnualExamineScoreExt(Long annualExamineId) {
		tblFwglAnnualExamineScoreExtOracleMapper.delete(TblFwglAnnualExamineScoreExtOracle.ofDelete(annualExamineId));
	}

	/**
	 * 查询
	 * @return
	 */
	@Override
	public TblFwglAnnualExamineScoreExtOracle findById(Long id) {
		TblFwglAnnualExamineScoreExtOracle annualExamineScoreExt = tblFwglAnnualExamineScoreExtOracleMapper
				.selectOne(TblFwglAnnualExamineScoreExtOracle.ofId(id));
		if (annualExamineScoreExt == null) {
			throw new ServiceException(400, 50001);
		}
		return annualExamineScoreExt;
	}

	/**
	 * 根据id查询 年度计划 是存在
	 * @param id
	 * @return
	 */
	private Boolean idById(Long id) {
		int count = tblFwglAnnualExamineScoreExtOracleMapper.selectCount(TblFwglAnnualExamineScoreExtOracle.ofId(id));
		if (count == 0) {
			return true;
		}
		return false;
	}
}
