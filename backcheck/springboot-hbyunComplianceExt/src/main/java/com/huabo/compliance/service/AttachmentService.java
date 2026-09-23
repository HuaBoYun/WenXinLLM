package com.huabo.compliance.service;

import com.hbfk.util.JsonBean;
import com.huabo.compliance.entity.TblAttachment;

public interface AttachmentService {

	JsonBean dgfjlistPageList(String token, Integer pageNumber, Integer pageSize,String attname) throws Exception;

	TblAttachment findById(String id);

	void modify(TblAttachment a);

}
