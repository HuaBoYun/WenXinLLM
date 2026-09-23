package com.huabo.legal.oracle.service.impl;

import com.hbfk.util.redis.Random.RandomUtil;
import com.huabo.legal.constant.YesNo;
import com.huabo.legal.exception.ServiceException;
import com.huabo.legal.oracle.entity.TblFwglLegalPersonnelExtOracle;
import com.huabo.legal.oracle.mapper.TblFwglLegalPersonnelExtOracleMapper;
import com.huabo.legal.oracle.service.TblFwglLegalPersonnelExtOracleService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
public class TblFwglLegalPersonnelExtOracleServiceImpl implements TblFwglLegalPersonnelExtOracleService {

	@Resource
	private TblFwglLegalPersonnelExtOracleMapper tblFwglLegalPersonnelExtOracleMapper;

	/**
	 * 法务人员-工作经历 新增/更新
	 * @param param
	 * @return
	 */
	@Override
	public TblFwglLegalPersonnelExtOracle saveOrUpdate(TblFwglLegalPersonnelExtOracle param) {
		Date now = new Date();
		if (param.getPersonnelExtId() == null) {
			param.setPersonnelExtId(RandomUtil.uuLongId());
			param.setState(YesNo.YES);
			param.setCreatedTime(now);
			param.setUpdatedTime(now);
			tblFwglLegalPersonnelExtOracleMapper.insertSelective(param);
		} else {
			if (idById(param.getPersonnelExtId())) {
				throw new ServiceException(400, 50001);
			}
			param.setCreatedTime(null);
			param.setUpdatedTime(now);
			tblFwglLegalPersonnelExtOracleMapper.updateByPrimaryKeySelective(param);
		}
		return findById(param.getPersonnelExtId());
	}

	/**
	 * 法务人员-工作经历详情 查询
	 * @param id
	 * @return
	 */
	@Override
	public TblFwglLegalPersonnelExtOracle findById(Long id) {
		TblFwglLegalPersonnelExtOracle legalPersonnelExt = tblFwglLegalPersonnelExtOracleMapper.selectByPrimaryKey(id);
		if (legalPersonnelExt == null) {
			throw new ServiceException(400, 50001);
		}
		return legalPersonnelExt;
	}

	/**
	 * 法务人员-工作经历 删除
	 * @param id
	 */
	@Override
	public void delete(Long id) {
		tblFwglLegalPersonnelExtOracleMapper.deleteByPrimaryKey(id);
	}

	/**
	 * 根据法务人员扩展ID 法务人员-工作经历列表 查询
	 * @param personnelExtId 法务人员扩展ID
	 */
	@Override
	public List<TblFwglLegalPersonnelExtOracle> getList(String personnelExtId) {
		Example example = new Example(TblFwglLegalPersonnelExtOracle.class);
		Example.Criteria criteria = example.createCriteria();
		if (StringUtils.isNotBlank(personnelExtId)) {
			criteria.andIn("personnelExtId", Arrays.asList(personnelExtId.split(",")));
		}
		example.setOrderByClause(" PERSONNELEXTID desc ");
		return tblFwglLegalPersonnelExtOracleMapper.selectByExample(example);
	}

	/**
	 * 根据id查询 法务人员-工作经历 是存在
	 * @param id
	 * @return
	 */
	private Boolean idById(Long id) {
		int count = tblFwglLegalPersonnelExtOracleMapper.selectCount(TblFwglLegalPersonnelExtOracle.ofId(id));
		if (count == 0) {
			return true;
		}
		return false;
	}
}
