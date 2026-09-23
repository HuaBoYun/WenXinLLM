package com.huabo.audit.oracle.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.audit.oracle.entity.TblYqnsPaperTb;

import org.apache.ibatis.annotations.*;

import java.math.BigDecimal;

public interface TblYqnsPaperTbMapper extends BaseMapper<TblYqnsPaperTb> {
	
	
    @Delete("DELETE FROM TBL_YQNS_PAPERTB WHERE TBID = #{tbid}")
    void deleteoneById(BigDecimal tbid) throws Exception;



    @Select("select *  FROM TBL_YQNS_PAPERTB WHERE TBID = #{tbid}")
    TblYqnsPaperTb selectById(BigDecimal tbid) throws Exception;


    @Insert("INSERT INTO TBL_YQNS_PAPERTB_GL(TBID,PERID) VALUES (#{tbid},#{id})")
    void insertglnr( BigDecimal tbid, String id) throws Exception;

    @Insert("DELETE FROM TBL_YQNS_PAPERTB_GL where  TBID=#{tbid}")
    void deleteglnr(BigDecimal tbid) throws Exception;

 




}
