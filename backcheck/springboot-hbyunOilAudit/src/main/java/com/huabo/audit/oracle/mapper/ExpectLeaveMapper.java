package com.huabo.audit.oracle.mapper;

import com.hbfk.util.PageInfo;
import com.huabo.audit.oracle.entity.ExpectLeaveEntity;
import com.huabo.audit.oracle.entity.TblOrganization;
import com.huabo.audit.oracle.entity.TblStaff;
import org.apache.ibatis.annotations.*;
import tk.mybatis.mapper.common.BaseMapper;

import java.util.List;

/**
 * @author Rui
 * @ClassName ExpectLeaveMapper
 * @Description
 * @DATE 2023/9/14
 */
public interface ExpectLeaveMapper extends BaseMapper<ExpectLeaveEntity> {

    @SelectProvider(method="selectByEntity",type=ExpectLeaveMapperSqlConfig.class)
    @Results(id="expectLeaveResultMap", value={
            @Result(column = "ID", property = "id"),
            @Result(column = "NAME", property = "name"),
            @Result(column = "NO", property = "no"),
            @Result(column = "RETIRE_TIME", property = "retireTime"),
            @Result(column = "AUDIT_TIME", property = "auditTime"),
            @Result(column = "PROJECT_NAME", property = "projectName"),
            @Result(column = "WORK_START_TIME", property = "workStartTime" ),
            @Result(column = "WORK_END_TIME", property = "workEndTime" ),
            @Result(column = "DO_AUDIT_TIME", property = "doAuditTime" ),
//            @Result(column = "TEAM_LEADER_ID", property = "teamLeader" , javaType = TblStaff.class,one = @One(select = "com.huabo.audit.oracle.mapper.TblStaffMapper.selectById")),
			@Result(column = "TEAM_LEADER_ID", property = "teamLeaderId"), 
			@Result(column = "TEAM_LEADER_NAME", property = "teamLeaderName"),
//            @Result(column = "LEADER_ID", property = "leader" , javaType = TblStaff.class,one = @One(select = "com.huabo.audit.oracle.mapper.TblStaffMapper.selectById")),
			@Result(column = "LEADER_ID", property = "leaderId"),
			@Result(column = "LEADER_NAME", property = "leaderName"),
            @Result(column = "PERSON_IDS", property = "personIds"),
//            @Result(column = "CHIEF_REVIEWER_ID", property = "chiefReviewer" , javaType = TblStaff.class,one = @One(select = "com.huabo.audit.oracle.mapper.TblStaffMapper.selectById")),
			@Result(column = "CHIEF_REVIEWER_ID", property = "chiefReviewerId"),
			@Result(column = "CHIEF_REVIEWER_NAME", property = "chiefReviewerName"),
//            @Result(column = "DEPUTY_REVIEWER_ID", property = "deputyReviewer" , javaType = TblStaff.class,one = @One(select = "com.huabo.audit.oracle.mapper.TblStaffMapper.selectById")),
			@Result(column = "DEPUTY_REVIEWER_ID", property = "deputyReviewerId"),
			@Result(column = "DEPUTY_REVIEWER_NAME", property = "deputyReviewerName"),
            @Result(column = "STATUS", property = "status"),
            @Result(column = "CREATE_USER", property = "createUser", javaType = TblStaff.class,one = @One(select = "com.huabo.audit.oracle.mapper.TblStaffMapper.selectById")),
            @Result(column = "UNITID", property = "tblOrganization", javaType = TblOrganization.class,one = @One(select = "com.huabo.audit.oracle.mapper.TblOrganizationMapper.selectById")),
            @Result(column = "CREATE_TIME", property = "createTime")
    })
    List<ExpectLeaveEntity> selectByEntity( ExpectLeaveEntity expectLeaveEntity) ;

    @SelectProvider(method="selectCountByEntity",type=ExpectLeaveMapperSqlConfig.class)
    Integer selectCountByEntity( ExpectLeaveEntity expectLeaveEntity) throws Exception;

    @Select("SELECT * FROM TBL_YQNS_EXPECT_LEAVE WHERE ID = #{id}")
    @ResultMap(value= "expectLeaveResultMap")
    ExpectLeaveEntity selectById(String id) throws Exception;

    @UpdateProvider(method="updateEntity", type=ExpectLeaveMapperSqlConfig.class)
    void updateEntity(ExpectLeaveEntity expectLeaveEntity) throws Exception;

    @InsertProvider(method="insertEntity", type=ExpectLeaveMapperSqlConfig.class)
    void insertEntity(ExpectLeaveEntity expectLeaveEntity);

    @DeleteProvider(method="deleteByIds", type=ExpectLeaveMapperSqlConfig.class)
    void deleteEntity(String ids);
}
