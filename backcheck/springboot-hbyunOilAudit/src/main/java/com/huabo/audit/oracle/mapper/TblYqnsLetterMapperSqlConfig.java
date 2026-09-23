package com.huabo.audit.oracle.mapper;

import com.huabo.audit.oracle.entity.TblYqnsLetter;
import com.huabo.audit.oracle.vo.TBlNbsjSheetVo;
import com.hbfk.util.PageInfo;

public class TblYqnsLetterMapperSqlConfig {
	
	 
	
	public String selectListByPageInfo(PageInfo<TblYqnsLetter> pageInfo,TBlNbsjSheetVo tBlNbsjSheetVo) {
		
		StringBuffer sb = new StringBuffer("SELECT * FROM "
				+ "(SELECT T1.*,ROWNUM RN  FROM "
				+ "(SELECT TNA.*,PRINCIPAL.REALNAME "
				+ "FROM TBL_YQNS_LETTER TNA "
				+ "LEFT JOIN TBL_STAFF PRINCIPAL ON PRINCIPAL.STAFFID = TNA.CREATESTAFF "
//				+ "LEFT JOIN TBL_NBSJ_PROJECT TNP  ON TNP.PROJECTID = TNA.PROJECTID "
				+ "WHERE 1=1 ");
		
		if(tBlNbsjSheetVo.getProjectId()!=null) {
			sb.append(" AND TNA.PROJECTID = '"+tBlNbsjSheetVo.getProjectId()+"'");
		}
		
		if(tBlNbsjSheetVo.getSheetcode()!=null && tBlNbsjSheetVo.getSheetcode().length()>0) {
			sb.append(" AND TNA.LETTERCODE LIKE '%"+tBlNbsjSheetVo.getSheetcode()+"%'");
		}
		
		if(tBlNbsjSheetVo.getSheetname()!=null && tBlNbsjSheetVo.getSheetname().length()>0) {
			sb.append(" AND TNA.LETTERNAME LIKE '%"+tBlNbsjSheetVo.getSheetname()+"%'");
		}
		
		if(tBlNbsjSheetVo.getProjectName()!=null && tBlNbsjSheetVo.getProjectName().length()>0) {
			sb.append(" AND TNP.prjoectname LIKE '%"+tBlNbsjSheetVo.getProjectName()+"%'"); 
		}
		
		
		if(tBlNbsjSheetVo.getStaffid() != null && tBlNbsjSheetVo.getStaffid().length()>0) {
			sb.append(" AND PRINCIPAL.STAFFID =  "+tBlNbsjSheetVo.getStaffid());
		}
		
		 
		
		sb.append(" ORDER BY TNA.LETTERID DESC) T1 WHERE ROWNUM <= "+(pageInfo.getCurrentRecord()+pageInfo.getPageSize())+" ) T2 WHERE T2.RN > "+pageInfo.getCurrentRecord());
		return sb.toString();
	}
	
	
	public String selectCountByPageInfo(PageInfo<TblYqnsLetter> pageInfo,TBlNbsjSheetVo tBlNbsjSheetVo) {
		StringBuffer sb = new StringBuffer("SELECT COUNT(0) "
				+ "FROM TBL_YQNS_LETTER TNA "
				+ "LEFT JOIN TBL_STAFF PRINCIPAL ON PRINCIPAL.STAFFID = TNA.CREATESTAFF "
//				+ "LEFT JOIN TBL_NBSJ_PROJECT TNP  ON TNP.PROJECTID = TNA.PROJECTID "
				+ "WHERE 1=1 ");
		
		if(tBlNbsjSheetVo.getProjectId()!=null) {
			sb.append(" AND TNA.PROJECTID = '"+tBlNbsjSheetVo.getProjectId()+"'");
		}
		
		if(tBlNbsjSheetVo.getSheetcode()!=null && tBlNbsjSheetVo.getSheetcode().length()>0) {
			sb.append(" AND TNA.LETTERCODE LIKE '%"+tBlNbsjSheetVo.getSheetcode()+"%'");
		}
		if(tBlNbsjSheetVo.getSheetname()!=null && tBlNbsjSheetVo.getSheetname().length()>0) {
			sb.append(" AND TNA.LETTERNAME LIKE '%"+tBlNbsjSheetVo.getSheetname()+"%'");
		}
		
		
		
		if(tBlNbsjSheetVo.getProjectName()!=null && tBlNbsjSheetVo.getProjectName().length()>0) {
			sb.append(" AND TNP.prjoectname LIKE '%"+tBlNbsjSheetVo.getProjectName()+"%'");
		}
		
		
		if(tBlNbsjSheetVo.getStaffid() != null && tBlNbsjSheetVo.getStaffid().length()>0) {
			sb.append(" AND PRINCIPAL.STAFFID =  "+tBlNbsjSheetVo.getStaffid());
		}
		
		return sb.toString();
	}
	 
	
	
	 
		
}
