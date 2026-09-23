package com.huabo.audit.oracle.mapper;

import com.huabo.audit.oracle.entity.TblAuditModelExcelExtOracle;
import com.huabo.audit.vo.param.TblAuditModelExcelExtQueryParam;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface TblAuditModelExcelExtOracleMapper extends Mapper<TblAuditModelExcelExtOracle> {

	/**
	 * 查询列表
	 * @param param
	 * @return
	 */
	List<TblAuditModelExcelExtOracle> getList(@Param("param") TblAuditModelExcelExtQueryParam param);
}