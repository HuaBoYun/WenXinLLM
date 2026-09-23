package com.huabo.audit.oracle.mapper;

import com.hbfk.util.PageInfo;
import com.huabo.audit.oracle.entity.QualityEvaluateEntity;
import com.huabo.audit.oracle.entity.ScoreManageEntity;
import com.huabo.audit.oracle.entity.TblStaff;
import org.apache.ibatis.annotations.*;
import tk.mybatis.mapper.common.BaseMapper;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author Rui
 * @ClassName QualityEvaluateMapper
 * @Description
 * @DATE 2023/10/10
 */
public interface QualityEvaluateMapper extends BaseMapper<QualityEvaluateEntity> {

    @SelectProvider(method="selectByEntity",type=QualityEvaluateMapperSqlConfig.class)
    @Results(id="qualityEvaluateResultMap", value={
            @Result(column = "ID", property = "id"),
            @Result(column = "NAME", property = "name"),
            @Result(column = "NO", property = "no"),
            @Result(column = "STATUS", property = "status"),
            @Result(column = "CREATE_USER", property = "createUser", javaType = TblStaff.class,one = @One(select = "com.huabo.audit.oracle.mapper.TblStaffMapper.selectById")),
            @Result(column = "CREATE_TIME", property = "createTime"),
            @Result(column = "ID", property= "scores", javaType = List.class, many = @Many(select= "selectScoresById"))
    })
    List<QualityEvaluateEntity> selectByEntity( QualityEvaluateEntity qualityEvaluateEntity) ;

    @SelectProvider(method="selectCountByEntity",type=QualityEvaluateMapperSqlConfig.class)
    Integer selectCountByEntity( QualityEvaluateEntity qualityEvaluateEntity) throws Exception;

    @Select("SELECT * FROM TBL_YQNS_QUALITY_EVALUATE WHERE ID = #{id}")
    @ResultMap(value= "qualityEvaluateResultMap")
    QualityEvaluateEntity selectById(String id) throws Exception;

    @UpdateProvider(method="updateEntity", type=QualityEvaluateMapperSqlConfig.class)
    void updateEntity(QualityEvaluateEntity qualityEvaluateEntity) throws Exception;

    @InsertProvider(method="insertEntity", type=QualityEvaluateMapperSqlConfig.class)
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "ID")
    void insertEntity(QualityEvaluateEntity qualityEvaluateEntity);

    @DeleteProvider(method="deleteByIds", type=QualityEvaluateMapperSqlConfig.class)
    void deleteEntity(String ids);

    @Select("SELECT * FROM TBL_YQNS_SCORE_MANAGE WHERE ID IN ( SELECT SMID FROM TBL_YQNS_QUALITY_EVALUATE_ITEM WHERE QEID = #{id} )")
    List<ScoreManageEntity> selectScoresById(BigDecimal id);

    @DeleteProvider(method="deleteQualityEvaluateItemsById", type=QualityEvaluateMapperSqlConfig.class)
    void deleteQualityEvaluateItemsById(String id);

    @InsertProvider(method="insertQualityEvaluateItem", type=QualityEvaluateMapperSqlConfig.class)
    void insertQualityEvaluateItem(BigDecimal qeId, BigDecimal smId );

    @Update("UPDATE TBL_YQNS_QUALITY_EVALUATE SET STATUS = #{status}")
    void batchResetStatus(Integer status);

    @UpdateProvider(method = "updateEntityStatus", type=QualityEvaluateMapperSqlConfig.class)
    void updateStatus(QualityEvaluateEntity qualityEvaluateEntity);
}
