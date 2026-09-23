package com.huabo.audit.oracle.mapper;

import java.math.BigDecimal;

import com.huabo.audit.oracle.entity.TblYqnsSheet;
import com.huabo.audit.oracle.vo.TBlNbsjSheetVo;
import com.hbfk.util.PageInfo;

public class TblYqnsSheetMapperSqlConfig {
	
	 
	
	public String selectListByPageInfo(PageInfo<TblYqnsSheet> pageInfo,TBlNbsjSheetVo tBlNbsjSheetVo) {
		
		StringBuffer sb = new StringBuffer("SELECT * FROM "
				+ "(SELECT T1.*,ROWNUM RN  FROM "
				+ "(SELECT TNA.*,TNP.prjoectname projectname,PRINCIPAL.REALNAME "
				+ "FROM TBL_YQNS_SHEET TNA "
				+ "LEFT JOIN TBL_STAFF PRINCIPAL ON PRINCIPAL.STAFFID = TNA.CREATESTAFF "
				+ "LEFT JOIN TBL_NBSJ_PROJECT TNP  ON TNP.PROJECTID = TNA.PROJECTID "
				+ "WHERE 1=1 ");
		
		if(tBlNbsjSheetVo.getProjectId()!=null) {
			sb.append(" AND TNA.PROJECTID = '"+tBlNbsjSheetVo.getProjectId()+"'");
		}
		
		if(tBlNbsjSheetVo.getSheetcode()!=null && tBlNbsjSheetVo.getSheetcode().length()>0) {
			sb.append(" AND TNA.SHEETCODE LIKE '%"+tBlNbsjSheetVo.getSheetcode()+"%'");
		}
		
		if(tBlNbsjSheetVo.getSheetname()!=null && tBlNbsjSheetVo.getSheetname().length()>0) {
			sb.append(" AND TNA.AUDITMATTERS LIKE '%"+tBlNbsjSheetVo.getSheetname()+"%'");
		}
		
		if(tBlNbsjSheetVo.getProjectName()!=null && tBlNbsjSheetVo.getProjectName().length()>0) {
			sb.append(" AND TNP.prjoectname LIKE '%"+tBlNbsjSheetVo.getProjectName()+"%'");
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
	
	
	public String selectCountByPageInfo(PageInfo<TblYqnsSheet> pageInfo,TBlNbsjSheetVo tBlNbsjSheetVo) {
		StringBuffer sb = new StringBuffer("SELECT COUNT(0) "
				+ "FROM TBL_YQNS_SHEET TNA "
				+ "LEFT JOIN TBL_STAFF PRINCIPAL ON PRINCIPAL.STAFFID = TNA.CREATESTAFF "
				+ "LEFT JOIN TBL_NBSJ_PROJECT TNP  ON TNP.PROJECTID = TNA.PROJECTID "
				+ "WHERE 1=1 ");
		
		if(tBlNbsjSheetVo.getProjectId()!=null) {
			sb.append(" AND TNA.PROJECTID = '"+tBlNbsjSheetVo.getProjectId()+"'");
		}
		
		if(tBlNbsjSheetVo.getSheetcode()!=null && tBlNbsjSheetVo.getSheetcode().length()>0) {
			sb.append(" AND TNA.SHEETCODE LIKE '%"+tBlNbsjSheetVo.getSheetcode()+"%'");
		}
		
		if(tBlNbsjSheetVo.getSheetname()!=null && tBlNbsjSheetVo.getSheetname().length()>0) {
			sb.append(" AND TNA.AUDITMATTERS LIKE '%"+tBlNbsjSheetVo.getSheetname()+"%'");
		}
		
		if(tBlNbsjSheetVo.getProjectName()!=null && tBlNbsjSheetVo.getProjectName().length()>0) {
			sb.append(" AND TNP.prjoectname LIKE '%"+tBlNbsjSheetVo.getProjectName()+"%'");
		}
		
		if(tBlNbsjSheetVo.getStatus() != null && tBlNbsjSheetVo.getStatus().length()>0) {
			sb.append(" AND TNA.STATE =  "+tBlNbsjSheetVo.getStatus());
		}
		
		if(tBlNbsjSheetVo.getStaffid() != null && tBlNbsjSheetVo.getStaffid().length()>0) {
			sb.append(" AND PRINCIPAL.STAFFID =  "+tBlNbsjSheetVo.getStaffid());
		}
		
		return sb.toString();
	}
	 
	
	
	//==
	public String getExportList(BigDecimal projectid) {
		StringBuffer sb = new StringBuffer(""
				+ "SELECT TNA.*,PRINCIPAL.REALNAME,ORG.ORGNAME,pr.PRJOECTNAME,AUS.REALNAME approver  "
				+ "FROM TBL_YQNS_SHEET TNA "
				+ " LEFT JOIN TBL_NBSJ_PROJECT pr ON TNA.PROJECTID=PR.PROJECTID "
				+ " LEFT JOIN TBL_STAFF aus ON aus.STAFFID = PR.AUDITSTAFFID "
				+ "LEFT JOIN TBL_STAFF PRINCIPAL ON PRINCIPAL.STAFFID = TNA.CREATESTAFF "
				+ "LEFT JOIN TBL_ORGANIZATION ORG  ON ORG.ORGID = TNA.AUDITORG "
				+ "WHERE 1=1 ");
		
		if(projectid!=null) {
			sb.append(" AND TNA.PROJECTID = '"+projectid+"'");
		}
		
		sb.append(" ORDER BY TNA.SHEETID DESC ");
		return sb.toString();
	}
	 
	
	//==
		public String getExportListstaff(BigDecimal projectid,BigDecimal staffid) {
			StringBuffer sb = new StringBuffer(""
					+ "SELECT TNA.*,PRINCIPAL.REALNAME,ORG.ORGNAME,pr.PRJOECTNAME,AUS.REALNAME approver  "
					+ "FROM TBL_YQNS_SHEET TNA "
					+ " LEFT JOIN TBL_NBSJ_PROJECT pr ON TNA.PROJECTID=PR.PROJECTID "
					+ " LEFT JOIN TBL_STAFF aus ON aus.STAFFID = PR.AUDITSTAFFID "
					+ "LEFT JOIN TBL_STAFF PRINCIPAL ON PRINCIPAL.STAFFID = TNA.CREATESTAFF "
					+ "LEFT JOIN TBL_ORGANIZATION ORG  ON ORG.ORGID = TNA.AUDITORG "
					+ "WHERE 1=1 ");
			
			if(projectid!=null) {
				sb.append(" AND TNA.PROJECTID = '"+projectid+"'");
			}
			if(staffid!=null) {
				sb.append(" AND TNA.CREATESTAFF = '"+staffid+"'");
			}
			
			sb.append(" ORDER BY TNA.SHEETID DESC ");
			return sb.toString();
		}
		
}
