package com.huabo.monitor.mapper;



import java.util.List;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.entity.TblTransferWorkUtils;
import com.hbfk.util.PageInfo;
import com.hbfk.util.database.DataBaseSqlConfig;
import com.huabo.monitor.entity.TblAssesslevel;
import com.huabo.monitor.entity.TblTestTemplate;

public class TblAssessPlanMapperSqlConfig {

	 
	public static String setSql(TblStaffUtil loginStaff){
		StringBuffer sqlSb=new StringBuffer();
		try {
		if(loginStaff.getCurrentOrg().getUseSecrect() == 1) {
			//知悉范围查询
			sqlSb.append(" AND ("+DataBaseSqlConfig.getWhereColumnInStr("STAFFSCOPEIDS", loginStaff.getStaffid().toString(),",")+" OR p.CREATESTAFFID = "+loginStaff.getStaffid()+" OR p.STAFFSCOPEIDS IS NULL OR p.STAFFSCOPEIDS = '' )");
			
			//密级查询
			 if(StringUtils.isNotBlank(loginStaff.getSecrectScopeIds())) {
		        sqlSb.append(" AND (p.SECRECTLEVELID IN (").append(loginStaff.getSecrectScopeIds()).append(") OR p.SECRECTLEVELID IS NULL OR p.SECRECTLEVELID = ''  )");
		     }else {
		        sqlSb.append(" AND (p.SECRECTLEVELID IS NULL OR p.SECRECTLEVELID = ''  )");
		     }
		}
		
		 //角色数据范围，工作交接 本人创建数据查询
		List<TblTransferWorkUtils> workList = loginStaff.getWorkList();
		if(StringUtils.isNotBlank(loginStaff.getDeptIds())) {
			sqlSb.append(" AND (p.CREATESTAFFID = ").append(loginStaff.getStaffid()).append(" OR p.LINKORGID IN (").append(loginStaff.getDeptIds()).append(") OR p.LINKDEPTID IN (").append(loginStaff.getDeptIds()).append(")");
		}else {
			sqlSb.append(" AND (p.CREATESTAFFID = ").append(loginStaff.getStaffid());
		}
		for (TblTransferWorkUtils work : workList) {
			sqlSb.append(" OR (p.CREATETIME >= "+DataBaseSqlConfig.getDateStrFormat(work.getTransfertime())+" AND p.LINKDEPTID IN ("+work.getTranorgidstrs()+") AND p.CREATESTAFFID = "+work.getTransferstaffid()+")"); 
		}
		sqlSb.append(")");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		return sqlSb.toString();
	}
	
 

}
