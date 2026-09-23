package com.huabo.legal.oracle.mapper;

import com.huabo.legal.oracle.entity.TblFwglInstitutionAuditOracle;
import com.huabo.legal.vo.param.TblFwglParam;
import com.huabo.legal.vo.result.LegalInstitutionResult;
import com.huabo.legal.vo.result.TblFwglComplianceResult;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.Date;
import java.util.List;

public interface TblFwglInstitutionAuditOracleMapper extends Mapper<TblFwglInstitutionAuditOracle> {

	/**
	 * 获取该集团下 工作单位的审核数量
	 * @param beginOfYear
	 * @param endYear
	 * @return
	 */
	List<TblFwglComplianceResult> findInstitutionAuditCompliance(@Param("type") Integer type, @Param("beginOfYear") Date beginOfYear,
			@Param("endYear") Date endYear, @Param("param") TblFwglParam param, @Param("tblOrganizationAll") List<Long> tblOrganizationAll);

	/**
	 * 全体系经营事项及制度审核数量
	 * @param param
	 * @return
	 */
	List<LegalInstitutionResult> getLegalInstitution(@Param("param") TblFwglParam param,
			@Param("tblOrganizationAll") List<Long> tblOrganizationAll);
}
