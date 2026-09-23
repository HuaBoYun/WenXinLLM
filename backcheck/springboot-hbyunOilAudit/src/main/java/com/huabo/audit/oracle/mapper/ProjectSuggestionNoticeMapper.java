package com.huabo.audit.oracle.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.audit.oracle.entity.ProjectSuggestionNoticeEntity;
import com.huabo.audit.oracle.entity.TblAttachment;
import com.huabo.audit.oracle.entity.TblStaff;
import org.apache.ibatis.annotations.*;

import java.math.BigDecimal;
import java.util.List;

public interface ProjectSuggestionNoticeMapper extends BaseMapper<ProjectSuggestionNoticeEntity> {

    @Results(id="projectSuggestionNoticeResultMap",value={
            @Result(column = "ID", property = "id"),
            @Result(column = "NOTICENO", property = "noticeNo"), 
            @Result(column = "NAME", property = "name"),
            @Result(column = "ORG_IDS", property = "orgIds"),
            @Result(column = "ORG_IDS", property = "organizations",javaType = List.class, many = @Many(select= "com.huabo.audit.oracle.mapper.TblOrganizationMapper.selectByIds")),
            @Result(column = "PS_IDS", property = "psIds"),
            @Result(column = "PS_IDS", property = "projectSuggestions",javaType = List.class, many = @Many(select= "com.huabo.audit.oracle.mapper.ProjectSuggestionMapper.selectByIds")),
            @Result(column = "REMARK", property = "remark"),
            @Result(column = "CREATE_USER", property = "createUser", javaType = TblStaff.class,one = @One(select = "com.huabo.audit.oracle.mapper.TblStaffMapper.selectById")),
            @Result(column = "CREATE_TIME", property = "createTime"),
            @Result(column = "ID", property= "attachments", javaType = List.class, many = @Many(select= "selectAttachmentById"))

    }) 
    @SelectProvider(method="selectByEntity",type=ProjectSuggestionNoticeMapperSqlConfig.class)
    List<ProjectSuggestionNoticeEntity> selectByEntity(ProjectSuggestionNoticeEntity entity,TblStaff staff);

    @Select("SELECT YN.*,STA.REALNAME,ORG.ORGNAME FROM TBL_YQNS_PS_NOTICE yn "
    		+ " LEFT JOIN TBL_ORGANIZATION org on YN.ORG_IDS=ORG.ORGID"
    		+ " LEFT JOIN TBL_STAFF sta on YN.CREATE_USER=STA.STAFFID"
    		+ "  WHERE ID = #{id}")
    @Results({
            @Result(column = "ID", property = "id"),
            @Result(column = "NOTICENO", property = "noticeNo"), 
            @Result(column = "NAME", property = "name"),
            @Result(column = "ORG_IDS", property = "orgIds"),
            @Result(column = "ORGNAME", property = "orgname"),
            @Result(column = "ORG_IDS", property = "organizations",javaType = List.class, many = @Many(select= "com.huabo.audit.oracle.mapper.TblOrganizationMapper.selectByIds")),
            @Result(column = "PS_IDS", property = "psIds"),
            @Result(column = "PS_IDS", property = "projectSuggestions",javaType = List.class, many = @Many(select= "com.huabo.audit.oracle.mapper.ProjectSuggestionMapper.selectByIds")),
            @Result(column = "REMARK", property = "remark"),
            @Result(column = "REALNAME", property = "createname"),
            @Result(column = "CREATE_USER", property = "createUser", javaType = TblStaff.class,one = @One(select = "com.huabo.audit.oracle.mapper.TblStaffMapper.selectById")),
            @Result(column = "CREATE_TIME", property = "createTime"),
            @Result(column = "ID", property= "attachments", javaType = List.class, many = @Many(select= "selectAttachmentById"))

    }) 
    ProjectSuggestionNoticeEntity findById(String id); 

    @Select("SELECT * FROM TBL_ATTACHMENT WHERE ATTID IN (SELECT ATTID FROM TBL_YQNS_PS_NOTICE_ATT WHERE PID = #{id})")
    List<TblAttachment> selectAttachmentById(Long id);

    @DeleteProvider(method="deleteAttachmentByIds", type=ProjectSuggestionNoticeMapperSqlConfig.class)
    void deleteAttachmentByIds(String ids);

    @InsertProvider(method="insertAttachments", type=ProjectSuggestionNoticeMapperSqlConfig.class)
    void insertAttachmentsWidthId(BigDecimal id, String attachmentId);

    @InsertProvider(method="insertEntity", type=ProjectSuggestionNoticeMapperSqlConfig.class)
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "ID")
    void insertEntity(ProjectSuggestionNoticeEntity projectSuggestionNoticeEntity);
    
    @Update("UPDATE TBL_YQNS_PS_NOTICE SET PERSON_IDS=#{ids} WHERE ID = #{id}")
    void xfxmzry(String id,String ids);
}
