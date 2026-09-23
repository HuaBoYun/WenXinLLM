package com.huabo.monitor.mysql.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hbfk.util.PageInfo;
import com.huabo.monitor.mysql.entity.TblVideoTypeMySql;
import org.apache.ibatis.annotations.*;

import java.math.BigDecimal;
import java.util.List;


/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author huabo
 * @since 2021-10-20
 */
public interface TblVideoTypeMySqlMapper extends BaseMapper<TblVideoTypeMySql> {

    @Select("SELECT * FROM TBL_VIDEO_TYPE where 1=1  and TYPENAME = #{typename}")
    List<TblVideoTypeMySql> findBytypename(String typename);


    @SelectProvider(type = TblVideoTypeMapperSqlMySqlConfig.class, method = "selectListByPageInfo")
    @Results({
            @Result(column = "TYPEID", property = "typeId"),
            @Result(column = "TYPENAME", property = "typename"),
            @Result(column = "VERSION", property = "version"),
            @Result(column = "ORGID", property = "orgid"),
            @Result(column = "TYPE", property = "type"),
    })
    List<TblVideoTypeMySql> selectListByPageInfo(PageInfo<TblVideoTypeMySql> pageInfo, Integer orgid);

    @Select("SELECT count(*) FROM TBL_VIDEO_TYPE where 1=1 and ORGID = #{orgid}")
    Integer selectCountByPageInfo(Integer orgid);

    @Select("SELECT * FROM TBL_VIDEO_TYPE where 1=1  and ORGID = #{orgid}")
    List<TblVideoTypeMySql> findByorgid(BigDecimal orgid);

    @Select("SELECT * FROM TBL_VIDEO_TYPE where 1=1 and ORGID = #{orgid}")
    List<TblVideoTypeMySql> selectListByVideoType(BigDecimal orgid);

    @Select("SELECT * FROM TBL_VIDEO_TYPE WHERE TYPEID = #{selectid}")
    TblVideoTypeMySql findByid(String selectid);

    @Delete("DELETE FROM TBL_VIDEO_TYPE WHERE TYPEID = #{typeId}")
    void deleteByTypeId(String typeId);

    @InsertProvider(method = "insertTblVideoType", type = TblVideoTypeMapperSqlMySqlConfig.class)
    void insertTblVideoType(TblVideoTypeMySql tnt);

    @UpdateProvider(type = TblVideoTypeMapperSqlMySqlConfig.class, method = "updateTblVideoType")
    void updateTblVideoType(TblVideoTypeMySql tnt);
}
