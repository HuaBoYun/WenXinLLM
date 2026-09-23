package com.huabo.fxgl.mapper;

import org.apache.commons.lang.StringUtils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hbfk.entity.GeneralEntity;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.database.GeneralSQLConcatConfig;
import com.huabo.fxgl.entity.Find;
import com.huabo.fxgl.entity.RiskAssplan;

public class RiskAssplanMapperSqlConfig {

	 public String selectListByPageInfo(TblStaffUtil staffUtil, Find find,Integer authorityType) throws Exception{
		 StringBuffer sb = new StringBuffer("SELECT a.*,o.orgname as orgname FROM TBL_RISK_ASSPLAN a left join tbl_organization o on a.UNIT=o.orgid WHERE PLANSTATUS = '3'");
         //风险管理员的验证
		 sb.append(GeneralSQLConcatConfig.concatSecrectSqlEntity(new GeneralEntity(staffUtil.getCurrentOrg().getUseSecrect(), authorityType==0, "UNIT", "LINKDEPTID", "CREATESTAFFID", "SECRECTLEVELID", "STAFFSCOPEIDS", staffUtil.getStaffid(), staffUtil.getDeptIds(), staffUtil.getSecrectScopeIds(),authorityType)));
		// sb.append(GeneralSQLConcatConfig.concatSecrectSql(staffUtil.getCurrentOrg().getUseSecrect(), authorityType==0, "UNIT", "LINKDEPTID", "CREATESTAFFID", "SECRECTLEVELID", "STAFFSCOPEIDS", staffUtil.getStaffid(), staffUtil.getDeptIds(), staffUtil.getSecrectScopeIds()));
		
	     // 如果提供了计划编号，添加计划编号的模糊匹配条件
	     if (StringUtils.isNotEmpty(find.getCode()))
	    	 sb.append(" AND PLANCODE LIKE '%").append(find.getCode()).append("%'");
	     // 如果提供了计划名称，添加计划名称的模糊匹配条件
	     if (StringUtils.isNotEmpty(find.getName()))
	        sb.append(" AND PLANNAME LIKE '%").append(find.getName()).append("%'");
	     // 如果提供了单位ID，添加单位ID的精确匹配条件
	     if (find != null && find.getId() != null)
	        sb.append(" AND UNIT = ").append(find.getId());
		 
	     sb.append(" ORDER BY RECORDDATE DESC ");
		 String sql = sb.toString();
		 return sql;
	 }
	
 

}
