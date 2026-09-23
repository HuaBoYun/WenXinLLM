package com.huabo.finance.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.huabo.finance.entity.TblSysScheduledTask;

/**
 * <p>
 * 公司采集配置方案信息表 服务类
 * </p>
 *
 * @author L
 * @since 2025-03-12
 */
public interface GatherFinanceDateService {

	JsonBean exeUnique(String fid, HttpServletRequest request, TblStaffUtil staff, String finitsqlid, String finitPlanid) throws Exception;

	void gatherFinanceDataExecuteSql(String taskName, String ip, TblStaffUtil staff, String finitsqlid, String finitPlanid) throws Exception;

	JsonBean stopGather(String fid, String finitsqlid, String finitPlanid, String recordSqlid) throws Exception;

	JsonBean getSqlGatherInfo(String recordid) throws Exception;

	JsonBean beginFinancePlan(TblStaffUtil staff,String ip, String fid) throws Exception;

	JsonBean getFinancePlanStatus(TblStaffUtil staff, String fid) throws Exception;

	JsonBean stopFinanceProcess(TblStaffUtil staff, String recordid) throws Exception;

	JsonBean stopFinancePlanProcess(TblStaffUtil staff, String planid) throws Exception;

	JsonBean getFinancePlanTreeList(TblStaffUtil staff, String fname, Integer fstatus) throws Exception;
	
	
	void beginCornFinanceTask(TblSysScheduledTask task) throws Exception;

	JsonBean gatherBussinessData(String fid, HttpServletRequest request, HttpServletResponse response) throws Exception;

	void gatherBussinessDataExecuteSql(String taskName, String ip, TblStaffUtil staff, String tableId) throws Exception;

	JsonBean stopGatherData(String fid) throws Exception;
}
