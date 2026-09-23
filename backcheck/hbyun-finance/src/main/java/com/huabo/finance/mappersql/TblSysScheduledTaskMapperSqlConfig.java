package com.huabo.finance.mappersql;

import org.apache.commons.lang.StringUtils;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.huabo.finance.vo.TblSysScheduledTaskVo;
import com.huabo.finance.vr.TblSysScheduledTaskVr;

public class TblSysScheduledTaskMapperSqlConfig {
	
	public String selectFinanceDataPage(Page<TblSysScheduledTaskVr> page, TblSysScheduledTaskVo vo) throws Exception{
		StringBuffer sqlSb = new StringBuffer("SELECT TSS.TASKID,TSS.TASKNAME,TSS.CORNEXPRESSION,TSS.DESCRIPTION,TSS.STATUS,TSS.CREATETIME,TSS.MODIFYTIME,CTS.REALNAME AS CREATESTAFFNAME,MTS.REALNAME AS MODIFYSTAFFNAME,TSS.PLANID,TFP.FNAME AS PLANNAME ")
				.append(" FROM TBL_SYS_SCHEDULEDTASK TSS LEFT JOIN TBL_STAFF CTS ON TSS.CREATESTAFFID = CTS.STAFFID LEFT JOIN TBL_STAFF MTS ON TSS.MODIFYSTAFFID = MTS.STAFFID LEFT JOIN BD_FINANCEPLAN TFP ON TSS.PLANID = TFP.FID ")
				.append(" WHERE TSS.LINKORGID = ").append(vo.getOrgId());
		
		if(StringUtils.isNotBlank(vo.getTaskName())) {
			sqlSb.append(" AND TSS.TASKNAME LIKE '%").append(vo.getTaskName()).append("%'");
		}
		
		if(StringUtils.isNotBlank(vo.getDescrtiption())) {
			sqlSb.append(" AND TSS.DESCRIPTION LIKE '%").append(vo.getDescrtiption()).append("%'");
		}
		
		if(StringUtils.isNotBlank(vo.getPlanName())) {
			sqlSb.append(" AND TFP.FNAME LIKE '%").append(vo.getPlanName()).append("%'");
		}
		
		if(vo.getStatus() != null) {
			sqlSb.append(" AND TSS.STATUS = ").append(vo.getStatus());
		}
		
		sqlSb.append(" ORDER BY TSS.CREATETIME ASC");
		
		String sql = sqlSb.toString();
		return sql;
	}
	
	
}
