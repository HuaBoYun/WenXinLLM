package com.huabo.legal.oracle.service.impl;

import com.hbfk.util.redis.Random.RandomUtil;
import com.huabo.legal.constant.YesNo;
import com.huabo.legal.exception.ServiceException;
import com.huabo.legal.oracle.entity.TblFwglLegalAdviserExtOracle;
import com.huabo.legal.oracle.mapper.TblFwglLegalAdviserExtOracleMapper;
import com.huabo.legal.oracle.service.TblFwglLegalAdviserExtOracleService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
public class TblFwglLegalAdviserExtOracleServiceImpl implements TblFwglLegalAdviserExtOracleService {

	@Resource
	private TblFwglLegalAdviserExtOracleMapper tblFwglLegalAdviserExtOracleMapper;

	/**
	 * 根据总法律顾问ID 总法律顾问-工作经历列表 查询
	 * @param adviserExtIds 总法律顾问扩展IDS
	 * @return
	 */
	@Override
	public List<TblFwglLegalAdviserExtOracle> getList(String adviserExtIds) {
		Example example = new Example(TblFwglLegalAdviserExtOracle.class);
		Example.Criteria criteria = example.createCriteria();
		if (StringUtils.isNotBlank(adviserExtIds)) {
			criteria.andIn("adviserExtId", Arrays.asList(adviserExtIds.split(",")));
		}
		example.setOrderByClause(" ADVISEREXTID desc ");
		return tblFwglLegalAdviserExtOracleMapper.selectByExample(example);
	}

	/**
	 * 总法律顾问-工作经历 新增/更新
	 * @param param
	 * @return
	 */
	@Override
	public TblFwglLegalAdviserExtOracle saveOrUpdate(TblFwglLegalAdviserExtOracle param) {
		Date now = new Date();
		if (param.getAdviserExtId() == null) {
			param.setAdviserExtId(RandomUtil.uuLongId());
			param.setState(YesNo.YES);
			param.setCreatedTime(now);
			param.setUpdatedTime(now);
			tblFwglLegalAdviserExtOracleMapper.insertSelective(param);
		} else {
			if (idById(param.getAdviserExtId())) {
				throw new ServiceException(400, 50001);
			}
			param.setCreatedTime(null);
			param.setUpdatedTime(now);
			tblFwglLegalAdviserExtOracleMapper.updateByPrimaryKeySelective(param);
		}
		return findById(param.getAdviserExtId());
	}

	/**
	 * 总法律顾问-工作经历详情 查询
	 * @param id
	 * @return
	 */
	@Override
	public TblFwglLegalAdviserExtOracle findById(Long id) {
		TblFwglLegalAdviserExtOracle legalAdviserExt = tblFwglLegalAdviserExtOracleMapper.selectByPrimaryKey(id);
		if (legalAdviserExt == null) {
			throw new ServiceException(400, 50001);
		}
		return legalAdviserExt;
	}

	/**
	 * 总法律顾问-工作经历 删除
	 * @param id
	 */
	@Override
	public void delete(Long id) {
		tblFwglLegalAdviserExtOracleMapper.deleteByPrimaryKey(id);
	}

	/**
	 * 根据id查询 法务人员-工作经历 是存在
	 * @param id
	 * @return
	 */
	private Boolean idById(Long id) {
		int count = tblFwglLegalAdviserExtOracleMapper.selectCount(TblFwglLegalAdviserExtOracle.ofId(id));
		if (count == 0) {
			return true;
		}
		return false;
	}
}
