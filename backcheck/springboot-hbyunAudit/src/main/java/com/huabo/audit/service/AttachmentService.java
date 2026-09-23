package com.huabo.audit.service;

import com.hbfk.util.JsonBean;

public interface AttachmentService {

	JsonBean dgfjlistPageList(String token, Integer pageNumber, Integer pageSize,String attname) throws Exception;
	  
}
