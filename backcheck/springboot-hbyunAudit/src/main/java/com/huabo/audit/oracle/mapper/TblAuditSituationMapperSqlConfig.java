package com.huabo.audit.oracle.mapper;

import org.apache.commons.lang.StringUtils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.DateUtil;
import com.hbfk.util.database.GeneralSQLConcatConfig;
import com.huabo.audit.oracle.entity.TblAuditOption;
import com.huabo.audit.oracle.entity.TblAuditSituationEntity;

public class TblAuditSituationMapperSqlConfig {
	
	public String selectPageInfoList(Page<TblAuditSituationEntity> page, String year, TblStaffUtil loginStaff) throws Exception {
		StringBuffer sqlSb = new StringBuffer("SELECT * FROM TBL_AUDIT_SITUATION WHERE 1=1");
		
		if (StringUtils.isNotBlank(year)) {
        	Integer yearInt = Integer.valueOf(year);
            yearInt = yearInt-1;
            sqlSb.append("AND SITUATIONYEAR LIKE '%").append(yearInt).append("%'");
        }
		
		sqlSb.append(GeneralSQLConcatConfig.concatSecrectSql(loginStaff.getCurrentOrg().getUseSecrect(), false, null, null, "CREATESTAFFID", "SECRECTLEVELID", "STAFFSCOPEIDS", loginStaff.getStaffid(), loginStaff.getDeptIds(), loginStaff.getSecrectScopeIds()));

        //倒序
        sqlSb.append(" ORDER BY ID DESC");
		
		String sql = sqlSb.toString();
		return sql;
	}
	
}
