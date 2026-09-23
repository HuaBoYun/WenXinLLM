package com.huabo.system.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.SelectProvider;

import com.huabo.system.mappersql.ScheduledTaskMapperSqlConfig;

public interface ScheduledTaskMapper{

	@SelectProvider(type = ScheduledTaskMapperSqlConfig.class,method = "selectContractPlanNodeTimeout")
	List<Map<String, Object>> selectContractPlanNodeTimeout() throws Exception;

	List<Map<String, Object>> selectContractPlanNodePaymentTimeout() throws Exception;



}
