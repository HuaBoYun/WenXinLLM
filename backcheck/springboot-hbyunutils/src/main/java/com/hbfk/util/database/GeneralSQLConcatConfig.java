package com.hbfk.util.database;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.hbfk.entity.GeneralEntity;
import com.hbfk.entity.TblTransferWorkUtils;

public class GeneralSQLConcatConfig {
	
	/**
	 * 拼接密级和知悉范围查询条件  通用方法
	 * @param isUseSecrect  -- 是否使用密级   来源loginStaff.getCurrentOrg().getUseSecrect()
	 * @param isSelf        -- 是否添加限制创建人条件  true 是，false 否
	 * @param companyCol    -- 业务数据所属 公司列名
	 * @param deptCol       -- 业务数据所属 部门列名 
	 * @param createUserCol -- 业务数据创建用户 列名
	 * @param secrectCol    -- 业务数据密级主键 列名
	 * @param staffScopeCol -- 业务数据知悉范围 列名
	 * @param staffid       -- 当前登录用户主键   来源loginStaff.getStaffid()
	 * @param deptIds		-- 当前登录用户允许查看部门数据范围 来源 loginStaff.getDeptIds()
	 * @param secrectScopeIds  --当前登录查看密级数据范围  来源 loginStaff.getSecrectScopeIds()
	 * @return
	 * @throws Exception
	 */
	public static String concatSecrectSql(Integer isUseSecrect,boolean isSelf,String companyCol,String deptCol,String createUserCol,String secrectCol,
			String staffScopeCol,BigDecimal staffid,String deptIds,String secrectScopeIds) throws  Exception {
		StringBuffer sqlSb = new StringBuffer();
		
		if(isUseSecrect != null && isUseSecrect == 1) {
			//知悉范围查询
			sqlSb.append(" AND ( ( "+DataBaseSqlConfig.getWhereColumnInStr(staffScopeCol, staffid.toString(),",")+" OR ")
				.append(createUserCol).append(" = "+staffid);
			
			
			if(!isSelf) {
				sqlSb.append(" OR ").append(staffScopeCol).append(" IS NULL OR ").append(staffScopeCol).append(" = ''");
			}
			
			sqlSb.append(" ) ");
			
			 if(StringUtils.isNotBlank(deptIds)) {
				 sqlSb.append(" OR ").append(companyCol).append(" IN (").append(deptIds).append(") OR ").append(deptCol).append(" IN (").append(deptIds).append(")  ");
			 }
			 sqlSb.append(" ) ");
			//密级查询
			 if(StringUtils.isNotBlank(secrectCol)) {
				 sqlSb.append(" AND (").append(secrectCol).append(" IN (").append(secrectScopeIds).append(") OR ").append(secrectCol).append(" IS NULL OR ").append(secrectCol).append(" = ''  )");
		     }else {
		    	 sqlSb.append(" AND (").append(secrectCol).append(" IS NULL OR ").append(secrectCol).append(" = ''  )");
		     }
			 
		}else {
			if(StringUtils.isNotBlank(deptIds) && StringUtils.isNotBlank(companyCol) && StringUtils.isNotBlank(deptCol) ) {
				sqlSb.append(" AND ( ").append(createUserCol).append(" = ").append(staffid).append(" OR ").append(companyCol).append(" IN (").append(deptIds).append(") OR ").append(deptCol).append(" IN (").append(deptIds).append(") )");
			}
			
			if(isSelf && StringUtils.isBlank(deptIds)) {
				sqlSb.append(" AND ").append(createUserCol).append(" = ").append(staffid);
			}
			
		}
		String sql = sqlSb.toString();
		return sql;
	}
	
	//用于有任务的责任人也可以看到数据的情况
	public static String concatSecrectSqlCase2(Integer isUseSecrect,boolean isSelf,String companyCol,String deptCol,String createUserCol,String secrectCol,
			String staffScopeCol,BigDecimal staffid,String deptIds,String secrectScopeIds,String querySql) throws  Exception {
		StringBuffer sqlSb = new StringBuffer();
		
		if(isUseSecrect != null && isUseSecrect == 1) {
			//知悉范围查询
			sqlSb.append(" AND ( ( "+DataBaseSqlConfig.getWhereColumnInStr(staffScopeCol, staffid.toString(),",")+" OR ")
				.append(createUserCol).append(" = "+staffid);
			
			
			if(!isSelf) {
				sqlSb.append(" OR ").append(staffScopeCol).append(" IS NULL OR ").append(staffScopeCol).append(" = ''");
			}
			
			if(StringUtils.isNotBlank(querySql)){
				sqlSb.append(querySql);
			}
			sqlSb.append(" ) ");
			
			 if(StringUtils.isNotBlank(deptIds)) {
				 sqlSb.append(" OR ").append(companyCol).append(" IN (").append(deptIds).append(") OR ").append(deptCol).append(" IN (").append(deptIds).append(")  ");
			 }
			 sqlSb.append(" ) ");
			//密级查询
			 if(StringUtils.isNotBlank(secrectCol)) {
				 sqlSb.append(" AND (").append(secrectCol).append(" IN (").append(secrectScopeIds).append(") OR ").append(secrectCol).append(" IS NULL OR ").append(secrectCol).append(" = ''  )");
		     }else {
		    	 sqlSb.append(" AND (").append(secrectCol).append(" IS NULL OR ").append(secrectCol).append(" = ''  )");
		     }
			 
		}else {
			if(StringUtils.isNotBlank(deptIds) && StringUtils.isNotBlank(companyCol) && StringUtils.isNotBlank(deptCol) ) {
				sqlSb.append(" AND ( ").append(createUserCol).append(" = ").append(staffid).append(" OR ").append(companyCol).append(" IN (").append(deptIds).append(") OR ").append(deptCol).append(" IN (").append(deptIds).append(") )");
			}
			
			if(isSelf && StringUtils.isBlank(deptIds)) {
				sqlSb.append(" AND ").append(createUserCol).append(" = ").append(staffid);
			}
			
		}
		String sql = sqlSb.toString();
		return sql;
	}
	
	/**
	 * 拼接密级和知悉范围查询条件  通用方法--条件是ENTITY
	 * @param isUseSecrect  -- 是否使用密级   来源loginStaff.getCurrentOrg().getUseSecrect()
	 * @param isSelf        -- 是否添加限制创建人条件  true 是，false 否
	 * @param companyCol    -- 业务数据所属 公司列名
	 * @param deptCol       -- 业务数据所属 部门列名 
	 * @param createUserCol -- 业务数据创建用户 列名
	 * @param secrectCol    -- 业务数据密级主键 列名
	 * @param staffScopeCol -- 业务数据知悉范围 列名
	 * @param staffid       -- 当前登录用户主键   来源loginStaff.getStaffid()
	 * @param deptIds		-- 当前登录用户允许查看部门数据范围 来源 loginStaff.getDeptIds()
	 * @param secrectScopeIds  --当前登录查看密级数据范围  来源 loginStaff.getSecrectScopeIds()
	 * @return
	 * @throws Exception
	 */
	public static String concatSecrectSqlEntity(GeneralEntity entity) throws  Exception {
		StringBuffer sqlSb = new StringBuffer();
		if(entity.getIsUseSecrect() != null && entity.getIsUseSecrect() == 1) {
			//知悉范围查询
		if(entity.getAuthorityType()==1){
			
		}else{
		 sqlSb.append(" AND ( ( "+DataBaseSqlConfig.getWhereColumnInStr(entity.getStaffScopeCol(), entity.getStaffid().toString(),",")+" OR ")
		 .append(entity.getCreateUserCol()).append(" = "+entity.getStaffid());
		if(!entity.isSelf()) {
			sqlSb.append(" OR ").append(entity.getStaffScopeCol()).append(" IS NULL OR ").append(entity.getStaffScopeCol()).append(" = ''");
		}
		sqlSb.append(" ) ");
		 if(StringUtils.isNotBlank(entity.getDeptIds())) {
			 sqlSb.append(" OR ").append(entity.getCompanyCol()).append(" IN (").append(entity.getDeptIds()).append(") OR ").append(entity.getDeptCol()).append(" IN (").append(entity.getDeptIds()).append(")  ");
		 }
		 sqlSb.append(" ) ");
		}
			//密级查询
			 if(StringUtils.isNotBlank(entity.getSecrectCol())) {
				 sqlSb.append(" AND (").append(entity.getSecrectCol()).append(" IN (").append(entity.getSecrectScopeIds()).append(") OR ").append(entity.getSecrectCol()).append(" IS NULL OR ").append(entity.getSecrectCol()).append(" = ''  )");
		     }else {
		    	 sqlSb.append(" AND (").append(entity.getSecrectCol()).append(" IS NULL OR ").append(entity.getSecrectCol()).append(" = ''  )");
		     }
			 
		}else {
			if(entity.getAuthorityType()==0){
			if(StringUtils.isNotBlank(entity.getDeptIds()) && StringUtils.isNotBlank(entity.getCompanyCol()) && StringUtils.isNotBlank(entity.getDeptCol()) ) {
				sqlSb.append(" AND ( ").append(entity.getCreateUserCol()).append(" = ").append(entity.getStaffid()).append(" OR ").append(entity.getCompanyCol()).append(" IN (").append(entity.getDeptIds()).append(") OR ").append(entity.getDeptCol()).append(" IN (").append(entity.getDeptIds()).append(") )");
			}
			
			if(entity.isSelf() && StringUtils.isBlank(entity.getDeptIds())) {
				sqlSb.append(" AND ").append(entity.getCreateUserCol()).append(" = ").append(entity.getStaffid());
			}
		}
		}
		String sql = sqlSb.toString();
		return sql;
	}
	
	
	
	
	/**
	 * 拼接历史工作数据移交 查询条件sql
	 * @param createTimeCol 	--业务单据 创建时间列名
	 * @param deptCol			--业务单据 所属部门列名
	 * @param createStaffCol	--业务单据 创建用户列名
	 * @param workList			--工作移交记录集合 来源loginStaff.getWorkList();
	 * @return
	 * @throws Exception
	 */
	public static String concatTranseSql(String createTimeCol,String deptCol, String createStaffCol, List<TblTransferWorkUtils> workList )throws Exception {
		StringBuffer sqlSb = new StringBuffer();
		if(workList != null && workList.size() > 0) {
			for (TblTransferWorkUtils work : workList) {
				sqlSb.append(" OR (").append(createTimeCol).append(" >= "+DataBaseSqlConfig.getDateStrFormat(work.getTransfertime())+" AND ").append(deptCol).append(" IN ("+work.getTranorgidstrs()+") AND ").append(createStaffCol).append(" = "+work.getTransferstaffid()+")"); 
			}
		}
		String sql = sqlSb.toString();
		return sql;
	}
	
	
	
	
	
	
	
	

}
