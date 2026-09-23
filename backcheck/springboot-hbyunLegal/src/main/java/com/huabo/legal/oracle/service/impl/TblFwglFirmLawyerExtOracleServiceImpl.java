package com.huabo.legal.oracle.service.impl;

import com.hbfk.util.redis.Random.RandomUtil;
import com.huabo.legal.constant.YesNo;
import com.huabo.legal.exception.ServiceException;
import com.huabo.legal.oracle.entity.TblFwglFirmLawyerExtOracle;
import com.huabo.legal.oracle.mapper.TblFwglFirmLawyerExtOracleMapper;
import com.huabo.legal.oracle.service.TblFwglFirmLawyerExtOracleService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
public class TblFwglFirmLawyerExtOracleServiceImpl implements TblFwglFirmLawyerExtOracleService {

	@Resource
	private TblFwglFirmLawyerExtOracleMapper tblFwglFirmLawyerExtOracleMapper;

	/**
	 * 根据公司律师扩展ID 查询 公司律师扩展详情列表
	 * @param lawyerExtId 公司律师扩展ID
	 * @return
	 */
	@Override
	public List<TblFwglFirmLawyerExtOracle> getList(String lawyerExtId) {
		Example example = new Example(TblFwglFirmLawyerExtOracle.class);
		Example.Criteria criteria = example.createCriteria();
		if (StringUtils.isNotBlank(lawyerExtId)) {
			criteria.andIn("lawyerExtId", Arrays.asList(lawyerExtId.split(",")));
		}
		example.setOrderByClause(" LAWYEREXTID desc ");
		return tblFwglFirmLawyerExtOracleMapper.selectByExample(example);
	}

	/**
	 * 查询 公司律师扩展详情
	 * @param id
	 * @return
	 */
	@Override
	public TblFwglFirmLawyerExtOracle findById(Long id) {
		TblFwglFirmLawyerExtOracle tblFwglFirmLawyerExt = tblFwglFirmLawyerExtOracleMapper.selectByPrimaryKey(id);
		if (tblFwglFirmLawyerExt == null) {
			throw new ServiceException(400, 50001);
		}
		return tblFwglFirmLawyerExt;
	}

	/**
	 * 公司律师-简历 删除
	 * @param id
	 */
	@Override
	public void delete(Long id) {
		tblFwglFirmLawyerExtOracleMapper.deleteByPrimaryKey(id);
	}

	/**
	 * 公司律师-简历 新增/更新
	 * @param param
	 * @return
	 */
	@Override
	public TblFwglFirmLawyerExtOracle saveOrUpdate(TblFwglFirmLawyerExtOracle param) {
		Date now = new Date();
		if (param.getLawyerExtId() == null) {
			param.setLawyerExtId(RandomUtil.uuLongId());
			param.setState(YesNo.YES);
			param.setCreatedTime(now);
			param.setUpdatedTime(now);
			tblFwglFirmLawyerExtOracleMapper.insertSelective(param);
		} else {
			if (idById(param.getLawyerExtId())) {
				throw new ServiceException(400, 50001);
			}
			param.setCreatedTime(null);
			param.setUpdatedTime(now);
			tblFwglFirmLawyerExtOracleMapper.updateByPrimaryKeySelective(param);
		}
		return findById(param.getLawyerExtId());
	}

	/**
	 * 根据id查询公司律师-简历是存在
	 * @param id
	 * @return
	 */
	private Boolean idById(Long id) {
		int count = tblFwglFirmLawyerExtOracleMapper.selectCount(TblFwglFirmLawyerExtOracle.ofId(id));
		if (count == 0) {
			return true;
		}
		return false;
	}

}
