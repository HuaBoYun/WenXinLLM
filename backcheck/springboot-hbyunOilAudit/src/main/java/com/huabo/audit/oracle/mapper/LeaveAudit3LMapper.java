package com.huabo.audit.oracle.mapper;

import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.PageInfo;
import com.huabo.audit.oracle.entity.*;
import com.huabo.audit.oracle.vo.LeaveAudit3LCountVo;
import org.apache.ibatis.annotations.*;
import tk.mybatis.mapper.common.BaseMapper;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author Rui
 * @ClassName LeaveAudit3LMapper
 * @Description
 * @DATE 2023/9/14
 */
public interface LeaveAudit3LMapper extends BaseMapper<LeaveAudit3LEntity> {

    @SelectProvider(method="selectByEntity",type=LeaveAudit3LMapperSqlConfig.class)
    @Results(id="leaveAudit3LResultMap", value={
            @Result(column = "ID", property = "id"),
            @Result(column = "NAME", property = "name"),
            @Result(column = "OLD_JOB", property = "oldJob"),
            @Result(column = "JOB", property = "job"),
            @Result(column = "OLD_LEVEL", property = "oldLevel"),
            @Result(column = "OLD_ORG_ID", property = "oldOrg", javaType = TblOrganization.class, one= @One(select = "com.huabo.audit.oracle.mapper.TblOrganizationMapper.selectById")),
            @Result(column = "ORG_ID", property = "org", javaType = TblOrganization.class, one= @One(select = "com.huabo.audit.oracle.mapper.TblOrganizationMapper.selectById")),
           // @Result(column = "QUARTER_ID", property = "quarter", javaType = QuarterEntity.class, one= @One(select = "com.huabo.audit.oracle.mapper.QuarterMapper.findById")),
			@Result(column = "OLD_ORG_ID", property = "oldOrgId"),
			@Result(column = "QUARTER_ID", property = "quarterId"),
            @Result(column = "OLD_JOB_START_TIME", property = "oldJobStartTime" ),
            @Result(column = "OLD_JOB_END_TIME", property = "oldJobEndTime" ),
            @Result(column = "DEPT_TYPE", property = "deptType"),
            @Result(column = "DEPT_HISTORY", property = "deptHistory"),
            @Result(column = "MAIN_DUTY", property = "mainDuty"),
            @Result(column = "MAIN_POWER", property = "mainPower"),
            @Result(column = "BUSINESS", property = "business"),
            @Result(column = "PERSON", property = "person"),
            @Result(column = "IS_SEPARATE_ACCOUNT", property = "isSeparateAccount"),
            @Result(column = "ASSET_INFO", property = "assetInfo"),
            @Result(column = "MAIN_COST", property = "mainCost"),
            @Result(column = "INCOME", property = "income"),
            @Result(column = "COST", property = "cost"),
            @Result(column = "CONTROLLABLE_COST", property = "controllableCost"),
            @Result(column = "HAS_EXTERNAL_PERSON", property = "hasExternalPerson"),
            @Result(column = "UNCONVENT_JOB", property = "unconventJob"),
            @Result(column = "IS_AUDIT", property = "isAudit"),
            @Result(column = "STATUS", property = "status"),
            @Result(column = "ZSSTAFFID", property = "zsstaffid"),
            @Result(column = "ZSNAME", property = "zsname"),
            @Result(column = "ZZSTAFFIDS", property = "zzstaffids"),
            @Result(column = "ZZNAMES", property = "zznames"),
            @Result(column = "FZSTAFFIDS", property = "fzstaffids"),
            @Result(column = "FZNAMES", property = "fznames"),
            @Result(column = "FZSTAFFID", property = "fzstaffid"),
            @Result(column = "FZNAME", property = "fzname"),
            @Result(column = "RSYQ", property = "rsyq"),
            @Result(column = "PROJECTNAME", property = "projectname"),
            @Result(column = "XCSRARTTIME", property = "xcsrarttime"),
            @Result(column = "XCENDTIME", property = "xcendtime"),
            @Result(column = "CREATE_USER", property = "createUser", javaType = TblStaff.class,one = @One(select = "com.huabo.audit.oracle.mapper.TblStaffMapper.selectById")),
            @Result(column = "CREATE_TIME", property = "createTime")
    })
    List<LeaveAudit3LEntity> selectByEntity( LeaveAudit3LEntity leaveAudit3LEntity, TblStaffUtil user,BigDecimal jdid) ;

    @SelectProvider(method="selectCountByEntity",type=LeaveAudit3LMapperSqlConfig.class)
    Integer selectCountByEntity( LeaveAudit3LEntity leaveAudit3LEntity) throws Exception;

    @Select("SELECT * FROM TBL_YQNS_LEAVE_AUDIT_3L WHERE ID = #{id}")
    @ResultMap(value= "leaveAudit3LResultMap")
    LeaveAudit3LEntity selectById(String id) throws Exception;

    @UpdateProvider(method="updateEntity", type=LeaveAudit3LMapperSqlConfig.class)
    void updateEntity(LeaveAudit3LEntity leaveAudit3LEntity) throws Exception;


    @InsertProvider(method="insertEntity", type=LeaveAudit3LMapperSqlConfig.class)
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "ID")
    void insertEntity(LeaveAudit3LEntity leaveAudit3LEntity); 

    @DeleteProvider(method="deleteByIds", type=LeaveAudit3LMapperSqlConfig.class)
    void deleteEntity(String ids);

    @SelectProvider(method="findByIds",type=LeaveAudit3LMapperSqlConfig.class)
    @ResultMap(value= "leaveAudit3LResultMap")
    List<LeaveAudit3LEntity> findByIds(String ids);

    /**
     * 获取三级单位离任审计汇总
     * @param queryYear 
     * @return
     */
    List<LeaveAudit3LCountVo> selectLeaveAudit3LSummary(Integer queryYear);

    @SelectProvider(method="selectListDraftPlan",type=LeaveAudit3LMapperSqlConfig.class)
    @ResultMap(value= "leaveAudit3LResultMap")
    List<LeaveAudit3LEntity> selectListDraftPlan(LeaveAudit3LEntity leaveAudit3LEntity, TblStaffUtil user, BigDecimal jdid);

    @SelectProvider(method="selectListByjhcgGlId",type=LeaveAudit3LMapperSqlConfig.class)
    @ResultMap(value= "leaveAudit3LResultMap")
	List<LeaveAudit3LEntity> selectListByjhcgGlId(BigDecimal id, BigDecimal orgId);

    @SelectProvider(method="selectDetailDistributeList",type=LeaveAudit3LMapperSqlConfig.class)
    @ResultMap(value= "leaveAudit3LResultMap")
    List<LeaveAudit3LEntity> selectDetailDistributeList(LeaveAudit3LEntity leaveAudit3LEntity, TblStaffUtil user);

    @SelectProvider(method="selectDistributeReceiveList",type=LeaveAudit3LMapperSqlConfig.class)
    @ResultMap(value= "leaveAudit3LResultMap")
    List<LeaveAudit3LEntity> selectDistributeReceiveList(LeaveAudit3LEntity vo, TblStaffUtil user);

    @Update("UPDATE TBL_YQNS_LEAVE_AUDIT_3L SET DISFIRSTPERSON = #{disFirstPerson} WHERE ID IN (${idStrs})")
	void updateDisFirstPersonByIds(@Param("disFirstPerson")BigDecimal disFirstPerson,@Param("idStrs") String idStrs) throws Exception;

    @Update("UPDATE TBL_YQNS_LEAVE_AUDIT_3L SET DISSECONDPERSON = #{disSecondPerson} WHERE ID IN (${idStrs})")
	void updateDisSecondPersonByIds(@Param("disSecondPerson")BigDecimal disSecondPerson,@Param("idStrs") String idStrs) throws Exception;

    @SelectProvider(method="selectListByjhchugGlId",type=LeaveAudit3LMapperSqlConfig.class)
    @ResultMap(value= "leaveAudit3LResultMap")
    List<LeaveAudit3LEntity> selectListByjhchugGlId(BigDecimal id, BigDecimal relaId) ;
 
    @SelectProvider(method="selectListByjhzgGlId",type=LeaveAudit3LMapperSqlConfig.class)
    @ResultMap(value= "leaveAudit3LResultMap") 
    List<LeaveAudit3LEntity> selectListByjhzgGlId(BigDecimal id, BigDecimal relaId,String type);
    
     
    
    @SelectProvider(method="getcwanbList",type=LeaveAudit3LMapperSqlConfig.class)
    @ResultMap(value= "leaveAudit3LResultMap")
    List<LeaveAudit3LEntity> getcwanbList(BigDecimal id, BigDecimal relaId);
    
    
    @Select("SELECT ID FROM TBL_YQNS_LEAVE_AUDIT_3L RS where RS.ID in (select NRID from TBL_YQNS_LEAVE_AUDIT_JD3L_GL where JDID in (${idStrs}))")
    List<Long> selectByIds(String idStrs) throws Exception;
    
    
    
    @Update("UPDATE TBL_YQNS_LEAVE_AUDIT_3L SET DISFIRSTPERSON = #{disFirstPerson} WHERE ID IN (${idStrs})")
	void updateDisFirstPerson(Long disFirstPerson, Long idStrs) throws Exception;


}
