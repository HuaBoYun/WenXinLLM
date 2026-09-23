package com.huabo.contract.mappersql;

/**
 * 智慧合同首页SQL Provider
 *
 * @author 华博云开发团队
 * @since 2025-06-01
 */
public class ContractSmartHomeSqlProvider {

    private static final String BASE = " AND RECORDTYPE IN ('HTGL002','HTGL005') AND ORGID = ";

    public String getYearMoneyInt(String orgid, Integer year, Integer month) {
        String sql = "SELECT NVL(SUM(CONTRACTMONEY),0) FROM TBL_CYHW_UNIT WHERE 1=1" + BASE + orgid
            + " AND CONTRACTSTATUS IN (3,6,7,8)";
        return sql;
    }

    public String getContractCountInt(String orgid, Integer year, Integer month) {
        String sql = "SELECT COUNT(0) FROM TBL_CYHW_UNIT WHERE 1=1" + BASE + orgid
            + " AND CONTRACTSTATUS IN (3,6,7,8)";
        return sql;
    }

    public String getBreachCountInt(String orgid, Integer year, Integer month) {
        String sql = "SELECT COUNT(DISTINCT TCU.CONTRACTID) FROM TBL_CYHW_UNIT TCU"
            + " LEFT JOIN TBL_CONTRACT_SPNODE TCS ON TCU.CONTRACTID = TCS.CONTRACTID"
            + " WHERE TCS.ISWY = '是'" + BASE + orgid;
        return sql;
    }

    public String getPendingCount(String orgid, Integer year) {
        String sql = "SELECT COUNT(0) FROM TBL_CYHW_UNIT WHERE CONTRACTSTATUS = 1" + BASE + orgid;
        return sql;
    }

    public String getExecutingCount(String orgid, Integer year) {
        String sql = "SELECT COUNT(0) FROM TBL_CYHW_UNIT WHERE CONTRACTSTATUS = 7" + BASE + orgid;
        return sql;
    }

    public String getReceivablesInt(String orgid, Integer year, Integer month) {
        String sql = "SELECT NVL(SUM(TPN.NODEMONEY),0) FROM TBL_CONTRACT_PLANNODE TPN"
            + " LEFT JOIN TBL_CYHW_UNIT TCU ON TPN.CONTRACTID = TCU.CONTRACTID"
            + " WHERE TCU.ORGID = " + orgid + " AND TCU.RECORDTYPE IN ('HTGL002','HTGL005')";
        return sql;
    }

    public String getActualCollectionInt(String orgid, Integer year, Integer month) {
        String sql = "SELECT NVL(SUM(TPN.NODEMONEY),0) FROM TBL_CONTRACT_PLANNODE TPN"
            + " LEFT JOIN TBL_CYHW_UNIT TCU ON TPN.CONTRACTID = TCU.CONTRACTID"
            + " WHERE TPN.PLANNODESTATUS = 2 AND TCU.ORGID = " + orgid
            + " AND TCU.RECORDTYPE IN ('HTGL002','HTGL005')";
        return sql;
    }

    public String getContractTypeStats(String orgid, Integer year, Integer month) {
        String sql = "SELECT CONTRACTTYPE AS NAME, COUNT(0) AS VALUE FROM TBL_CYHW_UNIT"
            + " WHERE CONTRACTSTATUS IN (3,6,7,8)" + BASE + orgid
            + " AND CONTRACTTYPE IS NOT NULL";
        sql += " GROUP BY CONTRACTTYPE ORDER BY COUNT(0) DESC";
        return sql;
    }

    public String getRiskWarningList(String orgid, Integer pageSize) {
        return "SELECT * FROM (SELECT TCU.CONTRACTID, TCU.CONTRACTNAME, TCU.CONTRACTLINK AS COUNTERPART,"
            + " CASE WHEN TCS.ISWY='是' THEN '付款逾期' WHEN TCU.ENDDATE<SYSDATE AND TCU.CONTRACTSTATUS=7 THEN '履约异常' ELSE '相对方预警' END AS RISK_TYPE,"
            + " CASE WHEN TCS.ISWY='是' THEN 'HIGH' WHEN TCU.ENDDATE<SYSDATE THEN 'MEDIUM' ELSE 'LOW' END AS RISK_LEVEL,"
            + " TO_CHAR(NVL(TCS.STARTDATE,TCU.CREATETIME),'MM-DD') AS TRIGGER_TIME"
            + " FROM TBL_CYHW_UNIT TCU LEFT JOIN TBL_CONTRACT_SPNODE TCS ON TCU.CONTRACTID=TCS.CONTRACTID"
            + " WHERE TCU.ORGID=" + orgid + " AND TCU.RECORDTYPE IN ('HTGL002','HTGL005')"
            + " AND (TCS.ISWY='是' OR (TCU.ENDDATE<SYSDATE AND TCU.CONTRACTSTATUS=7))"
            + " ORDER BY CASE WHEN TCS.ISWY='是' THEN 1 ELSE 2 END,TCU.CREATETIME DESC"
            + ") WHERE ROWNUM<=" + pageSize;
    }

    public String getExpiringList(String orgid, Integer days) {
        return "SELECT * FROM (SELECT TCU.CONTRACTID, TCU.CONTRACTNAME, TCU.CONTRACTLINK AS COUNTERPART,"
            + " TO_CHAR(TCU.ENDDATE,'YYYY-MM-DD') AS EXPIRE_DATE,"
            + " ROUND(TCU.ENDDATE-SYSDATE,0) AS REMAIN_DAYS"
            + " FROM TBL_CYHW_UNIT TCU"
            + " WHERE TCU.ORGID=" + orgid + " AND TCU.RECORDTYPE IN ('HTGL002','HTGL005')"
            + " AND TCU.CONTRACTSTATUS=7 AND TCU.ENDDATE IS NOT NULL"
            + " AND TCU.ENDDATE>=SYSDATE AND TCU.ENDDATE<=SYSDATE+" + days
            + " ORDER BY TCU.ENDDATE ASC) WHERE ROWNUM<=10";
    }

    public String getCounterpartRiskList(String orgid) {
        return "SELECT * FROM (SELECT TCP.BUDGETNAME AS NAME,"
            + " COUNT(TCU.CONTRACTID) AS CONTRACT_COUNT,"
            + " SUM(CASE WHEN TCS.ISWY='是' THEN 1 ELSE 0 END) AS OVERDUE_COUNT,"
            + " 0 AS BLACKLIST,"
            + " CASE WHEN SUM(CASE WHEN TCS.ISWY='是' THEN 1 ELSE 0 END)>=3 THEN 'HIGH'"
            + " WHEN SUM(CASE WHEN TCS.ISWY='是' THEN 1 ELSE 0 END)>=1 THEN 'MEDIUM'"
            + " ELSE 'LOW' END AS RISK_LEVEL,"
            + " ROUND(100-SUM(CASE WHEN TCS.ISWY='是' THEN 1 ELSE 0 END)*20,0) AS HEALTH_SCORE"
            + " FROM TBL_CYHW_UNIT TCU"
            + " LEFT JOIN TBL_CYHW_PROJECTBUDGET TCP ON TCU.CONTRACTXDFXINFO=TCP.BUDGETID"
            + " LEFT JOIN TBL_CONTRACT_SPNODE TCS ON TCU.CONTRACTID=TCS.CONTRACTID"
            + " WHERE TCU.ORGID=" + orgid + " AND TCU.RECORDTYPE IN ('HTGL002','HTGL005')"
            + " AND TCP.BUDGETNAME IS NOT NULL"
            + " GROUP BY TCP.BUDGETID,TCP.BUDGETNAME"
            + " ORDER BY OVERDUE_COUNT DESC) WHERE ROWNUM<=5";
    }

    public String getKeyContractList(String orgid, Integer pageSize) {
        return "SELECT * FROM (SELECT TCU.CONTRACTID, TCU.CONTRACTNAME, TCU.CONTRACTLINK AS COUNTERPART,"
            + " TCU.CONTRACTMONEY AS AMOUNT,"
            + " NVL(ROUND((SELECT COUNT(0) FROM TBL_CONTRACT_PLANNODE TPN WHERE TPN.CONTRACTID=TCU.CONTRACTID AND TPN.PLANNODESTATUS=2)*100.0/"
            + " NULLIF((SELECT COUNT(0) FROM TBL_CONTRACT_PLANNODE TPN2 WHERE TPN2.CONTRACTID=TCU.CONTRACTID),0),0),0) AS PROGRESS_RATE,"
            + " NVL(ROUND((SELECT NVL(SUM(TPN3.NODEMONEY),0) FROM TBL_CONTRACT_PLANNODE TPN3 WHERE TPN3.CONTRACTID=TCU.CONTRACTID AND TPN3.PLANNODESTATUS=2)*100.0/"
            + " NULLIF(TCU.CONTRACTMONEY,0),0),0) AS PAYMENT_RATE,"
            + " CASE WHEN TCU.ENDDATE<SYSDATE THEN ROUND(SYSDATE-TCU.ENDDATE,0) ELSE 0 END AS DELAY_DAYS"
            + " FROM TBL_CYHW_UNIT TCU"
            + " WHERE TCU.ORGID=" + orgid + " AND TCU.RECORDTYPE IN ('HTGL002','HTGL005')"
            + " AND TCU.CONTRACTSTATUS=7"
            + " AND TCU.CONTRACTMONEY IS NOT NULL AND TCU.CONTRACTMONEY>0"
            + " ORDER BY TCU.CONTRACTMONEY DESC) WHERE ROWNUM<=" + pageSize;
    }
}