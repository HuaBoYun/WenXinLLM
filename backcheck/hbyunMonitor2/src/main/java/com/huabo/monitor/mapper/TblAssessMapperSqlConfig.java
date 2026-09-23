package com.huabo.monitor.mapper;



import java.util.List;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.entity.TblTransferWorkUtils;
import com.hbfk.util.PageInfo;
import com.hbfk.util.database.DataBaseSqlConfig;
import com.huabo.monitor.entity.TblAssesslevel;
import com.huabo.monitor.entity.TblTestTemplate;

public class TblAssessMapperSqlConfig {

	public static String setSql(String t,TblStaffUtil loginStaff, Integer authorityType){
		StringBuffer sqlSb=new StringBuffer();
		try {
			
			if(loginStaff.getCurrentOrg().getUseSecrect() == 1) {
				//知悉范围查询
				sqlSb.append(" AND ("+DataBaseSqlConfig.getWhereColumnInStr(t+"STAFFSCOPEIDS", loginStaff.getStaffid().toString(),",")+" OR "+t+"STAFFSCOPEIDS IS NULL OR "+t+"STAFFSCOPEIDS = '' ");
				
				if(authorityType == 0) {
					sqlSb.append(" OR "+t+"CREATESTAFFID = "+loginStaff.getStaffid());
				}
				
				sqlSb.append(")");
				//密级查询
				 if(StringUtils.isNotBlank(loginStaff.getSecrectScopeIds())) {
			        sqlSb.append(" AND ("+t+"SECRECTLEVELID IN (").append(loginStaff.getSecrectScopeIds()).append(") OR "+t+"SECRECTLEVELID IS NULL OR "+t+"SECRECTLEVELID = ''  )");
			     }else {
			        sqlSb.append(" AND ("+t+"SECRECTLEVELID IS NULL OR "+t+"SECRECTLEVELID = ''  )");
			     }
			}
		
		 //角色数据范围，工作交接 本人创建数据查询
		List<TblTransferWorkUtils> workList = loginStaff.getWorkList();
		if(authorityType == 0 ) {
			if(StringUtils.isNotBlank(loginStaff.getDeptIds())) {
				sqlSb.append(" AND ("+t+"CREATESTAFFID = ").append(loginStaff.getStaffid()).append(" OR "+t+"LINKORGID IN (").append(loginStaff.getDeptIds()).append(") OR "+t+"LINKDEPTID IN (").append(loginStaff.getDeptIds()).append(")");
			}else {
				sqlSb.append(" AND ("+t+"CREATESTAFFID = ").append(loginStaff.getStaffid());
			}
		}
		
		for (TblTransferWorkUtils work : workList) {
			sqlSb.append(" OR ("+t+"CREATETIME >= "+DataBaseSqlConfig.getDateStrFormat(work.getTransfertime())+" AND "+t+"LINKDEPTID IN ("+work.getTranorgidstrs()+") AND "+t+"CREATESTAFFID = "+work.getTransferstaffid()+")"); 
		}
		sqlSb.append(")");
		} catch (Exception e) {
			e.printStackTrace();
		}

		return sqlSb.toString();
	}
	public static String setSqlNew(String t,TblStaffUtil loginStaff, Integer authorityType){
		StringBuffer sqlSb=new StringBuffer();
		try {
			
			if(loginStaff.getCurrentOrg().getUseSecrect() == 1) {
				//知悉范围查询
				sqlSb.append(" AND ("+DataBaseSqlConfig.getWhereColumnInStr(t+"STAFFSCOPEIDS", loginStaff.getStaffid().toString(),",")+" OR (("+t+"STAFFSCOPEIDS IS NULL OR "+t+"STAFFSCOPEIDS = '') ");
				
				if(authorityType == 0) {
					sqlSb.append(" and "+t+"CREATESTAFFID = "+loginStaff.getStaffid());
				}
				
				sqlSb.append(") or (TAS.STAFFID = "+loginStaff.getStaffid()+" OR t.LEADERID ="+loginStaff.getStaffid()+") ) ");
				//密级查询
				 if(StringUtils.isNotBlank(loginStaff.getSecrectScopeIds())) {
			        sqlSb.append(" AND ("+t+"SECRECTLEVELID IN (").append(loginStaff.getSecrectScopeIds()).append(") OR "+t+"SECRECTLEVELID IS NULL OR "+t+"SECRECTLEVELID = ''  )");
			     }else {
			        sqlSb.append(" AND ("+t+"SECRECTLEVELID IS NULL OR "+t+"SECRECTLEVELID = ''  )");
			     }
			}
		
		 //角色数据范围，工作交接 本人创建数据查询
		List<TblTransferWorkUtils> workList = loginStaff.getWorkList();
		if(authorityType == 0 ) {
			if(StringUtils.isNotBlank(loginStaff.getDeptIds())) {
				sqlSb.append(" AND ("+t+"CREATESTAFFID = ").append(loginStaff.getStaffid()).append(" OR "+t+"LINKORGID IN (").append(loginStaff.getDeptIds()).append(") OR "+t+"LINKDEPTID IN (").append(loginStaff.getDeptIds()).append(")");
			}else {
				sqlSb.append(" AND ("+t+"CREATESTAFFID = ").append(loginStaff.getStaffid());
			}
		}
		
		for (TblTransferWorkUtils work : workList) {
			sqlSb.append(" OR ("+t+"CREATETIME >= "+DataBaseSqlConfig.getDateStrFormat(work.getTransfertime())+" AND "+t+"LINKDEPTID IN ("+work.getTranorgidstrs()+") AND "+t+"CREATESTAFFID = "+work.getTransferstaffid()+")"); 
		}
		sqlSb.append(")");
		} catch (Exception e) {
			e.printStackTrace();
		}

		return sqlSb.toString();
	}
}
