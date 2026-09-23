package com.huabo.compliance.mysql.mapper;

import com.hbfk.util.PageInfo;
import com.huabo.compliance.entity.Find;
import com.huabo.compliance.mysql.entity.FindMySql;
import com.huabo.compliance.mysql.entity.TblOrganizationMySql;

import java.math.BigDecimal;

public class TblOrganizationMapperSqlMySqlConifg {


    public String selectListByPageInfo(PageInfo<TblOrganizationMySql> pageInfo) {

        StringBuffer sqlSb = new StringBuffer("SELECT * FROM (SELECT BUDGET.* FROM (SELECT org.* FROM TBL_ORGANIZATION org where ORGTYPE = 100 ");
        sqlSb.append(" ORDER BY ORGID desc ) BUDGET LIMIT " + pageInfo.getCurrentRecord() + " , " + pageInfo.getPageSize() + " ) AS A ");
        String sql = sqlSb.toString();
        return sql;
    }

    public String selectListByPageInOrgid(PageInfo<TblOrganizationMySql> pageInfo, BigDecimal orgid) {
        StringBuffer sbSql = new StringBuffer("SELECT * FROM TBL_ORGANIZATION");
        sbSql.append("WHERE orgid= " + orgid + "");
        sbSql.append(" T1 WHERE LIMIT " + pageInfo.getCurrentRecord() + " , " + pageInfo.getPageSize());
        return sbSql.toString();

    }

//	public String findByPageBean(PageInfo<TblOrganization> pageInfo, BigDecimal orgid,Find find) {
//		StringBuffer sbSql = new StringBuffer("SELECT ORGID,ORGNAME,ORGNUMBER,ORGMENO,MEMO,ORGTYPE,STATUS,ISZY,ISAUTONUMBER FROM ( SELECT ORGID,ORGNAME,ORGNUMBER,ORGMENO,MEMO,ORGTYPE,STATUS,ISZY,ISAUTONUMBER,@rownum := @rownum +1 RN FROM ( select ORGID,ORGNAME,ORGNUMBER,ORGMENO,MEMO,ORGTYPE,STATUS,ISZY,ISAUTONUMBER from TBL_ORGANIZATION org where ORGTYPE != 0 START WITH ORG.FATHERORGID = "+orgid+"");
//		if (find != null && find.getCode() != null && find.getCode().length() > 0) {
//			sbSql.append(" and ORGNUMBER like '%" + find.getCode() + "%' ");
//		}
//		if (find != null && find.getName() != null && find.getName().length() > 0) {
//			sbSql.append( " and ORGNAME  like '%" + find.getName() + "%' ");
//		}
//		sbSql.append("  CONNECT BY PRIOR ORG.ORGID = ORG.FATHERORGID ORDER BY orgtype asc,ORGID desc,rownum ASC ) T1 WHERE @rownum := @rownum +1 <= "+(pageInfo.getCurrentPage()*pageInfo.getPageSize())+" ) T2 WHERE T2.RN > "+pageInfo.getCurrentRecord());
//		return sbSql.toString();
//	}

    public String findByPageBean(PageInfo<TblOrganizationMySql> pageInfo, BigDecimal orgid, Find find) {
        StringBuffer sbSql = new StringBuffer("SELECT * FROM ( SELECT BUDGET.* FROM (select ORG.* from TBL_ORGANIZATION ORG where ORGTYPE != 0 ");
        if (find != null && find.getCode() != null && find.getCode().length() > 0) {
            sbSql.append(" and ORG.ORGNUMBER like '%" + find.getCode() + "%' ");
        }
        if (find != null && find.getName() != null && find.getName().length() > 0) {
            sbSql.append(" and ORG.ORGNAME  like '%" + find.getName() + "%' ");
        }
        sbSql.append("and getOrgIdList(ORG.FATHERORGID= " + orgid + ") ORDER BY ORG.orgtype asc,ORG.ORGID desc) BUDGET LIMIT " + pageInfo.getCurrentRecord() + " , " + pageInfo.getPageSize() + " )as A")  ;
        String sql = sbSql.toString();
        return sql;
    }

    public String findByPage(PageInfo<TblOrganizationMySql> pageInfo, BigDecimal orgid, Find find) {
        StringBuffer sbSql = new StringBuffer("SELECT COUNT(*) FROM (select * from TBL_ORGANIZATION ORG where ORG.ORGTYPE != 0 ");
        if (find != null && find.getCode() != null && find.getCode().length() > 0) {
            sbSql.append(" and ORG.ORGNUMBER like '%" + find.getCode() + "%' ");
        }
        if (find != null && find.getName() != null && find.getName().length() > 0) {
            sbSql.append(" and ORG.ORGNAME  like '%" + find.getName() + "%' ");
        }
        sbSql.append("and getOrgIdList(ORG.FATHERORGID= " + orgid + ")) as a");
        return sbSql.toString();
    }


    public String selectListByPageInfoOrgid(PageInfo<TblOrganizationMySql> pageInfo, FindMySql find, BigDecimal pid) {
        StringBuffer sbSql = new StringBuffer("SELECT * FROM ( SELECT BUDGET.* FROM (select * from TBL_ORGANIZATION where 1=1 ");
        if (find != null && find.getCode() != null && find.getCode().length() > 0) {
            sbSql.append(" and ORGNUMBER like '%" + find.getCode() + "%' ");
        }
        if (find != null && find.getName() != null && find.getName().length() > 0) {
            sbSql.append(" and ORGNAME  like '%" + find.getName() + "%' ");
        }
        sbSql.append("and getOrgIdList(FATHERORGID= " + pid + ") and ORGTYPE=0 ORDER BY ORGID desc,orderid ASC) BUDGET LIMIT " + pageInfo.getCurrentRecord() + " , " + pageInfo.getPageSize() + " ) as a ");
        String sql = sbSql.toString();
        return sql;
    }

    public String findAllCommpanyPageBeanStaffid(PageInfo<TblOrganizationMySql> pageInfo, BigDecimal pid) {
//		TblOrganization organization = pageInfo.getCondition();
        StringBuffer sbSql = new StringBuffer("SELECT * FROM (SELECT BUDGET.* FROM (select org.* from TBL_ORGANIZATION org where ORGTYPE != 0 and ORGTYPE<100 and status=0  and ORG.FATHERORGID = " + pid + "   ORDER BY orgid,orderid ASC");
        sbSql.append(") BUDGET LIMIT " + pageInfo.getCurrentRecord() + " , " + pageInfo.getPageSize() + " ) as a");
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
//		sbSql.append(") T1 WHERE @rownum := @rownum +1 rownum <= "+(pageInfo.getCurrentPage()*pageInfo.getPageSize())+" ) T2 WHERE T2.RNUM > "+pageInfo.getCurrentRecord());
//		return sbSql.toString();
//	}

    public String selectListByPageInfoo(PageInfo<TblOrganizationMySql> pageInfo) {
        StringBuffer sbSql = new StringBuffer("SELECT * FROM TBL_ORGANIZATION");
        sbSql.append(" T1 LIMIT " + pageInfo.getCurrentRecord() + " , " + pageInfo.getPageSize());
        return sbSql.toString();
    }

    public String findAllCommpanyPageBeanGSXj(PageInfo<TblOrganizationMySql> pageInfo, FindMySql find, String orgId) {
        StringBuffer sbSql = new StringBuffer("SELECT ORGID,ORGNAME,ORGNUMBER,ORGMENO,MEMO,ORGTYPE,STATUS,ISZY FROM ( SELECT ORGID,ORGNAME,ORGNUMBER,ORGMENO,MEMO,ORGTYPE,STATUS,ISZY FROM ( select ORGID,ORGNAME,ORGNUMBER,ORGMENO,MEMO,ORGTYPE,STATUS,ISZY from TBL_ORGANIZATION org where ORGTYPE != 0 and getOrgIdList(ORG.FATHERORGID = " + orgId + ")");
        if (find != null && find.getCode() != null && find.getCode().length() > 0) {
            sbSql.append(" and ORGNUMBER like '%" + find.getCode() + "%' ");
        }
        if (find != null && find.getName() != null && find.getName().length() > 0) {
            sbSql.append(" and ORGNAME  like '%" + find.getName() + "%' ");
        }

        sbSql.append(" ORDER BY orgtype asc,ORGID desc, ) T1 LIMIT " + pageInfo.getCurrentRecord() + " , " + pageInfo.getPageSize() + " ) as a");
        return sbSql.toString();

    }


    public String findAllCommpanyPage(PageInfo<TblOrganizationMySql> pageInfo, BigDecimal pid) {
        StringBuffer sbSql = new StringBuffer("SELECT ORGID,ORGNAME,FATHERORGID,ORGNUMBER,ORGMENO,MEMO,ICODE,ORGTYPE,AUDITTYPE,STATUS,ISZY,HYZSKTYPE,ORDERID,OUTSIDEID,OUTSIDEOPENDID,ISAUTONUMBER,ORGCREATE,ISINITIALIZATION,DUTIES,INDUSTRYID,BYWX,DATASOURCE,HISTORYCODE,HISTORYDEPARTMENTID FROM(");
        sbSql.append("SELECT T1.ORGID,T1.ORGNAME,T1.FATHERORGID,T1.ORGNUMBER,T1.ORGMENO,T1.MEMO,T1.ICODE,T1.ORGTYPE,T1.AUDITTYPE,T1.STATUS,T1.ISZY,T1.HYZSKTYPE,T1.ORDERID,T1.OUTSIDEID,T1.OUTSIDEOPENDID,T1.ISAUTONUMBER,T1.ORGCREATE,T1.ISINITIALIZATION,T1.DUTIES,T1.INDUSTRYID,T1.BYWX,T1.DATASOURCE,T1.HISTORYCODE,T1.HISTORYDEPARTMENTID FROM(");
        sbSql.append("SELECT T1.ORGID,T1.ORGNAME,T1.FATHERORGID,T1.ORGNUMBER,T1.ORGMENO,T1.MEMO,T1.ICODE,T1.ORGTYPE,T1.AUDITTYPE,T1.STATUS,T1.ISZY,T1.HYZSKTYPE,T1.ORDERID,T1.OUTSIDEID,T1.OUTSIDEOPENDID,T1.ISAUTONUMBER,T1.ORGCREATE,T1.ISINITIALIZATION,T1.DUTIES,T1.INDUSTRYID,T1.BYWX,T1.DATASOURCE,T1.HISTORYCODE,T1.HISTORYDEPARTMENTID FROM(");
        sbSql.append("select count(*) from TBL_ORGANIZATION org");
        sbSql.append("where ORGTYPE != 0  and ORGTYPE<100 and status=0  and ORG.FATHERORGID = " + pid + "   ORDER BY orgtype,orderid ASC");
        sbSql.append(") T1 LIMIT " + pageInfo.getCurrentRecord() + " , " + pageInfo.getPageSize() + " ) ");
        return sbSql.toString();
    }

    public String selectRepearBudgetName(String budgetname, BigDecimal orgid, String budgetId) {
        String sql = "SELECT COUNT(0) FROM TBL_CYHW_PROJECTBUDGET WHERE BUDGETNAME ='" + budgetname + "' AND ORGID = '" + orgid + "'";
        if (budgetId != null) {
            sql += " AND BUDGETID != " + budgetId;
        }
        return sql;
    }

    public String selectStaffInfoByModuleIdList(PageInfo<TblOrganizationMySql> pageInfo, Integer moduleId) throws Exception {
        StringBuffer sqlSb = new StringBuffer("SELECT * FROM (SELECT BUDGET.* FROM (SELECT ORGID,ORGNUMBER,ORGNAME FROM TBL_ORGANIZATION WHERE ORGID IN (SELECT ORGID FROM TBL_SYSTEM_MODELORG WHERE MODELID = " + moduleId);
        if (pageInfo.getCondition().getOrgname() != null && !"".equals(pageInfo.getCondition().getOrgname())) {
            sqlSb.append(" AND ORGNAME LIKE '%" + pageInfo.getCondition().getOrgname() + "%'");
        }
        sqlSb.append(")) BUDGET LIMIT " + pageInfo.getCurrentRecord() + " , " + pageInfo.getPageSize() + " ) as a ");
        String sql = sqlSb.toString();
        return sql;
    }

    public String selectStaffInfoByModuleIdCount(PageInfo<TblOrganizationMySql> pageInfo, Integer moduleId) throws Exception {
        String sqlCount = "SELECT COUNT(*) FROM TBL_ORGANIZATION WHERE ORGID IN (SELECT ORGID FROM TBL_SYSTEM_MODELORG WHERE MODELID = #{moduleId})";

        if (pageInfo.getCondition().getOrgname() != null && !"".equals(pageInfo.getCondition().getOrgname())) {
            sqlCount += " AND ORGNAME LIKE '%" + pageInfo.getCondition().getOrgname() + "%'";
        }
        return sqlCount;
    }

    public String selectListByPid(PageInfo<TblOrganizationMySql> pageInfo, String pid) {
        StringBuffer sqlSb = new StringBuffer("SELECT * FROM (SELECT BUDGET.* FROM (SELECT org.* FROM TBL_ORGANIZATION org where ORGTYPE = 100 and FATHERORGID= " + pid);
        sqlSb.append(" ORDER BY ORGID desc ) BUDGET LIMIT " + pageInfo.getCurrentRecord() + " , " + pageInfo.getPageSize() + " ) AS A " );
        String sql = sqlSb.toString();
        return sql;
    }

    public String selectListByStaffid(PageInfo<TblOrganizationMySql> pageInfo, String pid) {
        TblOrganizationMySql organizationMySql = new TblOrganizationMySql();
        StringBuffer sbSql = new StringBuffer("SELECT * FROM (SELECT BUDGET.* FROM (SELECT org.* FROM TBL_ORGANIZATION org where ORGTYPE = 100 and FATHERORGID= " + pid + "  ORDER BY ORGID desc");
        sbSql.append(") BUDGET LIMIT " + pageInfo.getCurrentRecord() + " , " + pageInfo.getPageSize() + " ) AS A ");
        return sbSql.toString();
    }

    public String getNodesa(PageInfo<TblOrganizationMySql> pageInfo) {
        TblOrganizationMySql organizationMySql = new TblOrganizationMySql();
        StringBuffer sbSql = new StringBuffer("SELECT * FROM (SELECT BUDGET.* FROM (select org.* from TBL_ORGANIZATION org order by orderid asc");
        sbSql.append(") BUDGET LIMIT " + pageInfo.getCurrentRecord() + " , " + pageInfo.getPageSize() + " ) AS A");
        return sbSql.toString();
    }

    public String getNodesaNodeId(PageInfo<TblOrganizationMySql> pageInfo, BigDecimal nodeId) {
        StringBuffer sbSql = new StringBuffer("SELECT * FROM (SELECT BUDGET.* FROM (select org.* from tbl_organization org where ORGID = " + nodeId + "  and orgtype < 100 ORDER BY orderid ASC");
        sbSql.append(") BUDGET LIMIT " + pageInfo.getCurrentRecord() + " , " + pageInfo.getPageSize() + " ) AS A ");
        return sbSql.toString();
    }

    public String saveModiOrganization(TblOrganizationMySql organization) {
        StringBuffer sql = new StringBuffer("UPDATE TBL_ORGANIZATION SET ORGNUMBER = '" + organization.getOrgnumber() + "' ");
        if (organization.getOrgname() != null && !"".equals(organization.getOrgname())) {
            sql.append(" , ORGNAME = '" + organization.getOrgname() + "'");
        }
        if (organization.getFatherorgid() != null && !"".equals(organization.getFatherorgid())) {
            sql.append(" , FATHERORGID = '" + organization.getFatherorgid() + "'");
        }
//		if(organization.getOrgnumber() != null && !"".equals(organization.getOrgnumber())) {
//			sql.append(" , ORGNUMBER = '"+organization.getOrgnumber()+"'");
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
        if (organization.getAuditType() != null && !"".equals(organization.getAuditType())) {
            sql.append(" , AUDITTYPE = '" + organization.getAuditType() + "'");
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

    public String saveAtionHangYe(TblOrganizationMySql organization) {
        StringBuffer column = new StringBuffer("INSERT INTO TBL_ORGANIZATION (ORGID");
        StringBuffer value = new StringBuffer(" VALUES ("+ organization.getOrgid() + "");

        if (organization.getOrgname() != null) {
            column.append(",ORGNAME");
            value.append(",'" + organization.getOrgname() + "'");
        }
        if (organization.getFatherorgid() != null) {
            column.append(",FATHERORGID");
            value.append(",'" + organization.getFatherorgid() + "'");
        }
        if (organization.getOrgnumber() != null) {
            column.append(",ORGNUMBER");
            value.append(",'" + organization.getOrgnumber() + "'");
        }
        if (organization.getOrgmeno() != null) {
            column.append(",ORGMENO");
            value.append(",'" + organization.getOrgmeno() + "'");
        }
        if (organization.getMemo() != null) {
            column.append(",MEMO");
            value.append(",'" + organization.getMemo() + "'");
        }
        if (organization.getIcode() != null) {
            column.append(",ICODE");
            value.append(",'" + organization.getIcode() + "'");
        }
        if (organization.getOrgtype() != null) {
            column.append(",ORGTYPE");
            value.append(",'" + organization.getOrgtype() + "'");
        }
        if (organization.getAuditType() != null) {
            column.append(",AUDITTYPE");
            value.append(",'" + organization.getAuditType() + "'");
        }
        if (organization.getStatus() != null) {
            column.append(",STATUS");
            value.append(",'" + organization.getStatus() + "'");
        }
        if (organization.getIszy() != null) {
            column.append(",ISZY");
            value.append(",'" + organization.getIszy() + "'");
        }
        if (organization.getHyzsktype() != null) {
            column.append(",HYZSKTYPE");
            value.append(",'" + organization.getHyzsktype() + "'");
        }
        if (organization.getOrderid() != null) {
            column.append(",ORDERID");
            value.append(",'" + organization.getOrderid() + "'");
        }
        if (organization.getOutsideid() != null) {
            column.append(",OUTSIDEID");
            value.append(",'" + organization.getOutsideid() + "'");
        }
        if (organization.getOutsideopendid() != null) {
            column.append(",OUTSIDEOPENDID");
            value.append(",'" + organization.getOutsideopendid() + "'");
        }
        if (organization.getIsautonumber() != null) {
            column.append(",ISAUTONUMBER");
            value.append(",'" + organization.getIsautonumber() + "'");
        }
        if (organization.getOrgcreate() != null) {
            column.append(",ORGCREATE");
            value.append(",'" + organization.getOrgcreate() + "'");
        }
        if (organization.getIsinitialization() != null) {
            column.append(",ISINITIALIZATION");
            value.append(",'" + organization.getIsinitialization() + "'");
        }
        if (organization.getDuties() != null) {
            column.append(",DUTIES");
            value.append(",'" + organization.getDuties() + "'");
        }
        if (organization.getIndustryid() != null) {
            column.append(",INDUSTRYID");
            value.append(",'" + organization.getIndustryid() + "'");
        }
        if (organization.getBywx() != null) {
            column.append(",BYWX");
            value.append(",'" + organization.getBywx() + "'");
        }
        if (organization.getDatasource() != null) {
            column.append(",DATASOURCE");
            value.append(",'" + organization.getDatasource() + "'");
        }
        if (organization.getHistorycode() != null) {
            column.append(",HISTORYCODE");
            value.append(",'" + organization.getHistorycode() + "'");
        }
        if (organization.getHistorydepartmentid() != null) {
            column.append(",HISTORYDEPARTMENTID");
            value.append(",'" + organization.getHistorydepartmentid() + "'");
        }
        column.append(")");
        value.append(")");
        String sql = column.toString() + value.toString();
        return sql;
    }

    public String addReturnId(TblOrganizationMySql organization) {
        StringBuffer column = new StringBuffer("INSERT INTO TBL_ORGANIZATION (ORGID");
        StringBuffer value = new StringBuffer(" VALUES ( "+ organization.getOrgid() + " ");

        if (organization.getOrgname() != null) {
            column.append(",ORGNAME");
            value.append(",'" + organization.getOrgname() + "'");
        }
        if (organization.getFatherorgid() != null) {
            column.append(",FATHERORGID");
            value.append(",'" + organization.getFatherorgid() + "'");
        }
        if (organization.getOrgnumber() != null) {
            column.append(",ORGNUMBER");
            value.append(",'" + organization.getOrgnumber() + "'");
        }
        if (organization.getOrgmeno() != null) {
            column.append(",ORGMENO");
            value.append(",'" + organization.getOrgmeno() + "'");
        }
        if (organization.getMemo() != null) {
            column.append(",MEMO");
            value.append(",'" + organization.getMemo() + "'");
        }
        if (organization.getIcode() != null) {
            column.append(",ICODE");
            value.append(",'" + organization.getIcode() + "'");
        }
        if (organization.getOrgtype() != null) {
            column.append(",ORGTYPE");
            value.append(",'" + organization.getOrgtype() + "'");
        }
        if (organization.getAuditType() != null) {
            column.append(",AUDITTYPE");
            value.append(",'" + organization.getAuditType() + "'");
        }
        if (organization.getStatus() != null) {
            column.append(",STATUS");
            value.append(",'" + organization.getStatus() + "'");
        }
        if (organization.getIszy() != null) {
            column.append(",ISZY");
            value.append(",'" + organization.getIszy() + "'");
        }
        if (organization.getHyzsktype() != null) {
            column.append(",HYZSKTYPE");
            value.append(",'" + organization.getHyzsktype() + "'");
        }
        if (organization.getOrderid() != null) {
            column.append(",ORDERID");
            value.append(",'" + organization.getOrderid() + "'");
        }
        if (organization.getOutsideid() != null) {
            column.append(",OUTSIDEID");
            value.append(",'" + organization.getOutsideid() + "'");
        }
        if (organization.getOutsideopendid() != null) {
            column.append(",OUTSIDEOPENDID");
            value.append(",'" + organization.getOutsideopendid() + "'");
        }
        if (organization.getIsautonumber() != null) {
            column.append(",ISAUTONUMBER");
            value.append(",'" + organization.getIsautonumber() + "'");
        }
        if (organization.getOrgcreate() != null) {
            column.append(",ORGCREATE");
            value.append(",'" + organization.getOrgcreate() + "'");
        }
        if (organization.getIsinitialization() != null) {
            column.append(",ISINITIALIZATION");
            value.append(",'" + organization.getIsinitialization() + "'");
        }
        if (organization.getDuties() != null) {
            column.append(",DUTIES");
            value.append(",'" + organization.getDuties() + "'");
        }
        if (organization.getIndustryid() != null) {
            column.append(",INDUSTRYID");
            value.append(",'" + organization.getIndustryid() + "'");
        }
        if (organization.getBywx() != null) {
            column.append(",BYWX");
            value.append(",'" + organization.getBywx() + "'");
        }
        if (organization.getDatasource() != null) {
            column.append(",DATASOURCE");
            value.append(",'" + organization.getDatasource() + "'");
        }
        if (organization.getHistorycode() != null) {
            column.append(",HISTORYCODE");
            value.append(",'" + organization.getHistorycode() + "'");
        }
        if (organization.getHistorydepartmentid() != null) {
            column.append(",HISTORYDEPARTMENTID");
            value.append(",'" + organization.getHistorydepartmentid() + "'");
        }
        column.append(")");
        value.append(")");
        String sql = column.toString() + value.toString();
        return sql;
    }

    public String saveModiOrganiza(TblOrganizationMySql organization) {
        StringBuffer column = new StringBuffer("INSERT INTO TBL_ORGANIZATION (ORGID");
        StringBuffer value = new StringBuffer(" VALUES ("+organization.getOrgid()+" ");

        if (organization.getOrgname() != null) {
            column.append(",ORGNAME");
            value.append(",'" + organization.getOrgname() + "'");
        }
        if (organization.getFatherorgid() != null) {
            column.append(",FATHERORGID");
            value.append(",'" + organization.getFatherorgid() + "'");
        }
        if (organization.getOrgnumber() != null) {
            column.append(",ORGNUMBER");
            value.append(",'" + organization.getOrgnumber() + "'");
        }
        if (organization.getOrgmeno() != null) {
            column.append(",ORGMENO");
            value.append(",'" + organization.getOrgmeno() + "'");
        }
        if (organization.getMemo() != null) {
            column.append(",MEMO");
            value.append(",'" + organization.getMemo() + "'");
        }
        if (organization.getIcode() != null) {
            column.append(",ICODE");
            value.append(",'" + organization.getIcode() + "'");
        }
        if (organization.getOrgtype() != null) {
            column.append(",ORGTYPE");
            value.append(",'" + organization.getOrgtype() + "'");
        }
//		if(organization.getAudittype()!= null) {
//			column.append(",AUDITTYPE");
//			value.append(",'"+organization.getAudittype()+"'");
//		}
        if (organization.getStatus() != null) {
            column.append(",STATUS");
            value.append(",'" + organization.getStatus() + "'");
        }
        if (organization.getIszy() != null) {
            column.append(",ISZY");
            value.append(",'" + organization.getIszy() + "'");
        }
        if (organization.getHyzsktype() != null) {
            column.append(",HYZSKTYPE");
            value.append(",'" + organization.getHyzsktype() + "'");
        }
        if (organization.getOrderid() != null) {
            column.append(",ORDERID");
            value.append(",'" + organization.getOrderid() + "'");
        }
        if (organization.getOutsideid() != null) {
            column.append(",OUTSIDEID");
            value.append(",'" + organization.getOutsideid() + "'");
        }
        if (organization.getOutsideopendid() != null) {
            column.append(",OUTSIDEOPENDID");
            value.append(",'" + organization.getOutsideopendid() + "'");
        }
        if (organization.getIsautonumber() != null) {
            column.append(",ISAUTONUMBER");
            value.append(",'" + organization.getIsautonumber() + "'");
        }
        if (organization.getOrgcreate() != null) {
            column.append(",ORGCREATE");
            value.append(",'" + organization.getOrgcreate() + "'");
        }
        if (organization.getIsinitialization() != null) {
            column.append(",ISINITIALIZATION");
            value.append(",'" + organization.getIsinitialization() + "'");
        }
        if (organization.getDuties() != null) {
            column.append(",DUTIES");
            value.append(",'" + organization.getDuties() + "'");
        }
        if (organization.getIndustryid() != null) {
            column.append(",INDUSTRYID");
            value.append(",'" + organization.getIndustryid() + "'");
        }
        if (organization.getBywx() != null) {
            column.append(",BYWX");
            value.append(",'" + organization.getBywx() + "'");
        }
        if (organization.getDatasource() != null) {
            column.append(",DATASOURCE");
            value.append(",'" + organization.getDatasource() + "'");
        }
        if (organization.getHistorycode() != null) {
            column.append(",HISTORYCODE");
            value.append(",'" + organization.getHistorycode() + "'");
        }
        if (organization.getHistorydepartmentid() != null) {
            column.append(",HISTORYDEPARTMENTID");
            value.append(",'" + organization.getHistorydepartmentid() + "'");
        }
        column.append(")");
        value.append(")");
        String sql = column.toString() + value.toString();
        return sql;
    }


    public String saveModiOrgan(TblOrganizationMySql organization) {
        StringBuffer column = new StringBuffer("INSERT INTO TBL_ORGANIZATION (ORGID");
        StringBuffer value = new StringBuffer(" VALUES ("+organization.getOrgid()+" ");

        if (organization.getOrgname() != null) {
            column.append(",ORGNAME");
            value.append(",'" + organization.getOrgname() + "'");
        }
        if (organization.getFatherorgid() != null) {
            column.append(",FATHERORGID");
            value.append(",'" + organization.getFatherorgid() + "'");
        }
        if (organization.getOrgnumber() != null) {
            column.append(",ORGNUMBER");
            value.append(",'" + organization.getOrgnumber() + "'");
        }
        if (organization.getOrgmeno() != null) {
            column.append(",ORGMENO");
            value.append(",'" + organization.getOrgmeno() + "'");
        }
        if (organization.getMemo() != null) {
            column.append(",MEMO");
            value.append(",'" + organization.getMemo() + "'");
        }
        if (organization.getIcode() != null) {
            column.append(",ICODE");
            value.append(",'" + organization.getIcode() + "'");
        }
        if (organization.getOrgtype() != null) {
            column.append(",ORGTYPE");
            value.append(",'" + organization.getOrgtype() + "'");
        }
//		if(organization.getAudittype()!= null) {
//			column.append(",AUDITTYPE");
//			value.append(",'"+organization.getAudittype()+"'");
//		}
        if (organization.getStatus() != null) {
            column.append(",STATUS");
            value.append(",'" + organization.getStatus() + "'");
        }
        if (organization.getIszy() != null) {
            column.append(",ISZY");
            value.append(",'" + organization.getIszy() + "'");
        }
        if (organization.getHyzsktype() != null) {
            column.append(",HYZSKTYPE");
            value.append(",'" + organization.getHyzsktype() + "'");
        }
        if (organization.getOrderid() != null) {
            column.append(",ORDERID");
            value.append(",'" + organization.getOrderid() + "'");
        }
        if (organization.getOutsideid() != null) {
            column.append(",OUTSIDEID");
            value.append(",'" + organization.getOutsideid() + "'");
        }
        if (organization.getOutsideopendid() != null) {
            column.append(",OUTSIDEOPENDID");
            value.append(",'" + organization.getOutsideopendid() + "'");
        }
        if (organization.getIsautonumber() != null) {
            column.append(",ISAUTONUMBER");
            value.append(",'" + organization.getIsautonumber() + "'");
        }
        if (organization.getOrgcreate() != null) {
            column.append(",ORGCREATE");
            value.append(",'" + organization.getOrgcreate() + "'");
        }
        if (organization.getIsinitialization() != null) {
            column.append(",ISINITIALIZATION");
            value.append(",'" + organization.getIsinitialization() + "'");
        }
        if (organization.getDuties() != null) {
            column.append(",DUTIES");
            value.append(",'" + organization.getDuties() + "'");
        }
        if (organization.getIndustryid() != null) {
            column.append(",INDUSTRYID");
            value.append(",'" + organization.getIndustryid() + "'");
        }
        if (organization.getBywx() != null) {
            column.append(",BYWX");
            value.append(",'" + organization.getBywx() + "'");
        }
        if (organization.getDatasource() != null) {
            column.append(",DATASOURCE");
            value.append(",'" + organization.getDatasource() + "'");
        }
        if (organization.getHistorycode() != null) {
            column.append(",HISTORYCODE");
            value.append(",'" + organization.getHistorycode() + "'");
        }
        if (organization.getHistorydepartmentid() != null) {
            column.append(",HISTORYDEPARTMENTID");
            value.append(",'" + organization.getHistorydepartmentid() + "'");
        }
        column.append(")");
        value.append(")");
        String sql = column.toString() + value.toString();
        return sql;
    }

    public String updateZuZhi(TblOrganizationMySql organization) {
        StringBuffer sql = new StringBuffer("UPDATE TBL_ORGANIZATION SET ORGNUMBER = '" + organization.getOrgnumber() + "' ");
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
        if (organization.getAuditType() != null && !"".equals(organization.getAuditType())) {
            sql.append(" , AUDITTYPE = '" + organization.getAuditType() + "'");
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

    public String updateAtionHangYe(TblOrganizationMySql organization) {
        StringBuffer sql = new StringBuffer("UPDATE TBL_ORGANIZATION SET ORGNUMBER = '" + organization.getOrgnumber() + "' ");
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
        if (organization.getAuditType() != null && !"".equals(organization.getAuditType())) {
            sql.append(" , AUDITTYPE = '" + organization.getAuditType() + "'");
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
}
