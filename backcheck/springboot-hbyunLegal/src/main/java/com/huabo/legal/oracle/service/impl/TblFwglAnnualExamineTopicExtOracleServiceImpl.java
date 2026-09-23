package com.huabo.legal.oracle.service.impl;

import com.hbfk.util.redis.Random.RandomUtil;
import com.huabo.legal.constant.YesNo;
import com.huabo.legal.exception.ServiceException;
import com.huabo.legal.oracle.entity.TblFwglAnnualExamineTopicExtOracle;
import com.huabo.legal.oracle.mapper.TblFwglAnnualExamineTopicExtOracleMapper;
import com.huabo.legal.oracle.service.TblFwglAnnualExamineTopicExtOracleService;
import com.huabo.legal.vo.param.TblFwglAnnualExamineTopicExtBatchAdd;
import com.huabo.legal.vo.param.TblFwglAnnualExamineTopicExtQueryParam;
import com.vip.vjtools.vjkit.collection.CollectionUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class TblFwglAnnualExamineTopicExtOracleServiceImpl implements TblFwglAnnualExamineTopicExtOracleService {

	@Resource
	private TblFwglAnnualExamineTopicExtOracleMapper tblFwglAnnualExamineTopicExtOracleMapper;

	/**
	 * 年度考核-考核题目列表 查询
	 * @param param
	 * @return
	 */
	@Override
	@Deprecated
	public List<TblFwglAnnualExamineTopicExtOracle> getList(TblFwglAnnualExamineTopicExtQueryParam param) {
		Example example = new Example(TblFwglAnnualExamineTopicExtOracle.class);
		Example.Criteria criteria = example.createCriteria();
		if (param.getExamineType() != null) {
			criteria.andEqualTo("examineType", param.getExamineType());
		}
		//		if (StringUtils.isNotBlank(param.getCreator())) {
		//			criteria.andEqualTo("creator", param.getCreator());
		//		}
		//		if (StringUtils.isNotBlank(param.getBelongGroup())) {
		//			criteria.andEqualTo("belongGroup", param.getBelongGroup());
		//		}
		//		if (StringUtils.isNotBlank(param.getWorkUnit())) {
		//			criteria.andEqualTo("workUnit", param.getWorkUnit());
		//		}
		example.setOrderByClause(" ANNUALEXAMINETOPICEXTID desc ");
		return tblFwglAnnualExamineTopicExtOracleMapper.selectByExample(example);
	}

	/**
	 * 年度考核-考核题目 新增/更新
	 * @param param
	 * @return
	 */
	@Override
	@Deprecated
	public TblFwglAnnualExamineTopicExtOracle saveOrUpdate(TblFwglAnnualExamineTopicExtOracle param) {
		Date now = new Date();
		if (param.getAnnualExamineTopicExtId() == null) {
			param.setAnnualExamineTopicExtId(RandomUtil.uuLongId());
			param.setState(YesNo.YES);
			param.setCreatedTime(now);
			param.setUpdatedTime(now);
			tblFwglAnnualExamineTopicExtOracleMapper.insertSelective(param);
		} else {
			if (idById(param.getAnnualExamineTopicExtId())) {
				throw new ServiceException(400, 50001);
			}
			param.setCreatedTime(null);
			param.setUpdatedTime(now);
			tblFwglAnnualExamineTopicExtOracleMapper.updateByPrimaryKeySelective(param);
		}
		return findById(param.getAnnualExamineTopicExtId());
	}

	/**
	 * 年度考核-考核题目 新增/更新
	 * @param param
	 * @return
	 */
	@Override
	@Transactional
	@Deprecated
	public void saveOrUpdate(TblFwglAnnualExamineTopicExtBatchAdd param) {
		if (CollectionUtil.isEmpty(param.getList())) {
			log.warn("年度考核-考核题目 新增/更新 list为空");
			return;
		}
		//删除题目
		Date now = new Date();
		tblFwglAnnualExamineTopicExtOracleMapper.delete(TblFwglAnnualExamineTopicExtOracle.ofDelete(param.getExamineType()));
		param.getList().forEach(x -> {
			TblFwglAnnualExamineTopicExtOracle model = new TblFwglAnnualExamineTopicExtOracle();
			BeanUtils.copyProperties(x, model);
			model.setAnnualExamineTopicExtId(RandomUtil.uuLongId());
			model.setState(YesNo.YES);
			model.setCreatedTime(now);
			model.setUpdatedTime(now);
			tblFwglAnnualExamineTopicExtOracleMapper.insertSelective(model);
		});
	}

	/**
	 * 年度考核-考核题目 删除
	 * @param id
	 */
	@Override
	@Deprecated
	public void delete(Long id) {
		tblFwglAnnualExamineTopicExtOracleMapper.deleteByPrimaryKey(id);
	}

	/**
	 * 年度考核-考核题目详情 查询
	 * @param id
	 * @return
	 */
	@Override
	@Deprecated
	public TblFwglAnnualExamineTopicExtOracle findById(Long id) {
		TblFwglAnnualExamineTopicExtOracle annualExamineTopicExt = tblFwglAnnualExamineTopicExtOracleMapper.selectByPrimaryKey(id);
		if (annualExamineTopicExt == null) {
			throw new ServiceException(400, 50001);
		}
		return annualExamineTopicExt;
	}

	/**
	 * 根据id查询 年度计划 是存在
	 * @param id
	 * @return
	 */
	private Boolean idById(Long id) {
		int count = tblFwglAnnualExamineTopicExtOracleMapper.selectCount(TblFwglAnnualExamineTopicExtOracle.ofId(id));
		if (count == 0) {
			return true;
		}
		return false;
	}
}
