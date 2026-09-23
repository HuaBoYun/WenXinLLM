package com.huabo.central.enterprises.audit.oracle.mapper;

import com.huabo.central.enterprises.audit.oracle.entity.TblCeaQualityAssessment;
import com.huabo.central.enterprises.audit.vo.param.TblCeaQualityAssessmentQueryParam;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface TblCeaQualityAssessmentMapper extends Mapper<TblCeaQualityAssessment> {

	/**
	 * 列表查询
	 * @param param
	 * @return
	 */
	List<TblCeaQualityAssessment> getList(@Param("param") TblCeaQualityAssessmentQueryParam param);
}