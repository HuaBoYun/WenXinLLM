package com.huabo.audit.service;

import com.hbfk.util.JsonBean;
import com.huabo.audit.oracle.entity.TblNbsjTypeOf;

public interface TblNbsjTypeOfService{
	
	public JsonBean findAllList(String token, Integer parentId,Integer pageNumber,Integer pageSize,String auditType) throws Exception;

	public JsonBean saveNbsjTypeOf(TblNbsjTypeOf nbsjTypeOf, String token)throws Exception;

	public JsonBean modifyNbsjTypeOf(String token,TblNbsjTypeOf nbsjTypeOf)throws Exception;

	public JsonBean removeNbsjTypeOf(String typeId, String token)throws Exception;

	public JsonBean SelectNbsjType(String typeid,String token) throws Exception;

	
}
