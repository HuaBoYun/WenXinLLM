package com.huabo.audit.oracle.mapper;

import java.math.BigDecimal;

import org.apache.commons.lang.StringUtils;

import com.hbfk.util.DateUtil;
import com.huabo.audit.oracle.entity.TblOrganization;
import com.huabo.audit.oracle.entity.TblStaff;
import com.huabo.audit.util.PageInfo;

public class TblStaffMapperSqlConfig {
	
	public String selectListByPageInfo(PageInfo<TblStaff> pageInfo,Integer orgid) {
		
		StringBuffer sb = new StringBuffer("SELECT * FROM "
				+ "(SELECT T1.*,ROWNUM RN  FROM "
				+ "(SELECT TNA.*,ol.ORGNAME "
				+ "FROM TBL_STAFF TNA "
				+ "LEFT JOIN TBL_ORGANIZATION ol ON ol.ORGID = TNA.ORGID "
				+ " WHERE TNA.ORGID in (select ORGID from TBL_ORGANIZATION org "
				+ " where 1=1 ");
				
		if(null != orgid){
			sb.append(" AND org.FATHERORGID="+orgid);
		}
		sb.append(" AND org.ORGTYPE=0 ) ");
		
		sb.append(" ORDER BY TNA.STAFFID DESC) T1 WHERE ROWNUM <= "+(pageInfo.getCurrentRecord()+pageInfo.getPageSize())+" ) T2 WHERE T2.RN > "+pageInfo.getCurrentRecord());
		return sb.toString();
	}
	
	public String selectCountByPageInfo(PageInfo<TblStaff> pageInfo,Integer orgid) {
		StringBuffer sb = new StringBuffer("SELECT COUNT(0) "
				+ "FROM TBL_STAFF TNA "
				+ "LEFT JOIN TBL_ORGANIZATION ol ON ol.ORGID = TNA.ORGID "
				+ " WHERE TNA.ORGID in (select ORGID from TBL_ORGANIZATION org "
				+ " where 1=1 ");
				
		if(null != orgid){
			sb.append(" AND org.FATHERORGID="+orgid);
		}
		sb.append(" AND org.ORGTYPE=0 ) ");
		
		
		return sb.toString();
	}
	
	
	public String findAllPageBeanPid(PageInfo<TblStaff> pageInfo, String username,String ralename,TblOrganization attribute){
        StringBuffer sqlSb = new StringBuffer("SELECT * FROM (SELECT BUDGET.*,ROWNUM RN FROM (select STAFFID,REALNAME,STA.ORGID,ORG.ORGNAME,STA.ADDRESS,STA.EMAIL,username from TBL_STAFF STA INNER JOIN TBL_ORGANIZATION ORG ON STA.ORGID=ORG.ORGID WHERE 1=1 ");
        if((username!=null &&  username.length()>0) ||( ralename!=null  && ralename.length()>0)) {
        	if(username!=null && username.length()>0) {
        		sqlSb.append(" AND STA.username like '%"+username+"%'");
        	}
        	if(ralename!=null && ralename.length()>0) {
        		sqlSb.append(" AND STA.REALNAME like '%"+ralename+"%'");
        	}
        }else {
        	if(attribute.getOrgtype()!=null && attribute.getOrgtype().toString().equals("0")){
                sqlSb.append("AND STA.ORGID ="+attribute.getOrgid()+" AND (STA.STATUS is NULL or STA.STATUS != 0)");
            }else {
                sqlSb.append("AND STA.ORGID in (select ORGID from TBL_ORGANIZATION org where FATHERORGID="+attribute.getOrgid()+" AND ORGTYPE=0 ) AND (STA.STATUS is NULL or STA.STATUS != 0)");
            }
        	
        }
        

        sqlSb.append(" order by STAFFID desc) BUDGET WHERE rownum <= "+(pageInfo.getCurrentPage()*pageInfo.getPageSize())+" ) WHERE RN > "+pageInfo.getCurrentRecord());
        String sql = sqlSb.toString();
        return sql.toString();
    }
    public String findAllCountPageBeanPid(PageInfo<TblStaff> pageInfo, String username,String ralename,TblOrganization attribute){
        StringBuffer sqlSb = new StringBuffer("select COUNT(*) from TBL_STAFF STA INNER JOIN TBL_ORGANIZATION ORG ON STA.ORGID=ORG.ORGID WHERE 1=1 ");
        if((username!=null &&  username.length()>0) ||( ralename!=null  && ralename.length()>0)) {
        	if(username!=null && username.length()>0) {
        		sqlSb.append(" AND STA.username like '%"+username+"%'");
        	}
        	if(ralename!=null && ralename.length()>0) {
        		sqlSb.append(" AND STA.REALNAME like '%"+ralename+"%'");
        	}
        }else {
        	if(attribute.getOrgtype()!=null && attribute.getOrgtype().toString().equals("0")){
                sqlSb.append("AND STA.ORGID ="+attribute.getOrgid()+" AND (STA.STATUS is NULL or STA.STATUS != 0)");
            }else {
                sqlSb.append("AND STA.ORGID in (select ORGID from TBL_ORGANIZATION org where FATHERORGID="+attribute.getOrgid()+" AND ORGTYPE=0 ) AND (STA.STATUS is NULL or STA.STATUS != 0)");
            }
        	
        }
        
        return sqlSb.toString();
    }
    
    
    public String selectByOrgidListPageInfo(PageInfo<TblStaff> pageInfo,BigDecimal orgid,String username,TblStaff staff) {
		
		StringBuffer sb = new StringBuffer("SELECT * FROM  (SELECT T1.*,ROWNUM RN  FROM ");
		sb.append("(select DISTINCT * from TBL_STAFF WHERE (STAFFID in (SELECT DISTINCT TE.STAFFID from TBL_NBSJ_TEAMSTAFF te LEFT JOIN TBL_NBSJ_PRO_TEAM pt ON TE.TEAMID=PT.TEAMID WHERE PROJECTID in (SELECT PROJECTID  from TBL_NBSJ_PROJECT WHERE STATUS=4 AND ORGID="+orgid+" )) ");
		sb.append(" OR STAFFID IN (SELECT PMID from TBL_NBSJ_PROJECT WHERE STATUS=4 AND ORGID="+orgid+"))  ");
		if(username!=null && !username.equals("")) {
			sb.append("  and USERNAME like '%"+username+"%' ");
		}
		if(staff.getRealname()!=null && !staff.getRealname().equals("")) {
			sb.append(" and REALNAME like '%"+staff.getRealname()+"%' ");
		}
		if(StringUtils.isNotBlank(staff.getMajor())) {
			sb.append(" AND Major like '%"+staff.getMajor()+"%'");
		}
		
		if(StringUtils.isNotBlank(staff.getEducation())) {
			sb.append(" AND Education like '%"+staff.getEducation()+"%'");
		}
		
		if(StringUtils.isNotBlank(staff.getJobexperiences())) {
			sb.append(" AND Jobexperiences like '%"+staff.getJobexperiences()+"%'");
		}
		sb.append(" ORDER BY STAFFID DESC) T1 WHERE ROWNUM <= "+(pageInfo.getCurrentRecord()+pageInfo.getPageSize())+" ) T2 WHERE T2.RN > "+pageInfo.getCurrentRecord());
		return sb.toString();
	}
	
    public String selectByOrgidCountPageInfo(BigDecimal orgid,String username,TblStaff staff) {
		
		StringBuffer sb = new StringBuffer("SELECT count(*) FROM (  ");
		sb.append("select DISTINCT * from TBL_STAFF WHERE (STAFFID in (SELECT DISTINCT TE.STAFFID from TBL_NBSJ_TEAMSTAFF te LEFT JOIN TBL_NBSJ_PRO_TEAM pt ON TE.TEAMID=PT.TEAMID WHERE PROJECTID in (SELECT PROJECTID  from TBL_NBSJ_PROJECT WHERE STATUS=4 AND ORGID="+orgid+" )) ");
		sb.append(" OR STAFFID IN (SELECT PMID from TBL_NBSJ_PROJECT WHERE STATUS=4 AND ORGID="+orgid+"))  ");
		if(username!=null && !username.equals("")) {
			sb.append(" and USERNAME like '%"+username+"%' ");
		}
		if(staff.getRealname()!=null && !staff.getRealname().equals("")) {
			sb.append(" and REALNAME like '%"+staff.getRealname()+"%' ");
		}
		if(StringUtils.isNotBlank(staff.getMajor())) {
			sb.append(" AND Major like '%"+staff.getMajor()+"%'");
		}
		
		if(StringUtils.isNotBlank(staff.getEducation())) {
			sb.append(" AND Education like '%"+staff.getEducation()+"%'");
		}
		
		if(StringUtils.isNotBlank(staff.getJobexperiences())) {
			sb.append(" AND Jobexperiences like '%"+staff.getJobexperiences()+"%'");
		}
		sb.append(" ORDER BY STAFFID DESC) T1   ");
		return sb.toString();
	}
    
    
    
    public String updateStaff(TblStaff user) {
        StringBuffer sql = new StringBuffer("UPDATE TBL_STAFF SET REALNAME = '"+user.getRealname()+"'");

        if(user.getFixedphone() != null && !"".equals(user.getFixedphone())) {
            sql.append(" , FIXEDPHONE = '"+user.getFixedphone()+"'");
        }
        if(user.getAddress() != null && !"".equals(user.getAddress())) {
            sql.append(" , ADDRESS = '"+user.getAddress()+"'");
        }
        if(user.getEmail() != null && !"".equals(user.getEmail())) {
            sql.append(" , EMAIL = '"+user.getEmail()+"'");
        }
        if(user.getMiblephone() != null && !"".equals(user.getMiblephone())) {
            sql.append(" , MIBLEPHONE = '"+user.getMiblephone()+"'");
        }
        if(user.getMemo() != null && !"".equals(user.getMemo())) {
            sql.append(" , MEMO = '"+user.getMemo()+"'");
        }
        if(user.getUsername() != null && !"".equals(user.getUsername())) {
            sql.append(" , USERNAME = '"+user.getUsername()+"'");
        }
        if(user.getPassword() != null && !"".equals(user.getPassword())) {
            sql.append(" , PASSWORD = '"+user.getPassword()+"'");
        }
        if(user.getJobid() != null && !"".equals(user.getJobid())) {
            sql.append(" , JOBID = '"+user.getJobid()+"'");
        }
        if(user.getStatus() != null && !"".equals(user.getStatus())) {
            sql.append(" , STATUS = '"+user.getStatus()+"'");
        }
        if(user.getOrgid() != null && !"".equals(user.getOrgid())) {
            sql.append(" , ORGID = '"+user.getOrgid()+"'");
        }
        if(user.getOutSideId() != null && !"".equals(user.getOrgid())) {
            sql.append(" , OUTSIDEID = '"+user.getOutSideId() +"'");
        }
        if(user.getOutSideOpenId() != null && !"".equals(user.getOutSideOpenId())) {
            sql.append(" , OUTSIDEOPENID = '"+user.getOutSideOpenId() +"'");
        }
        if(user.getIsAudit() != null && !"".equals(user.getIsAudit())) {
            sql.append(" , IsAudit = '"+user.getIsAudit()+"'");
        }
       
        if(user.getPoliticaloutlook() != null && !"".equals(user.getPoliticaloutlook())) {
            sql.append(" , Politicaloutlook = '"+user.getPoliticaloutlook()+"'");
        }
        if(user.getEducation() != null && !"".equals(user.getEducation())) {
            sql.append(" , Education = '"+user.getEducation()+"'");
        }
        if(user.getMajor() != null && !"".equals(user.getMajor())) {
            sql.append(" , Major = '"+user.getMajor()+"'");
        }
        if(user.getSchool() != null && !"".equals(user.getSchool())) {
            sql.append(" , School = '"+user.getSchool()+"'");
        }
        if(user.getOfficephone() != null && !"".equals(user.getOfficephone())) {
            sql.append(" , Officephone= '"+user.getOfficephone()+"'");
        }
        
        if(user.getTitle() != null && !"".equals(user.getTitle())) {
            sql.append(" , Title= '"+user.getTitle()+"'");
        }
        if(user.getQualification() != null && !"".equals(user.getQualification())) {
            sql.append(" , Qualification= '"+user.getQualification()+"'");
        }
        
        if(user.getSituation() != null && !"".equals(user.getSituation())) {
            sql.append(" , Situation= '"+user.getSituation()+"'");
        }
        if(user.getJobexperiences() != null && !"".equals(user.getJobexperiences())) {
            sql.append(" , Jobexperiences= '"+user.getJobexperiences()+"'");
        }
        
        if(user.getWorktime() != null ) {
            sql.append(" , worktime= TO_DATE('"+ DateUtil.parseDate(user.getWorktime(),"yyyy-MM-dd HH:mm:ss") +"', 'YYYY-MM-DD HH24:MI:SS')");
        }
        if(user.getBirthday() != null ) {
            sql.append(" , Birthday= TO_DATE('"+ DateUtil.parseDate(user.getBirthday(),"yyyy-MM-dd HH:mm:ss") +"', 'YYYY-MM-DD HH24:MI:SS')");
        }
        if(user.getEntrytime() != null ) {
            sql.append(" , entrytime= TO_DATE('"+ DateUtil.parseDate(user.getEntrytime(),"yyyy-MM-dd HH:mm:ss") +"', 'YYYY-MM-DD HH24:MI:SS')");
        }
        
        if(user.getResignationtime() != null ) {
            sql.append(" , resignationtime= TO_DATE('"+ DateUtil.parseDate(user.getResignationtime(),"yyyy-MM-dd HH:mm:ss") +"', 'YYYY-MM-DD HH24:MI:SS')");
        }
        if(user.getAuditortype() != null && !"".equals(user.getAuditortype())) {
            sql.append(" , auditortype= '"+user.getAuditortype()+"'");
        }
        
        if(user.getGender() != null && !"".equals(user.getGender())) {
            sql.append(" , gender= '"+user.getGender()+"'");
        }
        if(user.getType() != null && !"".equals(user.getType())) {
            sql.append(" , TYPE= '"+user.getType()+"'");
        }
        
        if(user.getHistorycode() != null && !"".equals(user.getHistorycode())) {
            sql.append(" , Historycode= '"+user.getHistorycode()+"'");
        }
        
        if(user.getHistorydepartmentid() != null && !"".equals(user.getHistorydepartmentid())) {
            sql.append(" , Historydepartmentid= '"+user.getHistorydepartmentid()+"'");
        }
        
        if(user.getDataSource() != null && !"".equals(user.getDataSource())) {
            sql.append(" , datasource= '"+user.getDataSource()+"'");
        }
        
        if(user.getAprStatus() != null && !"".equals(user.getAprStatus())) {
            sql.append(" , APRSTATUS= '"+user.getAprStatus()+"'");
        }
        
        sql.append(" WHERE STAFFID = '"+user.getStaffid()+"'");
        return sql.toString();
    }
    
    
    public String findByStaffManOrgs(String orgid){
    	String sql="SELECT * FROM TBL_STAFF WHERE MANAGEORGS like '%"+orgid+"%'";
    	return sql;
    }
    public String findByStaffFgOrgs(String orgid){
    	String sql="SELECT * FROM TBL_STAFF WHERE FGORGS like '%"+orgid+"%'";
    	System.out.println(sql);
    	return sql;
    }
    
    public String findByJobName(String jobname,String orgid){
		String sql="SELECT * FROM TBL_STAFF WHERE ORGID="+orgid+" AND JOBID in (SELECT JOBID from TBL_JOB WHERE COMPANYID in (select orgid from TBL_ORGANIZATION o where o.ORGTYPE != 0  START WITH o.ORGID = "+orgid+" CONNECT by PRIOR o.FATHERORGID = o.ORGID) AND JOBNAME like'%"+jobname+"%')";
    	return sql.toString();
    }
}
