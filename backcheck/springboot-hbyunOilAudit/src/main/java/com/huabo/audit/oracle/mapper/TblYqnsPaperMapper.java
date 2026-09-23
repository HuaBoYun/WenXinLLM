package com.huabo.audit.oracle.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.audit.oracle.entity.TblYqnsPaper;

import org.apache.ibatis.annotations.*;

import java.math.BigDecimal;
import java.util.List;

public interface TblYqnsPaperMapper extends BaseMapper<TblYqnsPaper> {
	
	
    @Delete("DELETE FROM TBL_YQNS_PAPER WHERE PERID = #{perid}")
    void deleteoneById(BigDecimal perid) throws Exception;

 

    @Select("select *  FROM TBL_YQNS_PAPER WHERE PERID = #{perid}") 
    TblYqnsPaper selectById(BigDecimal perid) throws Exception;


    @Insert("INSERT INTO TBL_YQNS_PAPER_ATT(PERID,ATTID) VALUES (#{perid},#{aid})")
    void insertAttInfoAtt( BigDecimal perid, String aid) throws Exception;

    @Insert("DELETE FROM TBL_YQNS_PAPER_ATT where  ATTID=#{aid}")
    void deleteAttInfoAtt( String aid) throws Exception;

    @Insert("DELETE FROM TBL_YQNS_PAPER_ATT where PERID=#{perid}")
    void deleteAttInfoAttByxm(BigDecimal perid) throws Exception;


 
    @Update("UPDATE TBL_YQNS_PAPER SET STATUS=1 WHERE PERID = #{perid}")
    void sblw(String perid) throws Exception;
    
    @Update("UPDATE TBL_YQNS_PAPER SET STATUS=0 WHERE PERID = #{perid}")
    void thlw(String perid) throws Exception;
    
    @Update("UPDATE TBL_YQNS_PAPER SET CODE=#{code} WHERE PERID = #{perid}")
    void pxxg(BigDecimal perid,Integer code) throws Exception; 
 
    
    @Select("SELECT * FROM TBL_YQNS_PAPER WHERE PERID IN (SELECT PERID FROM TBL_YQNS_PAPERTB_GL  WHERE TBID = #{tbid}) ORDER BY TOTAL ASC ")
    List<TblYqnsPaper> selectByListId(BigDecimal tbid) throws Exception;
    
    
    @Select("DELETE FROM TBL_YQNS_PAPERTB_GL  WHERE PERID = #{perid} ")
   void deletebyglid(BigDecimal tbid) throws Exception;
    
    @Update("UPDATE TBL_YQNS_PAPER SET TOTAL=#{total} WHERE PERID = #{perid}")
    void pxhj(BigDecimal perid,Integer total) throws Exception;
    
    @Select("SELECT * FROM TBL_YQNS_PAPER  order by TOTAL desc ")
    List<TblYqnsPaper> selectByListmc() throws Exception;
    
    
    @Update("UPDATE TBL_YQNS_PAPER SET RANKING=#{mc} WHERE PERID = #{perid}")
    void xgmc(BigDecimal perid,Integer mc) throws Exception;
 
    
    
    @Select("SELECT * FROM TBL_YQNS_PAPER where TOTAL is not null  order by TOTAL desc ")
    List<TblYqnsPaper> selectByListmcnot() throws Exception;
    
}
