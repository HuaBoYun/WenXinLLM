package com.huabo.audit.oracle.mapper;

import com.hbfk.util.PageInfo;
import com.huabo.audit.oracle.entity.*;
import org.apache.ibatis.annotations.*;
import tk.mybatis.mapper.common.BaseMapper;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author Rui
 * @ClassName ScoreManageMapper
 * @Description
 * @DATE 2023/9/30
 */
public interface ScoreManageMapper extends BaseMapper<ScoreManageEntity> {

    @SelectProvider(method="selectByEntity",type=ScoreManageMapperSqlConfig.class)
    @Results(id="scoreManageResultMap", value={
            @Result(column = "ID", property = "id"),
            @Result(column = "TYPE", property = "type"),
            @Result(column = "SCORE", property = "score"),
            @Result(column = "NO", property = "no"),
            @Result(column = "STATUS", property = "status"),
            @Result(column = "CREATE_USER", property = "createUser", javaType = TblStaff.class,one = @One(select = "com.huabo.audit.oracle.mapper.TblStaffMapper.selectById")),
            @Result(column = "CREATE_TIME", property = "createTime"),
            @Result(column = "ID", property= "scoreItems", javaType = List.class, many = @Many(select= "selectScoreItemsById"))
    })
    List<ScoreManageEntity> selectByEntity( ScoreManageEntity scoreManageEntity) ;

    @SelectProvider(method="selectCountByEntity",type=ScoreManageMapperSqlConfig.class)
    Integer selectCountByEntity( ScoreManageEntity scoreManageEntity) throws Exception;

    @Select("SELECT * FROM TBL_YQNS_SCORE_MANAGE WHERE ID = #{id}")
    @ResultMap(value= "scoreManageResultMap")
    ScoreManageEntity selectById(String id) throws Exception;

    @UpdateProvider(method="updateEntity", type=ScoreManageMapperSqlConfig.class)
    void updateEntity(ScoreManageEntity scoreManageEntity) throws Exception;

    @InsertProvider(method="insertEntity", type=ScoreManageMapperSqlConfig.class)
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "ID")
    void insertEntity(ScoreManageEntity scoreManageEntity);

    @DeleteProvider(method="deleteByIds", type=ScoreManageMapperSqlConfig.class)
    void deleteEntity(String ids);

    @Select("SELECT * FROM TBL_YQNS_SCORE_MANAGE_ITEM WHERE SMID = #{id} ORDER BY SORT ASC")
    List<ScoreManageItemEntity> selectScoreItemsById(BigDecimal id);

    @DeleteProvider(method="deleteScoreItemsByIds", type=ScoreManageMapperSqlConfig.class)
    void deleteScoreItemsById(String id);

    @InsertProvider(method="insertScoreItem", type=ScoreManageMapperSqlConfig.class)
    void insertScoreItem(ScoreManageItemEntity scoreItem );

    @Select("SELECT COUNT(*) FROM TBL_YQNS_SCORE_MANAGE WHERE TYPE = #{type} AND STATUS = 1")
    int selectCountByType(Integer type);

    @Select("SELECT * FROM TBL_YQNS_SCORE_MANAGE_ITEM WHERE ID = #{id}")
    ScoreManageItemEntity selectScoreManageItemById(BigDecimal id);

    @Select("SELECT * FROM TBL_YQNS_SCORE_MANAGE_ITEM WHERE SMID = (SELECT ID FROM TBL_YQNS_SCORE_MANAGE WHERE ID IN ( SELECT SMID FROM TBL_YQNS_QUALITY_EVALUATE_ITEM WHERE QEID = ( SELECT ID FROM TBL_YQNS_QUALITY_EVALUATE WHERE STATUS = 1 ) ) AND STATUS = 1 AND TYPE = #{type} ) ORDER BY SORT ASC")
    List<ScoreManageItemEntity> selectAvailableScoreItemsByType(Integer type);

    @Update("UPDATE TBL_YQNS_SCORE_MANAGE SET STATUS = #{status} WHERE ID = #{id}")
    void updateStatus(BigDecimal id, Integer status);
}
