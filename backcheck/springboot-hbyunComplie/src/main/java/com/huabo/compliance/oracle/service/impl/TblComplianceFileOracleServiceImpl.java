package com.huabo.compliance.oracle.service.impl;

import com.huabo.compliance.oracle.entity.TblComplianceFileOracle;
import com.huabo.compliance.oracle.mapper.TblComplianceFileOracleMapper;
import com.huabo.compliance.oracle.service.TblComplianceFileOracleService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Service
public class TblComplianceFileOracleServiceImpl implements TblComplianceFileOracleService {

	@Resource
	private TblComplianceFileOracleMapper tblComplianceFileOracleMapper;

	/**
	 * 新增 法务管理-文件存储详情
	 * @param param
	 * @return
	 */
	@Override
	public TblComplianceFileOracle saveOrUpdate(TblComplianceFileOracle param) {
		tblComplianceFileOracleMapper.insertSelective(param);
		return findById(param.getFileId());
	}

	/**
	 * 查询 法务管理-文件存储详情
	 * @param id
	 * @return
	 */
	@Override
	public TblComplianceFileOracle findById(Integer id) {
		TblComplianceFileOracle tblComplianceFile = tblComplianceFileOracleMapper.selectByPrimaryKey(id);
		if (tblComplianceFile == null) {
			return null;
			//throw new ServiceException(400, 50001);
		}
		return tblComplianceFile;
	}

	/**
	 * 批量查询 管理-文件存储详情
	 * @param ids
	 * @return
	 */
	@Override
	public List<TblComplianceFileOracle> findByIds(String ids) {
		if (StringUtils.isBlank(ids)){
			return Collections.emptyList();
		}
		Example example = new Example(TblComplianceFileOracle.class);
		example.createCriteria().andIn("fileId", Arrays.asList(ids.split(",")));
		return tblComplianceFileOracleMapper.selectByExample(example);
	}

	/**
	 * 删除 法务管理-文件存储详情
	 * @param fileId
	 */
	@Override
	public void delete(Integer fileId) {
		tblComplianceFileOracleMapper.deleteByPrimaryKey(fileId);
	}

}
