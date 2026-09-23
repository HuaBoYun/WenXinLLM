package com.huabo.system.oracle.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hbfk.util.redis.Random.RandomUtil;
import com.huabo.system.entity.TblSystemFileOracle;
import com.huabo.system.mapper.TblSystemFileOracleMapper;
import com.huabo.system.oracle.service.TblSystemFileOracleService;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@Service
public class TblSystemFileOracleServiceImpl implements TblSystemFileOracleService {

	@Resource
	private TblSystemFileOracleMapper tblSystemFileOracleMapper;

	/**
	 * 新增 文件存储详情
	 * @param param
	 * @return
	 */
	@Override
	public TblSystemFileOracle saveOrUpdate(TblSystemFileOracle param) {
		param.setFileId(RandomUtil.uuBigDecimalId());
		tblSystemFileOracleMapper.insert(param);
		return findById(param.getFileId());
	}

	/**
	 * 查询 文件存储详情
	 * @param id
	 * @return
	 */
	@Override
	public TblSystemFileOracle findById(BigDecimal id) {
		TblSystemFileOracle tblFwglFile = tblSystemFileOracleMapper.selectById(id);
		if (tblFwglFile == null) {
			return null;
			//throw new ServiceException(400, 50001);
		}
		return tblFwglFile;
	}

	/**
	 * 批量查询 文件存储详情
	 * @param ids
	 * @return
	 */
	@Override
	public List<TblSystemFileOracle> findByIds(String ids) {
		/*Example example = new Example(TblSystemFileOracle.class);
		example.createCriteria().andIn("fileId", Arrays.asList(ids.split(",")));*/
		QueryWrapper<TblSystemFileOracle> query = new QueryWrapper<TblSystemFileOracle>();
		query.in("fileId", Arrays.asList(ids.split(",")));
		return tblSystemFileOracleMapper.selectList(query);
	}

	/**
	 * 删除 文件存储详情
	 * @param fileId
	 */
	@Override
	public void delete(BigDecimal fileId) {
		tblSystemFileOracleMapper.deleteById(fileId);
	}

}
