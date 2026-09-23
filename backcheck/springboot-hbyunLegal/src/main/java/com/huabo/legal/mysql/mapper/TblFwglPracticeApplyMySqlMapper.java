package com.huabo.legal.mysql.mapper;

import com.huabo.legal.mysql.entity.TblFwglPracticeApplyMySql;
import com.huabo.legal.vo.param.TblFwglParam;
import com.huabo.legal.vo.result.LegalPersonnelCardEmploymentRateResult;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

public interface TblFwglPracticeApplyMySqlMapper extends Mapper<TblFwglPracticeApplyMySql> {

	/**
	 * 根据集团 统计持证人数
	 * @param param
	 * @return
	 */
	Integer findLegalPersonnelCardEmploymentRate(@Param("param") TblFwglParam param);

	/**
	 * 公司律师人数占比 公司律师人数
	 * @param param
	 * @return
	 */
	Integer findFirmLegalProportion(@Param("param") TblFwglParam param);
}