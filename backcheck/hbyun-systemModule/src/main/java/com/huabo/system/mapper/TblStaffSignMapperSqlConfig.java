package com.huabo.system.mapper;

import org.apache.commons.lang.StringUtils;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.huabo.system.entity.TblStaffSign;

/**
 * <p>
 * 电子签名存储表 Mapper 接口
 * </p>
 *
 * @author lhp
 * @since 2024-12-02
 */
public class TblStaffSignMapperSqlConfig {
	
	public String selectListPage(TblStaffSign sign, IPage<TblStaffSign> page) throws Exception {
		StringBuffer sql = new StringBuffer("SELECT SIGNID,SIGNSTAFF,SIGNSTAFFNAME,CREATEDATE,CREATESTAFFNAME,UPDATEDATE,UPDATESTAFFNAME FROM TBL_STAFF_SIGN WHERE 1 = 1");
		
		if(StringUtils.isNotBlank(sign.getSignstaffname())) {
			sql.append(" AND SIGNSTAFFNAME LIKE '%").append(sign.getSignstaffname()).append("%'");
		}
		
		if(StringUtils.isNotBlank(sign.getCreatestaffname())) {
			sql.append(" AND CREATESTAFFNAME LIKE '%").append(sign.getCreatestaffname()).append("%'");
		}
		
		if(StringUtils.isNotBlank(sign.getUpdatestaffname())) {
			sql.append(" AND UPDATESTAFFNAME LIKE '%").append(sign.getUpdatestaffname()).append("%'");
		}
		
		sql.append(" ORDER BY SIGNSTAFF , CREATEDATE ASC");
		String s = sql.toString();
		return s;
	}
}
