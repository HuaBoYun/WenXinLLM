package com.huabo.audit.oracle.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.audit.oracle.entity.TblYqnsPaperPx;

import org.apache.ibatis.annotations.*;

import java.math.BigDecimal;
import java.util.List;

public interface TblYqnsPaperPxMapper extends BaseMapper<TblYqnsPaperPx> {
	
	
    @Delete("DELETE FROM TBL_YQNS_PAPERPX WHERE PXID = #{pxid}")
    void deleteoneById(BigDecimal pxid) throws Exception;

 

    @Select("select *  FROM TBL_YQNS_PAPER WHERE PXID = #{pxid}") 
    TblYqnsPaperPx selectById(BigDecimal pxid) throws Exception;


    
    @Select("SELECT * FROM TBL_YQNS_PAPERPX WHERE PERID  = #{perid}")
    List<TblYqnsPaperPx> selectByListId(BigDecimal perid) throws Exception;
    
    @Select("SELECT sum(FS) FROM TBL_YQNS_PAPERPX WHERE PERID = #{perid}")
	Integer hzfs(BigDecimal perid);

}
