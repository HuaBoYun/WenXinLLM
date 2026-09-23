package com.huabo.system.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hbfk.util.PageInfo;
import com.hbfk.util.database.DataBaseSqlConfig;
import com.huabo.system.entity.TblOrganization;
import com.huabo.system.entity.TblRole;
import com.huabo.system.entity.TblVideoType;

import java.math.BigDecimal;


public class TblRoleDaoSqlConfig {
	
	public String findBysqlobj(String rid) throws Exception {
		String sql = "SELECT * from TBL_STAFF where "+DataBaseSqlConfig.getWhereColumnInStr("ROLEIDSTRS", rid, ",")+" AND (STATUS is NULL or STATUS != 0)";
		return sql;
	}

	public String selectReaprtRoleName(String rname, BigDecimal rid, BigDecimal pid) {
		String sql = "SELECT COUNT(0) FROM TBL_ROLE WHERE RNAME = '"+rname+"' AND companyid = "+pid;
		if(rid != null){
			sql += " AND RID != "+rid;
		}
		return sql;
	}
	
    public String selectListByPageInfo(IPage<TblRole> page,BigDecimal companyid,TblRole role, String orgIds, String roleName, String orgName) throws Exception {

        StringBuffer sbSql = new StringBuffer("SELECT TR.*,(SELECT COUNT(0) FROM TBL_SYSTEM_ROLE_RIGHT WHERE ROLEID = TR.RID ) AS RIGHTCOUNT FROM TBL_ROLE TR WHERE 1 = 1 AND TR.RNAME not in ('安全管理员','系统管理员','安全审计员')  " );
        if(orgIds != null && !"".equals(orgIds)) {
        	sbSql.append(" AND RID IN (SELECT ROLEID FROM TBL_ORG_ROLE WHERE ORGID IN ("+orgIds+"))");
        }
        if(companyid != null){
        	sbSql.append(" AND ( TR.companyid = "+companyid).append(" OR TR.RID IN (SELECT ROLEID FROM TBL_ORG_ROLE WHERE ORGID = ").append(companyid).append("))");
        }
        
        if (role != null) {
            if (role.getRid() != null) {
                sbSql.append(" and TR.rid like '%" + role.getRid() + "%'");
            }

            if (role.getRname() != null && !role.getRname().equals("")) {
                sbSql.append(" and TR.RNAME like '%" + role.getRname() + "%'");
            }
        }
        
        /*if(orgName != null && !"".equals(orgName)) {
        	sbSql.append(" AND ORG.ORGNAME LIKE '%"+orgName+"%'");
        }*/

        sbSql.append(" ORDER BY TR.RID DESC");
        String sql = sbSql.toString();
        return sql;
    }
    
    public String saveTblRole(TblRole tr) {
        StringBuffer column = new StringBuffer("INSERT INTO TBL_ROLE (RID");
        StringBuffer value = new StringBuffer(" VALUES ("+tr.getRid());

        if(tr.getRname() != null) {
            column.append(",RNAME");
            value.append(",'"+tr.getRname()+"'");
        }
        if(tr.getRdesc() != null) {
            column.append(",RDESC");
            value.append(",'"+tr.getRdesc()+"'");
        }
        if(tr.getRstatus() != null) {
            column.append(",RSTATUS");
            value.append(",'"+tr.getRstatus()+"'");
        }
        if(tr.getCompanyid() != null) {
            column.append(",COMPANYID");
            value.append(",'"+tr.getCompanyid()+"'");
        }

        column.append(")");
        value.append(")");
        String sql = column.toString()+value.toString();
        return sql;
    }


    public String updateTblRole(TblRole role) {
        StringBuffer sql = new StringBuffer("UPDATE TBL_ROLE SET RNAME = '"+role.getRname()+"' ");

//        if(role.getRname() != null && !"".equals(role.getRname())) {
//            sql.append(" , RNAME = '"+role.getRname()+"'");
//        }
        if(role.getRdesc() != null && !"".equals(role.getRdesc())) {
            sql.append(" , RDESC = '"+role.getRdesc()+"'");
        }
        if(role.getRstatus() != null && !"".equals(role.getRstatus())) {
            sql.append(" , RSTATUS = '"+role.getRstatus()+"'");
        }
        if(role.getCompanyid() != null && !"".equals(role.getCompanyid())) {
            sql.append(" , COMPANYID = '"+role.getCompanyid()+"'");
        }
        sql.append(" WHERE RID = '"+role.getRid()+"'");
        return sql.toString();
    }
}