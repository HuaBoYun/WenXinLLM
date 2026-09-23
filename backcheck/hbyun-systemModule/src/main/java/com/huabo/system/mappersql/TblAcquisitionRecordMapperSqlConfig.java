package com.huabo.system.mappersql;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hbfk.util.PageInfo;
import com.huabo.system.entity.TblAcctBook;
import com.huabo.system.entity.TblAcquisitionRecord;

public class TblAcquisitionRecordMapperSqlConfig {
	
	public String selectListByPageInfo(IPage<TblAcquisitionRecord> page, BigDecimal orgid) {
		StringBuffer sqlSb = new StringBuffer("SELECT RECORDID,RECORDYEAR,RECORDSTART,RECORDEND,RECORDTIME,F.REALNAME,O.ORGNAME,R.RETYPE,R.RECORDIP,R.RECORDMEMO "
				+ "FROM TBL_ACQUISITION_RECORD R LEFT JOIN TBL_STAFF F ON R.STAFFID = F.STAFFID LEFT JOIN TBL_ORGANIZATION O ON R.ORGID = O.ORGID WHERE R.ORGID IS NOT NULL AND R.ORGID = "+orgid);
		sqlSb.append(" ORDER BY R.RECORDSTART DESC ");
		String sql = sqlSb.toString();
		return sql;
	}
}
