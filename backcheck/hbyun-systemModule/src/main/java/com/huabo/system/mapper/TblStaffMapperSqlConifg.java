package com.huabo.system.mapper;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hbfk.util.DateUtil;
import com.hbfk.util.PageInfo;
import com.hbfk.util.database.DataBaseSqlConfig;
import com.hbfk.util.redis.Random.RandomUtil;
import com.huabo.system.entity.Find;
import com.huabo.system.entity.TblOrganization;
import com.huabo.system.entity.TblStaff;
import com.huabo.system.service.UserService;
import org.apache.commons.lang.StringUtils;

import static org.mockito.Mockito.RETURNS_DEEP_STUBS;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

public class TblStaffMapperSqlConifg {
	
	public String selectFlowHxrBySecrect(List<String> pkYmStaffIdList, String secrectId, String scopeStaffIds) throws Exception{
		StringBuffer sqlSb = new StringBuffer("SELECT PKYMSTAFFID FROM TBL_STAFF WHERE SECRECTLEVELID IN ('").append(secrectId.replace(",", "','")).append("') AND PKYMSTAFFID IN ('").append(String.join("','", pkYmStaffIdList)).append("')");
		
		if(StringUtils.isNotBlank(scopeStaffIds)) {
			sqlSb.append(" AND STAFFID IN (").append(scopeStaffIds).append(") ");
		}
		
		String sql = sqlSb.toString();
		return sql;
	}
	
	
	public String selectExprotList(TblStaff staff) throws Exception{
		StringBuffer sb = new StringBuffer("SELECT TS.STAFFID,TS.REALNAME,TS.ADDRESS,TS.EMAIL,TS.MIBLEPHONE,TS.MEMO,TS.USERNAME,TS.ROLEIDSTRS,ORG.ORGNAME FROM TBL_STAFF TS LEFT JOIN TBL_ORGANIZATION ORG ON TS.ORGID = ORG.ORGID ");
		sb.append(" WHERE TS.STAFFID IN (SELECT STAFFID FROM TBL_USER_ORGRELATION WHERE ");
		
		sb.append(DataBaseSqlConfig.getInOrNotSql("DEPTID", "IN", "OR", Arrays.asList(staff.getOrgIdStrs().split(",")))).append(" ) ");
		
		
		if(StringUtils.isNotBlank(staff.getRealname())) {
			sb.append(" AND TS.REALNAME LIKE '%").append(staff.getRealname()).append("%'");
		}
		
		sb.append(" ORDER BY TS.STATUS desc,TS.ORGID ASC,TS.STAFFID desc ");
		String sql = sb.toString();
		return sql;
	}
	
	
	public String findAllLeader2(IPage<TblStaff> page, String orgids) throws Exception{
		String sql = "select * from tbl_staff where orgid in (select orgid from tbl_organization where  orgname='公司领导' ";
		if(StringUtils.isNotBlank(orgids)) {
			sql += " and fatherorgid = "+orgids;
		}
		sql += ")";
		return sql;
	}
	
	
	public String selectbmfzrByOrgId(String orgid) throws Exception{
		String sql = "SELECT PKYMSTAFFID FROM TBL_STAFF WHERE "+DataBaseSqlConfig.getWhereColumnInStr("FGORGS", orgid, ",")+" AND "+DataBaseSqlConfig.getRowLimitSql(0, 1);
		return sql;
	}
	
	public String selectWxappAdmin(String orgIds) throws Exception{
		String sql = "SELECT * FROM TBL_STAFF WHERE CREATETIME = (" + 
				" SELECT MIN(CREATETIME) FROM TBL_STAFF WHERE ORGID IN ("+orgIds+") AND ORGID IN ("+orgIds+") AND " + DataBaseSqlConfig.getRowLimitSql(0, 1);
		sql += " ORDER BY CREATETIME ASC";
		return sql;
	}
	
	public String findUserInfoExam(BigDecimal staffId, BigDecimal rid) throws Exception{
		String sql = "select * from TBL_STAFF where STAFFID = "+staffId +" AND "+DataBaseSqlConfig.getWhereColumnInStr("ROLEIDSTRS", rid.toString(), ",");
		return sql;
	}

	public String selectListByThemeHouse(IPage<TblStaff> page,TblStaff staff) {
		
		String sql = " SELECT TS.*,ORG.ORGNAME FROM TBL_STAFF TS LEFT JOIN TBL_ORGANIZATION ORG ON TS.ORGID = ORG.ORGID WHERE TS.STAFFID IN (SELECT STAFFID FROM TBL_USER_ORGRELATION WHERE ORGID = "+staff.getOrgid()+" ) AND TS.STAFFID IN (SELECT STAFFID FROM TBL_BI_USER_PAGE WHERE PAGEID IN ("+staff.getMemo()+"))";
		
		if(StringUtils.isNotBlank(staff.getRealname())) {
			sql += " AND TS.REALNAME LIKE '%"+staff.getRealname()+"%'";
		}
		
		if(StringUtils.isNotBlank(staff.getUsername())) {
			sql += " AND TS.USERNAME LIKE '%"+staff.getUsername()+"%'";
		}
		
		sql += " ORDER BY TS.STAFFID ASC ";
		return sql;
	}
	
	public String selectCountByThemeHouse(PageInfo<TblStaff> pageInfo) {
		TblStaff staff = pageInfo.getCondition();
		
		String sql = " SELECT COUNT(0) FROM TBL_STAFF TS WHERE TS.STAFFID IN (SELECT STAFFID FROM TBL_USER_ORGRELATION WHERE ORGID = "+staff.getOrgid()+" ) AND TS.STAFFID IN (SELECT STAFFID FROM TBL_BI_USER_PAGE WHERE PAGEID IN ("+staff.getMemo()+"))";
		
		if(StringUtils.isNotBlank(staff.getRealname())) {
			sql += " AND TS.REALNAME LIKE '%"+staff.getRealname()+"%'";
		}
		
		if(StringUtils.isNotBlank(staff.getUsername())) {
			sql += " AND TS.USERNAME LIKE '%"+staff.getUsername()+"%'";
		}
		
		return sql;
	}
	
	
	
	public String selectStaffCountToGrantRole(PageInfo<TblStaff> pageInfo, String userName, String realName,
			Integer roleId, String deptName, String deptIds, Integer isAll) {
		StringBuffer sqlSb = new StringBuffer("SELECT COUNT(0) FROM TBL_STAFF TS LEFT JOIN TBL_ORGANIZATION DEPT ON TS.ORGID = DEPT.ORGID WHERE 1 = 1 ");
		
		if(StringUtils.isNotBlank(deptName)) {
			sqlSb.append(" AND DEPT.ORGNAME LIKE '%").append(deptName).append("%'");
		}
		
		if(StringUtils.isNotBlank(userName)) {
			sqlSb.append(" AND TS.USERNAME LIKE '%").append(userName).append("%'");
		}
		
		if(StringUtils.isNotBlank(realName)) {
			sqlSb.append(" AND TS.REALNAME LIKE '%").append(realName).append("%'");
		}
		
		if(StringUtils.isNotBlank(deptIds)) {
			sqlSb.append(" AND TS.STAFFID IN (SELECT STAFFID FROM TBL_USER_ORGRELATION WHERE DEPTID IN (").append(deptIds).append("))");
		}
		String sql = sqlSb.toString();
		return sql;
	}
	
	public String selectStaffListToGrantRole(IPage<TblStaff> page, String userName, String realName,
			BigDecimal roleId, String deptName, String deptIds, Integer isAll) throws Exception {
		StringBuffer sqlSb = new StringBuffer("SELECT TS.STAFFID,TS.REALNAME,TS.USERNAME,TS.CHARGELEADERSTAFFID,TS.MIBLEPHONE,TS.EMAIL,TS.FIXEDPHONE,TS.ROLEIDSTRS,DEPT.ORGID,DEPT.ORGNAME,CASE WHEN ")
				.append(DataBaseSqlConfig.getWhereColumnInStr("TS.ROLEIDSTRS", roleId.toString() , ",")).append(" THEN 1 ELSE 0 END AS ISCHECKED FROM TBL_STAFF TS LEFT JOIN TBL_ORGANIZATION DEPT ON TS.ORGID = DEPT.ORGID WHERE 1 = 1 "
						+ " AND TS.USERNAME not in ('aqadmin','sjadmin','sysadmin') ");//中核去掉三员
		
		if(StringUtils.isNotBlank(deptName)) {
			sqlSb.append(" AND DEPT.ORGNAME LIKE '%").append(deptName).append("%'");
		}
		
		if(StringUtils.isNotBlank(userName)) {
			sqlSb.append(" AND TS.USERNAME LIKE '%").append(userName).append("%'");
		}
		
		if(StringUtils.isNotBlank(realName)) {
			sqlSb.append(" AND TS.REALNAME LIKE '%").append(realName).append("%'");
		}
		
		if(StringUtils.isNotBlank(deptIds)) {
			sqlSb.append(" AND TS.STAFFID IN (SELECT STAFFID FROM TBL_USER_ORGRELATION WHERE DEPTID IN (").append(deptIds).append("))");
		}
		
		sqlSb.append(" ORDER BY ORGID,STAFFID ");

		String sql = sqlSb.toString();
		return sql;
	}
	
	public String selectStaffListByRoleId(IPage<TblStaff> page, String userName, String realName,
			BigDecimal roleId,String deptName, String deptIds) throws Exception {
		StringBuffer sqlSb = new StringBuffer("SELECT TS.STAFFID,TS.REALNAME,TS.USERNAME,TS.MIBLEPHONE,TS.EMAIL,TS.FIXEDPHONE,TS.ROLEIDSTRS,DEPT.ORGID,DEPT.ORGNAME,TS.CHARGELEADERSTAFFID FROM TBL_STAFF TS LEFT JOIN TBL_ORGANIZATION DEPT ON TS.ORGID = DEPT.ORGID WHERE ")
				.append(DataBaseSqlConfig.getWhereColumnInStr("TS.ROLEIDSTRS", roleId.toString(), ","));
		
		if(StringUtils.isNotBlank(deptName)) {
			sqlSb.append(" AND DEPT.ORGNAME LIKE '%").append(deptName).append("%'");
		}
		
		if(StringUtils.isNotBlank(userName)) {
			sqlSb.append(" AND TS.USERNAME LIKE '%").append(userName).append("%'");
		}
		
		if(StringUtils.isNotBlank(realName)) {
			sqlSb.append(" AND TS.REALNAME LIKE '%").append(realName).append("%'");
		}
		
		if(StringUtils.isNotBlank(deptIds)) {
			sqlSb.append(" AND TS.ORGID IN (").append(deptIds).append(")");
		}
		
		sqlSb.append(" ORDER BY ORGID,STAFFID ");

		String sql = sqlSb.toString();
		return sql;
	}
	
	public String selectStaffCountByRoleId(PageInfo<TblStaff> pageInfo, String userName, String realName, Integer roleId,
			String deptName, String deptIds) {
		
		StringBuffer sqlSb = new StringBuffer("SELECT COUNT(0) FROM TBL_STAFF TS LEFT JOIN TBL_ORGANIZATION DEPT ON TS.ORGID = DEPT.ORGID WHERE INSTR(','||TS.ROLEIDSTRS||',',',");
		sqlSb.append(roleId).append(",') > 0");
		
		if(StringUtils.isNotBlank(deptName)) {
			sqlSb.append(" AND DEPT.ORGNAME LIKE '%").append(deptName).append("%'");
		}
		
		if(StringUtils.isNotBlank(userName)) {
			sqlSb.append(" AND TS.USERNAME LIKE '%").append(userName).append("%'");
		}
		
		if(StringUtils.isNotBlank(realName)) {
			sqlSb.append(" AND TS.REALNAME LIKE '%").append(realName).append("%'");
		}
		
		if(StringUtils.isNotBlank(deptIds)) {
			sqlSb.append(" AND TS.ORGID IN (").append(deptIds).append(")");
		}

		String sql = sqlSb.toString();
		return sql;
	}
	
    public String selectUniqueCountBmfzr(String orgId, BigDecimal staffid) throws Exception {
        String sql = "SELECT COUNT(0) FROM TBL_STAFF WHERE " + DataBaseSqlConfig.getWhereColumnInStr("MANAGEORGS", orgId, ",");
        if (staffid != null) {
            sql += " AND STAFFID != " + staffid;
        }
        return sql;
    }

    public String selectUniqueCountFgld(String orgId, BigDecimal staffid) throws Exception {
        String sql = "SELECT COUNT(0) FROM TBL_STAFF WHERE "+DataBaseSqlConfig.getWhereColumnInStr("FGORGS", orgId, ",");
        if (staffid != null) {
            sql += " AND STAFFID != " + staffid;
        }
        return sql;
    }

    public String selectUniqueCountUserName(String username, BigDecimal staffid) {
        String sql = "SELECT COUNT(0) FROM TBL_STAFF WHERE USERNAME = '" + username + "'";
        if (staffid != null) {
            sql += " AND STAFFID != " + staffid;
        }
        return sql;
    }

    public String selectAllYmStaffList(BigDecimal orgId) {
        String sql = "SELECT STAFF.STAFFID,STAFF.USERNAME,STAFF.EMAIL,STAFF.MIBLEPHONE,STAFF.PKYMSTAFFID,STAFF.REALNAME,ORG.PKYMORGID,STAFF.ROLEIDSTRS ,STAFF.PASSWORD,JOB.PKYMJOBID,STAFF.CHARGELEADERSTAFFID " +
                " FROM TBL_STAFF STAFF LEFT JOIN TBL_ORGANIZATION ORG ON STAFF.ORGID = ORG.ORGID LEFT JOIN TBL_JOB JOB ON JOB.JOBID = STAFF.JOBID WHERE STAFF.ORGID = " + orgId + " AND STAFF.USERNAME IS NOT NULL";
        return sql;
    }

    public String selectAllYmStaffInfo(BigDecimal staffId) {
        String sql = "SELECT STAFF.STAFFID,STAFF.USERNAME,STAFF.EMAIL,STAFF.MIBLEPHONE,STAFF.PKYMSTAFFID,STAFF.REALNAME,ORG.PKYMORGID,STAFF.ROLEIDSTRS ,STAFF.PASSWORD,JOB.PKYMJOBID ,STAFF.ORGID,STAFF.CHARGELEADERSTAFFID  " +
                " FROM TBL_STAFF STAFF LEFT JOIN TBL_ORGANIZATION ORG ON STAFF.ORGID = ORG.ORGID LEFT JOIN TBL_JOB JOB ON JOB.JOBID = STAFF.JOBID WHERE STAFF.STAFFID = " + staffId + " AND STAFF.USERNAME IS NOT NULL";
        return sql;
    }

    public String selectListByPageInfoo(IPage<TblStaff> page, Find find, BigDecimal orgid) {
        StringBuffer sbSql = new StringBuffer("select * from tbl_staff where orgid= " + orgid + " ");
        if (find != null && StringUtils.isNotBlank(find.getName())) {
            sbSql.append("  and REALNAME like '%" + find.getName() + "%'");
        }
        if (find != null && StringUtils.isNotBlank(find.getUserName())) {
            sbSql.append("  and USERNAME like '%" + find.getUserName() + "%'");
        }
        sbSql.append(" order by STATUS desc,STAFFID desc");
        String sql = sbSql.toString();
        return sql;
    }

    public String selectListByPage(IPage<TblStaff> page, String pid) {
        StringBuffer sbSql = new StringBuffer("select toa.ORGNAME,ts.* from " +
                "(select * from TBL_STAFF  where orgid in (" + pid + ") AND (STATUS is NULL or STATUS != 0))ts " +
                "left join TBL_ORGANIZATION toa ON ts.ORGID = toa.ORGID");
        String sql = sbSql.toString();
        return sql;
    }

    public String selectListBy(IPage<TblStaff> page, BigDecimal orgid) {
        StringBuffer sbSql = new StringBuffer("select STAFFID,REALNAME,STA.ORGID,ORG.ORGNAME from tbl_staff sta INNER JOIN TBL_ORGANIZATION org ON STA.ORGID=ORG.ORGID  where STA.ORGID in " +
                "(select ORGID from TBL_ORGANIZATION org where FATHERORGID=1 AND ORGTYPE=0 " +
                ") AND (STA.STATUS is NULL or STA.STATUS != 0 ) order by STAFFID desc");
        String sql = sbSql.toString();
        return sql;
    }

    public String selectListByPageInfoOrgid(IPage<TblStaff> page, String orgIdStrs, Find find) {
		StringBuffer sbSql = new StringBuffer("select TS.*,ORG.ORGNAME,TSL.LEVELNAME AS SECRECTLEVELNAME from TBL_STAFF TS LEFT JOIN TBL_SECRECT_LEVEL TSL ON TS.SECRECTLEVELID = TSL.LEVELID LEFT JOIN TBL_ORGANIZATION ORG ON ORG.ORGID = TS.ORGID WHERE STAFFID IN (SELECT STAFFID FROM TBL_USER_ORGRELATION WHERE DEPTID IN ("+orgIdStrs+")) ");

		if (find != null && StringUtils.isNotBlank(find.getName())) {
			sbSql.append(" and TS.REALNAME like '%" + find.getName() + "%'");
		}
		if (find != null && StringUtils.isNotBlank(find.getUserName())) {
			sbSql.append("  and TS.USERNAME like '%" + find.getUserName() + "%'");
		}
		if (find != null && StringUtils.isNotBlank(find.getStaffid())) {
			sbSql.append("  and TS.STAFFID = " + find.getStaffid());
		}
		if(find != null && StringUtils.isNotBlank(find.getState())) {
			sbSql.append("  and TS.STATUS = " + find.getState());
		}
		sbSql.append(" order by TS.STATUS desc,TS.ORGID ASC,TS.STAFFID desc");
		String sql = sbSql.toString();
		return sql;
	}

    public String selectListByPageInfoFind(IPage<TblStaff> page, Find find) {
		StringBuffer sbSql = new StringBuffer("select TS.*,ORG.ORGNAME,TSL.LEVELNAME AS SECRECTLEVELNAME from TBL_STAFF TS LEFT JOIN TBL_SECRECT_LEVEL TSL ON TS.SECRECTLEVELID = TSL.LEVELID LEFT JOIN TBL_ORGANIZATION ORG ON ORG.ORGID = TS.ORGID WHERE TS.ORGID IS NOT NULL");
		if (find != null && StringUtils.isNotBlank(find.getName())) {
			sbSql.append(" and TS.REALNAME like '%" + find.getName() + "%'");
		}
		if (find != null && StringUtils.isNotBlank(find.getUserName())) {
			sbSql.append(" and TS.USERNAME like '%" + find.getUserName() + "%'");
		}
		if (find != null && StringUtils.isNotBlank(find.getStaffid())) {
			sbSql.append(" and TS.STAFFID = " + find.getStaffid() + "");
		}
		sbSql.append(" order by TS.STATUS desc,TS.ORGID ASC,TS.STAFFID desc");
		String sql = sbSql.toString();
		return sql;
	}
    
    
    public String selectAllStaffListByPageInfo(TblStaff staff) throws Exception{
    	StringBuffer sbSql = new StringBuffer("select TS.*,ORG.ORGNAME,TSL.LEVELNAME AS SECRECTLEVELNAME from TBL_STAFF TS LEFT JOIN TBL_ORGANIZATION ORG ON ORG.ORGID = TS.ORGID LEFT JOIN TBL_SECRECT_LEVEL TSL ON TS.SECRECTLEVELID = TSL.LEVELID WHERE 1 = 1 AND TS.USERNAME not in ('aqadmin','sjadmin','sysadmin')  ");
    	
    	if(StringUtils.isNotBlank(staff.getFormSecrectScope())) {
    		sbSql.append(" AND TS.SECRECTLEVELID IN (").append(staff.getFormSecrectScope()).append(")");
    	}
    	
    	if(StringUtils.isNotBlank(staff.getOrgIdStrs())) {
    		sbSql.append(" AND TS.STAFFID IN (SELECT STAFFID FROM TBL_USER_ORGRELATION WHERE ");
    		sbSql.append(DataBaseSqlConfig.getInOrNotSql("DEPTID", "IN", "OR", Arrays.asList(staff.getOrgIdStrs().split(",")))).append(" ) ");
    	}
    	
    	if(staff.getOrgIdList() != null && staff.getOrgIdList().size() > 0) {
    		sbSql.append(" AND TS.STAFFID IN (SELECT STAFFID FROM TBL_USER_ORGRELATION WHERE ");
    		sbSql.append(DataBaseSqlConfig.getInOrNotSql("DEPTID", "IN", "OR", staff.getOrgIdList())).append(" ) ");
    	}
    	
		if (StringUtils.isNotBlank(staff.getRealname())) {
			sbSql.append(" AND TS.REALNAME LIKE '%" + staff.getRealname() + "%'");
		}
		if (StringUtils.isNotBlank(staff.getUsername())) {
			sbSql.append(" AND TS.USERNAME LIKE '%" + staff.getUsername() + "%'");
		}
		if(staff.getStatus() != null) {
			sbSql.append(" AND TS.STATUS = "+staff.getStatus());
		}
		if (StringUtils.isNotBlank(staff.getMiblephone())) {
			sbSql.append(" AND TS.MIBLEPHONE LIKE '%" + staff.getMiblephone() + "%'");
		}
		if (StringUtils.isNotBlank(staff.getEducation())) {
			sbSql.append(" AND TS.EDUCATION LIKE '%" + staff.getEducation() + "%'");
		}
		if (StringUtils.isNotBlank(staff.getPoliticaloutlook())) {
			sbSql.append(" and TS.POLITICALOUTLOOK LIKE '%" + staff.getPoliticaloutlook() + "%'");
		}
		if (StringUtils.isNotBlank(staff.getMajor())) {
			sbSql.append(" AND TS.MAJOR LIKE '%" + staff.getMajor() + "%'");
		}
		if (StringUtils.isNotBlank(staff.getTitle())) {
			sbSql.append(" AND TS.TITLE LIKE '%" + staff.getTitle() + "%'");
		}
		if (StringUtils.isNotBlank(staff.getQualification())) {
			sbSql.append(" and TS.QUALIFICATION LIKE '%" + staff.getQualification() + "%'");
		}
        if (StringUtils.isNotBlank(staff.getAddress())) {
            sbSql.append(" and TS.REALNAME NOT LIKE '%" + staff.getAddress() + "%'");
        }
		
		sbSql.append(" ORDER BY TS.STATUS desc,TS.ORGID ASC,TS.STAFFID desc ");
		String sql = sbSql.toString();
		return sql;
    }
    
    
    public String selectListByPageInfo(PageInfo<UserService> pageInfo) {
        StringBuffer sbSql = new StringBuffer("select * from TBL_STAFF ");
        sbSql.append("where REALNAME like '%REALNAME%' and orgid= #{pid}(");
        sbSql.append(") T1 WHERE ROWNUM <= " + (pageInfo.getCurrentPage() * pageInfo.getPageSize()) + " ) T2 WHERE T2.RNUM > " + pageInfo.getCurrentRecord());
        return sbSql.toString();
    }


    public String selectListByPageIn(IPage<TblStaff> page, BigDecimal pid, Find find) {
        StringBuffer sbSql = new StringBuffer("select * from tbl_staff where ORGID in (select ORGID from TBL_ORGANIZATION org where FATHERORGID=" + pid + " AND ORGTYPE=0 )");
        if (find != null && StringUtils.isNotBlank(find.getName())) {
            sbSql.append("  and REALNAME like '%" + find.getName() + "%'");
        }
        if (find != null && StringUtils.isNotBlank(find.getUserName())) {
            sbSql.append("  and USERNAME like '%" + find.getUserName() + "%'");
        }
        sbSql.append("order by STATUS desc,STAFFID desc");
        String sql = sbSql.toString();
        return sql;
    }


    public String insertUser(TblStaff user) throws Exception {
        StringBuffer column = new StringBuffer("INSERT INTO TBL_STAFF (STAFFID");
        StringBuffer value = new StringBuffer(" VALUES (");
        
        if(user.getStaffid() == null) {
        	value.append(RandomUtil.uuBigDecimalId());
        }else {
        	value.append(user.getStaffid());
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

        if (user.getIsAudit() != null && !"".equals(user.getIsAudit())) {
            column.append(",IsAudit");
            value.append(",'" + user.getIsAudit() + "'");
        }

        if (user.getPoliticaloutlook() != null && !"".equals(user.getPoliticaloutlook())) {
            column.append(",Politicaloutlook");
            value.append(",'" + user.getPoliticaloutlook() + "'");
        }
        if (user.getEducation() != null && !"".equals(user.getEducation())) {
            column.append(",Education");
            value.append(",'" + user.getEducation() + "'");
        }
        if (user.getMajor() != null && !"".equals(user.getMajor())) {
            column.append(",Major");
            value.append(",'" + user.getMajor() + "'");
        }

        if (user.getSchool() != null && !"".equals(user.getSchool())) {
            column.append(",School");
            value.append(",'" + user.getSchool() + "'");
        }
        if (user.getOfficephone() != null && !"".equals(user.getOfficephone())) {
            column.append(",Officephone");
            value.append(",'" + user.getOfficephone() + "'");
        }

        if (user.getTitle() != null && !"".equals(user.getTitle())) {
            column.append(",Title");
            value.append(",'" + user.getTitle() + "'");
        }
        if (user.getQualification() != null && !"".equals(user.getQualification())) {
            column.append(",Qualification");
            value.append(",'" + user.getQualification() + "'");
        }

        if (user.getSituation() != null && !"".equals(user.getSituation())) {
            column.append(",Situation");
            value.append(",'" + user.getSituation() + "'");
        }
        if (user.getJobexperiences() != null && !"".equals(user.getJobexperiences())) {
            column.append(",Jobexperiences");
            value.append(",'" + user.getJobexperiences() + "'");
        }

        if (user.getCreateDate() != null) {
            column.append(",CREATETIME");
            value.append(",").append(DataBaseSqlConfig.getDateStrFormat(user.getCreateDate()));
        }

        if (user.getWorktime() != null) {
            column.append(",worktime");
            value.append(",").append(DataBaseSqlConfig.getDateStrFormat(user.getWorktime()));
        }
        if (user.getEntrytime() != null) {
            column.append(",entrytime");
            value.append(",").append(DataBaseSqlConfig.getDateStrFormat(user.getEntrytime()));
        }
        if (user.getResignationtime() != null) {
            column.append(",resignationtime");
            value.append(",").append(DataBaseSqlConfig.getDateStrFormat(user.getResignationtime()));
        }
        if (user.getAuditortype() != null && !"".equals(user.getAuditortype())) {
            column.append(",auditortype");
            value.append(",'" + user.getAuditortype() + "'");
        }

        if (user.getGender() != null && !"".equals(user.getGender())) {
            column.append(",gender");
            value.append(",'" + user.getGender() + "'");
        }
        if (user.getType() != null && !"".equals(user.getType())) {
            column.append(",TYPE");
            value.append(",'" + user.getType() + "'");
        }
        if (user.getHistorycode() != null && !"".equals(user.getHistorycode())) {
            column.append(",historycode");
            value.append(",'" + user.getHistorycode() + "'");
        }
        if (user.getHistorydepartmentid() != null && !"".equals(user.getHistorydepartmentid())) {
            column.append(",historydepartmentid");
            value.append(",'" + user.getHistorydepartmentid() + "'");
        }

        if (user.getPersonType() != null && !"".equals(user.getPersonType())) {
            column.append(",persontype");
            value.append(",'" + user.getPersonType() + "'");
        }

        if (user.getManageorgs() != null && !"".equals(user.getManageorgs())) {
            column.append(",MANAGEORGS");
            value.append(",'" + user.getManageorgs() + "'");
        }

        if (user.getManageorgnames() != null && !"".equals(user.getManageorgnames())) {
            column.append(",MANAGEORGNAMES");
            value.append(",'" + user.getManageorgnames() + "'");
        }

        if (user.getFgorgs() != null && !"".equals(user.getFgorgs())) {
            column.append(",FGORGS");
            value.append(",'" + user.getFgorgs() + "'");
        }

        if (user.getFgorgnames() != null && !"".equals(user.getFgorgnames())) {
            column.append(",FGORGNAMES");
            value.append(",'" + user.getFgorgnames() + "'");
        }

        if (user.getDataSource() != null && !"".equals(user.getDataSource())) {
            column.append(",DATASOURCE");
            value.append(",'" + user.getDataSource() + "'");
        }
        if(StringUtils.isNotBlank(user.getIdCard())) {
        	 column.append(",IDCARD");
             value.append(",'" + user.getIdCard() + "'");
        }
        if(user.getChargeLeaderStaffId() != null) {
       	 	column.append(",CHARGELEADERSTAFFID");
            value.append(",'" + user.getChargeLeaderStaffId() + "'");
        }
        
        
        if(user.getSecrectLevelId() != null) {
        	column.append(",SECRECTLEVELID");
            value.append(",'" + user.getSecrectLevelId() + "'");
        }
        column.append(")");
        value.append(")");
        String sql = column.toString() + value.toString();
        return sql;
    }

    public String updateStaffByUsername(TblStaff user) throws Exception {
        StringBuffer sql = updateSql(user);
        sql.append(" where username = '").append(user.getUsername()).append("'");
        return sql.toString();
    }

    public String updateStaff(TblStaff user) throws Exception {
        StringBuffer sql = updateSql(user);
        sql.append(" WHERE STAFFID = '" + user.getStaffid() + "'");
        return sql.toString();
    }

    private StringBuffer updateSql(TblStaff user) throws Exception {
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
        if (user.getRoleid() != null) {
            sql.append(" , ROLEID = '" + user.getRoleid() + "'");
        }
        if (user.getRoleIdStrs() != null && !"".equals(user.getRoleIdStrs())) {
            sql.append(" , ROLEIDSTRS = '" + user.getRoleIdStrs() + "'");
        }
        if (user.getIsAudit() != null && !"".equals(user.getIsAudit())) {
            sql.append(" , IsAudit = '" + user.getIsAudit() + "'");
        }
        if (user.getPoliticaloutlook() != null && !"".equals(user.getPoliticaloutlook())) {
            sql.append(" , Politicaloutlook = '" + user.getPoliticaloutlook() + "'");
        }
        if (user.getEducation() != null && !"".equals(user.getEducation())) {
            sql.append(" , Education = '" + user.getEducation() + "'");
        }
        if (user.getMajor() != null && !"".equals(user.getMajor())) {
            sql.append(" , Major = '" + user.getMajor() + "'");
        }
        if (user.getSchool() != null && !"".equals(user.getSchool())) {
            sql.append(" , School = '" + user.getSchool() + "'");
        }
        if (user.getOfficephone() != null && !"".equals(user.getOfficephone())) {
            sql.append(" , Officephone= '" + user.getOfficephone() + "'");
        }
        if (user.getTitle() != null && !"".equals(user.getTitle())) {
            sql.append(" , Title= '" + user.getTitle() + "'");
        }
        if (user.getQualification() != null && !"".equals(user.getQualification())) {
            sql.append(" , Qualification= '" + user.getQualification() + "'");
        }
        if (user.getSituation() != null && !"".equals(user.getSituation())) {
            sql.append(" , Situation= '" + user.getSituation() + "'");
        }
        if (user.getJobexperiences() != null && !"".equals(user.getJobexperiences())) {
            sql.append(" , Jobexperiences= '" + user.getJobexperiences() + "'");
        }
        if (user.getCreateDate() != null) {
            sql.append(" , CREATETIME = ").append(DataBaseSqlConfig.getDateStrFormat(user.getCreateDate()));
        }
        if (user.getWorktime() != null) {
            sql.append(" , worktime = ").append(DataBaseSqlConfig.getDateStrFormat(user.getWorktime()));
        }
        if (user.getBirthday() != null) {
            sql.append(" , Birthday = ").append(DataBaseSqlConfig.getDateStrFormat(user.getBirthday()));
        }
        if (user.getEntrytime() != null) {
            sql.append(" , entrytime = ").append(DataBaseSqlConfig.getDateStrFormat(user.getEntrytime()));
        }

        if (user.getResignationtime() != null) {
            sql.append(" , resignationtime = ").append(DataBaseSqlConfig.getDateStrFormat(user.getResignationtime()));
        }
        if (user.getAuditortype() != null && !"".equals(user.getAuditortype())) {
            sql.append(" , auditortype= '" + user.getAuditortype() + "'");
        }

        if (user.getGender() != null && !"".equals(user.getGender())) {
            sql.append(" , gender= '" + user.getGender() + "'");
        }
        if (user.getType() != null && !"".equals(user.getType())) {
            sql.append(" , TYPE= '" + user.getType() + "'");
        }

        if (user.getHistorycode() != null && !"".equals(user.getHistorycode())) {
            sql.append(" , Historycode= '" + user.getHistorycode() + "'");
        }

        if (user.getPersonType() != null && !"".equals(user.getPersonType())) {
            sql.append(" , persontype= '" + user.getPersonType() + "'");
        }
        if (user.getHistorydepartmentid() != null && !"".equals(user.getHistorydepartmentid())) {
            sql.append(" , Historydepartmentid= '" + user.getHistorydepartmentid() + "'");
        }

        if (user.getManageorgs() != null && !"".equals(user.getManageorgs())) {
            sql.append(" , MANAGEORGS= '" + user.getManageorgs() + "'");
        }
        if (user.getManageorgnames() != null && !"".equals(user.getManageorgnames())) {
            sql.append(" , MANAGEORGNAMES= '" + user.getManageorgnames() + "'");
        }
        if (user.getFgorgs() != null && !"".equals(user.getFgorgs())) {
            sql.append(" , FGORGS= '" + user.getFgorgs() + "'");
        }
        if (user.getFgorgnames() != null && !"".equals(user.getFgorgnames())) {
            sql.append(" , FGORGNAMES= '" + user.getFgorgnames() + "'");
        }
        if (user.getDataSource() != null && !"".equals(user.getDataSource())) {
            sql.append(" , datasource= '" + user.getDataSource() + "'");
        }
        if(StringUtils.isNotBlank(user.getIdCard())) {
        	sql.append(" , IDCARD = '" + user.getIdCard() + "'");
        }
        if(user.getChargeLeaderStaffId() != null) {
        	sql.append(" , CHARGELEADERSTAFFID = '" + user.getChargeLeaderStaffId() + "'");
        }
        if(user.getSecrectLevelId() != null) {
        	sql.append(" , SECRECTLEVELID = '" + user.getSecrectLevelId() + "'");
        }
        return sql;
    }

    public String findPageListBySql(IPage<TblStaff> page,TblStaff staff, String sqlStrs) {
    	 StringBuffer sbSql = new StringBuffer("SELECT s.STAFFID,s.REALNAME,o.ORGNAME FROM TBL_STAFF s LEFT JOIN TBL_ORGANIZATION o ON s.ORGID = o.ORGID WHERE s.STAFFID IN "
    	 		+ " (select STAFFID from TBL_MANAGE_USER_BOOK WHERE BOOKID IN (" + sqlStrs + ")) "
    	 		+ " AND s.ORGID IN ("+staff.getOrgIdStrs()+")");
         if (staff.getRealname() != null) {
             sbSql.append(" AND s.REALNAME LIKE '%" + staff.getRealname() + "%'");
         }
         sbSql.append(" ORDER BY s.STAFFID DESC");
         String sql = sbSql.toString();
         return sql;
    }

    public String findPageCountBySql(PageInfo<TblStaff> pageInfo) {
        StringBuffer sbSql = new StringBuffer("SELECT COUNT(*) FROM TBL_STAFF s LEFT JOIN TBL_ORGANIZATION o ON s.ORGID = o.ORGID WHERE s.STAFFID IN (select STAFFID from TBL_MANAGE_USER_BOOK WHERE BOOKID IN (" + pageInfo.getSqlStr() + ")) AND s.ORGID IN (SELECT ORGID FROM TBL_ORGANIZATION where ORGTYPE = 0 start with ORGID = " + pageInfo.getCondition().getOrgid() + " connect by prior ORGID = FATHERORGID)");
        if (pageInfo.getCondition().getRealname() != null) {
            sbSql.append(" AND s.REALNAME LIKE '%" + pageInfo.getCondition().getRealname() + "%'");
        }
        String sql = sbSql.toString();
        return sql;
    }


    public String findAllPageInfoByacctid(IPage<TblStaff> page, String bookid) {
        StringBuffer sbSql = new StringBuffer("SELECT STA.USERNAME, STA.ADDRESS, STA.REALNAME FROM TBL_STAFF sta LEFT JOIN TBL_ORGANIZATION org ON STA.ORGID = ORG.ORGID WHERE " +
                " sta.STAFFID IN ( select STAFFID FROM TBL_MANAGE_USER_BOOK WHERE BOOKID = '" + bookid + "'");

        sbSql.append(" ) ORDER BY STA.ORGID,STA.STAFFID");
        String sql = sbSql.toString();
        return sql;
    }

    public String findByAll(String pid, IPage<TblStaff> page) {
        StringBuffer sbSql = new StringBuffer("select * from TBL_STAFF TS LEFT JOIN TBL_ORGANIZATION TOR ON TS.ORGID = TOR.ORGID WHERE TS.ORGID = '" + pid + "' AND (TS.STATUS is NULL or TS.STATUS != 0)");

        sbSql.append(" ORDER BY TS.STAFFID DESC ");
        String sql = sbSql.toString();
        return sql;
    }

    public String findByAllORGID(BigDecimal orgid, IPage<TblStaff> page) {
        StringBuffer sbSql = new StringBuffer("SELECT * FROM TBL_STAFF TS LEFT JOIN TBL_ORGANIZATION TOR ON TS.ORGID = TOR.ORGID " +
                "WHERE TOR.FATHERORGID = '" + orgid + "' AND (TS.STATUS IS NULL OR TS.STATUS != 0)");

        sbSql.append(" ORDER BY TS.STAFFID DESC ");
        String sql = sbSql.toString();
        return sql;
    }

    public String findAllPageBeanPid(IPage<TblStaff> page, TblOrganization attribute) throws Exception {
        StringBuffer sbSql = new StringBuffer("");
        if (attribute.getOrgtype() != null && attribute.getOrgtype().toString().equals("0")) {
            sbSql = new StringBuffer("select STAFFID,REALNAME,STA.ORGID,ORG.ORGNAME,STA.ADDRESS,STA.EMAIL,username from tbl_staff sta INNER JOIN TBL_ORGANIZATION org ON STA.ORGID=ORG.ORGID  where STA.ORGID =" + attribute.getOrgid() + " AND (STA.STATUS is NULL or STA.STATUS != 0)");
        } else {
            sbSql = new StringBuffer("select STAFFID,REALNAME,STA.ORGID,ORG.ORGNAME,STA.ADDRESS,STA.EMAIL,username from tbl_staff sta INNER JOIN TBL_ORGANIZATION org ON STA.ORGID=ORG.ORGID  where STA.ORGID in (select ORGID from TBL_ORGANIZATION org where FATHERORGID=" + attribute.getOrgid() + " AND ORGTYPE=0 ) AND (STA.STATUS is NULL or STA.STATUS != 0)");
        }

        sbSql.append(" order by STAFFID desc ");
        return sbSql.toString();
    }

    public String selectStaffListByPageInfo(PageInfo<TblStaff> pageInfo, BigDecimal orgId, TblStaff staff) throws Exception {
        StringBuffer sb = new StringBuffer("SELECT * FROM (SELECT T1.*,ROWNUM RN  FROM (SELECT * FROM tbl_staff   WHERE isaudit='Y' and status='1'  ");
        if (StringUtils.isNotBlank(staff.getRealname())) {
            sb.append(" AND realname like '%" + staff.getRealname() + "%'");
        }
        if (StringUtils.isNotBlank(staff.getMajor())) {
            sb.append(" AND Major like '%" + staff.getMajor() + "%'");
        }

        if (StringUtils.isNotBlank(staff.getEducation())) {
            sb.append(" AND Education like '%" + staff.getEducation() + "%'");
        }

        if (StringUtils.isNotBlank(staff.getJobexperiences())) {
            sb.append(" AND Jobexperiences like '%" + staff.getJobexperiences() + "%'");
        }

        if (StringUtils.isNotBlank(staff.getJobName())) {
            sb.append(" AND jobid in (select jobid from tbl_job where jobname like '%" + staff.getJobName() + "%') ");
        }
        if (orgId != null) {
            sb.append(" AND ORGID IN (SELECT ORGID FROM TBL_ORGANIZATION START WITH ORGID = " + orgId + " CONNECT BY PRIOR ORGID = FATHERORGID AND ORGTYPE = 0)");
        }
        sb.append(" ORDER BY staffid DESC) T1 WHERE ROWNUM <= " + (pageInfo.getCurrentRecord() + pageInfo.getPageSize()) + " ) T2 WHERE T2.RN > " + pageInfo.getCurrentRecord());
        return sb.toString();
    }

    public String selectStaffCountByPageInfo(BigDecimal orgId, TblStaff staff) throws Exception {

        StringBuffer sb = new StringBuffer("SELECT count(*) FROM tbl_staff  WHERE ISAUDIT='Y' ");
        if (orgId != null) {
            sb.append(" AND ORGID IN (SELECT ORGID FROM TBL_ORGANIZATION START WITH ORGID = " + orgId + " CONNECT BY PRIOR ORGID = FATHERORGID AND ORGTYPE = 0)");
        }
        if (StringUtils.isNotBlank(staff.getRealname())) {
            sb.append(" AND realname like '%" + staff.getRealname() + "%'");
        }
        if (StringUtils.isNotBlank(staff.getMajor())) {
            sb.append(" AND Major like '%" + staff.getMajor() + "%'");
        }

        if (StringUtils.isNotBlank(staff.getEducation())) {
            sb.append(" AND Education like '%" + staff.getEducation() + "%'");
        }

        if (StringUtils.isNotBlank(staff.getJobexperiences())) {
            sb.append(" AND Jobexperiences like '%" + staff.getJobexperiences() + "%'");
        }

        if (StringUtils.isNotBlank(staff.getJobName())) {
            sb.append(" AND jobid in (select jobid from tbl_job where jobname like '%" + staff.getJobName() + "%') ");
        }
        return sb.toString();
    }

    public String getAuditorInformationList(String orgid) throws Exception {
        String sql = "  select  " +
                "(select count(*) from tbl_staff where   status='1'  and  orgid ='" + orgid + "') 实有人数, " +
                "(select count(*) from tbl_staff where type='专职' and status='1'  and  orgid ='" + orgid + "') 专职, " +
                "(select count(*) from tbl_staff where qualification is not null and status='1'  and orgid ='" + orgid + "') CIA, " +
                "(select count(*)  from tbl_staff where  education in ('硕士','博士','博士后') and education is not null and status='1'  and  orgid ='" + orgid + "') 硕士及以上, " +
                "(select count(*)  from tbl_staff where  education in ('本科') and education is not null and status='1'  and  orgid ='" + orgid + "') 本科, " +
                " (select count(*)  from tbl_staff where  education in ('大专','高中') and education is not null and  orgid ='" + orgid + "') 大专及以下, " +
                "(select count(*)  from tbl_staff where  title in ('高级') and title is not null and status='1'  and  orgid ='" + orgid + "') 高级职称 , " +
                "(select count(*)  from tbl_staff where  title in ('中级') and title is not null and status='1'  and  orgid ='" + orgid + "') 中级职称 , " +
                "(select count(*)  from tbl_staff where  title in ('初级') and title is not null and status='1'  and  orgid ='" + orgid + "') 初级职称, " +
                "(select count(*)  from tbl_staff where  title in ('无','') and status='1'  and  orgid ='" + orgid + "') 无职称, " +
                "( select  count(*)  from tbl_staff  where   floor("+DataBaseSqlConfig.getWhereNowTimeCompareToTime(DataBaseSqlConfig.TIMETYPEMONTH, "birthday")+"/12 ) >50 and birthday is not null and status='1'  and  orgid ='" + orgid + "') 五十岁以上, " +
                "( select  count(*)  from tbl_staff  where   floor("+DataBaseSqlConfig.getWhereNowTimeCompareToTime(DataBaseSqlConfig.TIMETYPEMONTH, "birthday")+"/12 ) >30 and floor("+DataBaseSqlConfig.getWhereNowTimeCompareToTime(DataBaseSqlConfig.TIMETYPEMONTH, "birthday")+"/12 )<=50 and birthday is not null and status='1'  and  orgid ='" + orgid + "' ) 三十到五十岁, " +
                "( select  count(*)  from tbl_staff  where   floor("+DataBaseSqlConfig.getWhereNowTimeCompareToTime(DataBaseSqlConfig.TIMETYPEMONTH, "birthday")+"/12 ) <=30 and birthday is not null and status='1'  and  orgid ='" + orgid + "') 三十岁以下, " +
                "( select  count(*)  from tbl_staff  where   major='审计' and  major  is not null and status='1'  and   orgid ='" + orgid + "') 审计, " +
                " ( select  count(*)  from tbl_staff  where   major='会计' and  major is not null and status='1'  and   orgid ='" + orgid + "') 会计, " +
                " ( select  count(*)  from tbl_staff  where   major='经济' and  major is not null and status='1'  and   orgid ='" + orgid + "') 经济, " +
                "( select  count(*)  from tbl_staff  where   major='法律' and  major is not null and status='1' and    orgid ='" + orgid + "') 法律, " +
                "( select  count(*)  from tbl_staff  where   major='管理' and  major is not null and status='1' and    orgid ='" + orgid + "') 管理, " +
                "( select  count(*)  from tbl_staff  where   major='信息技术' and  major is not null and status='1'  and  orgid ='" + orgid + "') 信息技术, " +
                " ( select  count(*)  from tbl_staff  where   major='工程' and  major is not null and status='1'  and  orgid ='" + orgid + "') 工程, " +
                "( select  count(*)  from tbl_staff  where   major='其他' and  major is not null   and status='1'  and  orgid ='" + orgid + "') 其他 " +
                " from   dual";
        return sql;
    }

    public String insertAttInfoForStaff(BigDecimal id, String attid) {
        StringBuffer column = new StringBuffer("INSERT INTO TBL_TRAININGSTAFF_ATT(STAFFID");
        StringBuffer value = new StringBuffer(" VALUES ('" + id + "'");
        if (attid != "") {
            column.append(",ATTID");
            value.append(",'" + attid + "'");
        }
        column.append(")");
        value.append(")");
        String sql = column.toString() + value.toString();
        return sql;
    }

    public String insertAttInfoForTrain(BigDecimal id, String attid) {
        StringBuffer column = new StringBuffer("INSERT INTO TBL_TRAINING_ATT(TRAINID");
        StringBuffer value = new StringBuffer(" VALUES ('" + id + "'");
        if (attid != "") {
            column.append(",ATTID");
            value.append(",'" + attid + "'");
        }
        column.append(")");
        value.append(")");
        String sql = column.toString() + value.toString();
        return sql;
    }


    public String selectListOrgInId(String sysorgid) {
        String lasts = sysorgid.substring(sysorgid.length() - 1);
        if (",".equals(lasts)) {
            sysorgid = sysorgid.substring(0, sysorgid.length() - 1);
        }

        StringBuffer sb = new StringBuffer("SELECT ORGNAME "
                + "FROM TBL_ORGANIZATION TNA "
                + "WHERE TNA.ORGID IN (" + sysorgid + ") ");

        return sb.toString();
    }

    public String selectListRoleInId(String roleids) {
        String lasts = roleids.substring(roleids.length() - 1);
        if (",".equals(lasts)) {
            roleids = roleids.substring(0, roleids.length() - 1);
        }

        StringBuffer sb = new StringBuffer("SELECT RNAME,PKYMROLEID "
                + "FROM TBL_ROLE TNA "
                + "WHERE TNA.RID IN (" + roleids + ") ");

        return sb.toString();
    }


    //
    public String selectListBmfzrByPageInfo(IPage<TblStaff> page, BigDecimal roleid) throws Exception {
        StringBuffer sbSql = new StringBuffer("select TS.*,TORG.ORGNAME from TBL_STAFF TS "
                + "LEFT JOIN TBL_ORGANIZATION TORG ON TORG.ORGID = TS.ORGID "
                + "WHERE ").append(DataBaseSqlConfig.getWhereColumnInStr("TS.ROLEIDSTRS", roleid.toString(), ","));
        
        sbSql.append(" order by TS.STATUS desc,TS.STAFFID desc ");
        String sql = sbSql.toString();
        return sql;
    }

    //
    public String selectListFgldByPageInfo(IPage<TblStaff> page, Integer orgid) {
        StringBuffer sbSql = new StringBuffer(" select TS.*,TORG.ORGNAME from TBL_STAFF TS "
                + " LEFT JOIN TBL_ORGANIZATION TORG ON TORG.ORGID = TS.ORGID "
                + " WHERE TS.FGORGS IS NOT NULL AND TORG.FATHERORGID=" + orgid);
        sbSql.append(" order by TS.STATUS desc,TS.STAFFID desc");
        String sql = sbSql.toString();
        return sql;
    }

    public String findAllLeader(PageInfo<TblStaff> pageInfo, String orgid) {
        StringBuffer sql = new StringBuffer("select * from (SELECT BUDGET.*,ROWNUM RNUM FROM (select * from tbl_staff where orgid in (select orgid "
                + "from tbl_organization where orgname='公司领导' ");
        if (orgid != null && orgid.length() > 0) {
            sql.append(" and fatherorgid='" + orgid + "'");
        }
        sql.append(")) BUDGET WHERE ROWNUM <= " + (pageInfo.getCurrentPage() * pageInfo.getPageSize()) + " ) WHERE RNUM > " + pageInfo.getCurrentRecord());
        return sql.toString();
    }

    public String findAllLeaderCount(String orgid) {
        StringBuffer sql = new StringBuffer("select count(*) from tbl_staff where orgid in (select orgid "
                + "from tbl_organization where orgname='公司领导' ");
        if (orgid != null && orgid.length() > 0) {
            sql.append(" and fatherorgid='" + orgid + "'");
        }
        sql.append(")");
        return sql.toString();
    }

    public String selectAllListByroleid(String  roleid,String username,String realname,IPage<TblStaff> page) throws Exception {
		String sql = "SELECT STA.*,ORG.ORGNAME FROM TBL_STAFF  sta LEFT JOIN TBL_ORGANIZATION org on STA.ORGID=ORG.ORGID where 1 = 1 ";
		
		if(StringUtils.isNotBlank(roleid)) {
			sql += " AND "+DataBaseSqlConfig.getWhereColumnInStr("ROLEIDSTRS", roleid, ",");
		}
		
		if (username != null && username.length()>0) {
			sql += " AND STA.USERNAME = '" + username+"'";
		}
		if (realname != null && realname.length()>0) {
			sql += " AND STA.REALNAME like '%" + realname+"%'";
		}
		sql+=" ORDER BY sta.orgid,sta.staffid ";
		return sql;
	}
	
	public String findByStaffManOrgs(String orgid){
    	String sql="SELECT * FROM TBL_STAFF WHERE MANAGEORGS like '%"+orgid+"%'";
    	return sql;
    }
	
	public String selectYmPkStaffIdByRoleId(String roleId) throws Exception {
		String sql = "SELECT PKYMSTAFFID FROM TBL_STAFF WHERE "+DataBaseSqlConfig.getWhereColumnInStr("ROLEIDSTRS", roleId, ",");
		return sql;
	}
    
	
	
	 public String selectStaffListByPageInfocs(PageInfo<TblStaff> pageInfo, String orgId, TblStaff staff) throws Exception {
	        StringBuffer sb = new StringBuffer("SELECT * FROM tbl_staff   WHERE isaudit='Y' and status='1'  ");
	        if (StringUtils.isNotBlank(staff.getRealname())) {
	            sb.append(" AND realname like '%" + staff.getRealname() + "%'");
	        }
	        if (StringUtils.isNotBlank(staff.getMajor())) {
	            sb.append(" AND Major like '%" + staff.getMajor() + "%'");
	        }

	        if (StringUtils.isNotBlank(staff.getEducation())) {
	            sb.append(" AND Education like '%" + staff.getEducation() + "%'");
	        }

	        if (StringUtils.isNotBlank(staff.getJobexperiences())) { 
	            sb.append(" AND Jobexperiences like '%" + staff.getJobexperiences() + "%'");
	        }

	        if (StringUtils.isNotBlank(staff.getJobName())) {
	            sb.append(" AND jobid in (select jobid from tbl_job where jobname like '%" + staff.getJobName() + "%') ");
	        }
	        if (orgId != null) {
	            sb.append(" AND ORGID IN (" + orgId + " )");
	        }
	        sb.append(" ORDER BY staffid DESC");
	        return sb.toString();
	    }

	public String findByRoleName(String roleId){
		String sql="SELECT * FROM TBL_STAFF WHERE ','||ROLEIDSTRS||',' like '%,"+roleId+",%' AND ROWNUM = 1";
		return sql;
	}
	
}
