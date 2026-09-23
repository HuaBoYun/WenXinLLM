package com.huabo.audit.oracle.mapper;

import com.huabo.audit.oracle.entity.TblAuditModelDataSourceOracle;
import com.huabo.audit.oracle.entity.TblNbsjAuditStepEntity;
import com.huabo.audit.vo.param.ExcelCheckParam;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import tk.mybatis.mapper.common.Mapper;

import java.math.BigDecimal;
import java.util.List;

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

	@Select("SELECT * FROM TBL_AUDIT_MODEL_DATA_SOURCE WHERE ID IN (SELECT BOOKID FROM TBL_NBSJ_AUDITSTEP WHERE QYSTATUS = #{qystatus} AND MPDELTYPE = #{mpdeltype})")
	List<TblAuditModelDataSourceOracle> selectAuditUseInfoList(TblNbsjAuditStepEntity step) throws Exception;

}