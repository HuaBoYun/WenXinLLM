package com.huabo.contract.mappersql;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hbfk.util.DateUtil;
import com.hbfk.util.database.DataBaseSqlConfig;
import com.huabo.contract.entity.TblContractSpnode;

public class TblContractSpnodeMapperSqlConfig {

	public String findListByXdf(IPage<TblContractSpnode> page, String budgetid) throws Exception {
		// 
		StringBuffer sqlSb = new StringBuffer("SELECT TCU.CONTRACTNO,TCU.CONTRACTNAME,TCU.STARTDATE,TCU.ENDDATE,TCU.CONTRACTID," +
				"TCS.NODEMEMO,TCS.ISWY,TCS.NODECONTENT,TCS.WYCONTENT,TCS.SPNODEID,TCS.NODEPOST " +
				"FROM TBL_CONTRACT_SPNODE TCS LEFT JOIN TBL_CYHW_UNIT TCU ON TCS.CONTRACTID = TCU.CONTRACTID " +
				"WHERE TCS.ISWY='是'  AND TCS.NODEPOST IS NOT NULL AND TCS.CONTRACTID IN (SELECT CONTRACTID FROM TBL_CONTRACT_BUDGET WHERE BUDGETID = '" + budgetid);
		sqlSb.append("' ) ");
		String sql = sqlSb.toString();
		return sql;
	}
	
	public String updateBySpnode(TblContractSpnode spNode) throws Exception {
		StringBuffer sql = new StringBuffer("UPDATE TBL_CONTRACT_SPNODE SET NODEID = '"+spNode.getNodeid()+"'");

		if(spNode.getNodestatus() != null && !"".equals(spNode.getNodestatus())) {
			sql.append(" ,NODESTATUS = '"+spNode.getNodestatus()+"'");
		}
		if(spNode.getNodememo() != null && !"".equals(spNode.getNodememo())) {
			sql.append(" ,NODEMEMO = '"+spNode.getNodememo()+"'");
		}
		if(spNode.getNodecontent() != null && !"".equals(spNode.getNodecontent())) {
			sql.append(" ,NODECONTENT = '"+spNode.getNodecontent()+"'");
		}
		if(spNode.getNodemoney() != null) {
			sql.append(" ,NODEMONEY = '"+spNode.getNodemoney()+"'");
		}
		if(spNode.getFormid() != null) {
			sql.append(" ,FORMID = '"+spNode.getFormid()+"'");
		}
		if(spNode.getContractid() != null) {
			sql.append(" ,CONTRACTID = '"+spNode.getContractid()+"'");
		}
		if(spNode.getNodepost() != null && !"".equals(spNode.getNodepost())) {
			sql.append(" ,NODEPOST = '"+spNode.getNodepost()+"'");
		}
		if(spNode.getIswy() != null && !"".equals(spNode.getIswy())) {
			sql.append(" ,ISWY = '"+spNode.getIswy()+"'");
		}
		if(spNode.getStartdate() != null) {
			sql.append(" ,STARTDATE = "+DataBaseSqlConfig.getDateStrFormat(spNode.getStartdate()));
		}
		if(spNode.getEnddate() != null) {
			sql.append(" ,ENDDATE = "+DataBaseSqlConfig.getDateStrFormat(spNode.getEnddate()));
		}
		if(spNode.getNodefinishdate() != null) {
			sql.append(" ,NODEFINISHDATE = "+DataBaseSqlConfig.getDateStrFormat(spNode.getNodefinishdate()));
		}
		if(spNode.getNodeplanfinishdate() != null) {
			sql.append(" ,NODEPLANFINISHDATE = "+DataBaseSqlConfig.getDateStrFormat(spNode.getNodeplanfinishdate()));
		}
		sql.append(" WHERE SPNODEID = '"+spNode.getSpnodeid()+"'");
		return sql.toString();
	}

	public String saveBySpnode(TblContractSpnode spNode) throws Exception {
		StringBuffer column = new StringBuffer("INSERT INTO TBL_CONTRACT_SPNODE (SPNODEID");
		StringBuffer value = new StringBuffer(" VALUES ("+spNode.getSpnodeid());

		if(spNode.getNodeid() != null) {
			column.append(",NODEID");
			value.append(",'"+spNode.getNodeid()+"'");
		}
		if(spNode.getNodestatus() != null && !"".equals(spNode.getNodestatus())) {
			column.append(",NODESTATUS");
			value.append(",'"+spNode.getNodestatus()+"'");
		}
		if(spNode.getNodememo() != null && !"".equals(spNode.getNodememo())) {
			column.append(",NODEMEMO");
			value.append(",'"+spNode.getNodememo()+"'");
		}
		if(spNode.getNodecontent() != null && !"".equals(spNode.getNodecontent())) {
			column.append(",NODECONTENT");
			value.append(",'"+spNode.getNodecontent()+"'");
		}
		if(spNode.getNodemoney() != null) {
			column.append(",NODEMONEY");
			value.append(",'"+spNode.getNodemoney()+"'");
		}
		if(spNode.getFormid() != null) {
			column.append(",FORMID");
			value.append(",'"+spNode.getFormid()+"'");
		}
		if(spNode.getContractid() != null) {
			column.append(",CONTRACTID");
			value.append(",'"+spNode.getContractid()+"'");
		}
		if(spNode.getNodepost() != null && !"".equals(spNode.getNodepost())) {
			column.append(",NODEPOST");
			value.append(",'"+spNode.getNodepost()+"'");
		}
		if(spNode.getIswy() != null && !"".equals(spNode.getIswy())) {
			column.append(",ISWY");
			value.append(",'"+spNode.getIswy()+"'");
		}
		if(spNode.getStartdate() != null) {
			column.append(",STARTDATE");
			value.append(","+DataBaseSqlConfig.getDateStrFormat(spNode.getStartdate()));
		}
		if(spNode.getEnddate() != null) {
			column.append(",ENDDATE");
			value.append(","+DataBaseSqlConfig.getDateStrFormat(spNode.getEnddate()));
		}
		if(spNode.getNodefinishdate() != null) {
			column.append(",NODEFINISHDATE");
			value.append(","+DataBaseSqlConfig.getDateStrFormat(spNode.getNodefinishdate()));
		}
		if(spNode.getNodeplanfinishdate() != null) {
			column.append(",NODEPLANFINISHDATE");
			value.append(","+DataBaseSqlConfig.getDateStrFormat(spNode.getNodeplanfinishdate()));
		}
		column.append(")");
		value.append(")");
		String sql = column.toString()+value.toString();
		return sql;
	}
}
