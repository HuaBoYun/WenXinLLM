package com.huabo.audit.service;


import java.math.BigDecimal;

import com.hbfk.util.JsonBean;
import com.huabo.audit.oracle.entity.TblNbsjRefopm;

public interface TblNbsjRefopmService {
	
	
	public JsonBean saveOrupdate(TblNbsjRefopm refor,String token,BigDecimal solutionid)  throws Exception;
	public JsonBean delete(BigDecimal reformid)  throws Exception;
	
	
	
	public JsonBean getByid(String reid)  throws Exception;
	
	
	public JsonBean getAllReformNbsjByProject(TblNbsjRefopm re,Integer startIndex, Integer pageSize,BigDecimal projectid,String selectidIdsstr)  throws Exception;
	
	public JsonBean findTblReformBySheetIdAndSoultionid(String code,BigDecimal soultionid,Integer startIndex,Integer pageSize,TblNbsjRefopm re,String type,String token)  throws Exception;
	
	public JsonBean update(TblNbsjRefopm refor,String token,String attids,String type)  throws Exception;
	
	
	public JsonBean tjyz(BigDecimal soultionid,String token) throws Exception;
	
	public JsonBean getAttListByReformId(String token, BigDecimal reformid) throws Exception;
	public JsonBean delAttListByReformId(String token, BigDecimal attid) throws Exception;
	
	
	public JsonBean update(TblNbsjRefopm refor,String token,BigDecimal solutionid,String attids,String type,String zgproblem) throws Exception;
	
	public JsonBean pjtjyz(BigDecimal soultionid,String token) throws Exception;
	
	
	public JsonBean getAllReformNbsjBywxh(TblNbsjRefopm re,Integer startIndex, Integer pageSize)  throws Exception;
	
	
	public JsonBean getResultall(BigDecimal soultionid,String token,String reformid) throws Exception;
	
	
	public JsonBean selectNbsjReformByAll( BigDecimal auditorg,  String code,String projectname,Integer startIndex, Integer pageSize,String token)  throws Exception;
	
	public JsonBean getzgproblemlist(String token,BigDecimal reformid,String datatype) throws Exception;
	
	
	public JsonBean deletezgproblemlist(String token,BigDecimal problemid) throws Exception;
	
	
	
	//public JsonBean findTblReformBySoultionidWWC(String soultionid);
	//public JsonBean findTblReformBySoultionidWC(String soultionid);
	//public JsonBean findTblReformBySoultionidPJWC(String soultionid);
}
