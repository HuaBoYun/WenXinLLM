package com.huabo.audit.oracle.mapper;

import com.hbfk.util.PageInfo;
import com.huabo.audit.oracle.entity.PlanDataEntity;
import com.huabo.audit.oracle.entity.TblAttachment;
import com.huabo.audit.oracle.entity.TblStaff;
import org.apache.ibatis.annotations.*;
import tk.mybatis.mapper.common.BaseMapper;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author Rui
 * @ClassName PlanDataMapper
 * @Description
 * @DATE 2023/9/30
 */
public interface PlanDataMapper extends BaseMapper<PlanDataEntity> {

    @SelectProvider(method="selectByEntity",type=PlanDataMapperSqlConfig.class)
    @Results(id="planDataResultMap", value={
            @Result(column = "ID", property = "id"),
            @Result(column = "NAME", property = "name"),
            @Result(column = "NO", property = "no"),
            @Result(column = "CREATE_USER", property = "createUser", javaType = TblStaff.class,one = @One(select = "com.huabo.audit.oracle.mapper.TblStaffMapper.selectById")),
            @Result(column = "CREATE_TIME", property = "createTime"),
            @Result(column = "ID", property= "attachments", javaType = List.class, many = @Many(select= "selectAttachmentById"))
    })
    List<PlanDataEntity> selectByEntity( PlanDataEntity planDataEntity) ;

    @SelectProvider(method="selectCountByEntity",type=PlanDataMapperSqlConfig.class)
    Integer selectCountByEntity( PlanDataEntity planDataEntity) throws Exception;

    @Select("SELECT * FROM TBL_YQNS_PLAN_DATA WHERE ID = #{id}")
    @ResultMap(value= "planDataResultMap")
    PlanDataEntity selectById(String id) throws Exception;

    @UpdateProvider(method="updateEntity", type=PlanDataMapperSqlConfig.class)
    void updateEntity(PlanDataEntity planDataEntity) throws Exception;

    @InsertProvider(method="insertEntity", type=PlanDataMapperSqlConfig.class)
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "ID")
    void insertEntity(PlanDataEntity planDataEntity);

    @DeleteProvider(method="deleteByIds", type=PlanDataMapperSqlConfig.class)
    void deleteEntity(String ids);

    @Select("SELECT * FROM TBL_ATTACHMENT WHERE ATTID IN (SELECT ATTID FROM TBL_YQNS_PLAN_DATA_ATT WHERE PDID = #{id})")
    List<TblAttachment> selectAttachmentById(BigDecimal id);

    @DeleteProvider(method="deleteAttachmentByIds", type=PlanDataMapperSqlConfig.class)
    void deleteAttachmentByIds(String ids);

    @InsertProvider(method="insertAttachments", type=PlanDataMapperSqlConfig.class)
    void insertAttachmentsWidthId(BigDecimal id, String attachmentId);
}
