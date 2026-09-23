package com.huabo.contract.mappersql;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hbfk.util.DateUtil;
import com.hbfk.util.PageInfo;
import com.hbfk.util.database.DataBaseSqlConfig;
import com.huabo.contract.entity.TblContractPayment;
import com.huabo.contract.entity.TblContractPlannode;

import java.math.BigDecimal;

import org.apache.commons.lang.StringUtils;

public class TblContractPlannodeMapperSqlConfig {

	public String findContractNodeListByPageInfo(TblContractPlannode node) {
        StringBuffer sbSql = new StringBuffer("SELECT TCSP.SPNODEID AS tcpspnodeid,TCSP.STARTDATE AS tcsstartdate,TCSP.ENDDATE AS tcsenddate,TCSP.NODEMEMO AS tcsnodememo,TCSP.ISWY,TCP.NODEID,TCP.NODECONTENT,TCP.PROJECTID,TCP.PLANSTARTDATE,TCP.PLANENDDATE,TCP.NODEPOST,TCP.NODEPLANPAYDATE,TCP.DISPATCHSTAFF,TCP.DISPATCHDEPT,TCP.PLANNODESTATUS,TCP.feedback FROM TBL_CONTRACT_PLANNODE TCP" + 
        		" LEFT JOIN TBL_CONTRACT_SPNODE TCSP ON TCP.NODEID = TCSP.NODEID " + 
        		" WHERE TCP.PROJECTID = " + node.getProjectid() );
        if (node.getDispatchstaff() != null && !"".equals(node.getDispatchstaff())) {
            sbSql.append(" AND TCP.DISPATCHSTAFF = " + node.getDispatchstaff());
        }

        if (node.getNodecontent() != null && !"".equals(node.getNodecontent())) {
            sbSql.append(" AND TCP.NODECONTENT LIKE '%" + node.getNodecontent() + "%'");
        }
        sbSql.append(" ORDER BY TCP.NODEID ASC,TCP.PLANSTARTDATE ASC,TCP.PLANENDDATE ASC");
        String sql = sbSql.toString();
        return sql;
	}
	
	public String findeSumMoneyByContractId(BigDecimal contractId, BigDecimal nodeid) throws Exception {
        String sql = "SELECT SUM(NODEMONEY) FROM TBL_CONTRACT_PLANNODE WHERE PROJECTID = "+contractId;
        if(nodeid != null) {
            sql += " AND NODEID != "+nodeid;
        }
        return sql;
    }
	
	public String findPlanNodeListForCollection(IPage<TblContractPlannode> page, TblContractPlannode node) {
        StringBuffer sbSql = new StringBuffer("SELECT TCP.NODEID,TCP.NODECONTENT,TCP.PLANSTARTDATE,TCP.PLANENDDATE,TCP.NODEPOST*TCU.CONTRACTMONEY/100 AS YFMONEY,TCS.NODECONTENT AS SPNODECONTENT,TCP.NODEPOST,TCS.NODEMEMO " +
                "FROM TBL_CONTRACT_PLANNODE TCP LEFT JOIN TBL_CYHW_UNIT TCU ON TCP.PROJECTID = TCU.CONTRACTID LEFT JOIN TBL_CONTRACT_SPNODE TCS ON TCP.NODEID = TCS.NODEID WHERE TCP.PROJECTID =  " + node.getProjectid() + " AND TCP.PLANNODESTATUS = 2 AND TCP.NODEID NOT IN (SELECT NODEID FROM TBL_CONTRACT_COLLECTION WHERE COLLECTIONSTATUS != 4 OR COLLECTIONSTATUS IS null)");
        if (StringUtils.isNotBlank(node.getNodecontent())) {
            sbSql.append(" AND TCP.NODECONTENT LIKE '%" +node.getNodecontent() + "%'");
        }

        sbSql.append(" ORDER BY TCP.PLANSTARTDATE ASC ");
        String sql = sbSql.toString();
        return sql;
	}
	
	public String findPlanNodeListForPayment(IPage<TblContractPlannode> page, TblContractPlannode node) {
		StringBuffer sbSql = new StringBuffer("SELECT TCP.NODEID,TCP.NODECONTENT,TCP.PLANSTARTDATE,TCP.PLANENDDATE,TCP.NODEPOST*TCU.CONTRACTMONEY/100 AS YFMONEY,TCS.NODECONTENT AS SPNODECONTENT,TCP.NODEPOST,TCS.NODEMEMO FROM TBL_CONTRACT_PLANNODE TCP LEFT JOIN TBL_CYHW_UNIT TCU ON TCP.PROJECTID = TCU.CONTRACTID LEFT JOIN TBL_CONTRACT_SPNODE TCS ON TCP.NODEID = TCS.NODEID WHERE TCP.PROJECTID = " + node.getProjectid() + " AND TCP.PLANNODESTATUS = 2 AND TCP.NODEID NOT IN (SELECT NODEID FROM TBL_CONTRACT_PAYMENT WHERE PAYMENTSTATUS != 4 OR PAYMENTSTATUS IS NULL)");
        if (node.getNodecontent() != null && !"".equals(node.getNodecontent())) {
            sbSql.append(" AND TCP.NODECONTENT LIKE '%" + node.getNodecontent() + "%'");
        }
        sbSql.append(" ORDER BY TCP.PLANSTARTDATE ASC");
        String sql = sbSql.toString();
        return sql;
	}
	
	public String saveContractPlannode(TblContractPlannode node) throws Exception {
        StringBuffer column = new StringBuffer("INSERT INTO TBL_CONTRACT_PLANNODE (NODEID");
        StringBuffer value = new StringBuffer(" VALUES ("+node.getNodeid());

        if(node.getNodecontent() != null && !"".equals(node.getNodecontent())) {
            column.append(",NODECONTENT");
            value.append(",'"+node.getNodecontent()+"'");
        }
        if(node.getProjectid() != null && !"".equals(node.getProjectid())) {
            column.append(",PROJECTID");
            value.append(",'"+node.getProjectid()+"'");
        }
        if(node.getBlprojectid() != null && !"".equals(node.getBlprojectid())) {
            column.append(",BLPROJECTID");
            value.append(",'"+node.getBlprojectid()+"'");
        }
        if(node.getNodepost() != null && !"".equals(node.getNodepost())) {
            column.append(",NODEPOST");
            value.append(",'"+node.getNodepost()+"'");
        }
        if(node.getDispatchstaff() != null && !"".equals(node.getDispatchstaff())) {
            column.append(",DISPATCHSTAFF");
            value.append(",'"+node.getDispatchstaff()+"'");
        }
        if(node.getDispatchdept() != null && !"".equals(node.getDispatchdept())) {
            column.append(",DISPATCHDEPT");
            value.append(",'"+node.getDispatchdept()+"'");
        }
        if(node.getPlannodestatus() != null && !"".equals(node.getPlannodestatus())) {
            column.append(",PLANNODESTATUS");
            value.append(",'"+node.getPlannodestatus()+"'");
        }
        if(node.getContractid() != null && !"".equals(node.getContractid())) {
            column.append(",CONTRACTID");
            value.append(",'"+node.getContractid()+"'");
        }
        if(node.getFeedback() != null && !"".equals(node.getFeedback())) {
            column.append(",FEEDBACK");
            value.append(",'"+node.getFeedback()+"'");
        }
        if(node.getNodemoney() != null && !"".equals(node.getNodemoney())) {
            column.append(",NODEMONEY");
            value.append(",'"+node.getNodemoney()+"'");
        }
        if(node.getPlanstartdate() != null) {
            column.append(",PLANSTARTDATE");
            value.append(","+ DataBaseSqlConfig.getDateStrFormat(node.getPlanstartdate()));
        }
        if(node.getPlanenddate() != null) {
            column.append(",PLANENDDATE");
            value.append(","+ DataBaseSqlConfig.getDateStrFormat(node.getPlanenddate()));
        }
        if(node.getNodeplanpaydate() != null) {
            column.append(",NODEPLANPAYDATE");
            value.append(","+ DataBaseSqlConfig.getDateStrFormat(node.getNodeplanpaydate()));
        }
        if(node.getGoodsName() != null && !"".equals(node.getGoodsName())) {
            column.append(",GOODSNAME");
            value.append(",'"+node.getGoodsName()+"'");
        }
        if(node.getGoodsCount()>=0) {
            column.append(",GOODSCOUNT");
            value.append(",'"+node.getGoodsCount()+"'");
        }
        if(node.getPerformanceCategory()!= null && !"".equals(node.getPerformanceCategory())) {
            column.append(",PERFORMANCECATEGORY");
            value.append(",'"+node.getPerformanceCategory()+"'");
        }
        if(node.getBudgetIds()!=null && !"".equals(node.getBudgetIds())) {
            column.append(",BUDGETIDS");
            value.append(",'"+node.getBudgetIds()+"'");
        }
        column.append(")");
        value.append(")");
        String sql = column.toString()+value.toString();
        return sql;
    }
	
	public String updateContractPlannode(TblContractPlannode node) throws Exception {
        StringBuffer sql = new StringBuffer("UPDATE TBL_CONTRACT_PLANNODE SET NODECONTENT = '"+node.getNodecontent()+"'");

        if(node.getProjectid() != null) {
            sql.append(" ,PROJECTID = '"+node.getProjectid()+"'");
        }
        if(node.getBlprojectid() != null) {
            sql.append(" ,BLPROJECTID = '"+node.getBlprojectid()+"'");
        }
        if(node.getNodepost() != null) {
            sql.append(" ,NODEPOST = '"+node.getNodepost()+"'");
        }
        if(node.getDispatchstaff() != null) {
            sql.append(" ,DISPATCHSTAFF = '"+node.getDispatchstaff()+"'");
        }
        if(node.getDispatchdept() != null) {
            sql.append(" ,DISPATCHDEPT = '"+node.getDispatchdept()+"'");
        }
        if(node.getPlannodestatus() != null) {
            sql.append(" ,PLANNODESTATUS = '"+node.getPlannodestatus()+"'");
        }
        if(node.getContractid() != null) {
            sql.append(" ,CONTRACTID = '"+node.getContractid()+"'");
        }
        if(node.getFeedback() != null) {
            sql.append(" ,FEEDBACK = '"+node.getFeedback()+"'");
        }
        if(node.getNodemoney() != null) {
            sql.append(" ,NODEMONEY = '"+node.getNodemoney()+"'");
        }
        if(node.getPlanstartdate() != null) {
            sql.append(" ,PLANSTARTDATE = "+DataBaseSqlConfig.getDateStrFormat(node.getPlanstartdate()));
        }
        if(node.getPlanenddate() != null) {
            sql.append(" ,PLANENDDATE = "+DataBaseSqlConfig.getDateStrFormat(node.getPlanenddate()));
        }
        if(node.getNodeplanpaydate() != null) {
            sql.append(" ,NODEPLANPAYDATE = "+DataBaseSqlConfig.getDateStrFormat(node.getNodeplanpaydate()));
        }
        if(node.getGoodsName() != null && !"".equals(node.getGoodsName())) {
            sql.append(" ,GOODSNAME = '"+node.getGoodsName()+"'");
        }
        if(node.getGoodsCount()>=0) {
            sql.append(" ,GOODSCOUNT ="+node.getGoodsCount());
        }
        if(node.getPerformanceCategory()!= null && !"".equals(node.getPerformanceCategory())) {
            sql.append(" ,PERFORMANCECATEGORY = '"+node.getPerformanceCategory()+"'");
        }
        if(node.getBudgetIds()!=null && !"".equals(node.getBudgetIds())) {
        	sql.append(" ,BUDGETIDS = '"+node.getBudgetIds()+"'");
        }
        sql.append(" WHERE NODEID = '"+node.getNodeid()+"'");
        return sql.toString();
    }
}
