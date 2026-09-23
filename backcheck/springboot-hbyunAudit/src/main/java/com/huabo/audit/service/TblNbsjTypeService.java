package com.huabo.audit.service;

import com.hbfk.util.JsonBean;
import com.huabo.audit.oracle.entity.TblNbsjType;

public interface TblNbsjTypeService{
	
	public JsonBean getNbsjTypeListForMerge(String token) throws Exception;
	
	public JsonBean   saveNbsjType(TblNbsjType type,String token) throws Exception;
	
	public JsonBean SelectNbsjType(String typeid,String token) throws Exception;
	
	public JsonBean  delNbsjType(String typeid,String token) throws Exception;

	public JsonBean getNbsjTypeListPage(String token, Integer pageNumber, Integer pageSize,String auditType) throws Exception;

	public JsonBean getNbsjTypeAllList(String token) throws Exception;
	
	
}
