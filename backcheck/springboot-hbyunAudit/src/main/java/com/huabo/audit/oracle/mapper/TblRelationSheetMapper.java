package com.huabo.audit.oracle.mapper;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.huabo.audit.oracle.entity.TblRelationSheet;

import tk.mybatis.mapper.common.Mapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author LHP
 * @since 2023-11-24
 */
public interface TblRelationSheetMapper extends Mapper<TblRelationSheet> {

	/**
	 * 组织用户中间关系表 根据 业务表单主键 删除所有组织信息
	 * @param formId  业务表单主键
	 * @param formtype  业务表单类型
	 * @param formcol	业务单据关联列类型
	 * @param objType	关系表关联类型 组织 or 用户
	 */
	@Delete("DELETE FROM TBL_RELATION_SHEET WHERE FORMID = #{formId} AND OBJTYPE = #{objType} AND FORMTYPE = #{formtype} AND FORMCOL = #{formcol}")
	void deleteRelation(@Param("formId") String formId,@Param("formtype") String formtype,@Param("formcol") String formcol,@Param("objType") String objType);

	/**
	 * 通过业务表单 获取 指定用户 或组织 的主键信息
	  * @param formId  业务表单主键
	 * @param formtype  业务表单类型
	 * @param formcol	业务单据关联列类型
	 * @param objType	关系表关联类型 组织 or 用户
	 * @return 组织 或 用户主键
	 * @throws Exception
	 */
	@Select("SELECT OBJECTID FROM TBL_RELATION_SHEET WHERE FORMID = #{formId} AND OBJTYPE = #{objType} AND FORMTYPE = #{formtype} AND FORMCOL = #{formcol} ORDER BY SORT DESC ")
	List<String> selectObjectIdListByForm(@Param("formId") String formId,@Param("formtype") String formtype,@Param("formcol") String formcol,@Param("objType") String objType) throws Exception;

	/**
	 * 根据组织主键逗号拼接的字符串 获取所有的组织名称
	 * @param orgIds  主键拼接的字符串
	 * @return  List<String> orgName 组织名称
	 * @throws Exception
	 */
	@Select("SELECT ORGNAME FROM TBL_ORGANIZATION WHERE ORGID IN (${orgIds})")
	List<String> selectOrgNameListByOrgIds(@Param("orgIds") String orgIds) throws Exception;

}
