package com.huabo.finance.mappersql;

import java.math.BigDecimal;

import org.apache.commons.lang.StringUtils;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.huabo.finance.vo.BdFinanceplanVo;
import com.huabo.finance.vr.BdFinanceplanVr;

public class BdFinanceplanMapperSqlConfig {
	
	public String selectPageInfo(IPage<BdFinanceplanVr> page, BdFinanceplanVo vo) throws Exception{
		StringBuffer sqlSb = new StringBuffer("SELECT BFP.*,BFD.FINTEXT AS DBCONFIGNAME,BFV.HANDTEXT AS FVERSIONNAME,CORG.ORGNAME AS FINANCEORGNAME,CTS.REALNAME AS CREATORNAME FROM BD_FINANCEPLAN BFP LEFT JOIN BD_FINANCEDATE BFD ON BFP.DBCONFIGID = BFD.FID")
				.append(" LEFT JOIN BD_FINVERSION BFV ON BFP.FVERSIONID = BFV.FID LEFT JOIN TBL_ORGANIZATION CORG ON BFP.FINANCEORGID = CORG.ORGID LEFT JOIN TBL_STAFF CTS ON BFP.CREATOR = CTS.STAFFID")
				.append(" WHERE 1 = 1 ");
		
		if(StringUtils.isNotBlank(vo.getFname())) {
			sqlSb.append(" AND BFP.FNAME LIKE '%").append(vo.getFversionname()).append("%'");
		}
		
		if(StringUtils.isNotBlank(vo.getDbconfigname())) {
			sqlSb.append(" AND BFD.FINTEXT LIKE '%").append(vo.getDbconfigname()).append("%'");
		}
		
		if(vo.getLinkOrgId() != null) {
			sqlSb.append(" AND BFP.LINKORGID = '").append(vo.getLinkOrgId()).append("'");
		}
		
		if(vo.getFstatus() != null) {
			sqlSb.append(" AND BFP.FSTATUS = '").append(vo.getFstatus()).append("'");
		}
		
		if(StringUtils.isNotBlank(vo.getFversionname())) {
			sqlSb.append(" AND BFV.HANDTEXT LIKE '%").append(vo.getFversionname()).append("%'");
		}
		
		if(StringUtils.isNotBlank(vo.getFinanceorgname())) {
			sqlSb.append(" AND CORG.ORGNAME LIKE '%").append(vo.getFinanceorgname()).append("%'");
		}
		
		if(StringUtils.isNotBlank(vo.getCreatorname())) {
			sqlSb.append(" AND CTS.REALNAME LIKE '%").append(vo.getCreatorname()).append("%'");
		}
		
		sqlSb.append(" ORDER BY BFP.CREATIONTIME ASC");
		
		String sql = sqlSb.toString();
		return sql;
	}
	
	public String  selectListForFinance(BigDecimal orgid, String fname, Integer fstatus) throws Exception {
		StringBuffer sqlSb = new StringBuffer("SELECT BFP.FID,BFP.FNAME,T2.RECORDID,T2.RECORDNAME,T2.ISCOMPLETED FROM BD_FINANCEPLAN BFP " + 
				"LEFT JOIN (" + 
				"SELECT BFR.* FROM BD_FINANCEDATE_RECORD BFR INNER JOIN (" + 
				"SELECT MAX(CREATETIME) AS LASTTIME FROM BD_FINANCEDATE_RECORD WHERE PRECORDID IS NULL AND SQLID IS NULL GROUP BY PLANID " + 
				") T1 ON BFR.CREATETIME = T1.LASTTIME WHERE BFR.SQLID IS NULL AND BFR.PRECORDID IS NULL" + 
				") T2 ON T2.PLANID = BFP.FID" + 
				" WHERE  1= 1 ");
		if(orgid != null){
			sqlSb.append(" AND BFP.LINKORGID = ").append(orgid);
		}
		
		if(fstatus != null) {
			sqlSb.append(" AND BFP.FSTATUS = ").append(fstatus);
		}
		
		if(StringUtils.isNotBlank(fname)) {
			sqlSb.append(" AND BFP.FNAME LIKE '%").append(fname).append("%'");
		}
		
		sqlSb.append(" ORDER BY BFP.FID ASC");
		
		String sql = sqlSb.toString();
		
		return sql;
	}

}
