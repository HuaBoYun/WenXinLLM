package com.huabo.legal.oracle.service.impl;

import com.hbfk.util.redis.Random.RandomUtil;
import com.huabo.legal.constant.YesNo;
import com.huabo.legal.exception.ServiceException;
import com.huabo.legal.oracle.entity.TblFwglLawServiceWorkReportOracle;
import com.huabo.legal.oracle.mapper.TblFwglLawServiceWorkReportOracleMapper;
import com.huabo.legal.oracle.service.TblFwglLawServiceWorkReportOracleService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
public class TblFwglLawServiceWorkReportOracleServiceImpl implements TblFwglLawServiceWorkReportOracleService {

	@Resource
	private TblFwglLawServiceWorkReportOracleMapper tblFwglLawServiceWorkReportOracleMapper;

	/**
	 * 根据常年法律服务id 法律服务-工作记录列表 查询
	 * @param workReportId
	 * @return
	 */
	@Override
	public List<TblFwglLawServiceWorkReportOracle> getList(String workReportId) {
		Example example = new Example(TblFwglLawServiceWorkReportOracle.class);
		Example.Criteria criteria = example.createCriteria();
		if (StringUtils.isNotBlank(workReportId)) {
			criteria.andIn("workReportId", Arrays.asList(workReportId.split(",")));
		}
		example.setOrderByClause(" WORKREPORTID desc ");
		return tblFwglLawServiceWorkReportOracleMapper.selectByExample(example);
	}

	/**
	 * 法律服务-工作记录 新增/更新
	 * @param param
	 * @return
	 */
	@Override
	public TblFwglLawServiceWorkReportOracle saveOrUpdate(TblFwglLawServiceWorkReportOracle param) {
		Date now = new Date();
		if (param.getWorkReportId() == null) {
			param.setWorkReportId(RandomUtil.uuLongId());
			param.setState(YesNo.YES);
			param.setCreatedTime(now);
			param.setUpdatedTime(now);
			tblFwglLawServiceWorkReportOracleMapper.insertSelective(param);
		} else {
			if (idById(param.getWorkReportId())) {
				throw new ServiceException(400, 50001);
			}
			param.setCreatedTime(null);
			param.setUpdatedTime(now);
			tblFwglLawServiceWorkReportOracleMapper.updateByPrimaryKeySelective(param);
		}
		return findById(param.getWorkReportId());
	}

	/**
	 * 法律服务-工作记录详情 查询
	 * @param id
	 * @return
	 */
	@Override
	public TblFwglLawServiceWorkReportOracle findById(Long id) {
		TblFwglLawServiceWorkReportOracle lawServiceWorkReport = tblFwglLawServiceWorkReportOracleMapper.selectByPrimaryKey(id);
		if (lawServiceWorkReport == null) {
			throw new ServiceException(400, 50001);
		}
		return lawServiceWorkReport;
	}

	/**
	 * 法律服务-工作记录 删除
	 * @param id
	 */
	@Override
	public void delete(Long id) {
		tblFwglLawServiceWorkReportOracleMapper.deleteByPrimaryKey(id);
	}

	/**
	 * 根据id查询 法律服务-工作记录 是存在
	 * @param id
	 * @return
	 */
	private Boolean idById(Long id) {
		int count = tblFwglLawServiceWorkReportOracleMapper.selectCount(TblFwglLawServiceWorkReportOracle.ofId(id));
		if (count == 0) {
			return true;
		}
		return false;
	}
}
