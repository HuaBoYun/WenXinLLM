package com.huabo.audit.oracle.mapper;

import com.hbfk.util.PageInfo;
import com.huabo.audit.oracle.entity.TblYqnsIssueListEntity;
import com.huabo.audit.oracle.entity.TblYqnsSjzgWtzg;
import org.apache.commons.lang3.StringUtils;

/**
 * @author wangxilu
 * @description 针对表【TBL_YQNS_SJZG_WTZG(问题整改表)】的数据库操作Mapper
 * @createDate 2023-09-27 16:46:40
 * @Entity TblYqnsSjzgWtzg
 */
public class TblYqnsSjzgWtzgMapperSqlConfig {
	
	public String rectificationLedgerFillin(TblYqnsSjzgWtzg vo, TblYqnsIssueListEntity issues) {
		StringBuffer sqlSb = new StringBuffer("SELECT TYI.ID,TYI.ISSUENUMBER,TYI.UNITORGID,TYI.UNITNAME,TYI.QUALITATIVE,TYI.PROJECTID,TYI.PROJECTNAME,TYI.QUALITATIVERULE,TYI.PROBLEMQUALITATIVE,TYI.HEAD,TYI.TIMELIMIT,TYSW.WTZGID,TYSW.WTZGFL,TYSW.ZGZT,TYSW.HFSPSTATUS,TYSW.STATUS AS ZGSTATUS,TYSW.DQZGZT,TYI.MONEY,TYSW.DQZGJE,TYSW.DQZJJJCG,TYSW.DQZJJJCGTYPE " + 
				" FROM TBL_YQNS_ISSUE_LIST TYI LEFT JOIN (SELECT TYIR.RECORDID,TYIR.ISSUESID,TYIR.VERSION,TYIR.WTZGID FROM TBL_YQNS_ISSUESRECORD TYIR JOIN ( SELECT ISSUESID,MAX(VERSION) AS VERSION FROM TBL_YQNS_ISSUESRECORD GROUP BY ISSUESID ) TYIRV ON TYIR.ISSUESID = TYIRV.ISSUESID AND TYIR.VERSION = TYIRV.VERSION ) TYIRR ON TYIRR.ISSUESID = TYI.ID")
				.append(" LEFT JOIN TBL_YQNS_SJZG_WTZG TYSW ON TYIRR.WTZGID = TYSW.WTZGID WHERE TYSW.WTZGID IS NOT NULL AND TYSW.PROJECTID = "+vo.getProjectId()+" AND TYI.PROJECTID = "+vo.getProjectId());
		
		if(StringUtils.isNotBlank(issues.getIssueNumber())) {
			sqlSb.append(" AND TYI.ISSUENUMBER LIKE '%").append(issues.getIssueNumber()).append("%'");
		}
		if(StringUtils.isNotBlank(issues.getUnitName())) {
			sqlSb.append(" AND TYI.UNITNAME LIKE '%").append(issues.getUnitName()).append("%'");
		}
		if(StringUtils.isNotBlank(vo.getZgzt())){
			sqlSb.append(" AND TYSW.ZGZT LIKE '%").append(vo.getZgzt()).append("%'");
		}
		if(StringUtils.isNotBlank(vo.getDqzgzt())) {
			sqlSb.append(" AND TYSW.DQZGZT LIKE '%").append(vo.getDqzgzt()).append("%'");
		}
		if(StringUtils.isNotBlank(vo.getWtzgfl())) {
			sqlSb.append(" AND TYSW.WTZGFL LIKE '%").append(vo.getWtzgfl()).append("%'");
		}
		
		if(vo.getStatus() != null) {
			sqlSb.append(" AND TYSW.STATUS = ").append(vo.getStatus());
		}
		
		sqlSb.append(" ORDER BY TYSW.CJSJ DESC");
		String sql = sqlSb.toString();
		return sql;
	}
	
	public String selectRectificationLedger(TblYqnsSjzgWtzg vo, TblYqnsIssueListEntity issues) {
		StringBuffer sqlSb = new StringBuffer("SELECT TYSW.PROJECTID,TYSW.PROJECTNAME,TYIL.UNITNAME,TYIP.PLAN_YEAR AS PLANYEAR,")
				.append("(SELECT COUNT(0) FROM TBL_YQNS_SJZG_WTZG WHERE WTZGID IN (SELECT TYIR.WTZGID FROM TBL_YQNS_ISSUESRECORD TYIR JOIN ( SELECT ISSUESID,MAX(VERSION) AS VERSION FROM TBL_YQNS_ISSUESRECORD GROUP BY ISSUESID ) TYIRV ON TYIR.ISSUESID = TYIRV.ISSUESID AND TYIR.VERSION = TYIRV.VERSION) AND PROJECTID = TYSW.PROJECTID ) AS WZGCOUNT,")
				.append("(SELECT COUNT(0) FROM TBL_YQNS_SJZG_WTZG WHERE WTZGID IN (SELECT TYIR.WTZGID FROM TBL_YQNS_ISSUESRECORD TYIR JOIN ( SELECT ISSUESID,MAX(VERSION) AS VERSION FROM TBL_YQNS_ISSUESRECORD GROUP BY ISSUESID ) TYIRV ON TYIR.ISSUESID = TYIRV.ISSUESID AND TYIR.VERSION = TYIRV.VERSION) AND PROJECTID = TYSW.PROJECTID AND ( (STATUS >= 6 AND ZGZT IN ('3','0') ) OR (HFSPSTATUS = 6 AND DQZGZT = '3') ) ) AS YWCCOUNT,")
				.append("(SELECT COUNT(0) FROM TBL_YQNS_SJZG_WTZG WHERE WTZGID IN (SELECT TYIR.WTZGID FROM TBL_YQNS_ISSUESRECORD TYIR JOIN ( SELECT ISSUESID,MAX(VERSION) AS VERSION FROM TBL_YQNS_ISSUESRECORD GROUP BY ISSUESID ) TYIRV ON TYIR.ISSUESID = TYIRV.ISSUESID AND TYIR.VERSION = TYIRV.VERSION) AND STATUS >= 6 AND PROJECTID = TYSW.PROJECTID  AND HFSPSTATUS = 6 AND DQZGZT = '1' ) AS BZGCOUNT,")
				.append("(SELECT COUNT(0) FROM TBL_YQNS_SJZG_WTZG WHERE WTZGID IN (SELECT TYIR.WTZGID FROM TBL_YQNS_ISSUESRECORD TYIR JOIN ( SELECT ISSUESID,MAX(VERSION) AS VERSION FROM TBL_YQNS_ISSUESRECORD GROUP BY ISSUESID ) TYIRV ON TYIR.ISSUESID = TYIRV.ISSUESID AND TYIR.VERSION = TYIRV.VERSION) AND PROJECTID = TYSW.PROJECTID AND (( HFSPSTATUS > 0 AND HFSPSTATUS < 6 ) OR (STATUS > 0 AND STATUS < 6) )) AS SPZCOUNT ")
				.append(" FROM TBL_YQNS_SJZG_WTZG TYSW LEFT JOIN TBL_YQNS_ISSUE_LIST TYIL ON TYSW.ISSUESID = TYIL.ID LEFT JOIN TBL_YQNS_IMPLEMENT_PLAN TYIP ON TYSW.PROJECTID = TYIP.ID ")
				.append(" WHERE TYSW.WTZGID IN (SELECT TYIR.WTZGID FROM TBL_YQNS_ISSUESRECORD TYIR JOIN ( SELECT ISSUESID,MAX(VERSION) AS VERSION FROM TBL_YQNS_ISSUESRECORD GROUP BY ISSUESID ) TYIRV ON TYIR.ISSUESID = TYIRV.ISSUESID AND TYIR.VERSION = TYIRV.VERSION ) ");
		
		if(StringUtils.isNotBlank(vo.getProjectName())) {
			sqlSb.append(" AND TYSW.PROJECTNAME LIKE '%").append(vo.getProjectName()).append("%'");
		}
		if(StringUtils.isNotBlank(issues.getUnitName())) {
			sqlSb.append(" AND TYIL.UNITNAME LIKE '%").append(issues.getUnitName()).append("%'");
		}
		if(vo.getPlanYear() != null){
			sqlSb.append(" AND TYIP.PLAN_YEAR = ").append(vo.getPlanYear());
		}
		
		sqlSb.append(" GROUP BY TYSW.PROJECTID,TYSW.PROJECTNAME, TYIL.UNITNAME,TYIP.PLAN_YEAR ORDER BY TYSW.PROJECTNAME ASC,TYIL.UNITNAME ASC");
		String sql = sqlSb.toString();
		return sql;
	}
	
	public String selectHgzgFillInListList(TblYqnsSjzgWtzg vo, TblYqnsIssueListEntity issues) {
		StringBuffer sqlSb = new StringBuffer("SELECT TYI.ID,TYI.ISSUENUMBER,TYI.UNITORGID,TYI.UNITNAME,TYI.QUALITATIVE,TYI.PROJECTID,TYI.PROJECTNAME,TYI.QUALITATIVERULE,TYI.PROBLEMQUALITATIVE,TYI.HEAD,TYI.TIMELIMIT,TYSW.WTZGID,TYSW.WTZGFL,TYSW.ZGZT,TYSW.HFSPSTATUS,TYSW.HXSPSTATUS,TYSW.STATUS AS ZGSTATUS,TYSW.DQZGZT,TYI.MONEY,TYSW.DQZGJE,TYSW.DQZJJJCG,TYSW.DQZJJJCGTYPE " + 
				" FROM TBL_YQNS_ISSUE_LIST TYI LEFT JOIN (SELECT TYIR.RECORDID,TYIR.ISSUESID,TYIR.VERSION,TYIR.WTZGID FROM TBL_YQNS_ISSUESRECORD TYIR JOIN ( SELECT ISSUESID,MAX(VERSION) AS VERSION FROM TBL_YQNS_ISSUESRECORD GROUP BY ISSUESID ) TYIRV ON TYIR.ISSUESID = TYIRV.ISSUESID AND TYIR.VERSION = TYIRV.VERSION ) TYIRR ON TYIRR.ISSUESID = TYI.ID")
				.append(" LEFT JOIN TBL_YQNS_SJZG_WTZG TYSW ON TYIRR.WTZGID = TYSW.WTZGID WHERE TYSW.STATUS >= 6 AND TYSW.ZGZT IN ('2','1') AND TYSW.PROJECTID = "+vo.getProjectId()+" AND TYI.PROJECTID = "+vo.getProjectId());
		if(issues.getRectPerson() != null) {
			sqlSb.append(" AND TYI.RECTPERSON = ").append(issues.getRectPerson());
		}
		
		if(StringUtils.isNotBlank(issues.getIssueNumber())) {
			sqlSb.append(" AND TYI.ISSUENUMBER LIKE '%").append(issues.getIssueNumber()).append("%'");
		}
		if(StringUtils.isNotBlank(issues.getUnitName())) {
			sqlSb.append(" AND TYI.UNITNAME LIKE '%").append(issues.getUnitName()).append("%'");
		}
		if(StringUtils.isNotBlank(vo.getZgzt())){
			sqlSb.append(" AND TYSW.ZGZT LIKE '%").append(vo.getZgzt()).append("%'");
		}
		if(StringUtils.isNotBlank(vo.getDqzgzt())) {
			sqlSb.append(" AND TYSW.DQZGZT LIKE '%").append(vo.getDqzgzt()).append("%'");
		}
		if(StringUtils.isNotBlank(vo.getWtzgfl())) {
			sqlSb.append(" AND TYSW.WTZGFL LIKE '%").append(vo.getWtzgfl()).append("%'");
		}
		
		
		if(StringUtils.isNotBlank(issues.getRectClass())) {
			sqlSb.append(" AND TYI.RECTCLASS LIKE '%").append(issues.getRectClass()).append("%'");
		}
		if(vo.getStatus() != null) {
			sqlSb.append(" AND TYSW.STATUS = ").append(vo.getStatus());
		}
		
		sqlSb.append(" ORDER BY TYSW.CJSJ DESC");
		String sql = sqlSb.toString();
		return sql;
	}
	
	public String selectHxzgList(TblYqnsSjzgWtzg vo, TblYqnsIssueListEntity issues) {
		StringBuffer sqlSb = new StringBuffer("SELECT TYSW.PROJECTID,TYSW.PROJECTNAME,TYIL.UNITNAME,TYIL.RECTPERNAME,TYIP.PLAN_YEAR AS PLANYEAR,")
				.append("(SELECT COUNT(0) FROM TBL_YQNS_SJZG_WTZG TYSW1 LEFT JOIN TBL_YQNS_ISSUE_LIST TYIL1 ON TYSW1.ISSUESID = TYIL1.ID WHERE TYSW1.WTZGID IN (SELECT TYIR.WTZGID FROM TBL_YQNS_ISSUESRECORD TYIR JOIN ( SELECT ISSUESID,MAX(VERSION) AS VERSION FROM TBL_YQNS_ISSUESRECORD GROUP BY ISSUESID ) TYIRV ON TYIR.ISSUESID = TYIRV.ISSUESID AND TYIR.VERSION = TYIRV.VERSION) AND TYSW1.ZGZT IN ('2','1') AND TYSW1.STATUS >= 6 AND TYSW1.PROJECTID = TYSW.PROJECTID AND TYIL1.RECTPERSON = "+issues.getRectPerson()+" ) AS WZGCOUNT,")
				.append("(SELECT COUNT(0) FROM TBL_YQNS_SJZG_WTZG TYSW2 LEFT JOIN TBL_YQNS_ISSUE_LIST TYIL2 ON TYSW2.ISSUESID = TYIL2.ID WHERE TYSW2.WTZGID IN (SELECT TYIR.WTZGID FROM TBL_YQNS_ISSUESRECORD TYIR JOIN ( SELECT ISSUESID,MAX(VERSION) AS VERSION FROM TBL_YQNS_ISSUESRECORD GROUP BY ISSUESID ) TYIRV ON TYIR.ISSUESID = TYIRV.ISSUESID AND TYIR.VERSION = TYIRV.VERSION) AND TYSW2.STATUS >= 6  AND TYSW2.PROJECTID = TYSW.PROJECTID  AND TYSW2.ZGZT IN ('2','1') AND TYSW2.HXSPSTATUS = 6 AND TYSW2.DQZGZT = '3' AND TYIL2.RECTPERSON = "+issues.getRectPerson()+" ) AS YWCCOUNT,")
				.append("(SELECT COUNT(0) FROM TBL_YQNS_SJZG_WTZG TYSW3 LEFT JOIN TBL_YQNS_ISSUE_LIST TYIL3 ON TYSW3.ISSUESID = TYIL3.ID WHERE TYSW3.WTZGID IN (SELECT TYIR.WTZGID FROM TBL_YQNS_ISSUESRECORD TYIR JOIN ( SELECT ISSUESID,MAX(VERSION) AS VERSION FROM TBL_YQNS_ISSUESRECORD GROUP BY ISSUESID ) TYIRV ON TYIR.ISSUESID = TYIRV.ISSUESID AND TYIR.VERSION = TYIRV.VERSION) AND TYSW3.STATUS >= 6 AND TYSW3.PROJECTID = TYSW.PROJECTID  AND TYSW3.ZGZT IN ('2','1') AND TYSW3.HXSPSTATUS = 6 AND TYSW3.DQZGZT = '1' AND TYIL3.RECTPERSON = "+issues.getRectPerson()+") AS BZGCOUNT,")
				.append("(SELECT COUNT(0) FROM TBL_YQNS_SJZG_WTZG TYSW4 LEFT JOIN TBL_YQNS_ISSUE_LIST TYIL4 ON TYSW4.ISSUESID = TYIL4.ID WHERE TYSW4.WTZGID IN (SELECT TYIR.WTZGID FROM TBL_YQNS_ISSUESRECORD TYIR JOIN ( SELECT ISSUESID,MAX(VERSION) AS VERSION FROM TBL_YQNS_ISSUESRECORD GROUP BY ISSUESID ) TYIRV ON TYIR.ISSUESID = TYIRV.ISSUESID AND TYIR.VERSION = TYIRV.VERSION) AND TYSW4.STATUS >= 6 AND TYSW4.PROJECTID = TYSW.PROJECTID  AND TYSW4.ZGZT IN ('2','1') AND TYSW4.HXSPSTATUS > 0 AND TYSW4.HXSPSTATUS < 6 AND TYIL4.RECTPERSON = "+issues.getRectPerson()+" ) AS SPZCOUNT ")
				.append(" FROM TBL_YQNS_SJZG_WTZG TYSW LEFT JOIN TBL_YQNS_ISSUE_LIST TYIL ON TYSW.ISSUESID = TYIL.ID LEFT JOIN TBL_YQNS_IMPLEMENT_PLAN TYIP ON TYSW.PROJECTID = TYIP.ID ")
				.append(" WHERE TYSW.WTZGID IN (SELECT TYIR.WTZGID FROM TBL_YQNS_ISSUESRECORD TYIR JOIN ( SELECT ISSUESID,MAX(VERSION) AS VERSION FROM TBL_YQNS_ISSUESRECORD GROUP BY ISSUESID ) TYIRV ON TYIR.ISSUESID = TYIRV.ISSUESID AND TYIR.VERSION = TYIRV.VERSION ) AND TYSW.STATUS >= 6 AND TYSW.ZGZT IN ('2','1') ");
		
		if(issues.getRectPerson() != null) {
			sqlSb.append(" AND TYIL.RECTPERSON = ").append(issues.getRectPerson());
		}
		
		if(StringUtils.isNotBlank(vo.getProjectName())) {
			sqlSb.append(" AND TYSW.PROJECTNAME LIKE '%").append(vo.getProjectName()).append("%'");
		}
		if(StringUtils.isNotBlank(issues.getUnitName())) {
			sqlSb.append(" AND TYIL.UNITNAME LIKE '%").append(issues.getUnitName()).append("%'");
		}
		
		
		sqlSb.append(" GROUP BY TYSW.PROJECTID,TYSW.PROJECTNAME, TYIL.UNITNAME,TYIP.PLAN_YEAR,TYIL.RECTPERNAME ORDER BY TYSW.PROJECTNAME ASC,TYIL.UNITNAME ASC");
		String sql = sqlSb.toString();
		return sql;
	}
	
	
	public String selectGzhfList(TblYqnsSjzgWtzg vo, TblYqnsIssueListEntity issues) {
		StringBuffer sqlSb = new StringBuffer("SELECT TYI.ID,TYI.ISSUENUMBER,TYI.UNITORGID,TYI.UNITNAME,TYI.MONEY,TYI.QUALITATIVE,TYI.PROJECTID,TYI.PROJECTNAME,TYI.QUALITATIVERULE,TYI.PROBLEMQUALITATIVE,TYI.HEAD,TYI.TIMELIMIT,TYSW.WTZGID,TYSW.WTZGFL,TYSW.ZGZT,TYSW.STATUS AS ZGSTATUS,TYSW.DQZGZT,TYSW.HFSPSTATUS FROM TBL_YQNS_ISSUESRECORD TYIR ")
				.append(" JOIN ( SELECT ISSUESID,MAX(VERSION) AS VERSION FROM TBL_YQNS_ISSUESRECORD GROUP BY ISSUESID ) TYIRV ON TYIR.ISSUESID = TYIRV.ISSUESID AND TYIR.VERSION = TYIRV.VERSION  ")
				.append(" LEFT JOIN TBL_YQNS_SJZG_WTZG TYSW ON TYIR.WTZGID = TYSW.WTZGID LEFT JOIN TBL_YQNS_ISSUE_LIST TYI ON TYI.ID = TYSW.ISSUESID ")
				.append(" WHERE 1 = 1 AND TYSW.STATUS >= 6 ");
		//TYI.PROJECTID IN ( SELECT ID FROM TBL_YQNS_IMPLEMENT_PLAN WHERE PROJECT_ORDER_ID = ").append(issues.getRectPerson()).append(" ) 
		if(StringUtils.isNotBlank(issues.getIssueNumber())) {
			sqlSb.append(" AND TYI.ISSUENUMBER LIKE '%").append(issues.getIssueNumber()).append("%'");
		}
		
		if(StringUtils.isNotBlank(issues.getProjectName())) {
			sqlSb.append(" AND TYI.PROJECTNAME LIKE '%").append(issues.getProjectName()).append("%'");
		}
		if(StringUtils.isNotBlank(issues.getUnitName())) {
			sqlSb.append(" AND TYI.UNITNAME LIKE '%").append(issues.getUnitName()).append("%'");
		}
		if(StringUtils.isNotBlank(vo.getZgzt())){
			sqlSb.append(" AND TYSW.ZGZT LIKE '%").append(vo.getZgzt()).append("%'");
		}
		
		if(StringUtils.isNotBlank(vo.getWtzgfl())) {
			sqlSb.append(" AND TYSW.WTZGFL LIKE '%").append(vo.getWtzgfl()).append("%'");
		}
		
		if(vo.getStatus() != null) {
			sqlSb.append(" AND TYSW.STATUS = ").append(vo.getStatus());
		}
		
		sqlSb.append(" ORDER BY TYSW.CJSJ DESC");
		String sql = sqlSb.toString();
		return sql;
	}
	
	public String selectPageInfoList(TblYqnsSjzgWtzg vo, TblYqnsIssueListEntity issues) {
		StringBuffer sqlSb = new StringBuffer("SELECT TYI.ID,TYI.ISSUENUMBER,TYI.UNITORGID,TYI.UNITNAME,TYI.QUALITATIVE,TYI.PROJECTID,TYI.PROJECTNAME,TYI.QUALITATIVERULE,TYI.PROBLEMQUALITATIVE,TYI.HEAD,TYI.TIMELIMIT,TYSW.WTZGID,TYSW.WTZGFL,TYSW.ZGZT,TYSW.STATUS AS ZGSTATUS,TYSW.DQZGZT,TYI.MONEY,TYSW.DQZGJE,TYSW.DQZJJJCG " + 
				" FROM TBL_YQNS_ISSUE_LIST TYI LEFT JOIN (SELECT TYIR.RECORDID,TYIR.ISSUESID,TYIR.VERSION,TYIR.WTZGID FROM TBL_YQNS_ISSUESRECORD TYIR JOIN ( SELECT ISSUESID,MAX(VERSION) AS VERSION FROM TBL_YQNS_ISSUESRECORD GROUP BY ISSUESID ) TYIRV ON TYIR.ISSUESID = TYIRV.ISSUESID AND TYIR.VERSION = TYIRV.VERSION ) TYIRR ON TYIRR.ISSUESID = TYI.ID")
				.append("  LEFT JOIN TBL_YQNS_SJZG_WTZG TYSW ON TYIRR.WTZGID = TYSW.WTZGID WHERE 1 = 1 AND TYI.STATUS = 6 AND ( TYSW.STATUS < 7 OR TYSW.STATUS IS NULL )");
		//  AND TYI.RECTPERSON = ").append(issues.getRectPerson());
		
		if(StringUtils.isNotBlank(issues.getIssueNumber())) {
			sqlSb.append(" AND TYI.ISSUENUMBER LIKE '%").append(issues.getIssueNumber()).append("%'");
		}
		if(StringUtils.isNotBlank(issues.getProjectName())) {
			sqlSb.append(" AND TYI.PROJECTNAME LIKE '%").append(issues.getProjectName()).append("%'");
		}
		
		
		if(StringUtils.isNotBlank(issues.getUnitName())) {
			sqlSb.append(" AND TYI.UNITNAME LIKE '%").append(issues.getUnitName()).append("%'");
		}
		if(StringUtils.isNotBlank(vo.getZgzt())){
			sqlSb.append(" AND TYSW.ZGZT LIKE '%").append(vo.getZgzt()).append("%'");
		} 
		
		if(StringUtils.isNotBlank(vo.getWtzgfl())) {
			sqlSb.append(" AND TYSW.WTZGFL LIKE '%").append(vo.getWtzgfl()).append("%'");
		}
		
		if(vo.getStatus() != null) {
			sqlSb.append(" AND TYSW.STATUS = ").append(vo.getStatus());
		}
		
		if(issues.getRectPerson() != null) {
			sqlSb.append(" AND TYI.RECTPERSON = ").append(issues.getRectPerson());
		}
		
		sqlSb.append(" ORDER BY TYSW.CJSJ DESC");
		String sql = sqlSb.toString();
		return sql;
	}
	

    public String selectCountByPageInfo(PageInfo<TblYqnsSjzgWtzg> pageInfo, TblYqnsSjzgWtzg vo) {
        StringBuffer sb = new StringBuffer("SELECT COUNT(0) "
                + " FROM TBL_YQNS_SJZG_WTZG TBL1 "
                + " WHERE 1=1");
        return sb.toString();
    }


    public String selectListByPageInfo(PageInfo<TblYqnsSjzgWtzg> pageInfo, TblYqnsSjzgWtzg vo) {
        StringBuffer sb = new StringBuffer("SELECT * FROM "
                + "(SELECT T1.*,ROWNUM RN  FROM "
                + "(SELECT TBL1.* "
                + "FROM TBL_YQNS_SJZG_WTZG TBL1 "
                + "WHERE 1=1");
        sb.append(" ORDER BY TBL1.WTZGID DESC) T1 WHERE ROWNUM <= " + (pageInfo.getCurrentRecord() + pageInfo.getPageSize()) + " ) T2 WHERE T2.RN > " + pageInfo.getCurrentRecord());
        return sb.toString();
    }


}




