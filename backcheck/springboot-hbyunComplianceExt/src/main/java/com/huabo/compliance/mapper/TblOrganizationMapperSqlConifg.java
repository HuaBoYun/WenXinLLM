package com.huabo.compliance.mapper;

import com.hbfk.util.DateUtil;
import com.hbfk.util.PageInfo;
import com.huabo.compliance.entity.Find;
import com.huabo.compliance.entity.TblOrganization;
import com.huabo.compliance.entity.TblOrganizationInfo;

import java.math.BigDecimal;

public class TblOrganizationMapperSqlConifg {
	
	
	public String selectListByPageInfo(PageInfo<TblOrganization> pageInfo) {

		StringBuffer sqlSb = new StringBuffer("SELECT * FROM (SELECT BUDGET.*,ROWNUM RN FROM (SELECT * FROM TBL_ORGANIZATION where ORGTYPE = 100 ");
		sqlSb.append(" ORDER BY ORGID desc ) BUDGET WHERE rownum <= "+(pageInfo.getCurrentPage()*pageInfo.getPageSize())+" ) WHERE RN > "+pageInfo.getCurrentRecord());
		String sql = sqlSb.toString();
		return sql;
	}

	public String selectListByPageInOrgid(PageInfo<TblOrganization> pageInfo,BigDecimal orgid) {
		StringBuffer sbSql = new StringBuffer("SELECT * FROM TBL_ORGANIZATION");
		sbSql.append("WHERE orgid= "+orgid+"");
		sbSql.append(" T1 WHERE ROWNUM <= "+(pageInfo.getCurrentPage()*pageInfo.getPageSize())+" ) T2 WHERE T2.RNUM > "+pageInfo.getCurrentRecord());
		return sbSql.toString();

	}

//	public String findByPageBean(PageInfo<TblOrganization> pageInfo, BigDecimal orgid,Find find) {
//		StringBuffer sbSql = new StringBuffer("SELECT ORGID,ORGNAME,ORGNUMBER,ORGMENO,MEMO,ORGTYPE,STATUS,ISZY,ISAUTONUMBER FROM ( SELECT ORGID,ORGNAME,ORGNUMBER,ORGMENO,MEMO,ORGTYPE,STATUS,ISZY,ISAUTONUMBER,ROWNUM RN FROM ( select ORGID,ORGNAME,ORGNUMBER,ORGMENO,MEMO,ORGTYPE,STATUS,ISZY,ISAUTONUMBER from TBL_ORGANIZATION org where ORGTYPE != 0 START WITH ORG.FATHERORGID = "+orgid+"");
//		if (find != null && find.getCode() != null && find.getCode().length() > 0) {
//			sbSql.append(" and ORGNUMBER like '%" + find.getCode() + "%' ");
//		}
//		if (find != null && find.getName() != null && find.getName().length() > 0) {
//			sbSql.append( " and ORGNAME  like '%" + find.getName() + "%' ");
//		}
//		sbSql.append("  CONNECT BY PRIOR ORG.ORGID = ORG.FATHERORGID ORDER BY orgtype asc,ORGID desc,rownum ASC ) T1 WHERE ROWNUM <= "+(pageInfo.getCurrentPage()*pageInfo.getPageSize())+" ) T2 WHERE T2.RN > "+pageInfo.getCurrentRecord());
//		return sbSql.toString();
//	}

	public String findByPageBean(PageInfo<TblOrganization> pageInfo, BigDecimal orgid, Find find) {
		StringBuffer sbSql = new StringBuffer("SELECT * FROM ( SELECT BUDGET.*,ROWNUM RNUM FROM (select * from TBL_ORGANIZATION ORG where ORGTYPE != 0 ");
		if (find != null && find.getCode() != null && find.getCode().length() > 0) {
			sbSql.append(" and ORG.ORGNUMBER like '%" + find.getCode() + "%' ");
		}
		if (find != null && find.getName() != null && find.getName().length() > 0) {
			sbSql.append(" and ORG.ORGNAME  like '%" + find.getName() + "%' ");
		}
		sbSql.append("start with  ORG.FATHERORGID= " + orgid + " CONNECT BY PRIOR ORG.ORGID = ORG.FATHERORGID ORDER BY ORG.orgtype asc,ORG.ORGID desc,rownum ASC) BUDGET WHERE rownum <= "+(pageInfo.getCurrentPage()*pageInfo.getPageSize())+" ) WHERE RNUM > "+pageInfo.getCurrentRecord());
		String sql = sbSql.toString();
		return sql;
	}
	public String findByPage(PageInfo<TblOrganization> pageInfo, BigDecimal orgid,Find find) {
		StringBuffer sbSql = new StringBuffer("SELECT COUNT(*) FROM (select * from TBL_ORGANIZATION ORG where ORG.ORGTYPE != 0 ");
		if (find != null && find.getCode() != null && find.getCode().length() > 0) {
			sbSql.append(" and ORG.ORGNUMBER like '%" + find.getCode() + "%' ");
		}
		if (find != null && find.getName() != null && find.getName().length() > 0) {
			sbSql.append(" and ORG.ORGNAME  like '%" + find.getName() + "%' ");
		}
		sbSql.append("start with  ORG.FATHERORGID= " + orgid + " CONNECT BY PRIOR ORG.ORGID = ORG.FATHERORGID )");
		return sbSql.toString();
	}

	
	public String selectListByPageInfoOrgid(PageInfo<TblOrganization> pageInfo,Find find,BigDecimal pid) {
		StringBuffer sbSql = new StringBuffer("SELECT * FROM ( SELECT BUDGET.*,ROWNUM RNUM FROM (select * from TBL_ORGANIZATION where 1=1 ");
		if (find != null && find.getCode() != null && find.getCode().length() > 0) {
			sbSql.append(" and ORGNUMBER like '%" + find.getCode() + "%' ");
		}
		if (find != null && find.getName() != null && find.getName().length() > 0) {
			sbSql.append(" and ORGNAME  like '%" + find.getName() + "%' ");
		}
		sbSql.append("start with  FATHERORGID= " + pid + " and ORGTYPE=0  connect by prior ORGID= FATHERORGID ORDER BY ORGID desc,orderid ASC) BUDGET WHERE rownum <= "+(pageInfo.getCurrentPage()*pageInfo.getPageSize())+" ) WHERE RNUM > "+pageInfo.getCurrentRecord());
		String sql = sbSql.toString();
		return sql;
	}

	public String findAllCommpanyPageBeanStaffid(PageInfo<TblOrganization> pageInfo, BigDecimal pid){
//		TblOrganization organization = pageInfo.getCondition();
		StringBuffer sbSql = new StringBuffer("SELECT * FROM (SELECT BUDGET.*,ROWNUM RNUM FROM (select * from TBL_ORGANIZATION org where ORGTYPE != 0 and ORGTYPE<100 and status=0  and ORG.FATHERORGID = " + pid + "   ORDER BY orgid,orderid ASC");
		sbSql.append(") BUDGET WHERE ROWNUM <= "+(pageInfo.getCurrentPage()*pageInfo.getPageSize())+" ) WHERE RNUM > "+pageInfo.getCurrentRecord());
		return sbSql.toString();
	}


//	public String selectCountPage(PageInfo<TblOrganization> pageInfo,Find find,BigDecimal orgid) {
//		StringBuffer sbSql = new StringBuffer("select count(*) from TBL_ORGANIZATION where 1=1 start with  FATHERORGID= " + orgid + " and ORGTYPE=0  connect by prior orgid= FATHERORGID ORDER BY ORGID desc,orderid ASC");
//		if (find != null && find.getCode() != null && find.getCode().length() > 0) {
//			sbSql.append(" and ORGNUMBER like '%" + find.getCode() + "%' ");
//		}
//		if (find != null && find.getName() != null && find.getName().length() > 0) {
//			sbSql.append(" and ORGNAME  like '%" + find.getName() + "%' ");
//		}
//		sbSql.append(" ORDER BY ORGID desc,orderid ASC) BUDGET WHERE rownum <= "+(pageInfo.getCurrentPage()*pageInfo.getPageSize())+" ) WHERE RNUM > "+pageInfo.getCurrentRecord());
//		String sql = sbSql.toString();
//		return sql;
//	}
	
//	public String selectCountByPageInfo(PageInfo<TblOrganization> pageInfo) {
//		StringBuffer sbSql = new StringBuffer("select count(*) from TBL_ORGANIZATION(");
//		sbSql.append(" where ORGTYPE = 100 ");
//		sbSql.append(") T1 WHERE ROWNUM <= "+(pageInfo.getCurrentPage()*pageInfo.getPageSize())+" ) T2 WHERE T2.RNUM > "+pageInfo.getCurrentRecord());
//		return sbSql.toString();
//	}

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

	public String selectStaffInfoByModuleIdList(PageInfo<TblOrganization> pageInfo, Integer moduleId) throws Exception {
		StringBuffer sqlSb = new StringBuffer("SELECT * FROM (SELECT BUDGET.*,ROWNUM RN FROM (SELECT ORGID,ORGNUMBER,ORGNAME FROM TBL_ORGANIZATION WHERE ORGID IN (SELECT ORGID FROM TBL_SYSTEM_MODELORG WHERE MODELID = "+moduleId);
		if(pageInfo.getCondition().getOrgname() != null && !"".equals(pageInfo.getCondition().getOrgname())) {
			sqlSb.append(" AND ORGNAME LIKE '%"+pageInfo.getCondition().getOrgname()+"%'");
		}
		sqlSb.append(")) BUDGET WHERE rownum <= "+(pageInfo.getCurrentPage()*pageInfo.getPageSize())+" ) WHERE RN > "+pageInfo.getCurrentRecord());
		String sql = sqlSb.toString();
		return sql;
	}

	public String selectStaffInfoByModuleIdCount(PageInfo<TblOrganization> pageInfo, Integer moduleId) throws Exception {
		String sqlCount = "SELECT COUNT(*) FROM TBL_ORGANIZATION WHERE ORGID IN (SELECT ORGID FROM TBL_SYSTEM_MODELORG WHERE MODELID = #{moduleId})";

		if(pageInfo.getCondition().getOrgname() != null && !"".equals(pageInfo.getCondition().getOrgname())) {
			sqlCount += " AND ORGNAME LIKE '%"+pageInfo.getCondition().getOrgname() +"%'";
		}
		return sqlCount;
	}

	public String selectListByPid(PageInfo<TblOrganization> pageInfo,String pid) {
		StringBuffer sqlSb = new StringBuffer("SELECT * FROM (SELECT BUDGET.*,ROWNUM RN FROM (SELECT * FROM TBL_ORGANIZATION where ORGTYPE = 100 and FATHERORGID= "+pid);
		sqlSb.append(" ORDER BY ORGID desc ) BUDGET WHERE rownum <= "+(pageInfo.getCurrentPage()*pageInfo.getPageSize())+" ) WHERE RN > "+pageInfo.getCurrentRecord());
		String sql = sqlSb.toString();
		return sql;
	}

	public String selectListByStaffid(PageInfo<TblOrganization> pageInfo,String pid) {
		TblOrganization organization = new TblOrganization();
		StringBuffer sbSql = new StringBuffer("SELECT * FROM (SELECT BUDGET.*,ROWNUM RNUM FROM (SELECT * FROM TBL_ORGANIZATION where ORGTYPE = 100 and FATHERORGID= "+pid+"  ORDER BY ORGID desc");
		sbSql.append(") BUDGET WHERE ROWNUM <= "+(pageInfo.getCurrentPage()*pageInfo.getPageSize())+" ) WHERE RNUM > "+pageInfo.getCurrentRecord());
		return sbSql.toString();
	}

	public String getNodesa(PageInfo<TblOrganization> pageInfo) {
		TblOrganization organization = new TblOrganization();
		StringBuffer sbSql = new StringBuffer("SELECT * FROM (SELECT BUDGET.*,ROWNUM RNUM FROM (select * from TBL_ORGANIZATION order by orderid asc");
		sbSql.append(") BUDGET WHERE ROWNUM <= "+(pageInfo.getCurrentPage()*pageInfo.getPageSize())+" ) WHERE RNUM > "+pageInfo.getCurrentRecord());
		return sbSql.toString();
	}

	public String getNodesaNodeId(PageInfo<TblOrganization> pageInfo,BigDecimal nodeId) {
		StringBuffer sbSql = new StringBuffer("SELECT * FROM (SELECT BUDGET.*,ROWNUM RNUM FROM (select * from tbl_organization where ORGID = "+nodeId+"  and orgtype < 100 ORDER BY orderid ASC");
		sbSql.append(") BUDGET WHERE ROWNUM <= "+(pageInfo.getCurrentPage()*pageInfo.getPageSize())+" ) WHERE RNUM > "+pageInfo.getCurrentRecord());
		return sbSql.toString();
	}

	public String saveModiOrganization(TblOrganization organization) {
		StringBuffer sql = new StringBuffer("UPDATE TBL_ORGANIZATION SET ORGNUMBER = '"+organization.getOrgnumber()+"' ");
		if(organization.getOrgname() != null && !"".equals(organization.getOrgname())) {
			sql.append(" , ORGNAME = '"+organization.getOrgname()+"'");
		}
		if(organization.getFatherorgid() != null && !"".equals(organization.getFatherorgid())) {
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
		if(organization.getOrgtype() != null && !"".equals(organization.getOrgtype())) {
			sql.append(" , ORGTYPE = '"+organization.getOrgtype()+"'");
		}
		if(organization.getAudittype() != null && !"".equals(organization.getAudittype())) {
			sql.append(" , AUDITTYPE = '"+organization.getAudittype()+"'");
		}
		if(organization.getStatus() != null && !"".equals(organization.getStatus())) {
			sql.append(" , STATUS = '"+organization.getStatus()+"'");
		}
		if(organization.getIszy() != null && !"".equals(organization.getIszy())) {
			sql.append(" , ISZY = '"+organization.getIszy()+"'");
		}
		if(organization.getHyzsktype() != null && !"".equals(organization.getHyzsktype())) {
			sql.append(" , HYZSKTYPE = '"+organization.getHyzsktype()+"'");
		}
		if(organization.getOrderid() != null && !"".equals(organization.getOrderid())) {
			sql.append(" , ORDERID = '"+organization.getOrderid()+"'");
		}
		if(organization.getOutsideid() != null && !"".equals(organization.getOutsideid())) {
			sql.append(" , OUTSIDEID = '"+organization.getOutsideid()+"'");
		}
		if(organization.getOutsideopendid() != null && !"".equals(organization.getOutsideopendid())) {
			sql.append(" , OUTSIDEOPENDID = '"+organization.getOutsideopendid()+"'");
		}
		if(organization.getIsautonumber() != null && !"".equals(organization.getIsautonumber())) {
			sql.append(" , ISAUTONUMBER = '"+organization.getIsautonumber()+"'");
		}
		if(organization.getOrgcreate() != null && !"".equals(organization.getOrgcreate())) {
			sql.append(" , ORGCREATE = '"+organization.getOrgcreate()+"'");
		}
		if(organization.getIsinitialization() != null && !"".equals(organization.getIsinitialization())) {
			sql.append(" , ISINITIALIZATION = '"+organization.getIsinitialization()+"'");
		}
		if(organization.getDuties() != null && !"".equals(organization.getDuties())) {
			sql.append(" , DUTIES = '"+organization.getDuties()+"'");
		}
		if(organization.getIndustryid() != null && !"".equals(organization.getIndustryid())) {
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

		sql.append(" WHERE ORGID = '"+organization.getOrgid()+"'");
		return sql.toString();
//		StringBuffer column = new StringBuffer("INSERT INTO TBL_ORGANIZATION (ORGID");
//		StringBuffer value = new StringBuffer(" VALUES (HIBERNATE_SEQUENCE.nextval");
//
//		if(organization.getOrgname() != null) {
//			column.append(",ORGNAME");
//			value.append(",'"+organization.getOrgname()+"'");
//		}
//		if(organization.getFatherorgid() != null) {
//			column.append(",FATHERORGID");
//			value.append(",'"+organization.getFatherorgid()+"'");
//		}
//		if(organization.getOrgnumber() != null) {
//			column.append(",ORGNUMBER");
//			value.append(",'"+organization.getOrgnumber()+"'");
//		}
//		if(organization.getOrgmeno() != null) {
//			column.append(",ORGMENO");
//			value.append(",'"+organization.getOrgmeno()+"'");
//		}
//		if(organization.getMemo() != null) {
//			column.append(",MEMO");
//			value.append(",'"+organization.getMemo()+"'");
//		}
//		if(organization.getIcode() != null) {
//			column.append(",ICODE");
//			value.append(",'"+organization.getIcode()+"'");
//		}
//		if(organization.getOrgtype()!= null) {
//			column.append(",ORGTYPE");
//			value.append(",'"+organization.getOrgtype()+"'");
//		}
////		if(organization.getAudittype()!= null) {
////			column.append(",AUDITTYPE");
////			value.append(",'"+organization.getAudittype()+"'");
////		}
//		if(organization.getStatus()!= null) {
//			column.append(",STATUS");
//			value.append(",'"+organization.getStatus()+"'");
//		}
//		if(organization.getIszy() != null) {
//			column.append(",ISZY");
//			value.append(",'"+organization.getIszy()+"'");
//		}
//		if(organization.getHyzsktype() != null) {
//			column.append(",HYZSKTYPE");
//			value.append(",'"+organization.getHyzsktype()+"'");
//		}
//		if(organization.getOrderid() != null) {
//			column.append(",ORDERID");
//			value.append(",'"+organization.getOrderid()+"'");
//		}
//		if(organization.getOutsideid() != null) {
//			column.append(",OUTSIDEID");
//			value.append(",'"+organization.getOutsideid()+"'");
//		}
//		if(organization.getOutsideopendid() != null) {
//			column.append(",OUTSIDEOPENDID");
//			value.append(",'"+organization.getOutsideopendid()+"'");
//		}
//		if(organization.getIsautonumber() != null) {
//			column.append(",ISAUTONUMBER");
//			value.append(",'"+organization.getIsautonumber()+"'");
//		}
//		if(organization.getOrgcreate() != null) {
//			column.append(",ORGCREATE");
//			value.append(",'"+organization.getOrgcreate()+"'");
//		}
//		if(organization.getIsinitialization() != null) {
//			column.append(",ISINITIALIZATION");
//			value.append(",'"+organization.getIsinitialization() +"'");
//		}
//		if(organization.getDuties() != null) {
//			column.append(",DUTIES");
//			value.append(",'"+organization.getDuties()+"'");
//		}
//		if(organization.getIndustryid() != null) {
//			column.append(",INDUSTRYID");
//			value.append(",'"+organization.getIndustryid() +"'");
//		}
//		if(organization.getBywx() != null) {
//			column.append(",BYWX");
//			value.append(",'"+organization.getBywx() +"'");
//		}
//		if(organization.getDatasource() != null) {
//			column.append(",DATASOURCE");
//			value.append(",'"+organization.getDatasource() +"'");
//		}
//		if(organization.getHistorycode() != null) {
//			column.append(",HISTORYCODE");
//			value.append(",'"+organization.getHistorycode() +"'");
//		}
//		if(organization.getHistorydepartmentid() != null) {
//			column.append(",HISTORYDEPARTMENTID");
//			value.append(",'"+organization.getHistorydepartmentid() +"'");
//		}
//		column.append(")");
//		value.append(")");
//		String sql = column.toString()+value.toString();
//		return sql;
	}

	public String saveAtionHangYe(TblOrganization organization) {
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
		if(organization.getAudittype() != null) {
			column.append(",AUDITTYPE");
			value.append(",'"+organization.getAudittype()+"'");
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

	public String addReturnId(TblOrganization organization) {
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
		if(organization.getAudittype() != null) {
			column.append(",AUDITTYPE");
			value.append(",'"+organization.getAudittype()+"'");
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

	public String saveModiOrganiza(TblOrganization organization) {
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
		column.append(")");
		value.append(")");
		String sql = column.toString()+value.toString();
		return sql;
	}

	public String updateZuZhi(TblOrganization organization) {
		StringBuffer sql = new StringBuffer("UPDATE TBL_ORGANIZATION SET ORGNUMBER = '"+organization.getOrgnumber()+"' ");
		if (organization.getOrgname() != null && !"".equals(organization.getOrgname())) {
			sql.append(" , ORGNAME = '" + organization.getOrgname() + "'");
		}
		if (organization.getFatherorgid() != null && !"".equals(organization.getFatherorgid())) {
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
		if (organization.getOrgtype() != null && !"".equals(organization.getOrgtype())) {
			sql.append(" , ORGTYPE = '" + organization.getOrgtype() + "'");
		}
		if(organization.getAudittype() != null && !"".equals(organization.getAudittype())) {
			sql.append(" , AUDITTYPE = '"+organization.getAudittype()+"'");
		}
		if (organization.getStatus() != null && !"".equals(organization.getStatus())) {
			sql.append(" , STATUS = '" + organization.getStatus() + "'");
		}
		if (organization.getIszy() != null && !"".equals(organization.getIszy())) {
			sql.append(" , ISZY = '" + organization.getIszy() + "'");
		}
		if (organization.getHyzsktype() != null && !"".equals(organization.getHyzsktype())) {
			sql.append(" , HYZSKTYPE = '" + organization.getHyzsktype() + "'");
		}
		if (organization.getOrderid() != null && !"".equals(organization.getOrderid())) {
			sql.append(" , ORDERID = '" + organization.getOrderid() + "'");
		}
		if (organization.getOutsideid() != null && !"".equals(organization.getOutsideid())) {
			sql.append(" , OUTSIDEID = '" + organization.getOutsideid() + "'");
		}
		if (organization.getOutsideopendid() != null && !"".equals(organization.getOutsideopendid())) {
			sql.append(" , OUTSIDEOPENDID = '" + organization.getOutsideopendid() + "'");
		}
		if (organization.getIsautonumber() != null && !"".equals(organization.getIsautonumber())) {
			sql.append(" , ISAUTONUMBER = '" + organization.getIsautonumber() + "'");
		}
		if (organization.getOrgcreate() != null && !"".equals(organization.getOrgcreate())) {
			sql.append(" , ORGCREATE = '" + organization.getOrgcreate() + "'");
		}
		if (organization.getIsinitialization() != null && !"".equals(organization.getIsinitialization())) {
			sql.append(" , ISINITIALIZATION = '" + organization.getIsinitialization() + "'");
		}
		if (organization.getDuties() != null && !"".equals(organization.getDuties())) {
			sql.append(" , DUTIES = '" + organization.getDuties() + "'");
		}
		if (organization.getIndustryid() != null && !"".equals(organization.getIndustryid())) {
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

		sql.append(" WHERE ORGID = '" + organization.getOrgid() + "'");
		return sql.toString();
	}

	public String updateAtionHangYe(TblOrganization organization) {
		StringBuffer sql = new StringBuffer("UPDATE TBL_ORGANIZATION SET ORGNUMBER = '"+organization.getOrgnumber()+"' ");
		if (organization.getOrgname() != null && !"".equals(organization.getOrgname())) {
			sql.append(" , ORGNAME = '" + organization.getOrgname() + "'");
		}
		if (organization.getFatherorgid() != null && !"".equals(organization.getFatherorgid())) {
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
		if (organization.getOrgtype() != null && !"".equals(organization.getOrgtype())) {
			sql.append(" , ORGTYPE = '" + organization.getOrgtype() + "'");
		}
		if(organization.getAudittype() != null && !"".equals(organization.getAudittype())) {
			sql.append(" , AUDITTYPE = '"+organization.getAudittype()+"'");
		}
		if (organization.getStatus() != null && !"".equals(organization.getStatus())) {
			sql.append(" , STATUS = '" + organization.getStatus() + "'");
		}
		if (organization.getIszy() != null && !"".equals(organization.getIszy())) {
			sql.append(" , ISZY = '" + organization.getIszy() + "'");
		}
		if (organization.getHyzsktype() != null && !"".equals(organization.getHyzsktype())) {
			sql.append(" , HYZSKTYPE = '" + organization.getHyzsktype() + "'");
		}
		if (organization.getOrderid() != null && !"".equals(organization.getOrderid())) {
			sql.append(" , ORDERID = '" + organization.getOrderid() + "'");
		}
		if (organization.getOutsideid() != null && !"".equals(organization.getOutsideid())) {
			sql.append(" , OUTSIDEID = '" + organization.getOutsideid() + "'");
		}
		if (organization.getOutsideopendid() != null && !"".equals(organization.getOutsideopendid())) {
			sql.append(" , OUTSIDEOPENDID = '" + organization.getOutsideopendid() + "'");
		}
		if (organization.getIsautonumber() != null && !"".equals(organization.getIsautonumber())) {
			sql.append(" , ISAUTONUMBER = '" + organization.getIsautonumber() + "'");
		}
		if (organization.getOrgcreate() != null && !"".equals(organization.getOrgcreate())) {
			sql.append(" , ORGCREATE = '" + organization.getOrgcreate() + "'");
		}
		if (organization.getIsinitialization() != null && !"".equals(organization.getIsinitialization())) {
			sql.append(" , ISINITIALIZATION = '" + organization.getIsinitialization() + "'");
		}
		if (organization.getDuties() != null && !"".equals(organization.getDuties())) {
			sql.append(" , DUTIES = '" + organization.getDuties() + "'");
		}
		if (organization.getIndustryid() != null && !"".equals(organization.getIndustryid())) {
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
}
