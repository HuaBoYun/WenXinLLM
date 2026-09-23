package com.huabo.system.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.SelectProvider;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.huabo.system.entity.TblImplog;
import com.huabo.system.mappersql.TblImplogMapperSqlConfig;

public interface TblImplogMapper extends BaseMapper<TblImplog> {

    @SelectProvider(type=TblImplogMapperSqlConfig.class,method="findByTblImplogList")
    @Results({
            @Result(column="IMPID",property="impid"),
            @Result(column="IMPDESC",property="impdesc"),
            @Result(column="IMPURL",property="impurl"),
            @Result(column="IMPFILENAME",property="impfilename"),
            @Result(column="IMPCREATETIME",property="impcreatetime"),
            @Result(column="IMPCREATEUSERNAME",property="impcreateusername"),
            @Result(column="IMPTYPE",property="imptype"),
            @Result(column="IMPSTATE",property="impstate"),
    })
    IPage<TblImplog> findByTblImplogList(IPage<TblImplog> page, @Param("username") String username, @Param("type")String type);
}
