package com.huabo.legal.mysql.service.impl;

import com.huabo.legal.exception.ServiceException;
import com.huabo.legal.mysql.entity.TblFwglFileMySql;
import com.huabo.legal.mysql.mapper.TblFwglFileMySqlMapper;
import com.huabo.legal.mysql.service.TblFwglFileMySqlService;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

@Service
public class TblFwglFileMySqlServiceImpl implements TblFwglFileMySqlService {

	@Resource
	private TblFwglFileMySqlMapper tblFwglFileMySqlMapper;

	/**
	 * 新增 法务管理-文件存储详情
	 * @param param
	 * @return
	 */
	@Override
	public TblFwglFileMySql saveOrUpdate(TblFwglFileMySql param) {
		tblFwglFileMySqlMapper.insertSelective(param);
		return findById(param.getFileId());
	}

	/**
	 * 查询 法务管理-文件存储详情
	 * @param id
	 * @return
	 */
	@Override
	public TblFwglFileMySql findById(Integer id) {
		TblFwglFileMySql tblFwglFile = tblFwglFileMySqlMapper.selectByPrimaryKey(id);
		if (tblFwglFile == null) {
			throw new ServiceException(400, 50001);
		}
		return tblFwglFile;
	}

	/**
	 * 批量查询 法务管理-文件存储详情
	 * @param ids
	 * @return
	 */
	@Override
	public List<TblFwglFileMySql> findByIds(String ids) {
		Example example = new Example(TblFwglFileMySql.class);
		example.createCriteria().andIn("fileId", Arrays.asList(ids.split(",")));
		return tblFwglFileMySqlMapper.selectByExample(example);
	}

	/**
	 * 删除 法务管理-文件存储详情
	 * @param fileId
	 */
	@Override
	public void delete(Integer fileId) {
		tblFwglFileMySqlMapper.deleteByPrimaryKey(fileId);
	}

}
