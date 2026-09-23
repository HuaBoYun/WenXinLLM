package com.huabo.audit.oracle.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.audit.oracle.entity.TblYqnsRule;
import com.huabo.audit.oracle.entity.TblYqnsRuletb;

import org.apache.ibatis.annotations.*;

import java.math.BigDecimal;
import java.util.List;

public interface TblYqnsRuletbMapper extends BaseMapper<TblYqnsRuletb> {
	
	
    @Delete("DELETE FROM TBL_YQNS_RULETB WHERE TBID = #{tbid}")
    void deleteoneById(BigDecimal tbid) throws Exception;



    @Select("select *  FROM TBL_YQNS_RULETB WHERE TBID = #{tbid}")
    TblYqnsRuletb selectById(BigDecimal tbid) throws Exception;


    @Insert("INSERT INTO TBL_YQNS_RULE(TBID,RULEID,HJBL,HJTYPE) VALUES (#{tbid},#{id},#{hjbj},#{hjtype})")
    void insertglnr( BigDecimal tbid, BigDecimal id,Integer hjbj,String hjtype) throws Exception;

    @Insert("DELETE FROM TBL_YQNS_RULE where  TBID=#{tbid}")
    void deleteglnr(BigDecimal tbid) throws Exception;

 
    @Select("select *  FROM TBL_YQNS_RULE WHERE TBID = #{tbid} ORDER BY HJBL  ASC ")
    List<TblYqnsRule> selectBytbId(BigDecimal tbid) throws Exception;

    @Select("select *  FROM TBL_YQNS_RULETB WHERE RULEYEAR = #{year}")
    List<TblYqnsRuletb> yzyear(String  year) throws Exception;
    
    @Select("select HJBL/100  FROM TBL_YQNS_RULE WHERE TBID = #{tbid} and HJTYPE=#{dl} ORDER BY HJBL  ASC ")
    double selectBydj(BigDecimal tbid,String dl) throws Exception;

}
