package com.huabo.compliance.mapper;


import com.hbfk.util.DateUtil;
import com.hbfk.util.PageInfo;
import com.huabo.compliance.entity.Find;
import com.huabo.compliance.entity.TblOrganization;
import com.huabo.compliance.entity.TblStaff;
import com.huabo.compliance.service.UserService;
import org.apache.commons.lang.StringUtils;

import java.math.BigDecimal;

public class TblStaffMapperSqlConifg {

	public String selectAllYmStaffList(Integer orgId) {
		String sql = "SELECT STAFF.STAFFID,STAFF.USERNAME,STAFF.EMAIL,STAFF.MIBLEPHONE,STAFF.PKYMSTAFFID,STAFF.REALNAME,ORG.PKYMORGID,STAFF.ROLEIDSTRS ,STAFF.PASSWORD,JOB.PKYMJOBID " + 
				" FROM TBL_STAFF STAFF LEFT JOIN TBL_ORGANIZATION ORG ON STAFF.ORGID = ORG.ORGID LEFT JOIN TBL_JOB JOB ON JOB.JOBID = STAFF.JOBID WHERE STAFF.ORGID = "+orgId+" AND STAFF.USERNAME IS NOT NULL";
		return sql;
	}
	
	public String selectAllYmStaffInfo(BigDecimal staffId) {
		String sql = "SELECT STAFF.STAFFID,STAFF.USERNAME,STAFF.EMAIL,STAFF.MIBLEPHONE,STAFF.PKYMSTAFFID,STAFF.REALNAME,ORG.PKYMORGID,STAFF.ROLEIDSTRS ,STAFF.PASSWORD,JOB.PKYMJOBID ,STAFF.ORGID  " + 
				" FROM TBL_STAFF STAFF LEFT JOIN TBL_ORGANIZATION ORG ON STAFF.ORGID = ORG.ORGID LEFT JOIN TBL_JOB JOB ON JOB.JOBID = STAFF.JOBID WHERE STAFF.STAFFID = "+staffId+" AND STAFF.USERNAME IS NOT NULL";
		return sql;
	}
	
    public String selectListByPageInfoo(PageInfo<TblStaff> pageInfo, Find find, BigDecimal orgid) {
        StringBuffer sbSql = new StringBuffer("SELECT * FROM (SELECT BUDGET.*,ROWNUM RNUM FROM(select * from tbl_staff where orgid= "+orgid+" order by STATUS desc,STAFFID desc");
        if (find != null && StringUtils.isNotBlank(find.getName())) {
            sbSql.append("  and REALNAME like '%" + find.getName() + "%'");
        }
        if (find != null && StringUtils.isNotBlank(find.getUserName())) {
            sbSql.append("  and USERNAME like '%" + find.getUserName() + "%'");
        }
        sbSql.append(") BUDGET WHERE rownum <= "+(pageInfo.getCurrentPage()*pageInfo.getPageSize())+" ) WHERE RNUM > "+pageInfo.getCurrentRecord());
        String sql = sbSql.toString();
        return sql;
    }

    public String selectListByPage(PageInfo<TblStaff> pageInfo, String pid) {
        StringBuffer sbSql = new StringBuffer("SELECT * FROM " +
                "(SELECT BUDGET.*,ROWNUM RNUM FROM " +
                "(select toa.ORGNAME,ts.* from " +
                "(select * from TBL_STAFF  where orgid in ("+pid+") AND (STATUS is NULL or STATUS != 0))ts " +
                "left join TBL_ORGANIZATION toa ON ts.ORGID = toa.ORGID");
        sbSql.append(") BUDGET WHERE rownum <= "+(pageInfo.getCurrentPage()*pageInfo.getPageSize())+" ) WHERE RNUM > "+pageInfo.getCurrentRecord());
        String sql = sbSql.toString();
        return sql;
    }

    public String selectListByPageCount(PageInfo<TblStaff> pageInfo, String pid) {
        StringBuffer sbSql = new StringBuffer("SELECT count(*) FROM \n" +
                "(SELECT BUDGET.*,ROWNUM RNUM FROM \n" +
                "(select toa.ORGNAME,ts.* from \n" +
                "(select * from TBL_STAFF  where orgid in ("+pid+") AND (STATUS is NULL or STATUS != 0))ts\n" +
                "left join TBL_ORGANIZATION toa ON ts.ORGID = toa.ORGID");
        sbSql.append(") BUDGET WHERE rownum <= "+(pageInfo.getCurrentPage()*pageInfo.getPageSize())+" ) WHERE RNUM > "+pageInfo.getCurrentRecord());
        String sql = sbSql.toString();
        return sql;
    }

    public String selectListBy(PageInfo<TblStaff> pageInfo, BigDecimal orgid) {
        StringBuffer sbSql = new StringBuffer("SELECT * FROM \n" +
                "(SELECT BUDGET.*,ROWNUM RNUM FROM \n" +
                "(select STAFFID,REALNAME,STA.ORGID,ORG.ORGNAME from tbl_staff sta INNER JOIN TBL_ORGANIZATION org ON STA.ORGID=ORG.ORGID  where STA.ORGID in \n" +
                "(select ORGID from TBL_ORGANIZATION org where FATHERORGID=1 AND ORGTYPE=0 \n" +
                ") AND \n" +
                "(STA.STATUS is NULL or STA.STATUS != 0\n" +
                ") order by STAFFID desc");
        sbSql.append(") BUDGET WHERE rownum <= "+(pageInfo.getCurrentPage()*pageInfo.getPageSize())+" ) WHERE RNUM > "+pageInfo.getCurrentRecord());
        String sql = sbSql.toString();
        return sql;
    }

    public String selectListByOrgid(PageInfo<TblStaff> pageInfo, BigDecimal orgid) {
        StringBuffer sbSql = new StringBuffer("SELECT * FROM \n" +
                "(SELECT BUDGET.*,ROWNUM RNUM FROM \n" +
                "(select COUNT(*) from tbl_staff sta \n" +
                "INNER JOIN TBL_ORGANIZATION org ON STA.ORGID=ORG.ORGID  where STA.ORGID in \n" +
                "(select ORGID from TBL_ORGANIZATION org where FATHERORGID=1 AND ORGTYPE=0 \n" +
                ") AND \n" +
                "(STA.STATUS is NULL or STA.STATUS != 0\n" +
                ") order by STAFFID desc");
        sbSql.append(") BUDGET WHERE rownum <= "+(pageInfo.getCurrentPage()*pageInfo.getPageSize())+" ) WHERE RNUM > "+pageInfo.getCurrentRecord());
        String sql = sbSql.toString();
        return sql;
    }

//    public String selectCountFind(PageInfo<TblStaff> pageInfo, Find find) {
//        TblStaff tblStaff = new TblStaff();
//        StringBuffer sbSql = new StringBuffer("SELECT count(*) FROM (SELECT BUDGET.*,ROWNUM RNUM FROM(SELECT * FROM TBL_STAFF where ORGID = "+ tblStaff.getOrgid() + " and REALNAME like '% "+ find.getName() +" %' and USERNAME like '% "+ find.getUserName() + "%'");
//        sbSql.append(" )BUDGET WHERE ROWNUM <= "+(pageInfo.getCurrentPage()*pageInfo.getPageSize())+" ) WHERE RNUM > "+pageInfo.getCurrentRecord());
//        return sbSql.toString();
//    }


    public String selectListByPageInfoOrgid(PageInfo<TblStaff> pageInfo, BigDecimal pid,Find find) {
          StringBuffer sbSql = new StringBuffer("SELECT * FROM (SELECT BUDGET.*,ROWNUM RNUM FROM (select * from TBL_STAFF TS LEFT JOIN TBL_ORGANIZATION TOR ON TS.ORGID = TOR.ORGID WHERE TS.ORGID ="+pid);

          if (find != null && StringUtils.isNotBlank(find.getName())) {
            sbSql.append(" and TS.REALNAME like '%" + find.getName() + "%'");
        }
        if (find != null && StringUtils.isNotBlank(find.getUserName())) {
            sbSql.append("  and TS.USERNAME like '%" + find.getUserName() + "%'");
        }
        if (find != null && StringUtils.isNotBlank(find.getStaffid())){
            sbSql.append("  and TS.STAFFID = "+find.getStaffid()+"");
        }
        sbSql.append(" order by TS.STATUS desc,TS.STAFFID desc) BUDGET WHERE rownum <= "+(pageInfo.getCurrentPage()*pageInfo.getPageSize())+" ) WHERE RNUM > "+pageInfo.getCurrentRecord());
        String sql = sbSql.toString();
        return sql;
    }



    public String selectListByPageInfoFind(PageInfo<TblStaff> pageInfo,Find find, BigDecimal orgid) {
        StringBuffer sbSql = new StringBuffer("select * from (SELECT BUDGET.*,ROWNUM RNUM FROM ( select * from TBL_STAFF TS LEFT JOIN TBL_ORGANIZATION TOR ON TS.ORGID = TOR.ORGID WHERE TOR.FATHERORGID =  '"+orgid+"' AND TOR.ORGTYPE = 0 ");
        if (find != null && StringUtils.isNotBlank(find.getName())) {
            sbSql.append(" and TS.REALNAME like '%" + find.getName() + "%'");
        }
        if (find != null && StringUtils.isNotBlank(find.getUserName())) {
            sbSql.append(" and TS.USERNAME like '%" + find.getUserName() + "%'");
        }
        if (find != null && StringUtils.isNotBlank(find.getStaffid())){
            sbSql.append(" and TS.STAFFID = "+find.getStaffid()+"");
        }
        sbSql.append(" order by TS.STATUS desc,TS.STAFFID desc ) BUDGET WHERE ROWNUM <= "+(pageInfo.getCurrentPage()*pageInfo.getPageSize())+" ) WHERE RNUM > "+pageInfo.getCurrentRecord());
        String sql = sbSql.toString();
        return sql;
    }
//    public String selectListByPageInfoCount(PageInfo<TblStaff> pageInfo,Find find, BigDecimal orgid) {
//        String sqlCount=("SELECT count(*) FROM TBL_STAFF where orgid="+orgid);
//        if (find != null && StringUtils.isNotBlank(find.getName())) {
//            sqlCount+="and REALNAME like '%" + find.getName() + "%'";
//        }
//        if (find != null && StringUtils.isNotBlank(find.getUserName())) {
//            sqlCount+="  and USERNAME like '%" + find.getUserName() + "%'";
//        }
//        sqlCount+="order by STATUS desc,STAFFID desc";
//        return sqlCount;
//    }

//    public String selectListByPageInfoCountOrgid(PageInfo<TblStaff> pageInfo,Find find, BigDecimal orgid) {
//        String sqlCount=("select count(*) from TBL_STAFF WHERE ORGID = "+orgid );
//        if (find != null && StringUtils.isNotBlank(find.getName())) {
//            sqlCount+="and REALNAME like '%" + find.getName() + "%'";
//        }
//        if (find != null && StringUtils.isNotBlank(find.getUserName())) {
//            sqlCount+="and USERNAME like '%" + find.getUserName() + "%'";
//        }
//        sqlCount+="order by STATUS desc,STAFFID desc";
//        return sqlCount;
//    }




    public String selectListByPageInfo(PageInfo<UserService> pageInfo){
        StringBuffer sbSql = new StringBuffer("select * from TBL_STAFF ");
        sbSql.append("where REALNAME like '%REALNAME%' and orgid= #{pid}(");
        sbSql.append(") T1 WHERE ROWNUM <= "+(pageInfo.getCurrentPage()*pageInfo.getPageSize())+" ) T2 WHERE T2.RNUM > "+pageInfo.getCurrentRecord());
        return sbSql.toString();
    }



    public String selectListByPageIn(PageInfo<TblStaff> pageInfo,BigDecimal pid,Find find){
        StringBuffer sbSql = new StringBuffer("select * from (SELECT BUDGET.*,ROWNUM RNUM FROM (select * from tbl_staff where ORGID in (select ORGID from TBL_ORGANIZATION org where FATHERORGID=" + pid + " AND ORGTYPE=0 )");
        if (find != null && StringUtils.isNotBlank(find.getName())) {
            sbSql.append("  and REALNAME like '%" + find.getName() + "%'");
        }
        if (find != null && StringUtils.isNotBlank(find.getUserName())) {
            sbSql.append("  and USERNAME like '%" + find.getUserName() + "%'");
        }
        sbSql.append("order by STATUS desc,STAFFID desc) BUDGET WHERE ROWNUM <= "+(pageInfo.getCurrentPage()*pageInfo.getPageSize())+" ) WHERE RNUM > "+pageInfo.getCurrentRecord());
        String sql = sbSql.toString();
        return sql;
    }


    public String insertUser(TblStaff user) {
        StringBuffer column = new StringBuffer("INSERT INTO TBL_STAFF (STAFFID");
        StringBuffer value = new StringBuffer(" VALUES (HIBERNATE_SEQUENCE.nextval");


        if(user.getStaffid() != null) {
            column.append("STAFFID");
            value.append(",'"+user.getStaffid()+"'");
        }
        if(user.getRealname() != null) {
            column.append(",REALNAME");
            value.append(",'"+user.getRealname()+"'");
        }
        if(user.getFixedphone() != null) {
            column.append(",FIXEDPHONE");
            value.append(",'"+user.getFixedphone()+"'");
        }
        if(user.getAddress() != null) {
            column.append(",ADDRESS");
            value.append(",'"+user.getAddress()+"'");
        }
        if(user.getEmail() != null) {
            column.append(",EMAIL");
            value.append(",'"+user.getEmail()+"'");
        }
        if(user.getMiblephone() != null) {
            column.append(",MIBLEPHONE");
            value.append(",'"+user.getMiblephone()+"'");
        }
        if(user.getMemo() != null) {
            column.append(",MEMO");
            value.append(",'"+user.getMemo()+"'");
        }
        if(user.getUsername() != null) {
            column.append(",USERNAME");
            value.append(",'"+user.getUsername()+"'");
        }
        if(user.getPassword() != null) {
            column.append(",PASSWORD");
            value.append(",'"+user.getPassword()+"'");
        }
        if(user.getJobid() != null) {
            column.append(",JOBID");
            value.append(",'"+user.getJobid()+"'");
        }
        if (user.getRoleid() != null){
            column.append(",ROLEID");
            value.append(",'"+user.getRoleid()+"'");
        }
        if(user.getCreateDate() != null) {
            column.append(",CREATETIME");
            value.append(",TO_DATE('"+ DateUtil.parseDate(user.getCreateDate(),"yyyy-MM-dd HH:mm:ss") +"', 'YYYY-MM-DD HH24:MI:SS')");
        }
        if(user.getStatus() != null) {
            column.append(",STATUS");
            value.append(",'"+user.getStatus()+"'");
        }
        if(user.getOrgid() != null) {
            column.append(",ORGID");
            value.append(",'"+user.getOrgid()+"'");
        }
        if(user.getOutsideid() != null) {
            column.append(",OUTSIDEID");
            value.append(",'"+user.getOutsideid()+"'");
        }
        if(user.getOutsideopendid() != null) {
            column.append(",OUTSIDEOPENDID");
            value.append(",'"+user.getOutsideopendid()+"'");
        }
        if(user.getRoleidstrs() != null && !"".equals(user.getRoleidstrs())) {
            column.append(",ROLEIDSTRS");
            value.append(",'"+user.getRoleidstrs()+"'");
        }

        if(user.getIsaudit() != null && !"".equals(user.getIsaudit())) {
            column.append(",IsAudit");
            value.append(",'"+user.getIsaudit()+"'");
        }
        
        if(user.getPoliticaloutlook() != null && !"".equals(user.getPoliticaloutlook())) {
            column.append(",Politicaloutlook");
            value.append(",'"+user.getPoliticaloutlook()+"'");
        }
        if(user.getEducation() != null && !"".equals(user.getEducation())) {
            column.append(",Education");
            value.append(",'"+user.getEducation()+"'");
        }
        if(user.getMajor() != null && !"".equals(user.getMajor())) {
            column.append(",Major");
            value.append(",'"+user.getMajor()+"'");
        }
          
        if(user.getSchool() != null && !"".equals(user.getSchool())) {
            column.append(",School");
            value.append(",'"+user.getSchool()+"'");
        }
        if(user.getOfficephone() != null && !"".equals(user.getOfficephone())) {
            column.append(",Officephone");
            value.append(",'"+user.getOfficephone()+"'");
        }
         
        if(user.getTitle() != null && !"".equals(user.getTitle())) {
            column.append(",Title");
            value.append(",'"+user.getTitle()+"'");
        }
        if(user.getQualification() != null && !"".equals(user.getQualification())) {
            column.append(",Qualification");
            value.append(",'"+user.getQualification()+"'");
        }
        
        if(user.getSituation() != null && !"".equals(user.getSituation())) {
            column.append(",Situation");
            value.append(",'"+user.getSituation()+"'");
        }
        if(user.getJobexperiences() != null && !"".equals(user.getJobexperiences())) {
            column.append(",Jobexperiences");
            value.append(",'"+user.getJobexperiences()+"'");
        }
        
        if(user.getWorktime() != null) {
            column.append(",worktime");
            value.append(",TO_DATE('"+ DateUtil.parseDate(user.getWorktime(),"yyyy-MM-dd HH:mm:ss") +"', 'YYYY-MM-DD HH24:MI:SS')");
        }
        if(user.getEntrytime() != null) {
            column.append(",entrytime");
            value.append(",TO_DATE('"+ DateUtil.parseDate(user.getEntrytime(),"yyyy-MM-dd HH:mm:ss") +"', 'YYYY-MM-DD HH24:MI:SS')");
        }
        if(user.getResignationtime() != null) {
            column.append(",resignationtime");
            value.append(",TO_DATE('"+ DateUtil.parseDate(user.getResignationtime(),"yyyy-MM-dd HH:mm:ss") +"', 'YYYY-MM-DD HH24:MI:SS')");
        }
        if(user.getAuditortype() != null && !"".equals(user.getAuditortype())) {
            column.append(",auditortype");
            value.append(",'"+user.getAuditortype()+"'");
        }
        
        if(user.getGender()!= null && !"".equals(user.getGender())) {
            column.append(",gender");
            value.append(",'"+user.getGender()+"'");
        }
        if(user.getType()!= null && !"".equals(user.getType())) {
            column.append(",TYPE");
            value.append(",'"+user.getType()+"'");
        }
        if(user.getHistorycode()!= null && !"".equals(user.getHistorycode())) {
            column.append(",historycode");
            value.append(",'"+user.getHistorycode()+"'");
        }
        if(user.getHistorydepartmentid()!= null && !"".equals(user.getHistorydepartmentid())) {
            column.append(",historydepartmentid");
            value.append(",'"+user.getHistorydepartmentid()+"'");
        }

        if(user.getManageorgs() != null && !"".equals(user.getManageorgs())) {
        	column.append(",MANAGEORGS");
            value.append(",'"+user.getManageorgs()+"'");
        }
        
        if(user.getManageorgnames() != null && !"".equals(user.getManageorgnames())) {
        	column.append(",MANAGEORGNAMES");
            value.append(",'"+user.getManageorgnames()+"'");
        }
        
        if(user.getFgorgs() != null && !"".equals(user.getFgorgs())) {
        	column.append(",FGORGS");
            value.append(",'"+user.getFgorgs()+"'");
        }
        
        if(user.getFgorgnames() != null && !"".equals(user.getFgorgnames())) {
        	column.append(",FGORGNAMES");
            value.append(",'"+user.getFgorgnames()+"'");
        }
        
        
        column.append(")");
        value.append(")");
        String sql = column.toString()+value.toString();
        return sql;
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
        if(user.getCreateDate() != null && !"".equals(user.getCreateDate())) {
            sql.append(" , CREATETIME = '"+user.getCreateDate()+"'");
        }
        if(user.getStatus() != null && !"".equals(user.getStatus())) {
            sql.append(" , STATUS = '"+user.getStatus()+"'");
        }
        if(user.getOrgid() != null && !"".equals(user.getOrgid())) {
            sql.append(" , ORGID = '"+user.getOrgid()+"'");
        }
        if(user.getOutsideid() != null && !"".equals(user.getOutsideid())) {
            sql.append(" , OUTSIDEID = '"+user.getOutsideid() +"'");
        }
        if(user.getOutsideopendid() != null && !"".equals(user.getOutsideopendid())) {
            sql.append(" , OUTSIDEOPENDID = '"+user.getOutsideopendid() +"'");
        }
        if(user.getRoleid() != null && !"".equals(user.getRoleid())) {
            sql.append(" , ROLEID = '"+user.getRoleid()+"'");
        }
        if(user.getRoleidstrs() != null && !"".equals(user.getRoleidstrs())) {
            sql.append(" , ROLEIDSTRS = '"+user.getRoleidstrs()+"'");
        }
        if(user.getIsaudit() != null && !"".equals(user.getIsaudit())) {
            sql.append(" , IsAudit = '"+user.getIsaudit()+"'");
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
        
        if(user.getDatasource() != null && !"".equals(user.getDatasource())) {
            sql.append(" , datasource= '"+user.getDatasource()+"'");
        }

        if(user.getManageorgs() != null && !"".equals(user.getManageorgs())) {
            sql.append(" , MANAGEORGS= '"+user.getManageorgs()+"'");
        }
        if(user.getManageorgnames() != null && !"".equals(user.getManageorgnames())) {
            sql.append(" , MANAGEORGNAMES= '"+user.getManageorgnames()+"'");
        }
        if(user.getFgorgs() != null && !"".equals(user.getFgorgs())) {
            sql.append(" , FGORGS= '"+user.getFgorgs()+"'");
        }
        if(user.getFgorgnames() != null && !"".equals(user.getFgorgnames())) {
            sql.append(" , FGORGNAMES= '"+user.getFgorgnames()+"'");
        }
        
        sql.append(" WHERE STAFFID = '"+user.getStaffid()+"'");
        return sql.toString();
    }

    public String findPageListBySql(PageInfo<TblStaff> pageInfo) {
        StringBuffer sbSql = new StringBuffer("SELECT * FROM (SELECT BUDGET.*,ROWNUM RNUM FROM(SELECT s.STAFFID,s.REALNAME,o.ORGNAME FROM TBL_STAFF s LEFT JOIN TBL_ORGANIZATION o ON s.ORGID = o.ORGID WHERE s.STAFFID IN (select STAFFID from TBL_MANAGE_USER_BOOK WHERE BOOKID IN ("+pageInfo.getSqlStr()+")) AND s.ORGID IN (SELECT ORGID FROM TBL_ORGANIZATION where ORGTYPE = 0 start with ORGID = "+pageInfo.getCondition().getOrgid()+" connect by prior ORGID = FATHERORGID)");
        if(pageInfo.getCondition().getRealname() != null){
            sbSql.append(" AND s.REALNAME LIKE '%"+pageInfo.getCondition().getRealname()+"%'");
        }
        sbSql.append(") BUDGET WHERE rownum <= "+(pageInfo.getCurrentPage()*pageInfo.getPageSize())+" ) WHERE RNUM > "+pageInfo.getCurrentRecord());
        String sql = sbSql.toString();
        return sql;
    }
    public String findPageCountBySql(PageInfo<TblStaff> pageInfo) {
        StringBuffer sbSql = new StringBuffer("SELECT COUNT(*) FROM TBL_STAFF s LEFT JOIN TBL_ORGANIZATION o ON s.ORGID = o.ORGID WHERE s.STAFFID IN (select STAFFID from TBL_MANAGE_USER_BOOK WHERE BOOKID IN ("+pageInfo.getSqlStr()+")) AND s.ORGID IN (SELECT ORGID FROM TBL_ORGANIZATION where ORGTYPE = 0 start with ORGID = "+pageInfo.getCondition().getOrgid()+" connect by prior ORGID = FATHERORGID)");
        if(pageInfo.getCondition().getRealname() != null){
            sbSql.append(" AND s.REALNAME LIKE '%"+pageInfo.getCondition().getRealname()+"%'");
        }
        String sql = sbSql.toString();
        return sql;
    }


    public String findAllPageInfoByacctid(PageInfo<TblStaff> pageInfo, String bookid) {
        StringBuffer sbSql = new StringBuffer("SELECT * FROM ( SELECT BUDGET.*,ROWNUM RNUM FROM ( SELECT STA.USERNAME, STA.ADDRESS, STA.REALNAME, ( SELECT ORGNAME FROM TBL_ORGANIZATION org WHERE 1 = 1  \n" +
                "AND ORGTYPE <> 0 AND orgtype < 100 START WITH ORGID = sta.ORGID CONNECT BY PRIOR FATHERORGID = ORGID AND ROWNUM = 1  ) orgname FROM TBL_STAFF sta LEFT JOIN TBL_ORGANIZATION org ON STA.ORGID = ORG.ORGID WHERE \n" +
                " \t sta.STAFFID IN ( select STAFFID FROM TBL_MANAGE_USER_BOOK WHERE BOOKID = '"+bookid +"'");

        sbSql.append(" ) ORDER BY STA.STAFFID) BUDGET WHERE rownum <= "+(pageInfo.getCurrentPage()*pageInfo.getPageSize())+" ) WHERE RNUM > "+pageInfo.getCurrentRecord());
        String sql = sbSql.toString();
        return sql;
    }

    public String findAllPageInfoByacctidcCount( String bookid) {
        StringBuffer sbSql = new StringBuffer("SELECT COUNT(*) FROM TBL_STAFF sta LEFT JOIN TBL_ORGANIZATION org ON STA.ORGID = ORG.ORGID WHERE \n" +
                " \t sta.STAFFID IN ( select STAFFID FROM TBL_MANAGE_USER_BOOK WHERE BOOKID = '"+bookid +"'" );

        sbSql.append(" ) ORDER BY STA.STAFFID");
        String sql = sbSql.toString();
        return sql;
    }


    public String findByAll(String pid, PageInfo<TblStaff> pageInfo) {
        StringBuffer sbSql = new StringBuffer("SELECT * FROM (SELECT BUDGET.*,ROWNUM RNUM FROM( select * from TBL_STAFF TS LEFT JOIN TBL_ORGANIZATION TOR ON TS.ORGID = TOR.ORGID WHERE TS.ORGID = '"+pid+"' AND (TS.STATUS is NULL or TS.STATUS != 0)");
        
        sbSql.append(") BUDGET WHERE rownum <= "+(pageInfo.getCurrentPage()*pageInfo.getPageSize())+" ) WHERE RNUM > "+pageInfo.getCurrentRecord());
        String sql = sbSql.toString();
        return sql;
    }

    public String findByAllORGID(BigDecimal orgid, PageInfo<TblStaff> pageInfo) {
        StringBuffer sbSql = new StringBuffer("SELECT * FROM (SELECT BUDGET.*,ROWNUM RNUM FROM(SELECT * FROM TBL_STAFF TS LEFT JOIN TBL_ORGANIZATION TOR ON TS.ORGID = TOR.ORGID \n" +
                "WHERE TOR.FATHERORGID = '"+orgid+"' AND (TS.STATUS IS NULL OR TS.STATUS != 0)");

        sbSql.append(") BUDGET WHERE rownum <= "+(pageInfo.getCurrentPage()*pageInfo.getPageSize())+" ) WHERE RNUM > "+pageInfo.getCurrentRecord());
        String sql = sbSql.toString();
        return sql;
    }

    public String findAllPageBeanPid(PageInfo<TblStaff> pageInfo, TblOrganization attribute) throws Exception{
        StringBuffer sbSql = new StringBuffer("");
        if (attribute.getOrgtype() != null && attribute.getOrgtype().toString().equals("0")) {
            sbSql = new StringBuffer("SELECT * FROM (SELECT BUDGET.*,ROWNUM RNUM FROM(select STAFFID,REALNAME,STA.ORGID,ORG.ORGNAME,STA.ADDRESS,STA.EMAIL,username from tbl_staff sta INNER JOIN TBL_ORGANIZATION org ON STA.ORGID=ORG.ORGID  where STA.ORGID =" + attribute.getOrgid() + " AND (STA.STATUS is NULL or STA.STATUS != 0)");
        }else{
            sbSql = new StringBuffer("SELECT * FROM (SELECT BUDGET.*,ROWNUM RNUM FROM(select STAFFID,REALNAME,STA.ORGID,ORG.ORGNAME,STA.ADDRESS,STA.EMAIL,username from tbl_staff sta INNER JOIN TBL_ORGANIZATION org ON STA.ORGID=ORG.ORGID  where STA.ORGID in (select ORGID from TBL_ORGANIZATION org where FATHERORGID=" + attribute.getOrgid() + " AND ORGTYPE=0 ) AND (STA.STATUS is NULL or STA.STATUS != 0)");
        }

        sbSql.append(" order by STAFFID desc ) BUDGET WHERE rownum <= "+(pageInfo.getCurrentPage()*pageInfo.getPageSize())+" ) WHERE RNUM > "+pageInfo.getCurrentRecord());
        return sbSql.toString();
    }
    public String findCountPageBeanPid(PageInfo<TblStaff> pageInfo, TblOrganization attribute)throws Exception {
        StringBuffer sbSql = new StringBuffer("");
        if (attribute.getOrgtype() != null && attribute.getOrgtype().toString().equals("0")) {
            sbSql = new StringBuffer("select count(*) FROM(select STAFFID,REALNAME,STA.ORGID,ORG.ORGNAME,STA.ADDRESS,STA.EMAIL,username from tbl_staff sta INNER JOIN TBL_ORGANIZATION org ON STA.ORGID=ORG.ORGID  where STA.ORGID =" + attribute.getOrgid() + " AND (STA.STATUS is NULL or STA.STATUS != 0))");
        }else{
            sbSql = new StringBuffer(" select count(*) FROM (select STAFFID,REALNAME,STA.ORGID,ORG.ORGNAME,STA.ADDRESS,STA.EMAIL,username from tbl_staff sta INNER JOIN TBL_ORGANIZATION org ON STA.ORGID=ORG.ORGID  where STA.ORGID in (select ORGID from TBL_ORGANIZATION org where FATHERORGID=" + attribute.getOrgid() + " AND ORGTYPE=0 ) AND (STA.STATUS is NULL or STA.STATUS != 0))");
        }

        return sbSql.toString();
    }


    public String selectStaffListByPageInfo(PageInfo<TblStaff> pageInfo,  BigDecimal orgId, String realname) throws Exception{
       StringBuffer sb = new StringBuffer("SELECT * FROM (SELECT T1.*,ROWNUM RN  FROM (SELECT * FROM tbl_staff  WHERE isaudit='Y' and status='1'  ");
		if(StringUtils.isNotBlank(realname)) {
			sb.append(" AND realname like '%"+realname+"%'");
		}
		if(orgId != null) {
			sb.append(" AND ORGID IN (SELECT ORGID FROM TBL_ORGANIZATION START WITH ORGID = "+orgId+" CONNECT BY PRIOR ORGID = FATHERORGID AND ORGTYPE = 0)");
		}
		sb.append(" ORDER BY staffid DESC) T1 WHERE ROWNUM <= "+(pageInfo.getCurrentRecord()+pageInfo.getPageSize())+" ) T2 WHERE T2.RN > "+pageInfo.getCurrentRecord());
		return sb.toString();
	}
	
	public String selectStaffCountByPageInfo(BigDecimal orgId, String realname) throws Exception{
		
		StringBuffer sb = new StringBuffer("SELECT count(*) FROM tbl_staff  WHERE ISAUDIT='Y' ");
		if(orgId != null) {
			sb.append(" AND ORGID IN (SELECT ORGID FROM TBL_ORGANIZATION START WITH ORGID = "+orgId+" CONNECT BY PRIOR ORGID = FATHERORGID AND ORGTYPE = 0)");
		}
		if(StringUtils.isNotBlank(realname)) {
			sb.append(" AND realname like '%"+realname+"%'");
		}
		return sb.toString();
	}
	
	public String getAuditorInformationList(String orgid) throws Exception{
      String sql="  select  "+  
      "(select nvl(count(*),0) from tbl_staff where   status='1'  and  orgid ='"+orgid+"') 实有人数, "+
      "(select nvl(count(*),0) from tbl_staff where type='专职' and status='1'  and  orgid ='"+orgid+"') 专职, "+
      "(select nvl(count(*),0) from tbl_staff where qualification is not null and status='1'  and orgid ='"+orgid+"') CIA, "+
      "(select nvl(count(*),0)  from tbl_staff where  education in ('硕士','博士','博士后') and education is not null and status='1'  and  orgid ='"+orgid+"') 硕士及以上, "+
      "(select nvl(count(*),0)  from tbl_staff where  education in ('本科') and education is not null and status='1'  and  orgid ='"+orgid+"') 本科, "+
     " (select nvl(count(*),0)  from tbl_staff where  education in ('大专','高中') and education is not null and  orgid ='"+orgid+"') 大专及以下, "+
      "(select nvl(count(*),0)  from tbl_staff where  title in ('高级') and title is not null and status='1'  and  orgid ='"+orgid+"') 高级职称 , "+
      "(select nvl(count(*),0)  from tbl_staff where  title in ('中级') and title is not null and status='1'  and  orgid ='"+orgid+"') 中级职称 , "+
      "(select nvl(count(*),0)  from tbl_staff where  title in ('初级') and title is not null and status='1'  and  orgid ='"+orgid+"') 初级职称, "+
      "(select nvl(count(*),0)  from tbl_staff where  title in ('无','') and status='1'  and  orgid ='"+orgid+"') 无职称, "+
      "( select  nvl(count(*),0)  from tbl_staff  where   floor(months_between(sysdate,birthday)/12 ) >50 and birthday is not null and status='1'  and  orgid ='"+orgid+"') 五十岁以上, "+
      "( select  nvl(count(*),0)  from tbl_staff  where   floor(months_between(sysdate,birthday)/12 ) >30 and floor(months_between(sysdate,birthday)/12 )<=50 and birthday is not null and status='1'  and  orgid ='"+orgid+"' ) 三十到五十岁, "+
      "( select  nvl(count(*),0)  from tbl_staff  where   floor(months_between(sysdate,birthday)/12 ) <=30 and birthday is not null and status='1'  and  orgid ='"+orgid+"') 三十岁以下, "+
      "( select  nvl(count(*),0)  from tbl_staff  where   major='审计' and  major  is not null and status='1'  and   orgid ='"+orgid+"') 审计, "+
     " ( select  nvl(count(*),0)  from tbl_staff  where   major='会计' and  major is not null and status='1'  and   orgid ='"+orgid+"') 会计, "+
     " ( select  nvl(count(*),0)  from tbl_staff  where   major='经济' and  major is not null and status='1'  and   orgid ='"+orgid+"') 经济, "+
      "( select  nvl(count(*),0)  from tbl_staff  where   major='法律' and  major is not null and status='1' and    orgid ='"+orgid+"') 法律, "+
      "( select  nvl(count(*),0)  from tbl_staff  where   major='管理' and  major is not null and status='1' and    orgid ='"+orgid+"') 管理, "+
      "( select  nvl(count(*),0)  from tbl_staff  where   major='信息技术' and  major is not null and status='1'  and  orgid ='"+orgid+"') 信息技术, "+
     " ( select  nvl(count(*),0)  from tbl_staff  where   major='工程' and  major is not null and status='1'  and  orgid ='"+orgid+"') 工程, "+
      "( select  nvl(count(*),0)  from tbl_staff  where   major='其他' and  major is not null   and status='1'  and  orgid ='"+orgid+"') 其他 "+
      " from   dual";
      return sql;
	}
	  public String insertAttInfoForStaff(BigDecimal id,String attid) {
	        StringBuffer column = new StringBuffer("INSERT INTO TBL_TRAININGSTAFF_ATT(STAFFID");
	        StringBuffer value = new StringBuffer(" VALUES ('"+id+"'");
	        if(attid!="") {
	            column.append(",ATTID");
	            value.append(",'"+attid+"'");
	        }
	        column.append(")");
	        value.append(")");
	        String sql = column.toString()+value.toString();
	        return sql;
	    }
	  public String insertAttInfoForTrain(BigDecimal id,String attid) {
	        StringBuffer column = new StringBuffer("INSERT INTO TBL_TRAINING_ATT(TRAINID");
	        StringBuffer value = new StringBuffer(" VALUES ('"+id+"'");
	        if(attid!="") {
	            column.append(",ATTID");
	            value.append(",'"+attid+"'");
	        }
	        column.append(")");
	        value.append(")");
	        String sql = column.toString()+value.toString();
	        return sql;
	    }
	  
	  
	  public String selectListOrgInId(String sysorgid) {
			String lasts = sysorgid.substring(sysorgid.length() - 1);
			if(",".equals(lasts)) {
				sysorgid = sysorgid.substring(0,sysorgid.length() - 1);
			}
			
			StringBuffer sb = new StringBuffer("SELECT ORGNAME "
					+ "FROM TBL_ORGANIZATION TNA "
					+ "WHERE TNA.ORGID IN ("+sysorgid+") ");
			
			return sb.toString();
		}
	    
	    public String selectListRoleInId(String roleids) {
			String lasts = roleids.substring(roleids.length() - 1);
			if(",".equals(lasts)) {
				roleids = roleids.substring(0,roleids.length() - 1);
			}
			
			StringBuffer sb = new StringBuffer("SELECT RNAME,PKYMROLEID "
					+ "FROM TBL_ROLE TNA "
					+ "WHERE TNA.RID IN ("+roleids+") ");
			
			return sb.toString();
		}
}
