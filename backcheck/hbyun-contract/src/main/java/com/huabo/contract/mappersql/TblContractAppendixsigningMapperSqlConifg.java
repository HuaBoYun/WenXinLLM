package com.huabo.contract.mappersql;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hbfk.util.PageInfo;
import com.huabo.contract.entity.TblContractAppendixsigning;

public class TblContractAppendixsigningMapperSqlConifg {

	public String findFileListByContractIdPageInfo(IPage<TblContractAppendixsigning> page,TblContractAppendixsigning tca) {
		StringBuffer sqlSb = new StringBuffer("SELECT TAC.SINGINGID,TAC.SINGINGNAME,TAC.SINGINGPATH,TAC.SINGINGSIZE,TAC.SINGINGTYPE,TAC.SINGINGSTATUS,TAC.CONSTRACTID,TAC.UPLOADTIME,TAC.UPLOADER,TS.REALNAME FROM TBL_CONTRACT_APPENDIXSIGNING TAC LEFT JOIN TBL_STAFF TS ON TAC.UPLOADER = TS.STAFFID WHERE TAC.CONSTRACTID = " + tca.getConstractId());
		if (tca.getSingingName() != null && !"".equals(tca.getSingingName())) {
			sqlSb.append(" AND TAC.SINGINGNAME LIKE '%" + tca.getSingingName() + "%'");
		}
		sqlSb.append(" ORDER BY TAC.UPLOADER ");
		String sql = sqlSb.toString();
		return sql;
	}

}
