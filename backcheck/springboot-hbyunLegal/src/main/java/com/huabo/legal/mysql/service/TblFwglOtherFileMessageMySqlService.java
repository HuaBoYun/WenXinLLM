package com.huabo.legal.mysql.service;

import com.github.pagehelper.PageInfo;
import com.huabo.legal.mysql.entity.TblFwglOtherFileMessageMySql;
import com.huabo.legal.vo.param.TblFwglOtherFileMessageQueryParam;

public interface TblFwglOtherFileMessageMySqlService {

	/**
	 * 其他文件报送列表 查询
	 * @param param
	 * @return
	 */
	PageInfo<TblFwglOtherFileMessageMySql> getList(TblFwglOtherFileMessageQueryParam param);

	/**
	 * 其他文件报送 新增/更新
	 * @param param
	 * @return
	 */
	TblFwglOtherFileMessageMySql saveOrUpdate(TblFwglOtherFileMessageMySql param);

	/**
	 * 其他文件报送 删除
	 * @param id
	 */
	void delete(Integer id);

	/**
	 * 其他文件报送详情 查询
	 * @param id
	 * @return
	 */
	TblFwglOtherFileMessageMySql findById(Integer id);
}
