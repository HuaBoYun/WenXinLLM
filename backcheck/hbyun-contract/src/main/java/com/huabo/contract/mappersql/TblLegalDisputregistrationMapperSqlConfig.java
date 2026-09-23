package com.huabo.contract.mappersql;

import java.math.BigDecimal;

import javax.annotation.Resource;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hbfk.util.database.DataBaseSqlConfig;
import com.huabo.contract.entity.TblLegalDisputregistration;
import com.huabo.contract.mapper.TblLegalDisputregistrationMapper;

public class TblLegalDisputregistrationMapperSqlConfig {
	
	@Resource
    private TblLegalDisputregistrationMapper tblLegalDisputregistrationMapper;
    
	public String selectDisputeMoneyList(Integer year, BigDecimal orgid) throws Exception{
		String yearSql="";
    	if(year!=null){
    		yearSql=" and "+DataBaseSqlConfig.getDateColumn("createtime", "YYYY")+" ="+year;
    	}
        StringBuffer sqlSb = new StringBuffer("select  Orgmeno as orgname,"
    			+ " (select count(*) from TBL_LEGAL_DISPUTREGISTRATION where linkorg=orgid "+yearSql+") as num, "
    			+ " (select sum(litigationamount) from TBL_LEGAL_DISPUTREGISTRATION where  linkorg =orgid  "+yearSql+") as money "
    			+ " from tbl_organization where (fatherorgid="+orgid+" or orgid="+orgid+") and orgtype>0  and status=0 order by orgid ");
        return sqlSb.toString();
		
	}
	
	public String selectSlztList(Integer year, BigDecimal orgid) throws Exception{
		String sql = "select  Orgmeno as key,"+
	    		 " (select count(*) from TBL_LEGAL_PROCEEDINGSRECORD where "+DataBaseSqlConfig.getDateColumn("createtime", "YYYY")+" = "+year+
	    		 " and linkorg=orgid  and PORCEEDSTAGE in ('一审审理中','一审已判决') and PORCEEDSTAGE is not null) ys,"+
	    		 " (select count(*) from TBL_LEGAL_PROCEEDINGSRECORD where "+DataBaseSqlConfig.getDateColumn("createtime", "YYYY")+" = "+year+
	    		 "  and linkorg=orgid  and PORCEEDSTAGE in ('二审审理中','二审已判决') and PORCEEDSTAGE is not null) es,"+
	    		 " (select count(*) from TBL_LEGAL_PROCEEDINGSRECORD where "+DataBaseSqlConfig.getDateColumn("createtime", "YYYY")+" = "+year+
	    		 " and linkorg=orgid  and PORCEEDSTAGE in ('再审审理中','再审已判决') and PORCEEDSTAGE is not null ) zs "+
	    		 " from tbl_organization where (fatherorgid="+orgid+" or orgid="+orgid+") and orgtype>0  and status=0 order by orgid";
		return sql;
	}
	
	public String selectSsTypeList(Integer year, BigDecimal orgid) throws Exception{
		String sql = " select  Orgmeno as key,"+
			     " (select count(*) from TBL_LEGAL_DISPUTREGISTRATION where whethersued=1 and "+DataBaseSqlConfig.getDateColumn("createtime", "YYYY")+" = "+year+
			     " and linkorg=orgid) as yg,"+
			     " (select count(*) from TBL_LEGAL_DISPUTREGISTRATION where whethersued=2 and "+DataBaseSqlConfig.getDateColumn("createtime", "YYYY")+" = "+year+
			     "  and linkorg=orgid) as bg,"+
			     " (select count(*) from TBL_LEGAL_DISPUTREGISTRATION where whethersued=3 and "+DataBaseSqlConfig.getDateColumn("createtime", "YYYY")+" = "+year+
			     " and linkorg=orgid) as dsr"+
			     " from tbl_organization where (fatherorgid= "+orgid+" or orgid= "+orgid+") and orgtype > 0  and status = 0 order by orgid ";
		return sql;
	}
	
	public String selectMoneyValue(Integer year, BigDecimal orgid) throws Exception{
		String sql = "select  Orgmeno as key,(select sum(litigationamount)"
				+ " from TBL_LEGAL_DISPUTREGISTRATION where "+DataBaseSqlConfig.getDateColumn("createtime", "YYYY")+" = "+year+" and linkorg =orgid) as money "
				+ " from tbl_organization where (fatherorgid = "+orgid+" or orgid = "+orgid+") and orgtype>0  and status=0 order by orgid ";
		return sql;    
	}
	
	public String selectJfslList(Integer year,BigDecimal orgid) throws Exception{
		String sql = "select Orgmeno as key,"+
			     " (select count(*) from TBL_LEGAL_DISPUTREGISTRATION where "+DataBaseSqlConfig.getDateColumn("createtime", "YYYY")+" = "+year+"  and linkorg=orgid ) xz,"+
			     " (select count(*) from TBL_LEGAL_DISPUTREGISTRATION where "+DataBaseSqlConfig.getDateColumn("createtime", "YYYY")+" = "+year+"  and linkorg=orgid "+ 
			     " and disputeid not in ( select distinct disputeid from  TBL_LEGAL_LITIGATIONSETTLEMENT where"+ 
			     " litigationid in (select distinct litigationid from TBL_LEGAL_CLOSESUM where litigationid is not null) and disputeid is not null )) wj "+ 
			     " from tbl_organization where (fatherorgid= "+orgid+" or orgid = "+orgid+" ) and orgtype > 0  and status=0 order by orgid";
		return sql;
	}
	
	public String findClassicCaseListByPageInfo(String companyId,IPage<TblLegalDisputregistration> page, TblLegalDisputregistration dispute, BigDecimal orgid) throws Exception{
		StringBuffer sqlSb = new StringBuffer("SELECT TLD.DISPUTEID,TLD.DISPUTENO,TCU.CONTRACTNAME,TCU.CONTRACTNO,TLD.DISPUTETYPE,TLD.ATTORNEY,TLD.ISUEGENT,TLD.WHETHERSUED,TLD.LASTDEALDATE,TLN.NEGOTIAID,TLP.PROCEEDID,TLL.LITIGATIONID,TLA.ARBITRAID,TLQ.QUALID," +
                "TLF.INFORID,TLC.CLOSEID," +
                "(SELECT COUNT(0) FROM TBL_LEGAL_NEGOTIATEDSETTLEMEN WHERE DISPUINFO = TLD.DISPUTEID) AS XSCOUNT," +
                "(SELECT COUNT(0) FROM TBL_LEGAL_NEGOTIATEDSETTLEMEN WHERE DISPUINFO = TLD.DISPUTEID) AS SSCOUNT," +
                "(SELECT COUNT(0) FROM TBL_LEGAL_ARBITRATSETTLEMENT WHERE NEGOTIATEINFO IN (SELECT NEGOTIATEINFO FROM TBL_LEGAL_NEGOTIATEDSETTLEMEN WHERE DISPUINFO = TLD.DISPUTEID)) AS ZCCOUNT," +
                "(SELECT COUNT(0) FROM TBL_LEGAL_CLOSEINFORMATION WHERE DISPUTINFO = TLD.DISPUTEID) AS CLOSECOUNT,TLD.DISPUTEITEM " +
                "FROM TBL_LEGAL_DISPUTREGISTRATION TLD " +
                "LEFT JOIN TBL_CYHW_UNIT TCU ON TLD.CONTRACTINFO = TCU.CONTRACTID " +
                "LEFT JOIN TBL_STAFF TS ON TLD.DISPUTEUNDERTAKER = TS.STAFFID " +
                "LEFT JOIN TBL_LEGAL_NEGOTIATEDSETTLEMEN TLN on TLD.DISPUTEID = TLN.DISPUINFO " +
                "LEFT JOIN TBL_LEGAL_LITIGATIONSETTLEMENT TLL ON TLD.DISPUTEID = TLL.DISPUTEINFO " +
                "LEFT JOIN TBL_LEGAL_PROCEEDINGSRECORD TLP ON TLL.LITIGATIONID = TLP.LITIGATIONINFO " +
                "LEFT JOIN TBL_LEGAL_ARBITRATSETTLEMENT TLA ON TLA.NEGOTIATEINFO = TLN.NEGOTIAID " +
                "LEFT JOIN TBL_LEGAL_QUALIFICATION TLQ ON TLQ.DISPUTEINFO = TLD.DISPUTEID " +
                "LEFT JOIN TBL_LEGAL_FROZENACCOUNT TLF ON TLP.PROCEEDID = TLF.PROCEEDINFO " +
                "LEFT JOIN TBL_LEGAL_CLOSEINFORMATION TLC ON TLC.DISPUTINFO = TLD.DISPUTEID " +
                "WHERE tld.LINKORG = " + orgid +
                " AND ISCLASSICCASE = 1 ");

        if (dispute.getDisputeno() != null && !"".equals(dispute.getDisputeno())) {
            sqlSb.append(" AND tld.DISPUTENO LIKE '%" + dispute.getDisputeno() + "%'");
        }
        if (dispute.getDisputeitem() != null && !"".equals(dispute.getDisputeitem())) {
            sqlSb.append(" AND tld.DISPUTEITEM LIKE '%" + dispute.getDisputeitem() + "%'");
        }
        if (dispute.getDisputetype() != null && !"".equals(dispute.getDisputetype())) {
            sqlSb.append(" AND tld.DISPUTETYPE LIKE '%" + dispute.getDisputetype() + "%'");
        }
        if (dispute.getContractname() != null && !"".equals(dispute.getContractname())) {
            sqlSb.append(" AND tld.tcu.CONTRACTNAME LIKE '%" + dispute.getContractname() + "%'");
        }
        if (dispute.getPlaintiff() != null && !"".equals(dispute.getPlaintiff())) {
            sqlSb.append(" AND tld.PLAINTIFF LIKE '%" + dispute.getPlaintiff() + "%'");
        }
        if (dispute.getDefendant() != null && !"".equals(dispute.getDefendant())) {
            sqlSb.append(" AND tld.DEFENDANT LIKE '%" + dispute.getDefendant() + "%'");
        }
        if (companyId != null&& !"".equals(companyId)) {
        	String [] stringArr= companyId.split(",");
            sqlSb.append(" AND tld.LINKORG IN (" + stringArr+")");
        }
        if (dispute.getUniqueResult() != null && dispute.getUniqueResult() == 1) {
            String disputeIds = this.tblLegalDisputregistrationMapper.findDisputeIdsForDisputeClose(orgid);
            sqlSb.append(" AND tld.disputeId NOT IN (" + disputeIds + ")");
        }
        if (dispute.getUniqueResult() != null && dispute.getUniqueResult() == 1) {
            String disputeIds = this.tblLegalDisputregistrationMapper.findDisputeIdsForDisputeClose(orgid);
            sqlSb.append(" AND tld.disputeId NOT IN (" + disputeIds + ")");
        }

        sqlSb.append(" ORDER BY disputeId DESC");
        String sql = sqlSb.toString();
        return sql;
	}
	
	public String findListByPageInfoDispute(IPage<TblLegalDisputregistration> page,
			TblLegalDisputregistration dispute, BigDecimal pid, String disputeIds) {
		StringBuffer sqlSb = new StringBuffer("SELECT " +
                "TLD.*," +
                "TCU.CONTRACTID,\n" +
                "TCU.CONTRACTSTATUS,\n" +
                "TCU.CONTRACTNAME,\n" +
                "TCU.CONTRACTNO,\n" +
                "TCU.FLOWID,\n" +
                "TCU.UNITNAME,\n" +
                "TCU.CONTRACTDEPT,\n" +
                "TCU.CONTRACTLINK,\n" +
                "TCU.ORGID  " +
                "FROM TBL_LEGAL_DISPUTREGISTRATION TLD " +
                "LEFT JOIN TBL_CYHW_UNIT TCU ON TLD.CONTRACTINFO = TCU.CONTRACTID " +
                "WHERE TLD.LINKORG = "+pid);

        if (dispute.getDisputeno() != null && !"".equals(dispute.getDisputeno())) {
            sqlSb.append(" AND disputeNo LIKE '%" + dispute.getDisputeno() + "%'");
        }
        if (dispute.getDisputeitem() != null && !"".equals(dispute.getDisputeitem())) {
            sqlSb.append(" AND disputeItem LIKE '%" + dispute.getDisputeitem() + "%'");
        }
        if (dispute.getDisputetype() != null && !"".equals(dispute.getDisputetype())) {
            sqlSb.append(" AND disputeType LIKE '%" + dispute.getDisputetype() + "%'");
        }
        if (dispute.getContractinfo() != null && !"".equals(dispute.getContractinfo())) {
            sqlSb.append(" AND CONTRACTINFO LIKE '%" + dispute.getContractinfo() + "%'");
        }
        if (dispute.getPlaintiff() != null && !"".equals(dispute.getPlaintiff())) {
            sqlSb.append(" AND PLAINTIFF LIKE '%" + dispute.getPlaintiff() + "%'");
        }
        if (dispute.getDefendant() != null && !"".equals(dispute.getDefendant())) {
            sqlSb.append(" AND DEFENDANT LIKE '%" + dispute.getDefendant() + "%'");
        }

        if (disputeIds != null){
            sqlSb.append(" AND DISPUTEID NOT IN ("+disputeIds+")");
        }

        sqlSb.append(" ORDER BY disputeId DESC ");
        String sql = sqlSb.toString();
        return sql;
	}
	
	public String findDisputeIdsForDispute(String colName, String tableName, String orgColName, String whSql, BigDecimal oid) {
		StringBuffer sqlSb = new StringBuffer("SELECT "+colName+" FROM "+tableName+ " WHERE " + orgColName +" = " + oid + whSql);
        //sqlSb.append(" ORDER BY disputeId DESC) BUDGET WHERE rownum <= "+(pageInfo.getCurrentPage()*pageInfo.getPageSize())+" ) WHERE RN > "+pageInfo.getCurrentRecord());
        String sql = sqlSb.toString();
        return sql;
	}
	
	public String saveAttacheMent(Integer type, BigDecimal bid, BigDecimal aid){
        String tableName = "";
        String bidColName = "";
        String aidColName = "";
        String attType = null;
        switch (type){
            case 1:
                tableName = "TBL_LEGAL_DISPUTE_ATT";
                bidColName = "DISPUTEID";
                aidColName = "ATTID";
                break;
            case 2:
                tableName = "TBL_LEGAL_NEGOTIATEE_ATT";
                bidColName = "NEGOTIAID";
                aidColName = "ATTID";
                break;
            case 3:
                tableName = "TBL_LEGAL_LSETTLEMENT_ATT";
                bidColName = "LITIGATIONID";
                aidColName = "ATTID";
                break;
            case 4:
                tableName = "TBL_LEGAL_ARBITRATION_ATT";
                bidColName = "ARBITRAID";
                aidColName = "ATTID";
                break;
            case 5:
                tableName = "TBL_LEGAL_CONTRACT_PROJECT_ATT";
                bidColName = "PROJECTID";
                aidColName = "ATTID";
                break;
            case 6:
                tableName = "TBL_CONTRACTNODE_ATT";
                bidColName = "NODEID";
                aidColName = "ATTID";
                attType = "ATTTYPE";
                break;  
                
                
        }
        StringBuilder sql = new StringBuilder("INSERT INTO ")
                .append(tableName)
                .append(" (")
                .append(aidColName)
                .append(",")
                .append(bidColName);
	       if(attType != null) {
	    	   sql .append(","+attType);
	       }    
	            
	       sql.append(") VALUES(")
	                .append(aid)
	                .append(",")
	                .append(bid);
	       if(attType != null) {
	            sql.append(",1");
	      }  
	       sql .append(")");
        return sql.toString();
    }
	
	public String findAttacheMentByBid(Integer type, BigDecimal bid){
        String tableName = "";
        String bidColName = "";

        switch (type){
            case 1:
                tableName = "TBL_LEGAL_DISPUTE_ATT";
                bidColName = "DISPUTEID";
                break;
            case 2:
                tableName = "TBL_LEGAL_NEGOTIATEE_ATT";
                bidColName = "NEGOTIAID";
                break;
            case 3:
                tableName = "TBL_LEGAL_LSETTLEMENT_ATT";
                bidColName = "LITIGATIONID";
                break;
            case 4:
                tableName = "TBL_LEGAL_ARBITRATION_ATT";
                bidColName = "ARBITRAID";
                break;
            case 5:
                tableName = "TBL_LEGAL_CONTRACT_PROJECT_ATT";
                bidColName = "PROJECTID";
                break;
                
        }
        StringBuilder sql = new StringBuilder("SELECT TA.* FROM ")
                .append(tableName)
                .append(" TLD LEFT JOIN TBL_ATTACHMENT TA ON TLD.ATTID = TA.ATTID WHERE TLD.")
                .append(bidColName)
                .append(" = ")
                .append(bid);
        return sql.toString();
    }
	
	//根据附件id,删除单个
    public String deleteAttacheMentByBid(Integer type, BigDecimal aid){
        String tableName = "";
        String aidColName = "";

        switch (type){
            case 1:
                tableName = "TBL_LEGAL_DISPUTE_ATT";
                aidColName = "ATTID";
                break;
            case 2:
                tableName = "TBL_LEGAL_NEGOTIATEE_ATT";
                aidColName = "ATTID";
                break;
            case 3:
                tableName = "TBL_LEGAL_LSETTLEMENT_ATT";
                aidColName = "ATTID";
                break;
            case 4:
                tableName = "TBL_LEGAL_ARBITRATION_ATT";
                aidColName = "ATTID";
                break;
            case 5:
                tableName = "TBL_LEGAL_CONTRACT_PROJECT_ATT";
                aidColName = "ATTID";
                break;
                
                
        }
        StringBuilder sql = new StringBuilder("DELETE FROM ")
                .append(tableName)
                .append("WHERE ")
                .append(aidColName)
                .append(" = ")
                .append(aid);
        return sql.toString();
    }
    
    public String selectCountByDisputreNo(String disputeno) throws Exception{
    	String column = "DISPUTENO";
    	String split = "-";
    	String sql = "SELECT "+DataBaseSqlConfig.getMaxNoDeal(column, split, disputeno)+" FROM TBL_LEGAL_DISPUTREGISTRATION WHERE DISPUTENO LIKE '"+disputeno+"%'";
    	return sql;
    }
    
    public String findListByPageInfo(String companyId, IPage<TblLegalDisputregistration> page,
			TblLegalDisputregistration dispute, BigDecimal pid) throws Exception{
    	StringBuffer sqlSb = new StringBuffer(
				"SELECT TLD.DISPUTEID,TLD.BUSINESSDATE,TLD.DISPUTESTATUS,TLD.DISPUTENO,TLD.LITIGATIONPOS,TCU.CONTRACTSTATUS,TCU.CONTRACTCHILDREN,TCU.CONTRACTNAME,TCU.CONTRACTNO,TLD.DISPUTETYPE,TLD.ATTORNEY,TLD.ISUEGENT,TLD.WHETHERSUED,TLD.LASTDEALDATE,"
				+ "	TLD.DISPUTEUNDERTAKER,TS.REALNAME as disputeundertakername,org.ORGNAME AS orgname,TLD.createstaff,"
				+ " (SELECT COUNT(0) FROM TBL_LEGAL_NEGOTIATEDSETTLEMEN WHERE disputeid = TLD.DISPUTEID) AS XSCOUNT,"
				+ " (SELECT COUNT(0) FROM TBL_LEGAL_LITIGATIONSETTLEMENT WHERE DISPUTEID = TLD.DISPUTEID) AS SSCOUNT,"
				+ " (SELECT COUNT(0) FROM TBL_LEGAL_ARBITRATSETTLEMENT WHERE disputeid = TLD.DISPUTEID) AS ZCCOUNT,"
				+ " (SELECT count(0)  FROM TBL_LEGAL_CLOSEINFORMATION WHERE DISPUTINFO = TLD.DISPUTEID) AS CLOSECOUNT,TLD.DISPUTEITEM "
				+ " FROM TBL_LEGAL_DISPUTREGISTRATION TLD "
				+ " LEFT JOIN TBL_CYHW_UNIT TCU ON TLD.CONTRACTINFO = TCU.CONTRACTID "
				+ " LEFT JOIN TBL_STAFF TS ON TLD.DISPUTEUNDERTAKER = TS.STAFFID "
				+ " LEFT  JOIN TBL_ORGANIZATION org ON org.ORGID =TLD.LINKORG  "
				
//                "LEFT JOIN TBL_LEGAL_CLOSEINFORMATION TLC ON TLC.DISPUTINFO = TLD.DISPUTEID\n" +
                + " WHERE  1 = 1 " );
    	sqlSb.append(" AND tld.LINKORG IN ( "+companyId+" )");
 //tld.LINKORG = " + pid
        if (dispute.getDisputeno() != null && !"".equals(dispute.getDisputeno())) {
            sqlSb.append(" AND tld.DISPUTENO LIKE '%" + dispute.getDisputeno() + "%'");
        }
        if (dispute.getDisputeitem() != null && !"".equals(dispute.getDisputeitem())) {
            sqlSb.append(" AND tld.DISPUTEITEM LIKE '%" + dispute.getDisputeitem() + "%'");
        }
        if (dispute.getDisputetype() != null && !"".equals(dispute.getDisputetype())) {
            sqlSb.append(" AND tld.DISPUTETYPE LIKE '%" + dispute.getDisputetype() + "%'");
        }
        if (dispute.getContractname() != null && !"".equals(dispute.getContractname())) {
            sqlSb.append(" AND tcu.CONTRACTNAME LIKE '%" + dispute.getContractname() + "%'");
        }
        if (dispute.getPlaintiff() != null && !"".equals(dispute.getPlaintiff())) {
            sqlSb.append(" AND tld.PLAINTIFF LIKE '%" + dispute.getPlaintiff() + "%'");
        }
        
        if (dispute.getIsclassiccase() != null ) {
            sqlSb.append(" AND tld.ISCLASSICCASE = "+dispute.getIsclassiccase());
        }
        
        if (dispute.getDefendant() != null && !"".equals(dispute.getDefendant())) {
            sqlSb.append(" AND tld.DEFENDANT LIKE '%" + dispute.getDefendant() + "%'");
        }
        	
        if (dispute.getUniqueResult() != null && dispute.getUniqueResult() == 1) {
            sqlSb.append(" AND tld.disputeId NOT IN (SELECT disputeId FROM TBL_LEGAL_DISPUTREGISTRATION WHERE DISPUTEID = "+pid+")");
        }
        sqlSb.append(" ORDER BY disputeId DESC");
        String sql = sqlSb.toString();
        return sql;
    }
    
    public String findListLSByPageInfo(String companyId, IPage<TblLegalDisputregistration> page,TblLegalDisputregistration dispute, String companyIdStrs) throws Exception{
    	StringBuffer sqlSb = new StringBuffer(
				"SELECT TLD.LITIGATIONAMOUNT,TLD.DISPUTEID,TLD.DISPUTESTATUS,TLD.DISPUTENO,TLD.LITIGATIONPOS,TCU.CONTRACTCHILDREN,TCU.CONTRACTNAME,TCU.CONTRACTNO,TLD.DISPUTETYPE,TLD.ATTORNEY,TLD.ISUEGENT,TLD.WHETHERSUED,TLD.LASTDEALDATE,"
				+ " TLD.DISPUTEUNDERTAKER,TS.REALNAME as disputeundertakername,"
				+ " TLD.DISPUTEITEM "
				+ " FROM TBL_LEGAL_DISPUTREGISTRATION TLD "
				+ " LEFT JOIN TBL_CYHW_UNIT TCU ON TLD.CONTRACTINFO = TCU.CONTRACTID "
				+ " LEFT JOIN TBL_STAFF TS ON TLD.DISPUTEUNDERTAKER = TS.STAFFID "
				+ " LEFT JOIN TBL_ORGANIZATION TORG ON TORG.ORGID=TLD.LINKORG "
				+ " LEFT JOIN TBL_LEGAL_ARBITRATSETTLEMENT tla ON tla.DISPUTEID=TLD.DISPUTEID "
				+ " LEFT JOIN TBL_LEGAL_LITIGATIONSETTLEMENT tll ON tll.DISPUTEID=TLD.DISPUTEID "
				+ " LEFT JOIN TBL_LEGAL_CLOSESUM tlctla ON tlctla.ARBITRAID=tla.ARBITRAID "
				+ " LEFT JOIN TBL_LEGAL_CLOSESUM tlctll ON tlctll.LITIGATIONID=tll.LITIGATIONID " 
				//TBL_LEGAL_DISPUTREGISTRATION
//                "LEFT JOIN TBL_LEGAL_CLOSEINFORMATION TLC ON TLC.DISPUTINFO = TLD.DISPUTEID\n" +
              //  + " WHERE tld.disputestatus=6 and tld.LINKORG = " + pid
				+ " WHERE tld.disputestatus=6  AND tld.LINKORG IN ("+companyIdStrs+") " 
                + " AND (tlctla.ID is null or  tlctll.id is null ) " );

        if (dispute.getDisputeno() != null && !"".equals(dispute.getDisputeno())) {
            sqlSb.append(" AND tld.DISPUTENO LIKE '%" + dispute.getDisputeno() + "%'");
        }
        if (dispute.getDisputeitem() != null && !"".equals(dispute.getDisputeitem())) {
            sqlSb.append(" AND tld.DISPUTEITEM LIKE '%" + dispute.getDisputeitem() + "%'");
        }
        if (dispute.getDisputetype() != null && !"".equals(dispute.getDisputetype())) {
            sqlSb.append(" AND tld.DISPUTETYPE LIKE '%" + dispute.getDisputetype() + "%'");
        }
        if (dispute.getContractname() != null && !"".equals(dispute.getContractname())) {
            sqlSb.append(" AND tld.tcu.CONTRACTNAME LIKE '%" + dispute.getContractname() + "%'");
        }
        if (dispute.getPlaintiff() != null && !"".equals(dispute.getPlaintiff())) {
            sqlSb.append(" AND tld.PLAINTIFF LIKE '%" + dispute.getPlaintiff() + "%'");
        }
        
        if (dispute.getIsclassiccase() != null ) {
            sqlSb.append(" AND tld.ISCLASSICCASE = "+dispute.getIsclassiccase());
        }
        
        if (dispute.getDefendant() != null && !"".equals(dispute.getDefendant())) {
            sqlSb.append(" AND tld.DEFENDANT LIKE '%" + dispute.getDefendant() + "%'");
        }
        if (companyId != null&& !"".equals(companyId)) {
        	String [] stringArr= companyId.split(",");
            sqlSb.append(" AND tld.LINKORG IN (" + stringArr+")");
        }
        /*if (dispute.getUniqueResult() != null && dispute.getUniqueResult() == 1) {
        	sqlSb.append(" AND tld.disputeId NOT IN (SELECT disputeId FROM TBL_LEGAL_DISPUTREGISTRATION WHERE DISPUTEID = "+pid+")");
        }*/
        
        if (dispute.getDisputestatus() != null) {
            sqlSb.append(" AND TLD.DISPUTESTATUS = "+dispute.getDisputestatus()+" ");
        }
        
       // sqlSb.append(" start with TORG.ORGID="+pid+" connect by prior TORG.ORGID=TORG.FATHERORGID ");

        sqlSb.append(" ORDER BY TLD.disputeId DESC");
        String sql = sqlSb.toString();
        return sql;
    }
    
  //根据业务ID，批量删除
    public String deleteAttacheMents(Integer type, BigDecimal bid){
        String tableName = "";
        String aidColName = "";

        switch (type){
            case 1:
                tableName = "TBL_LEGAL_DISPUTE_ATT";
                aidColName = "DISPUTEID";
                break;
            case 2:
                tableName = "TBL_LEGAL_NEGOTIATEE_ATT";
                aidColName = "NEGOTIAID";
                break;
            case 3:
                tableName = "TBL_LEGAL_LSETTLEMENT_ATT";
                aidColName = "LITIGATIONID";
                break;
            case 4:
                tableName = "TBL_LEGAL_ARBITRATION_ATT";
                aidColName = "ARBITRAID";
                break;
            case 5:
                tableName = "TBL_LEGAL_CONTRACT_PROJECT_ATT";
                aidColName = "PROJECTID";
                break;
                
        }
        StringBuilder sql = new StringBuilder("DELETE FROM ")
                .append(tableName)
                .append(" WHERE ")
                .append(aidColName)
                .append(" = ")
                .append(bid);
        return sql.toString();
    }
}
