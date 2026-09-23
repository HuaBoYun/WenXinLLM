package com.huabo.system.mapper;

import java.math.BigDecimal;

import com.hbfk.util.database.DataBaseSqlConfig;
import com.huabo.system.entity.TblSystemOrgRight;
import com.huabo.system.entity.TblSystemRight;

public class TblSystemRightMapperSqlConfig {
	
	public String selectAllRightIdListByRoleId(Integer type, BigDecimal roleId, String moduleType) throws Exception{
		String sql = "SELECT TMR.ID FROM TBL_SYSTEM_RIGHT TMR LEFT JOIN TBL_SYSTEM_ROLE_RIGHT TORN ON TMR.ID = TORN.RIGHTID "
				+ "AND TORN.ROLEID = "+roleId+" WHERE TMR.VISIBLE = 1 ";
		
		if(moduleType != null && !"".equals(moduleType)) {
			sql += " AND TMR.MODULETYPE = '"+moduleType+"'";
		}
		
		sql += " AND TORN.ROLEID = "+roleId+" AND TMR.TYPE = "+type+" ORDER BY SORT ASC";
		return sql;
	}
	
	public String selectAllRightListByCompanye(Integer type, BigDecimal orgid, String moduleType) throws Exception {
		String sql = "SELECT TMR.ID,"+DataBaseSqlConfig.getNullColumn("TORN.RIGHTNAME", "TMR.NAME")+" RIGHTNAME,TMR.ICON,TMR.PARENT,TMR.SORT,TMR.VISIBLE,TMR.ISLINK,TMR.PATH,TMR.PERMS,TMR.TYPE,TMR.MODULETYPE,TMR.SECRECTLEVELID,TSL.LEVELNAME AS SECRECTLEVELNAME FROM TBL_SYSTEM_RIGHT TMR LEFT JOIN TBL_SECRECT_LEVEL TSL ON TMR.SECRECTLEVELID = TSL.LEVELID LEFT JOIN TBL_SYSTEM_ORG_RIGHT TORN ON TMR.ID = TORN.RIGHTID "
				+ "AND TORN.ORGID = "+orgid+" WHERE TMR.VISIBLE = 1 ";
		
		if(moduleType != null && !"".equals(moduleType)) {
			sql += " AND TMR.MODULETYPE = '"+moduleType+"'";
		}
		
		sql += " AND TORN.ORGID = "+orgid+" AND TMR.TYPE = "+type+" AND TORN.RIGHTSTATUS = 1 ORDER BY SORT ASC";
		return sql;
	}
	
	
	public String selectRoleRigetListByType(String username,String roleIdStrs, BigDecimal orgid, String moduleType,Integer type) throws Exception {
		String sql = "SELECT DISTINCT TMR.ID,"+DataBaseSqlConfig.getNullColumn("TORN.RIGHTNAME", "TMR.NAME")+" AS RIGHTNAME,TMR.ICON,TMR.PATH,TMR.PERMS,TMR.TYPE,"
				+ "TMR.COMPONENT,TMR.PARENT,TMR.SORT,TMR.VISIBLE,TMR.ISLINK,TMR.MODULETYPE,TMR.SECRECTLEVELID,TCL.LEVELNAME "
				+ "FROM TBL_SYSTEM_RIGHT TMR "
				+ "LEFT JOIN TBL_SECRECT_LEVEL TCL ON TMR.SECRECTLEVELID = TCL.LEVELID "
				+ "LEFT JOIN TBL_SYSTEM_ROLE_RIGHT TMUR ON TMR.ID = TMUR.RIGHTID "
				+ "LEFT JOIN TBL_SYSTEM_ORG_RIGHT TORN ON TMR.ID = TORN.RIGHTID  AND TORN.ORGID = "+orgid+" "
						+ "WHERE TMR.VISIBLE = 1 AND TORN.ORGID = "+orgid+" AND TORN.RIGHTSTATUS = 1 AND TMR.TYPE = "+type;
		if(roleIdStrs != null && !"".equals(roleIdStrs)) {
			sql +=  " AND TMUR.ROLEID IN (SELECT ROLEID FROM TBL_ORG_ROLE WHERE ORGID = "+orgid+" AND ROLEID IN ("+roleIdStrs+"))";
		}
//		if (username !=null && !"".equals(username)){
//			sql +=" AND (EXISTS (SELECT 1 FROM TBL_SECRECT_LEVEL WHERE LEVELID = " +
//					"(SELECT SECRECTLEVELID FROM TBL_STAFF WHERE USERNAME = 'rq' " +
//					" ) AND ','||SECRECYSTAFFSCOPE||',' LIKE '%,'||TMR.SECRECTLEVELID||',%') " +
//					" OR TMR.SECRECTLEVELID IS NULL)";
//		}
		if(moduleType != null && !"".equals(moduleType)) {
			sql += " AND TMR.MODULETYPE = '"+moduleType+"'";
		}
		sql += " ORDER BY SORT ASC";
		return sql;
	}
	
	
	
	public String selectFatherRightIdStrByRight(String rightId, String allRightIds) {
		String sql = "SELECT PARENT FROM TBL_SYSTEM_RIGHT WHERE ID IN ("+rightId+") AND ID NOT IN ("+allRightIds+") ";
		return sql;
	}
	
	public String selectRightIdStrByRight(TblSystemRight right) {
		String sql = "SELECT ID FROM TBL_SYSTEM_RIGHT WHERE 1 = 1 ";
		if(right.getName() != null && !"".equals(right.getName())){
			sql += " AND NAME LIKE '%"+right.getName()+"%'";
		}
		if(right.getId() != null) {
			sql += " AND ID = "+right.getId();
		}
		if(right.getComponent() != null && !"".equals(right.getComponent())) {
			sql += " AND COMPONENT LIKE '%"+right.getComponent()+"%'";
		}
		if(right.getIcon() != null && !"".equals(right.getIcon())) {
			sql += " AND ICON LIKE '%"+right.getIcon()+"%'";
		}
		if(right.getIslink() != null) {
			sql += " AND ISLINK = "+right.getIslink();
		}
		if(right.getPath() != null && !"".equals(right.getPath())) {
			sql += " AND PATH LIKE '%"+right.getPath()+"%'";
		}
		if(right.getPerms() != null && !"".equals(right.getPerms())) {
			sql += " AND PERMS LIKE '%"+right.getPerms()+"%'";
		}
		if(right.getType() != null) {
			sql += " AND TYPE LIKE '%"+right.getType()+"%'";
		}
		if(right.getVisible() != null) {
			sql += " AND VISIBLE = "+right.getVisible();
		}
		if(right.getModuletype() != null && !"".equals(right.getModuletype())) {
			sql += " AND MODULETYPE = '"+right.getModuletype()+"'";
		}
		return sql;
	}
	
	
	public String selectChildrenRightListByUser(BigDecimal rightId, BigDecimal staffid, BigDecimal orgid,String moduleType) throws Exception {
		String sql = "SELECT TMR.ID,"+DataBaseSqlConfig.getNullColumn("TORN.RIGHTNAME", "TMR.NAME")+" RIGHTNAME,TMR.ICON,TMR.PARENT,TMR.SORT,TMR.VISIBLE,TMR.PATH,TMR.PERMS,TMR.TYPE,TMR.COMPONENT,TMR.MODULETYPE,TMR.ISLINK FROM TBL_SYSTEM_RIGHT TMR LEFT JOIN TBL_SYSTEM_USER_RIGHT TMUR ON TMR.ID = TMUR.RIGHTID LEFT JOIN TBL_SYSTEM_ORG_RIGHT TORN ON TMR.ID = TORN.RIGHTID  AND TORN.ORGID = "+orgid+" WHERE TMR.VISIBLE = 1 AND TORN.ORGID = "+orgid+" AND TORN.RIGHTSTATUS = 1 AND TMUR.STAFFID = "+staffid;
		if(orgid.compareTo(BigDecimal.valueOf(1)) == 0) {
			sql += " AND TMR.PARENT = "+rightId;
		}else {
			if(moduleType != null && !"".equals(moduleType)) {
				sql += " AND TMR.MODULETYPE = '"+moduleType+"' AND TMR.PARENT = "+rightId;
			}
		}
		sql += " ORDER BY SORT ASC";
		return sql;
	}
	
	public String selectAllRightListByFatherCompany(Integer type, BigDecimal orgid, String moduleType, BigDecimal checkOrgId) throws Exception {
		String sql = "";
		if(orgid.compareTo(BigDecimal.valueOf(1)) == 0) {
			sql = "SELECT TMR.ID,TMR.NAME RIGHTNAME,TMR.ICON,TMR.PARENT,TMR.SORT,TMR.VISIBLE,TMR.PATH,TMR.PERMS,TMR.TYPE,TMR.COMPONENT,TMR.ISLINK,TMR.SECRECTLEVELID,TSL.LEVELNAME AS SECRECTLEVELNAME,(SELECT COUNT(0) FROM TBL_SYSTEM_ORG_RIGHT WHERE ORGID = 1 AND RIGHTID = TMR.ID ) AS ISCHECKED FROM TBL_SYSTEM_RIGHT TMR LEFT JOIN TBL_SECRECT_LEVEL TSL ON TMR.SECRECTLEVELID = TSL.LEVELID WHERE TMR.VISIBLE = 1 AND TMR.TYPE = "+type;
		}else {
			sql = "SELECT TMR.ID,"+DataBaseSqlConfig.getNullColumn("TORN.RIGHTNAME", "TMR.NAME")+" AS RIGHTNAME,TMR.ICON,TMR.PARENT,TMR.PATH,TMR.PERMS,TMR.TYPE,TMR.SECRECTLEVELID,TSL.LEVELNAME AS SECRECTLEVELNAME,TMR.COMPONENT,TMR.SORT,TMR.VISIBLE,TMR.ISLINK,(SELECT COUNT(0) FROM TBL_SYSTEM_ORG_RIGHT WHERE ORGID = "+checkOrgId+" AND RIGHTID = TMR.ID ) AS ISCHECKED FROM TBL_SYSTEM_RIGHT TMR LEFT JOIN TBL_SECRECT_LEVEL TSL ON TMR.SECRECTLEVELID = TSL.LEVELID LEFT JOIN TBL_SYSTEM_ORG_RIGHT TORN ON TMR.ID = TORN.RIGHTID WHERE TMR.VISIBLE = 1 AND TMR.TYPE = "+type+" AND TORN.ORGID = (SELECT CASE WHEN FATHERORGID = -1 THEN 1 ELSE FATHERORGID END FROM TBL_ORGANIZATION WHERE ORGID = "+orgid+") ";
		}
		if(moduleType != null && !"".equals(moduleType)) {
			sql += " AND TMR.MODULETYPE = '"+moduleType+"'";
		}
		sql += " ORDER BY SORT ASC";
		return sql;
	}
	
	public String insertTblManageRight(TblSystemRight right) {
		StringBuffer column = new StringBuffer("INSERT INTO TBL_SYSTEM_RIGHT (ID");
		StringBuffer value = new StringBuffer(" VALUES ("+right.getId());
		
		if(right.getComponent() != null && !"".equals(right.getComponent())) {
			column.append(",COMPONENT");
			value.append(",'"+right.getComponent()+"'");
		}
		if(right.getIcon() != null && !"".equals(right.getIcon())) {
			column.append(",ICON");
			value.append(",'"+right.getIcon()+"'");
		}
		if(right.getIslink() != null) {
			column.append(",ISLINK");
			value.append(",'"+right.getIslink()+"'");
		}
		if(right.getName() != null && !"".equals(right.getName())) {
			column.append(",NAME");
			value.append(",'"+right.getName()+"'");
		}
		if(right.getParent() != null) {
			column.append(",PARENT");
			value.append(",'"+right.getParent()+"'");
		}
		if(right.getPath() != null && !"".equals(right.getPath())) {
			column.append(",PATH");
			value.append(",'"+right.getPath()+"'");
		}
		if(right.getPerms() != null && !"".equals(right.getPerms())) {
			column.append(",PERMS");
			value.append(",'"+right.getPerms()+"'");
		}
		if(right.getSort() != null) {
			column.append(",SORT");
			value.append(",'"+right.getSort()+"'");
		}
		if(right.getType() != null ) {
			column.append(",TYPE");
			value.append(",'"+right.getType()+"'");
		}
		if(right.getVisible() != null) {
			column.append(",VISIBLE");
			value.append(",'"+right.getVisible()+"'");
		}
		if(right.getModuletype() != null && !"".equals(right.getModuletype())) {
			column.append(",MODULETYPE");
			value.append(",'"+right.getModuletype()+"'");
		}
		if(right.getSecrectLevelId() != null) {
			column.append(",SECRECTLEVELID");
			value.append(",'"+right.getSecrectLevelId()+"'");
		}
		column.append(")");
		value.append(")");
		String sql = column.toString()+value.toString();
		return sql;
	}
	
	public String updateTblSystemRight(TblSystemRight right) {
    	StringBuffer sql = new StringBuffer("UPDATE TBL_SYSTEM_RIGHT SET NAME = '"+right.getName()+"'");
		
    	if(right.getComponent() != null && !"".equals(right.getComponent())) {
			sql.append(",COMPONENT = '"+right.getComponent()+"'");
		}
    	if(right.getIcon() != null && !"".equals(right.getIcon())) {
			sql.append(",ICON = '"+right.getIcon()+"'");
		}
    	if(right.getIslink() != null) {
			sql.append(",ISLINK = '"+right.getIslink()+"'");
		}
    	if(right.getParent() != null) {
			sql.append(",PARENT = '"+right.getParent()+"'");
		}
    	if(right.getPath() != null && !"".equals(right.getPath())) {
			sql.append(",PATH = '"+right.getPath()+"'");
		}
    	if(right.getPerms() != null && !"".equals(right.getPerms())) {
			sql.append(",PERMS = '"+right.getPerms()+"'");
		}
    	if(right.getSort() != null) {
			sql.append(",SORT = '"+right.getSort()+"'");
		}
    	if(right.getType() != null ) {
			sql.append(",TYPE = '"+right.getType()+"'");
		}
    	if(right.getVisible() != null) {
			sql.append(",visible = '"+right.getVisible()+"'");
		}
    	if(right.getModuletype() != null && !"".equals(right.getModuletype())) {
			sql.append(",MODULETYPE = '"+right.getModuletype()+"'");
		}
    	if(right.getSecrectLevelId() != null) {
			sql.append(",SECRECTLEVELID = '"+right.getSecrectLevelId()+"'");
		}
		sql.append(" WHERE ID = "+right.getId());
		return sql.toString();
    }
	
	public String updateSystemSettingRight(TblSystemOrgRight orgRight) {
		StringBuffer sql = new StringBuffer("UPDATE TBL_SYSTEM_RIGHT SET RIGHTNAME = '"+orgRight.getRightname()+"'");
		
    	if(orgRight.getRightstatus() != null ) {
			sql.append(",RIGHTSTATUS = '"+orgRight.getRightstatus()+"'");
		}
		sql.deleteCharAt(sql.length()-1);
		sql.append(" WHERE RIGHTID = "+orgRight.getRightid()+" AND ORGID = "+orgRight.getOrgid());
		return sql.toString();
	}
	
	public String selectChildrenRightListByRole(BigDecimal rightId, String roleIds, BigDecimal orgid,String moduleType) throws Exception {
		String sql = "SELECT DISTINCT TMR.ID,"+DataBaseSqlConfig.getNullColumn("TORN.RIGHTNAME", "TMR.NAME")+" AS RIGHTNAME,TMR.ICON,TMR.PARENT,TMR.PATH,TMR.PERMS,TMR.TYPE,TMR.COMPONENT,TMR.SORT,TMR.VISIBLE,TMR.ISLINK,TMR.MODULETYPE,TMR.SECRECTLEVELID,TSL.LEVELNAME AS SECRECTLEVELNAME FROM TBL_SYSTEM_RIGHT TMR LEFT JOIN TBL_SECRECT_LEVEL TSL ON TMR.SECRECTLEVELID = TSL.LEVELID LEFT JOIN TBL_SYSTEM_ROLE_RIGHT TMUR ON TMR.ID = TMUR.RIGHTID  LEFT JOIN TBL_SYSTEM_ORG_RIGHT TORN ON TMR.ID = TORN.RIGHTID  AND TORN.ORGID = "+orgid+" WHERE TMR.VISIBLE = 1 AND TORN.ORGID = "+orgid+" AND TORN.RIGHTSTATUS = 1 AND TMUR.ROLEID IN ("+roleIds+")";
		if(rightId != null) {
			sql += " AND TMR.PARENT = "+rightId;
		}
		
		if(moduleType != null && !"".equals(moduleType)) {
				sql += " AND TMR.MODULETYPE = '"+moduleType+"'";
		}
		sql += " ORDER BY SORT ASC";
		return sql;
	}
	
	public String selectChildrenRightListByRoleType(BigDecimal rightId, String roleId, BigDecimal orgid,String moduleType) throws Exception {
		String sql = "SELECT DISTINCT TMR.ID,"+DataBaseSqlConfig.getNullColumn("TORN.RIGHTNAME", "TMR.NAME")+" AS RIGHTNAME,TMR.ICON,TMR.PARENT,TMR.PATH,TMR.PERMS,TMR.TYPE,TMR.COMPONENT,TMR.SORT,TMR.VISIBLE,TMR.ISLINK,TMR.MODULETYPE,TMR.TYPE FROM TBL_SYSTEM_RIGHT TMR LEFT JOIN TBL_SYSTEM_ROLE_RIGHT TMUR ON TMR.ID = TMUR.RIGHTID LEFT JOIN TBL_SYSTEM_ORG_RIGHT TORN ON TMR.ID = TORN.RIGHTID  AND TORN.ORGID = "+orgid+" WHERE TMR.VISIBLE = 1 AND TORN.ORGID = "+orgid+" AND TORN.RIGHTSTATUS = 1 AND TMUR.ROLEID IN (SELECT ROLEID FROM TBL_ORG_ROLE WHERE ORGID = "+orgid+" AND ROLEID IN ("+roleId+")) AND TMR.PARENT = "+rightId;
		
		if(moduleType != null && !"".equals(moduleType)) {
				sql += " AND TMR.MODULETYPE = '"+moduleType+"'";
		}
		sql += " ORDER BY SORT ASC";
		return sql;
	}
}

