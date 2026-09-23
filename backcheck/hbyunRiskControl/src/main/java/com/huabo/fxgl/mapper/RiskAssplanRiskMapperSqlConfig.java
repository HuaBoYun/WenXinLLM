package com.huabo.fxgl.mapper;

import java.math.BigDecimal;

import com.hbfk.util.PageInfo;
import com.huabo.fxgl.entity.RiskAssplanRisk;

public class RiskAssplanRiskMapperSqlConfig {
	public String selectRiskInRiskIdAndAssId(PageInfo<RiskAssplanRisk> pageInfo) {
		RiskAssplanRisk ass = pageInfo.getCondition();
		String sql = "SELECT T2.* FROM ( SELECT T1.*,ROWNUM RN FROM (SELECT TRAR.ASSRISKID,TRAR.ASSPLANID,TRAR.RISKID,TRAR.RISKLEVEL,TRAR.ASSDATE,TR.RISKNUMBER,TR.RISKDES,TR.RISKNAME FROM TBL_RISK_ASSPLAN_RISK TRAR LEFT JOIN TBL_RISK TR ON TRAR.RISKID = TR.RISKID ";
		sql += " WHERE TRAR.ASSPLANID = "+ass.getAssplanid()+" AND TRAR.RISKID IN ("+ass.getRiskIds()+")";
		sql += " ORDER BY TRAR.ASSRISKID ASC ) T1 WHERE ROWNUM > "+pageInfo.getCurrentRecord()+" ) T2 WHERE T2.RN <= "+(pageInfo.getPageSize()*pageInfo.getCurrentPage());
		return sql;
	}
	
	public String selectRiskInRiskIdCount(PageInfo<RiskAssplanRisk> pageInfo) {
		RiskAssplanRisk ass = pageInfo.getCondition();
		String sql = "SELECT COUNT(0) FROM TBL_RISK_ASSPLAN_RISK WHERE ASSPLANID = "+ass.getAssplanid()+" AND RISKID IN ("+ass.getRiskIds()+")";
		return sql;
	}

	
}
