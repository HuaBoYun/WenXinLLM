package com.huabo.audit.oracle.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.audit.oracle.entity.TblWgzzWghc;
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
 * @Time:12:07
 */
public interface TblWgzzWghcMapper extends BaseMapper<TblWgzzWghc> {

    @SelectProvider(method="selectBywghcBy",type=TblWgzzWghcMapperSqlConfig.class)
    @Results({
            @Result(column="ID",property="id"),
            @Result(column="CLUEZRDX",property="clueZrdx"),
            @Result(column="CLUESSRD",property="clueSsrd"),
            @Result(column="CLUECLJY",property="clueCljy"),
    })
    List<TblWgzzWghc> selectBywghcBy(PageInfo<TblWgzzWghc> pageInfo, String creator);

    @Select("select * from TBL_WGZZ_WGHC where ID = #{id}")
    @Results({
            @Result(column="ID",property="id"),
            @Result(column="CLUEZRDX",property="clueZrdx"),
            @Result(column="CLUESSRD",property="clueSsrd"),
            @Result(column="CLUECLJY",property="clueCljy"),
    })
    TblWgzzWghc selectBywghcXQBy(@Param("id") BigDecimal id);

    @SelectProvider(method="getByContWghcList",type=TblWgzzWghcMapperSqlConfig.class)
    Integer getByContWghcList(PageInfo<TblWgzzWghc> pageInfo,String creator);

    @Update("update TBL_WGZZ_WGHC set STATUS=#{status} where ID = #{id}")
    void updateStatus(@Param("id") BigDecimal id,
                      @Param("status") Integer status);

    @Update("update TBL_WGZZ_WGHC set CLUENABER=#{cluenaber},HSCONTENT=#{hscontent} where ID = #{id}")
    void updateWghc(@Param("id") BigDecimal id,
                      @Param("cluenaber") Integer cluenaber,
                      @Param("hscontent") String hscontent);
}
