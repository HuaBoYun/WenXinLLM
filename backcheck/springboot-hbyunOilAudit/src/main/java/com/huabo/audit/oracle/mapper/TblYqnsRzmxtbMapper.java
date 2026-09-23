package com.huabo.audit.oracle.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.audit.oracle.entity.TblYqnsRzmxtb;

import org.apache.ibatis.annotations.*;

import java.math.BigDecimal;

public interface TblYqnsRzmxtbMapper extends BaseMapper<TblYqnsRzmxtb> {
	
	
    @Delete("DELETE FROM TBL_YQNS_RZTB WHERE TBID = #{tbid}")
    void deleteoneById(BigDecimal tbid) throws Exception;



    @Select("select *  FROM TBL_YQNS_RZTB WHERE TBID = #{tbid}")
    TblYqnsRzmxtb selectById(BigDecimal tbid) throws Exception;


    @Insert("INSERT INTO TBL_YQNS_RZTB_GL(TBID,DEID) VALUES (#{tbid},#{id})")
    void insertglnr( BigDecimal tbid, String id) throws Exception;

    @Insert("DELETE FROM TBL_YQNS_RZTB_GL where  TBID=#{tbid}")
    void deleteglnr(BigDecimal tbid) throws Exception;

 




}
