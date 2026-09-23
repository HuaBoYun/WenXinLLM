package com.huabo.contract.mappersql;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hbfk.util.PageInfo;
import com.huabo.contract.entity.TblContractContentPdf;

public class TblContractContentPdfMapperSqlConifg {

	public String findFileListByContractIdPageInfo(IPage<TblContractContentPdf> page,TblContractContentPdf tca) throws Exception {
		StringBuffer sqlSb = new StringBuffer("SELECT TAC.CONTENTPDFID,TAC.CONTENTPDFNAME,TAC.CONTENTPDFPATH,TAC.CONTENTPDFSIZE,TAC.CONTENTPDFTYPE,TAC.CONTENTPDFSTATUS,TAC.CONSTRACTID,TAC.UPLOADTIME,TAC.UPLOADER,TS.REALNAME,TAC.OAATTID FROM TBL_CONTRACT_CONTENTPDF TAC LEFT JOIN TBL_STAFF TS ON TAC.UPLOADER = TS.STAFFID WHERE TAC.CONSTRACTID = " + tca.getConstractId());
		if (tca.getContentPdfName() != null && !"".equals(tca.getContentPdfName())) {
			sqlSb.append(" AND TAC.CONTENTPDFNAME LIKE '%" + tca.getContentPdfName() + "%'");
		}
		sqlSb.append(" ORDER BY TAC.UPLOADER");
		String sql = sqlSb.toString();
		return sql;
	}

}
