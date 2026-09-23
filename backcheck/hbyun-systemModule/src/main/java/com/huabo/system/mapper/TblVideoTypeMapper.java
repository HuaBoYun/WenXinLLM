package com.huabo.system.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hbfk.util.PageInfo;
import com.huabo.system.entity.TblVideoType;

import org.apache.ibatis.annotations.*;


import java.math.BigDecimal;
import java.util.List;


/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author huabo
 * @since 2021-10-20
 */
public interface TblVideoTypeMapper extends BaseMapper<TblVideoType> {

    @Select("SELECT * FROM TBL_VIDEO_TYPE where 1=1  and TYPENAME = #{typename}")
    List<TblVideoType> findBytypename(String typename);


    @SelectProvider(type=TblVideoTypeMapperSqlConfig.class,method="selectListByPageInfo")
    @Results({
            @Result(column="TYPEID",property="typeId"),
            @Result(column="TYPENAME",property="typename"),
            @Result(column="VERSION",property="version"),
            @Result(column="ORGID",property="orgid"),
            @Result(column="TYPE",property="type"),
    })
    IPage<TblVideoType> selectListByPageInfo(IPage<TblVideoType> page, Integer orgid);

    @Select("SELECT * FROM TBL_VIDEO_TYPE where 1=1  and ORGID = #{orgid}")
    List<TblVideoType> findByorgid(BigDecimal orgid);

    @Select("SELECT * FROM TBL_VIDEO_TYPE where 1=1 and ORGID = #{orgid}")
    List<TblVideoType> selectListByVideoType(BigDecimal orgid);

    @Select("SELECT * FROM TBL_VIDEO_TYPE WHERE TYPEID = #{selectid}")
    TblVideoType findByid(String selectid);

    @Delete("DELETE FROM TBL_VIDEO_TYPE WHERE TYPEID = #{typeId}")
    void deleteByTypeId(String typeId);

    @InsertProvider(method="insertTblVideoType",type=TblVideoTypeMapperSqlConfig.class)
    void insertTblVideoType(TblVideoType tnt);

    @UpdateProvider(type=TblVideoTypeMapperSqlConfig.class,method="updateTblVideoType")
    void updateTblVideoType(TblVideoType tnt);
}
