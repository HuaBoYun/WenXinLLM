package com.huabo.legal.oracle.mapper;

import com.huabo.legal.oracle.entity.TblFwglPracticeApplyOracle;
import com.huabo.legal.vo.param.TblFwglParam;
import com.huabo.legal.vo.result.LegalCompanyLawyerResult;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface TblFwglPracticeApplyOracleMapper extends Mapper<TblFwglPracticeApplyOracle> {

	/**
	 * 法务人员持证上岗率 持证人数
	 * @param param
	 */
	Integer findLegalPersonnelCardEmploymentRate(@Param("param") TblFwglParam param, @Param("tblOrganizationAll") List<Long> tblOrganizationAll);

	/**
	 * 公司律师人数占比 公司律师人数
	 * @param param
	 * @return
	 */
	Integer findFirmLegalProportion(@Param("param") TblFwglParam param, @Param("tblOrganizationAll") List<Long> tblOrganizationAll);

	/**
	 * 公司律师人数
	 * @param param
	 * @return
	 */
	List<LegalCompanyLawyerResult> getLegalCompanyLawyer(@Param("param") TblFwglParam param,
			@Param("tblOrganizationAll") List<Long> tblOrganizationAll);
}
