package com.huabo.legal.oracle.service.impl;

import com.hbfk.util.redis.Random.RandomUtil;
import com.huabo.legal.constant.YesNo;
import com.huabo.legal.exception.ServiceException;
import com.huabo.legal.oracle.entity.TblFwglPracticeApplyExtOracle;
import com.huabo.legal.oracle.mapper.TblFwglPracticeApplyExtOracleMapper;
import com.huabo.legal.oracle.service.TblFwglPracticeApplyExtOracleService;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
public class TblFwglPracticeApplyExtOracleServiceImpl implements TblFwglPracticeApplyExtOracleService {

	@Resource
	private TblFwglPracticeApplyExtOracleMapper tblFwglPracticeApplyExtOracleMapper;

	/**
	 * 根据执业申请-简历ID 执业申请-简历列表 查询
	 * @param practiceApplyExtId 执业申请-简历ID
	 * @return
	 */
	@Override
	public List<TblFwglPracticeApplyExtOracle> getList(String practiceApplyExtId) {
		Example example = new Example(TblFwglPracticeApplyExtOracle.class);
		Example.Criteria criteria = example.createCriteria();
		if (practiceApplyExtId != null) {
			criteria.andIn("practiceApplyExtId", Arrays.asList(practiceApplyExtId.split(",")));
		}
		example.setOrderByClause(" PRACTICEAPPLYEXTID desc ");
		return tblFwglPracticeApplyExtOracleMapper.selectByExample(example);
	}

	/**
	 * 执业申请-简历 新增/更新
	 * @param param
	 * @return
	 */
	@Override
	public TblFwglPracticeApplyExtOracle saveOrUpdate(TblFwglPracticeApplyExtOracle param) {
		Date now = new Date();
		if (param.getPracticeApplyExtId() == null) {
			param.setPracticeApplyExtId(RandomUtil.uuLongId());
			param.setState(YesNo.YES);
			param.setCreatedTime(now);
			param.setUpdatedTime(now);
			tblFwglPracticeApplyExtOracleMapper.insertSelective(param);
		} else {
			if (idById(param.getPracticeApplyExtId())) {
				throw new ServiceException(400, 50001);
			}
			param.setCreatedTime(null);
			param.setUpdatedTime(now);
			tblFwglPracticeApplyExtOracleMapper.updateByPrimaryKeySelective(param);
		}
		return findById(param.getPracticeApplyExtId());
	}

	/**
	 * 执业申请-简历详情 查询
	 * @param id
	 * @return
	 */
	@Override
	public TblFwglPracticeApplyExtOracle findById(Long id) {
		TblFwglPracticeApplyExtOracle practiceApplyExt = tblFwglPracticeApplyExtOracleMapper.selectByPrimaryKey(id);
		if (practiceApplyExt == null) {
			throw new ServiceException(400, 50001);
		}
		return practiceApplyExt;
	}

	/**
	 * 执业申请-简历 删除
	 * @param id
	 */
	@Override
	public void delete(Long id) {
		tblFwglPracticeApplyExtOracleMapper.deleteByPrimaryKey(id);
	}

	/**
	 * 根据id查询 执业申请-简历 是存在
	 * @param id
	 * @return
	 */
	private Boolean idById(Long id) {
		int count = tblFwglPracticeApplyExtOracleMapper.selectCount(TblFwglPracticeApplyExtOracle.ofId(id));
		if (count == 0) {
			return true;
		}
		return false;
	}
}

