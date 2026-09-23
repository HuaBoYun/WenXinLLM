package com.huabo.legal.oracle.service.impl;

import com.hbfk.util.redis.Random.RandomUtil;
import com.huabo.legal.constant.YesNo;
import com.huabo.legal.exception.ServiceException;
import com.huabo.legal.oracle.entity.TblFwglLawServiceWorkRecordOracle;
import com.huabo.legal.oracle.mapper.TblFwglLawServiceWorkRecordOracleMapper;
import com.huabo.legal.oracle.service.TblFwglLawServiceWorkRecordOracleService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
public class TblFwglLawServiceWorkRecordOracleServiceImpl implements TblFwglLawServiceWorkRecordOracleService {

	@Resource
	private TblFwglLawServiceWorkRecordOracleMapper tblFwglLawServiceWorkRecordOracleMapper;

	/**
	 * 根据常年法律服务id/专项法律服务id 法律服务-工作记录列表 查询
	 * @param workRecordId
	 * @return
	 */
	@Override
	public List<TblFwglLawServiceWorkRecordOracle> getList(String workRecordId) {
		Example example = new Example(TblFwglLawServiceWorkRecordOracle.class);
		Example.Criteria criteria = example.createCriteria();
		if (StringUtils.isNotEmpty(workRecordId)) {
			criteria.andIn("workRecordId", Arrays.asList(workRecordId.split(",")));
		}
		example.setOrderByClause(" WORKRECORDID desc ");
		return tblFwglLawServiceWorkRecordOracleMapper.selectByExample(example);
	}

	/**
	 * 法律服务-工作记录 新增/更新
	 * @param param
	 * @return
	 */
	@Override
	public TblFwglLawServiceWorkRecordOracle saveOrUpdate(TblFwglLawServiceWorkRecordOracle param) {
		Date now = new Date();
		if (param.getWorkRecordId() == null) {
			param.setWorkRecordId(RandomUtil.uuLongId());
			param.setState(YesNo.YES);
			param.setCreatedTime(now);
			param.setUpdatedTime(now);
			tblFwglLawServiceWorkRecordOracleMapper.insertSelective(param);
		} else {
			if (idById(param.getWorkRecordId())) {
				throw new ServiceException(400, 50001);
			}
			param.setCreatedTime(null);
			param.setUpdatedTime(now);
			tblFwglLawServiceWorkRecordOracleMapper.updateByPrimaryKeySelective(param);
		}
		return findById(param.getWorkRecordId());
	}

	/**
	 * 法律服务-工作记录详情 查询
	 * @param id
	 * @return
	 */
	@Override
	public TblFwglLawServiceWorkRecordOracle findById(Long id) {
		TblFwglLawServiceWorkRecordOracle lawServiceWorkRecord = tblFwglLawServiceWorkRecordOracleMapper.selectByPrimaryKey(id);
		if (lawServiceWorkRecord == null) {
			throw new ServiceException(400, 50001);
		}
		return lawServiceWorkRecord;
	}

	/**
	 * 法律服务-工作记录 删除
	 * @param id
	 */
	@Override
	public void delete(Long id) {
		tblFwglLawServiceWorkRecordOracleMapper.deleteByPrimaryKey(id);
	}

	/**
	 * 根据id查询 法律服务-工作记录 是存在
	 * @param id
	 * @return
	 */
	private Boolean idById(Long id) {
		int count = tblFwglLawServiceWorkRecordOracleMapper.selectCount(TblFwglLawServiceWorkRecordOracle.ofId(id));
		if (count == 0) {
			return true;
		}
		return false;
	}
}

