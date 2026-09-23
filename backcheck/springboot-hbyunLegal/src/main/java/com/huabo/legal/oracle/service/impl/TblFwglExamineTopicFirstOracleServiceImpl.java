package com.huabo.legal.oracle.service.impl;

import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
import com.hbfk.util.redis.Random.RandomUtil;
import com.huabo.legal.constant.YesNo;
import com.huabo.legal.exception.ServiceException;
import com.huabo.legal.oracle.entity.TblFwglExamineTopicFirstOracle;
import com.huabo.legal.oracle.mapper.TblFwglExamineTopicFirstOracleMapper;
import com.huabo.legal.oracle.service.TblFwglExamineTopicFirstOracleService;
import com.huabo.legal.vo.param.TblFwglExamineTopicFirstQueryParam;
import com.vip.vjtools.vjkit.collection.CollectionUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TblFwglExamineTopicFirstOracleServiceImpl implements TblFwglExamineTopicFirstOracleService {

	@Resource
	private TblFwglExamineTopicFirstOracleMapper tblFwglExamineTopicFirstOracleMapper;

	/**
	 * 年度考核题目-首部分 列表 查询
	 * @param param
	 * @return
	 */
	@Override
	public PageInfo<TblFwglExamineTopicFirstOracle> getList(TblFwglExamineTopicFirstQueryParam param) {
		Example example = new Example(TblFwglExamineTopicFirstOracle.class);
		Example.Criteria criteria = example.createCriteria();
		if (param.getExamineType() != null) {
			criteria.andEqualTo("examineType", param.getExamineType());
		}
		if (StringUtils.isNotBlank(param.getCreator())) {
			criteria.andEqualTo("creator", param.getCreator());
		}
		if (StringUtils.isNotBlank(param.getBelongGroup())) {
			criteria.andCondition(" ( belongGroup in (select ORGID from TBL_ORGANIZATION where 1=1 start with ORGID= " + param.getBelongGroup()
					+ " and ORGTYPE!=0 AND " + "ORGTYPE<100  connect by prior orgid= FATHERORGID) or belongGroup = " + param.getBelongGroup() + ") ");
		}
		if (param.getWorkUnit() != null) {
			criteria.andEqualTo("workUnit", param.getWorkUnit());
		}
		example.setOrderByClause(" id desc ");
		return PageMethod.startPage(param.getPageNumber(), param.getPageSize())
				.doSelectPageInfo(() -> tblFwglExamineTopicFirstOracleMapper.selectByExample(example));
	}

	/**
	 * 年度考核题目-首部分 新增/更新
	 * @param param
	 * @return
	 */
	@Override
	public TblFwglExamineTopicFirstOracle saveOrUpdate(TblFwglExamineTopicFirstOracle param) {
		Date now = new Date();
		if (param.getId() == null) {
			param.setId(RandomUtil.uuLongId());
			param.setState(YesNo.YES);
			param.setCreatedTime(now);
			param.setUpdatedTime(now);
			tblFwglExamineTopicFirstOracleMapper.insertSelective(param);
		} else {
			if (idById(param.getId())) {
				throw new ServiceException(400, 50001);
			}
			param.setCreatedTime(null);
			param.setUpdatedTime(now);
			tblFwglExamineTopicFirstOracleMapper.updateByPrimaryKeySelective(param);
		}
		return findById(param.getId());
	}

	/**
	 * 年度考核题目-首部分 删除
	 * @param id
	 */
	@Override
	public void delete(Long id) {
		tblFwglExamineTopicFirstOracleMapper.deleteByPrimaryKey(id);
	}

	/**
	 * 年度考核题目-首部分 详情 查询
	 * @param id
	 * @return
	 */
	@Override
	public TblFwglExamineTopicFirstOracle findById(Long id) {
		TblFwglExamineTopicFirstOracle examineTopicFirst = tblFwglExamineTopicFirstOracleMapper.selectByPrimaryKey(id);
		if (examineTopicFirst == null) {
			throw new ServiceException(400, 50001);
		}
		return examineTopicFirst;
	}

	/**
	 * 年度考核题目-首部分Map 列表 查询
	 * @return
	 */
	@Override
	public Map<Long, TblFwglExamineTopicFirstOracle> getMap() {
		List<TblFwglExamineTopicFirstOracle> examineTopicFirst = tblFwglExamineTopicFirstOracleMapper.selectAll();
		if (CollectionUtil.isEmpty(examineTopicFirst)) {
			return Collections.emptyMap();
		}
		return examineTopicFirst.stream().collect(Collectors.toMap(TblFwglExamineTopicFirstOracle::getId, x -> x));
	}

	/**
	 * 根据id查询 年度计划 是存在
	 * @param id
	 * @return
	 */
	private Boolean idById(Long id) {
		int count = tblFwglExamineTopicFirstOracleMapper.selectCount(TblFwglExamineTopicFirstOracle.ofId(id));
		if (count == 0) {
			return true;
		}
		return false;
	}
}
