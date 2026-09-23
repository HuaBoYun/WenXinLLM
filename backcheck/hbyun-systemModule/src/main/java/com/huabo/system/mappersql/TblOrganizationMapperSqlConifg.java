package com.huabo.system.mappersql;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hbfk.util.DateUtil;
import com.hbfk.util.PageInfo;
import com.hbfk.util.database.DataBaseSqlConfig;
import com.hbfk.util.redis.Random.RandomUtil;
import com.huabo.system.entity.Find;
import com.huabo.system.entity.TblOrganization;
import com.huabo.system.entity.TblOrganizationInfo;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.commons.lang.StringUtils;

public class TblOrganizationMapperSqlConifg {
	
	public String insertSystemProjectAuth(BigDecimal orgid, BigDecimal staffid, BigDecimal projectId, BigDecimal id) throws Exception{
		String sql = "INSERT INTO TBL_SYSTEM_PROJECT_AUTH(ID, AUTHORGID, PROJECTID, STATE, CREATOR, WORKUNIT, BELONGGROUP, CREATEDTIME, UPDATEDTIME) VALUES("
				+id+","+orgid+","+projectId+",1,"+staffid+","+orgid+","+orgid+","+DataBaseSqlConfig.getDateStrFormat(new Date())+","+DataBaseSqlConfig.getDateStrFormat(new Date())+")";
		return sql;
	}
	
	public String findAllorganization() throws Exception {
		String sql = "SELECT * FROM TBL_ORGANIZATION WHERE FATHERORGID = ( SELECT orgid FROM TBL_ORGANIZATION WHERE ORGNAME = '行业' AND ORGTYPE = 100 ) AND " + DataBaseSqlConfig.getRowLimitSql(0, 1);
		return sql;
	}
	
	public String findByWPZJK(BigDecimal orgid, String wpzjk) throws Exception{
		String sql = "select * from TBL_ORGANIZATION where FATHERORGID = #{orgid} and ISZY = #{wpzjk} and "+DataBaseSqlConfig.getRowLimitSql(0, 1);
		sql += " ORDER BY orderid ASC";
		return sql;
	}
	
	public String selectGrantDataRightDeptListByCompanyId(IPage<TblOrganization> page, String deptIds,
			String deptName, String deptNumber, BigDecimal roleId, BigDecimal pid) throws Exception {
		StringBuffer sb = new StringBuffer("SELECT ORG.ORGID,ORG.ORGNUMBER,ORG.ORGNAME,ORG.ORGTYPE,(SELECT COUNT(0) FROM TBL_SYSTEM_DATA_RIGHT TSDR WHERE TSDR.ORGID = ")
				.append(pid).append(" AND TSDR.ROLEID = ").append(roleId).append(" AND ").append(DataBaseSqlConfig.getWhereColumnInCol("TSDR.DEPTIDSTRS", "ORG.ORGID", ","))
				.append(") AS ISCHECKED FROM TBL_ORGANIZATION ORG WHERE ORGID = ").append(pid).append(" UNION ALL SELECT ORG.ORGID,ORG.ORGNUMBER,ORG.ORGNAME,ORG.ORGTYPE,(SELECT COUNT(0) FROM TBL_SYSTEM_DATA_RIGHT TSDR WHERE TSDR.ORGID = ")
				.append(pid).append(" AND TSDR.ROLEID = ").append(roleId).append(" AND ").append(DataBaseSqlConfig.getWhereColumnInCol("TSDR.DEPTIDSTRS", "ORG.ORGID", ","))
				.append(") AS ISCHECKED FROM TBL_ORGANIZATION ORG WHERE ORG.ORGTYPE = 0 AND ORG.STATUS = 0 ");
		if(StringUtils.isNotBlank(deptNumber)) {
			sb.append(" AND ORG.ORGNUMBER LIKE '%").append(deptNumber).append("%'");
		}
		if(StringUtils.isNotBlank(deptName)) {
			sb.append(" AND ORG.ORGNAME LIKE '%").append(deptName).append("%'");
		}
		sb.append(" AND ORG.FATHERORGID IN (").append(deptIds).append(") ORDER BY ORGID ASC");
		String sql = sb.toString();
		System.out.println(sql);
		return sql;
	}
	
	public String selectGrantDataRightDeptCountByCompanyId(PageInfo<TblOrganization> pageInfo, BigDecimal pid,
			String deptName, String deptNumber, Integer roleId) {
		String sql = "SELECT COUNT(0) FROM TBL_ORGANIZATION ORG WHERE 1 = 1 ";
		if(StringUtils.isNotBlank(deptNumber)) {
			sql += " AND ORG.ORGNUMBER LIKE '%"+deptNumber+"%'";
		}
		if(StringUtils.isNotBlank(deptName)) {
			sql += " AND ORG.ORGNAME LIKE '%"+deptName+"%'";
		}
		sql += "START WITH ORG.FATHERORGID = "+pid+" AND ORG.ORGTYPE = 0 CONNECT BY PRIOR ORG.ORGID = ORG.FATHERORGID";
		return sql;
	}
	
	public String selectDeptInfoByGranDataList(IPage<TblOrganization> page , String deptName,String companyName, BigDecimal roleId) throws Exception {
		String sql = "SELECT ORG.ORGID,ORG.ORGNAME,COM.ORGNAME AS FATHERORGNAME,COM.ORGID AS FATHERORGID FROM TBL_ORGANIZATION ORG LEFT JOIN TBL_SYSTEM_DATA_RIGHT TSDR ON "
				+ DataBaseSqlConfig.getWhereColumnInCol("TSDR.DEPTIDSTRS", "ORG.ORGID", ",")
				+ " LEFT JOIN TBL_ORGANIZATION COM ON TSDR.ORGID = COM.ORGID WHERE TSDR.ROLEID = "+roleId;
		
		if(StringUtils.isNotBlank(deptName)) {
			sql += "  AND ORG.ORGNAME LIKE '%"+deptName+"%'";
		}
		if(StringUtils.isNotBlank(companyName)) {
			sql += "  AND COM.ORGNAME LIKE '%"+companyName+"%'";
		}
		sql += " ORDER BY COM.ORGID,ORG.ORGID ASC";
		return sql;
	}
	
	public String selectRepeatNumber(String orgnumber, String uniqueNumber, BigDecimal orgId){
		String sql = "SELECT COUNT(0) FROM TBL_ORGANIZATION WHERE ORGNUMBER = '"+orgnumber+"' AND UNIQUENUMBER = '"+uniqueNumber+"'";
		if(orgId != null) {
			sql += " AND ORGID != " + orgId;
		}
		return sql;
	}
	
	public String selectRepeatName(String orgname, String uniqueNumber, BigDecimal orgId){
		String sql = "SELECT COUNT(0) FROM TBL_ORGANIZATION WHERE ORGNAME = '"+orgname+"' AND UNIQUENUMBER = '"+uniqueNumber+"'";
		if(orgId != null) {
			sql += " AND ORGID != " + orgId;
		}
		return sql;
	}
	
	public String selectListByPageInfo(IPage<TblOrganization> page) {
		StringBuffer sqlSb = new StringBuffer("SELECT * FROM TBL_ORGANIZATION where ORGTYPE = 100 ");
		sqlSb.append(" ORDER BY ORGID desc ");
		String sql = sqlSb.toString();
		return sql;
	}

	public String selectListByPageInOrgid(PageInfo<TblOrganization> pageInfo,BigDecimal orgid) {
		StringBuffer sbSql = new StringBuffer("SELECT * FROM TBL_ORGANIZATION");
		sbSql.append(" WHERE orgid= "+orgid+"");
		sbSql.append(" T1 WHERE ROWNUM <= "+(pageInfo.getCurrentPage()*pageInfo.getPageSize())+" ) T2 WHERE T2.RNUM > "+pageInfo.getCurrentRecord());
		return sbSql.toString();

	}

	public String findByPageBean(IPage<TblOrganization> page, String orgIdStrs,Find find) throws Exception {
		StringBuffer sbSql = new StringBuffer("select ORGID,ORGNAME,FATHERORGID,ORGNUMBER,ORGMENO,MEMO,ICODE,ORGTYPE,AUDITTYPE,STATUS,ISZY,HYZSKTYPE,ORDERID,OUTSIDEID,OUTSIDEOPENDID,ISAUTONUMBER,ISINITIALIZATION,DUTIES,INDUSTRYID,BYWX,DATASOURCE,HISTORYCODE,HISTORYDEPARTMENTID,WRITTENBYDEPT,PKYMORGID,PRINCIPALCODE,CHARGELEADERCODE,PRINCIPALSTAFFID,CHARGELEADERSTAFFID,USESECRECT,BGIMAGE,LOGOIMAGE,JDZTIMAGE,CTZTIMAGE,BGNAME,LOGONAME,JDZTNAME,CTZTNAME,BANAME from TBL_ORGANIZATION where ORGTYPE != 0 ");
		if (find != null && find.getCode() != null && find.getCode().length() > 0) {
			sbSql.append(" and ORGNUMBER like '%" + find.getCode() + "%' ");
		}
		if (find != null && find.getName() != null && find.getName().length() > 0) {
			sbSql.append(" and ORGNAME  like '%" + find.getName() + "%' ");
		}
		if (find != null && find.getStatus() != null && find.getStatus().length() > 0) {
			sbSql.append(" and STATUS =" + find.getStatus());
		}
		sbSql.append(" AND " + DataBaseSqlConfig.getWhereColumnInStr("ORGANIZATIONTREES", orgIdStrs, ",") + " ORDER BY ORGID asc ");
		String sql = sbSql.toString();
		return sql;
	}
	
	public String selectListByPageInfoOrgid(Find find,BigDecimal pid, String orgIdStrs) throws Exception {
		StringBuffer sbSql = new StringBuffer("select ORGID,ORGNAME,FATHERORGID,ORGNUMBER,ORGMENO,MEMO,ICODE,ORGTYPE,AUDITTYPE,STATUS,ISZY,HYZSKTYPE,ORDERID,OUTSIDEID,OUTSIDEOPENDID,ISAUTONUMBER,ISINITIALIZATION,DUTIES,INDUSTRYID,BYWX,DATASOURCE,HISTORYCODE,HISTORYDEPARTMENTID,WRITTENBYDEPT,PKYMORGID,PRINCIPALCODE,CHARGELEADERCODE,PRINCIPALSTAFFID,CHARGELEADERSTAFFID from TBL_ORGANIZATION where ")
				.append(DataBaseSqlConfig.getWhereColumnInStr("ORGANIZATIONTREES", orgIdStrs, ","));
		if (find != null && find.getCode() != null && find.getCode().length() > 0) {
			sbSql.append(" and ORGNUMBER like '%" + find.getCode() + "%' ");
		}
		if (find != null && find.getName() != null && find.getName().length() > 0) {
			sbSql.append(" and ORGNAME  like '%" + find.getName() + "%' ");
		}
		sbSql.append(" ORDER BY ORGID desc,orderid ASC ");
		String sql = sbSql.toString();
		return sql;
	}

	public String findAllCommpanyPageBeanStaffid(IPage<TblOrganization> page, String orgIdStrs, String orgname, String orgnumber) throws Exception{
		StringBuffer sbSql = new StringBuffer("select ORGID,ORGNAME,FATHERORGID,ORGNUMBER,ORGMENO,MEMO,ICODE,ORGTYPE,AUDITTYPE,STATUS,ISZY,HYZSKTYPE,ORDERID,OUTSIDEID,OUTSIDEOPENDID,ISAUTONUMBER,ISINITIALIZATION,DUTIES,INDUSTRYID,BYWX,DATASOURCE,HISTORYCODE,HISTORYDEPARTMENTID,WRITTENBYDEPT,PKYMORGID,PRINCIPALCODE,CHARGELEADERCODE,PRINCIPALSTAFFID,CHARGELEADERSTAFFID,(SELECT COUNT(0) FROM TBL_SYSTEM_ORG_RIGHT WHERE ORGID = org.ORGID) AS ISCHECKED "
				+ " from TBL_ORGANIZATION org where ORGTYPE > 0 AND ORGTYPE < 100 AND ").append(DataBaseSqlConfig.getWhereColumnInStr("ORGANIZATIONTREES", orgIdStrs, ",") );
		
		if(StringUtils.isNotBlank(orgname)) {
			sbSql.append(" AND ORGNAME LIKE '%").append(orgname).append("%'");
		}
		if(StringUtils.isNotBlank(orgnumber)) {
			sbSql.append(" AND ORGNUMBER LIKE '%").append(orgnumber).append("%'");
		}
		
		sbSql.append(" ORDER BY orgid ASC");
		return sbSql.toString();
	}


	public String selectListByPageInfoo(PageInfo<TblOrganization> pageInfo){
		StringBuffer sbSql = new StringBuffer("SELECT * FROM TBL_ORGANIZATION");
		sbSql.append(" T1 WHERE ROWNUM <= "+(pageInfo.getCurrentPage()*pageInfo.getPageSize())+" ) T2 WHERE T2.RNUM > "+pageInfo.getCurrentRecord());
		return sbSql.toString();
	}

	public String findAllCommpanyPageBeanGSXj(PageInfo<TblOrganization> pageInfo, Find find, String orgId){
		StringBuffer sbSql = new StringBuffer("SELECT ORGID,ORGNAME,ORGNUMBER,ORGMENO,MEMO,ORGTYPE,STATUS,ISZY FROM ( SELECT ORGID,ORGNAME,ORGNUMBER,ORGMENO,MEMO,ORGTYPE,STATUS,ISZY,ROWNUM RN FROM ( select ORGID,ORGNAME,ORGNUMBER,ORGMENO,MEMO,ORGTYPE,STATUS,ISZY from TBL_ORGANIZATION org where ORGTYPE != 0 START WITH ORG.FATHERORGID =");
		if (find != null && find.getCode() != null && find.getCode().length() > 0) {
			sbSql.append(" and ORGNUMBER like '%" + find.getCode() + "%' ");
		}
		if (find != null && find.getName() != null && find.getName().length() > 0) {
			sbSql.append( " and ORGNAME  like '%" + find.getName() + "%' ");
		}

		sbSql.append(orgId+"  CONNECT BY PRIOR ORG.ORGID = ORG.FATHERORGID ORDER BY orgtype asc,ORGID desc,rownum ASC ) T1 WHERE ROWNUM <= "+(pageInfo.getCurrentPage()*pageInfo.getPageSize())+" ) T2 WHERE T2.RN > "+pageInfo.getCurrentRecord());
		return sbSql.toString();

	}




	public String findAllCommpanyPage(PageInfo<TblOrganization> pageInfo, BigDecimal pid){
		StringBuffer sbSql = new StringBuffer("SELECT ORGID,ORGNAME,FATHERORGID,ORGNUMBER,ORGMENO,MEMO,ICODE,ORGTYPE,AUDITTYPE,STATUS,ISZY,HYZSKTYPE,ORDERID,OUTSIDEID,OUTSIDEOPENDID,ISAUTONUMBER,ORGCREATE,ISINITIALIZATION,DUTIES,INDUSTRYID,BYWX,DATASOURCE,HISTORYCODE,HISTORYDEPARTMENTID FROM(");
		sbSql.append("SELECT T1.ORGID,T1.ORGNAME,T1.FATHERORGID,T1.ORGNUMBER,T1.ORGMENO,T1.MEMO,T1.ICODE,T1.ORGTYPE,T1.AUDITTYPE,T1.STATUS,T1.ISZY,T1.HYZSKTYPE,T1.ORDERID,T1.OUTSIDEID,T1.OUTSIDEOPENDID,T1.ISAUTONUMBER,T1.ORGCREATE,T1.ISINITIALIZATION,T1.DUTIES,T1.INDUSTRYID,T1.BYWX,T1.DATASOURCE,T1.HISTORYCODE,T1.HISTORYDEPARTMENTID FROM(");
		sbSql.append("SELECT T1.ORGID,T1.ORGNAME,T1.FATHERORGID,T1.ORGNUMBER,T1.ORGMENO,T1.MEMO,T1.ICODE,T1.ORGTYPE,T1.AUDITTYPE,T1.STATUS,T1.ISZY,T1.HYZSKTYPE,T1.ORDERID,T1.OUTSIDEID,T1.OUTSIDEOPENDID,T1.ISAUTONUMBER,T1.ORGCREATE,T1.ISINITIALIZATION,T1.DUTIES,T1.INDUSTRYID,T1.BYWX,T1.DATASOURCE,T1.HISTORYCODE,T1.HISTORYDEPARTMENTID FROM(");
		sbSql.append("select count(*) from TBL_ORGANIZATION org");
		sbSql.append("where ORGTYPE != 0  and ORGTYPE<100 and status=0  and ORG.FATHERORGID = "+pid+"   ORDER BY orgtype,orderid ASC");
		sbSql.append(") T1 WHERE ROWNUM <= "+(pageInfo.getCurrentPage()*pageInfo.getPageSize())+" ) T2 WHERE T2.RNUM > "+pageInfo.getCurrentRecord());
		return sbSql.toString();
	}

	public String selectRepearBudgetName(String budgetname, BigDecimal orgid, String budgetId) {
		String sql = "SELECT COUNT(0) FROM TBL_CYHW_PROJECTBUDGET WHERE BUDGETNAME ='"+budgetname+"' AND ORGID = '"+orgid+"'";
		if(budgetId != null) {
			sql += " AND BUDGETID != "+budgetId;
		}
		return sql;
	}

	public String selectStaffInfoByModuleIdList(IPage<TblOrganization> page, Integer moduleId, TblOrganization tblOrganization) throws Exception {
		StringBuffer sqlSb = new StringBuffer("SELECT ORGID,ORGNUMBER,ORGNAME FROM TBL_ORGANIZATION WHERE ORGID IN (SELECT ORGID FROM TBL_SYSTEM_MODELORG WHERE MODELID = "+moduleId);
		if(tblOrganization.getOrgname() != null && !"".equals(tblOrganization.getOrgname())) {
			sqlSb.append(" AND ORGNAME LIKE '%"+tblOrganization.getOrgname()+"%'");
		}
		sqlSb.append(")");
		String sql = sqlSb.toString();
		return sql;
	}

	public String selectListByPid(IPage<TblOrganization> page,String pid) {
		StringBuffer sqlSb = new StringBuffer("SELECT * FROM TBL_ORGANIZATION where ORGTYPE = 100 and FATHERORGID= "+pid);
		sqlSb.append(" ORDER BY ORGID desc ");
		String sql = sqlSb.toString();
		return sql;
	}

	public String selectListByStaffid(IPage<TblOrganization> page,String pid) {
		StringBuffer sbSql = new StringBuffer("SELECT * FROM TBL_ORGANIZATION where ORGTYPE = 100 and FATHERORGID= "+pid+"  ORDER BY ORGID desc");
		return sbSql.toString();
	}

	public String getNodesa(IPage<TblOrganization> page) {
		TblOrganization organization = new TblOrganization();
		StringBuffer sbSql = new StringBuffer("select * from TBL_ORGANIZATION order by orderid asc");
		return sbSql.toString();
	}

	public String getNodesaNodeId(IPage<TblOrganization> page,BigDecimal nodeId) {
		StringBuffer sbSql = new StringBuffer("select * from tbl_organization where ORGID = "+nodeId+"  and orgtype < 100 ORDER BY orderid ASC");
		return sbSql.toString();
	}

	public String saveModiOrganization(TblOrganization organization) throws Exception {
		StringBuffer sql = new StringBuffer("UPDATE TBL_ORGANIZATION SET ORGNUMBER = '"+organization.getOrgnumber()+"' ");
		if(organization.getOrgname() != null && !"".equals(organization.getOrgname())) {
			sql.append(" , ORGNAME = '"+organization.getOrgname()+"'");
		}
		if(organization.getFatherorgid() != null) {
			sql.append(" , FATHERORGID = '"+organization.getFatherorgid()+"'");
		}
//		if(organization.getOrgnumber() != null && !"".equals(organization.getOrgnumber())) {
//			sql.append(" , ORGNUMBER = '"+organization.getOrgnumber()+"'");
//		}
		if(organization.getOrgmeno() != null && !"".equals(organization.getOrgmeno())) {
			sql.append(" , ORGMENO = '"+organization.getOrgmeno()+"'");
		}
		if(organization.getMemo() != null && !"".equals(organization.getMemo())) {
			sql.append(" , MEMO = '"+organization.getMemo()+"'");
		}
		if(organization.getIcode() != null && !"".equals(organization.getIcode())) {
			sql.append(" , icode = '"+organization.getIcode()+"'");
		}
		if(organization.getOrgtype() != null) {
			sql.append(" , ORGTYPE = '"+organization.getOrgtype()+"'");
		}
		if(organization.getAuditType() != null) {
			sql.append(" , AUDITTYPE = '"+organization.getAuditType()+"'");
		}
		if(organization.getStatus() != null) {
			sql.append(" , STATUS = '"+organization.getStatus()+"'");
		}
		if(organization.getIszy() != null && !"".equals(organization.getIszy())) {
			sql.append(" , ISZY = '"+organization.getIszy()+"'");
		}
		if(organization.getHyzsktype() != null && !"".equals(organization.getHyzsktype())) {
			sql.append(" , HYZSKTYPE = '"+organization.getHyzsktype()+"'");
		}
		if(organization.getOrderid() != null) {
			sql.append(" , ORDERID = '"+organization.getOrderid()+"'");
		}
		if(organization.getOutsideid() != null) {
			sql.append(" , OUTSIDEID = '"+organization.getOutsideid()+"'");
		}
		if(organization.getOutsideopendid() != null) {
			sql.append(" , OUTSIDEOPENDID = '"+organization.getOutsideopendid()+"'");
		}
		if(organization.getIsautonumber() != null) {
			sql.append(" , ISAUTONUMBER = '"+organization.getIsautonumber()+"'");
		}
		if(organization.getOrgcreate() != null) {
			sql.append(" , ORGCREATE = "+DataBaseSqlConfig.getDateHmsStrFormat(organization.getOrgcreate()));
		}
		if(organization.getIsinitialization() != null) {
			sql.append(" , ISINITIALIZATION = '"+organization.getIsinitialization()+"'");
		}
		if(organization.getDuties() != null && !"".equals(organization.getDuties())) {
			sql.append(" , DUTIES = '"+organization.getDuties()+"'");
		}
		if(organization.getIndustryid() != null) {
			sql.append(" , INDUSTRYID = '"+organization.getIndustryid()+"'");
		}
		if(organization.getBywx() != null && !"".equals(organization.getBywx())) {
			sql.append(" , BYWX = '"+organization.getBywx()+"'");
		}
		if(organization.getDatasource() != null && !"".equals(organization.getDatasource())) {
			sql.append(" , DATASOURCE = '"+organization.getDatasource()+"'");
		}
		if(organization.getHistorycode() != null && !"".equals(organization.getHistorycode())) {
			sql.append(" , HISTORYCODE = '"+organization.getHistorycode()+"'");
		}
		if(organization.getHistorydepartmentid() != null && !"".equals(organization.getHistorydepartmentid())) {
			sql.append(" , HISTORYDEPARTMENTID = '"+organization.getHistorydepartmentid()+"'");
		}
		if(organization.getPrincipalStaffId() != null) {
			sql.append(" , PRINCIPALSTAFFID = '"+organization.getPrincipalStaffId() +"'");
		}
		if(organization.getChargeLeaderCode() != null) {
			sql.append(" , CHARGELEADERCODE = '"+organization.getChargeLeaderCode() +"'");
		}
		if(organization.getUseSecrect() != null) {
			sql.append(" , USESECRECT = '"+organization.getUseSecrect() +"'");
		}
		if(organization.getJtorgid() != null) {
			sql.append(" , JTORGID = '"+organization.getJtorgid() +"'");
		}
		
		if(organization.getJtorgname() != null && !"".equals(organization.getJtorgname())) {
			sql.append(" , JTORGNAME = '"+organization.getJtorgname()+"'");
		}
		
		
		if(organization.getBgimage() != null && !"".equals(organization.getBgimage())) {
			sql.append(" , BGIMAGE = '"+organization.getBgimage()+"'");
		}
		if(organization.getBgname() != null && !"".equals(organization.getBgname())) {
			sql.append(" , BGNAME = '"+organization.getBgname()+"'");
		}
		if(organization.getLogoimage() != null && !"".equals(organization.getLogoimage())) {
			sql.append(" , LOGOIMAGE = '"+organization.getLogoimage()+"'");
		}
		if(organization.getLogoname() != null && !"".equals(organization.getLogoname())) {
			sql.append(" , LOGONAME = '"+organization.getLogoname()+"'");
		}
		if(organization.getJdztimage() != null && !"".equals(organization.getJdztimage())) {
			sql.append(" , JDZTIMAGE = '"+organization.getJdztimage()+"'");
		}
		if(organization.getJdztname() != null && !"".equals(organization.getJdztname())) {
			sql.append(" , JDZTNAME = '"+organization.getJdztname()+"'");
		}
		
		if(organization.getCtztimage() != null && !"".equals(organization.getCtztimage())) {
			sql.append(" , CTZTIMAGE = '"+organization.getCtztimage()+"'");
		}
		if(organization.getCtztname()!= null && !"".equals(organization.getCtztname())) {
			sql.append(" , CTZTNAME = '"+organization.getCtztname()+"'");
		}
		if(organization.getBaname()!= null && !"".equals(organization.getBaname())) {
			sql.append(" , BANAME = '"+organization.getBaname()+"'");
		}
		sql.append(" WHERE ORGID = '"+organization.getOrgid()+"'");
		return sql.toString();
	}

	public String saveAtionHangYe(TblOrganization organization) throws Exception {
		StringBuffer column = new StringBuffer("INSERT INTO TBL_ORGANIZATION (ORGID");
		StringBuffer value = new StringBuffer(" VALUES ("+RandomUtil.uuBigDecimalId());

		if(organization.getOrgname() != null) {
			column.append(",ORGNAME");
			value.append(",'"+organization.getOrgname()+"'");
		}
		if(organization.getFatherorgid() != null) {
			column.append(",FATHERORGID");
			value.append(",'"+organization.getFatherorgid()+"'");
		}
		if(organization.getOrgnumber() != null) {
			column.append(",ORGNUMBER");
			value.append(",'"+organization.getOrgnumber()+"'");
		}
		if(organization.getOrgmeno() != null) {
			column.append(",ORGMENO");
			value.append(",'"+organization.getOrgmeno()+"'");
		}
		if(organization.getMemo() != null) {
			column.append(",MEMO");
			value.append(",'"+organization.getMemo()+"'");
		}
		if(organization.getIcode() != null) {
			column.append(",ICODE");
			value.append(",'"+organization.getIcode()+"'");
		}
		if(organization.getOrgtype()!= null) {
			column.append(",ORGTYPE");
			value.append(",'"+organization.getOrgtype()+"'");
		}
		if(organization.getAuditType() != null) {
			column.append(",AUDITTYPE");
			value.append(",'"+organization.getAuditType()+"'");
		}
		if(organization.getStatus()!= null) {
			column.append(",STATUS");
			value.append(",'"+organization.getStatus()+"'");
		}
		if(organization.getIszy() != null) {
			column.append(",ISZY");
			value.append(",'"+organization.getIszy()+"'");
		}
		if(organization.getHyzsktype() != null) {
			column.append(",HYZSKTYPE");
			value.append(",'"+organization.getHyzsktype()+"'");
		}
		if(organization.getOrderid() != null) {
			column.append(",ORDERID");
			value.append(",'"+organization.getOrderid()+"'");
		}
		if(organization.getOutsideid() != null) {
			column.append(",OUTSIDEID");
			value.append(",'"+organization.getOutsideid()+"'");
		}
		if(organization.getOutsideopendid() != null) {
			column.append(",OUTSIDEOPENDID");
			value.append(",'"+organization.getOutsideopendid()+"'");
		}
		if(organization.getIsautonumber() != null) {
			column.append(",ISAUTONUMBER");
			value.append(",'"+organization.getIsautonumber()+"'");
		}
		if(organization.getOrgcreate() != null) {
			column.append(",ORGCREATE");
			value.append(",'"+organization.getOrgcreate()+"'");
		}
		if(organization.getIsinitialization() != null) {
			column.append(",ISINITIALIZATION");
			value.append(",'"+organization.getIsinitialization() +"'");
		}
		if(organization.getDuties() != null) {
			column.append(",DUTIES");
			value.append(",'"+organization.getDuties()+"'");
		}
		if(organization.getIndustryid() != null) {
			column.append(",INDUSTRYID");
			value.append(",'"+organization.getIndustryid() +"'");
		}
		if(organization.getBywx() != null) {
			column.append(",BYWX");
			value.append(",'"+organization.getBywx() +"'");
		}
		if(organization.getDatasource() != null) {
			column.append(",DATASOURCE");
			value.append(",'"+organization.getDatasource() +"'");
		}
		if(organization.getHistorycode() != null) {
			column.append(",HISTORYCODE");
			value.append(",'"+organization.getHistorycode() +"'");
		}
		if(organization.getHistorydepartmentid() != null) {
			column.append(",HISTORYDEPARTMENTID");
			value.append(",'"+organization.getHistorydepartmentid() +"'");
		}
		column.append(")");
		value.append(")");
		String sql = column.toString()+value.toString();
		return sql;
	}

	public String addReturnId(TblOrganization organization) throws Exception {
		StringBuffer column = new StringBuffer("INSERT INTO TBL_ORGANIZATION (ORGID");
		StringBuffer value = new StringBuffer(" VALUES ("+RandomUtil.uuBigDecimalId());

		if(organization.getOrgname() != null) {
			column.append(",ORGNAME");
			value.append(",'"+organization.getOrgname()+"'");
		}
		if(organization.getFatherorgid() != null) {
			column.append(",FATHERORGID");
			value.append(",'"+organization.getFatherorgid()+"'");
		}
		if(organization.getOrgnumber() != null) {
			column.append(",ORGNUMBER");
			value.append(",'"+organization.getOrgnumber()+"'");
		}
		if(organization.getOrgmeno() != null) {
			column.append(",ORGMENO");
			value.append(",'"+organization.getOrgmeno()+"'");
		}
		if(organization.getMemo() != null) {
			column.append(",MEMO");
			value.append(",'"+organization.getMemo()+"'");
		}
		if(organization.getIcode() != null) {
			column.append(",ICODE");
			value.append(",'"+organization.getIcode()+"'");
		}
		if(organization.getOrgtype()!= null) {
			column.append(",ORGTYPE");
			value.append(",'"+organization.getOrgtype()+"'");
		}
		if(organization.getAuditType() != null) {
			column.append(",AUDITTYPE");
			value.append(",'"+organization.getAuditType()+"'");
		}
		if(organization.getStatus()!= null) {
			column.append(",STATUS");
			value.append(",'"+organization.getStatus()+"'");
		}
		if(organization.getIszy() != null) {
			column.append(",ISZY");
			value.append(",'"+organization.getIszy()+"'");
		}
		if(organization.getHyzsktype() != null) {
			column.append(",HYZSKTYPE");
			value.append(",'"+organization.getHyzsktype()+"'");
		}
		if(organization.getOrderid() != null) {
			column.append(",ORDERID");
			value.append(",'"+organization.getOrderid()+"'");
		}
		if(organization.getOutsideid() != null) {
			column.append(",OUTSIDEID");
			value.append(",'"+organization.getOutsideid()+"'");
		}
		if(organization.getOutsideopendid() != null) {
			column.append(",OUTSIDEOPENDID");
			value.append(",'"+organization.getOutsideopendid()+"'");
		}
		if(organization.getIsautonumber() != null) {
			column.append(",ISAUTONUMBER");
			value.append(",'"+organization.getIsautonumber()+"'");
		}
		if(organization.getOrgcreate() != null) {
			column.append(",ORGCREATE");
			value.append(","+DataBaseSqlConfig.getDateStrFormat(new Date()));
		}
		if(organization.getIsinitialization() != null) {
			column.append(",ISINITIALIZATION");
			value.append(",'"+organization.getIsinitialization() +"'");
		}
		if(organization.getDuties() != null) {
			column.append(",DUTIES");
			value.append(",'"+organization.getDuties()+"'");
		}
		if(organization.getIndustryid() != null) {
			column.append(",INDUSTRYID");
			value.append(",'"+organization.getIndustryid() +"'");
		}
		if(organization.getBywx() != null) {
			column.append(",BYWX");
			value.append(",'"+organization.getBywx() +"'");
		}
		if(organization.getDatasource() != null) {
			column.append(",DATASOURCE");
			value.append(",'"+organization.getDatasource() +"'");
		}
		if(organization.getHistorycode() != null) {
			column.append(",HISTORYCODE");
			value.append(",'"+organization.getHistorycode() +"'");
		}
		if(organization.getPrincipalStaffId() != null) {
			column.append(",PRINCIPALSTAFFID");
			value.append(",'"+organization.getPrincipalStaffId() +"'");
		}
		if(organization.getChargeLeaderCode() != null) {
			column.append(",CHARGELEADERCODE");
			value.append(",'"+organization.getChargeLeaderCode() +"'");
		}
		if(organization.getHistorydepartmentid() != null) {
			column.append(",HISTORYDEPARTMENTID");
			value.append(",'"+organization.getHistorydepartmentid() +"'");
		}
		column.append(")");
		value.append(")");
		String sql = column.toString()+value.toString();
		return sql;
	}

	public String saveModiOrganiza(TblOrganization organization) {
		StringBuffer column = new StringBuffer("INSERT INTO TBL_ORGANIZATION (ORGID");
		StringBuffer value = new StringBuffer(" VALUES ("+RandomUtil.uuBigDecimalId());

		if(organization.getOrgname() != null) {
			column.append(",ORGNAME");
			value.append(",'"+organization.getOrgname()+"'");
		}
		if(organization.getFatherorgid() != null) {
			column.append(",FATHERORGID");
			value.append(",'"+organization.getFatherorgid()+"'");
		}
		if(organization.getOrgnumber() != null) {
			column.append(",ORGNUMBER");
			value.append(",'"+organization.getOrgnumber()+"'");
		}
		if(organization.getOrgmeno() != null) {
			column.append(",ORGMENO");
			value.append(",'"+organization.getOrgmeno()+"'");
		}
		if(organization.getMemo() != null) {
			column.append(",MEMO");
			value.append(",'"+organization.getMemo()+"'");
		}
		if(organization.getIcode() != null) {
			column.append(",ICODE");
			value.append(",'"+organization.getIcode()+"'");
		}
		if(organization.getOrgtype()!= null) {
			column.append(",ORGTYPE");
			value.append(",'"+organization.getOrgtype()+"'");
		}
//		if(organization.getAudittype()!= null) {
//			column.append(",AUDITTYPE");
//			value.append(",'"+organization.getAudittype()+"'");
//		}
		if(organization.getStatus()!= null) {
			column.append(",STATUS");
			value.append(",'"+organization.getStatus()+"'");
		}
		if(organization.getIszy() != null) {
			column.append(",ISZY");
			value.append(",'"+organization.getIszy()+"'");
		}
		if(organization.getHyzsktype() != null) {
			column.append(",HYZSKTYPE");
			value.append(",'"+organization.getHyzsktype()+"'");
		}
		if(organization.getOrderid() != null) {
			column.append(",ORDERID");
			value.append(",'"+organization.getOrderid()+"'");
		}
		if(organization.getOutsideid() != null) {
			column.append(",OUTSIDEID");
			value.append(",'"+organization.getOutsideid()+"'");
		}
		if(organization.getOutsideopendid() != null) {
			column.append(",OUTSIDEOPENDID");
			value.append(",'"+organization.getOutsideopendid()+"'");
		}
		if(organization.getIsautonumber() != null) {
			column.append(",ISAUTONUMBER");
			value.append(",'"+organization.getIsautonumber()+"'");
		}
		if(organization.getOrgcreate() != null) {
			column.append(",ORGCREATE");
			value.append(",'"+organization.getOrgcreate()+"'");
		}
		if(organization.getIsinitialization() != null) {
			column.append(",ISINITIALIZATION");
			value.append(",'"+organization.getIsinitialization() +"'");
		}
		if(organization.getDuties() != null) {
			column.append(",DUTIES");
			value.append(",'"+organization.getDuties()+"'");
		}
		if(organization.getIndustryid() != null) {
			column.append(",INDUSTRYID");
			value.append(",'"+organization.getIndustryid() +"'");
		}
		if(organization.getBywx() != null) {
			column.append(",BYWX");
			value.append(",'"+organization.getBywx() +"'");
		}
		if(organization.getDatasource() != null) {
			column.append(",DATASOURCE");
			value.append(",'"+organization.getDatasource() +"'");
		}
		if(organization.getHistorycode() != null) {
			column.append(",HISTORYCODE");
			value.append(",'"+organization.getHistorycode() +"'");
		}
		if(organization.getHistorydepartmentid() != null) {
			column.append(",HISTORYDEPARTMENTID");
			value.append(",'"+organization.getHistorydepartmentid() +"'");
		}
		if(organization.getPrincipalStaffId() != null) {
			column.append(",PRINCIPALSTAFFID");
			value.append(",'"+organization.getPrincipalStaffId() +"'");
		}
		if(organization.getChargeLeaderCode() != null) {
			column.append(",CHARGELEADERCODE");
			value.append(",'"+organization.getChargeLeaderCode() +"'");
		}
		column.append(")");
		value.append(")");
		String sql = column.toString()+value.toString();
		return sql;
	}


	public String saveModiOrgan(TblOrganization organization) {
		StringBuffer column = new StringBuffer("INSERT INTO TBL_ORGANIZATION (ORGID");
		StringBuffer value = new StringBuffer(" VALUES (HIBERNATE_SEQUENCE.nextval");

		if(organization.getOrgname() != null) {
			column.append(",ORGNAME");
			value.append(",'"+organization.getOrgname()+"'");
		}
		if(organization.getFatherorgid() != null) {
			column.append(",FATHERORGID");
			value.append(",'"+organization.getFatherorgid()+"'");
		}
		if(organization.getOrgnumber() != null) {
			column.append(",ORGNUMBER");
			value.append(",'"+organization.getOrgnumber()+"'");
		}
		if(organization.getOrgmeno() != null) {
			column.append(",ORGMENO");
			value.append(",'"+organization.getOrgmeno()+"'");
		}
		if(organization.getMemo() != null) {
			column.append(",MEMO");
			value.append(",'"+organization.getMemo()+"'");
		}
		if(organization.getIcode() != null) {
			column.append(",ICODE");
			value.append(",'"+organization.getIcode()+"'");
		}
		if(organization.getOrgtype()!= null) {
			column.append(",ORGTYPE");
			value.append(",'"+organization.getOrgtype()+"'");
		}
//		if(organization.getAudittype()!= null) {
//			column.append(",AUDITTYPE");
//			value.append(",'"+organization.getAudittype()+"'");
//		}
		if(organization.getStatus()!= null) {
			column.append(",STATUS");
			value.append(",'"+organization.getStatus()+"'");
		}
		if(organization.getIszy() != null) {
			column.append(",ISZY");
			value.append(",'"+organization.getIszy()+"'");
		}
		if(organization.getHyzsktype() != null) {
			column.append(",HYZSKTYPE");
			value.append(",'"+organization.getHyzsktype()+"'");
		}
		if(organization.getOrderid() != null) {
			column.append(",ORDERID");
			value.append(",'"+organization.getOrderid()+"'");
		}
		if(organization.getOutsideid() != null) {
			column.append(",OUTSIDEID");
			value.append(",'"+organization.getOutsideid()+"'");
		}
		if(organization.getOutsideopendid() != null) {
			column.append(",OUTSIDEOPENDID");
			value.append(",'"+organization.getOutsideopendid()+"'");
		}
		if(organization.getIsautonumber() != null) {
			column.append(",ISAUTONUMBER");
			value.append(",'"+organization.getIsautonumber()+"'");
		}
		if(organization.getOrgcreate() != null) {
			column.append(",ORGCREATE");
			value.append(",'"+organization.getOrgcreate()+"'");
		}
		if(organization.getIsinitialization() != null) {
			column.append(",ISINITIALIZATION");
			value.append(",'"+organization.getIsinitialization() +"'");
		}
		if(organization.getDuties() != null) {
			column.append(",DUTIES");
			value.append(",'"+organization.getDuties()+"'");
		}
		if(organization.getIndustryid() != null) {
			column.append(",INDUSTRYID");
			value.append(",'"+organization.getIndustryid() +"'");
		}
		if(organization.getBywx() != null) {
			column.append(",BYWX");
			value.append(",'"+organization.getBywx() +"'");
		}
		if(organization.getDatasource() != null) {
			column.append(",DATASOURCE");
			value.append(",'"+organization.getDatasource() +"'");
		}
		if(organization.getHistorycode() != null) {
			column.append(",HISTORYCODE");
			value.append(",'"+organization.getHistorycode() +"'");
		}
		if(organization.getHistorydepartmentid() != null) {
			column.append(",HISTORYDEPARTMENTID");
			value.append(",'"+organization.getHistorydepartmentid() +"'");
		}
		if(organization.getPrincipalStaffId() != null) {
			column.append(",PRINCIPALSTAFFID");
			value.append(",'"+organization.getPrincipalStaffId() +"'");
		}
		if(organization.getChargeLeaderCode() != null) {
			column.append(",CHARGELEADERCODE");
			value.append(",'"+organization.getChargeLeaderCode() +"'");
		}
		column.append(")");
		value.append(")");
		String sql = column.toString()+value.toString();
		return sql;
	}

	public String updateZuZhi(TblOrganization organization) throws Exception {
		StringBuffer sql = new StringBuffer("UPDATE TBL_ORGANIZATION SET ORGNUMBER = '"+organization.getOrgnumber()+"' ");
		if (organization.getOrgname() != null && !"".equals(organization.getOrgname())) {
			sql.append(" , ORGNAME = '" + organization.getOrgname() + "'");
		}
		if (organization.getFatherorgid() != null ) {
			sql.append(" , FATHERORGID = '" + organization.getFatherorgid() + "'");
		}
//		if (organization.getOrgnumber() != null && !"".equals(organization.getOrgnumber())) {
//			sql.append(" , ORGNUMBER = '" + organization.getOrgnumber() + "'");
//		}
		if (organization.getOrgmeno() != null && !"".equals(organization.getOrgmeno())) {
			sql.append(" , ORGMENO = '" + organization.getOrgmeno() + "'");
		}
		if (organization.getMemo() != null && !"".equals(organization.getMemo())) {
			sql.append(" , MEMO = '" + organization.getMemo() + "'");
		}
		if (organization.getIcode() != null && !"".equals(organization.getIcode())) {
			sql.append(" , icode = '" + organization.getIcode() + "'");
		}
		if (organization.getOrgtype() != null) {
			sql.append(" , ORGTYPE = '" + organization.getOrgtype() + "'");
		}
		if(organization.getAuditType() != null ) {
			sql.append(" , AUDITTYPE = '"+organization.getAuditType()+"'");
		}
		if (organization.getStatus() != null ) {
			sql.append(" , STATUS = '" + organization.getStatus() + "'");
		}
		if (organization.getIszy() != null ) {
			sql.append(" , ISZY = '" + organization.getIszy() + "'");
		}
		if (organization.getHyzsktype() != null && !"".equals(organization.getHyzsktype())) {
			sql.append(" , HYZSKTYPE = '" + organization.getHyzsktype() + "'");
		}
		if (organization.getOrderid() != null ) {
			sql.append(" , ORDERID = '" + organization.getOrderid() + "'");
		}
		if (organization.getOutsideid() != null ) {
			sql.append(" , OUTSIDEID = '" + organization.getOutsideid() + "'");
		}
		if (organization.getOutsideopendid() != null ) {
			sql.append(" , OUTSIDEOPENDID = '" + organization.getOutsideopendid() + "'");
		}
		if (organization.getIsautonumber() != null) {
			sql.append(" , ISAUTONUMBER = '" + organization.getIsautonumber() + "'");
		}
		if (organization.getOrgcreate() != null) {
			sql.append(" , ORGCREATE = " +DataBaseSqlConfig.getDateHmsStrFormat(organization.getOrgcreate()));
		}
		if (organization.getIsinitialization() != null ) {
			sql.append(" , ISINITIALIZATION = '" + organization.getIsinitialization() + "'");
		}
		if (organization.getDuties() != null && !"".equals(organization.getDuties())) {
			sql.append(" , DUTIES = '" + organization.getDuties() + "'");
		}
		if (organization.getIndustryid() != null) {
			sql.append(" , INDUSTRYID = '" + organization.getIndustryid() + "'");
		}
		if (organization.getBywx() != null && !"".equals(organization.getBywx())) {
			sql.append(" , BYWX = '" + organization.getBywx() + "'");
		}
		if (organization.getDatasource() != null && !"".equals(organization.getDatasource())) {
			sql.append(" , DATASOURCE = '" + organization.getDatasource() + "'");
		}
		if (organization.getHistorycode() != null && !"".equals(organization.getHistorycode())) {
			sql.append(" , HISTORYCODE = '" + organization.getHistorycode() + "'");
		}
		if (organization.getHistorydepartmentid() != null && !"".equals(organization.getHistorydepartmentid())) {
			sql.append(" , HISTORYDEPARTMENTID = '" + organization.getHistorydepartmentid() + "'");
		}
		if(organization.getPrincipalStaffId() != null) {
			sql.append(" , PRINCIPALSTAFFID = '"+organization.getPrincipalStaffId() +"'");
		}
		if(organization.getChargeLeaderCode() != null) {
			sql.append(" , CHARGELEADERCODE = '"+organization.getChargeLeaderCode() +"'");
		}
		sql.append(" WHERE ORGID = '" + organization.getOrgid() + "'");
		return sql.toString();
	}

	public String updateAtionHangYe(TblOrganization organization) {
		StringBuffer sql = new StringBuffer("UPDATE TBL_ORGANIZATION SET ORGNUMBER = '"+organization.getOrgnumber()+"' ");
		if (organization.getOrgname() != null && !"".equals(organization.getOrgname())) {
			sql.append(" , ORGNAME = '" + organization.getOrgname() + "'");
		}
		if (organization.getFatherorgid() != null) {
			sql.append(" , FATHERORGID = '" + organization.getFatherorgid() + "'");
		}
//		if (organization.getOrgnumber() != null && !"".equals(organization.getOrgnumber())) {
//			sql.append(" , ORGNUMBER = '" + organization.getOrgnumber() + "'");
//		}
		if (organization.getOrgmeno() != null && !"".equals(organization.getOrgmeno())) {
			sql.append(" , ORGMENO = '" + organization.getOrgmeno() + "'");
		}
		if (organization.getMemo() != null && !"".equals(organization.getMemo())) {
			sql.append(" , MEMO = '" + organization.getMemo() + "'");
		}
		if (organization.getIcode() != null && !"".equals(organization.getIcode())) {
			sql.append(" , icode = '" + organization.getIcode() + "'");
		}
		if (organization.getOrgtype() != null) {
			sql.append(" , ORGTYPE = '" + organization.getOrgtype() + "'");
		}
		if(organization.getAuditType() != null) {
			sql.append(" , AUDITTYPE = '"+organization.getAuditType()+"'");
		}
		if (organization.getStatus() != null) {
			sql.append(" , STATUS = '" + organization.getStatus() + "'");
		}
		if (organization.getIszy() != null && !"".equals(organization.getIszy())) {
			sql.append(" , ISZY = '" + organization.getIszy() + "'");
		}
		if (organization.getHyzsktype() != null && !"".equals(organization.getHyzsktype())) {
			sql.append(" , HYZSKTYPE = '" + organization.getHyzsktype() + "'");
		}
		if (organization.getOrderid() != null) {
			sql.append(" , ORDERID = '" + organization.getOrderid() + "'");
		}
		if (organization.getOutsideid() != null) {
			sql.append(" , OUTSIDEID = '" + organization.getOutsideid() + "'");
		}
		if (organization.getOutsideopendid() != null && !"".equals(organization.getOutsideopendid())) {
			sql.append(" , OUTSIDEOPENDID = '" + organization.getOutsideopendid() + "'");
		}
		if (organization.getIsautonumber() != null) {
			sql.append(" , ISAUTONUMBER = '" + organization.getIsautonumber() + "'");
		}
		if (organization.getOrgcreate() != null) {
			sql.append(" , ORGCREATE = '" + organization.getOrgcreate() + "'");
		}
		if (organization.getIsinitialization() != null) {
			sql.append(" , ISINITIALIZATION = '" + organization.getIsinitialization() + "'");
		}
		if (organization.getDuties() != null && !"".equals(organization.getDuties())) {
			sql.append(" , DUTIES = '" + organization.getDuties() + "'");
		}
		if (organization.getIndustryid() != null) {
			sql.append(" , INDUSTRYID = '" + organization.getIndustryid() + "'");
		}
		if (organization.getBywx() != null && !"".equals(organization.getBywx())) {
			sql.append(" , BYWX = '" + organization.getBywx() + "'");
		}
		if (organization.getDatasource() != null && !"".equals(organization.getDatasource())) {
			sql.append(" , DATASOURCE = '" + organization.getDatasource() + "'");
		}
		if (organization.getHistorycode() != null && !"".equals(organization.getHistorycode())) {
			sql.append(" , HISTORYCODE = '" + organization.getHistorycode() + "'");
		}
		if (organization.getHistorydepartmentid() != null && !"".equals(organization.getHistorydepartmentid())) {
			sql.append(" , HISTORYDEPARTMENTID = '" + organization.getHistorydepartmentid() + "'");
		}
//		if (organization.getOrgmeno() != null && !"".equals(organization.getOrgmeno())) {
//			sql.append(" , ORGMENO = '" + organization.getOrgmeno() + "'");
//		}
		if(organization.getPrincipalStaffId() != null) {
			sql.append(" , PRINCIPALSTAFFID = '"+organization.getPrincipalStaffId() +"'");
		}
		if(organization.getChargeLeaderCode() != null) {
			sql.append(" , CHARGELEADERCODE = '"+organization.getChargeLeaderCode() +"'");
		}
		sql.append(" WHERE ORGID = '" + organization.getOrgid() + "'");
		return sql.toString();
	}
	
	public String updateOrganInfo(TblOrganizationInfo info) {
		StringBuffer sql = new StringBuffer("UPDATE TBL_ORGANIZATION_INFO SET orgid='"+info.getOrgid()+"'");
		
		if(info.getOrgname() != null&& !"".equals(info.getOrgname())) {
			sql.append(" , orgname = '"+info.getOrgname()+"'");
		}
		
		if(info.getUnifiedcode() != null&& !"".equals(info.getUnifiedcode())) {
			sql.append(" , UNIFIEDCODE = '"+info.getUnifiedcode()+"'");
		}
		if(info.getNotunifiedcode() != null&& !"".equals(info.getNotunifiedcode())) {
			sql.append(" , NOTUNIFIEDCODE = '"+info.getNotunifiedcode()+"'");
		}
		if(info.getOrganizationtype() != null&& !"".equals(info.getOrganizationtype())) {
			sql.append(" , ORGANIZATIONTYPE = '"+info.getOrganizationtype()+"'");
		}
		if(info.getMainactivityone() != null&& !"".equals(info.getMainactivityone())) {
			sql.append(" , MAINACTIVITYONE = '"+info.getMainactivityone()+"'");
		}
		if(info.getMainactivitytwo()!= null&& !"".equals(info.getMainactivitytwo())) {
			sql.append(" , MAINACTIVITYTWO = '"+info.getMainactivitytwo()+"'");
		}
		if(info.getMainactivitythr()!= null&& !"".equals(info.getMainactivitythr())) {
			sql.append(" , MAINACTIVITYTHR = '"+info.getMainactivitythr()+"'");
		}
		if(info.getIndustrycode()!= null&& !"".equals(info.getIndustrycode())) {
			sql.append(" , INDUSTRYCODE = '"+info.getIndustrycode()+"'");
		}
		if(info.getProvince()!= null&& !"".equals(info.getProvince())) {
			sql.append(" , PROVINCE = '"+info.getProvince()+"'");
		}
		if(info.getLand() != null&& !"".equals(info.getLand())) {
			sql.append(" , LAND = '"+info.getLand()+"'");
		}
		if(info.getCounty() != null&& !"".equals(info.getCounty())) {
			sql.append(" , COUNTY = '"+info.getCounty()+"'");
		}
		if(info.getCountry() != null&& !"".equals(info.getCounty())) {
			sql.append(" , COUNTRY = '"+info.getCountry()+"'");
		}
		if(info.getStreet() != null&& !"".equals(info.getStreet())) {
			sql.append(" , STREET = '"+info.getStreet()+"'");
		}
		if(info.getSubdistrictoffice()!= null&& !"".equals(info.getSubdistrictoffice())) {
			sql.append(" , SUBDISTRICTOFFICE = '"+info.getSubdistrictoffice()+"'");
		}
		if(info.getCommunity() != null&& !"".equals(info.getCommunity())) {
			sql.append(" , COMMUNITY = '"+info.getCommunity()+"'");
		}
		if(info.getAreacode()!= null&& !"".equals(info.getAreacode())) {
			sql.append(" , AREACODE = '"+info.getAreacode()+"'");
		}
		if(info.getRuralcode() != null&& !"".equals(info.getRuralcode())) {
			sql.append(" , RURALCODE = '"+info.getRuralcode()+"'");
		}
		if(info.getUnitsize() != null&& !"".equals(info.getUnitsize())) {
			sql.append(" , UNITSIZE = '"+info.getUnitsize()+"'");
		}
		if(info.getNumberemployees()!= null&& !"".equals(info.getNumberemployees())) {
			sql.append(" , NUMBEREMPLOYEES = '"+info.getNumberemployees()+"'");
		}
		if(info.getLegalrepresentative() != null&& !"".equals(info.getLegalrepresentative())) {
			sql.append(" , LEGALREPRESENTATIVE = '"+info.getLegalrepresentative()+"'");
		}
		if(info.getAccountingstandards() != null&& !"".equals(info.getAccountingstandards())) {
			sql.append(" , ACCOUNTINGSTANDARDS = '"+info.getAccountingstandards()+"'");
		}
		if(info.getEmail() != null&& !"".equals(info.getEmail())) {
			sql.append(" , EMAIL = '"+info.getEmail()+"'");
		}
		
		if(info.getPhone() != null&& !"".equals(info.getPhone())) {
			sql.append(" , PHONE = '"+info.getPhone()+"'");
		}
		if(info.getFixedtelephone() != null&& !"".equals(info.getFixedtelephone())) {
			sql.append(" , FIXEDTELEPHONE = '"+info.getFixedtelephone()+"'");
		}
		if(info.getPostalcode() != null&& !"".equals(info.getPostalcode())) {
			sql.append(" , POSTALCODE = '"+info.getPostalcode()+"'");
		}
		if(info.getWebsite() != null&& !"".equals(info.getWebsite())) {
			sql.append(" , WEBSITE = '"+info.getWebsite()+"'");
		}
		
		if(info.getSfysjfr() != null&& !"".equals(info.getSfysjfr())) {
			sql.append(" , SFYSJFR = '"+info.getSfysjfr()+"'");
		}
		if(info.getUnifiedcodeone() != null&& !"".equals(info.getUnifiedcodeone())) {
			sql.append(" , UNIFIEDCODEONE = '"+info.getUnifiedcodeone()+"'");
		}
		if(info.getUnifiedcodetwo()!= null&& !"".equals(info.getUnifiedcodetwo())) {
			sql.append(" , UNIFIEDCODETWO = '"+info.getUnifiedcodetwo()+"'");
		}
		if(info.getUnifiedcodethr()!= null&& !"".equals(info.getUnifiedcodethr())) {
			sql.append(" , UNIFIEDCODETHR = '"+info.getUnifiedcodethr()+"'");
		}
		
		if(info.getWhetherfit() != null&& !"".equals(info.getWhetherfit())) {
			sql.append(" , WHETHERFIT = '"+info.getWhetherfit()+"'");
		}
		if(info.getPositionlevel()!= null&& !"".equals(info.getPositionlevel())) {
			sql.append(" , POSITIONLEVEL = '"+info.getPositionlevel()+"'");
		}
		if(info.getAppointmentmode()!= null&& !"".equals(info.getAppointmentmode())) {
			sql.append(" , APPOINTMENTMODE = '"+info.getAppointmentmode()+"'");
		}
		if(info.getIfsetup()!= null&& !"".equals(info.getIfsetup())) {
			sql.append(" , IFSETUP = '"+info.getIfsetup()+"'");
		}
		if(info.getAuditname() != null&& !"".equals(info.getAuditname())) {
			sql.append(" , AUDITNAME = '"+info.getAuditname()+"'");
		}
		if(info.getLeadingorganization() != null&& !"".equals(info.getLeadingorganization())) {
			sql.append(" , LEADINGORGANIZATION = '"+info.getLeadingorganization()+"'");
		}
		if(info.getIfindependently() != null&& !"".equals(info.getIfindependently())) {
			sql.append(" , IFINDEPENDENTLY = '"+info.getIfindependently()+"'");
		}
		if(info.getFunctionaldepartment()!= null&& !"".equals(info.getFunctionaldepartment())) {
			sql.append(" , FUNCTIONALDEPARTMENT = '"+info.getFunctionaldepartment()+"'");
		}
		if(info.getInternalaudit()!= null&& !"".equals(info.getInternalaudit())) {
			sql.append(" , INTERNALAUDIT = '"+info.getInternalaudit()+"'");
		}
		if(info.getFillingdate()!= null) {
			sql.append(" , fillingdate = TO_DATE('"+ DateUtil.parseDate(info.getFillingdate(),"yyyy-MM-dd") +"', 'YYYY-MM-DD')");
		}
		if(info.getContactnumber()!= null&& !"".equals(info.getContactnumber())) {
			sql.append(" , Contactnumber = '"+info.getContactnumber() +"'");
		}
		if(info.getPreparer()!= null&& !"".equals(info.getPreparer())) {
			sql.append(" , Preparer = '"+info.getPreparer() +"'");
		}
		if(info.getStatistical()!= null&& !"".equals(info.getStatistical())) {
			sql.append(" , statistical = '"+info.getStatistical() +"'");
		}
		
		sql.append(" WHERE ORGID = '" + info.getOrgid()+ "'");
		return sql.toString();
	}
	

	public String insertOrganInfo(TblOrganizationInfo info) {
		StringBuffer column = new StringBuffer("INSERT INTO TBL_ORGANIZATION_INFO (INFOID");
		StringBuffer value = new StringBuffer(" VALUES (HIBERNATE_SEQUENCE.nextval");

		if(info.getOrgname() != null&& !"".equals(info.getOrgname())) {
			column.append(",ORGNAME");
			value.append(",'"+info.getOrgname()+"'");
		}
		if(info.getOrgid() != null&& !"".equals(info.getOrgid())) {
			column.append(",ORGID");
			value.append(",'"+info.getOrgid()+"'");
		}
		if(info.getUnifiedcode() != null&& !"".equals(info.getUnifiedcode())) {
			column.append(",UNIFIEDCODE");
			value.append(",'"+info.getUnifiedcode()+"'");
		}
		if(info.getNotunifiedcode() != null&& !"".equals(info.getNotunifiedcode())) {
			column.append(",NOTUNIFIEDCODE");
			value.append(",'"+info.getNotunifiedcode()+"'");
		}
		if(info.getOrganizationtype() != null&& !"".equals(info.getOrganizationtype())) {
			column.append(",ORGANIZATIONTYPE");
			value.append(",'"+info.getOrganizationtype()+"'");
		}
		if(info.getMainactivityone() != null&& !"".equals(info.getMainactivityone())) {
			column.append(",MAINACTIVITYONE");
			value.append(",'"+info.getMainactivityone()+"'");
		}
		if(info.getMainactivitytwo()!= null&& !"".equals(info.getMainactivitytwo())) {
			column.append(",MAINACTIVITYTWO");
			value.append(",'"+info.getMainactivitytwo()+"'");
		}
		if(info.getMainactivitythr()!= null&& !"".equals(info.getMainactivitythr())) {
			column.append(",MAINACTIVITYTHR");
			value.append(",'"+info.getMainactivitythr()+"'");
		}
		if(info.getIndustrycode()!= null&& !"".equals(info.getIndustrycode())) {
			column.append(",INDUSTRYCODE");
			value.append(",'"+info.getIndustrycode()+"'");
		}
		if(info.getProvince()!= null&& !"".equals(info.getProvince())) {
			column.append(",PROVINCE");
			value.append(",'"+info.getProvince()+"'");
		}
		if(info.getLand() != null&& !"".equals(info.getLand())) {
			column.append(",LAND");
			value.append(",'"+info.getLand()+"'");
		}
		if(info.getCounty() != null&& !"".equals(info.getCounty())) {
			column.append(",COUNTY");
			value.append(",'"+info.getCounty()+"'");
		}
		if(info.getCountry() != null&& !"".equals(info.getCountry())) {
			column.append(",COUNTRY");
			value.append(",'"+info.getCountry()+"'");
		}
		if(info.getStreet() != null&& !"".equals(info.getStreet())) {
			column.append(",STREET");
			value.append(",'"+info.getStreet()+"'");
		}
		if(info.getSubdistrictoffice()!= null&& !"".equals(info.getSubdistrictoffice())) {
			column.append(",SUBDISTRICTOFFICE");
			value.append(",'"+info.getSubdistrictoffice()+"'");
		}
		if(info.getCommunity() != null&& !"".equals(info.getCommunity())) {
			column.append(",COMMUNITY");
			value.append(",'"+info.getCommunity()+"'");
		}
		if(info.getAreacode()!= null&& !"".equals(info.getAreacode())) {
			column.append(",AREACODE");
			value.append(",'"+info.getAreacode() +"'");
		}
		if(info.getRuralcode() != null&& !"".equals(info.getRuralcode())) {
			column.append(",RURALCODE");
			value.append(",'"+info.getRuralcode()+"'");
		}
		if(info.getUnitsize() != null&& !"".equals(info.getUnitsize())) {
			column.append(",UNITSIZE");
			value.append(",'"+info.getUnitsize() +"'");
		}
		if(info.getNumberemployees()!= null&& !"".equals(info.getNumberemployees())) {
			column.append(",NUMBEREMPLOYEES");
			value.append(",'"+info.getNumberemployees() +"'");
		}
		if(info.getLegalrepresentative() != null&& !"".equals(info.getLegalrepresentative())) {
			column.append(",LEGALREPRESENTATIVE");
			value.append(",'"+info.getLegalrepresentative() +"'");
		}
		if(info.getAccountingstandards() != null&& !"".equals(info.getAccountingstandards())) {
			column.append(",ACCOUNTINGSTANDARDS");
			value.append(",'"+info.getAccountingstandards() +"'");
		}
		if(info.getEmail() != null&& !"".equals(info.getEmail())) {
			column.append(",EMAIL");
			value.append(",'"+info.getEmail() +"'");
		}
		
		if(info.getPhone() != null&& !"".equals(info.getPhone())) {
			column.append(",PHONE");
			value.append(",'"+info.getPhone() +"'");
		}
		if(info.getFixedtelephone() != null&& !"".equals(info.getFixedtelephone())) {
			column.append(",FIXEDTELEPHONE");
			value.append(",'"+info.getFixedtelephone() +"'");
		}
		if(info.getPostalcode() != null&& !"".equals(info.getPostalcode())) {
			column.append(",POSTALCODE");
			value.append(",'"+info.getPostalcode() +"'");
		}
		if(info.getWebsite() != null&& !"".equals(info.getWebsite())) {
			column.append(",WEBSITE");
			value.append(",'"+info.getWebsite() +"'");
		}
		
		if(info.getWebsite() != null&& !"".equals(info.getWebsite())) {
			column.append(",SFYSJFR");
			value.append(",'"+info.getSfysjfr() +"'");
		}
		if(info.getUnifiedcodeone() != null&& !"".equals(info.getUnifiedcodeone())) {
			column.append(",UNIFIEDCODEONE");
			value.append(",'"+info.getUnifiedcodeone() +"'");
		}
		if(info.getUnifiedcodetwo()!= null&& !"".equals(info.getUnifiedcodetwo())) {
			column.append(",UNIFIEDCODETWO");
			value.append(",'"+info.getUnifiedcodetwo() +"'");
		}
		if(info.getUnifiedcodethr()!= null&& !"".equals(info.getUnifiedcodethr())) {
			column.append(",UNIFIEDCODETHR");
			value.append(",'"+info.getUnifiedcodethr() +"'");
		}
		
		if(info.getWhetherfit() != null&& !"".equals(info.getWhetherfit())) {
			column.append(",WHETHERFIT");
			value.append(",'"+info.getWhetherfit() +"'");
		}
		if(info.getPositionlevel()!= null&& !"".equals(info.getPositionlevel())) {
			column.append(",POSITIONLEVEL");
			value.append(",'"+info.getPositionlevel() +"'");
		}
		if(info.getAppointmentmode()!= null&& !"".equals(info.getAppointmentmode())) {
			column.append(",APPOINTMENTMODE");
			value.append(",'"+info.getAppointmentmode() +"'");
		}
		if(info.getIfsetup()!= null&& !"".equals(info.getIfsetup())) {
			column.append(",IFSETUP");
			value.append(",'"+info.getIfsetup() +"'");
		}
		if(info.getAuditname() != null&& !"".equals(info.getAuditname())) {
			column.append(",AUDITNAME");
			value.append(",'"+info.getAuditname() +"'");
		}
		if(info.getLeadingorganization() != null&& !"".equals(info.getLeadingorganization())) {
			column.append(",LEADINGORGANIZATION");
			value.append(",'"+info.getLeadingorganization() +"'");
		}
		if(info.getIfindependently() != null&& !"".equals(info.getIfindependently())) {
			column.append(",IFINDEPENDENTLY");
			value.append(",'"+info.getIfindependently() +"'");
		}
		if(info.getFunctionaldepartment()!= null&& !"".equals(info.getCommunity())) {
			column.append(",FUNCTIONALDEPARTMENT");
			value.append(",'"+info.getFunctionaldepartment() +"'");
		}
		if(info.getInternalaudit()!= null&& !"".equals(info.getInternalaudit())) {
			column.append(",INTERNALAUDIT");
			value.append(",'"+info.getInternalaudit() +"'");
		}
		
		if(info.getFillingdate()!= null) {
			column.append(",fillingdate");
            value.append(",TO_DATE('"+ DateUtil.parseDate(info.getFillingdate(),"yyyy-MM-dd") +"', 'YYYY-MM-DD')");
		}
		if(info.getContactnumber()!= null&& !"".equals(info.getContactnumber())) {
			column.append(",Contactnumber");
			value.append(",'"+info.getContactnumber() +"'");
		}
		if(info.getPreparer()!= null&& !"".equals(info.getPreparer())) {
			column.append(",Preparer");
			value.append(",'"+info.getPreparer() +"'");
		}
		if(info.getStatistical()!= null&& !"".equals(info.getStatistical())) {
			column.append(",statistical");
			value.append(",'"+info.getStatistical() +"'");
		}
		column.append(")");
		value.append(")");
		String sql = column.toString()+value.toString();
		return sql;
	}
	
	public String selectDeptListByroleid(String roleid,IPage<TblOrganization> page, String orgname) {
		StringBuffer sbSql = new StringBuffer("SELECT ORGID,ORGNAME,ORGTYPE,FATHERORGID,ORGANIZATIONTREES FROM TBL_ORGANIZATION WHERE ORGID IN (select ORGID from TBL_ORG_ROLE where ROLEID="+roleid+")");
		if (orgname != null &&  orgname.length() > 0) {
			sbSql.append(" and ORGNAME  like '%" + orgname + "%' ");
		}
		sbSql.append(" ORDER BY ORGID ");
		String sql = sbSql.toString();
		return sql;
	}
}
