package com.huabo.compliance.mysql.mapper;


import com.huabo.compliance.mysql.entity.TblSystemOrgRightMySql;
import com.huabo.compliance.mysql.entity.TblSystemRightMySql;

import java.math.BigDecimal;

public class TblSystemRightMapperSqlMySqlConfig {

	
	public String selectAllRightListByCompanye(Integer rightId, BigDecimal orgid, String moduleType) {
		String sql = "SELECT TMR.ID,IFNULL(TORN.RIGHTNAME, TMR.NAME) RIGHTNAME,TMR.ICON,TMR.PARENT,TMR.SORT,TMR.VISIBLE,TMR.ISLINK,TMR.PATH,TMR.PERMS,TMR.TYPE,TMR.MODULETYPE FROM TBL_SYSTEM_RIGHT TMR LEFT JOIN TBL_SYSTEM_ORG_RIGHT TORN ON TMR.ID = TORN.RIGHTID "
				+ " AND TORN.ORGID = "+orgid+" WHERE TMR.VISIBLE = 1 ";
		
		if(moduleType != null && !"".equals(moduleType)) {
			sql += " AND TMR.MODULETYPE = '"+moduleType+"'";
		}
		
		sql += " AND TORN.ORGID = "+orgid+" AND TMR.PARENT = "+rightId+" AND TORN.RIGHTSTATUS = 1 ORDER BY SORT ASC";
		return sql;
	}
	
    public String selectRoleRigetListByType(String roleIdStrs, BigDecimal orgid, String moduleType, Integer type) {
    	String sql = "SELECT DISTINCT TMR.ID,IFNULL(TORN.RIGHTNAME, TMR.NAME) RIGHTNAME,TMR.ICON,TMR.PATH,TMR.PERMS,TMR.TYPE,TMR.COMPONENT,TMR.PARENT,TMR.SORT,TMR.VISIBLE,TMR.ISLINK,TMR.MODULETYPE FROM TBL_SYSTEM_RIGHT TMR LEFT JOIN TBL_SYSTEM_ROLE_RIGHT TMUR ON TMR.ID = TMUR.RIGHTID LEFT JOIN TBL_SYSTEM_ORG_RIGHT TORN ON TMR.ID = TORN.RIGHTID  AND TORN.ORGID = "+orgid+" WHERE TMR.VISIBLE = 1 AND TORN.ORGID = "+orgid+" AND TORN.RIGHTSTATUS = 1 AND TMR.TYPE = "+type;
		if(roleIdStrs != null && !"".equals(roleIdStrs)) {
			sql += " AND TMUR.ROLEID IN ("+roleIdStrs+")";
		}
		
		if(moduleType != null && !"".equals(moduleType)) {
			sql += " AND TMR.MODULETYPE = '"+moduleType+"'";
		}
		sql += " ORDER BY SORT ASC";
		return sql;
    }


    public String selectFatherRightIdStrByRight(String rightId) {
        String sql = "SELECT group_concat(PARENT) FROM TBL_SYSTEM_RIGHT WHERE ID IN (" + rightId + ") ";
        return sql;
    }

    public String selectRightIdStrByRight(TblSystemRightMySql right) {
        String sql = "SELECT group_concat(ID) FROM TBL_SYSTEM_RIGHT WHERE 1 = 1 ";
        if (right.getName() != null && !"".equals(right.getName())) {
            sql += " AND NAME LIKE '%" + right.getName() + "%'";
        }
        if (right.getId() != null) {
            sql += " AND ID = " + right.getId();
        }
        if (right.getComponent() != null && !"".equals(right.getComponent())) {
            sql += " AND COMPONENT LIKE '%" + right.getComponent() + "%'";
        }
        if (right.getIcon() != null && !"".equals(right.getIcon())) {
            sql += " AND ICON LIKE '%" + right.getIcon() + "%'";
        }
        if (right.getIslink() != null) {
            sql += " AND ISLINK = " + right.getIslink();
        }
        if (right.getPath() != null && !"".equals(right.getPath())) {
            sql += " AND PATH LIKE '%" + right.getPath() + "%'";
        }
        if (right.getPerms() != null && !"".equals(right.getPerms())) {
            sql += " AND PERMS LIKE '%" + right.getPerms() + "%'";
        }
        if (right.getType() != null) {
            sql += " AND TYPE LIKE '%" + right.getType() + "%'";
        }
        if (right.getVisible() != null) {
            sql += " AND VISIBLE = " + right.getVisible();
        }
        if (right.getModuletype() != null && !"".equals(right.getModuletype())) {
            sql += " AND MODULETYPE = '" + right.getModuletype() + "'";
        }
        return sql;
    }


    public String selectChildrenRightListByUser(Integer rightId, BigDecimal staffid, BigDecimal orgid, String moduleType) {
        String sql = "SELECT TMR.ID,IFNULL(TORN.RIGHTNAME, TMR.NAME) RIGHTNAME,TMR.ICON,TMR.PARENT,TMR.SORT,TMR.VISIBLE,TMR.PATH,TMR.PERMS,TMR.TYPE,TMR.COMPONENT,TMR.MODULETYPE,TMR.ISLINK FROM TBL_SYSTEM_RIGHT TMR LEFT JOIN TBL_SYSTEM_USER_RIGHT TMUR ON TMR.ID = TMUR.RIGHTID LEFT JOIN TBL_SYSTEM_ORG_RIGHT TORN ON TMR.ID = TORN.RIGHTID  AND TORN.ORGID = " + orgid + " WHERE TMR.VISIBLE = 1 AND TORN.ORGID = " + orgid + " AND TORN.RIGHTSTATUS = 1 AND TMUR.STAFFID = " + staffid;
        if (rightId == 0) {
            sql += " AND TMR.PARENT = " + rightId;
        } else {
            if (moduleType != null && !"".equals(moduleType)) {
                sql += " AND TMR.MODULETYPE = '" + moduleType + "' AND TMR.PARENT = " + rightId;
            }
        }
        sql += " ORDER BY SORT ASC";
        return sql;
    }

    public String selectAllRightListByFatherCompany(Integer rightId, Integer orgid, String moduleType) {
    	String sql = "";
		if(orgid == 1) {
			sql = "SELECT TMR.ID,TMR.NAME RIGHTNAME,TMR.ICON,TMR.PARENT,TMR.SORT,TMR.VISIBLE,TMR.PATH,TMR.PERMS,TMR.TYPE,TMR.COMPONENT,TMR.ISLINK FROM TBL_SYSTEM_RIGHT TMR WHERE TMR.VISIBLE = 1 AND TMR.PARENT = "+rightId;
		}else {
			sql = "SELECT TMR.ID,IFNULL(TORN.RIGHTNAME, TMR.NAME) RIGHTNAME,TMR.ICON,TMR.PARENT,TMR.PATH,TMR.PERMS,TMR.TYPE,TMR.COMPONENT,TMR.SORT,TMR.VISIBLE,TMR.ISLINK FROM TBL_SYSTEM_RIGHT TMR LEFT JOIN TBL_SYSTEM_ORG_RIGHT TORN ON TMR.ID = TORN.RIGHTID WHERE TMR.VISIBLE = 1 AND TMR.PARENT = "+rightId+" AND TORN.ORGID = (SELECT CASE WHEN FATHERORGID = -1 THEN 1 ELSE FATHERORGID END FROM TBL_ORGANIZATION WHERE ORGID = "+orgid+") ";
		}
		if(moduleType != null && !"".equals(moduleType)) {
			sql += " TMR.MODULETYPE = '"+moduleType+"'";
		}
		sql += " ORDER BY SORT ASC";
		return sql;
    }

    public String insertTblManageRight(TblSystemRightMySql right) {
        StringBuffer column = new StringBuffer("INSERT INTO TBL_SYSTEM_RIGHT (ID");
        StringBuffer value = new StringBuffer(" VALUES ("+right.getId()+"");

        if (right.getComponent() != null && !"".equals(right.getComponent())) {
            column.append(",COMPONENT");
            value.append(",'" + right.getComponent() + "'");
        }
        if (right.getIcon() != null && !"".equals(right.getIcon())) {
            column.append(",ICON");
            value.append(",'" + right.getIcon() + "'");
        }
        if (right.getIslink() != null) {
            column.append(",ISLINK");
            value.append(",'" + right.getIslink() + "'");
        }
        if (right.getName() != null && !"".equals(right.getName())) {
            column.append(",NAME");
            value.append(",'" + right.getName() + "'");
        }
        if (right.getParent() != null) {
            column.append(",PARENT");
            value.append(",'" + right.getParent() + "'");
        }
        if (right.getPath() != null && !"".equals(right.getPath())) {
            column.append(",PATH");
            value.append(",'" + right.getPath() + "'");
        }
        if (right.getPerms() != null && !"".equals(right.getPerms())) {
            column.append(",PERMS");
            value.append(",'" + right.getPerms() + "'");
        }
        if (right.getSort() != null) {
            column.append(",SORT");
            value.append(",'" + right.getSort() + "'");
        }
        if (right.getType() != null) {
            column.append(",TYPE");
            value.append(",'" + right.getType() + "'");
        }
        if (right.getVisible() != null) {
            column.append(",VISIBLE");
            value.append(",'" + right.getVisible() + "'");
        }
        if (right.getModuletype() != null && !"".equals(right.getModuletype())) {
            column.append(",MODULETYPE");
            value.append(",'" + right.getModuletype() + "'");
        }
        column.append(")");
        value.append(")");
        String sql = column.toString() + value.toString();
        return sql;
    }

    public String updateTblSystemRight(TblSystemRightMySql right) {
        StringBuffer sql = new StringBuffer("UPDATE TBL_SYSTEM_RIGHT SET NAME = '" + right.getName() + "'");

        if (right.getComponent() != null && !"".equals(right.getComponent())) {
            sql.append(",COMPONENT = '" + right.getComponent() + "'");
        }
        if (right.getIcon() != null && !"".equals(right.getIcon())) {
            sql.append(",ICON = '" + right.getIcon() + "'");
        }
        if (right.getIslink() != null) {
            sql.append(",ISLINK = '" + right.getIslink() + "'");
        }
        if (right.getParent() != null) {
            sql.append(",PARENT = '" + right.getParent() + "'");
        }
        if (right.getPath() != null && !"".equals(right.getPath())) {
            sql.append(",PATH = '" + right.getPath() + "'");
        }
        if (right.getPerms() != null && !"".equals(right.getPerms())) {
            sql.append(",PERMS = '" + right.getPerms() + "'");
        }
        if (right.getSort() != null) {
            sql.append(",SORT = '" + right.getSort() + "'");
        }
        if (right.getType() != null) {
            sql.append(",TYPE = '" + right.getType() + "'");
        }
        if (right.getVisible() != null) {
            sql.append(",visible = '" + right.getVisible() + "'");
        }
        if (right.getModuletype() != null && !"".equals(right.getModuletype())) {
            sql.append(",MODULETYPE = '" + right.getModuletype() + "'");
        }
        sql.append(" WHERE ID = " + right.getId());
        return sql.toString();
    }

    public String updateSystemSettingRight(TblSystemOrgRightMySql orgRight) {
        StringBuffer sql = new StringBuffer("UPDATE TBL_SYSTEM_RIGHT SET RIGHTNAME = '" + orgRight.getRightname() + "'");

        if (orgRight.getRightstatus() != null) {
            sql.append(",RIGHTSTATUS = '" + orgRight.getRightstatus() + "'");
        }
        sql.deleteCharAt(sql.length() - 1);
        sql.append(" WHERE RIGHTID = " + orgRight.getRightid() + " AND ORGID = " + orgRight.getOrgid());
        return sql.toString();
    }

    public String selectChildrenRightListByRole(Integer rightId, String roleIds, BigDecimal orgid, String moduleType) {
    	String sql = "SELECT DISTINCT TMR.ID,IFNULL(TORN.RIGHTNAME, TMR.NAME) RIGHTNAME,TMR.ICON,TMR.PARENT,TMR.PATH,TMR.PERMS,TMR.TYPE,TMR.COMPONENT,TMR.SORT,TMR.VISIBLE,TMR.ISLINK,TMR.MODULETYPE FROM TBL_SYSTEM_RIGHT TMR LEFT JOIN TBL_SYSTEM_ROLE_RIGHT TMUR ON TMR.ID = TMUR.RIGHTID LEFT JOIN TBL_SYSTEM_ORG_RIGHT TORN ON TMR.ID = TORN.RIGHTID  AND TORN.ORGID = "+orgid+" WHERE TMR.VISIBLE = 1 AND TORN.ORGID = "+orgid+" AND TORN.RIGHTSTATUS = 1 AND TMUR.ROLEID IN ("+roleIds+")";
		if(rightId != null) {
			sql += " AND TMR.PARENT = "+rightId;
		}
		
		if(moduleType != null && !"".equals(moduleType)) {
				sql += " AND TMR.MODULETYPE = '"+moduleType+"'";
		}
		sql += " ORDER BY SORT ASC";
		return sql;
    }

    public String selectChildrenRightListByRoleType(Integer rightId, String roleId, BigDecimal orgid, String moduleType, Integer type) {
    	String sql = "SELECT DISTINCT TMR.ID,IFNULL(TORN.RIGHTNAME, TMR.NAME) RIGHTNAME,TMR.ICON,TMR.PARENT,TMR.PATH,TMR.PERMS,TMR.TYPE,TMR.COMPONENT,TMR.SORT,TMR.VISIBLE,TMR.ISLINK,TMR.MODULETYPE,TMR.TYPE FROM TBL_SYSTEM_RIGHT TMR LEFT JOIN TBL_SYSTEM_ROLE_RIGHT TMUR ON TMR.ID = TMUR.RIGHTID LEFT JOIN TBL_SYSTEM_ORG_RIGHT TORN ON TMR.ID = TORN.RIGHTID  AND TORN.ORGID = "+orgid+" WHERE TMR.VISIBLE = 1 AND TORN.ORGID = "+orgid+" AND TORN.RIGHTSTATUS = 1 AND TMUR.ROLEID IN ("+roleId+")  AND TMR.PARENT = "+rightId+ " AND TMR.TYPE = "+type;
		if(moduleType != null && !"".equals(moduleType)) {
				sql += " AND TMR.MODULETYPE = '"+moduleType+"'";
		}
		sql += " ORDER BY SORT ASC";
		return sql;
    }
}
