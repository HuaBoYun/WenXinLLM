package com.huabo.audit.service;

import com.hbfk.util.JsonBean;
import com.huabo.audit.oracle.entity.TblNbsjQuestionType;
import com.huabo.audit.oracle.entity.TblNbsjStatType;

public interface TblNbsjQuestionTypeService {
	
	//==审计问题类型
	public JsonBean getNbsjQuestionTypeListPage(String token, Integer pageNumber, Integer pageSize) throws Exception;
	
	public JsonBean getNbsjQuestionType(String typeid,String token) throws Exception;
	
	public JsonBean saveNbsjQuestionType(TblNbsjQuestionType type,String token) throws Exception;
	
	public JsonBean delNbsjType(String typeid,String token) throws Exception;
	
	//==统计类型维护
	public JsonBean getNbsjStatTypeListPage(String token, Integer pageNumber, Integer pageSize) throws Exception;
	
	public JsonBean getNbsjStatType(String typeid,String token) throws Exception;
	
	public JsonBean saveNbsjStatType(TblNbsjStatType type,String token) throws Exception;
	
	public JsonBean delNbsjStatType(String typeid,String token) throws Exception;
	
	//==
	public JsonBean getNbsjQuestionTypeList(String token) throws Exception;

	
}
