package com.huabo.compliance.mysql.mapper;

import com.huabo.compliance.mysql.entity.TblSystemRightMySql;
import org.apache.ibatis.annotations.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author huabo
 * @since 2022-05-22
 */
public interface TblSystemRightMySqlMapper {

    @InsertProvider(method = "insertTblManageRight", type = TblSystemRightMapperSqlMySqlConfig.class)
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "ID")
    void insertTblManageRight(TblSystemRightMySql right);

    @Select("select * from TBL_SYSTEM_RIGHT where ID = #{id}")
    TblSystemRightMySql findById(BigDecimal id) throws Exception;

    @UpdateProvider(method = "updateTblSystemRight", type = TblSystemRightMapperSqlMySqlConfig.class)
    void updateTblSystemRight(TblSystemRightMySql right) throws Exception;

    @SelectProvider(method = "selectChildrenRightListByUser", type = TblSystemRightMapperSqlMySqlConfig.class)
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
    List<TblSystemRightMySql> selectChildrenRightListByUser(Integer rightId, BigDecimal staffid, BigDecimal orgid, String moduleType) throws Exception;

    @SelectProvider(method="selectAllRightListByCompanye",type=TblSystemRightMapperSqlMySqlConfig.class)
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
    })
    List<TblSystemRightMySql> selectAllRightListByCompanye(Integer rightId, BigDecimal orgid, String moduleType);

    @SelectProvider(method = "selectAllRightListByFatherCompany", type = TblSystemRightMapperSqlMySqlConfig.class)
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
    })
    List<TblSystemRightMySql> selectAllRightListByFatherCompany(Integer rightId, Integer orgid, String moduleType);

    @Insert("INSERT INTO TBL_SYSTEM_USER_RIGHT(RIGHTID,STAFFID) VALUES (#{id},#{staffId})")
    void saveUserRight(@Param("id") String id, @Param("staffId") Integer staffId) throws Exception;

    @Delete("DELETE FROM TBL_SYSTEM_USER_RIGHT WHERE STAFFID = #{staffId}")
    void removeUserRigthAll(@Param("staffId") Integer staffId) throws Exception;

    @Delete("DELETE FROM TBL_SYSTEM_ORG_RIGHT WHERE ORGID = #{orgId} AND  RIGHTID IN (SELECT ID FROM TBL_SYSTEM_RIGHT WHERE MODULETYPE = #{moduleType})")
    void removeCompanyRigthAll(@Param("orgId")String orgId,@Param("moduleType") String moduleType) throws Exception;

    @Insert("INSERT INTO TBL_SYSTEM_ORG_RIGHT(RIGHTID,ORGID,RIGHTSTATUS) VALUES (#{id},#{orgId},1)")
    void saveCompanyIdRight(@Param("id") String id, @Param("orgId") String orgId) throws Exception;

    @Select("SELECT TSR.ID,TSR.COMPONENT,TSR.ICON,TSR.ISLINK,IFNULL(TSOR.RIGHTNAME,TSR.NAME) RIGHTNAME,TSR.PARENT,TSR.PATH,TSR.PERMS,TSR.SORT,TSR.TYPE,TSR.MODULETYPE,TSOR.RIGHTSTATUS FROM TBL_SYSTEM_RIGHT TSR LEFT JOIN TBL_SYSTEM_ORG_RIGHT TSOR ON TSR.ID = TSOR.ORGID WHERE TSR.ID = #{id}")
    @Results({
            @Result(column = "ID", property = "id"),
            @Result(column = "COMPONENT", property = "component"),
            @Result(column = "ICON", property = "icon"),
            @Result(column = "ISLINK", property = "islink"),
            @Result(column = "RIGHTNAME", property = "name"),
            @Result(column = "PARENT", property = "parent"),
            @Result(column = "PATH", property = "path"),
            @Result(column = "PERMS", property = "perms"),
            @Result(column = "SORT", property = "sort"),
            @Result(column = "TYPE", property = "type"),
            @Result(column = "MODULETYPE", property = "moduletype"),
            @Result(column = "RIGHTSTATUS", property = "visible"),
    })
    TblSystemRightMySql findSettingRightById(@Param("id") BigDecimal id) throws Exception;

    @Select("SELECT * FROM TBL_SYSTEM_RIGHT WHERE PARENT = #{rightId} ORDER BY SORT ASC")
    List<TblSystemRightMySql> selectAllRightList(@Param("rightId") Integer rightId) throws Exception;

    @SelectProvider(method = "selectRightIdStrByRight", type = TblSystemRightMapperSqlMySqlConfig.class)
    String selectRightIdStrByRight(TblSystemRightMySql right) throws Exception;

    @Select("SELECT * FROM TBL_SYSTEM_RIGHT WHERE ID IN (${rightIdStr}) AND PARENT = #{rightId} ")
    List<TblSystemRightMySql> selectAllRightListByChoice(@Param("rightId") Integer rightId, @Param("rightIdStr") String rightIdStr) throws Exception;


    @SelectProvider(method = "selectChildrenRightListByRole", type = TblSystemRightMapperSqlMySqlConfig.class)
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
	})
    List<TblSystemRightMySql> selectChildrenRightListByRole(Integer rightId, String roleIds, BigDecimal orgid, String moduleType) throws Exception;

    @Delete("DELETE FROM TBL_SYSTEM_ROLE_RIGHT WHERE ROLEID = #{roleId}")
    void removeRoleRigthAll(@Param("roleId") Integer roleId) throws Exception;

    @Delete("DELETE FROM TBL_SYSTEM_ROLE_RIGHT "
            + " WHERE ROLEID = #{roleId} "
            + " AND RIGHTID IN (SELECT ID FROM TBL_SYSTEM_RIGHT WHERE MODULETYPE=#{moduleType}) ")
    void removeRoleRigthByModuleType(@Param("roleId") Integer roleId,@Param("moduleType") String moduleType) throws Exception;

    @Insert("INSERT INTO TBL_SYSTEM_ROLE_RIGHT(RIGHTID,ROLEID) VALUES (#{id},#{roleId})")
    void saveRoleRight(@Param("id") String id, @Param("roleId") Integer roleId) throws Exception;

    @SelectProvider(method = "selectFatherRightIdStrByRight", type = TblSystemRightMapperSqlMySqlConfig.class)
    String selectFatherRightIdStrByRight(String rightId) throws Exception;

    @Select("SELECT count(*) FROM TBL_SYSTEM_RIGHT WHERE PARENT = #{rightId} ")
    Integer selectChildrenCount(BigDecimal rightId);

    @Delete("DELETE FROM TBL_SYSTEM_ORG_RIGHT WHERE RIGHTID = #{rightId}")
    void deleteOrgRIght(BigDecimal rightId);

    @Delete("DELETE FROM TBL_SYSTEM_ROLE_RIGHT WHERE RIGHTID = #{rightId}")
    void deleteRoleRIght(BigDecimal rightId);

    @Delete("DELETE FROM TBL_SYSTEM_RIGHT WHERE ID = #{rightId}")
    void deleteSystemRIght(BigDecimal rightId);

    @SelectProvider(method = "selectRoleRigetListByType", type = TblSystemRightMapperSqlMySqlConfig.class)
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
	})
    List<TblSystemRightMySql> selectRoleRigetListByType(String roleIdStrs, BigDecimal orgid, String moduleType, Integer type) throws Exception;

    @SelectProvider(method = "selectChildrenRightListByRoleType", type = TblSystemRightMapperSqlMySqlConfig.class)
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
    List<TblSystemRightMySql> selectChildrenRightListByRoleType(Integer rightId, String roleId, BigDecimal orgid,
                                                           String moduleType, Integer type) throws Exception;

    @Update("UPDATE TBL_SYSTEM_RIGHT SET visible = #{visible} WHERE ID = #{rightId}")
    void updateSystemRightVisible(@Param("rightId") Integer rightId, @Param("visible") Integer visible) throws Exception;


    @Select("SELECT COUNT(*) FROM TBL_SYSTEM_ORG_RIGHT WHERE RIGHTID = #{rightId} AND ORGID = #{checkOrgid}")
    Integer judgeGrantRightByOrgid(@Param("rightId") BigDecimal rightId, @Param("checkOrgid") Integer checkOrgid) throws Exception;

    @Select("SELECT COUNT(*) FROM TBL_SYSTEM_ROLE_RIGHT WHERE RIGHTID = #{rightId} AND ROLEID = #{roleId}")
    Integer judgeGrantRightByRoleId(@Param("rightId") BigDecimal rightId, @Param("roleId") Integer roleId) throws Exception;

}
