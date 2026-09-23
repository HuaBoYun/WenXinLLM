package com.huabo.cybermonitor.mapper;

import com.hbfk.util.DateUtil;
import com.hbfk.util.PageInfo;
import com.huabo.cybermonitor.entity.TblNbsjSheetEntity;
import com.huabo.cybermonitor.vo.TBlNbsjSheetVo;

public class TblNbsjSheetMapperSqlConfig {
	


	public String selectListByPageInfo(PageInfo<TblNbsjSheetEntity> pageInfo, TBlNbsjSheetVo tBlNbsjSheetVo) {
		
		StringBuffer sb = new StringBuffer("SELECT * FROM "
				+ "(SELECT T1.*,ROWNUM RN  FROM "
				+ "(SELECT TNA.*,TNP.prjoectname projectName,PRINCIPAL.REALNAME,ORG.ORGNAME "
				+ "FROM TBL_YJPT_SHEET TNA "
				+ "LEFT JOIN TBL_STAFF PRINCIPAL ON PRINCIPAL.STAFFID = TNA.CREATESTAFF "
				+ "LEFT JOIN TBL_ORGANIZATION ORG  ON ORG.ORGID = TNA.AUDITORG "
				+ "LEFT JOIN TBL_NBSJ_PROJECT TNP  ON TNP.PROJECTID = TNA.PROJECTID "
				+ "WHERE 1=1 ");
		
		if(tBlNbsjSheetVo.getProjectid()!=null) {
			sb.append(" AND TNA.PROJECTID = '"+tBlNbsjSheetVo.getProjectid()+"'");
		}
		
		if(tBlNbsjSheetVo.getSheetcode()!=null && tBlNbsjSheetVo.getSheetcode().length()>0) {
			sb.append(" AND TNA.SHEETCODE LIKE '%"+tBlNbsjSheetVo.getSheetcode()+"%'");
		}
		
		if(tBlNbsjSheetVo.getSheetname()!=null && tBlNbsjSheetVo.getSheetname().length()>0) {
			sb.append(" AND TNA.SHEETNAME LIKE '%"+tBlNbsjSheetVo.getSheetname()+"%'");
		}
		
		if(tBlNbsjSheetVo.getStatus() != null && tBlNbsjSheetVo.getStatus().length()>0) {
			sb.append(" AND TNA.STATE =  "+tBlNbsjSheetVo.getStatus());
		}
		
		if(tBlNbsjSheetVo.getStaffid() != null && tBlNbsjSheetVo.getStaffid().length()>0) {
			sb.append(" AND PRINCIPAL.STAFFID =  "+tBlNbsjSheetVo.getStaffid());
		}
		
		sb.append(" ORDER BY TNA.SHEETID DESC) T1 WHERE ROWNUM <= "+(pageInfo.getCurrentRecord()+pageInfo.getPageSize())+" ) T2 WHERE T2.RN > "+pageInfo.getCurrentRecord());
		return sb.toString();
	}
	
	
	public String selectCountByPageInfo(PageInfo<TblNbsjSheetEntity> pageInfo,TBlNbsjSheetVo tBlNbsjSheetVo) {
//		TblNbsjSheetEntity plan = pageInfo.getCondition();
		StringBuffer sb = new StringBuffer("SELECT COUNT(0) "
				+ "FROM TBL_YJPT_SHEET TNA "
				+ "LEFT JOIN TBL_STAFF PRINCIPAL ON PRINCIPAL.STAFFID = TNA.CREATESTAFF "
				+ "LEFT JOIN TBL_ORGANIZATION ORG  ON ORG.ORGID = TNA.AUDITORG "
				+ "WHERE 1=1 ");
		
		if(tBlNbsjSheetVo.getProjectid()!=null) {
			sb.append(" AND TNA.PROJECTID = '"+tBlNbsjSheetVo.getProjectid()+"'");
		}
		
		if(tBlNbsjSheetVo.getSheetcode()!=null && tBlNbsjSheetVo.getSheetcode().length()>0) {
			sb.append(" AND TNA.SHEETCODE LIKE '%"+tBlNbsjSheetVo.getSheetcode()+"%'");
		}
		
		if(tBlNbsjSheetVo.getSheetname()!=null && tBlNbsjSheetVo.getSheetname().length()>0) {
			sb.append(" AND TNA.SHEETNAME LIKE '%"+tBlNbsjSheetVo.getSheetname()+"%'");
		}
		
		if(tBlNbsjSheetVo.getStatus() != null && tBlNbsjSheetVo.getStatus().length()>0) {
			sb.append(" AND TNA.STATE =  "+tBlNbsjSheetVo.getStatus());
		}
		
		if(tBlNbsjSheetVo.getStaffid() != null && tBlNbsjSheetVo.getStaffid().length()>0) {
			sb.append(" AND PRINCIPAL.STAFFID =  "+tBlNbsjSheetVo.getStaffid());
		}
		
		return sb.toString();
	}
	


	
}
