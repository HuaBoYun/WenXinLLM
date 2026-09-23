package com.huabo.cybermonitor.mapper;

import com.hbfk.util.PageInfo;
import com.huabo.cybermonitor.entity.TblNbsjProject;
import com.huabo.cybermonitor.vo.TblnbsjProjectVo;

import java.math.BigDecimal;
import java.util.Map;

public class TblNbsjProjectMapperSqlConfig {

	public String selectCountByPageInfo(PageInfo<TblNbsjProject> pageInfo, TblnbsjProjectVo tblnbsjProjectVo) {
//		TblNbsjProject plan = pageInfo.getCondition();
		StringBuffer sb = new StringBuffer("SELECT COUNT(0) "
				+ "FROM TBL_NBSJ_PROJECT TNA "
				+ "left join TBL_NBSJ_AUDIT_PROJECDATA dd on TNA.PROJECTID = dd.OLDPROJECTID "
				+ "LEFT JOIN TBL_STAFF CRESTAFF ON CRESTAFF.STAFFID = TNA.PMID "
				+ "LEFT JOIN TBL_ORGANIZATION ORG ON ORG.ORGID = TNA.AUDITORGID "
				+ "WHERE 1=1 ");

		if(tblnbsjProjectVo.getProjectId()!=null) {
			sb.append(" AND dd.PROJECTID = '"+tblnbsjProjectVo.getProjectId()+"'");
		}
		if(tblnbsjProjectVo.getPrjoectName()!=null && tblnbsjProjectVo.getPrjoectName().length()>0) {
			sb.append(" AND TNA.PRJOECTNAME LIKE '%"+tblnbsjProjectVo.getPrjoectName()+"%'");
		}

		return sb.toString();
	}

	public String selectListByPageInfo(PageInfo<TblNbsjProject> pageInfo,TblnbsjProjectVo tblnbsjProjectVo) {

		StringBuffer sb = new StringBuffer("SELECT * FROM "
				+ "(SELECT T1.*,ROWNUM RN  FROM "
				+ "(SELECT TNA.*,CRESTAFF.REALNAME REALNAME,ORG.ORGNAME auditOrgName,TS.REALNAME auditStaffName "
				+ "FROM TBL_NBSJ_PROJECT TNA "
				+ "left join TBL_NBSJ_AUDIT_PROJECDATA dd on TNA.PROJECTID = dd.OLDPROJECTID "
				+ "LEFT JOIN TBL_STAFF CRESTAFF ON CRESTAFF.STAFFID = TNA.PMID "
				+ "LEFT JOIN TBL_STAFF TS ON TS.STAFFID = TNA.AUDITSTAFFID "
				+ "LEFT JOIN TBL_ORGANIZATION ORG ON ORG.ORGID = TNA.AUDITORGID "
				+ "WHERE 1=1 ");
//				+ "AND TNA.PROJECTID = "+projectId);


		if(tblnbsjProjectVo.getProjectId()!=null) {
			sb.append(" AND dd.PROJECTID = '"+tblnbsjProjectVo.getProjectId()+"'");
		}
		if(tblnbsjProjectVo.getPrjoectName()!=null && tblnbsjProjectVo.getPrjoectName().length()>0) {
			sb.append(" AND TNA.PRJOECTNAME LIKE '%"+tblnbsjProjectVo.getPrjoectName()+"%'");
		}

		sb.append(" ORDER BY TNA.PROJECTID DESC) T1 WHERE ROWNUM <= "+(pageInfo.getCurrentRecord()+pageInfo.getPageSize())+" ) T2 WHERE T2.RN > "+pageInfo.getCurrentRecord());
		return sb.toString();
	}
		
}
