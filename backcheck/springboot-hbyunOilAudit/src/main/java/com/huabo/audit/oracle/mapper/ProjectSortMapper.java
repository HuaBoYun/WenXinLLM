package com.huabo.audit.oracle.mapper;

import com.hbfk.util.PageInfo;
import com.huabo.audit.oracle.entity.ProjectSortEntity;
import com.huabo.audit.oracle.entity.TblOrganization;
import com.huabo.audit.oracle.entity.TblStaff;
import org.apache.ibatis.annotations.*;
import tk.mybatis.mapper.common.BaseMapper;

import java.util.List;

/**
 * @author Rui
 * @ClassName ProjectSortMapper
 * @Description
 * @DATE 2023/9/23
 */
public interface ProjectSortMapper extends BaseMapper<ProjectSortEntity> {

    @SelectProvider(method="selectByEntity",type=ProjectSortMapperSqlConfig.class)
    @Results(id="projectSortResultMap", value={
            @Result(column = "ID", property = "id"),
            @Result(column = "SUGGEST_DEPT_ID", property = "suggestDept", javaType = TblOrganization.class,one= @One(select = "com.huabo.audit.oracle.mapper.TblOrganizationMapper.selectById")),
			@Result(column = "SUGGEST_DEPT_ID", property = "suggestDeptId"),
            @Result(column = "SORT", property = "sort"),
            @Result(column = "PROJECT_NAME", property = "projectName"),
            @Result(column = "PROJECT_PURPOSE", property = "projectPurpose" ),
            @Result(column = "CONCERNS_CONTENT", property = "concernsContent" ),
            @Result(column = "UNIT_RANGE", property = "unitRange" ),
            @Result(column = "TIME_RANGE", property = "timeRange" ),
            @Result(column = "STATUS", property = "status"),
            @Result(column = "CREATE_USER", property = "createUser", javaType = TblStaff.class,one = @One(select = "com.huabo.audit.oracle.mapper.TblStaffMapper.selectById")),
            @Result(column = "CREATE_TIME", property = "createTime")
    })
    List<ProjectSortEntity> selectByEntity( ProjectSortEntity projectSortEntity) ;

    @SelectProvider(method="selectCountByEntity",type=ProjectSortMapperSqlConfig.class)
    Integer selectCountByEntity( ProjectSortEntity projectSortEntity) throws Exception;

    @Select("SELECT * FROM TBL_YQNS_PROJECT_SORT WHERE ID = #{id}")
    @ResultMap(value= "projectSortResultMap")
    ProjectSortEntity selectById(String id) throws Exception;

    @UpdateProvider(method="updateEntity", type=ProjectSortMapperSqlConfig.class)
    void updateEntity(ProjectSortEntity projectSortEntity) throws Exception;

    @InsertProvider(method="insertEntity", type=ProjectSortMapperSqlConfig.class)
    void insertEntity(ProjectSortEntity projectSortEntity);

    @DeleteProvider(method="deleteByIds", type=ProjectSortMapperSqlConfig.class)
    void deleteEntity(String ids);
}
