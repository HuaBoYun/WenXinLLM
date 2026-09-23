package com.huabo.audit.service;

import java.math.BigDecimal;
import java.util.List;

import com.hbfk.util.JsonBean;
import com.hbfk.util.Tree;
import com.huabo.audit.oracle.entity.TblOrganization;

public interface TblOrganizaService {

	JsonBean getOrgTreeListByAuditObj(String token, BigDecimal nodeId) throws Exception;
	
 	List<TblOrganization> getHyOrgTree();
 	
	TblOrganization getHY();
	
	List<Tree> getTrees(BigDecimal nodeId);
	
	List<Tree> getNodeAlls(BigDecimal nodeId);

	 String selectNamesByids(String ids) throws Exception;
     
	  String selectNameByids(BigDecimal id) throws Exception;

}
