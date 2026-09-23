package com.huabo.audit.oracle.mapper;

import com.hbfk.util.PageInfo;
import com.huabo.audit.oracle.entity.*;
import org.apache.ibatis.annotations.*;
import tk.mybatis.mapper.common.BaseMapper;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author Rui
 * @ClassName RequireSuggestionMapper
 * @Description
 * @DATE 2023/9/7
 */
public interface RequireSuggestionMapper extends BaseMapper<RequireSuggestionEntity> {

    @SelectProvider(method="selectByEntity",type=RequireSuggestionMapperSqlConfig.class)
    @Results(id="requireSuggestionResultMap", value={
            @Result(column = "ID", property = "id"),
            @Result(column = "CONCERNS", property = "concerns"),
            @Result(column = "SUGGESTIONNO", property = "suggestionNo"),
            @Result(column = "CONCERNS_CONTENT", property = "concernsContent"),
            @Result(column = "UNIT_ID", property = "organization", javaType = TblOrganization.class, one= @One(select = "com.huabo.audit.oracle.mapper.TblOrganizationMapper.selectById")),
			@Result(column = "UNIT_ID", property = "organizationId"),
            @Result(column = "PROJECT_TYPE", property = "projectType"),
            @Result(column = "DRAFT_Id", property = "draftId" ),
            @Result(column = "DRAFT_Id", property = "draft", javaType = TblYqnsAuditMyManuscriptEntity.class, one=@One(select="com.huabo.audit.oracle.mapper.TblYqnsAuditMyManuscriptMapper.selectById")),
            @Result(column = "REMARK", property = "remark"),
            @Result(column = "PERSON_IDS", property = "personIds"),
            @Result(column = "CREATE_USER", property = "createUser", javaType = TblStaff.class,one = @One(select = "com.huabo.audit.oracle.mapper.TblStaffMapper.selectById")),
            @Result(column = "CREATE_TIME", property = "createTime")
    })
    List<RequireSuggestionEntity> selectByEntity( RequireSuggestionEntity requireSuggestionEntity) ;

    @SelectProvider(method="selectCountByEntity",type=RequireSuggestionMapperSqlConfig.class)
    Integer selectCountByEntity( RequireSuggestionEntity requireSuggestionEntity) throws Exception;

    @Select("SELECT * FROM TBL_YQNS_REQUIRE_SUGGESTION WHERE ID = #{id}")
    @ResultMap(value= "requireSuggestionResultMap")
    RequireSuggestionEntity selectById(String id) throws Exception;

    @UpdateProvider(method="updateEntity", type=RequireSuggestionMapperSqlConfig.class)
    void updateEntity(RequireSuggestionEntity requireSuggestionEntity) throws Exception;

    @InsertProvider(method="insertEntity", type=RequireSuggestionMapperSqlConfig.class)
    void insertEntity(RequireSuggestionEntity requireSuggestionEntity);

    @DeleteProvider(method="deleteByIds", type=RequireSuggestionMapperSqlConfig.class)
    void deleteEntity(String ids);

    @Select("SELECT * FROM TBL_YQNS_REQUIRE_SUGGESTION WHERE SUGGESTIONNO = #{suggestionNo}")
    @ResultMap(value= "requireSuggestionResultMap")
	RequireSuggestionEntity selecrRepeatSuggentionNoCount(BigDecimal suggestionNo) throws Exception;

    @Select("SELECT MAX(SUGGESTIONNO) FROM TBL_YQNS_REQUIRE_SUGGESTION WHERE SUGGESTIONNO LIKE '${currentYear}%' ")
	BigDecimal selectMapRequireSuggestionNo(@Param("currentYear")Integer currentYear) throws Exception;

}
