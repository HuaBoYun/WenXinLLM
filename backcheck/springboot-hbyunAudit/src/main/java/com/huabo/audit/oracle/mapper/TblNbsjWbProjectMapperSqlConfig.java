package com.huabo.audit.oracle.mapper;

import org.apache.commons.lang.StringUtils;

import com.huabo.audit.oracle.entity.TblNbsjWbProject;
import com.huabo.audit.oracle.vo.TblZgzzProjectVo;
import com.huabo.audit.util.PageInfo;

public class TblNbsjWbProjectMapperSqlConfig {
	
	public String selectPageInfoListByRectification(TblZgzzProjectVo project, Integer planType) {
		StringBuffer sqlSb = new StringBuffer("SELECT PROJECTID,PROJECTCODE,PROJECTNAME FROM TBL_NBSJ_WBPROJECT WHERE 1 = 1");
		
		if(StringUtils.isNotBlank(project.getPlanCode())) {
			sqlSb.append(" AND PROJECTCODE LIKE '%").append(project.getPlanCode()).append("%'");
		}
		if(StringUtils.isNotBlank(project.getPlanName())) {
			sqlSb.append(" AND PROJECTNAME LIKE '%").append(project.getPlanName()).append("%'");
		}
		
		if(planType != null) {
			sqlSb.append(" AND PROJECTTYPE = ").append(planType);
		}
		
		sqlSb.append(" ORDER BY PROJECTID DESC ");
		System.out.println(sqlSb.toString());
		return sqlSb.toString();
	}
	
	public String selectListByPageInfo(PageInfo<TblNbsjWbProject> pageInfo) {
		
		StringBuffer sb = new StringBuffer("SELECT * FROM "
				+ "(SELECT T1.*,ROWNUM RN  FROM "
				+ "(SELECT TNA.* "
				+ "FROM TBL_NBSJ_WBPROJECT TNA "
				+ "WHERE 1=1 ");
		
		sb.append(" ORDER BY TNA.PROJECTID DESC) T1 WHERE ROWNUM <= "+(pageInfo.getCurrentRecord()+pageInfo.getPageSize())+" ) T2 WHERE T2.RN > "+pageInfo.getCurrentRecord());
		return sb.toString();
	}
	
	
	public String selectCountByPageInfo(PageInfo<TblNbsjWbProject> pageInfo) {
		StringBuffer sb = new StringBuffer("SELECT COUNT(0) "
				+ "FROM TBL_NBSJ_WBPROJECT TNA "
				+ "WHERE 1=1 ");
		
		return sb.toString();
	}
	 
	
	
	 
		
}
