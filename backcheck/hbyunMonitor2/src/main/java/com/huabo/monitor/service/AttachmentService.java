package com.huabo.monitor.service;

import com.hbfk.util.JsonBean;
import com.huabo.monitor.entity.TblAttachment;

public interface AttachmentService {

	JsonBean dgfjlistPageList(String token, Integer pageNumber, Integer pageSize,String attname) throws Exception;

	TblAttachment findById(String id);

	void modify(TblAttachment a);

}
