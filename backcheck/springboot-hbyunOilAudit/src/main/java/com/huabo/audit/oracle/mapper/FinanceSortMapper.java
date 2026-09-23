package com.huabo.audit.oracle.mapper;

import com.hbfk.util.PageInfo;
import com.huabo.audit.oracle.entity.FinanceSortEntity;
import com.huabo.audit.oracle.entity.TblOrganization;
import com.huabo.audit.oracle.entity.TblStaff;
import org.apache.ibatis.annotations.*;
import tk.mybatis.mapper.common.BaseMapper;

import java.util.List;

/**
 * @author Rui
 * @ClassName FinanceSortMapper
 * @Description
 * @DATE 2023/9/23
 */
public interface FinanceSortMapper extends BaseMapper<FinanceSortEntity> {

    @SelectProvider(method="selectByEntity",type=FinanceSortMapperSqlConfig.class)
    @Results(id="financeSortResultMap", value={
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
    List<FinanceSortEntity> selectByEntity( FinanceSortEntity financeSortEntity) ;

    @SelectProvider(method="selectCountByEntity",type=FinanceSortMapperSqlConfig.class)
    Integer selectCountByEntity( FinanceSortEntity financeSortEntity) throws Exception;

    @Select("SELECT * FROM TBL_YQNS_FINANCE_SORT WHERE ID = #{id}")
    @ResultMap(value= "financeSortResultMap")
    FinanceSortEntity selectById(String id) throws Exception;

    @UpdateProvider(method="updateEntity", type=FinanceSortMapperSqlConfig.class)
    void updateEntity(FinanceSortEntity financeSortEntity) throws Exception;

    @InsertProvider(method="insertEntity", type=FinanceSortMapperSqlConfig.class)
    void insertEntity(FinanceSortEntity financeSortEntity);

    @DeleteProvider(method="deleteByIds", type=FinanceSortMapperSqlConfig.class)
    void deleteEntity(String ids);
}
