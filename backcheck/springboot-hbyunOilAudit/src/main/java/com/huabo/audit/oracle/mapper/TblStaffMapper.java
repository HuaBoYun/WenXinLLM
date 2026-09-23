package com.huabo.audit.oracle.mapper;

import java.math.BigDecimal;
import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hbfk.util.JsonBean;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.UpdateProvider;

import com.huabo.audit.oracle.entity.TblOrganization;
import com.huabo.audit.oracle.entity.TblStaff;
import com.huabo.audit.util.PageInfo;

public interface TblStaffMapper extends BaseMapper<TblStaff> {
	
	
	@Select("SELECT * from TBL_STAFF WHERE STAFFID= #{staffid} ")
    TblStaff getById(String staffid);

	@Select("SELECT * from TBL_STAFF WHERE STAFFID in (${staffid}) ")
	List<TblStaff> selectTblStaffByIds(String staffids);



	@SelectProvider(method="selectCountByPageInfo",type=TblStaffMapperSqlConfig.class)
   	Integer selectCountByPageInfo(PageInfo<TblStaff> pageInfo,BigDecimal orgid) throws Exception;

    @Select("SELECT TNA.*,ORG.ORGNAME "
    		+ " FROM TBL_STAFF TNA "
    		+ " LEFT JOIN TBL_ORGANIZATION ORG ON ORG.ORGID = TNA.ORGID "
    		+ " WHERE TNA.STAFFID = #{staffid}")
    @Results({
    	@Result(column="STAFFID",property="staffid"),
    })
   	TblStaff selectById(@Param("staffid") BigDecimal staffid) throws Exception;

	@Select("SELECT TNA.realname "
			+ " FROM TBL_STAFF TNA "
			+ " LEFT JOIN TBL_ORGANIZATION ORG ON ORG.ORGID = TNA.ORGID "
			+ " WHERE TNA.STAFFID = #{staffid}")
	String selectNameById(@Param("staffid") BigDecimal staffid) throws Exception;
    
    @SelectProvider(method="selectListByPageInfo",type=TblStaffMapperSqlConfig.class)
    @Results({
    	@Result(column="STAFFID",property="staffid"),
    	@Result(column="ORGNAME",property="orgName"),
    	
    })
	List<TblStaff> selectListByPageInfo(PageInfo<TblStaff> pageInfo,BigDecimal orgid) throws Exception;
    
    @Select("SELECT realname from TBL_STAFF WHERE STAFFID in (${staffid}) ")
    List<String> getByIds(String staffid);


    
    @Select("SELECT rid from TBL_ROLE WHERE RNAME like '%${rname}%' ")
    List<String> getByroleIds(String rname);
    
    @Select("select * from TBL_STAFF WHERE ROLEIDSTRS LIKE '%${rids}%'  ")
    List<TblStaff> selectbyRoleids(String rids);

//    @Delete("DELETE FROM TBL_STAFF WHERE STAFFID = #{staffid}")
//    void deleteById(Integer staffid) throws Exception;
//
//    @SelectProvider(method="selectPlanCodeByOrgid",type=TblStaffMapperSqlConfig.class)
//	Integer selectPlanCodeByOrgid(TblStaff plan) throws Exception;
//    
//    @InsertProvider(method="insertEntity",type=TblStaffMapperSqlConfig.class)
//    @Options(useGeneratedKeys=true, keyProperty="staffid", keyColumn="STAFFID")
//	void insertEntity(TblStaff plan) throws Exception;
//
//    @UpdateProvider(method="updateEntity",type=TblStaffMapperSqlConfig.class)
//	void updateEntity(TblStaff plan) throws Exception;
    
    
    @SelectProvider(method = "findAllPageBeanPid",type = TblStaffMapperSqlConfig.class)
    List<TblStaff> findAllPageBeanPid(PageInfo<TblStaff> pageInfo,String username,String ralename, TblOrganization attribute);
	@SelectProvider(method = "findAllCountPageBeanPid",type = TblStaffMapperSqlConfig.class)
	Integer findAllCountPageBeanPid(PageInfo<TblStaff> pageInfo,String username,String ralename, TblOrganization attribute);
	
	
	 @SelectProvider(method = "selectByOrgidListPageInfo",type = TblStaffMapperSqlConfig.class)
	 List<TblStaff> selectByOrgidListPageInfo(PageInfo<TblStaff> pageInfo, BigDecimal orgid,String username,TblStaff staff);
	 
	 @SelectProvider(method = "selectByOrgidCountPageInfo",type = TblStaffMapperSqlConfig.class)
	Integer selectByOrgidCountPageInfo(BigDecimal orgid,String username,TblStaff staff);
	
	
	@UpdateProvider(type=TblStaffMapperSqlConfig.class,method="updateStaff")
	void updateStaff(TblStaff user);
	
	@Update("UPDATE TBL_STAFF SET ONDUTYSTATUS=2 WHERE STAFFID  in (${ids})")
	void updateStatus(String ids);
	
	
	
	@SelectProvider(type=TblStaffMapperSqlConfig.class,method="findByStaffManOrgs")
	public TblStaff findByStaffManOrgs(String orgid);
	@SelectProvider(type=TblStaffMapperSqlConfig.class,method="findByStaffFgOrgs")
	public TblStaff findByStaffFgOrgs(String orgid);
	
	@SelectProvider(type=TblStaffMapperSqlConfig.class,method="findByJobName")
	TblStaff findByJobName(String jobname, String orgid);

	@Select("SELECT TNA.*,ORG.ORGNAME "
			+ " FROM TBL_STAFF TNA "
			+ " LEFT JOIN TBL_ORGANIZATION ORG ON ORG.ORGID = TNA.ORGID "
			+ " WHERE TNA.AUDITSTATE =#{count}")//count是什么条件
    List<TblStaff> queryAuditObjectStaffAll( @Param("count") Integer count);
}