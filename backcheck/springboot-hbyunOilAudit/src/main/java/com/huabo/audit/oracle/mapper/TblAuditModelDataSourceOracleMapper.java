package com.huabo.audit.oracle.mapper;

import com.huabo.audit.oracle.entity.TblAuditModelDataSourceOracle;
import com.huabo.audit.vo.param.ExcelCheckParam;

import java.math.BigDecimal;

import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

public interface TblAuditModelDataSourceOracleMapper extends Mapper<TblAuditModelDataSourceOracle> {

	/**
	 * excel工作副本名校验
	 * @param param
	 * @return
	 */
	Integer excelCheck(@Param("param") ExcelCheckParam param);

	/**
	 * 根据excelId 查询数据库源
	 * @param excelId
	 * @return
	 */
	TblAuditModelDataSourceOracle findExcelIdDataSource(@Param("excelId") BigDecimal excelId);

}