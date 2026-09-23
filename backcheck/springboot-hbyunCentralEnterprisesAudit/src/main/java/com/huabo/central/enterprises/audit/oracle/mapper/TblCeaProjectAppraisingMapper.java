package com.huabo.central.enterprises.audit.oracle.mapper;

import com.huabo.central.enterprises.audit.oracle.entity.TblCeaProjectAppraising;
import com.huabo.central.enterprises.audit.vo.result.ImplementationPlanResult;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface TblCeaProjectAppraisingMapper extends Mapper<TblCeaProjectAppraising> {

	/**
	 * 根据项目管理实施方案ID查询信息
	 * @param implementationPlanId
	 * @return
	 */
	List<ImplementationPlanResult> findImplementationPlanList(@Param("implementationPlanId") List<String> implementationPlanId);
}