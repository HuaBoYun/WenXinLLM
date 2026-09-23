package com.huabo.system.service;



import com.hbfk.util.JsonBean;
import com.huabo.system.entity.TblSystemImportLog;

public interface TblSystemImportLogService {

	void save(TblSystemImportLog imlog) throws Exception;

	JsonBean getSystemImportLogList(Integer pageNumber, Integer pageSize, String token, String createTime, Integer importType) throws Exception;

}
