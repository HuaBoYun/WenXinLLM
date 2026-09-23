package com.huabo.audit.oracle.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.audit.oracle.entity.TblYqnsResearchtb;

import org.apache.ibatis.annotations.*;

import java.math.BigDecimal;
import java.util.List;

public interface TblYqnsResearchtbMapper extends BaseMapper<TblYqnsResearchtb> {
	
	
    @Delete("DELETE FROM TBL_YQNS_RESEARCHTB WHERE TBID = #{tbid}")
    void deleteoneById(BigDecimal tbid) throws Exception;



    @Select("select *  FROM TBL_YQNS_RESEARCHTB WHERE TBID = #{tbid}")
    TblYqnsResearchtb selectById(BigDecimal tbid) throws Exception;


    @Insert("INSERT INTO TBL_YQNS_RESEARCHTB_GL(TBID,CHID) VALUES (#{tbid},#{id})")
    void insertglnr( BigDecimal tbid, String id) throws Exception;

    @Insert("DELETE FROM TBL_YQNS_RESEARCHTB_GL where  TBID=#{tbid}")
    void deleteglnr(BigDecimal tbid) throws Exception;
    
    
    @Select("select *  FROM TBL_YQNS_RESEARCHTB WHERE TBID in ( SELECT tbid FROM TBL_YQNS_RESEARCHTB_GL where CHID= #{chid} )")
    List<TblYqnsResearchtb> selectBychId(BigDecimal chid) throws Exception;

}
