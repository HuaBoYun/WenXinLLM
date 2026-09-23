package com.huabo.legal.oracle.service.impl;

import com.hbfk.util.redis.Random.RandomUtil;
import com.huabo.legal.constant.YesNo;
import com.huabo.legal.exception.ServiceException;
import com.huabo.legal.oracle.entity.TblFwglLawServiceLawyerOracle;
import com.huabo.legal.oracle.mapper.TblFwglLawServiceLawyerOracleMapper;
import com.huabo.legal.oracle.service.TblFwglLawServiceLawyerOracleService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
public class TblFwglLawServiceLawyerOracleServiceImpl implements TblFwglLawServiceLawyerOracleService {

	@Resource
	private TblFwglLawServiceLawyerOracleMapper tblFwglLawServiceLawyerOracleMapper;

	/**
	 * 根据常年法律服务id/专项法律服务id 法律服务-律师信息列表 查询
	 * @param lawyerId
	 * @return
	 */
	@Override
	public List<TblFwglLawServiceLawyerOracle> getList(String lawyerId) {
		Example example = new Example(TblFwglLawServiceLawyerOracle.class);
		Example.Criteria criteria = example.createCriteria();
		if (StringUtils.isNotBlank(lawyerId)) {
			criteria.andIn("lawyerId", Arrays.asList(lawyerId.split(",")));
		}
		example.setOrderByClause(" LAWYERID desc ");
		return tblFwglLawServiceLawyerOracleMapper.selectByExample(example);
	}

	/**
	 * 法律服务-律师信息 新增/更新
	 * @param param
	 * @return
	 */
	@Override
	public TblFwglLawServiceLawyerOracle saveOrUpdate(TblFwglLawServiceLawyerOracle param) {
		Date now = new Date();
		if (param.getLawyerId() == null) {
			param.setLawyerId(RandomUtil.uuLongId());
			param.setState(YesNo.YES);
			param.setCreatedTime(now);
			param.setUpdatedTime(now);
			tblFwglLawServiceLawyerOracleMapper.insertSelective(param);
		} else {
			if (idById(param.getLawyerId())) {
				throw new ServiceException(400, 50001);
			}
			param.setCreatedTime(null);
			param.setUpdatedTime(now);
			tblFwglLawServiceLawyerOracleMapper.updateByPrimaryKeySelective(param);
		}
		return findById(param.getLawyerId());
	}

	/**
	 * 法律服务-律师信息详情 查询
	 * @param id
	 * @return
	 */
	@Override
	public TblFwglLawServiceLawyerOracle findById(Long id) {
		TblFwglLawServiceLawyerOracle lawServiceLawyer = tblFwglLawServiceLawyerOracleMapper.selectByPrimaryKey(id);
		if (lawServiceLawyer == null) {
			throw new ServiceException(400, 50001);
		}
		return lawServiceLawyer;
	}

	/**
	 * 法律服务-律师信息 删除
	 * @param id
	 */
	@Override
	public void delete(Long id) {
		tblFwglLawServiceLawyerOracleMapper.deleteByPrimaryKey(id);
	}

	/**
	 * 根据id查询 法律服务-律师信息 是存在
	 * @param id
	 * @return
	 */
	private Boolean idById(Long id) {
		int count = tblFwglLawServiceLawyerOracleMapper.selectCount(TblFwglLawServiceLawyerOracle.ofId(id));
		if (count == 0) {
			return true;
		}
		return false;
	}
}
