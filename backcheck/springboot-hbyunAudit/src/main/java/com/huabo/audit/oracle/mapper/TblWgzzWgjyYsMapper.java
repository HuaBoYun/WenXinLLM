package com.huabo.audit.oracle.mapper;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;

import com.huabo.audit.oracle.entity.TblWgzzWgjyYs;
import com.huabo.audit.util.PageInfo;

/**
 * @Created with IDEA 2019
 * @package: com.huabo.audit.oracle.mapper
 * @project_name: SVN4
 * @author:wjh
 * @Date:2023/5/4
 * @Time:11:42
 */
public interface TblWgzzWgjyYsMapper extends tk.mybatis.mapper.common.Mapper<TblWgzzWgjyYs> {

    @SelectProvider(method="getwgzzList",type=TblWgzzWgjyYsMapperSqlConfig.class)
    @Results({
            @Result(column="ID",property="id"),
            @Result(column="CLUENABER",property="cluenaber"),
            @Result(column="HSCONTENT",property="hscontent"),
            @Result(column="JBRNAME",property="jbrname"),
    })
    List<TblWgzzWgjyYs> getWgjyYsList(PageInfo<TblWgzzWgjyYs> pageInfo, String clueNaber);

    @SelectProvider(method="getByContWgjyYsList",type=TblWgzzWgjyYsMapperSqlConfig.class)
    Integer getWgjyYsContList(PageInfo<TblWgzzWgjyYs> pageInfo, String clueNaber);

    //删除
    @Delete("delete from TBL_WGZZ_WGJY_YS t where t.ID = #{id}")
    Integer deleteWgjyYs(@Param("id") BigDecimal id);

    @Select("select * from TBL_WGZZ_WGJY_YS where WGHCID = #{wghcid}")
    @Results({
            @Result(column="ID",property="id"),
            @Result(column="CLUENABER",property="cluenaber"),
            @Result(column="CLUERESOURCE",property="hscontent"),
            @Result(column="DEPARTMENT",property="department"),
            @Result(column="PERSONNEL",property="personnel"),
            @Result(column="HSCONTENT",property="hscontent"),
            @Result(column="ZCSSQK",property="zcssqk"),
            @Result(column="WJWTXS",property="wjwtxs"),
            @Result(column="JBRNAME",property="jbrname"),
            @Result(column="BGSZR",property="bgszr"),
            @Result(column="LDXZZZ",property="ldxzzz"),
    })
    TblWgzzWgjyYs selectBywghcId(@Param("wghcid") BigDecimal wghcid);
    
    
    @Select("select * from TBL_WGZZ_WGJY_YS where ID = #{id}")
    TblWgzzWgjyYs selectById( BigDecimal id);
}
