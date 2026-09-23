package com.huabo.legal.oracle.service;

import com.github.pagehelper.PageInfo;
import com.huabo.legal.mysql.entity.TblFwglOtherFileMessageMySql;
import com.huabo.legal.oracle.entity.TblFwglOtherFileMessageOracle;
import com.huabo.legal.vo.param.TblFwglOtherFileMessageQueryParam;

public interface TblFwglOtherFileMessageOracleService {

	/**
	 * 其他文件报送列表 查询
	 * @param param
	 * @return
	 */
	PageInfo<TblFwglOtherFileMessageOracle> getList(TblFwglOtherFileMessageQueryParam param);

	/**
	 * 其他文件报送 新增/更新
	 * @param param
	 * @return
	 */
	TblFwglOtherFileMessageOracle saveOrUpdate(TblFwglOtherFileMessageOracle param);

	/**
	 * 其他文件报送 删除
	 * @param id
	 */
	void delete(Long id);

	/**
	 * 其他文件报送详情 查询
	 * @param id
	 * @return
	 */
	TblFwglOtherFileMessageOracle findById(Long id);
}
