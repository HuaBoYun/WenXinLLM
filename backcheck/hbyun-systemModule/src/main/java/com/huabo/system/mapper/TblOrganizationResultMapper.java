package com.huabo.system.mapper;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.system.entity.TblOrganization;
import com.huabo.system.vo.param.TblOrganizationQueryParam;
import com.huabo.system.vo.result.TblOrganizationResult;


/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author huabo
 * @since 2021-10-19
 */
public interface TblOrganizationResultMapper extends BaseMapper<TblOrganizationResult> {

	@SelectProvider(method = "selectRootTreeInfoById",type = TblOrganizationResultMapperSqlConifg.class)
	TblOrganizationResult selectRootTreeInfoById(TblOrganizationQueryParam query) throws Exception;

	@SelectProvider(method = "selectTreeListInfoByFatherId",type = TblOrganizationResultMapperSqlConifg.class)
	List<TblOrganizationResult> selectTreeListInfoByFatherId(TblOrganizationQueryParam query) throws Exception;

	@SelectProvider(method = "selectTreeListInfoByOrgId",type = TblOrganizationResultMapperSqlConifg.class)
	List<TblOrganizationResult> selectTreeListInfoByOrgId(TblOrganizationQueryParam query) throws Exception;

	@SelectProvider(method = "selectAllOrgIdByOrgName",type = TblOrganizationResultMapperSqlConifg.class)
	List<String> selectAllOrgIdByOrgName(TblOrganizationQueryParam query) throws Exception;
	
	@SelectProvider(method = "selectOrgIdsByallOrgIdStrs",type = TblOrganizationResultMapperSqlConifg.class)
	List<String> selectOrgIdsByallOrgIdStrs(List<String> orgStrIds,  List<String> allOrgIdStrs) throws Exception;

	@SelectProvider(method = "selectAllListByOrgtypeAndOrgIds",type = TblOrganizationResultMapperSqlConifg.class)
	List<TblOrganizationResult> selectAllListByOrgtypeAndOrgIds(int orgtype, String allOrgIdStrs,TblOrganizationQueryParam query) throws Exception;

	@SelectProvider(method = "selectChilrenListByOrgIdScope",type = TblOrganizationResultMapperSqlConifg.class)
	List<TblOrganizationResult> selectChilrenListByOrgIdScope(BigDecimal orgid, String orgIdsSql, TblOrganizationQueryParam query) throws Exception;

	@SelectProvider(method = "selectChildrenCompanyIds",type = TblOrganizationResultMapperSqlConifg.class)
	List<String> selectChildrenCompanyIds(String choiceIds, Integer status) throws Exception;

	@SelectProvider(method = "selectListByPageInfo",type = TblOrganizationResultMapperSqlConifg.class)
	List<TblOrganizationResult> selectListByPageInfo(TblOrganizationQueryParam query) throws Exception;

	@SelectProvider(method = "selectChildrenDepartmentIds",type = TblOrganizationResultMapperSqlConifg.class)
	List<String> selectChildrenDepartmentIds(String choiceIds, Integer status) throws Exception;

	@SelectProvider(method = "selectAllChildrenIds",type = TblOrganizationResultMapperSqlConifg.class)
	List<String> selectAllChildrenIds(String orgId, Integer orgtype) throws Exception;

	@SelectProvider(method = "selectRootOrgListByOrgIds",type = TblOrganizationResultMapperSqlConifg.class)
	List<TblOrganizationResult> selectRootOrgListByOrgIds(List<String> allOrgIdList) throws Exception;

	@SelectProvider(method = "selectAllOrgIdTreeByOrgName",type = TblOrganizationResultMapperSqlConifg.class)
	List<String> selectAllOrgIdTreeByOrgName(TblOrganizationQueryParam query) throws Exception;

	@SelectProvider(method = "selectOrgListByIds",type = TblOrganizationResultMapperSqlConifg.class)
	List<TblOrganizationResult> selectOrgListByIds(List<String> enList) throws Exception;

	@Select("SELECT ORGANIZATIONTREES FROM TBL_ORGANIZATION WHERE ORGID = #{orgid}")
	String selectOrgTreeByorgId(@Param("orgid") BigDecimal orgid) throws Exception;

	@SelectProvider(method = "selectAllCompanyChildrenIdsByTrees",type = TblOrganizationResultMapperSqlConifg.class)
	List<String> selectAllCompanyChildrenIdsByTrees(String orgId, Integer status) throws Exception;

	@SelectProvider(method = "selectAllChildrenIdsByTrees",type = TblOrganizationResultMapperSqlConifg.class)
	List<String> selectAllChildrenIdsByTrees(String orgId, Integer status) throws Exception;

}
