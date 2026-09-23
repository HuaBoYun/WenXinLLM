package com.huabo.system.mappersql;

import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hbfk.util.PageInfo;
import com.hbfk.util.database.DataBaseSqlConfig;
import com.huabo.system.entity.TblFlowInformInfo;

public class TblFlowInformInfoMapperSqlConfig {
	
	public String selectListByLoginUser(String taskIds, BigDecimal staffid) throws Exception{
		StringBuffer sqlSb = new StringBuffer("SELECT INFOA.FLOWID,INFOA.ID,INFOA.THISSTEPID,INFOA.ISREAD FROM TBL_FLOW_INFORMINFO INFOA INNER JOIN (SELECT MAX(CREATETIME) CREATETIME,FLOWID,ID,THISSTEPID FROM TBL_FLOW_INFORMINFO WHERE INFORMSTAFFID = '")
				.append(staffid).append("' AND ID IN ('").append(taskIds).append("') GROUP BY FLOWID,ID,THISSTEPID) INFOB ON INFOA.FLOWID = INFOB.FLOWID AND INFOA.ID = INFOB.ID AND INFOA.THISSTEPID = INFOB.THISSTEPID")
				.append(" AND INFOA.CREATETIME = INFOB.CREATETIME WHERE INFOA.INFORMSTAFFID = '").append(staffid).append("' AND INFOA.ID IN ('").append(taskIds).append("')");
		String sql = sqlSb.toString();
		return sql;
	}
	
	
	public String updateEntity(BigDecimal staffid,String flowId, String id,BigDecimal formId) throws Exception{
		String sql = "UPDATE TBL_FLOW_INFORMINFO SET ISREAD = 1 ,READTIME = "+DataBaseSqlConfig.getDateStrFormat(new Date())+" WHERE INFORMSTAFFID = "+staffid
				+" AND FLOWID = '"+flowId+"' AND ID = '"+id+"' AND FORMID = "+formId;
		return sql;
	}
	
	public String insertEntity(BigDecimal createstaff,BigDecimal informstaff, String flowId, String id
			, BigDecimal formId, String thisStepId, BigDecimal infoId) throws Exception{
		String sql = "INSERT INTO TBL_FLOW_INFORMINFO(INFOID,CREATETIME,CREATESTAFF,INFORMSTAFFID,ID,FLOWID,THISSTEPID,FORMID,ISREAD) "
				+ " VALUES("+infoId+","+DataBaseSqlConfig.getDateStrFormat(new Date())+","+createstaff+","+informstaff+",'"+id+"','"+flowId+"','"+thisStepId+"','"+formId+"',0)";
		return sql;
	}

	public String selectPageListByFlowInfo(IPage<TblFlowInformInfo> page, TblFlowInformInfo inform) {
		String sql = "SELECT INFO.INFOID,INFO.CREATETIME,INFO.CREATESTAFF,CT.REALNAME AS CREATESTAFFNAME,INFO.READTIME,INFO.INFORMSTAFFID,IT.REALNAME AS INFORMSTAFFNAME,INFO.ID,INFO.FLOWID,INFO.FORMID,INFO.THISSTEPID,INFO.ISREAD FROM TBL_FLOW_INFORMINFO INFO LEFT JOIN TBL_STAFF CT ON INFO.CREATESTAFF = CT.STAFFID LEFT JOIN TBL_STAFF IT ON INFO.INFORMSTAFFID = IT.STAFFID  WHERE INFO.ID = '"+inform.getId()+"' AND INFO.FLOWID = '"+inform.getFlowId()+"'";
		
		if(inform.getIsRead() != null) {
			sql += " AND INFO.ISREAD = "+ inform.getIsRead();
		}
		
		if(inform.getCreateStaffName() != null && !"".equals(inform.getCreateStaffName())) {
			sql += " AND CT.REALNAME LIKE = '%"+inform.getCreateStaffName()+"%'";
		}
		
		if(inform.getInformStaffName() != null && !"".equals(inform.getInformStaffName())) {
			sql += " AND IT.REALNAME LIKE = '%"+inform.getInformStaffName()+"%'";
		}
		
		sql += " ORDER BY INFO.CREATETIME DESC ";
		return sql;
	}
	
}
