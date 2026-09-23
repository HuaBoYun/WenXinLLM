package com.huabo.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hbfk.util.PageInfo;
import com.huabo.system.entity.TblRole;
import com.huabo.system.entity.TblStaff;

import org.apache.ibatis.annotations.*;

import java.math.BigDecimal;
import java.util.List;

public interface TblRoleDao extends BaseMapper<TblRole> {

    @Select("SELECT * from TBL_ROLE where RSTATUS= '1' and COMPANYID = #{companyid}")
    List<TblRole> findBysql(BigDecimal companyid);

    @InsertProvider(method = "saveTblRole",type = TblRoleDaoSqlConfig.class)
    void saveTblRole(TblRole tr);

    @Select("SELECT * from TBL_ROLE where RID= #{rid}")
    List<TblRole> findByRid(String rid);

    @UpdateProvider(type=TblRoleDaoSqlConfig.class,method="updateTblRole")
    void updateTblRole(TblRole role);


    @SelectProvider(method = "selectListByPageInfo",type = TblRoleDaoSqlConfig.class)
    IPage<TblRole> selectListByPageInfo(IPage<TblRole> page,BigDecimal companyid,TblRole role, String orgIds, String roleName, String orgName);

    @Delete("DELETE FROM TBL_ROLE WHERE RID = #{rid}")
    void deleteByRid(Integer rid);

    @Delete("DELETE FROM TBL_ROLE WHERE RID = #{rid}")
    void deleteRole(BigDecimal rid);

    @Insert("INSERT INTO TBL_ORG_ROLE (ROLEID, DEPTID, ORGID) VALUES (#{roleid}, #{orgid} , #{rootId})")
	void inertOrgandRole(@Param("orgid")String orgid,@Param("roleid") String roleid,@Param("rootId") BigDecimal rootId);

    @Delete("delete from TBL_ORG_ROLE  where ROLEID=#{roleid} and  DEPTID=#{orgid}")
    void delOrgandRole(String orgid, String roleid);

    @SelectProvider(method = "selectReaprtRoleName",type = TblRoleDaoSqlConfig.class)
	Integer selectReaprtRoleName(String rname, BigDecimal rid, BigDecimal pid) throws Exception;
    
    @SelectProvider(method = "findBysqlobj",type = TblRoleDaoSqlConfig.class)
    List<TblStaff> findBysqlobj(String rid);

    @Select("SELECT ORGANIZATIONTREES FROM TBL_ORGANIZATION WHERE ORGID IN (SELECT DEPTID FROM TBL_ORG_ROLE WHERE ROLEID = #{roleId})")
	List<String> selectOrgNamesByRoleId(@Param("roleId")BigDecimal roleId) throws Exception;

    @Select("SELECT DEPTID FROM TBL_ORG_ROLE WHERE ROLEID = #{roleId}")
	List<String> selectOrgRoleIds(@Param("roleId")BigDecimal roleId) throws Exception;

}
