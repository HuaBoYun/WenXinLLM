package com.huabo.audit.oracle.mapper;

import java.math.BigDecimal;

import com.hbfk.util.DateUtil;
import com.hbfk.util.PageInfo;
import com.huabo.audit.oracle.entity.TblNbsjReformSolution;
import com.huabo.audit.oracle.entity.TblStaff;

public class TblNbsjReformSolutionMapperSqlConfig {
	
	
	public String insertEntity(TblNbsjReformSolution solution){
		StringBuffer colSb = new StringBuffer("INSERT INTO TBL_NBSJ_REFORM_SOLUTION(SOLUTIONID,STAFFID,CREATEDATE,ENDDATE,PROJECTID");
		StringBuffer valSb = new StringBuffer("  VALUES (HIBERNATE_SEQUENCE.nextval,"+solution.getStaffid()+",TO_DATE('"+DateUtil.parseDate(solution.getCreatedate(), "yyyy-MM-dd HH:mm:ss")+"','YYYY-MM-DD HH24:mi:ss'),TO_DATE('"+DateUtil.parseDate(solution.getEnddate(), "yyyy-MM-dd HH:mm:ss")+"','YYYY-MM-DD HH24:mi:ss'),"+solution.getProjectid());
		if(solution.getSolutioncode()!= null && !"".equals(solution.getSolutioncode())) {
			colSb.append(",SOLUTIONCODE");
			valSb.append(",'"+solution.getSolutioncode()+"'");
		}
		
		if(solution.getSolutionname() != null && !"".equals(solution.getSolutionname())) {
			colSb.append(",SOLUTIONNAME");
			valSb.append(",'"+solution.getSolutionname()+"'");
		}
		
		if(solution.getRunstatus() != null ) {
			colSb.append(",RUNSTATUS");
			valSb.append(",'"+solution.getRunstatus()+"'");
		}else {
			colSb.append(",RUNSTATUS");
			valSb.append(",0 ");
		}
		if(solution.getMemo() != null && !"".equals(solution.getMemo())) {
			colSb.append(",MEMO");
			valSb.append(",'"+solution.getMemo()+"'");
		}
		
		if(solution.getOrgid() != null && !"".equals(solution.getOrgid().toString())) {
			colSb.append(",ORGID");
			valSb.append(",'"+solution.getOrgid()+"'");
		}
		
		if(solution.getPmid() != null  ) {
			colSb.append(",PMID");
			valSb.append(",'"+solution.getPmid()+"'");
		}
		if(solution.getParentid() != null  ) {
			colSb.append(",PARENTID");
			valSb.append(","+solution.getParentid());
		}
		if(solution.getBsjdwzrrid() != null  ) { 
			colSb.append(",BSJDWZRRID");
			valSb.append(",'"+solution.getBsjdwzrrid()+"'");
		}
		if(solution.getBsjdwzfr() != null && !"".equals(solution.getBsjdwzfr())) {
			colSb.append(",BSJDWZFR");
			valSb.append(",'"+solution.getBsjdwzfr()+"'");
		}
		
		if(solution.getReformuserid() != null && !"".equals(solution.getReformuserid().toString())) {
			colSb.append(",REFORMUSERID");
			valSb.append(",'"+solution.getReformuserid()+"'");
		}
		
		String sql = colSb.toString()+")"+valSb.toString()+")";
		return sql;
	}

	
	
	public String updateEntity(TblNbsjReformSolution solution){
		StringBuffer colSb = new StringBuffer("UPDATE TBL_NBSJ_REFORM_SOLUTION  SET SOLUTIONID=SOLUTIONID ");
		if(solution.getSolutioncode()!= null && !"".equals(solution.getSolutioncode())) {
			colSb.append(" ,SOLUTIONCODE = '"+solution.getSolutioncode()+"'");
		}
		
		if(solution.getSolutionname() != null && !"".equals(solution.getSolutionname())) {
			colSb.append(" ,SOLUTIONNAME = '"+solution.getSolutionname()+"'");
		}
		
		if(solution.getMemo() != null && !"".equals(solution.getMemo())) {
			colSb.append(" ,MEMO = '"+solution.getMemo()+"'");
		}
		
		if(solution.getEnddate() != null ) {
			colSb.append(" ,enddate = TO_DATE('"+DateUtil.parseDate(solution.getEnddate(), "yyyy-MM-dd")+"','YYYY-MM-DD HH24:mi:ss')");
		}
		
		
		if(solution.getRunstatus() != null && !"".equals(solution.getRunstatus().toString())) {
			colSb.append(" ,RUNSTATUS ="+solution.getRunstatus());
		}
		
		if(solution.getReformuserid() != null && !"".equals(solution.getReformuserid().toString())) {
			colSb.append(" ,REFORMUSERID = "+solution.getReformuserid());
		}
		
		if(solution.getZgstatus() != null && !"".equals(solution.getZgstatus().toString())) {
			colSb.append(" ,ZGSTATUS = "+solution.getZgstatus());
		}
		
		if(solution.getPmid() != null  ) {
			colSb.append(" ,PMID = "+solution.getPmid());
		}
		if(solution.getBsjdwzrrid() != null  ) {
			colSb.append(" ,BSJDWZRRID = "+solution.getBsjdwzrrid());
		}
		if(solution.getBsjdwzfr() != null && !"".equals(solution.getBsjdwzfr())) {
			colSb.append(" ,BSJDWZFR = '"+solution.getBsjdwzfr()+"'");
		}
		
		colSb.append(" WHERE SOLUTIONID = "+solution.getSolutionid());
		return colSb.toString();
	}
	
	
	
	
	public String selectNbsjReformSolutionByPageInfo(PageInfo<TblNbsjReformSolution> pageInfo,BigDecimal orgId,TblNbsjReformSolution solution ,String type) throws Exception {
			
			StringBuffer sb = new StringBuffer("SELECT * FROM (SELECT T1.*,ROWNUM RN  FROM (SELECT SOLUTIONID,SOLUTIONCODE,SOLUTIONNAME,sta.REALNAME, sta.STAFFID,s.CREATEDATE,s.ENDDATE,ST.REALNAME zgname,ST.STAFFID zgstaffid,s.RUNSTATUS,s.ZGSTATUS "
					+ " FROM TBL_NBSJ_REFORM_SOLUTION s  " );
			sb.append(" LEFT JOIN TBL_STAFF sta on s.STAFFID=STA.STAFFID ");
			sb.append(" LEFT JOIN TBL_STAFF st ON s.reformuserid = ST.STAFFID ");
			sb.append("  where 1=1 ");
			if(type!=null && type.equals("1")) {
				sb.append(" and s.reformuserid= "+orgId  +" and s.RUNSTATUS=1 ");
			}else if(type!=null && type.equals("2")) {
				sb.append(" and ( s.SOLUTIONID in ( select DISTINCT SOLUTIONID from TBL_NBSJ_REFOPM where (status=1 or status=2) and PERONINCHARGE= "+orgId+") or (s.reformuserid= "+orgId+" and s.RUNSTATUS<6  and s.RUNSTATUS>1))");
			}else if(type!=null && type.equals("3")) {
				sb.append(" and s.RUNSTATUS=6 and s.ORGID in (select DISTINCT ORGID from TBL_ORGANIZATION where FATHERORGID="+orgId+" OR ORGID="+orgId	+")");
			}else {
				sb.append(" and s.ORGID in (select DISTINCT ORGID from TBL_ORGANIZATION where FATHERORGID="+orgId+" OR ORGID="+orgId	+")");
			}
			if(solution.getSolutioncode()!=null && solution.getSolutioncode().trim().length()>0){
				sb.append(" and SOLUTIONCODE like '%"+solution.getSolutioncode()+"%'");
			}
			if(solution.getSolutionname()!=null && solution.getSolutionname().trim().length()>0){
				sb.append(" and SOLUTIONNAME like '%"+solution.getSolutionname()+"%'");
			}
			
			TblStaff csf = solution.getCreateStaff();
			if(null != csf) {
				if(null != csf.getStaffid()) {
					sb.append(" and s.STAFFID = "+csf.getStaffid()+"");
				}
			}
			if(solution.getRunstatus()!=null){
				sb.append(" and RUNSTATUS = "+solution.getRunstatus()+"");
			}
			
			sb.append(" ORDER BY SOLUTIONID DESC) T1 WHERE ROWNUM <= "+(pageInfo.getCurrentRecord()+pageInfo.getPageSize())+" ) T2 WHERE T2.RN > "+pageInfo.getCurrentRecord());
			return sb.toString();
		}
	
	
	public String selectNbsjReformSolutionByCount(BigDecimal orgId,TblNbsjReformSolution solution,String type) throws Exception {
		
		StringBuffer sb = new StringBuffer("SELECT count(*) FROM TBL_NBSJ_REFORM_SOLUTION s  " );
		sb.append("  where 1=1 ");
		if(type!=null && type.equals("1")) {
			sb.append(" and s.reformuserid= "+orgId);
		}else if(type!=null && type.equals("2")) {
			sb.append(" and ( s.SOLUTIONID in ( select DISTINCT SOLUTIONID from TBL_NBSJ_REFOPM where (status=1 or status=2) and PERONINCHARGE= "+orgId+") or (s.reformuserid= "+orgId+" and s.RUNSTATUS!=6 ))");
			//sb.append(" and s.SOLUTIONID in ( select DISTINCT SOLUTIONID from TBL_NBSJ_REFOPM where (status=1 or status=2) and PERONINCHARGE= "+orgId+") ");
		}else if(type!=null && type.equals("3")) {
			sb.append(" and s.RUNSTATUS=6 and s.ORGID in (select DISTINCT ORGID from TBL_ORGANIZATION where FATHERORGID="+orgId+" OR ORGID="+orgId	+")");
		}else {
			sb.append(" and s.ORGID in (select DISTINCT ORGID from TBL_ORGANIZATION where FATHERORGID="+orgId+" OR ORGID="+orgId	+")");
		}
		
		if(solution.getSolutioncode()!=null && solution.getSolutioncode().trim().length()>0){
			sb.append(" and SOLUTIONCODE like '%"+solution.getSolutioncode()+"%'");
		}
		if(solution.getSolutionname()!=null && solution.getSolutionname().trim().length()>0){
			sb.append(" and SOLUTIONNAME like '%"+solution.getSolutionname()+"%'");
		}
		
		TblStaff csf = solution.getCreateStaff();
		if(null != csf) {
			if(null != csf.getStaffid()) {
				sb.append(" and s.STAFFID = "+csf.getStaffid()+"");
			}
		}
		if(solution.getRunstatus()!=null){
			sb.append(" and RUNSTATUS = "+solution.getRunstatus()+"");
		}
		
		return sb.toString();
	}
	
public String selectNbsjReformSolutionByjg(BigDecimal solutionid) throws Exception {
		
		String sql="select distinct p.PRJOECTNAME,s.ORGIDNAMES orgname,s.businessType,s.SHEETID, dbms_lob.substr(s.AUDITDISCOVERABLE,400,1) as AUDITDISCOVERABLE ,"
		  		+ "r.reformmeasure,r.reformcarryout,r.reformresult, r.nextmplancomdate,r.realname,r.SOLUTIONID,p.audittype,r.reformid,"
		  		+ " dbms_lob.substr(s.AUDITCOURSE,4000,1) as AUDITCOURSE,r.orgname orgname,r.REFORMDEADLINE,r.reformresult reformresults  "
		  + " from TBL_NBSJ_QUESTION q left join TBL_NBSJ_SHEET s on q.sheetid=s.sheetid left join tbl_organization o on o.orgid=s.AUDITORG "+
		  " left join ( SELECT  re.*,sa.realname,org.orgname FROM  TBL_NBSJ_REFOPM re LEFT JOIN TBL_STAFF sa ON re.PERONINCHARGE = SA.STAFFID "
		  + " LEFT JOIN TBL_ORGANIZATION org on sa.orgid=org.orgid "
		  + " ) r on r.questionid=q.questionid"+
		  " left join tbl_nbsj_project p on p.projectid=s.projectid where solutionid is not null  and r.status='3' ";
		if(solutionid!=null ){
			sql+="  and r.SOLUTIONID= "+solutionid;
		}
		sql+=" ORDER BY s.businessType DESC ";
		return sql;
	}
	
}
