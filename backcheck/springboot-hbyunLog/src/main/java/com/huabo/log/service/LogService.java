package com.huabo.log.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.huabo.log.db.entity.UserLoginLog;
import com.huabo.log.db.entity.UserRequestLog;
import com.huabo.log.vo.LogPageReq;
import com.huabo.log.vo.OperationLog;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface LogService {
    void saveBusLog(OperationLog operationLog);
    Page<UserRequestLog> list(LogPageReq req, boolean errorFlag);

    Page<UserRequestLog> listError(LogPageReq req);

    void export(HttpServletResponse response, LogPageReq req) throws Exception;

    void delBatch(List<String> ids);

    Page<UserLoginLog> listLogin(LogPageReq req);

    void delLogBatch(List<String> ids);
    
	void exportError(HttpServletResponse response, LogPageReq req) throws Exception;
	
	void exportlistLogin(LogPageReq req, HttpServletResponse response) throws Exception;
	
	UserRequestLog detail(long id) throws Exception;
}
