package com.huabo.legal.mysql.service.impl;

import com.huabo.legal.constant.YesNo;
import com.huabo.legal.exception.ServiceException;
import com.huabo.legal.mysql.entity.TblFwglAnnualExamineScoreExtMySql;
import com.huabo.legal.mysql.mapper.TblFwglAnnualExamineScoreExtMySqlMapper;
import com.huabo.legal.mysql.service.TblFwglAnnualExamineScoreExtMySqlService;
import com.huabo.legal.util.SnowflakeIdWorker;
import com.huabo.legal.vo.param.TblFwglAnnualExamineScoreExtQueryParam;
import com.vip.vjtools.vjkit.collection.CollectionUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class TblFwglAnnualExamineScoreExtMySqlServiceImpl implements TblFwglAnnualExamineScoreExtMySqlService {

	private final SnowflakeIdWorker snowflakeIdWorker = new SnowflakeIdWorker(5, 5);

	@Resource
	private TblFwglAnnualExamineScoreExtMySqlMapper tblFwglAnnualExamineScoreExtMySqlMapper;

	/**
	 * 年度考核-考核评分列表 查询
	 * @param param
	 * @return
	 */
	@Override
	public List<TblFwglAnnualExamineScoreExtMySql> getList(TblFwglAnnualExamineScoreExtQueryParam param) {
		Example example = new Example(TblFwglAnnualExamineScoreExtMySql.class);
		Example.Criteria criteria = example.createCriteria();
		if (StringUtils.isNotBlank(param.getScoreTransaction())) {
			criteria.andEqualTo("scoreTransaction", param.getScoreTransaction());
		}
		if (StringUtils.isNotBlank(param.getCreator())) {
			criteria.andEqualTo("creator", param.getCreator());
		}
		if (StringUtils.isNotBlank(param.getBelongGroup())) {
			criteria.andEqualTo("belongGroup", param.getBelongGroup());
		}
		if (StringUtils.isNotBlank(param.getWorkUnit())) {
			criteria.andEqualTo("workUnit", param.getWorkUnit());
		}
		example.setOrderByClause(" ANNUALEXAMINESCOREEXTID desc ");
		return tblFwglAnnualExamineScoreExtMySqlMapper.selectByExample(example);
	}

	/**
	 * 年度考核-考核评分 批量新增/更新
	 * @param param
	 * @return
	 */
	@Override
	@Transactional
	public String saveOrUpdate(List<TblFwglAnnualExamineScoreExtMySql> param) {
		long transaction = snowflakeIdWorker.nextId(); //雪花算法
		String transactionId = String.valueOf(transaction);
		AtomicBoolean flag = new AtomicBoolean(true);
		//校验判断
		verify(param);
		//循环操作
		param.forEach(x -> {
			Date now = new Date();
			if (x.getAnnualExamineScoreExtId() == null) {
				x.setScoreTransaction(transactionId);
				x.setState(YesNo.YES);
				x.setCreatedTime(now);
				x.setUpdatedTime(now);
				tblFwglAnnualExamineScoreExtMySqlMapper.insertSelective(x);
			} else {
				if (idById(x.getAnnualExamineScoreExtId())) {
					throw new ServiceException(400, 50001);
				}
				flag.set(false);
				x.setCreatedTime(null);
				x.setUpdatedTime(now);
				tblFwglAnnualExamineScoreExtMySqlMapper.updateByPrimaryKeySelective(x);
			}
		});
		//新增返回  更新返回
		if (flag.get()) {
			return transactionId;
		}
		return null;
	}

	/**
	 * 校验判断
	 * @param param
	 */
	private void verify(List<TblFwglAnnualExamineScoreExtMySql> param) {
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
	public void delete(Integer id) {
		tblFwglAnnualExamineScoreExtMySqlMapper.deleteByPrimaryKey(id);
	}

	/**
	 * 年度考核-考核评分 删除
	 * @param annualExamineId 年度考核ID
	 */
	@Override
	public void deleteTblFwglAnnualExamineScoreExt(Integer annualExamineId) {
		tblFwglAnnualExamineScoreExtMySqlMapper.delete(TblFwglAnnualExamineScoreExtMySql.ofDelete(annualExamineId));
	}

	/**
	 * 根据id查询 年度计划 是存在
	 * @param id
	 * @return
	 */
	private Boolean idById(Integer id) {
		int count = tblFwglAnnualExamineScoreExtMySqlMapper.selectCount(TblFwglAnnualExamineScoreExtMySql.ofId(id));
		if (count == 0) {
			return true;
		}
		return false;
	}
}
