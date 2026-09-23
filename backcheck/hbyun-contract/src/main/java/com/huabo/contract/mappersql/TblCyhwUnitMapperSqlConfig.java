package com.huabo.contract.mappersql;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.http.client.utils.DateUtils;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hbfk.util.DateUtil;
import com.hbfk.util.PageInfo;
import com.hbfk.util.database.DataBaseSqlConfig;
import com.hbfk.util.redis.Random.RandomUtil;
import com.huabo.contract.entity.TblContractTran;
import com.huabo.contract.entity.TblCyhwProjectbudget;
import com.huabo.contract.entity.TblCyhwUnit;
import com.huabo.contract.entity.TblStaff;

import net.sf.jsqlparser.schema.Database;

public class TblCyhwUnitMapperSqlConfig {
	
	public String findLedgerListOrgForExport(TblCyhwUnit unit, String allCompanyIds) throws Exception {
        String sql = " SELECT TCU.CONTRACTID,TCU.CONTRACTNO,TCU.CONTRACTNAME,TCPJ.PROJECTNAME AS TOPICNAME,TCU.CONTRACTMONEY,TCU.CONTRACTITEM,TCU.CONTRACTTYPE,TCU.CREATETIME,TCU.STARTDATE,TCU.ENDDATE,TCP.BUDGETNAME AS recordConcatname,ZXB.ORGNAME AS choicecontractDeptId,ZXU.ORGNAME AS choicejbunitid,TCU.CONTRACTSTATUS,TS.REALNAME AS topic,	(SELECT COUNT(0) FROM TBL_CYHW_UNIT_ATT WHERE  CONTRACTID = TCU.CONTRACTID) AS nodeCount,YYS.CREATETIME AS yongyintime,TCU.DCTYPE,ORG.ORGNAME AS ORGNAME,CASE (SELECT COUNT(0) FROM TBL_CONTRACT_SPNODE TCS WHERE TCS.ISWY = '是' AND TCS.CONTRACTID = TCU.CONTRACTID) WHEN 0 THEN '否' ELSE '是' END AS ISWY FROM TBL_CYHW_UNIT TCU LEFT JOIN TBL_CYHW_PROJECTBUDGET TCP ON TCU.CONTRACTXDFXINFO = TCP.BUDGETID LEFT JOIN TBL_ORGANIZATION ZXB ON TCU.CONTRACTDEPT = ZXB.ORGID LEFT JOIN TBL_ORGANIZATION ZXU ON TCU.ZXUNIT = ZXU.ORGID LEFT JOIN TBL_STAFF TS ON TCU.CONTRACTSTAFF = TS.STAFFID LEFT JOIN TBL_ORGANIZATION ORG ON TCU.ORGID = ORG.ORGID LEFT JOIN TBL_CONTRACT_PROJECT TCPJ ON TCU.TOPICID = TCPJ.PROJECTID LEFT JOIN TBL_CYHW_PROJECTBUDGET YYS ON TCU.CONTRACTID = YYS.RECORDPARENT AND YYS.RECORDTYPE = 'HTGL003'";
        sql += " WHERE TCU.RECORDTYPE IN ('HTGL002','HTGL005') ";
        //AND TCU.ORGID IN (SELECT ORGID FROM TBL_ORGANIZATION START WITH ORGID = "+unit.getOrgid()+" CONNECT BY PRIOR ORGID = FATHERORGID AND ORGTYPE > 0 AND ORGTYPE < 100) AND TCU.CONTRACTID NOT IN (SELECT RECORDPARENT FROM TBL_CYHW_UNIT WHERE RECORDTYPE = 'HTGL005' AND ORGID = "+unit.getOrgid()+" AND RECORDPARENT IS NOT NULL)";
        
        if(unit.getOrgname()!=null && unit.getOrgname().length()>0) {
        	sql += " AND TCU.ORGID = "+unit.getOrgid();
        }else {
        	sql +=" AND TCU.ORGID IN ("+allCompanyIds+") ";
        }
        
        if(unit.getCreateuser() != null) {
            sql += " AND TCU.CREATEUSER = " + unit.getCreateuser();
        }
        if(unit.getContractno() != null && !"".equals(unit.getContractno())) {
            sql += " AND TCU.CONTRACTNO LIKE '%" + unit.getContractno()+"%'";
        }
        if(unit.getContractname() != null && !"".equals(unit.getContractname())) {
            sql += " AND TCU.CONTRACTNAME LIKE '%" + unit.getContractname()+"%'";
        }
        if(unit.getContractitem() != null && !"".equals(unit.getContractitem())) {
            sql += " AND TCPJ.PROJECTNAME LIKE '%" + unit.getContractitem()+"%'";
        }
        if(unit.getContracttype() != null && !"".equals(unit.getContracttype())) {
            sql += " AND TCU.CONTRACTTYPE = '" + unit.getContracttype()+"'";
        }
        if(unit.getRecordConcatname() != null && !"".equals(unit.getRecordConcatname())) {
            sql += " AND TCP.BUDGETNAME LIKE '%" + unit.getRecordConcatname()+"%'";
        }
        if(unit.getDctype() != null && !"".equals(unit.getDctype())) {
            sql += " AND TCU.DCTYPE = '" + unit.getDctype()+"'";
        }
        if(unit.getContractstatus() != null) {
            if(unit.getContractstatus() == 0) {
                sql += " AND (TCU.CONTRACTSTATUS = " + unit.getContractstatus()+" OR TCU.CONTRACTSTATUS IS NULL)";
            }else {
                sql += " AND TCU.CONTRACTSTATUS = " + unit.getContractstatus();
            }
        }
        if(unit.getMinMoney() != null) {
            sql += " AND TCU.CONTRACTMONEY >= '" + unit.getMinMoney()+"'";
        }
        if(unit.getMaxMoney() != null) {
            sql += " AND TCU.CONTRACTMONEY <= '" + unit.getMaxMoney()+"'";
        }

        if(unit.getStartdate() != null) {
            sql += " AND TCU.CREATETIME >= "+ DataBaseSqlConfig.getDateStrFormat(unit.getStartdate());

        }
        if(unit.getEnddate() != null) {
            sql += " AND TCU.CREATETIME <= "+ DataBaseSqlConfig.getDateStrFormat(unit.getEnddate());

        }
        
        if(unit.getIsWy() != null && !"".equals(unit.getIsWy())){
        	if("是".equals(unit.getIsWy())) {
        		sql += " AND TCU.CONTRACTID IN ( SELECT CONTRACTID FROM TBL_CONTRACT_SPNODE WHERE ISWY = '"+unit.getIsWy()+"')";
        	}
        }
        
        sql += "ORDER BY TCU.CONTRACTID ASC";
        return sql.toString();
    }
	
	public String selectNowyearcount1(String budgetid,String dateStr)  throws Exception{
		StringBuffer sb = new StringBuffer("SELECT count(*) from TBL_CYHW_UNIT WHERE CREATETIME >= ");
		sb.append(DataBaseSqlConfig.getDateStrFormat(dateStr))
			.append(" AND RECORDTYPE IN ('HTGL002','HTGL005') AND CONTRACTID IN (SELECT CONTRACTID FROM TBL_CONTRACT_BUDGET WHERE BUDGETID = '")
			.append(budgetid).append("')");
		String sql = sb.toString();
		return sql;
	}
	
	
	public String insertCyhwUnit(TblCyhwUnit tcu) throws Exception {
		StringBuffer column = new StringBuffer("INSERT INTO TBL_CYHW_UNIT (CONTRACTID");
        StringBuffer value = new StringBuffer(" VALUES ("+tcu.getContractid());

        if(tcu.getHtoaid() != null){
            column.append(",htoaid");
            value.append(",'"+tcu.getHtoaid()+"'");
        }

        if(tcu.getFlowid() != null) {
            column.append(",FLOWID");
            value.append(",'"+tcu.getFlowid()+"'");
        }
        if(tcu.getUnitname() != null) {
            column.append(",UNITNAME");
            value.append(",'"+tcu.getUnitname()+"'");
        }
        if(tcu.getContractname() != null) {
            column.append(",CONTRACTNAME");
            value.append(",'"+tcu.getContractname()+"'");
        }
        if(tcu.getContractno() != null) {
            column.append(",CONTRACTNO");
            value.append(",'"+tcu.getContractno()+"'");
        }
        if(tcu.getContractdept() != null) {
            column.append(",CONTRACTDEPT");
            value.append(",'"+tcu.getContractdept()+"'");
        }
        if(tcu.getTypefl() != null) {
            column.append(",TYPEFL");
            value.append(",'"+tcu.getTypefl()+"'");
        }
        if(tcu.getContractlink() != null) {
            column.append(",CONTRACTLINK");
            value.append(",'"+tcu.getContractlink()+"'");
        }
        if(tcu.getContractmoney() != null) {
            column.append(",CONTRACTMONEY");
            value.append(",'"+tcu.getContractmoney()+"'");
        }
        if(tcu.getContractstatus() != null) {
            column.append(",CONTRACTSTATUS");
            value.append(",'"+tcu.getContractstatus()+"'");
        }
        if(tcu.getLinkdept() != null) {
            column.append(",LINKDEPT");
            value.append(",'"+tcu.getLinkdept()+"'");
        }
        if(tcu.getOrgid() != null) {
            column.append(",ORGID");
            value.append(",'"+tcu.getOrgid()+"'");
        }
        if(tcu.getCreateuser() != null) {
            column.append(",CREATEUSER");
            value.append(",'"+tcu.getCreateuser()+"'");
        }
        if(tcu.getCreatetime() != null) {
            column.append(",CREATETIME");
            value.append(","+DataBaseSqlConfig.getDateStrFormat(tcu.getCreatetime()));
        }
        if(tcu.getMomoconcat() != null) {
            column.append(",MOMOCONCAT");
            value.append(",'"+tcu.getMomoconcat()+"'");
        }
        if(tcu.getRiskcontrol() != null) {
            column.append(",RISKCONTROL");
            value.append(",'"+tcu.getRiskcontrol()+"'");
        }
        if(tcu.getDescribe() != null) {
            column.append(",DESCRIBE");
            value.append(",'"+tcu.getDescribe()+"'");
        }
        if(tcu.getContractstaff() != null) {
            column.append(",CONTRACTSTAFF");
            value.append(",'"+tcu.getContractstaff()+"'");
        }
        if(tcu.getScrilevel() != null) {
            column.append(",SCRILEVEL");
            value.append(",'"+tcu.getScrilevel()+"'");
        }
        if(tcu.getStartdate() != null) {
            column.append(",STARTDATE");
            value.append(","+DataBaseSqlConfig.getDateStrFormat(tcu.getStartdate()));
        }
        if(tcu.getEnddate() != null) {
            column.append(",ENDDATE");
            value.append(","+DataBaseSqlConfig.getDateStrFormat(tcu.getEnddate()));
        }
        if(tcu.getContractitem() != null) {
            column.append(",CONTRACTITEM");
            value.append(",'"+tcu.getContractitem()+"'");
        }
        if(tcu.getContracttype() != null) {
            column.append(",CONTRACTTYPE");
            value.append(",'"+tcu.getContracttype()+"'");
        }
        if(tcu.getContractdatetype() != null) {
            column.append(",CONTRACTDATETYPE");
            value.append(",'"+tcu.getContractdatetype()+"'");
        }
        if(tcu.getContractxz() != null) {
            column.append(",CONTRACTXZ");
            value.append(",'"+tcu.getContractxz()+"'");
        }
        if(tcu.getContractxdfxinfo() != null) {
            column.append(",CONTRACTXDFXINFO");
            value.append(",'"+tcu.getContractxdfxinfo()+"'");
        }
        if(tcu.getRecordtype() != null) {
            column.append(",RECORDTYPE");
            value.append(",'"+tcu.getRecordtype()+"'");
        }
        if(tcu.getRecordparent() != null) {
            column.append(",RECORDPARENT");
            value.append(",'"+tcu.getRecordparent()+"'");
        }
        if(tcu.getHzsumowing() != null) {
            column.append(",HZSUMOWING");
            value.append(",'"+tcu.getHzsumowing()+"'");
        }
        if(tcu.getJijiatype() != null) {
            column.append(",JIJIATYPE");
            value.append(",'"+tcu.getJijiatype()+"'");
        }
        if(tcu.getMoneytype() != null) {
            column.append(",MONEYTYPE");
            value.append(",'"+tcu.getMoneytype()+"'");
        }
        if(tcu.getDctype() != null) {
            column.append(",DCTYPE");
            value.append(",'"+tcu.getDctype()+"'");
        }
        if(tcu.getContractbd() != null) {
            column.append(",CONTRACTBD");
            value.append(",'"+tcu.getContractbd()+"'");
        }
        if(tcu.getContractzd() != null) {
            column.append(",CONTRACTZD");
            value.append(",'"+tcu.getContractzd()+"'");
        }
        if(tcu.getJbstaff() != null) {
            column.append(",JBSTAFF");
            value.append(",'"+tcu.getJbstaff()+"'");
        }
        if(tcu.getJbdept() != null) {
            column.append(",JBDEPT");
            value.append(",'"+tcu.getJbdept()+"'");
        }
        if(tcu.getJbunit() != null) {
            column.append(",JBUNIT");
            value.append(",'"+tcu.getJbunit()+"'");
        }
        if(tcu.getZxunit() != null) {
            column.append(",ZXUNIT");
            value.append(",'"+tcu.getZxunit()+"'");
        }
        if(tcu.getContractchildren() != null) {
            column.append(",CONTRACTCHILDREN");
            value.append(",'"+tcu.getContractchildren()+"'");
        }
        if(tcu.getContractplan() != null) {
            column.append(",CONTRACTPLAN");
            value.append(",'"+tcu.getContractplan()+"'");
        }
        if(tcu.getTopicid() != null) {
            column.append(",TOPICID");
            value.append(",'"+tcu.getTopicid()+"'");
        }
        if(tcu.getTopicname() != null) {
            column.append(",TOPICNAME");
            value.append(",'"+tcu.getTopicname()+"'");
        }
        if(tcu.getHiscontractstatus() != null) {
            column.append(",HISCONTRACTSTATUS");
            value.append(",'"+tcu.getHiscontractstatus()+"'");
        }
        if(tcu.getChangetype() != null) {
            column.append(",CHANGETYPE");
            value.append(",'"+tcu.getChangetype()+"'");
        }
        if(tcu.getChangedate() != null) {
            column.append(",CHANGEDATE");
            value.append(",'"+tcu.getChangedate()+"'");
        }
        if(tcu.getChangedesc() != null) {
            column.append(",CHANGEDESC");
            value.append(",'"+tcu.getChangedesc()+"'");
        }
        if(tcu.getCounterpartbank() != null) {
            column.append(",COUNTERPARTBANK");
            value.append(",'"+tcu.getCounterpartbank()+"'");
        }
        if(tcu.getIsmany() != null) {
            column.append(",ISMANY");
            value.append(",'"+tcu.getIsmany()+"'");
        }
        if(tcu.getAgreementcount() != null) {
            column.append(",AGREEMENTCOUNT");
            value.append(",'"+tcu.getAgreementcount()+"'");
        }
        column.append(")");
        value.append(")");
        String sql = column.toString()+value.toString();
        return sql;
	}
	
	public String findFulfillmentContract(IPage<TblCyhwUnit> page, TblCyhwUnit unit) {
        StringBuffer sbSql = new StringBuffer("SELECT TCU.CONTRACTID,TCU.FLOWID,TCU.CONTRACTNAME,TCU.CONTRACTNO,TCU.CONTRACTMONEY,TCU.CONTRACTSTATUS,TCU.ORGID,TCU.RECORDTYPE,TCU.DCTYPE,TCU.CONTRACTTYPE,(SELECT COUNT(0) FROM TBL_CONTRACT_PLANNODE WHERE PROJECTID = TCU.CONTRACTID AND (PLANNODESTATUS != 2 OR PLANNODESTATUS IS NULL)) PLANNUM FROM TBL_CONTRACT_PLANNODE TCP LEFT JOIN TBL_CYHW_UNIT TCU ON TCP.PROJECTID = TCU.CONTRACTID WHERE TCP.DISPATCHSTAFF = " + unit.getJbstaff() + " AND TCU.RECORDTYPE IN ('HTGL002','HTGL005') AND TCU.CONTRACTSTATUS IN (7,17,8)  AND TCU.ORGID = " + unit.getOrgid());
        if (unit.getContractno() != null && !"".equals(unit.getContractno())) {
            sbSql.append( " AND TCU.CONTRACTNO LIKE '%" + unit.getContractno() + "%'");
        }

        if (unit.getContractname() != null && !"".equals(unit.getContractname())) {
            sbSql.append(" AND TCU.CONTRACTNAME LIKE '%" + unit.getContractname() + "%'");
        }
        sbSql.append("  GROUP BY TCU.CONTRACTID,TCU.FLOWID,TCU.CONTRACTNAME,TCU.CONTRACTNO,TCU.CONTRACTMONEY,TCU.CONTRACTSTATUS,TCU.ORGID,TCU.RECORDTYPE,TCU.DCTYPE,TCU.CONTRACTTYPE,TCU.CREATETIME ORDER BY TCU.CREATETIME DESC");
        String sql = sbSql.toString();
        return sql;
    }
	
	public String findeContractListPerformanceTracking(IPage<TblCyhwUnit> page, TblCyhwUnit unit) {
        StringBuffer sbSql = new StringBuffer("SELECT TCU.CONTRACTID,TCU.CONTRACTNAME,TCU.CONTRACTNO,TCU.CONTRACTMONEY,TCU.CONTRACTSTATUS,TCU.DCTYPE,TCU.CONTRACTTYPE,TCU.RECORDTYPE,TCU.FLOWID,COUNT(0) AS PLANNUM FROM TBL_CYHW_UNIT TCU LEFT JOIN TBL_CONTRACT_PLANNODE TCPN ON TCU.CONTRACTID = TCPN.PROJECTID where TCU.ORGID = " + unit.getOrgid() + " AND (TCU.RECORDTYPE = 'HTGL002' OR TCU.RECORDTYPE = 'HTGL005') AND TCU.CONTRACTSTATUS IN (7,17,8) ");
        if (unit.getContractstaff() != null && !"".equals(unit.getContractstaff())) {
            sbSql.append(" AND TCU.CONTRACTSTAFF = " + unit.getContractstaff());
        }

        if (unit.getContractno() != null && !"".equals(unit.getContractno())) {
            sbSql.append( " AND TCU.CONTRACTNO LIKE '%" + unit.getContractno() + "%'");
        }

        if (unit.getContractname() != null && !"".equals(unit.getContractname())) {
            sbSql.append(" AND TCU.CONTRACTNAME LIKE '%" + unit.getContractname() + "%'");
        }

        sbSql.append("  GROUP BY TCU.CONTRACTID,TCU.CONTRACTNAME,TCU.CONTRACTNO,TCU.CONTRACTMONEY,TCU.CONTRACTSTATUS,TCU.DCTYPE,TCU.CONTRACTTYPE,TCU.RECORDTYPE,TCU.FLOWID,TCU.CREATETIME ORDER BY TCU.CREATETIME DESC");
        String sql = sbSql.toString();
        return sql;
	}
	
	public String findeContractListByContractStaff(IPage<TblCyhwUnit> page, TblCyhwUnit unit) {
        StringBuffer sbSql = new StringBuffer("SELECT tcu.CONTRACTID,tcu.CONTRACTNAME,tcu.CONTRACTNO,tcu.CONTRACTMONEY,tcu.CONTRACTSTATUS,tcu.DCTYPE,tcu.CONTRACTTYPE,tcu.RECORDTYPE,tcu.FLOWID "
        		+ " FROM TBL_CYHW_UNIT tcu "
        		+ " LEFT JOIN TBL_CYHW_PROJECTBUDGET tcp ON tcu.CONTRACTID = tcp.RECORDPARENT AND tcp.RECORDTYPE = 'HTGL003' "
        		+ " where tcu.ORGID = " + unit.getOrgid() + " AND (tcu.RECORDTYPE = 'HTGL002' OR tcu.RECORDTYPE = 'HTGL005') "
        		+ " AND (tcu.CONTRACTSTATUS > 6 OR (tcu.CONTRACTSTATUS = 6 AND tcp.INSPECTIONSTATUS = 6) )");
        		//+ " AND TCU.CONTRACTID NOT IN (SELECT RECORDPARENT FROM TBL_CYHW_UNIT where recordtype= 'HTGL005' AND CONTRACTSTATUS = 6 AND ORGID = tcu.ORGID AND RECORDPARENT IS NOT NULL) ");
        if (unit.getContractno() != null && !"".equals(unit.getContractno())) {
            sbSql.append(" AND tcu.CONTRACTNO LIKE '%" + unit.getContractno() + "%'");
        }

        if (unit.getContractname() != null && !"".equals(unit.getContractname())) {
            sbSql.append(" AND tcu.CONTRACTNAME LIKE '%" + unit.getContractname() + "%'") ;
        }

        if (unit.getContracttype() != null && !"".equals(unit.getContracttype())) {
            sbSql.append(" AND tcu.CONTRACTTYPE LIKE '%" + unit.getContracttype() + "%'") ;
        }
        
        if(unit.getContractdept() != null) {
        	sbSql.append(" AND tcu.CONTRACTDEPT = " + unit.getContractdept()) ;
        }else {
        	sbSql.append(" AND tcu.CONTRACTSTAFF = " + unit.getContractstaff()) ;
        }
        
        if (unit.getDctype() != null && !"".equals(unit.getDctype())) {
            sbSql.append(" AND tcu.DCTYPE = '" + unit.getDctype() + "'") ;
        }
        sbSql.append("  ORDER BY tcu.CREATETIME DESC");
        String sql = sbSql.toString();
        return sql;
	}
	
	public String findSealedContractList(IPage<TblCyhwUnit> page, TblCyhwUnit unit) throws Exception{
        StringBuffer sqlSb = new StringBuffer("SELECT tcu.CONTRACTID,SEALORG.ORGNAME SEALORGNAME,tcu.FLOWID,tcu.CONTRACTNAME,tcu.CONTRACTITEM,tcu.CONTRACTNO,tcu.CONTRACTSTATUS,tcp.COUNTERPARTCODE,tcp.COUNTERPARTHANK,"+DataBaseSqlConfig.getNullColumn("tcp.INSPECTIONSTATUS", "-1")+" AS inspectionstatus,tcp.BUDGETID "
        		+ " from TBL_CYHW_UNIT tcu " +
                "  LEFT JOIN TBL_CYHW_PROJECTBUDGET tcp ON tcu.CONTRACTID = tcp.RECORDPARENT AND tcp.RECORDTYPE = '"+unit.getRecordtype()+"' " +
                "  LEFT JOIN TBL_ORGANIZATION SEALORG ON SEALORG.ORGID = tcp.SEALORGID"
                + " LEFT JOIN TBL_CONTRACT_PROJECT TCPJ ON TCU.TOPICID = TCPJ.PROJECTID "+
                "  where tcu.ORGID= "+unit.getOrgid()
                +" AND (tcu.RECORDTYPE = 'HTGL002' OR tcu.RECORDTYPE = 'HTGL005') AND tcu.CONTRACTSTATUS >= 6 "
                + " AND tcp.inspectionstatus=6 "
                + " AND tcu.CONTRACTID NOT IN (SELECT TCT.CONTRACTID FROM TBL_CONTRACT_TRAN TCT WHERE TCT.TRANORGID="+unit.getOrgid()+" ) ");
        if(unit.getJbstaff()!= null) {
            sqlSb.append(" AND tcu.CREATEUSER = "+unit.getJbstaff());
        }
        if(unit.getContractname() != null && !"".equals(unit.getContractname())) {
            sqlSb.append( " AND tcu.CONTRACTNAME LIKE '%"+unit.getContractname()+"%'");
        }
        sqlSb.append(" ORDER BY tcu.CONTRACTID DESC");
        String sql = sqlSb.toString();
        return sql;
	}
	
	public String insertTblContractTran(TblContractTran tct) throws Exception{
		StringBuffer colSb = new StringBuffer("INSERT INTO TBL_CONTRACT_TRAN(TRANID,CREATETIME,TRANSTATUS");
		StringBuffer valSb = new StringBuffer("  VALUES ("+tct.getTranId()+","+DataBaseSqlConfig.getDateStrFormat(tct.getCreateTime())+",0");
		
		if(tct.getLegalMemo() != null && !"".equals(tct.getLegalMemo())) {
			colSb.append(",LEGALMEMO");
			valSb.append(",'"+tct.getLegalMemo()+"'");
		}
		if(tct.getHandStaffId() != null && !"".equals(tct.getHandStaffId())) {
			colSb.append(",HANDSTAFFID");
			valSb.append(",'"+tct.getHandStaffId()+"'");
		}
		if(tct.getHandStaffName() != null && !"".equals(tct.getHandStaffName())) {
			colSb.append(",HANDSTAFFName");
			valSb.append(",'"+tct.getHandStaffName()+"'");
		}
		if(tct.getHandDeptId() != null && !"".equals(tct.getHandDeptId())) {
			colSb.append(",HANDDEPTID");
			valSb.append(","+tct.getHandDeptId()+"");
		}
		if(tct.getHandDeptName() != null && !"".equals(tct.getHandDeptName())) {
			colSb.append(",HANDDEPTNAME");
			valSb.append(",'"+tct.getHandDeptName()+"'");
		}
		if(tct.getTranOrgId() != null && !"".equals(tct.getTranOrgId())) {
			colSb.append(",TRANORGID");
			valSb.append(","+tct.getTranOrgId()+"");
		}
		if(tct.getTranOrgName() != null && !"".equals(tct.getTranOrgName())) {
			colSb.append(",TRANORGNAME");
			valSb.append(",'"+tct.getTranOrgName()+"'");
		}
		if(tct.getContractId() != null && !"".equals(tct.getContractId())) {
			colSb.append(",CONTRACTID");
			valSb.append(","+tct.getContractId()+"");
		}
		if(tct.getContractNo() != null && !"".equals(tct.getContractNo())) {
			colSb.append(",CONTRACTNO");
			valSb.append(",'"+tct.getContractNo()+"'");
		}
		if(tct.getContractName() != null && !"".equals(tct.getContractName())) {
			colSb.append(",CONTRACTNAME");
			valSb.append(",'"+tct.getContractName()+"'");
		}
		if(tct.getContractCnt() != null && !"".equals(tct.getContractCnt())) {
			colSb.append(",CONTRACTCNT");
			valSb.append(",'"+tct.getContractCnt()+"'");
		}
		if(tct.getTranSituation() != null && !"".equals(tct.getTranSituation())) {
			colSb.append(",TRANSITUATION");
			valSb.append(",'"+tct.getTranSituation()+"'");
		}
		
		String sql = colSb.toString()+")"+valSb.toString()+")";
		return sql;
	}
	
	public String updateTblContractTran(TblContractTran tct) {
		StringBuffer sqlSb = new StringBuffer("UPDATE TBL_CONTRACT_TRAN SET CONTRACTID = '"+tct.getContractId()+"' ");
		if(tct.getContractNo() != null && !"".equals(tct.getContractNo())) {
			sqlSb.append(" ,CONTRACTNO = '"+tct.getContractNo()+"'");
		}
		if(tct.getContractName() != null && !"".equals(tct.getContractName())) {
			sqlSb.append(" ,CONTRACTNAME = '"+tct.getContractName()+"'");
		}
		if(tct.getContractCnt() != null && !"".equals(tct.getContractCnt())) {
			sqlSb.append(" ,CONTRACTCNT = '"+tct.getContractCnt()+"'");
		}
		if(tct.getTranStatus() != null && !"".equals(tct.getTranStatus())) {
			sqlSb.append(" ,TRANSTATUS = '"+tct.getTranStatus()+"'");
		}
		if(tct.getLegalMemo() != null && !"".equals(tct.getLegalMemo())) {
			sqlSb.append(" ,LEGALMEMO = '"+tct.getLegalMemo()+"'");
		}
		if(tct.getHandStaffId() != null && !"".equals(tct.getHandStaffId())) {
			sqlSb.append(" ,HANDSTAFFID = '"+tct.getHandStaffId()+"'");
		}
		if(tct.getHandStaffName() != null && !"".equals(tct.getHandStaffName())) {
			sqlSb.append(" ,HANDSTAFFNAME = '"+tct.getHandStaffName()+"'");
		}
		if(tct.getHandDeptId() != null && !"".equals(tct.getHandDeptId())) {
			sqlSb.append(" ,HANDDEPTID = '"+tct.getHandDeptId()+"'");
		}
		if(tct.getHandDeptName() != null && !"".equals(tct.getHandDeptName())) {
			sqlSb.append(" ,HANDDEPTNAME = '"+tct.getHandDeptName()+"'");
		}
		if(tct.getTranOrgId() != null && !"".equals(tct.getTranOrgId())) {
			sqlSb.append(" ,TRANORGID = '"+tct.getTranOrgId()+"'");
		}
		if(tct.getTranOrgName() != null && !"".equals(tct.getTranOrgName())) {
			sqlSb.append(" ,TRANORGNAME = '"+tct.getTranOrgName()+"'");
		}
		if(tct.getTranSituation() != null && !"".equals(tct.getTranSituation())) {
			sqlSb.append(" ,TRANSITUATION = '"+tct.getTranSituation()+"'");
		}
		
		sqlSb.append(" WHERE TRANID= "+tct.getTranId());
		return sqlSb.toString();
	}
	
	public String findContractTranList(IPage<TblContractTran> page, TblContractTran tct) {
        StringBuffer sqlSb = new StringBuffer("SELECT * from TBL_CONTRACT_TRAN TCT " +
                "  where TCT.TRANORGID= "+tct.getTranOrgId());
        
        if(tct.getTranStatus()!= null) {
            sqlSb.append(" AND TCT.TRANSTATUS = "+tct.getTranStatus());
        }
        if(tct.getContractName() != null && !"".equals(tct.getContractName())) {
            sqlSb.append( " AND TCT.CONTRACTNAME LIKE '%"+tct.getContractName()+"%'");
        }
        if(tct.getContractNo() != null && !"".equals(tct.getContractNo())) {
            sqlSb.append( " AND TCT.CONTRACTNO LIKE '%"+tct.getContractNo()+"%'");
        }
        sqlSb.append(" ORDER BY TCT.CREATETIME DESC");
        String sql = sqlSb.toString();
        return sql;
	}
	
	public String findChooseLendContractList(IPage<TblCyhwUnit> page, TblCyhwUnit unit) throws Exception{
        StringBuffer sqlSb = new StringBuffer("SELECT TCU.CONTRACTID,TCU.CONTRACTNAME,TCU.CONTRACTNO,TCU.CONTRACTMONEY,TCU.CONTRACTSTATUS,TCU.DCTYPE,TCU.CONTRACTTYPE,TCU.RECORDTYPE,TCU.FLOWID,TCU.CONTRACTSTAFF");
        sqlSb.append(" FROM TBL_CYHW_UNIT TCU WHERE TCU.CONTRACTID NOT IN (SELECT DISTINCT CONTRACTID FROM TBL_CONTRACT_LEND WHERE USERID = "+unit.getStaffid()+" AND LENDSTATUS = 6 AND RETURNDATE >= "+DataBaseSqlConfig.getDateStrFormat(new Date())+") AND (TCU.CONTRACTID IN (SELECT CONTRACTID FROM TBL_CONTRACT_TRAN WHERE TRANSTATUS = 6) OR TCU.CONTRACTSTATUS = 8 )");
        
        sqlSb.append(" AND TCU.ORGID = "+unit.getOrgid());
        if(unit.getContractno() != null && !"".equals(unit.getContractno())) {
            sqlSb.append( " AND TCU.CONTRACTNO LIKE '%"+unit.getContractno()+"%'");
        }
        if(unit.getContractname() != null && !"".equals(unit.getContractname())) {
            sqlSb.append( " AND TCU.CONTRACTNAME LIKE '%"+unit.getContractname()+"%'");
        }

        sqlSb.append(" ORDER BY TCU.CREATETIME DESC ");
        String sql = sqlSb.toString();
        return sql;
	}
	
	public String findeFilContractList(IPage<TblCyhwUnit> page, TblCyhwUnit unit) throws Exception {
        StringBuffer sqlSb = new StringBuffer("SELECT * FROM (")
        .append("SELECT TCU.CONTRACTID,TCU.FLOWID,TCU.CONTRACTNAME,TCU.CONTRACTNO,TCU.CONTRACTPLAN,TCU.CONTRACTMONEY,TCU.CONTRACTSTATUS,TCU.CREATETIME,TCU.SCRILEVEL,TCU.STARTDATE,TCU.ENDDATE,TCU.CONTRACTITEM,TCU.CONTRACTTYPE,TCU.CONTRACTDATETYPE,TCU.CONTRACTXZ,TCU.RECORDTYPE,TCU.DCTYPE,")
        .append(" (SELECT COUNT(0) FROM TBL_CONTRACT_PLANNODE TCP WHERE  TCP.PLANNODESTATUS = 2 AND TCP.PROJECTID = TCU.CONTRACTID ) AS YICHULI,")
    	.append(" (SELECT COUNT(0) FROM TBL_CONTRACT_PLANNODE TCP WHERE  (TCP.PLANNODESTATUS != 2 OR TCP.PLANNODESTATUS IS NULL )AND TCP.PROJECTID = TCU.CONTRACTID ) AS WEICHULI,")
    	.append(" (SELECT COUNT(0) FROM TBL_CONTRACT_PLANNODE TCP WHERE  TCP.PROJECTID = TCU.CONTRACTID ) AS ZONGSHU,")
    	.append(" (SELECT COUNT(0) FROM TBL_CYHW_UNIT WHERE RECORDPARENT = TCU.CONTRACTID AND (CONTRACTSTATUS = 0 OR CONTRACTSTATUS IS NULL)) ZISTATUS, ")
    	.append(" (SELECT COUNT(0) FROM TBL_CYHW_UNIT WHERE RECORDPARENT = TCU.CONTRACTID AND RECORDTYPE = 'HTGL005' AND (CONTRACTSTATUS >= 6 OR CONTRACTSTATUS = 4)) BGSTATUS ");
        sqlSb.append(" FROM TBL_CYHW_UNIT TCU WHERE ");
        sqlSb.append("( TCU.RECORDTYPE = 'HTGL002' OR TCU.RECORDTYPE = 'HTGL005')  AND TCU.CONTRACTSTATUS >= 6 ");
        //sqlSb.append(" TCU.CONTRACTID IN (SELECT CONTRACTID FROM TBL_CONTRACT_LEND WHERE USERID = "+unit.getStaffid()+" AND LENDSTATUS = 6 AND RETURNDATE >= "+DataBaseSqlConfig.getDateStrFormat(new Date())+") OR ( TCU.CONTRACTID IN (SELECT CONTRACTID FROM TBL_CONTRACT_TRAN WHERE TRANSTATUS = 6) )");
        
        
        if(unit.getNodeCount()!=null && unit.getNodeCount()==1) {
        	sqlSb.append(" AND TCU.ORGID = "+unit.getOrgid());
        }else {
            if(unit.getContractstaff() != null && unit.getCreateuser() != null) {
            	sqlSb.append(" AND ( TCU.CONTRACTSTAFF = "+unit.getContractstaff() + " OR TCU.CREATEUSER = "+unit.getCreateuser()+" ) ");
            }else if(unit.getContractstaff() != null) {
            	sqlSb.append(" AND TCU.CONTRACTSTAFF = "+unit.getContractstaff());
            }else if(unit.getCreateuser() != null) {
                sqlSb.append(" AND TCU.CREATEUSER = "+unit.getCreateuser());
            }
        }
        
        if(unit.getContractno() != null && !"".equals(unit.getContractno())) {
            sqlSb.append( " AND TCU.CONTRACTNO LIKE '%"+unit.getContractno()+"%'");
        }
        if(unit.getContractname() != null && !"".equals(unit.getContractname())) {
            sqlSb.append( " AND TCU.CONTRACTNAME LIKE '%"+unit.getContractname()+"%'");
        }
        sqlSb.append("  ORDER BY TCU.CREATETIME DESC )").append(DataBaseSqlConfig.setTableAliasName("T1"))
        .append(" WHERE ((CONTRACTPLAN ='否' OR CONTRACTPLAN  IS  NULL) OR (CONTRACTPLAN ='是' AND ((CONTRACTSTATUS = 7 AND YICHULI = ZONGSHU) OR CONTRACTSTATUS > 8 )) OR BGSTATUS > 0  )");
        //.append(" OR (CONTRACTSTATUS = 7) ))) AND ZISTATUS = 0 ");
        String sql = sqlSb.toString();
        return sql;
	}
	
	public String findContractSealList(IPage<TblCyhwUnit> page, TblCyhwUnit unit) throws Exception{
		StringBuffer sqlSb = new StringBuffer("SELECT tcu.CONTRACTID,SEALORG.ORGNAME SEALORGNAME,tcu.FLOWID,tcu.CONTRACTNAME,tcu.CONTRACTITEM,tcu.CONTRACTNO,tcu.CONTRACTSTATUS,tcp.COUNTERPARTCODE,tcp.COUNTERPARTHANK,"+
				"tcu.reservedstring1, tcu.reservedstring2, tcu.reservedstring3, tcu.reservedstring4, tcu.reservedstring5, tcu.reservedstring6, tcu.reservedstring7, tcu.reservedstring8, tcu.reservedstring9, tcu.reservedstring10, tcu.reserveddropdownmultiple1, tcu.reserveddropdownmultiple2, tcu.reserveddropdownmultiple3, tcu.reserveddropdownmultiple4, tcu.reserveddropdownmultiple5, tcu.reservedmultiplechoice1, tcu.reservedmultiplechoice2, tcu.reservedmultiplechoice3, tcu.reservedmultiplechoice4, tcu.reservedmultiplechoice5, tcu.reservedyeartime1, tcu.reservedyeartime2, tcu.reservedyeartime3, tcu.reservedyeartime4, tcu.reservedyeartime5, tcu.reservedyearaccuratetime1, tcu.reservedyearaccuratetime2, tcu.reservedyearaccuratetime3, tcu.reservedyearaccuratetime4,tcu.reservedyearaccuratetime5, tcu.reservedtime1, tcu.reservedtime2, tcu.reservedtime3, tcu.reservedtime4, tcu.reservedtime5, tcu.reservedsinglechoice1, tcu.reservedsinglechoice2, tcu.reservedsinglechoice3, tcu.reservedsinglechoice4, tcu.reservedsinglechoice5, tcu.reserveddropdownsinglechoice1, tcu.reserveddropdownsinglechoice2, tcu.reserveddropdownsinglechoice3, tcu.reserveddropdownsinglechoice4, tcu.reserveddropdownsinglechoice5, tcu.reservednum1, tcu.reservednum2, tcu.reservednum3, tcu.reservednum4, tcu.reservednum5, tcu.staffid1, tcu.staffid2, tcu.staffid3, tcu.staffid4, tcu.staffid5, tcu.staffids1, tcu.staffids2, tcu.staffids3, tcu.staffids4, tcu.staffids5, tcu.orgid1, tcu.orgid2, tcu.orgid3, tcu.orgid4, tcu.orgid5, tcu.orgids1, tcu.orgids2, tcu.orgids3, tcu.orgids4, tcu.orgids5,"
				+DataBaseSqlConfig.getNullColumn("tcp.INSPECTIONSTATUS","-1")+" inspectionstatus,tcp.BUDGETID "	        		+ " from TBL_CYHW_UNIT tcu " +
	                "  LEFT JOIN TBL_CYHW_PROJECTBUDGET tcp ON tcu.CONTRACTID = tcp.RECORDPARENT AND tcp.RECORDTYPE = '"+unit.getRecordtype()+"' " +
	                "  LEFT JOIN TBL_ORGANIZATION SEALORG ON SEALORG.ORGID = tcp.SEALORGID"
	                + " LEFT JOIN TBL_CONTRACT_PROJECT TCPJ ON TCU.TOPICID = TCPJ.PROJECTID "
	               // + " LEFT JOIN TBL_CONTRACT_APPENDIXSIGNING app on TCU.CONTRACTID=APP.CONSTRACTID "
	                + "  where tcu.ORGID= "+unit.getOrgid()
	                +" AND (tcu.RECORDTYPE = 'HTGL002' OR tcu.RECORDTYPE = 'HTGL005') AND tcu.CONTRACTSTATUS >= 6");
	        if(unit.getJbstaff()!= null) {
	            sqlSb.append(" AND tcu.CREATEUSER = "+unit.getJbstaff());
	        }
	        if(unit.getContractname() != null && !"".equals(unit.getContractname())) {
	            sqlSb.append( " AND tcu.CONTRACTNAME LIKE '%"+unit.getContractname()+"%'");
	        }
	        sqlSb.append(" ORDER BY tcu.CREATETIME DESC ");
	        String sql = sqlSb.toString();
	        return sql;
	}
	
	public String  findListByPageInfo(IPage<TblCyhwUnit> page, TblCyhwUnit tcu, String orgId) throws Exception{
        StringBuffer sqlSb = new StringBuffer("SELECT tcu.CONTRACTID,tcu.FLOWID,tcu.UNITNAME,tcu.CONTRACTNAME,tcu.CONTRACTNO,tcu.CONTRACTDEPT,tcu.CONTRACTLINK,tcu.CONTRACTMONEY,tcu.CONTRACTSTATUS,tcu.STARTDATE,tcu.ENDDATE,tcu.CONTRACTITEM,tcu.CONTRACTTYPE,tcu.CONTRACTDATETYPE,tcu.CONTRACTXZ,tcu.RECORDTYPE,tcu.DCTYPE,tcu.CONTRACTBD from TBL_CYHW_UNIT tcu LEFT JOIN TBL_CYHW_PROJECTBUDGET tcp ON tcu.CONTRACTXDFXINFO = tcp.BUDGETID  where tcu.ORGID="+orgId);
        if(tcu.getContractno()!=null && !"".equals(tcu.getContractno())){
            sqlSb.append(" AND TCU.CONTRACTNO LIKE '%" + tcu.getContractno() + "%'");
        }
        if(tcu.getContractname() != null && !"".equals(tcu.getContractname())) {
            sqlSb.append(" AND tcu.CONTRACTNAME LIKE '%"+tcu.getContractname()+"%'");
        }
        if(tcu.getContractbd() != null && !"".equals(tcu.getContractbd())) {
            sqlSb.append(" AND tcu.CONTRACTBD LIKE '%"+tcu.getContractbd()+"%'");
        }
        if(tcu.getContracttype() != null && !"".equals(tcu.getContracttype())) {
            sqlSb.append(" AND tcu.CONTRACTTYPE LIKE '%"+tcu.getContracttype()+"%'");
        }
        if(tcu.getChoicejbunitid() != null && !"".equals(tcu.getChoicejbunitid())) {
            sqlSb.append(" AND tcu.LINKDEPT = '"+tcu.getChoicejbunitid()+"'");
        }
        if(tcu.getChoicecontractDeptId() != null && !"".equals(tcu.getChoicecontractDeptId())) {
            sqlSb.append(" AND tcu.CONTRACTDEPT = '"+tcu.getChoicecontractDeptId()+"'");
        }
        if(tcu.getRecordConcatname() != null && !"".equals(tcu.getRecordConcatname())) {
            sqlSb.append(" AND tcp.BUDGETNAME LIKE '%"+tcu.getRecordConcatname()+"%'");
        }

        if(tcu.getRecordtype() != null && !"".equals(tcu.getRecordtype())){
            switch (tcu.getRecordtype()) {
                case "HTGL002":
                    sqlSb.append("AND tcu.RECORDTYPE='HTGL002'");
                    break;
                case "HTGL005":
                    sqlSb.append(" AND tcu.RECORDTYPE = 'HTGL002' AND tcu.CONTRACTID NOT IN ( SELECT RECORDPARENT FROM TBL_CYHW_PROJECTBUDGET WHERE RECORDTYPE='HTGL006' AND INSPECTIONSTATUS != 3)");
                    break;
                case "HTGL003":
                    sqlSb.append(" AND (tcu.RECORDTYPE = 'HTGL002' OR tcu.RECORDTYPE = 'HTGL005') AND tcu.CONTRACTID NOT IN (SELECT RECORDPARENT FROM TBL_CYHW_PROJECTBUDGET WHERE RECORDTYPE = 'HTGL003' AND INSPECTIONSTATUS != 3) "
                            + " AND tcu.CONTRACTID NOT IN ( SELECT RECORDPARENT FROM TBL_CYHW_PROJECTBUDGET WHERE RECORDTYPE='HTGL006' AND INSPECTIONSTATUS != 3)");
                    break;
                case "HTGL004":
                    sqlSb.append(" AND tcu.CONTRACTID IN (SELECT RECORDPARENT FROM TBL_CYHW_PROJECTBUDGET WHERE RECORDTYPE = 'HTGL003' AND INSPECTIONSTATUS = 6) AND tcu.RECORDTYPE = 'HTGL002'");
                    break;
                case "HTGL006":
                    sqlSb.append(" AND tcu.CONTRACTID IN (SELECT RECORDPARENT FROM TBL_CYHW_PROJECTBUDGET WHERE RECORDTYPE = 'HTGL003' AND INSPECTIONSTATUS = 6 ) AND ((SELECT COUNT(0) FROM TBL_CONTRACT_PLANNODE WHERE PROJECTID = CONTRACTID)=(SELECT COUNT(0) FROM TBL_CONTRACT_SPNODE WHERE BUDGETID IN (SELECT BUDGETID FROM TBL_CYHW_PROJECTBUDGET WHERE RECORDTYPE = 'HTGL004' AND INSPECTIONSTATUS = 6 AND NODEFINISHDATE IS NOT NULL) "
                            +" AND NODEID IN (SELECT NODEID FROM TBL_CONTRACT_PLANNODE WHERE PROJECTID = tcu.CONTRACTID)))"
                            + " AND tcu.CONTRACTID NOT IN ( SELECT RECORDPARENT FROM TBL_CYHW_PROJECTBUDGET WHERE RECORDTYPE='HTGL006' AND INSPECTIONSTATUS != 3)");
                    break;
                default:
                    break;
            }
        }
        if(tcu.getContractstatus() != null){
            sqlSb.append(" AND tcu.CONTRACTSTATUS = "+tcu.getContractstatus());
        }
        sqlSb.append(" ORDER BY tcu.createtime DESC");
        String sql = sqlSb.toString();
        return sql;
	}
	
	public String updateCyhwUnit(TblCyhwUnit tcu) throws Exception {
        StringBuffer sql = new StringBuffer("UPDATE TBL_CYHW_UNIT SET CONTRACTNAME = '"+tcu.getContractname()+"'");
        if(tcu.getIsmany() != null) {
        	sql.append(" ,ISMANY = '"+tcu.getIsmany()+"'");
        }
        if(tcu.getAgreementcount() != null) {
        	sql.append(" ,AGREEMENTCOUNT = '"+tcu.getAgreementcount()+"'");
        }
        
        if(tcu.getTypefl() != null) {
            sql.append(" ,TYPEFL = '"+tcu.getTypefl()+"'");
        }
        if(tcu.getUnitname() != null) {
            sql.append(" ,UNITNAME = '"+tcu.getUnitname()+"'");
        }
        if(tcu.getFlowid() != null) {
            sql.append(" ,FLOWID = '"+tcu.getFlowid()+"'");
        }
        if(tcu.getContractno() != null) {
            sql.append(" ,CONTRACTNO = '"+tcu.getContractno()+"'");
        }
        if(tcu.getContractdept() != null) {
            sql.append(" ,CONTRACTDEPT = '"+tcu.getContractdept()+"'");
        }
        if(tcu.getContractlink() != null) {
            sql.append(" ,CONTRACTLINK = '"+tcu.getContractlink()+"'");
        }
        if(tcu.getContractmoney() != null) {
            sql.append(" ,CONTRACTMONEY = '"+tcu.getContractmoney()+"'");
        }
        if(tcu.getContractstatus() != null) {
            sql.append(" ,CONTRACTSTATUS = '"+tcu.getContractstatus()+"'");
        }
        if(tcu.getLinkdept() != null) {
            sql.append(" ,LINKDEPT = '"+tcu.getLinkdept()+"'");
        }
        if(tcu.getOrgid() != null) {
            sql.append(" ,ORGID = '"+tcu.getOrgid()+"'");
        }
        if(tcu.getCreateuser() != null) {
            sql.append(" ,CREATEUSER = '"+tcu.getCreateuser()+"'");
        }
        if(tcu.getMomoconcat() != null) {
            sql.append(" ,MOMOCONCAT = '"+tcu.getMomoconcat()+"'");
        }
        if(tcu.getRiskcontrol() != null) {
            sql.append(" ,RISKCONTROL = '"+tcu.getRiskcontrol()+"'");
        }
        if(tcu.getDescribe() != null) {
            sql.append(" ,DESCRIBE = '"+tcu.getDescribe()+"'");
        }
        if(tcu.getContractstaff() != null) {
            sql.append(" ,CONTRACTSTAFF = '"+tcu.getContractstaff()+"'");
        }
        if(tcu.getCreatetime() != null) {
            sql.append(" ,CREATETIME = "+DataBaseSqlConfig.getDateStrFormat(tcu.getCreatetime()));
        }
        if(tcu.getScrilevel() != null) {
            sql.append(" ,SCRILEVEL = '"+tcu.getScrilevel()+"'");
        }
        if(tcu.getStartdate() != null) {
            sql.append(" ,STARTDATE = "+DataBaseSqlConfig.getDateStrFormat(tcu.getStartdate()));
        }
        if(tcu.getEnddate() != null) {
            sql.append(" ,ENDDATE = "+DataBaseSqlConfig.getDateStrFormat(tcu.getEnddate()));
        }
        if(tcu.getContractitem() != null) {
            sql.append(" ,CONTRACTITEM = '"+tcu.getContractitem()+"'");
        }
        if(tcu.getContracttype() != null) {
            sql.append(" ,CONTRACTTYPE = '"+tcu.getContracttype()+"'");
        }
        if(tcu.getContractdatetype() != null) {
            sql.append(" ,CONTRACTDATETYPE = '"+tcu.getContractdatetype()+"'");
        }
        if(tcu.getContractxz() != null) {
            sql.append(" ,CONTRACTXZ = '"+tcu.getContractxz()+"'");
        }
        if(tcu.getContractxdfxinfo() != null) {
            sql.append(" ,CONTRACTXDFXINFO = '"+tcu.getContractxdfxinfo()+"'");
        }
        if(tcu.getRecordtype() != null) {
            sql.append(" ,RECORDTYPE = '"+tcu.getRecordtype()+"'");
        }
        if(tcu.getHzsumowing() != null) {
            sql.append(" ,HZSUMOWING = '"+tcu.getHzsumowing()+"'");
        }
        if(tcu.getJijiatype() != null) {
            sql.append(" ,JIJIATYPE = '"+tcu.getJijiatype()+"'");
        }
        if(tcu.getMoneytype() != null) {
            sql.append(" ,MONEYTYPE = '"+tcu.getMoneytype()+"'");
        }
        if(tcu.getDctype() != null) {
            sql.append(" ,DCTYPE = '"+tcu.getDctype()+"'");
        }
        if(tcu.getContractbd() != null) {
            sql.append(" ,CONTRACTBD = '"+tcu.getContractbd()+"'");
        }
        if(tcu.getContractzd() != null) {
            sql.append(" ,CONTRACTZD = '"+tcu.getContractzd()+"'");
        }
        if(tcu.getJbstaff() != null) {
            sql.append(" ,JBSTAFF = '"+tcu.getJbstaff()+"'");
        }
        if(tcu.getJbdept() != null) {
            sql.append(" ,JBDEPT = '"+tcu.getJbdept()+"'");
        }
        if(tcu.getJbunit() != null) {
            sql.append(" ,JBUNIT = '"+tcu.getJbunit()+"'");
        }
        if(tcu.getZxunit() != null) {
            sql.append(" ,ZXUNIT = '"+tcu.getZxunit()+"'");
        }
        if(tcu.getContractchildren() != null) {
            sql.append(" ,CONTRACTCHILDREN = '"+tcu.getContractchildren()+"'");
            if("否".equals(tcu.getContractchildren()) && tcu.getRecordparent() != null && "HTGL002".equals(tcu.getRecordtype())) {
            	sql.append(" ,RECORDPARENT = NULL");
            }else if(tcu.getRecordparent() != null){
            	sql.append(" ,RECORDPARENT = '"+tcu.getRecordparent()+"'");
            }
        }
        if(tcu.getContractplan() != null) {
            sql.append(" ,CONTRACTPLAN = '"+tcu.getContractplan()+"'");
        }
        if(tcu.getTopicid() != null) {
            sql.append(" ,TOPICID = '"+tcu.getTopicid()+"'");
        }
        if(tcu.getTopicname() != null) {
            sql.append(" ,TOPICNAME = '"+tcu.getTopicname()+"'");
        }
        if(tcu.getHiscontractstatus() != null) {
            sql.append(" ,HISCONTRACTSTATUS = '"+tcu.getHiscontractstatus()+"'");
        }
        if(tcu.getChangetype() != null) {
            sql.append(" ,CHANGETYPE = '"+tcu.getChangetype()+"'");
        }
        if(tcu.getChangedate() != null) {
            sql.append(" ,CHANGEDATE = "+DataBaseSqlConfig.getDateStrFormat(tcu.getChangedate()));
        }
        if(tcu.getChangedesc() != null) {
            sql.append(" ,CHANGEDESC = '"+tcu.getChangedesc()+"'");
        }
        if(tcu.getCounterpartbank() != null) {
            sql.append(" ,COUNTERPARTBANK = '"+tcu.getCounterpartbank()+"'");
        }
        if(tcu.getIsbigmatter() != null) {//是否三重一大事项
            sql.append(" ,ISBIGMATTER = '"+tcu.getIsbigmatter()+"'");
        }
        if(tcu.getMatterorg() != null) {//事项审议机构
            sql.append(" ,MATTERORG = '"+tcu.getMatterorg()+"'");
        }
        
        //授权委托人
        if(tcu.getEntrustStaffId() != null) {
            sql.append(" ,ENTRUSTSTAFFID = '"+tcu.getEntrustStaffId()+"'");
        }

        sql.append(" WHERE CONTRACTID = '"+tcu.getContractid()+"'");
        return sql.toString();
    }
	
	public String insertContractBudget(BigDecimal contractid, String xdfxId, String bugetType,String contractname) {
        StringBuffer column = new StringBuffer("INSERT INTO TBL_CONTRACT_BUDGET (ID");
        StringBuffer value = new StringBuffer(" VALUES ("+RandomUtil.uuBigDecimalId());

        if(contractid != null) {
            column.append(",CONTRACTID");
            value.append(",'"+contractid+"'");
        }
        if(xdfxId != null) {
            column.append(",BUDGETID");
            value.append(",'"+xdfxId+"'");
        }
        if(bugetType != null) {
            column.append(",BUDGETTYPE");
            value.append(",'"+bugetType+"'");
        }
        if(contractname != null) {
            column.append(",CONTRACTNAME");
            value.append(",'"+contractname+"'");
        }
      
        column.append(")");
        value.append(")");
        String sql = column.toString()+value.toString();
        return sql;
    }

    public String insertContractBudgetOrder(BigDecimal contractid, String xdfxId, String bugetType,String contractname,Integer orderby) {
        StringBuffer column = new StringBuffer("INSERT INTO TBL_CONTRACT_BUDGET (ID");
        StringBuffer value = new StringBuffer(" VALUES ("+RandomUtil.uuBigDecimalId());

        if(contractid != null) {
            column.append(",CONTRACTID");
            value.append(",'"+contractid+"'");
        }
        if(xdfxId != null) {
            column.append(",BUDGETID");
            value.append(",'"+xdfxId+"'");
        }
        if(bugetType != null) {
            column.append(",BUDGETTYPE");
            value.append(",'"+bugetType+"'");
        }
        if(contractname != null) {
            column.append(",CONTRACTNAME");
            value.append(",'"+contractname+"'");
        }
        if(orderby != null) {
            column.append(",ORDERBY");
            value.append(",'"+orderby+"'");
        }

        column.append(")");
        value.append(")");
        String sql = column.toString()+value.toString();
        return sql;
    }
	
	
	
	
	public String selectUnitListByPageInfo(IPage<TblCyhwUnit> page, String staffid, Integer paCount,TblCyhwUnit unit) throws Exception{
        StringBuffer sqlSb = new StringBuffer("SELECT tcu.CONTRACTID,tcu.FLOWID,tcu.CONTRACTNAME,tcu.CONTRACTNO,tcu.CONTRACTMONEY,tcu.CONTRACTSTATUS,tcu.CREATETIME,tcu.SCRILEVEL,tcu.STARTDATE,tcu.ENDDATE,tcu.CONTRACTITEM,tcu.CONTRACTTYPE,tcu.CONTRACTDATETYPE,tcu.CONTRACTXZ,tcu.RECORDTYPE,tcu.DCTYPE,TORG.ORGNAME AS orgname "
        		+ " from TBL_CYHW_UNIT tcu LEFT JOIN TBL_CYHW_PROJECTBUDGET tcp ON tcu.CONTRACTXDFXINFO = tcp.BUDGETID "
        		+ " LEFT JOIN TBL_ORGANIZATION TORG ON tcu.JBUNIT = TORG.ORGID where 1=1" ); //tcu.ORGID=\"+orgid

        if(paCount != 0) {
            sqlSb.append(" AND tcu.CREATEUSER = '"+staffid+"'");
        }
        if(unit.getBudgetid() != null) {
            sqlSb.append(" AND tcu.CONTRACTXDFXINFO = "+unit.getBudgetid()+"");
        }
        if(unit.getContractno()!=null && !"".equals(unit.getContractno())){
            sqlSb.append(" AND tcu.CONTRACTNO LIKE '%"+unit.getContractno()+"%'");
        }
        if(unit.getContractname() != null && !"".equals(unit.getContractname())) {
            sqlSb.append(" AND tcu.CONTRACTNAME LIKE '%"+unit.getContractname()+"%'");
        }
        if(unit.getContractbd() != null && !"".equals(unit.getContractbd())) {
            sqlSb.append(" AND tcu.CONTRACTBD LIKE '%"+unit.getContractbd()+"%'");
        }
        if(unit.getContracttype() != null && !"".equals(unit.getContracttype())) {
            sqlSb.append(" AND tcu.CONTRACTTYPE LIKE '%"+unit.getContracttype()+"%'");
        }
        if(unit.getLinkdept() != null && !"".equals(unit.getLinkdept())) {
            sqlSb.append(" AND tcu.LINKDEPT = "+unit.getLinkdept()+"");
        }
        if(unit.getBudgetname() != null && !"".equals(unit.getBudgetname())) {
            sqlSb.append(" AND tcp.BUDGETNAME LIKE '%"+unit.getBudgetname()+"%'");
        }

        if(unit.getRecordtype()!=null && !"".equals(unit.getRecordtype())){
            sqlSb.append(" AND tcu.RECORDTYPE = '"+unit.getRecordtype()+"' ");
        }
        if(unit.getContractstatus() != null){
            sqlSb.append(" AND tcu.CONTRACTSTATUS = "+unit.getContractstatus()+"");
        }
        if(unit.getContractdept() != null) {
        	sqlSb.append(" AND tcu.CONTRACTDEPT = "+unit.getContractdept()+"");
        }
        sqlSb.append(" ORDER BY tcu.CREATETIME DESC ");
        String sql = sqlSb.toString();
        return sql;
	}
	
	public String selectUnitListInfo(IPage<TblCyhwUnit> page, String orgid, String staffid, Integer paCount,TblCyhwUnit unit,String secrectScopeIds) throws Exception {
        StringBuffer sqlSb = new StringBuffer("SELECT tcu.CONTRACTID,tcu.FLOWID,tcu.CONTRACTNAME,tcu.CONTRACTNO,tcu.CREATEUSER"
        		+ ",tcu.CONTRACTMONEY,tcu.CONTRACTSTATUS,tcu.CREATETIME,tcu.SCRILEVEL,tcu.STARTDATE,tcu.ENDDATE,"
        		+ "tcu.CONTRACTITEM,tcu.CONTRACTTYPE,tcu.CONTRACTDATETYPE,tcu.CONTRACTXZ,tcu.RECORDTYPE,tcu.DCTYPE"
        		+ ",TORG.ORGNAME AS orgname "
        		+ " from TBL_CYHW_UNIT tcu LEFT JOIN TBL_CYHW_PROJECTBUDGET tcp ON tcu.CONTRACTXDFXINFO = tcp.BUDGETID "
        		+ " LEFT JOIN TBL_ORGANIZATION TORG ON tcu.JBUNIT = TORG.ORGID where tcu.ORGID="+orgid );

        sqlSb.append(" AND ("+DataBaseSqlConfig.getWhereColumnInStr("STAFFSCOPEIDS", staffid,",")+" OR tcu.CREATEUSER = '"+staffid+"' " +
//                "OR STAFFSCOPEIDS IS NULL OR STAFFSCOPEIDS = '' " +
                ")");
        
        if(StringUtils.isNotBlank(secrectScopeIds)) {
        	sqlSb.append(" AND (SECRECTLEVELID IN (").append(secrectScopeIds).append(") OR SECRECTLEVELID IS NULL OR SECRECTLEVELID = ''  )");
        }else {
        	sqlSb.append(" AND (SECRECTLEVELID IS NULL OR SECRECTLEVELID = ''  )");
            
        }
        
        /*if(StringUtils.isNotBlank(condition.getDeptIds())) {
			sqlSb.append(" (TZI.CREATESTAFF = ").append(condition.getCreateStaff()).append(" OR TZI.LINKORGID IN (").append(condition.getDeptIds()).append(") OR TZI.LINKDEPTID IN (").append(condition.getDeptIds()).append("))");
		}else {
			sqlSb.append(" TZI.CREATESTAFF = ").append(condition.getCreateStaff());
		}*/
        
        
        
//        if(paCount != 0) {
//            sqlSb.append(" AND tcu.CREATEUSER = '"+staffid+"'");
//        }
        if(unit.getBudgetid() != null) {
            sqlSb.append(" AND tcu.CONTRACTXDFXINFO = "+unit.getBudgetid()+"");
        }
        if(unit.getContractno()!=null && !"".equals(unit.getContractno())){
            sqlSb.append(" AND tcu.CONTRACTNO LIKE '%"+unit.getContractno()+"%'");
        }
        if(unit.getContractname() != null && !"".equals(unit.getContractname())) {
            sqlSb.append(" AND tcu.CONTRACTNAME LIKE '%"+unit.getContractname()+"%'");
        }
        if(unit.getContractbd() != null && !"".equals(unit.getContractbd())) {
            sqlSb.append(" AND tcu.CONTRACTBD LIKE '%"+unit.getContractbd()+"%'");
        }
        if(unit.getContracttype() != null && !"".equals(unit.getContracttype())) {
            sqlSb.append(" AND tcu.CONTRACTTYPE LIKE '%"+unit.getContracttype()+"%'");
        }
        if(unit.getLinkdept() != null && !"".equals(unit.getLinkdept())) {
            sqlSb.append(" AND tcu.LINKDEPT = "+unit.getLinkdept()+"");
        }
        if(unit.getBudgetname() != null && !"".equals(unit.getBudgetname())) {
            sqlSb.append(" AND tcp.BUDGETNAME LIKE '%"+unit.getBudgetname()+"%'");
        }

        if(unit.getRecordtype()!=null && !"".equals(unit.getRecordtype())){
            sqlSb.append(" AND tcu.RECORDTYPE = '"+unit.getRecordtype()+"' ");
        }
        if(unit.getContractstatus() != null){
            sqlSb.append(" AND tcu.CONTRACTSTATUS = "+unit.getContractstatus()+"");
        }
        if(unit.getContractdept() != null) {
        	sqlSb.append(" AND tcu.CONTRACTDEPT = "+unit.getContractdept()+"");
        }
        
        
        sqlSb.append(" ORDER BY tcu.CREATETIME DESC ");
        String sql = sqlSb.toString();
        return sql;
	}
	
	
	public String selectCountByContractNo(String contractno, String contractnolike) throws Exception {
		String sql = "SELECT "+DataBaseSqlConfig.getMaxNoDeal("CONTRACTNO", "-", contractno)+"  FROM TBL_CYHW_UNIT WHERE CONTRACTNO LIKE '"+contractnolike+"%'";
		return sql;
	}
	
	 public String selectLedgerListPageInfo(IPage<TblCyhwUnit> page, TblCyhwUnit condition, String fatherOrgIds) throws Exception {
	        StringBuffer sqlSb = new StringBuffer("SELECT TCU.CONTRACTID,TCU.FLOWID,TCU.CONTRACTNAME,TCU.CONTRACTNO,TCU.CONTRACTMONEY,TCU.CONTRACTSTATUS,TCU.CREATETIME,"
	        		+ "TCU.RECORDTYPE,TCU.CONTRACTITEM,TCPJ.PROJECTNAME AS TOPICNAME,TCU.CONTRACTTYPE,TCU.DCTYPE,TCP.BUDGETNAME,ORG.ORGNAME AS ORGNAME,CASE (SELECT COUNT(0) FROM TBL_CONTRACT_SPNODE TCS WHERE TCS.ISWY = '是' AND TCS.CONTRACTID = TCU.CONTRACTID) WHEN 0 THEN '否' ELSE '是' END AS ISWY "
	        		+ "FROM TBL_CYHW_UNIT TCU LEFT JOIN TBL_CONTRACT_PROJECT TCPJ ON TCU.TOPICID = TCPJ.PROJECTID  "
	        		+ "LEFT JOIN TBL_CYHW_PROJECTBUDGET TCP ON TCU.CONTRACTXDFXINFO = TCP.BUDGETID LEFT JOIN TBL_ORGANIZATION ORG ON TCU.ORGID = ORG.ORGID "
	        		+ "WHERE TCU.RECORDTYPE IN ('HTGL002','HTGL005') ");
	        		
	        if(StringUtils.isNotBlank(fatherOrgIds)) {
	        	sqlSb.append(" AND TCU.ORGID IN ("+fatherOrgIds+")");
	        }else {
	        	sqlSb.append(" AND TCU.ORGID = "+condition.getOrgid());
	        }
	        		
//	        		+" AND TCU.CONTRACTID NOT IN (SELECT RECORDPARENT FROM TBL_CYHW_UNIT WHERE RECORDTYPE = 'HTGL005' AND ORGID = TCU.ORGID AND RECORDPARENT IS NOT NULL) ");
	        if(condition.getContractstaff() != null) {
	        	sqlSb.append(" AND TCU.CONTRACTSTAFF = "+condition.getContractstaff());
	        }
	        
	        if(condition.getCreateuser() != null) {
	            sqlSb.append(" AND TCU.CREATEUSER = "+condition.getCreateuser());
	        }
	        if(condition.getContractno() != null && !"".equals(condition.getContractno())) {
	            sqlSb.append(" AND TCU.CONTRACTNO LIKE '%"+condition.getContractno()+"%'");
	        }
	        if(condition.getContractname() != null && !"".equals(condition.getContractname())) {
	            sqlSb.append(" AND TCU.CONTRACTNAME LIKE '%"+condition.getContractname()+"%'");
	        }
	        if(condition.getContractitem() != null && !"".equals(condition.getContractitem())) {
	            sqlSb.append(" AND TCPJ.PROJECTNAME LIKE '%"+condition.getContractitem()+"%'");
	        }
	        if(condition.getContracttype() != null && !"".equals(condition.getContracttype())) {
	            sqlSb.append(" AND TCU.CONTRACTTYPE LIKE '%"+condition.getContracttype()+"%'");
	        }
	        if(condition.getBudgetname() != null && !"".equals(condition.getBudgetname())) {
	            sqlSb.append(" AND TCP.BUDGETNAME LIKE '%"+condition.getBudgetname()+"%'");
	        }
	        //增加承办部门
	        if(condition.getContractdept() != null) {
	            sqlSb.append(" AND TCU.CONTRACTDEPT = "+condition.getContractdept()+"");
	        }
	        //增加承办人
	        if(condition.getContractstaff() != null) {
	            sqlSb.append(" AND TCU.CONTRACTSTAFF = "+condition.getContractstaff()+"");
	        }
	        if(condition.getDctype() != null && !"".equals(condition.getDctype())) {
	            sqlSb.append(" AND TCU.DCTYPE = '"+condition.getDctype()+"'");
	        }
	        if(condition.getContractstatus() != null) {
	            if(condition.getContractstatus() == 0) {
	                sqlSb.append(" AND (TCU.CONTRACTSTATUS = " + condition.getContractstatus()+" OR TCU.CONTRACTSTATUS IS NULL)");
	            }else {
	                sqlSb.append(" AND TCU.CONTRACTSTATUS = "+condition.getContractstatus()+"");
	            }
	        }
	        if(condition.getMinMoney() != null) {
	            sqlSb.append(" AND TCU.CONTRACTMONEY >= '" + condition.getMinMoney()+"'");
	        }
	        if(condition.getMaxMoney() != null) {
	            sqlSb.append(" AND TCU.CONTRACTMONEY <= '" + condition.getMaxMoney()+"'");
	        }

	        if(condition.getStartdate() != null) {
	            sqlSb.append(" AND TCU.CREATETIME >= "+ DataBaseSqlConfig.getDateStrFormat(condition.getStartdate()));
	        }
	        if(condition.getEnddate() != null) {
	            sqlSb.append("AND TCU.CREATETIME <= "+DataBaseSqlConfig.getDateStrFormat(condition.getEnddate()));
	        }
	        
	        if(condition.getIsWy() != null && !"".equals(condition.getIsWy())){
	        	if("是".equals(condition.getIsWy())) {
	        		sqlSb.append(" AND TCU.CONTRACTID IN ( SELECT CONTRACTID FROM TBL_CONTRACT_SPNODE WHERE ISWY = '"+condition.getIsWy()+"')");
	        	}else {
	        		sqlSb.append(" AND TCU.CONTRACTID NOT IN ( SELECT CONTRACTID FROM TBL_CONTRACT_SPNODE WHERE ISWY = '"+condition.getIsWy()+"')");
	        	}
	        }
	        
	        sqlSb.append(" ORDER BY TCU.CONTRACTID DESC ");
	        String sql = sqlSb.toString();
	        return sql;
	    }
	
	 public String selectLedgerOrgListPageInfo(IPage<TblCyhwUnit> page, TblCyhwUnit unit, String allCompanyIds) throws Exception {
	        StringBuffer sqlSb = new StringBuffer("SELECT TCU.CONTRACTID,TCU.FLOWID,TCU.CONTRACTNAME,TCU.CONTRACTNO,TCU.CONTRACTMONEY,TCU.CONTRACTSTATUS,TCU.CREATETIME,"
	        		+ "TCU.RECORDTYPE,TCU.CONTRACTITEM,TCPJ.PROJECTNAME AS TOPICNAME,TCU.CONTRACTTYPE,TCU.DCTYPE,TCP.BUDGETNAME,ORG.ORGNAME,CASE (SELECT COUNT(0) FROM TBL_CONTRACT_SPNODE TCS WHERE TCS.ISWY = '是' AND TCS.CONTRACTID = TCU.CONTRACTID) WHEN 0 THEN '否' ELSE '是' END AS ISWY "
	        		+ "FROM TBL_CYHW_UNIT TCU "
	        		+ "LEFT JOIN TBL_CONTRACT_PROJECT TCPJ ON TCU.TOPICID = TCPJ.PROJECTID  "
	        		+ "LEFT JOIN TBL_CYHW_PROJECTBUDGET TCP ON TCU.CONTRACTXDFXINFO = TCP.BUDGETID LEFT JOIN TBL_ORGANIZATION ORG ON TCU.ORGID = ORG.ORGID "
	        		+ "WHERE TCU.RECORDTYPE IN ('HTGL002','HTGL005') ");
//	        		+" AND TCU.CONTRACTID NOT IN (SELECT RECORDPARENT FROM TBL_CYHW_UNIT WHERE RECORDTYPE = 'HTGL005' AND ORGID = TCU.ORGID AND RECORDPARENT IS NOT NULL) ");

	        if(unit.getOrgname()!=null && unit.getOrgname().length()>0) {
	            sqlSb.append(" AND TCU.ORGID = "+unit.getOrgid());
	        }else {
	        	sqlSb.append(" AND TCU.ORGID IN ("+allCompanyIds+") ");
	        }
	        
	        
	        if(unit.getCreateuser() != null) {
	            sqlSb.append(" AND TCU.CREATEUSER = "+unit.getCreateuser()+"");
	        }
	        if(unit.getContractno() != null && !"".equals(unit.getContractno())) {
	            sqlSb.append(" AND TCU.CONTRACTNO LIKE '%"+unit.getContractno()+"%'");
	        }
	        if(unit.getContractname() != null && !"".equals(unit.getContractname())) {
	            sqlSb.append(" AND TCU.CONTRACTNAME LIKE '%"+unit.getContractname()+"%'");
	        }
	        if(unit.getContractitem() != null && !"".equals(unit.getContractitem())) {
	            sqlSb.append(" AND TCPJ.PROJECTNAME LIKE '%"+unit.getContractitem()+"%'");
	        }
	        if(unit.getContracttype() != null && !"".equals(unit.getContracttype())) {
	            sqlSb.append(" AND TCU.CONTRACTTYPE LIKE '"+unit.getContracttype()+"'");
	        }
	        if(unit.getBudgetname() != null && !"".equals(unit.getBudgetname())) {
	            sqlSb.append(" AND TCP.BUDGETNAME LIKE '%"+unit.getBudgetname()+"%'");
	        }
	        if(unit.getDctype() != null && !"".equals(unit.getDctype())) {
	            sqlSb.append(" AND TCU.DCTYPE = '"+unit.getDctype()+"'");
	        }
	        if(unit.getContractdept() != null) {
	        	sqlSb.append(" AND TCU.CONTRACTDEPT = "+unit.getContractdept()+"");
	        }
	        if(unit.getContractstaff() != null) {
	       	 	sqlSb.append(" AND TCU.CONTRACTSTAFF = "+unit.getContractstaff()+"");
	        }
	        if(unit.getContractstatus() != null) {
	            if(unit.getContractstatus() == 0) {
	                sqlSb.append(" AND (TCU.CONTRACTSTATUS = " + unit.getContractstatus()+" OR TCU.CONTRACTSTATUS IS NULL)");
	            }else {
	                sqlSb.append(" AND TCU.CONTRACTSTATUS = "+unit.getContractstatus()+"");
	            }
	        }
	        if(unit.getMinMoney() != null) {
	            sqlSb.append(" AND TCU.CONTRACTMONEY >= '" + unit.getMinMoney()+"'");
	        }
	        if(unit.getMaxMoney() != null) {
	            sqlSb.append(" AND TCU.CONTRACTMONEY <= '" + unit.getMaxMoney()+"'");
	        }

	        if(unit.getStartdate() != null) {
	            sqlSb.append(" AND TCU.CREATETIME >= "+DataBaseSqlConfig.getDateStrFormat(unit.getStartdate()));
	        }
	        if(unit.getEnddate() != null) {
	            sqlSb.append("AND TCU.CREATETIME <= "+DataBaseSqlConfig.getDateStrFormat(unit.getEnddate()));

	        }
	        if(unit.getIsWy() != null && !"".equals(unit.getIsWy())){
	        	if("是".equals(unit.getIsWy())) {
	        		sqlSb.append(" AND TCU.CONTRACTID IN ( SELECT CONTRACTID FROM TBL_CONTRACT_SPNODE WHERE ISWY = '"+unit.getIsWy()+"')");
	        	}else {
	        		sqlSb.append(" AND TCU.CONTRACTID NOT IN ( SELECT CONTRACTID FROM TBL_CONTRACT_SPNODE WHERE ISWY = '"+unit.getIsWy()+"')");
	        	}
	        }
	        sqlSb.append(" ORDER BY TCU.CONTRACTID DESC ");
	        String sql = sqlSb.toString();
	        return sql;
	    }
	    
	 public String findLedgerListForExport(TblCyhwUnit unit, String fatherOrgIds) throws Exception {
	        StringBuffer sqlSb = new StringBuffer("SELECT  TCU.CONTRACTID,TCU.CONTRACTNO,TCU.CONTRACTNAME,TCPJ.PROJECTNAME AS TOPICNAME,TCU.CONTRACTMONEY,TCU.CONTRACTITEM,TCU.CONTRACTTYPE,TCU.CREATETIME,TCU.STARTDATE"
	        		+ " ,TCU.ENDDATE,TCP.BUDGETNAME AS recordConcatname,ZXB.ORGNAME AS choicecontractDeptId,ZXU.ORGNAME AS choicejbunitid,TCU.CONTRACTSTATUS,TS.REALNAME AS topic,	"
	        		+ " (SELECT COUNT(0) FROM TBL_CYHW_UNIT_ATT WHERE  CONTRACTID = TCU.CONTRACTID) AS nodeCount,YYS.CREATETIME AS yongyintime,TCU.DCTYPE,"
	        		+ " CASE (SELECT COUNT(0) FROM TBL_CONTRACT_SPNODE TCS WHERE TCS.ISWY = '是' AND TCS.CONTRACTID = TCU.CONTRACTID) WHEN 0 THEN '否' ELSE '是' END AS ISWY "
	        		+ " FROM TBL_CYHW_UNIT TCU LEFT JOIN TBL_CONTRACT_PROJECT TCPJ ON TCU.TOPICID = TCPJ.PROJECTID  "
	        		+ " LEFT JOIN TBL_CYHW_PROJECTBUDGET TCP ON TCU.CONTRACTXDFXINFO = TCP.BUDGETID LEFT JOIN TBL_ORGANIZATION ZXB ON TCU.CONTRACTDEPT = ZXB.ORGID LEFT JOIN TBL_ORGANIZATION ZXU ON TCU.ZXUNIT = ZXU.ORGID"
	        		+ " LEFT JOIN TBL_STAFF TS ON TCU.CONTRACTSTAFF = TS.STAFFID LEFT JOIN TBL_CYHW_PROJECTBUDGET YYS ON TCU.CONTRACTID = YYS.RECORDPARENT AND YYS.RECORDTYPE = 'HTGL003' WHERE TCU.RECORDTYPE IN ('HTGL002','HTGL005') ");
	        		
	        if(StringUtils.isNotBlank(fatherOrgIds)) {
	        	sqlSb.append(" AND TCU.ORGID IN ("+fatherOrgIds+")");
	        }else {
	        	sqlSb.append(" AND TCU.ORGID = "+unit.getOrgid());
	        }
	        		
//	        		+" AND TCU.CONTRACTID NOT IN (SELECT RECORDPARENT FROM TBL_CYHW_UNIT WHERE RECORDTYPE = 'HTGL005' AND ORGID = TCU.ORGID AND RECORDPARENT IS NOT NULL) ");
	        if(unit.getContractstaff() != null) {
	        	sqlSb.append(" AND TCU.CONTRACTSTAFF = "+unit.getContractstaff());
	        }
	        
	        if(unit.getCreateuser() != null) {
	            sqlSb.append(" AND TCU.CREATEUSER = "+unit.getCreateuser());
	        }
	        if(unit.getContractno() != null && !"".equals(unit.getContractno())) {
	            sqlSb.append(" AND TCU.CONTRACTNO LIKE '%"+unit.getContractno()+"%'");
	        }
	        if(unit.getContractname() != null && !"".equals(unit.getContractname())) {
	            sqlSb.append(" AND TCU.CONTRACTNAME LIKE '%"+unit.getContractname()+"%'");
	        }
	        if(unit.getContractitem() != null && !"".equals(unit.getContractitem())) {
	            sqlSb.append(" AND TCPJ.PROJECTNAME LIKE '%"+unit.getContractitem()+"%'");
	        }
	        if(unit.getContracttype() != null && !"".equals(unit.getContracttype())) {
	            sqlSb.append(" AND TCU.CONTRACTTYPE LIKE '%"+unit.getContracttype()+"%'");
	        }
	        if(unit.getBudgetname() != null && !"".equals(unit.getBudgetname())) {
	            sqlSb.append(" AND TCP.BUDGETNAME LIKE '%"+unit.getBudgetname()+"%'");
	        }
	        //增加承办部门
	        if(unit.getContractdept() != null) {
	            sqlSb.append(" AND TCU.CONTRACTDEPT = "+unit.getContractdept()+"");
	        }
	        //增加承办人
	        if(unit.getContractstaff() != null) {
	            sqlSb.append(" AND TCU.CONTRACTSTAFF = "+unit.getContractstaff()+"");
	        }
	        if(unit.getDctype() != null && !"".equals(unit.getDctype())) {
	            sqlSb.append(" AND TCU.DCTYPE = '"+unit.getDctype()+"'");
	        }
	        if(unit.getContractstatus() != null) {
	            if(unit.getContractstatus() == 0) {
	                sqlSb.append(" AND (TCU.CONTRACTSTATUS = " + unit.getContractstatus()+" OR TCU.CONTRACTSTATUS IS NULL)");
	            }else {
	                sqlSb.append(" AND TCU.CONTRACTSTATUS = "+unit.getContractstatus()+"");
	            }
	        }
	        if(unit.getMinMoney() != null) {
	            sqlSb.append(" AND TCU.CONTRACTMONEY >= '" + unit.getMinMoney()+"'");
	        }
	        if(unit.getMaxMoney() != null) {
	            sqlSb.append(" AND TCU.CONTRACTMONEY <= '" + unit.getMaxMoney()+"'");
	        }

	        if(unit.getStartdate() != null) {
	            sqlSb.append(" AND TCU.CREATETIME >= "+ DataBaseSqlConfig.getDateStrFormat(unit.getStartdate()));
	        }
	        if(unit.getEnddate() != null) {
	            sqlSb.append("AND TCU.CREATETIME <= "+DataBaseSqlConfig.getDateStrFormat(unit.getEnddate()));

	        }
	        if(unit.getIsWy() != null && !"".equals(unit.getIsWy())){
	        	sqlSb.append(" AND TCU.CONTRACTID IN (SELECT CONTRACTID FROM TBL_CONTRACT_SPNODE WHERE ISWY = '"+unit.getIsWy()+"')");
	        }
	        sqlSb.append(" ORDER BY TCU.CONTRACTID DESC ");
	        String sql = sqlSb.toString();
	        return sql;
	    }
	
	 public String findListByXdf(IPage<TblCyhwUnit> page, String budgetid) throws Exception {
	        StringBuffer sqlSb = new StringBuffer("SELECT * from TBL_CYHW_UNIT WHERE CONTRACTID IN (SELECT CONTRACTID FROM TBL_CONTRACT_BUDGET WHERE BUDGETID = '"+budgetid+"')");

	        sqlSb.append(" ORDER BY CONTRACTID DESC ");
	        String sql = sqlSb.toString();
	        return sql;
	 }
	 
	 public String findCollectionChoiceContractPid(IPage<TblCyhwUnit> page, BigDecimal pid, String contractno,String contractname) {
	        StringBuffer sbSql = new StringBuffer("SELECT TCU.CONTRACTID,TCU.CONTRACTNAME,TCU.CONTRACTNO,TCU.CONTRACTMONEY,TCU.CONTRACTTYPE,TCU.STARTDATE,TCU.ENDDATE,TCP.BUDGETID,TCP.BUDGETNAME,TCB.BANKID,TCB.BANKACCOUNT" +
	                " FROM TBL_CYHW_UNIT TCU " +
	                " LEFT JOIN TBL_CYHW_PROJECTBUDGET TCP ON TCU.CONTRACTXDFXINFO = TCP.BUDGETID " +
	                " LEFT JOIN TBL_COUNTERPART_BANKINFO TCB ON TCU.COUNTERPARTBANK = TCB.BANKID "+
	                "WHERE TCU.RECORDTYPE IN ('HTGL002','HTGL005') AND TCU.ORGID = " + pid + " AND TCU.CONTRACTSTATUS IN (7,17) AND TCU.CONTRACTID IN (SELECT PROJECTID FROM TBL_CONTRACT_PLANNODE WHERE PLANNODESTATUS = 2 AND NODEID NOT IN (SELECT NODEID FROM TBL_CONTRACT_COLLECTION)) AND TCU.DCTYPE = '收款' ");
	        if (contractno != null && !"".equals(contractno)) {
	            sbSql.append(" AND TCU.CONTRACTNO LIKE '%" + contractno + "%'");
	        }
	        if (contractname != null && !"".contentEquals(contractname)) {
	            sbSql.append(" AND TCU.CONTRACTNAME LIKE '%" + contractname + "%'");
	        }

	        sbSql.append(" ORDER BY TCU.CONTRACTID DESC");
	        String sql = sbSql.toString();
	        return sql;
	    }

	 public String selectListByPageInfo(IPage<TblCyhwUnit> page, TblCyhwUnit unit) {
		 StringBuffer sqlSb = new StringBuffer("SELECT TCU.CONTRACTID,TCPAY.PAYMENMONEY,TCU.CONTRACTNAME,TCU.CONTRACTNO, TCU.CONTRACTMONEY,TCU.CONTRACTTYPE,TCU.STARTDATE,TCU.ENDDATE,TS.REALNAME,TCPB.BUDGETID,TCPB.COUNTERPARTNO,TCPB.BUDGETNAME,TCPB.COUNTERPARTHANK,TCPB.COUNTERPARTHANKACCOUNT,TCPB.PROJECTSTAGEGOAL,COUNTERPARTPHONE,(SELECT SUM(PAYMENMONEY) FROM TBL_CONTRACT_PAYMENT TCPAY WHERE TCPAY.CONTRACTID = TCU.CONTRACTID AND PAYMENTSTATUS = 6) AS PAYMONEY FROM TBL_CYHW_UNIT TCU LEFT JOIN TBL_CONTRACT_PAYMENT TCPAY ON TCPAY.CONTRACTID = TCU.CONTRACTID LEFT JOIN TBL_STAFF TS ON TS.STAFFID = TCU.CONTRACTSTAFF LEFT JOIN TBL_CYHW_PROJECTBUDGET TCPB ON TCU.CONTRACTXDFXINFO = TCPB.BUDGETID WHERE TCU.RECORDTYPE IN ('HTGL002','HTGL005') AND TCU.ORGID =  " + unit.getOrgid() + "  AND TCU.CONTRACTSTATUS IN (7,17) AND TCU.CONTRACTID IN (SELECT PROJECTID FROM TBL_CONTRACT_PLANNODE WHERE PLANNODESTATUS = 2 AND NODEID NOT IN ( SELECT NODEID FROM TBL_CONTRACT_PAYMENT)) AND TCU.DCTYPE = '付款' ");
	        if (unit.getContractno() != null && !"".equals(unit.getContractno())) {
	            sqlSb.append(" AND TCU.CONTRACTNO LIKE '%" + unit.getContractno() + "%'");
	        }
	        if (unit.getContractname() != null && !"".contentEquals(unit.getContractname())) {
	            sqlSb.append(" AND TCU.CONTRACTNAME LIKE '%" + unit.getContractname() + "%'");
	        }
	        sqlSb.append(" ORDER BY TCU.CONTRACTID DESC");
	        String sql = sqlSb.toString();
	        return sql;
	 }
	 
	 public String getYearMoney(String orgid, String startDate, String endDate) throws Exception {
			String qdSql = "SELECT SUM(CONTRACTMONEY) FROM TBL_CYHW_UNIT WHERE RECORDTYPE IN ('HTGL002','HTGL005')  AND ORGID = "+orgid+" AND CREATETIME >= "+DataBaseSqlConfig.getDateStrFormat(startDate)+" AND CREATETIME <= "+DataBaseSqlConfig.getDateStrFormat(endDate)+" and contractid in (select distinct contractid  from tbl_cyhw_unit  where ( contractid in (SELECT RECORDPARENT FROM TBL_CYHW_PROJECTBUDGET WHERE RECORDTYPE = 'HTGL003' AND INSPECTIONSTATUS = 6  AND ORGID =  "+orgid+")  or  contractid in (select contractid  from tbl_cyhw_unit where CONTRACTSTATUS in (6,7,8)  AND ORGID = "+orgid+" ))  AND CONTRACTID NOT IN ( SELECT RECORDPARENT FROM TBL_CYHW_UNIT WHERE RECORDTYPE = 'HTGL005' AND ORGID =  "+orgid+" AND RECORDPARENT IS NOT NULL ) )";
			return qdSql;
	}
	 
	 public String getContractCount(String orgid, String startDate, String endDate) throws Exception {
		 String countSql = "SELECT COUNT(0) FROM TBL_CYHW_UNIT WHERE RECORDTYPE IN ('HTGL002','HTGL005') AND ORGID = "+orgid+" AND CREATETIME >= "+DataBaseSqlConfig.getDateStrFormat(startDate)+" AND CREATETIME <= "+DataBaseSqlConfig.getDateStrFormat(endDate)+" and contractid in (select distinct contractid  from tbl_cyhw_unit  where ( contractid in (SELECT RECORDPARENT FROM TBL_CYHW_PROJECTBUDGET WHERE RECORDTYPE = 'HTGL003' AND INSPECTIONSTATUS = 6  AND ORGID =  "+orgid+")  or  contractid in (select contractid  from tbl_cyhw_unit where CONTRACTSTATUS in (6,7,8)  AND ORGID = "+orgid+" ))  AND CONTRACTID NOT IN ( SELECT RECORDPARENT FROM TBL_CYHW_UNIT WHERE RECORDTYPE = 'HTGL005' AND ORGID =  "+orgid+" AND RECORDPARENT IS NOT NULL ) )";
		 return countSql;
	 }
	 
	 public String getBreachCount(String orgid, String startDate, String endDate) throws Exception {
		 String wyCount = "SELECT COUNT(0) FROM TBL_CYHW_UNIT TCU LEFT JOIN TBL_CONTRACT_SPNODE TCS ON TCU.CONTRACTID = TCS.CONTRACTID WHERE TCU.RECORDTYPE IN ('HTGL002','HTGL005')  AND TCU.ORGID = "+orgid+" AND CREATETIME >= "+DataBaseSqlConfig.getDateStrFormat(startDate)+" AND CREATETIME <= "+DataBaseSqlConfig.getDateStrFormat(endDate)+" and TCU.CONTRACTID  in (select distinct contractid  from tbl_cyhw_unit  where ( contractid in (SELECT RECORDPARENT FROM TBL_CYHW_PROJECTBUDGET WHERE RECORDTYPE = 'HTGL003' AND INSPECTIONSTATUS = 6  AND ORGID =  "+orgid+")  or  contractid in (select contractid  from tbl_cyhw_unit where CONTRACTSTATUS in (6,7,8)  AND ORGID = "+orgid+" ))  AND CONTRACTID NOT IN ( SELECT RECORDPARENT FROM TBL_CYHW_UNIT WHERE RECORDTYPE = 'HTGL005' AND ORGID =  "+orgid+" AND RECORDPARENT IS NOT NULL ) ) AND TCS.ISWY = '是'";
		 return wyCount;
	}
	 
	 public String getCounterpartCount(String orgid, String startDate, String endDate) throws Exception {
		String budCount = "SELECT COUNT(0) FROM TBL_CYHW_PROJECTBUDGET WHERE RECORDTYPE = 'HTGL001'  ";
			budCount += " and BUDGETID IN   ( SELECT DISTINCT BG.BUDGETID FROM TBL_CONTRACT_BUDGET bg LEFT JOIN TBL_CYHW_UNIT tcu ON BG.CONTRACTID=TCU.CONTRACTID WHERE  TCU.ORGID="+orgid;
			budCount += " AND TCU.CREATETIME >= "+DataBaseSqlConfig.getDateStrFormat(startDate)+" AND TCU.CREATETIME <= "+DataBaseSqlConfig.getDateStrFormat(endDate);
			budCount +="  )";
			return budCount;
	 }
	 
	 public String getReceivables(String orgid, String startDate, String endDate) throws Exception {
			String sql="SELECT SUM(NODEMONEY)  FROM TBL_CONTRACT_PLANNODE WHERE PROJECTID IN (SELECT CONTRACTID  FROM TBL_CYHW_UNIT  WHERE RECORDTYPE IN ('HTGL002', 'HTGL005')  AND ORGID = "+orgid+"  and  CONTRACTID  in (select distinct contractid  from tbl_cyhw_unit  where ( contractid in (SELECT RECORDPARENT FROM TBL_CYHW_PROJECTBUDGET WHERE RECORDTYPE = 'HTGL003' AND INSPECTIONSTATUS = 6  AND ORGID =  "+orgid+")  or  contractid in (select contractid  from tbl_cyhw_unit where CONTRACTSTATUS in (6,7,8)  AND ORGID = "+orgid+" ))  AND CONTRACTID NOT IN ( SELECT RECORDPARENT FROM TBL_CYHW_UNIT WHERE RECORDTYPE = 'HTGL005' AND ORGID =  "+orgid+" AND RECORDPARENT IS NOT NULL ) ) AND DCTYPE = '收款') "
					+ " and NODEPLANPAYDATE >= "+DataBaseSqlConfig.getDateStrFormat(startDate)+" AND NODEPLANPAYDATE <= "+DataBaseSqlConfig.getDateStrFormat(endDate);
			return sql;
		}
	 
	public String getActualCollection(String orgid, String startDate, String endDate) throws Exception{
		String sjskSql = "SELECT SUM(INVOICEMONEY) FROM TBL_CONTRACT_INVOICESMANAGEMEN TCI LEFT JOIN TBL_CONTRACT_COLLECTION TCC ON TCI.INVOICEID = TCC.INVOICEID WHERE TCC.COLLECTIONSTATUS = 6 AND TCC.CONTRACTID IN (SELECT CONTRACTID FROM TBL_CYHW_UNIT WHERE RECORDTYPE IN ('HTGL002','HTGL005') AND ORGID = "+orgid+
				" AND CREATETIME >= "+DataBaseSqlConfig.getDateStrFormat(startDate)+" AND CREATETIME <= "+DataBaseSqlConfig.getDateStrFormat(endDate)+" and CONTRACTID  in (select distinct contractid  from tbl_cyhw_unit  where ( contractid in (SELECT RECORDPARENT FROM TBL_CYHW_PROJECTBUDGET WHERE RECORDTYPE = 'HTGL003' AND INSPECTIONSTATUS = 6  AND ORGID =  "+orgid+")  or  contractid in (select contractid  from tbl_cyhw_unit where CONTRACTSTATUS in (6,7,8)  AND ORGID = "+orgid+" ))  AND CONTRACTID NOT IN ( SELECT RECORDPARENT FROM TBL_CYHW_UNIT WHERE RECORDTYPE = 'HTGL005' AND ORGID =  "+orgid+" AND RECORDPARENT IS NOT NULL ) ) AND DCTYPE = '收款')";
		return sjskSql;
	 }
	
	public String getPayable(String orgid, String startDate, String endDate) throws Exception{
		String sql="SELECT SUM(NODEMONEY) FROM TBL_CONTRACT_PLANNODE WHERE PROJECTID IN (SELECT CONTRACTID FROM TBL_CYHW_UNIT WHERE RECORDTYPE IN ('HTGL002','HTGL005') AND ORGID = "+orgid+"  and  CONTRACTID  in (select distinct contractid  from tbl_cyhw_unit  where ( contractid in (SELECT RECORDPARENT FROM TBL_CYHW_PROJECTBUDGET WHERE RECORDTYPE = 'HTGL003' AND INSPECTIONSTATUS = 6  AND ORGID =  "+orgid+")  or  contractid in (select contractid  from tbl_cyhw_unit where CONTRACTSTATUS in (6,7,8)  AND ORGID = "+orgid+" ))  AND CONTRACTID NOT IN ( SELECT RECORDPARENT FROM TBL_CYHW_UNIT WHERE RECORDTYPE = 'HTGL005' AND ORGID =  "+orgid+" AND RECORDPARENT IS NOT NULL ) ) AND DCTYPE = '付款' "
				+ " AND NODEPLANPAYDATE >= "+DataBaseSqlConfig.getDateStrFormat(startDate)+" AND NODEPLANPAYDATE <= "+DataBaseSqlConfig.getDateStrFormat(endDate)+" )  ";
		return sql;
	}
	
	public String getActualPayment(String orgid, String startDate, String endDate) throws Exception{
		String sjfkSql = "SELECT SUM(PAYMENMONEY) FROM  TBL_CONTRACT_PAYMENT WHERE PAYMENTSTATUS = 6 AND CONTRACTID IN (SELECT CONTRACTID FROM TBL_CYHW_UNIT WHERE RECORDTYPE IN ('HTGL002','HTGL005') AND ORGID = "+orgid
				+" AND CREATETIME >= "+DataBaseSqlConfig.getDateStrFormat(startDate)+" AND CREATETIME <= "+DataBaseSqlConfig.getDateStrFormat(endDate)
				+" and CONTRACTID  in (select distinct contractid  from tbl_cyhw_unit  where ( contractid in (SELECT RECORDPARENT FROM TBL_CYHW_PROJECTBUDGET WHERE RECORDTYPE = 'HTGL003' AND INSPECTIONSTATUS = 6  AND ORGID =  "+orgid+")  or  contractid in (select contractid  from tbl_cyhw_unit where CONTRACTSTATUS in (6,7,8)  AND ORGID = "+orgid+" ))  AND CONTRACTID NOT IN ( SELECT RECORDPARENT FROM TBL_CYHW_UNIT WHERE RECORDTYPE = 'HTGL005' AND ORGID =  "+orgid+" AND RECORDPARENT IS NOT NULL ) ) AND DCTYPE = '付款'";
		sjfkSql += ")";
		return sjfkSql;
	}
	
	public String getContractType(String orgid, String startDate, String endDate) throws Exception {
		String htqk = "SELECT CONTRACTTYPE AS name,COUNT(0) AS value FROM TBL_CYHW_UNIT WHERE RECORDTYPE IN ('HTGL002','HTGL005') AND ORGID = "
				+ orgid + " AND CREATETIME >= " + DataBaseSqlConfig.getDateStrFormat(startDate) + " AND CREATETIME <= "+ DataBaseSqlConfig.getDateStrFormat(endDate)
				+ "and  CONTRACTID  in (select contractid  from tbl_cyhw_unit  where ( contractid in (SELECT RECORDPARENT FROM TBL_CYHW_PROJECTBUDGET WHERE RECORDTYPE = 'HTGL003' AND INSPECTIONSTATUS = 6  AND ORGID =  "+orgid+")  or  contractid in (select contractid  from tbl_cyhw_unit where CONTRACTSTATUS in (6,7,8)  AND ORGID = "+orgid+" ))  AND CONTRACTID NOT IN ( SELECT RECORDPARENT FROM TBL_CYHW_UNIT WHERE RECORDTYPE = 'HTGL005' AND ORGID =  "+orgid+" AND RECORDPARENT IS NOT NULL ) )";
		htqk += " GROUP BY CONTRACTTYPE ORDER BY value desc";
		return htqk;
	}
	
	public String getContractMonthYear(String orgid, String startDate, String endDate) throws Exception {
		 String sql="SELECT COUNT(0) AS value,"+DataBaseSqlConfig.getDateColumn("CREATETIME", "MM")+" AS name FROM TBL_CYHW_UNIT WHERE RECORDTYPE IN ('HTGL002', 'HTGL005') AND ORGID = "+orgid
				 + " AND CREATETIME >= " + DataBaseSqlConfig.getDateStrFormat(startDate)+ " AND CREATETIME <= "+DataBaseSqlConfig.getDateStrFormat(endDate)
				 + " AND CONTRACTID  in (select distinct contractid  from tbl_cyhw_unit  where ( contractid in (SELECT RECORDPARENT FROM TBL_CYHW_PROJECTBUDGET WHERE RECORDTYPE = 'HTGL003' AND INSPECTIONSTATUS = 6  AND ORGID =  "+orgid+")  or  contractid in (select contractid  from tbl_cyhw_unit where CONTRACTSTATUS in (6,7,8)  AND ORGID = "+orgid+" ))  AND CONTRACTID NOT IN ( SELECT RECORDPARENT FROM TBL_CYHW_UNIT WHERE RECORDTYPE = 'HTGL005' AND ORGID =  "+orgid+" AND RECORDPARENT IS NOT NULL ) )"
				 + " group by "+DataBaseSqlConfig.getDateColumn("CREATETIME", "MM")+"  order by "+DataBaseSqlConfig.getDateColumn("CREATETIME", "MM");
		return sql;
	}
	
	public String getOrgCount(String orgid, String startDate, String endDate) throws Exception {
		String sjfkSql = "SELECT O.Orgname AS name,count(0) AS value FROM TBL_CYHW_UNIT u  left join TBL_ORGANIZATION O ON O.ORGID = u.contractdept"
		 +" WHERE RECORDTYPE IN ('HTGL002', 'HTGL005') and O.orgtype=0 and O.status=0 AND u.ORGID ="+orgid
		 +" AND CREATETIME >= " + DataBaseSqlConfig.getDateStrFormat(startDate) + " AND CREATETIME <= "+DataBaseSqlConfig.getDateStrFormat(endDate)
		 +" AND CONTRACTID in (select distinct contractid  from tbl_cyhw_unit  where ( contractid in (SELECT RECORDPARENT FROM TBL_CYHW_PROJECTBUDGET WHERE RECORDTYPE = 'HTGL003' AND INSPECTIONSTATUS = 6  AND ORGID =  "+orgid+")  or  contractid in (select contractid  from tbl_cyhw_unit where CONTRACTSTATUS in (6,7,8)  AND ORGID = "+orgid+" ))  AND CONTRACTID NOT IN ( SELECT RECORDPARENT FROM TBL_CYHW_UNIT WHERE RECORDTYPE = 'HTGL005' AND ORGID =  "+orgid+" AND RECORDPARENT IS NOT NULL ) )";
		sjfkSql += " GROUP BY O.Orgname";
		return sjfkSql;
	}
	
	public String getCompanyCount2(String orgid, String startDate, String endDate) throws Exception{
		String sjfkSql = "SELECT count(0) AS value FROM TBL_CYHW_UNIT u  left join TBL_ORGANIZATION O   ON O.ORGID=u.orgid"
			+" WHERE RECORDTYPE IN ('HTGL002', 'HTGL005') and u.orgid = "+orgid
			+ " AND CREATETIME >= "+DataBaseSqlConfig.getDateStrFormat(startDate)+" AND CREATETIME <= "+DataBaseSqlConfig.getDateStrFormat(endDate)
			+ " AND CONTRACTID  in (select contractid  from tbl_cyhw_unit  where ( contractid in (SELECT RECORDPARENT FROM TBL_CYHW_PROJECTBUDGET WHERE RECORDTYPE = 'HTGL003' AND INSPECTIONSTATUS = 6  AND ORGID =  "+orgid+")  or  contractid in (select contractid  from tbl_cyhw_unit where CONTRACTSTATUS in (6,7,8)  AND ORGID = "+orgid+" ))  AND CONTRACTID NOT IN ( SELECT RECORDPARENT FROM TBL_CYHW_UNIT WHERE RECORDTYPE = 'HTGL005' AND ORGID =  "+orgid+" AND RECORDPARENT IS NOT NULL ) )";
		return sjfkSql;
	}
	
	public String getCompanyAmount2(String orgid, String startDate, String endDate) throws Exception {
		String sjfkSql = "SELECT sum(contractmoney) AS value FROM TBL_CYHW_UNIT u  left join TBL_ORGANIZATION O   ON O.ORGID=u.orgid"
		 +" WHERE RECORDTYPE IN ('HTGL002', 'HTGL005') and u.orgid = "+orgid
		 +" and  dctype='付款' AND CREATETIME >= "
		 + DataBaseSqlConfig.getDateStrFormat(startDate)+" AND CREATETIME <= "+DataBaseSqlConfig.getDateStrFormat(endDate)
		 +" and  CONTRACTID  in (select contractid  from tbl_cyhw_unit  where ( contractid in (SELECT RECORDPARENT FROM TBL_CYHW_PROJECTBUDGET WHERE RECORDTYPE = 'HTGL003' AND INSPECTIONSTATUS = 6  AND ORGID =  "+orgid+")  or  contractid in (select contractid  from tbl_cyhw_unit where CONTRACTSTATUS in (6,7,8)  AND ORGID = "+orgid+" ))  AND CONTRACTID NOT IN ( SELECT RECORDPARENT FROM TBL_CYHW_UNIT WHERE RECORDTYPE = 'HTGL005' AND ORGID =  "+orgid+" AND RECORDPARENT IS NOT NULL ) ) ";
		return sjfkSql;
	}
	
	public String findLegalContractListByPageInfo(IPage<TblCyhwUnit> page, TblCyhwUnit tcu) throws Exception{
		StringBuffer sbSql = new StringBuffer("SELECT TCU.CONTRACTID,TCU.FLOWID,TCU.CONTRACTNAME,TCU.CONTRACTNO,TCU.CONTRACTMONEY,TCU.CONTRACTTYPE,TCU.CONTRACTITEM,TCU.CONTRACTBD," +
                "TS.REALNAME FROM TBL_CYHW_UNIT TCU LEFT JOIN TBL_STAFF TS ON TCU.CONTRACTSTAFF = TS.STAFFID WHERE TCU.ORGID = " + tcu.getOrgid() + "" +
                " AND TCU.RECORDTYPE IN ('HTGL002','HTGL005') AND TCU.CONTRACTID NOT IN (SELECT RECORDPARENT FROM TBL_CYHW_UNIT WHERE ORGID = " + tcu.getOrgid() + " AND RECORDTYPE = 'HTGL005'  AND RECORDPARENT IS NOT NULL) " +
                " AND TCU.CONTRACTID NOT IN ( SELECT CONTRACTINFO FROM TBL_LEGAL_DISPUTREGISTRATION WHERE CONTRACTINFO IS NOT NULL )"+
                " AND CONTRACTSTATUS NOT IN (1,2,3,4,5,8,12,13,14,15,16)");
//                " AND CONTRACTSTATUS IN (7,9,11)");
        if(tcu.getContractno() != null && !"".equals(tcu.getContractno())) {
            sbSql.append(" AND CONTRACTNO LIKE '%"+tcu.getContractno()+"%'");
        }
        if(tcu.getContractname() != null && !"".equals(tcu.getContractname())) {
            sbSql.append(" AND CONTRACTNAME LIKE '%"+tcu.getContractname()+"%'");
        }
        sbSql.append(" ORDER BY TCU.CONTRACTID DESC");
        String sql = sbSql.toString();
        return sql;
	}
	
	public String getContractMonth(String orgid, Integer year) throws Exception {
		String sql="SELECT COUNT(0) AS value,"+DataBaseSqlConfig.getDateColumn("CREATETIME", "MM")+" AS name FROM TBL_CYHW_UNIT WHERE RECORDTYPE IN ('HTGL002', 'HTGL005') AND ORGID = "+orgid+" AND "+DataBaseSqlConfig.getDateColumn("CREATETIME", "YYYY")+" = "+year+" and  CONTRACTID  in (select distinct contractid  from tbl_cyhw_unit  where ( contractid in (SELECT RECORDPARENT FROM TBL_CYHW_PROJECTBUDGET WHERE RECORDTYPE = 'HTGL003' AND INSPECTIONSTATUS = 6  AND ORGID =  "+orgid+")  or  contractid in (select contractid  from tbl_cyhw_unit where CONTRACTSTATUS in (6,7,8)  AND ORGID = "+orgid+" ))  AND CONTRACTID NOT IN ( SELECT RECORDPARENT FROM TBL_CYHW_UNIT WHERE RECORDTYPE = 'HTGL005' AND ORGID =  "+orgid+" AND RECORDPARENT IS NOT NULL ) ) group by "+DataBaseSqlConfig.getDateColumn("CREATETIME", "MM")+"  order by "+DataBaseSqlConfig.getDateColumn("CREATETIME", "MM");
		return sql;
	}
	
	public String getAmountMonth(String orgid, Integer year) throws Exception {
		String sql="SELECT sum(contractmoney) AS value,"+DataBaseSqlConfig.getDateColumn("CREATETIME", "MM")+" AS name FROM TBL_CYHW_UNIT WHERE RECORDTYPE IN ('HTGL002', 'HTGL005') AND ORGID = "+orgid+" AND "+DataBaseSqlConfig.getDateColumn("CREATETIME", "YYYY")+" = "+year+"  and  CONTRACTID  in (select distinct contractid  from tbl_cyhw_unit  where ( contractid in (SELECT RECORDPARENT FROM TBL_CYHW_PROJECTBUDGET WHERE RECORDTYPE = 'HTGL003' AND INSPECTIONSTATUS = 6  AND ORGID =  "+orgid+")  or  contractid in (select contractid  from tbl_cyhw_unit where CONTRACTSTATUS in (6,7,8)  AND ORGID = "+orgid+" ))  AND CONTRACTID NOT IN ( SELECT RECORDPARENT FROM TBL_CYHW_UNIT WHERE RECORDTYPE = 'HTGL005' AND ORGID =  "+orgid+" AND RECORDPARENT IS NOT NULL ) ) group by "+DataBaseSqlConfig.getDateColumn("CREATETIME", "MM")+" order by "+DataBaseSqlConfig.getDateColumn("CREATETIME", "MM");
		return sql;
	}
	
	public String getPaymentAmount(String orgid, Integer year) throws Exception {
		String sql="SELECT SUM(NODEMONEY) AS value,"+DataBaseSqlConfig.getDateColumn("NODEPLANPAYDATE", "MM")+" AS name FROM TBL_CONTRACT_PLANNODE WHERE PROJECTID IN (SELECT CONTRACTID FROM TBL_CYHW_UNIT WHERE RECORDTYPE IN ('HTGL002','HTGL005') AND ORGID = "+orgid+"  and  CONTRACTID  in (select distinct contractid  from tbl_cyhw_unit  where ( contractid in (SELECT RECORDPARENT FROM TBL_CYHW_PROJECTBUDGET WHERE RECORDTYPE = 'HTGL003' AND INSPECTIONSTATUS = 6  AND ORGID =  "+orgid+")  or  contractid in (select contractid  from tbl_cyhw_unit where CONTRACTSTATUS in (6,7,8)  AND ORGID = "+orgid+" ))  AND CONTRACTID NOT IN ( SELECT RECORDPARENT FROM TBL_CYHW_UNIT WHERE RECORDTYPE = 'HTGL005' AND ORGID =  "+orgid+" AND RECORDPARENT IS NOT NULL ) ) AND DCTYPE = '付款' AND "+DataBaseSqlConfig.getDateColumn("NODEPLANPAYDATE", "YYYY")+" = "+year+" ) group by "+DataBaseSqlConfig.getDateColumn("NODEPLANPAYDATE", "MM")+" order by "+DataBaseSqlConfig.getDateColumn("NODEPLANPAYDATE", "MM");
		return sql;
	}
	
	public String getActualPaymentAmount(String orgid, Integer year) throws Exception {
		String sql="SELECT SUM(PAYMENMONEY) AS value,"+DataBaseSqlConfig.getDateColumn("APPLYDATE", "MM")+" AS name FROM TBL_CONTRACT_PAYMENT TCP WHERE PAYMENTSTATUS = 6 AND CONTRACTID IN (SELECT CONTRACTID FROM TBL_CYHW_UNIT WHERE RECORDTYPE IN ('HTGL002','HTGL005') AND ORGID = "+orgid+"  and  CONTRACTID  in (select distinct contractid  from tbl_cyhw_unit  where ( contractid in (SELECT RECORDPARENT FROM TBL_CYHW_PROJECTBUDGET WHERE RECORDTYPE = 'HTGL003' AND INSPECTIONSTATUS = 6  AND ORGID =  "+orgid+")  or  contractid in (select contractid  from tbl_cyhw_unit where CONTRACTSTATUS in (6,7,8)  AND ORGID = "+orgid+" ))  AND CONTRACTID NOT IN ( SELECT RECORDPARENT FROM TBL_CYHW_UNIT WHERE RECORDTYPE = 'HTGL005' AND ORGID =  "+orgid+" AND RECORDPARENT IS NOT NULL ) ) AND DCTYPE = '付款' )  AND "+DataBaseSqlConfig.getDateColumn("APPLYDATE", "YYYY")+"="+year+" group by "+DataBaseSqlConfig.getDateColumn("APPLYDATE", "MM")+" order by "+DataBaseSqlConfig.getDateColumn("APPLYDATE", "MM");
		return sql;
	}
	
	public String getCollectionAmount(String orgid, Integer year) throws Exception {
		String sql="SELECT SUM(NODEMONEY) AS value,"+DataBaseSqlConfig.getDateColumn("NODEPLANPAYDATE", "MM")+" AS name FROM TBL_CONTRACT_PLANNODE WHERE PROJECTID IN (SELECT CONTRACTID  FROM TBL_CYHW_UNIT  WHERE RECORDTYPE IN ('HTGL002', 'HTGL005')  AND ORGID = "+orgid+"  and  CONTRACTID  in (select distinct contractid  from tbl_cyhw_unit  where ( contractid in (SELECT RECORDPARENT FROM TBL_CYHW_PROJECTBUDGET WHERE RECORDTYPE = 'HTGL003' AND INSPECTIONSTATUS = 6  AND ORGID =  "+orgid+")  or  contractid in (select contractid  from tbl_cyhw_unit where CONTRACTSTATUS in (6,7,8)  AND ORGID = "+orgid+" ))  AND CONTRACTID NOT IN ( SELECT RECORDPARENT FROM TBL_CYHW_UNIT WHERE RECORDTYPE = 'HTGL005' AND ORGID =  "+orgid+" AND RECORDPARENT IS NOT NULL ) ) AND DCTYPE = '收款') and TO_CHAR(NODEPLANPAYDATE,'YYYY') ="+year+" group by "+DataBaseSqlConfig.getDateColumn("NODEPLANPAYDATE", "MM")+" order by "+DataBaseSqlConfig.getDateColumn("NODEPLANPAYDATE", "MM");
		return sql;
	}
	
	public String getActualCollectionAmount(String orgid, Integer year) throws Exception {
		String sql="SELECT SUM(INVOICEMONEY) AS value,"+DataBaseSqlConfig.getDateColumn("collectionskdate", "MM")+" AS name FROM TBL_CONTRACT_INVOICESMANAGEMEN TCI LEFT JOIN TBL_CONTRACT_COLLECTION TCC ON TCI.INVOICEID = TCC.INVOICEID LEFT JOIN TBL_CONTRACT_PLANNODE TCP ON TCC.NODEID = TCP.NODEID  WHERE TCC.COLLECTIONSTATUS = 6 AND TCC.CONTRACTID IN ( SELECT CONTRACTID FROM TBL_CYHW_UNIT WHERE RECORDTYPE IN ('HTGL002','HTGL005') AND ORGID = "+orgid+"  and  CONTRACTID  in (select distinct contractid  from tbl_cyhw_unit  where ( contractid in (SELECT RECORDPARENT FROM TBL_CYHW_PROJECTBUDGET WHERE RECORDTYPE = 'HTGL003' AND INSPECTIONSTATUS = 6  AND ORGID =  "+orgid+")  or  contractid in (select contractid  from tbl_cyhw_unit where CONTRACTSTATUS in (6,7,8)  AND ORGID = "+orgid+" ))  AND CONTRACTID NOT IN ( SELECT RECORDPARENT FROM TBL_CYHW_UNIT WHERE RECORDTYPE = 'HTGL005' AND ORGID =  "+orgid+" AND RECORDPARENT IS NOT NULL ) ) AND DCTYPE = '收款') and "+DataBaseSqlConfig.getDateColumn("collectionskdate", "YYYY")+" ="+year+"   group by "+DataBaseSqlConfig.getDateColumn("collectionskdate", "MM")+" order by "+DataBaseSqlConfig.getDateColumn("collectionskdate", "MM");
		return sql;
	}
	
	public String getOrgCollectionAmount(String orgid, Integer year) throws Exception {
	     String sql="SELECT SUM(INVOICEMONEY) AS value ,O.ORGNAME AS name FROM TBL_CONTRACT_INVOICESMANAGEMEN TCI  LEFT JOIN TBL_CONTRACT_COLLECTION TCC "
             +" ON TCI.INVOICEID = TCC.INVOICEID LEFT JOIN TBL_CONTRACT_PLANNODE TCP ON TCC.NODEID = TCP.NODEID left join TBL_CYHW_UNIT UN ON UN.CONTRACTID=TCC.CONTRACTID "
             +" LEFT JOIN TBL_ORGANIZATION O ON O.ORGID=UN.contractdept WHERE TCC.COLLECTIONSTATUS = 6 AND TCC.CONTRACTID IN (SELECT CONTRACTID   FROM TBL_CYHW_UNIT "
             +" WHERE RECORDTYPE IN ('HTGL002', 'HTGL005') AND ORGID ="+orgid+"    and  CONTRACTID  in (select distinct contractid  from tbl_cyhw_unit  where ( contractid in (SELECT RECORDPARENT FROM TBL_CYHW_PROJECTBUDGET WHERE RECORDTYPE = 'HTGL003' AND INSPECTIONSTATUS = 6  AND ORGID =  "+orgid+")  or  contractid in (select contractid  from tbl_cyhw_unit where CONTRACTSTATUS in (6,7,8)  AND ORGID = "+orgid+" ))  AND CONTRACTID NOT IN ( SELECT RECORDPARENT FROM TBL_CYHW_UNIT WHERE RECORDTYPE = 'HTGL005' AND ORGID =  "+orgid+" AND RECORDPARENT IS NOT NULL ) )  AND DCTYPE = '收款')  and "+DataBaseSqlConfig.getDateColumn("TCP.NODEPLANPAYDATE", "YYYY")+" = "+year+" GROUP BY O.ORGNAME ";
		return sql;
	}
	
	public String getOrgAmount(String orgid, Integer year,Integer quarter) throws Exception {
	     String sql="SELECT o.orgname AS name,SUM(NODEMONEY) AS value FROM TBL_CONTRACT_PLANNODE p "
	     		+ " left join tbl_cyhw_unit un on un.contractid=p.projectid"
	     		+ " left join tbl_organization o on o.orgid=un.contractdept"
               + " WHERE PROJECTID IN "
               + "(SELECT CONTRACTID   FROM TBL_CYHW_UNIT  WHERE RECORDTYPE IN ('HTGL002', 'HTGL005')  AND ORGID ="+orgid
               + " and o.orgname is not null and  CONTRACTID  in (select distinct contractid  from tbl_cyhw_unit  where ( contractid in (SELECT RECORDPARENT FROM TBL_CYHW_PROJECTBUDGET WHERE RECORDTYPE = 'HTGL003' AND INSPECTIONSTATUS = 6  AND ORGID =  "+orgid+")  or  contractid in (select contractid  from tbl_cyhw_unit where CONTRACTSTATUS in (6,7,8)  AND ORGID = "+orgid+" ))  AND CONTRACTID NOT IN ( SELECT RECORDPARENT FROM TBL_CYHW_UNIT WHERE RECORDTYPE = 'HTGL005' AND ORGID =  "+orgid+" AND RECORDPARENT IS NOT NULL ) ) AND DCTYPE = '付款' "
               + " AND "+DataBaseSqlConfig.getDateColumn("NODEPLANPAYDATE", "YYYY")+" = "+year;
	     
		if (quarter != null && quarter == 1) { // 第一季度
			sql += "AND "+DataBaseSqlConfig.getDateColumn("NODEPLANPAYDATE", "MM")+" in (1,2,3))";
		} else if (quarter != null && quarter == 2) {
			sql += "AND "+DataBaseSqlConfig.getDateColumn("NODEPLANPAYDATE", "MM")+" in (4,5,6))";
		} else if (quarter != null && quarter == 3) {
			sql += "AND "+DataBaseSqlConfig.getDateColumn("NODEPLANPAYDATE", "MM")+" in (7,8,9))";
		} else if (quarter != null && quarter == 4) {
			sql += "AND "+DataBaseSqlConfig.getDateColumn("NODEPLANPAYDATE", "MM")+" in (10,11,12))";
		}else{
			sql += ")";
		}
       sql+= " group by o.orgname";
		return sql;
	}
	
	public String getOrgFactualAmount(String orgid, Integer year,Integer quarter) throws Exception {
	     String sql="SELECT o.orgname name,SUM(PAYMENMONEY) value FROM   TBL_CONTRACT_PAYMENT TCP "
	     		+ " left join tbl_cyhw_unit un on un.contractid=TCP.contractid"
	     		+ " left join tbl_organization o on o.orgid=un.contractdept "
	     		+ " WHERE PAYMENTSTATUS = 6 AND TCP.CONTRACTID IN "
	     		+ " (SELECT CONTRACTID FROM TBL_CYHW_UNIT WHERE RECORDTYPE IN ('HTGL002','HTGL005') AND"
	     		+ " ORGID = "+orgid+" and  CONTRACTID  in (select distinct contractid  from tbl_cyhw_unit  where ( contractid in (SELECT RECORDPARENT FROM TBL_CYHW_PROJECTBUDGET WHERE RECORDTYPE = 'HTGL003' AND INSPECTIONSTATUS = 6  AND ORGID =  "+orgid+")  or  contractid in (select contractid  from tbl_cyhw_unit where CONTRACTSTATUS in (6,7,8)  AND ORGID = "+orgid+" ))  AND CONTRACTID NOT IN ( SELECT RECORDPARENT FROM TBL_CYHW_UNIT WHERE RECORDTYPE = 'HTGL005' AND ORGID =  "+orgid+" AND RECORDPARENT IS NOT NULL ) ) "
	     		+ " AND DCTYPE = '付款'  AND "+DataBaseSqlConfig.getDateColumn("APPLYDATE", "YYYY")+" = "+year;
	     if (quarter != null && quarter == 1) { // 第一季度
				sql += " AND "+DataBaseSqlConfig.getDateColumn("APPLYDATE", "MM")+" in (1,2,3))";
			} else if (quarter != null && quarter == 2) {
				sql += " AND "+DataBaseSqlConfig.getDateColumn("APPLYDATE", "MM")+" in (4,5,6))";
			} else if (quarter != null && quarter == 3) {
				sql += " AND "+DataBaseSqlConfig.getDateColumn("APPLYDATE", "MM")+" in (7,8,9))";
			} else if (quarter != null && quarter == 4) {
				sql += " AND "+DataBaseSqlConfig.getDateColumn("APPLYDATE", "MM")+" in (10,11,12))";
			}else{
				sql += ")";
			}
	     	sql+= " group by o.orgname";		 
		return sql;
	}
	
	public String getPlannedProject(String orgid, Integer year,Integer quarter) throws Exception {
		String sql="select o.orgname name ,count(0) value from (select  projectid,max(NODEPLANPAYDATE) d  from TBL_CONTRACT_PLANNODE where projectid in("
                 +" SELECT contractid FROM TBL_CYHW_UNIT WHERE RECORDTYPE IN ('HTGL002', 'HTGL005')  AND ORGID ="+orgid
                +" AND "+DataBaseSqlConfig.getDateColumn("CREATETIME", "YYYY")+" = "+year+"  and CONTRACTID  in (select distinct contractid  from tbl_cyhw_unit  where ( contractid in (SELECT RECORDPARENT FROM TBL_CYHW_PROJECTBUDGET WHERE RECORDTYPE = 'HTGL003' AND INSPECTIONSTATUS = 6  AND ORGID =  "+orgid+")  or  contractid in (select contractid  from tbl_cyhw_unit where CONTRACTSTATUS in (6,7,8)  AND ORGID = "+orgid+" ))  AND CONTRACTID NOT IN ( SELECT RECORDPARENT FROM TBL_CYHW_UNIT WHERE RECORDTYPE = 'HTGL005' AND ORGID =  "+orgid+" AND RECORDPARENT IS NOT NULL ) ) )"
                + " group by projectid ) ii "
                + " left join tbl_cyhw_unit u on u.contractid=ii.projectid left join tbl_organization o "
                + " on o.orgid=u.CONTRACTDEPT where   o.orgname is not null and o.orgtype=0";
		if (quarter != null && quarter == 1) { // 第一季度
			sql += " AND "+DataBaseSqlConfig.getDateColumn("ii.d", "MM")+" in (1,2,3))";
		} else if (quarter != null && quarter == 2) {
			sql += " AND "+DataBaseSqlConfig.getDateColumn("ii.d", "MM")+" in (4,5,6))";
		} else if (quarter != null && quarter == 3) {
			sql += " AND "+DataBaseSqlConfig.getDateColumn("ii.d", "MM")+" in (7,8,9))";
		} else if (quarter != null && quarter == 4) {
			sql += " AND "+DataBaseSqlConfig.getDateColumn("ii.d", "MM")+" in (10,11,12))";
		} 
         sql+="group by o.orgname";
		 return sql;
	}
	
	public String getFactualProject(String orgid, Integer year,Integer quarter) throws Exception {
		String sql="select o.orgname name ,count(0) value from (select  projectid,max(NODEPLANPAYDATE) d  from TBL_CONTRACT_PLANNODE where projectid in("
                +" SELECT contractid FROM TBL_CYHW_UNIT WHERE RECORDTYPE IN ('HTGL002', 'HTGL005')  AND ORGID ="+orgid
               +" AND "+DataBaseSqlConfig.getDateColumn("CREATETIME", "YYYY")+" = "+year+" and  CONTRACTID  in (select distinct contractid  from tbl_cyhw_unit  where ( contractid in (SELECT RECORDPARENT FROM TBL_CYHW_PROJECTBUDGET WHERE RECORDTYPE = 'HTGL003' AND INSPECTIONSTATUS = 6  AND ORGID =  "+orgid+")  or  contractid in (select contractid  from tbl_cyhw_unit where CONTRACTSTATUS in (6,7,8)  AND ORGID = "+orgid+" ))  AND CONTRACTID NOT IN ( SELECT RECORDPARENT FROM TBL_CYHW_UNIT WHERE RECORDTYPE = 'HTGL005' AND ORGID =  "+orgid+" AND RECORDPARENT IS NOT NULL ) ))"
               + " and PLANNODESTATUS = 2 group by projectid ) ii "
               + " left join tbl_cyhw_unit u on u.contractid=ii.projectid left join tbl_organization o "
               + " on o.orgid=u.CONTRACTDEPT where   o.orgname is not null and o.orgtype=0";
		if (quarter != null && quarter == 1) { // 第一季度
			sql += " AND "+DataBaseSqlConfig.getDateColumn("ii.d", "MM")+" in (1,2,3))";
		} else if (quarter != null && quarter == 2) {
			sql += " AND "+DataBaseSqlConfig.getDateColumn("ii.d", "MM")+" in (4,5,6))";
		} else if (quarter != null && quarter == 3) {
			sql += " AND "+DataBaseSqlConfig.getDateColumn("ii.d", "MM")+" in (7,8,9))";
		} else if (quarter != null && quarter == 4) {
			sql += " AND "+DataBaseSqlConfig.getDateColumn("ii.d", "MM")+" in (10,11,12))";
		} 
        sql+="group by o.orgname";
		 return sql;
	}
	
	public String contractLegalAprStat(String orgid, Integer year,Integer quarter) throws Exception {
	     String sql="SELECT o.orgname name,count(*) cnt,NVL(SUM(CONTRACTMONEY),0) value  FROM TBL_CYHW_UNIT tcu "
	     		+ " left join tbl_organization o on o.orgid=tcu.contractdept"
              + " WHERE o.FATHERORGID="+orgid+" "
              + " AND RECORDTYPE='HTGL002' "
              + " AND o.orgname is not null "
              + " AND tcu.CONTRACTSTATUS in (1,2,6,7,8,9,10,11,12) "
              + " AND "+DataBaseSqlConfig.getDateColumn("CREATETIME", "YYYY")+" = "+year+" ";
	     if (quarter != null && quarter == 1) { // 第一季度
				sql += " AND "+DataBaseSqlConfig.getDateColumn("CREATETIME", "MM")+" in (1,2,3))";
			} else if (quarter != null && quarter == 2) {
				sql += " AND "+DataBaseSqlConfig.getDateColumn("CREATETIME", "MM")+" in (4,5,6))";
			} else if (quarter != null && quarter == 3) {
				sql += " AND "+DataBaseSqlConfig.getDateColumn("CREATETIME", "MM")+" in (7,8,9))";
			} else if (quarter != null && quarter == 4) {
				sql += " AND "+DataBaseSqlConfig.getDateColumn("CREATETIME", "MM")+" in (10,11,12))";
			} 
      sql+= " group by o.orgname";
		return sql;
	}
	
	public String getList(String staffid, String startDate,String orgid) throws Exception {
		StringBuffer sqlSb = new StringBuffer("SELECT  DISTINCT TCU.contractname, TCU.contractno,"+DataBaseSqlConfig.getNullColumn("TCU.contractmoney", "0")+" CONTRACTMONEY,  TCU.contracttype, s.realname, s.USERNAME," +
                        " CASE  WHEN DCTYPE = '付款' THEN  ( SELECT SUM(Paymenmoney ) FROM TBL_CONTRACT_PAYMENT WHERE contractid=TCU.CONTRACTID) " +
                        " END AS paymoney,TCU.contractid   FROM TBL_CYHW_UNIT TCU left join tbl_staff s on s.staffid=TCU.CREATEUSER" +
                        "  LEFT JOIN TBL_CYHW_PROJECTBUDGET TCPB ON TCU.CONTRACTXDFXINFO = TCPB.BUDGETID " +
                        " AND TCPB.RECORDTYPE = 'HTGL001'  WHERE (TCU.CONTRACTID IN ( SELECT RECORDPARENT   FROM  TBL_CYHW_PROJECTBUDGET  WHERE RECORDTYPE = 'HTGL003'  AND INSPECTIONSTATUS = 6  " +
                        "  AND TCU.CONTRACTID NOT IN (SELECT RECORDPARENT FROM TBL_CYHW_UNIT WHERE RECORDTYPE = 'HTGL005' AND RECORDPARENT IS NOT NULL) AND TCU.RECORDTYPE IN ('HTGL002','HTGL005')" +
                        "    ) or contractstatus in ('7','6','17'))");
        if (staffid != null && !"".equals(staffid)) {
            sqlSb.append("  AND ( TCU.CREATEUSER = '" + staffid + "' OR TCU.JBSTAFF = '" + staffid + "' OR TCU.CONTRACTSTAFF = '" + staffid + "' or  TCU.CONTRACTDEPT="+orgid+" )  ");
        }
        String sql = sqlSb.toString();
		return sql;
	}
}
 


