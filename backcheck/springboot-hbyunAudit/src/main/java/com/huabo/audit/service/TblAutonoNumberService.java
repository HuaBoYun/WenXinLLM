package com.huabo.audit.service;

import com.hbfk.util.JsonBean;

import java.math.BigDecimal;

public interface TblAutonoNumberService {
	
	JsonBean findFlowNextId(String tblName, String column, String orgCol, BigDecimal NoId,
			String chChoiceCol, String choiceVal, String bjf,String token) throws Exception;
	
	
	
	/**
	 * 此方法 根据父级的组织ID，查询该组织下的所有父级ID,在根据父级ID 查询出需要插入信息的最大编号
	 * 适用于，插入的子级信息无法依靠组织划分需要依靠父级划分。子级的编号独立，不与父级编号产生上下级的关系， 只要上级编号存在组织信息，就可查询下级编号。
	 * 
	 * chidNumberCol ---- 子表中编号的列名 chilTblName ---- 子表的表名 chilParentCol ----
	 * 子表中父级列的列名 parentIdCol ---- 父表主键ID的列名 parentTblName ---- 父表的表名 parnetOrgCol
	 * ---- 父表中组织的列名 orgid ---- 组织ID noId ---- 编号信息表对应的Id middleTblname ---- 中间表表名
	 * middleChilCol ---- 中间表子列的列名 middleParentCol ---- 中间表父列的列名
	 */
	String findRootNumberByParentId(String chilNumberCol, String chilTblName, String chilParentCol, String parentIdCol,
			String parentTblName, String parnetOrgCol, String token, BigDecimal noId, String middleTblname,
			String middleChilCol, String middleParentCol,String type) throws Exception;
	
	
}
