package com.huabo.monitor.mysql.mapper;


import com.hbfk.util.DateUtil;
import com.hbfk.util.PageInfo;
import com.huabo.monitor.entity.Find;
import com.huabo.monitor.mysql.entity.FindMySql;
import com.huabo.monitor.mysql.entity.TblOrganizationMySql;
import com.huabo.monitor.mysql.entity.TblStaffMySql;
import com.huabo.monitor.service.UserService;
import org.apache.commons.lang.StringUtils;

import java.math.BigDecimal;

public class TblStaffMapperSqlMySqlConifg {

	public String selectAllYmStaffList(String orgIds) throws Exception{
		String sql = "SELECT STAFF.STAFFID,STAFF.USERNAME,STAFF.EMAIL,STAFF.MIBLEPHONE,STAFF.PKYMSTAFFID,STAFF.REALNAME,ORG.PKYMORGID,(SELECT GROUP_CONCAT(PKYMROLEID) FROM TBL_ROLE WHERE RID IN (STAFF.ROLEIDSTRS)) ROLEIDS,STAFF.PASSWORD " + 
				"FROM TBL_STAFF STAFF LEFT JOIN TBL_ORGANIZATION ORG ON STAFF.ORGID = ORG.ORGID LEFT JOIN TBL_JOB JOB ON JOB.JOBID = STAFF.JOBID WHERE STAFF.ORGID = "+orgIds+" AND STAFF.USERNAME IS NOT NULL";
		return sql;
	}
	
    public String selectListByPageInfoo(PageInfo<TblStaffMySql> pageInfo, FindMySql find, BigDecimal orgid) {
        StringBuffer sbSql = new StringBuffer("select * from tbl_staff where orgid= " + orgid + " order by STATUS desc,STAFFID desc");
        if (find != null && StringUtils.isNotBlank(find.getName())) {
            sbSql.append("  and REALNAME like '%" + find.getName() + "%'");
        }
        if (find != null && StringUtils.isNotBlank(find.getUserName())) {
            sbSql.append("  and USERNAME like '%" + find.getUserName() + "%'");
        }
        sbSql.append(" LIMIT " + pageInfo.getCurrentRecord() + "," + pageInfo.getPageSize());
        String sql = sbSql.toString();
        return sql;
    }

    public String selectListByPage(PageInfo<TblStaffMySql> pageInfo, String pid) {
        StringBuffer sbSql = new StringBuffer("select toa.ORGNAME,ts.* from \n" +
                "(select * from TBL_STAFF  where orgid in (" + pid + ") AND (STATUS is NULL or STATUS != 0))ts\n" +
                "left join TBL_ORGANIZATION toa ON ts.ORGID = toa.ORGID");
        sbSql.append(" LIMIT " + pageInfo.getCurrentRecord() + "," + pageInfo.getPageSize());
        String sql = sbSql.toString();
        return sql;
    }

    public String selectListByPageCount(PageInfo<TblStaffMySql> pageInfo, String pid) {
        StringBuffer sbSql = new StringBuffer("select toa.ORGNAME,ts.* from \n" +
                "(select * from TBL_STAFF  where orgid in (" + pid + ") AND (STATUS is NULL or STATUS != 0))ts\n" +
                "left join TBL_ORGANIZATION toa ON ts.ORGID = toa.ORGID");
        sbSql.append(" LIMIT " + pageInfo.getCurrentRecord() + "," + pageInfo.getPageSize());
        String sql = sbSql.toString();
        return sql;
    }

    public String selectListBy(PageInfo<TblStaffMySql> pageInfo, BigDecimal orgid) {
        StringBuffer sbSql = new StringBuffer("select STAFFID,REALNAME,STA.ORGID,ORG.ORGNAME from tbl_staff sta INNER JOIN TBL_ORGANIZATION org ON STA.ORGID=ORG.ORGID  where STA.ORGID in \n" +
                "(select ORGID from TBL_ORGANIZATION org where FATHERORGID=1 AND ORGTYPE=0 \n" +
                ") AND \n" +
                "(STA.STATUS is NULL or STA.STATUS != 0\n" +
                ") order by STAFFID desc");
        sbSql.append(" LIMIT " + pageInfo.getCurrentRecord() + "," + pageInfo.getPageSize());
        String sql = sbSql.toString();
        return sql;
    }

    public String selectListByOrgid(PageInfo<TblStaffMySql> pageInfo, BigDecimal orgid) {
        StringBuffer sbSql = new StringBuffer("select COUNT(*) from tbl_staff sta \n" +
                "INNER JOIN TBL_ORGANIZATION org ON STA.ORGID=ORG.ORGID  where STA.ORGID in \n" +
                "(select ORGID from TBL_ORGANIZATION org where FATHERORGID=1 AND ORGTYPE=0 \n" +
                ") AND \n" +
                "(STA.STATUS is NULL or STA.STATUS != 0\n" +
                ") order by STAFFID desc");
        sbSql.append(" LIMIT " + pageInfo.getCurrentRecord() + "," + pageInfo.getPageSize());
        String sql = sbSql.toString();
        return sql;
    }

//    public String selectCountFind(PageInfo<TblStaff> pageInfo, Find find) {
//        TblStaff tblStaff = new TblStaff();
//        StringBuffer sbSql = new StringBuffer("SELECT count(*) FROM (SELECT BUDGET.*,ROWNUM RNUM FROM(SELECT * FROM TBL_STAFF where ORGID = "+ tblStaff.getOrgid() + " and REALNAME like '% "+ find.getName() +" %' and USERNAME like '% "+ find.getUserName() + "%'");
//        sbSql.append(" )BUDGET WHERE ROWNUM <= "+(pageInfo.getCurrentPage()*pageInfo.getPageSize())+" ) WHERE RNUM > "+pageInfo.getCurrentRecord());
//        return sbSql.toString();
//    }


    public String selectListByPageInfoOrgid(PageInfo<TblStaffMySql> pageInfo, BigDecimal pid, FindMySql find) {
        StringBuffer sbSql = new StringBuffer("select * from TBL_STAFF TS LEFT JOIN TBL_ORGANIZATION TOR ON TS.ORGID = TOR.ORGID WHERE TS.ORGID =" + pid);

        if (find != null && StringUtils.isNotBlank(find.getName())) {
            sbSql.append(" and TS.REALNAME like '%" + find.getName() + "%'");
        }
        if (find != null && StringUtils.isNotBlank(find.getUserName())) {
            sbSql.append("  and TS.USERNAME like '%" + find.getUserName() + "%'");
        }
        if (find != null && StringUtils.isNotBlank(find.getStaffid())) {
            sbSql.append("  and TS.STAFFID = " + find.getStaffid() + "");
        }
        sbSql.append(" order by TS.STATUS desc,TS.STAFFID desc  LIMIT " + pageInfo.getCurrentRecord() + "," + pageInfo.getPageSize());
        String sql = sbSql.toString();
        return sql;
    }


    public String selectListByPageInfoFind(PageInfo<TblStaffMySql> pageInfo, Find find, BigDecimal orgid) {
        StringBuffer sbSql = new StringBuffer(" select * from TBL_STAFF TS LEFT JOIN TBL_ORGANIZATION TOR ON TS.ORGID = TOR.ORGID WHERE TOR.FATHERORGID =  '" + orgid + "' AND TOR.ORGTYPE = 0 ");
        if (find != null && StringUtils.isNotBlank(find.getName())) {
            sbSql.append(" and TS.REALNAME like '%" + find.getName() + "%'");
        }
        if (find != null && StringUtils.isNotBlank(find.getUserName())) {
            sbSql.append(" and TS.USERNAME like '%" + find.getUserName() + "%'");
        }
        if (find != null && StringUtils.isNotBlank(find.getStaffid())) {
            sbSql.append(" and TS.STAFFID = " + find.getStaffid() + "");
        }
        sbSql.append(" order by TS.STATUS desc,TS.STAFFID desc  LIMIT " + pageInfo.getCurrentRecord() + "," + pageInfo.getPageSize());
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


    public String selectListByPageInfo(PageInfo<UserService> pageInfo) {
        StringBuffer sbSql = new StringBuffer("select * from TBL_STAFF ");
        sbSql.append("where REALNAME like '%REALNAME%' and orgid= #{pid}");
        sbSql.append(" LIMIT " + pageInfo.getCurrentRecord() + "," + pageInfo.getPageSize());
        return sbSql.toString();
    }


    public String selectListByPageIn(PageInfo<TblStaffMySql> pageInfo, BigDecimal pid, FindMySql find) {
        StringBuffer sbSql = new StringBuffer("select * from tbl_staff where ORGID in (select ORGID from TBL_ORGANIZATION org where FATHERORGID=" + pid + " AND ORGTYPE=0 )");
        if (find != null && StringUtils.isNotBlank(find.getName())) {
            sbSql.append("  and REALNAME like '%" + find.getName() + "%'");
        }
        if (find != null && StringUtils.isNotBlank(find.getUserName())) {
            sbSql.append("  and USERNAME like '%" + find.getUserName() + "%'");
        }
        sbSql.append("order by STATUS desc,STAFFID desc LIMIT " + pageInfo.getCurrentRecord() + "," + pageInfo.getPageSize());
        String sql = sbSql.toString();
        return sql;
    }


    public String insertUser(TblStaffMySql user) {
        StringBuffer column = new StringBuffer("INSERT INTO TBL_STAFF (STAFFID");
        StringBuffer value = new StringBuffer(" VALUES ( "+ user.getStaffid() + " " );


        if (user.getStaffid() != null) {
            column.append("STAFFID");
            value.append(",'" + user.getStaffid() + "'");
        }
        if (user.getRealname() != null) {
            column.append(",REALNAME");
            value.append(",'" + user.getRealname() + "'");
        }
        if (user.getFixedphone() != null) {
            column.append(",FIXEDPHONE");
            value.append(",'" + user.getFixedphone() + "'");
        }
        if (user.getAddress() != null) {
            column.append(",ADDRESS");
            value.append(",'" + user.getAddress() + "'");
        }
        if (user.getEmail() != null) {
            column.append(",EMAIL");
            value.append(",'" + user.getEmail() + "'");
        }
        if (user.getMiblephone() != null) {
            column.append(",MIBLEPHONE");
            value.append(",'" + user.getMiblephone() + "'");
        }
        if (user.getMemo() != null) {
            column.append(",MEMO");
            value.append(",'" + user.getMemo() + "'");
        }
        if (user.getUsername() != null) {
            column.append(",USERNAME");
            value.append(",'" + user.getUsername() + "'");
        }
        if (user.getPassword() != null) {
            column.append(",PASSWORD");
            value.append(",'" + user.getPassword() + "'");
        }
        if (user.getJobid() != null) {
            column.append(",JOBID");
            value.append(",'" + user.getJobid() + "'");
        }
        if (user.getRoleid() != null) {
            column.append(",ROLEID");
            value.append(",'" + user.getRoleid() + "'");
        }
        if (user.getCreateDate() != null) {
            column.append(",CREATETIME");
            value.append(",'" + DateUtil.parseDate(user.getCreateDate(), "yyyy-MM-dd HH:mm:ss") + "'");
        }
        if (user.getStatus() != null) {
            column.append(",STATUS");
            value.append(",'" + user.getStatus() + "'");
        }
        if (user.getOrgid() != null) {
            column.append(",ORGID");
            value.append(",'" + user.getOrgid() + "'");
        }
        if (user.getOutSideId() != null) {
            column.append(",OUTSIDEID");
            value.append(",'" + user.getOutSideId() + "'");
        }
        if (user.getOutSideOpenId() != null) {
            column.append(",OUTSIDEOPENID");
            value.append(",'" + user.getOutSideOpenId() + "'");
        }
        if (user.getRoleIdStrs() != null && !"".equals(user.getRoleIdStrs())) {
            column.append(",ROLEIDSTRS");
            value.append(",'" + user.getRoleIdStrs() + "'");
        }


        column.append(")");
        value.append(")");
        String sql = column.toString() + value.toString();
        return sql;
    }

    public String updateStaff(TblStaffMySql user) {
        StringBuffer sql = new StringBuffer("UPDATE TBL_STAFF SET REALNAME = '" + user.getRealname() + "'");

        if (user.getFixedphone() != null && !"".equals(user.getFixedphone())) {
            sql.append(" , FIXEDPHONE = '" + user.getFixedphone() + "'");
        }
        if (user.getAddress() != null && !"".equals(user.getAddress())) {
            sql.append(" , ADDRESS = '" + user.getAddress() + "'");
        }
        if (user.getEmail() != null && !"".equals(user.getEmail())) {
            sql.append(" , EMAIL = '" + user.getEmail() + "'");
        }
        if (user.getMiblephone() != null && !"".equals(user.getMiblephone())) {
            sql.append(" , MIBLEPHONE = '" + user.getMiblephone() + "'");
        }
        if (user.getMemo() != null && !"".equals(user.getMemo())) {
            sql.append(" , MEMO = '" + user.getMemo() + "'");
        }
        if (user.getUsername() != null && !"".equals(user.getUsername())) {
            sql.append(" , USERNAME = '" + user.getUsername() + "'");
        }
        if (user.getPassword() != null && !"".equals(user.getPassword())) {
            sql.append(" , PASSWORD = '" + user.getPassword() + "'");
        }
        if (user.getJobid() != null && !"".equals(user.getJobid())) {
            sql.append(" , JOBID = '" + user.getJobid() + "'");
        }
        if (user.getCreateDate() != null && !"".equals(user.getCreateDate())) {
            sql.append(" , CREATETIME = '" + user.getCreateDate() + "'");
        }
        if (user.getStatus() != null && !"".equals(user.getStatus())) {
            sql.append(" , STATUS = '" + user.getStatus() + "'");
        }
        if (user.getOrgid() != null && !"".equals(user.getOrgid())) {
            sql.append(" , ORGID = '" + user.getOrgid() + "'");
        }
        if (user.getOutSideId() != null && !"".equals(user.getOrgid())) {
            sql.append(" , OUTSIDEID = '" + user.getOutSideId() + "'");
        }
        if (user.getOutSideOpenId() != null && !"".equals(user.getOutSideOpenId())) {
            sql.append(" , OUTSIDEOPENID = '" + user.getOutSideOpenId() + "'");
        }
        if (user.getRoleid() != null && !"".equals(user.getRoleid())) {
            sql.append(" , ROLEID = '" + user.getRoleid() + "'");
        }
        if (user.getRoleIdStrs() != null && !"".equals(user.getRoleIdStrs())) {
            sql.append(" , ROLEIDSTRS = '" + user.getRoleIdStrs() + "'");
        }

        sql.append(" WHERE STAFFID = '" + user.getStaffid() + "'");
        return sql.toString();
    }

    public String findPageListBySql(PageInfo<TblStaffMySql> pageInfo) {
        StringBuffer sbSql = new StringBuffer("SELECT s.STAFFID,s.REALNAME,o.ORGNAME FROM TBL_STAFF s LEFT JOIN TBL_ORGANIZATION o ON s.ORGID = o.ORGID WHERE s.STAFFID IN (select STAFFID from TBL_MANAGE_USER_BOOK WHERE BOOKID IN (" + pageInfo.getSqlStr() + ")) AND s.ORGID IN (SELECT ORGID FROM TBL_ORGANIZATION where ORGTYPE = 0 and getOrgIdList(ORGID = " + pageInfo.getCondition().getOrgid() + ") )");
        if (pageInfo.getCondition().getRealname() != null) {
            sbSql.append(" AND s.REALNAME LIKE '%" + pageInfo.getCondition().getRealname() + "%'");
        }
        sbSql.append("  LIMIT " + pageInfo.getCurrentRecord() + "," + pageInfo.getPageSize());
        String sql = sbSql.toString();
        return sql;
    }

    public String findPageCountBySql(PageInfo<TblStaffMySql> pageInfo) {
        StringBuffer sbSql = new StringBuffer("SELECT COUNT(*) FROM TBL_STAFF s LEFT JOIN TBL_ORGANIZATION o ON s.ORGID = o.ORGID WHERE s.STAFFID IN (select STAFFID from TBL_MANAGE_USER_BOOK WHERE BOOKID IN (" + pageInfo.getSqlStr() + ")) AND s.ORGID IN (SELECT ORGID FROM TBL_ORGANIZATION where ORGTYPE = 0 and getOrgIdList(ORGID = " + pageInfo.getCondition().getOrgid() + "))");
        if (pageInfo.getCondition().getRealname() != null) {
            sbSql.append(" AND s.REALNAME LIKE '%" + pageInfo.getCondition().getRealname() + "%'");
        }
        String sql = sbSql.toString();
        return sql;
    }


    public String findAllPageInfoByacctid(PageInfo<TblStaffMySql> pageInfo, String bookid) {
        StringBuffer sbSql = new StringBuffer(" SELECT STA.USERNAME, STA.ADDRESS, STA.REALNAME, ( SELECT ORGNAME FROM TBL_ORGANIZATION org WHERE 1 = 1  \n" +
                "AND ORGTYPE <> 0 AND orgtype < 100 and getOrgIdList(ORGID = sta.ORGID) ) orgname FROM TBL_STAFF sta LEFT JOIN TBL_ORGANIZATION org ON STA.ORGID = ORG.ORGID WHERE \n" +
                " \t sta.STAFFID IN ( select STAFFID FROM TBL_MANAGE_USER_BOOK WHERE BOOKID = '" + bookid + "'");

        sbSql.append(" ) ORDER BY STA.STAFFID  LIMIT " + pageInfo.getCurrentRecord() + "," + pageInfo.getPageSize());
        String sql = sbSql.toString();
        return sql;
    }

    public String findAllPageInfoByacctidcCount(String bookid) {
        StringBuffer sbSql = new StringBuffer("SELECT COUNT(*) FROM TBL_STAFF sta LEFT JOIN TBL_ORGANIZATION org ON STA.ORGID = ORG.ORGID WHERE \n" +
                " \t sta.STAFFID IN ( select STAFFID FROM TBL_MANAGE_USER_BOOK WHERE BOOKID = '" + bookid + "'");

        sbSql.append(" ) ORDER BY STA.STAFFID");
        String sql = sbSql.toString();
        return sql;
    }


    public String findByAll(String pid, PageInfo<TblStaffMySql> pageInfo) {
        StringBuffer sbSql = new StringBuffer(" select * from TBL_STAFF TS LEFT JOIN TBL_ORGANIZATION TOR ON TS.ORGID = TOR.ORGID WHERE TS.ORGID = '" + pid + "' AND (TS.STATUS is NULL or TS.STATUS != 0)");

        sbSql.append(" LIMIT " + pageInfo.getCurrentRecord() + "," + pageInfo.getPageSize());
        String sql = sbSql.toString();
        return sql;
    }

    public String findByAllORGID(BigDecimal orgid, PageInfo<TblStaffMySql> pageInfo) {
        StringBuffer sbSql = new StringBuffer("SELECT * FROM TBL_STAFF TS LEFT JOIN TBL_ORGANIZATION TOR ON TS.ORGID = TOR.ORGID \n" +
                "WHERE TOR.FATHERORGID = '" + orgid + "' AND (TS.STATUS IS NULL OR TS.STATUS != 0)");

        sbSql.append("  LIMIT " + pageInfo.getCurrentRecord() + "," + pageInfo.getPageSize());
        String sql = sbSql.toString();
        return sql;
    }

    public String findAllPageBeanPid(PageInfo<TblStaffMySql> pageInfo, TblOrganizationMySql attribute) {
        StringBuffer sbSql = new StringBuffer("");
        if (attribute.getOrgtype() != null && attribute.getOrgtype().toString().equals("0")) {
            sbSql = new StringBuffer("select STAFFID,REALNAME,STA.ORGID,ORG.ORGNAME,STA.ADDRESS,STA.EMAIL,username from tbl_staff sta INNER JOIN TBL_ORGANIZATION org ON STA.ORGID=ORG.ORGID  where STA.ORGID =" + attribute.getOrgid() + " AND (STA.STATUS is NULL or STA.STATUS != 0)");
        } else {
            sbSql = new StringBuffer("select STAFFID,REALNAME,STA.ORGID,ORG.ORGNAME,STA.ADDRESS,STA.EMAIL,username from tbl_staff sta INNER JOIN TBL_ORGANIZATION org ON STA.ORGID=ORG.ORGID  where STA.ORGID in (select ORGID from TBL_ORGANIZATION org where FATHERORGID=" + attribute.getOrgid() + " AND ORGTYPE=0 ) AND (STA.STATUS is NULL or STA.STATUS != 0)");
        }

        sbSql.append(" order by STAFFID desc  LIMIT " + pageInfo.getCurrentRecord() + "," + pageInfo.getPageSize());
        return sbSql.toString();
    }

    public String findCountPageBeanPid(PageInfo<TblStaffMySql> pageInfo, TblOrganizationMySql attribute) {
        StringBuffer sbSql = new StringBuffer("");
        if (attribute.getOrgtype() != null && attribute.getOrgtype().toString().equals("0")) {
            sbSql = new StringBuffer("select count(*) FROM(select STAFFID,REALNAME,STA.ORGID,ORG.ORGNAME,STA.ADDRESS,STA.EMAIL,username from tbl_staff sta INNER JOIN TBL_ORGANIZATION org ON STA.ORGID=ORG.ORGID  where STA.ORGID =" + attribute.getOrgid() + " AND (STA.STATUS is NULL or STA.STATUS != 0)) as a ");
        } else {
            sbSql = new StringBuffer(" select count(*) FROM (select STAFFID,REALNAME,STA.ORGID,ORG.ORGNAME,STA.ADDRESS,STA.EMAIL,username from tbl_staff sta INNER JOIN TBL_ORGANIZATION org ON STA.ORGID=ORG.ORGID  where STA.ORGID in (select ORGID from TBL_ORGANIZATION org where FATHERORGID=" + attribute.getOrgid() + " AND ORGTYPE=0 ) AND (STA.STATUS is NULL or STA.STATUS != 0)) as a ");
        }

        return sbSql.toString();
    }

    public String findByJobName(String jobname, String orgid){
        return null;
    }
}
