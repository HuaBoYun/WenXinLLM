package com.huabo.contract.mappersql;

import java.math.BigDecimal;
import java.util.Date;

import com.hbfk.util.PageInfo;
import org.apache.commons.lang.StringUtils;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.database.DataBaseSqlConfig;
import com.huabo.contract.entity.TblCyhwProjectbudget;
import com.huabo.contract.entity.TblCyhwUnit;


public class TblCyhwProjectbudgetMapperSqlConifg {
	
	public String selectBlackList(BigDecimal orgid, TblStaffUtil staff, boolean taskCount, TblCyhwProjectbudget tcb) throws Exception{
		StringBuffer sqlSb = new StringBuffer("SELECT COUNTERPARTNO,BUDGETNAME,PROJECTSTAGEGOAL,SERVICETYPE,TOTALTMONEY,CASE TORA.OBRTYPE WHEN 1 THEN '短期' WHEN 2 THEN '长期' ELSE '未加入' END AS BLACKTYPE,TORA.BLACKDEADTIME AS EFFECTDATE")
				.append("  from TBL_CYHW_PROJECTBUDGET TCP LEFT JOIN ( SELECT TOR.* FROM TBL_OPPBLACK_RECORD TOR INNER JOIN (SELECT MAX(VERSION) AS MAVER ,OPPOID FROM TBL_OPPBLACK_RECORD GROUP BY OPPOID) TORV")
				.append(" ON TOR.VERSION = TORV.MAVER AND TOR.OPPOID = TORV.OPPOID LEFT JOIN TBL_OPPBLACK_REMOVE TORM ON TOR.BRID = TORM.BRID WHERE TOR.BRID NOT IN (SELECT BRID FROM TBL_OPPBLACK_REMOVE WHERE RMSTATUS = 6 AND BRID IS NOT NULL AND BRID != '') ")
				.append(" ) TORA ON TCP.BUDGETID = TORA.OPPOID LEFT JOIN TBL_OPPBLACK_REMOVE TOBR ON TORA.BRID = TOBR.BRID WHERE TCP.RECORDTYPE = 'HTGL001' AND ((TCP.ISBLACK = 1 AND TORA.OBRTYPE = 2 ) OR (TCP.ISBLACK = 1 AND TORA.OBRTYPE = 1 AND ")
				.append(DataBaseSqlConfig.getDateStrFormat(new Date())).append(" <= TORA.BLACKDEADTIME ))");
		/*if(!taskCount) {
			sqlSb.append(" AND  (BUDGETID in (select FROMID from TBL_MY_TASK where USRID="+staff.getStaffid()+") or CREATEUSER="+staff.getStaffid()+" ) ");
		}*/
		
		if (tcb.getBudgetname() != null && !"".equals(tcb.getBudgetname())) {
			sqlSb.append("  AND TCP.BUDGETNAME LIKE '%" + tcb.getBudgetname()+ "%'");
		}
	
		if (tcb.getRecordtype() != null && !"".equals(tcb.getRecordtype())) {
			sqlSb.append(" AND TCP.RECORDTYPE = '" + tcb.getRecordtype() + "'");
		}
	
		if (tcb.getInspectionstatus() != null) {
			sqlSb.append(" AND TCP.INSPECTIONSTATUS = '" + tcb.getInspectionstatus() + "'");
		}
		if (tcb.getCounterpartno() != null && !"".equals(tcb.getCounterpartno())) {
			sqlSb.append(" AND TCP.COUNTERPARTNO LIKE '%" + tcb.getCounterpartno().trim() + "%'");
		}
	
		if (tcb.getProjectstagegoal() != null && !"".equals(tcb.getProjectstagegoal())) {
			sqlSb.append(" AND TCP.PROJECTSTAGEGOAL LIKE '%" + tcb.getProjectstagegoal().trim() + "%'");
		}
	
		if (tcb.getCounterpartaddress() != null && !"".equals(tcb.getCounterpartaddress())) {
			sqlSb.append(" AND TCP.COUNTERPARTADDRESS LIKE '%" + tcb.getCounterpartaddress().trim() + "%'");
		}
		
		if(tcb.getBlackAprStatus() != null) {
			sqlSb.append(" AND TCP.BLACKAPRSTATUS = " + tcb.getBlackAprStatus());
		}
	
		if (tcb.getBlacktype() != null && tcb.getBlacktype() == 3) {
			sqlSb.append(" AND TCP.BLACKTYPE IS NOT NULL");
		}
	
		sqlSb.append(" ORDER BY TCP.BUDGETID DESC ");
		String sql = sqlSb.toString();
		return sql;
		
	}
	
	public String selectOppsiteNamesByUnitTaiZhang(String idStrs) throws Exception{
		StringBuffer sqlSb = new StringBuffer("SELECT TCB.CONTRACTID,TCP.BUDGETID,TCP.BUDGETNAME FROM TBL_CONTRACT_BUDGET TCB LEFT JOIN TBL_CYHW_PROJECTBUDGET TCP ON TCB.BUDGETID = "+DataBaseSqlConfig.toCharColumn("TCP.BUDGETID")
				+ " WHERE TCB.CONTRACTID IN ("+idStrs+") ORDER BY TCB.CONTRACTID DESC ");
        String sql = sqlSb.toString();
        return sql;
	}
	
	public String selectOppsiteNamesByUnitTaiZhangExport(TblCyhwUnit unit, String fatherOrgIds) throws Exception{
		StringBuffer sqlSb = new StringBuffer("SELECT TCB.CONTRACTID,TCP.BUDGETID,TCP.BUDGETNAME FROM TBL_CONTRACT_BUDGET TCB LEFT JOIN TBL_CYHW_PROJECTBUDGET TCP ON TCB.BUDGETID = "+DataBaseSqlConfig.toCharColumn("TCP.BUDGETID")
				+ " WHERE TCB.CONTRACTID IN ( SELECT TCU.CONTRACTID FROM TBL_CYHW_UNIT TCU LEFT JOIN TBL_CONTRACT_PROJECT TCPJ ON TCU.TOPICID = TCPJ.PROJECTID  "
        		+ " WHERE TCU.RECORDTYPE IN ('HTGL002','HTGL005') ");
        		
        if(StringUtils.isNotBlank(fatherOrgIds)) {
        	sqlSb.append(" AND TCU.ORGID IN ("+fatherOrgIds+")");
        }else {
        	sqlSb.append(" AND TCU.ORGID = "+unit.getOrgid());
        }
        		
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
            sqlSb.append(" AND TCU.CONTRACTTYPE LIKE '"+unit.getContracttype()+"'");
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
        sqlSb.append(") ORDER BY TCB.CONTRACTID DESC ");
        String sql = sqlSb.toString();
        return sql;
	}
	
	public String selectListByUnitExport(TblCyhwUnit unit, String allCompanyIds) throws Exception{
		String sql = "SELECT TCB.CONTRACTID,TCP.BUDGETID,TCP.BUDGETNAME FROM TBL_CONTRACT_BUDGET TCB LEFT JOIN TBL_CYHW_PROJECTBUDGET TCP ON TCB.BUDGETID = "+DataBaseSqlConfig.toCharColumn("TCP.BUDGETID")
				+ " WHERE TCB.CONTRACTID IN (SELECT TCU.CONTRACTID FROM TBL_CYHW_UNIT TCU ";
        sql += " WHERE TCU.RECORDTYPE IN ('HTGL002','HTGL005') ";
        
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
        
        sql += ") ORDER BY TCB.CONTRACTID ASC";
        return sql.toString();
	}
	
	
	public String findAutoNumber(String counterpartno, BigDecimal orgid) throws Exception {
		String sql = "SELECT "+DataBaseSqlConfig.getMaxNoDeal("COUNTERPARTNO", "-", "1")+" FROM TBL_CYHW_PROJECTBUDGET WHERE ORGID = '"+orgid+"' AND COUNTERPARTNO LIKE '"+counterpartno+"' AND RECORDTYPE = 'HTGL001'";
		return sql;
	}
	
	
	public String insertOppositePartyOld(TblCyhwProjectbudget tcpb) throws Exception{
		StringBuffer column = new StringBuffer("INSERT INTO TBL_CYHW_PROJECTBUDGET (BUDGETID");
		StringBuffer value = new StringBuffer(" VALUES ("+tcpb.getBudgetid());
		
		if(tcpb.getBudgetname() != null) {
			column.append(",BUDGETNAME");
			value.append(",'"+tcpb.getBudgetname()+"'");
		}
		if(tcpb.getTotaltmoney() != null) {
			column.append(",TOTALTMONEY");
			value.append(",'"+tcpb.getTotaltmoney()+"'");
		}
		if(tcpb.getFinancemoney() != null) {
			column.append(",FINANCEMONEY");
			value.append(",'"+tcpb.getFinancemoney()+"'");
		}
		if(tcpb.getOthermoney() != null) {
			column.append(",OTHERMONEY");
			value.append(",'"+tcpb.getOthermoney()+"'");
		}
		if(tcpb.getGoodstype() != null) {
			column.append(",GOODSTYPE");
			value.append(",'"+tcpb.getGoodstype()+"'");
		}
		if(tcpb.getServicetype() != null) {
			column.append(",SERVICETYPE");
			value.append(",'"+tcpb.getServicetype()+"'");
		}
		if(tcpb.getProjecttype() != null) {
			column.append(",PROJECTTYPE");
			value.append(",'"+tcpb.getProjecttype()+"'");
		}
		if(tcpb.getOrthertype() != null) {
			column.append(",ORTHERTYPE");
			value.append(",'"+tcpb.getOrthertype()+"'");
		}
		if(tcpb.getIsgovernment() != null) {
			column.append(",ISGOVERNMENT");
			value.append(",'"+tcpb.getIsgovernment()+"'");
		}
		if(tcpb.getIsdirect() != null) {
			column.append(",ISDIRECT");
			value.append(",'"+tcpb.getIsdirect()+"'");
		}
		if(tcpb.getItembudgettype() != null) {
			column.append(",ITEMBUDGETTYPE");
			value.append(",'"+tcpb.getItembudgettype()+"'");
		}
		if(tcpb.getProjecteason() != null) {
			column.append(",PROJECTEASON");
			value.append(",'"+tcpb.getProjecteason()+"'");
		}
		if(tcpb.getProjectgoal() != null) {
			column.append(",PROJECTGOAL");
			value.append(",'"+tcpb.getProjectgoal()+"'");
		}
		if(tcpb.getProjectstagegoal() != null) {
			column.append(",PROJECTSTAGEGOAL");
			value.append(",'"+tcpb.getProjectstagegoal()+"'");
		}
		if(tcpb.getProjectcondition() != null) {
			column.append(",PROJECTCONDITION");
			value.append(",'"+tcpb.getProjectcondition()+"'");
		}
		if(tcpb.getProjectrisk() != null) {
			column.append(",PROJECTRISK");
			value.append(",'"+tcpb.getProjectrisk()+"'");
		}
		if(tcpb.getOrgid() != null) {
			column.append(",ORGID");
			value.append(",'"+tcpb.getOrgid()+"'");
		}
		if(tcpb.getCreateuser() != null) {
			column.append(",CREATEUSER");
			value.append(",'"+tcpb.getCreateuser()+"'");
		}
		if(tcpb.getCreatetime() != null) {
			column.append(",CREATETIME");
			value.append(","+DataBaseSqlConfig.getDateStrFormat(tcpb.getCreatetime()));
		}
		if(tcpb.getFlowid() != null) {
			column.append(",FLOWID");
			value.append(",'"+tcpb.getFlowid()+"'");
		}
		if(tcpb.getLinkdepr() != null) {
			column.append(",LINKDEPR");
			value.append(",'"+tcpb.getLinkdepr()+"'");
		}
		if(tcpb.getReporttodept() != null) {
			column.append(",REPORTTODEPT");
			value.append(",'"+tcpb.getReporttodept()+"'");
		}
		if(tcpb.getInspectionstatus() != null) {
			column.append(",INSPECTIONSTATUS");
			value.append(",'"+tcpb.getInspectionstatus()+"'");
		}
		if(tcpb.getGoodsprice() != null) {
			column.append(",GOODSPRICE");
			value.append(",'"+tcpb.getGoodsprice()+"'");
		}
		if(tcpb.getGoodsamount() != null) {
			column.append(",GOODSAMOUNT");
			value.append(",'"+tcpb.getGoodsamount()+"'");
		}
		if(tcpb.getGoodsmodel() != null) {
			column.append(",GOODSMODEL");
			value.append(",'"+tcpb.getGoodsmodel()+"'");
		}
		if(tcpb.getCounterpartno() != null) {
			column.append(",COUNTERPARTNO");
			value.append(",'"+tcpb.getCounterpartno()+"'");
		}
		if(tcpb.getCounterpartaddress() != null) {
			column.append(",COUNTERPARTADDRESS");
			value.append(",'"+tcpb.getCounterpartaddress()+"'");
		}
		if(tcpb.getCounterpartcode() != null) {
			column.append(",COUNTERPARTCODE");
			value.append(",'"+tcpb.getCounterpartcode()+"'");
		}
		if(tcpb.getCounterparthank() != null) {
			column.append(",COUNTERPARTHANK");
			value.append(",'"+tcpb.getCounterparthank()+"'");
		}
		if(tcpb.getCounterparthankaccount() != null) {
			column.append(",COUNTERPARTHANKACCOUNT");
			value.append(",'"+tcpb.getCounterparthankaccount()+"'");
		}
		if(tcpb.getCounterpartnetaddress() != null) {
			column.append(",COUNTERPARTNETADDRESS");
			value.append(",'"+tcpb.getCounterpartnetaddress()+"'");
		}
		if(tcpb.getCounterpartphone() != null) {
			column.append(",COUNTERPARTPHONE");
			value.append(",'"+tcpb.getCounterpartphone()+"'");
		}
		if(tcpb.getRecordtype() != null) {
			column.append(",RECORDTYPE");
			value.append(",'"+tcpb.getRecordtype()+"'");
		}
		if(tcpb.getRecordparent() != null) {
			column.append(",RECORDPARENT");
			value.append(",'"+tcpb.getRecordparent()+"'");
		}
		if(tcpb.getRecordconcat() != null) {
			column.append(",RECORDCONCAT");
			value.append(",'"+tcpb.getRecordconcat()+"'");
		}
		if(tcpb.getAction() != null) {
			column.append(",ACTION");
			value.append(",'"+tcpb.getAction()+"'");
		}
		if(tcpb.getEqbflowid() != null) {
			column.append(",EQBFLOWID");
			value.append(",'"+tcpb.getEqbflowid()+"'");
		}
		if(tcpb.getBizno() != null) {
			column.append(",BIZNO");
			value.append(",'"+tcpb.getBizno()+"'");
		}
		if(tcpb.getResultdescription() != null) {
			column.append(",RESULTDESCRIPTION");
			value.append(",'"+tcpb.getResultdescription()+"'");
		}
		if(tcpb.getEqbstatus() != null) {
			column.append(",EQBSTATUS");
			value.append(",'"+tcpb.getEqbstatus()+"'");
		}
		if(tcpb.getFlowtype() != null) {
			column.append(",FLOWTYPE");
			value.append(",'"+tcpb.getFlowtype()+"'");
		}
		if(tcpb.getEabfileck() != null) {
			column.append(",EABFILECK");
			value.append(",'"+tcpb.getEabfileck()+"'");
		}
		if(tcpb.getFileurl() != null) {
			column.append(",FILEURL");
			value.append(",'"+tcpb.getFileurl()+"'");
		}
		if(tcpb.getOppositenature() != null) {
			column.append(",OPPOSITENATURE");
			value.append(",'"+tcpb.getOppositenature()+"'");
		}
		if(tcpb.getCounterparttype() != null) {
			column.append(",COUNTERPARTTYPE");
			value.append(",'"+tcpb.getCounterparttype()+"'");
		}
		if(tcpb.getIseffect() != null) {
			column.append(",ISEFFECT");
			value.append(",'"+tcpb.getIseffect()+"'");
		}
		if(tcpb.getCounterpartdesc() != null) {
			column.append(",COUNTERPARTDESC");
			value.append(",'"+tcpb.getCounterpartdesc()+"'");
		}
		if(tcpb.getSafeorg() != null) {
			column.append(",SAFEORG");
			value.append(",'"+tcpb.getSafeorg()+"'");
		}
		if(tcpb.getSafestaff() != null) {
			column.append(",SAFESTAFF");
			value.append(",'"+tcpb.getSafestaff()+"'");
		}
		if(tcpb.getEffectdate() != null) {
			column.append(",EFFECTDATE");
			value.append(","+DataBaseSqlConfig.getDateStrFormat(tcpb.getEffectdate()));
		}
		if(tcpb.getSafedate() != null) {
			column.append(",SAFEDATE");
			value.append(","+DataBaseSqlConfig.getDateStrFormat(tcpb.getSafedate()));
		}
		if(tcpb.getDirector() != null) {
			column.append(",DIRECTOR");
			value.append(",'"+tcpb.getDirector()+"'");
		}
		if(tcpb.getContacts() != null) {
			column.append(",CONTACTS");
			value.append(",'"+tcpb.getContacts()+"'");
		}
		if(tcpb.getContactsphone() != null) {
			column.append(",CONTACTSPHONE");
			value.append(",'"+tcpb.getContactsphone()+"'");
		}
		if(tcpb.getContactsadress() != null) {
			column.append(",CONTACTSADRESS");
			value.append(",'"+tcpb.getContactsadress()+"'");
		}
		if(tcpb.getContactsemail() != null) {
			column.append(",CONTACTSEMAIL");
			value.append(",'"+tcpb.getContactsemail()+"'");
		}
		if(tcpb.getStation() != null) {
			column.append(",STATION");
			value.append(",'"+tcpb.getStation()+"'");
		}
		if(tcpb.getCallname() != null) {
			column.append(",CALLNAME");
			value.append(",'"+tcpb.getCallname()+"'");
		}
		if(tcpb.getRemarks() != null) {
			column.append(",REMARKS");
			value.append(",'"+tcpb.getRemarks()+"'");
		}
		if(tcpb.getIsblack() != null) {
			column.append(",ISBLACK");
			value.append(",'"+tcpb.getIsblack()+"'");
		}
		if(tcpb.getBlacktype() != null) {
			column.append(",BLACKTYPE");
			value.append(",'"+tcpb.getBlacktype()+"'");
		}
		if(tcpb.getPstartdate() != null) {
			column.append(",PSTARTDATE");
			value.append(","+DataBaseSqlConfig.getDateStrFormat(tcpb.getPstartdate()));
		}
		if(tcpb.getPenddate() != null) {
			column.append(",PENDDATE");
			value.append(","+DataBaseSqlConfig.getDateStrFormat(tcpb.getPenddate()));
		}
		if(tcpb.getCretificateno() != null) {
			column.append(",CRETIFICATENO");
			value.append(",'"+tcpb.getCretificateno()+"'");
		}
		if(tcpb.getOutsideId() != null) {
			column.append(",OUTSIDEID");
			value.append(",'"+tcpb.getOutsideId()+"'");
		}
		column.append(")");
		value.append(")");
		String sql = column.toString()+value.toString();
		return sql;
	}
	
	public String insertOppositeParty(TblCyhwProjectbudget tcpb,BigDecimal sealorgid) throws Exception{
		StringBuffer column = new StringBuffer("INSERT INTO TBL_CYHW_PROJECTBUDGET (BUDGETID");
		StringBuffer value = new StringBuffer(" VALUES ("+tcpb.getBudgetid());
		if(sealorgid != null) {
			column.append(",SEALORGID");
			value.append(",'"+sealorgid+"'");
		}
		if(tcpb.getBudgetname() != null) {
			column.append(",BUDGETNAME");
			value.append(",'"+tcpb.getBudgetname()+"'");
		}
		if(tcpb.getTotaltmoney() != null) {
			column.append(",TOTALTMONEY");
			value.append(",'"+tcpb.getTotaltmoney()+"'");
		}
		if(tcpb.getFinancemoney() != null) {
			column.append(",FINANCEMONEY");
			value.append(",'"+tcpb.getFinancemoney()+"'");
		}
		if(tcpb.getOthermoney() != null) {
			column.append(",OTHERMONEY");
			value.append(",'"+tcpb.getOthermoney()+"'");
		}
		if(tcpb.getGoodstype() != null) {
			column.append(",GOODSTYPE");
			value.append(",'"+tcpb.getGoodstype()+"'");
		}
		if(tcpb.getServicetype() != null) {
			column.append(",SERVICETYPE");
			value.append(",'"+tcpb.getServicetype()+"'");
		}
		if(tcpb.getProjecttype() != null) {
			column.append(",PROJECTTYPE");
			value.append(",'"+tcpb.getProjecttype()+"'");
		}
		if(tcpb.getOrthertype() != null) {
			column.append(",ORTHERTYPE");
			value.append(",'"+tcpb.getOrthertype()+"'");
		}
		if(tcpb.getIsgovernment() != null) {
			column.append(",ISGOVERNMENT");
			value.append(",'"+tcpb.getIsgovernment()+"'");
		}
		if(tcpb.getIsdirect() != null) {
			column.append(",ISDIRECT");
			value.append(",'"+tcpb.getIsdirect()+"'");
		}
		if(tcpb.getItembudgettype() != null) {
			column.append(",ITEMBUDGETTYPE");
			value.append(",'"+tcpb.getItembudgettype()+"'");
		}
		if(tcpb.getProjecteason() != null) {
			column.append(",PROJECTEASON");
			value.append(",'"+tcpb.getProjecteason()+"'");
		}
		if(tcpb.getProjectgoal() != null) {
			column.append(",PROJECTGOAL");
			value.append(",'"+tcpb.getProjectgoal()+"'");
		}
		if(tcpb.getProjectstagegoal() != null) {
			column.append(",PROJECTSTAGEGOAL");
			value.append(",'"+tcpb.getProjectstagegoal()+"'");
		}
		if(tcpb.getProjectcondition() != null) {
			column.append(",PROJECTCONDITION");
			value.append(",'"+tcpb.getProjectcondition()+"'");
		}
		if(tcpb.getProjectrisk() != null) {
			column.append(",PROJECTRISK");
			value.append(",'"+tcpb.getProjectrisk()+"'");
		}
		if(tcpb.getOrgid() != null) {
			column.append(",ORGID");
			value.append(",'"+tcpb.getOrgid()+"'");
		}
		if(tcpb.getCreateuser() != null) {
			column.append(",CREATEUSER");
			value.append(",'"+tcpb.getCreateuser()+"'");
		}
		if(tcpb.getCreatetime() != null) {
			column.append(",CREATETIME");
			value.append(","+DataBaseSqlConfig.getDateStrFormat(tcpb.getCreatetime()));
		}
		if(tcpb.getFlowid() != null) {
			column.append(",FLOWID");
			value.append(",'"+tcpb.getFlowid()+"'");
		}
		if(tcpb.getLinkdepr() != null) {
			column.append(",LINKDEPR");
			value.append(",'"+tcpb.getLinkdepr()+"'");
		}
		if(tcpb.getReporttodept() != null) {
			column.append(",REPORTTODEPT");
			value.append(",'"+tcpb.getReporttodept()+"'");
		}
		if(tcpb.getInspectionstatus() != null) {
			column.append(",INSPECTIONSTATUS");
			value.append(",'"+tcpb.getInspectionstatus()+"'");
		}
		if(tcpb.getGoodsprice() != null) {
			column.append(",GOODSPRICE");
			value.append(",'"+tcpb.getGoodsprice()+"'");
		}
		if(tcpb.getGoodsamount() != null) {
			column.append(",GOODSAMOUNT");
			value.append(",'"+tcpb.getGoodsamount()+"'");
		}
		if(tcpb.getGoodsmodel() != null) {
			column.append(",GOODSMODEL");
			value.append(",'"+tcpb.getGoodsmodel()+"'");
		}
		if(tcpb.getCounterpartno() != null) {
			column.append(",COUNTERPARTNO");
			value.append(",'"+tcpb.getCounterpartno()+"'");
		}
		if(tcpb.getCounterpartaddress() != null) {
			column.append(",COUNTERPARTADDRESS");
			value.append(",'"+tcpb.getCounterpartaddress()+"'");
		}
		if(tcpb.getCounterpartcode() != null) {
			column.append(",COUNTERPARTCODE");
			value.append(",'"+tcpb.getCounterpartcode()+"'");
		}
		if(tcpb.getCounterparthank() != null) {
			column.append(",COUNTERPARTHANK");
			value.append(",'"+tcpb.getCounterparthank()+"'");
		}
		if(tcpb.getCounterparthankaccount() != null) {
			column.append(",COUNTERPARTHANKACCOUNT");
			value.append(",'"+tcpb.getCounterparthankaccount()+"'");
		}
		if(tcpb.getCounterpartnetaddress() != null) {
			column.append(",COUNTERPARTNETADDRESS");
			value.append(",'"+tcpb.getCounterpartnetaddress()+"'");
		}
		if(tcpb.getCounterpartphone() != null) {
			column.append(",COUNTERPARTPHONE");
			value.append(",'"+tcpb.getCounterpartphone()+"'");
		}
		if(tcpb.getRecordtype() != null) {
			column.append(",RECORDTYPE");
			value.append(",'"+tcpb.getRecordtype()+"'");
		}
		if(tcpb.getRecordparent() != null) {
			column.append(",RECORDPARENT");
			value.append(",'"+tcpb.getRecordparent()+"'");
		}
		if(tcpb.getRecordconcat() != null) {
			column.append(",RECORDCONCAT");
			value.append(",'"+tcpb.getRecordconcat()+"'");
		}
		if(tcpb.getAction() != null) {
			column.append(",ACTION");
			value.append(",'"+tcpb.getAction()+"'");
		}
		if(tcpb.getEqbflowid() != null) {
			column.append(",EQBFLOWID");
			value.append(",'"+tcpb.getEqbflowid()+"'");
		}
		if(tcpb.getBizno() != null) {
			column.append(",BIZNO");
			value.append(",'"+tcpb.getBizno()+"'");
		}
		if(tcpb.getResultdescription() != null) {
			column.append(",RESULTDESCRIPTION");
			value.append(",'"+tcpb.getResultdescription()+"'");
		}
		if(tcpb.getEqbstatus() != null) {
			column.append(",EQBSTATUS");
			value.append(",'"+tcpb.getEqbstatus()+"'");
		}
		if(tcpb.getFlowtype() != null) {
			column.append(",FLOWTYPE");
			value.append(",'"+tcpb.getFlowtype()+"'");
		}
		if(tcpb.getEabfileck() != null) {
			column.append(",EABFILECK");
			value.append(",'"+tcpb.getEabfileck()+"'");
		}
		if(tcpb.getFileurl() != null) {
			column.append(",FILEURL");
			value.append(",'"+tcpb.getFileurl()+"'");
		}
		if(tcpb.getOppositenature() != null) {
			column.append(",OPPOSITENATURE");
			value.append(",'"+tcpb.getOppositenature()+"'");
		}
		if(tcpb.getCounterparttype() != null) {
			column.append(",COUNTERPARTTYPE");
			value.append(",'"+tcpb.getCounterparttype()+"'");
		}
		if(tcpb.getIseffect() != null) {
			column.append(",ISEFFECT");
			value.append(",'"+tcpb.getIseffect()+"'");
		}
		if(tcpb.getCounterpartdesc() != null) {
			column.append(",COUNTERPARTDESC");
			value.append(",'"+tcpb.getCounterpartdesc()+"'");
		}
		if(tcpb.getSafeorg() != null) {
			column.append(",SAFEORG");
			value.append(",'"+tcpb.getSafeorg()+"'");
		}
		if(tcpb.getSafestaff() != null) {
			column.append(",SAFESTAFF");
			value.append(",'"+tcpb.getSafestaff()+"'");
		}
		if(tcpb.getEffectdate() != null) {
			column.append(",EFFECTDATE");
			value.append(","+DataBaseSqlConfig.getDateStrFormat(tcpb.getEffectdate()));
		}
		if(tcpb.getSafedate() != null) {
			column.append(",SAFEDATE");
			value.append(","+DataBaseSqlConfig.getDateStrFormat(tcpb.getSafedate()));
		}
		if(tcpb.getDirector() != null) {
			column.append(",DIRECTOR");
			value.append(",'"+tcpb.getDirector()+"'");
		}
		if(tcpb.getContacts() != null) {
			column.append(",CONTACTS");
			value.append(",'"+tcpb.getContacts()+"'");
		}
		if(tcpb.getContactsphone() != null) {
			column.append(",CONTACTSPHONE");
			value.append(",'"+tcpb.getContactsphone()+"'");
		}
		if(tcpb.getContactsadress() != null) {
			column.append(",CONTACTSADRESS");
			value.append(",'"+tcpb.getContactsadress()+"'");
		}
		if(tcpb.getContactsemail() != null) {
			column.append(",CONTACTSEMAIL");
			value.append(",'"+tcpb.getContactsemail()+"'");
		}
		if(tcpb.getStation() != null) {
			column.append(",STATION");
			value.append(",'"+tcpb.getStation()+"'");
		}
		if(tcpb.getCallname() != null) {
			column.append(",CALLNAME");
			value.append(",'"+tcpb.getCallname()+"'");
		}
		if(tcpb.getRemarks() != null) {
			column.append(",REMARKS");
			value.append(",'"+tcpb.getRemarks()+"'");
		}
		if(tcpb.getIsblack() != null) {
			column.append(",ISBLACK");
			value.append(",'"+tcpb.getIsblack()+"'");
		}
		if(tcpb.getBlacktype() != null) {
			column.append(",BLACKTYPE");
			value.append(",'"+tcpb.getBlacktype()+"'");
		}
		if(tcpb.getPstartdate() != null) {
			column.append(",PSTARTDATE");
			value.append(","+DataBaseSqlConfig.getDateStrFormat(tcpb.getPstartdate()));
		}
		if(tcpb.getPenddate() != null) {
			column.append(",PENDDATE");
			value.append(","+DataBaseSqlConfig.getDateStrFormat(tcpb.getPenddate()));
		}
		if(tcpb.getCretificateno() != null) {
			column.append(",CRETIFICATENO");
			value.append(",'"+tcpb.getCretificateno()+"'");
		}
		if(tcpb.getOutsideId() != null) {
			column.append(",OUTSIDEID");
			value.append(",'"+tcpb.getOutsideId()+"'");
		}
		column.append(")");
		value.append(")");
		String sql = column.toString()+value.toString();
		return sql;
	}
	
	public String updateOppositeInfoById(TblCyhwProjectbudget tcpb,BigDecimal sealorgid) throws Exception {
		StringBuffer sql = new StringBuffer("UPDATE TBL_CYHW_PROJECTBUDGET SET BUDGETID = '"+tcpb.getBudgetid()+"'");
		if(sealorgid != null) {
			sql.append(" ,SEALORGID = '"+sealorgid+"'");
		}
		if(tcpb.getBudgetname() != null) {
			sql.append(" ,BUDGETNAME = '"+tcpb.getBudgetname()+"'");
		}
		if(tcpb.getTotaltmoney() != null) {
			sql.append(" ,TOTALTMONEY = '"+tcpb.getTotaltmoney()+"'");
		}
		if(tcpb.getFinancemoney() != null) {
			sql.append(" ,FINANCEMONEY = '"+tcpb.getFinancemoney()+"'");
		}
		if(tcpb.getOthermoney() != null) {
			sql.append(" ,OTHERMONEY = '"+tcpb.getOthermoney()+"'");
		}
		if(tcpb.getGoodstype() != null) {
			sql.append(" ,GOODSTYPE = '"+tcpb.getGoodstype()+"'");
		}
		if(tcpb.getServicetype() != null) {
			sql.append(" ,SERVICETYPE = '"+tcpb.getServicetype()+"'");
		}
		if(tcpb.getProjecttype() != null) {
			sql.append(" ,PROJECTTYPE = '"+tcpb.getProjecttype()+"'");
		}
		if(tcpb.getOrthertype() != null) {
			sql.append(" ,ORTHERTYPE = '"+tcpb.getOrthertype()+"'");
		}
		if(tcpb.getIsgovernment() != null) {
			sql.append(" ,ISGOVERNMENT = '"+tcpb.getIsgovernment()+"'");
		}
		if(tcpb.getIsdirect() != null) {
			sql.append(" ,ISDIRECT = '"+tcpb.getIsdirect()+"'");
		}
		if(tcpb.getItembudgettype() != null) {
			sql.append(" ,ITEMBUDGETTYPE = '"+tcpb.getItembudgettype()+"'");
		}
		if(tcpb.getProjecteason() != null) {
			sql.append(" ,PROJECTEASON = '"+tcpb.getProjecteason()+"'");
		}
		if(tcpb.getProjectgoal() != null) {
			sql.append(" ,PROJECTGOAL = '"+tcpb.getProjectgoal()+"'");
		}
		if(tcpb.getProjectstagegoal() != null) {
			sql.append(" ,PROJECTSTAGEGOAL = '"+tcpb.getProjectstagegoal()+"'");
		}
		if(tcpb.getProjectcondition() != null) {
			sql.append(" ,PROJECTCONDITION = '"+tcpb.getProjectcondition()+"'");
		}
		if(tcpb.getProjectrisk() != null) {
			sql.append(" ,PROJECTRISK = '"+tcpb.getProjectrisk()+"'");
		}
		if(tcpb.getCreatetime() != null) {
			sql.append(" ,CREATETIME = "+DataBaseSqlConfig.getDateStrFormat(tcpb.getCreatetime()));
		}
		if(tcpb.getLinkdepr() != null) {
			sql.append(" ,LINKDEPR = '"+tcpb.getLinkdepr()+"'");
		}
		if(tcpb.getReporttodept() != null) {
			sql.append(" ,REPORTTODEPT = '"+tcpb.getReporttodept()+"'");
		}
		if(tcpb.getInspectionstatus() != null) {
			sql.append(" ,INSPECTIONSTATUS = '"+tcpb.getInspectionstatus()+"'");
		}
		if(tcpb.getGoodsprice() != null) {
			sql.append(" ,GOODSPRICE = '"+tcpb.getGoodsprice()+"'");
		}
		if(tcpb.getGoodsamount() != null) {
			sql.append(" ,GOODSAMOUNT = '"+tcpb.getGoodsamount()+"'");
		}
		if(tcpb.getGoodsmodel() != null) {
			sql.append(" ,GOODSMODEL = '"+tcpb.getGoodsmodel()+"'");
		}
		if(tcpb.getCounterpartno() != null) {
			sql.append(" ,COUNTERPARTNO = '"+tcpb.getCounterpartno()+"'");
		}
		if(tcpb.getCounterpartaddress() != null) {
			sql.append(" ,COUNTERPARTADDRESS = '"+tcpb.getCounterpartaddress()+"'");
		}
		if(tcpb.getCounterpartcode() != null) {
			sql.append(" ,COUNTERPARTCODE = '"+tcpb.getCounterpartcode()+"'");
		}
		if(tcpb.getCounterparthank() != null) {
			sql.append(" ,COUNTERPARTHANK = '"+tcpb.getCounterparthank()+"'");
		}
		if(tcpb.getCounterparthankaccount() != null) {
			sql.append(" ,COUNTERPARTHANKACCOUNT = '"+tcpb.getCounterparthankaccount()+"'");
		}
		if(tcpb.getCounterpartnetaddress() != null) {
			sql.append(" ,COUNTERPARTNETADDRESS = '"+tcpb.getCounterpartnetaddress()+"'");
		}
		if(tcpb.getCounterpartphone() != null) {
			sql.append(" ,COUNTERPARTPHONE = '"+tcpb.getCounterpartphone()+"'");
		}
		if(tcpb.getRecordparent() != null) {
			sql.append(" ,RECORDPARENT = '"+tcpb.getRecordparent()+"'");
		}
		if(tcpb.getRecordconcat() != null) {
			sql.append(" ,RECORDCONCAT = '"+tcpb.getRecordconcat()+"'");
		}
		if(tcpb.getAction() != null) {
			sql.append(" ,ACTION = '"+tcpb.getAction()+"'");
		}
		if(tcpb.getEqbflowid() != null) {
			sql.append(" ,EQBFLOWID = '"+tcpb.getEqbflowid()+"'");
		}
		if(tcpb.getBizno() != null) {
			sql.append(" ,BIZNO = '"+tcpb.getBizno()+"'");
		}
		if(tcpb.getResultdescription() != null) {
			sql.append(" ,RESULTDESCRIPTION = '"+tcpb.getResultdescription()+"'");
		}
		if(tcpb.getEqbstatus() != null) {
			sql.append(" ,EQBSTATUS = '"+tcpb.getEqbstatus()+"'");
		}
		if(tcpb.getFlowtype() != null) {
			sql.append(" ,FLOWTYPE = '"+tcpb.getFlowtype()+"'");
		}
		if(tcpb.getEabfileck() != null) {
			sql.append(" ,EABFILECK = '"+tcpb.getEabfileck()+"'");
		}
		if(tcpb.getFileurl() != null) {
			sql.append(" ,FILEURL = '"+tcpb.getFileurl()+"'");
		}
		if(tcpb.getOppositenature() != null) {
			sql.append(" ,OPPOSITENATURE = '"+tcpb.getOppositenature()+"'");
		}
		if(tcpb.getCounterparttype() != null) {
			sql.append(" ,COUNTERPARTTYPE = '"+tcpb.getCounterparttype()+"'");
		}
		if(tcpb.getIseffect() != null) {
			sql.append(" ,ISEFFECT = '"+tcpb.getIseffect()+"'");
		}
		if(tcpb.getCounterpartdesc() != null) {
			sql.append(" ,COUNTERPARTDESC = '"+tcpb.getCounterpartdesc()+"'");
		}
		if(tcpb.getSafeorg() != null) {
			sql.append(" ,SAFEORG = '"+tcpb.getSafeorg()+"'");
		}
		if(tcpb.getSafestaff() != null) {
			sql.append(" ,SAFESTAFF = '"+tcpb.getSafestaff()+"'");
		}
		if(tcpb.getEffectdate() != null) {
			sql.append(" ,EFFECTDATE = "+DataBaseSqlConfig.getDateStrFormat(tcpb.getEffectdate()));
		}
		if(tcpb.getSafedate() != null) {
			sql.append(" ,SAFEDATE = "+DataBaseSqlConfig.getDateStrFormat(tcpb.getSafedate()));
		}
		if(tcpb.getDirector() != null) {
			sql.append(" ,DIRECTOR = '"+tcpb.getDirector()+"'");
		}
		if(tcpb.getContacts() != null) {
			sql.append(" ,CONTACTS = '"+tcpb.getContacts()+"'");
		}
		if(tcpb.getContactsphone() != null) {
			sql.append(" ,CONTACTSPHONE = '"+tcpb.getContactsphone()+"'");
		}
		if(tcpb.getContactsadress() != null) {
			sql.append(" ,CONTACTSADRESS = '"+tcpb.getContactsadress()+"'");
		}
		if(tcpb.getContactsemail() != null) {
			sql.append(" ,CONTACTSEMAIL = '"+tcpb.getContactsemail()+"'");
		}
		if(tcpb.getStation() != null) {
			sql.append(" ,STATION = '"+tcpb.getStation()+"'");
		}
		if(tcpb.getCallname() != null) {
			sql.append(" ,CALLNAME = '"+tcpb.getCallname()+"'");
		}
		if(tcpb.getRemarks() != null) {
			sql.append(" ,REMARKS = '"+tcpb.getRemarks()+"'");
		}
		if(tcpb.getIsblack() != null) {
			sql.append(" ,ISBLACK = '"+tcpb.getIsblack()+"'");
		}
		if(tcpb.getBlacktype() != null) {
			sql.append(" ,BLACKTYPE = '"+tcpb.getBlacktype()+"'");
		}
		if(tcpb.getPstartdate() != null) {
			sql.append(" ,PSTARTDATE = "+DataBaseSqlConfig.getDateStrFormat(tcpb.getPstartdate()));
		}
		if(tcpb.getPenddate() != null) {
			sql.append(" ,PENDDATE = "+DataBaseSqlConfig.getDateStrFormat(tcpb.getPenddate()));
		}
		if(tcpb.getCretificateno() != null) {
			sql.append(" ,CRETIFICATENO = '"+tcpb.getCretificateno()+"'");
		}
		if(tcpb.getOutsideId() != null) {
			sql.append(" ,OUTSIDEID = '"+tcpb.getOutsideId()+"'");
		}
		sql.append(" WHERE BUDGETID = '"+tcpb.getBudgetid()+"'");
		return sql.toString();
	}
	
	public String findListByPageInfo(IPage<TblCyhwProjectbudget> page, String orgId,TblCyhwProjectbudget tcpb) throws Exception{
		StringBuffer sqlSb = new StringBuffer("SELECT TORA.BRID,TORA.OPPOID,TORA.VERSION,TORA.OBRTYPE,TORA.BLACKDEADTIME,TORA.APRSTATUS,TORA.BACKREASON,TORA.CREATETIME TORCT,TORA.CREATESTAFF TORCR " + 
				" ,TCP.* from TBL_CYHW_PROJECTBUDGET TCP LEFT JOIN ( SELECT TOR.* FROM TBL_OPPBLACK_RECORD TOR INNER JOIN (SELECT MAX(VERSION) AS MAVER ,OPPOID FROM TBL_OPPBLACK_RECORD GROUP BY OPPOID) TORV " + 
				" ON TOR.VERSION = TORV.MAVER AND TOR.OPPOID = TORV.OPPOID LEFT JOIN TBL_OPPBLACK_REMOVE TORM ON TOR.BRID = TORM.BRID WHERE TOR.BRID NOT IN (SELECT BRID FROM TBL_OPPBLACK_REMOVE WHERE RMSTATUS = 6 AND BRID IS NOT NULL AND BRID != '') " + 
				" ) TORA ON TCP.BUDGETID = TORA.OPPOID WHERE ((TCP.ISBLACK = 2 OR TCP.ISBLACK = 0 OR TCP.ISBLACK IS NULL OR TCP.ISBLACK = '' ) OR (TCP.ISBLACK = 1 AND TORA.OBRTYPE = 1 AND ")
				.append(DataBaseSqlConfig.getDateStrFormat(new Date())).append(" >= TORA.BLACKDEADTIME )) ");
		
		if(tcpb.getBudgetname()!=null && !"".equals(tcpb.getBudgetname())){
			sqlSb.append("  AND BUDGETNAME LIKE '%"+tcpb.getBudgetname()+"%'");
		}
		if(tcpb.getFlowid()!=null){
			sqlSb.append(" and FLOWID="+tcpb.getFlowid());
		}
		if(tcpb.getRecordtype() != null && !"".equals(tcpb.getRecordtype())){
			sqlSb.append(" AND RECORDTYPE = '"+tcpb.getRecordtype()+"'");
		}
		if(tcpb.getInspectionstatus() != null ){
			sqlSb.append(" AND INSPECTIONSTATUS = '"+tcpb.getInspectionstatus()+"'");

		}
		if(tcpb.getCounterpartno()!=null && !"".equals(tcpb.getCounterpartno())) {
			sqlSb.append(" AND counterpartNo LIKE '%"+tcpb.getCounterpartno().trim()+"%'");

		}
		if(tcpb.getProjectstagegoal()!=null && !"".equals(tcpb.getProjectstagegoal())) {
			sqlSb.append(" AND PROJECTSTAGEGOAL LIKE '%"+tcpb.getProjectstagegoal().trim()+"%'");

		}
		if(tcpb.getOrgid() != null) {
			sqlSb.append(" AND ORGID = '"+tcpb.getOrgid()+"'");
		}
		if(tcpb.getCounterpartaddress()!=null && !"".equals(tcpb.getCounterpartaddress())) {
			sqlSb.append(" AND COUNTERPARTADDRESS LIKE '%"+tcpb.getCounterpartaddress().trim()+"%'");

		}
		sqlSb.append(" ORDER BY BUDGETID DESC");
		String sql = sqlSb.toString();
		return sql;
	}
	
	public String selectListByPageInfo(IPage<TblCyhwProjectbudget> page, TblStaffUtil staff,Integer taskCount,
			TblCyhwProjectbudget budget) {
		 //ORGID = "+staff.getCurrentOrg().getOrgid());
		StringBuffer sqlSb = new StringBuffer("SELECT TORA.BRID,TORA.OPPOID,TORA.VERSION,TORA.OBRTYPE,TORA.BLACKDEADTIME,TORA.APRSTATUS,TORA.BACKREASON,TORA.CREATETIME TORCT,TORA.CREATESTAFF TORCR,TCP.* from TBL_CYHW_PROJECTBUDGET TCP ")
				.append(" LEFT JOIN ( SELECT TOR.* FROM TBL_OPPBLACK_RECORD TOR INNER JOIN (SELECT MAX(VERSION) AS MAVER ,OPPOID FROM TBL_OPPBLACK_RECORD GROUP BY OPPOID) TORV ON TOR.VERSION = TORV.MAVER AND TOR.OPPOID = TORV.OPPOID")
				.append(" WHERE TOR.BRID NOT IN (SELECT BRID FROM TBL_OPPBLACK_REMOVE WHERE RMSTATUS = 6 AND BRID IS NOT NULL AND BRID != '' ) ) TORA ON TCP.BUDGETID = TORA.OPPOID WHERE TCP.RECORDTYPE = 'HTGL001' ") ;
		
		if(budget.getBudgetname() != null && !"".equals(budget.getBudgetname())) {
			sqlSb.append(" AND TCP.BUDGETNAME LIKE '%"+budget.getBudgetname()+"%'");
		}
		if(budget.getRecordtype() != null && !"".equals(budget.getRecordtype())) {
			sqlSb.append(" AND TCP.RECORDTYPE = '"+budget.getRecordtype()+"'");
		}
		if(budget.getInspectionstatus() != null && !"".equals(budget.getInspectionstatus())) {
			sqlSb.append(" AND TCP.INSPECTIONSTATUS = '"+budget.getInspectionstatus()+"'");
		}
		if(budget.getCounterpartno()!=null && !"".equals(budget.getCounterpartno())) {
			sqlSb.append(" AND TCP.COUNTERPARTNO LIKE '%"+budget.getCounterpartno().trim()+"%'");
		}
		if(budget.getProjectstagegoal() != null && !"".equals(budget.getProjectgoal())) {
			sqlSb.append(" AND TCP.PROJECTSTAGEGOAL LIKE '%"+budget.getProjectstagegoal()+"%'");
		}
		if(budget.getCounterpartaddress() != null && !"".equals(budget.getCounterpartaddress())) {
			sqlSb.append(" AND TCP.COUNTERPARTADDRESS LIKE '%"+budget.getCounterpartaddress().trim()+"%'");
		}
		
		/*if(budget.getIsblack() != null) {
			if( budget.getBlacktype()==3) {
				sqlSb.append(" AND ((ISBLACK = 2 OR ISBLACK IS NULL) OR (ISBLACK = 1 AND BLACKTYPE = 1 AND EFFECTDATE <= SYSDATE))");
			}else if(budget.getIsblack() == 2) {
				sqlSb.append(" AND ISBLACK = 2");
			}else if(budget.getIsblack() == 1) {
				sqlSb.append(" AND (ISBLACK = 1 AND BLACKTYPE = 1 AND EFFECTDATE <= SYSDATE)");
			}
		}*/
		sqlSb.append(" ORDER BY TCP.BUDGETID DESC ");
		String sql = sqlSb.toString();
		return sql;
	}
	
	public String selectRepearBudgetName(String budgetname, BigDecimal orgid, BigDecimal budgetId) {
		String sql = "SELECT COUNT(0) FROM TBL_CYHW_PROJECTBUDGET WHERE BUDGETNAME ='"+budgetname+"' ";//+"' AND ORGID = '"+orgid+"'"
		if(budgetId != null) {
			sql += " AND BUDGETID != "+budgetId;
		}
		return sql;
	}
	
	public String selectListByPageInfoFlowid(IPage<TblCyhwProjectbudget> page, TblCyhwProjectbudget budget,
			TblStaffUtil staff, Integer taskCount, String flowid) {
		StringBuffer sqlSb = new StringBuffer("SELECT TORA.BRID,TORA.OPPOID,TORA.VERSION,TORA.OBRTYPE,TORA.BLACKDEADTIME,TORA.APRSTATUS,TORA.BACKREASON,TORA.CREATETIME TORCT,TORA.CREATESTAFF TORCR" + 
				" ,TCP.* from TBL_CYHW_PROJECTBUDGET TCP LEFT JOIN ( SELECT TOR.* FROM TBL_OPPBLACK_RECORD TOR INNER JOIN (SELECT MAX(VERSION) AS MAVER ,OPPOID FROM TBL_OPPBLACK_RECORD GROUP BY OPPOID) TORV" + 
				" ON TOR.VERSION = TORV.MAVER AND TOR.OPPOID = TORV.OPPOID LEFT JOIN TBL_OPPBLACK_REMOVE TORM ON TOR.BRID = TORM.BRID WHERE TOR.BRID NOT IN (SELECT BRID FROM TBL_OPPBLACK_REMOVE WHERE RMSTATUS = 6 AND BRID IS NOT NULL AND BRID != '') " + 
				" ) TORA ON TCP.BUDGETID = TORA.OPPOID WHERE TCP.RECORDTYPE = 'HTGL001' ");
		
		if(budget.getBudgetname() != null && !"".equals(budget.getBudgetname())) {
			sqlSb.append(" AND TCP.BUDGETNAME LIKE '%"+budget.getBudgetname()+"%'");
		}
		if(budget.getRecordtype() != null && !"".equals(budget.getRecordtype())) {
			sqlSb.append(" AND TCP.RECORDTYPE = '"+budget.getRecordtype()+"'");
		}
		if(budget.getInspectionstatus() != null) {
			sqlSb.append(" AND TCP.INSPECTIONSTATUS = '"+budget.getInspectionstatus()+"'");
		}
		if(budget.getCounterpartno()!=null && !"".equals(budget.getCounterpartno())) {
			sqlSb.append(" AND TCP.COUNTERPARTNO LIKE '%"+budget.getCounterpartno().trim()+"%'");
		}
		if(budget.getProjectstagegoal() != null && !"".equals(budget.getProjectgoal())) {
			sqlSb.append(" AND TCP.PROJECTSTAGEGOAL LIKE '%"+budget.getProjectgoal().trim()+"%'");
		}
		if(budget.getCounterpartaddress() != null && !"".equals(budget.getCounterpartaddress())) {
			sqlSb.append(" AND TCP.COUNTERPARTADDRESS LIKE '%"+budget.getCounterpartaddress().trim()+"%'");
		}
		sqlSb.append(" ORDER BY TCP.BUDGETID DESC ");
		String sql = sqlSb.toString();
		return sql;
	}
	
	public String selectOppsiteBlackList(IPage<TblCyhwProjectbudget> page, TblCyhwProjectbudget tcb,TblStaffUtil staff, boolean taskCount) throws Exception {
		StringBuffer sqlSb = new StringBuffer("SELECT TOBR.RBID,TOBR.REMREASON,TOBR.RMSTATUS,TORA.BRID,TORA.OPPOID,TORA.VERSION,TORA.OBRTYPE,TORA.BLACKDEADTIME,TORA.APRSTATUS,TORA.BACKREASON,TORA.CREATETIME TORCT,TORA.CREATESTAFF TORCR")
				.append(" ,TCP.* from TBL_CYHW_PROJECTBUDGET TCP LEFT JOIN ( SELECT TOR.* FROM TBL_OPPBLACK_RECORD TOR INNER JOIN (SELECT MAX(VERSION) AS MAVER ,OPPOID FROM TBL_OPPBLACK_RECORD GROUP BY OPPOID) TORV")
				.append(" ON TOR.VERSION = TORV.MAVER AND TOR.OPPOID = TORV.OPPOID LEFT JOIN TBL_OPPBLACK_REMOVE TORM ON TOR.BRID = TORM.BRID WHERE TOR.BRID NOT IN (SELECT BRID FROM TBL_OPPBLACK_REMOVE WHERE RMSTATUS = 6 AND BRID IS NOT NULL AND BRID != '') ")
				.append(" ) TORA ON TCP.BUDGETID = TORA.OPPOID LEFT JOIN TBL_OPPBLACK_REMOVE TOBR ON TORA.BRID = TOBR.BRID WHERE TCP.RECORDTYPE = 'HTGL001' AND ((TCP.ISBLACK = 1 AND TORA.OBRTYPE = 2 ) OR (TCP.ISBLACK = 1 AND TORA.OBRTYPE = 1 AND ")
				.append(DataBaseSqlConfig.getDateStrFormat(new Date())).append(" <= TORA.BLACKDEADTIME ))");
		/*if(!taskCount) {
			sqlSb.append(" AND  (BUDGETID in (select FROMID from TBL_MY_TASK where USRID="+staff.getStaffid()+") or CREATEUSER="+staff.getStaffid()+" ) ");
		}*/
		
		if (tcb.getBudgetname() != null && !"".equals(tcb.getBudgetname())) {
			sqlSb.append("  AND TCP.BUDGETNAME LIKE '%" + tcb.getBudgetname()+ "%'");
		}
	
		if (tcb.getRecordtype() != null && !"".equals(tcb.getRecordtype())) {
			sqlSb.append(" AND TCP.RECORDTYPE = '" + tcb.getRecordtype() + "'");
		}
	
		if (tcb.getInspectionstatus() != null) {
			sqlSb.append(" AND TCP.INSPECTIONSTATUS = '" + tcb.getInspectionstatus() + "'");
		}
		if (tcb.getCounterpartno() != null && !"".equals(tcb.getCounterpartno())) {
			sqlSb.append(" AND TCP.COUNTERPARTNO LIKE '%" + tcb.getCounterpartno().trim() + "%'");
		}
	
		if (tcb.getProjectstagegoal() != null && !"".equals(tcb.getProjectstagegoal())) {
			sqlSb.append(" AND TCP.PROJECTSTAGEGOAL LIKE '%" + tcb.getProjectstagegoal().trim() + "%'");
		}
	
		if (tcb.getCounterpartaddress() != null && !"".equals(tcb.getCounterpartaddress())) {
			sqlSb.append(" AND TCP.COUNTERPARTADDRESS LIKE '%" + tcb.getCounterpartaddress().trim() + "%'");
		}
		
		if(tcb.getBlackAprStatus() != null) {
			sqlSb.append(" AND TCP.BLACKAPRSTATUS = " + tcb.getBlackAprStatus());
		}
	
		if (tcb.getBlacktype() != null && tcb.getBlacktype() == 3) {
			sqlSb.append(" AND TCP.BLACKTYPE IS NOT NULL");
		}
	
		sqlSb.append(" ORDER BY TCP.BUDGETID DESC ");
		String sql = sqlSb.toString();
		return sql;
	}
	
	public String findCollectionChoiceContractPid(IPage<TblCyhwProjectbudget> page,TblCyhwProjectbudget tcpb) throws Exception {
		//" AND ORGID = " + tcpb.getOrgid()
		StringBuffer sqlSb = new StringBuffer("SELECT TCP.BUDGETID,TCP.BUDGETNAME,TCP.COUNTERPARTNO,TCP.PROJECTSTAGEGOAL,TCP.COUNTERPARTADDRESS,TCP.COUNTERPARTHANK,TCP.TOTALTMONEY,TCP.RESULTDESCRIPTION FROM TBL_CYHW_PROJECTBUDGET TCP " +
				"LEFT JOIN (\n" +
				"    SELECT\n" +
				"        TOR.*\n" +
				"    FROM\n" +
				"        TBL_OPPBLACK_RECORD TOR\n" +
				"    INNER JOIN (\n" +
				"        SELECT\n" +
				"            MAX(VERSION) AS MAVER ,\n" +
				"            OPPOID\n" +
				"        FROM\n" +
				"            TBL_OPPBLACK_RECORD\n" +
				"        GROUP BY\n" +
				"            OPPOID) TORV \n" +
				"                ON\n" +
				"        TOR.VERSION = TORV.MAVER\n" +
				"        AND TOR.OPPOID = TORV.OPPOID\n" +
				"    LEFT JOIN TBL_OPPBLACK_REMOVE TORM ON\n" +
				"        TOR.BRID = TORM.BRID\n" +
				"    WHERE\n" +
				"        TOR.BRID NOT IN (\n" +
				"        SELECT\n" +
				"            BRID\n" +
				"        FROM\n" +
				"            TBL_OPPBLACK_REMOVE\n" +
				"        WHERE\n" +
				"            RMSTATUS = 6\n" +
				"            AND BRID IS NOT NULL\n" +
				"            AND BRID != '')  \n" +
				"                ) TORA ON\n" +
				"    TCP.BUDGETID = TORA.OPPOID" +
				" WHERE TCP.RECORDTYPE = 'HTGL001' AND TCP.INSPECTIONSTATUS = " + tcpb.getInspectionstatus());
		if (tcpb.getBudgetname() != null && !"".equals(tcpb.getBudgetname())) {
			sqlSb.append(" AND TCP.BUDGETNAME LIKE '%" + tcpb.getBudgetname() + "%'");
		}
		if (tcpb.getCounterpartno() != null && !"".equals(tcpb.getCounterpartno())) {
			sqlSb.append(" AND TCP.COUNTERPARTNO LIKE '%" + tcpb.getCounterpartno() + "%'");
		}
		if ((tcpb.getIsblack() != null && (tcpb.getIsblack() == 2))) {
			sqlSb.append(" AND ((TCP.ISBLACK = 2 OR TCP.ISBLACK = 0 OR TCP.ISBLACK IS NULL OR TCP.ISBLACK = ''  ) OR (TCP.ISBLACK = 1 AND TORA.OBRTYPE = 1 AND ").append(DataBaseSqlConfig.getDateStrFormat(new Date())).append(" >= TORA.BLACKDEADTIME ))");
		}
		sqlSb.append(" ORDER BY TCP.BUDGETID DESC");
		String sql = sqlSb.toString();
		return sql;
	}

}
