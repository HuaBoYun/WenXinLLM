package com.huabo.compliance.oracle.mapper;

import com.huabo.compliance.oracle.entity.TblComplianceInspectPlanOracle;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.common.Mapper;

public interface TblComplianceInspectPlanOracleMapper extends Mapper<TblComplianceInspectPlanOracle> {

	/**
	 * 编号-自动编码 查询
	 * @param autoNum
	 * @return
	 */
	@Select("SELECT MAX(TO_NUMBER(SUBSTR(PLANCODE,INSTR(PLANCODE,'-',-1)+1))) FROM TBL_COMPLIANCE_INSPECT_PLAN WHERE REGEXP_LIKE(PLANCODE, #{autoNum})")
	String findAutoNum(@Param("autoNum") String autoNum);
	
	/**
	 * 首页-合规审查数量
	 * @param
	 * @return
	 */
	@Select("SELECT COUNT(*) CNT FROM tbl_fwgl_institution_audit WHERE isViolationLegal=1")
	Integer compcnt_institution();
	
	/**
	 * 首页-合规审查数量
	 * @param
	 * @return
	 */
	@Select("SELECT COUNT(*) CNT FROM TBL_FWGL_MATTERS_AUDIT WHERE ISCOMPARE=1")
	Integer compcnt_matters();
	
	/**
	 * 首页-疑似问题数量
	 * @param
	 * @return
	 */
	@Select("SELECT COUNT(*) CNT FROM TBL_COMPLIANCE_INSPECT_IMP WHERE 1=1")
	Integer compcnt_suspected();
	
	/**
	 * 首页-整改问题数量
	 * @param
	 * @return
	 */
	@Select("SELECT COUNT(*) CNT FROM TBL_COMPLIANCE_RECTIFICATION WHERE 1=1")
	Integer compcnt_reform();
	
	
}