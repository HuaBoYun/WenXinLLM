package com.huabo.audit.oracle.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hbfk.util.PageInfo;
import com.huabo.audit.oracle.entity.QuarterEntity;
import com.huabo.audit.oracle.entity.TblOrganization;
import com.huabo.audit.oracle.entity.TblStaff;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface QuarterMapper extends BaseMapper<QuarterEntity> {
    
    @SelectProvider(method="selectByEntity",type= QuarterMapperSqlConfig.class)
    @Results(id="quarterResultMap", value={
            @Result(column = "ID", property = "id"),
            @Result(column = "QUARTER", property = "quarter"),
            @Result(column = "ORG_ID", property = "orgId"),
            @Result(column = "TYPE", property = "type"),
            @Result(column = "ORG_ID", property = "org", javaType = TblOrganization.class,one= @One(select = "com.huabo.audit.oracle.mapper.TblOrganizationMapper.selectById")),
            @Result(column = "CREATE_USER", property = "createUser", javaType = TblStaff.class,one = @One(select = "com.huabo.audit.oracle.mapper.TblStaffMapper.selectById")),
            @Result(column = "CREATE_TIME", property = "createTime")
    })
    List<QuarterEntity> selectByEntity( QuarterEntity entity);

    @SelectProvider(method="selectCountByEntity",type=QuarterMapperSqlConfig.class)
    Integer selectCountByEntity(QuarterEntity entity);

    @Select("SELECT * FROM TBL_YQNS_QUARTER WHERE ID = #{id} ")
    @ResultMap("quarterResultMap")
    QuarterEntity findById(String id);

    @InsertProvider(method="insertEntity", type=QuarterMapperSqlConfig.class)
    void insertEntity(QuarterEntity quarterEntity);
}
