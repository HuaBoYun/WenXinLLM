package com.huabo.legal.oracle.mapper;

import com.huabo.legal.oracle.entity.TblFwglLegalPersonnelOracle;
import com.huabo.legal.vo.param.TblFwglParam;
import com.huabo.legal.vo.result.LegalFullimePercentageResult;
import com.huabo.legal.vo.result.LegalPersonnelCountResult;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface TblFwglLegalPersonnelOracleMapper extends Mapper<TblFwglLegalPersonnelOracle> {

	/**
	 * 法务人员数量
	 * @param param
	 * @return
	 */
	List<LegalPersonnelCountResult> getLegalPersonnelCount(@Param("param") TblFwglParam param,
			@Param("tblOrganizationAll") List<Long> tblOrganizationAll);

	/**
	 * 法务人数数量-专职法务人鱼/兼职法务人 占比
	 * @param param
	 * @return
	 */
	LegalFullimePercentageResult getLegalFullimePercentage(@Param("param") TblFwglParam param,
			@Param("tblOrganizationAll") List<Long> tblOrganizationAll);

	/**
	 * 总人数
	 * @param param
	 * @return
	 */
	LegalFullimePercentageResult getCount(@Param("param") TblFwglParam param, @Param("tblOrganizationAll") List<Long> tblOrganizationAll);
}
