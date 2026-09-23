package com.huabo.audit.oracle.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.audit.oracle.entity.TblYqnsResearch;

import org.apache.ibatis.annotations.*;

import java.math.BigDecimal;
import java.util.List;

public interface TblYqnsResearchMapper extends BaseMapper<TblYqnsResearch> {
	
	


    @Select("SELECT * FROM TBL_YQNS_RESEARCH WHERE CHID IN (SELECT CHID FROM TBL_YQNS_RESEARCHTB_GL  WHERE TBID = #{tbid})")
  	List<TblYqnsResearch> selectListBytbid(BigDecimal tbid);


    @Delete("DELETE FROM TBL_YQNS_RESEARCH WHERE CHID = #{chid}")
    void deleteoneByzbId(BigDecimal chid) throws Exception;
    
    @Delete("DELETE FROM TBL_YQNS_RESEARCH WHERE CHID IN (SELECT CHID FROM TBL_YQNS_RESEARCHTB_GL  WHERE TBID = #{tbid})")
    void deleteallBytbId(BigDecimal tbid) throws Exception;
    
    @Select("select *  FROM TBL_YQNS_RESEARCH WHERE CHID = #{chid}")
    TblYqnsResearch selectByzbId(BigDecimal chid) throws Exception;

}
