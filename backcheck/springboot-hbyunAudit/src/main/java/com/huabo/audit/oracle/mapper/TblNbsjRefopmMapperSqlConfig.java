package com.huabo.audit.oracle.mapper;

import java.math.BigDecimal;

import com.hbfk.util.DateUtil;
import com.hbfk.util.PageInfo;
import com.huabo.audit.oracle.entity.TblNbsjRefopm;

public class TblNbsjRefopmMapperSqlConfig {
	
	public String insertEntity(TblNbsjRefopm re){
		StringBuffer colSb = new StringBuffer("INSERT INTO TBL_NBSJ_REFOPM(REFORMID,createid,CREATEDATE,STATUS,PROJECTID,SOLUTIONID");
		StringBuffer valSb = new StringBuffer("  VALUES (HIBERNATE_SEQUENCE.nextval,"+re.getCreateid()+",TO_DATE('"+DateUtil.parseDate(re.getCreatedate(), "yyyy-MM-dd")+"','YYYY-MM-DD HH24:mi:ss'),1,"+re.getProjectid()+","+re.getSolutionid());
		if(re.getBugid()!= null && !"".equals(re.getBugid().toString())) {
			colSb.append(",BUGID");
			valSb.append(",'"+re.getBugid()+"'");
		}
		
		if(re.getQuestionid() != null && !"".equals(re.getQuestionid().toString())) {
			colSb.append(",QUESTIONID");
			valSb.append(",'"+re.getQuestionid()+"'");
		}
		if(re.getPeronincharge()!=null) {
			colSb.append(",PERONINCHARGE");
			valSb.append(",'"+re.getPeronincharge()+"'");
		}
		if(re.getLastreformstatus()!=null) {
			colSb.append(",LASTREFORMSTATUS");
			valSb.append(",'"+re.getLastreformstatus()+"'");
		}
		
		
		if(re.getNextmeasures()!=null && !re.getNextmeasures().equals("")) {
			colSb.append(",nextmeasures");
			valSb.append(",'"+re.getNextmeasures()+"'");
		}
		
		
		String sql = colSb.toString()+")"+valSb.toString()+")";
		return sql;
	}

	public String updateEntity(TblNbsjRefopm re){
		StringBuffer colSb = new StringBuffer("UPDATE TBL_NBSJ_REFOPM  SET createid=createid ");
		if(re.getPeronincharge()!= null && !"".equals(re.getPeronincharge().toString())) {
			colSb.append(" ,PERONINCHARGE = "+re.getPeronincharge());
		}
		
		if(re.getZgstatus()!= null && !"".equals(re.getZgstatus().toString())) {
			colSb.append(" ,ZGSTATUS = "+re.getZgstatus());
		}
		
		if(re.getStatus()!= null && !"".equals(re.getStatus().toString())) {
			colSb.append(" ,STATUS = "+re.getStatus());
		}
		
		if(re.getLastreformstatus() != null && !"".equals(re.getLastreformstatus().toString())) {
			colSb.append(" ,LASTREFORMSTATUS = "+re.getLastreformstatus());
		}
		
		if(re.getCloseyy() != null && !"".equals(re.getCloseyy())) {
			colSb.append(" ,CLOSEYY = '"+re.getCloseyy()+"'");
		}
		
		if(re.getWzgreason() != null && !"".equals(re.getWzgreason())) {
			colSb.append(" ,WZGREASON = '"+re.getWzgreason()+"'");
		}
		
		if(re.getZgwdreason() != null && !"".equals(re.getZgwdreason())) {
			colSb.append(" ,ZGWDREASON = '"+re.getZgwdreason()+"'");
		}
		
		if(re.getThreason() != null && !"".equals(re.getThreason())) {
			colSb.append(" ,THREASON = '"+re.getThreason()+"'");
		}
		
		if(re.getInspect() != null && !"".equals(re.getInspect())) {
			colSb.append(" ,INSPECT = '"+re.getInspect()+"'");
		}
		
		if(re.getNextmplancomdate() != null ) {
			colSb.append(" ,NEXTMPLANCOMDATE = TO_DATE('"+DateUtil.parseDate(re.getNextmplancomdate(), "yyyy-MM-dd HH:mm:ss")+"','YYYY-MM-DD HH24:mi:ss')");
		}
		
		if(re.getNextmeasures() != null && !"".equals(re.getNextmeasures())) {
			colSb.append(" ,NEXTMEASURES = '"+re.getNextmeasures()+"'");
		}
		
		if(re.getHandling() != null && !"".equals(re.getHandling())) {
			colSb.append(" ,HANDLING = '"+re.getHandling()+"'");
		}
		
		if(re.getReformdeadline() != null ) {
			colSb.append(" ,REFORMDEADLINE = TO_DATE('"+DateUtil.parseDate(re.getReformdeadline(), "yyyy-MM-dd HH:mm:ss")+"','YYYY-MM-DD HH24:mi:ss')");
		}
		
		if(re.getReformcarryout() != null && !"".equals(re.getReformcarryout())) {
			colSb.append(" ,REFORMCARRYOUT = '"+re.getReformcarryout()+"'");
		}
		
		if(re.getReformresult() != null && !"".equals(re.getReformresult())) {
			colSb.append(" ,REFORMRESULT = '"+re.getReformresult()+"'");
		}
		
		if(re.getReformmeasure() != null && !"".equals(re.getReformmeasure())) {
			colSb.append(" ,REFORMMEASURE = '"+re.getReformmeasure()+"'");
		}
		
		if(re.getZgjgresult() != null && !"".equals(re.getZgjgresult())) {
			colSb.append(" ,ZGJGRESULT = '"+re.getZgjgresult()+"'");
		}
		
		if(re.getZgdwdate() != null ) {
			colSb.append(" ,ZGDWDATE = TO_DATE('"+DateUtil.parseDate(re.getZgdwdate(), "yyyy-MM-dd HH:mm:ss")+"','YYYY-MM-DD HH24:mi:ss')");
		}
		
		
		if(re.getWxhstatus() != null && !"".equals(re.getWxhstatus().toString())) {
			colSb.append(" ,WXHSTATUS = "+re.getWxhstatus());
		}else {
			colSb.append(" ,WXHSTATUS = null ");
		}
		
		colSb.append(" WHERE REFORMID = "+re.getReformid());
		return colSb.toString();
	}
	
	
	public String   selectNbsjReformByPageInfo(PageInfo<TblNbsjRefopm> pageInfo, BigDecimal projectid, TblNbsjRefopm re,String selectidIdsstr) throws Exception {
		
		StringBuffer sb = new StringBuffer("SELECT * FROM (SELECT T1.*,ROWNUM RN  FROM (select * from ( SELECT DISTINCT BUG.BUGID||'__b' problemid, BUG.BUGNUMBER code,o.orgname company,'缺陷'  source,BUG.DISCOVERPERSON discoverer,bug.BUGDESCRIPTE details FROM TBL_NBSJ_BUG bug LEFT JOIN TBL_ORGANIZATION o ON o.orgid = BUG.BUGDEPARTMENT WHERE BUG.NEEDREFORM = '是' AND BUG.INBUGIDB IS NULL and BUG.BUGBYSYSTEM like '%nb%'   " );
		if(null!=selectidIdsstr&&!"".equals(selectidIdsstr)) {
			sb.append(" AND BUG.BUGID NOT IN ("+(selectidIdsstr.replace("__p", "")).replace("__b", "")+")");
		 }
		
		if (re.getCode()!=null ) {
			sb.append(" and BUG.BUGNUMBER like'%"+re.getCode()+"%' ");
		 }
		
		if(null!=projectid ) {
			sb.append(" and BUG.PROJECTID ="+projectid);
		 }
		
		sb.append(" UNION ALL ");
		sb.append(" SELECT DISTINCT QU.QUESTIONID||'__p' problemid, AA.SHEETCODE code,aa.orgIdNames company,'审计发现'  source, AA.REALNAME  discoverer,dbms_lob.substr(aa.AUDITDISCOVERABLE,400,1) details ");
		sb.append(" FROM TBL_NBSJ_QUESTION qu LEFT JOIN (SELECT SH.SHEETID, SA.REALNAME, SH.PROJECTID, SH.SHEETCODE, SH.CREATESTAFF, ORG.ORGNAME,SH.AUDITDISCOVERABLE,sh.orgIdNames ");
		sb.append(" FROM TBL_NBSJ_SHEET sh LEFT JOIN TBL_STAFF sa ON SH.CREATESTAFF = SA.STAFFID LEFT JOIN TBL_ORGANIZATION org ON sh.AUDITORG = ORG.ORGID  ) aa ON QU.SHEETID = AA.SHEETID  ");
		sb.append("  WHERE 1 = 1 AND QU.RECSTATUS = 1 AND AA.PROJECTID = "+projectid);
		
		if(null!=selectidIdsstr&&!"".equals(selectidIdsstr)) {
			sb.append(" AND QU.QUESTIONID NOT IN ("+(selectidIdsstr.replace("__p", "")).replace("__b", "")+")");
		 }
		if (re.getCode()!=null ) {
			sb.append(" and SHEETCODE like'%"+re.getCode()+"%' ");
		 }
		 
		
		sb.append(" ) ORDER BY problemid ) T1 WHERE ROWNUM <= "+(pageInfo.getCurrentRecord()+pageInfo.getPageSize())+" ) T2 WHERE T2.RN > "+pageInfo.getCurrentRecord());
		return sb.toString();
	}
	
	
	
	
	
	public String   selectNbsjReformByProjectCount( BigDecimal projectid, TblNbsjRefopm re,String selectidIdsstr) throws Exception {
		
		StringBuffer sb = new StringBuffer("select count(*) from ( SELECT DISTINCT BUG.BUGID||'__b' problemid, BUG.BUGNUMBER code,o.orgname company,'缺陷'  source,BUG.DISCOVERPERSON discoverer,bug.BUGDESCRIPTE details FROM TBL_NBSJ_BUG bug LEFT JOIN TBL_ORGANIZATION o ON o.orgid = BUG.BUGDEPARTMENT WHERE BUG.NEEDREFORM = '是' AND BUG.INBUGIDB IS NULL and BUG.BUGBYSYSTEM like '%nb%'   " );
		if(null!=selectidIdsstr&&!"".equals(selectidIdsstr)) {
			sb.append(" AND BUG.BUGID NOT IN nvl((SELECT BUGID FROM TBL_NBSJ_REFOPM where REFORMID IN ("+selectidIdsstr+")),0)");
		 }
		
		if (re.getCode()!=null ) {
			sb.append(" and BUG.BUGNUMBER like'%"+re.getCode()+"%' ");
		 }
		
		if(null!=projectid ) {
			sb.append(" and BUG.PROJECTID ="+projectid);
		 }
		
		sb.append(" UNION ALL ");
		sb.append(" SELECT DISTINCT QU.QUESTIONID||'__p' problemid, AA.SHEETCODE code,aa.orgname company,'审计发现'  source, AA.REALNAME  discoverer,dbms_lob.substr(aa.AUDITDISCOVERABLE,400,1) details ");
		sb.append(" FROM TBL_NBSJ_QUESTION qu LEFT JOIN (SELECT SH.SHEETID, SA.REALNAME, SH.PROJECTID, SH.SHEETCODE, SH.CREATESTAFF, ORG.ORGNAME,SH.AUDITDISCOVERABLE ");
		sb.append(" FROM TBL_NBSJ_SHEET sh LEFT JOIN TBL_STAFF sa ON SH.CREATESTAFF = SA.STAFFID LEFT JOIN TBL_ORGANIZATION org ON sh.AUDITORG = ORG.ORGID  ) aa ON QU.SHEETID = AA.SHEETID  ");
		sb.append("  WHERE 1 = 1 AND QU.RECSTATUS = 1 AND AA.PROJECTID = "+projectid);
		
		if(null!=selectidIdsstr&&!"".equals(selectidIdsstr)) {
			sb.append(" AND QU.QUESTIONID NOT IN ("+selectidIdsstr+")");
		 }
		if (re.getCode()!=null ) {
			sb.append(" and SHEETCODE like'%"+re.getCode()+"%' ");
		 }
		
		
		sb.append(" ) ORDER BY problemid ");
		return sb.toString();
	}
	
	
	
	
	public String   selectNbsjReformBySolutionPageInfo(PageInfo<TblNbsjRefopm> pageInfo, BigDecimal solutionid, TblNbsjRefopm re,String type) throws Exception {
		
		StringBuffer sb = new StringBuffer("SELECT * FROM (SELECT T1.*,ROWNUM RN  FROM (select * from ( SELECT DISTINCT BUG.BUGID||'__b' problemid,BUG.BUGNUMBER code,o.orgname company,'缺陷' source,BUG.DISCOVERPERSON discoverer,sa.REALNAME zgzxxrname,bug.BUGDESCRIPTE details,RE.STATUS STATUS,re.REFORMID REFORMID,RE.REFORMRESULT,RE.REFORMMEASURE,RE.REFORMCARRYOUT  " );
		sb.append(" FROM TBL_NBSJ_BUG bug LEFT JOIN TBL_NBSJ_REFOPM re ON BUG.BUGID = RE.BUGID LEFT JOIN TBL_ORGANIZATION o ON o.orgid = BUG.BUGDEPARTMENT  ");
		sb.append(" LEFT JOIN TBL_STAFF sa ON re.PERONINCHARGE = SA.STAFFID  ");
		sb.append(" WHERE BUG.NEEDREFORM = '是' AND BUG.INBUGIDB IS NULL  AND SOLUTIONID = "+solutionid);
		
		if (re.getCode()!=null ) {
			sb.append(" and BUG.BUGNUMBER like'%"+re.getCode()+"%' ");
		 }
		
		
		if(type!=null && type.equals("1")) {
			sb.append(" AND RE.STATUS <> 0 AND (RE.LASTREFORMSTATUS is null or RE.LASTREFORMSTATUS=1) ");
		}
		if(type!=null && type.equals("2")) {
			sb.append(" AND RE.STATUS <> 0 AND  RE.LASTREFORMSTATUS=1 and re.status<>3 ");
			sb.append(" AND sa.staffid = " +re.getPeronincharge());
		}
		 
		sb.append(" UNION ALL ");
		sb.append(" SELECT DISTINCT QU.QUESTIONID||'__p' problemid, AA.SHEETCODE code,aa.ORGIDNAMES company,'审计发现' source,AA.REALNAME discoverer,bb.REALNAME zgzxxrname,dbms_lob.substr(aa.AUDITDISCOVERABLE,400,1) details,bb.STATUS  STATUS,bb.REFORMID REFORMID,bb.REFORMRESULT,bb.REFORMMEASURE,bb.REFORMCARRYOUT ");
		sb.append(" FROM TBL_NBSJ_QUESTION qu LEFT JOIN ( SELECT SH.SHEETID,SA.REALNAME,SH.PROJECTID,SH.SHEETCODE,SH.CREATESTAFF,ORG.ORGNAME,SH.AUDITDISCOVERABLE,SH.ORGIDNAMES FROM TBL_NBSJ_SHEET sh   ");
		sb.append(" LEFT JOIN TBL_STAFF sa ON SH.CREATESTAFF = SA.STAFFID LEFT JOIN TBL_ORGANIZATION org ON sh.AUDITORG = ORG.ORGID ) aa ON QU.SHEETID = AA.SHEETID  ");
		sb.append(" LEFT JOIN ( SELECT RE.QUESTIONID,SA.REALNAME,RE.STATUS,RE.LASTREFORMSTATUS,RE.SOLUTIONID,re.REFORMID,re.PERONINCHARGE,RE.REFORMRESULT,RE.REFORMMEASURE,RE.REFORMCARRYOUT FROM TBL_NBSJ_REFOPM re LEFT JOIN TBL_STAFF sa ON re.PERONINCHARGE = SA.STAFFID ) bb ON BB.QUESTIONID = QU.QUESTIONID  ");
		sb.append("  WHERE 1 = 1 AND QU.RECSTATUS = 1    and bb.SOLUTIONID= "+solutionid);
		
		if (re.getCode()!=null ) {
			sb.append(" and SHEETCODE like'%"+re.getCode()+"%' ");
		 }
		if(type!=null && type.equals("1")) {
			sb.append(" AND bb.status<>0 and ( bb.LASTREFORMSTATUS is null or bb.LASTREFORMSTATUS =1) ");
		}
		if(type!=null && type.equals("2")) {
			sb.append(" AND bb.status<>0 and bb.LASTREFORMSTATUS =1 AND bb.status<>3 ");
			sb.append(" AND bb.PERONINCHARGE= "+re.getPeronincharge());
		}
		
		
		sb.append(" ) ORDER BY problemid ) T1 WHERE ROWNUM <= "+(pageInfo.getCurrentRecord()+pageInfo.getPageSize())+" ) T2 WHERE T2.RN > "+pageInfo.getCurrentRecord());
		return sb.toString();
	}
	
	
	public String   selectNbsjReformBySolutionCount(BigDecimal SOLUTIONID, TblNbsjRefopm re,String type) throws Exception {
		
		StringBuffer sb = new StringBuffer("select count(*) from ( SELECT DISTINCT BUG.BUGID||'__b' problemid,BUG.BUGNUMBER code,o.orgname company,'缺陷' source,BUG.DISCOVERPERSON discoverer,sa.REALNAME zgzxxrname,bug.BUGDESCRIPTE details,RE.STATUS STATUS,re.REFORMID REFORMID  " );
		sb.append(" FROM TBL_NBSJ_BUG bug LEFT JOIN TBL_NBSJ_REFOPM re ON BUG.BUGID = RE.BUGID LEFT JOIN TBL_ORGANIZATION o ON o.orgid = BUG.BUGDEPARTMENT  ");
		sb.append(" LEFT JOIN TBL_STAFF sa ON re.PERONINCHARGE = SA.STAFFID  ");
		sb.append(" WHERE BUG.NEEDREFORM = '是' AND BUG.INBUGIDB IS NULL  AND SOLUTIONID = "+SOLUTIONID);
		
		if (re.getCode()!=null ) {
			sb.append(" and BUG.BUGNUMBER like'%"+re.getCode()+"%' ");
		 }
		
		if(type!=null && type.equals("1")) {
			sb.append(" AND RE.STATUS <> 0 AND (RE.LASTREFORMSTATUS is null or RE.LASTREFORMSTATUS=1) ");
		}
		sb.append(" UNION ALL ");
		sb.append(" SELECT DISTINCT QU.QUESTIONID||'__p' problemid, AA.SHEETCODE code,aa.orgname company,'审计发现' source,AA.REALNAME discoverer,bb.REALNAME zgzxxrname,dbms_lob.substr(aa.AUDITDISCOVERABLE,400,1) details,bb.STATUS  STATUS,bb.REFORMID REFORMID ");
		sb.append(" FROM TBL_NBSJ_QUESTION qu LEFT JOIN ( SELECT SH.SHEETID,SA.REALNAME,SH.PROJECTID,SH.SHEETCODE,SH.CREATESTAFF,ORG.ORGNAME,SH.AUDITDISCOVERABLE FROM TBL_NBSJ_SHEET sh   ");
		sb.append(" LEFT JOIN TBL_STAFF sa ON SH.CREATESTAFF = SA.STAFFID LEFT JOIN TBL_ORGANIZATION org ON sh.AUDITORG = ORG.ORGID ) aa ON QU.SHEETID = AA.SHEETID  ");
		sb.append(" LEFT JOIN ( SELECT RE.QUESTIONID,SA.REALNAME,RE.STATUS,RE.LASTREFORMSTATUS,RE.SOLUTIONID,re.REFORMID,re.PERONINCHARGE FROM TBL_NBSJ_REFOPM re LEFT JOIN TBL_STAFF sa ON re.PERONINCHARGE = SA.STAFFID ) bb ON BB.QUESTIONID = QU.QUESTIONID  ");
		sb.append("  WHERE 1 = 1 AND QU.RECSTATUS = 1   and bb.SOLUTIONID= "+SOLUTIONID);
		
		if (re.getCode()!=null ) {
			sb.append(" and SHEETCODE like'%"+re.getCode()+"%' ");
		 }
		if(type!=null && type.equals("1")) {
			sb.append(" and bb.status<>0 and ( bb.LASTREFORMSTATUS is null or bb.LASTREFORMSTATUS =1) ");
		}
		if(type!=null && type.equals("2")) {
			sb.append(" AND bb.status<>0 and bb.LASTREFORMSTATUS =1 AND bb.status<>3 ");
			sb.append(" AND bb.PERONINCHARGE= "+re.getPeronincharge());
		}
		
		sb.append(" ) ORDER BY problemid   ");
		return sb.toString();
	}
	 
	
	
	
	public String   selectNbsjReformBySolutionwt(BigDecimal soultionid, String bugid,String souid) throws Exception {
		
		StringBuffer sb = new StringBuffer(" select * from  TBL_NBSJ_REFOPM where SOLUTIONID="+soultionid);
		if(bugid!=null && bugid.length()>0) {
			sb.append(" and BUGID="+bugid);
		}
		if(souid!=null && souid.length()>0) {
			sb.append(" and QUESTIONID="+souid);
		}
		sb.append(" and reformresult is not null  ");
		return sb.toString();
	}
	
	
	
	public String   selectNbsjReformBySolutionAll(BigDecimal solutionid, TblNbsjRefopm re) throws Exception {
		
		StringBuffer sb = new StringBuffer("select * from ( SELECT DISTINCT BUG.BUGID||'__b' problemid,BUG.BUGNUMBER code,o.orgname company,'缺陷' source,BUG.DISCOVERPERSON discoverer,sa.REALNAME zgzxxrname,bug.BUGDESCRIPTE details,RE.STATUS STATUS,re.REFORMID REFORMID,RE.REFORMRESULT,RE.REFORMMEASURE,RE.REFORMCARRYOUT  " );
		sb.append(" FROM TBL_NBSJ_BUG bug LEFT JOIN TBL_NBSJ_REFOPM re ON BUG.BUGID = RE.BUGID LEFT JOIN TBL_ORGANIZATION o ON o.orgid = BUG.BUGDEPARTMENT  ");
		sb.append(" LEFT JOIN TBL_STAFF sa ON re.PERONINCHARGE = SA.STAFFID  ");
		sb.append(" WHERE BUG.NEEDREFORM = '是'  AND BUG.INBUGIDB IS NULL AND RE.STATUS <> 0 AND (RE.LASTREFORMSTATUS is null or RE.LASTREFORMSTATUS=1) AND SOLUTIONID = "+solutionid);
		
		if (re!=null && re.getCode()!=null ) {
			sb.append(" and BUG.BUGNUMBER like'%"+re.getCode()+"%' ");
		 }
		
		
		sb.append(" UNION ALL ");
		sb.append(" SELECT DISTINCT QU.QUESTIONID||'__p' problemid, AA.SHEETCODE code,aa.orgname company,'审计发现' source,AA.REALNAME discoverer,bb.REALNAME zgzxxrname,dbms_lob.substr(aa.AUDITDISCOVERABLE,400,1) details,bb.STATUS  STATUS,bb.REFORMID REFORMID,bb.REFORMRESULT,bb.REFORMMEASURE,bb.REFORMCARRYOUT ");
		sb.append(" FROM TBL_NBSJ_QUESTION qu LEFT JOIN ( SELECT SH.SHEETID,SA.REALNAME,SH.PROJECTID,SH.SHEETCODE,SH.CREATESTAFF,ORG.ORGNAME,SH.AUDITDISCOVERABLE FROM TBL_NBSJ_SHEET sh   ");
		sb.append(" LEFT JOIN TBL_STAFF sa ON SH.CREATESTAFF = SA.STAFFID LEFT JOIN TBL_ORGANIZATION org ON sh.AUDITORG = ORG.ORGID ) aa ON QU.SHEETID = AA.SHEETID  ");
		sb.append(" LEFT JOIN ( SELECT RE.QUESTIONID,SA.REALNAME,RE.STATUS,RE.LASTREFORMSTATUS,RE.SOLUTIONID,re.REFORMID,RE.REFORMRESULT,RE.REFORMMEASURE,RE.REFORMCARRYOUT FROM TBL_NBSJ_REFOPM re LEFT JOIN TBL_STAFF sa ON re.PERONINCHARGE = SA.STAFFID ) bb ON BB.QUESTIONID = QU.QUESTIONID  ");
		sb.append("  WHERE 1 = 1 AND QU.RECSTATUS = 1 AND bb.status<>0 and ( bb.LASTREFORMSTATUS is null or bb.LASTREFORMSTATUS =1)  and bb.SOLUTIONID= "+solutionid);
		
		if (re!=null && re.getCode()!=null ) {
			sb.append(" and SHEETCODE like'%"+re.getCode()+"%' ");
		 }
		
		
		sb.append(" ) ORDER BY problemid     ");
		return sb.toString();
	}
	
	
	
	
public String   selectNbsjReformById(BigDecimal reformid) throws Exception {
		
		StringBuffer sb = new StringBuffer("select * from ( SELECT DISTINCT BUG.BUGID||'__b' problemid,BUG.BUGNUMBER code,o.orgname company,'缺陷' source,BUG.DISCOVERPERSON discoverer,sa.REALNAME zgzxxrname,bug.BUGDESCRIPTE details,RE.STATUS STATUS,re.REFORMID REFORMID,re.reformmeasure,RE.reformresult,RE.reformcarryout,RE.handling,re.reformdeadline,RE.nextmeasures,re.nextmplancomdate,re.inspect,RE.wzgreason,re.zgwdreason,re.threason,re.zgjgresult,re.closeyy,re.solutionid,RE.PERONINCHARGE  " );
		sb.append(" FROM TBL_NBSJ_BUG bug LEFT JOIN TBL_NBSJ_REFOPM re ON BUG.BUGID = RE.BUGID LEFT JOIN TBL_ORGANIZATION o ON o.orgid = BUG.BUGDEPARTMENT  ");
		sb.append(" LEFT JOIN TBL_STAFF sa ON re.PERONINCHARGE = SA.STAFFID  ");
		sb.append(" WHERE BUG.NEEDREFORM = '是' AND BUG.INBUGIDB IS NULL AND RE.STATUS <> 0 AND (RE.LASTREFORMSTATUS is null or RE.LASTREFORMSTATUS=1) AND re.reformid = "+reformid);
		
		sb.append(" UNION ALL ");
		sb.append(" SELECT DISTINCT QU.QUESTIONID||'__p' problemid, AA.SHEETCODE code,aa.orgname company,'审计发现' source,AA.REALNAME discoverer,bb.REALNAME zgzxxrname,dbms_lob.substr(aa.AUDITDISCOVERABLE,400,1) details,bb.STATUS  STATUS,bb.REFORMID REFORMID,bb.reformmeasure,bb.reformresult,bb.reformcarryout,bb.handling,bb.reformdeadline,bb.nextmeasures,bb.nextmplancomdate,bb.inspect,bb.wzgreason,bb.zgwdreason,bb.threason,bb.zgjgresult,bb.closeyy,bb.solutionid,bb.PERONINCHARGE ");
		sb.append(" FROM TBL_NBSJ_QUESTION qu LEFT JOIN ( SELECT SH.SHEETID,SA.REALNAME,SH.PROJECTID,SH.SHEETCODE,SH.CREATESTAFF,ORG.ORGNAME,SH.AUDITDISCOVERABLE FROM TBL_NBSJ_SHEET sh   ");
		sb.append(" LEFT JOIN TBL_STAFF sa ON SH.CREATESTAFF = SA.STAFFID LEFT JOIN TBL_ORGANIZATION org ON sh.AUDITORG = ORG.ORGID ) aa ON QU.SHEETID = AA.SHEETID  ");
		sb.append(" LEFT JOIN ( SELECT RE.QUESTIONID,SA.REALNAME,RE.STATUS,RE.LASTREFORMSTATUS,RE.SOLUTIONID,re.REFORMID,re.reformmeasure,RE.reformresult,RE.reformcarryout,RE.handling,re.reformdeadline,RE.nextmeasures,re.nextmplancomdate,re.inspect,RE.wzgreason,re.zgwdreason,re.threason,re.zgjgresult,re.closeyy,RE.PERONINCHARGE FROM TBL_NBSJ_REFOPM re LEFT JOIN TBL_STAFF sa ON re.PERONINCHARGE = SA.STAFFID ) bb ON BB.QUESTIONID = QU.QUESTIONID  ");
		sb.append("  WHERE 1 = 1 AND QU.RECSTATUS = 1 AND bb.status<>0 and ( bb.LASTREFORMSTATUS is null or bb.LASTREFORMSTATUS =1)  and bb.reformid= "+reformid);
		
		
		sb.append(" ) ORDER BY problemid   ");
		return sb.toString();
	}
	
	public String   selectNbsjReformBySolutionZsCount(BigDecimal soultionid) throws Exception {
		StringBuffer sb = new StringBuffer("select count(*) from  TBL_NBSJ_REFOPM where   SOLUTIONID=" +soultionid+" and LASTREFORMSTATUS=1   ORDER BY  REFORMID DESC");
		return sb.toString();
	}

	public String   selectNbsjReformBySolutionYwcsCount(BigDecimal soultionid) throws Exception {
		StringBuffer sb = new StringBuffer("select count(*) from  TBL_NBSJ_REFOPM where   SOLUTIONID=" +soultionid+" and STATUS=3 and LASTREFORMSTATUS=1   ORDER BY  REFORMID DESC");
		return sb.toString();
	}
	
	
	public String   selectNbsjReformBySolutionRwAll( BigDecimal soultionid,BigDecimal userid) throws Exception {
		
		StringBuffer sb = new StringBuffer("select reformid,status,wxhstatus from  TBL_NBSJ_REFOPM where PERONINCHARGE = "+userid+" and SOLUTIONID=" +soultionid+" and status=1  and LASTREFORMSTATUS=1 ORDER BY  REFORMID DESC");
		return sb.toString();
	}
	
	public String   selectNbsjReformBySolutionRwAlls( BigDecimal soultionid) throws Exception {
		
		StringBuffer sb = new StringBuffer("select reformid,status,wxhstatus from  TBL_NBSJ_REFOPM where  SOLUTIONID=" +soultionid+" and status=1  and LASTREFORMSTATUS=1 ORDER BY  REFORMID DESC");
		return sb.toString();
	}
	
	
	public String   selectNbsjReformBySolutionRwwcAll( BigDecimal soultionid) throws Exception {
		
		StringBuffer sb = new StringBuffer("select reformid,status,wxhstatus from  TBL_NBSJ_REFOPM where  SOLUTIONID=" +soultionid+" and status=2  and LASTREFORMSTATUS=1 ORDER BY  REFORMID DESC");
		return sb.toString();
	}
	
	public String   selectNbsjReformBySolutionpjCount(BigDecimal soultionid) throws Exception {
		StringBuffer sb = new StringBuffer("select count(*) from  TBL_NBSJ_REFOPM where   SOLUTIONID=" +soultionid+" and LASTREFORMSTATUS=1   ORDER BY  REFORMID DESC");
		return sb.toString();
	}
	
	public String   selectNbsjReformBySolutionpjwcCount(BigDecimal soultionid) throws Exception {
		StringBuffer sb = new StringBuffer("select count(*) from  TBL_NBSJ_REFOPM where   SOLUTIONID=" +soultionid+" and zgjgresult is not null  and LASTREFORMSTATUS=1   ORDER BY  REFORMID DESC");
		return sb.toString();
	}
	
	
	public String   selectNbsjReformBySolutionPageInfowxh(PageInfo<TblNbsjRefopm> pageInfo,TblNbsjRefopm re) throws Exception {
		
		StringBuffer sb = new StringBuffer("SELECT * FROM (SELECT T1.*,ROWNUM RN  FROM (select * from ( SELECT DISTINCT BUG.BUGID||'__b' problemid,BUG.BUGNUMBER code,o.orgname company,'缺陷' source,BUG.DISCOVERPERSON discoverer,sa.REALNAME zgzxxrname,bug.BUGDESCRIPTE details,RE.STATUS STATUS,re.REFORMID REFORMID,re.zgjgresult, re.WXHSTATUS,pro.PRJOECTNAME,pro.AUDITORGID,re.SOLUTIONID   " );
		sb.append(" FROM TBL_NBSJ_BUG bug  LEFT JOIN TBL_NBSJ_PROJECT pro on BUG.PROJECTID=PRO.PROJECTID LEFT JOIN TBL_NBSJ_REFOPM re ON BUG.BUGID = RE.BUGID LEFT JOIN TBL_ORGANIZATION o ON o.orgid = BUG.BUGDEPARTMENT  ");
		sb.append(" LEFT JOIN TBL_STAFF sa ON re.PERONINCHARGE = SA.STAFFID  ");
		sb.append(" WHERE BUG.NEEDREFORM = '是' AND BUG.INBUGIDB IS NULL  AND ( (re.STATUS =3 and re.LASTREFORMSTATUS =1   AND  re.reformresult  is not null and re.zgjgresult!='已完成整改'  ) or (  re.WXHSTATUS=1 and re.LASTREFORMSTATUS =1 ) )   ");
		
		if (re.getCode()!=null ) {
			sb.append(" and BUG.BUGNUMBER like'%"+re.getCode()+"%' ");
		 }
		if (re.getCode()!=null ) {
			sb.append(" and pro.PRJOECTNAME like'%"+re.getProjectname()+"%' ");
		 }
		if(re.getCompany()!=null && re.getCompany().length()>0) {
			sb.append(" and pro.AUDITORGID = "+re.getCompany());
		}
		
		sb.append(" UNION ALL ");
		sb.append(" SELECT DISTINCT QU.QUESTIONID||'__p' problemid, AA.SHEETCODE code,aa.orgname company,'审计发现' source,AA.REALNAME discoverer,bb.REALNAME zgzxxrname,dbms_lob.substr(aa.AUDITDISCOVERABLE,400,1) details,bb.STATUS  STATUS,bb.REFORMID,bb.zgjgresult,bb.WXHSTATUS,aa.PRJOECTNAME,aa.AUDITORGID,bb.SOLUTIONID ");
		sb.append(" FROM TBL_NBSJ_QUESTION qu LEFT JOIN ( SELECT SH.SHEETID,SA.REALNAME,SH.PROJECTID,SH.SHEETCODE,SH.CREATESTAFF,ORG.ORGNAME,SH.AUDITDISCOVERABLE,PRO.PRJOECTNAME,PRO.AUDITORGID FROM TBL_NBSJ_SHEET sh   LEFT JOIN TBL_NBSJ_PROJECT pro on SH.PROJECTID=PRO.PROJECTID  ");
		sb.append(" LEFT JOIN TBL_STAFF sa ON SH.CREATESTAFF = SA.STAFFID LEFT JOIN TBL_ORGANIZATION org ON sh.AUDITORG = ORG.ORGID ) aa ON QU.SHEETID = AA.SHEETID  ");
		sb.append(" LEFT JOIN ( SELECT RE.QUESTIONID,SA.REALNAME,RE.STATUS,RE.LASTREFORMSTATUS,RE.SOLUTIONID,re.REFORMID,re.PERONINCHARGE,re.zgjgresult,re.WXHSTATUS FROM TBL_NBSJ_REFOPM re LEFT JOIN TBL_STAFF sa ON re.PERONINCHARGE = SA.STAFFID ) bb ON BB.QUESTIONID = QU.QUESTIONID  ");
		sb.append("  WHERE 1 = 1 AND QU.RECSTATUS = 1     AND ( (QU.RECSTATUS = 1 AND  bb.STATUS =3 and bb.LASTREFORMSTATUS =1   AND  bb.zgjgresult  is not null and bb.zgjgresult!='已完成整改' ) or ( QU.RECSTATUS = 1 AND bb.WXHSTATUS=1 and bb.LASTREFORMSTATUS =1 ) ) ");
		
		if (re.getCode()!=null ) {
			sb.append(" and SHEETCODE like'%"+re.getCode()+"%' ");
		 }
		if (re.getCode()!=null ) {
			sb.append(" and PRJOECTNAME like'%"+re.getProjectname()+"%' ");
		 }
		if(re.getCompany()!=null && re.getCompany().length()>0) {
			sb.append(" and AUDITORGID = "+re.getCompany());
		}
		
		sb.append(" ) ORDER BY problemid ) T1 WHERE ROWNUM <= "+(pageInfo.getCurrentRecord()+pageInfo.getPageSize())+" ) T2 WHERE T2.RN > "+pageInfo.getCurrentRecord());
		return sb.toString();
	}
	
	
	
public String   selectNbsjReformBySolutionPageInfowxhcount(PageInfo<TblNbsjRefopm> pageInfo,TblNbsjRefopm re) throws Exception {
		
		StringBuffer sb = new StringBuffer("select count(*) from ( SELECT DISTINCT BUG.BUGID||'__b' problemid,BUG.BUGNUMBER code,o.orgname company,'缺陷' source,BUG.DISCOVERPERSON discoverer,sa.REALNAME zgzxxrname,bug.BUGDESCRIPTE details,RE.STATUS STATUS,re.REFORMID REFORMID,re.zgjgresult, re.WXHSTATUS,PRO.PRJOECTNAME,PRO.AUDITORGID   " );
		sb.append(" FROM TBL_NBSJ_BUG bug LEFT JOIN TBL_NBSJ_PROJECT pro on BUG.PROJECTID=PRO.PROJECTID LEFT JOIN TBL_NBSJ_REFOPM re ON BUG.BUGID = RE.BUGID LEFT JOIN TBL_ORGANIZATION o ON o.orgid = BUG.BUGDEPARTMENT  ");
		sb.append(" LEFT JOIN TBL_STAFF sa ON re.PERONINCHARGE = SA.STAFFID  ");
		sb.append(" WHERE BUG.NEEDREFORM = '是' AND BUG.INBUGIDB IS NULL  AND ( (re.STATUS =3 and re.LASTREFORMSTATUS =1   AND  re.reformresult  is not null and re.zgjgresult!='已完成整改'  ) or (  re.WXHSTATUS=1 and re.LASTREFORMSTATUS =1 ) )   ");
		
		if (re.getCode()!=null ) {
			sb.append(" and BUG.BUGNUMBER like'%"+re.getCode()+"%' ");
		 }
		
		if (re.getCode()!=null ) {
			sb.append(" and pro.PRJOECTNAME like'%"+re.getProjectname()+"%' ");
		 }
		if(re.getCompany()!=null && re.getCompany().length()>0) {
			sb.append(" and pro.AUDITORGID = "+re.getCompany());
		}
		
		
		sb.append(" UNION ALL ");
		sb.append(" SELECT DISTINCT QU.QUESTIONID||'__p' problemid, AA.SHEETCODE code,aa.orgname company,'审计发现' source,AA.REALNAME discoverer,bb.REALNAME zgzxxrname,dbms_lob.substr(aa.AUDITDISCOVERABLE,400,1) details,bb.STATUS  STATUS,bb.REFORMID,bb.zgjgresult,bb.WXHSTATUS,aa.PRJOECTNAME,aa.AUDITORGID ");
		sb.append(" FROM TBL_NBSJ_QUESTION qu LEFT JOIN ( SELECT SH.SHEETID,SA.REALNAME,SH.PROJECTID,SH.SHEETCODE,SH.CREATESTAFF,ORG.ORGNAME,SH.AUDITDISCOVERABLE,PRO.PRJOECTNAME,PRO.AUDITORGID FROM TBL_NBSJ_SHEET sh  LEFT JOIN TBL_NBSJ_PROJECT pro on SH.PROJECTID=PRO.PROJECTID   ");
		sb.append(" LEFT JOIN TBL_STAFF sa ON SH.CREATESTAFF = SA.STAFFID LEFT JOIN TBL_ORGANIZATION org ON sh.AUDITORG = ORG.ORGID ) aa ON QU.SHEETID = AA.SHEETID  ");
		sb.append(" LEFT JOIN ( SELECT RE.QUESTIONID,SA.REALNAME,RE.STATUS,RE.LASTREFORMSTATUS,RE.SOLUTIONID,re.REFORMID,re.PERONINCHARGE,re.zgjgresult,re.WXHSTATUS FROM TBL_NBSJ_REFOPM re LEFT JOIN TBL_STAFF sa ON re.PERONINCHARGE = SA.STAFFID ) bb ON BB.QUESTIONID = QU.QUESTIONID  ");
		sb.append("  WHERE 1 = 1 AND QU.RECSTATUS = 1     AND ( (QU.RECSTATUS = 1 AND  bb.STATUS =3 and bb.LASTREFORMSTATUS =1   AND  bb.zgjgresult  is not null and bb.zgjgresult!='已完成整改' ) or ( QU.RECSTATUS = 1 AND bb.WXHSTATUS=1 and bb.LASTREFORMSTATUS =1 ) ) ");
		
		if (re.getCode()!=null ) {
			sb.append(" and SHEETCODE like'%"+re.getCode()+"%' ");
		 }
		if (re.getCode()!=null ) {
			sb.append(" and PRJOECTNAME like'%"+re.getProjectname()+"%' ");
		 }
		if(re.getCompany()!=null && re.getCompany().length()>0) {
			sb.append(" and AUDITORGID = "+re.getCompany());
		}
		sb.append(" ) ORDER BY problemid   ");
		return sb.toString();
	}



public String   selectNbsjReformByAllPageInfo(PageInfo<TblNbsjRefopm> pageInfo, BigDecimal auditorg,  String code,String projectname) throws Exception {
	
	StringBuffer sb = new StringBuffer("SELECT * FROM (SELECT T1.*,ROWNUM RN  FROM (select * from (SELECT RE.REFORMID,PRO.PRJOECTNAME,CASE WHEN SH.ORGIDS=NULL THEN ST.REALNAME   ELSE SH.ORGIDNAMES   END oegname,SH.SHEETCODE,dbms_lob.substr(sh.AUDITDISCOVERABLE,400,1) auditDiscoverable,RE.REFORMRESULT,RE.REFORMMEASURE,STA.REALNAME,RE.STATUS  " );
	sb.append("   from TBL_NBSJ_REFOPM re LEFT JOIN TBL_NBSJ_PROJECT pro on RE.PROJECTID=PRO.PROJECTID  LEFT JOIN TBL_STAFF sta on RE.PERONINCHARGE=STA.STAFFID  ");
	sb.append(" LEFT JOIN TBL_NBSJ_QUESTION qu on re.QUESTIONID=QU.QUESTIONID  LEFT JOIN TBL_NBSJ_SHEET sh on QU.SHEETID=SH.SHEETID LEFT JOIN TBL_STAFF st on SH.AUDITSTAFFID=ST.STAFFID LEFT JOIN TBL_ORGANIZATION org on SH.AUDITORG=ORG.ORGID  ");
	sb.append(" WHERE RE.LASTREFORMSTATUS=1 AND RE.PROJECTID is not NULL ");
	
	
	if (projectname!=null && !projectname.trim().equals("") ) {
		sb.append(" and PRO.PRJOECTNAME like '%"+projectname+"%' ");
	 }
	if (code!=null && !code.trim().equals("")  ) {
		sb.append(" and SHEETCODE like'%"+code+"%' ");
	 }
	
	if (auditorg!=null ) {
		sb.append(" and (sh.AUDITORG="+auditorg+" or sh.AUDITSTAFFID="+auditorg+" )");
	 }
	
	sb.append(" ) order BY REFORMID ) T1 WHERE ROWNUM <= "+(pageInfo.getCurrentRecord()+pageInfo.getPageSize())+" ) T2 WHERE T2.RN > "+pageInfo.getCurrentRecord());
	return sb.toString();
}


public String   selectNbsjReformByAllCount( BigDecimal auditorg, String code,String projectname) throws Exception {
	
	StringBuffer sb = new StringBuffer("select count(*) from (SELECT RE.REFORMID,PRO.PRJOECTNAME,CASE WHEN SH.ORGIDS=NULL THEN ST.REALNAME   ELSE SH.ORGIDNAMES   END oegname,SH.SHEETCODE,dbms_lob.substr(sh.AUDITDISCOVERABLE,400,1) auditDiscoverable,RE.REFORMRESULT,RE.REFORMMEASURE,STA.REALNAME,RE.STATUS  " );
	sb.append("   from TBL_NBSJ_REFOPM re LEFT JOIN TBL_NBSJ_PROJECT pro on RE.PROJECTID=PRO.PROJECTID  LEFT JOIN TBL_STAFF sta on RE.PERONINCHARGE=STA.STAFFID  ");
	sb.append(" LEFT JOIN TBL_NBSJ_QUESTION qu on re.QUESTIONID=QU.QUESTIONID  LEFT JOIN TBL_NBSJ_SHEET sh on QU.SHEETID=SH.SHEETID LEFT JOIN TBL_STAFF st on SH.AUDITSTAFFID=ST.STAFFID LEFT JOIN TBL_ORGANIZATION org on SH.AUDITORG=ORG.ORGID  ");
	sb.append(" WHERE RE.LASTREFORMSTATUS=1 AND RE.PROJECTID is not NULL ");
	
	
	if (projectname!=null && !projectname.trim().equals("") ) {
		sb.append(" and PRO.PRJOECTNAME like '%"+projectname+"%' ");
	 }
	if (auditorg!=null ) {
		sb.append(" and (sh.AUDITORG="+auditorg+" or sh.AUDITSTAFFID="+auditorg+" )");
	 }
	if (code!=null && !code.trim().equals("")  ) {
		sb.append(" and SHEETCODE like'%"+code+"%' ");
	 }
	
	sb.append(" ) order BY REFORMID   ");
	return sb.toString();
}

	
}
