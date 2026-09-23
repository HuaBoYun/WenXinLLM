package com.huabo.audit.oracle.mapper;

import java.util.List;

import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;

import com.huabo.audit.oracle.entity.ProjectSuggestionEntity;
import com.huabo.audit.oracle.entity.TblStaff;

import tk.mybatis.mapper.common.BaseMapper;

/**
 * @author Rui
 * @ClassName ProjectSuggestionMapper
 * @Description
 * @DATE 2023/9/7
 */
public interface ProjectSuggestionMapper extends BaseMapper<ProjectSuggestionEntity> {

    @SelectProvider(method="selectByEntity",type=ProjectSuggestionMapperSqlConfig.class)
    @Results(id="projectSuggestionResultMap", value={
            @Result(column = "ID", property = "id"),
            @Result(column = "PURPOSENO", property = "purposeNo"),
            @Result(column = "PROJECT_NAME", property = "projectName"),
            @Result(column = "PROJECT_PURPOSE", property = "projectPurpose"),
            @Result(column = "CONCERNS_CONTENT", property = "concernsContent"),
            @Result(column = "PROJECT_TYPE", property = "projectType"),
            @Result(column = "UNIT_RANGE", property = "unitRange" ),
            @Result(column = "TIME_RANGE", property = "timeRange" ),
            @Result(column = "PERSON_IDS", property = "personIds"),
            @Result(column = "REMARK", property = "remark"),
            @Result(column = "TBORGID", property = "tborgid"),
            @Result(column = "TBORGNAME", property = "tborgname"),
            @Result(column = "CREATE_USER", property = "createUser", javaType = TblStaff.class,one = @One(select = "com.huabo.audit.oracle.mapper.TblStaffMapper.selectById")),
            @Result(column = "CREATE_TIME", property = "createTime")
    })
    List<ProjectSuggestionEntity> selectByEntity( ProjectSuggestionEntity projectSuggestionEntity,String createYear) ;

    @SelectProvider(method="selectCountByEntity",type=ProjectSuggestionMapperSqlConfig.class)
    Integer selectCountByEntity( ProjectSuggestionEntity projectSuggestionEntity) throws Exception;

    @Select("SELECT * FROM TBL_YQNS_PROJECT_SUGGESTION WHERE ID = #{id}")
    @ResultMap(value= "projectSuggestionResultMap")
    ProjectSuggestionEntity selectById(String id) throws Exception;

    @SelectProvider(method="selectByIds",type=ProjectSuggestionMapperSqlConfig.class)
    @ResultMap(value= "projectSuggestionResultMap")
    List<ProjectSuggestionEntity> selectByIds(String ids) throws Exception;

    @UpdateProvider(method="updateEntity", type=ProjectSuggestionMapperSqlConfig.class)
    void updateEntity(ProjectSuggestionEntity projectSuggestionEntity) throws Exception;

    @InsertProvider(method="insertEntity", type=ProjectSuggestionMapperSqlConfig.class)
    void insertEntity(ProjectSuggestionEntity projectSuggestionEntity);

    @DeleteProvider(method="deleteByIds", type=ProjectSuggestionMapperSqlConfig.class)
    void deleteEntity(String ids);
}
