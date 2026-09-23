package com.huabo.finance.mappersql;

import org.apache.commons.lang.StringUtils;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.huabo.finance.vo.BdPlanSqlconfigVo;
import com.huabo.finance.vr.BdPlanSqlconfigVr;

public class BdPlanSqlconfigMapperSqlConfig {

	public String selectPageInfo(IPage<BdPlanSqlconfigVr> page, BdPlanSqlconfigVo vo) throws Exception{
		StringBuffer sqlSb = new StringBuffer("SELECT BIS.FID AS SQLCONFIGID,BIS.INCREMENTCOL AS FINITINCREMENTCOL ,BIS.FNAME AS SQLCONFIGNAME,BIS.FVERSIONID AS CONFIGVERSIONID,BIS.FINSPECIFICITYCOL AS CONFIGSPECIFICITYCOL,'").append(vo.getFplanid()).append("' AS FINITPLANID,BPS.* FROM BD_INIT_SQLCONFIG BIS LEFT JOIN BD_PLAN_SQLCONFIG BPS ON BPS.FPLANID = '").append(vo.getFplanid()).append("' AND BIS.FID = BPS.FINITSQLID ");
		
		sqlSb.append(" WHERE BIS.FVERSIONID = (SELECT FVERSIONID FROM BD_FINANCEPLAN WHERE FID = '").append(vo.getFplanid()).append("')");
		
		if(StringUtils.isNotBlank(vo.getFname())) {
			sqlSb.append(" AND ( BPS.FNAME LIKE '%").append(vo.getFname()).append("%' OR BIS.FNAME LIKE '%").append(vo.getFname()).append("%'");
		}
		sqlSb.append(" ORDER BY BIS.FID ASC ");
		String sql = sqlSb.toString();
		System.out.println(sql);
		return sql;
	}
}
