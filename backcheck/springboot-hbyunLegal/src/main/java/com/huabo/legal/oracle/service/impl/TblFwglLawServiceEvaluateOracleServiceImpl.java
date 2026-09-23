package com.huabo.legal.oracle.service.impl;

import com.hbfk.util.redis.Random.RandomUtil;
import com.huabo.legal.constant.YesNo;
import com.huabo.legal.exception.ServiceException;
import com.huabo.legal.oracle.entity.TblFwglLawServiceEvaluateOracle;
import com.huabo.legal.oracle.mapper.TblFwglLawServiceEvaluateOracleMapper;
import com.huabo.legal.oracle.service.TblFwglLawServiceEvaluateOracleService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
public class TblFwglLawServiceEvaluateOracleServiceImpl implements TblFwglLawServiceEvaluateOracleService {

	@Resource
	private TblFwglLawServiceEvaluateOracleMapper tblFwglLawServiceEvaluateOracleMapper;

	/**
	 * 常年法律服务-评价 列表 查询
	 * @param ids
	 * @return
	 */
	@Override
	public List<TblFwglLawServiceEvaluateOracle> getList(String ids) {
		Example example = new Example(TblFwglLawServiceEvaluateOracle.class);
		Example.Criteria criteria = example.createCriteria();
		if (StringUtils.isNotBlank(ids)) {
			criteria.andIn("id", Arrays.asList(ids.split(",")));
		}
		example.setOrderByClause(" id desc ");
		return tblFwglLawServiceEvaluateOracleMapper.selectByExample(example);
	}

	/**
	 * 常年法律服务-评价 新增/更新
	 * @param param
	 * @return
	 */
	@Override
	public TblFwglLawServiceEvaluateOracle saveOrUpdate(TblFwglLawServiceEvaluateOracle param) {
		Date now = new Date();
		if (param.getId() == null) {
			param.setId(RandomUtil.uuLongId());
			param.setState(YesNo.YES);
			param.setCreatedTime(now);
			param.setUpdatedTime(now);
			tblFwglLawServiceEvaluateOracleMapper.insertSelective(param);
		} else {
			if (idById(param.getId())) {
				throw new ServiceException(400, 50001);
			}
			param.setCreatedTime(null);
			param.setUpdatedTime(now);
			tblFwglLawServiceEvaluateOracleMapper.updateByPrimaryKeySelective(param);
		}
		return findById(param.getId());
	}

	/**
	 * 常年法律服务-评价 删除
	 * @param id
	 */
	@Override
	public void delete(Long id) {
		tblFwglLawServiceEvaluateOracleMapper.deleteByPrimaryKey(id);
	}

	/**
	 * 常年法律服务-评价 查询
	 * @param id
	 * @return
	 */
	@Override
	public TblFwglLawServiceEvaluateOracle findById(Long id) {
		TblFwglLawServiceEvaluateOracle lawServiceEvaluate = tblFwglLawServiceEvaluateOracleMapper.selectByPrimaryKey(id);
		if (lawServiceEvaluate == null) {
			throw new ServiceException(400, 50001);
		}
		return lawServiceEvaluate;
	}

	/**
	 * 根据id判断是否存在
	 * @param id
	 * @return
	 */
	private Boolean idById(Long id) {
		int count = tblFwglLawServiceEvaluateOracleMapper.selectCount(TblFwglLawServiceEvaluateOracle.ofId(id));
		if (count == 0) {
			return true;
		}
		return false;
	}
}
