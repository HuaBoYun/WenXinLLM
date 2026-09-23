package com.huabo.audit.oracle.mapper;

import com.hbfk.util.PageInfo;
import com.huabo.audit.oracle.entity.*;
import org.apache.ibatis.annotations.*;
import tk.mybatis.mapper.common.BaseMapper;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author Rui
 * @ClassName QualityResultMapper
 * @Description
 * @DATE 2023/10/16
 */
public interface QualityResultMapper extends BaseMapper<QualityResultEntity> {

    @SelectProvider(method="selectByEntity",type=QualityResultMapperSqlConfig.class)
    @Results(id="qualityResultMap", value={
            @Result(column = "ID", property = "id"),
            @Result(column = "PROJECT_ID", property = "project", javaType = TblNbsjProject.class,one = @One(select = "com.huabo.audit.oracle.mapper.ImplementPlanMapper.selectById")),
            @Result(column = "ASSESSOR_ID", property = "assessor", javaType = String.class,one = @One(select = "com.huabo.audit.oracle.mapper.TblStaffMapper.selectNameById")),
            @Result(column = "ASSESSOR_ID", property = "assessorId"),
            @Result(column = "REVIEWER_ID", property = "reviewer", javaType = String.class,one = @One(select = "com.huabo.audit.oracle.mapper.TblStaffMapper.selectNameById")),
            @Result(column = "REVIEWER_ID", property = "reviewerId"),
            @Result(column = "RESULT", property = "result"),
            @Result(column = "NO", property = "no"),
            @Result(column = "SCORE", property = "score"),
            @Result(column = "REMARK", property = "remark"),
            @Result(column = "PROJECT_ID", property= "qualities", javaType = List.class, many = @Many(select= "com.huabo.audit.oracle.mapper.QualityMapper.selectQualityByProjectId"))
    })
    List<QualityResultEntity> selectByEntity( QualityResultEntity qualityResultEntity) ;

    @SelectProvider(method="selectCountByEntity",type=QualityResultMapperSqlConfig.class)
    Integer selectCountByEntity( QualityResultEntity qualityResultEntity) throws Exception;

    @Select("SELECT * FROM TBL_YQNS_QUALITY_RESULT WHERE ID = #{id} ")
    @ResultMap("qualityResultMap")
    QualityResultEntity selectById(String id) throws Exception;

    @UpdateProvider(method="updateEntity", type=QualityResultMapperSqlConfig.class)
    void updateEntity(QualityResultEntity qualityResultEntity) throws Exception;

    @InsertProvider(method="insertEntity", type=QualityResultMapperSqlConfig.class)
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "ID")
    void insertEntity(QualityResultEntity qualityResultEntity);

    @DeleteProvider(method="deleteByIds", type=QualityResultMapperSqlConfig.class)
    void deleteEntity(String ids);

}
