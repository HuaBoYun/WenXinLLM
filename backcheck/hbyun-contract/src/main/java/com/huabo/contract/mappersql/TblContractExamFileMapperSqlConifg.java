package com.huabo.contract.mappersql;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hbfk.util.PageInfo;
import com.huabo.contract.entity.TblContractExamFile;

public class TblContractExamFileMapperSqlConifg {

	public String findFileListByContractIdPageInfo(IPage<TblContractExamFile> page,TblContractExamFile tca) throws Exception {
		StringBuffer sqlSb = new StringBuffer("SELECT TAC.ID,TAC.FILENAME,TAC.FILEPATH,TAC.FILESIZE,TAC.FILETYPE,TAC.FILESTATUS,TAC.CONSTRACTID,TAC.UPLOADTIME,TAC.UPLOADER,TAC.uploaderName FROM TBL_CONTRACT_EXAMFILE TAC   WHERE TAC.CONSTRACTID = " + tca.getConstractId());
		if (tca.getFileName() != null && !"".equals(tca.getFileName())) {
			sqlSb.append(" AND TAC.FILENAME LIKE '%" + tca.getFileName() + "%'");
		}
		sqlSb.append(" ORDER BY TAC.UPLOADER");
		String sql = sqlSb.toString();
		return sql;
	}

}
