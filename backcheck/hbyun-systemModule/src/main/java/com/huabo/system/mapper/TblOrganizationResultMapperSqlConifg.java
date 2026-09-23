package com.huabo.system.mapper;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.hbfk.util.database.DataBaseSqlConfig;
import com.huabo.system.vo.param.TblOrganizationQueryParam;

public class TblOrganizationResultMapperSqlConifg {
	
	public String selectListByPageInfo(TblOrganizationQueryParam query) throws Exception {
		StringBuffer sqlSb = new StringBuffer("SELECT * FROM TBL_ORGANIZATION WHERE 1 = 1");
		if(StringUtils.isNotBlank(query.getOrgIdStrs())) {
			sqlSb.append(" AND "+DataBaseSqlConfig.getInOrNotSql("ORGID", "IN", "OR", Arrays.asList(query.getOrgIdStrs().split(","))));
		}
		
		if(query.getOrgIdList() != null && query.getOrgIdList().size() > 0) {
			sqlSb.append(" AND "+DataBaseSqlConfig.getInOrNotSql("ORGID", "IN", "OR", query.getOrgIdList()));
		}
		
		if(StringUtils.isNotBlank(query.getOrgname())) {
			sqlSb.append(" AND ORGNAME LIKE '%").append(query.getOrgname()).append("%'");
		}
		if(StringUtils.isNotBlank(query.getOrgnumber())) {
			sqlSb.append(" AND ORGNUMBER LIKE '%").append(query.getOrgnumber()).append("%'");
		}
/*		if(query.getFatherorgid() != null) {
			sqlSb.append(" AND FATHERORGID = ").append(query.getFatherorgid());
		}*/
		if(StringUtils.isNotBlank(query.getOrgmeno())) {
			sqlSb.append(" AND ORGMENO LIKE '%").append(query.getOrgmeno()).append("%'");
		}
		if(StringUtils.isNotBlank(query.getMemo())) {
			sqlSb.append(" AND MEMO LIKE '%").append(query.getMemo()).append("%'");
		}
		if(StringUtils.isNotBlank(query.getIcode())) {
			sqlSb.append(" AND ICODE LIKE '%").append(query.getIcode()).append("%'");
		}
		if(query.getIndustryid() != null) {
			sqlSb.append(" AND INDUSTRYID = ").append(query.getIndustryid());
		}
		if(query.getStatus() != null) {
			sqlSb.append(" AND STATUS = ").append(query.getStatus());
		}
		return sqlSb.toString();
	}
	
	public String selectRootTreeInfoById(TblOrganizationQueryParam query) throws Exception{
		StringBuffer sqlSb = new StringBuffer("SELECT ORGID,ORGNAME,FATHERORGID,ORGTYPE,BGIMAGE,LOGOIMAGE,JDZTIMAGE,CTZTIMAGE,BGNAME,LOGONAME,JDZTNAME,CTZTNAME,BANAME FROM TBL_ORGANIZATION WHERE ORGID = '").append(query.getFatherorgid()).append("'");
		if(query.getStatus() != null) {
			sqlSb.append(" AND STATUS = '").append(query.getStatus()).append("'");
		}
		sqlSb.append(" ORDER BY ORGID ASC ");
		return sqlSb.toString();
	}
	
	public String selectTreeListInfoByFatherId(TblOrganizationQueryParam query) throws Exception{
		StringBuffer sqlSb = new StringBuffer("SELECT ORGID,ORGNAME,FATHERORGID FROM TBL_ORGANIZATION WHERE FATHERORGID = '").append(query.getFatherorgid()).append("'");
		if(query.getStatus() != null) {
			sqlSb.append(" AND STATUS = '").append(query.getStatus()).append("'");
		}
		if(query.getOrgtype() != null) {
			if(query.getOrgtype() == -2) {
				sqlSb.append(" AND ORGTYPE > 0 AND ORGTYPE <= 100");
			}else if(query.getOrgtype() == -1) {
				sqlSb.append(" AND ORGTYPE = 0");
			}
			
		}
		sqlSb.append(" ORDER BY ORDERID, ORGID ASC ");
		return sqlSb.toString();
	}
	
	public String selectTreeListInfoByOrgId(TblOrganizationQueryParam query) throws Exception{
		StringBuffer sqlSb = new StringBuffer("SELECT ORGID,ORGNAME,FATHERORGID,ORGTYPE,BGIMAGE,LOGOIMAGE,JDZTIMAGE,CTZTIMAGE,BGNAME,LOGONAME,JDZTNAME,CTZTNAME,BANAME FROM TBL_ORGANIZATION WHERE FATHERORGID = '").append(query.getOrgid()).append("'");
		if(query.getStatus() != null) {
			sqlSb.append(" AND STATUS = '").append(query.getStatus()).append("'");
		}
		if(query.getOrgtype() != null) {
			if(query.getOrgtype() == -2) {
				sqlSb.append(" AND ORGTYPE > 0 AND ORGTYPE <= 100");
			}else if(query.getOrgtype() == -1) {
				sqlSb.append(" AND ORGTYPE = 0");
			}
			
		}
		sqlSb.append(" ORDER BY ORGTYPE,ORDERID,ORGNUMBER, ORGID ASC ");
		return sqlSb.toString();
	}
	
	public String selectAllOrgIdByOrgName(TblOrganizationQueryParam query) throws Exception{
		StringBuffer sqlSb = new StringBuffer("SELECT ORGID FROM TBL_ORGANIZATION WHERE 1 = 1 ");
		
		if(StringUtils.isNotBlank(query.getOrgname())) {
			sqlSb.append(" AND ORGNAME LIKE '%").append(query.getOrgname()).append("%'");
		}
		
		if(query.getStatus() != null) {
			sqlSb.append(" AND STATUS = '").append(query.getStatus()).append("'");
		}
		if(query.getOrgtype() != null) {
			if(query.getOrgtype() == -2) {
				sqlSb.append(" AND ORGTYPE > 0 AND ORGTYPE <= 100");
			}else if(query.getOrgtype() == -1) {
				sqlSb.append(" AND ORGTYPE = 0");
			}else if(query.getOrgtype() == -3) {
				sqlSb.append(" AND ORGTYPE = 100");
			}
		}
		sqlSb.append("ORDER BY ORGTYPE,ORDERID");
		return sqlSb.toString();
	}
	
	public String selectAllOrgIdTreeByOrgName(TblOrganizationQueryParam query) throws Exception{
		StringBuffer sqlSb = new StringBuffer("SELECT ORGANIZATIONTREES FROM TBL_ORGANIZATION WHERE ORGANIZATIONTREES IS NOT NULL ");
		
		/*if(query.getFatherorgid() != null) {
			sqlSb.append(" AND ").append(DataBaseSqlConfig.getWhereColumnInStr("ORGANIZATIONTREES", query.getFatherorgid().toString(), ","));
		}*/
		
		if(StringUtils.isNotBlank(query.getOrgname())) {
			sqlSb.append(" AND ORGNAME LIKE '%").append(query.getOrgname()).append("%'");
		}
		
		if(query.getStatus() != null) {
			sqlSb.append(" AND STATUS = '").append(query.getStatus()).append("'");
		}
		if(query.getOrgtype() != null) {
			if(query.getOrgtype() == -2) {
				sqlSb.append(" AND ORGTYPE > 0 AND ORGTYPE <= 100");
			}else if(query.getOrgtype() == -1) {
				sqlSb.append(" AND ORGTYPE = 0");
			}else if(query.getOrgtype() == -3) {
				sqlSb.append(" AND ORGTYPE = 100");
			}
		}
		sqlSb.append(" ORDER BY ORGTYPE ASC,ORDERID ASC ");
		return sqlSb.toString();
	}
	
	public String  selectAllListByOrgtypeAndOrgIds(int orgtype, String allOrgIdStrs,TblOrganizationQueryParam query) throws Exception{
		StringBuffer sqlSb = new StringBuffer("SELECT ORGID,ORGNAME,FATHERORGID FROM TBL_ORGANIZATION WHERE ORGTYPE = ").append(orgtype).append(" AND ORGID IN (").append(allOrgIdStrs).append(")");
		if(query.getStatus() != null) {
			sqlSb.append(" AND STATUS = '").append(query.getStatus()).append("'");
		}
		if(query.getOrgtype() != null) {
			if(query.getOrgtype() == -2) {
				sqlSb.append(" AND ORGTYPE > 0 AND ORGTYPE <= 100");
			}else if(query.getOrgtype() == -1) {
				sqlSb.append(" AND ORGTYPE = 0");
			}else if(query.getOrgtype() == -3) {
				sqlSb.append(" AND ORGTYPE = 100");
			}
		}
		sqlSb.append(" ORDER BY ORDERID , ORGID ASC ");
		return sqlSb.toString();
	}
	
	public String selectChilrenListByOrgIdScope(BigDecimal orgid,  String orgIdsSql, TblOrganizationQueryParam query) throws Exception {
		StringBuffer sqlSb = new StringBuffer("SELECT ORGID,ORGNAME,ORGNUMBER,FATHERORGID,STATUS,ORGTYPE FROM TBL_ORGANIZATION WHERE FATHERORGID = ").append(orgid).append(" AND ").append(orgIdsSql);
		if(query.getStatus() != null) {
			sqlSb.append(" AND STATUS = '").append(query.getStatus()).append("'");
		}
		if(query.getOrgtype() != null) {
			if(query.getOrgtype() == -2) {
				sqlSb.append(" AND ORGTYPE > 0 AND ORGTYPE <= 100");
			}else if(query.getOrgtype() == -1) {
				sqlSb.append(" AND ORGTYPE = 0");
			}
			
		}
		
		sqlSb.append(" ORDER BY ORGTYPE,ORDERID,ORGNUMBER, ORGID ASC ");
		return sqlSb.toString();
	}
	
	public String selectChildrenCompanyIds(String choiceIds, Integer status) {
		StringBuffer sqlSb = new StringBuffer("SELECT ORGID FROM TBL_ORGANIZATION WHERE FATHERORGID IN (").append(choiceIds).append(") AND ORGTYPE > 0 AND ORGTYPE < 100");
		if(status != null) {
			sqlSb.append(" AND STATUS = '").append(status).append("'");
		}
		return sqlSb.toString();
	}
	
	public String selectChildrenDepartmentIds(String choiceIds, Integer status) {
/*		StringBuffer sqlSb = new StringBuffer("SELECT ORGID FROM ( SELECT ORGID, ORGTYPE, STATUS FROM REDACTED.TBL_ORGANIZATION" +
				" START WITH FATHERORGID IN ").append(choiceIds).append(" CONNECT BY PRIOR ORGID = FATHERORGID " +
				")t WHERE t.ORGTYPE = 0 ");
		if(status != null) {
			sqlSb.append(" AND STATUS = '").append(status).append("'");*/
//		}
		StringBuffer sqlSb = new StringBuffer("SELECT ORGID FROM TBL_ORGANIZATION WHERE FATHERORGID IN (").append(choiceIds).append(") AND ORGTYPE = 0 ");
		if(status != null) {
			sqlSb.append(" AND STATUS = '").append(status).append("'");
		}
		return sqlSb.toString();
	}
	
	public String selectAllChildrenIds(String orgId, Integer orgtype) {
		StringBuffer sqlSb = new StringBuffer("SELECT ORGID FROM TBL_ORGANIZATION WHERE FATHERORGID IN (").append(orgId).append(") AND STATUS = 0 ");
		if(orgtype != null) {
			sqlSb.append(" AND ORGTYPE = '").append(orgtype).append("'");
		}
		return sqlSb.toString();
	}
	
	public String selectOrgIdsByallOrgIdStrs(List<String> orgStrIds,  List<String> allOrgIdStrs) throws Exception{
		StringBuffer sqlSb = new StringBuffer("SELECT ORGID FROM TBL_ORGANIZATION WHERE ORGID IN (SELECT FATHERORGID FROM TBL_ORGANIZATION WHERE ");
		sqlSb.append(DataBaseSqlConfig.getInOrNotSql("ORGID", "IN", "OR", orgStrIds));
		sqlSb.append(") AND ").append(DataBaseSqlConfig.getInOrNotSql("ORGID", "NOT IN", "AND", allOrgIdStrs));
		sqlSb.append("ORDER BY ORGTYPE,ORDERID");
		String sql = sqlSb.toString();
		return sql;
	}
	
	public String selectRootOrgListByOrgIds(List<String> allOrgIdList) throws Exception{
		StringBuffer sqlSb = new StringBuffer("SELECT ORGID,ORGNAME,ORGNUMBER,FATHERORGID,ORGTYPE FROM TBL_ORGANIZATION WHERE ");
		sqlSb.append(DataBaseSqlConfig.getInOrNotSql("ORGID", "IN", "OR", allOrgIdList));
		sqlSb.append(" AND ORGTYPE != 0 AND FATHERORGID = -1 ");
		String sql = sqlSb.toString();
		return sql;
	}
	
	public String selectOrgListByIds(List<String> enList) throws Exception {
		StringBuffer sqlSb = new StringBuffer("SELECT ORGID,ORGNAME,ORGNUMBER,FATHERORGID,ORGTYPE,BGIMAGE,LOGOIMAGE,JDZTIMAGE,CTZTIMAGE,BGNAME,LOGONAME,JDZTNAME,CTZTNAME,BANAME FROM TBL_ORGANIZATION WHERE ");
		sqlSb.append(DataBaseSqlConfig.getInOrNotSql("ORGID", "IN", "OR", enList));
		sqlSb.append(" ORDER BY ORGTYPE,ORDERID,ORGNUMBER, ORGID ASC ");
		String sql = sqlSb.toString();
		return sql;
	}
	
	public String selectAllCompanyChildrenIdsByTrees(String orgId, Integer status) throws Exception{
		StringBuffer sqlSb = new StringBuffer("SELECT ORGID FROM TBL_ORGANIZATION WHERE ORGTYPE > 0 AND ORGTYPE <= 100 AND ");
		sqlSb.append(DataBaseSqlConfig.getWhereColumnInStr("ORGANIZATIONTREES", orgId, ","));
		if(status != null) {
			sqlSb.append(" AND STATUS = '").append(status).append("'");
		}
		sqlSb.append(" ORDER BY ORGTYPE,ORDERID");
		String sql = sqlSb.toString();
		return sql;
	}
	
	public String selectAllChildrenIdsByTrees(String orgId, Integer status) throws Exception {
		StringBuffer sqlSb = new StringBuffer("SELECT ORGID FROM TBL_ORGANIZATION WHERE ");
		sqlSb.append(DataBaseSqlConfig.getWhereColumnInStr("ORGANIZATIONTREES", orgId, ","));
		if(status != null) {
			sqlSb.append(" AND STATUS = '").append(status).append("'");
		}
		String sql = sqlSb.toString();
		return sql;
	}
	
}
