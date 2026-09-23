package com.huabo.audit.oracle.service;

import com.huabo.audit.oracle.entity.TblAuditModelExcelContentOracle;
import com.huabo.audit.vo.param.TblAuditModelExcelContentQueryParam;

import java.util.List;

public interface TblAuditModelExcelContentOracleService {

	/**
	 * excel分析预览表数据内容 列表 查询
	 * @param param
	 * @return
	 */
	List<TblAuditModelExcelContentOracle> getList(TblAuditModelExcelContentQueryParam param);

	/**
	 * excel分析预览表数据内容 新增/更新
	 * @param param
	 * @return
	 */
	TblAuditModelExcelContentOracle saveOrUpdate(TblAuditModelExcelContentOracle param);

	/**
	 * excel分析预览表数据内容 删除
	 * @param id
	 */
	void delete(Long id);

	/**
	 * excel分析预览表数据内容 详情 查询
	 * @param id
	 * @return
	 */
	TblAuditModelExcelContentOracle findById(Long id);
}
