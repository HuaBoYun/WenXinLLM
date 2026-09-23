package com.huabo.compliance.service;

import com.hbfk.util.JsonBean;

import java.math.BigDecimal;

public interface TblAutonoNumberService {
	
	JsonBean findFlowNextId(String tblName, String column, String orgCol, Integer NoId,
			String chChoiceCol, String choiceVal, String bjf,String token) throws Exception;

	String getNewCodeByHyZsk(BigDecimal orgid, Integer noId, String parentId, String parentTblName, String parentIdCol,
							 String parentFatherCol, String parentNumberCol, String ancestorsNo, String chilTblName,
							 String chilNumberCol, String chilOrgCol, String chChoiceCol, String choiceVal, String bjf, String token) throws Exception;
	
	/**
	 * 此方法 适用于 一节点所在的表存在组织Id 并且 在该一级节点下的二级、三级节点等等所在的表，都存在一级节点的主键列，保持上下级
	 * 满足上述两个条件，节点都存在于一张表也可使用此方法 适用于 内控合规 测试模板
	 * 
	 * 传入参数说明 chTblName ---- 插入子节点所在的表 chNumberCol --- 插入子节点所在的表 编号的列名 chirldIdCol
	 * ---- 子节点表中一级节点的父列名 chOrgCol --- 编号所在的组织的列名 此处无用 orgid ----- 组织ID noId ------
	 * TBL_AUTONO_INFO的主键标识 列入 流程编号就是3 parentTblName --- 父节点所在的表 parentIdCol ----
	 * 父节点主键ID的列名 parentId ----- 父节点的主键值 parentNumberCol --- 父节点编号的 列名
	 * grandFatherTblName ---- 一级节点所在的表名 grandFatherIdCol ----- 一级节点主键ID 的列名
	 * grandFatherOrgCol ----- 一级节点所在的组织
	 */
	String findNumberLevelNextId(String chTblName, String chNumberCol, String chOrgCol, BigDecimal orgid, Integer noId,
			String parentTblName, String parentIdCol, String parentId, String grandFatherTblName,
			String grandFatherIdCol, String grandFatherOrgCol, String chirldIdCol, String parentNumberCol,String token)
			throws Exception;

	
	/**
	 * 此方法 根据父级的组织ID，查询该组织下的所有父级ID,在根据父级ID 查询出需要插入信息的最大编号
	 * 适用于，插入的子级信息无法依靠组织划分需要依靠父级划分。子级编号与父级保持上下级关系， 只要上级编号存在组织信息，就可查询下级编号。
	 * 
	 * chidNumberCol ---- 子表中编号的列名 chilTblName ---- 子表的表名 chilParentCol ----
	 * 子表中父级列的列名 parentIdCol ---- 父表主键ID的列名 parentTblName ---- 父表的表名 parnetOrgCol
	 * ---- 父表中组织的列名 orgid ---- 组织ID noId ---- 编号信息表对应的Id middleTblname ---- 中间表表名
	 * middleChilCol ---- 中间表子列的列名 middleParentCol ---- 中间表父列的列名
	 * 
	 * @param middleNumberCol
	 * @param middleIdCol
	 */
	String findRootNumberByParentIdLevel(String chilNumberCol, String chilTblName, String chilParentCol,
			String parentIdCol, String parentTblName, String parnetOrgCol, String parentNumberCol, String parentId,
			BigDecimal orgid, Integer noId, String middleTblname, String middleChilCol, String middleParentCol,
			String middleIdCol, String middleNumberCol,String token) throws Exception;
}
