package com.huabo.legal.oracle.service.impl;

import com.hbfk.util.redis.Random.RandomUtil;
import com.huabo.legal.constant.YesNo;
import com.huabo.legal.exception.ServiceException;
import com.huabo.legal.oracle.entity.TblFwglPerennialLawServiceGradeOracle;
import com.huabo.legal.oracle.mapper.TblFwglPerennialLawServiceGradeOracleMapper;
import com.huabo.legal.oracle.service.TblFwglPerennialLawServiceGradeOracleService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
public class TblFwglPerennialLawServiceGradeOracleServiceImpl implements TblFwglPerennialLawServiceGradeOracleService {

	@Resource
	private TblFwglPerennialLawServiceGradeOracleMapper tblFwglPerennialLawServiceGradeOracleMapper;

	/**
	 * 根据 常年法律服务-评分列表 查询
	 * @param gradeId
	 * @return
	 */
	@Override
	public List<TblFwglPerennialLawServiceGradeOracle> getList(String gradeId) {
		Example example = new Example(TblFwglPerennialLawServiceGradeOracle.class);
		Example.Criteria criteria = example.createCriteria();
		if (StringUtils.isNotBlank(gradeId)) {
			criteria.andEqualTo("gradeId", Arrays.asList(gradeId.split(",")));
		}
		example.setOrderByClause(" GRADEID desc ");
		return tblFwglPerennialLawServiceGradeOracleMapper.selectByExample(example);
	}

	/**
	 * 常年法律服务-评分 新增/更新
	 * @param param
	 * @return
	 */
	@Override
	public TblFwglPerennialLawServiceGradeOracle saveOrUpdate(TblFwglPerennialLawServiceGradeOracle param) {
		Date now = new Date();
		if (param.getGradeId() == null) {
			param.setGradeId(RandomUtil.uuLongId());
			param.setState(YesNo.YES);
			param.setCreatedTime(now);
			param.setUpdatedTime(now);
			tblFwglPerennialLawServiceGradeOracleMapper.insertSelective(param);
		} else {
			if (idById(param.getGradeId())) {
				throw new ServiceException(400, 50001);
			}
			param.setCreatedTime(null);
			param.setUpdatedTime(now);
			tblFwglPerennialLawServiceGradeOracleMapper.updateByPrimaryKeySelective(param);
		}
		return findById(param.getGradeId());
	}

	/**
	 * 常年法律服务-评分详情 查询
	 * @param id
	 * @return
	 */
	@Override
	public TblFwglPerennialLawServiceGradeOracle findById(Long id) {
		TblFwglPerennialLawServiceGradeOracle perennialLawServiceGrade = tblFwglPerennialLawServiceGradeOracleMapper.selectByPrimaryKey(id);
		if (perennialLawServiceGrade == null) {
			throw new ServiceException(400, 50001);
		}
		return perennialLawServiceGrade;
	}

	/**
	 * 常年法律服务-评分 删除
	 * @param id
	 */
	@Override
	public void delete(Long id) {
		tblFwglPerennialLawServiceGradeOracleMapper.deleteByPrimaryKey(id);
	}

	/**
	 * 根据id查询 常年法律服务-评分 是存在
	 * @param id
	 * @return
	 */
	private Boolean idById(Long id) {
		int count = tblFwglPerennialLawServiceGradeOracleMapper.selectCount(TblFwglPerennialLawServiceGradeOracle.ofId(id));
		if (count == 0) {
			return true;
		}
		return false;
	}
}
