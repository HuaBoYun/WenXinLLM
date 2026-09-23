package com.huabo.finance.mappersql;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.util.UriUtils;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hbfk.entity.FaAccbookinfoUtil;
import com.hbfk.util.database.DataBaseSqlConfig;
import com.huabo.finance.config.DatabaseConfig;
import com.huabo.finance.entity.caiji.GlVoucher;
import com.huabo.finance.vo.BdPlanSqlconfigVo;
import com.huabo.finance.vo.ExportRequestVo;
import com.huabo.finance.vo.GlAssBalaneVo;
import com.huabo.finance.vo.GlVoucherVo;
import com.huabo.finance.vr.BdPlanSqlconfigVr;
import com.huabo.finance.vr.GlAssBalaneVr;

public class GlAssBalaneMapperSqlConfig {
	
	public String  exportAccAssGeneralLedgerList(ExportRequestVo exportRequestVo) throws Exception{
		StringBuffer sqlSb = new StringBuffer("SELECT GAB.*,BAC.CODE,BAC.NAME,BAC.BALANORIENT,BFA.ASSNAME,BFA.ASSDES,BFA.ASSTYPE FROM GL_ASS_BALANE GAB LEFT JOIN BD_ACCOUNT BAC ON GAB.PK_ACCASOA = BAC.PK_ACCOUNT AND BAC.FPLANID = GAB.FPLANID AND BAC.DATAORIGINFLAG = -2 LEFT JOIN BD_FINANCE_ACCASS BFA ON GAB.PK_ACCASS = BFA.PK_BUNESSIES AND GAB.FPLANID = GAB.FPLANID AND GAB.DATAORIGINFLAG = -2 WHERE 1 = 1");		
		
		if(StringUtils.isNotBlank(exportRequestVo.getPkOrg())) {
			sqlSb.append(" AND GAB.PK_ORG = '"+exportRequestVo.getPkOrg()+"'");
		}
		
		if(exportRequestVo.getPkAssbalances() != null){
				sqlSb.append(" AND GAB.PK_ASSBALANCE IN (");
				for (String pka : exportRequestVo.getPkAssbalances()) {
					sqlSb.append("'").append(UriUtils.decode(pka, "utf-8")).append("',");
				}
				sqlSb.deleteCharAt(sqlSb.length()-1).append(")");
		}
		
		sqlSb.append(" ORDER BY ").append(DataBaseSqlConfig.toNumberColumn("GAB.YEAR"))
		.append(" DESC , GAB.PK_ACCASS,BAC.CODE , ").append(DataBaseSqlConfig.toNumberColumn("GAB.PERIOD")).append(" ASC");
		String sql = sqlSb.toString();
		return sql;
	}
	
	public String  exportAccAssBalanceList(ExportRequestVo exportRequestVo) throws Exception{
		StringBuffer sqlSb = new StringBuffer("SELECT GAB.PK_ASSBALANCE,GAB.PK_ORG,GAB.PK_ACCASOA,GAB.YEAR,GAB.PERIOD,GAB.PK_ACCASS,GAB.DEBITAMOUNT,GAB.CREDITMOUNT,GAB.BEGINBALANCEMOUNT,GAB.ENDBALANCEMOUNT,GAB.YEARDEBITAMOUNT,GAB.YEARCREDITMOUNT,BFA.ASSTYPE,BFA.ASSNAME,BFA.ASSDES,BFA.ASSDD FROM GL_ASS_BALANE GAB LEFT JOIN BD_FINANCE_ACCASS BFA ON GAB.PK_ACCASS = BFA.PK_BUNESSIES");		
		
		sqlSb.append(" WHERE 1 = 1 AND GAB.PK_ORG = '"+exportRequestVo.getPkOrg()+"'");
		
		if(StringUtils.isNotBlank(exportRequestVo.getPkOrg())) {
			sqlSb.append(" AND GAB.PK_ORG = '"+exportRequestVo.getPkOrg()+"'");
		}
		
			if(exportRequestVo.getPkAssbalances() != null){
					sqlSb.append(" AND GAB.PK_ASSBALANCE IN (");
					for (String pka : exportRequestVo.getPkAssbalances()) {
						sqlSb.append("'").append(UriUtils.decode(pka, "utf-8")).append("',");
					}
					sqlSb.deleteCharAt(sqlSb.length()-1).append(")");
			}
		sqlSb.append(" ORDER BY GAB.YEAR,GAB.PERIOD ");
		String sql = sqlSb.toString();
		return sql;
	}
	
	public String selectMinPeriod(GlAssBalaneVr vr) throws Exception{
		StringBuffer sqlSb = new StringBuffer("SELECT MIN(").append(DataBaseSqlConfig.toNumberColumn("PERIOD")).append(") FROM GL_ASS_BALANE WHERE PK_ORG = '")
				.append(vr.getPkOrg()).append("' AND PK_ACCASOA = '").append(vr.getPkAccasoa()).append("' AND YEAR = '").append(vr.getYear())
				.append("' AND PK_ACCASS = '").append(vr.getPkAccass()).append("' AND FPLANID = '").append(vr.getFplanid()).append("' AND DATAORIGINFLAG = -2");
		
		String sql = sqlSb.toString();
		return sql;
	}
	

	public String  selectFinanceAccAssBalanceList(Page<GlAssBalaneVr> page, GlAssBalaneVo vo,FaAccbookinfoUtil bookInfo) throws Exception{
		StringBuffer sqlSb = new StringBuffer("SELECT DISTINCT GAB.PK_ASSBALANCE,GAB.PK_ORG,GAB.PK_ACCASOA,GAB.YEAR,GAB.PERIOD,GAB.PK_ACCASS,GAB.DEBITAMOUNT,GAB.CREDITMOUNT,GAB.BEGINBALANCEMOUNT,GAB.ENDBALANCEMOUNT,GAB.YEARDEBITAMOUNT,GAB.YEARCREDITMOUNT,BFA.ASSTYPE,BFA.ASSNAME,BFA.ASSDES,BAC.BALANORIENT FROM GL_ASS_BALANE GAB LEFT JOIN BD_FINANCE_ACCASS BFA ON GAB.PK_ACCASS = BFA.PK_BUNESSIES");		
		
		sqlSb.append(" LEFT JOIN BD_ACCOUNT BAC ON GAB.PK_ACCASOA = BAC.PK_ACCOUNT WHERE "+ DataBaseSqlConfig.getWhereColumnInStr("BFA.PK_ACCASSITEM", vo.getPkAccassitem(), ",")+" AND GAB.FPLANID = '"+bookInfo.getPkFinanplanid()+"' AND GAB.DATAORIGINFLAG = -2 AND GAB.PK_ORG = '"+vo.getPkOrg()+"'");
		
		if(StringUtils.isNotBlank(vo.getYear())) {
			sqlSb.append(" AND GAB.YEAR = '").append(vo.getYear()).append("'");
		}
		
		if(StringUtils.isNotBlank(vo.getPeriod())) {
			sqlSb.append(" AND GAB.PERIOD = '").append(vo.getPeriod()).append("'");
		}
		
		if(StringUtils.isNotBlank(vo.getAssname())) {
			sqlSb.append(" AND BFA.ASSNAME LIKE '%").append(vo.getAssname()).append("%'");
		}
		
		if(StringUtils.isNotBlank(vo.getAssdes())) {
			sqlSb.append(" AND BFA.ASSDES LIKE '%").append(vo.getAssdes()).append("%'");
		}
		
		if(StringUtils.isNotBlank(vo.getAssdd())) {
			sqlSb.append(" AND BFA.ASSDD LIKE '%").append(vo.getAssdd()).append("%'");
		}
		
		sqlSb.append(" ORDER BY GAB.YEAR,GAB.PERIOD ");
		String sql = sqlSb.toString();
		return sql;
	}
	
	
	public String  selectFinanceAccAssGeneralLedgerList(Page<GlAssBalaneVr> page, GlAssBalaneVo vo,FaAccbookinfoUtil bookInfo) throws Exception{
		StringBuffer sqlSb = new StringBuffer("SELECT DISTINCT GAB.*,BAC.CODE,BAC.NAME,BAC.BALANORIENT,BFA.ASSNAME,BFA.ASSDES,BFA.ASSTYPE FROM GL_ASS_BALANE GAB LEFT JOIN BD_ACCOUNT BAC ON GAB.PK_ACCASOA = BAC.PK_ACCOUNT AND BAC.FPLANID = GAB.FPLANID AND BAC.DATAORIGINFLAG = -2 LEFT JOIN BD_FINANCE_ACCASS BFA ON GAB.PK_ACCASS = BFA.PK_BUNESSIES AND GAB.FPLANID = GAB.FPLANID AND GAB.DATAORIGINFLAG = -2");		
		
		sqlSb.append(" WHERE BFA.PK_ACCASSITEM = "+DataBaseSqlConfig.getWhereColumnInStr("BFA.PK_ACCASSITEM", vo.getPkAccassitem(), ",")+" AND GAB.FPLANID = '"+bookInfo.getPkFinanplanid()+"' AND GAB.DATAORIGINFLAG = -2 AND GAB.PK_ORG = '"+vo.getPkOrg()+"'");
		
		if(StringUtils.isNotBlank(vo.getYear())) {
			sqlSb.append(" AND GAB.YEAR = '").append(vo.getYear()).append("'");
		}
		
		if(StringUtils.isNotBlank(vo.getPeriod())) {
			sqlSb.append(" AND GAB.PERIOD = '").append(vo.getPeriod()).append("'");
		}
		
		if(StringUtils.isNotBlank(vo.getAssname())) {
			sqlSb.append(" AND BFA.ASSNAME LIKE '%").append(vo.getAssname()).append("%'");
		}
		
		if(StringUtils.isNotBlank(vo.getAssdes())) {
			sqlSb.append(" AND BFA.ASSDES LIKE '%").append(vo.getAssdes()).append("%'");
		}
		
		if(StringUtils.isNotBlank(vo.getAssdd())) {
			sqlSb.append(" AND BFA.ASSDD LIKE '%").append(vo.getAssdd()).append("%'");
		}
		
		sqlSb.append(" ORDER BY ").append(DataBaseSqlConfig.toNumberColumn("GAB.YEAR"))
		.append(" DESC , GAB.PK_ACCASS,BAC.CODE , ").append(DataBaseSqlConfig.toNumberColumn("GAB.PERIOD")).append(" ASC");
		String sql = sqlSb.toString();
		return sql;
	}
}
