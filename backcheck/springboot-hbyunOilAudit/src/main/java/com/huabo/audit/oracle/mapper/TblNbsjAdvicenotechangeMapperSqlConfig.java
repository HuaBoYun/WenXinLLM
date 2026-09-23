package com.huabo.audit.oracle.mapper;

import org.apache.commons.lang3.StringUtils;

import com.huabo.audit.oracle.entity.TblNbsjAdvicenoteChangeEntity;
import com.huabo.audit.oracle.entity.TblYqnsAdvicenoteChangeEntity;
import com.huabo.audit.util.PageInfo;

public class TblNbsjAdvicenotechangeMapperSqlConfig {
	
	public String selectListByPageInfo(PageInfo<TblYqnsAdvicenoteChangeEntity> pageInfo, TblYqnsAdvicenoteChangeEntity tblYqnsAdvicenoteChange) {
		StringBuffer sb = new StringBuffer("SELECT TNA.*,CRESTAFF.REALNAME,TORG.ORGNAME, TPLAN.PROJECT_NAME "
				+ "FROM TBL_YQNS_ADVICENOTE_CHANGE TNA "
				+ "LEFT JOIN TBL_STAFF CRESTAFF ON CRESTAFF.STAFFID = TNA.CREATESTAFFID "
				+ "LEFT JOIN TBL_ORGANIZATION TORG ON TORG.ORGID = TNA.ORGID "
				+ "LEFT JOIN TBL_YQNS_IMPLEMENT_PLAN TPLAN ON TPLAN.ID = TNA.PROGECTID "
				+ "WHERE 1=1 ");

		if(tblYqnsAdvicenoteChange.getStartDate() !=null&&"".equals(tblYqnsAdvicenoteChange.getStartDate())==false){
			sb.append(" AND TNA.CREATRTIME >= '" + tblYqnsAdvicenoteChange.getStartDate()+"'");
		}
		
		if(tblYqnsAdvicenoteChange.getEndDate() !=null&&"".equals(tblYqnsAdvicenoteChange.getEndDate())==false){
			sb.append(" AND TNA.CREATRTIME <= '" + tblYqnsAdvicenoteChange.getEndDate() + " 23:59:59'");
		}
		
		if(tblYqnsAdvicenoteChange.getChangething() !=null && !"".equals(tblYqnsAdvicenoteChange.getChangething())){
			sb.append(" AND TNA.changething LIKE '%" + tblYqnsAdvicenoteChange.getChangething() + "%'");
		}
		if(tblYqnsAdvicenoteChange.getChangebefore() !=null && !"".equals(tblYqnsAdvicenoteChange.getChangebefore())){
			sb.append(" AND TNA.changebefore LIKE '%" + tblYqnsAdvicenoteChange.getChangebefore() + "%'");
		}
		if(tblYqnsAdvicenoteChange.getChangeafter() !=null && !"".equals(tblYqnsAdvicenoteChange.getChangeafter())){
			sb.append(" AND TNA.changeafter LIKE '%" + tblYqnsAdvicenoteChange.getChangeafter() + "%'");
		}
		
		
		if(tblYqnsAdvicenoteChange.getProjectId() !=null ){
			sb.append(" AND TNA.PROGECTID = " + tblYqnsAdvicenoteChange.getProjectId() );
		}
		
		if (StringUtils.isNotBlank(tblYqnsAdvicenoteChange.getOrgid())) {
			 sb.append(" and ( TNA.CREATESTAFFID='"+tblYqnsAdvicenoteChange.getCreatestaffid()+"'  or TNA.CREATESTAFFID in (SELECT DISTINCT STAFFID FROM TBL_USER_ORGRELATION WHERE DEPTID IN ("+tblYqnsAdvicenoteChange.getOrgid()+")  ))");
	     }else {
	        sb.append(" and TNA.CREATESTAFFID='"+tblYqnsAdvicenoteChange.getCreatestaffid()+"'" );
	     }
		
		
		sb.append(" ORDER BY TNA.CHANGEID DESC");
		return sb.toString();
	}
	
	
	public String selectCountByPageInfo(PageInfo<TblNbsjAdvicenoteChangeEntity> pageInfo,TblNbsjAdvicenoteChangeEntity tblNbsjAdvicenoteVo) {
		TblNbsjAdvicenoteChangeEntity plan = pageInfo.getCondition();
		StringBuffer sb = new StringBuffer("SELECT COUNT(0) "
				+ "FROM TBL_NBSJ_ADVICENOTE_CHANGE TNA "
				+ "WHERE 1=1 ");
		if(tblNbsjAdvicenoteVo.getStartDate() !=null&&"".equals(tblNbsjAdvicenoteVo.getStartDate())==false){
			sb.append(" AND TNA.CREATRTIME >= '" + tblNbsjAdvicenoteVo.getStartDate()+"'");
		}

		if(tblNbsjAdvicenoteVo.getEndDate() !=null&&"".equals(tblNbsjAdvicenoteVo.getEndDate())==false){
			sb.append(" AND TNA.CREATRTIME <= '" + tblNbsjAdvicenoteVo.getEndDate() + " 23:59:59'");
		}
		
		return sb.toString();
	}
}
