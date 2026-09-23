package com.huabo.audit.oracle.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.audit.oracle.entity.TblYqnsLxjytb;

import org.apache.ibatis.annotations.*;

import java.math.BigDecimal;

public interface TblYqnsLxjytbMapper extends BaseMapper<TblYqnsLxjytb> {
	
	
    @Delete("DELETE FROM TBL_YQNS_NDTB WHERE TBID = #{tbid}")
    void deleteoneById(BigDecimal tbid) throws Exception;



    @Select("select *  FROM TBL_YQNS_NDTB WHERE TBID = #{tbid}")
    TblYqnsLxjytb selectById(BigDecimal tbid) throws Exception;


    @Insert("INSERT INTO TBL_YQNS_NDTB_GL(TBID,EVAID) VALUES (#{tbid},#{id})")
    void insertglnr( BigDecimal tbid, String id) throws Exception;

    @Insert("DELETE FROM TBL_YQNS_NDTB_GL where  TBID=#{tbid}")
    void deleteglnr(BigDecimal tbid) throws Exception;

 




}
