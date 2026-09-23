package com.huabo.audit.oracle.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.audit.oracle.entity.TblYqnsFundtb;

import io.lettuce.core.dynamic.annotation.Param;

import org.apache.ibatis.annotations.*;

import java.math.BigDecimal;

public interface TblYqnsFundtbMapper extends BaseMapper<TblYqnsFundtb> {
	
	
    @Delete("DELETE FROM TBL_YQNS_FUNDTB WHERE TBID = #{tbid}")
    void deleteoneById(BigDecimal tbid) throws Exception;



    @Select("select *  FROM TBL_YQNS_FUNDTB WHERE TBID = #{tbid}")
    TblYqnsFundtb selectById(BigDecimal tbid) throws Exception;


    @Insert("INSERT INTO TBL_YQNS_FUNDTB_GL(TBID,FUNID) VALUES (#{tbid},#{id})")
    void insertglnr( BigDecimal tbid, String id) throws Exception;

    @Insert("DELETE FROM TBL_YQNS_FUNDTB_GL where  TBID=#{tbid}")
    void deleteglnr(BigDecimal tbid) throws Exception;
    
    @Insert("INSERT INTO TBL_YQNS_FUNDTB_ATT(TBID,ATTID) VALUES (#{tbid},#{attid})")
	void insertAttInfoForPlan(BigDecimal tbid, String attid) throws Exception;
    
    @Delete("DELETE FROM TBL_YQNS_FUNDTB_ATT WHERE ATTID=#{attid}")
	void deleteFileInfoByAttId(String attid);

    
    @Delete("DELETE FROM TBL_ATTACHMENT WHERE ATTID = #{attid}")
	void deleteEntity(String attid) throws Exception;

}
