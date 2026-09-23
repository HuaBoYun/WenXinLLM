package com.huabo.legal.oracle.service.impl;

import com.huabo.legal.oracle.entity.TblFwglFileOracle;
import com.huabo.legal.oracle.mapper.TblFwglFileOracleMapper;
import com.huabo.legal.oracle.service.TblFwglFileOracleService;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

@Service
public class TblFwglFileOracleServiceImpl implements TblFwglFileOracleService {

	@Resource
	private TblFwglFileOracleMapper tblFwglFileOracleMapper;

	/**
	 * 新增 法务管理-文件存储详情
	 * @param param
	 * @return
	 */
	@Override
	public TblFwglFileOracle saveOrUpdate(TblFwglFileOracle param) {
		tblFwglFileOracleMapper.insertSelective(param);
		return findById(param.getFileId());
	}

	/**
	 * 查询 法务管理-文件存储详情
	 * @param id
	 * @return
	 */
	@Override
	public TblFwglFileOracle findById(Long id) {
		TblFwglFileOracle tblFwglFile = tblFwglFileOracleMapper.selectByPrimaryKey(id);
		if (tblFwglFile == null) {
			return null;
			//throw new ServiceException(400, 50001);
		}
		return tblFwglFile;
	}

	/**
	 * 批量查询 法务管理-文件存储详情
	 * @param ids
	 * @return
	 */
	@Override
	public List<TblFwglFileOracle> findByIds(String ids) {
		Example example = new Example(TblFwglFileOracle.class);
		example.createCriteria().andIn("fileId", Arrays.asList(ids.split(",")));
		return tblFwglFileOracleMapper.selectByExample(example);
	}

	/**
	 * 删除 法务管理-文件存储详情
	 * @param fileId
	 */
	@Override
	public void delete(Long fileId) {
		tblFwglFileOracleMapper.deleteByPrimaryKey(fileId);
	}

}
