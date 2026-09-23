package com.huabo.system.mapper;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.system.entity.TblRole;

/**
 * <p>
 * 角色表管理
rid:主键ID,自动增长；
rname:角色名称；
rdesc:角色描述；
rstatus:角色状态，是否启用 Mapper 接口
 * </p>
 *
 * @author huabo
 * @since 2021-10-20
 */
public interface TblRoleMapper extends BaseMapper<TblRole> {
	
	@Select("select * from TBL_ROLE WHERE RDESC = #{code}")
	TblRole geTblRoleCode(String code);
	
	@InsertProvider(method = "addRoleReturnId",type = TblRoleMapperSqlConfig.class)
	@Options(useGeneratedKeys=true, keyProperty="rid", keyColumn="RID")
	void addRoleReturnId(TblRole role);

	// 通过ridList 获取 List<TblRole>
	List<TblRole> findByRidList(@Param("ridList") List<String> ridList);



	@Results({
		@Result(column="RID",property="rid"),
		@Result(column="RNAME",property="rname"),
		@Result(column="RDESC",property="rdesc"),
		@Result(column="RSTATUS",property="rstatus"),
		@Result(column="PKYMORGID",property="pkYmOrgId"),
		@Result(column="PKYMROLEID",property="pkYmRoleId"),
		@Result(column="ORGNAME",property="orgName"),
	})
	@SelectProvider(method = "selectAllRoleListToYM", type = TblRoleMapperSqlConfig.class)
	List<TblRole> selectAllRoleListToYM(BigDecimal orgId) throws Exception;

	@Update("UPDATE TBL_ROLE SET PKYMROLEID = #{pkYmRoleId} WHERE RID = #{rid}")
	void updatePkYmbyRoleId(String pkYmRoleId, BigDecimal rid) throws Exception;

	@Results({
		@Result(column="RID",property="rid"),
		@Result(column="RNAME",property="rname"),
		@Result(column="RDESC",property="rdesc"),
		@Result(column="RSTATUS",property="rstatus"),
		@Result(column="PKYMORGID",property="pkYmOrgId"),
		@Result(column="PKYMROLEID",property="pkYmRoleId"),
		@Result(column="ORGNAME",property="orgName"),
	})
	@SelectProvider(method = "selectAllRoleInfoToYM", type = TblRoleMapperSqlConfig.class)
	TblRole selectAllRoleInfoToYM(BigDecimal rid) throws Exception;

	@Delete("DELETE FROM TBL_SYSTEM_DATA_RIGHT WHERE ROLEID = #{roleId}")
	void deleteDateRightInfoByRoleId(BigDecimal roleId) throws Exception;

	@Insert("INSERT INTO TBL_SYSTEM_DATA_RIGHT(ROLEID,ORGID,DEPTIDSTRS) VALUES(#{roleId},#{companyId},#{detpId}) ")
	void insertDataRightInfo(BigDecimal roleId, String companyId, String detpId)throws Exception;

	@Select("SELECT DEPTIDSTRS FROM TBL_SYSTEM_DATA_RIGHT WHERE ROLEID = #{roleId} AND ORGID = #{orgId}")
	String selectDateRightDeptIDByOrgId(BigDecimal roleId, String orgId) throws Exception;

	@Update("UPDATE TBL_SYSTEM_DATA_RIGHT SET DEPTIDSTRS = #{detpId} WHERE ROLEID = #{roleId} AND ORGID = #{companyId}")
	void updateDataRightInfo(BigDecimal roleId, String companyId, String detpId) throws Exception;

	@Delete("DELETE FROM TBL_SYSTEM_DATA_RIGHT WHERE ROLEID = #{roleId} AND ORGID = #{companyId}")
	void removeDateRightInfo(BigDecimal roleId, String companyId);

	@Select("select PKYMROLEID from TBL_ROLE WHERE RID = #{roleid}")
	String selectYmRoleIdByRoleId(String roleid) throws Exception;

	@Insert("INSERT INTO TBL_SYSTEM_ROLE_RIGHT(ROLEID,RIGHTID) SELECT #{rid},ID FROM TBL_SYSTEM_RIGHT")
	void inserRoleRightInfo(@Param("rid") BigDecimal rid) throws Exception;

	@Insert("INSERT INTO TBL_ORG_ROLE(ROLEID,ORGID) VALUES (#{rid},#{orgid})")
	void insertOrgRoleRelation(@Param("rid") BigDecimal rid,@Param("orgid") BigDecimal orgid) throws Exception;

	@Select("SELECT RNAME FROM TBL_ROLE WHERE RID IN (${roleIdStrs})")
	List<String> selectRoleNamesByIds(String roleIdStrs) throws Exception;


}
