package com.huabo.audit.oracle.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.audit.oracle.entity.TblWgzzEntity;
import com.huabo.audit.oracle.entity.TblWgzzWghc;
import com.huabo.audit.oracle.entity.TblWgzzWghcBg;
import com.huabo.audit.oracle.entity.TblWgzzWghs;
import com.huabo.audit.util.PageInfo;
import org.apache.ibatis.annotations.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Created with IDEA 2019
 * @package: com.huabo.audit.oracle.mapper
 * @project_name: SVN4
 * @author:wjh
 * @Date:2023/5/4
 * @Time:11:42
 */
public interface TblWgzzWghcBgMapper extends tk.mybatis.mapper.common.Mapper<TblWgzzWghcBg> {

    @SelectProvider(method="getwgzzList",type=TblWgzzWghcBgMapperSqlConfig.class)
    @Results({
            @Result(column="ID",property="id"),
            @Result(column="CLUENABER",property="cluenaber"),
            @Result(column="HSCONTENT",property="hscontent"),
            @Result(column="JBRNAME",property="jbrname"),
            @Result(column="BGSZRNAME",property="bgszrname"),
    })
    List<TblWgzzWghcBg> getWghcBgList(PageInfo<TblWgzzWghcBg> pageInfo, String clueNaber);

    @SelectProvider(method="getByContWghcBgList",type=TblWgzzWghcBgMapperSqlConfig.class)
    Integer getWghcBgContList(PageInfo<TblWgzzWghcBg> pageInfo, String clueNaber);

    //删除
    @Delete("delete from TBL_WGZZ_WGHC_BG t where t.ID = #{id}")
    Integer deleteWghcBg(@Param("id") BigDecimal id);

    @Select("select * from TBL_WGZZ_WGHC_BG where WGHCID = #{wghcid}")
    @Results({
            @Result(column="ID",property="id"),
            @Result(column="CLUENABER",property="cluenaber"),
            @Result(column="HSCONTENT",property="hscontent"),
            @Result(column="HSFW",property="hsfw"),
            @Result(column="ORGNAME",property="orgname"),
            @Result(column="ZRDX",property="zrdx"),
            @Result(column="ZCSSRD",property="zcssrd"),
            @Result(column="ZRZJCLJY",property="zrzjcljy"),
            @Result(column="JBRNAME",property="jbrname"),
            @Result(column="BGSZRNAME",property="bgszrname"),
            @Result(column="LDXZZZNAME",property="ldxzzzname"),
            @Result(column="LDXZHYJYID",property="ldxzhyjyid"),
            @Result(column="DWHHYJYID",property="dwhhyjyid"),
            @Result(column="HCBGFILEID",property="hcbgfileid"),
    })
    TblWgzzWghcBg selectBywghcId(@Param("wghcid") BigDecimal wghcid);


}
