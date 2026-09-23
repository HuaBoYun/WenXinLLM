package com.huabo.system.mapper;

import io.lettuce.core.dynamic.annotation.Param;

import java.math.BigDecimal;
import java.util.List;

import javax.transaction.xa.XAException;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.UpdateProvider;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hbfk.util.JsonBean;
import com.huabo.system.entity.TblSystemRight;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author huabo
 * @since 2022-05-22
 */
public interface TblSystemRightMapper extends BaseMapper<TblSystemRight> {

	@InsertProvider(method="insertTblManageRight",type=TblSystemRightMapperSqlConfig.class)
	@Options(useGeneratedKeys=true, keyProperty="id", keyColumn="ID")
	void insertTblManageRight(TblSystemRight right);

	@Select("select TSR.*,TSL.LEVELNAME AS SECRECTLEVELNAME FROM TBL_SYSTEM_RIGHT TSR LEFT JOIN TBL_SECRECT_LEVEL TSL ON TSR.SECRECTLEVELID = TSL.LEVELID where TSR.ID = #{id}")
	TblSystemRight findById(BigDecimal id) throws Exception;

	@UpdateProvider(method="updateTblSystemRight",type=TblSystemRightMapperSqlConfig.class)
	void updateTblSystemRight(TblSystemRight right) throws Exception;

    @SelectProvider(method="selectChildrenRightListByUser",type=TblSystemRightMapperSqlConfig.class)
	@Results({
    	@Result(column="ID",property="id"),
    	@Result(column="RIGHTNAME",property="name"),
    	@Result(column="ICON",property="icon"),
    	@Result(column="ISLINK",property="islink"),
    	@Result(column="PARENT",property="parent"),
    	@Result(column="SORT",property="sort"),
    	@Result(column="VISIBLE",property="visible"),
    	@Result(column="MODULETYPE",property="moduletype"),
    	@Result(column="PATH",property="path"),
    	@Result(column="PERMS",property="perms"),
    	@Result(column="TYPE",property="type"),
    })
	List<TblSystemRight> selectChildrenRightListByUser(BigDecimal rightId, BigDecimal staffid, BigDecimal orgid,String moduleType) throws Exception;

	@SelectProvider(method="selectAllRightListByCompanye",type=TblSystemRightMapperSqlConfig.class)
	@Results({
    	@Result(column="ID",property="id"),
    	@Result(column="RIGHTNAME",property="name"),
    	@Result(column="ICON",property="icon"),
    	@Result(column="PARENT",property="parent"),
    	@Result(column="SORT",property="sort"),
    	@Result(column="VISIBLE",property="visible"),
    	@Result(column="ISLINK",property="islink"),
    	@Result(column="PATH",property="path"),
    	@Result(column="PERMS",property="perms"),
    	@Result(column="TYPE",property="type"),
    	@Result(column="MODULETYPE",property="moduletype"),
    	@Result(column="SECRECTLEVELNAME",property="secrectLevelName"),
    	@Result(column="SECRECTLEVELID",property="secrectLevelId"),
    })
	List<TblSystemRight> selectAllRightListByCompanye(Integer type, BigDecimal orgid, String moduleType);

	@SelectProvider(method="selectAllRightListByFatherCompany",type=TblSystemRightMapperSqlConfig.class)
	@Results({
    	@Result(column="ID",property="id"),
    	@Result(column="RIGHTNAME",property="name"),
    	@Result(column="ICON",property="icon"),
    	@Result(column="PARENT",property="parent"),
    	@Result(column="SORT",property="sort"),
    	@Result(column="VISIBLE",property="visible"),
    	@Result(column="ISLINK",property="islink"),
    	@Result(column="PATH",property="path"),
    	@Result(column="PERMS",property="perms"),
    	@Result(column="TYPE",property="type"),
    	@Result(column="SECRECTLEVELID",property="secrectLevelId"),
    	@Result(column="SECRECTLEVELNAME",property="secrectLevelName"),
    })
	List<TblSystemRight> selectAllRightListByFatherCompany(Integer type, BigDecimal orgid, String moduleType, BigDecimal checkOrgId);

	@Insert("INSERT INTO TBL_SYSTEM_USER_RIGHT(RIGHTID,STAFFID) VALUES (#{id},#{staffid})")
	void saveUserRight(@Param("id")String id,@Param("staffid") BigDecimal staffId) throws Exception;

	@Delete("DELETE FROM TBL_SYSTEM_USER_RIGHT WHERE STAFFID = #{staffid}")
	void removeUserRigthAll(@Param("staffid") BigDecimal staffId) throws Exception;

	@Delete("DELETE FROM TBL_SYSTEM_ORG_RIGHT WHERE ORGID = #{orgId} AND RIGHTID IN (SELECT ID FROM TBL_SYSTEM_RIGHT WHERE MODULETYPE = #{moduleType}) ")
	void removeCompanyRigthAll(@Param("orgId")String orgId,@Param("moduleType") String moduleType) throws Exception;

	@Insert("INSERT INTO TBL_SYSTEM_ORG_RIGHT(RIGHTID,ORGID,RIGHTSTATUS) VALUES (#{id},#{orgId},1)")
	void saveCompanyIdRight(@Param("id")String id,@Param("orgId") String orgId) throws Exception;

	@Select("SELECT TSR.ID,TSR.COMPONENT,TSR.ICON,TSR.ISLINK,NVL(TSOR.RIGHTNAME,TSR.NAME) RIGHTNAME,TSR.PARENT,TSR.PATH,TSR.PERMS,TSR.SORT,TSR.TYPE,TSR.MODULETYPE,TSOR.RIGHTSTATUS FROM TBL_SYSTEM_RIGHT TSR LEFT JOIN TBL_SYSTEM_ORG_RIGHT TSOR ON TSR.ID = TSOR.ORGID WHERE TSR.ID = #{id}")
	@Results({
    	@Result(column="ID",property="id"),
    	@Result(column="COMPONENT",property="component"),
    	@Result(column="ICON",property="icon"),
    	@Result(column="ISLINK",property="islink"),
    	@Result(column="RIGHTNAME",property="name"),
    	@Result(column="PARENT",property="parent"),
    	@Result(column="PATH",property="path"),
    	@Result(column="PERMS",property="perms"),
    	@Result(column="SORT",property="sort"),
    	@Result(column="TYPE",property="type"),
    	@Result(column="MODULETYPE",property="moduletype"),
    	@Result(column="RIGHTSTATUS",property="visible"),
    })
	TblSystemRight findSettingRightById(@Param("id")BigDecimal id) throws Exception;

	@Select("SELECT TSR.*,TSL.LEVELNAME AS SECRECTLEVELNAME FROM TBL_SYSTEM_RIGHT TSR LEFT JOIN TBL_SECRECT_LEVEL TSL ON TSR.SECRECTLEVELID = TSL.LEVELID  WHERE PARENT = #{rightId} ORDER BY SORT ASC")
	List<TblSystemRight> selectAllRightList(@Param("rightId")BigDecimal rightId) throws Exception;

	@SelectProvider(method="selectRightIdStrByRight",type=TblSystemRightMapperSqlConfig.class)
	List<String> selectRightIdStrByRight(TblSystemRight right) throws Exception;

	@Select("SELECT TSR.*,TSL.LEVELNAME AS SECRECTLEVELNAME FROM TBL_SYSTEM_RIGHT TSR LEFT JOIN TBL_SECRECT_LEVEL TSL ON TSR.SECRECTLEVELID = TSL.LEVELID WHERE TSR.ID IN (${rightIdStr}) AND TSR.PARENT = #{rightId} ")
	List<TblSystemRight> selectAllRightListByChoice(@Param("rightId")BigDecimal rightId,@Param("rightIdStr") String rightIdStr) throws Exception;
	
	@Select("SELECT TSR.*,TSL.LEVELNAME AS SECRECTLEVELNAME FROM TBL_SYSTEM_RIGHT TSR LEFT JOIN TBL_SECRECT_LEVEL TSL ON TSR.SECRECTLEVELID = TSL.LEVELID WHERE TSR.TYPE = #{type} AND TSR.MODULETYPE = #{moduletype} ")
	List<TblSystemRight> selectAllRightListByModuleType(int type, String moduletype) throws Exception;


	@SelectProvider(method="selectChildrenRightListByRole",type=TblSystemRightMapperSqlConfig.class)
	@Results({
	    @Result(column="ID",property="id"),
	    @Result(column="RIGHTNAME",property="name"),
	    @Result(column="ICON",property="icon"),
	    @Result(column="PARENT",property="parent"),
	    @Result(column="SORT",property="sort"),
	    @Result(column="VISIBLE",property="visible"),
	    @Result(column="MODULETYPE",property="moduletype"),
	    @Result(column="ISLINK",property="islink"),
	    @Result(column="PATH",property="path"),
	    @Result(column="PERMS",property="perms"),
    	@Result(column="TYPE",property="type"),
    	@Result(column="SECRECTLEVELID",property="secrectLevelId"),
    	@Result(column="SECRECTLEVELNAME",property="secrectLevelName"),
	})
	List<TblSystemRight> selectChildrenRightListByRole(BigDecimal rightId, String roleIds, BigDecimal orgid,String moduleType) throws Exception;
	
	@Delete("DELETE FROM TBL_SYSTEM_ROLE_RIGHT WHERE ROLEID = #{roleId}")
	void removeRoleRigthAll(@Param("roleId") Integer roleId) throws Exception;
	
	
	@Delete("DELETE FROM TBL_SYSTEM_ROLE_RIGHT "
			+ " WHERE ROLEID = #{roleId} "
			+ " AND RIGHTID IN (SELECT ID FROM TBL_SYSTEM_RIGHT WHERE MODULETYPE=#{moduleType}) ")
	void removeRoleRigthByModuleType(@Param("roleId") BigDecimal roleId,@Param("moduleType") String moduleType) throws Exception;
	
	@Insert("INSERT INTO TBL_SYSTEM_ROLE_RIGHT(RIGHTID,ROLEID) VALUES (#{id},#{roleId})")
	void saveRoleRight(@Param("id")String id,@Param("roleId") BigDecimal roleId) throws Exception;

	@SelectProvider(method="selectFatherRightIdStrByRight",type=TblSystemRightMapperSqlConfig.class)
	List<String> selectFatherRightIdStrByRight(String rightId, String allRightIds) throws Exception;

	@Select("SELECT count(*) FROM TBL_SYSTEM_RIGHT WHERE PARENT = #{rightId} ")
	Integer selectChildrenCount(BigDecimal rightId);
	
	@Delete("DELETE FROM TBL_SYSTEM_ORG_RIGHT WHERE RIGHTID = #{rightId}")
	void deleteOrgRIght(BigDecimal rightId);
	@Delete("DELETE FROM TBL_SYSTEM_ROLE_RIGHT WHERE RIGHTID = #{rightId}")
	void deleteRoleRIght(BigDecimal rightId);
	@Delete("DELETE FROM TBL_SYSTEM_RIGHT WHERE ID = #{rightId}")
	void deleteSystemRIght(BigDecimal rightId);

	@SelectProvider(method="selectRoleRigetListByType",type=TblSystemRightMapperSqlConfig.class)
	@Results({
	    @Result(column="ID",property="id"),
	    @Result(column="RIGHTNAME",property="name"),
	    @Result(column="ICON",property="icon"),
	    @Result(column="PARENT",property="parent"),
	    @Result(column="SORT",property="sort"),
	    @Result(column="VISIBLE",property="visible"),
	    @Result(column="MODULETYPE",property="moduletype"),
	    @Result(column="ISLINK",property="islink"),
	    @Result(column="PATH",property="path"),
	    @Result(column="PERMS",property="perms"),
    	@Result(column="TYPE",property="type"),
    	@Result(column="SECRECTLEVELID",property="secrectLevelId"),
    	@Result(column="LEVELNAME",property="secrectLevelName"),
	})
	List<TblSystemRight> selectRoleRigetListByType(String username,String roleIdStrs, BigDecimal orgid, String moduleType,Integer type) throws Exception;


	@Update("UPDATE TBL_SYSTEM_RIGHT SET visible = #{visible} WHERE ID = #{rightId}")
	void updateSystemRightVisible(@Param("rightId")BigDecimal rightId,@Param("visible") Integer visible) throws Exception;

	
	@Select("SELECT COUNT(*) FROM TBL_SYSTEM_ORG_RIGHT WHERE RIGHTID = #{rightId} AND ORGID = #{checkOrgid}")
	Integer judgeGrantRightByOrgid(@Param("rightId")BigDecimal rightId,@Param("checkOrgid") BigDecimal checkOrgid) throws Exception;

	@Select("SELECT COUNT(*) FROM TBL_SYSTEM_ROLE_RIGHT WHERE RIGHTID = #{rightId} AND ROLEID = #{roleId}")
	Integer judgeGrantRightByRoleId(@Param("rightId")BigDecimal rightId,@Param("roleId") BigDecimal roleId) throws Exception;

	@Select("SELECT * FROM TBL_SYSTEM_RIGHT WHERE ID in (${rightIds})")
	List<TblSystemRight> getRightNamesList(@Param("rightIds") String rightIds);

	@SelectProvider(method="selectChildrenRightListByRoleType",type=TblSystemRightMapperSqlConfig.class)
	@Results({
	    @Result(column="ID",property="id"),
	    @Result(column="RIGHTNAME",property="name"),
	    @Result(column="ICON",property="icon"),
	    @Result(column="PARENT",property="parent"),
	    @Result(column="SORT",property="sort"),
	    @Result(column="VISIBLE",property="visible"),
	    @Result(column="MODULETYPE",property="moduletype"),
	    @Result(column="ISLINK",property="islink"),
	    @Result(column="TYPE",property="type"),
	    @Result(column="PATH",property="path"),
	    @Result(column="PERMS",property="perms"),
    	@Result(column="TYPE",property="type"),
	})
	List<TblSystemRight> selectChildrenRightListByRoleType(BigDecimal rightId, String roleId, BigDecimal orgid,
			String moduleType) throws Exception;

	@SelectProvider(method="selectAllRightIdListByRoleId",type=TblSystemRightMapperSqlConfig.class)
	List<BigDecimal> selectAllRightIdListByRoleId(Integer type, BigDecimal roleId, String moduleType) throws Exception;

	@Select("SELECT NAME FROM TBL_SYSTEM_RIGHT WHERE ID in (${rightIds})")
	List<String> selectNameListByIds(@Param("rightIds")String rightIds) throws Exception;

}
